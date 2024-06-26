package ch.interlis.ili2c.generator;


import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.io.IndentPrintWriter;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.generator.TransformationParameter.ModelTransformation;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXml;
import ch.interlis.ili2c.generator.nls.ModelElements;
import ch.interlis.ili2c.generator.nls.TranslationElement;
import ch.interlis.ili2c.metamodel.*;
import ch.interlis.ili2c.parser.InterlisString;

/** A class used to generate an INTERLIS model description as INTERLIS-2.
*/
public class Interlis2Generator
{
  protected IndentPrintWriter   ipw;
  TransferDescription td;
  /** the predefined model INTERLIS
   *
   */
  PredefinedModel               modelInterlis;
  Unit                anyUnit;
  boolean             withPredefined;
  private boolean onlyLastFile;
  int                 numErrors = 0;
  private boolean doIli24=false;
  
  private static final String FR = Ili2TranslationXml.FR;
  private static final String IT = Ili2TranslationXml.IT;
  private static final String EN = Ili2TranslationXml.EN;
  private static final String DE = Ili2TranslationXml.DE;
  
  private java.util.List<Constraint> selfStandingConstraints=null;
  private ModelElements translationConfig;
  private TransformationParameter params = null;

  public Interlis2Generator()
  {
  }
  public static Interlis2Generator generateElements(Writer out, TransferDescription td) {
	  return generateElements(out, td, null);
  }
  public static Interlis2Generator generateElements (
	Writer out, TransferDescription td, ModelElements modelElements)
  {
	Interlis2Generator i = new Interlis2Generator();
	i.setup(out, td, false, false,modelElements, null,null);
	return i;
  }
  public static Interlis2Generator generateElements24(Writer out, TransferDescription td) {
      Interlis2Generator i = new Interlis2Generator();
      i.setup(out, td, false, false,null, null,true);
      return i;
  }
  static public String debugToString(TransferDescription td,ch.interlis.ili2c.metamodel.Element ele)
  {
	java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
	Interlis2Generator makeSyntax = Interlis2Generator.generateElements(syntaxBuffer, td, null);
	makeSyntax.printElement(ele.getContainer(), null, ele, null);
	makeSyntax.ipw.flush();
    return syntaxBuffer.toString();
  }

  private void finish ()
  {
    ipw.close();
  }



  protected void printError ()
  {
    ipw.print (Element.makeErrorName (null));
    numErrors = numErrors + 1;
  }



  /** Generates an INTERLIS-2 model description.


      @param out            A Writer object to which the text will be written.
      @param td             The description to be written.
      @param withPredefined Whether or not to include the predefined <code>MODEL INTERLIS</code>.


      @return The number of errors that have occured while generating, for example because
              an invalid model has been passed. Invalid models can result from parsing a
              file with recoverable syntactic or semantic errors. Of course, the parser reports
              these errors.
  */
	public int generate(Writer out, TransferDescription rd, boolean withPredefined) {
		setup(out, rd, withPredefined, false,null, null,null);
		printTransferDescription(rd, null);
		finish();
		return numErrors;
	}
	public int generateWithNewCrs(Writer out, TransferDescription rd, TransformationParameter params) {
		setup(out, rd, false, true,null, params,null);
		printTransferDescription(rd, null);
		finish();
		return numErrors;
	}
  public int generateWithNewLanguage (
	Writer out, TransferDescription td,ModelElements modelEles, String language)
	{
      setup(out, td, false, true,modelEles, null,null);
      printTransferDescription(td, language);
      finish();
      return numErrors;
	}
private void setup(
	Writer out,
	TransferDescription td,
	boolean withPredefined, boolean onlyLastFile, ModelElements modelEles, TransformationParameter params,Boolean doIli24) {
	ipw = new IndentPrintWriter (out);
	this.td = td;
	modelInterlis = td.INTERLIS;
	anyUnit = td.INTERLIS.ANYUNIT;
	this.withPredefined = withPredefined;
	this.translationConfig = modelEles;
	this.params = params;
	this.onlyLastFile=onlyLastFile;
	this.doIli24=doIli24!=null?doIli24:!td.getLastModel().isIli23();
}

  private boolean printModifierHelper(boolean first, boolean flag, String what)
  {
    if (flag) {
      if (!first)
        ipw.print(", ");
      ipw.print(what);
      return false;
    } else
      return first;
  }



  protected void printModifiers(boolean _abstract,
    boolean _final, boolean _extended,boolean _ordered, boolean _external,boolean _transient, boolean _generic)
  {
    if (!_abstract && !_final && !_extended && !_ordered && !_external && !_transient && !_generic) {
		return;
    }


    boolean first = true;
    ipw.print(" (");
    first = printModifierHelper(first, _abstract, "ABSTRACT");
    first = printModifierHelper(first, _final, "FINAL");
    first = printModifierHelper(first, _extended, "EXTENDED");
	first = printModifierHelper(first, _ordered, "ORDERED");
	first = printModifierHelper(first, _external, "EXTERNAL");
	first = printModifierHelper(first, _transient, "TRANSIENT");
	first = printModifierHelper(first, _generic, "GENERIC");
    ipw.print(')');
  }

  protected void printTopic (Topic topic,String language)
  {
    if (topic == null)
      return;

	selfStandingConstraints=new java.util.ArrayList<Constraint>();
	
    Topic extending = (Topic) topic.getExtending();

    printDocumentation(topic,language);
    printMetaValues(topic,topic.getMetaValues(), language, topic.getScopedName());
    ipw.print("TOPIC ");
    printName(topic,language);
    printModifiers(topic.isAbstract(), topic.isFinal(),
      /* EXTENDED */false, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false,/*GENERIC*/false);


    if (extending != null)
    {
      ipw.print(" EXTENDS ");
      printRef(topic,extending,language);
    }

    ipw.println(" =");
    ipw.indent();
	
	Domain basketOid = getTopicBasketOid(topic);
	if(basketOid!=null){
		
		ipw.print("BASKET OID AS ");
        printRef(topic,basketOid,language);
		ipw.println(';');
	}
	Domain classOid = getTopicClassOid(topic);
	if(classOid!=null){
		
		ipw.print("OID AS ");
        printRef(topic,classOid,language);
		ipw.println(';');
	}

    Iterator it = getTopicDependsOn(topic);
    if (it.hasNext())
    {
      ipw.print("DEPENDS ON ");
      Topic depTopic = (Topic) it.next();
      printRef(topic,depTopic,language);
     
      while (it.hasNext())
      {
        ipw.print(", ");
        depTopic = (Topic) it.next();
        printRef(topic,depTopic,language);
      }
      ipw.println(';');
    }


    printElements(topic, language);
    printSelfStandingConstraints(selfStandingConstraints,language);
    ipw.unindent();
    ipw.println ();
    ipw.print("END ");
    printName(topic,language);
    ipw.println(';');
    selfStandingConstraints=null;
  }
protected void printSelfStandingConstraints(List<Constraint> selfStandingConstraints,String language) {
    Iterator<Constraint> csi=selfStandingConstraints.iterator();
    Viewable view=null;
	Viewable lastView=null;
    while(csi.hasNext()){
    	Constraint cs=csi.next();
    	view=(Viewable)cs.getContainer();
    	if(view!=lastView){
    		if(lastView!=null){
				ipw.unindent();
				ipw.println("END;");
    		}else{
				ipw.println ();
    		}
    		lastView=view;
			ipw.print("CONSTRAINTS OF ");
			if (language == null) {
			    ipw.print(view.getName());
			} else {
			    String name = getNameInLanguage(view, language);
			    if (name == null || name == "") {
			        ipw.print(view.getName());
			    } else {
			        ipw.print(name);
			    }
			}
			
			ipw.println('=');
			ipw.indent();
    	}
    	printConstraint(cs, language);
    }
	if(lastView!=null){
		ipw.unindent();
		ipw.println("END;");
	}
}
protected Iterator getTopicDependsOn(Topic topic) {
    return topic.getDependentOn();
}
protected Domain getTopicClassOid(Topic topic) {
    Domain classOid=topic.getOid();
    return classOid;
}
protected Domain getTopicBasketOid(Topic topic) {
    Domain basketOid=topic.getBasketOid();
    return basketOid;
}

protected void printAbstractClassDef (AbstractClassDef def, String language)
  {
    printDocumentation(def,language);
	printMetaValues(def,def.getMetaValues(), language, def.getScopedName());

	String keyword;
	if(def instanceof Table){
		Table tdef=(Table)def;
		if (tdef.isIdentifiable())
		  keyword = "CLASS";
		else
		  keyword = "STRUCTURE";
	}else if(def instanceof AssociationDef){
		keyword = "ASSOCIATION";
	}else{
		throw new IllegalArgumentException();
	}

    printStart (keyword, def, /* based on */ null,language);
    ipw.println (" =");
    ipw.indent ();
    Domain oid = getAbstarctClassDefOid(def);
    if(oid!=null){
    	if(oid instanceof NoOid){
    	    ipw.println ("NO OID;");
    	}else{
    	    ipw.print ("OID AS ");
    	    printRef(def.getContainer(),oid,language);
    	    ipw.println (";");
    	}
    }
	printElements(def, language);
	printEnd(def, language);
  }
protected Domain getAbstarctClassDefOid(AbstractClassDef def) {
    Domain oid=def.getDefinedOid();
    return oid;
}

  private void printRenamedViewableRef (View view, ViewableAlias ref, String language)
  {
    if (ref == null)
    {
      printError ();
      return;
    }


    String name = ref.getName();
    if(language!=null) {
        String scopedName=view.getScopedName()+"."+ref.getName();
        name=getNameInLanguage(scopedName, language);
        if(name==null || name.equals("")) {
            name=ref.getName();
        }
    }
    
    Viewable v = ref.getAliasing ();


    if ((name == null) || (v == null))
    {
      printError ();
      return;
    }


    if (!name.equals (v.getName()))
    {
      ipw.print (name);
      ipw.print ('~');
    }
    printRef(view.getContainer(),v,language);
  }

protected void printRenamedViewableRefs (View scope, ViewableAlias[] refs, String language)
  {
    if ((refs == null) || (refs.length == 0))
      return;


    printRenamedViewableRef (scope, refs[0], language);
    for (int i = 1; i < refs.length; i++)
    {
      ipw.print (", ");
      printRenamedViewableRef (scope, refs[i], language);
    }
  }



  protected void printStart (String keyword, ExtendableContainer ec, Viewable basedOn,String language)
  {
    if (ec == null)
      return;


    ExtendableContainer extending = (ExtendableContainer) ec.getExtending ();
    boolean extendingSameName = false;
    if (extending != null)
    {
      Topic myTopic = (Topic) ec.getContainer (Topic.class);
      Topic extendedTopic = (Topic) extending.getContainer (Topic.class);


      if ((myTopic != null) && (extendedTopic != null)
        && myTopic.isExtending (extendedTopic)
        && ec.getName().equals (extending.getName()))
      {
        extendingSameName = true;
      }
    }


    ipw.print (keyword);
    ipw.print (' ');
    printName(ec,language);
    boolean _transient=false;
    if(ec instanceof View){
    	_transient=((View)ec).isTransient();
    }
    printModifiers (ec.isAbstract(), ec.isFinal(), extendingSameName, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/_transient,/*GENERIC*/false);


    if ((extending != null) && !extendingSameName)
    {
      ipw.print(" EXTENDS ");
      printRef(ec.getContainer(),extending,language);
    }


    if (basedOn != null)
    {
      ipw.print (" BASED ON ");
      printRef(ec.getContainer(),basedOn,language);
    }


  }


  protected void printEnd (ExtendableContainer ec,String language)
  {
    if (ec == null)
      return;


    ipw.unindent ();
    ipw.print ("END ");
    printName(ec,language);
    ipw.println (';');
  }

  public void printView (View view) {
	  printView (view,null);
  }

  public void printView (View view,String language)
  {
	  printView (view,false,language);
  }
  
  public void printView (View view,boolean suppressDoc) {
	  printView(view, suppressDoc, null);
  }
  
  public void printView (View view,boolean suppressDoc, String language)
  {
	  if(!suppressDoc){
	      printDocumentation(view,language);
	      printMetaValues(view,view.getMetaValues(), language, view.getScopedName());
	  }
	  printStart("VIEW", view, /* basedOn */ null, language);
    ipw.println ("");
    ipw.indent ();
    if (view instanceof Projection)
    {
      ipw.print("PROJECTION OF ");
      printRenamedViewableRef(view, ((Projection) view).getSelected(), language);
    }
    else if (view instanceof JoinView)
    {
      ipw.print("JOIN OF ");
      printRenamedViewableRefs (/* scope */ view,
                                ((JoinView) view).getJoining(), language);
    }
    else if (view instanceof UnionView)
    {
      ipw.print("UNION OF ");
      printRenamedViewableRefs (/* scope */ view,
                                ((UnionView) view).getUnited(), language);
    }
    else if (view instanceof DecompositionView)
    {
      ObjectPath decomposedAttribute;
      Viewable      decomposedViewable = null;


      decomposedAttribute = ((DecompositionView) view).getDecomposedAttribute ();
      if (((DecompositionView) view).isAreaDecomposition())
        ipw.print ("AREA ");
      ipw.print ("DECOMPOSITION OF ");


      if (decomposedAttribute == null)
        printError ();
      else
      {
      	// TODO ce 7.2.2002
        //ViewableAlias alias = decomposedAttribute.getStart();
        //if (alias != null)
        //  decomposedViewable = alias.getAliasing();
        //
        //printRef (/* scope */ view, decomposedViewable);
        //ipw.print (':');
        //printAttributePath (/* scope */ view, decomposedAttribute);
      }
    }
    else {
      printError ();
      ipw.println("<unknown view type>");
    }
    ipw.println (";");
    
    printSelection(view, language);

    ipw.println ("=");

	printElements(view, language);
	printEnd(view, language);
  }
protected void printSelection(Container view, String language) {
    Iterator it = view.iterator();
    while (it.hasNext()) {
        Element elt=(Element)it.next();
        if (elt instanceof ExpressionSelection)
        {
          ipw.println("WHERE");
          ipw.indent();
          printExpression (((ExpressionSelection) elt).getSelected(),
                           ((ExpressionSelection) elt).getCondition(), language);
          ipw.println (';');
          ipw.unindent();
        }
    }
}
  
  public void printGraphic (Graphic graph) {
	  printGraphic(graph,null);
  }
  
  public void printGraphic (Graphic graph,String language)
  {
	  printGraphic (graph,false,language);
  }
  
  public void printGraphic (Graphic graph,boolean suppressDoc) {
	  printGraphic(graph,suppressDoc, null);
  }
  public void printGraphic (Graphic graph,boolean suppressDoc, String language)
  {
    if (graph == null)
      return;

    if(!suppressDoc){
        printDocumentation(graph,language);
    	printMetaValues(graph,graph.getMetaValues(), language, graph.getScopedName());
    }
   printStart ("GRAPHIC", graph, /* basedOn */ graph.getBasedOn(),language);
   ipw.println (" =");
   ipw.indent ();
   printSelection(graph,language);
    printElements (graph,language);
    printEnd (graph, language);
  }

  protected void printExpression (Container scope, Evaluable expr,String language)
  {
    printExpression (scope, expr, 0,language);
  }



  protected void printExpression (Container scope, Evaluable expr, int precedence, String language)
  {
    int exprPrec = 0;

    if (expr instanceof ObjectPath)
    {
      printAttributePath (scope, (ObjectPath) expr,language);
      return;
    }



    if (expr instanceof Constant.Undefined)
    {
      ipw.print ("UNDEFINED");
      return;
    }


    if (expr instanceof Constant.Numeric)
    {
      Constant.Numeric cnum = (Constant.Numeric) expr;
      ipw.print (cnum.getValue ());
      if (cnum.getUnit() != null)
      {
        ipw.print ('[');
        printRef (scope, cnum.getUnit(),language);
        ipw.print (']');
      }
      return;
    }


    if (expr instanceof Constant.Text)
    {
      String value = ((Constant.Text) expr).getValue();
      ipw.print ('"');
      ipw.print (InterlisString.escapeSpecialChars(value));
      ipw.print ('"');
      return;
    }

    if (expr instanceof Constant.AttributePath)
    {
      ipw.print (">>");
      if (language == null) {
          ipw.print (((Constant.AttributePath) expr).getValue().getName());
      } else {
          String name = getNameInLanguage(((Constant.AttributePath) expr).getValue(), language);
          if (name == null || name == "") {
              
          } else {
              ipw.print(name);
          }
      }
      return;
    }

    if (expr instanceof Constant.Enumeration)
    {
      ipw.print ('#');
      String[] val = ((Constant.Enumeration) expr).getValue ();
      if (val == null)
        printError ();
      else
      {
          String scopedName=null;
          Element attrEle = ((Constant.Enumeration) expr).getSourceOfType();
          if(attrEle!=null) {
              if(attrEle instanceof AttributeDef) {
                  AttributeDef attr=(AttributeDef)attrEle;
                  if(attr.getDomain() instanceof TypeAlias) {
                      Domain domain=((TypeAlias)attr.getDomain()).getAliasing();
                      scopedName=domain.getScopedName();
                  }else {
                      scopedName=attr.getScopedName();
                  }
              }else if(attrEle instanceof Domain) {
                  scopedName=((Domain)attrEle).getScopedName();
              }
          }
          if(language==null) {
              for (int i = 0; i < val.length; i++)
              {
                if (i > 0) {
                  ipw.print ('.');
                }
                if (val[i] == null) {
                  printError ();
                }else {
                  ipw.print (val[i]);
                }
              }
          }else {
              String namePrefix[]=new String[val.length+1];
              namePrefix[0]=scopedName;
              for (int i = 0; i < val.length; i++)
              {
                if (i > 0) {
                  ipw.print ('.');
                }
                namePrefix[i+1]=namePrefix[i]+"."+val[i];
                String nameEle=getNameInLanguage(namePrefix[i+1], language);
                if(nameEle!=null && nameEle.length()>0) {
                    ipw.print (nameEle);
                }else {
                    ipw.print (val[i]);
                }
              }
              
          }
      }
      return;
    }


    if (expr instanceof Constant.ReferenceToMetaObject)
    {
      MetaObject refMO = ((Constant.ReferenceToMetaObject) expr).getReferred();


      ipw.print ('{');
      if (refMO != null)
        if (language == null) {
            ipw.print (refMO.getName());
        } else {
            String name = getNameInLanguage(refMO, language);
            if (name == null || name == "") {
                ipw.print (refMO.getName());
            } else {
                ipw.print (name);
            }
        }
        
      else
        printError ();
      ipw.print ('}');
      return;
    }


    if (expr instanceof Constant.Structured)
    {
      String val = ((Constant.Structured) expr).toString ();
      if (val == null)
        printError ();
      else
        ipw.print (val);
      return;
    }


    if (expr instanceof FunctionCall)
    {
      FunctionCall f = (FunctionCall) expr;
      printRef (scope, f.getFunction(),language);
      ipw.print ("(");
      Evaluable[] args = f.getArguments();
      if (args == null)
        printError ();
      else
        for (int i = 0; i < args.length; i++)
        {
          if (i > 0)
            ipw.print (", ");
          printExpression (scope, args [i], language);
        }
      ipw.print (')');
      return;
    }



    if (expr instanceof ParameterValue)
    {
      ipw.print ("PARAMETER ");
      printRef (scope, ((ParameterValue) expr).getParameter (),language);
      return;
    }


    if (expr instanceof Expression.Implication)
    {
        printExpression (scope, ((Expression.Implication) expr).getLeft(), exprPrec, language);
        ipw.print (" => ");
        printExpression (scope, ((Expression.Implication) expr).getRight(), exprPrec, language);
        return;
    }

    if (expr instanceof Expression.Disjunction)
    {
      Evaluable[] disjoined = ((Expression.Disjunction) expr).getDisjoined ();
      for (int i = 0; i < disjoined.length; i++)
      {
        if (i > 0)
          ipw.print (" OR ");


        printExpression (scope, disjoined[i], exprPrec, language);
      }
      return;
    }
    if (expr instanceof Expression.Addition)
    {
        printExpression (scope, ((Expression.Addition) expr).getLeft(), exprPrec, language);
        ipw.print (" + ");
        printExpression (scope, ((Expression.Addition) expr).getRight(), exprPrec, language);
        return;
    }
    if (expr instanceof Expression.Subtraction)
    {
        printExpression (scope, ((Expression.Subtraction) expr).getLeft(), exprPrec, language);
        ipw.print (" - ");
        printExpression (scope, ((Expression.Subtraction) expr).getRight(), exprPrec, language);
        return;
    }


    if (expr instanceof Expression.Conjunction)
    {
      Evaluable[] conjoined = ((Expression.Conjunction) expr).getConjoined ();
      for (int i = 0; i < conjoined.length; i++)
      {
        if (i > 0)
          ipw.print (" AND ");


        printExpression (scope, conjoined[i], exprPrec, language);
      }
      return;
    }
    if (expr instanceof Expression.Multiplication)
    {
        printExpression (scope, ((Expression.Multiplication) expr).getLeft(), exprPrec, language);
        ipw.print (" * ");
        printExpression (scope, ((Expression.Multiplication) expr).getRight(), exprPrec, language);
        return;
    }
    if (expr instanceof Expression.Division)
    {
        printExpression (scope, ((Expression.Division) expr).getLeft(), exprPrec, language);
        ipw.print (" / ");
        printExpression (scope, ((Expression.Division) expr).getRight(), exprPrec, language);
        return;
    }

    if (expr instanceof Expression.Negation)
    {
      ipw.print ("NOT (");
      printExpression (scope, ((Expression.Negation) expr).getNegated(), exprPrec, language);
	  ipw.print (")");
      return;
    }
    if (expr instanceof Expression.Subexpression)
    {
      ipw.print ("(");
      printExpression (scope, ((Expression.Subexpression) expr).getSubexpression(), exprPrec, language);
      ipw.print (")");
      return;
    }


    if (expr instanceof Expression.Equality)
    {
      printExpression (scope, ((Expression.Equality) expr).getLeft(), exprPrec, language);
      ipw.print (" == ");
      printExpression (scope, ((Expression.Equality) expr).getRight(), exprPrec, language);
      return;
    }


    if (expr instanceof Expression.Inequality)
    {
      printExpression (scope, ((Expression.Inequality) expr).getLeft(), exprPrec, language);
      ipw.print (" <> ");
      printExpression (scope, ((Expression.Inequality) expr).getRight(), exprPrec, language);
      return;
    }


    if (expr instanceof Expression.LessThanOrEqual)
    {
      printExpression (scope, ((Expression.LessThanOrEqual) expr).getLeft(), exprPrec, language);
      ipw.print (" <= ");
      printExpression (scope, ((Expression.LessThanOrEqual) expr).getRight(), exprPrec, language);
      return;
    }


    if (expr instanceof Expression.GreaterThanOrEqual)
    {
      printExpression (scope, ((Expression.GreaterThanOrEqual) expr).getLeft(), exprPrec, language);
      ipw.print (" >= ");
      printExpression (scope, ((Expression.GreaterThanOrEqual) expr).getRight(), exprPrec, language);
      return;
    }


    if (expr instanceof Expression.LessThan)
    {
      printExpression (scope, ((Expression.LessThan) expr).getLeft(), exprPrec, language);
      ipw.print (" < ");
      printExpression (scope, ((Expression.LessThan) expr).getRight(), exprPrec, language);
      return;
    }


    if (expr instanceof Expression.GreaterThan)
    {
      printExpression (scope, ((Expression.GreaterThan) expr).getLeft(), exprPrec, language);
      ipw.print (" > ");
      printExpression (scope, ((Expression.GreaterThan) expr).getRight(), exprPrec, language);
      return;
    }

    if (expr instanceof Expression.DefinedCheck)
    {
      ipw.print ("DEFINED(");
      printExpression (scope, ((Expression.DefinedCheck) expr).getArgument(), exprPrec, language);
      ipw.print (')');
      return;
    }



    if (expr instanceof ConditionalExpression)
    {
      ConditionalExpression.Condition[] conds = ((ConditionalExpression) expr).getConditions();


      ipw.print ("WITH ");
      printAttributePath (scope, ((ConditionalExpression) expr).getAttribute(),language);
      ipw.println (" (" /*)*/ );
      ipw.indent ();


      if (conds == null)
        printError();
      else
      {
        for (int i = 0; i < conds.length; i++)
        {
          if (i != 0)
            ipw.println (",");


          if (conds[i] == null)
          {
            printError();
            continue;
          }


          printExpression (scope, conds[i].getValue(),language);
          ipw.print (" WHEN IN ");
          printExpression (scope, conds[i].getCondition(),language);
        }
      }


      ipw.unindent ();
      ipw.print (/*(*/ ')');
      return;
    }


    if (expr instanceof Objects) {
        ipw.print("ALL");
        return;
    }


    if (expr instanceof ValueRefThis) {
        ipw.print("THIS");
        return;
    }


    printError ();
  }

    protected void printExistenceConstraint(ExistenceConstraint ec, String language) {
        ipw.print("EXISTENCE CONSTRAINT ");
        if (ec.hasCustomName()) {
            ipw.print(ec.getName() + ": ");
        }
        ObjectPath attr = ec.getRestrictedAttribute();
        printAttributePath(ec.getContainer(), attr, language);
        ipw.print(" REQUIRED IN ");
        Iterator<ObjectPath> reqi = ec.iteratorRequiredIn();
        String next = "";
        while (reqi.hasNext()) {
            ObjectPath req = reqi.next();
            ipw.print(next);
            next = " OR ";
            printRef(ec.getContainer(), req.getRoot(), language);
            ipw.print(":");
            printAttributePath(ec.getContainer(), req, language);
        }
        ipw.println(';');
    }

    protected void printSetConstraint(SetConstraint sc, String language) {
        ipw.print("SET CONSTRAINT");
        if (sc.perBasket()) {
            ipw.print(" (BASKET)");
        }
        if (sc.hasCustomName()) {
            ipw.print(" " + sc.getName() + ":");
        }
        if (sc.getPreCondition() != null) {
            ipw.print(" WHERE ");
            printExpression(sc.getContainer(), sc.getPreCondition(), language);
            ipw.print(":");
        }
        ipw.println();
        ipw.indent();
        printExpression(sc.getContainer(), sc.getCondition(), language);
        ipw.println(';');
        ipw.unindent();
    }

    protected void printUniquenessConstraint(UniquenessConstraint uc, String language) {
        UniqueEl uel = uc.getElements();
        Iterator<ObjectPath> pathi = uel.iteratorAttribute();
        ipw.print("UNIQUE ");
        if (uc.perBasket()) {
            ipw.print("(BASKET) ");
        }
        if (uc.hasCustomName()) {
            ipw.print(uc.getName() + ": ");
        }
        if (uc.getPreCondition() != null) {
            ipw.print("WHERE ");
            printExpression(uc.getContainer(), uc.getPreCondition(), language);
            ipw.println(": ");
        }
        if (uc.getLocal()) {
            ipw.print("(LOCAL) ");
            ObjectPath prefix = uc.getPrefix();
            printAttributePath(uc.getContainer(), prefix, language);
            String next = ": ";
            while (pathi.hasNext()) {
                ObjectPath path = pathi.next();
                ipw.print(next);
                next = ", ";
                printAttributePath(uc.getContainer(), path, language);
            }
        } else {
            String next = "";
            while (pathi.hasNext()) {
                ObjectPath path = pathi.next();
                ipw.print(next);
                next = ", ";
                printAttributePath(uc.getContainer(), path, language);
            }
        }
        ipw.println(';');
    }

    protected void printPlausibilityConstraint(PlausibilityConstraint pc, String language) {
        ipw.print("CONSTRAINT ");
        if (pc.hasCustomName()) {
            ipw.print(pc.getName() + ": ");
        }
        if (pc.getDirection() == PlausibilityConstraint.DIRECTION_AT_LEAST) {
            ipw.print(">= ");
        } else {
            ipw.print("<= ");
        }
        ipw.print(pc.getPercentage());
        ipw.print("% ");
        printExpression(pc.getContainer(), pc.getCondition(), language);
        ipw.println(';');
    }

    protected void printMandatoryConstraint(MandatoryConstraint mc, String language) {
        ipw.print("MANDATORY CONSTRAINT ");
        if (mc.hasCustomName()) {
            ipw.print(mc.getName() + ": ");
        }
        printExpression(mc.getContainer(), mc.getCondition(), language);
        ipw.println(';');
    }

    protected void printDomainConstraint(DomainConstraint constraint, String language) {
        // the domain constraint name is mandatory
        ipw.print(constraint.getName() + ": ");
        printExpression(constraint.getContainer(), constraint.getCondition(), language);
    }

  public void printConstraint(Constraint elt) {
	  printConstraint(elt,null);
  }
  
  public void printConstraint(Constraint elt,String language)
  {
	  printConstraint(elt,false,language);  
  }
  
  public void printConstraint(Constraint elt,boolean suppressDoc) {
	  printConstraint(elt,suppressDoc,null);
  }

    public void printConstraint(Constraint elt, boolean suppressDoc, String language) {
        if (!suppressDoc) {
            printDocumentation(elt, language);
            printMetaValues(elt, elt.getMetaValues(), language, elt.getScopedName());
        }
        if (elt instanceof MandatoryConstraint) {
            printMandatoryConstraint((MandatoryConstraint) elt, language);
        } else if (elt instanceof PlausibilityConstraint) {
            printPlausibilityConstraint((PlausibilityConstraint) elt, language);
        } else if (elt instanceof UniquenessConstraint) {
            printUniquenessConstraint((UniquenessConstraint) elt, language);
        } else if (elt instanceof ExistenceConstraint) {
            printExistenceConstraint((ExistenceConstraint) elt, language);
        } else if (elt instanceof SetConstraint) {
            printSetConstraint((SetConstraint) elt, language);
        } else if (elt instanceof DomainConstraint) {
            printDomainConstraint((DomainConstraint) elt, language);
        }
    }

  public void printGraphicParameterDef(GraphicParameterDef gfxp) {
	  printGraphicParameterDef(gfxp,null);  
  }

  public void printGraphicParameterDef(GraphicParameterDef gfxp,String language)
  {
	  printGraphicParameterDef(gfxp,false,language);
  }
  public void printGraphicParameterDef(GraphicParameterDef gfxp,boolean suppressDoc) {
	  printGraphicParameterDef(gfxp,suppressDoc,null);
  }
  public void printGraphicParameterDef(GraphicParameterDef gfxp,boolean suppressDoc,String language)
  {
	  if(!suppressDoc){
	      printDocumentation(gfxp,language);
	      printMetaValues(gfxp,gfxp.getMetaValues(), language, gfxp.getScopedName());
	  }
	  printName(gfxp,language);
	String scopedNamePrefix = gfxp.getScopedName();
    ipw.print(" : ");
    printType(gfxp.getContainer(),gfxp.getDomain(), language, scopedNamePrefix);
    ipw.println(";");
  }
  public void printMetaDataUseDef(MetaDataUseDef mu) {
	  printMetaDataUseDef(mu,null);
  }
  public void printMetaDataUseDef(MetaDataUseDef mu,String language)
  {
	  printMetaDataUseDef(mu,false, language);
  }
  public void printMetaDataUseDef(MetaDataUseDef mu,boolean suppressDoc) {
	  printMetaDataUseDef(mu,suppressDoc,null);
  }
  public void printMetaDataUseDef(MetaDataUseDef mu,boolean suppressDoc,String language)
  {
	  if(!suppressDoc){
	      printDocumentation(mu,language);
			printMetaValues(mu,mu.getMetaValues(), language, mu.getScopedName());
	  }
    if(mu.isSignData()){
      ipw.print("SIGN BASKET ");
    }else{
      ipw.print("REFSYSTEM BASKET ");
    }
    
    printName(mu,language);
    
    printModifiers(/*ABSTRACT*/false, mu.isFinal(),/*EXTENDED*/false, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false,/*GENERIC*/false);

    /* TODO ce 2002-05-07 handle EXTENDS in MetaDataUseDef
    MetaDataUseDef extending = (MetaDataUseDef) mu.getExtending();
    if (extending != null)
    {
      ipw.print(" EXTENDS ");
      ipw.print(extending.getScopedName(mu.getContainer()));
    }
    */
    Topic topic=mu.getTopic();
    ipw.print("~");
    printRef(mu.getContainer(),topic,language);
    ipw.indent();
    ipw.indent();
    Table lastTable=null;
    String sep="";
    for(Iterator moi=mu.iterator();moi.hasNext();){
    	Object moo=moi.next();
    	if(moo instanceof MetaObject){
    		MetaObject mo=(MetaObject)moo;
    	    Table table=mo.getTable();
    	    if(table!=lastTable){
       	    	ipw.println();
    	        ipw.unindent();
    	    	ipw.print("OBJECTS OF ");
    	    	printName(table,language);
    	    	ipw.println(":");
    	        ipw.indent();
    	    	lastTable=table;
    	    }else{
    	    	ipw.println(",");
    	    }
    	    if (language == null) {
    	        printDocumentation(mo.getDocumentation());
    	    } else {
    	        String docu = getElementDocumentation(mo,language);
    	        if (docu == null || docu == "") {
    	            printDocumentation(mo.getDocumentation());
    	        } else {
    	            printDocumentation(docu);
    	        }
    	    }
	    	
	    	printMetaValues(mo,mo.getMetaValues(), language, mo.getScopedName());
	    	printName(mo,language);
    	}
    }
    ipw.println(";");
    ipw.unindent();
    ipw.unindent();

  }
  
  public void printUnit (Container scope, Unit u) {
	  printUnit (scope, u, null);
  }

  public void printUnit (Container scope, Unit u, String language)
  {
	  printUnit (scope, u,false, language);
  }
  
  public void printUnit (Container scope, Unit u,boolean suppressDoc) {
	  printUnit(scope, u,suppressDoc,null);
  }
  
  public void printUnit (Container scope, Unit u,boolean suppressDoc,String language)
  {
    if (u == null)
    {
      printError ();
      return;
    }


    Unit extending = (Unit)u.getExtending();

    if(!suppressDoc){
        printDocumentation(u,language);
    	printMetaValues(u,u.getMetaValues(), language, u.getScopedName());
    }
    ipw.print(u.getDocName());
    if (!u.getDocName().equals(u.getName())) {
      ipw.print(" [");
		if (language == null) {
			ipw.print(u.getName());
		} else {
			String value = getNameInLanguage(u, language);
			if (value == null || value == "") {
			    ipw.print(u.getName());
			} else {
			    ipw.print(value);
			}
		}
      ipw.print(']');
    }


    printModifiers (u.isAbstract(), /* FINAL */ false, /* EXTENDED */ false, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false,/*GENERIC*/false);


    if ((extending != null) && (extending != anyUnit) && (!(u instanceof DerivedUnit)))
    {
      ipw.print(" EXTENDS ");
     //ipw.print(extending.getScopedName(scope));
      printRef(scope,extending,language);
    }


    if (u instanceof NumericallyDerivedUnit)
    {
      NumericallyDerivedUnit.Factor[] factors;


      factors = ((NumericallyDerivedUnit) u).getConversionFactors();
      ipw.print(" =");


      if (factors.length >= 1)
      {
        for (int i = 0; i < factors.length; i++)
        {
          if (i > 0)
          {
            ipw.print (' ');
            ipw.print (factors[i].getConversionOperator());
          }
          ipw.print (' ');
          printNumericConst (factors[i].getConversionFactor());
        }
      }


      ipw.print (" [");
      printRef (scope, ((NumericallyDerivedUnit) u).getExtending(), language);
      ipw.print (']');
    }
    else if (u instanceof FunctionallyDerivedUnit)
    {
      ipw.print (" = FUNCTION ");
      printExplanation (((FunctionallyDerivedUnit) u).getExplanation ());
      ipw.print (" [");
      printRef (scope, ((FunctionallyDerivedUnit) u).getExtending(), language);
      ipw.print (']');
    }
    else if (u instanceof ComposedUnit)
    {
      ComposedUnit.Composed[] composed;


      composed = ((ComposedUnit) u).getComposedUnits();
      ipw.print(" = (");
      for (int i = 0; i < composed.length; i++)
      {
      	if(i>0){
	        ipw.print (' ');
	        ipw.print (composed[i].getCompositionOperator());
	        ipw.print (' ');
		}
        printRef (scope, composed[i].getUnit(),language);
      }
      ipw.print(')');
    }
    else if (u instanceof StructuredUnit)
    {
      StructuredUnit.Part[] parts;

      parts = ((StructuredUnit) u).getParts();
      ipw.print (" = {");
      printRef (scope, ((StructuredUnit) u).getFirstUnit(), language);
      for (int i = 0; i < parts.length; i++)
      {
        ipw.print (':');
        printRef (scope, parts[i].getUnit (), language);
        ipw.print ('[');
        ipw.print (parts[i].getMinimum ());
        ipw.print ("..");
        ipw.print (parts[i].getMaximum ());
        ipw.print (']');
      }
      ipw.print ('}');


      if (((StructuredUnit) u).isContinuous())
        ipw.print (" CONTINUOUS");
    }

    if (u instanceof StructuredUnit){
        EhiLogger.logError("UNIT "+u.getName()+": StructuredUnit not supported by INTERLIS 2.3");
        ipw.println("; !! Hint: comment out/remove");
    }else{
        ipw.println(';');
    }

  }

    public void printContextSyntax(Container<?> scope, ContextDef contextDef) {
      printContextSyntax(scope, contextDef, null);
    }

    public void printContextSyntax(Container<?> scope, ContextDef contextDef, String language) {
        printRef(scope, contextDef.getGeneric(), language);
        ipw.print(" = ");
        boolean first = true;
        for (Domain domain : contextDef.getConcretes()) {
            if (first) {
                first = false;
            } else {
                ipw.print(" OR ");
            }
            printRef(scope, domain, language);
        }
        ipw.println(";");
    }

    public void printContext(Container<?> scope, ContextDef contextDef) {
        printContext(scope, contextDef, false, null);
    }

    public void printContext(Container<?> scope, ContextDef contextDef, boolean suppressDoc, String language) {
        if (contextDef == null) {
            printError();
            return;
        }

        if (!suppressDoc) {
            printDocumentation(contextDef, language);
            printMetaValues(contextDef, contextDef.getMetaValues(), language, contextDef.getScopedName());
        }

        ipw.print(contextDef.getName());
        ipw.println(" =");
        ipw.indent();

        printContextSyntax(scope, contextDef, language);

        ipw.unindent();
    }

  private void printName(Element elt, String language) {
      ipw.print(getElementName(elt,language));
  }
  protected String getElementName(Element elt,String language)
  {
      if (language == null) {
          if (params!=null && params.getNewModelName() != null && elt instanceof Model) {
              return params.getNewModelName();
          }else {
              return elt.getName();
          }
      } else {
          String name = getNameInLanguage(elt, language);
          if (name==null || name == "") {
              return elt.getName();
          } else {
              return name;
          }
      }
  }
  protected void printRef (Container scope, Element elt,String language)
  {
    if (elt == null){
      printError ();
    }else if(elt==modelInterlis.ANYCLASS){
		ipw.print ("ANYCLASS");
    }else if(elt==modelInterlis.ANYSTRUCTURE){
        ipw.print ("ANYSTRUCTURE");
    }else if(elt==modelInterlis.XmlDate && doIli24){
        ipw.print ("DATE");
    }else if(elt==modelInterlis.XmlDateTime && doIli24){
        ipw.print ("DATETIME");
    }else if(elt==modelInterlis.XmlTime && doIli24){
        ipw.print ("TIMEOFDAY");
    }else{
        if (language == null) {
			if (params != null && params.getImportModels()!=null) {
	      		ModelTransformation[] importModels = params.getImportModels();
	      		for (ModelTransformation importModel : importModels) {
	      			if (elt.getScopedName(scope).contains(importModel.getFromModel())) {
	      				String newScopeName = "";
	      				String[] scopeName = elt.getScopedName(scope).split("\\.");
	      				for (String scopeN : scopeName) {
	      					if (scopeN.equals(importModel.getFromModel())) {
	      						newScopeName = importModel.getToModel();
	      					} else {
	      						newScopeName = newScopeName + "."  + scopeN;
	      					}
	      				}
	      				ipw.print(newScopeName);
	      			} else {
	      				//ipw.print(elt.getScopedName(scope));
                        ipw.print(elt.getScopedName(null));
	      			}
	      		}
			} else {
                //ipw.print(elt.getScopedName(scope));
				ipw.print(elt.getScopedName(null));
			}
		} else {
		    String text = getNameInLanguage(elt, language);
		    if (text == "" || text == null) {
		        //ipw.print(elt.getScopedName(scope));
                ipw.print(elt.getScopedName(null));
		    } else {
		        ipw.print(text);
		    }
		}
    }
  }
  
  public void printParameter (Container scope, Parameter par) {
	  printParameter(scope, par,null);
  }

  public void printParameter (Container scope, Parameter par,String language)
  {
	  printParameter (scope, par,false,language);
  }
  
  public void printParameter (Container scope, Parameter par,boolean suppressDoc) {
	  printParameter(scope, par,suppressDoc,null);
  }
  
  public void printParameter (Container scope, Parameter par,boolean suppressDoc,String language)
  {
	String scopedNamePrefix = "";
    if (par == null)
    {
      printError ();
      return;
    }


    if(!suppressDoc){
        printDocumentation(par,language);
    	printMetaValues(par,par.getMetaValues(), language, par.getScopedName());
    }
    if (language == null) {
    	ipw.print(par.getName());
    } else {
        String name = getNameInLanguage(par, language);
        if (name == "" || name == null) {
            ipw.print(par.getName());
        } else {
            ipw.print(name);
        }
    }

    Parameter ext = par.getExtending ();
    if (ext != null)
    {
      if (par.getName().equals (ext.getName()))
        ipw.print (" (EXTENDED)");
      else
      {
        ipw.print (" EXTENDS ");
        printRef (scope, ext,language);
      }
    }


    ipw.print(": ");


    Type typ = par.getType();
    if (typ instanceof ReferenceType)
    {
      ipw.print ("-> ");
      printRef (scope, ((ReferenceType) typ).getReferred(),language);
    }
    else
    	scopedNamePrefix = typ.getScopedName();
      printType (scope, typ,language,scopedNamePrefix);
    ipw.println(';');
  }



  private void printNumericConst (PrecisionDecimal num)
  {
    if (num == PrecisionDecimal.PI)
    {
      ipw.print("PI");
      return;
    }
    if (num == PrecisionDecimal.LNBASE)
    {
      ipw.print("LNBASE");
      return;
    }
    ipw.print(num.toString());
  }


  protected void printRoleDef(Container scope,
								RoleDef role, String language)
  {

      printDocumentation(role,language);
      printMetaValues(role,role.getMetaValues(), language, role.getScopedName());
      printName(role,language);
	printModifiers(role.isAbstract(), role.isFinal(),
	  role.isExtended(),role.isOrdered(),role.isExternal(),/*TRANSIENT*/false,/*GENERIC*/false);
	String kind="";
	switch(role.getKind()){
	  case RoleDef.Kind.eASSOCIATE:
		kind=" -- ";
		break;
	  case RoleDef.Kind.eAGGREGATE:
	  	kind=" -<> ";
		break;
	  case RoleDef.Kind.eCOMPOSITE:
	  	kind=" -<#> ";
		break;
	}
	ipw.print(kind);
	if(role.getDefinedCardinality()!=null){
	  ipw.print(role.getDefinedCardinality()+" ");
	}
	printRef (scope, role.getDestination(), language);
	Iterator resti=role.getReference().iteratorRestrictedTo();
	String sep=" RESTRICTION (";
	boolean hasRestriction=false;
	while(resti.hasNext()){
		AbstractClassDef rest=(AbstractClassDef)resti.next();
		ipw.print(sep);sep=";";
		printRef (scope, rest,language);
		hasRestriction=true;
	}
	if(hasRestriction){
		ipw.print(")");
	}
	ipw.println(';');
		
  }
  
  protected void printAttribute(Container scope,
                                AttributeDef attrib, String language)
  {
    if (attrib == null)
    {
      printError ();
      return;
    }
	if(attrib instanceof LocalAttribute){
		LocalAttribute la=(LocalAttribute)attrib;
		if(la.isGeneratedByAllOf()){
			return;
		}
		
	}
	Type proxyType=attrib.getDomain();
	if(proxyType!=null && (proxyType instanceof ObjectType)){
		if(((ObjectType)proxyType).isAllOf()){
            ipw.print("ALL OF ");
		    printName(attrib,language);
            ipw.println(";");
		}else{
			// skip implicit particles (base-viewables) of views
		}
		return;
	}
    printDocumentation(attrib,language);
	printMetaValues(attrib,attrib.getMetaValues(), language, attrib.getScopedName());

	if(attrib instanceof LocalAttribute){
		LocalAttribute la=(LocalAttribute)attrib;
		if(la.isSubdivision()){
			if(la.isContinuous()){
				ipw.print("CONTINUOUS ");
			}
			ipw.print("SUBDIVISION ");
		}
	}
    printName(attrib,language);
    printModifiers(attrib.isAbstract(), attrib.isFinal(),
      /* EXTENDED */ attrib.getExtending() != null, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/attrib.isTransient(),/*GENERIC*/false);


    if (attrib instanceof LocalAttribute){
      if(attrib.getDomain()==null && scope instanceof View){
    	  // skip typ if attribute inside ViewDef
      }else{
          ipw.print(" : ");
          String scopedNamePrefix = attrib.getScopedName();
          printType (scope, attrib.getDomain(),language,scopedNamePrefix);
      }
      printAttributeBasePath(scope, attrib,language);
    }
    if(attrib instanceof LocalAttribute && attrib.getDomain() instanceof StructuredUnitType){
        EhiLogger.logError("ATTRIBUTE "+attrib.getScopedName(null)+": StructuredUnitType not supported by INTERLIS 2.3; replace by TextType or FormattedType/XMLDate");
        ipw.println("; !! Hint: replace by TextType or FormattedType/XMLDate");
    }else{
        ipw.println(';');
    }
  }
  
  public void printAttributeBasePath(Container scope, AttributeDef attrib) {
	  printAttributeBasePath(scope, attrib,null);
  }
  
public void printAttributeBasePath(Container scope, AttributeDef attrib,String language) {
	Evaluable[] paths = ((LocalAttribute) attrib).getBasePaths ();
      if ((paths == null) || (paths.length == 0)){
        ; // nothing
      }else{
        ipw.print (" := ");
        for (int i = 0; i < paths.length; i++)
        {
          if (i > 0)
            ipw.print (", ");
          printExpression(scope, paths[i],language);
        }
      }
}



  protected void printSignAttribute (Graphic scope,
                                     SignAttribute attrib,String language)
  {
    SignAttribute extending = (SignAttribute)attrib.getExtending();

    printDocumentation(attrib,language);
    printName(attrib,language);

    printModifiers(/* ABSTRACT */ false, /* FINAL */ false,
      /* EXTENDED */ extending != null, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false,/*GENERIC*/false);


    if ((extending == null)
        || (attrib.getGenerating() != extending.getGenerating()))
    {
      ipw.print (" OF ");
      printRef (scope, attrib.getGenerating(),language);
    }


    ipw.println(":");
    ipw.indent ();


    SignInstruction[] instructions = attrib.getInstructions ();
    for (int i = 0; i < instructions.length; i++)
    {
      if (i > 0)
        ipw.println (',');
      printSignInstruction (scope.getBasedOn(), instructions[i],language);
    }


    ipw.unindent ();
    ipw.println(';');
  }



  protected void printSignInstruction (Viewable basedOn, SignInstruction instr,String language)
  {
    if (instr == null)
    {
      printError ();
      return;
    }


    Evaluable restrictor = instr.getRestrictor ();
    if (restrictor != null)
    {
      ipw.print ("WHERE ");
      printExpression (basedOn, restrictor,language);
      ipw.println ();
    }
    ipw.println ('(');
    ipw.indent ();


    ParameterAssignment[] assignments = instr.getAssignments();
    for (int i = 0; i < assignments.length; i++)
    {
      if (i > 0)
        ipw.println (';');
      printParameterAssignment (basedOn, assignments[i], language);
    }


    ipw.unindent ();
    ipw.println ();
    ipw.print (')');
  }



  protected void printParameterAssignment (Viewable basedOn, ParameterAssignment parass, String language)
  {
    if (parass == null)
    {
      printError ();
      return;
    }

    Parameter assigned = parass.getAssigned();
    if (assigned == null) {
      printError ();
    }else {
        printName(assigned,language);
    }
    ipw.print (" := ");
    printExpression (basedOn, parass.getValue(), language);
  }
  
  public void printMetaValues(ch.ehi.basics.settings.Settings values) {
	  printMetaValues(null,values, null, null);
  }

  public void printMetaValues(ch.ehi.basics.settings.Settings values, String language, String scopedNamePrefix)
  {
      printMetaValues(null,values,language,scopedNamePrefix);
  }
  protected void printMetaValues(Object ili2cElement,ch.ehi.basics.settings.Settings values, String language, String scopedNamePrefix)
  {
		if (values != null) {
			for (Iterator valuei = values.getValues().iterator(); valuei.hasNext();) {
				String name = (String) valuei.next();
				String value = "";
				if (params!=null && name.equals("CRS")) {
					value  = String.valueOf(params.getEpsgCode());
				} else {
					value = values.getValue(name);
				}
				ipw.print("!!@ ");
				ipw.print(name);
				ipw.print("=");
				String scopedName = scopedNamePrefix + "." + "METAOBJECT" + "." + name;
				if (value.indexOf(' ') != -1 || value.indexOf('=') != -1 || value.indexOf(';') != -1
						|| value.indexOf(',') != -1 || value.indexOf('"') != -1 || value.indexOf('\\') != -1) {
					if (language == null) {
						ipw.println("\""+value+"\"");
					} else {
					    String metaValue = getElementNameInLanguage(scopedName, language);
					    if (metaValue == null || metaValue == "") {
					        ipw.println("\""+value+"\"");
					    } else {
					        ipw.println("\"" + metaValue + "\"");
					    }
					}

				} else {
					if (language == null) {
						ipw.println(value);
					} else {
					    String metaValue = getElementNameInLanguage(scopedName, language);
					    if (metaValue == "" || metaValue == null) {
					        ipw.println(value);
					    } else {
					        ipw.println(metaValue);
					    }
					}
				}
			}
		}
  }
  public void printDocumentation(Element def,String language)
  {
      if (language == null) {
          printDocumentation(def.getDocumentation());
      } else {
          String documentation = getElementDocumentation(def, language);
          if (documentation == "" || documentation == null) { 
              printDocumentation(def.getDocumentation());
          } else {
              printDocumentation(documentation);
          }
      }
  }
  public void printDocumentation(String doc)
	{
	if(doc==null)return;
	if(doc.length()==0)return;
	String beg="/** ";
	// for each line
	int last=0;
	int next=doc.indexOf("\n",last);
	while(next>-1){
	  String line=doc.substring(last,next);
	  ipw.println(beg+line);
	  beg=" * ";
	  last=next+1;
	  next=doc.indexOf("\n",last);
	}
	  String line=doc.substring(last);
	  ipw.println(beg+line);
	  ipw.println(" */");
	}

  protected void printModel (Model mdef,String language)
  {
    printDocumentation(mdef,language);
	printMetaValues(mdef,mdef.getMetaValues(),language,mdef.getScopedName());
	
	if(mdef.isContracted()){
		ipw.print("CONTRACTED ");
	}
	if(mdef instanceof RefSystemModel) {
        ipw.print("REFSYSTEM ");
	}else if(mdef instanceof SymbologyModel) {
        ipw.print("SYMBOLOGY ");
	}else if(mdef instanceof TypeModel) {
        ipw.print("TYPE ");
	}
    ipw.print("MODEL ");
    printName(mdef,language);

	if (language == null) {
		if (mdef.getLanguage() != null) {
			ipw.print(" (" + mdef.getLanguage() + ")");
		}
	} else {
		ipw.print(" (" + language + ")");
	}
	ipw.println ();
	ipw.indent();
	String issuer = getModelIssuer(mdef);
    ipw.println("AT \"" + InterlisString.escapeSpecialChars(issuer) + "\"");
    String version = getModelVersion(mdef);
    ipw.print("VERSION \"" + InterlisString.escapeSpecialChars(version) + "\"");
	String expl = getModelVersionExpl(mdef);
	if ((expl != null) && (expl.length() > 0))
	{
	  ipw.print (' ');
	  printExplanation (expl);
	}
    ipw.println("");
	if (translationConfig!=null) {
        String translationText = "TRANSLATION OF " + mdef.getName() + " [\""
                + InterlisString.escapeSpecialChars(mdef.getModelVersion()) + "\"]";
        ipw.println(translationText);
	}else {
	    // TODO Translation
	}
	ipw.println("=");


    Importable[] imported = mdef.getImporting ();

    String sep="";
    boolean modelsImported=false;
    for (int i = 0; i < imported.length; i++)
    {

      Importable curImport = (Importable) imported[i];

      if (curImport instanceof Model){
      	if(curImport!=modelInterlis){
      		if(!modelsImported){
      		    ipw.print("IMPORTS ");
      		    modelsImported=true;
      		}
      		if (params != null) {
                ModelTransformation[] importModels = params.getImportModels();
                for(ModelTransformation importModel : importModels) {
                    if (importModel.getFromModel().equals(((Model) curImport).getName())) {
                        ipw.print(sep + importModel.getToModel());
                    } else {
                        ipw.print(sep+((Model) curImport).getName());
                    }
                }      		    
      		} else {
      		  ipw.print(sep+((Model) curImport).getName());
      		}
          	sep=", ";
      	}
      }else{
        printError ();
      }
    }
	if(modelsImported){
	    ipw.println(';');
	}

    printElements (mdef, language);

    ipw.unindent();
    ipw.println ();
    ipw.print ("END ");
    printName(mdef,language);
    ipw.println ('.');
  }
protected String getModelVersionExpl(Model mdef) {
    String expl=mdef.getModelVersionExpl();
    return expl;
}
protected String getModelIssuer(Model mdef) {
    String issuer=mdef.getIssuer();
	if(issuer==null){
		issuer="mailto:"+System.getProperty("user.name")+"@localhost";
	}
    return issuer;
}
protected String getModelVersion(Model mdef) {
    String version=mdef.getModelVersion();
    if(version==null){
		java.util.Calendar current=java.util.Calendar.getInstance();
		java.text.DecimalFormat digit4 = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat digit2 = new java.text.DecimalFormat("00");
		version=digit4.format(current.get(java.util.Calendar.YEAR))
			+"-"+digit2.format(current.get(java.util.Calendar.MONTH)+1)
			+"-"+digit2.format(current.get(java.util.Calendar.DAY_OF_MONTH));
    }
    return version;
}

	private String getElementDocumentation(Element ele, String language) {
		String modelName = "";
		Iterator<TranslationElement> iteratorModelElement = translationConfig.iterator();

		while (iteratorModelElement.hasNext()) {
			TranslationElement element = iteratorModelElement.next();
			if (Ili2TranslationXml.getElementInRootLanguage(ele).getScopedName().equals(element.getScopedName())) {
				if (language.equals(FR)) {
					modelName = element.getDocumentation_fr();
					return modelName;
				} else if (language.equals(EN)) {
					modelName = element.getDocumentation_en();
					return modelName;
				} else if (language.equals(IT)) {
					modelName = element.getDocumentation_it();
					return modelName;
				} else if (language.equals(DE)) {
					modelName = element.getDocumentation_de();
					return modelName;
				}
			}
		}
		return "";
	}

	private String getNameInLanguage(Element ele, String language) {

        final String scopedName = Ili2TranslationXml.getElementInRootLanguage(ele).getScopedName();
        return getNameInLanguage(scopedName, language);
	}
    private String getNameInLanguage(String scopedName, String language) {
        Iterator<TranslationElement> iteratorModelElement = translationConfig.iterator();
		while (iteratorModelElement.hasNext()) {
			TranslationElement element = iteratorModelElement.next();
            if (scopedName.equals(element.getScopedName())) {
				if (language.equals(FR)) {
					String modelName = element.getName_fr();
					return modelName;
				} else if (language.equals(EN)) {
					String modelName = element.getName_en();
					return modelName;
				} else if (language.equals(IT)) {
					String modelName = element.getName_it();
					return modelName;
				} else if (language.equals(DE)) {
					String  modelName = element.getName_de();
					return modelName;
				}
			}
		}
		return "";
    }

  protected void printExplanation(String explanationText)
  {
    ipw.print("//");
    ipw.print(explanationText);
    ipw.print("//");
  }

  protected void printDomainDef (Container scope, Domain dd,String language)
  {
    Domain extending = dd.getExtending();
    String scopedNamePrefix = dd.getScopedName();
    
    printDocumentation(dd,language);
	printMetaValues(dd,dd.getMetaValues(), language, dd.getScopedName());
	printName(dd,language);
    if(dd.getType() instanceof TypeAlias && ((TypeAlias)dd.getType()).getAliasing()==td.INTERLIS.INTERLIS_1_DATE){
    	Domain dd2=((TypeAlias)dd.getType()).getAliasing();
        printModifiers (dd2.isAbstract(), dd2.isFinal(),
      	      /* EXTENDED */ false, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false,/*GENERIC*/false);
	    ipw.print(" = ");
	    printType (scope, dd2.getType(),language, scopedNamePrefix);
        ipw.println(';');
    }else{
        boolean generic = false;
        if (dd.getType() instanceof AbstractCoordType) {
            AbstractCoordType coordType = (AbstractCoordType) dd.getType();
            generic = coordType.isGeneric();
        }
        printModifiers (dd.isAbstract(), dd.isFinal(),
        	      /* EXTENDED */ false, /*ORDERED*/false,/*EXTERNAL*/false,/*TRANSIENT*/false, generic);


        	    if (extending != null)
        	    {
        	      ipw.print(" EXTENDS ");
        	      printRef (scope, extending,language);
        	    }


        	    ipw.print(" = ");
        	    printType (scope, dd.getType(),language, scopedNamePrefix);

                Iterator<DomainConstraint> iterator = dd.iteratorConstraints();
                if (iterator.hasNext()) {
                    ipw.print(" CONSTRAINTS ");
                    boolean first = true;
                    while (iterator.hasNext()) {
                        DomainConstraint constraint = iterator.next();
                        if (first) {
                            first = false;
                        } else {
                            ipw.print(", ");
                        }
                        printConstraint(constraint, language);
                    }
                }

        	    if(dd.getType() instanceof StructuredUnitType){
        	        EhiLogger.logError("DOMAIN "+dd.getName()+": StructuredUnitType not supported by INTERLIS 2.3; replace by TextType or FormattedType/XMLDate");
        	        ipw.println("; !! Hint: replace by TextType or FormattedType/XMLDate");
        	    }else{
        	        ipw.println(';');
        	    }
    }
  }



  public void printReferenceSysRef (Container scope, RefSystemRef rsr)
  {
	  printReferenceSysRef(scope, rsr, null);
  }
  public void printReferenceSysRef (Container scope, RefSystemRef rsr,String language)
  {
    if (rsr == null)
    {
      printError ();
      return;
    }


    if (rsr instanceof RefSystemRef.CoordSystem)
    {
      RefSystemRef.CoordSystem cs = (RefSystemRef.CoordSystem) rsr;
      ipw.print ('{');
      printRef (scope, cs.getSystem (), language);
      ipw.print ('}');
    }
    else if (rsr instanceof RefSystemRef.CoordSystemAxis)
    {
      RefSystemRef.CoordSystemAxis csa = (RefSystemRef.CoordSystemAxis) rsr;
      ipw.print ('{');
      printRef (scope, csa.getSystem (),language);
      ipw.print ('[');
      ipw.print (csa.getAxisNumber ());
      ipw.print (']');
      ipw.print ('}');
    }
    else if (rsr instanceof RefSystemRef.CoordDomain)
    {
      RefSystemRef.CoordDomain cda = (RefSystemRef.CoordDomain) rsr;
      ipw.print ('<');
      printRef (scope, cda.getReferredDomain (),language);
      ipw.print ('>');
    }
    else if (rsr instanceof RefSystemRef.CoordDomainAxis)
    {
      RefSystemRef.CoordDomainAxis cda = (RefSystemRef.CoordDomainAxis) rsr;
      ipw.print ('<');
      printRef (scope, cda.getReferredDomain (), language);
      ipw.print ('[');
      ipw.print (cda.getAxisNumber ());
      ipw.print (']');
      ipw.print ('>');
    }
    else
    {
      printError ();
    }
  }
  
  public void printType (Container scope, Type dd) {
	  printType(scope, dd, null, null,true);
  }
  public void printType (Container scope, Type dd,boolean withCardinality) {
      printType(scope, dd, null, null,withCardinality);
  }

  public void printType (Container scope, Type dd, String language, String scopedNamePrefix)
  {
      printType(scope, dd,language, scopedNamePrefix,true);
  }
  public void printType (Container scope, Type dd, String language, String scopedNamePrefix,boolean withCardinality)
  {
    if (dd == null)
    {
      printError ();
      return;
    }

    if(withCardinality) {
        Cardinality card = dd.getCardinality();
        if (card.getMaximum() == 1)
        {
          /* [MANDATORY] OBJECT */
          if (card.getMinimum() == 1)
            ipw.print("MANDATORY ");
        }
        else
        {
          /* BAG OR LIST */
          ipw.print(dd.isOrdered() ? "LIST " : "BAG ");
          ipw.print(card);
          ipw.print(' ');
          ipw.print("OF ");
        }
    }


    if (dd instanceof NumericalType)
      printNumericalType (scope, (NumericalType) dd, language,0);
    else if (dd instanceof TextType)
    {
      int len = ((TextType) dd).getMaxLength();
      if(((TextType) dd).isNormalized()){
          ipw.print("TEXT");
      }else{
          ipw.print("MTEXT");
      }


      if (len != -1) {
        ipw.print('*');
        ipw.print(len);
      }
    }
    else if (dd instanceof FormattedType)
    {
        FormattedType ft = (FormattedType) dd;
        if(ft.getDefinedBaseStruct()!=null){
        	ipw.print("FORMAT BASED ON ");
            printRef (scope, ft.getDefinedBaseStruct(), language);
            Iterator baseAttri=ft.iteratorDefinedBaseAttrRef();
            if(baseAttri.hasNext()){
            	ipw.print(" (");
            	String sep="";
            	if(ft.getExtending()!=null){
            		ipw.print("INHERITANCE");
            		sep=" ";
            	}
            	if(ft.getDefinedPrefix()!=null){
            		ipw.print("\"" + InterlisString.escapeSpecialChars(ft.getDefinedPrefix()) + "\"");
            		sep=" ";
            	}
            	while(baseAttri.hasNext()){
                	ipw.print(sep);
                	FormattedTypeBaseAttrRef baseAttr=(FormattedTypeBaseAttrRef)baseAttri.next();
                	if(baseAttr.getFormatted()!=null){
                	    if (language == null) {
                	        ipw.print(baseAttr.getAttr().getName());
                	    } else {
                	        String name = getNameInLanguage(baseAttr.getAttr(), language);
                	        if (name == null || name == "") {
                	            ipw.print(baseAttr.getAttr().getName());
                	        } else {
                	            ipw.print(name);
                	        }
                	    }
                    	
                    	ipw.print("/");
                    	printRef(scope,baseAttr.getFormatted(),language);
                	}else{
                	    if (language == null) {
                	        ipw.print(baseAttr.getAttr().getName());
                	    } else {
                	        String name = getNameInLanguage(baseAttr.getAttr(), language);
                	        if (name == null || name == "") {
                	            ipw.print(baseAttr.getAttr().getName());
                	        } else {
                	            ipw.print(name);
                	        }
                	    }
                    	
                    	if(baseAttr.getIntPos()!=0){
                        	ipw.print("/");
                    		ipw.print(baseAttr.getIntPos());
                    	}
                	}
                	if(baseAttr.getPostfix()!=null){
                		ipw.print(" \"" + InterlisString.escapeSpecialChars(baseAttr.getPostfix()) + "\"");
                	}
            		sep=" ";
            	}
            	ipw.print(")");
            }
        	if(ft.getDefinedMinimum()!=null){
            	ipw.print(" ");
                printFormatedTypeMinMax(ft);
        	}
        }else if(ft.getDefinedBaseDomain()!=null){
        	ipw.print("FORMAT ");
            printRef (scope, ft.getDefinedBaseDomain(), language);
        	ipw.print(" ");
            printFormatedTypeMinMax(ft);
        }else{
            printFormatedTypeMinMax(ft);
        }
    }
    else if (dd instanceof EnumerationType)
    {
      EnumerationType et = (EnumerationType) dd;
      printEnumeration(et.getEnumeration(), language, scopedNamePrefix);
      if (et.isCircular())
        ipw.print(" CIRCULAR");
      else if (et.isOrdered())
        ipw.print(" ORDERED");
    }
    else if (dd instanceof EnumTreeValueType)
    {
      EnumTreeValueType et = (EnumTreeValueType) dd;
      ipw.print("ALL OF ");
      printRef (scope, et.getEnumType(), language);
    }  
    else if (dd instanceof EnumValType)
    {
        if(((EnumValType) dd).isOnlyLeafs()){
            ipw.print("ENUMVAL");
        }else{
            ipw.print("ENUMTREEVAL");
        }
    }  
    else if (dd instanceof TypeAlias){
      Domain def=((TypeAlias) dd).getAliasing();
      if(def==modelInterlis.BOOLEAN){
        ipw.print("BOOLEAN");
      }else if(def==modelInterlis.VALIGNMENT){
        ipw.print("VALIGNMENT");
      }else if(def==modelInterlis.HALIGNMENT){
        ipw.print("HALIGNMENT");
      }else{
        printRef (scope, ((TypeAlias) dd).getAliasing(),language);
      }
    }else if (dd instanceof CompositionType)
    {
      CompositionType comp = (CompositionType) dd;
      printRef (scope, comp.getComponentType(), language);
    }
    else if (dd instanceof ReferenceType)
    {
			ReferenceType ref = (ReferenceType) dd;
			ipw.print("REFERENCE TO ");
			if (ref.isExternal()) {
				ipw.print("(EXTERNAL) ");
			}
			printRef(scope, ref.getReferred(), language);
			Iterator resti = ref.iteratorRestrictedTo();
			String sep = " RESTRICTION (";
			boolean hasRestriction = false;
			while (resti.hasNext()) {
				AbstractClassDef rest = (AbstractClassDef) resti.next();
				ipw.print(sep);
				sep = ";";
				printRef(scope, rest, language);
				hasRestriction = true;
			}
			if (hasRestriction) {
				ipw.print(")");
			}
    }
    else if (dd instanceof AbstractCoordType)
    {
      NumericalType[] nts = ((AbstractCoordType) dd).getDimensions();
      int nullAxis = ((AbstractCoordType) dd).getNullAxis();
      int piHalfAxis = ((AbstractCoordType) dd).getPiHalfAxis();

      if(dd instanceof MultiCoordType){
          ipw.print ("MULTICOORD ");
      }else{
          ipw.print ("COORD ");
      }
      ipw.indent ();
      for (int i = 0; i < nts.length; i++)
      {
        if (i > 0)
          ipw.print (", ");
        printNumericalType (scope, nts[i], language, i);
      }
      if (nullAxis != 0)
      {
        ipw.println (',');
        ipw.print ("ROTATION ");
        ipw.print (nullAxis);
        ipw.print (" -> ");
        if (piHalfAxis > 0)
          ipw.print (piHalfAxis);
        else
          printError ();
      }
      ipw.unindent ();
    }
    else if (dd instanceof LineType)
    {
      LineType lt = (LineType) dd;
      if (lt instanceof PolylineType){
        ipw.print (((PolylineType) lt).isDirected() ? "DIRECTED POLYLINE" : "POLYLINE");
      }else if (lt instanceof MultiPolylineType){
          ipw.print (((MultiPolylineType) lt).isDirected() ? "DIRECTED MULTIPOLYLINE" : "MULTIPOLYLINE");
      }else if (lt instanceof SurfaceType){
        ipw.print ("SURFACE");
      }else if (lt instanceof MultiSurfaceType){
        ipw.print ("MULTISURFACE");
      }else if (lt instanceof AreaType){
        ipw.print ("AREA");
      }else if (lt instanceof MultiAreaType){
          ipw.print ("MULTIAREA");
      }else{
        printError ();
      }


      LineForm[] lineForms = lt.getDefinedLineForms ();
      PrecisionDecimal maxOverlap = lt.getDefinedMaxOverlap ();
      Domain controlPointDomain = lt.getDefinedControlPointDomain ();
      Table lineAttributeStructure = null;


      if (lt instanceof SurfaceOrAreaType){
        lineAttributeStructure = ((SurfaceOrAreaType) lt).getLineAttributeStructure ();
      }else if(lt instanceof MultiSurfaceOrAreaType){
          lineAttributeStructure = ((MultiSurfaceOrAreaType) lt).getLineAttributeStructure ();
      }

      ipw.indent ();
      boolean needNewLine = false;


      if (lineForms.length > 0)
      {
        if (needNewLine)
          ipw.println ();


        ipw.print (" WITH (");
        for (int i = 0; i < lineForms.length; i++)
        {
          if (i > 0)
            ipw.print (", ");
          if (language == null) {
              ipw.print (lineForms[i].getName());   
          } else {
              String name = getNameInLanguage(lineForms[i], language);
              if (name == "" || name == null) {
                  ipw.print (lineForms[i].getName());
              } else {
                  ipw.print (name);
              }
          }
        }
        ipw.print (')');
        needNewLine = true;
      }


      if (controlPointDomain != null)
      {
        ipw.print (" VERTEX ");
        printRef (scope, controlPointDomain,language);
        needNewLine = true;
      }


      if (maxOverlap != null)
      {
        if (needNewLine)
          ipw.println ();


        ipw.print (" WITHOUT OVERLAPS > ");
        ipw.print (maxOverlap.toString());
      }



      if (lineAttributeStructure != null)
      {
        ipw.println ();
        ipw.print ("LINE ATTRIBUTES ");
        printRef (scope, lineAttributeStructure,language);
      }


      ipw.unindent ();
    }else if(dd instanceof OIDType){
      Type type=((OIDType)dd).getOIDType();
      if(type==null){
        ipw.print ("OID ANY");
      }else{
        ipw.print ("OID ");
        scopedNamePrefix = dd.getScopedName();
        printType(scope, type, language, scopedNamePrefix);
      }
	}else if(dd instanceof BlackboxType){
	  BlackboxType bt=(BlackboxType)dd;
	  ipw.print ("BLACKBOX");
	  int kind=bt.getKind();
	  if(kind==BlackboxType.eXML){
		ipw.print (" XML");
	  }else if(kind==BlackboxType.eBINARY){
		ipw.print (" BINARY");
	  }
    }else if(dd instanceof BasketType){
      BasketType bt=(BasketType)dd;
      ipw.print ("BASKET");
      int kind=bt.getKind();
      if(kind==Properties.eDATA){
        ipw.print (" (DATA)");
      }else if(kind==Properties.eVIEW){
        ipw.print (" (VIEW)");
      }else if(kind==Properties.eBASE){
        ipw.print (" (BASE)");
      }else if(kind==Properties.eGRAPHIC){
        ipw.print (" (GRAPHIC)");
      }
      Topic spec=bt.getTopic();
      if(spec!=null){
        ipw.print (" OF ");
        printRef(scope,spec,language);
      }
    }else if(dd instanceof ClassType){
      ClassType ct=(ClassType)dd;
      if(ct.isStructure()){
        ipw.print ("STRUCTURE");
      }else{
        ipw.print ("CLASS");
      }
      String next=" RESTRICTED TO ";
      Iterator resti=ct.iteratorRestrictedTo();
      while(resti.hasNext()){
        Table rest=(Table)resti.next();
        ipw.print(next);
        printRef(scope,rest,language);
        next=" ,";
      }
    }else if(dd instanceof AttributePathType){
    	AttributePathType ct=(AttributePathType)dd;
        ipw.print ("ATTRIBUTE");
        FormalArgument argRestr=ct.getArgRestriction();
        ObjectPath attrRestr=ct.getAttrRestriction();
        if(argRestr!=null){
            ipw.print (" OF @ ");
            if (language == null) {
                ipw.print(argRestr.getName());
            } else {
                String name = getNameInLanguage(argRestr, language);
                if (name == null || name == "") {
                    ipw.print(argRestr.getName());
                } else {
                    ipw.print(name);
                }
            }
        	
        }else if(attrRestr!=null){
            ipw.print (" OF ");
        	printAttributePath(scope,attrRestr,language);
        }
        Type[] typeRestr=ct.getTypeRestriction();
        if(typeRestr!=null){
            ipw.print (" RESTRICTION ( ");
        	String sep="";
        	for(int typei=0;typei<typeRestr.length;typei++){
                ipw.print (sep);
        		printType(scope,typeRestr[typei], language, scopedNamePrefix);
        		sep=";";
        	}
            ipw.print (" )");
        }
    }else if(dd instanceof ObjectType){
      ObjectType ot=(ObjectType)dd;
      if(ot.isObjects()){
          ipw.print ("OBJECTS OF ");
      }else{
          ipw.print ("OBJECT OF ");
      }
      Viewable ref=ot.getRef();
      printRef(scope,ref,language);
    }else if(dd instanceof MetaobjectType){
      MetaobjectType ot=(MetaobjectType)dd;
      ipw.print ("METAOBJECT");
      Table ref=ot.getReferred();
      if(ref!=scope){
        ipw.print (" OF ");
        printRef(scope,ref,language);
      }
    }
  }
private void printFormatedTypeMinMax(FormattedType ft) {
	ipw.print("\"");
	ipw.print(InterlisString.escapeSpecialChars(ft.getDefinedMinimum()));
	ipw.print("\" .. \"");
	ipw.print(InterlisString.escapeSpecialChars(ft.getDefinedMaximum()));
	ipw.print("\"");
}

  /** Prints a numerical type (either a NumericType or a StructuredUnitType).
  */
  protected void printNumericalType (Container scope, NumericalType type, String language, int i)
  {
    if (type == null)
    {
      printError ();
      return;
    }


    if (type instanceof NumericType)
    {
      NumericType ntyp = (NumericType) type;
      PrecisionDecimal min = ntyp.getMinimum();
      PrecisionDecimal max = ntyp.getMaximum();
      if (min == null)
        ipw.print("NUMERIC");
      else
      {
    	if (params != null) {
        	if ((i % 2) == 0) {
        		
    			int value = (int) ((int) (params.getFactor_x() * min.doubleValue()) + params.getDiff_x());
    			String newValue = String.valueOf(value);
    			for (int j = 0; j < min.getAccuracy(); j++) {
    				if (j == 0) {
    					newValue = newValue + ".0"; 
    				} else {
    					newValue += "0";
    				}
    			}
    			ipw.print(newValue);
    		
    		ipw.print(" .. ");

    			value = (int) ((int) (params.getFactor_x() * max.doubleValue()) + params.getDiff_x());
    			newValue = String.valueOf(value);
    			for (int j = 0; j < min.getAccuracy(); j++) {
    				if (j == 0) {
    					newValue = newValue + ".0"; 
    				} else {
    					newValue += "0";
    				}
    			}
    			ipw.print(newValue);

    	} else {
    			int value = (int) ((int) (params.getFactor_y() * min.doubleValue()) + params.getDiff_y());
    			String newValue = String.valueOf(value);
    			for (int j = 0; j < min.getAccuracy(); j++) {
    				if (j == 0) {
    					newValue = newValue + ".0"; 
    				} else {
    					newValue += "0";
    				}
    			}
    			ipw.print(newValue);

    		ipw.print(" .. ");

    			value = (int) ((int) (params.getFactor_y() * max.doubleValue()) + params.getDiff_y());
    			newValue = String.valueOf(value);
    			for (int j = 0; j < min.getAccuracy(); j++) {
    				if (j == 0) {
    					newValue = newValue + ".0"; 
    				} else {
    					newValue += "0";
    				}
    			}
    			ipw.print(newValue);
    	}
    	} else {
            ipw.print(min.toString());
            ipw.print(" .. ");
            ipw.print(max.toString());    		
    	}

      }
    }
    else if (type instanceof StructuredUnitType)
    {
      ipw.print (((StructuredUnitType) type).getMinimum().toString ());
      ipw.print (" .. ");
      ipw.print (((StructuredUnitType) type).getMaximum().toString ());
    }


    if (type.isCircular())
        ipw.print(" CIRCULAR");

    Unit unit=getTypeUnit(type);
    if (unit != null)
    {
      ipw.print (" [");
      ipw.print (unit.getScopedName(scope));
      ipw.print (']');
    }


    switch (type.getRotation())
    {
    case NumericType.ROTATION_COUNTERCLOCKWISE:
      ipw.print(" COUNTERCLOCKWISE");
      break;


    case NumericType.ROTATION_CLOCKWISE:
      ipw.print(" CLOCKWISE");
      break;
    }


    final RefSystemRef referenceSystem = getTypeReferenceSystem(type);
    if (referenceSystem != null)
    {
      ipw.print (' ');
      printReferenceSysRef (scope, referenceSystem, language);
    }
  }
protected RefSystemRef getTypeReferenceSystem(NumericalType type) {
    return type.getReferenceSystem();
}
protected Unit getTypeUnit(NumericalType type) {
    return type.getUnit();
}



  protected void printEnumeration (ch.interlis.ili2c.metamodel.Enumeration enumer,String language,String scopedNamePrefix )
  {
    ipw.println ('(');
    ipw.indent ();


    if (enumer == null)
      printError ();
    else
    {
      Iterator iter = enumer.getElements();
      while (iter.hasNext()) {
        printEnumerationElement((ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next(),language,scopedNamePrefix);
        if (iter.hasNext())
          ipw.println (',');
      }
    }


    ipw.println ("");
    ipw.unindent ();
    ipw.print (')');
  }



  protected void printEnumerationElement (ch.interlis.ili2c.metamodel.Enumeration.Element ee, String language,
		  String scopedNamePrefix)
  {
		String scopedName = scopedNamePrefix + "." + ee.getName();
		if (language == null) {
			printDocumentation(ee.getDocumentation());
			printMetaValues(ee,ee.getMetaValues(), language, scopedName);
			ipw.print(ee.getName());
		} else {
			String docu = getElementDocumentationInLanguage(scopedName, language);
			if (docu == null || docu == "") {
			    printDocumentation(ee.getDocumentation());
			} else {
			    printDocumentation(docu);
			}
			printMetaValues(ee,ee.getMetaValues(), language, scopedName);
			String name = getElementNameInLanguage(scopedName, language);
			if (name == null || name == "") {
			    ipw.print(ee.getName());
			} else {
			    ipw.print(name);
			}
		}

		ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
		if (subEnum != null) {
			ipw.print(' ');
			printEnumeration(subEnum, language, scopedName);
		}
  }

	private String getElementNameInLanguage(String scopedNamePrefix, String language) {
		String modelName = "";
		Iterator<TranslationElement> iteratorModelElement = translationConfig.iterator();

		while (iteratorModelElement.hasNext()) {
			TranslationElement element = iteratorModelElement.next();
			if (scopedNamePrefix.equals(element.getScopedName())) {
				if (language.equals(FR)) {
					modelName = element.getName_fr();
					return modelName;
				} else if (language.equals(EN)) {
					modelName = element.getName_en();
					return modelName;
				} else if (language.equals(IT)) {
					modelName = element.getName_it();
					return modelName;
				} else if (language.equals(DE)) {
					modelName = element.getName_de();
					return modelName;
				}
			}
		}
		return "";
	}
  
	private String getElementDocumentationInLanguage(String scopedNamePrefix, String language) {
		String modelName = "";
		Iterator<TranslationElement> iteratorModelElement = translationConfig.iterator();

		while (iteratorModelElement.hasNext()) {
			TranslationElement element = iteratorModelElement.next();
			if (scopedNamePrefix.equals(element.getScopedName())) {
				if (language.equals(FR)) {
					modelName = element.getDocumentation_fr();
					return modelName;
				} else if (language.equals(EN)) {
					modelName = element.getDocumentation_en();
					return modelName;
				} else if (language.equals(IT)) {
					modelName = element.getDocumentation_it();
					return modelName;
				} else if (language.equals(DE)) {
					modelName = element.getDocumentation_de();
					return modelName;
				}
			}
		}
		return "";
	}
  
	public void printLineFormTypeDef (Container scope, LineForm lf) {
		printLineFormTypeDef(scope, lf, null);
	}
  
  public void printLineFormTypeDef (Container scope, LineForm lf, String language)
  {
    if (lf == null)
    {
      printError ();
      return;
    }

    printDocumentation(lf,language);
	printMetaValues(lf,lf.getMetaValues(), language, lf.getScopedName());
	if (language == null) {
	    ipw.print (lf.getName ());
	} else {
	    String name = getNameInLanguage(lf,language);
	    if (name == null || name == "") {
	        ipw.print (lf.getName ());
	    } else {
	        ipw.print (name);
	    }
	}
    
    
    String explanation = lf.getExplanation();
    if (explanation != null)
    {
      ipw.print (' ');
      printExplanation(explanation);
    }
    
    Table segmentStructure = lf.getSegmentStructure();
    if (!segmentStructure.isEmpty()) {
        ipw.print (" : ");
        if (language == null) {
            ipw.print (segmentStructure.getName());
        } else {
            String name = getNameInLanguage(segmentStructure, language);
            if (name == null || name == "") {
                ipw.print (segmentStructure.getName());
            } else {
                ipw.print (name);
            }
        }
    }

    ipw.println (';');
  }

  public void printFunctionDeclaration(Container scope, Function f) {
	  printFunctionDeclaration(scope, f, null);
  }

  public void printFunctionDeclaration(Container scope, Function f, String language)
  {
	  printFunctionDeclaration(scope,f,false,language);
  }
  public void printFunctionDeclaration(Container scope, Function f,boolean suppressDoc) {
	  printFunctionDeclaration(scope, f,suppressDoc, null);
  }
  public void printFunctionDeclaration(Container scope, Function f,boolean suppressDoc, String language)
  {
    if (f == null) {
      printError ();
      return;
    }

    if(!suppressDoc){
        printDocumentation(f,language);
        printMetaValues(f,f.getMetaValues(), language, f.getScopedName()); 
    }
    
    ipw.print("FUNCTION ");
    printName(f,language);
    ipw.print("(");


    FormalArgument[] args = f.getArguments ();
    if (args == null)
      printError ();
    else
    {
    	String sep="";
      for (int i = 0; i < args.length; i++)
      {
          FormalArgument arg=args[i];
          
          String name = arg.getName();
          if(language!=null) {
              String scopedName=f.getScopedName()+"."+arg.getName();
              name=getNameInLanguage(scopedName, language);
              if(name==null || name.equals("")) {
                  name=arg.getName();
              }
          }
          
          ipw.print(sep+name+": ");
        printType (scope, args[i].getType(), language, scope.getScopedName());
        sep="; ";
      }
    }


    ipw.print(") : ");
    printType (scope, f.getDomain(), language, scope.getScopedName());


    String explanation = f.getExplanation();
    if (explanation != null)
      printExplanation (explanation);


    ipw.println(';');
  }



  protected void printAttributeRefs (AttributeRef[] refs)
  {
    if (refs == null)
    {
      printError ();
      return;
    }


    for (int i = 0; i < refs.length; i++)
    {
      if (i > 0)
        ipw.print ('.');


      if (refs[i] == null)
        printError ();
      else
        ipw.print (refs[i].getName ());
    }
  }



  protected void printAttributePath (Container scope, ObjectPath path, String language)
  {
    if (path == null)
    {
      printError ();
      return;
    }
    PathEl[] elv=path.getPathElements();
    String sep="";
    for(int i=0;i<elv.length;i++){
	ipw.print(sep);sep="->";
	if (language == null) {
		ipw.print(elv[i].getName());
	} else {
		if (elv[i] instanceof AttributeRef) {
			AttributeRef attr = (AttributeRef) elv[i];
			if (attr.getAttr() instanceof LocalAttribute) {
			    String name = getElementNameInLanguage(attr.getAttr().getScopedName(), language);
			    if (name == null || name == "") {
			        ipw.print(elv[i].getName());
			    } else {
			        ipw.print(name);
			    }
			}
        } else if (elv[i] instanceof PathElBase) {
            PathElBase pathEl = (PathElBase) elv[i];
            String scopedEleName=pathEl.getCurrentViewable().getScopedName()+"."+pathEl.getName();
            String name = getElementNameInLanguage(scopedEleName, language);
            if (name == null || name == "") {
                ipw.print(elv[i].getName());
            } else {
                ipw.print(name);
            }
        } else if (elv[i] instanceof PathElAssocRole) {
            PathElAssocRole assocRole = (PathElAssocRole) elv[i];
            if (assocRole.getRole() instanceof RoleDef) {
                String name = getElementNameInLanguage(assocRole.getRole().getScopedName(), language);
                if (name == null || name == "") {
                    ipw.print(elv[i].getName());
                } else {
                    ipw.print(name);
                }
                
            }
        } else if (elv[i] instanceof PathElAbstractClassRole) {
            PathElAbstractClassRole assocRole = (PathElAbstractClassRole) elv[i];
            if (assocRole.getRole() instanceof RoleDef) {
                String name = getElementNameInLanguage(assocRole.getRole().getScopedName(), language);
                if (name == null || name == "") {
                    ipw.print(elv[i].getName());
                } else {
                    ipw.print(name);
                }
                
            }
        } else if (elv[i] instanceof AssociationPath) {
            AssociationPath assocRole = (AssociationPath) elv[i];
            if (assocRole.getTargetRole() instanceof RoleDef) {
                String name = getElementNameInLanguage(assocRole.getTargetRole().getScopedName(), language);
                if (name == null || name == "") {
                    ipw.print("\\\\"+elv[i].getName());
                } else {
                    ipw.print("\\\\"+name);
                }
                
            }
        } else if (elv[i] instanceof PathElThis) {
            ipw.print("THIS");
		}
	}
	
    }
  }
  
  protected void printElements (Container container,String language)
  {
    Class lastClass = null;
    if(onlyLastFile && container instanceof TransferDescription) {
        for(Model model:((TransferDescription)container).getModelsFromLastFile()) {
            lastClass = printElement(container, lastClass, model,language);
        }
    }else {
        Iterator it = container.iterator();
        while (it.hasNext()) {
          ch.interlis.ili2c.metamodel.Element elt = (ch.interlis.ili2c.metamodel.Element) it.next();
          if(container instanceof Topic) {
              if(selfStandingConstraints.size()>0) {
                  List<Constraint> flushConstraints=new ArrayList<Constraint>();
                  for(Constraint cs:selfStandingConstraints) {
                      if(elt.getSourceLine()>cs.getSourceLine()) {
                          flushConstraints.add(cs);
                      }
                  }
                  if(flushConstraints.size()>0) {
                      selfStandingConstraints.removeAll(flushConstraints);
                      printSelfStandingConstraints(flushConstraints,language);
                  }
              }
          }
          lastClass = printElement(container, lastClass, elt,language);
        }
        
    }
  }
protected Class printElement(Container container, Class lastClass, ch.interlis.ili2c.metamodel.Element elt,
		String language) {
	if (elt instanceof AttributeDef)
      {
        printAttribute (container, (AttributeDef) elt, language);
        lastClass = AttributeDef.class;
      }
	  else if (elt instanceof RoleDef)
	  {
		printRoleDef(container, (RoleDef) elt, language);
		lastClass = RoleDef.class;
	  }
      else if (elt instanceof Function)
      {
        if (lastClass!=null && lastClass!=Function.class) {
          ipw.println();
        }
        printFunctionDeclaration (container, (Function) elt, language);
        lastClass = Function.class;
      }
      else if (elt instanceof Parameter)
      {
        if (lastClass!=Parameter.class)
        {
          ipw.println ("PARAMETER");
        }
        printParameter (container, (Parameter) elt, language);
        lastClass = Parameter.class;
      }
      else if (elt instanceof ContextDef)
      {
        if (lastClass!=ContextDef.class)
        {
          ipw.println();
          ipw.println ("CONTEXT");
        }
        ipw.indent();
        printContext(container, (ContextDef) elt);
        ipw.unindent();
        lastClass = ContextDef.class;
      }
      else if (elt instanceof Domain)
      {
        if (lastClass != Domain.class)
        {
          ipw.println();
          ipw.println("DOMAIN");
        }
        ipw.indent();
        printDomainDef (container, (Domain) elt, language);
        ipw.unindent();
        lastClass = Domain.class;
      }
      else if (elt instanceof LineForm)
      {
        if (lastClass!=LineForm.class)
        {
          ipw.println();
          ipw.println ("LINE FORM");
        }
        ipw.indent ();
        printLineFormTypeDef (container, (LineForm) elt, language);
        ipw.unindent ();
        lastClass = LineForm.class;
      }
      else if (elt instanceof Unit)
      {
        if (lastClass!=Unit.class) {
          ipw.println();
          ipw.println("UNIT");
        }
        ipw.indent();
        printUnit(container, (Unit) elt, language);
        ipw.unindent();
        lastClass = Unit.class;
      }
      else if (elt instanceof Model)
      {
        if (withPredefined || !(elt instanceof PredefinedModel))
        {
          ipw.println();
          printModel((Model) elt, language);
          lastClass = Model.class;
        }
      }
      else if (elt instanceof Topic)
      {
        ipw.println ();
        printTopic((Topic) elt, language);
        lastClass = Topic.class;
      }
      else if (elt instanceof MetaDataUseDef)
      {
        ipw.println ();
        printMetaDataUseDef((MetaDataUseDef) elt, language);
        lastClass = MetaDataUseDef.class;
      }
      else if (elt instanceof Table)
      {
        /* Only explicit tables are printed out.
           Line attribute tables, for example,
           are not printed out.
        */
        if (!((Table) elt).isImplicit ())
        {
          ipw.println ();
          printAbstractClassDef((Table) elt, language);
          lastClass = AbstractClassDef.class;
        }
      }
	  else if (elt instanceof AssociationDef)
	  {
		  ipw.println ();
		  printAbstractClassDef((AssociationDef) elt, language);
		  lastClass = AbstractClassDef.class;
	  }
      else if (elt instanceof View)
      {
        ipw.println();
        printView((View) elt, language);
        lastClass = View.class;
      }
      else if (elt instanceof Graphic)
      {
        ipw.println();
        printGraphic ((Graphic) elt, language);
        lastClass = Graphic.class;
      }
      else if (elt instanceof Constraint)
      {
		if(((Constraint)elt).isSelfStanding()){
			selfStandingConstraints.add((Constraint)elt);
		}else{
			printConstraint((Constraint)elt, language);
			lastClass = Constraint.class;
		}

      }
      else if (elt instanceof SignAttribute)
      {
        if (lastClass != SignAttribute.class)
        {
          if (lastClass != null)
            ipw.println();
        }
        printSignAttribute ((Graphic) container, (SignAttribute) elt, language);
        lastClass = SignAttribute.class;
      }
	return lastClass;
}

  protected void printTransferDescription (
    TransferDescription   td, String language)
  {
    if(doIli24) {
        ipw.println("INTERLIS 2.4;");
    }else {
        ipw.println("INTERLIS 2.3;");
    }
    printElements(td, language);
  }
}
