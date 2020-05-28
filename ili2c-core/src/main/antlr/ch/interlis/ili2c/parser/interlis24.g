header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.metamodel.*;
	import ch.interlis.ili2c.generator.Interlis2Generator;
	import ch.interlis.ili2c.CompilerLogEvent;
	import java.util.*;
	import ch.ehi.basics.logging.EhiLogger;
	import ch.ehi.basics.settings.Settings;
}

class Ili24Parser extends Parser;

options
{
  codeGenMakeSwitchThreshold = 3;
  codeGenBitsetTestThreshold = 4;
  buildAST=false;
}
{
  protected PredefinedModel modelInterlis;
  protected Type predefinedBooleanType;
  protected Table predefinedScalSystemClass;
  protected Table predefinedCoordSystemClass;
  protected TransferDescription td;
  private Ili24Lexer lexer;
  private antlr.TokenStreamHiddenTokenFilter filter;
  private boolean checkMetaObjs;
  private Ili2cMetaAttrs externalMetaAttrs=new Ili2cMetaAttrs();

  /** Parse the contents of a stream according to INTERLIS-1 or INTERLIS-2 syntax
      (the version is detected automatically by the parser) and add the
      encountered contents to this TransferDescription.

      @return false if there have been any fatal errors which would lead
              this TransferDescription in an inconsistent state.
  */
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,java.io.Reader stream
    ,boolean checkMetaObjects
    ,int line0Offest
    ,Ili2cMetaAttrs metaAttrs
    )
  {
  	return parseIliFile (td,filename,new Ili24Lexer (stream),checkMetaObjects,line0Offest,metaAttrs);
  }
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,java.io.InputStream stream
    ,boolean checkMetaObjects
    ,int line0Offest
    ,Ili2cMetaAttrs metaAttrs
    )
  {
  	return parseIliFile (td,filename,new Ili24Lexer (stream),checkMetaObjects,line0Offest,metaAttrs);
  }
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,Ili24Lexer lexer
    ,boolean checkMetaObjects
    ,int line0Offest
    ,Ili2cMetaAttrs metaAttrs
    )
  {

    try {
	if ((filename != null) && "".equals (td.getName())){
		td.setName(filename);
	}
      
      //
      // setup token stream splitting to filter out comments
      //
      //filter.getHiddenAfter(end)
      //filter.getHiddenBefore(begin)

      // create token objects augmented with links to hidden tokens. 
      lexer.setTokenObjectClass("antlr.CommonHiddenStreamToken");

      // create filter that pulls tokens from the lexer
      antlr.TokenStreamHiddenTokenFilter filter = new antlr.TokenStreamHiddenTokenFilter(lexer);

      // tell the filter which tokens to hide, and which to discard
      filter.hide(ILI_DOC);
      filter.hide(ILI_METAVALUE);

      // connect parser to filter (instead of lexer)
      Ili24Parser parser = new Ili24Parser (filter);
      
      // Ili2.3 always check existence of metaobject
      parser.checkMetaObjs=true; // checkMetaObjects;
      parser.lexer=lexer;
      parser.filter=filter;
      parser.setFilename (filename);
      return parser.interlisDescription (td);
    }catch(antlr.RecognitionException ex){
      //listener.error (new ErrorListener.ErrorEvent (ex, filename, ex.recog.getLine(),
      //  ErrorListener.ErrorEvent.SEVERITY_ERROR));
      int line=ex.getLine();
      CompilerLogEvent.logError(filename,line,ex.getLocalizedMessage());
      return false;
    }catch(Ili2cSemanticException ex){
	      int line=((Ili2cSemanticException)ex).getSourceLine();
	      CompilerLogEvent.logError(filename,line,ex.getLocalizedMessage());
	      return false;
    }catch(antlr.TokenStreamRecognitionException ex){
    	if(ex.recog instanceof antlr.NoViableAltForCharException){
		antlr.NoViableAltForCharException ex2=(antlr.NoViableAltForCharException)ex.recog;
		String msg="unexpected char: "+ex2.foundChar+" (0x"+Integer.toHexString(ex2.foundChar).toUpperCase()+")";
		CompilerLogEvent.logError(filename,ex2.getLine(),msg);
	}else{
		CompilerLogEvent.logError(filename,ex.recog.getLine(),ex.getLocalizedMessage());
	}
      return false;
    } catch (antlr.ANTLRError ex) {
      EhiLogger.traceState(filename+": "+ex);
      return false;
    } catch (Exception ex) {
      CompilerLogEvent.logError(filename,0,ex);
      return false;
    }
  }

	/** compiler error messages
	*/
	ResourceBundle rsrc = ResourceBundle.getBundle(
		ErrorMessages.class.getName(),
		Locale.getDefault());

	/** check if there is a attribute with the given name.
	* @returns null if no attribute found or scope not a Viewable
	*/
	protected AttributeDef findAttribute(Container scope,String name)
	{
		if(!(scope instanceof Viewable))return null;
		Viewable currentViewable=(Viewable)scope;
		AttributeDef attrdef=null;
		Iterator it=currentViewable.getAttributes();
		while(it.hasNext()){
			AttributeDef ele=(AttributeDef)it.next();
			if(ele.getName().equals(name)){
				attrdef=ele;
				break;
			}
		}
		return attrdef; // may be null if no attribute found
	}
	/** check if the given name is part of a AttributeRef, that is 
	*   not a reference attribute and not a proxy for a 
	*   basename inside a viewable.
	*   semantic predicate
	*/
	protected boolean isAttributeRef(Viewable v,String name)
	{
			AttributeDef attr=findAttribute(v,name);
			if(attr==null){
			    // no attribute name in v
			    return false;
			}
			// check if attribute is a reference attribute
			Type type=attr.getDomainResolvingAliases();
			if(type instanceof ReferenceType){
				// attr is a reference attribute
				return false;
			}
			if(type instanceof ObjectType){
				// attr is a proxy for a basename
				return false;
			}
		return true;
	}
	protected void validateFormattedConst(FormattedType ft,String value, int srcLine)
	{
			try{
				if(!ft.isValueInRange(value)){
					reportError(formatMessage("err_formattedType_valueOutOfRange",value),srcLine);
				}
			}catch(NumberFormatException ex){
				reportError(formatMessage("err_formattedType_illegalFormat",value),srcLine);
			}
	}
    protected void validateEqualsArgumentTypes(Evaluable exprRet, Evaluable expr, Evaluable comparedWith,int srcLine)
    {
            if(!expr.isDirty() && !comparedWith.isDirty()){
            	if(expr instanceof Constant.Undefined || comparedWith instanceof Constant.Undefined){
            		// UNDEFINED is always == or !=
            	}else{
					Type expr1Type=expr.getType();
					Type expr2Type=comparedWith.getType();
					if(expr1Type!=null && expr2Type!=null){
						if(expr1Type.resolveAliases() instanceof TextType && expr2Type.resolveAliases() instanceof TextType){
							// text
						}else if(expr1Type.resolveAliases() instanceof CompositionType && expr2Type.resolveAliases() instanceof CompositionType){
							// structs
						}else if(expr1Type.resolveAliases() instanceof OIDType && expr2Type.resolveAliases() instanceof OIDType){
						}else if(expr1Type.resolveAliases() instanceof ClassType && expr2Type.resolveAliases() instanceof ClassType){
						}else if(expr1Type.resolveAliases() instanceof AttributePathType && expr2Type.resolveAliases() instanceof AttributePathType){
						}else if(expr1Type.resolveAliases() instanceof NumericType && expr2Type.resolveAliases() instanceof NumericType){
							// numeric
						}else if(expr1Type.resolveAliases() instanceof FormattedType && expr2Type.resolveAliases() instanceof FormattedType){
							// formatted
						}else if(expr1Type.resolveAliases() instanceof FormattedType && comparedWith instanceof Constant.Text){
							// formatted
							validateFormattedConst((FormattedType)expr1Type.resolveAliases(),((Constant.Text)comparedWith).getValue(),srcLine);
						}else if(expr instanceof Constant.Text && expr2Type.resolveAliases() instanceof FormattedType){
							// formatted
							validateFormattedConst((FormattedType)expr2Type.resolveAliases(),((Constant.Text)expr).getValue(),srcLine);
						}else if(expr1Type.resolveAliases() instanceof CoordType && expr2Type.resolveAliases() instanceof CoordType){
							// coord
						}else if(expr1Type.resolveAliases() instanceof EnumerationType && expr2Type.resolveAliases() instanceof EnumerationType){
							// enum
							if(expr instanceof Constant.Enumeration && comparedWith instanceof Constant.Enumeration){
								// both factors are constants of unknown enumerations
							}else if(expr instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								String value=((Constant.Enumeration)expr).toString();
								List<String> values=((EnumerationType)expr2Type.resolveAliases()).getValues();
								if(!values.contains(value.substring(1))){
									reportError (formatMessage ("err_enumerationType_MissingEle",value),
										srcLine);
								}
								((Constant.Enumeration)expr).setType((EnumerationType)expr2Type.resolveAliases());
							}else if(comparedWith instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								String value=((Constant.Enumeration)comparedWith).toString();
								List<String> values=((EnumerationType)expr1Type.resolveAliases()).getValues();
								if(!values.contains(value.substring(1))){
									reportError (formatMessage ("err_enumerationType_MissingEle",value),
										srcLine);
								}
								((Constant.Enumeration)comparedWith).setType((EnumerationType)expr1Type.resolveAliases());
							}else{
							 if(expr1Type.resolveAliases()!=expr2Type.resolveAliases()){
								reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
										 srcLine);
								exprRet.setDirty(true);
							 }
							}
						}else if(expr1Type.resolveAliases() instanceof EnumTreeValueType && expr2Type.resolveAliases() instanceof EnumTreeValueType){
							 if(expr1Type.resolveAliases()!=expr2Type.resolveAliases()){
								reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
										 srcLine);
								exprRet.setDirty(true);
							 }
						}else if(expr instanceof Constant.Enumeration && expr2Type.resolveAliases() instanceof EnumTreeValueType){
								// validate that constant is a member of the enumeration type
								String value=((Constant.Enumeration)expr).toString();
								List<String> values=((EnumTreeValueType)expr2Type.resolveAliases()).getValues();
								if(!values.contains(value.substring(1))){
									reportError (formatMessage ("err_enumerationType_MissingEle",value),
										srcLine);
								}
								((Constant.Enumeration)expr).setType((EnumTreeValueType)expr2Type.resolveAliases());
						}else if(expr1Type.resolveAliases() instanceof EnumTreeValueType && comparedWith instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								String value=((Constant.Enumeration)comparedWith).toString();
								List<String> values=((EnumTreeValueType)expr1Type.resolveAliases()).getValues();
								if(!values.contains(value.substring(1))){
									reportError (formatMessage ("err_enumerationType_MissingEle",value),
										srcLine);
								}
								((Constant.Enumeration)comparedWith).setType((EnumTreeValueType)expr1Type.resolveAliases());
						}else if(expr1Type.resolveAliases() instanceof ObjectType && expr2Type.resolveAliases() instanceof ObjectType){
							// object
						}else{
							reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
									 srcLine);
							exprRet.setDirty(true);
						}
					}else{
							reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
									 srcLine);
							exprRet.setDirty(true);
					}
            	}
            }
    }
    protected void validateCompareArgumentTypes(Evaluable exprRet, Evaluable expr, Evaluable comparedWith,int srcLine)
    {
            if(!expr.isDirty() && !comparedWith.isDirty()){
            	{
					Type expr1Type=expr.getType();
					Type expr2Type=comparedWith.getType();
					if(expr1Type!=null && expr2Type!=null){
						if(expr1Type.resolveAliases() instanceof NumericType && expr2Type.resolveAliases() instanceof NumericType){
							// numeric
						}else if(expr1Type.resolveAliases() instanceof FormattedType && expr2Type.resolveAliases() instanceof FormattedType){
							// formatted
						}else if(expr1Type.resolveAliases() instanceof FormattedType && comparedWith instanceof Constant.Text){
							// formatted
							validateFormattedConst((FormattedType)expr1Type.resolveAliases(),((Constant.Text)comparedWith).getValue(),srcLine);
						}else if(expr instanceof Constant.Text && expr2Type.resolveAliases() instanceof FormattedType){
							// formatted
							validateFormattedConst((FormattedType)expr2Type.resolveAliases(),((Constant.Text)expr).getValue(),srcLine);
						}else if(expr1Type.resolveAliases() instanceof EnumerationType && expr2Type.resolveAliases() instanceof EnumerationType){
							// enum
							if(expr instanceof Constant.Enumeration && comparedWith instanceof Constant.Enumeration){
								// both factors are constants of unknown enumerations
							}else if(expr instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								EnumerationType enumType=(EnumerationType)expr2Type.resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									String value=((Constant.Enumeration)expr).toString();
									List<String> values=enumType.getValues();
									if(!values.contains(value.substring(1))){
										reportError (formatMessage ("err_enumerationType_MissingEle",value),
											srcLine);
									}
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
							}else if(comparedWith instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								EnumerationType enumType=(EnumerationType)expr1Type.resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									String value=((Constant.Enumeration)comparedWith).toString();
									List<String> values=enumType.getValues();
									if(!values.contains(value.substring(1))){
										reportError (formatMessage ("err_enumerationType_MissingEle",value),
											srcLine);
									}
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
							}else{
							 if(expr1Type.resolveAliases()!=expr2Type.resolveAliases()){
								reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
										 srcLine);
								exprRet.setDirty(true);
							 }else{
								EnumerationType enumType=(EnumerationType)expr1Type.resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									// ok
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
							 }
							}
						}else if(expr1Type.resolveAliases() instanceof EnumTreeValueType && expr2Type.resolveAliases() instanceof EnumTreeValueType){
							 if(expr1Type.resolveAliases()!=expr2Type.resolveAliases()){
								reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
										 srcLine);
								exprRet.setDirty(true);
							 }else{
								EnumerationType enumType=(EnumerationType)((EnumTreeValueType)expr1Type.resolveAliases()).getEnumType().getType().resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									// ok
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
							 }
						}else if(expr instanceof Constant.Enumeration && expr2Type.resolveAliases() instanceof EnumTreeValueType){
								// validate that constant is a member of the enumeration type
								EnumerationType enumType=(EnumerationType)((EnumTreeValueType)expr2Type.resolveAliases()).getEnumType().getType().resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									String value=((Constant.Enumeration)expr).toString();
									List<String> values=((EnumTreeValueType)expr2Type.resolveAliases()).getValues();
									if(!values.contains(value.substring(1))){
										reportError (formatMessage ("err_enumerationType_MissingEle",value),
											srcLine);
									}
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
						}else if(expr1Type.resolveAliases() instanceof EnumTreeValueType && comparedWith instanceof Constant.Enumeration){
								// validate that constant is a member of the enumeration type
								EnumerationType enumType=(EnumerationType)((EnumTreeValueType)expr1Type.resolveAliases()).getEnumType().getType().resolveAliases();
								if(enumType.isOrdered() && !enumType.isCircular()){
									String value=((Constant.Enumeration)comparedWith).toString();
									List<String> values=((EnumTreeValueType)expr1Type.resolveAliases()).getValues();
									if(!values.contains(value.substring(1))){
										reportError (formatMessage ("err_enumerationType_MissingEle",value),
											srcLine);
									}
								}else{
									reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
											 srcLine);
									exprRet.setDirty(true);
								}
						}else{
							reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
									 srcLine);
							exprRet.setDirty(true);
						}
					}else{
							reportError (formatMessage ("err_expr_incompatibleTypes",(String)null),
									 srcLine);
							exprRet.setDirty(true);
					}
            	}
            }
    }

	protected Domain resolveDomainRef(Container scope,String[] nams, int lin)
	{
	      Model model;
	      AbstractPatternDef topic;
	      String domainName=null;
	      Domain d=null;

	switch (nams.length) {
	      case 1:
		model = (Model) scope.getContainerOrSame(Model.class);
		topic = (AbstractPatternDef) scope.getContainerOrSame(AbstractPatternDef.class);
		domainName = (String) nams[0];
		break;

	      case 2:
		model = resolveOrFixModelName (scope, nams[0], lin);
		topic = null;
		domainName = nams[1];
		break;

	      case 3:
		model = resolveOrFixModelName (scope, nams[0], lin);
		topic = resolveOrFixTopicName (model, nams[1], lin);
		domainName = nams[2];
		break;

	      default:
		reportError(rsrc.getString("err_domainRef_weird"), lin);
		model = resolveModelName (scope, nams[0]);
		topic = null;
		if (model == null)
		  model = (Model) scope.getContainerOrSame(Model.class);
		domainName = (String) nams[nams.length - 1];
		break;
	      }

	      d = null;
	      if (topic != null)
		d = (Domain) topic.getRealElement (Domain.class, domainName);

	      if ((d == null) && ((topic == null) | (nams.length == 1)))
		d = (Domain) model.getRealElement(Domain.class, domainName);

	      if ((d == null) && (nams.length == 1)){
	      	// unqualified name; search also in unqaulified imported models
		d = (Domain) model.getImportedElement(Domain.class, domainName);
	      }
	      if (d == null)
	      {
		if (topic == null)
		  reportError(
		    formatMessage ("err_domainRef_notInModel", domainName, model.toString()),
		    lin);
		else
		  reportError(
		    formatMessage ("err_domainRef_notInModelOrTopic", domainName,
				   topic.toString(), model.toString()),
		    lin);

		try {
		  d = new Domain();
		  d.setName(domainName);
		  if (topic == null)
		    model.add(d);
		  else
		    topic.add(d);
		} catch (Exception ex) {
		  panic();
		}
	      }
	      return d;
	}
	protected Element resolveStructureOrDomainRef(Container scope,String[] nams,int lin)
	{
      Model model;
      AbstractPatternDef    topic;
      String   modelName, topicName,tableName;
      Table t;

      switch (nams.length) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (AbstractPatternDef) scope.getContainerOrSame(AbstractPatternDef.class);
        tableName = nams[0];
        break;

      case 2:
        modelName = nams[0];
        model = resolveOrFixModelName(scope, modelName, lin);
        tableName = nams[1];
        topic = null;
        break;

      case 3:
        modelName = nams[0];
        topicName = nams[1];
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = resolveOrFixTopicName(model, topicName, lin);
        tableName = nams[2];
        break;

      default:
        reportError(rsrc.getString("err_weirdTableRef"), lin);
        model = resolveModelName(scope, nams[0]);
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);

        topic = null;
        tableName = nams[nams.length - 1];
        break;
      }

      t = null;
      if (topic != null)
        t = (Table) topic.getElement (Table.class, tableName);
      if ((t == null) && (model != null))
        t = (Table) model.getElement (Table.class, tableName);
      if ((t == null) && (nams.length == 1))
        t = (Table) model.getImportedElement (Table.class, tableName);
        if(t!=null){
		if(t.isIdentifiable()){
			reportError(formatMessage("err_structRef_StructRequired",t.getScopedName(null)),lin);
		}
          return t;
        }
                 return resolveDomainRef(scope,nams,lin);
	}

	protected GraphicParameterDef resolveRuntimeParameterRef(Container scope,String[] nams, int lin)
	{
	      Model model;
	      String domainName=null;
	      GraphicParameterDef d=null;

	switch (nams.length) {
	      case 1:
		model = (Model) scope.getContainerOrSame(Model.class);
		domainName = (String) nams[0];
		break;

	      case 2:
		model = resolveOrFixModelName (scope, nams[0], lin);
		domainName = nams[1];
		break;

	      default:
		reportError(rsrc.getString("err_runtimeParamRef_weird"), lin);
		model = resolveModelName (scope, nams[0]);
		if (model == null)
		  model = (Model) scope.getContainerOrSame(Model.class);
		domainName = (String) nams[nams.length - 1];
		break;
	      }

	      d = null;
	      d = (GraphicParameterDef) model.getRealElement(GraphicParameterDef.class, domainName);
	      if ((d == null) && (nams.length == 1)){
	      	// unqualified name; search also in unqaulified imported models
		d = (GraphicParameterDef) model.getImportedElement(GraphicParameterDef.class, domainName);
	      }
	      if (d == null)
	      {
		  reportError(
		    formatMessage ("err_runtimeParamRef_notInModel", domainName, model.toString()),
		    lin);
	      }
	      return d;
	}
  public void reportError (String message)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,0,message);
  }


  public void reportWarning (String message)
  {
      String filename=getFilename();
      CompilerLogEvent.logWarning(filename,0,message);
  }


  protected void reportError (String message, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,message);
  }


  protected void reportWarning (String message, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logWarning(filename,lineNumber,message);
  }


  protected void reportError (Throwable ex, int lineNumber)
  {
      String filename=getFilename();
      if(ex instanceof antlr.RecognitionException){
	      CompilerLogEvent.logError(filename,lineNumber,ex.getLocalizedMessage());
      }else if(ex instanceof Ili2cSemanticException){
	      CompilerLogEvent.logError(filename,lineNumber,ex.getLocalizedMessage());
      }else{
	      CompilerLogEvent.logError(filename,lineNumber,ex);
      }
  }
  protected void reportError (Ili2cSemanticException ex)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,ex.getSourceLine(),ex.getLocalizedMessage());
  }
  protected void reportError (List<Ili2cSemanticException> errs)
  {
      String filename=getFilename();
  	for(Ili2cSemanticException ex:errs){
      CompilerLogEvent.logError(filename,ex.getSourceLine(),ex.getLocalizedMessage());
  	}
  }
  public void reportError (antlr.RecognitionException ex)
  {
      String filename=getFilename();
      int lineNumber=((antlr.RecognitionException)ex).getLine();
      CompilerLogEvent.logError(filename,lineNumber,ex.getLocalizedMessage());
  }

  protected void reportInternalError(int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,formatMessage("err_internalCompilerError", /* exception */ ""));
  }


  protected void reportInternalError (Throwable ex, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,formatMessage("err_internalCompilerError", ""),ex);
  }


  protected String formatMessage (String msg, Object[] args)
  {
    try {
      java.text.MessageFormat mess = new java.text.MessageFormat(
        rsrc.getString(msg));
      return mess.format(args);
    } catch (Exception ex) {
      EhiLogger.logError("Internal compiler error",ex);
      return "Internal compiler error [" + ex.getLocalizedMessage() + "]";
    }
  }


  protected String formatMessage(String msg, String arg) {
    return formatMessage(msg, new Object[] { arg });
  }


  protected String formatMessage(String msg, String arg1, String arg2) {
    return formatMessage(msg, new Object[] { arg1, arg2 });
  }

  protected String formatMessage(String msg, String arg1, String arg2, String arg3) {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3 });
  }

  public static void panic ()
  {
    throw new antlr.ANTLRError();
  }


  protected Model resolveModelName (Container scope, String modelName)
  {
    Model scopeModel;

    if (scope instanceof TransferDescription)
      scopeModel = null;
    else
      scopeModel = (Model) scope.getContainerOrSame (Model.class);

    if ((scopeModel != null)
        && modelName.equals(((Model) scopeModel).getName()))
      return scopeModel;

    return (Model) td.getRealElement (Model.class, modelName);
  }


  protected Model resolveOrFixModelName(
      Container    scope,
      String                      modelName,
      int                         line)
  {
    Model m;
    Model scopeModel = (Model) scope.getContainerOrSame (Model.class);

    m = resolveModelName (scope, modelName);
    if (m != null)
    {
      if ((scopeModel != null)
          && (scopeModel != m)
          && !scopeModel.isImporting (m) && m!=modelInterlis)
      {
        reportError (formatMessage ("err_model_notImported",
          scopeModel.toString(), m.toString()),
          line);

        try {
          scopeModel.addImport (m,false);
        } catch (Exception ex) {
          panic ();
        }
      }
      return m;
    }

    if (scopeModel != null) {
      Topic referredTopic = (Topic) scopeModel.getRealElement (Topic.class, modelName);
      if (referredTopic != null)
      {
        reportError (formatMessage (
          "err_topicRef_withoutModelScope", referredTopic.toString()), line);
        panic ();
      }
    }

    /* find the model and transfer description that contain scope */
    try {
      reportError(formatMessage("err_noSuchModel", modelName), line);
      Model artificialModel = new DataModel ();
      artificialModel.setName (modelName);
      td.add (artificialModel);
    } catch (Exception ex) {
      /* Can't fix, for whatever reason this might occur */
      panic();
    }

    /* Check out whether the fix was successful. Should
       always be the case when reaching this line. */
    m = resolveModelName(scope, modelName);
    if (m == null)
      panic();

    return m;
  }


  protected Topic resolveOrFixTopicName(
      Model      model,
      String     topicName,
      int        line)
  {
    Topic  topic;

    topic = (Topic) model.getRealElement(Topic.class, topicName);
    if (topic == null) {
      reportError(
        formatMessage("err_noSuchTopic", topicName, model.toString()),
        line);
      try {
        topic = new Topic();
        topic.setName(topicName);
        model.add(topic);
      } catch (Exception ex) {
        panic();
      }
    }
    return topic;
  }


  protected MetaObject resolveMetaObject (MetaDataUseDef basket, Table polymorphicTo, String name, int line)
  {

  List matching = basket.findMatchingMetaObjects (polymorphicTo, name);
    if (matching.size() >= 2)
    {
      reportError (formatMessage ("err_metaObject_refAmbiguous",
                                  name,
                                  ((MetaObject) matching.get(0)).getScopedName (null),
                                  ((MetaObject) matching.get(1)).getScopedName (null)),
                   line);
      return (MetaObject) matching.get(0);
    }
    else if (matching.size() == 1)
      return (MetaObject) matching.get(0);
    else
    {
      /* Nothing found. */
      reportError (formatMessage ("err_metaObject_unknownName",
                                  name,
                                  basket.getScopedName(null),polymorphicTo.getScopedName(null)),
                   line);
      return null;
    }
  }

	protected MetaDataUseDef resolveOrFixBasketName(Container container,String basketName,int line)
 	{
	        Model model = (Model) container.getContainerOrSame(Model.class);
	        AbstractPatternDef topic = (AbstractPatternDef) container.getContainerOrSame(AbstractPatternDef.class);
		MetaDataUseDef basket=null;
		if(topic!=null){
			basket=  (MetaDataUseDef) topic.getRealElement (MetaDataUseDef.class, basketName);
		}
		if(basket==null){
			basket=  (MetaDataUseDef) model.getRealElement (MetaDataUseDef.class, basketName);
		}
		if(basket!=null){
			return basket;
		}
		reportError (formatMessage ("err_noSuchMetaDataUseDef",
			basketName, container.toString()),
                   line);
		try {
		  MetaDataUseDef ref = new MetaDataUseDef();
		  ref.setName(basketName);
		  if (topic != null)
		    topic.add(ref);
		  else
		    model.add(ref);
		  return ref;
		} catch (Exception ex) {
		  panic();
		}

		return null;
	}

  protected AttributeDef findOverridingAttribute (
    Viewable container, int mods, String name, int line)
  {
    boolean      declaredExtended = (mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0;
    AttributeDef overriding;

    overriding =  (AttributeDef) container.getRealElement (AttributeDef.class, name);
    if ((overriding == null) && declaredExtended)
    {
      reportError (formatMessage ("err_noAttrToExtend",
                                  name, container.toString()),
                   line);
    }

    if ((overriding != null)
        && (overriding.getContainer(Viewable.class) == container))
    {
      reportError (formatMessage ("err_attrNameInSameViewable",
                                  container.toString(), name),
                   line);
    }
    else if ((overriding != null) && !declaredExtended)
    {
      reportError (formatMessage ("err_attrExtendedWithoutDecl",
                                  name, container.toString(),
                                  overriding.toString()),
                   line);
    }
    return overriding;
  }
	private Table buildDecomposedStruct(AttributeDef attrdef,boolean areaDecomp)
		throws java.beans.PropertyVetoException
	{
		Type type=attrdef.getDomainResolvingAliases(); 
		if(type instanceof CompositionType){
			// composition
			return ((CompositionType)type).getComponentType();
		}else if(type instanceof PolylineType){
			// polyline
			return ((PolylineType)type).getImplicitLineGeometryStructure();
		}else if(type instanceof SurfaceType){
			// surface
			return ((SurfaceType)type).getImplicitSurfaceBoundaryStructure();
		}else if(type instanceof AreaType){
			// area
			if(areaDecomp){
				return ((AreaType)type).getImplicitSurfaceEdgeStructure();
			}else{
				return ((AreaType)type).getImplicitSurfaceBoundaryStructure();
			}
		}else{
			throw new IllegalArgumentException(formatMessage(
				"err_decompositionView_notDecomposable",attrdef.getName()
			));
		}
	}
	private AttributeDef getBaseViewableProxyAttr(Viewable start1,String base,int line)
	{
		AttributeDef baseProxy =  (AttributeDef)start1.getRealElement (AttributeDef.class, base);
		if(baseProxy==null){
			return null;
		}
		Type proxyType=baseProxy.getDomain();
		if(!(proxyType instanceof ObjectType)){
			return null;
		}
		return baseProxy;
	}
	private Viewable getBaseViewable(AttributeDef baseProxy)
	{
		if(baseProxy==null){
			return null;
		}
		Type proxyType=baseProxy.getDomain();
		if(!(proxyType instanceof ObjectType)){
			return null;
		}
		return ((ObjectType)proxyType).getRef();
	}
	
	private String getIliDoc()
	throws antlr.TokenStreamException
	{ 
		String ilidoc=null;
		ArrayList docs=new ArrayList();
		antlr.CommonHiddenStreamToken cmtToken=((antlr.CommonHiddenStreamToken)LT(1)).getHiddenBefore();
		while(cmtToken!=null){
			if(cmtToken.getType()==ILI_DOC){
				docs.add(0,cmtToken);
			}
			cmtToken=cmtToken.getHiddenBefore();
		}
		Iterator doci=docs.iterator();
		StringBuilder buf=new StringBuilder();
		String sep="";
		while(doci.hasNext()){
			cmtToken=(antlr.CommonHiddenStreamToken)doci.next();
			ilidoc=cmtToken.getText().trim();
			if(ilidoc.startsWith("/**")){
				ilidoc=ilidoc.substring(3);
			}
			if(ilidoc.endsWith("*/")){
				ilidoc=ilidoc.substring(0,ilidoc.length()-2);
			}
			java.io.LineNumberReader lines=new java.io.LineNumberReader(new java.io.StringReader(ilidoc.trim()));
			String line;
			try{
				while((line=lines.readLine())!=null){
					line=line.trim();
					if(line.startsWith("*")){
						line=line.substring(1).trim();
					}
					buf.append(sep);
					buf.append(line);
					sep="\n";
				}
			}catch(java.io.IOException ex){
				EhiLogger.logError(ex);
			}
		}
			ilidoc=buf.toString();
			if(ilidoc.length()==0){
				ilidoc=null;
			}
		return ilidoc;
	}
	private ch.ehi.basics.settings.Settings getMetaValues()
	throws antlr.TokenStreamException
	{ 
		ArrayList docs=new ArrayList();
		antlr.CommonHiddenStreamToken cmtToken=((antlr.CommonHiddenStreamToken)LT(1)).getHiddenBefore();
		while(cmtToken!=null){
			if(cmtToken.getType()==ILI_METAVALUE){
				docs.add(0,cmtToken);
			}
			cmtToken=cmtToken.getHiddenBefore();
		}
		Iterator doci=docs.iterator();
		StringBuilder metaValuesText=new StringBuilder();
		String sep="";
		while(doci.hasNext()){
			cmtToken=(antlr.CommonHiddenStreamToken)doci.next();
			String valueText=cmtToken.getText().trim();
			metaValuesText.append(sep+valueText);
			sep=";";
		}
		ch.ehi.basics.settings.Settings metaValues=null;
		try{
			metaValues=MetaValue.parseMetaValues(metaValuesText.toString());
		}catch(antlr.ANTLRException ex){
			reportError("failed to parse Metavalue <"+metaValuesText.toString()+">",LT(1).getLine());
		}
		return metaValues;
	}

}

//
//  start of grammar
//
public interlisDescription [TransferDescription td1]
	returns [boolean canProceed]
	{
		canProceed = true;
		this.td = td1;
		this.modelInterlis = td.INTERLIS;
		this.predefinedBooleanType = Type.findReal (td.INTERLIS.BOOLEAN.getType());
		this.predefinedScalSystemClass = td.INTERLIS.SCALSYSTEM;
		this.predefinedCoordSystemClass = td.INTERLIS.COORDSYSTEM;
	}
	:	(	interlis2Def
		)
	    exception
		    catch [NoViableAltException nvae]
		    {
		      reportError (rsrc.getString ("err_notIliDescription"));
		      canProceed = false;
		    }
	;


protected interlis2Def
	{
		PrecisionDecimal version;
	}
	:	ili:"INTERLIS" version=decimal
	    {
	      if (version.doubleValue()!=2.4) {
	        reportError(formatMessage("err_wrongInterlisVersion",version.toString()),
	                    ili.getLine());
	        panic();
	      }
              // set lexer mode to Ili 2
              lexer.isIli1=false;
	    }
	SEMI ( modelDef )* EOF
	;

protected modelDef
	{
	  Model md = null;
	  String[] importedNames = null;
	  Table tabDef;
	  int mods = 0;
	  boolean unqualified=false;
	  boolean contracted=false;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	:	{ ilidoc=getIliDoc(); metaValues=getMetaValues();
		}
		("CONTRACTED" { contracted=true; })? 
		(	"REFSYSTEM" "MODEL" { md = new RefSystemModel(); }
		|	"SYMBOLOGY" "MODEL" { md = new SymbologyModel(); }
		|	"TYPE" "MODEL" { md = new TypeModel(); }
		|	"MODEL" { md = new DataModel(); }
		)
		n1:NAME
			{
				try {
				 md.setContracted(contracted);
				 md.setName(n1.getText());
				 md.setSourceLine(n1.getLine());
                                  md.setFileName(getFilename());
				  md.setDocumentation(ilidoc);
				  md.setMetaValues(metaValues);
				  md.setIliVersion(Model.ILI2_4);
				 td.add(md);
				} catch (Exception ex) {
				 reportError(ex, n1.getLine());
				 panic();
				}
			}
		(	LPAREN n:NAME RPAREN
		      {
		        try {
		          md.setLanguage(n.getText());
		        } catch (Exception ex) {
		          reportError(ex, n.getLine());
		        }
		      }
		|
		)
		("NOINCREMENTALTRANSFER"  {
			md.setNoIncrementalTransfer(true);
		}
		)?
		"AT" issuerToken:STRING {
			String issuer=issuerToken.getText();
			md.setIssuer(issuer);
			// http://www.ietf.org/rfc/rfc3986.txt
			// <scheme>:<scheme-specific-part>
			// scheme = alpha *( alpha | digit | "+" | "-" | "." )
			//  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
			java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?");
			java.util.regex.Matcher matcher=pattern.matcher(issuer);
			if(matcher.matches()){
				String scheme=matcher.group(1);
				if(scheme==null){
					reportError("issuer doesn't match URI syntax <"+issuer+">",issuerToken.getLine());
				}
			}else{
				reportError("issuer doesn't match URI syntax <"+issuer+">",issuerToken.getLine());
			}
		}
		"VERSION" ver:STRING (verexpl:EXPLANATION)? {
			md.setModelVersion(ver.getText());
			if(verexpl!=null){
				md.setModelVersionExpl(verexpl.getText());
			}
		}
		(	"TRANSLATION" "OF" translationOf:NAME LBRACE translationOfVersion:STRING RBRACE { md.setTranslationOf(translationOf.getText(),translationOfVersion.getText()); }
		|
		)
		EQUALS
		("CHARSET" ianaNameToken:STRING SEMI {
			md.setCharSetIANAName(ianaNameToken.getText());
		}	
		)?
		("XMLNS" xmlnsToken:STRING SEMI {
			md.setXmlns(xmlnsToken.getText());
		}
		)?
		( "IMPORTS" ("UNQUALIFIED" {unqualified=true;} | /* empty */ {unqualified=false;}) 
			(imp1:NAME
			{
				Model imported;
				imported=resolveOrFixModelName(td, imp1.getText(), imp1.getLine());
				if(imported!=null){
			        md.addImport(imported,unqualified);
				}
			}
			| "INTERLIS"
				{
			        md.addImport(modelInterlis,unqualified);
				}
			)
			( COMMA ("UNQUALIFIED" {unqualified=true;} | /* empty */ {unqualified=false;}) 
				(imp2:NAME
					{
					Model imported;
					imported=resolveOrFixModelName(td, imp2.getText(), imp2.getLine());
					if(imported!=null){
				        	md.addImport(imported,unqualified);
					}
					}
				| "INTERLIS"
					{
					md.addImport(modelInterlis,unqualified);
					}
				)
			)* SEMI
		)*
		(	metaDataBasketDef[md]
		|	unitDefs[md]
		|	functionDef[md]
		|	lineFormTypeDef[md]
		|	domainDefs[md]
		|	contextDefs[md]
		|	runTimeParameterDef[md]
		|	classDef[md]
		|	topicDef[md]
		)*
		end[md] endDot:DOT
	     {
	       try {
			 List<Ili2cSemanticException> errs=new java.util.ArrayList<Ili2cSemanticException>();	       		
	         md.checkIntegrity (errs);
	         reportError(errs);
	       } catch (Ili2cSemanticException ex) {
	         reportError (ex);
	       } catch (Exception ex) {
	         reportError (ex, endDot.getLine());
	       }
	     }
	;


protected topicDef[Container container]
	{
	  Topic topic = null;
	  Topic extending = null;
	  Topic depTopic = null;
	  int mods;
	  boolean viewTopic=false;
	  Domain topicOid=null;
	  Domain topicOid2=null;
	  Domain genericDomain=null;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
	(	"VIEW" {viewTopic=true;}
		|
		)
		"TOPIC" n1:NAME
		    {
		      try {
		        topic = new Topic();
		        topic.setSourceLine(n1.getLine());
			topic.setViewTopic(viewTopic);
		        topic.setName(n1.getText());
			topic.setDocumentation(ilidoc);
			topic.setMetaValues(metaValues);
		      } catch (Exception ex) {
		        reportError(ex, n1.getLine());
		      }
		    }
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT | ch.interlis.ili2c.metamodel.Properties.eFINAL]
		    {
		      try {
		        container.add(topic);
		        topic.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
		        topic.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
		      } catch (Exception ex) {
		        reportError(ex, n1.getLine());
		      }
		    }
		(	extToken:"EXTENDS" extending=topicRef[container]
		      {
		        try {
		          topic.setExtending(extending);
		        } catch (Exception ex) {
		          reportError(ex, extToken.getLine());
		        }
		      }
		|
		)
		EQUALS
		( "BASKET" oid2:"OID" "AS" topicOid2=domainRef[container] SEMI 
			{
				if(!(topicOid2.getType() instanceof OIDType)){
					reportError (formatMessage ("err_topic_domainnotanoid",topicOid2.toString()),oid2.getLine());
				}
				topic.setBasketOid(topicOid2);
			}
		)?
		( oid:"OID" "AS" topicOid=domainRef[container] SEMI
			{
				if(!(topicOid.getType() instanceof OIDType)){
					reportError (formatMessage ("err_topic_domainnotanoid",topicOid.toString()),oid.getLine());
				}
				topic.setOid(topicOid);
			}
		)?
		(	"DEPENDS" on:"ON"
	      depTopic = topicRef [/*scope*/ topic]
	      {
	        try {
	          topic.makeDependentOn(depTopic);
	        } catch (Exception ex) {
	          reportError(ex, on.getLine());
	        }
	      }
		 ( com:COMMA
		      depTopic = topicRef [/*scope*/ topic]
		      {
		        try {
		          topic.makeDependentOn(depTopic);
		        } catch (Exception ex) {
		          reportError(ex, com.getLine());
		        }
		      }
		 )* SEMI
		)*
		(	"DEFERRED" "GENERICS" 
			genericDomain=domainRef[container]
				{ 
					topic.addDeferredGeneric(genericDomain);
				}
			( COMMA genericDomain=domainRef[container]
				{ 
					topic.addDeferredGeneric(genericDomain);
				}
			)* SEMI
		)?
		definitions[topic]
		end[topic]
		SEMI
	;

protected definitions[Container scope]
	:	(	metaDataBasketDef[scope]
		|	unitDefs[scope]
		|	functionDef[scope]
		|	domainDefs[scope]
		|	contextDefs[scope]
		|	classDef[scope]
		|	associationDef[scope]
		|	constraintsDef[scope]
		|	viewDef[scope]
		|	graphicDef[scope]
		)*
	;


protected topicRef[Container scope]
	returns [Topic topic]
	{
	  List      nams = new LinkedList();
	  topic = null;
	  int lin = 0;
	}
	:	lin=names2[nams]
    {
      Model model;
      String   topicName;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topicName = (String) nams.get(0);
        break;

      case 2:
        String modelName = (String) nams.get(0);
        model = resolveOrFixModelName(scope, modelName, lin);
        topicName = (String) nams.get(1);
        break;

      default:
        reportError(rsrc.getString("err_weirdTopicRef"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        topicName = (String) nams.get(nams.size() - 1);
        break;
      }

    topic = (Topic) model.getRealElement(Topic.class, topicName);
    if (topic == null && nams.size()==1) {
	topic = (Topic) model.getImportedElement(Topic.class, topicName);
    }
    if (topic == null) {
      reportError(
        formatMessage("err_noSuchTopic", topicName, model.toString()),
        lin);
      try {
        topic = new Topic();
        topic.setName(topicName);
        model.add(topic);
      } catch (Exception ex) {
        panic();
      }
    }
    }
	;

protected classDef[Container container]
	{
	  Table table = null;
	  Table extending = null;
	  Table overwriting = null;
	  boolean identifiable = true;
	  Constraint constr = null;
	  int mods;
	  Domain classOid=null;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
		(	"CLASS"
		|	("STRUCTURE" { identifiable = false; } )
		)
		n1:NAME
	    {
	      try {
	        table = new Table();
	        table.setSourceLine(n1.getLine());
	        table.setName (n1.getText());
		table.setDocumentation(ilidoc);
		table.setMetaValues(metaValues);
	        table.setIdentifiable (identifiable);
	        table.setAbstract (true);
	      } catch (Exception ex) {
	        reportError(ex, n1.getLine());
	      }

	      overwriting = (Table)container.getElement(Table.class, n1.getText());
	    }
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT | ch.interlis.ili2c.metamodel.Properties.eEXTENDED | ch.interlis.ili2c.metamodel.Properties.eFINAL]
	    {
	      try {
	        table.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
	        table.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
	        if ((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0)
	        {
	          if (overwriting == null)
	          {
	            reportError (formatMessage (
	              "err_noTableOrStructureToExtend",
	              n1.getText(),
	              container.toString()),
	            n1.getLine());
	          }
	          else
	          {
	            table.setExtending (overwriting);
	            table.setExtended(true);
	          }
	        }

	        /* Correct non-ABSTRACT table in Model */
	        if ((container instanceof Model)
	            && !table.isAbstract() && table.isIdentifiable())
	        {
	           reportError (formatMessage (
	             "err_table_concreteOutsideTopic",
	              table.toString()),
	              n1.getLine());

	           table.setFinal(false);
	           table.setAbstract(true);
	        }
	      } catch (Exception ex) {
	        reportError(ex, n1.getLine());
	      }
	    }
		(	extToken:"EXTENDS" extending=classRef[container]
		      {
		        if ((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0)
		        {
		          reportError(rsrc.getString("err_extendedWithExtends"),
		                      extToken.getLine());
		        }
		        else
		        {
		          try {
		            table.setExtending(extending);
		          } catch (Exception ex) {
		            reportError(ex, extToken.getLine());
		          }
		        }
		      }
		|
		)
		EQUALS
		    {
		      try {
		        container.add (table);
		      } catch (Exception ex) {
		        reportError (ex, n1.getLine ());
		        panic ();
		      }
		    }
		( (oid:"OID" "AS" classOid=domainRef[container]  
			{
					if(!(classOid.getType() instanceof OIDType)){
						reportError (formatMessage ("err_topic_domainnotanoid",classOid.toString()),oid.getLine());
					}
			}
		| "NO" "OID" {classOid=NoOid.createNoOid();}) 
			{
				table.setOid(classOid);
			} 
			SEMI 
		)?
		(	"ATTRIBUTE"
		|
		)
		( attributeDef[table] )*
		( constr=constraintDef[table,table.getContainer()]
		      {
		        if (constr != null)
		          table.add (constr);
		      }
		)*
		(	"PARAMETER" ( parameterDef[table] )*
		|
		)
		end[table] SEMI
	;

protected classRef [Container scope]
  returns [Table t]
  {
  List      nams = new LinkedList();
  t = null;
  int lin = 0;
  String tableName = null;
  }
  : 
   lin = names2[nams]
    {
      Model model;
      AbstractPatternDef    topic;
      String   modelName, topicName;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (AbstractPatternDef) scope.getContainerOrSame(AbstractPatternDef.class);
        tableName = (String) nams.get(0);
        break;

      case 2:
        modelName = (String) nams.get(0);
        model = resolveOrFixModelName(scope, modelName, lin);
        tableName = (String) nams.get(1);
        topic = null;
        break;

      case 3:
        modelName = (String) nams.get(0);
        topicName = (String) nams.get(1);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = resolveOrFixTopicName(model, topicName, lin);
        tableName = (String) nams.get(2);
        break;

      default:
        reportError(rsrc.getString("err_weirdTableRef"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);

        topic = null;
        tableName = (String) nams.get(nams.size() - 1);
        break;
      }

      t = null;
      if (topic != null)
        t = (Table) topic.getElement (Table.class, tableName);
      if ((t == null) && (model != null)){
        t = (Table) model.getElement (Table.class, tableName);
      }
      if ((t == null) && (nams.size() == 1)){
      	// unqualified name; search also in unqaulified imported models
        t = (Table) model.getImportedElement (Table.class, tableName);
      }

      if (t == null)
      {
        if (topic == null)
          reportError (formatMessage ("err_noSuchTable", tableName,
                                    model.toString()), lin);
        else
          reportError (formatMessage ("err_noSuchTable", tableName,
                                    topic.toString()), lin);

        if (model != modelInterlis)
        {
          try {
            /* try a fix, so we can continue parsing */
            t = new Table();
            t.setName(tableName);
            if (topic == null) {
              t.setAbstract(true);
              model.add(t);
            } else {
              topic.add(t);
            }
          } catch (Exception ex) {
            panic();
          }
        }

      }
    }
  ;

protected structureRef[Container scope]
  returns [Table t]
  	{
	t=null;
	int refto=0;
	}
	:	{refto=LT(1).getLine();}
		t=classRef[scope]
		{
		if(t.isIdentifiable()){
			reportError(formatMessage("err_structRef_StructRequired",t.getScopedName(null)),refto);
		}
		}
	;

protected attributeDef[Viewable container]
	{
	  int mods = 0;
	  LocalAttribute attrib = null;
	  AttributeDef overriding = null;
	  Type overridingDomain = null;
	  Cardinality overridingCardinality = new Cardinality(0,Cardinality.UNBOUND);
	  Type type = null;
	  boolean mandatory = false;
	  boolean isContinuous=false;
	  boolean isSubdivision=false;
	  String ilidoc=null;
	  Settings metaValues=null;
	  Evaluable f=null;
	  ArrayList fv=new ArrayList();
	}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
	( ( "CONTINUOUS" {isContinuous=true;})? "SUBDIVISION" {isSubdivision=true;})?
	n:NAME
	mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		|ch.interlis.ili2c.metamodel.Properties.eTRANSIENT
		]
	COLON
    {
      overriding = findOverridingAttribute (
        container, mods, n.getText(), n.getLine());
      if (overriding != null){
        overridingDomain = overriding.getDomainResolvingAliases();
      }
 	attrib = new LocalAttribute();
        try {
          attrib.setName(n.getText());
          attrib.setSourceLine(n.getLine());
	  attrib.setDocumentation(ilidoc);
	  attrib.setMetaValues(metaValues);
          attrib.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
          attrib.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
	  attrib.setSubdivision(isSubdivision);
	  attrib.setContinuous(isContinuous);
	  attrib.setTransient((mods & ch.interlis.ili2c.metamodel.Properties.eTRANSIENT) != 0);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
    }
	type=attrTypeDef[container,/* alias ok */ true, overridingDomain,
                     n.getLine(),null]
		{
                    if(type!=null){
		    	if(type instanceof ReferenceType){
				ReferenceType rt=(ReferenceType)type;
				if(!((Table)rt.getReferred()).isIdentifiable()){
					reportError(formatMessage("err_attributeDef_refattrToStruct",n.getText()),n.getLine());
				}
			}
			try{
          			attrib.setDomain(type);
			}catch(Exception ex){
				reportError(ex, n.getLine());
			}
                    }
		}
		(	COLONEQUALS
			f=factor[container,container]
				{
				fv.add(f);
				}
			( COMMA f=factor[container,container]
				{
				fv.add(f);
				}
			)*
			{
			attrib.setBasePaths((Evaluable[])fv.toArray(new Evaluable[fv.size()]));
			}
		)?
		{
			if(attrib.isTransient() && f==null && !attrib.isAbstract()){
				reportError(formatMessage("err_attributeDef_transientWoFactor",n.getText()),n.getLine());
			}
			if(overriding!=null){
				if(overriding.isTransient()!=attrib.isTransient()){
					reportError(formatMessage("err_attributeDef_transientModeChange",n.getText()),n.getLine());
				}
			}
		}
		SEMI
    {
        try {
          container.add(attrib);
          attrib.setExtending(overriding);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
    }
	;

protected attrTypeDef[Container  scope,
	boolean    allowAliases,
	Type       extending,
	int        line,
	ArrayList formalArgs]
	returns [Type typ]
	{
		typ=null;
		Cardinality card=null;
		CompositionType ct=null;
		boolean ordered=false;
	}
	:	
	"MANDATORY" (	
		typ=attrType[scope,allowAliases,extending,line,formalArgs]
			{
			     try {
				if (typ != null){
				  typ.setMandatory(true);
				}
			      } catch (Exception ex) {
				reportError(ex, line);
			      }
			  if(extending!=null && typ instanceof EnumerationType){
				try {
				  ((EnumerationType)typ).checkTypeExtension(extending,false);
				} catch (Exception ex) {
				  reportError (ex, line);
				  typ = null;
				}
			  }
			}
		| /* empty */
		    {
		      if (extending != null){
				try {
				  typ = (Type) extending.clone ();
				} catch (Exception ex) {
				  reportError (ex, line);
				  typ = null;
				}
		      }else{
				reportError (rsrc.getString ("err_type_mandatoryLonely"), line);
				typ = null;
		      }
		      try {
			if (typ != null){
			  typ.setMandatory(true);
			}
		      } catch (Exception ex) {
			reportError(ex, line);
		      }
		    }
		)
	|	typ=attrType[scope,allowAliases,extending,line,formalArgs] 
		{
			  if(extending!=null && typ instanceof EnumerationType){
				try {
				  ((EnumerationType)typ).checkTypeExtension(extending,false);
				} catch (Exception ex) {
				  reportError (ex, line);
				  typ = null;
				}
			  }
		}
	|	(	"BAG" {ordered=false;}
		|	"LIST" {ordered=true;}
		)
		(	card=cardinality
		|
		)
		"OF" typ=attrType[scope,allowAliases,extending,line,formalArgs]
		{
			try{
				if(card!=null){
					typ.setCardinality(card);
				}else{
					typ.setCardinality(new Cardinality(0,Cardinality.UNBOUND));
				}
				typ.setOrdered(ordered);
			}catch(Exception ex){
			    reportError(ex, line);
			}
		}
	;

protected attrType[Container  scope,
	boolean    allowAliases,
	Type       extending,
	int        line,
	ArrayList formalArgs]
	returns [Type typ]
	{
		List nams = new LinkedList();
		typ=null;
		Table restrictedTo=null;
		int lin=0;
		CompositionType ct=null;
	}
	:	typ=type[scope,extending,formalArgs,false]
	//|	domainRef
	//|	restrictedStructureRef
	| (lin = names2[nams]
		{
			Table s;
			Element e=resolveStructureOrDomainRef(scope,(String[]) nams.toArray(new String[nams.size()]),lin);
			if(e instanceof Table){
				s=(Table)e;
				ct=new CompositionType();
				try{
					ct.setSourceLine(lin);
					ct.setCardinality(new Cardinality(0,1));
					ct.setComponentType(s);
				}catch(Exception ex){
				    reportError(ex, line);
				}
				typ=ct;
			}else{
				Domain aliased=(Domain)e;
				if ((!allowAliases)
				  && !((aliased != null) && (aliased.getContainer (Model.class) == td.INTERLIS)))
				{
				  reportError (rsrc.getString ("err_aliasAtWrongPlace"), line);
				}
				else
				{
				  typ = new TypeAlias();
				  try {
				    ((TypeAlias) typ).setAliasing (aliased);
				  } catch (Exception ex) {
				    reportError(ex, line);
				  }
				}
				ct=null;
			}

		}
		| as:"ANYSTRUCTURE" {
				ct=new CompositionType();
				try{
					ct.setSourceLine(as.getLine());
					ct.setCardinality(new Cardinality(0,1));
					ct.setComponentType(modelInterlis.ANYSTRUCTURE);
				}catch(Exception ex){
				    reportError(ex, as.getLine());
				}
				typ=ct;
		}
		)
		(
		{ct!=null}?
		"RESTRICTION" LPAREN restrictedTo=structureRef[scope]
			{ ct.addRestrictedTo(restrictedTo); }
			( SEMI restrictedTo=structureRef[scope]
				{ ct.addRestrictedTo(restrictedTo); }
			)*
			RPAREN
		)?
	|       typ=referenceAttr[scope]
	;

protected referenceAttr[Container scope]
	returns[ReferenceType rt]
	{
	rt=null;
	int mods=0;
	}
	: refkw:"REFERENCE" "TO" 
 	mods=properties[ch.interlis.ili2c.metamodel.Properties.eEXTERNAL]
	rt=restrictedClassOrAssRef[scope]
	{
		boolean external=(mods & ch.interlis.ili2c.metamodel.Properties.eEXTERNAL)!=0;
		Topic thisTopic=(Topic)scope.getContainerOrSame(Topic.class);
	  	try{
		  Iterator<AbstractClassDef> targeti=rt.iteratorRestrictedTo();
		  if(targeti.hasNext()){
			  while(targeti.hasNext()){
				  AbstractClassDef<?> target=targeti.next();
				  AbstractPatternDef.checkRefTypeTarget(thisTopic, null,null, target, external);
			  }
		  }else{
			  AbstractClassDef<?> target=rt.getReferred();
			  AbstractPatternDef.checkRefTypeTarget(thisTopic, null, null,target, external);
		  }
		}catch(Ili2cSemanticException ex){
	            reportError(ex,refkw.getLine());
	        }
		rt.setExternal(external);
	}
	;

protected restrictedClassOrAssRef[Container scope]
	returns[ReferenceType rt]
	{
		AbstractClassDef ref;
		AbstractClassDef restrictedTo;
		rt=new ReferenceType();
		int refto=0;
		int mods=0;
	}
    :   {refto=LT(1).getLine();}
    	("ANYCLASS"
	{
		try{
			rt.setReferred(modelInterlis.ANYCLASS);
		}catch(Exception ex){
			reportError(ex,refto);
		}
	}
    	| 
    	ref=classOrAssociationRef[scope]
    	{
		try{
		  rt.setReferred(ref);
		}catch(Exception ex){
			reportError(ex,refto);
		}
	}
	)
	{ rt.setSourceLine(refto);
	}
    	("RESTRICTION" LPAREN restrictedTo=classOrAssociationRef[scope]
			{ rt.addRestrictedTo(restrictedTo); }
		(SEMI restrictedTo=classOrAssociationRef[scope]
			{ rt.addRestrictedTo(restrictedTo); }
		)*
		RPAREN
	)?
    ;

protected classOrAssociationRef[Container scope]
	returns[AbstractClassDef def]
	{
	def=null;
	Viewable ref;
	int line=0;
	}
	:	{line=LT(1).getLine();}
		ref=viewableRef[scope]
		{
			if(ref instanceof AbstractClassDef){
				def=(AbstractClassDef)ref;
			}else{
				reportError(formatMessage ("err_classOrAssociationRef",ref.getScopedName(null)),line);
			}
		}
	;

protected restrictedStructureRef[Container scope]
	returns[CompositionType ct]
	{
		Table ref=null;
		Table restrictedTo;
		ct=null;
		int line=0;
	}
	: {line=LT(1).getLine();}
	( "ANYSTRUCTURE"
		{
		ref=modelInterlis.ANYSTRUCTURE;
		}
	|
		
		ref=structureRef[scope]
	)
			{
				ct=new CompositionType();
				try{
					ct.setSourceLine(line);
					ct.setComponentType(ref);
				}catch(Exception ex){
					reportError(ex, line);
				}
			}
		(	"RESTRICTION" LPAREN restrictedTo=structureRef[scope]
			{ ct.addRestrictedTo(restrictedTo); }
			( SEMI restrictedTo=structureRef[scope]
				{ ct.addRestrictedTo(restrictedTo); }
			)*
			RPAREN
		)?
	;

protected associationDef[Container scope]
	{
	int mods;
	AssociationDef def=new AssociationDef();
	AssociationDef extending = null;
	ViewableAlias derivedFrom;
	Constraint constr;
	Cardinality card=null;
	  String ilidoc=null;
	  Settings metaValues=null;
	  Domain assocOid=null;
	}
	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
		a:"ASSOCIATION"
		( n:NAME
			{
				try{
					def.setName(n.getText());
				} catch (Exception ex) {
					reportError(ex, n.getLine());
				}
			}
		)?
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL
			|ch.interlis.ili2c.metamodel.Properties.eOID
			]
			{
				try {
					def.setSourceLine(a.getLine());
					def.setDocumentation(ilidoc);
					def.setMetaValues(metaValues);
					def.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
					def.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
	    				def.setExtended((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0);
	    				def.setIdentifiable((mods & ch.interlis.ili2c.metamodel.Properties.eOID) != 0);
				} catch (Exception ex) {
					reportError(ex, a.getLine());
				}
			}
		( extToken:"EXTENDS" extending=associationRef[scope]
		      {
		        if ((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0)
		        {
		          reportError(rsrc.getString("err_extendedWithExtends"),
		                      extToken.getLine());
		        }
		        else
		        {
		          try {
		            def.setExtending(extending);
		          } catch (Exception ex) {
		            reportError(ex, extToken.getLine());
		          }
		        }
		      }
		)?
		(	derivedToken:"DERIVED" "FROM" derivedFrom=renamedViewableRef[scope]
		        {
		          try {
		            def.setDerivedFrom(derivedFrom.getAliasing());
				LocalAttribute attrib=new LocalAttribute();
				attrib.setName(derivedFrom.getName());
				attrib.setDomain(new ObjectType(derivedFrom.getAliasing()));
				def.add(attrib);
		          } catch (Exception ex) {
		            reportError(ex, derivedToken.getLine());
		          }
		        }
		)?
		EQUALS
		( (oid:"OID" "AS" assocOid=domainRef[scope] 
				{
					if(!(assocOid.getType() instanceof OIDType)){
						reportError (formatMessage ("err_topic_domainnotanoid",assocOid.toString()),oid.getLine());
					}
				}
			| "NO" "OID" {assocOid=NoOid.createNoOid();}) 
			SEMI 
				{ 
					def.setOid(assocOid); 
				}
		)?
		{
			scope.add(def);
		}
		roleDefs[def]
		{
			if ((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0)
			{
				AssociationDef overwriting = null;
				AbstractPatternDef baseTopic=(AbstractPatternDef)((AbstractPatternDef)scope).getExtending();
				if(baseTopic!=null){
					overwriting = (AssociationDef) baseTopic.getRealElement
	                                       (AssociationDef.class, def.getName());
				}
			  if (overwriting == null)
			  {
			    reportError (formatMessage (
			      "err_noAssociationToExtend",
			      def.getName(),
			      scope.toString()),
			    a.getLine());
			  }
			  else
			  {
			  	try{
				    def.setExtending (overwriting);
				}catch(Exception ex){
			            reportError(ex, a.getLine());
				}
			  }
			}
		  	try{
			    // check roleDefs
			    def.fixupRoles();
			}catch(Ili2cSemanticException ex){
		            reportError(ex);
			}catch(Exception ex){
		            reportError(ex, a.getLine());
			}
		}
		("ATTRIBUTE")?
		( attributeDef[def] )*
		("CARDINALITY" EQUALS card=cardinality SEMI 
			{
			def.setCardinality(card);
			}
		)?
		( constr=constraintDef[def,def.getContainer()]
			{if(constr!=null)def.add(constr);}
		)*
		"END"
		(nam:NAME
			{
			if (!nam.getText().equals(def.getName())){
				reportError(
					formatMessage ("err_end_mismatch", def.toString(),
					def.getName(), nam.getText()),
					nam.getLine());
			}
			}
		)?
		SEMI
	;
protected roleDefs[AssociationDef container]
	: (NAME properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL
			|ch.interlis.ili2c.metamodel.Properties.eHIDING
			|ch.interlis.ili2c.metamodel.Properties.eORDERED
			|ch.interlis.ili2c.metamodel.Properties.eEXTERNAL
			]
		(	ASSOCIATE
		|	AGGREGATE
		|	COMPOSITE
		) )=> roleDef[container] roleDefs[container]
	|
	;
protected associationRef[Container scope]
	returns[AssociationDef ref]
	{ ref=null;
	Viewable t;
	int refline=0;
	}
	:
		{refline=LT(1).getLine();}
		t=viewableRef[scope]
		{
			if(t instanceof AssociationDef){
				ref=(AssociationDef)t;
			}else{
				reportError(formatMessage ("err_assocref_notAnAssoc",t.getScopedName(null)),refline);
			}
		}
	;


protected roleDef[AssociationDef container]
	{
		Cardinality card=null;
		int mods;
		ReferenceType ref=null;
		Evaluable obj=null;
		RoleDef def=new RoleDef(true);
		int kind=0;
		boolean external=false;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
	n:NAME {def.setSourceLine(n.getLine());}
	mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		|ch.interlis.ili2c.metamodel.Properties.eHIDING
		|ch.interlis.ili2c.metamodel.Properties.eORDERED
		|ch.interlis.ili2c.metamodel.Properties.eEXTERNAL
		]
	(	ASSOCIATE {kind=RoleDef.Kind.eASSOCIATE;}
	|	AGGREGATE {kind=RoleDef.Kind.eAGGREGATE;}
	|	COMPOSITE {kind=RoleDef.Kind.eCOMPOSITE;}
	)
	(	card=cardinality
	)?
	ref=restrictedClassOrAssRef[container]
	{
		try {
			
			external=(mods & ch.interlis.ili2c.metamodel.Properties.eEXTERNAL)!=0;
			Topic targetTopic=(Topic)ref.getReferred().getContainerOrSame(Topic.class);
			Topic thisTopic=(Topic)container.getContainerOrSame(Topic.class);
			// target in a topic and targets topic not a base of this topic 
			if(targetTopic!=null && thisTopic!=null && !thisTopic.isExtending(targetTopic)){
				if(!external){
					// must be external
					reportError(formatMessage ("err_role_externalreq",""),n.getLine());
				}else{
				  if(targetTopic!=thisTopic){
				    if(!thisTopic.isDependentOn(targetTopic)){
				      reportError(formatMessage ("err_viewableref_topicdepreq",
					thisTopic.getName(),
					targetTopic.getName()),n.getLine());
				    }
				  }
				}
			}
			ref.setExternal(external);
		  def.setName(n.getText());
		  def.setDocumentation(ilidoc);
		  def.setMetaValues(metaValues);
		  def.setExtended((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0);
		  def.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
		  def.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
		  def.setHiding((mods & ch.interlis.ili2c.metamodel.Properties.eHIDING) != 0);
		  def.setOrdered((mods & ch.interlis.ili2c.metamodel.Properties.eORDERED) != 0);
		  def.setKind(kind);
		  if(card!=null){
		  	if(kind==RoleDef.Kind.eCOMPOSITE
				&& card.getMaximum()>1){
                              reportError(formatMessage ("err_role_maxcard1",n.getText())
			      	,n.getLine());
			}
		  	def.setCardinality(card);
		  }
		if((ref.getReferred() instanceof Table) && !((Table)ref.getReferred()).isIdentifiable()){
			reportError(formatMessage("err_role_toStruct",n.getText()),n.getLine());
		}
		  def.addReference(ref);
		  container.add(def);
		} catch (Exception ex) {
		  reportError(ex, n.getLine());
		}
	}
	( "OR" ref=restrictedClassOrAssRef[container]
		{ 
			Topic targetTopic=(Topic)ref.getReferred().getContainerOrSame(Topic.class);
			Topic thisTopic=(Topic)container.getContainerOrSame(Topic.class);
			// target in a topic and targets topic not a base of this topic 
			if(targetTopic!=null && thisTopic!=null && !thisTopic.isExtending(targetTopic)){
				if(!external){
					// must be external
					reportError(formatMessage ("err_role_externalreq",""),n.getLine());
				}else{
				  if(targetTopic!=thisTopic){
				    if(!thisTopic.isDependentOn(targetTopic)){
				      reportError(formatMessage ("err_viewableref_topicdepreq",
					thisTopic.getName(),
					targetTopic.getName()),n.getLine());
				    }
				  }
				}
			}
			ref.setExternal(external);
			if((ref.getReferred() instanceof Table) && !((Table)ref.getReferred()).isIdentifiable()){
				reportError(formatMessage("err_role_toStruct",n.getText()),n.getLine());
			}
			def.addReference(ref);
		
		}
	)*
	(	 col:COLONEQUALS obj=factor[container,container]
	{
		  	if(!(obj instanceof ObjectPath)){
                              reportError(formatMessage ("err_role_factorNotAnObjectPath","")
			      	,col.getLine());

			}
		  	def.setDerivedFrom((ObjectPath)obj);
	}
	)?
	SEMI
	;

protected cardinality
	returns [Cardinality card]
	{
	  long min = 0;
	  long max = Cardinality.UNBOUND;
	  card=null;
	}
	: lcurl:LCURLY
    (
      STAR
    | min = posInteger
      ( DOTDOT ( max = posInteger | STAR )
      | /* empty, as in "{42}" */ { max = min; })
    )
    RCURLY
    {
      try {
        card = new Cardinality(min, max);
      } catch (Exception ex) {
        reportError(ex, lcurl.getLine());
      }
    }
  ;


protected domainDef[Container container]
	{
	  Domain     extending = null;
	  Type       extendingType = null;
	  Type       declared = null;
	  int        mods = 0;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
		n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eFINAL|ch.interlis.ili2c.metamodel.Properties.eGENERIC]
		(	"EXTENDS" extending=domainRef[container]
		      {
			if (extending != null)
			  extendingType = extending.getType();
		      }
		)?
		eq:EQUALS
		(	"MANDATORY" (declared=type[container,extendingType,null,(mods&ch.interlis.ili2c.metamodel.Properties.eGENERIC)!=0])?
		|	declared=type[container,extendingType,null,(mods&ch.interlis.ili2c.metamodel.Properties.eGENERIC)!=0]
		)
		( "CONSTRAINTS" 
		NAME COLON expression[container,predefinedBooleanType,container] 
			(COMMA NAME COLON expression[container,predefinedBooleanType,container] 
			)*
		)?
		SEMI
		{
		Domain dd = new Domain ();

		try {
		dd.setName (n.getText());
		dd.setDocumentation(ilidoc);
		dd.setMetaValues(metaValues);
		dd.setSourceLine(n.getLine());
		try {
		  if ((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0)
		    dd.setAbstract (true);
		} catch (Exception ex) {
		  reportError (ex, n.getLine());
		}

		try {
		  if ((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0)
		    dd.setFinal (true);
		} catch (Exception ex) {
		  reportError (ex, n.getLine());
		}

		try {
		  if (declared != null)
		    dd.setType (declared);
		} catch (Exception ex) {
		  reportError (ex, n.getLine());
		}

		try {
		  if ((declared != null)
		      && !declared.isMandatory()
		      && (extendingType != null)
		      && extendingType.isMandatory())
		  {
		    reportError(
			formatMessage("err_extendingMandatoryDomain",
				      dd.toString (), extending.toString ()),
			eq.getLine());
		    declared.setMandatory(true);
		  }

		  if(declared!=null && declared instanceof EnumerationType){
			try {
			  ((EnumerationType)declared).checkTypeExtension(extendingType,false);
			} catch (Exception ex) {
			  reportError (ex, n.getLine());
			}
		  }
		  dd.setExtending (extending);
		} catch (Exception ex) {
		  reportError(ex, n.getLine());
		}

		container.add (dd);
		} catch (Exception ex) {
		reportError(ex, n.getLine());
		}
		}
	;

protected domainDefs[Container container]
	:	"DOMAIN" ( domainDef[container] )*
	;

protected type[Container scope,Type extending,ArrayList formalArgs,boolean isGeneric]
	returns [Type typ]
	{ typ=null;
	}
	:	(	typ=baseType[scope,extending,formalArgs,isGeneric]
		|	typ=lineType[scope,extending]
		)

	;

protected domainRef [Container scope]
  returns [Domain d]
{
  List      nams = new LinkedList();
  d = null;
  int lin = 0;
}
  :

    lin = names2[nams]
    {
      d=resolveDomainRef(scope,(String[]) nams.toArray(new String[nams.size()]),lin);
    }
  ;

protected baseType [Container scope, Type extending,ArrayList formalArgs,boolean isGeneric]
	returns [Type bt]
	{
		bt = null;
	}
	:	bt=textType[extending]
	|	bt=enumerationType[extending]
	|	bt=enumTreeValueType[scope,extending]
	|	bt=alignmentType
	|	bt=booleanType
	|	bt=numericType[scope,extending,false]
	|	bt=formattedType[scope,extending]
	|	bt=dateTimeType[scope,extending]
	|	bt=coordinateType[scope,extending,isGeneric]
	|	bt=oIDType[scope,extending]
	|	bt=blackboxType[scope,extending]
	|	bt=classType[scope,extending]
	|	bt=attributePathType[scope,extending,formalArgs]
	;

protected constant [Container scope]
	returns [Constant c]
	{
	  c = null;
	}
	:	"UNDEFINED" { c = new Constant.Undefined(); }
	|	c=numericConst[scope]
	|	c=textConst
//	|	c=formattedConst[scope] same as textConst
	|	c=enumerationConst
	|	c=classConst[scope]
	|	c=attributeConst[scope]
	;

protected textType[Type extending]
	returns [Type tt]
	{
		tt = null;
		int i = -1;
		boolean normalizedText=false;
	}
	: ur:"URI"
    {
      tt = new TypeAlias ();
      try {
        ((TypeAlias) tt).setAliasing (modelInterlis.URI);
      } catch (Exception ex) {
        reportInternalError (ex, ur.getLine());
      }
    }

  | nm:"NAME"
    {
      tt = new TypeAlias ();
      try {
        ((TypeAlias) tt).setAliasing (modelInterlis.NAME);
      } catch (Exception ex) {
        reportInternalError (ex, nm.getLine());
      }
    }

  | ("MTEXT"
  	| "TEXT" {normalizedText=true;})
    (
      ( star:STAR i=posInteger
        {
          try {
            tt = new TextType(i);
	    ((TextType)tt).setNormalized(normalizedText);
          } catch (Exception ex) {
            reportError (ex, star.getLine());
          }
        }
      )

      | /* epsilon */
        {
          tt = new TextType ();
          ((TextType)tt).setNormalized(normalizedText);
        }
    )
  ;


protected textConst
	returns [Constant c]
	{
		c=null;
	}
	: s:STRING {c=new Constant.Text(s.getText());}
	;

protected enumerationType [Type extending]
  returns [EnumerationType et]
{
  et = new EnumerationType();
  ch.interlis.ili2c.metamodel.Enumeration enumer;
}
  : enumer = enumeration [extending]
    {
      try {
        et.setEnumeration(enumer);
      } catch (Exception ex) {
        reportError(ex, 0);
      }
    }
    ( ord:"ORDERED"
      {
        try {
          et.setOrdered(true);
        } catch (Exception ex) {
          reportError(ex, ord.getLine());
        }
      }
    | circ:"CIRCULAR"
      {
        try {
          et.setCircular(true);
        } catch (Exception ex) {
          reportError(ex, circ.getLine());
        }
      }
    )?
  ;
  
protected enumTreeValueType [Container scope, Type extending]
  returns [EnumTreeValueType et]
  {
  Domain ref=null;
  et=null;
  }
  :
  "ALL" "OF" ref=domainRef[scope] 
  	{ /* TODO check that enumType */
	et=new EnumTreeValueType();
	et.setEnumType(ref);
	return et;
	}
  ;

protected enumeration [Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration enumer]
{
  List elements = new LinkedList();
  ch.interlis.ili2c.metamodel.Enumeration.Element curElement;
  boolean isFinal=false;
  enumer = null;
}
  : lp:LPAREN
    (
      (curElement = enumElement[elements,extending] { elements.add(curElement); }
        (COMMA curElement = enumElement[elements,extending] 
    	  { 
    	  	// new element?
    	  	if(curElement!=null){
		  Iterator elei=elements.iterator();
		  while(elei.hasNext()){
			  ch.interlis.ili2c.metamodel.Enumeration.Element ele=(ch.interlis.ili2c.metamodel.Enumeration.Element)elei.next();
			  if(ele.getName().equals(curElement.getName())){
				  reportError(formatMessage("err_enumerationType_DupEle",curElement.getName()),curElement.getSourceLine());
				  break;
			  }
		  }
		  elements.add(curElement); 
    	  	}else{
    	  	  // extension of existing element alread done in enumElement[]
    	  	}
	  }
        )*
        ( COLON "FINAL"  {isFinal=true;} )?
      )
      | "FINAL" {isFinal=true;}
    )
    RPAREN
    {
      enumer = new ch.interlis.ili2c.metamodel.Enumeration(elements);
      enumer.setFinal(isFinal);
      enumer.setSourceLine(lp.getLine());
    }
  ;



protected enumElement [List elements,Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration.Element ee]
{
  ch.interlis.ili2c.metamodel.Enumeration subEnum = null;
  ch.interlis.ili2c.metamodel.Enumeration.Element curEnum = null;
  ee = null;
  int lineNumber = 0;
  List elt = new LinkedList();
  int siz = 0;
	  String ilidoc=null;
	  Settings metaValues=null;
}
  :  { ilidoc=getIliDoc();metaValues=getMetaValues();}
   lineNumber = enumNameList [elt]
    ( subEnum = enumeration[extending] )?

    {
      siz = elt.size();
      if ((subEnum == null) && (siz > 1)){
        reportError (rsrc.getString("err_dottedEnum"), lineNumber);
      }
      if(extending!=null){
      	List eles=elements;
 	ch.interlis.ili2c.metamodel.Enumeration.Element existingEle=null;
 	int existingEleNameIdx=0;
      	for(int i=0;i<siz;i++){
      		String curPathEle=(String)elt.get(i);
      		ch.interlis.ili2c.metamodel.Enumeration.Element foundEle=null;
		Iterator elei=eles.iterator();
		while(elei.hasNext()){
			  ch.interlis.ili2c.metamodel.Enumeration.Element ele=(ch.interlis.ili2c.metamodel.Enumeration.Element)elei.next();
			  if(ele.getName().equals(curPathEle)){
			  	foundEle=ele;
			  	break;
			  }
		}
		if(foundEle!=null){
			existingEleNameIdx=i;
			existingEle=foundEle;
			if(foundEle.getSubEnumeration()!=null){
				eles=new ArrayList<ch.interlis.ili2c.metamodel.Enumeration.Element>();
				for(Iterator<ch.interlis.ili2c.metamodel.Enumeration.Element> e=foundEle.getSubEnumeration().getElements();e.hasNext();){
					eles.add(e.next());
				}
				continue;
			}
		}
		break;
	}
      	// new path?
      	if(existingEle==null){
	      for (int i = siz - 1; i >= 0; i--)
	      {
		// last path element?
		if (i == siz - 1)
		{
		  if (subEnum == null)
		  {
		    // new leaf
		    ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
		       (String) elt.get(i));
		    ee.setDocumentation(ilidoc);
		    ee.setMetaValues(metaValues);
		    ee.setSourceLine(lineNumber);
		  }
		  else
		  {
		    // new subtree
		    ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
		       (String) elt.get(i),
		       subEnum);
		    ee.setDocumentation(ilidoc);
		    ee.setMetaValues(metaValues);
		    ee.setSourceLine(lineNumber);
		  }
		}
		else
		{
		  // not last path element
		  List subEe=new ArrayList();
		  subEe.add(ee);
		  ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
		       (String) elt.get(i),
		       new ch.interlis.ili2c.metamodel.Enumeration (subEe)
		  );
		  ee.setSourceLine(lineNumber);
		}
	      }
      	}else{
	      	// extend exisiting path
	      ch.interlis.ili2c.metamodel.Enumeration.Element newEle=null;
	      for (int i = siz - 1; i >= existingEleNameIdx; i--)
	      {
		// last path element?
		if (i == siz - 1)
		{
		  if (subEnum == null)
		  {
		    // new leaf
		    newEle = new ch.interlis.ili2c.metamodel.Enumeration.Element (
		       (String) elt.get(i));
		    newEle.setDocumentation(ilidoc);
		    newEle.setMetaValues(metaValues);
		    newEle.setSourceLine(lineNumber);
		  }
		  else
		  {
		    // new subtree
		    newEle = new ch.interlis.ili2c.metamodel.Enumeration.Element (
		       (String) elt.get(i),
		       subEnum);
		    newEle.setDocumentation(ilidoc);
		    newEle.setMetaValues(metaValues);
		    newEle.setSourceLine(lineNumber);
		  }
		}
		else
		{
		  // not last path element
		  // first new path element?
		  if(i==existingEleNameIdx){
		  	// check that it is a new name in this extension
			  Iterator elei=existingEle.getSubEnumeration().getElements();
			  while(elei.hasNext()){
				  ch.interlis.ili2c.metamodel.Enumeration.Element ele=(ch.interlis.ili2c.metamodel.Enumeration.Element)elei.next();
				  if(ele.getName().equals(newEle.getName())){
					  reportError(formatMessage("err_enumerationType_DupEle",newEle.getName()),existingEle.getSourceLine());
					  break;
				  }
			  }
		  	existingEle.getSubEnumeration().addElement(newEle);
		  }else{
			  // not first new path element
			  List subEe=new ArrayList();
			  subEe.add(newEle);
			  newEle = new ch.interlis.ili2c.metamodel.Enumeration.Element (
			       (String) elt.get(i),
			       new ch.interlis.ili2c.metamodel.Enumeration (subEe)
			  );
			  newEle.setSourceLine(lineNumber);
		  }
		}
	      }
      	}
      }else{
      	if(siz > 1){
        	reportError (rsrc.getString("err_dottedEnum"), lineNumber);
        }
          if (subEnum == null)
          {
	    // new leaf
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               (String) elt.get(0));
	    ee.setDocumentation(ilidoc);
	    ee.setMetaValues(metaValues);
	    ee.setSourceLine(lineNumber);
          }
          else
          {
	    // new subtree
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               (String) elt.get(0),
               subEnum);
	    ee.setDocumentation(ilidoc);
	    ee.setMetaValues(metaValues);
	    ee.setSourceLine(lineNumber);
          }
      }
    }
  ;

protected enumerationConst
	returns[Constant.Enumeration c]
	{
	List mentionedNames=new ArrayList();
	int lin=0;
	c=null;
	}
	: HASH
	( lin=enumNameList[mentionedNames] 
		( DOT "OTHERS" 
			{mentionedNames.add(Constant.Enumeration.OTHERS);
			}
		)?
		{
		c = new Constant.Enumeration(mentionedNames);
		}
	| "OTHERS"
		{
		mentionedNames.add(Constant.Enumeration.OTHERS);
		c = new Constant.Enumeration(mentionedNames);
		}
	)
	;

protected alignmentType
  returns [Type tt]
{
  tt = null;
}
  : h:"HALIGNMENT"
    {
      tt = new TypeAlias ();
      try {
        ((TypeAlias) tt).setAliasing (modelInterlis.HALIGNMENT);
      } catch (Exception ex) {
        reportInternalError (ex, h.getLine());
      }
    }

  | v:"VALIGNMENT"
    {
      tt = new TypeAlias ();
      try {
        ((TypeAlias) tt).setAliasing (modelInterlis.VALIGNMENT);
      } catch (Exception ex) {
        reportInternalError (ex, v.getLine());
      }
    }
  ;

protected booleanType
  returns [Type tt]
{
  tt = null;
}
  : b:"BOOLEAN"
    {
      tt = new TypeAlias ();
      try {
        ((TypeAlias) tt).setAliasing (modelInterlis.BOOLEAN);
      } catch (Exception ex) {
        reportInternalError (ex, b.getLine());
      }
    }
  ;

protected numericType [Container scope, Type extending,boolean isCoord]
  returns [NumericType ntyp]
{
  PrecisionDecimal min = null, max = null;
  int rotation = 0;
  int rotationLine = 0;
  ntyp = null;
  Unit u = null;
  RefSystemRef referenceSystem = null;
  int line = 0;
}
  : ( min=decimal dots:DOTDOT max=decimal
      {
        line = rotationLine = dots.getLine();
        try {
          ntyp = new NumericType(min, max);
        } catch (Exception ex) {
          reportError(ex, dots.getLine());
          ntyp = new NumericType();
        }
      }
    | numer:"NUMERIC"
      {
        ntyp = new NumericType();
        line = rotationLine = numer.getLine();
      }
    )

    ( circ:"CIRCULAR"
      {
        try {
          ntyp.setCircular(true);
        } catch (Exception ex) {
          reportError(ex, circ.getLine());
        }
      }
    )?

    ( lbrac:LBRACE
      u = unitRef [scope]
      RBRACE
    )?

    ( cw:"CLOCKWISE"
      {
        rotation = NumericalType.ROTATION_CLOCKWISE;
        rotationLine = cw.getLine();
      }
    | ccw:"COUNTERCLOCKWISE"
      {
        rotation = NumericalType.ROTATION_COUNTERCLOCKWISE;
        rotationLine = ccw.getLine();
      }
    |  referenceSystem = refSys [scope,isCoord]
    )?

    {
      try {
        if ((u == null) && (extending instanceof NumericType))
          u = ((NumericType) extending).getUnit ();

        ntyp.setUnit(u);
      } catch (Exception ex) {
        reportError(ex, lbrac.getLine());
      }

      try {
        if (ntyp != null)
          ntyp.setRotation(rotation);
      } catch (Exception ex) {
        reportError(ex, rotationLine);
      }

      try {
        if ((referenceSystem == null) && (extending instanceof NumericType))
          referenceSystem = ((NumericType) extending).getReferenceSystem ();

        ntyp.setReferenceSystem (referenceSystem);
      } catch (Exception ex) {
        reportError(ex, line);
      }
    }
  ;

protected refSys[Container scope,boolean isCoord]
	returns [RefSystemRef rsr]
	{
	rsr = null;

	MetaObject  system = null;
	Domain        domain = null;
	int axisNumber = 0;
	}

	:
	lpar:LCURLY
           ({isCoord}?     // "((NAME (DOT NAME)* LBRACE)=>" commented because syntactic predicate superfluous for single alternative
	      (system=metaObjectRef[scope,predefinedCoordSystemClass]
		 slash1:LBRACE axisNumber=posInteger RBRACE
		      { 
			try {
			  if (system != null)
			    rsr = new RefSystemRef.CoordSystemAxis (system, axisNumber);
			} catch (Exception ex) {
			  reportError (ex, slash1.getLine ());
			}
		      }
	     )
	  | {!isCoord}? system=metaObjectRef[scope,predefinedScalSystemClass]
		      {
			try {
			  if (system != null)
			    rsr = new RefSystemRef.CoordSystem (system);
			} catch (Exception ex) {
			  reportError (ex, lpar.getLine ());
			}
		      }
		      
	  )
	RCURLY
  | less:LESS
    domain=domainRef [scope]
    (
      slash2:LBRACE
      axisNumber = posInteger
      RBRACE
      {
        try {
          if (domain != null)
            rsr = new RefSystemRef.CoordDomainAxis (domain, axisNumber);
        } catch (Exception ex) {
          reportError (ex, slash2.getLine ());
        }
      }
    | /* epsilon */
      {
        try {
          if (domain != null)
            rsr = new RefSystemRef.CoordDomain (domain);
        } catch (Exception ex) {
          reportError (ex, less.getLine ());
        }
      }
    )
    GREATER
	;

protected decConst
	returns [PrecisionDecimal dec]
	{
	dec=null;
	}
	: "PI" { dec = PrecisionDecimal.PI; }
	| "LNBASE" {dec = PrecisionDecimal.LNBASE;}
	| dec = decimal
	;

protected numericConst[Container scope]
	returns[Constant c]
	{ Unit un=null;
	PrecisionDecimal val;
	c=null;
	}
	: val=decConst ( LBRACE un=unitRef[scope] RBRACE )?
	{
		if (un == null){
			c = new Constant.Numeric (val);
		}else{
			c = new Constant.Numeric (val, un);
		}
	}

	;

protected formattedType[Container scope, Type extending]
	returns [FormattedType ft]
	{
		ft=new FormattedType();
		Domain domain=null;
		Table struct=null;
		FormattedTypeBaseAttrRef baseAttr=null;
		int line=0;
	}
	: 
	(( "FORMAT" "BASED")=>( format_kw:"FORMAT" "BASED" "ON" struct=structureRef[scope] 
		{
			line=format_kw.getLine();
			ft=new FormattedType();
			if(extending!=null){
				try{
				ft.setExtending(extending);
				}catch(Exception ex){
					reportError(ex,line);
				}
			}
			ft.setBaseStruct(struct);
		}
		LPAREN ("INHERITANCE")?
                  ( prefix:STRING {
				if(prefix!=null){
					ft.setPrefix(prefix.getText());
				}
                  	}
                  )? 
                  ( baseAttr=baseAttrRef[ft,struct] {postfix=null;} (postfix:STRING)?
		  		{ 	if(postfix!=null){
						baseAttr.setPostfix(postfix.getText());
					}
					ft.addBaseAttrRef(baseAttr);
				}
		  	)*
                             RPAREN
		( min:STRING DOTDOT max:STRING )?
		{ 
			if(min!=null){
				validateFormattedConst(ft,min.getText(),min.getLine());
				validateFormattedConst(ft,max.getText(),max.getLine());
				ft.setMinimum(min.getText());
				ft.setMaximum(max.getText());
			}
		}
	) 
	| ( min2:STRING DOTDOT max2:STRING 
		{
			line=min2.getLine();
			ft=new FormattedType();
			if(extending!=null){
				//EhiLogger.debug("extending baseStruct "+((FormattedType)extending).getBaseStruct().getScopedName(null));
				//EhiLogger.debug("extending format "+((FormattedType)extending).getFormat());
				try{
				ft.setExtending(extending);
				}catch(Exception ex){
					reportError(ex,line);
				}
			}
			validateFormattedConst(ft,min2.getText(),min2.getLine());
			validateFormattedConst(ft,max2.getText(),max2.getLine());
			ft.setMinimum(min2.getText());
			ft.setMaximum(max2.getText());
		}
	)
	| ( "FORMAT" domain=domainRef[scope] min3:STRING DOTDOT max3:STRING
		{
			line=min3.getLine();
			ft=new FormattedType();
			if(extending!=null){
				try{
				ft.setExtending(extending);
				}catch(Exception ex){
					reportError(ex,line);
				}
			}
			ft.setBaseDomain(domain);
			try{
				if(!ft.isValueInRange(min3.getText())){
					reportError(formatMessage("err_formattedType_valueOutOfRange",min3.getText()),min3.getLine());
				}
			}catch(NumberFormatException ex){
				reportError(formatMessage("err_formattedType_illegalFormat",min3.getText()),min3.getLine());
			}
			try{
				if(!ft.isValueInRange(max3.getText())){
					reportError(formatMessage("err_formattedType_valueOutOfRange",max3.getText()),max3.getLine());
				}
			}catch(NumberFormatException ex){
				reportError(formatMessage("err_formattedType_illegalFormat",max3.getText()),max3.getLine());
			}
			ft.setMinimum(min3.getText());
			ft.setMaximum(max3.getText());
		}
	))
	;

protected baseAttrRef[FormattedType ft,Table scope]
	returns [FormattedTypeBaseAttrRef baseAttr]
	{
		baseAttr=null;
		Domain domain=null;
		int intPos=0;
	}
:
 
 (NAME SLASH NAME) => (name:NAME SLASH domain=domainRef[scope] )
 	{
			AttributeDef attrdef=findAttribute(scope,name.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_formattedType_unknownAttr", name.getText(),
				scope.toString()), name.getLine());
			}
			Type type=attrdef.getDomain();
			if(!(type instanceof CompositionType)){
				reportError (formatMessage ("err_formattedType_StructAttrRequired", name.getText(),
				scope.toString()), name.getLine());
			}
			if(((CompositionType)type).getCardinality().getMaximum()>1){
				reportError (formatMessage ("err_formattedType_maxCard1", name.getText(),
				scope.toString()), name.getLine());
			}
			baseAttr=new FormattedTypeBaseAttrRef(ft,attrdef,domain);
	}
 | (name2:NAME ( SLASH intPos=posInteger )? )
	{ // TODO formattedTyp
			AttributeDef attrdef=findAttribute(scope,name2.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_formattedType_unknownAttr", name2.getText(),
				scope.toString()), name2.getLine());
			}
			Type type=attrdef.getDomainResolvingAliases();
			if(!(type instanceof NumericType)){
				reportError (formatMessage ("err_formattedType_NumericAttrRequired", name2.getText(),
				scope.toString()), name2.getLine());
			}
			baseAttr=new FormattedTypeBaseAttrRef(ft,attrdef,intPos);
	}
 ;

 
protected formattedConst
	returns [Constant c]
	{
		c=null;
	}
: c=textConst
;

protected dateTimeType [Container scope, Type extending]
  returns [CoordType ct]
{
	ct=null;
}
: "DATE" | "TIMEOFDAY" | "DATETIME" // FIXME24
;
	
protected coordinateType [Container scope, Type extending,boolean isGeneric]
  returns [AbstractCoordType ct]
{
  NumericalType nt1 = null;
  NumericalType nt2 = null;
  NumericalType nt3 = null;
  int[] rots = null;
  ct = null;
  boolean multiCoord=false;
  NumericalType ext_nt1 = null;
  NumericalType ext_nt2 = null;
  NumericalType ext_nt3 = null;
  String refsys=null;

  if (extending instanceof CoordType)
  {
    NumericalType[] ext_dimensions = ((CoordType) extending).getDimensions ();
    if (ext_dimensions.length >= 1)
      ext_nt1 = ext_dimensions [0];
    if (ext_dimensions.length >= 2)
      ext_nt2 = ext_dimensions [1];
    if (ext_dimensions.length >= 3)
      ext_nt3 = ext_dimensions [2];
  }
}
  : (coord:"COORD" | "MULTICOORD" {multiCoord=true;})
    
       (
	    nt1 = numericType [scope, ext_nt1,true]
	    ( COMMA
	      nt2 = numericType [scope, ext_nt2,true]
	      (
		COMMA
		( rots=rotationDef
		| (nt3=numericType [scope, ext_nt3,true] (COMMA rots=rotationDef)?)
		)
	      )?
	     ("REFSYS" refsysT:STRING {refsys=refsysT.getText();})? 
	    )?
	 )
    {
      NumericalType[] nts;

      if (nt3 != null)
        nts = new NumericalType[] { nt1, nt2, nt3 };
      else if (nt2 != null)
        nts = new NumericalType[] { nt1, nt2 };
      else
        nts = new NumericalType[] { nt1 };

      try {
        if (rots == null){
          if(multiCoord){
	          ct = new MultiCoordType (nts);
          }else{
	          ct = new CoordType (nts);
          }
        }else{
          if(multiCoord){
	          ct = new MultiCoordType (nts, rots[0], rots[1]);
          }else{
	          ct = new CoordType (nts, rots[0], rots[1]);
          }
        }
        ct.setGeneric(isGeneric);
        ct.setCrs(refsys);
      } catch (Exception ex) {
        reportError (ex, coord.getLine());
      }
    }
	 
    
  ;

protected rotationDef
	returns [int[] rots]
	{
	  rots = null;
	  int nullAxis;
	  int piHalfAxis;
	}
	: rot:"ROTATION"
    nullAxis = posInteger
    POINTSTO
    piHalfAxis = posInteger
    {
      if ((nullAxis == 0) || (piHalfAxis == 0))
        reportError (rsrc.getString ("err_axisNumber_zero"), rot.getLine ());
      else
        rots = new int[] { nullAxis, piHalfAxis };
    }
  ;

protected contextDefs[Container container]
	{
	  ContextDefs defs=null;
	  int nameIdx=1;
	}
	:	"CONTEXT"  n:NAME EQUALS 
		{defs=new ContextDefs(n.getText());
		}
		( 
		contextDef[container,defs,nameIdx++] 
		)*
		{
		container.add(defs);
		}
	;
protected contextDef[Container container,ContextDefs defs,int nameIdx]
	{
	  String ilidoc=null;
	  Settings metaValues=null;
	  Domain genericCoordDef=null;
	  Domain concreteCoordDef=null;
	  ArrayList<Domain> concreteCoordDefs=new ArrayList<Domain>();
	}
	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
		genericCoordDef=domainRef[container]
		eq:EQUALS
		concreteCoordDef=domainRef[container]
			{ concreteCoordDefs.add(concreteCoordDef);
			}
		( "OR" concreteCoordDef=domainRef[container] 
			{ concreteCoordDefs.add(concreteCoordDef);
			}
		)*
		{
			ContextDef def=new ContextDef(nameIdx,genericCoordDef,concreteCoordDefs.toArray(new Domain[concreteCoordDefs.size()]));
			def.setDocumentation(ilidoc);
			def.setMetaValues(metaValues);
			defs.add(def);
		}
		SEMI
	;
  
protected oIDType[Container scope,Type extending]
	returns[OIDType bt]
	{
		bt=null;
		Type t;
		NumericType nt;
		Type extendingOidType=null;
		if(extending!=null && extending instanceof OIDType){
			extendingOidType=((OIDType)extending).getOIDType();
		}
	}
	: "OID"
	( "ANY" { bt=new AnyOIDType(); }
	| t=textType[extendingOidType] { bt=new TextOIDType(t); }
	| nt=numericType[scope,extendingOidType,false] { bt=new NumericOIDType(nt); }
	)
	;

protected blackboxType  [Container scope, Type extending]
  returns [BlackboxType sutype]
{
sutype=null;
}
: "BLACKBOX" 
	( "XML" {sutype=new BlackboxType();sutype.setKind(BlackboxType.eXML);} 
	| "BINARY" {sutype=new BlackboxType();sutype.setKind(BlackboxType.eBINARY);}
	)
;

protected classType[Container scope,Type extending]
	returns[ClassType bt]
	{
		bt=new ClassType();
		Viewable restrictedTo;
	}
	: ( "CLASS" 
		( "RESTRICTION" LPAREN restrictedTo=classOrAssociationRef[scope]
			{ bt.addRestrictedTo(restrictedTo); }
			(SEMI restrictedTo=classOrAssociationRef[scope]
				{ bt.addRestrictedTo(restrictedTo); }
			)*
			RPAREN
		)?
	)
	| ( "STRUCTURE" {bt.setStructure(true);}
		( "RESTRICTION" LPAREN restrictedTo=structureRef[scope]
				{ bt.addRestrictedTo(restrictedTo); }
			(SEMI restrictedTo=structureRef[scope]
				{ bt.addRestrictedTo(restrictedTo); }
			)*
			RPAREN
		)?
	)
	;

protected attributePathType  [Container scope, Type extending,ArrayList formalArgs]
  returns [AttributePathType sutype]
{
	sutype=null;
	ObjectPath attrRestr=null;
	Type type=null;
	ArrayList typev=new ArrayList();
	FormalArgument argRestr=null;
}
: "ATTRIBUTE" 
	( of:"OF" (
		({scope instanceof Viewable}? attrRestr=attributePath[(Viewable)scope,scope]
		{
			Type attrType=null;
			try{
			  attrType=attrRestr.getType();
			}catch(Exception ex){
				reportError(ex.getLocalizedMessage(),of.getLine());
				attrRestr=null;
			}
			if(attrType!=null && !(attrType instanceof ClassType)){
				reportError(formatMessage ("err_objectPath_targetAttrNotClassType",attrRestr.toString()), of.getLine());
				attrRestr=null;
			}
		})?
		({formalArgs!=null}? AT n:NAME
		{	
			  for(int argi=0;argi<formalArgs.size();argi++){
			  	FormalArgument arg=(FormalArgument)formalArgs.get(argi);
				  if(arg.getName().equals(n.getText())){
					 argRestr=arg;
					 break;
				  }
			  }
			if(argRestr==null){
				reportError (formatMessage ("err_function_noSuchArgWoScope", n.getText()), n.getLine());
			}
		}
		)?
		)
	)?
	( "RESTRICTION" lp:LPAREN type=attrTypeDef[scope,/* alias ok */ true, null,
                     lp.getLine(),null]
		{ 
			typev.add(type);
		}
		( semi:SEMI type=attrTypeDef[scope,/* alias ok */ true, null,
                     semi.getLine(),null]
		     {
		     	typev.add(type);
		     }
		)*
		RPAREN
	)?
	{ 
		sutype=new AttributePathType();
		if(attrRestr!=null){
			sutype.setAttrRestriction(attrRestr);
		}else if(argRestr!=null){
			sutype.setArgRestriction(argRestr);
		}
		if(typev.size()>0){
			sutype.setTypeRestriction((Type[])typev.toArray(new Type[typev.size()]));
		}

	}
;

classConst[Container scope] 
	returns [Constant c]
	{
	c=null;
	Viewable ref=null;
	}
: 
GREATER ref=viewableRef[scope] 
	{
	c=new Constant.Class(ref);
	}
;

attributeConst[Container scope] 
	returns [Constant c]
	{
	c=null;
	Viewable ref=null;
	AttributeDef attr=null;
	}
:
	GREATERGREATER ((xyRef POINTSTO)=> ref=viewableRef[scope] POINTSTO )? n:NAME 
	{ 
	if(ref!=null){
		scope=ref;
	}
	// find attribute in scope
	attr=findAttribute(scope,n.getText());
	if(attr==null){
		reportError (formatMessage ("err_attributePathConst_unknownAttr", n.getText(),
		scope.toString()), n.getLine());
	}
	
	c=new Constant.AttributePath(attr);
	}
	;
	
protected lineType [Container scope, Type extending]
  returns [LineType lt]
{
  boolean directed = false;
  boolean withStraights = false;
  boolean withArcs = false;
  LineForm[] theLineForms = null;
  PrecisionDecimal theMaxOverlap = null;
  Domain controlPointDomain = null;
  int line = 0;
  lt = null;
}
  : ( ( "DIRECTED" { directed = true; } )? (pl:"POLYLINE" {line = pl.getLine();} | mpl:"MULTIPOLYLINE" {line = mpl.getLine();})
      {
      	if(pl!=null){
		lt = new PolylineType ();
		try {
		  ((PolylineType) lt).setDirected (directed);
		} catch (Exception ex) {
		  reportError (ex, line);
		}
      	}else{
		lt = new MultiPolylineType ();
		try {
		  ((MultiPolylineType) lt).setDirected (directed);
		} catch (Exception ex) {
		  reportError (ex, line);
		}
      	}
      }
    | surf:"SURFACE" { line = surf.getLine(); lt = new SurfaceType(); }
    |  msurf:"MULTISURFACE" { line = msurf.getLine(); lt = new MultiSurfaceType(); }
    | area:"AREA" {line = area.getLine(); lt = new AreaType(); }
    |  marea:"MULTIAREA" {line = marea.getLine(); lt = new MultiAreaType(); }
    )

    ( theLineForms = lineForm[scope] )?
    ( "VERTEX" controlPointDomain = domainRef[scope] )?
    ( "WITHOUT" "OVERLAPS" GREATER theMaxOverlap = decimal )?

    {

      try {
        if (theLineForms != null)
          lt.setLineForms (theLineForms);
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        if(controlPointDomain!=null){
          lt.setControlPointDomain (controlPointDomain);
        }
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        if(theMaxOverlap!=null){
          lt.setMaxOverlap (theMaxOverlap);
        }
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }

    {
      if(extending!=null){
        try{
          lt.setExtending(extending);
        }catch(Exception ex){
          reportError (ex, line);
        }
      }
    }
  ;

protected lineForm [Container scope]
  returns [LineForm[] lf]
{
  List ll = null;
  LineForm linForm = null;
  lf = new LineForm[0];
}
  : "WITH"
    LPAREN
    {
      ll = new LinkedList();
    }

    linForm = lineFormType [scope] { if (linForm != null) ll.add(linForm); }
    ( COMMA
      linForm = lineFormType [scope] { if (linForm != null) ll.add(linForm); }
    )*
    RPAREN
    {
      lf = (LineForm[]) ll.toArray (lf);
    }
  ;

protected lineFormType [Container scope]
  returns [LineForm lf]
{
  lf = null;
}
  : "ARCS"
    {
      lf = modelInterlis.ARCS;
    }

  | "STRAIGHTS"
    {
      lf = modelInterlis.STRAIGHTS;
    }

  | nam:NAME
    {
    	/* TODO als Verweis ist hier auch ein qualifizierter Name zulaessig
	*/
      Model scopeModel = (Model) scope.getContainerOrSame (Model.class);
      lf = (LineForm) scopeModel.getRealElement (LineForm.class, nam.getText());
      if (lf == null)
        lf = (LineForm) modelInterlis.getRealElement (LineForm.class, nam.getText());

      if (lf == null)
        reportError (formatMessage (
          "err_lineForm_unknownName", nam.getText(), scopeModel.toString()),
          nam.getLine());
    }
  ;

protected lineFormTypeDef[Model model]
{
  String explString = null;
	  String ilidoc=null;
	  Settings metaValues=null;
}
  : 
  	linform:"LINE" "FORM"
    ( { ilidoc=getIliDoc();metaValues=getMetaValues();}
      nam:NAME
      COLON lineStructure:NAME
      SEMI

      {
        LineForm lf = new LineForm ();
        try {
          lf.setName (nam.getText ());
	  lf.setDocumentation(ilidoc);
	  lf.setMetaValues(metaValues);
	  Table seg=(Table)model.getImportedElement(Table.class,lineStructure.getText());
	  if(seg==null){
	          reportError (formatMessage ("err_noSuchTable", lineStructure.getText(),
                                    model.toString()), lineStructure.getLine());
	  }
          lf.setSegmentStructure(seg);
          model.add (lf);
        } catch (Exception ex) {
          reportError (ex, nam.getLine());
        }
      }
    )*
  ;

protected unitDefs[Container scope]
	:	"UNIT" ( unitDef[scope] )*
	;


protected unitDef[Container scope]
{
  int mods = 0;
  Unit extending = null;
  Unit u = null;
  boolean _abstract = false;
  int unitSourceLine=0;
  String docName = null, idName = null;
	  String ilidoc=null;
	  Settings metaValues=null;
}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
		n:NAME { docName = idName = n.getText(); unitSourceLine=n.getLine();}
			(	LBRACE idn:NAME RBRACE  {idName = idn.getText ();}
			|	LPAREN "ABSTRACT" RPAREN {_abstract=true;}
			|
			)
		(
			ext:"EXTENDS"
			extending = unitRef[scope]
		)?
    (
      EQUALS
      (
        u = derivedUnit[scope, idName, docName, _abstract]
        {
          if (extending != null)
            reportError (rsrc.getString ("err_derivedUnit_ext"), ext.getLine());
        }
      | u = composedUnit[scope, idName, docName, _abstract]
      )
      SEMI

    | SEMI
      {
        u = new BaseUnit();
        try {
          u.setName (idName);
          u.setDocName (docName);
        } catch (Exception ex) {
          reportError (ex, n.getLine());
        }

        try {
          u.setAbstract (_abstract);
        } catch (Exception ex) {
          reportError (ex, n.getLine());
        }
      }
    )

    {
      try {
        u.setSourceLine(unitSourceLine);
      	u.setDocumentation(ilidoc);
	u.setMetaValues(metaValues);
        scope.add(u);
	if(extending!=null){
        	u.setExtending(extending);
	}
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
    }
  ;


protected derivedUnit [Container scope, String idName, String docName, boolean _abstract]
  returns [Unit u]
{
  u = null;
  Unit baseUnit = null;
  List factors = null;
  int line = 0;
  char compOp = '*';
  PrecisionDecimal fac = new PrecisionDecimal("1");
}
  : f:"FUNCTION" exp:EXPLANATION LBRACE baseUnit=unitRef[scope] RBRACE
    {
      u = new FunctionallyDerivedUnit ();

      try {
        u.setName (idName);
        u.setDocName (docName);
      } catch (Exception ex) {
        reportError(ex, f.getLine());
      }

      try {
        ((FunctionallyDerivedUnit) u).setExplanation (exp.getText());
      } catch (Exception ex) {
        reportError(ex, exp.getLine());
      }

      try {
        u.setAbstract (_abstract);
      } catch (Exception ex) {
        reportError(ex, f.getLine());
      }

      try {
        ((FunctionallyDerivedUnit) u).setExtending(baseUnit);
      } catch (Exception ex) {
        reportError(ex, f.getLine());
      }
    }

  | fac = decConst
    {
      u = new NumericallyDerivedUnit();
      factors = new LinkedList();

      try {
        factors.add (new NumericallyDerivedUnit.Factor ('*', fac));
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }

    (
      ( st:STAR { compOp = '*'; line=st.getLine(); }
      | sl:SLASH { compOp = '/'; line=sl.getLine(); }
      )

      fac = decConst

      {
        try {
          factors.add (new NumericallyDerivedUnit.Factor (compOp, fac));
        } catch (Exception ex) {
          reportError (ex, line);
        }
      }

    )*

    LBRACE
    baseUnit=unitRef[scope]
    RBRACE

    {
      NumericallyDerivedUnit ndu = (NumericallyDerivedUnit) u;
      try {
        u.setName (idName);
        u.setDocName (docName);
      } catch (Exception ex) {
        reportError(ex, line);
      }

      try {
        u.setAbstract (_abstract);
      } catch (Exception ex) {
        reportError(ex, line);
      }

      try {
        ndu.setExtending (baseUnit);
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        ndu.setConversionFactors (
          (NumericallyDerivedUnit.Factor[]) factors.toArray (
                            new NumericallyDerivedUnit.Factor[factors.size()]));
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }


  | LBRACE
    baseUnit=unitRef[scope]
    RBRACE

    {
      u = new NumericallyDerivedUnit();
      NumericallyDerivedUnit ndu = (NumericallyDerivedUnit) u;
      try {
        u.setName (idName);
        u.setDocName (docName);
      } catch (Exception ex) {
        reportError(ex, line);
      }

      try {
        u.setAbstract (_abstract);
      } catch (Exception ex) {
        reportError(ex, line);
      }

      try {
        ndu.setExtending (baseUnit);
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        ndu.setConversionFactors (
          new NumericallyDerivedUnit.Factor[] {
            new NumericallyDerivedUnit.Factor ('*', new PrecisionDecimal("1"))
          });
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }
  ;


protected composedUnit [Container scope, String idName, String docName, boolean _abstract]
  returns [ComposedUnit u]
{
  u = null;
  List composed = null;

  int line = 0;
  char compOp = '*';
  Unit compUnit = null;
}
  : lbrac:LPAREN
    compUnit=unitRef[scope]
    {
      u = new ComposedUnit();
      composed = new LinkedList();

      try {
        composed.add (new ComposedUnit.Composed ('*', compUnit));
        u.setName (idName);
        u.setDocName (docName);
      } catch (Exception ex) {
        reportError(ex, lbrac.getLine());
      }

      try {
        u.setAbstract (_abstract);
      } catch (Exception ex) {
        reportError(ex, lbrac.getLine());
      }
    }

    (
      ( st:STAR { compOp = '*'; line=st.getLine(); }
      | sl:SLASH { compOp = '/'; line=sl.getLine(); }
      )

      compUnit = unitRef[scope]

      {
        try {
          composed.add (new ComposedUnit.Composed (compOp, compUnit));
        } catch (Exception ex) {
          reportError (ex, line);
        }
      }

    )*
    RPAREN

    {
      try {
        u.setComposedUnits (
          (ComposedUnit.Composed[]) composed.toArray (
                            new ComposedUnit.Composed[composed.size()]));
      } catch (Exception ex) {
        reportError (ex, lbrac.getLine());
      }
    }
  ;

protected unitRef [Container scope]
  returns [Unit u]
{
  List      nams = new LinkedList();
  u = null;
  int lin = 0;
}
    : lin = names2[nams]
    {
      Model model;
      Topic topic;

      String   modelName, topicName, unitName;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (Topic) scope.getContainerOrSame(Topic.class);
        unitName = (String) nams.get(0);
        break;

      case 2:
        modelName = (String) nams.get(0);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = null;
        unitName = (String) nams.get(1);
        break;

      case 3:
        modelName = (String) nams.get(0);
        topicName = (String) nams.get(1);
        unitName = (String) nams.get(2);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = resolveOrFixTopicName(model, topicName, lin);
        break;

      default:
        reportError(rsrc.getString("err_weirdUnitRef"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        topic = null;
        unitName = (String) nams.get(nams.size() - 1);
        break;
      }

      u = null;
      if (topic != null)
        u = (Unit) topic.getRealElement(Unit.class, unitName);

      if (u == null)
        u = (Unit) model.getRealElement(Unit.class, unitName);

      if ((u == null) && (nams.size() == 1))
        u = (Unit) model.getImportedElement (Unit.class, unitName);

      if (u == null)
      {
        /* A presumably common error is to use the docName instead of the name,
           e.g. by referring to a unit by "meters" instead of using "m". */
        Iterator it;

        if (topic != null)
          it = topic.iterator();
        else
          it = model.iterator();

        while (it.hasNext()) {
          Object o = it.next();
          if ((o instanceof Unit) && unitName.equals(((Unit) o).getDocName()))
          {
            u = (Unit) o;
            reportError(
              formatMessage("err_unitRefByDocName", unitName,
                            model.toString(), u.getScopedName(scope)),
              lin);
            break;
          }
        }
      }

      if (u == null)
      {
        if (topic != null)
          reportError (formatMessage ("err_noSuchUnitInModelOrTopic",
                                      unitName,
                                      topic.toString(),
                                      model.toString()),
                        lin);
        else
          reportError (formatMessage ("err_noSuchUnit", unitName,
                                      model.toString()),
                        lin);

        try {
          u = new BaseUnit();
          u.setName(unitName);
          if (topic != null)
            topic.add(u);
          else
            model.add(u);
        } catch (Exception ex) {
          panic();
        }
      }
    }
  ;

protected metaDataBasketDef[Container scope]
    {
    int mods;
    boolean sign=false;
    MetaDataUseDef def=null;
    MetaDataUseDef base=null;
    Topic topic = null;
    Table aclass=null;
	  String ilidoc=null;
	  Settings metaValues=null;
    }
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
		(	"SIGN" {sign=true;}
		|	"REFSYSTEM" {sign=false;}
		)
		"BASKET" n:NAME mods=properties[ch.interlis.ili2c.metamodel.Properties.eFINAL]
		{
			def=new MetaDataUseDef();
			def.setDocumentation(ilidoc);
			def.setMetaValues(metaValues);
			def.setSignData(sign);
			def.setName(n.getText());
			try{
			  def.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL)!=0);
			} catch (Exception ex) {
		            reportError(ex, n.getLine());
		        }
		}
		(	ext:"EXTENDS" base=metaDataBasketRef[scope]
                    {
			try{
		      def.setExtending(base);
			} catch (Exception ex) {
		            reportError(ex, ext.getLine());
		        }
                    }
		)?
		TILDE topic=topicRef[scope]
		{
			def.setTopic(topic);
			scope.add(def);
			// ili2.3
			// - add proxies from contents of ili-file
			// (no data container)
		}
		(
		"OBJECTS" "OF" classNameTok:NAME COLON
			{ ilidoc=getIliDoc(); metaValues=getMetaValues();
			}
			objNameTok1:NAME 
				{
					// find class in topic
					String className=classNameTok.getText();
					aclass = (Table) topic.getRealElement(Table.class, className);
					if (aclass == null) {
						reportError (formatMessage ("err_noSuchTable", className,
							topic.toString()), classNameTok.getLine());
					}else{
						// check if aclass is an extension of INTERLIS.METAOBJECT
						if(!aclass.isExtending(td.INTERLIS.METAOBJECT)){
						reportError (formatMessage ("err_class_superNonMetaObject", className
							), classNameTok.getLine());
						}
					}
					// add object to basket
					if(aclass!=null){
						String objName=objNameTok1.getText();
						MetaObject mo=new MetaObject(objName,aclass);
						mo.setDocumentation(ilidoc);
						mo.setMetaValues(metaValues);
						def.add(mo);
					}
				}
			(COMMA 
				{ ilidoc=getIliDoc(); metaValues=getMetaValues();
				}
			objNameTok2:NAME
				{
					// add object to basket
					if(aclass!=null){
						String objName=objNameTok2.getText();
						MetaObject mo=new MetaObject(objName,aclass);
						mo.setDocumentation(ilidoc);
						mo.setMetaValues(metaValues);
						def.add(mo);
					}
				}
			)* 
		)*
		SEMI
	;

protected metaDataBasketRef[Container scope]
	returns[MetaDataUseDef ref]
	{ ref=null;
	int lin=0;
	LinkedList nams=new LinkedList();
	}
	:
     lin = names2[nams]
    {
      Model model;
      Topic topic;

      String   modelName, topicName, basketName;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (Topic) scope.getContainerOrSame(Topic.class);
        basketName = (String) nams.get(0);
        break;

      case 2:
        modelName = (String) nams.get(0);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = null;
        basketName = (String) nams.get(1);
        break;

      case 3:
        modelName = (String) nams.get(0);
        topicName = (String) nams.get(1);
        basketName = (String) nams.get(2);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = resolveOrFixTopicName(model, topicName, lin);
        break;

      default:
        reportError(rsrc.getString("err_weirdMetaDataUseRef"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        topic = null;
        basketName = (String) nams.get(nams.size() - 1);
        break;
      }

      ref = null;
      if (topic != null)
        ref = (MetaDataUseDef) topic.getRealElement(MetaDataUseDef.class, basketName);

      if (ref == null)
        ref = (MetaDataUseDef) model.getRealElement(MetaDataUseDef.class, basketName);

      if ((ref == null) && (nams.size() == 1))
        ref = (MetaDataUseDef) model.getImportedElement (MetaDataUseDef.class, basketName);

      if (ref == null)
      {
        if (topic != null)
          reportError (formatMessage ("err_noSuchMetaDataUseDefInModelOrTopic",
                                      basketName,
                                      topic.toString(),
                                      model.toString()),
                        lin);
        else
          reportError (formatMessage ("err_noSuchMetaDataUseDef", basketName,
                                      model.toString()),
                        lin);

        try {
          ref = new MetaDataUseDef();
          ref.setName(basketName);
          if (topic != null)
            topic.add(ref);
          else
            model.add(ref);
        } catch (Exception ex) {
          panic();
        }
      }
    }
	;

protected metaObjectRef[Container scope,Table polymorphicTo]
	returns[MetaObject referred]
	{ referred=null;
	int lin=0;
	LinkedList nams=new LinkedList();
	}
	:
     lin = names2[nams]
    {
      Model model;
      Topic topic;
      MetaDataUseDef ref;

      String   modelName, topicName, basketName,objectName;

      switch (nams.size()) {
      case 1:
		ref=null;
	    model = (Model) scope.getContainerOrSame(Model.class);
	    topic = (Topic) scope.getContainerOrSame(Topic.class);
		MetaDataUseDef basket=null;
		if(topic!=null){
			Iterator i=topic.iterator();
			while(i.hasNext()){
				Object ele=i.next();
				if(ele instanceof MetaDataUseDef){
					if(ref==null){
						ref=(MetaDataUseDef)ele;
					}else{
						// multiple MetaDataUseDef and unqualified basketref
				        reportError(rsrc.getString("err_metaObject_scopeRequired"), lin);
					}
				}
			}
		}
			Iterator i=model.iterator();
			while(i.hasNext()){
				Object ele=i.next();
				if(ele instanceof MetaDataUseDef){
					if(ref==null){
						ref=(MetaDataUseDef)ele;
					}else{
						// multiple MetaDataUseDef and unqualified basketref
				        reportError(rsrc.getString("err_metaObject_scopeRequired"), lin);
					}
				}
			}
		if(ref==null){
			// no MetaDataUseDef at all
	        reportError(rsrc.getString("err_noMetaDataUseDef"), lin);
		}
        objectName = (String) nams.get(0);
		referred=resolveMetaObject(ref,polymorphicTo,objectName,lin);
        break;

      case 2:
        basketName = (String) nams.get(0);
        ref = resolveOrFixBasketName(scope, basketName, lin);
        objectName = (String) nams.get(1);
	referred=resolveMetaObject(ref,polymorphicTo,objectName,lin);
        break;

      case 3:
        modelName = (String) nams.get(0);
        basketName = (String) nams.get(1);
        objectName = (String) nams.get(2);
        model = resolveOrFixModelName(scope, modelName, lin);
        ref = resolveOrFixBasketName(model, basketName, lin);
	referred=resolveMetaObject(ref,polymorphicTo,objectName,lin);
        break;

      case 4:
        modelName = (String) nams.get(0);
        topicName = (String) nams.get(1);
        basketName = (String) nams.get(2);
        objectName = (String) nams.get(3);
        model = resolveOrFixModelName(scope, modelName, lin);
        topic = resolveOrFixTopicName(model, topicName, lin);
        ref = resolveOrFixBasketName(topic, basketName, lin);
	referred=resolveMetaObject(ref,polymorphicTo,objectName,lin);
        break;

      default:
        reportError(rsrc.getString("err_weirdMetaObjectRef"), lin);
        break;
      }

    }
	;


protected parameterDef[Table container]
	{
	int mods = 0;
	Type type = null;
	boolean mandatory = false;
	boolean declaredExtended = false;
	Parameter overriding = null;
	Type overridingDomain = null;
	Table referred = null;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
		n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		COLON
    {
    	/* TODO handle ABSTRACT and FINAL */
      declaredExtended = (mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0;

      overriding =  (Parameter) container.getRealElement (
        Parameter.class, n.getText());
      if (overriding != null)
        overridingDomain = overriding.getType();

      if ((overriding == null) && declaredExtended)
      {
        reportError (formatMessage ("err_parameter_nothingToExtend",
                                    n.getText(),
                                    container.toString()),
                     n.getLine());
      }

      if ((overriding != null)
          && (container == overriding.getContainer (Viewable.class)))
      {
        reportError (formatMessage ("err_parameter_nameInSameContainer",
                                    container.toString(), n.getText()),
                     n.getLine());
      }
      else if ((overriding != null) && !declaredExtended)
      {
        reportError (formatMessage ("err_parameter_extendedWithoutDecl",
                                    n.getText(), container.toString(),
                                    overriding.toString()),
                     n.getLine());
      }
    }
		(	
		("METAOBJECT")=>	pt:"METAOBJECT"
			( "OF" referred=classRef[container]
			| /* empty */
				{
					referred=container;
				}
			)
				{
					MetaobjectType reference = new MetaobjectType();

					try {
						reference.setReferred (referred);
					} catch (Exception ex) {
						reportError (ex, pt.getLine());
					}
					type = reference;
				}
		| type=attrTypeDef[container,true,overridingDomain,n.getLine(),null]
		)
    {
      Parameter p = new Parameter();

      try {
        p.setName (n.getText());
	p.setDocumentation(ilidoc);
	p.setMetaValues(metaValues);
        container.add(p);
        p.setType (type);
        p.setExtending (overriding);
        p.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
        p.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
    }

    SEMI
  ;

protected runTimeParameterDef[Container scope]
	{
		Type domain;
		GraphicParameterDef def=null;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	: 
	"PARAMETER"
        ( { ilidoc=getIliDoc();metaValues=getMetaValues();}
	  n:NAME COLON
                {
			def=new GraphicParameterDef();
			def.setSourceLine(n.getLine());
			def.setName(n.getText());
			def.setDocumentation(ilidoc);
			def.setMetaValues(metaValues);
                }
            domain=attrTypeDef[scope,true,null,n.getLine(),null]
                {
                  def.setDomain(domain);
                  scope.add(def);
                }
	    SEMI
	)*
	;

protected constraintDef[Viewable constrained,Container context]
	returns [Constraint constr]
	{
	constr = null;
	  String ilidoc=null;
	  Settings metaValues=null;
	}

	: { ilidoc=getIliDoc();metaValues=getMetaValues();}
	(
	constr=mandatoryConstraint[constrained,context]
	| constr=plausibilityConstraint[constrained,context]
	| constr=existenceConstraint[constrained,context]
	| constr=uniquenessConstraint[constrained,context]
	| constr=setConstraint[constrained,context]
	)
	{
		if(constr!=null){
			constr.setDocumentation(ilidoc);
			constr.setMetaValues(metaValues);
		}
	}
	;

protected mandatoryConstraint [Viewable v,Container context]
  returns [MandatoryConstraint constr]
{
  Evaluable condition = null;
  constr = null;
}
  : mand:"MANDATORY"
    "CONSTRAINT" ((NAME COLON) =>n:NAME COLON )?
    condition = expression [v, /* expectedType */ predefinedBooleanType,context]
    SEMI

    {
      try {
        constr = new MandatoryConstraint();
	if(n!=null){constr.setName(n.getText());}
      	if(!condition.isDirty() && !condition.isLogical()){
			reportError (formatMessage ("err_expr_noLogical",(String)null),
                     mand.getLine());
            constr.setDirty(true);
      	}
        constr.setCondition(condition);
      } catch (Exception ex) {
        reportError(ex, mand.getLine());
      }
    }
  ;


protected plausibilityConstraint [Viewable v,Container context]
  returns [PlausibilityConstraint constr]
{
  PrecisionDecimal       percentage;
  int                    direction = 0;
  Evaluable              condition = null;
  constr = null;
}
  : tok:"CONSTRAINT" ((NAME COLON) =>n:NAME COLON )?

    ( LESSEQUAL { direction = PlausibilityConstraint.DIRECTION_AT_MOST; }
    | GREATEREQUAL { direction = PlausibilityConstraint.DIRECTION_AT_LEAST; }
    )

    percentage = decimal PERCENT
    condition = expression [v, /* expectedType */ predefinedBooleanType,context]
    SEMI
    {
      try {
        constr = new PlausibilityConstraint();
	if(n!=null){constr.setName(n.getText());}
      	if(!condition.isDirty() && !condition.isLogical()){
			reportError (formatMessage ("err_expr_noLogical",(String)null),
                     tok.getLine());
            constr.setDirty(true);
      	}
        constr.setDirection(direction);
        constr.setCondition(condition);
        constr.setPercentage(percentage.doubleValue());
      } catch (Exception ex) {
        reportError (ex, tok.getLine());
      }
    }
  ;


protected existenceConstraint[Viewable v,Container context]
	returns [ExistenceConstraint constr]
	{
		ObjectPath attr;
		Viewable ref;
		constr=new ExistenceConstraint();
		ObjectPath attrRef=null;
	}
	: e:"EXISTENCE" "CONSTRAINT" ((NAME COLON) => n:NAME COLON )?
	attr=attributePath[v,context]
		{
			try{
				if(n!=null){constr.setName(n.getText());}
			} catch (Exception ex) {
				reportError(ex, e.getLine());
			}
			constr.setRestrictedAttribute(attr);
		}
	"REQUIRED" "IN" ref=viewableRefDepReq[v] COLON attrRef=attributePath[ref,context]
		{
			constr.addRequiredIn(attrRef); 
		}
	( "OR" ref=viewableRefDepReq[v] COLON attrRef=attributePath[ref,context]
		{
			constr.addRequiredIn(attrRef); 
		}
	)*
	SEMI

	;


protected uniquenessConstraint[Viewable v,Container context]
	returns [UniquenessConstraint constr]
  	{
	Evaluable preCond=null;
		constr=new UniquenessConstraint();
	}
	: u:"UNIQUE" ( (LPAREN "BASKET") => LPAREN "BASKET" RPAREN )? ((NAME COLON) => n:NAME COLON )?
	{
		try{
			if(n!=null){constr.setName(n.getText());}
		} catch (Exception ex) {
			reportError(ex, u.getLine());
		}
	}
  	( "WHERE" preCond=expression[v, /* expectedType */ predefinedBooleanType,context] COLON
	)?
	( constr=globalUniqueness[v,context]
	| constr=localUniqueness[v]
	)
	{
		if(preCond!=null){ 
			if(!preCond.isDirty() && !preCond.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 u.getLine());
				constr.setDirty(true);
			}
			constr.setPreCondition(preCond);
		}
		// check that all attrPaths do not point to a struct
		UniqueEl elements=constr.getElements();
		Iterator attri=elements.iteratorAttribute();
		while(attri.hasNext()){
			ObjectPath attr=(ObjectPath)attri.next();
			if(attr.isAttributePath() && attr.getType() instanceof CompositionType){
				reportError(formatMessage ("err_uniqueness_StructNoAllowed",attr.toString()),u.getLine());
			}
		}
	}
	SEMI
	;

protected globalUniqueness[Viewable scope,Container context]
	returns [UniquenessConstraint constr]
  	{
		constr=new UniquenessConstraint();
		UniqueEl elements=null;
	}
	: elements=uniqueEl[scope,context]
	{
		constr.setElements(elements);
	}
	;

protected uniqueEl[Viewable scope,Container context]
	returns[UniqueEl ret]
	{
		ret=null;
		ObjectPath attr=null;
	}
	: attr=objectOrAttributePath[scope,context]
		{ 
			ret=new UniqueEl();
			ret.addAttribute(attr);
		}
	( COMMA attr=objectOrAttributePath[scope,context]
		{
			ret.addAttribute(attr);
		}
	)*
	;

protected localUniqueness[Viewable start]
	returns [UniquenessConstraint constr]
  	{
		constr=new UniquenessConstraint();
		constr.setLocal(true);
		UniqueEl elements=new UniqueEl();
		constr.setElements(elements);
		ObjectPath prefix=null;
		PathEl el;
		LinkedList path=new LinkedList();
		Viewable next=null;
		Viewable localViewable=null;
	}
	: LPAREN "LOCAL" RPAREN n:NAME
		{
			next=start;
			AttributeDef attrdef=findAttribute(start,n.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_localUniqueness_unknownAttr", n.getText(),
				next.toString()), n.getLine());
			}
			if(!(attrdef.getDomainResolvingAliases() instanceof CompositionType)){
				reportError (formatMessage ("err_localUniqueness_unknownStructAttr", n.getText(),
					next.toString()), n.getLine());
			}
			el=new AttributeRef(attrdef);
			path.add(el);
			next=el.getViewable();
		}
	( POINTSTO 
		n2:NAME
		{
			AttributeDef attrdef=findAttribute(next,n2.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_localUniqueness_unknownAttr", n2.getText(),
				next.toString()), n2.getLine());
			}
			if(!(attrdef.getDomainResolvingAliases() instanceof CompositionType)){
				reportError (formatMessage ("err_localUniqueness_unknownStructAttr", n2.getText(),
					next.toString()), n2.getLine());
			}
			el=new AttributeRef(attrdef);
			path.add(el);
			next=el.getViewable();
		}
	)*
		{
			prefix=new ObjectPath(start,(PathEl[])path.toArray(new PathEl[path.size()]));
			constr.setPrefix(prefix);
		}

	COLON
	n3:NAME
		{
			localViewable=next;
			AttributeDef attrdef=findAttribute(localViewable,n3.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_localUniqueness_unknownAttr", n3.getText(),
				localViewable.toString()), n3.getLine());
			}
			AttributeRef[] attrRef=new AttributeRef[1];
			attrRef[0]=new AttributeRef(attrdef);
			elements.addAttribute(new ObjectPath(localViewable,attrRef));
		}
	( COMMA n4:NAME
		{
			AttributeDef attrdef=findAttribute(localViewable,n4.getText());
			if(attrdef==null){
				reportError (formatMessage ("err_localUniqueness_unknownAttr", n4.getText(),
				localViewable.toString()), n4.getLine());
			}
			AttributeRef[] attrRef=new AttributeRef[1];
			attrRef[0]=new AttributeRef(attrdef);
			elements.addAttribute(new ObjectPath(localViewable,attrRef));
		}
	)*
	;

protected setConstraint [Viewable v,Container context]
  returns [SetConstraint constr]
{
	Evaluable preCond=null;
	Evaluable condition=null;
  constr = new SetConstraint();
}
  : tok:"SET" "CONSTRAINT" ( (LPAREN "BASKET") => LPAREN "BASKET" RPAREN )? ((NAME COLON)=> n:NAME COLON )?
  	( "WHERE" preCond=expression[v, /* expectedType */ predefinedBooleanType,context] COLON
		{
			if(!preCond.isDirty() && !preCond.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 tok.getLine());
				constr.setDirty(true);
			}
	        constr.setPreCondition(preCond);
		}
	)?
	condition=expression[v, /* expectedType */ predefinedBooleanType,context]
	SEMI
	{
	  if(v instanceof Table && !((Table)v).isIdentifiable()){
			reportError (formatMessage ("err_constraint_illegalSetInStruct",
				v.getScopedName(null)), tok.getLine());
	  }else{
			if(!condition.isDirty() && !condition.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 tok.getLine());
				constr.setDirty(true);
			}
		try {
			if(n!=null){constr.setName(n.getText());}
			constr.setCondition(condition);
		} catch (Exception ex) {
			reportError(ex, tok.getLine());
		}
	  }
	}
;
protected constraintsDef[Container scope]
	{
		Viewable def;
		Constraint constr;
	}
	:	c:"CONSTRAINTS" "OF" def=viewableRef[scope]
	EQUALS
	{
	  if(scope==def.getContainer() || (scope instanceof Extendable && ((Extendable)scope).isExtending(def.getContainer()))){
	  	// ok
	  }else{
				reportError (formatMessage ("err_constraint_viewref",
				scope.getScopedName(null)), c.getLine());
	  }
	}
	( constr=constraintDef[def,scope]
		{if(constr!=null)def.add(constr);}
	)*
	"END" SEMI
	;

protected expression [Container ns, Type expectedType,Container functionNs]
	returns [Evaluable expr]
	{
	expr = null;
	}
 	: expr=term[ns,expectedType, functionNs]
	;

protected term[Container ns, Type expectedType, Container functionNs]
  returns [Evaluable expr]
{
  expr = null;
  int lineNumber = 0;
  boolean dirty=false;
}
  : {lineNumber=LT(1).getLine();}
  expr=term0 [ns, expectedType, functionNs]
    (
      IMPLIES
      expr = term0 [ns, expectedType, functionNs]
    )?
  ; 
  
protected term0[Container ns, Type expectedType, Container functionNs]
  returns [Evaluable expr]
{
  List disjoined = null;
  expr = null;
  int lineNumber = 0;
  boolean dirty=false;
}
  : {lineNumber=LT(1).getLine();} 
  expr=term1 [ns, expectedType, functionNs]
    {
      disjoined = new LinkedList ();
      if(expr!=null){
	      disjoined.add(expr);
      }
    }

    (
      o:"OR"
      expr = term1 [ns, expectedType, functionNs]
      {
      	if(expr!=null){
			disjoined.add (expr);
			lineNumber = o.getLine();
			if(!dirty && !expr.isDirty() && !expr.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 lineNumber);
				dirty=true;
			}
      	}
      }
    )*

    {
      if (disjoined.size() == 1){
        expr = (Evaluable) disjoined.get(0);
        if(dirty){
          expr.setDirty(dirty);
        }
      }else if (disjoined.size() > 1){
        try {
			expr = (Evaluable) disjoined.get(0);
			if(!dirty && !expr.isDirty() && !expr.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 lineNumber);
				dirty=true;
			}
          expr = new Expression.Disjunction (
            (Evaluable[]) disjoined.toArray (new Evaluable[disjoined.size()]));
            expr.setDirty(dirty);
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }else{
      	expr=null;
      }
    }
  ;


protected term1 [Container ns, Type expectedType,Container functionNs]
  returns [Evaluable expr]
{
  List conjoined = null;
  expr = null;
  int lineNumber = 0;
  boolean dirty=false;
}
  : {lineNumber=LT(1).getLine();}
  expr=term2 [ns, expectedType, functionNs]
    {
      conjoined = new LinkedList ();
      if(expr!=null){
	    conjoined.add(expr);
	  }
    }

    (
      an:"AND"
      expr = term2 [ns, expectedType, functionNs]
      {
      	if(expr!=null){
			conjoined.add (expr);
			lineNumber = an.getLine();
			if(!dirty && !expr.isDirty() && !expr.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 lineNumber);
				dirty=true;
			}
		}
      }
    )*

    {
      if (conjoined.size() == 1){
        expr = (Evaluable) conjoined.get(0);
        if(dirty){
          expr.setDirty(dirty);
        }
      } else if (conjoined.size() > 1){
        try {
        
			expr = (Evaluable) conjoined.get(0);        
			if(!dirty && !expr.isDirty() && !expr.isLogical()){
					reportError (formatMessage ("err_expr_noLogical",(String)null),
							 lineNumber);
					dirty=true;
			}
			expr = new Expression.Conjunction(
            	(Evaluable[]) conjoined.toArray(new Evaluable[conjoined.size()]));
            expr.setDirty(dirty);
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }else{
      	expr=null;
      }
    }
  ;


protected term2 [Container ns, Type expectedType,Container functionNs]
  returns [Evaluable expr]
{
  expr = null;
  Evaluable exprRet = null;
  Evaluable comparedWith = null;
  char op = '=';
  int[] lineNumberPar = null;
}
  : expr = predicate[ns, expectedType,functionNs]
    (
      {
        lineNumberPar = new int[1];
      }

      op = relation[lineNumberPar]
      comparedWith = predicate[ns, expectedType,functionNs]
      {
        try {
          switch (op)
          {
          /* EQUALSEQUALS */
          case '=':
            exprRet = new Expression.Equality (expr, comparedWith);
            validateEqualsArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;

          /* LESSGREATER, BANGEQUALS */
          case '!':
            exprRet = new Expression.Inequality (expr, comparedWith);
            validateEqualsArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;

          /* LESSEQUAL */
          case 'l':
            exprRet = new Expression.LessThanOrEqual (expr, comparedWith);
            validateCompareArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;

          /* GREATEREQUAL */
          case 'g':
            exprRet = new Expression.GreaterThanOrEqual (expr, comparedWith);
            validateCompareArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;

          /* LESS */
          case '<':
            exprRet = new Expression.LessThan (expr, comparedWith);
            validateCompareArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;

          /* GREATER */
          case '>':
            exprRet = new Expression.GreaterThan (expr, comparedWith);
            validateCompareArgumentTypes(exprRet,expr,comparedWith,lineNumberPar[0]);
            break;
          }
          expr=exprRet;
        } catch (Exception ex) {
          reportError (ex, lineNumberPar[0]);
        }
      }
    )?
  ;


protected predicate[Container ns, Type expectedType,Container functionNs]
	returns [Evaluable expr]
	{
	expr = null;
	boolean negation=false;
	}
 	: (nt:"NOT" {negation=true;})? LPAREN expr=expression[ns,expectedType,functionNs] RPAREN
		    { if(negation){
		    	boolean dirty=false;
				if(!expr.isDirty() && !expr.isLogical()){
					reportError (formatMessage ("err_expr_noLogical",(String)null),
							 nt.getLine());
					dirty=true;
				}
			      try {
			      	expr = new Expression.Negation (expr);
			      	expr.setDirty(dirty);
			      } catch (Exception ex) {
			      	reportError (ex, nt.getLine());
			      }
			}
		    }
	| def:"DEFINED" LPAREN expr=factor[ns,functionNs] RPAREN
		{
		      try {
			expr = new Expression.DefinedCheck (expr);
		      } catch (Exception ex) {
			reportError (ex, def.getLine());
		      }
		}
	| expr=factor[ns,functionNs]
	;


protected relation[int[] lineNumberPar]
  returns [char code]
{
  code = '=';
}
  : eq:EQUALSEQUALS { code = '='; lineNumberPar[0]=eq.getLine(); }
  | be:BANGEQUALS   { code = '!'; lineNumberPar[0]=be.getLine(); }
  | lg:LESSGREATER  { code = '!'; lineNumberPar[0]=lg.getLine(); }
  | le:LESSEQUAL    { code = 'l'; lineNumberPar[0]=le.getLine(); }
  | ge:GREATEREQUAL { code = 'g'; lineNumberPar[0]=ge.getLine(); }
  | ls:LESS         { code = '<'; lineNumberPar[0]=ls.getLine(); }
  | gr:GREATER      { code = '>'; lineNumberPar[0]=gr.getLine(); }
  ;

  /* functionNs is the name space to use for FunctionCall and RuntimeParameter-Name
  */
protected factor[Container ns,Container functionNs]
	returns [Evaluable ev]
	{
	ev = null;
	Viewable dummy;
	Function dummyFunc;
	Evaluable dyEv;
	GraphicParameterDef param=null;
	List      nams = new LinkedList();
	int lin = 0;
	InspectionFactor inspFactor=null;
	ObjectPath inspRestriction=null;
	}
	:	( xyRef LPAREN ) => ev=functionCall[ns,functionNs]
	|	("PARAMETER" lin=names2[nams]) 
		{
			param=resolveRuntimeParameterRef(ns,(String[]) nams.toArray(new String[nams.size()]),lin);
			ev=new ParameterValue(param);
		}
	|       (( (("AREA")? "INSPECTION" "OF" )=>dummy=inspection[ns] 
			{
			DecompositionView insp=(DecompositionView)dummy;
			ev=inspFactor=new InspectionFactor();
			inspFactor.setRenamedViewable(insp.getRenamedViewable());
			inspFactor.setDecomposedAttribute(insp.getDecomposedAttribute());
			inspFactor.setAreaInspection(insp.isAreaDecomposition());
			} 
		| "INSPECTION" dummy=viewableRef[ns]
			{ ev=inspFactor=new InspectionFactor();
			inspFactor.setInspectionViewable((DecompositionView)dummy);
			} 
		) 
		("OF" inspRestriction=objectOrAttributePath[(Viewable)ns,functionNs]
			{ inspFactor.setRestriction(inspRestriction); 
			}
		)?) 
	|	({
			if(!(ns instanceof Viewable)){
				reportError (formatMessage ("err_Container_currentIsNotViewable",
				ns.toString()), LT(1).getLine());
			}
		}
		ev=objectOrAttributePath[(Viewable)ns,functionNs]
		)
	|	ev=constant[ns]
		
	;

protected objectOrAttributePath[Viewable start,Container context]
	returns[ObjectPath object]
	{
		object=null;
		PathEl el;
		LinkedList path=new LinkedList();
		Viewable next=null;
	}
	: el=pathEl[start,context]
		{
			path.add(el);
			next=start;
		}
	( p:POINTSTO
		{
			Object prenext=next;
			next=el.getViewable();
			// System.err.println(el+": "+prenext+"->"+next);
		}
	el=pathEl[next,null]
		{
			path.add(el);
		}
	)*
		{
			object=new ObjectPath(start,(PathEl[])path.toArray(new PathEl[path.size()]));
		}
	;

protected attributePath[Viewable ns,Container context]
	returns[ObjectPath object]
	{
	object=null;
	}
	: object=objectOrAttributePath[ns,context]
	;

protected pathEl[Viewable currentViewable,Container context]
	returns[PathEl el]
	{
		el=null;
	}
	:	"THIS" {el=new PathElThis(currentViewable);}
	|	kwi:"THISAREA" {
			// can only be applied to an area inspection view
			if(!(currentViewable instanceof DecompositionView) || !((DecompositionView)currentViewable).isAreaDecomposition()){
				reportError (formatMessage ("err_pathEl_thisareaOnNonAreaInspection",
					currentViewable.toString()), kwi.getLine());
			}
			el=new ThisArea((DecompositionView)currentViewable,false);
		}
	|	kwa:"THATAREA" {
			// can only be applied to an area inspection view
			if(!(currentViewable instanceof DecompositionView) || !((DecompositionView)currentViewable).isAreaDecomposition()){
				reportError (formatMessage ("err_pathEl_thatareaOnNonAreaInspection",
					currentViewable.toString()), kwa.getLine());
			}
			el=new ThisArea((DecompositionView)currentViewable,true);
		}
	|	kwp:"PARENT" {
			// can only be applied to an inspection view that is not an area inspection
			if(!(currentViewable instanceof DecompositionView) || ((DecompositionView)currentViewable).isAreaDecomposition()){
				reportError (formatMessage ("err_pathEl_parentOnNonInspection",
					currentViewable.toString()), kwp.getLine());
			}
			el=new PathElParent(currentViewable);
		}
	|	el=associationPath[currentViewable]
		{ // TODO pathEl adapt associationPath
		}
	| 	{(isAttributeRef(currentViewable,LT(1).getText()) || LT(1).getText().equals("AGGREGATES")) }? el=attributeRef[currentViewable]
        /* | ReferenceAttribute-Name
        ** | Role-Name
	** | Base-Name
        */
	|	n:NAME
		{ 
		AttributeDef refattr=null;
		refattr=findAttribute(currentViewable,n.getText());
		RoleDef oppend=null;
		if(currentViewable instanceof AbstractClassDef){
			if(context!=null){
				oppend=((AbstractClassDef)currentViewable).findOpposideRole(context,n.getText());
			}else{
				oppend=((AbstractClassDef)currentViewable).findOpposideRole(n.getText());
			}
		}
		if(refattr!=null && refattr.getDomainResolvingAliases() instanceof ReferenceType){
			// ReferenceAttribute
			el=new PathElRefAttr(refattr);
		}else if(refattr!=null && refattr.getDomainResolvingAliases() instanceof ObjectType){
			ObjectType ref=(ObjectType)refattr.getDomainResolvingAliases();
			el=new PathElBase(currentViewable,n.getText(),ref.getRef());
		}else if(currentViewable instanceof AssociationDef && currentViewable.getRealElement(RoleDef.class,n.getText())!=null){
			// currentView is an Association? -> role name
			el=new PathElAssocRole((RoleDef)currentViewable.getRealElement(RoleDef.class,n.getText()));
		}else if(currentViewable instanceof AbstractClassDef && oppend!=null){
			// currentView is an AbstractClassDef -> role name
			el=new PathElAbstractClassRole(oppend);
		}else{
			reportError (formatMessage ("err_pathEl_wrongName",
				n.getText(),currentViewable.toString()), n.getLine());
		}
		}
	/* TODO	| Role-Name [ '[' Association-Name']' ] */

	;

protected associationPath[Viewable currentViewable]
	returns[AssociationPath el]
	{
		el=null;
	}
	:	bs:BACKSLASH roleName:NAME
			{
			// check if currentViewable is a Class or an Asssociation
			if(!(currentViewable instanceof Table) && !(currentViewable instanceof AssociationDef)){
				// an association path may only be applied to a class or association
				reportError (formatMessage ("err_associationPath_currentIsNotClass",
					currentViewable.toString()), bs.getLine());
			}
			// check if role exists in currentViewable
			RoleDef targetRole=((AbstractClassDef)currentViewable).findOpposideRole(roleName.getText());
			if(targetRole==null){
				// no role with given name
				reportError (formatMessage ("err_associationPath_noRole",
					roleName.getText(),currentViewable.toString()), roleName.getLine());
			}
			el=new AssociationPath(targetRole);
			}
	;

protected attributeRef[Viewable currentViewable]
	returns[AbstractAttributeRef el]
	{
		long idx;
		el=null;
	}
	:	(n:NAME
			(	LBRACE idx=listIndex RBRACE
				{
				AttributeDef attrdef=findAttribute(currentViewable,n.getText());
				if(attrdef==null){
					// no attribute 'name' in 'currentView'
					reportError (formatMessage ("err_attributeRef_unknownAttr", n.getText(),
						currentViewable.toString()), n.getLine());
				}
				Type type=attrdef.getDomainResolvingAliases();
				if(type instanceof CoordType){
					if(idx<=0 || idx>=((CoordType)type).getDimensions().length){
						reportError (formatMessage ("err_attributeRef_axisOutOfBound", Integer.toString(((CoordType)type).getDimensions().length)), n.getLine());

					}
					AxisAttributeRef attrref=new AxisAttributeRef(attrdef,(int)idx);
					el=attrref;
				}else if(type instanceof CompositionType){
					StructAttributeRef attrref=new StructAttributeRef(attrdef,idx);
					el=attrref;
				}else{
					// AttributeRef name[] requires a COORD or STRUCTURE attribute in 'currentView'
					reportError (formatMessage ("err_attributeRef_unknownStructAttr", n.getText(),
						currentViewable.toString()), n.getLine());
				}
				}
			| /* empty */
				{
				AttributeDef attrdef=findAttribute(currentViewable,n.getText());
				if(attrdef==null){
					// no attribute 'name' in 'currentView'
					reportError (formatMessage ("err_attributeRef_unknownAttr", n.getText(),
						currentViewable.toString()), n.getLine());
				}
				AttributeRef attrref=new AttributeRef(attrdef);
				el=attrref;
				}
			))
		| aggr:"AGGREGATES"
			{
			// check if currentView is an Aggregation
			if(!(currentViewable instanceof AggregationView)){
				// no attribute 'name' in 'currentView'
				reportError (formatMessage ("err_attributeRef_noAggregates",
					currentViewable.toString()), aggr.getLine());
			}
			el=new AggregationRef((AggregationView)currentViewable);
			}
	;

protected listIndex
	returns[long index]
	{
		index=0;
	}
	:	"FIRST" {index=StructAttributeRef.eFIRST;}
	|	"LAST"  {index=StructAttributeRef.eLAST;}
	|	index=posInteger
	;

protected functionCall[Container ns,Container functionNs]
	returns[FunctionCall call]
{
  call = null;
  Function called = null;
  Evaluable arg = null;
  LinkedList args = null;
  FormalArgument       formalArguments[] = null;
  Type       expectedType = null;
  int        curArgument = 0;
  List      nams = new LinkedList();
  int lin = 0;
}
	: 
	lin = names2[nams]
	    {
	      String elementName=null;
	      Container container = null;
	
	      switch (nams.size()) {
	      case 1:
		/* FUNCTION */
		elementName = (String) nams.get(0);
		container = functionNs.getContainerOrSame(AbstractPatternDef.class);
		if (container == null)
		  container = functionNs.getContainerOrSame(Model.class);
		break;
	
	      case 2:
		/* MODEL.FUNCTION */
		container = resolveOrFixModelName(functionNs, (String) nams.get(0), lin);
		elementName = (String) nams.get(1);
		break;
	
	      case 3:
		/* MODEL.TOPIC.FUNCTION */
		container = resolveOrFixTopicName(
			      resolveOrFixModelName(functionNs, (String) nams.get(0), lin),
			      (String) nams.get(1),
			      lin);
		elementName = (String) nams.get(2);
		break;
	
	      default:
		reportError(rsrc.getString("err_weirdFunctionRef"), lin);
		panic();
		break;
	      }
	
	      called = (Function) container.getRealElement(Function.class, elementName);
	      if ((called == null) && (nams.size() == 1)){
		// unqualified name; search also in unqaulified imported models
		Model model = (Model) functionNs.getContainerOrSame(Model.class);
		called = (Function) model.getImportedElement (Function.class, elementName);
	      }
	      if (called == null) {
		reportError(
		  formatMessage("err_functionRef_weird", elementName, container.toString()),
		  lin);
		panic();
	      }
	    }

    lpar:LPAREN
    {
      args = new LinkedList();
      if (called != null)
        formalArguments = called.getArguments();
    }

    {
      curArgument = curArgument + 1;
      /* The semantic layer will complain if there are too many arguments,
         just make sure here that the parser does not crash in that case. */
      if ((formalArguments == null) || (curArgument > formalArguments.length))
        expectedType = null;
      else
        expectedType = formalArguments[curArgument - 1].getType();
    }

    (
    arg = argument[ns, expectedType,functionNs]
    {
      args.add (arg);
    }

    (
      COMMA
      {
        curArgument = curArgument + 1;
        /* The semantic layer will complain if there are too many arguments,
           just make sure here that the parser does not crash in that case. */
        if ((formalArguments == null) || (curArgument > formalArguments.length))
          expectedType = null;
        else
          expectedType = formalArguments[curArgument - 1].getType();
      }
      arg = argument[ns, expectedType,functionNs]
      {
        args.add (arg);
      }
    )*
    )?
    RPAREN

    {
      try {
        call = new FunctionCall (
          called,
          (Evaluable[]) args.toArray (new Evaluable[args.size()]));
      } catch (Exception ex) {
        reportError (ex, lpar.getLine());
      }
    }
  ;

protected argument[Container ns,Type expectedType,Container functionNs]
	returns[Evaluable arg]
	{
	Viewable ref=null;
	Viewable restrictedTo=null;
	boolean classRequired=(expectedType instanceof ClassType);
	arg=null;
	ch.interlis.ili2c.metamodel.Objects objs=null;
	}
	: 
	   arg=expression[ns,expectedType,functionNs]
	| ("ALL" 
			{
			 Viewable context=(Viewable)ns.getContainerOrSame(Viewable.class);
			 arg=objs=new ch.interlis.ili2c.metamodel.Objects(context);
			}
		( LPAREN 
		(
		ref=viewableRef[ns]
		| "ANYCLASS" {ref=modelInterlis.ANYCLASS;}
		)
	       {
	       		objs.setBase(ref);
	       }
	       ({ref instanceof AbstractClassDef}? ("RESTRICTION" LPAREN restrictedTo=classOrAssociationRef[ns]
			{ objs.addRestrictedTo(restrictedTo); }
		(SEMI restrictedTo=classOrAssociationRef[ns]
			{ objs.addRestrictedTo(restrictedTo); }
		)*
		RPAREN
		)?)	       
		RPAREN
		)? 
		)
	;

protected functionDef[Container container]
{
  Type t = null;
  FormalArgument arg=null;
  Function f = null;
  ArrayList     args = null;
	  String ilidoc=null;
	  Settings metaValues=null;
}
  : { ilidoc=getIliDoc();metaValues=getMetaValues();}
  	"FUNCTION"
    fn:NAME
    {
      f = new Function ();
      args = new ArrayList ();
      try {
        f.setName(fn.getText());
	f.setDocumentation(ilidoc);
	f.setMetaValues(metaValues);
      } catch (Exception ex) {
        reportError(ex, fn.getLine());
	return;
      }
    }

    lpar:LPAREN
    (
    arg = formalArgument[container, lpar.getLine(),args]
    {
      try {
        args.add (arg);
      } catch (Exception ex) {
        reportError (ex, lpar.getLine ());
	return;
      }
    }
    ( sem:SEMI arg = formalArgument[container, sem.getLine(),args]
      {
        try {
          args.add (arg);
        } catch (Exception ex) {
          reportError (ex, sem.getLine ());
	  return;
        }
      }
    )*
    )?
    RPAREN

    col:COLON

    t = argumentType[container, col.getLine(),null]
    {
      try {
        f.setArguments ((FormalArgument[]) args.toArray (new FormalArgument[args.size()]));
      } catch (Exception ex) {
        reportError (ex, col.getLine());
	return;
      }

      try {
        if (t != null)
          f.setDomain(t);
      } catch (Exception ex) {
        reportError(ex, col.getLine());
	return;
      }
    }

    ( explan:EXPLANATION
      {
        try {
          f.setExplanation(explan.getText());
        } catch (Exception ex) {
          reportError(ex, explan.getLine());
	  return;
        }
      }
    )?

    SEMI
    {
      try {
        container.add(f);
      } catch (Exception ex) {
        reportError(ex, fn.getLine());
	return;
      }
    }
  ;

protected formalArgument[Container scope, int line,ArrayList formalArgs]
	returns[FormalArgument arg]
	{ arg=null;
	Type domain=null;
	}
	: n:NAME COLON domain=argumentType[scope,line,formalArgs]
		{
			arg=new FormalArgument(n.getText(),domain);
		}
	;

protected argumentType[Container scope,int line,ArrayList formalArgs]
	returns[Type domain]
	{
		Viewable ref=null;
		domain=null;
		boolean objects=false;
		ObjectType ot=null;
		AbstractClassDef restrictedTo=null;
	}
	:	domain=attrTypeDef[scope,true,null,line,formalArgs]
	| ("OBJECT" {objects=false;}| "OBJECTS" {objects=true;}) "OF" 
		(
		/*
		** restrictedClassOrAssRef
                ** | restrictedStructureRef
                ** | viewRef
		*/
		ref=viewableRef[scope]
		| "ANYCLASS" {ref=modelInterlis.ANYCLASS;}
		)
	       {
	       		domain=ot=new ObjectType(ref,objects);
	       }
	       ({ref instanceof AbstractClassDef}? ("RESTRICTION" LPAREN restrictedTo=classOrAssociationRef[scope]
			{ ot.addRestrictedTo(restrictedTo); }
		(SEMI restrictedTo=classOrAssociationRef[scope]
			{ ot.addRestrictedTo(restrictedTo); }
		)*
		RPAREN
		)?)	       
	| "ENUMVAL" { domain=new EnumValType(true); }
	| "ENUMTREEVAL" { domain=new EnumValType(false); }
	;

protected viewDef[Container container]
	{
	View view = null;
	View base = null;
	LinkedList aliases=new LinkedList();
	Viewable decomposedViewable=null;
	boolean areaDecomp=false;
	Constraint constr;
    	Selection select;
	int selLine=0;
	LinkedList cols=null;
	int props=0;
	  String ilidoc=null;
	  Settings metaValues=null;
	}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
	viewToken:"VIEW"
	n:NAME
		props=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL
			|ch.interlis.ili2c.metamodel.Properties.eTRANSIENT
			]
	( view=formationDef[container] 
		{ if((props&ch.interlis.ili2c.metamodel.Properties.eEXTENDED)!=0){
			reportError(formatMessage("err_view_formationdef",n.getText()),n.getLine());
		}
		}
	| extToken:"EXTENDS" base=viewRef[container] {
		        if ((props & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0)
		        {
		          reportError(rsrc.getString("err_extendedWithExtends"),
		                      extToken.getLine());
				// create a dummy view
				view=new Projection(); 
		        }else{
				try {
					view=new ExtendedView(base);
				} catch (Exception ex) {
					reportError(ex, extToken.getLine());
				}
				((ExtendedView)view).setExtended(false);
			}
		}
	| /* empty */
		{ if((props&ch.interlis.ili2c.metamodel.Properties.eEXTENDED)==0){
			reportError(formatMessage("err_view_missingFormationdef",n.getText()),n.getLine());
			// create a dummy view
			view=new Projection(); 
		}else{
			// check if base topic contains a viewdef with the same name
			base = null;
			AbstractPatternDef baseTopic=(AbstractPatternDef)((AbstractPatternDef)container).getExtending();
			if(baseTopic!=null){
				base = (View) baseTopic.getRealElement(View.class, n.getText());
			}
			  if (base == null)
			  {
			    reportError (formatMessage (
			      "err_view_nothingToExtend",
			      n.getText(),
			      container.toString()),
			      viewToken.getLine());
			  }
			  else
			  {
				try {
					view=new ExtendedView(base);
				} catch (Exception ex) {
					reportError(ex, n.getLine());
				}
				((ExtendedView)view).setExtended(true);
			  }
		}
		}
	) {
		try {
			view.setSourceLine(n.getLine());
			view.setName(n.getText());
			view.setAbstract((props & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
			view.setFinal((props & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
			view.setTransient((props & ch.interlis.ili2c.metamodel.Properties.eTRANSIENT) != 0);
		} catch (Exception ex) {
			reportError(ex, n.getLine());
		}
		container.add(view);		
	}
	( baseExtensionDef[view]
	)*
    (
    	{
		selLine=LT(1).getLine(); 
	}
      select = selection [view,view]
      {
        try {
          if (select != null)
            view.add (select);
        } catch (Exception ex) {
          reportError (ex, selLine );
        }
      }
    )*
	EQUALS
	{
	view.setDocumentation(ilidoc);
	view.setMetaValues(metaValues);
	}
	viewAttributes[view] 

    ( constr=constraintDef[view,view.getContainer()]
      {
        if (constr != null)
          view.add (constr);
      }
    )*

    end [view]
    SEMI
	;

protected viewRef [Container scope]
  returns [View view]
  {
  view=null;
  Viewable viewable;
  }
  : viewable=viewableRef[scope]
  	{ view=(View)viewable; }
  ;

protected formationDef[Container container]
	returns[View view]
	{
	view=null;
	}
	: (view=projection[container]
	| view=join[container]
	| view=union[container]
	| view=aggregation[container]
	| view=inspection[container]
	) SEMI
	;

protected projection[Container container]
	returns[Projection view]
	{
	view=null;
	ViewableAlias base=null;
	}
	: projToken:"PROJECTION" "OF" base=renamedViewableRef[container]
		{
			view=new Projection();
			try{
				view.setSelected(base);
				LocalAttribute attrib=new LocalAttribute();
				attrib.setName(base.getName());
				attrib.setDomain(new ObjectType(base.getAliasing()));
				view.add(attrib);
			} catch (Exception ex) {
			  reportError(ex, projToken.getLine());
			}
		}
	;

protected join[Container container]
	returns[View view]
	{
	ViewableAlias viewable = null;
	LinkedList aliases=new LinkedList();
	view=null;
	}
	: join:"JOIN" "OF"
				viewable=renamedViewableRef[container] {aliases.add(viewable);}
				( COMMA viewable=renamedViewableRef[container]
				( LPAREN "OR" "NULL" RPAREN {viewable.setIncludeNull(true);})?
				{aliases.add(viewable);}
				)+ 
		      {
			view = new JoinView();
			try {
			  ((JoinView) view).setJoining((ViewableAlias[]) aliases.toArray (new ViewableAlias[aliases.size()]));
			  for(int i=0;i<aliases.size();i++){
			  	ViewableAlias base=(ViewableAlias)aliases.get(i);
				    AttributeDef exstAttr =  (AttributeDef)view.getRealElement (AttributeDef.class, base.getName());
				    if(exstAttr!=null){
					reportError (formatMessage ("err_attrNameInSameViewable",
							  view.toString(), base.getName()),
					   join.getLine());
				    }
				LocalAttribute attrib=new LocalAttribute();
				attrib.setName(base.getName());
				attrib.setDomain(new ObjectType(base.getAliasing()));
				view.add(attrib);
			  }
			} catch (Exception ex) {
			  reportError(ex, join.getLine());
			}
		      }
	;

protected union[Container container]
	returns[View view]
	{
	ViewableAlias viewable = null;
	LinkedList aliases=new LinkedList();
	view=null;
	}
	: union:"UNION" "OF" viewable=renamedViewableRef[container] {aliases.add(viewable);}
			(COMMA viewable=renamedViewableRef[container] {aliases.add(viewable);})+
			
		      {
			view = new UnionView();
			try {
			  ((UnionView) view).setUnited((ViewableAlias[]) aliases.toArray (new ViewableAlias[aliases.size()]));
			  for(int i=0;i<aliases.size();i++){
			  	ViewableAlias base=(ViewableAlias)aliases.get(i);
				    AttributeDef exstAttr =  (AttributeDef)view.getRealElement (AttributeDef.class, base.getName());
				    if(exstAttr!=null){
					reportError (formatMessage ("err_attrNameInSameViewable",
							  view.toString(), base.getName()),
					   union.getLine());
				    }
				LocalAttribute attrib=new LocalAttribute();
				attrib.setName(base.getName());
				attrib.setDomain(new ObjectType(base.getAliasing()));
				view.add(attrib);
			  }
			} catch (Exception ex) {
			  reportError(ex, union.getLine());
			}
		      }
	;

protected aggregation[Container container]
	returns[AggregationView view]
	{
	ViewableAlias base = null;
	UniqueEl cols=null;
	view=null;
	}
	: agg:"AGGREGATION" "OF"
			base=renamedViewableRef[container]
			{
                           view=new AggregationView(base);
				try{
					LocalAttribute attrib=new LocalAttribute();
					attrib.setName(base.getName());
					attrib.setDomain(new ObjectType(base.getAliasing()));
					view.add(attrib);
				} catch (Exception ex) {
				  reportError(ex, agg.getLine());
				}
			}
			(	"ALL"
			|	eq:"EQUAL"
                            LPAREN cols=uniqueEl[view,container] RPAREN
			    {
                                view.setEqual(cols);
			    }
			)
			
	;

protected inspection[Container container]
	returns[View view]
	{
	LinkedList aliases=new LinkedList();
	ViewableAlias decomposedViewable=null;
	boolean areaDecomp=false;
	view=null;
	}
	: (	"AREA" {areaDecomp=true;} )?
	decomp:"INSPECTION" "OF" decomposedViewable=renamedViewableRef[container]
	POINTSTO
	n1:NAME {aliases.add(n1.getText());} ( POINTSTO n2:NAME {aliases.add(n2.getText());})*
	
	{
		view= new DecompositionView();
		try{
			String[] aliasv=(String[])aliases.toArray (new String[aliases.size()]);
			AttributeRef attrRef[]=new AttributeRef[aliasv.length];
			Viewable currentView=decomposedViewable.getAliasing();
			AttributeDef attrdef=null;
			for(int i=0;i<aliasv.length;i++){
				if(i>0){
					if(!(attrRef[i-1].getDomain() instanceof CompositionType)){
						reportError (formatMessage ("err_inspection_noStructAttr", aliasv[i-1]
							), n1.getLine());
					}
					currentView=attrRef[i-1].getViewable();
				}
				attrdef=findAttribute(currentView,aliasv[i]);
				if(attrdef==null){
					// no attribute 'name' in 'currentView'
					reportError (formatMessage ("err_attributeRef_unknownAttr", aliasv[i],
						currentView.toString()), n1.getLine());
				}
				attrRef[i]=new AttributeRef(attrdef);
			}
			((DecompositionView) view).setDecomposedAttribute(
				new ObjectPath(decomposedViewable.getAliasing(),attrRef)
				);
			((DecompositionView) view).setAreaDecomposition(areaDecomp);
			((DecompositionView) view).setRenamedViewable(decomposedViewable);
			Table decomposedStruct=buildDecomposedStruct(attrdef,areaDecomp);
			LocalAttribute attrib=new LocalAttribute();
			attrib.setName(decomposedViewable.getName());
			attrib.setDomain(new ObjectType(decomposedStruct));
			view.add(attrib);
		}catch( Exception ex){
			reportError(ex,decomp.getLine());
		}
	}
	;


protected renamedViewableRef[Container scope]
	returns[ViewableAlias found]
	{
		String aliasName=null;
		Viewable aliasFor=null;
		found=null;
	}
	:	(	( NAME TILDE ) => n:NAME TILDE { aliasName=n.getText();}
		)?
		aliasFor=viewableRefDepReq[scope]
		{
			found=new ViewableAlias(aliasName,aliasFor);
		}
	;

protected viewableRef [Container scope]
  returns [Viewable found]
{
  List      nams = new LinkedList();
  int lin = 0;
  found = null;
  String elementName = null;
  ch.interlis.ili2c.metamodel.Element elt;
}
  : lin = names2[nams]
    {
      Container container = null;

      switch (nams.size()) {
      case 1:
        /* VIEWorTABLE */
        elementName = (String) nams.get(0);
        container = scope.getContainerOrSame(AbstractPatternDef.class);
        if (container == null)
          container = scope.getContainerOrSame(Model.class);
        break;

      case 2:
        /* MODEL.VIEWorTABLE */
        container = resolveOrFixModelName(scope, (String) nams.get(0), lin);
        elementName = (String) nams.get(1);
        break;

      case 3:
        /* MODEL.TOPIC.VIEWorTABLE */
        container = resolveOrFixTopicName(
                      resolveOrFixModelName(scope, (String) nams.get(0), lin),
                      (String) nams.get(1),
                      lin);
        elementName = (String) nams.get(2);
        break;

      default:
        reportError(rsrc.getString("err_weirdViewOrTableRef"), lin);
        panic();
        break;
      }

      elt = (Viewable) container.getRealElement(Viewable.class, elementName);
      if ((elt == null) && (nams.size() == 1)){
      	// unqualified name; search also in unqaulified imported models
        Model model = (Model) scope.getContainerOrSame(Model.class);
        elt = (Viewable) model.getImportedElement (Viewable.class, elementName);
      }
      if (elt == null) {
        reportError(
          formatMessage("err_noSuchViewOrTable", elementName, container.toString()),
          lin);
        panic();
      }
      found = (Viewable) elt;
    }
  ;

protected viewableRefDepReq [Container scope]
  returns [Viewable ref]
  {
  	ref=null;
	int refLine=0;
  }
 :	{refLine=LT(1).getLine();}
 	ref=viewableRef[scope]
  {
  	if(ref!=null){
                        // check that scope's topic depends on ref's topic
                        AbstractPatternDef scopeTopic=(AbstractPatternDef)scope.getContainerOrSame(AbstractPatternDef.class);
                          AbstractPatternDef refTopic=(AbstractPatternDef)ref.getContainer(AbstractPatternDef.class);
                          if(refTopic!=scopeTopic){
                            if(!scopeTopic.isDependentOn(refTopic)){
                              reportError(formatMessage ("err_viewableref_topicdepreq",
				scopeTopic.getName(),
				refTopic.getName()),refLine);
                            }
                          }
	}
  }
;

protected baseExtensionDef[Viewable scope]
{
	Viewable base=null;
	ViewableAlias ext1=null;
	ViewableAlias ext2=null;
}
	: "BASE" baseName:NAME "EXTENDED" by:"BY" 
	ext1=renamedViewableRef[scope] 
	  {
	base=getBaseViewable(getBaseViewableProxyAttr(scope,baseName.getText(),baseName.getLine()));
        if (base == null)
        {
            reportError(
              formatMessage ("err_viewable_noSuchBase",
                             baseName.getText(), scope.toString()),
                             baseName.getLine());
        }
	    AttributeDef exstAttr =  (AttributeDef)scope.getRealElement (AttributeDef.class, ext1.getName());
	    if(exstAttr!=null){
	    	reportError (formatMessage ("err_attrNameInSameViewable",
                                  scope.toString(), ext1.getName()),
                   by.getLine());
	    }
	    // check ext1 is a extension of base
	    if(!ext1.getAliasing().isExtending(base)){
	    	reportError (formatMessage ("err_viewext_notBase",
                                  ext1.getName(), baseName.getText()),
                   by.getLine());
	    }
	    try{
		LocalAttribute attrib=new LocalAttribute();
		attrib.setName(ext1.getName());
		attrib.setDomain(new ObjectType(ext1.getAliasing()));
		scope.add(attrib);
	    }catch( Exception ex){
	    	reportError(ex,by.getLine());
	    }
	  }
	(cm:COMMA ext2=renamedViewableRef[scope]
	{
	    AttributeDef exstAttr =  (AttributeDef)scope.getRealElement (AttributeDef.class, ext2.getName());
	    if(exstAttr!=null){
	    	reportError (formatMessage ("err_attrNameInSameViewable",
                                  scope.toString(), ext2.getName()),
                   cm.getLine());
	    }
	    // check ext2 is a extension of base
	    if(!ext2.getAliasing().isExtending(base)){
	    	reportError (formatMessage ("err_viewext_notBase",
                                  ext2.getName(), baseName.getText()),
                   cm.getLine());
	    }
	    try{
		LocalAttribute attrib=new LocalAttribute();
		attrib.setName(ext2.getName());
		attrib.setDomain(new ObjectType(ext2.getAliasing()));
		scope.add(attrib);
	    }catch( Exception ex){
	    	reportError(ex,cm.getLine());
	    }
	  }
	)*
	;

protected selection [Viewable view,Container functionNs]
	returns [Selection sel]
	{
		sel = null;
		Evaluable logex = null;
		Viewable ref;
		LinkedList base=new LinkedList();
	}
	: tok:"WHERE"
	logex = expression [view, /* expectedType */ predefinedBooleanType,functionNs]
		{
			if(!logex.isDirty() && !logex.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 tok.getLine());
				logex.setDirty(true);
			}
			sel = new ExpressionSelection(view, logex);
		}
	SEMI
	;

protected viewAttributes[Viewable view]
	{
		int mods=0;
		Evaluable f=null;
	}
	:
	( "ATTRIBUTE" )?
	(	all:"ALL" "OF" v:NAME SEMI
      {
      	AttributeDef allOfAttr=null;
    {
      Viewable attrScope = (Viewable) view.getContainerOrSame (Viewable.class);
      if (attrScope == null)
        reportInternalError (v.getLine());
      else
      {
      	if(attrScope instanceof UnionView){
            reportError(
              formatMessage ("err_unionView_illegalallof",
                             v.getText()),
                             v.getLine());
	}
	allOfAttr=getBaseViewableProxyAttr(attrScope,v.getText(),v.getLine());
        if (allOfAttr == null)
        {
            reportError(
              formatMessage ("err_viewable_noSuchBase",
                             v.getText(), attrScope.toString()),
                             v.getLine());
        }
      }
    }

        if (allOfAttr != null)
        {
          ObjectType proxyType=(ObjectType)allOfAttr.getDomain();
          proxyType.setAllOf(true);
      	  Viewable allOf=getBaseViewable(allOfAttr);
          Iterator attrs = allOf.getAttributes ();
          while (attrs.hasNext ())
          {
            AttributeDef attr = (AttributeDef) attrs.next();
	    AttributeDef exstAttr =  (AttributeDef)view.getRealElement (AttributeDef.class, attr.getName());
	    if(exstAttr!=null){
	    	reportError (formatMessage ("err_attrNameInSameViewable",
                                  view.toString(), attr.getName()),
                   v.getLine());
	    }
	    if(attr.isAbstract() && !view.isAbstract()){
	    	reportError (formatMessage ("err_view_abstractAttr",
                                  view.toString(), attr.getName()),
                   v.getLine());
	    }
            LocalAttribute pa = new LocalAttribute ();
            pa.setGeneratedByAllOf(true);
            try {
              ObjectPath path;
              AttributeRef[] pathItems;

              pathItems = new AttributeRef[]
              {
                new AttributeRef(attr)
              };
              path = new ObjectPath(allOf, pathItems);

              pa.setName (attr.getName ());
              pa.setDomain (attr.getDomain ());
              pa.setBasePaths (new ObjectPath[] { path });
              view.add (pa);
            } catch (Exception ex) {
              reportError (ex, all.getLine ());
            }
          }
        }
      }
	| (NAME 
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		|ch.interlis.ili2c.metamodel.Properties.eTRANSIENT
		]
		COLONEQUALS
	)=> n:NAME 
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		|ch.interlis.ili2c.metamodel.Properties.eTRANSIENT
		]
		COLONEQUALS
		f=factor[view,view]
		{
			AttributeDef overriding = findOverridingAttribute (
				view, mods, n.getText(), n.getLine());
			Type overridingDomain = null;
			if (overriding != null){
				overridingDomain = overriding.getDomainResolvingAliases();
			}
			LocalAttribute attrib=new LocalAttribute();
			try {
				attrib.setName(n.getText());
				attrib.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
				attrib.setTransient((mods & ch.interlis.ili2c.metamodel.Properties.eTRANSIENT) != 0);
				// always final, but don't set, so that ili-export is the same as import
				// attrib.setFinal(true);
				if(overridingDomain!=null){
					attrib.setDomain((Type)overridingDomain.clone());
					attrib.setTypeProxy(true);
				}else{
					// type derived from the follwoing constructs
					// Factor = ( ObjectOrAttributePath
					// | ( Inspection | 'INSPECTION' Inspection-ViewableRef )
					// [ 'OF' ObjectOrAttributePath ]
					// | FunctionCall
					// | 'PARAMETER' [ Model-Name '.' ] RunTimeParameter-Name
					// | Constant ).				
				}
				attrib.setBasePaths (new Evaluable[] { f });
			} catch (Exception ex) {
				reportError(ex, n.getLine());
			}
			try {
				view.add(attrib);
				attrib.setExtending(overriding);
			} catch (Exception ex) {
				reportError(ex, n.getLine());
			}
		}
		SEMI		
	|	attributeDef[view]
	)*
	;

protected graphicDef[Container cont]
{
  Viewable basedOn = null;
  int mods = 0;
  Graphic graph = null;
  Graphic extending = null;
  Selection sel = null;
  int selLine=0;
	  String ilidoc=null;
	  Settings metaValues=null;
}
	:	{ ilidoc=getIliDoc();metaValues=getMetaValues();}
		"GRAPHIC" n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		( "EXTENDS" extending=graphicRef[cont] )?
		( "BASED" "ON" basedOn=viewableRefDepReq[cont] )?
		EQUALS
    {
      graph = new Graphic ();
      try {
        graph.setName (n.getText());
	graph.setDocumentation(ilidoc);
	graph.setMetaValues(metaValues);
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }

      try {
        graph.setAbstract ((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
      } catch (Exception ex) {
        reportError (ex, n.getLine ());
      }

      try {
        graph.setFinal ((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
      } catch (Exception ex) {
        reportError (ex, n.getLine ());
      }


      try {
        graph.setExtending (extending);
      } catch (Exception ex) {
        reportError (ex, n.getLine ());
      }

      try {
        if (basedOn == null)
        {
          if (extending == null)
          {
            reportError (formatMessage (
              "err_graphic_basedOnOmitted",
              graph.toString ()),
              n.getLine ());
            panic ();
          }
          basedOn = extending.getBasedOn ();
        }

        graph.setBasedOn (basedOn);
      } catch (Exception ex) {
        reportError (ex, n.getLine ());
      }

      try {
        cont.add (graph);
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }

    }
		( {selLine=LT(1).getLine(); } sel=selection[basedOn,graph]
      {
        if (sel != null)
        {
          try {
            graph.add (sel);
          } catch (Exception ex) {
            reportError (ex, selLine);
          }
        }
      }
		)*
		( drawingRule[graph] )*
		end[graph]
		SEMI
	;

protected graphicRef [Container scope]
  returns [Graphic graph]
{
  List      nams = new LinkedList();
  graph = null;
  String   graphicName = null;
  int lin = 0;
}
  : lin = names2[nams]
    {
      Model model;
      Topic topic;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame (Model.class);
        topic = (Topic) scope.getContainerOrSame (Topic.class);
        graphicName = (String) nams.get(0);
        break;

      case 2:
        model = resolveOrFixModelName (scope, (String) nams.get(0), lin);
        topic = null;
        graphicName = (String) nams.get(1);
        break;

      case 3:
        model = resolveOrFixModelName (scope, (String) nams.get(0), lin);
        topic = resolveOrFixTopicName (model, (String) nams.get(1), lin);
        graphicName = (String) nams.get(2);
        break;

      default:
        reportError (rsrc.getString("err_graphicRef_weird"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        topic = null;
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        graphicName = (String) nams.get(nams.size() - 1);
        break;
      }

      graph = null;
      if (topic != null)
        graph = (Graphic) topic.getRealElement (Graphic.class, graphicName);

      if ((graph == null) && (topic == null))
        graph = (Graphic) model.getRealElement(Graphic.class, graphicName);
      if ((graph == null) && (nams.size() == 1))
        graph = (Graphic) model.getImportedElement(Graphic.class, graphicName);

      if (graph == null)
      {
        if (topic == null)
          reportError(
            formatMessage ("err_graphicRef_notInModel", graphicName, model.toString()),
            lin);
        else
          reportError(
            formatMessage ("err_graphicRef_notInModelOrTopic", graphicName,
                           topic.toString(), model.toString()),
            lin);

        try {
          graph = new Graphic();
          graph.setName (graphicName);
          if (topic == null)
            model.add (graph);
          else
            topic.add (graph);
        } catch (Exception ex) {
          panic();
        }
      }
    }
  ;

protected drawingRule[Graphic graph]
{
  int mods = 0;
  Table signTab = null;
  SignAttribute attr = null;
  SignAttribute overriding = null;
  boolean declaredExtended = false;
  SignInstruction instruct = null;
  List instructs = null;
  Viewable basedOn=graph.getBasedOn();
}
	:	n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		( "OF" signTab=classRef[graph] )?
		COLON
    {
      declaredExtended = (mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0;
      overriding =  (SignAttribute) graph.getRealElement (
        SignAttribute.class, n.getText());

      if ((overriding == null) && declaredExtended)
      {
        if (graph.getRealExtending() == null)
        {
          reportError (formatMessage ("err_signAttr_extendedInRootGraphic",
                                      n.getText(),
                                      graph.toString()),
                       n.getLine());
        }
        else
        {
          reportError (formatMessage ("err_signAttr_nothingToExtend",
                                      n.getText(),
                                      graph.getRealExtending().toString()),
                       n.getLine());
        }
      }

      if ((overriding != null)
          && (overriding.getContainer(Graphic.class) == graph))
      {
        reportError (formatMessage ("err_signAttr_inSameGraphic",
                                    graph.toString(),
                                    n.getText()),
                     n.getLine());
      }
      else if ((overriding != null) && !declaredExtended)
      {
        reportError (formatMessage ("err_signAttr_extendedWithoutDecl",
                                    n.getText(),
                                    graph.toString(),
                                    overriding.toString()),
                     n.getLine());
      }
    }

    {
      attr = new SignAttribute ();
      try {
        attr.setName (n.getText());
        attr.setExtending (overriding);
	attr.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT)!=0);
        graph.add (attr);
      } catch (Exception ex) {
        reportError (ex, n.getLine());
        panic ();
      }

      try {
        if (signTab == null)
        {
          if (overriding != null)
            signTab = overriding.getGenerating ();
          else
          {
            reportError (formatMessage ("err_signAttr_ofOmitted",
                                        attr.toString()),
                         n.getLine());
          }
        }
        attr.setGenerating (signTab);
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }
    }
    instruct = condSigParamAssignment [graph, signTab]
    {
      instructs = new LinkedList();
      if (instruct != null)
        instructs.add (instruct);
    }
    (
      COMMA
      instruct = condSigParamAssignment [graph, signTab]
      {
        if (instruct != null)
          instructs.add (instruct);
      }
    )*
    SEMI
    {
      try {
        attr.setInstructions (
          (SignInstruction[]) instructs.toArray (new SignInstruction[instructs.size()])
        );
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }
    }
  ;

protected condSigParamAssignment [Graphic graph,  Table signTab]
  returns [SignInstruction instruct]
{
  Evaluable            restrictor = null;
  List                 paramAssignments = null;
  ParameterAssignment  assign = null;
  Viewable basedOn=graph.getBasedOn();

  instruct = null;
}
  : (
      tok:"WHERE"
      restrictor = expression [basedOn, /* expectedType */ predefinedBooleanType,graph]
      {
			if(!restrictor.isDirty() && !restrictor.isLogical()){
				reportError (formatMessage ("err_expr_noLogical",(String)null),
						 tok.getLine());
				restrictor.setDirty(true);
			}
      }
    )?

    LPAREN
    assign = sigParamAssignment[graph, signTab]
    {
      paramAssignments = new LinkedList ();
      if (assign != null)
        paramAssignments.add (assign);
    }
    (
      SEMI
      assign = sigParamAssignment[graph, signTab]
      {
        if (assign != null)
          paramAssignments.add (assign);
      }
    )*
    RPAREN
    {
      instruct = new SignInstruction (
        restrictor,
        (ParameterAssignment[]) paramAssignments.toArray (
          new ParameterAssignment[paramAssignments.size()])
      );
    }
  ;


protected sigParamAssignment [Graphic graph, Table signTab]
  returns [ParameterAssignment assign]
{
  List assignments = null;
  Parameter assignedParam = null;
  Evaluable value = null;
  assign = null;
  Type   expectedType = null;
  FunctionCall dummyFunc;
  FunctionCall fcall;
  MetaObject metaObj=null;
  Viewable basedOn=graph.getBasedOn();
}
  : parm:NAME
    doteq:COLONEQUALS
    	{
        assignedParam = (Parameter) signTab.getRealElement(Parameter.class,
                                                        parm.getText());
        if (assignedParam == null)
        {
          reportError (formatMessage ("err_parameter_unknownInSignTable",
                                      parm.getText(),
                                      signTab.toString()),
                       parm.getLine());
        }else{
          expectedType = Type.findReal (assignedParam.getType());
	}
      }

    ( value = conditionalExpression[graph, expectedType,(expectedType instanceof MetaobjectType ? ((MetaobjectType)expectedType).getReferred() : null)]
    |  LCURLY metaObj=metaObjectRef[graph,((MetaobjectType)expectedType).getReferred()] RCURLY
    	{ value=new Constant.ReferenceToMetaObject(metaObj);
	}
    | value = factor[basedOn,graph]
    )

    {
      try {
        assign = new ParameterAssignment (assignedParam, value);
      } catch (Exception ex) {
        reportError (ex, doteq.getLine());
      }
    }
  ;

protected conditionalExpression [Graphic graph, Type expectedType,Table metaobjectclass]
  returns [ConditionalExpression condex]
{
  ObjectPath attrPath = null;
  List items = null;
  ConditionalExpression.Condition cond = null;
  Viewable basedOn=graph.getBasedOn();

  condex = null;
}
  : "ACCORDING"
    attrPath = attributePath[basedOn,graph.getContainer()]

    LPAREN
    cond = enumAssignment [graph, expectedType,metaobjectclass]
    {
      items = new LinkedList();
      if (cond != null)
        items.add (cond);
    }
    (
      COMMA
      cond = enumAssignment[graph, expectedType,metaobjectclass]
      {
        if (cond != null)
          items.add (cond);
      }
    )*
    RPAREN
    {
      condex = new ConditionalExpression (
        null /* TODO should be attrPath */ ,
        (ConditionalExpression.Condition[]) items.toArray (
           new ConditionalExpression.Condition[items.size()]));
    }
  ;

protected enumAssignment [Graphic graph, Type expectedType,Table metaobjectclass]
  returns [ConditionalExpression.Condition cond]
{
  Constant cnst = null;
  Constant.EnumConstOrRange range = null;
  cond = null;
  MetaObject metaObj=null;
}
  : (cnst = constant[graph]
  	| LCURLY metaObj=metaObjectRef[graph,metaobjectclass] RCURLY
		{
			cnst=new Constant.ReferenceToMetaObject(metaObj);
		}
	)
    wh:"WHEN"
    "IN"
    range = enumRange
    {
      try {
        cond = new ConditionalExpression.Condition (cnst, range);
      } catch (Exception ex) {
        reportError (ex, wh.getLine());
      }
    }
  ;

protected enumRange
  returns [Constant.EnumConstOrRange rangeOrEnum]
{
  rangeOrEnum = null;
  Constant.Enumeration from = null;
  Constant.Enumeration to = null;
  String[] commonPrefix = null;
}
  : from=enumerationConst
    (
      ddot:DOTDOT
      to = enumerationConst
      {
        /* length of "from" and "to" must be the same */
        if (from.getValue().length != to.getValue().length)
        {
          rangeOrEnum = from;
          reportError (formatMessage ("err_enumRange_notSameLength",
            rangeOrEnum.toString(),
            to.toString()),
            ddot.getLine());
        }
        else
        {
          commonPrefix = new String[from.getValue().length - 1];
          /* must have common prefix */
          for (int i = 0; i < commonPrefix.length; i++)
          {
            commonPrefix[i] = from.getValue()[i];
            if (!from.getValue()[i].equals(to.getValue()[i]))
            {
              reportError (formatMessage ("err_enumRange_notCommonPrefix",
                from.toString(),
                to.toString()),
                ddot.getLine());
              break;
            }
          }
          rangeOrEnum = new Constant.EnumerationRange (
            commonPrefix,
            from.getValue()[from.getValue().length - 1],
            to.getValue()[from.getValue().length -1]);
        }
      }
    |
      /* epsilon -- only one single enumeration constant */
      {
        rangeOrEnum = from;
      }
    )
  ;

protected properties [int acceptable]
  returns [int mods]
  {
  mods = 0;
  int mod;
  }
  :  (
       LPAREN
       mod = property [acceptable, mods]
       { mods = mod; }

       (
         COMMA
         mod = property [acceptable, mods]
         { mods |= mod; }
       )*
       RPAREN
     )?
  ;


protected property [int acceptable, int encountered]
  returns [int mod]
  {
  mod = 0;
  }
  :	a:"ABSTRACT"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) == 0)
                reportError(rsrc.getString("err_cantBeAbstract"),
                            a.getLine());
              else
                mod = ch.interlis.ili2c.metamodel.Properties.eABSTRACT;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString ("err_multipleAbstract"),
                               a.getLine());
            }
	|	f:"FINAL"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eFINAL) == 0)
                reportError(rsrc.getString ("err_cantBeFinal"),
                            f.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eFINAL;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString ("err_multipleFinal"),
                               f.getLine());
            }
	|	e:"EXTENDED"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) == 0)
                reportError(rsrc.getString ("err_cantBeExtended"),
                            e.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eEXTENDED;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleExtended"),
                               e.getLine());
            }
	|	o:"ORDERED"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eORDERED) == 0)
                reportError(rsrc.getString ("err_cantBeOrdered"),
                            o.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eORDERED;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleOrdered"),
                               o.getLine());
            }
	|	d:"DATA"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eDATA) == 0)
                reportError(rsrc.getString ("err_cantBeData"),
                            d.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eDATA;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleData"),
                               d.getLine());
            }
	|	v:"VIEW"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eVIEW) == 0)
                reportError(rsrc.getString ("err_cantBeView"),
                            v.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eVIEW;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleView"),
                               v.getLine());
            }
	|	b:"BASE"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eBASE) == 0)
                reportError(rsrc.getString ("err_cantBeBase"),
                            b.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eBASE;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleBase"),
                               b.getLine());
            }
	|	g:"GRAPHIC"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eGRAPHIC) == 0)
                reportError(rsrc.getString ("err_cantBeGraphic"),
                            g.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eGRAPHIC;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleGraphic"),
                               g.getLine());
            }
	|	x:"EXTERNAL"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eEXTERNAL) == 0)
                reportError(rsrc.getString ("err_cantBeExternal"),
                            x.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eEXTERNAL;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleExternal"),
                               x.getLine());
            }
	|	t:"TRANSIENT"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eTRANSIENT) == 0)
                reportError(rsrc.getString ("err_cantBeTransient"),
                            t.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eTRANSIENT;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleTransient"),
                               t.getLine());
            }
	|	oidTok:"OID"     
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eOID) == 0)
                reportError(rsrc.getString ("err_cantBeOid"),
                            oidTok.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eOID;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleOid"),
                               oidTok.getLine());
            }
	|	hidTok:"HIDING"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eHIDING) == 0)
                reportError(rsrc.getString ("err_cantBeHiding"),
                            hidTok.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eHIDING;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleHiding"),
                               hidTok.getLine());
            }
	|	genTok:"GENERIC"
            {
              if ((acceptable & ch.interlis.ili2c.metamodel.Properties.eGENERIC) == 0)
                reportError(rsrc.getString ("err_cantBeGeneric"),
                            genTok.getLine());
               else
                 mod = ch.interlis.ili2c.metamodel.Properties.eGENERIC;

              if ((encountered & mod) != 0)
                reportWarning (rsrc.getString("err_multipleGeneric"),
                               genTok.getLine());
            }
	;


protected end[Element elt]
	:	"END" nam:NAME
			{
				if (elt != null)
				{
					if (!nam.getText().equals(elt.getName())){
						reportError(
							formatMessage ("err_end_mismatch", elt.toString(),
							elt.getName(), nam.getText()),
							nam.getLine());
					}
				}
			}
	;

protected decimal
	returns [PrecisionDecimal dec]
	{
		dec = null;
	}
	:	d:DEC
    	{ dec = new PrecisionDecimal(d.getText()); }
	|	p:POSINT
    	{ dec = new PrecisionDecimal(p.getText()); }
	|	n:NUMBER
	    { dec = new PrecisionDecimal(n.getText()); }
	;

protected xyRef
	:	(NAME | "INTERLIS" ) (DOT NAME)*
	;


protected posInteger
	returns [int i]
	{
		i = 0;
	}
	:	p:POSINT
		{
			try {
				i = Integer.parseInt(p.getText());
			} catch (Exception ex) {
				/* An exception here would mean that the lexer detects
				   numbers which are not numbers for Java. */
				reportInternalError(ex, p.getLine());
			}
		}
	;

/* Returns a String[] consisting of each name as a String */
protected enumNameList[List namList]
 returns [int lin]
 {
 lin=0;
 }
  : firstName:NAME
    { namList.add(firstName.getText());
    lin=firstName.getLine();
    }
    enumNameListHelper[namList]
  ;

/* this rule is a helper to rule enumNameList
* it uses a syntactic predicate to avoid nondeterminism between
* DOT NAME and DOT OTHERS
*/
protected enumNameListHelper[List namList]
  : (DOT NAME )=> DOT n:NAME {namList.add(n.getText());} enumNameListHelper[namList]
  | /* empty */
  ;

/* helper for qualified model element names.
*/
protected names2 [List names]
  returns [int lineNumber]
{
  lineNumber = 0;
}
  : (ili:"INTERLIS" DOT {lineNumber = ili.getLine();}
  	( (iName:NAME  {names.add(ili.getText());names.add(iName.getText());}
		(DOT i2Name:NAME {names.add(i2Name.getText());})?
		)
  	| isg:"SIGN"  {names.add(ili.getText());names.add(isg.getText());}
	| irf:"REFSYSTEM" {names.add(ili.getText());names.add(irf.getText());}
	| imo:"METAOBJECT" {names.add(ili.getText());names.add(imo.getText());}
	| iur:"URI" {names.add(ili.getText());names.add(iur.getText());}
	| inm:"NAME" {names.add(ili.getText());names.add(inm.getText());}
	| ibo:"BOOLEAN" {names.add(ili.getText());names.add(ibo.getText());}
	| iha:"HALIGNMENT" {names.add(ili.getText());names.add(iha.getText());}
	| iva:"VALIGNMENT" {names.add(ili.getText());names.add(iva.getText());}
	))
  | sg:"SIGN" {lineNumber = sg.getLine();names.add(sg.getText());}
  | rf:"REFSYSTEM" {lineNumber = rf.getLine();names.add(rf.getText());}
  | mo:"METAOBJECT" {lineNumber = mo.getLine();names.add(mo.getText());}
  | (firstName:NAME 
    {
      lineNumber = firstName.getLine();
      names.add(firstName.getText());
    }

    ( DOT!
      nextName:NAME
      { names.add(nextName.getText()); }
    )*
    )
  ;



class Ili24Lexer extends Lexer;
options {
  charVocabulary = '\u0000'..'\uFFFE'; // set the vocabulary to be all 8 bit binary values
  k=5;                   // number of lookahead characters
  testLiterals = false;  // do not test for literals by default
}


tokens {
  PLUS;
  MINUS;
}

{
  public boolean isIli1=false;
}

// Whitespace -- ignored
WS
  : (
      ' '
    | '\t'
    | '\f'

    // handle newlines
    | (
        options { generateAmbigWarnings=false; } :

        "\r\n"  // DOS
        | '\r'    // Macintosh
        | '\n'    // Unix
      )
      { newline(); }
    )+
    { $setType(Token.SKIP); }
  ;

ILI_METAVALUE
  : "!!@"!
    ( ~('\n'|'\r') )*
    ( '\n' | '\r' ( '\n' )? )
    { newline(); }
  ;

// Single Line comment -- ignored
SL_COMMENT
  : "!!"!
    ( ~('\n'|'\r') )*
    ( '\n' | '\r' ( '\n' )? )
    { $setType(Token.SKIP); newline(); }
  ;

  
ILI_DOC
  : "/**"
    ( /* '\r' '\n' can be matched in one alternative or by matching
         '\r' in one iteration and '\n' in another.  I am trying to
         handle any flavor of newline that comes in, but the language
         that allows both "\r\n" and "\r" and "\n" to all be valid
         newline is ambiguous.  Consequently, the resulting grammar
         must be ambiguous.  I'm shutting this warning off.
      */
      options {
        generateAmbigWarnings=false;
      }
      :
      { LA(2) != '/' }? '*'
      | ILI_DOC
      | '\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r')
    )*
    "*/"
  ;

/* multiple line comments
 are ignored.
   This code has been copied from the ANTLR example grammar for
   the Java syntax.
*/
ML_COMMENT
  : "/*" ('\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r'))
    ( /* '\r' '\n' can be matched in one alternative or by matching
         '\r' in one iteration and '\n' in another.  I am trying to
         handle any flavor of newline that comes in, but the language
         that allows both "\r\n" and "\r" and "\n" to all be valid
         newline is ambiguous.  Consequently, the resulting grammar
         must be ambiguous.  I'm shutting this warning off.
      */
      options {
        generateAmbigWarnings=false;
      }
      :
      { LA(2) != '/' }? '*'
      | ML_COMMENT
      | '\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r')
    )*
    "*/"
    {$setType(Token.SKIP);}
  ;


// see multiple-line comments in ANTLR example grammar for Java syntax
EXPLANATION
  : "//"!
    (
      /* '\r' '\n' can be matched in one alternative or by matching
         '\r' in one iteration and '\n' in another.  I am trying to
         handle any flavor of newline that comes in, but the language
         that allows both "\r\n" and "\r" and "\n" to all be valid
         newline is ambiguous.  Consequently, the resulting grammar
         must be ambiguous.  I'm shutting this warning off.
      */
      options { generateAmbigWarnings=false; } :

      { LA(2)!='/' }? '/'
      | '\r' '\n'		{newline();}
      | '\r'			{newline();}
      | '\n'			{newline();}
      | ~('/'|'\n'|'\r')
    )*
    "//"!
  ;


LPAREN options { paraphrase = "'('"; }
  : '('
  ;


RPAREN options { paraphrase = "')'"; }
  : ')'
  ;


LBRACE options { paraphrase = "'['"; }
  : '['
  ;


RBRACE options { paraphrase = "']'"; }
  : ']'
  ;


LCURLY options { paraphrase = "'{'"; }
  : '{'
  ;

RCURLY options { paraphrase = "'}'"; }
  : '}'
  ;


STAR options { paraphrase = "'*'"; }
  : '*'
  ;

SLASH options { paraphrase = "'/'"; }
  : '/'
  ;

BACKSLASH options { paraphrase = "'\\'"; }
  : '\\'
  ;


PERCENT options { paraphrase = "'%'"; }
  : '%'
  ;

AT options { paraphrase = "'@'"; }
  : '@'
  ;


HASH options { paraphrase = "'#'"; }
  : '#'
  ;


TILDE options { paraphrase = "'~'"; }
  : '~'
  ;


LESS options { paraphrase = "'<'"; }
  : '<'
  ;

LESSEQUAL options { paraphrase = "'<='"; }
  : "<="
  ;

GREATERGREATER options { paraphrase = "'>>'"; }
  : ">>"
  ;
GREATER options { paraphrase = "'>'"; }
  : '>'
  ;

GREATEREQUAL options { paraphrase = "'>='"; }
  : ">="
  ;


SEMI options { paraphrase = "';'"; }
  : ';'
  ;


EQUALS options { paraphrase = "'='"; }
  : '='
  ;


EQUALSEQUALS options { paraphrase = "'=='"; }
  : "=="
  ;


LESSGREATER options { paraphrase = "'<>'"; }
  : "<>"
  ;


BANGEQUALS options { paraphrase = "'!='"; }
  : "!="
  ;


COLONEQUALS options { paraphrase = "':='"; }
  : ":="
  ;


DOT options { paraphrase = "'.'"; }
  : '.'
  ;


DOTDOT options { paraphrase = "'..'"; }
  : ".."
  ;


COLON options { paraphrase = "':'"; }
  : ':'
  ;


COMMA options { paraphrase = "','"; }
  : ','
  ;


LESSMINUS options { paraphrase = "'<-'"; }
  : "<-"
  ;


POINTSTO options { paraphrase = "'->'"; }
  : "->"
  ;

IMPLIES options { paraphrase = "'=>'"; }
  : "=>"
  ;


AGGREGATE options { paraphrase = "'-<>'"; }
  : "-<>"
  ;


ASSOCIATE options { paraphrase = "'--'"; }
	: "--"
	;

COMPOSITE options { paraphrase = "'-<#>'"; }
	: "-<#>"
	;

protected ESC
  : '\\'
    ( '"' | '\\' | 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT )
  ;


STRING
  : '"'!
    ( ESC | ~( '"' | '\\' ) )*
    '"'!
  ;


protected DIGIT
    :   '0' .. '9'
    ;

protected LETTER
    :   'a' .. 'z' | 'A' .. 'Z'
    ;


protected POSINT
  : ( DIGIT )+
  ;


protected NUMBER
  : ( '+'! | '-')? POSINT
  ;


protected ILI1_SCALING
  : 'S'  NUMBER
  ;

protected SCALING
  : ('E'|'e')  NUMBER
  ;

protected ILI1_DEC
  : NUMBER
    ( DOT POSINT )?
    ( ILI1_SCALING )?
  ;

/*
Dec = ( Number [ '.' PosNumber ] | Float ).
Float = [ '+' | '-' ] '0.' (('1'|'2'|...|'9') [PosNumber] | (* '0' *))
        Scaling.
*/

protected DEC
  : 
  (( '+' | '-')? '0' DOT (('1'..'9' (POSINT)?) | ('0')+) ('e' | 'E')) => NUMBER DOT POSINT SCALING
  | NUMBER
    ( DOT POSINT )?
     ;      



protected HEXDIGIT
  : DIGIT
    | 'a' .. 'f'
    | 'A' .. 'F'
  ;


HEXNUMBER
  :  '0' ( 'x' | 'X' ) (HEXDIGIT)+
  ;


NUMERICSTUFF
  : ( POSINT DOTDOT ) => POSINT { $setType(POSINT); }
  | ( NUMBER DOTDOT ) => NUMBER { $setType(NUMBER); }
  | ( NUMBER ( '.' | {isIli1}? 'S' | {!isIli1}? ('e' | 'E') ) ) => ({isIli1}? ILI1_DEC { $setType(ILI1_DEC); } | {!isIli1}? DEC { $setType(DEC); })
  | '+'! ( POSINT {$setType(NUMBER);} | {$setType(PLUS);})
  | '-' ( POSINT {$setType(NUMBER);} | {$setType(MINUS);})
  | POSINT {$setType(POSINT);}
  ;


NAME
options { testLiterals = true; }
  :  LETTER
     ( LETTER | '_' | DIGIT )*
  ;
