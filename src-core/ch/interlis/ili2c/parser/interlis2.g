/* ************************************************/
/*                                                */
/* interlis2.g                                    */
/* -----------                                    */
/* ANTLR Grammar for INTERLIS-2                   */
/* see http://www.antlr.org                       */
/*                                                */
/* Sascha Brawer <sb@adasys.ch>                   */
/*                                                */
/* ************************************************/

header
{
package ch.interlis.parser;
import ch.interlis.*;
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
  protected TransferDescription td;
  
  ResourceBundle rsrc = ResourceBundle.getBundle(
    "ch.interlis.ErrorMessages",
    Locale.getDefault());
  
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
    // exception.printStackTrace();
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
  
  public void reportError (antlr.ParserException ex)
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
    
    return (Model) td.getElement (Model.class, modelName);
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
      Topic referredTopic = (Topic) scopeModel.getElement (Topic.class, modelName);
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
    
    topic = (Topic) model.getElement(Topic.class, topicName);
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
  


  protected MetaObject resolveMetaObject (Model model, Table polymorphicTo, String name, int line)
  {
    List matching = model.findMatchingMetaObjects (polymorphicTo, name);
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
                                  model.toString()),
                   line);
      return null;
    }
  }

  protected MetaObject resolveMetaObject_inTable (Model model, Table table, String name, int line)
  {
    List matching = model.findMatchingMetaObjects (table, name);
    
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
                                  table.getScopedName (null) + "." + name,
                                  model.toString()),
                   line);
      return null;
    }
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
        model.setContractIssuer (rsrc.getString ("err_lineForm_ili1_artificialContractorName"));
        model.setContractExplanation (rsrc.getString ("err_lineForm_ili1_artificialContractExplanation"));
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

    overriding =  (AttributeDef) container.getElement (AttributeDef.class, name);
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


public interlisDescription [TransferDescription td]
  returns [boolean canProceed]
{
  canProceed = true;
  this.td = td;
  this.modelInterlis = td.INTERLIS;
  this.predefinedBooleanType = Type.findReal (td.INTERLIS.BOOLEAN.getType());
}
  : ( interlis2Def [td] | interlis1Def [td] )

    exception catch [NoViableAltException nvae]
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


protected interlis2Def [TransferDescription td]
{
  PrecisionDecimal version;
}
  : ili:"INTERLIS"
    version = decimal
    {
      if (version.floatValue() != 2.0) {
        reportError(formatMessage("err_wrongInterlisVersion",version.toString()),
                    ili.getLine());
        panic();
      }
    }
    SEMI

    ( "LANGUAGE" EQUALS n:NAME SEMI
      {
        try {
          td.setLanguage(n.getText());
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
      }
    )?

    ( modelDef[td] )*
    
    EOF
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
  : a:"ABSTRACT"
    {
      if ((acceptable & 0x01) == 0)
        reportError(rsrc.getString("err_cantBeAbstract"),
                    a.getLine());
      else
        mod = 0x01;
      
      if ((encountered & mod) != 0)
        reportWarning (rsrc.getString ("err_multipleAbstract"),
                       a.getLine());
    }

  | f:"FINAL"
    {
      if ((acceptable & 0x02) == 0)
        reportError(rsrc.getString ("err_cantBeFinal"),
                    f.getLine());
       else
         mod = 0x02;

      if ((encountered & mod) != 0)
        reportWarning (rsrc.getString ("err_multipleFinal"),
                       f.getLine());
    }

  | e:"EXTENDED"
    {
      if ((acceptable & 0x04) == 0)
        reportError(rsrc.getString ("err_cantBeExtended"),
                    e.getLine());
       else
         mod = 0x04;

      if ((encountered & mod) != 0)
        reportWarning (rsrc.getString("err_multipleExtended"),
                       e.getLine());
    }
  ;


protected end [Element elt]
  : "END"
    nam:NAME
    {
      if (elt != null)
      {
        if (!nam.getText().equals(elt.getName()))
          reportError(
            formatMessage ("err_end_mismatch", elt.toString(),
                           elt.getName(), nam.getText()),
            nam.getLine());
      }
    }
  ;


/* --------------------------------------------------------------------- */
/*  Model                                                                */
/* --------------------------------------------------------------------- */

protected modelDef [TransferDescription transfer]
{
  Model md = null;
  String[] importedNames = null;
  Table tabDef;
  int mods = 0;
  Importable imported;
}
  :  (
       "DATA" "MODEL" { md = new DataModel(); }
     | "GRAPHIC" "MODEL" { md = new GraphicModel(); }
     | "REFSYSTEM" "MODEL" { md = new RefSystemModel(); }
     | "SYMBOLOGY" "MODEL" { md = new SymbologyModel(); }
     | "TYPE" "MODEL" { md = new TypeModel(); }
     | "VIEW" "MODEL" { md = new ViewModel(); }
     | modl:"MODEL"
       {
         md = new DataModel();
         reportError (rsrc.getString("err_modelTypeMissing"),
                      modl.getLine());
       }
     )

     n1:NAME
     {
       try {
         md.setName(n1.getText());
         transfer.add(md);
       } catch (Exception ex) {
         reportError(ex, n1.getLine());
         panic();
       }
     }


     EQUALS
     
     ( contr:"CONTRACT" "ISSUED" "BY" contrIssuer:NAME
       {
         try {
           md.setContractIssuer (contrIssuer.getText());
         } catch (Exception ex) {
           reportError(ex, contrIssuer.getLine());
         }
       }
       
       ( contrExpl:EXPLANATION
         {
           try {
             md.setContractExplanation (contrExpl.getText());
           } catch (Exception ex) {
             reportError(ex, contrExpl.getLine());
           }
         }
       )?
       SEMI
     )?
     
     ( "IMPORTS"
       imported = importable[transfer]
       {
         if (imported != null)
           md.addImport(imported);
       }
       
       (
         COMMA!
         imported = importable[transfer]
         {
           if (imported != null)
             md.addImport(imported);
         }
       )*
       SEMI
     )*

     ( unitDefs [md]
     | function [md]
     | lineFormTypeDef [md]
     | domainDefs [md]
     | "PARAMETER" ( parameterDef[md] )*
     | topicDef [md]
     | tableDef [md]
     | graphicDef [md]
     | viewDef [md]
     | projectionDef [md]
     )*


     end [md]
     endDot:DOT
     
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



protected importable [Container scope]
  returns [Importable m]
{
  m = null;
}
  : n:NAME
    {
      m = resolveOrFixModelName(scope, n.getText(), n.getLine());
    }
    (
      DOT
      d:NAME
      {
        Model model = (Model) m;
        Iterator iter = model.getDataContainers ();
        m = null;
        while (iter.hasNext ())
        {
          DataContainer dc = (DataContainer) iter.next ();
          if (dc.getName().equals (d.getText()))
          {
            m = dc;
            break;
          }
        }
        if (m == null)
        {
          reportError (formatMessage ("err_metaObject_unknownContainer",
                                      d.getText(), model.getName()),
                       d.getLine());
        }
      }
    )?
  ;


/* --------------------------------------------------------------------- */
/*  Topic                                                                */
/* --------------------------------------------------------------------- */

protected topicDef[Container container]
{
  Topic topic = null;
  Topic extending = null;
  Topic depTopic = null;
  int mods;
}
  : "TOPIC"
    n1:NAME
    {
      try {
        topic = new Topic();
        topic.setName(n1.getText());
      } catch (Exception ex) {
        reportError(ex, n1.getLine());
      }
    }
    
    mods = properties[/* acceptable: ABSTRACT | FINAL */ 3]
    {
      try {
        container.add(topic);
        topic.setAbstract((mods & 1) != 0);
        topic.setFinal((mods & 2) != 0);
      } catch (Exception ex) {
        reportError(ex, n1.getLine());
      }
    }

    ( extToken:"EXTENDS"
      extending=topicRef[container]
      {
        try {
          topic.setExtending(extending);
        } catch (Exception ex) {
          reportError(ex, extToken.getLine());
        }
      }
    )?
    
    EQUALS

    ( "DEPENDS" on:"ON"
      depTopic = topicRef [/*scope*/ topic]
      {
        try {
          topic.makeDependentOn(depTopic);
        } catch (Exception ex) {
          reportError(ex, on.getLine());
        }
      }
      (
        com:COMMA
        depTopic = topicRef[topic]
        {
          try {
            topic.makeDependentOn(depTopic);
          } catch (Exception ex) {
            reportError(ex, com.getLine());
          }
        }
      )*
      SEMI
    )?
    
    (
      unitDefs [topic]
    | domainDefs [topic]
    | tableDef [topic]
    | viewDef [topic]
    | projectionDef [topic]
    | graphicDef [topic]
    )*
    
    end [topic]
    SEMI
  ;


protected topicRef [Container scope]
  returns [Topic topic]
{
  List      nams = new LinkedList();
  topic = null;
  int lin = 0;
}
  : lin = names2[nams]
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


/* --------------------------------------------------------------------- */
/*  Table                                                                */
/* --------------------------------------------------------------------- */

protected tableDef [Container container]
{
  Table table = null;
  Table extending = null;
  Table overwriting = null;
  boolean identifiable = true;
  Constraint constr = null;
  int mods;
}
  : ( "CLASS" | ( "STRUCTURE" { identifiable = false; } ))
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
      
      overwriting = (Table) container.getElement
                                       (Table.class, n1.getText());
    }
    
    mods = properties[/* ABSTRACT | FINAL | EXTENDED */ 7]

    {
      try {
        table.setAbstract((mods & 1) != 0);
        table.setFinal((mods & 2) != 0);
        if ((mods & 4) != 0)
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
    
    ( extToken:"EXTENDS" extending=tableRef[container]
      {
        if ((mods & 4) != 0)
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
    )?
        
    EQUALS
    {
      try {
        container.add (table);
      } catch (Exception ex) {
        reportError (ex, n1.getLine ());
        panic ();
      }
    }

    ( "ATTRIBUTE" )?  ( attribute[table] )*
    
    ( constr=constraintDef[table]
      {
        if (constr != null)
          table.add (constr);
      }
    )*

    ( "PARAMETER" ( parameterDef[table] )* )?
    
    end [table]
    SEMI
  ;
  

protected tableRef [Container scope]
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
        t = (Table) modelInterlis.getElement(Table.class, tableName);
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
      Topic    topic;
      String   modelName, topicName;
      
      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (Topic) scope.getContainerOrSame(Topic.class);
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
        t = (Table) modelInterlis.getElement (Table.class, tableName);
        
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


/* --------------------------------------------------------------------- */
/*  View                                                                 */
/* --------------------------------------------------------------------- */

protected viewDef [Container container]
{
  int mods = 0;
  Viewable extending = null;
  Viewable overwriting = null;
  View view = null;
  ViewableAlias selected = null;
  ViewableAlias[] aliases = null;
  Selection select = null;
  Constraint constr = null;
  Viewable decomposedViewable = null;
  AttributePath decomposedAttribute = null;
  NameSpace ns = new NameSpace (container);
  NameSpace subns = ns;
  boolean areaDecomp = false;
}
  : "VIEW"
    n:NAME
    {
      overwriting = (Viewable) container.getElement (Viewable.class, n.getText());
    }
    mods = properties[/* none */ 0]
    (
      ext:"EXTENDS"
      extending = viewableRef [container]
      {
        if ((mods & 4) != 0)
          reportError(rsrc.getString("err_extendedWithExtends"),
                      ext.getLine());
      }
    )?

    eq:EQUALS
    
    ( sel:"SELECTION"
      "OF"
      selected=renamedViewableDef[container]
      SEMI
      {
        view = new SelectionView();
        try {
          ((SelectionView) view).setSelected(selected);
        } catch (Exception ex) {
          reportError(ex, sel.getLine());
        }
      }
      
    | join:"JOIN"
      "OF"
      aliases = renamedViewableDefs[container]
      SEMI
      {
        view = new JoinView();
        try {
          view.setName (n.getText());
          ((JoinView) view).setJoining(aliases);
        } catch (Exception ex) {
          reportError(ex, join.getLine());
        }
      }
      
    | uni:"UNION"
      "OF"
      aliases = renamedViewableDefs[container]
      SEMI
      {
        view = new UnionView();
        try {
          view.setName (n.getText());
          ((UnionView) view).setUnited(aliases);
        } catch (Exception ex) {
          reportError(ex, uni.getLine());
        }
      }
      
    | (
        "AREA"
        {
          areaDecomp = true;
        }
      )?
    
      decomp:"DECOMPOSITION"
      "OF"
      decomposedViewable = viewableRef [container]
      COLON
      decomposedAttribute = attributePathAfterColon [new ViewableAlias (null, decomposedViewable)]
      SEMI
      {
        view = new DecompositionView ();
        try {
          view.setName (n.getText());
          ((DecompositionView) view).setDecomposedAttribute (decomposedAttribute);
        } catch (Exception ex) {
          reportError(ex, decomp.getLine());
        }

        try {
          ((DecompositionView) view).setAreaDecomposition (areaDecomp);
        } catch (Exception ex) {
          reportError(ex, decomp.getLine());
        }
      }
    )?
    
    {
      if ((extending != null) && !(extending instanceof View))
      {
        reportError(
          formatMessage("err_viewExtendsNonview", extending.toString()),
          ext.getLine());
        extending = null;
      }
      

      if ((mods & 4) != 0)
      {
        if (overwriting == null)
        {
          reportError (formatMessage (
            "err_view_nothingToExtend",
            n.getText(),
            container.toString()),
            n.getLine());
        }
        else
          extending = overwriting;
      }
      else if (overwriting != null)
      {
        reportError (formatMessage ("err_view_extendedWithoutDecl",
                                    n.getText (),
                                    container.toString (),
                                    overwriting.toString ()),
                     n.getLine ());

        /* Try to fix */
        extending = overwriting;
      }
      

      if (view == null) {
        if (extending == null) {
          reportError(rsrc.getString("err_viewNotExtending"), eq.getLine());
          panic();
        }
        
        try {
          if (extending instanceof JoinView)
          {
            view = new JoinView ();
            ((JoinView) view).setJoining (
              ((JoinView) extending).getJoining ());
          }
          else if (extending instanceof UnionView)
          {
            view = new UnionView ();
            ((UnionView) view).setUnited (
              ((UnionView) extending).getUnited ());
          }
          else if (extending instanceof SelectionView)
          {
            view = new SelectionView ();
            ((SelectionView) view).setSelected (
              ((SelectionView) extending).getSelected ());
          }
          else
            throw new IllegalStateException ();
        } catch (Exception ex) {
          reportError(ex, eq.getLine());
          panic ();
        }
      }

      try {
        view.setName(n.getText());
      } catch (Exception ex) {
        reportError(ex, n.getLine());
        panic();
      }


      try {
        view.setExtending((View) extending);
      } catch (Exception ex) {
        reportError(ex, ext.getLine());
      }

      try {
        view.setAbstract((mods & 1) != 0); 
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }

      try {
        view.setFinal((mods & 2) != 0); 
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
      
      try {
        container.add(view);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
      
      subns = new NameSpace (view);
    }

    (
      select = selection [subns, view]
      selSemi:SEMI
      {
        try {
          if (select != null)
            view.add (select);
        } catch (Exception ex) {
          reportError (ex, selSemi.getLine ());
        }
      }
    )*
    
    projectionAttributes [view]
    
    ( constr=constraintDef[view]
      {
        if (constr != null)
          view.add (constr);
      }
    )*
    
    end [view]
    SEMI
  ;
  

/* --------------------------------------------------------------------- */
/*  Projection                                                           */
/* --------------------------------------------------------------------- */

protected projectionDef [Container container]
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
      overwriting = (Viewable) container.getElement (Viewable.class, n.getText());
    }

    mods = properties[/* EXTENDED, ABSTRACT, FINAL */ 7]

    (
      ext:"EXTENDS"
      extending = viewableRef [container]
      {
        if ((mods & 4) != 0)
          reportError(rsrc.getString("err_extendedWithExtends"),
                      ext.getLine());
      }
    
    | bo:"BASED"
      "ON"
      basedOn = viewableRef [container]
      {
        try {
          proj.setBasedOn (basedOn);
        } catch (Exception ex) {
          reportError (ex, bo.getLine());
        }
      }
    )

    eq:EQUALS
    
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
      

      if ((mods & 4) != 0)
      {
        if (overwriting == null)
        {
          reportError (formatMessage (
            "err_projection_nothingToExtend",
            n.getText(),
            container.toString()),
            n.getLine());
        }
        else
          extending = overwriting;
      }
      else if (overwriting != null)
      {
        reportError (formatMessage ("err_projection_extendedWithoutDecl",
                                    n.getText (),
                                    container.toString (),
                                    overwriting.toString ()),
                     n.getLine ());

        /* Try to fix */
        extending = overwriting;
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
        proj.setAbstract((mods & 1) != 0); 
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }

      try {
        proj.setFinal((mods & 2) != 0); 
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
      
      try {
        container.add(proj);
      } catch (Exception ex) {
        reportError(ex, n.getLine());
      }
    }

    projectionAttributes [proj]
    
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


protected projectionAttributes [Viewable view]
{
  ViewableAlias allOf = null;
}
  :
    ( "ATTRIBUTE" )?
    
    (
      all:"ALL" "OF" allOf=renamedViewableRef [view] SEMI
      {
        if ((allOf != null) && (allOf.getAliasing() != null))
        {
          /* In selection and decomposition views, and in projections,
             we do not want to start attribute paths with an named alias
             prefix. These types of views always project on one single Viewable.
          */
          if ((view instanceof SelectionView)
              || (view instanceof DecompositionView)
              || (view instanceof Projection))
            allOf = new ViewableAlias (null, allOf.getAliasing());
          
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
                new AttributeRef.NamedAttribute (attr.getName ())
              };
              path = new AttributePath (allOf, pathItems);
              
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
      
    | projectionAttributeDef [view]
    )*
  ;


protected projectionAttributeDef [Viewable container]
{
  int             mods = 0;
  AttributeDef    attrib = null;
  AttributeDef    overriding = null;
  Type            overridingDomain = null;
  Type            type = null;
  boolean         mandatory = false;
  FunctionCall    fcall = null;
  Function        dummyFunc = null;
  AttributePath[] paths = null;
  
  Container       rvalueNS = null;
  NameSpace       ns = null;
}
  : n:NAME
    mods = properties[/* ABSTRACT | FINAL | EXTENDED */ 7]
    COLON
    {
      /* Determine the name space */
      rvalueNS = container.getRHSNameSpace();
      if (rvalueNS == null)
        panic();

      ns = new NameSpace (
        /*lvalues*/        container,
        /*rvalues*/        rvalueNS,
        /* meta objects */ (Model) container.getContainerOrSame (Model.class));

      overriding = findOverridingAttribute (
        container, mods, n.getText(), n.getLine());
      if (overriding != null)
        overridingDomain = overriding.getDomainResolvingAliases();
    }
    
    ( mand:"MANDATORY" { mandatory = true; } )?
    type = type [container, mandatory, /* alias ok */ true, overridingDomain,
                   n.getLine()]
    
    ce:COLONEQUALS
    
    (
      ( dummyFunc = functionRef[ns] LPAREN )
      => fcall = functionCall [ns]
      {
        attrib = new FunctionAttribute ();
        try {
          if ((type != null) && (type.isAbstract()))
          {
            reportError (formatMessage ("err_projectionAttribute_abstractDomain",
                                        "" + container + ":" + n.getText()));
          }
          
          ((FunctionAttribute) attrib).setDomain (type);
        } catch (Exception ex) {
          reportError (ex, ce.getLine());
        }

        try {
          ((FunctionAttribute) attrib).setInvocation (fcall);
        } catch (Exception ex) {
          reportError (ex, ce.getLine());
        }
      }
      
    | paths = attributePaths [ns]
      {
        attrib = new ProjectionAttribute ();
        try {
          attrib.setName(n.getText());
          attrib.setAbstract((mods & 1) != 0);
          if (type != null)
            attrib.setDomain(type);
        } catch (Exception ex) {
          reportError(ex, ce.getLine());
        }
        
        try {
          ((ProjectionAttribute) attrib).setBasePaths (paths);
        } catch (Exception ex) {
          reportError (ex, ce.getLine());
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
          attrib.setFinal((mods & 2) != 0);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
      }
    }
  ;



/*
    (
      eq:EQUALS
      attrPath = attributePath [view]
      {
        paths = new LinkedList ();
        if (paths != null)
          paths.add (attrPath);
      }
      (
        COMMA
        attrPath = attributePath [view]
        {
          if (attrPath != null)
            paths.add (attrPath);
        }
      )*
      {
        AttributePath[] pathsArr;
        pathsArr = (AttributePath[]) paths.toArray (new AttributePath[0]);
        ViewAttribute attr = new ViewAttribute ();
        try {
          attr.setName (nam.getText ());
          if (pathsArr.length > 0)
            attr.setViewedPaths (pathsArr);
          view.add (attr);
        } catch (Exception ex) {
          reportError (ex, eq.getLine ());
        }
      }
      
    | col:COLON
      fcall = functionCall [ns]
      {
        try {
          FunctionAttribute fattr;
          fattr = new FunctionAttribute ();
          fattr.setName (nam.getText ());
          fattr.setInvocation (fcall);
          view.add (fattr);
        } catch (Exception ex) {
          reportError (ex, col.getLine());
        }
      }
    )
*/


protected renamedViewableRef [Container scope]
  returns [ViewableAlias found]
{
  found = null;
}
  : v:NAME
    {
      Viewable attrScope = (Viewable) scope.getContainerOrSame (Viewable.class); //.getRHSNameSpace();
      if (attrScope == null)
        reportInternalError (v.getLine());
      else
      {
        found = attrScope.resolveBaseAlias (v.getText());
        if (found == null)
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
  ;

  
protected viewableRef [Container scope]
  returns [Viewable found]
{
  List      nams = new LinkedList();
  int lin = 0;
  found = null;
  ch.interlis.Element elt;
}
  : lin = names2[nams]
    {
      Container container = null;
      String elementName = null;
      
      switch (nams.size()) {
      case 1:
        /* VIEWorTABLE */
        elementName = (String) nams.get(0);
        container = scope.getContainerOrSame(Topic.class);
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

      elt = (Viewable) container.getElement(Viewable.class, elementName);
      if (elt == null) {
        reportError(
          formatMessage("err_noSuchViewOrTable", elementName, container.toString()),
          lin);
        panic();
      }
      found = (Viewable) elt;
    }
  ;



protected renamedViewableDef [Container scope]
  returns [ViewableAlias found]
{
  String    aliasName = null;
  Viewable  aliasFor = null;

  found = null;
}
  : (
      /* nondeterminism: NAME TIDLE vs. NAME DOT NAME */
      ( NAME TILDE) =>
      n:NAME
      TILDE
      {
        aliasName = n.getText ();
      }
    )?
    
    aliasFor = viewableRef [scope]

    {
      if (aliasName == null)
        aliasName = aliasFor.getName();
      found = new ViewableAlias (aliasName, aliasFor);
    }
  ;


protected renamedViewableDefs [Container scope]
  returns [ViewableAlias[] found]
{
  List refs = new LinkedList();
  ViewableAlias current;
  found = new ViewableAlias[0];
}
  : current = renamedViewableDef[scope] { refs.add(current); }
    ( COMMA
      current = renamedViewableDef[scope]
      { refs.add(current); }
    )*
    {
      found = (ViewableAlias[]) refs.toArray(found);
    }
  ;

  
/* --------------------------------------------------------------------- */
/*  Selection                                                            */
/* --------------------------------------------------------------------- */

protected selection [NameSpace ns, Viewable selected]
  returns [Selection sel]
{
  sel = null;
  
  Evaluable logex = null;
}
  : "WHERE"
    logex = expression [ns, /* expectedType */ predefinedBooleanType]
    {
      sel = new Selection (selected, logex);
    }
  ;



/* --------------------------------------------------------------------- */
/*  Units                                                                */
/* --------------------------------------------------------------------- */

protected unitDefs [Container container]
  : "UNIT"
    ( unitDef[container] )*
  ;


protected unitDef [Container container]
{
  int mods = 0;
  Unit extending = null;
  Unit u = null;
  boolean _abstract = false;
  
  String docName = null, idName = null;
}
  : n:NAME { docName = idName = n.getText(); }
    mods = properties[/* ABSTRACT */ 1]
    { _abstract = (mods & 1) != 0; }
    
    ( LBRACE idn:NAME RBRACE
      {
        /* An abstract unit must not specify a short name. */
        if (_abstract)
          reportError (rsrc.getString ("err_abstractUnitWithShortName"),
                       idn.getLine());
        else
          idName = idn.getText ();
      }
    )?

    (
      ext:"EXTENDS"
      extending = unitRef[container]
    )?

    (
      EQUALS
      (
        u = derivedUnit[container, idName, docName, _abstract]
        {
          Unit base = null;
          if (u != null) 
            base = ((DerivedUnit) u).getBaseUnit();
          if (base != null)
            base = base.getExtending();
          if (extending != null)
            reportError (rsrc.getString ("err_derivedUnit_ext"), ext.getLine());
          extending = base;
        }
      | u = composedUnit[container, idName, docName, _abstract]
      | u = structuredUnit[container, idName, docName, _abstract]
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
        container.add(u);
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
  double fac = 1.0;
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
    
  | fac = plainNumericConst
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
      
      fac = plainNumericConst
      
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
                            new NumericallyDerivedUnit.Factor[0]));
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
            new NumericallyDerivedUnit.Factor ('*', 1.0)
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
                            new ComposedUnit.Composed[0]));
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
                            new StructuredUnit.Part[0]));
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
        u = (Unit) modelInterlis.getElement (Unit.class, n.getText());
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
        u = (Unit) topic.getElement(Unit.class, unitName);
      
      if (u == null)
        u = (Unit) model.getElement(Unit.class, unitName);

      if ((u == null) && (nams.size() == 1))
        u = (Unit) modelInterlis.getElement (Unit.class, unitName);
      
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



/* --------------------------------------------------------------------- */
/*  Types                                                                */
/* --------------------------------------------------------------------- */

protected type
  [Container  scope,
   boolean    mandatory,
   boolean    allowAliases,
   Type       extending,
   int        line]
  returns [Type typ]
{
  typ = null;
  Domain aliased;
}
  : ( typ = baseType [scope, extending]
    | typ = lineType [scope, extending]
    
    | aliased = domainRef [scope]
      {
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
    
    | /* epsilon */
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
          typ.setMandatory(mandatory);
      } catch (Exception ex) {
        reportError(ex, line);
      }
    }
  ;

protected baseType [Container scope, Type extending]
  returns [Type bt]
{
  bt = null;
}
  : bt = textualType
  | bt = numericType [scope, extending]
  | bt = structuredUnitType [scope, extending]
  | bt = booleanType
  | bt = alignmentType
  | bt = enumerationType [extending]
  | bt = coordinateType [scope, extending]
  ;


protected textualType returns [Type tt]
{
  tt = null;
  int i = -1;
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


protected numericalType [Container scope, Type extending]
  returns [NumericalType ntyp]
{
  ntyp = null;
}
  : ntyp = structuredUnitType [scope, extending]
  | ntyp = numericType [scope, extending]
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


protected enumerationType [Type extending]
  returns [EnumerationType et]
{
  et = new EnumerationType();
  ch.interlis.Enumeration enumer;
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
  returns [ch.interlis.Enumeration enumer]
{
  List elements = new LinkedList();
  ch.interlis.Enumeration.Element curElement;
  
  enumer = null;
}
  : LPAREN
    curElement = enumElement[extending] { elements.add(curElement); }
    ( COMMA curElement = enumElement[extending] { elements.add(curElement); } )*
    RPAREN
    {
      enumer = new ch.interlis.Enumeration(
        (ch.interlis.Enumeration.Element[]) elements.toArray(
           new ch.interlis.Enumeration.Element[0]));
    }
  ;


protected enumElement [Type extending]
  returns [ch.interlis.Enumeration.Element ee]
{
  ch.interlis.Enumeration subEnum = null;
  ch.interlis.Enumeration.Element curEnum = null;
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
            ee = new ch.interlis.Enumeration.Element (
               (String) elt.get(i));
          }
          else
          {
            ee = new ch.interlis.Enumeration.Element (
               (String) elt.get(i),
               subEnum);
          }
        }
        else
        {
          ee = new ch.interlis.Enumeration.Element (
               (String) elt.get(i),
               new ch.interlis.Enumeration (
                 new ch.interlis.Enumeration.Element[] {
                   ee
               })
          );
        }
      }
      
      if ((subEnum == null) && (siz > 1))
        reportError (rsrc.getString("err_dottedEnum"), lineNumber);
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
    
    ( theLineForms = lineForms [scope] )?
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
      lineAttrStructure=tableRef[scope]
      {
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


protected lineForms [Container scope]
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

    linForm = lineFormRef [scope] { if (linForm != null) ll.add(linForm); }
    ( COMMA
      linForm = lineFormRef [scope] { if (linForm != null) ll.add(linForm); }
    )*
    RPAREN
    {
      lf = (LineForm[]) ll.toArray (lf);
    }
  ;


/* --------------------------------------------------------------------- */
/*  Reference Systems                                                    */
/* --------------------------------------------------------------------- */

protected refSys [Container scope]
  returns [RefSystemRef rsr]
{
  rsr = null;

  MetaObject  system = null;
  Domain        domain = null;
  int axisNumber = 0;
}
  : lpar:LCURLY
    system=refSysRef [scope]
    (
      slash1:SLASH
      axisNumber = posInteger
      {
        try {
          if (system != null)
            rsr = new RefSystemRef.CoordSystemAxis (system, axisNumber);
        } catch (Exception ex) {
          reportError (ex, slash1.getLine ());
        }
      }
    | /* epsilon */
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


protected refSysRef [Container scope]
  returns [MetaObject referred]
{
  referred = null;
  
  int line = 0;
  List ll = new LinkedList ();
  Model modl = null;
}
  : line = names2 [ll]
    {
      switch (ll.size())
      {
      case 1:
        referred = resolveMetaObject (
          (Model) scope.getContainerOrSame (Model.class),
          td.INTERLIS.REFSYSTEM, (String) ll.get(0), line);
        break;
      
      case 4:
        /* model.topic.table.object */
        Model model = resolveOrFixModelName (scope, (String) ll.get(0), line);
        Topic topic = resolveOrFixTopicName (model, (String) ll.get(1), line);
        Table table = (Table) topic.getElement (Table.class, (String) ll.get(2));
        if (table == null)
          reportError (formatMessage ("err_noSuchTable",
                                      (String) ll.get(2),
                                      topic.toString()),
                       line);
        else
          referred = resolveMetaObject_inTable (model,
            table, (String) ll.get(3), line);
        break;
      
      default:
        reportError (rsrc.getString ("err_metaObject_refWeird"), line);
        break;
      }
    }
  ;
  

/* --------------------------------------------------------------------- */
/*  Constants                                                            */
/* --------------------------------------------------------------------- */

protected plainNumericConst
  returns [double val]
{
  val = 0;
  PrecisionDecimal dec;
}
  : "PI" { val = Math.PI; }
  | dec = decimal { val = dec.doubleValue(); }
  ;


protected enumerationConst
  returns [String[] mentionedNames]
{
  mentionedNames = null;
}
  : HASH
    mentionedNames = names
  ;


protected enumerationRange
  returns [Constant.EnumConstOrRange rangeOrEnum]
{
  rangeOrEnum = null;
  String[] from = null;
  String[] to = null;
  String[] commonPrefix = null;
}
  : from = enumerationConst
    (
      ddot:DOTDOT
      to = enumerationConst
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
    
protected constant [NameSpace ns, Type expectedType]
  returns [Constant c]
{
  c = null;
  double val;
  Unit un = null;
  String[] mentionedNames = null;
  Container scope = ns.getLValues();
}
  : "UNDEFINED" { c = new Constant.Undefined(); }
  
  | val = plainNumericConst
    ( LBRACE un=unitRef[scope] RBRACE )?
    {
      if (un == null)
        c = new Constant.Numeric (val);
      else
        c = new Constant.Numeric (val, un);
    }
  
  | s:STRING
    {
      try {
        // System.err.println ("*** got " + s.getText() + ", ns=" + ns);
        
        if (expectedType instanceof ReferenceType)
          c = new Constant.ReferenceToMetaObject (
            resolveMetaObject (ns.getMetaObjectNS (),
                               /* polymorphic to */ td.INTERLIS.METAOBJECT,
                               s.getText(), s.getLine()));
        else
          c = new Constant.Text (s.getText());
      } catch (Exception ex) {
        reportError (ex, s.getLine());
      }
    }
  
  | mentionedNames = enumerationConst
    { c = new Constant.Enumeration (mentionedNames); }
  
  | str:STRUCTDEC
    { c = new Constant.Structured (str.getText()); }
  ;



/* --------------------------------------------------------------------- */
/*  Domains                                                              */
/* --------------------------------------------------------------------- */

protected domainDefs [Container container]
  : "DOMAIN" ( domainDef[container] )*
  ;
  

protected domainDef [Container container]
{
  Domain     extending = null;
  Type       extendingType = null;
  Type       declared = null;
  int        mods = 0;
}
  : n:NAME
    mods = properties[/* abstract, final */ 3]
    ( "EXTENDS" extending = domainRef[container]
      {
        if (extending != null)
          extendingType = extending.getType();
      }
    )?
    eq:EQUALS
    
    (
      mand:"MANDATORY"
      declared = type [container, true, /* aliases? */ false, extendingType, mand.getLine()]
      
    | declared = type [container, false, /* aliases? */ false, extendingType, eq.getLine()]
    )
    
    SEMI
      
    {
      Domain dd = new Domain ();
      
      try {
        dd.setName (n.getText());
        
        try {
          if ((mods & 1) != 0)
            dd.setAbstract (true);
        } catch (Exception ex) {
          reportError (ex, n.getLine());
        }
          
        try {
          if ((mods & 2) != 0)
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


protected domainRef [Container scope]
  returns [Domain d]
{
  List      nams = new LinkedList();
  d = null;
  String   domainName = null;
  int lin = 0;
}
  : "INTERLIS" DOT
    ( ido:NAME { domainName = ido.getText(); lin=ido.getLine(); }
    | nm:"NAME" { domainName = nm.getText(); lin=nm.getLine(); }
    | ur:"URI" { domainName = ur.getText(); lin=ur.getLine(); }
    | bl:"BOOLEAN" { domainName = bl.getText(); lin=bl.getLine(); }
    | ha:"HALIGNMENT" { domainName = ha.getText(); lin=ha.getLine(); }
    | va:"VALIGNMENT" { domainName = va.getText(); lin=va.getLine(); }
    )
    {
      d = (Domain) modelInterlis.getElement (Domain.class, domainName);
      if (d == null)
        reportError(
          formatMessage ("err_domainRef_notInModel", domainName,
                         modelInterlis.toString()),
          lin);
    }
    
  | lin = names2[nams]
    {
      Model model;
      Topic topic;
      
      switch (nams.size()) {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        topic = (Topic) scope.getContainerOrSame(Topic.class);
        domainName = (String) nams.get(0);
        break;
        
      case 2:
        model = resolveOrFixModelName (scope, (String) nams.get(0), lin);
        topic = null;
        domainName = (String) nams.get(1);
        break;
      
      case 3:
        model = resolveOrFixModelName (scope, (String) nams.get(0), lin);
        topic = resolveOrFixTopicName (model, (String) nams.get(1), lin);
        domainName = (String) nams.get(2);
        break;
        
      default:
        reportError(rsrc.getString("err_domainRef_weird"), lin);
        model = resolveModelName (scope, (String) nams.get(0));
        topic = null;
        if (model == null)
          model = (Model) scope.getContainerOrSame(Model.class);
        domainName = (String) nams.get(nams.size() - 1);
        break;
      }

      d = null;
      if (topic != null)
        d = (Domain) topic.getElement (Domain.class, domainName);
      
      if ((d == null) && ((topic == null) | (nams.size() == 1)))
        d = (Domain) model.getElement(Domain.class, domainName);

      if ((d == null) && (nams.size() == 1))
        d = (Domain) modelInterlis.getElement(Domain.class, domainName);
      
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
    }
  ;

  
/* --------------------------------------------------------------------- */
/*  Attributes                                                           */
/* --------------------------------------------------------------------- */

protected attribute [Viewable container]
{
  int mods = 0;
  AttributeDef attrib = null;
  AttributeDef overriding = null;
  Type overridingDomain = null;
  Cardinality overridingCardinality = Cardinality.ZERO_TO_ANY;
  Type type = null;
  boolean mandatory = false;
  Function dummyFunc = null;
  FunctionCall fcall = null;
  NameSpace ns = new NameSpace (container);
}
  : n:NAME
    mods = properties[/* ABSTRACT | FINAL | EXTENDED */ 7]
    COLON
    {
      overriding = findOverridingAttribute (
        container, mods, n.getText(), n.getLine());
      if (overriding != null)
        overridingDomain = overriding.getDomainResolvingAliases();
    }
    
    ( mand:"MANDATORY" { mandatory = true; } )?
    
    (
      attrib = relAttribute[container, n.getText(), mandatory]
      SEMI
      {
        try {
          if (attrib != null)
            attrib.setAbstract ((mods & 1) != 0);
        } catch (Exception ex) {
          reportError (ex, n.getLine());
        }
      }
      
    | 
      (
        type = compositionType[container, overridingCardinality, mandatory]
      | type = type [container, mandatory, /* alias ok */ true, overridingDomain,
                     n.getLine()]
      )
      
      (
        ce:COLONEQUALS
        fcall = functionCall [ns]
        {
          attrib = new FunctionAttribute ();
          try {
            ((FunctionAttribute) attrib).setDomain (type);
          } catch (Exception ex) {
            reportError (ex, ce.getLine());
          }

          try {
            ((FunctionAttribute) attrib).setInvocation (fcall);
          } catch (Exception ex) {
            reportError (ex, ce.getLine());
          }
        }
      
      | /* epsilon */
        {
          if (type instanceof CompositionType)
            attrib = new CompositionAttribute();
          else
            attrib = new LocalAttribute();
          
          try {
            attrib.setName(n.getText());
            attrib.setAbstract((mods & 1) != 0);
            if (type != null)
              attrib.setDomain (type);
          } catch (Exception ex) {
            reportError(ex, n.getLine());
          }
        }
      )
      SEMI
    )

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
          attrib.setFinal((mods & 2) != 0);
        } catch (Exception ex) {
          reportError(ex, n.getLine());
        }
      }
    }
  ;


/* Both LocalAttribute and RelAttribute can start with
   MANDATORY. Therefore, the MANDATORY must be consumed
   by the outer rule (e.g. Attribute) if present, and
   it has to be passed as an argument whether or not
   MANDATORY has been encountered.
*/
protected relAttribute
  [Container                container,
   String                   name,
   boolean                  mandatory]
  returns [RelAttribute attrib]
{
  Cardinality  card = Cardinality.ZERO_TO_ANY;
  Table        referred = null;
  int          lineNumber = 0;
  boolean      aggregative = false;
  
  attrib = null;
}
  : ( card = cardinality )?
    
    ( pt:POINTSTO { lineNumber=pt.getLine(); }
    | ag:AGGREGATE { lineNumber=ag.getLine(); aggregative=true; }
    )
    
    {
      attrib = new RelAttribute();
      try {
        attrib.setName(name);
        attrib.setCardinality(card);
        attrib.setAggregative(aggregative);
      } catch (Exception ex) {
        reportError(ex, lineNumber);
      }
    }
    referred = tableRef[container]
    {
      ReferenceType reference = new ReferenceType();

      try {
        reference.setReferred (referred);
        reference.setMandatory (mandatory);
      } catch (Exception ex) {
        reportError(ex, lineNumber);
      }

      try {
        attrib.setDomain (reference);
      } catch (Exception ex) {
        reportError(ex, lineNumber);
        panic (); /* lots of exceptions with err_relAttrDomainIsReferenceToStructure; was easiest way */
      }
    }
    
    ( LPAREN role:NAME RPAREN 
      {
        try {
          attrib.setTargetRoleName (role.getText());
        } catch (Exception ex) {
          reportError(ex, role.getLine());
        }
      }
    )?
  ;
  

protected compositionType [Container container, Cardinality defaultCard, boolean mandatory]
  returns [CompositionType ct]
{
  Cardinality  card = defaultCard;
  Table        componentType = null;
  int          lineNumber = 0;
  boolean      ordered = false;

  ct = null;
}
  : ( bag:"BAG" ( card = cardinality )?
      {
        if (mandatory)
          reportError (rsrc.getString ("err_bagNotMandatory"),
                       bag.getLine());
      }

    | list:"LIST" { ordered = true; } ( card = cardinality )?
      {
        if (mandatory)
          reportError (rsrc.getString ("err_listNotMandatory"),
                       list.getLine());
      }

    | "OBJECT"
      {
        if (mandatory)
          card = Cardinality.EXACTLY_ONE;
        else
          card = Cardinality.ZERO_TO_ONE;
        ordered = true;
      }
    )
    
    of:"OF"
    
    {
      lineNumber = of.getLine();

      ct = new CompositionType ();
      try {
        ct.setCardinality(card);
        ct.setOrdered(ordered);
      } catch (Exception ex) {
        reportError(ex, lineNumber);
      }
    }
    
    componentType = tableRef[container]
    {
      try {
        if (componentType != null)
          ct.setComponentType(componentType);
      } catch (Exception ex) {
        reportError(ex, lineNumber);
      }
    }
  ;
  

protected cardinality
  returns [Cardinality card]
{
  long min = 0;
  long max = Long.MAX_VALUE;
  card = Cardinality.ZERO_TO_ANY;
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
        /* if "0..*" has been specified, we can keep the current
           value of card, i.e. Cardinality.ZERO_TO_ANY */
        if ((min != 0) || (max != Long.MAX_VALUE))
          card = new Cardinality(min, max);
      } catch (Exception ex) {
        reportError(ex, lcurl.getLine());
      }
    }
  ;


/* --------------------------------------------------------------------- */
/*  Constraints                                                          */
/* --------------------------------------------------------------------- */

protected constraintDef [Viewable constrained]
  returns [Constraint constr]
{
  constr = null;
}
  : constr = mandatoryConstraint[constrained]
  | constr = plausibilityConstraint[constrained]
  | constr = uniquenessConstraint[constrained]
  ;


protected mandatoryConstraint [Viewable v]
  returns [MandatoryConstraint constr]
{
  Evaluable condition = null;
  constr = null;
  NameSpace ns = new NameSpace (v);
}
  : mand:"MANDATORY"
    "CONSTRAINT"
    condition = expression [ns, /* expectedType */ predefinedBooleanType]
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
  NameSpace              ns = new NameSpace (v);
  constr = null;
}
  : tok:"CONSTRAINT"

    ( LESSEQUAL { direction = PlausibilityConstraint.DIRECTION_AT_MOST; }
    | GREATEREQUAL { direction = PlausibilityConstraint.DIRECTION_AT_LEAST; }
    )

    percentage = decimal PERCENT
    condition = expression [ns, /* expectedType */ predefinedBooleanType]
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


protected uniquenessConstraint [Viewable v]
  returns [Constraint constr]
{
  constr = null;
  
  List ll = null;
  AttributePath prefixPath = null;
  List attrList = null;
  Viewable constrained = v;
  AttributeDef curAttribute = null;
}
  : u:"UNIQUE"
    {
      /* The delayed creation of the linked list saves us
         from superfluous object creation when in guessing
         mode.
      */
      ll = new LinkedList ();
    }
    
    (
      ( ( NAME | "OBJECT") (DOT NAME)* COLON )
      => ( nam:NAME
           {
             ll.add (new AttributeRef.NamedAttribute (nam.getText ()));
           }
         | "OBJECT"
           {
             ll.add (AttributeRef.OBJECT);
           }
         )
         (
           DOT
           nam2:NAME
           {
             ll.add (new AttributeRef.NamedAttribute (nam2.getText ()));
           }
         )*
         COLON
    )?

    anam:NAME
    {
      AttributeRef[] prefix;
      
      prefix = (AttributeRef[]) ll.toArray (new AttributeRef[0]);
      try {
        prefixPath = new AttributePath (new ViewableAlias (null, v), prefix);
      } catch (Exception ex) {
        reportError (ex, anam.getLine ());
        try {
          prefixPath = new AttributePath (new ViewableAlias (null, v), null);
        } catch (Exception ex1) {
          panic ();
        }
      }
      
      attrList = new LinkedList ();
      
      curAttribute = (AttributeDef) constrained.getElement (
        AttributeDef.class,
        anam.getText ());
      if (curAttribute == null)
      {
        reportError (formatMessage ("err_attributePath_unknownAttr_short",
                                    anam.getText (),
                                    constrained.toString ()),
                     anam.getLine ());
      }
      else
        attrList.add (curAttribute);
    }
    (
      COMMA
      bnam:NAME
      {
        curAttribute = (AttributeDef) constrained.getElement (
          AttributeDef.class,
          bnam.getText ());
        if (curAttribute == null)
        {
          reportError (formatMessage ("err_attributePath_unknownAttr_short",
                                      bnam.getText (),
                                      constrained.toString ()),
                       bnam.getLine ());
        }
        else
          attrList.add (curAttribute);
      }
    )*
    SEMI
    {
      constr = new UniquenessConstraint (
        prefixPath,
        (AttributeDef[]) attrList.toArray (new AttributeDef[0]));
    }
  ;


/* --------------------------------------------------------------------- */
/*  Evaluable Expressions                                                */
/* --------------------------------------------------------------------- */
/* Note: The expectedType is needed at some place in syntax, for instance
   to decide whether a String is a text constant or a reference to a
   meta object. The grammar rules are not supposed to check type
   compatibility and have to be prepared to receive null in case of
   recovering from parsing errors. However, in normal parsing, the
   expectedType is not null.
*/

protected expression [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
}
  : expr=term_1 [ns, expectedType]
  ;


protected term_1 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  List disjoined = null;
  expr = null;
  int lineNumber = 0;
}
  : expr=term_2 [ns, expectedType]
    {
      disjoined = new LinkedList ();
      disjoined.add(expr);
    }
    
    (
      o:"OR"
      expr = term_2 [ns, expectedType]
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
            (Evaluable[]) disjoined.toArray (new Evaluable[0]));
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }
    }
  ;


protected term_2 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  List conjoined = null;
  expr = null;
  int lineNumber = 0;
}
  : expr=term_3 [ns, expectedType]
    {
      conjoined = new LinkedList ();
      conjoined.add(expr);
    }
    
    (
      an:"AND"
      expr = term_3 [ns, expectedType]
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
            (Evaluable[]) conjoined.toArray(new Evaluable[0]));
        } catch (Exception ex) {
          reportError (ex, lineNumber);
        }
      }
    }
  ;


protected term_3 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
  Evaluable conclusion = null;
}
  : expr = term_4 [ns, expectedType]
    (
      pt:POINTSTO
      conclusion = term_4 [ns, expectedType]
      {
        try {
          expr = new Expression.Implication (expr, conclusion);
        } catch (Exception ex) {
          reportError (ex, pt.getLine());
        }
      }
    )?
  ;


protected term_4 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
}
  : nt:"NOT" expr = term_5 [ns, expectedType]
    {
      try {
        expr = new Expression.Negation (expr);
      } catch (Exception ex) {
        reportError (ex, nt.getLine());
      }
    }
  
  | expr = term_5 [ns, expectedType]
  ;


protected term_5 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
  Evaluable comparedWith = null;
  char op = '=';
  int[] lineNumberPar = null;
}
  : expr = term_6 [ns, expectedType]
    (
      {
        lineNumberPar = new int[1];
      }
      
      op = comparisionOperator [lineNumberPar]
      comparedWith = term_6 [ns, expectedType]
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


protected term_6 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
  AttributePath path = null;
  List pathList = null;
}
  : expr = term_7 [ns, expectedType]
    (
      exin:"EXISTS" "IN"
      
      LPAREN
      path = attributePath [ns]
      {
        pathList = new LinkedList ();
        pathList.add (path);
      }
      
      (
        comm:COMMA
        path = attributePath [ns]
        {
          pathList.add (path);
        }
      )*
      RPAREN
      
      {
        try {
          expr = new Expression.ExistenceCheck (
            expr,
            (AttributePath[]) pathList.toArray (new AttributePath[0]));
        } catch (Exception ex) {
          reportError (ex, exin.getLine());
        }
      }
    )?
  ;
  
protected term_7 [NameSpace ns, Type expectedType]
  returns [Evaluable expr]
{
  expr = null;
}
  : LPAREN
    expr = term_1 [ns, expectedType]
    RPAREN
  
  | def:"DEFINED"
    LPAREN
    expr = expressionElement [ns, expectedType]
    RPAREN
    {
      try {
        expr = new Expression.DefinedCheck (expr);
      } catch (Exception ex) {
        reportError (ex, def.getLine());
      }
    }

  | expr = expressionElement [ns, expectedType]
  ;


protected expressionElement [NameSpace ns, Type expectedType]
  returns [Evaluable ev]
{
  ev = null;
  Viewable dummy;
  Function dummyFunc;
  Parameter param;
  Container scope = ns.getRValues();
}
  : ( dummyFunc=functionRef[ns] LPAREN ) => ev = functionCall [ns]

  | ev = attributePath [ns]

  | "PARAMETER" param = parameterRef [scope]
    {
      ev = new ParameterValue (param);
    }

  | ev = constant [ns, expectedType]
  ;


protected comparisionOperator [int[] lineNumberPar]
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




/* --------------------------------------------------------------------- */
/*  Attribute Paths                                                      */
/* --------------------------------------------------------------------- */

protected attributePathAfterColon [ViewableAlias pathStart]
  returns [AttributePath path]
{
  path = null;

  List ll = null;
  int[] lineArr = new int[1];
  int line = 0;
  AttributeRef attr = null;
}
  : attr=attributeRef [lineArr]
    {
      line = lineArr[0];
      ll = new LinkedList();
      if (attr != null)
        ll.add (attr);
    }
    
    ( DOT
      attr=attributeRef [lineArr]
      {
        line = lineArr[0];
        if (attr != null)
          ll.add (attr);
      }
    )*
    
    {
      try {
        if ((ll.size() > 0) && (pathStart != null))
          path = new AttributePath (
            /* start */ pathStart,
            /* path */  (AttributeRef[]) ll.toArray (new AttributeRef[0]));
      } catch (Exception ex) {
        reportError (ex, line);
      }
    }
  ;


protected attributePath [NameSpace ns]
  returns [AttributePath path]
{
  Container scope = ns.getRValues ();
  ViewableAlias pathStart = null;
  ViewableAlias dummy = null;
  path = null;
  int line = 0;
}
  : (
      ( dummy = renamedViewableRef[scope] COLON )
      => pathStart = renamedViewableRef[scope]
         col:COLON
         {
           line = col.getLine ();
         }

    | /* epsilon */
      {
        Viewable attrNameSpace = (Viewable) scope.getContainerOrSame (Viewable.class);
        if (attrNameSpace == null)
        {
          reportInternalError (new IllegalStateException ("not in Viewable:" + scope),
                               0);
        }
        
        pathStart = new ViewableAlias (null, attrNameSpace);
      }
    )
    
    path = attributePathAfterColon [pathStart]
  ;


protected attributePaths [NameSpace ns]
  returns [AttributePath[] paths]
{
  AttributePath attrPath = null;
  List          pathList = null;
  
  paths = null;
}
  : attrPath = attributePath [ns]
    {
      pathList = new LinkedList ();
      if (attrPath != null)
        pathList.add (attrPath);
    }

    (
      COMMA
      attrPath = attributePath [ns]
      {
        if (attrPath != null)
          pathList.add (attrPath);
      }
    )*

    {
      paths = (AttributePath[]) pathList.toArray (new AttributePath[0]);
    }
  ;


protected attributeRef [int[] line]
  returns [AttributeRef attr]
{
  attr = null;
  int axnum = 1;
}
  : n:NAME
    { 
      attr = new AttributeRef.NamedAttribute (n.getText());
      line[0] = n.getLine ();
    }
  | thisar:"THISAREA"
    {
      attr = AttributeRef.THISAREA;
      line[0] = thisar.getLine ();
    }
  | thatar:"THATAREA"
    {
      attr = AttributeRef.THATAREA;
      line[0] = thatar.getLine ();
    }
  | obj:"OBJECT"
    {
      attr = AttributeRef.OBJECT;
      line[0] = obj.getLine ();
    }
  | par:"PARENT" 
    {
      attr = AttributeRef.PARENT;
      line[0] = par.getLine ();
    }
  | lbrac:LBRACE axnum=posInteger RBRACE
    {
      line[0] = lbrac.getLine ();
      try {
        attr = new AttributeRef.Axis (axnum);
      } catch (IllegalArgumentException ex) {
        reportError (rsrc.getString ("err_axisNumber_zero"), lbrac.getLine());
        attr = new AttributeRef.Axis (1);
      }
    }
  ;



/* --------------------------------------------------------------------- */
/*  Parameters                                                           */
/* --------------------------------------------------------------------- */

protected parameterDef [Container container]
{
  int mods = 0;
  Type type = null;
  boolean mandatory = false;
  boolean declaredExtended = false;
  Parameter overriding = null;
  Table referred = null;
}
  : n:NAME
    mods = properties[/* EXTENDED */ 4]
    col:COLON
    {
      declaredExtended = (mods & 4) != 0;
      
      overriding =  (Parameter) container.getElement (
        Parameter.class, n.getText());
      
      if ((overriding == null) && declaredExtended)
      {
        reportError (formatMessage ("err_parameter_nothingToExtend",
                                    n.getText(),
                                    container.toString()),
                     n.getLine());
      }
      
      if ((overriding != null)
          && (container == overriding.getContainer (Viewable.class)
              || (container == overriding.getContainer (Model.class))))
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

    ( "MANDATORY" { mandatory = true; } )?
    
    (
      type = type [container, mandatory, /* alias ok */ true,
                   /* extending */ (overriding == null) ? null : overriding.getType(),
                   col.getLine()]
                   
    | pt:POINTSTO
      referred = tableRef[container]
      {
        ReferenceType reference = new ReferenceType();

        try {
          reference.setReferred (referred);
          reference.setMandatory (mandatory);
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


protected parameterRef [Container scope]
  returns [Parameter param]
{
  param = null;
  Table scopeTable = null;
  Model scopeModel = null;
  String parameterName = null;
}
  : n:NAME
    {
      parameterName = n.getText ();
      scopeTable = (Table) scope.getContainerOrSame (Table.class);
      if (scopeTable != null)
        param = (Parameter) scopeTable.getElement (Parameter.class, parameterName);
      
      if (param == null)
      {
        scopeModel = (Model) scope.getContainerOrSame (Model.class);
        if (scopeModel != null)
          param = (Parameter) scopeModel.getElement (Parameter.class, parameterName);
      }
      
      if (param == null)
      {
        if (scopeTable == null)
          reportError (formatMessage ("err_parameter_unknownInModel",
            parameterName, scopeModel.toString()), n.getLine());
        else
          reportError (formatMessage (
            "err_parameter_unknownInTableAndModel",
            parameterName, scopeTable.toString(), scopeModel.toString()),
            n.getLine());
      }
    }
  ;


/* --------------------------------------------------------------------- */
/*  Functions                                                            */
/* --------------------------------------------------------------------- */

protected function [Container container]
{
  Type t = null;
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
    t = argumentType [container, lpar.getLine()]
    {
      try {
        args.add (t);
      } catch (Exception ex) {
        reportError (ex, lpar.getLine ());
      }
    }
    ( sem:SEMI t = argumentType [container, sem.getLine()]
      {
        try {
          args.add (t);
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
        f.setArguments ((Type[]) args.toArray (new Type[0]));
      } catch (Exception ex) {
        reportError (ex, lpar.getLine());
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


protected argumentType [Container scope, int line]
  returns [Type domain]
{
  domain = null;
  boolean mandatory = false;
  int lin = line;
}
  : ( mand:"MANDATORY" { mandatory = true; lin = mand.getLine(); })?
    (
      domain = type [scope, mandatory, /* alias ok*/ true,
                     /* extending */ null, lin]
    | domain = compositionType [scope, Cardinality.ZERO_TO_ANY, mandatory]
    )
  ;


protected functionRef [NameSpace ns]
  returns [Function func]
{
  List      nams = new LinkedList();
  int       lin = 0;
  Model     model = null;
  String    functionName = null;
  Container scope = ns.getRValues ();
  
  func = null;
}
  : lin = names2 [nams]
    {
      switch (nams.size())
      {
      case 1:
        model = (Model) scope.getContainerOrSame(Model.class);
        functionName = (String) nams.get(0);
        break;
        
      case 2:
        model = resolveOrFixModelName(scope, (String) nams.get(0), lin);
        functionName = (String) nams.get(1);
        break;
      
      default:
        reportError (rsrc.getString ("err_functionRef_weird"), lin);
        model = resolveModelName (scope, (String) nams.get(0));
        if (model == null)
          model = (Model) scope.getContainerOrSame (Model.class);
        functionName = (String) nams.get(nams.size() - 1);
        break;
      }

      func = (Function) model.getElement (Function.class, functionName);
      if (func == null)
      {
        reportError (formatMessage ("err_functionRef_weird", functionName,
          model.toString()), lin);
      }
    }
  ;


protected functionCall [NameSpace ns]
  returns [FunctionCall call]
{
  call = null;
  Function called = null;
  Evaluable arg = null;
  LinkedList args = null;
  Type       expectedTypes[] = null;
  Type       expectedType = null;
  int        curArgument = 0;
}
  : called = functionRef [ns]
  
    lpar:LPAREN
    {
      args = new LinkedList();
      if (called != null)
        expectedTypes = called.getArguments();
    }
    
    {
      curArgument = curArgument + 1;
      /* The semantic layer will complain if there are too many arguments,
         just make sure here that the parser does not crash in that case. */
      if ((expectedTypes == null) || (curArgument > expectedTypes.length))
        expectedType = null;
      else
        expectedType = expectedTypes [curArgument - 1];
    }
    
    arg = expression [ns, expectedType]
    {
      args.add (arg);
    }
    
    (
      COMMA
      {
        curArgument = curArgument + 1;
        /* The semantic layer will complain if there are too many arguments,
           just make sure here that the parser does not crash in that case. */
        if ((expectedTypes == null) || (curArgument > expectedTypes.length))
          expectedType = null;
        else
          expectedType = expectedTypes [curArgument - 1];
      }
      arg = expression [ns, expectedType]
      {
        args.add (arg);
      }
    )*
    RPAREN
    
    {
      try {
        call = new FunctionCall (
          called,
          (Evaluable[]) args.toArray (new Evaluable[0]));
      } catch (Exception ex) {
        reportError (ex, lpar.getLine());
      }
    }
  ;


/* --------------------------------------------------------------------- */
/*  Line Form Type                                                       */
/* --------------------------------------------------------------------- */

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
  

protected lineFormRef [Container scope]
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
      Model scopeModel = (Model) scope.getContainerOrSame (Model.class);
      lf = (LineForm) scopeModel.getElement (LineForm.class, nam.getText());
      if (lf == null)
        lf = (LineForm) modelInterlis.getElement (LineForm.class, nam.getText());
      
      if (lf == null)
        reportError (formatMessage (
          "err_lineForm_unknownName", nam.getText(), scopeModel.toString()),
          nam.getLine());
    }
  ;


/* --------------------------------------------------------------------- */
/*  Graphics                                                             */
/* --------------------------------------------------------------------- */

protected graphicDef [Container cont]
{
  Viewable basedOn = null;
  int mods = 0;
  Graphic graph = null;
  Graphic overriding = null;
  Graphic extending = null;
  Selection sel = null;
  NameSpace ns = new NameSpace (cont);
  NameSpace subns = ns;
}
  : "GRAPHIC" n:NAME
    mods=properties[/* ABSTRACT, FINAL, EXTENDED */ 7]
    ( ext:"EXTENDS" extending=graphicRef[cont] )?
    ( "BASED" "ON"
      basedOn = viewableRef [cont]
    )?
    EQUALS
    {
      graph = new Graphic ();
      try {
        graph.setName (n.getText());
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }
      overriding = (Graphic) cont.getElement (Graphic.class, n.getText());
      if ((overriding == null) && ((mods & 4) != 0))
        reportError (formatMessage ("err_graphic_nothingToExtend",
          n.getText(), cont.toString()), n.getLine());
      
      if ((overriding != null) && (extending != null))
      {
        reportError (rsrc.getString ("err_extendedWithExtends"),
                     ext.getLine ());
      }
      
      try {
        graph.setAbstract ((mods & 1) != 0);
      } catch (Exception ex) {
        reportError (ex, n.getLine ());
      }

      try {
        graph.setFinal ((mods & 2) != 0);
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
      
      subns = new NameSpace (graph);
    }

    (
      sel = selection[subns, basedOn] sem:SEMI
      {
        if (sel != null)
        {
          try {
            graph.add (sel);
          } catch (Exception ex) {
            reportError (ex, sem.getLine());
          }
        }
      }
    )*
    
    ( signAttribute[graph, basedOn] )*

    end [graph]
    SEMI
  ;

protected signAttribute [Graphic graph, Viewable basedOn]
{
  int mods = 0;
  Table signTab = null;
  SignAttribute attr = null;
  SignAttribute overriding = null;
  boolean declaredExtended = false;
  SignInstruction instruct = null;
  List instructs = null;
}
  : n:NAME
    mods=properties[/* EXTENDED */ 4]
    (
      "OF"
      signTab = tableRef [graph]
    )?
    COLON

    {
      declaredExtended = (mods & 4) != 0;
      overriding =  (SignAttribute) graph.getElement (
        SignAttribute.class, n.getText());

      if ((overriding == null) && declaredExtended)
      {
        if (graph.getExtending() == null)
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
                                      graph.getExtending().toString()),
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

    instruct = condSigParamAss [graph, basedOn, signTab]
    {
      instructs = new LinkedList();
      if (instruct != null)
        instructs.add (instruct);
    }
    (
      COMMA
      instruct = condSigParamAss [graph, basedOn, signTab]
      {
        if (instruct != null)
          instructs.add (instruct);
      }
    )*
    SEMI
    {
      try {
        attr.setInstructions (
          (SignInstruction[]) instructs.toArray (new SignInstruction[0])
        );
      } catch (Exception ex) {
        reportError (ex, n.getLine());
      }
    }
  ;


protected condSigParamAss [Graphic graph, Viewable basedOn, Table signTab]
  returns [SignInstruction instruct]
{
  Evaluable            restrictor = null;
  List                 paramAssignments = null;
  ParameterAssignment  assign = null;
  NameSpace            ns;
  
  ns = new NameSpace (
    /* lvalues */ signTab,
    /* rvalues */ basedOn,
    /* metaobjs */ (Model) graph.getContainerOrSame (Model.class));
  instruct = null;
}
  : (
      "WHERE"
      restrictor = expression [ns, /* expectedType */ predefinedBooleanType]
    )?

    LPAREN
    assign = sigAssignment[ns, graph, basedOn]
    {
      paramAssignments = new LinkedList ();
      if (assign != null)
        paramAssignments.add (assign);
    }
    (
      SEMI
      assign = sigAssignment[ns, graph, basedOn]
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
          new ParameterAssignment[0])
      );
    }
  ;


protected sigAssignment [NameSpace ns, Graphic graph, Viewable basedOn]
  returns [ParameterAssignment assign]
{
  List assignments = null;
  Parameter assignedParam = null;
  Evaluable value = null;
  assign = null;
  Type   expectedType = null;
}
  : parm:NAME
    doteq:COLONEQUALS
    {
      if ((ns.getLValues()) != null)
      {
        assignedParam = (Parameter) (ns.getLValues()).getElement (Parameter.class,
                                                        parm.getText());
        if (assignedParam == null)
        {
          reportError (formatMessage ("err_parameter_unknownInSignTable",
                                      parm.getText(),
                                      (ns.getLValues()).toString()),
                       parm.getLine());
        }
        else
          expectedType = Type.findReal (assignedParam.getType());
      }
    }
    
    ( value = constant[ns, expectedType]
    | value = attributePath[ns]
    | value = conditionalExpression[ns, expectedType]
    )
    
    {
      try {
        assign = new ParameterAssignment (assignedParam, value);
      } catch (Exception ex) {
        reportError (ex, doteq.getLine());
      }
    }
  ;


protected conditionalExpression [NameSpace ns, Type expectedType]
  returns [ConditionalExpression condex]
{
  AttributePath attrPath = null;
  List items = null;
  ConditionalExpression.Condition cond = null;

  condex = null;
}
  : "WITH"
    attrPath = attributePath [ns /* previously: ns.getRValues().getContainerOrSame (Viewable.class) */]

    LPAREN
    cond = enumAssignment [ns, expectedType]
    {
      items = new LinkedList();
      if (cond != null)
        items.add (cond);
    }
    (
      COMMA
      cond = enumAssignment[ns, expectedType]
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
           new ConditionalExpression.Condition[0]));
    }
  ;


protected enumAssignment [NameSpace ns, Type expectedType]
  returns [ConditionalExpression.Condition cond]
{
  Constant cnst = null;
  Constant.EnumConstOrRange range = null;
  cond = null;
}
  : cnst = constant[ns, expectedType]
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
        graph = (Graphic) topic.getElement (Graphic.class, graphicName);
      
      if ((graph == null) && ((topic == null) | (nams.size() == 1)))
        graph = (Graphic) model.getElement(Graphic.class, graphicName);

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


/* --------------------------------------------------------------------- */
/*  Utilities                                                            */
/* --------------------------------------------------------------------- */

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

    { nams = (String[]) namList.toArray(new String[0]); }
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


protected posInteger
  returns [int i]
{
  i = 0;
}
  : p:POSINT
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


protected code returns [int i]
{
  i = 0;
}
  : p:POSINT { i = Integer.parseInt(p.getText()); }
  | h:HEXNUMBER { i = Integer.parseInt(p.getText(), 16); }
  ;


protected decimal returns [PrecisionDecimal dec]
{
  dec = null;
}
  : d:DEC
    { dec = new PrecisionDecimal(d.getText()); }
  | p:POSINT
    { dec = new PrecisionDecimal(Double.parseDouble(p.getText()), 0); }
  | n:NUMBER
    { dec = new PrecisionDecimal(Double.parseDouble(n.getText()), 0); }
  ;



/* --------------------------------------------------------------------- */
/*  Interlis-1                                                           */
/* --------------------------------------------------------------------- */

protected interlis1Def [TransferDescription td]
{
  Model   model = null;
}
  : "TRANSFER" transferName:NAME SEMI
    {
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
        attrib = new RelAttribute ();
        Table referred = (Table) topic.getElement (Table.class, tabnam.getText ());
        if (referred == null)
        {
          reportError (formatMessage ("err_noSuchTable", tabnam.getText(),
                                      topic.toString ()),
                       tabnam.getLine ());
        }
        type = new ReferenceType ();
        try {
          ((ReferenceType) type).setReferred (referred);
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
  ;


protected ili1_identifications [Table table]
{
  List ll = null;
  AttributeDef curAttribute = null;
}
  : "NO" "IDENT"
  | "IDENT"
    (
      anam:NAME
      {
        ll = new LinkedList ();
        curAttribute = (AttributeDef) table.getElement (
          AttributeDef.class,
          anam.getText ());
        if (curAttribute == null)
        {
          reportError (formatMessage ("err_attributePath_unknownAttr_short",
                                      anam.getText (),
                                      table.toString ()),
                       anam.getLine ());
        }
        else
          ll.add (curAttribute);
      }
      (
        COMMA
        bnam:NAME
        {
          curAttribute = (AttributeDef) table.getElement (
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
        try {
          /* Construct a new uniqueness constraint */
          UniquenessConstraint constr = new UniquenessConstraint (
            new AttributePath (new ViewableAlias (null, table),
                               new AttributeRef[0]),
            (AttributeDef[]) ll.toArray (new AttributeDef[0]));
          
          /* Add it to the table. */
          table.add (constr);
        } catch (Exception ex) {
          reportError (ex, semi.getLine ());
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
        domain = (Domain) forTopic.getElement (Domain.class, nam.getText ());
        if (domain == null)
          domain = (Domain) forModel.getElement (Domain.class, nam.getText ());
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
        domain = (Domain) forModel.getElement (Domain.class, nam.getText ());
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
    eMin = decimal
    nMin = decimal
    eMax = decimal
    nMax = decimal
    
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
    eMin = decimal
    nMin = decimal
    hMin = decimal
    eMax = decimal
    nMax = decimal
    hMax = decimal
    
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
    min = decimal
    DOTDOT
    max = decimal
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
    min = decimal
    max = decimal
    
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
    min = decimal
    max = decimal
    
    {
      m2 = (ComposedUnit) inModel.getElement (ComposedUnit.class, rsrc.getString ("err_unit_ili1_DIM2_name"));
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
        u = (Unit) containingModel.getElement (Unit.class,
                                               rsrc.getString ("err_unit_ili1_DEGREES_name"));
        if (u == null)
        {
          try {
            NumericallyDerivedUnit.Factor timesPi;
            NumericallyDerivedUnit.Factor by180;
            
            timesPi = new NumericallyDerivedUnit.Factor ('*', Math.PI);
            by180 = new NumericallyDerivedUnit.Factor ('/', 180.0);
            
            NumericallyDerivedUnit degr = new NumericallyDerivedUnit ();
            degr.setName (rsrc.getString ("err_unit_ili1_DEGREES_name"));
            degr.setDocName (rsrc.getString ("err_unit_ili1_DEGREES_docName"));
            degr.setBaseUnit (td.INTERLIS.RADIAN);
            degr.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              timesPi,
              by180
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
        u = (Unit) containingModel.getElement (Unit.class,
                                               rsrc.getString ("err_unit_ili1_GRADS_docName"));
        if (u == null)
        {
          try {
            NumericallyDerivedUnit.Factor timesPi;
            NumericallyDerivedUnit.Factor by200;
            
            timesPi = new NumericallyDerivedUnit.Factor ('*', Math.PI);
            by200 = new NumericallyDerivedUnit.Factor ('/', 200.0);
            
            NumericallyDerivedUnit gon = new NumericallyDerivedUnit ();
            gon.setName (rsrc.getString ("err_unit_ili1_GRADS_docName"));
            gon.setDocName (rsrc.getString ("err_unit_ili1_GRADS_docName"));
            gon.setBaseUnit (td.INTERLIS.RADIAN);
            gon.setConversionFactors (new NumericallyDerivedUnit.Factor[] {
              timesPi,
              by200
            });
            u = gon;
            containingModel.add (gon);
          } catch (Exception ex) {
            reportInternalError (ex, grads.getLine());
          }
        } /* if (u == null) */
      }
    )
    
    min = decimal
    max = decimal
    
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
  ch.interlis.Enumeration enumer = null;
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
      lineForms = (LineForm[]) lineFormList.toArray (new LineForm[0]);
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
    maxOverlap = decimal
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


/* ===================================================================== */
/*  Lexer                                                                */
/* ===================================================================== */

class Ili2Lexer extends Lexer;


options {
  k=2;                   // two characters of lookahead
  testLiterals = false;  // do not test for literals by default
}


tokens {
  PLUS;
  MINUS;
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


protected SCALING
  : 'S'  NUMBER
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
  | ( NUMBER ( '.' | 'S' ) ) => DEC { $setType(DEC); }
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
