header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.metamodel.*;
	import java.util.*;
}

class Ili2Parser extends Parser;

options
{
  codeGenMakeSwitchThreshold = 3;
  codeGenBitsetTestThreshold = 4;
  buildAST=false;
}
{
  protected PredefinedModel modelInterlis;
  protected Type predefinedBooleanType;
  protected Table predefinedRefSystemClass;
  protected Table predefinedCoordSystemClass;
  protected TransferDescription td;
  private Ili2Lexer lexer;
  private Set ili1TableRefAttrs;
  private Ili2cMetaAttrs externalMetaAttrs=new Ili2cMetaAttrs();
  
  /** Parse the contents of a stream according to INTERLIS-1 or INTERLIS-2 syntax
      (the version is detected automatically by the parser) and add the
      encountered contents to this TransferDescription.

      @return false if there have been any fatal errors which would lead
              this TransferDescription in an inconsistent state.
  */
  static public boolean parseIliFile (TransferDescription td,
    String filename,
    java.io.InputStream stream,
    ErrorListener listener
    ,Ili2cMetaAttrs metaAttrs
    )
  {

    try {
	if ((filename != null) && "".equals (td.getName())){
		td.setName(filename);
	}
      Ili2Lexer lexer = new Ili2Lexer (stream);

      Ili2Parser parser = new Ili2Parser (lexer);
      parser.lexer=lexer;
      parser.setFilename (filename);
      parser.setErrorListener (listener);
      return parser.interlisDescription (td);
    } catch (Exception ex) {
		ex.printStackTrace(Trace.getTraceStream());
      listener.error (new ErrorListener.ErrorEvent (ex, filename, 0,
        ErrorListener.ErrorEvent.SEVERITY_ERROR));
      return false;
    }
  }

	/** compiler error messages
	*/
	ResourceBundle rsrc = ResourceBundle.getBundle(
		ErrorMessages.class.getName(),
		Locale.getDefault());

	/** check if there is a role with the given name.
	* semantic predicate
	*/
	protected boolean isRoleName(String name)
	{
		// TODO
		return false;
	}
	/** check if there is a structure attribute with the given name.
	* semantic predicate
	*/
	protected boolean isStructAttrName(Viewable v,String name,int lin)
	{
			AttributeDef attr=(AttributeDef)v.getRealElement(AttributeDef.class,name);
			if(attr==null){
				// no attribute name in v
        		reportError (formatMessage (
          			"err_ili2parser_noSuchAttribute",
          				name,
			     		v.toString()),lin);
			    return false;
			}
			// check if attribute is a structure attribute
			Type type=attr.getDomainResolvingAliases();
			if(type instanceof CompositionType){
				// attr is a structure attribute
				return true;
			}
		return false;
	}

	/** check if there is a graphic parameter with the given name.
	* semantic predicate
	* @param modelName may be null
	*/
	protected boolean isGraphicParam(String modelName,String name)
	{
		// TODO
		return false;
	}


	protected Domain resolveDomainRef(Container scope,String[] nams, int lin)
	{
	      Model model;
	      AbstractPatternDef topic;
	      String domainName=null;
	      Domain d=null;

	      // TODO handle INTERLIS.xx DomainRefs
	      /*
			"INTERLIS" DOT
		    ( ido:NAME { domainName = ido.getText(); lin=ido.getLine(); }
		    | nm:"NAME" { domainName = nm.getText(); lin=nm.getLine(); }
		    | ur:"URI" { domainName = ur.getText(); lin=ur.getLine(); }
		    | bl:"BOOLEAN" { domainName = bl.getText(); lin=bl.getLine(); }
		    | ha:"HALIGNMENT" { domainName = ha.getText(); lin=ha.getLine(); }
		    | va:"VALIGNMENT" { domainName = va.getText(); lin=va.getLine(); }
		    )
		    {
		      d = (Domain) modelInterlis.getRealElement (Domain.class, domainName);
		      if (d == null)
			reportError(
			  formatMessage ("err_domainRef_notInModel", domainName,
					 modelInterlis.toString()),
			  lin);
		    }
		  */
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

	      if ((d == null) && (nams.length == 1))
		d = (Domain) modelInterlis.getRealElement(Domain.class, domainName);

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
        t = (Table) modelInterlis.getRealElement (Table.class, tableName);
        if(t!=null){
          return t;
        }
                 return resolveDomainRef(scope,nams,lin);
	}
  /* By default, ANTLR calls System.exit() when the parser
     is in an irrecoverable state. This is not acceptable
     for many applications. Since large parts of the parser
     code catch for thrown Exception objects (which makes
     sense because the semantic layer throws RuntimeExceptions
     fairly often, due to the restrictions of the Collection
     framework), it is easiest to define a ParserPanic Error
     which is not an Exception. However, it is really important
     that this somewhat quick-and-dirty thing is not visible to
     the outside; be sure to catch ParserPanic in all public
     methods of the parser.
  */
  private static class ParserPanic extends java.lang.Error {
    public ParserPanic() {
    }
  }

  protected ErrorListener errorListener = null;

  public void setErrorListener (ErrorListener listener)
  {
    errorListener = listener;
  }


  public void reportError (String message)
  {
    errorListener.error (new ErrorListener.ErrorEvent (
      message,
      getFilename(),
      /* line */ 0,
      ErrorListener.ErrorEvent.SEVERITY_ERROR));
  }


  public void reportWarning (String message)
  {
    errorListener.error (new ErrorListener.ErrorEvent (
      message,
      getFilename(),
      /* line */ 0,
      ErrorListener.ErrorEvent.SEVERITY_WARNING));
  }


  protected void reportError (String message, int lineNumber)
  {
    if (message == null)
      reportInternalError(lineNumber);

    errorListener.error (new ErrorListener.ErrorEvent (
      message,
      getFilename(),
      lineNumber,
      ErrorListener.ErrorEvent.SEVERITY_ERROR));
  }


  protected void reportWarning (String message, int lineNumber)
  {
    if (message == null)
      reportInternalError(lineNumber);

    errorListener.error (new ErrorListener.ErrorEvent (
      message,
      getFilename(),
      lineNumber,
      ErrorListener.ErrorEvent.SEVERITY_WARNING));
  }


  protected void reportError (Throwable exception, int lineNumber)
  {
    exception.printStackTrace(Trace.getTraceStream());
    String msg = exception.getLocalizedMessage();
    if (msg == null)
      reportInternalError (exception, lineNumber);

    errorListener.error (new ErrorListener.ErrorEvent (
      exception,
      getFilename(),
      lineNumber,
      ErrorListener.ErrorEvent.SEVERITY_ERROR));
  }

  protected void reportInternalError(int lineNumber)
  {
  	Throwable e = new Throwable();
  	e.fillInStackTrace();
  	e.printStackTrace(Trace.getTraceStream());

    reportError(
      formatMessage("err_internalCompilerError", /* exception */ ""),
      lineNumber);
  }


  protected void reportInternalError (Throwable exception, int lineNumber)
  {
    String msg;

    msg = exception.getLocalizedMessage();
    if (msg == null)
      msg = exception.getClass().getName();

    reportError(
      formatMessage("err_internalCompilerError", msg),
      lineNumber);
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
    errorListener.error (new ErrorListener.ErrorEvent (
      ex.getErrorMessage(),
      getFilename(),
      ex.getLine(),
      ErrorListener.ErrorEvent.SEVERITY_ERROR));
  }

  protected String formatMessage (String msg, Object[] args)
  {
    try {
      java.text.MessageFormat mess = new java.text.MessageFormat(
        rsrc.getString(msg));
      return mess.format(args);
    } catch (Exception ex) {
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

  /** Overrides ANTLR's panic() method. Instead of calling
      System.exit(), an instance of a subsclass of RuntimeError
      is thrown.

     See documentation for inner class ParserPanic.
  */
  public static void panic ()
  {
    throw new ParserPanic();
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
          && !scopeModel.isImporting (m))
      {
        reportError (formatMessage ("err_model_notImported",
          scopeModel.toString(), m.toString()),
          line);

        try {
          scopeModel.addImport (m);
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
                                  basket.getScopedName(null)),
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
		if(basket!=null){
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

  /** Find a LineForm given its explanation string. Used by INTERLIS-1 parser. */
  private LineForm findLineFormByExplanation (Container scope, String explanation)
  {
    Model model = (Model) scope.getContainerOrSame (Model.class);
    Iterator iter = model.iterator();
    while (iter.hasNext ())
    {
      Object obj = iter.next ();
      if ((obj instanceof LineForm)
          && explanation.equals (((LineForm) obj).getExplanation ()))
        return (LineForm) obj;
    }
    return null;
  }

  private int numIli1LineAttrStructures = 0;

  private Table createImplicitLineAttrStructure (Container container, int lineNumber)
  {
    Table result = new Table ();

    ++numIli1LineAttrStructures;
    try {
      result.setName ("LineAttrib" + numIli1LineAttrStructures);
      result.setAbstract (true);
      result.setIdentifiable (false); /* make it a structure */
      result.setExtending (td.INTERLIS.SURFACE_LINE);
      container.add (result);
    } catch (Exception ex) {
      reportInternalError (ex, lineNumber);
    }

    return result;
  }


  private int countElements (Container container, Class klass)
  {
    int numMatchingElements = 0;
    Iterator iter = container.iterator ();
    while (iter.hasNext ())
    {
      if (klass.isInstance (iter.next()))
        numMatchingElements = numMatchingElements + 1;
    }

    return numMatchingElements;
  }


  /** Used by INTERLIS-1 parser. */
  protected LineForm addLineFormIfNoSuchExplanation (Container scope, String explanation, int line)
  {
    LineForm result;

    result = findLineFormByExplanation (scope, explanation);
    if (result != null)
      return result;

    Model model = (Model) scope.getContainerOrSame (Model.class);

    /* No line form with the same explanation has been found. Create a new one. */
    if (!model.isContracted())
    {
      /* Only contracted models may contain line forms.
         Add an artificial contract, but emit a warning. */
      reportWarning (formatMessage ("err_lineForm_ili1_artificialContract",
                                    model.getName ()),
                     line);
      try {
      	Contract contract=new Contract(
		rsrc.getString ("err_lineForm_ili1_artificialContractorName")
		,rsrc.getString ("err_lineForm_ili1_artificialContractExplanation")
		);
        model.addContract(contract);
      } catch (Exception ex) {
        reportError (ex, line);
        panic ();
      }
    } /* if (!model.isContracted()) */

    try {
      result = new LineForm (formatMessage ("err_lineForm_ili1_artificialName",
                                            Integer.toString (countElements (model,
                                                                             LineForm.class) + 1)));
      result.setExplanation (explanation);
      model.add (result);
    } catch (Exception ex) {
      reportError (ex, line);
    }

    return result;
  }



  protected AttributeDef findOverridingAttribute (
    Viewable container, int mods, String name, int line)
  {
    boolean      declaredExtended = (mods & 4) != 0;
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

}

public interlisDescription [TransferDescription td1]
	returns [boolean canProceed]
	{
		canProceed = true;
		this.td = td1;
		this.modelInterlis = td.INTERLIS;
		this.predefinedBooleanType = Type.findReal (td.INTERLIS.BOOLEAN.getType());
		this.predefinedRefSystemClass = td.INTERLIS.REFSYSTEM;
		this.predefinedCoordSystemClass = td.INTERLIS.COORDSYSTEM;
	}
	:	(	interlis2Def
		|	interlis1Def
		)
	    exception
		    catch [NoViableAltException nvae]
		    {
		      reportError (rsrc.getString ("err_notIliDescription"));
		      canProceed = false;
		    }
		    catch [ParserPanic pp]
		    {
		      reportError(rsrc.getString("err_abortParsing"));
		      canProceed = false;
		    }
	;


protected interlis2Def
	{
		PrecisionDecimal version;
	}
	:	ili:"INTERLIS" version=decimal
	    {
	      if (version.doubleValue() != 2.1f) {
	        reportError(formatMessage("err_wrongInterlisVersion",version.toString()),
	                    ili.getLine());
	        panic();
	      }
              // set lexer mode to Ili 2.1
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
	  Contract contract=null;
	}
	:	(	"REFSYSTEM" "MODEL" { md = new RefSystemModel(); }
		|	"SYMBOLOGY" "MODEL" { md = new SymbologyModel(); }
		|	"TYPE" "MODEL" { md = new TypeModel(); }
		|	"MODEL" { md = new DataModel(); }
		)
		n1:NAME
			{
				try {
				 md.setName(n1.getText());
                                  md.setFileName(getFilename());
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
		(	"TRANSLATION" "OF" NAME { /* TODO */ }
		|
		)
		EQUALS
		(	contr:"CONTRACT" "ISSUED" "BY" contrIssuer:NAME
			(	contrExpl:EXPLANATION
		         {
			 	contract=new Contract(contrIssuer.getText(),contrExpl.getText());
		         }
			| /* empty */
		         {
			 	contract=new Contract(contrIssuer.getText());
		         }
			)
		       {
		         try {
		           md.addContract(contract);
		         } catch (Exception ex) {
		           reportError(ex, contrIssuer.getLine());
		         }
		       }
			SEMI
		)*
		( "IMPORTS" imp1:NAME
			{
				Model imported;
				imported=resolveOrFixModelName(td, imp1.getText(), imp1.getLine());
				if(imported!=null){
			        md.addImport(imported);
			    }
			}
			( COMMA imp2:NAME
				{
					Model imported;
					imported=resolveOrFixModelName(td, imp2.getText(), imp2.getLine());
					if(imported!=null){
				        md.addImport(imported);
				    }
				}
			)* SEMI
		)*
		(	metaDataUseDef[md]
		|	unitDefs[md]
		|	functionDef[md]
		|	lineFormTypeDef[md]
		|	domainDefs[md]
		|	graphicParameterDef[md]
		|	classDef[md]
		|	patternDef[md]
		|	topicDef[md]
		)*
		end[md] endDot:DOT
	     {
	       try {
			 List<Ili2cSemanticException> errs=new java.util.ArrayList<Ili2cSemanticException>();	       		
	         md.checkIntegrity (errs);
	         reportError(errs);
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
      PatternUse pUse=null;
	}
	:	(	"VIEW" {viewTopic=true;}
		|
		)
		"TOPIC" n1:NAME
		    {
		      try {
		        topic = new Topic();
			topic.setViewTopic(viewTopic);
		        topic.setName(n1.getText());
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
		( "USES" pUse=patternUse[topic]
			{
				topic.addPatternUse(pUse);
			}
			( COMMA pUse=patternUse[topic]
				{
					topic.addPatternUse(pUse);
				}
			)* SEMI
		)*
		definitions[topic]
		end[topic]
		SEMI
	;

protected definitions[Container scope]
	:	(	metaDataUseDef[scope]
		|	unitDefs[scope]
		|	domainDefs[scope]
		|	classDef[scope]
		|	associationDef[scope]
		|	constraintsDef[scope]
		|	viewDef[scope]
		|	viewProjectionDef[scope]
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

      topic = resolveOrFixTopicName(model, topicName, lin);
    }
	;

protected patternUse[Container scope]
	returns[PatternUse patternUse]
	{
		PatternDef ref=null;
		patternUse=new PatternUse();
	}
	:	ref=patternRef[scope]
			{
				patternUse.setUse(ref);
			}
		(	"WITH" "PREFIX" prefix:NAME
			{
				patternUse.setPrefix(prefix.getText());
			}
		|
		)
	;

protected classDef[Container container]
	{
	  Table table = null;
	  Table extending = null;
	  Table overwriting = null;
	  boolean identifiable = true;
	  Constraint constr = null;
	  int mods;
	}
	:	(	"CLASS"
		|	("STRUCTURE" { identifiable = false; } )
		)
		n1:NAME
	    {
	      try {
	        table = new Table();
	        table.setName (n1.getText());
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
		(	"ATTRIBUTE"
		|
		)
		( attributeDef[table] )*
		( constr=constraintDef[table]
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
  : "SIGN"        { t = modelInterlis.SIGN; }
  | "COORDSYSTEM" { t = modelInterlis.COORDSYSTEM; }
  | "AXIS"        { t = modelInterlis.AXIS; }
  | "REFSYSTEM"   { t = modelInterlis.REFSYSTEM; }

  | "INTERLIS" DOT
    ( nam:NAME { tableName=nam.getText(); lin=nam.getLine(); }
      {
        t = (Table) modelInterlis.getRealElement(Table.class, tableName);
        if (t == null)
          reportError (formatMessage ("err_noSuchTable", tableName,
                                      modelInterlis.toString ()),
                       lin);
      }

    | sg:"SIGN"        { t = modelInterlis.SIGN; }
    | cs:"COORDSYSTEM" { t = modelInterlis.COORDSYSTEM; }
    | ax:"AXIS"        { t = modelInterlis.AXIS; }
    | rf:"REFSYSTEM"   { t = modelInterlis.REFSYSTEM; }
    )


  | lin = names2[nams]
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
      if ((t == null) && (model != null))
        t = (Table) model.getElement (Table.class, tableName);
      if ((t == null) && (nams.size() == 1))
        t = (Table) modelInterlis.getRealElement (Table.class, tableName);

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
	}
	:	t=classRef[scope]
	;

protected patternDef[Container scope]
    { int props;
    PatternDef patternDef=new PatternDef();
    PatternDef extending=null;
    PatternUse pUse=null;
    }
	:	"PATTERN" n1:NAME
			{
				try{
					patternDef.setName(n1.getText());
				} catch (Exception ex) {
					reportError(ex, n1.getLine());
				}
			}
		props=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eFINAL]
			{
			  try{
		        patternDef.setAbstract((props & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
		        patternDef.setFinal((props & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
			  } catch (Exception ex) {
			    reportError(ex, n1.getLine());
			  }
			}
		(	extToken:"EXTENDS" extending=patternRef[scope]
			{
		          try {
		            patternDef.setExtending(extending);
		          } catch (Exception ex) {
		            reportError(ex, extToken.getLine());
		          }
		    }
		|
		)
		EQUALS
		    {
		      try {
		        scope.add (patternDef);
		      } catch (Exception ex) {
		        reportError (ex, n1.getLine ());
		        panic ();
		      }
		    }
		( "USES" pUse=patternUse[patternDef]
			{
				patternDef.addPatternUse(pUse);
			}
			( COMMA pUse=patternUse[patternDef]
			{
				patternDef.addPatternUse(pUse);
			}
			)* SEMI
		)*
		definitions[patternDef]
		end[patternDef] SEMI
	;

protected patternRef[Container scope]
  returns [PatternDef p]
  {
  List      nams = new LinkedList();
  p = null;
  int lin = 0;
  String patternName = null;
  }
	:	lin = names2[nams]
    {
      Model model;
      String   modelName;

      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        patternName = (String) nams.get(0);
        break;

      case 2:
        modelName = (String) nams.get(0);
        model = resolveOrFixModelName(scope, modelName, lin);
        patternName = (String) nams.get(1);
        break;

      default:
        reportError(rsrc.getString("err_weirdPatternRef"), lin);
        model = resolveModelName(scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        patternName = (String) nams.get(nams.size() - 1);
        break;
      }

      p = null;
      if (model != null)
        p = (PatternDef) model.getRealElement (PatternDef.class, patternName);

      if (p == null)
      {
          reportError (formatMessage ("err_noSuchPatternDef", patternName,
                                    model.toString()), lin);

          try {
            /* try a fix, so we can continue parsing */
            p = new PatternDef();
            p.setName(patternName);
            model.add(p);
          } catch (Exception ex) {
            panic();
          }

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
	  FunctionCall dummyFunc = null;
	  FunctionCall fcall = null;
	  Constant cnst;
	  AttributePath attrPath;
	}
	:	n:NAME
	mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		]
	COLON
    {
      overriding = findOverridingAttribute (
        container, mods, n.getText(), n.getLine());
      if (overriding != null)
        overridingDomain = overriding.getDomainResolvingAliases();

 	attrib = new LocalAttribute();
        try {
          attrib.setName(n.getText());
          attrib.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
          attrib.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
    }
	type=attrTypeDef[container,/* alias ok */ true, overridingDomain,
                     n.getLine()]
		{
                    if(type!=null){
			try{
          				attrib.setDomain(type);
			}catch(Exception ex){
				reportError(ex, n.getLine());
			}
                    }
		}
		(	COLONEQUALS
			(	(dummyFunc=functionCall[container])=>fcall=functionCall[container]
				{
					attrib.setFunctionCall(fcall);
				}
			|	cnst=constant[container,type]
				{
					attrib.setConstant(cnst);
				}
			|	attrPath=attributePath[container]
				{
					attrib.setAttributePath(attrPath);
				}
			)
		)?
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
	int        line]
	returns [Type typ]
	{
		typ=null;
	}
	:	"MANDATORY"
	(	typ=attrType[scope,allowAliases,extending,line]
	|
		    {
		      if (extending != null)
		      {
			try {
			  typ = (Type) extending.clone ();
			} catch (Exception ex) {
			  reportError (ex, line);
			  typ = null;
			}
		      }
		      else
		      {
			reportError (rsrc.getString ("err_type_mandatoryLonely"), line);
			typ = null;
		      }
		    }
	)
	    {
	      try {
		if (typ != null)
		  typ.setMandatory(true);
	      } catch (Exception ex) {
		reportError(ex, line);
	      }
	    }
	|	typ=attrType[scope,allowAliases,extending,line]
	;

protected attrType[Container  scope,
	boolean    allowAliases,
	Type       extending,
	int        line]
	returns [Type typ]
	{
		List nams = new LinkedList();
		typ=null;
		Table restrictedTo=null;
		Cardinality card=null;
		int lin=0;
		CompositionType ct=null;
		boolean ordered=false;
	}
	:	typ=type[scope,extending]
	//|	domainRef
	//|	restrictedStructureRef
	| lin = names2[nams]
		{
			Table s;
			Element e=resolveStructureOrDomainRef(scope,(String[]) nams.toArray(new String[nams.size()]),lin);
			if(e instanceof Table){
				s=(Table)e;
				ct=new CompositionType();
				try{
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
			}

		}
		(
		"RESTRICTED" "TO" restrictedTo=structureRef[scope]
			{ ct.addRestrictedTo(restrictedTo); }
			( COMMA restrictedTo=structureRef[scope]
				{ ct.addRestrictedTo(restrictedTo); }
			)*
		)?
	|       typ=restrictedObjectReference[scope]
	|	(	"BAG" {ordered=false;}
		|	"LIST" {ordered=true;}
		)
		(	card=cardinality
		|
		)
		"OF" ct=restrictedStructureRef[scope]
		{
			try{
				if(card!=null){
					ct.setCardinality(card);
				}
				ct.setOrdered(ordered);
			}catch(Exception ex){
			    reportError(ex, line);
			}
			typ=ct;
		}
	;

protected restrictedObjectReference[Container scope]
	returns[ReferenceType rt]
	{
		Viewable ref;
		Viewable restrictedTo;
		rt=new ReferenceType();
	}
    : refto:"REFERENCE" "TO" ref=classOrAssociationRef[scope]
    	{
		try{
                        // check that scope's topic depends on ref's topic
                        AbstractPatternDef scopeTopic=(AbstractPatternDef)scope.getContainer(AbstractPatternDef.class);
                        if(scopeTopic instanceof PatternDef){
                          // skip the check now
                          // do it later, while checking integrity
                        }else{
                          AbstractPatternDef refTopic=(AbstractPatternDef)ref.getContainer(AbstractPatternDef.class);
                          if(refTopic!=scopeTopic){
                            if(!scopeTopic.isDependentOn(refTopic)){
                              reportError(formatMessage ("err_refattr_topicdepreq",
				scopeTopic.getName(),
				refTopic.getName()),refto.getLine());
                            }
                          }
                        }
			rt.setReferred(ref);
		}catch(Exception ex){
			reportError(ex,refto.getLine());
		}
	}
    	("RESTRICTED" "TO" restrictedTo=classOrAssociationRef[scope]
			{ rt.addRestrictedTo(restrictedTo); }
		(COMMA restrictedTo=classOrAssociationRef[scope]
			{ rt.addRestrictedTo(restrictedTo); }
		)*
	)?
    ;

protected restrictedStructureRef[Container scope]
	returns[CompositionType ct]
	{
		Table ref;
		Table restrictedTo;
		ct=null;
		int line=0;
	}
	:	{line=LT(1).getLine();}
		ref=structureRef[scope]
			{
				ct=new CompositionType();
				try{
					ct.setComponentType(ref);
				}catch(Exception ex){
					reportError(ex, line);
				}
			}
		(	"RESTRICTED" "TO" restrictedTo=structureRef[scope]
			{ ct.addRestrictedTo(restrictedTo); }
			( COMMA restrictedTo=structureRef[scope]
				{ ct.addRestrictedTo(restrictedTo); }
			)*
		)?
	;

protected associationDef[Container scope]
	{
	int mods;
	AssociationDef def=new AssociationDef();
	AssociationDef extending = null;
	Viewable derivedFrom;
	Constraint constr;
	}
	:	a:"ASSOCIATION"
		( n:NAME
			{
				try{
					def.setName(n.getText());
				} catch (Exception ex) {
					reportError(ex, n.getLine());
				}
			}
		)?
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eEXTENDED|ch.interlis.ili2c.metamodel.Properties.eFINAL]
			{
				try {
					def.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
					def.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
	    				def.setExtended((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0);
				} catch (Exception ex) {
					reportError(ex, n.getLine());
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
		(	derivedToken:"DERIVED" "FROM" derivedFrom=viewableRef[scope]
		        {
		          try {
		            def.setDerivedFrom(derivedFrom);
		          } catch (Exception ex) {
		            reportError(ex, derivedToken.getLine());
		          }
		        }
		)?
		EQUALS
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
			}catch(Exception ex){
		            reportError(ex, a.getLine());
			}
		}
		("ATTRIBUTE")?
		( attributeDef[def] )*
		( constr=constraintDef[def]
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
	: (NAME properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eEXTENDED|ch.interlis.ili2c.metamodel.Properties.eFINAL|ch.interlis.ili2c.metamodel.Properties.eORDERED]
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
	}
	:	t=viewableRef[scope]
		{
			ref=(AssociationDef)t;
		}
	;


protected roleDef[AssociationDef container]
	{
		Cardinality card=null;
		int mods;
		Viewable ref=null;
		ObjectPath obj=null;
		RoleDef def=new RoleDef();
		int kind=0;
	}
	:
	n:NAME
	mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
		|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
		|ch.interlis.ili2c.metamodel.Properties.eFINAL
		|ch.interlis.ili2c.metamodel.Properties.eORDERED
		]
	(	ASSOCIATE {kind=RoleDef.Kind.eASSOCIATE;}
	|	AGGREGATE {kind=RoleDef.Kind.eAGGREGATE;}
	|	COMPOSITE {kind=RoleDef.Kind.eCOMPOSITE;}
	)
	(	card=cardinality
	)?
	ref=classOrAssociationRef[container]
	(	"FROM" obj=objectPath[container]
	)?
	{
		try {
		  def.setName(n.getText());
		  def.setExtended((mods & ch.interlis.ili2c.metamodel.Properties.eEXTENDED) != 0);
		  def.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
		  def.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
		  def.setOrdered((mods & ch.interlis.ili2c.metamodel.Properties.eORDERED) != 0);
		  def.setKind(kind);
		  if(card!=null){
		  	def.setCardinality(card);
		  }
		  def.setDestination(ref);
		  if(obj!=null){
		  	def.setDerivedFrom(obj);
		  }
		  container.add(def);
		} catch (Exception ex) {
		  reportError(ex, n.getLine());
		}
	}
	SEMI
	;

protected classOrAssociationRef[Container scope]
	returns[Viewable def]
	{
	def=null;
	}
	:	def=viewableRef[scope]
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
      | /* epsilon, as in "{42}" */ { max = min; })
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
	}
	:	n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		(	"EXTENDS" extending=domainRef[container]
		      {
			if (extending != null)
			  extendingType = extending.getType();
		      }
		)?
		eq:EQUALS
		(	"MANDATORY" (declared=type[container,extendingType])?
		|	declared=type[container,extendingType]
		)
		SEMI
		{
		Domain dd = new Domain ();

		try {
		dd.setName (n.getText());

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

protected type[Container scope,Type extending]
	returns [Type typ]
	{ typ=null;
	}
	:	(	typ=baseType[scope,extending]
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
      d=resolveDomainRef(scope,(String[]) nams.toArray(new String[0]),lin);
    }
  ;

protected baseType [Container scope, Type extending]
	returns [Type bt]
	{
		bt = null;
	}
	:	bt=textType[extending]
	|	bt=numericType[scope,extending]
	|	bt=structuredUnitType[scope,extending]
	|	bt=enumerationType[extending]
	|	bt=alignmentType
	|	bt=booleanType
	|	bt=coordinateType[scope,extending]
	|	bt=basketType[scope,extending]
	;

protected constant [Container scope, Type expectedType]
	returns [Constant c]
	{
	  c = null;
	}
	:	"UNDEFINED" { c = new Constant.Undefined(); }
	|	c=numericConst[scope]
	|	c=textConst
	|	c=structUnitConst[scope]
	|	c=enumerationConst
	;

protected textType[Type extending]
	returns [Type tt]
	{
		tt = null;
		int i = -1;
		/* TODO extending verarbeiten */
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

  | "TEXT"
    (
      ( star:STAR i=posInteger
        {
          try {
            tt = new TextType(i);
          } catch (Exception ex) {
            reportError (ex, star.getLine());
          }
        }
      )

      | /* epsilon */
        {
          tt = new TextType ();
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

protected enumeration [Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration enumer]
{
  List elements = new LinkedList();
  ch.interlis.ili2c.metamodel.Enumeration.Element curElement;

  enumer = null;
}
  : LPAREN
    curElement = enumElement[extending] { elements.add(curElement); }
    ( COMMA curElement = enumElement[extending] { elements.add(curElement); } )*
    RPAREN
    {
      enumer = new ch.interlis.ili2c.metamodel.Enumeration(
        (ch.interlis.ili2c.metamodel.Enumeration.Element[]) elements.toArray(
           new ch.interlis.ili2c.metamodel.Enumeration.Element[elements.size()]));
    }
  ;


protected enumElement [Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration.Element ee]
{
  ch.interlis.ili2c.metamodel.Enumeration subEnum = null;
  ch.interlis.ili2c.metamodel.Enumeration.Element curEnum = null;
  ee = null;
  int lineNumber = 0;
  List elt = new LinkedList();
  int siz = 0;
}
  : lineNumber = names2 [elt]
    ( subEnum = enumeration[extending] )?

    {
      siz = elt.size();
      for (int i = siz - 1; i >= 0; i--)
      {
        if (i == siz - 1)
        {
          if (subEnum == null)
          {
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               (String) elt.get(i));
          }
          else
          {
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               (String) elt.get(i),
               subEnum);
          }
        }
        else
        {
          ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               (String) elt.get(i),
               new ch.interlis.ili2c.metamodel.Enumeration (
                 new ch.interlis.ili2c.metamodel.Enumeration.Element[] {
                   ee
               })
          );
        }
      }

      if ((subEnum == null) && (siz > 1))
        reportError (rsrc.getString("err_dottedEnum"), lineNumber);
    }
  ;

protected enumerationConst
	returns[Constant c]
	{
	String[] mentionedNames;
	c=null;
	}
	:	HASH mentionedNames=names
		{
		c = new Constant.Enumeration (mentionedNames);
		}
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

protected numericType [Container scope, Type extending]
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
    | /* epsilon */
      {
        if (extending instanceof NumericalType)
          rotation = ((NumericalType) extending).getRotation ();
        else
          rotation = NumericalType.ROTATION_NONE;
      }
    )

    (
      referenceSystem = refSys [scope]
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

protected refSys[Container scope]
	returns [RefSystemRef rsr]
	{
	rsr = null;

	MetaObject  system = null;
	Domain        domain = null;
	int axisNumber = 0;
	}

	:
	lpar:LCURLY
          ( (names SLASH)=>system=metaObjectRef[scope,predefinedCoordSystemClass]
		 slash1:SLASH axisNumber=posInteger
		      {
			try {
			  if (system != null)
			    rsr = new RefSystemRef.CoordSystemAxis (system, axisNumber);
			} catch (Exception ex) {
			  reportError (ex, slash1.getLine ());
			}
		      }
	  | system=metaObjectRef[scope,predefinedRefSystemClass]
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
      slash2:SLASH
      axisNumber = posInteger
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
	| "LNBASE" {val = PrecisionDecimal.LNBASE;}
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

protected structuredUnitType [Container scope, Type extending]
  returns [StructuredUnitType sutyp]
{
  sutyp = null;
  int rotation = 0;
  int rotationLine = 0;
  Unit u = null;
  RefSystemRef referenceSystem = null;
}
  : ( min:STRUCTDEC DOTDOT max:STRUCTDEC
      {
        try {
          sutyp = new StructuredUnitType (
            new Constant.Structured (min.getText()),
            new Constant.Structured (max.getText()));
        } catch (Exception ex) {
          reportError(ex, min.getLine());
        }
      }
    )

    ( circ:"CIRCULAR"
      {
        try {
          sutyp.setCircular (true);
        } catch (Exception ex) {
          reportError (ex, circ.getLine());
        }
      }
    )?

    lbrac:LBRACE
    u = unitRef [scope]
    RBRACE

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
    | /* epsilon */
      {
        if (extending instanceof NumericalType)
          rotation = ((NumericalType) extending).getRotation ();
        else
          rotation = NumericalType.ROTATION_NONE;
      }
    )

    {
      if (sutyp != null)
      {
        try {
          sutyp.setUnit(u);
        } catch (Exception ex) {
          reportError (ex, lbrac.getLine());
        }

        try {
          sutyp.setRotation (rotation);
        } catch (Exception ex) {
          reportError (ex, rotationLine);
        }
      }
    }

    (
      referenceSystem = refSys [scope]
      {
        try {
          if (sutyp != null)
            sutyp.setReferenceSystem (referenceSystem);
        } catch (Exception ex) {
          reportError(ex, lbrac.getLine());
        }
      }
    )?
  ;

protected structUnitConst[Container scope]
	returns[Constant c]
	{ Unit un=null;
	c=null;
	}
	: str:STRUCTDEC (LBRACE un=unitRef[scope] RBRACE)?
		{
			if(un==null){
				c=new Constant.Structured (str.getText());
			}else{
				c=new Constant.Structured (str.getText(),un);
			}
		}
	;

protected coordinateType [Container scope, Type extending]
  returns [CoordType ct]
{
  NumericalType nt1 = null;
  NumericalType nt2 = null;
  NumericalType nt3 = null;
  int[] rots = null;
  ct = null;

  NumericalType ext_nt1 = null;
  NumericalType ext_nt2 = null;
  NumericalType ext_nt3 = null;

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
  : coord:"COORD"
    nt1 = numericalType [scope, ext_nt1]
    ( COMMA
      nt2 = numericalType [scope, ext_nt2]
      (
        COMMA
        ( rots=rotationDef
        | nt3=numericalType [scope, ext_nt3] (COMMA rots=rotationDef)?
        )
      )?
    )?
    {
      NumericalType[] nts;

      if (nt3 != null)
        nts = new NumericalType[] { nt1, nt2, nt3 };
      else if (nt2 != null)
        nts = new NumericalType[] { nt1, nt2 };
      else
        nts = new NumericalType[] { nt1 };

      try {
        if (rots == null)
          ct = new CoordType (nts);
        else
          ct = new CoordType (nts, rots[0], rots[1]);
      } catch (Exception ex) {
        reportError (ex, coord.getLine());
      }
    }
  ;

protected numericalType [Container scope, Type extending]
  returns [NumericalType ntyp]
{
  ntyp = null;
}
  : ntyp = structuredUnitType [scope, extending]
  | ntyp = numericType [scope, extending]
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


protected basketType[Container scope,Type extending]
	returns[BasketType bt]
	{
		int mods;
		bt=new BasketType();
		Topic topic=null;
	}
	:	b:"BASKET" mods=properties[ch.interlis.ili2c.metamodel.Properties.eDATA|ch.interlis.ili2c.metamodel.Properties.eVIEW|ch.interlis.ili2c.metamodel.Properties.eBASE|ch.interlis.ili2c.metamodel.Properties.eGRAPHIC]
			{
                         try {
 			    bt.setKind(mods);
                           } catch (Exception ex) {
                             reportError (ex, b.getLine());
			   }
			}
		(	"OF"
			( (NAME DOT)=>	 modelName:NAME DOT topicName:NAME
				{
					Model model = resolveOrFixModelName (scope, modelName.getText(), b.getLine());
					topic = resolveOrFixTopicName (model, topicName.getText(), b.getLine());
				}
			| topicName2:NAME
				{
					Model model = (Model) scope.getContainerOrSame(Model.class);
					topic = resolveOrFixTopicName (model, topicName2.getText(), b.getLine());

				}
			)
			{
				bt.setTopic(topic);
			}
		)?
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
  Table lineAttrStructure = null;
  int line = 0;
  lt = null;
}
  : ( ( "DIRECTED" { directed = true; } )? pl:"POLYLINE"
      {
        line = pl.getLine();
        lt = new PolylineType ();
        try {
          ((PolylineType) lt).setDirected (directed);
        } catch (Exception ex) {
          reportError (ex, line);
        }
      }
    | surf:"SURFACE" { line = surf.getLine(); lt = new SurfaceType(); }
    | area:"AREA" {line = area.getLine(); lt = new AreaType(); }
    )

    ( theLineForms = lineForm[scope] )?
    ( "VERTEX" controlPointDomain = domainRef[scope] )?
    ( "WITHOUT" "OVERLAPS" GREATER theMaxOverlap = decimal )?

    {
      /* If no line forms are specified, take the inherited value. */
      if ((theLineForms == null) && (extending instanceof LineType))
        theLineForms = ((LineType) extending).getLineForms ();

      /* If no maximal overlap is specified, take the inherited value. */
      if ((theMaxOverlap == null) && (extending instanceof LineType))
        theMaxOverlap = ((LineType) extending).getMaxOverlap ();

      /* If no control point domain is specified, take the inherited value. */
      if ((controlPointDomain == null) && (extending instanceof LineType))
        controlPointDomain = ((LineType) extending).getControlPointDomain ();

      try {
        if (theLineForms != null)
          lt.setLineForms (theLineForms);
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        lt.setControlPointDomain (controlPointDomain);
      } catch (Exception ex) {
        reportError (ex, line);
      }

      try {
        /* FIXME: Check whether it is an AREA; reportError + provide artificial value */

        lt.setMaxOverlap (theMaxOverlap);
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }

    (
      att:"LINE" "ATTRIBUTES"
      lineAttrStructure=classRef[scope]
      {
      /* TODO als Verweis ist hier nur ein Name auf eine STRUCTURE zulaessig
      */
        try {
          if (lt instanceof SurfaceOrAreaType)
            ((SurfaceOrAreaType) lt).setLineAttributeStructure (lineAttrStructure);
          else
            reportError (
              formatMessage ("err_lineType_lineAttrForPolyline", ""),
              att.getLine());
        } catch (Exception ex) {
          reportError (ex, att.getLine ());
        }
      }
    )?
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

  | "INTERLIS" DOT
    ( "STRAIGHTS" { lf = modelInterlis.STRAIGHTS; }
    | "ARCS" { lf = modelInterlis.ARCS; }
    )

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
}
  : linform:"LINE" "FORM"
    (
      nam:NAME
      ( expl:EXPLANATION { explString = expl.getText(); } )?
      SEMI

      {
        LineForm lf = new LineForm ();
        try {
          lf.setName (nam.getText ());
          lf.setExplanation (explString);
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

  String docName = null, idName = null;
}
	:	n:NAME { docName = idName = n.getText(); }
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
          Unit base = null;
          if (u != null)
            base = ((DerivedUnit) u).getBaseUnit();
          if (base != null)
            base = (Unit)base.getRealExtending();
          if (extending != null)
            reportError (rsrc.getString ("err_derivedUnit_ext"), ext.getLine());
          extending = base;
        }
      | u = composedUnit[scope, idName, docName, _abstract]
      | u = structuredUnit[scope, idName, docName, _abstract]
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
        scope.add(u);
        u.setExtending(extending);
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
  PrecisionDecimal fac = new PrecisionDecimal("1";
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
        ((FunctionallyDerivedUnit) u).setBaseUnit(baseUnit);
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
        ndu.setBaseUnit (baseUnit);
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
        ndu.setBaseUnit (baseUnit);
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
  Unit baseUnit = null;
  List composed = null;

  int line = 0;
  char compOp = '*';
  Unit compUnit = null;
}
  : lbrac:LPAREN
    baseUnit=unitRef[scope]
    {
      u = new ComposedUnit();
      composed = new LinkedList();

      try {
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
        u.setBaseUnit (baseUnit);
      } catch (Exception ex) {
        reportError (ex, lbrac.getLine());
      }

      try {
        u.setComposedUnits (
          (ComposedUnit.Composed[]) composed.toArray (
                            new ComposedUnit.Composed[composed.size()]));
      } catch (Exception ex) {
        reportError (ex, lbrac.getLine());
      }
    }
  ;


protected structuredUnit [Container scope, String idName, String docName, boolean _abstract]
  returns [StructuredUnit u]
{
  u = null;
  Unit firstUnit = null;
  List parts = null;

  int line = 0;
  PrecisionDecimal min = null;
  PrecisionDecimal max = null;
  Unit compUnit = null;
  boolean continuous = false;
}
  : lpar:LCURLY
    firstUnit=unitRef[scope]
    {
      u = new StructuredUnit();
      parts = new LinkedList();

      try {
        u.setName (idName);
        u.setDocName (docName);
      } catch (Exception ex) {
        reportError(ex, lpar.getLine());
      }

      try {
        u.setAbstract (_abstract);
      } catch (Exception ex) {
        reportError(ex, lpar.getLine());
      }
    }

    (
      col:COLON
      compUnit = unitRef[scope]
      lbrac:LBRACE
      min=decimal
      DOTDOT
      max=decimal
      RBRACE
      {
        try {
          parts.add (new StructuredUnit.Part (compUnit, min, max));
        } catch (Exception ex) {
          reportError (ex, col.getLine());
        }
      }

    )*

    RCURLY

    (
      "CONTINUOUS"
      { continuous = true; }
    )?

    {
      try {
        u.setFirstUnit (firstUnit);
      } catch (Exception ex) {
        reportError (ex, lpar.getLine());
      }

      try {
        u.setParts (
          (StructuredUnit.Part[]) parts.toArray (
                            new StructuredUnit.Part[parts.size()]));
      } catch (Exception ex) {
        reportError (ex, lpar.getLine());
      }

      try {
        u.setContinuous (continuous);
      } catch (Exception ex) {
        reportError (ex, lpar.getLine ());
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
  :  "INTERLIS" DOT n:NAME
     {
        u = (Unit) modelInterlis.getRealElement (Unit.class, n.getText());
        if (u == null)
          reportError (formatMessage ("err_noSuchUnit", n.getText(),
                                      modelInterlis.toString()),
                       n.getLine());
     }
  |
     lin = names2[nams]
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
        u = (Unit) modelInterlis.getRealElement (Unit.class, unitName);

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

protected metaDataUseDef[Container scope]
    {
    int mods;
    boolean sign=false;
    }
	:	(	"SIGN" {sign=true;}
		|	"REFSYSTEM" {sign=false;}
		)
		"BASKET" n:NAME mods=properties[ch.interlis.ili2c.metamodel.Properties.eEXTENDED|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		(	"EXTENDS" MetaDataUseRef
                    {
                      // TODO ce 2002-05-07 handle MetaDataUseDef extends, EXTENDED, FINAL
                    }
		)?
		TILDE modelName:NAME DOT topicName:NAME SEMI
		{
			int lin=modelName.getLine();
			Model model = resolveOrFixModelName(scope, modelName.getText(), lin);
			Topic topic = resolveOrFixTopicName(model, topicName.getText(), lin);
			MetaDataUseDef def=new MetaDataUseDef();
			def.setSignData(sign);
			def.setName(n.getText());
			def.setTopic(topic);
			scope.add(def);
			DataContainer basket=td.getMetaDataContainer(def.getScopedName(null));
			def.setDataContainer(basket);
		}
	;

protected metaDataUseRef[Container scope]
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
        ref = (MetaDataUseDef) modelInterlis.getRealElement (MetaDataUseDef.class, basketName);

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
	        reportError(rsrc.getString("err_weirdMetaObjectRef"), lin);
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
        topicName = (String) nams.get(0);
        basketName = (String) nams.get(1);
        objectName = (String) nams.get(2);
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = resolveOrFixTopicName(model, topicName, lin);
        ref = resolveOrFixBasketName(topic, basketName, lin);
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
	}
	: n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eEXTENDED|ch.interlis.ili2c.metamodel.Properties.eFINAL]
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
		(	type=attrTypeDef[container,true,overridingDomain,n.getLine()]
		|	pt:"METAOBJECT"
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
		)
    {
      Parameter p = new Parameter();

      try {
        p.setName (n.getText());
        container.add(p);
        p.setType (type);
        p.setExtending (overriding);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
    }

    SEMI
  ;

protected constraintDef[Viewable constrained]
	returns [Constraint constr]
	{
	constr = null;
	}

	: constr=mandatoryConstraint[constrained]
	| constr=plausibilityConstraint[constrained]
	| constr=existenceConstraint[constrained]
	| constr=uniquenessConstraint[constrained]
	;

protected mandatoryConstraint [Viewable v]
  returns [MandatoryConstraint constr]
{
  Evaluable condition = null;
  constr = null;
}
  : mand:"MANDATORY"
    "CONSTRAINT"
    condition = expression [v, /* expectedType */ predefinedBooleanType]
    SEMI

    {
      try {
        constr = new MandatoryConstraint();
        constr.setCondition(condition);
      } catch (Exception ex) {
        reportError(ex, mand.getLine());
      }
    }
  ;


protected plausibilityConstraint [Viewable v]
  returns [PlausibilityConstraint constr]
{
  PrecisionDecimal       percentage;
  int                    direction = 0;
  Evaluable              condition = null;
  constr = null;
}
  : tok:"CONSTRAINT"

    ( LESSEQUAL { direction = PlausibilityConstraint.DIRECTION_AT_MOST; }
    | GREATEREQUAL { direction = PlausibilityConstraint.DIRECTION_AT_LEAST; }
    )

    percentage = decimal PERCENT
    condition = expression [v, /* expectedType */ predefinedBooleanType]
    SEMI
    {
      try {
        constr = new PlausibilityConstraint();
        constr.setDirection(direction);
        constr.setCondition(condition);
        constr.setPercentage(percentage.doubleValue());
      } catch (Exception ex) {
        reportError (ex, tok.getLine());
      }
    }
  ;


protected existenceConstraint[Viewable v]
	returns [ExistenceConstraint constr]
	{
		AttributePath attr;
		Viewable ref;
		constr=new ExistenceConstraint();
		LinkedList attrRef=new LinkedList();
	}
	: "EXISTENCE" "CONSTRAINT" attr=attributePath[v]
		{
			constr.setRestrictedAttribute(attr);
		}
	"REQUIRED" "IN" ref=viewableRef[v] COLON attrRef=attributeRef[attrRef]
		{
			constr.addRequiredIn(ref,(AttributeRef[])attrRef.toArray(new AttributeRef[attrRef.size()]));
			attrRef.clear();
		}
	( "OR" ref=viewableRef[v] COLON attrRef=attributeRef[attrRef]
		{
			constr.addRequiredIn(ref,(AttributeRef[])attrRef.toArray(new AttributeRef[attrRef.size()]));
			attrRef.clear();
		}
	)*
	SEMI

	;


protected uniquenessConstraint[Viewable v]
	returns [UniquenessConstraint constr]
  	{
		constr=new UniquenessConstraint();
		LinkedList prefix;
		LinkedList attrs;
		Viewable start=v;
	}
	: "UNIQUE"
		( {isStructAttrName(v,LT(1).getText(),LT(1).getLine())}? prefix=structAttr[v]
			{
				constr.setPrefix((AttributeRef[])prefix.toArray(new AttributeRef[prefix.size()]));
				Iterator it=prefix.iterator();
				while(it.hasNext()){
					AttributeRef ref=(AttributeRef)it.next();
					AttributeDef attr=(AttributeDef)start.getRealElement(AttributeDef.class,ref.getName());
					CompositionType type=(CompositionType)attr.getDomainResolvingAliases();
					start=type.getComponentType();

				}
			}
		COLON attrs=uniqueEl[start]
	| attrs=uniqueEl[v]
	)
	SEMI
		{
			constr.setElements((UniqueEl[])attrs.toArray(new UniqueEl[attrs.size()]));
		}
	;

protected uniqueEl[Container ns]
	returns[/* List<UniqueEl> */ LinkedList list]
	{
		list=new LinkedList();
		AttributePath attr;
		UniqueEl uniqueEl=null;
	}
	: ( {isRoleName(LT(1).getText())}? n1:NAME
			{
				uniqueEl=new UniqueEl();
				uniqueEl.setRole(n1.getText());
			}
		| attr=attributePath[ns]
			{
				uniqueEl=new UniqueEl();
				uniqueEl.setAttribute(attr);
			}
	)
		{
			list.add(uniqueEl);
		}
	( COMMA
		( {isRoleName(LT(1).getText())}? n2:NAME
			{
				uniqueEl=new UniqueEl();
				uniqueEl.setRole(n2.getText());
			}
		| attr=attributePath[ns]
			{
				uniqueEl=new UniqueEl();
				uniqueEl.setAttribute(attr);
			}
		)
		{
			list.add(uniqueEl);
		}
	)*
	;

protected structAttr[Viewable start]
	returns[/* List<AttributeRef> */ LinkedList list]
	{
		list=new LinkedList();
		Viewable v=start;
	}
	: n1:NAME
		{
			AttributeDef attr=(AttributeDef)v.getRealElement(AttributeDef.class,n1.getText());
			if(attr==null){
				reportError(formatMessage("err_ili2parser_noSuchAttribute",n1.getText(),v.toString()),n1.getLine());
			}
			Type type=attr.getDomainResolvingAliases();
			if(!(type instanceof CompositionType)){
				reportError(formatMessage("err_ili2parser_attributeIsntComposition",n1.getText(),v.toString()),n1.getLine());
			}
			v=((CompositionType)type).getComponentType();
			AttributeRef ref=new AttributeRef();
			ref.setName(n1.getText());
			list.add(ref);
		}
	( DOT n2:NAME
		{
			AttributeDef attr=(AttributeDef)v.getRealElement(AttributeDef.class,n2.getText());
			if(attr==null){
				reportError(formatMessage("err_ili2parser_noSuchAttribute",n2.getText(),v.toString()),n1.getLine());
			}
			Type type=attr.getDomainResolvingAliases();
			if(!(type instanceof CompositionType)){
				reportError(formatMessage("err_ili2parser_attributeIsntComposition",n2.getText(),v.toString()),n1.getLine());
			}
			v=((CompositionType)type).getComponentType();
			AttributeRef ref=new AttributeRef();
			ref.setName(n2.getText());
			list.add(ref);
		}
	)*
	;

protected constraintsDef[Container scope]
	{
		Viewable def;
		Constraint constr;
	}
	:	"CONSTRAINTS" "OF" def=classOrAssociationRef[scope]
	EQUALS
	( constr=constraintDef[def]
		{if(constr!=null)def.add(constr);}
	)*
	"END" SEMI
	;

protected expression [Container ns, Type expectedType]
	returns [Evaluable expr]
	{
	expr = null;
	}
 	: expr=term[ns,expectedType]
	;

protected term[Container ns, Type expectedType]
  returns [Evaluable expr]
{
  List disjoined = null;
  expr = null;
  int lineNumber = 0;
}
  : expr=term1 [ns, expectedType]
    {
      disjoined = new LinkedList ();
      disjoined.add(expr);
    }

    (
      o:"OR"
      expr = term1 [ns, expectedType]
      {
        disjoined.add (expr);
        lineNumber = o.getLine();
      }
    )*

    {
      if (disjoined.size() == 1)
        expr = (Evaluable) disjoined.get(0);
      else
      {
        try {
          expr = new Expression.Disjunction (
            (Evaluable[]) disjoined.toArray (new Evaluable[disjoined.size()]));
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }
    }
  ;


protected term1 [Container ns, Type expectedType]
  returns [Evaluable expr]
{
  List conjoined = null;
  expr = null;
  int lineNumber = 0;
}
  : expr=term2 [ns, expectedType]
    {
      conjoined = new LinkedList ();
      conjoined.add(expr);
    }

    (
      an:"AND"
      expr = term2 [ns, expectedType]
      {
        conjoined.add (expr);
        lineNumber = an.getLine();
      }
    )*

    {
      if (conjoined.size() == 1)
        expr = (Evaluable) conjoined.get(0);
      else
      {
        try {
          expr = new Expression.Conjunction(
            (Evaluable[]) conjoined.toArray(new Evaluable[conjoined.size()]));
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }
    }
  ;

protected term2 [Container ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
  Evaluable conclusion;
}
  : expr=term3 [ns, expectedType]
    (
      an:POINTSTO
      conclusion = term3 [ns, expectedType]
      {
        try {
          expr = new Expression.Implication(expr,conclusion);
        } catch (Exception ex) {
          reportError (ex, an.getLine());
        }
      }
    )*

  ;

protected term3 [Container ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
  Evaluable comparedWith = null;
  char op = '=';
  int[] lineNumberPar = null;
}
  : expr = predicate[ns, expectedType]
    (
      {
        lineNumberPar = new int[1];
      }

      op = relation[lineNumberPar]
      comparedWith = predicate[ns, expectedType]
      {
        try {
          switch (op)
          {
          /* EQUALSEQUALS */
          case '=':
            expr = new Expression.Equality (expr, comparedWith);
            break;

          /* LESSGREATER, BANGEQUALS */
          case '!':
            expr = new Expression.Inequality (expr, comparedWith);
            break;

          /* LESSEQUAL */
          case 'l':
            expr = new Expression.LessThanOrEqual (expr, comparedWith);
            break;

          /* GREATEREQUAL */
          case 'g':
            expr = new Expression.GreaterThanOrEqual (expr, comparedWith);
            break;

          /* LESS */
          case '<':
            expr = new Expression.LessThan (expr, comparedWith);
            break;

          /* GREATER */
          case '>':
            expr = new Expression.GreaterThan (expr, comparedWith);
            break;

          default:
            reportInternalError (0);
            panic ();
          }
        } catch (Exception ex) {
          reportError (ex, lineNumberPar[0]);
        }
      }
    )?
  ;


protected predicate[Container ns, Type expectedType]
	returns [Evaluable expr]
	{
	expr = null;
	boolean negation=false;
	}
 	: (nt:"NOT" {negation=true;})? LPAREN expr=expression[ns,expectedType] RPAREN
		    { if(negation){
			      try {
				expr = new Expression.Negation (expr);
			      } catch (Exception ex) {
				reportError (ex, nt.getLine());
			      }
			}
		    }
	| def:"DEFINED" LPAREN expr=factor[ns,expectedType] RPAREN
		{
		      try {
			expr = new Expression.DefinedCheck (expr);
		      } catch (Exception ex) {
			reportError (ex, def.getLine());
		      }
		}
	| expr=factor[ns,expectedType]
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

protected factor[Container ns, Type expectedType]
	returns [Evaluable ev]
	{
	ev = null;
	Viewable dummy;
	Function dummyFunc;
	Evaluable dyEv;
	GraphicParameterDef param;
	}
	:	( xyRef LPAREN ) => ev=functionCall[ns]
	|	(dyEv=objectPath[ns]) => ev=objectPath[ns]
	|	{isGraphicParam(LT(1).getText(),LT(3).getText())}? modelName:NAME DOT graphicParamName:NAME
			{
				Model scopeModel = resolveOrFixModelName(ns,modelName.getText(),modelName.getLine());
				param = (GraphicParameterDef) scopeModel.getRealElement (GraphicParameterDef.class, graphicParamName.getText());
				if(param==null){
					reportError (formatMessage ("err_parameter_unknownInModel",
            					graphicParamName.getText(), scopeModel.toString()), graphicParamName.getLine());
				}
				ev=new ParameterValue(param);
			}
	|	{isGraphicParam(null,LT(1).getText())}? graphicParamName2:NAME
			{
				Model scopeModel = (Model) ns.getContainerOrSame (Model.class);
				param = (GraphicParameterDef) scopeModel.getRealElement (GraphicParameterDef.class, graphicParamName2.getText());
				if(param==null){
					reportError (formatMessage ("err_parameter_unknownInModel",
            					graphicParamName2.getText(), scopeModel.toString()), graphicParamName2.getLine());
				}
				ev=new ParameterValue(param);
			}
	|	ev=attributePath[ns]
	|	ev=constant[ns,expectedType]
	;

protected objectPath[Container ns]
	returns[ObjectPath object]
	{
		object=null;
		PathEl el;
		LinkedList path=new LinkedList();
	}
	: el=pathEl[ns]
		{
			path.add(el);
		}
	( DOT el=pathEl[ns]
		{
			path.add(el);
		}
	)*
		{
			Viewable start = (Viewable) ns.getContainerOrSame (Viewable.class);
			if (start == null)
			{
				reportInternalError (new IllegalStateException ("not in Viewable:" + ns),
					0);
			}
			object=new ObjectPath(start,(PathEl[])path.toArray(new PathEl[path.size()]));
		}
	;

protected pathEl[Container ns]
	returns[PathEl el]
	{
		el=null;
	}
	:	"THIS" {el=PathEl.THIS;}
	|	"THISAREA" {el=PathEl.THISAREA;}
	|	"THATAREA" {el=PathEl.THATAREA;}
	|	"PARENT" {el=PathEl.PARENT;}
	|	el=associationPath[ns]
        /* | ReferenceAttribute-Name
        ** | Role-Name
        */
	|	n:NAME
		{ PathElViewable e=new PathElViewable();
		el=e;
		e.setTable(n.getText());
		}
	|	"BASE"
		(baseName:NAME
			{ el=new PathElBase(baseName.getText());
			}
		| /* empty */
			{ el=new PathElBase();
			}
		)
	;

protected associationPath[Container ns]
	returns[PathElViewable el]
	{
		el=new PathElViewable();
	}
	:	BACKSLASH role1Name:NAME DOT assocName:NAME
		(	{isRoleName(LT(2).getText())}? DOT role2Name:NAME
			{
				el.setTable(role1Name.getText(),assocName.getText(),role2Name.getText());
			}
		| /* empty */
			{
				el.setAssociation(role1Name.getText(),assocName.getText());
			}
		)
	;

protected attributePath[Container ns]
	returns[AttributePath path]
	{
	ObjectPath pathStart = null;
	ObjectPath dyPathStart;
	path = null;
	LinkedList attrRef=new LinkedList();
	}
	:	(	(dyPathStart=objectPath[ns] COLON ) => pathStart=objectPath[ns] COLON
		| /* epsilon */
			{
			Viewable attrNameSpace = (Viewable) ns.getContainerOrSame (Viewable.class);
			if (attrNameSpace == null)
			{
				reportInternalError (new IllegalStateException ("not in Viewable:" + ns),
					0);
			}

			pathStart = new ObjectPath(attrNameSpace);
			}
		)
		attrRef=attributeRef[attrRef]
			{
				path=new AttributePath(pathStart,(AttributeRef[])attrRef.toArray(new AttributeRef[attrRef.size()]));
			}
	;

protected attributeRef[/* List<AttributeRef> */ LinkedList list1 ]
	returns[/* List<AttributeRef> */ LinkedList list]
	{
		list=list1;
		int axisNumber;
		long idx,dyIdx;
	}
	:	n:NAME
		( ((LBRACE dyIdx=listIndex RBRACE)? DOT)=>
			(	LBRACE idx=listIndex RBRACE
				{ StructAttributeRef attr=new StructAttributeRef();
				attr.setName(n.getText());
				attr.setListIndex(idx);
				list.add(attr);
				}
			| /* empty */
				{ AttributeRef attr=new AttributeRef();
				attr.setName(n.getText());
				list.add(attr);
				}
			)
			DOT list=attributeRef[list]
		|	LBRACE axisNumber=posInteger RBRACE
			{ AxisAttributeRef attr=new AxisAttributeRef();
			attr.setName(n.getText());
			attr.setAxisNumber(axisNumber);
			list.add(attr);
			}
		|
			{ AttributeRef attr=new AttributeRef();
			attr.setName(n.getText());
			list.add(attr);
			}
		)
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

protected functionCall[Container ns]
	returns[FunctionCall call]
{
  call = null;
  Function called = null;
  Evaluable arg = null;
  LinkedList args = null;
  FormalArgument       formalArguments[] = null;
  Type       expectedType = null;
  int        curArgument = 0;
}
	: ((NAME DOT NAME)=>modelName:NAME DOT functionName:NAME
			{
				Model model = resolveOrFixModelName(ns, modelName.getText(), modelName.getLine());
				called = (Function) model.getRealElement (Function.class, functionName.getText());
				if (called == null)
				{
					reportError (formatMessage ("err_functionRef_weird", functionName.getText(),
						model.toString()), functionName.getLine());
				}
			}
		| functionName2:NAME
			{
				Model model = (Model) ns.getContainerOrSame(Model.class);
				called = (Function) model.getRealElement (Function.class, functionName2.getText());
				if (called == null)
				{
					reportError (formatMessage ("err_functionRef_weird", functionName2.getText(),
						model.toString()), functionName2.getLine());
				}
			}
	)

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

    arg = argument[ns, expectedType]
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
      arg = argument[ns, expectedType]
      {
        args.add (arg);
      }
    )*
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

protected argument[Container ns,Type expectedType]
	returns[Evaluable arg]
	{
	Viewable ref;
	boolean classRequired=(expectedType instanceof ClassType);
	arg=null;
	}
	: {classRequired}? ref=viewableRef[ns]
		{
			arg=new ViewableAlias(null,ref);
		}
	|   arg=expression[ns,expectedType]
	|	"AGGREGATES"
		{
			arg=new ViewableAggregate();
		}
	;

protected functionDef[Container container]
{
  Type t = null;
  FormalArgument arg=null;
  Function f = null;
  List     args = null;
}
  : "FUNCTION"
    fn:NAME
    {
      f = new Function ();
      args = new LinkedList ();
      try {
        f.setName(fn.getText());
      } catch (Exception ex) {
        reportError(ex, fn.getLine());
      }
    }

    lpar:LPAREN
    arg = formalArgument[container, lpar.getLine()]
    {
      try {
        args.add (arg);
      } catch (Exception ex) {
        reportError (ex, lpar.getLine ());
      }
    }
    ( sem:SEMI arg = formalArgument[container, sem.getLine()]
      {
        try {
          args.add (arg);
        } catch (Exception ex) {
          reportError (ex, sem.getLine ());
        }
      }
    )*
    RPAREN

    col:COLON

    t = argumentType [container, col.getLine()]
    {
      try {
        f.setArguments ((FormalArgument[]) args.toArray (new FormalArgument[args.size()]));
      } catch (Exception ex) {
        reportError (ex, col.getLine());
      }

      try {
        if (t != null)
          f.setDomain(t);
      } catch (Exception ex) {
        reportError(ex, col.getLine());
      }
    }

    ( explan:EXPLANATION
      {
        try {
          f.setExplanation(explan.getText());
        } catch (Exception ex) {
          reportError(ex, explan.getLine());
        }
      }
    )?

    SEMI
    {
      try {
        container.add(f);
      } catch (Exception ex) {
        reportError(ex, fn.getLine());
      }
    }
  ;

protected formalArgument[Container scope, int line]
	returns[FormalArgument arg]
	{ arg=null;
	Type domain=null;
	}
	: n:NAME COLON domain=argumentType[scope,line]
		{
			arg=new FormalArgument(n.getText(),domain);
		}
	;

protected argumentType[Container scope,int line]
	returns[Type domain]
	{
		Viewable ref;
		domain=null;
	}
	:	domain=attrTypeDef[scope,true,null,line]
	|	"OBJECT"
		(	"OF" ref=viewableRef[scope]
			{
				domain=new ObjectType(ref);
			}
		| /* empty */
			{
				domain=new ObjectType();
			}
		)
	|	"CLASS"
		{
			domain=new ClassType();
		}
	;

protected viewDef[Container container]
	{
	View view = null;
	ViewableAlias viewable = null;
	LinkedList aliases=new LinkedList();
	Viewable decomposedViewable=null;
	boolean areaDecomp=false;
	Constraint constr;
    	Selection select;
	int selLine=0;
	LinkedList cols=null;
	}
	:	"VIEW" n:NAME EQUALS
		(	sel:"SELECTION" "OF" viewable=renamedViewableRef[container] SEMI
		      {
			view = new SelectionView();
			try {
			  ((SelectionView) view).setSelected(viewable);
			} catch (Exception ex) {
			  reportError(ex, sel.getLine());
			}
		      }
		|	join:"JOIN" "OF"
				viewable=renamedViewableRef[container] {aliases.add(viewable);}
				( COMMA viewable=renamedViewableRef[container]
				( LPAREN "OR" "NULL" RPAREN {viewable.setIncludeNull(true);})?
				{aliases.add(viewable);}
				)+ SEMI
		      {
			view = new JoinView();
			try {
			  ((JoinView) view).setJoining((ViewableAlias[]) aliases.toArray (new ViewableAlias[aliases.size()]));
			} catch (Exception ex) {
			  reportError(ex, join.getLine());
			}
		      }
		|	union:"UNION" "OF" viewable=renamedViewableRef[container] {aliases.add(viewable);}
			(COMMA viewable=renamedViewableRef[container] {aliases.add(viewable);})+
			SEMI
		      {
			view = new UnionView();
			try {
			  ((UnionView) view).setUnited((ViewableAlias[]) aliases.toArray (new ViewableAlias[aliases.size()]));
			} catch (Exception ex) {
			  reportError(ex, union.getLine());
			}
		      }
		|	"AGGREGATION" "OF"
			(	"ALL"
				{
					cols=null;
				}
			|	eq:"EQUAL"
                            {reportError("AGGREGATION OF EQUAL not yet supported",eq.getLine());
                            // TODO ce 2002-04-10 rework syntax to avoid lookahead
                            // correct the follwing line to uniqueEl[viewable]
                            }
                            LPAREN cols=uniqueEl[container] RPAREN
			)
			viewable=renamedViewableRef[container] SEMI
			{
                                if(cols!=null){
                                  view=new AggregationView(viewable,(UniqueEl[])cols.toArray(new UniqueEl[cols.size()]));
                                }else{
                                  view=new AggregationView(viewable,null);
                                }

			}
		|	(	"AREA" {areaDecomp=true;} )?
			decomp:"INSPECTION" "OF" decomposedViewable=viewableRefDepReq[container] COLON
				n1:NAME {aliases.add(n1.getText());} ( DOT n2:NAME {aliases.add(n2.getText());})* SEMI
			{
				view= new DecompositionView();
				try{
					ObjectPath pathStart=new ObjectPath(decomposedViewable);
					String[] aliasv=(String[])aliases.toArray (new String[aliases.size()]);
					AttributeRef attrRef[]=new AttributeRef[aliasv.length];
					for(int i=0;i<aliasv.length;i++){
						attrRef[i]=new AttributeRef();
						attrRef[i].setName(aliasv[i]);
					}
					((DecompositionView) view).setDecomposedAttribute(
						new AttributePath(pathStart,attrRef)
						);
					((DecompositionView) view).setAreaDecomposition(areaDecomp);
				}catch( Exception ex){
					reportError(ex,decomp.getLine());
				}
			}
		|
		)
		{
			try {
				view.setName(n.getText());
				container.add(view);
			} catch (Exception ex) {
				reportError(ex, n.getLine());
				panic();
			}
		}

    (
    	{selLine=LT(1).getLine(); }
      select = selection [view]
      {
        try {
          if (select != null)
            view.add (select);
        } catch (Exception ex) {
          reportError (ex, selLine );
        }
      }
    )*

    viewProjectionAttributes [view]

    ( constr=constraintDef[view]
      {
        if (constr != null)
          view.add (constr);
      }
    )*

    end [view]
    SEMI
	;

protected selection [Viewable view]
	returns [Selection sel]
	{
		sel = null;
		Evaluable logex = null;
		Viewable ref;
		LinkedList base=new LinkedList();
	}
	: (
		"WHERE"
		logex = expression [view, /* expectedType */ predefinedBooleanType]
		{
			sel = new ExpressionSelection(view, logex);
		}
	| "BASE" b1:NAME
		{
			base.add(b1.getText());
		}
		( DOT b2:NAME
			{
				base.add(b2.getText());
			}
		)*
		"RESTRICTED" "TO" ref=classOrAssociationRef[view]
			{
				sel=new BaseRestriction(view,(String[])base.toArray(new String[base.size()]));
				((BaseRestriction)sel).addRestrictedTo(ref);
			}
		( COMMA ref=classOrAssociationRef[view]
			{
				((BaseRestriction)sel).addRestrictedTo(ref);
			}
		)*
	)
	SEMI
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
  ch.interlis.ili2c.metamodel.Element elt;
}
  : lin = names2[nams]
    {
      Container container = null;
      String elementName = null;

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
                        if(scopeTopic instanceof PatternDef){
                          // skip the check now
                          // do it later, while checking integrity
                        }else{
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
  }
;

protected viewProjectionDef [Container container]
{
  int mods = 0;
  Viewable   extending = null;
  Viewable   basedOn = null;
  Projection proj = null;
  Constraint constr = null;
  Viewable overwriting = null;
}
  : "PROJECTION" n:NAME
    {
      proj = new Projection ();
      try {
        proj.setName (n.getText());
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }
    }
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		(	ext:"EXTENDS" extending=viewableRef[container]
		|	bo:"BASED" "ON" basedOn=viewableRefDepReq[container]
		      {
			try {
			  proj.setBasedOn (basedOn);
			} catch (Exception ex) {
			  reportError (ex, bo.getLine());
			}
		      }
		)
		EQUALS
    {
      if ((extending != null) && !(extending instanceof Projection))
      {
        String errorKey;

        if (extending instanceof View)
          errorKey = "err_projection_extView";
        else
          errorKey = "err_projection_extOther";

        reportError(
          formatMessage (errorKey, extending.toString()),
                         ext.getLine());
        extending = null;
      }
      try {
        if (extending != null)
          proj.setExtending (extending);
        else
          proj.setBasedOn (basedOn);
      } catch (Exception ex) {
        reportError(ex, ext.getLine());
      }

      try {
        proj.setAbstract((mods & ch.interlis.ili2c.metamodel.Properties.eABSTRACT) != 0);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }

      try {
        proj.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }

      try {
        container.add(proj);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
    }

		viewProjectionAttributes[proj]
    (
      constr=constraintDef[proj]
      {
        if (constr != null)
          proj.add (constr);
      }
    )*

    end [proj]
    SEMI
  ;

protected viewProjectionAttributes[Viewable view]
	:
	( "ATTRIBUTE" )?
	(	all:"ALL" "OF" v:NAME SEMI
      {
      	ViewableAlias allOf=null;
    {
      Viewable attrScope = (Viewable) view.getContainerOrSame (Viewable.class);
      if (attrScope == null)
        reportInternalError (v.getLine());
      else
      {
        allOf = attrScope.resolveBaseAlias (v.getText());
        if (allOf == null)
        {
          if (attrScope instanceof Table)
          {
            reportError(
              formatMessage ("err_class_noBase",
                             v.getText(), attrScope.toString()),
                             v.getLine());
          }
          else
          {
            reportError(
              formatMessage ("err_viewable_noSuchBase",
                             v.getText(), attrScope.toString()),
                             v.getLine());
          }
        }
      }
    }

        if ((allOf != null) && (allOf.getAliasing() != null))
        {
          Iterator attrs = allOf.getAliasing().getAttributes ();
          while (attrs.hasNext ())
          {
            AttributeDef attr = (AttributeDef) attrs.next();
            ProjectionAttribute pa = new ProjectionAttribute ();
            try {
              AttributePath path;
              AttributeRef[] pathItems;

              pathItems = new AttributeRef[]
              {
                new AttributeRef(attr.getName ())
              };
              path = new AttributePath (new ObjectPath(allOf.getAliasing()), pathItems);

              pa.setName (attr.getName ());
              pa.setDomain (attr.getDomain ());
              pa.setBasePaths (new AttributePath[] { path });
              view.add (pa);
            } catch (Exception ex) {
              reportError (ex, all.getLine ());
            }
          }
        }
      }
	|	viewProjectionAttributeDef[view]
	)*
	;


protected viewProjectionAttributeDef[Viewable container]
{
  int             mods = 0;
  ProjectionAttribute    attrib = null;
  AttributeDef    overriding = null;
  Type            overridingDomain = null;
  Type            type = null;
  boolean         mandatory = false;
  FunctionCall    fcall = null;
  FunctionCall    dyFunc = null;
  AttributePath baseAttr;
  LinkedList basev=new LinkedList();
}
	:	n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eEXTENDED
			|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		COLON
    {

      overriding = findOverridingAttribute (
        container, mods, n.getText(), n.getLine());
      if (overriding != null){
        overridingDomain = overriding.getDomainResolvingAliases();
	 }
	 attrib=new ProjectionAttribute();
    }
		type=attrTypeDef[container,true,overridingDomain,n.getLine()]
		{
			try{
				attrib.setDomain(type);
			}catch(Exception ex){
				reportError(ex, n.getLine());
			}
		}
		COLONEQUALS
		( (dyFunc=functionCall[container])=>fcall=functionCall[container]
				{
					attrib.setFunctionCall(fcall);
				}
		| (NAME COLON) => baseAttr=baseAttributeRef[container]
			{
				basev.add(baseAttr);
			}
			( COMMA baseAttr=baseAttributeRef[container]
				{
					basev.add(baseAttr);
				}
			)*
			{
				try{
					attrib.setBasePaths((AttributePath[])basev.toArray(new AttributePath[basev.size()]));
				} catch (Exception ex) {
					reportError(ex, n.getLine());
				}
			}
		)
		SEMI
    {
      if (attrib != null)
      {
        try {
          attrib.setName(n.getText());
          container.add(attrib);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }

        try {
          attrib.setExtending(overriding);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }

        try {
          attrib.setFinal((mods & ch.interlis.ili2c.metamodel.Properties.eFINAL) != 0);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
      }
    }
	;

protected baseAttributeRef[Container ns]
	returns[AttributePath path]
	{
	path=null;
	LinkedList<AttributeRef> attrRef = new LinkedList();
	}
	:	baseName:NAME COLON attr0:NAME
		{
			AttributeRef attr=new AttributeRef();
			attr.setName(attr0.getText());
			attrRef.add(attr);
		}
	( DOT attri:NAME
		{
			AttributeRef attr=new AttributeRef();
			attr.setName(attri.getText());
			attrRef.add(attr);
		}
	)*
		{
			PathElBase[] e=new PathElBase[1];
			e[0]=new PathElBase(baseName.getText());
			Viewable start=(Viewable)ns;
			ObjectPath pathStart=new ObjectPath(start,e);

			path=new AttributePath(pathStart,(AttributeRef[])attrRef.toArray(new AttributeRef[attrRef.size()]));

		}
	;

protected graphicParameterDef[Container scope]
	{
		Type domain;
		GraphicParameterDef def=null;
	}
	: "PARAMETER"
        ( n:NAME COLON
                {
			def=new GraphicParameterDef();
			def.setName(n.getText());
                }
            domain=attrTypeDef[scope,true,null,n.getLine()]
                {
                  def.setDomain(domain);
                  scope.add(def);
                }
	    SEMI
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
}
	:	"GRAPHIC" n:NAME
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT
			|ch.interlis.ili2c.metamodel.Properties.eFINAL]
		( "EXTENDS" extending=graphicRef[cont] )?
		( "BASED" "ON" basedOn=viewableRefDepReq[cont] )?
		EQUALS
    {
      graph = new Graphic ();
      try {
        graph.setName (n.getText());
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
		( {selLine=LT(1).getLine(); } sel=selection[basedOn]
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
		( signAttribute[graph] )*
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

      if ((graph == null) && ((topic == null) | (nams.size() == 1)))
        graph = (Graphic) model.getRealElement(Graphic.class, graphicName);

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

protected signAttribute[Graphic graph]
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
		mods=properties[ch.interlis.ili2c.metamodel.Properties.eABSTRACT|ch.interlis.ili2c.metamodel.Properties.eEXTENDED|ch.interlis.ili2c.metamodel.Properties.eFINAL]
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
    instruct = condSigParamAss [graph, signTab]
    {
      instructs = new LinkedList();
      if (instruct != null)
        instructs.add (instruct);
    }
    (
      COMMA
      instruct = condSigParamAss [graph, signTab]
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

protected condSigParamAss [Graphic graph,  Table signTab]
  returns [SignInstruction instruct]
{
  Evaluable            restrictor = null;
  List                 paramAssignments = null;
  ParameterAssignment  assign = null;
  Viewable basedOn=graph.getBasedOn();

  instruct = null;
}
  : (
      "WHERE"
      restrictor = expression [basedOn, /* expectedType */ predefinedBooleanType]
    )?

    LPAREN
    assign = sigAssignment[graph, signTab]
    {
      paramAssignments = new LinkedList ();
      if (assign != null)
        paramAssignments.add (assign);
    }
    (
      SEMI
      assign = sigAssignment[graph, signTab]
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


protected sigAssignment [Graphic graph, Table signTab]
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

    ( value = constant[graph, expectedType]
    | value = conditionalExpression[graph, expectedType,(expectedType instanceof MetaobjectType ? ((MetaobjectType)expectedType).getReferred() : null)]
    | {expectedType instanceof MetaobjectType}? metaObj=metaObjectRef[graph,((MetaobjectType)expectedType).getReferred()]
    	{ value=new Constant.ReferenceToMetaObject(metaObj);
	}
    | (dummyFunc=functionCall[basedOn])=>value=functionCall[basedOn]
    | value = attributePath[basedOn]
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
  AttributePath attrPath = null;
  List items = null;
  ConditionalExpression.Condition cond = null;
  Viewable basedOn=graph.getBasedOn();

  condex = null;
}
  : "ACCORDING"
    attrPath = attributePath [basedOn]

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
        attrPath,
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
  : (cnst = constant[graph, expectedType]
  	| metaObj=metaObjectRef[graph,metaobjectclass]
		{
			cnst=new Constant.ReferenceToMetaObject(metaObj);
		}
	)
    wh:"WHEN"
    "IN"
    range = enumerationRange
    {
      try {
        cond = new ConditionalExpression.Condition (cnst, range);
      } catch (Exception ex) {
        reportError (ex, wh.getLine());
      }
    }
  ;

protected enumerationRange
  returns [Constant.EnumConstOrRange rangeOrEnum]
{
  rangeOrEnum = null;
  String[] from = null;
  String[] to = null;
  String[] commonPrefix = null;
}
  : HASH from=names
    (
      ddot:DOTDOT
      HASH to = names
      {
        /* length of "from" and "to" must be the same */
        if (from.length != to.length)
        {
          rangeOrEnum = new Constant.Enumeration (from);
          reportError (formatMessage ("err_enumRange_notSameLength",
            rangeOrEnum.toString(),
            (new Constant.Enumeration (to)).toString()),
            ddot.getLine());
        }
        else
        {
          commonPrefix = new String[from.length - 1];
          /* must have common prefix */
          for (int i = 0; i < commonPrefix.length; i++)
          {
            commonPrefix[i] = from[i];
            if (!from[i].equals(to[i]))
            {
              reportError (formatMessage ("err_enumRange_notCommonPrefix",
                (new Constant.Enumeration (from)).toString(),
                (new Constant.Enumeration (to)).toString()),
                ddot.getLine());
              break;
            }
          }
          rangeOrEnum = new Constant.EnumerationRange (
            commonPrefix,
            from[from.length - 1],
            to[from.length -1]);
        }
      }
    |
      /* epsilon -- only one single enumeration constant */
      {
        rangeOrEnum = new Constant.Enumeration (from);
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
    	{ dec = new PrecisionDecimal(Double.parseDouble(p.getText()), 0); }
	|	n:NUMBER
	    { dec = new PrecisionDecimal(Double.parseDouble(n.getText()), 0); }
	;

protected xyRef
	:	NAME (DOT NAMES)*
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
protected names returns [String[] nams]
{
  List  namList = new LinkedList();
  nams = null;
}
  : firstName:NAME
    { namList.add(firstName.getText()); }

    ( DOT!
      nextName:NAME
      { namList.add(nextName.getText()); }
    )*

    { nams = (String[]) namList.toArray(new String[namList.size()]); }
  ;

/* Similar to names, but adds encountered names to a List and
   returns the line number of the first name.
*/
protected names2 [List names]
  returns [int lineNumber]
{
  lineNumber = 0;
}
  : firstName:NAME
    {
      lineNumber = firstName.getLine();
      names.add(firstName.getText());
    }

    ( DOT!
      nextName:NAME
      { names.add(nextName.getText()); }
    )*
  ;


/****************************************************************
* INTERLIS 1
*/

protected interlis1Def
{
  Model   model = null;
}
  : "TRANSFER" transferName:NAME SEMI
    {
      // set lexer mode to Ili 1
      lexer.isIli1=true;
      model = new DataModel ();
      try {
        td.setName (transferName.getText ());
        model.setName (transferName.getText ());
      } catch (Exception ex) {
        reportError (ex, transferName.getLine ());
      }
    }

    ( ili1_domainDefs [model] )?

    "MODEL" modelName:NAME
    {
      try {
        model.setName (modelName.getText ());
        model.setFileName(getFilename());
      } catch (Exception ex) {
        reportError (ex, transferName.getLine ());
      }

      try {
        td.add (model);
      } catch (Exception ex) {
        reportError (ex, transferName.getLine ());
      }
    }

    ( ili1_domainDefs [model] )?
    ( ili1_topic [model] )+

    "END" modelName2:NAME DOT
    {
      if (!model.getName().equals (modelName2.getText()))
      {
        /* model.toString() would return "DATA MODEL xxx", which
           could confuse users. Construct a string according to
           INTERLIS-1 on the fly. */
        reportError (formatMessage ("err_end_mismatch",
                                    "MODEL " + model.getName(),
                                    model.getName(),
                                    modelName2.getText()),
                     modelName2.getLine ());
      }
    }

    ( ili1_derivatives [model] )?
    ( ili1_view [model] )*
    ili1_format
    ili1_encoding
    EOF
  ;



protected ili1_domainDefs [Container container]
{
  Model model = (Model) container.getContainerOrSame (Model.class);
  Topic topic = (Topic) container.getContainerOrSame (Topic.class);
  Type type = null;
}
  : "DOMAIN"
    (
      domainName:NAME
      EQUALS
      type = ili1_attributeType [model, topic]
      SEMI
      {
        Domain domain = null;

        try {
          domain = new Domain ();
          domain.setName (domainName.getText ());
          if (type != null)
            domain.setType (type);
          domain.setAbstract (false);
          domain.setFinal (false);
        } catch (Exception ex) {
          reportError (ex, domainName.getLine ());
          try {
            /* try to fix it */
            domain = new Domain ();
            domain.setName (domainName.getText ());
            domain.setAbstract (false);
            domain.setFinal (false);
          } catch (Exception ex2) {
            panic ();
          }
        }

        try {
          if (domain != null)
            container.add (domain);
        } catch (Exception ex) {
          reportError (ex, domainName.getLine ());
        }
      }
    )+
  ;


protected ili1_topic [Model model]
{
  Topic topic = null;
}
  : "TOPIC" topicName:NAME EQUALS
    {
      topic = new Topic ();
      try {
        topic.setName (topicName.getText ());
        topic.setAbstract (false);
        model.add (topic);
      } catch (Exception ex) {
        reportError (ex, topicName.getLine ());
      }
    }

    (
      ili1_table [topic]
    | ili1_domainDefs [topic]
    )+

    end [topic]
    DOT
  ;


protected ili1_table [Topic topic]
{
  boolean optional = false;
  Table   table = null;
}
  : ( "OPTIONAL" { optional = true; } )?
    "TABLE"
    tableName:NAME
    EQUALS
    {
      table = new Table ();
      ili1TableRefAttrs=new HashSet();
      try {
        table.setName (tableName.getText ());
        table.setAbstract (false);
        topic.add (table);
      } catch (Exception ex) {
        reportError (ex, tableName.getLine ());
      }
    }

    ( ili1_attribute [table] )+
    ili1_identifications [table]

    "END" tableName2:NAME SEMI
    {
      ili1TableRefAttrs=null;
      if (!table.getName().equals (tableName2.getText ()))
      {
        /* table.toString() would return "CLASS xxx", which
           could confuse users. Construct a string according to
           INTERLIS-1 on the fly. */
        reportError (formatMessage ("err_end_mismatch",
                                    "TABLE " + table.getName(),
                                    table.getName (),
                                    tableName2.getText ()),
                     tableName2.getLine ());
      }
    }

  ;


protected ili1_attribute [Table table]
{
  boolean optional = false;
  Type type = null;
  AttributeDef attrib = null;
  Model model = (Model) table.getContainer (Model.class);
  Topic topic = (Topic) table.getContainer (Topic.class);
}
  : attributeName:NAME
    col:COLON
    ( "OPTIONAL" { optional = true; } )?

    ( type = ili1_localAttributeType [model, topic]
      {
        attrib = new LocalAttribute ();
      }

    | POINTSTO
      tabnam:NAME
      {
        Table referred = (Table) topic.getRealElement (Table.class, tabnam.getText ());
        if (referred == null)
        {
          reportError (formatMessage ("err_noSuchTable", tabnam.getText(),
                                      topic.toString ()),
                       tabnam.getLine ());
        }
        try {
        AssociationDef assoc=new AssociationDef();
        assoc.setName(table.getName()+attributeName.getText ());
        RoleDef role1=new RoleDef();
        role1.setName(table.getName());
        role1.setDestination(table);
        role1.setCardinality(new Cardinality(0, Cardinality.UNBOUND));
        assoc.add(role1);
        RoleDef role2=new RoleDef();
        role2.setName(attributeName.getText());
        role2.setDestination(referred);
        role2.setCardinality(new Cardinality(optional?0:1, 1));
        assoc.add(role2);
        assoc.fixupRoles();
        topic.add(assoc);
	ili1TableRefAttrs.add(attributeName.getText());
        } catch (Exception ex) {
          reportError (ex, tabnam.getLine ());
        }
      }
    )

    (
      expl:EXPLANATION
      {
        reportWarning (formatMessage ("err_attribute_ili1_constraintLost",
                                      attributeName.getText ()),
                       expl.getLine ());
      }
    )?

    SEMI

    {
      if(attrib!=null){
      try {
        attrib.setName (attributeName.getText ());
      } catch (Exception ex) {
        reportError (ex, attributeName.getLine ());
        panic ();
      }

      try {
        type.setMandatory (!optional);
      } catch (Exception ex) {
        reportError (ex, col.getLine ());
      }

      try {
        attrib.setDomain (type);
      } catch (Exception ex) {
        reportError (ex, col.getLine ());
      }

      try {
        table.add (attrib);
      } catch (Exception ex) {
        reportError (ex, col.getLine ());
      }
     }
    }
  ;


protected ili1_identifications [Table table]
{
  List<AttributeDef> ll = null;
  AttributeDef curAttribute = null;
  boolean ignore=false;
}
  : "NO" "IDENT"
  | "IDENT"
    (
      anam:NAME
      {
        ll = new LinkedList<AttributeDef>();
        curAttribute = (AttributeDef) table.getRealElement (
          AttributeDef.class,
          anam.getText ());
        if (curAttribute == null)
        {
	  if(!ili1TableRefAttrs.contains(anam.getText())){
	          reportError (formatMessage ("err_attributePath_unknownAttr_short",
                                      anam.getText (),
                                      table.toString ()),
                       anam.getLine ());
	  }else{
	  	ignore=true;
	  }
        }
        else
          ll.add (curAttribute);
      }
      (
        COMMA
        bnam:NAME
        {
          curAttribute = (AttributeDef) table.getRealElement (
            AttributeDef.class,
            bnam.getText ());
          if (curAttribute == null)
          {
            reportError (formatMessage ("err_attributePath_unknownAttr_short",
                                        bnam.getText (),
                                        table.toString ()),
                         bnam.getLine ());
          }
          else
            ll.add (curAttribute);
        }
      )*
      semi:SEMI
      {
      	if(ignore){
	        reportWarning("IDENT constraint lost",
                       anam.getLine ());
	}else{
        try {
		UniqueEl[] uelv=new UniqueEl[ll.size()];
		for(int i=0;i<uelv.length;i++){
			ObjectPath pathStart = new ObjectPath(table);
			AttributeRef[] attrRef=new AttributeRef[1];
			attrRef[0]=new AttributeRef();
			attrRef[0].setName(ll.get(i).getName());
			AttributePath path;
			path=new AttributePath(pathStart,attrRef);
			uelv[i]=new UniqueEl();
			uelv[i].setAttribute(path);
	  	}
          /* Construct a new uniqueness constraint */
          UniquenessConstraint constr = new UniquenessConstraint();
          constr.setElements(uelv);
          /* Add it to the table. */
          table.add (constr);
        } catch (Exception ex) {
          reportError (ex, semi.getLine ());
        }
	}
      }
    )+
  ;


protected ili1_localAttributeType [Model forModel, Topic forTopic]
  returns [Type type]
{
  type = null;
}
  : type = ili1_type [forModel, forTopic]
  ;


protected ili1_type [Model forModel, Topic forTopic]
  returns [Type type]
{
  type = null;
}
  : type = ili1_attributeType [forModel, forTopic]
  | nam:NAME
    {
      Domain domain = null;

      /* Look in the topic */
      if (forTopic != null)
      {
        domain = (Domain) forTopic.getRealElement (Domain.class, nam.getText ());
        if (domain == null)
          domain = (Domain) forModel.getRealElement (Domain.class, nam.getText ());
        if (domain == null)
        {
          reportError (formatMessage ("err_domainRef_notInModelOrTopic",
                                      nam.getText (),
                                      forTopic.toString (),
                                      "MODEL " + forModel.getName ()),
                       nam.getLine ());
          try {
            domain = new Domain ();
            domain.setName (nam.getText ());
            forTopic.add (domain);
          } catch (Exception ex) {
            panic ();
          }
        }
      }
      else /* forTopic == null */
      {
        domain = (Domain) forModel.getRealElement (Domain.class, nam.getText ());
        if (domain == null)
        {
          reportError (formatMessage ("err_domainRef_notInModel",
                                      nam.getText (),
                                      "MODEL " + forModel.getName ()),
                       nam.getLine ());
          try {
            domain = new Domain ();
            domain.setName (nam.getText ());
            forModel.add (domain);
          } catch (Exception ex) {
            panic ();
          }
        }
      }

      /* At this point, it is guaranteed that domain != null */
      type = new TypeAlias ();
      try {
        ((TypeAlias) type).setAliasing (domain);
      } catch (Exception ex) {
        reportError (ex, nam.getLine ());
      }
    }
  ;


protected ili1_attributeType [Model forModel, Topic forTopic]
  returns [Type type]
{
  type = null;
}
  : type = ili1_baseType [forModel]
  | type = ili1_lineType [forModel, forTopic]
  | type = ili1_areaType [forModel, forTopic]
  ;


protected ili1_baseType [Model containingModel]
  returns [Type type]
{
  type = null;
}
  : type = ili1_coord2
  | type = ili1_coord3
  | type = ili1_dim1Type
  | type = ili1_dim2Type [containingModel]
  | type = ili1_angleType [containingModel]
  | type = ili1_numericRange
  | type = ili1_textType
  | type = ili1_dateType
  | type = ili1_enumerationType
  | type = ili1_horizAlignment
  | type = ili1_vertAlignment
  ;


protected ili1_coord2
  returns [CoordType type]
{
  type = null;
  PrecisionDecimal eMin, nMin, eMax, nMax;
}
  : coord:"COORD2"
    eMin = ili1_decimal
    nMin = ili1_decimal
    eMax = ili1_decimal
    nMax = ili1_decimal

    {
      NumericType easting = null;
      NumericType northing = null;

      try {
        easting = new NumericType (eMin, eMax);
      } catch (Exception ex) {
        reportError (ex, coord.getLine ());
      }

      try {
        northing = new NumericType (nMin, nMax);
      } catch (Exception ex) {
        reportError (ex, coord.getLine ());
      }

      if ((easting != null) && (northing != null))
      {
        try {
          type = new CoordType (
            new NumericType[]
            {
              easting,
              northing
            },
            /* null axis */ 2,
            /* pi/2 axis */ 1);
        } catch (Exception ex) {
          reportError (ex, coord.getLine ());
        }
      } /* if ((easting != null) && (northing != null)) */
    }
  ;


protected ili1_coord3
  returns [CoordType type]
{
  type = null;
  PrecisionDecimal eMin, nMin, hMin, eMax, nMax, hMax;
}
  : coord:"COORD3"
    eMin = ili1_decimal
    nMin = ili1_decimal
    hMin = ili1_decimal
    eMax = ili1_decimal
    nMax = ili1_decimal
    hMax = ili1_decimal

    {
      NumericType easting = null;
      NumericType northing = null;
      NumericType height = null;

      try {
        easting = new NumericType (eMin, eMax);
      } catch (Exception ex) {
        reportError (ex, coord.getLine ());
      }

      try {
        northing = new NumericType (nMin, nMax);
      } catch (Exception ex) {
        reportError (ex, coord.getLine ());
      }

      try {
        height = new NumericType (hMin, hMax);
      } catch (Exception ex) {
        reportError (ex, coord.getLine ());
      }

      if ((easting != null) && (northing != null) && (height != null))
      {
        try {
          type = new CoordType (
            new NumericType[]
            {
              easting,
              northing,
              height
            },
            /* null axis */ 2,
            /* pi/2 axis */ 1);
        } catch (Exception ex) {
          reportError (ex, coord.getLine ());
        }
      } /* if ((easting != null) && (northing != null) && (height != null)) */
    }
  ;


protected ili1_numericRange
  returns [NumericType type]
{
  type = null;
  PrecisionDecimal min = null, max = null;
}
  : lbrac:LBRACE
    min = ili1_decimal
    DOTDOT
    max = ili1_decimal
    RBRACE

    {
      try {
        type = new NumericType (min, max);
      } catch (Exception ex) {
        reportError (ex, lbrac.getLine ());
      }
    }
  ;


protected ili1_dim1Type
  returns [NumericType type]
{
  type = null;
  PrecisionDecimal min = null, max = null;
}
  : dim1:"DIM1"
    min = ili1_decimal
    max = ili1_decimal

    {
      try {
        type = new NumericType (min, max);
        type.setUnit (td.INTERLIS.METER);
      } catch (Exception ex) {
        reportError (ex, dim1.getLine ());
      }
    }
  ;


protected ili1_dim2Type [Model inModel]
  returns [NumericType type]
{
  type = null;
  PrecisionDecimal min = null, max = null;
  ComposedUnit m2 = null;
}
  : dim2:"DIM2"
    min = ili1_decimal
    max = ili1_decimal

    {
      m2 = (ComposedUnit) inModel.getRealElement (ComposedUnit.class, rsrc.getString ("err_unit_ili1_DIM2_name"));
      if (m2 == null)
      {
        try {
          ComposedUnit.Composed timesMeter;

          timesMeter = new ComposedUnit.Composed ('*', td.INTERLIS.METER);
          m2 = new ComposedUnit ();
          m2.setName (rsrc.getString ("err_unit_ili1_DIM2_name"));
          m2.setDocName (rsrc.getString ("err_unit_ili1_DIM2_docName"));
          m2.setBaseUnit (td.INTERLIS.METER);
          m2.setComposedUnits (new ComposedUnit.Composed[] { timesMeter });
          inModel.add (m2);
        } catch (Exception ex) {
          reportInternalError (dim2.getLine ());
          panic ();
        }
      }
      try {
        type = new NumericType (min, max);
        type.setUnit (m2);
      } catch (Exception ex) {
        reportError (ex, dim2.getLine ());
      }
    }
  ;


protected ili1_angleType [Model containingModel]
  returns [NumericType type]
{
  type = null;
  PrecisionDecimal min = null, max = null;
  Unit u = null;
  int lineNumber = 0;
}
  : ( radians:"RADIANS"
      {
        u = td.INTERLIS.RADIAN;
        lineNumber = radians.getLine ();
      }

    | degrees:"DEGREES"
      {
        lineNumber = degrees.getLine ();
        u = (Unit) containingModel.getRealElement (Unit.class,
                                               rsrc.getString ("err_unit_ili1_DEGREES_name"));
        if (u == null)
        {
          try {
            NumericallyDerivedUnit.Factor times180;
            NumericallyDerivedUnit.Factor byPi;

            byPi = new NumericallyDerivedUnit.Factor ('/', PrecisionDecimal.PI);
            times180 = new NumericallyDerivedUnit.Factor ('*', new PrecisionDecimal("180.0"));

            NumericallyDerivedUnit degr = new NumericallyDerivedUnit ();
            degr.setName (rsrc.getString ("err_unit_ili1_DEGREES_name"));
            degr.setDocName (rsrc.getString ("err_unit_ili1_DEGREES_docName"));
            degr.setBaseUnit (td.INTERLIS.RADIAN);
            degr.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              times180,
              byPi
            });
            u = degr;
            containingModel.add (degr);
          } catch (Exception ex) {
            reportInternalError (ex, degrees.getLine());
          }
        } /* if (u == null) */
      }

    | grads:"GRADS"
      {
        lineNumber = grads.getLine ();
        u = (Unit) containingModel.getRealElement (Unit.class,
                                               rsrc.getString ("err_unit_ili1_GRADS_docName"));
        if (u == null)
        {
          try {
            NumericallyDerivedUnit.Factor times200;
            NumericallyDerivedUnit.Factor byPi;

            byPi = new NumericallyDerivedUnit.Factor ('/', PrecisionDecimal.PI);
            times200 = new NumericallyDerivedUnit.Factor ('*', new PrecisionDecimal("200.0"));

            NumericallyDerivedUnit gon = new NumericallyDerivedUnit ();
            gon.setName (rsrc.getString ("err_unit_ili1_GRADS_docName"));
            gon.setDocName (rsrc.getString ("err_unit_ili1_GRADS_docName"));
            gon.setBaseUnit (td.INTERLIS.RADIAN);
            gon.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              times200,
              byPi
            });
            u = gon;
            containingModel.add (gon);
          } catch (Exception ex) {
            reportInternalError (ex, grads.getLine());
          }
        } /* if (u == null) */
      }
    )

    min = ili1_decimal
    max = ili1_decimal

    {
      try {
        type = new NumericType (min, max);
        type.setUnit (u);
      } catch (Exception ex) {
        reportError (ex, lineNumber);
      }
    }
  ;


protected ili1_textType
  returns [TextType type]
{
  type = null;
  int numChars = 0;
}
  : "TEXT"
    star:STAR
    numChars = posInteger
    {
      try {
        type = new TextType (numChars);
      } catch (Exception ex) {
        /* Correct only the case TEXT*0; do not correct other
           weird numbers, because we could not anticipate a
           reasonable alternative */
        if (numChars < 1)
          type = new TextType (1);
        reportError (ex, star.getLine());
      }
    }
  ;


protected ili1_dateType
  returns [TypeAlias type]
{
  type = null;
}
  : date:"DATE"
    {
      type = new TypeAlias ();
      try {
        type.setAliasing (td.INTERLIS.INTERLIS_1_DATE);
      } catch (Exception ex) {
        reportInternalError (ex, date.getLine ());
      }
    }
  ;


protected ili1_enumerationType
  returns [EnumerationType type]
{
  type = null;
  ch.interlis.ili2c.metamodel.Enumeration enumer = null;
}
  : enumer = enumeration [/* extending */ null]
    {
      type = new EnumerationType ();
      try {
        type.setEnumeration (enumer);
      } catch (Exception ex) {
        reportError(ex, 0);
      }
    }
  ;


protected ili1_horizAlignment
  returns [TypeAlias type]
{
  type = null;
}
  : halign:"HALIGNMENT"
    {
      type = new TypeAlias ();
      try {
        type.setAliasing (td.INTERLIS.HALIGNMENT);
      } catch (Exception ex) {
        reportInternalError (ex, halign.getLine ());
      }
    }
  ;


protected ili1_vertAlignment
  returns [TypeAlias type]
{
  type = null;
}
  : valign:"VALIGNMENT"
    {
      type = new TypeAlias ();
      try {
        type.setAliasing (td.INTERLIS.VALIGNMENT);
      } catch (Exception ex) {
        reportInternalError (ex, valign.getLine ());
      }
    }
  ;


protected ili1_lineType [Model forModel, Topic forTopic]
  returns [LineType type]
{
  type = null;
}
  : "POLYLINE"
    {
      type = new PolylineType ();
    }
    ili1_form [type, forModel]
    ili1_controlPoints [type, forModel, forTopic]
    ( ( "WITHOUT" ) => ili1_intersectionDef [type] )?
  ;


protected ili1_areaType [Model forModel, Topic forTopic]
  returns [LineType type]
{
  type = null;
  Container scope;

  if (forTopic != null)
    scope = forTopic;
  else
    scope = forModel;
}
  : (
      "SURFACE"
      {
        type = new SurfaceType ();
      }
      ili1_form [type, forModel]
      ili1_controlPoints [type, forModel, forTopic]
      ( ( "WITHOUT" ) => ili1_intersectionDef [type] )?
    |
      "AREA"
      {
        type = new AreaType ();
      }
      ili1_form [type, forModel]
      ili1_controlPoints [type, forModel, forTopic]
      ili1_intersectionDef [type]
    )

    ( ( "LINEATTR" ) => ili1_lineAttributes [type, scope] )?
  ;


protected ili1_form [LineType lineType, Model forModel]
{
  List lineFormList = null;
  LineForm curLineForm = null;
  LineForm[] lineForms = null;
}
  : with:"WITH"
    LPAREN
    curLineForm = ili1_lineForm [forModel]
    {
      lineFormList = new LinkedList ();
      if (curLineForm != null)
        lineFormList.add (curLineForm);
    }

    (
      COMMA
      curLineForm = ili1_lineForm [forModel]
      {
        if (curLineForm != null)
          lineFormList.add (curLineForm);
      }
    )*

    RPAREN
    {
      lineForms = (LineForm[]) lineFormList.toArray (new LineForm[lineFormList.size()]);
      try {
        if (lineType != null)
          lineType.setLineForms (lineForms);
      } catch (Exception ex) {
        reportError (ex, with.getLine ());
      }
    }
  ;


protected ili1_lineForm [Model forModel]
  returns [LineForm lineForm]
{
  lineForm = null;
}
  : "STRAIGHTS"
    {
      lineForm = td.INTERLIS.STRAIGHTS;
    }

  | "ARCS"
    {
      lineForm = td.INTERLIS.ARCS;
    }

  | expl:EXPLANATION
    {
      /* Check out whether there is already a line form with the same
         explanation. */
      lineForm = addLineFormIfNoSuchExplanation (forModel,
                                                 expl.getText (),
                                                 expl.getLine ());
    }
  ;


protected ili1_intersectionDef [LineType lineType]
{
  PrecisionDecimal maxOverlap = null;
}
  : without:"WITHOUT"
    "OVERLAPS"
    GREATER
    maxOverlap = ili1_decimal
    {
      if ((lineType != null) && (maxOverlap != null))
      {
        try {
          lineType.setMaxOverlap (maxOverlap);
        } catch (Exception ex) {
          reportError (ex, without.getLine ());
        }
      }
    }
  ;


protected ili1_controlPoints [LineType lineType, Model inModel, Topic inTopic]
{
  Type type = null;
  Domain controlPointDomain = null;
}
  : vertex:"VERTEX"
    type = ili1_type [inModel, inTopic]
    ( ( "BASE" ) =>
      base:"BASE" EXPLANATION
      {
        reportWarning (rsrc.getString ("err_lineType_ili1_baseLost"),
                       base.getLine ());
      }
    )?

    {
      Type realType = Type.findReal (type);
      if (realType != null)
      {
        if (realType instanceof CoordType)
        {
          if (!(type instanceof TypeAlias))
          {
            /* In INTERLIS-1, it is possible to define coordinates
               inside the line type. This is not possible with
               INTERLIS-2. So, declare a new "dummy" domain
               if needed.
            */
            controlPointDomain = new Domain ();
            try {
              int numDomains = 0;
              Iterator iter = inModel.iterator();
              while (iter.hasNext())
              {
                if (iter.next() instanceof Domain)
                  numDomains = numDomains + 1;
              }
              controlPointDomain.setName (formatMessage ("err_domain_artificialName",
                                                       Integer.toString (numDomains + 1)));
              controlPointDomain.setType (type);
            } catch (Exception ex) {
              reportError (ex, vertex.getLine ());
              controlPointDomain = null;
            }
          }
          else
            controlPointDomain = ((TypeAlias) type).getAliasing ();
        }
        else
        {
          reportError (formatMessage ("err_lineType_vertexNotCoordType",
                                      type.toString()),
                       vertex.getLine ());
        }
      }

      if (lineType != null)
      {
        try {
          lineType.setControlPointDomain (controlPointDomain);
        } catch (Exception ex) {
          reportError (ex, vertex.getLine());
        }
      } /* if (lineType != null) */
    }
  ;


/* Code changed again because line attributes are handled quite
   differently now. -- Sascha Brawer <brawer@acm.org>, 2000-02-08

   BUG-1999-00001: java.lang.NullPointerException in rule ili1_type
   when parsing a non-empty line attribute specification. Reason was
   that the BeanContext had not been set by ili1_lineAttributes,
   whereas the corresponding INTERLIS-2 always had set the bean context.

   Reported by: Stefan Keller <Stefan.Keller@lt.admin.ch>, 1999-10-04
   Fixed by;    Sascha Brawer <brawer@acm.org>, 1999-10-07
*/
protected ili1_lineAttributes [LineType lineType, Container scope]
{
  Table lineAttrStruct = null;
}
  : att:"LINEATTR" EQUALS
    {
      lineAttrStruct = createImplicitLineAttrStructure (scope, att.getLine());
      try {
        ((SurfaceOrAreaType) lineType).setLineAttributeStructure (lineAttrStruct);
      } catch (Exception ex) {
        reportInternalError (ex, att.getLine ());
        panic ();
      }
    }
    ( ili1_attribute [lineAttrStruct] )+
    ( ili1_identifications [lineAttrStruct] )?
    "END"
  ;


protected ili1_derivatives [Model mainModel]
  : "DERIVATIVES"
    derivativeName:NAME

    ( ili1_domainDefs [mainModel] )?
    ( ili1_topic [mainModel] )+

    "END" derivativeName2:NAME
    {
      if (!derivativeName.getText().equals (derivativeName2.getText ()))
        reportError (formatMessage ("err_end_mismatch",
                                    "DERIVATIVES " + derivativeName.getText (),
                                    derivativeName.getText (),
                                    derivativeName2.getText ()),
                     derivativeName2.getLine ());
    }
  ;


protected ili1_view [Model mainModel]
  : "VIEW"
    viewName:NAME
    (
      NAME DOT NAME COLON
      ili1_viewDef
      ( COMMA ili1_viewDef )*
      SEMI
    )*
    "END"
    viewName2:NAME
    DOT
    {
      reportWarning (formatMessage ("err_view_ili1", viewName.getText ()),
                     viewName.getLine ());
    }
  ;


protected ili1_viewDef
  : "VERTEXINFO" NAME EXPLANATION
  | "WITH" "PERIPHERY" NAME
  | "CONTOUR" NAME ( "WITH" "PERIPHERY" )?
  | LESSMINUS NAME DOT NAME
  ;


protected ili1_format
{
  int lineSize = 0;
  int tidSize = 0;
}
  : "FORMAT"
    ( "FREE"
    | "FIX" "WITH"
      "LINESIZE" EQUALS lineSize = posInteger COMMA
      "TIDSIZE" EQUALS tidSize = posInteger
    )
    SEMI
  ;


protected ili1_encoding
  : "CODE"
    ( ili1_font )?
    ili1_specialCharacter
    ili1_transferId
    "END"
    DOT
  ;


protected ili1_font
  : "FONT"
    EQUALS
    EXPLANATION
    SEMI
  ;


protected ili1_specialCharacter
{
  int blankCode = 0x5f;
  int undefinedCode = 0x40;
  int continueCode = 0x5c;
}
  : "BLANK" EQUALS ( "DEFAULT" | blankCode = code ) COMMA
    "UNDEFINED" EQUALS ( "DEFAULT" | undefinedCode = code ) COMMA
    "CONTINUE" EQUALS ( "DEFAULT" | continueCode = code ) SEMI
  ;


protected ili1_transferId
  : "TID"
    EQUALS
    (
      "I16"
    | "I32"
    | "ANY"
    | EXPLANATION )
    SEMI
  ;

protected code returns [int i]
{
  i = 0;
}
  : p:POSINT { i = Integer.parseInt(p.getText()); }
  | h:HEXNUMBER { i = Integer.parseInt(p.getText(), 16); }
  ;

protected ili1_decimal
	returns [PrecisionDecimal dec]
	{
		dec = null;
	}
	:	d:ILI1_DEC
    	{ dec = new PrecisionDecimal(d.getText()); }
	|	p:POSINT
    	{ dec = new PrecisionDecimal(Double.parseDouble(p.getText()), 0); }
	|	n:NUMBER
	    { dec = new PrecisionDecimal(Double.parseDouble(n.getText()), 0); }
	;

class Ili2Lexer extends Lexer;
options {
  k=3;                   // two characters of lookahead
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


// Single Line comment -- ignored
SL_COMMENT
  : "!!"!
    ( ~('\n'|'\r') )*
    ( '\n' | '\r' ( '\n' )? )
    { $setType(Token.SKIP); newline(); }
  ;


/* multiple line comments
 are ignored.
   This code has been copied from the ANTLR example grammar for
   the Java syntax.
*/
ML_COMMENT
  : "/*"
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

protected DEC
  : NUMBER
    ( DOT POSINT )?
    ( SCALING )?
  ;

protected STRUCTDEC
  : NUMBER
    ( COLON POSINT )+
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
  | ( NUMBER COLON ) => STRUCTDEC { $setType(STRUCTDEC); }
  | '+'! ( POSINT {$setType(NUMBER);} | {$setType(PLUS);})
  | '-' ( POSINT {$setType(NUMBER);} | {$setType(MINUS);})
  | POSINT {$setType(POSINT);}
  ;


NAME
options { testLiterals = true; }
  :  LETTER
     ( LETTER | '_' | DIGIT )*
  ;


// a dummy rule to force vocabulary to be all characters (except special
//   ones that ANTLR uses internally (0 to 2)

protected VOCAB
  : '\3'..'\377'
  ;
