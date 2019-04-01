header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.metamodel.*;
	import ch.interlis.ili2c.CompilerLogEvent;
	import java.util.*;
	import ch.ehi.basics.logging.EhiLogger;
	import ch.ehi.basics.settings.Settings;
}

class Ili1Parser extends Parser;

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
  private Ili1Lexer lexer;
  private antlr.TokenStreamHiddenTokenFilter filter;
  private Map ili1TableRefAttrs;
  private Ili2cMetaAttrs externalMetaAttrs=new Ili2cMetaAttrs();
  /** helps to remember ordering of reference attributes
  */
  private int ili1AttrCounter=0;
  
  /** list of assocs generate from ili1 ref attrs. 
   *  Used to fix names of assocs at end of topic parse.
  */
  private ArrayList ili1assocs=null;

  /** Parse the contents of a stream according to INTERLIS-1 or INTERLIS-2 syntax
      (the version is detected automatically by the parser) and add the
      encountered contents to this TransferDescription.

      @return false if there have been any fatal errors which would lead
              this TransferDescription in an inconsistent state.
  */
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,java.io.Reader stream
    ,int line0Offest
    ,Ili2cMetaAttrs metaAttrs
    )
  {
  	return parseIliFile(td,filename,new Ili1Lexer(stream),line0Offest,metaAttrs);
  }
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,java.io.InputStream stream
    ,int line0Offest
    ,Ili2cMetaAttrs metaAttrs
    )
  {
  	return parseIliFile(td,filename,new Ili1Lexer(stream),line0Offest,metaAttrs);
  }
  static public boolean parseIliFile (TransferDescription td
    ,String filename
    ,Ili1Lexer lexer
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
      Ili1Parser parser = new Ili1Parser (filter);
      if(metaAttrs!=null){
	parser.externalMetaAttrs=metaAttrs;
      }
      
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


  private void reportError (String message, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,message);
  }


  private void reportWarning (String message, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logWarning(filename,lineNumber,message);
  }


  private void reportError (Throwable ex, int lineNumber)
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
  private void reportError (Ili2cSemanticException ex)
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

  private void reportInternalError(int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,formatMessage("err_internalCompilerError", /* exception */ ""));
  }


  private void reportInternalError (Throwable ex, int lineNumber)
  {
      String filename=getFilename();
      CompilerLogEvent.logError(filename,lineNumber,formatMessage("err_internalCompilerError", ""),ex);
  }


  private String formatMessage (String msg, Object[] args)
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


  private String formatMessage(String msg, String arg) {
    return formatMessage(msg, new Object[] { arg });
  }


  private String formatMessage(String msg, String arg1, String arg2) {
    return formatMessage(msg, new Object[] { arg1, arg2 });
  }

  private String formatMessage(String msg, String arg1, String arg2, String arg3) {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3 });
  }

  public static void panic ()
  {
    throw new antlr.ANTLRError();
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

  private Table createImplicitLineAttrStructure (Container container, Viewable table,int lineNumber)
  {
    Table result = new Table ();

    ++numIli1LineAttrStructures;
    try {
      result.setName ("LineAttrib" + numIli1LineAttrStructures);
      result.setIdentifiable (false); /* make it a structure */
      result.setIli1LineAttrStruct(true);
      if(container instanceof Topic && table!=null){
	      ((Topic)container).addBefore (result,table);
      }else if(container instanceof Model && table!=null){
	      ((Model)container).addBefore (result,table);
      }else{
	      container.add (result);
      }
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
  private LineForm addLineFormIfNoSuchExplanation (Container scope, String explanation, int line)
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
	
	private String getIliDoc()
	throws antlr.TokenStreamException
	{ 
		String ilidoc=null;
		antlr.Token cmtToken=filter.getHiddenBefore((antlr.CommonHiddenStreamToken)LT(1));
		if(cmtToken!=null){
			ilidoc=cmtToken.getText().trim();
			if(ilidoc.startsWith("/**")){
				ilidoc=ilidoc.substring(3);
			}
			if(ilidoc.endsWith("*/")){
				ilidoc=ilidoc.substring(0,ilidoc.length()-2);
			}
			java.io.LineNumberReader lines=new java.io.LineNumberReader(new java.io.StringReader(ilidoc.trim()));
			String line;
			StringBuilder buf=new StringBuilder();
			String sep="";
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
			ilidoc=buf.toString();
			if(ilidoc.length()==0){
				ilidoc=null;
			}
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
	:	(
			interlis1Def
		)
	    exception
		    catch [NoViableAltException nvae]
		    {
		      reportError (rsrc.getString ("err_notIliDescription"));
		      canProceed = false;
		    }
	;
	
protected enumeration [Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration enumer]
{
  List elements = new LinkedList();
  ch.interlis.ili2c.metamodel.Enumeration.Element curElement;
  enumer = null;
}
  : lp:LPAREN
    (
      (curElement = enumElement[extending] { elements.add(curElement); }
        (COMMA curElement = enumElement[extending] 
    	  { 
		  Iterator elei=elements.iterator();
		  while(elei.hasNext()){
			  ch.interlis.ili2c.metamodel.Enumeration.Element ele=(ch.interlis.ili2c.metamodel.Enumeration.Element)elei.next();
			  if(ele.getName().equals(curElement.getName())){
				  reportError(formatMessage("err_enumerationType_DupEle",curElement.getName()),curElement.getSourceLine());
				  break;
			  }
		  }
	  	elements.add(curElement); 
	  }
        )*
      )
    )
    RPAREN
    {
      enumer = new ch.interlis.ili2c.metamodel.Enumeration(elements);
      enumer.setFinal(false);
      enumer.setSourceLine(lp.getLine());
    }
  ;



protected enumElement [Type extending]
  returns [ch.interlis.ili2c.metamodel.Enumeration.Element ee]
{
  ch.interlis.ili2c.metamodel.Enumeration subEnum = null;
  ch.interlis.ili2c.metamodel.Enumeration.Element curEnum = null;
  ee = null;
  int lineNumber = 0;
  String ilidoc=null;
  Settings metaValues=null;
}
  :  { ilidoc=getIliDoc();metaValues=getMetaValues();}
   	en:NAME
    ( subEnum = enumeration[extending] )?

    {
          if (subEnum == null)
          {
	    // new leaf
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               en.getText());
	    ee.setDocumentation(ilidoc);
	    ee.setMetaValues(metaValues);
	    ee.setSourceLine(en.getLine());
          }
          else
          {
	    // new subtree
            ee = new ch.interlis.ili2c.metamodel.Enumeration.Element (
               en.getText(),
               subEnum);
	    ee.setDocumentation(ilidoc);
	    ee.setMetaValues(metaValues);
	    ee.setSourceLine(en.getLine());
          }
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
	
/****************************************************************
* INTERLIS 1
*/

protected interlis1Def
{
  Model   model = null;
  Ili1Format format = new Ili1Format();
  String ilidoc=null;
  Settings metaValues=null;
}
  : "TRANSFER" transferName:NAME SEMI
    {
      model = new DataModel ();
      try {
        td.setName (transferName.getText ());
        model.setName (transferName.getText ());
	model.setIliVersion(Model.ILI1);
      } catch (Exception ex) {
        reportError (ex, transferName.getLine ());
      }
    }

    ( ili1_domainDefs [model] )?

    { ilidoc=getIliDoc();metaValues=getMetaValues();}
	"MODEL" modelName:NAME
    {
      try {
        model.setName (modelName.getText ());
        model.setSourceLine (modelName.getLine());
        model.setFileName(getFilename());
	    model.setDocumentation(ilidoc);
	    model.setMetaValues(metaValues);
        String translationOfName=externalMetaAttrs.getMetaAttrValue(model.getName(),Ili2cMetaAttrs.ILI2C_TRANSLATION_OF);
        if(translationOfName!=null){
        	Model translationOf=(Model)td.getElement(Model.class,translationOfName);
		if(translationOf==null){
	        	reportError(formatMessage("err_noSuchModel", translationOfName), modelName.getLine());
		}else{
			model.setTranslationOf(translationOf.getName(),translationOf.getModelVersion());
		}
        }
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

    "END" modelName2:NAME endDot:DOT
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
	       try {
			List<Ili2cSemanticException> errs=new java.util.ArrayList<Ili2cSemanticException>();	       		
	         model.checkIntegrity (errs);
	         reportError(errs);
	       } catch (Ili2cSemanticException ex) {
	         reportError (ex);
	       } catch (Exception ex) {
	         reportError (ex, endDot.getLine());
	       }
    }

    ( ili1_derivatives [model] )?
    ( ili1_view [model] )*
    ili1_format[format]
    ili1_encoding[format]
    {td.setIli1Format(format);}
    EOF
  ;



protected ili1_domainDefs [Container container]
{
  Model model = (Model) container.getContainerOrSame (Model.class);
  Topic topic = (Topic) container.getContainerOrSame (Topic.class);
  Type type = null;
  String ilidoc=null;
  Settings metaValues=null;
}
  : "DOMAIN"
    (
      { ilidoc=getIliDoc();metaValues=getMetaValues();}
      domainName:NAME
      EQUALS
      type = ili1_attributeType [model, topic,null]
      SEMI
      {
        Domain domain = null;

        try {
          domain = new Domain ();
          domain.setSourceLine(domainName.getLine ());
          domain.setName (domainName.getText ());
	      domain.setDocumentation(ilidoc);
	      domain.setMetaValues(metaValues);
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
  String ilidoc=null;
  Settings metaValues=null;
}
  :
      { ilidoc=getIliDoc();metaValues=getMetaValues();}
  "TOPIC" topicName:NAME EQUALS
    {
      ili1assocs=new ArrayList();
      topic = new Topic ();
      try {
        topic.setSourceLine(topicName.getLine());
        topic.setName (topicName.getText ());
	    topic.setDocumentation(ilidoc);
	    topic.setMetaValues(metaValues);
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
    {
    	// fix assocs name
	Iterator associ=ili1assocs.iterator();
	while(associ.hasNext()){
		AssociationDef assoc=(AssociationDef)associ.next();
		int uniqueName=2;
		String assocName=assoc.getName();
		String assocBasename=assocName.substring(0,assocName.indexOf(':'));
		assocName=assocBasename;
		boolean assocNameConflict=false;
		do{
			assocNameConflict=false;
			Iterator elei=topic.iterator();
			while (elei.hasNext()){
			      Element ele = (Element)elei.next();
			      if(ele!=assoc && ele.getName().equals(assocName)){
				assocNameConflict=true;
				break;
			      }
			}
			if(!assocNameConflict){
				try{
					assoc.setName(assocName);
				}catch(java.beans.PropertyVetoException ex){
					assocNameConflict=true;
				}
			}
			if(assocNameConflict){
				assocName=assocBasename+Integer.toString(uniqueName);
				uniqueName++;
			}
		}while(assocNameConflict);
	}
    }
  ;


protected ili1_table [Topic topic]
{
  boolean optional = false;
  Table   table = null;
  ili1AttrCounter=0;
  String ilidoc=null;
  Settings metaValues=null;
}
  : 
    { ilidoc=getIliDoc();metaValues=getMetaValues();}  
  ( "OPTIONAL" { optional = true; } )?
    "TABLE"
    tableName:NAME
    EQUALS
    {
      table = new Table ();
      ili1TableRefAttrs=new HashMap();
      try {
        table.setName (tableName.getText ());
        table.setSourceLine(tableName.getLine());
	    table.setDocumentation(ilidoc);
	    table.setMetaValues(metaValues);
        table.setAbstract (false);
	table.setIli1Optional(optional);
        topic.add (table);
      } catch (Exception ex) {
        reportError (ex, tableName.getLine ());
      }
    }

    ( ili1_attribute [table] 
    	{
		ili1AttrCounter++;
	}
    )+
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
  String ilidoc=null;
  Settings metaValues=null;
  
  String explText=null;
}
  : 
    { ilidoc=getIliDoc();metaValues=getMetaValues();}
  attributeName:NAME
    col:COLON
    ( "OPTIONAL" { optional = true; } )?

    ( type = ili1_localAttributeType [model, topic,table]
      {
        attrib = new LocalAttribute ();
	    attrib.setDocumentation(ilidoc);
	    attrib.setMetaValues(metaValues);
      }

    | POINTSTO
      tabnam:NAME
      {
        AssociationDef assoc=new AssociationDef();
	assoc.setSourceLine(tabnam.getLine ());
        Table referred = (Table) topic.getRealElement (Table.class, tabnam.getText ());
        if (referred == null)
        {
          reportError (formatMessage ("err_noSuchTable", tabnam.getText(),
                                      topic.toString ()),
                       tabnam.getLine ());
	  assoc.setDirty(true);
        }
	if(referred==table){
          reportError (formatMessage ("err_relAttribute_recursive", attributeName.getText(),tabnam.getText()),
                       tabnam.getLine ());
	  assoc.setDirty(true);
	}
        try {
	String thisRoleName=table.getName();
	String oppendRoleName=attributeName.getText();
	// ensure thisRoleName is unique in referred table and 
	// that it is different from oppendRoleName
	int uniqueName=2;
	String thisRoleBasename=thisRoleName;
	if(thisRoleName.equals(oppendRoleName)){
		thisRoleName=thisRoleName+Integer.toString(uniqueName);
		uniqueName++;
	}
	boolean roleNameConflict=false;
	do{
		roleNameConflict=false;
		if(referred.getElement(AttributeDef.class,thisRoleName)!=null){
			roleNameConflict=true;
		}else{
			Iterator rolei=referred.getOpposideRoles();
			while (rolei.hasNext()){
			      RoleDef targetOppRole = (RoleDef)rolei.next();
			      if(targetOppRole.getName().equals(thisRoleName)){
				roleNameConflict=true;
				break;
			      }
			}
		}
		if(roleNameConflict){
			thisRoleName=thisRoleBasename+Integer.toString(uniqueName);
			uniqueName++;
		}
	}while(roleNameConflict);
	// ensure assocName is unique in topic
	// use temporary/illegal name, fix it at end of topic
	String assocName=thisRoleName+oppendRoleName+":"+ili1assocs.size();
        assoc.setName(assocName);
        RoleDef role1=new RoleDef(true);
	role1.setSourceLine(tabnam.getLine ());
	role1.setIli1AttrIdx(ili1AttrCounter);
        role1.setName(thisRoleName);
	ReferenceType role1ref=new ReferenceType();
	role1ref.setReferred(table);
        role1.setReference(role1ref);
        role1.setCardinality(new Cardinality(0, Cardinality.UNBOUND));
        assoc.add(role1);
        RoleDef role2=new RoleDef(true);
	role2.setSourceLine(tabnam.getLine ());
        role2.setName(oppendRoleName);
	ReferenceType role2ref=new ReferenceType();
	role2ref.setReferred(referred);
        role2.setReference(role2ref);
        role2.setCardinality(new Cardinality(optional?0:1, 1));
        assoc.add(role2);
        topic.add(assoc);
        assoc.fixupRoles();
	// remember assoc to fix name at end of topic
	ili1assocs.add(assoc);
	// remember map from attrname to role for IDENT parsing
	// has to be attrname (and not rolename!) to be able to find role when parsing IDENTs
	ili1TableRefAttrs.put(attributeName.getText(),role2); 
        } catch (Exception ex) {
          reportError (ex, tabnam.getLine ());
        }
      }
    )

    (
      expl:EXPLANATION
      {
        explText=expl.getText();
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
        attrib.setSourceLine(attributeName.getLine());
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
      
      attrib.setExplanation(explText);

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
  List<String> ll = null;
  boolean ignore=false;
  boolean selfStanding=false;
}
  : "NO" "IDENT"
  | "IDENT"
    (
      anam:NAME
      {
	selfStanding=false;
        ll = new LinkedList<String>();
        ll.add (anam.getText());
      }
      (
        COMMA
        bnam:NAME
        {
            ll.add(bnam.getText());
        }
      )*
      semi:SEMI
      {
        try {
		UniqueEl uel=new UniqueEl();
		for(int i=0;!ignore && i<ll.size();i++){
		String attrnam = ll.get(i);
		AttributeDef curAttribute = (AttributeDef) table.getRealElement (
		  AttributeDef.class,
		  attrnam);
		if (curAttribute != null){
			AttributeRef[] attrRef=new AttributeRef[1];
			attrRef[0]=new AttributeRef(curAttribute);
			ObjectPath path;
			path=new ObjectPath(table,attrRef);
			uel.addAttribute(path);
		}else{
		  if(!ili1TableRefAttrs.containsKey(attrnam)){
			  reportError (formatMessage ("err_attributePath_unknownAttr_short",
					      attrnam,
					      table.toString ()),
			       semi.getLine ());
			ignore=true;
		  }else{
			PathElAbstractClassRole[] roleRef=new PathElAbstractClassRole[1];
			roleRef[0]=new PathElAbstractClassRole((RoleDef)ili1TableRefAttrs.get(attrnam));
			ObjectPath path;
			path=new ObjectPath(table,roleRef);
			uel.addAttribute(path);
			selfStanding=true;
		  }
		}
	  }
	  if(!ignore){
		  /* Construct a new uniqueness constraint */
		  UniquenessConstraint constr = new UniquenessConstraint();
		  constr.setElements(uel);
		  constr.setSelfStanding(selfStanding);
		  /* Add it to the table. */
		  table.add (constr);
	  }else{
	        reportWarning("IDENT constraint lost",
                       semi.getLine ());
	  }
        } catch (Exception ex) {
          reportError (ex, semi.getLine ());
        }
      }
    )+
  ;


protected ili1_localAttributeType [Model forModel, Topic forTopic,Viewable table]
  returns [Type type]
{
  type = null;
}
  : type = ili1_type [forModel, forTopic,table]
  ;


protected ili1_type [Model forModel, Topic forTopic,Viewable table]
  returns [Type type]
{
  type = null;
}
  : type = ili1_attributeType [forModel, forTopic,table]
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


protected ili1_attributeType [Model forModel, Topic forTopic,Viewable table]
  returns [Type type]
{
  type = null;
}
  : type = ili1_baseType [forModel]
  | type = ili1_lineType [forModel, forTopic]
  | type = ili1_areaType [forModel, forTopic,table]
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

          m2 = new ComposedUnit ();
          m2.setName (rsrc.getString ("err_unit_ili1_DIM2_name"));
          m2.setDocName (rsrc.getString ("err_unit_ili1_DIM2_docName"));
          m2.setComposedUnits (new ComposedUnit.Composed[] { 
		  new ComposedUnit.Composed ('*', td.INTERLIS.METER)
		  ,new ComposedUnit.Composed ('*', td.INTERLIS.METER)
	  });
          inModel.addPreLast (m2); // before reference
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
            degr.setExtending (td.INTERLIS.RADIAN);
            degr.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              times180,
              byPi
            });
            u = degr;
            containingModel.addPreLast (degr);
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
            gon.setExtending (td.INTERLIS.RADIAN);
            gon.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              times200,
              byPi
            });
            u = gon;
            containingModel.addPreLast (gon);
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


protected ili1_areaType [Model forModel, Topic forTopic,Viewable table]
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
        type = new SurfaceType (true);
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

    ( ( "LINEATTR" ) => ili1_lineAttributes [type, scope,table] )?
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
    type = ili1_type [inModel, inTopic,null]
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
	      inModel.addPreLast(controlPointDomain);
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
protected ili1_lineAttributes [LineType lineType, Container scope,Viewable table]
{
  Table lineAttrStruct = null;
}
  : att:"LINEATTR" EQUALS
    {
      lineAttrStruct = createImplicitLineAttrStructure (scope, table, att.getLine());
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


protected ili1_format[Ili1Format format]
{
  int lineSize = 0;
  int tidSize = 0;
}
  : "FORMAT"
    ( "FREE"  {format.isFree=true;}
    | "FIX" "WITH"
      "LINESIZE" EQUALS lineSize = posInteger COMMA
      "TIDSIZE" EQUALS tidSize = posInteger
      	{
      	format.isFree=true;
	format.lineSize=lineSize;
	format.tidSize=tidSize;
	}
    )
    SEMI
  ;


protected ili1_encoding[Ili1Format format]
  : "CODE"
    ( ili1_font[format] )?
    ili1_specialCharacter[format]
    ili1_transferId[format]
    "END"
    DOT
  ;


protected ili1_font[Ili1Format format]
  : "FONT"
    EQUALS
    expl:EXPLANATION
    SEMI
    {format.font=expl.getText();}
  ;


protected ili1_specialCharacter[Ili1Format format]
{
  int blankCode = 0x5f;
  int undefinedCode = 0x40;
  int continueCode = 0x5c;
}
  : "BLANK" EQUALS ( "DEFAULT" | blankCode = code ) COMMA
    "UNDEFINED" EQUALS ( "DEFAULT" | undefinedCode = code ) COMMA
    "CONTINUE" EQUALS ( "DEFAULT" | continueCode = code ) SEMI
	{
	  format.blankCode=blankCode;
	  format.undefinedCode=undefinedCode;
	  format.continueCode=continueCode;
	}
  ;


protected ili1_transferId[Ili1Format format]
  : "TID"
    EQUALS
    (
      "I16"  {format.tidKind=Ili1Format.TID_I16;}
    | "I32" {format.tidKind=Ili1Format.TID_I32;}
    | "ANY" {format.tidKind=Ili1Format.TID_ANY;}
    | exp:EXPLANATION {format.tidKind=Ili1Format.TID_EXPLANATION;
    	format.tidExplanation=exp.getText();
    	}
    )
    SEMI
  ;

protected code returns [int i]
{
  i = 0;
}
  : p:POSINT { i = Integer.parseInt(p.getText()); }
  | h:HEXNUMBER { i = Integer.parseInt(h.getText().substring(2), 16); }
  ;

protected ili1_decimal
	returns [PrecisionDecimal dec]
	{
		dec = null;
	}
	:	d:ILI1_DEC
    	{ dec = new PrecisionDecimal(d.getText()); }
	|	p:POSINT
    	{ dec = new PrecisionDecimal(p.getText()); }
	|	n:NUMBER
	    { dec = new PrecisionDecimal(n.getText()); }
	;

class Ili1Lexer extends Lexer;
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
  : "/*" 
  
  	('\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r')
      )
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

STAR options { paraphrase = "'*'"; }
  : '*'
  ;

GREATER options { paraphrase = "'>'"; }
  : '>'
  ;


SEMI options { paraphrase = "';'"; }
  : ';'
  ;


EQUALS options { paraphrase = "'='"; }
  : '='
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
  | ( NUMBER ( '.' | 'S' ))  => ( ILI1_DEC { $setType(ILI1_DEC); } )
  | '+'! ( POSINT {$setType(NUMBER);} | {$setType(PLUS);})
  | '-' ( POSINT {$setType(NUMBER);} | {$setType(MINUS);})
  | POSINT {$setType(POSINT);}
  ;


NAME
options { testLiterals = true; }
  :  LETTER
     ( LETTER | '_' | DIGIT )*
  ;
