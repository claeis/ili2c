/*****************************************************************************
 *
 * Model.java
 * ----------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;


/** An abstract class that groups together all the different
    kinds of models.
*/
public abstract class Model extends Importable
{
  protected List     contents = new LinkedList ();
  protected List     runtimeParameters = new LinkedList ();
  protected List     qualifiedImports = new LinkedList(); // List<Model>
  protected List     unqualifiedImports = new LinkedList(); // List<Model>
  private List contracts=new LinkedList(); // List<Contract>
  private String language=null;
  private boolean contracted=false;
  private String issuer=null;
  private String modelVersion=null;
  private String modelVersionExpl=null;

  protected class ElementDelegate extends AbstractCollection
  {
    public Iterator iterator ()
    {
      return new CombiningIterator ( new Iterator[]
                                     {
                                       runtimeParameters.iterator (),
                                       contents.iterator (),
                                     });
    }



    public int size()
    {
      return runtimeParameters.size () + contents.size () ;
    }



    public boolean add (Object o)
    {
		if(!checkChildElement(o)){
			return false;
		}
        if (o instanceof GraphicParameterDef)
        {
          return runtimeParameters.add (o);
        }
		return contents.add(o);


    }

	public boolean checkChildElement (Object o)
	{
		/* In all sorts of model: UNIT, DOMAIN, FUNCTION, LINE FORM, STRUCTURE,
		   abstract TABLE */
		Element e=(Element)o;
		if (o instanceof MetaDataUseDef)
	{
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_nonuniqueMetaDataUseDefName",
			  e.getName(),
			  Model.this.toString()));
		  return true;
	}


	if (o instanceof Unit)
		{
		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_duplicateUnitName",
			  e.getName(),
			  Model.this.toString()));


		  return true;
		}


		if (o instanceof Function)
		{


		  if (!isContracted() && !(Model.this instanceof PredefinedModel))
			throw new IllegalArgumentException (formatMessage (
			  "err_model_functionButNoContract",
			  e.toString(),
			  Model.this.toString()));


		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_function_duplicateName",
			  e.getName(),
			  Model.this.toString(),
			  conflicting.toString()));


		  return true;
		}



		if (o instanceof LineForm)
		{


		  if (!isContracted() && !(Model.this instanceof PredefinedModel))
			throw new IllegalArgumentException (formatMessage (
			  "err_lineForm_inUnconctractedModel",
			  Model.this.toString()));


		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_lineForm_duplicateName",
			  e.getName(),
			  Model.this.toString(),
			  conflicting.toString()));


		  return true;
		}


		if (o instanceof Domain)
		{
		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_duplicateDomainName",
			  e.getName(),
			  Model.this.toString()));


		  return true;
		}



		if (o instanceof GraphicParameterDef)
		{
		  Element conflicting = getRuntimeParameter ( e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_graphicparam_nonunique",
			  e.getName(),
			  Model.this.toString()));


		  return true;
		}


		if (o instanceof Topic)
		{
		  if (Model.this instanceof TypeModel)
			throw new IllegalArgumentException (formatMessage (
			  "err_typeModel_addTopic",
			  o.toString (),
			  Model.this.toString ()));
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_topic_nonunique",
			  e.getName(),
			  Model.this.toString()));


		  return true;
		}


	if(o instanceof Table){
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e))
			throw new IllegalArgumentException (formatMessage (
			  "err_table_nonunique",
			  e.getName(),
			  Model.this.toString()));
		return true;
	}


		throw new ClassCastException (formatMessage (
		  "err_container_cannotContain",
		  Model.this.toString(), o.toString()));
	}

  };

  public void addPreLast(Object o)
  {
	  if(!((ElementDelegate)elements).checkChildElement(o)){
		  return;
	  }
	  if (o instanceof GraphicParameterDef)
	  {
		runtimeParameters.add (o);
	  }
	  contents.add(contents.size()-1,o);


  }


  /** Constructs a new model.


      @exception java.lang.IllegalArgumentException if the
                 name parameter is <code>null</code> or an empty string.
   */
  protected Model ()
  {
    this.name = "";
  }
  protected Collection createElements(){
    return new ElementDelegate();
  }

  public Element getImportedElement (Class aclass, String name)
  {
	  Element e=getElement(aclass,name);
	  if(e!=null){
		  return e;
	  }
	  Iterator it=unqualifiedImports.iterator();
	  while(it.hasNext()){
		  Model i=(Model)it.next();
		  e=i.getElement(aclass,name);
		  if(e!=null){
			  return e;
		  }
	  }
	  return null;
  }

  /** find an element with the given name in the type namespace
   */
  private Element getElement(String name){
	    if (name == null)
	      return null;


	    Iterator it = contents.iterator();
	    while (it.hasNext ())
	    {
	      Element e = (Element) it.next ();
	      if (name.equals (e.getName()))
	        return e;
	    }
      return null;
  }
  /** find an runtime parameter with the given name
   */
  private Element getRuntimeParameter(String name){
	    if (name == null)
	      return null;


	    Iterator it = runtimeParameters.iterator();
	    while (it.hasNext ())
	    {
	      Element e = (Element) it.next ();
	      if (name.equals (e.getName()))
	        return e;
	    }
      return null;
  }


  /* The semantic rules of Interlis ensure that a model <i>M</i>
     is dependent on a model <i>I</i> if and only if <i>M</i>
     imports <i>I</i>.
  */
  public boolean isDependentOn (Element e)
  {
    if (e instanceof Model)
      return isImporting((Model)e);


    return false;
  }



  /** Returns the name of this model.
  */
  public String getName ()
  {
    return name;
  }



  /* FIXME: Should check for name uniqueness. */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;


    checkNameSanity(newValue, /* empty ok? */ false);


    fireVetoableChange("name", oldValue, newValue);


    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }



  /** Returns whether or not this Model is subject to a contract.
      @return <code>false</code> iff. the contract issuer's name
              is empty.
  */
  public boolean isContracted ()
  {
    return contracted || !contracts.isEmpty();
  }
  public void setContracted(boolean contracted1)
  {
	contracted=contracted1;
  }
public void setIssuer(String issuer1)
{
	issuer=issuer1;
}
public String getIssuer()
{
	return issuer;
}
public void setModelVersion(String modelVersion1)
{
	modelVersion=modelVersion1;
}
public String getModelVersion()
{
	return modelVersion;
}
public void setModelVersionExpl(String modelVersionExpl1)
{
	modelVersionExpl=modelVersionExpl1;
}
public String getModelVersionExpl()
{
	return modelVersionExpl;
}


  public void addContract(Contract contract)
  {
	  contracts.add(contract);
  }


  public Contract[] getContracts()
  {
	  return (Contract[])contracts.toArray(new Contract[0]);
  }



  /** Returns the Importables that this model imports.
  */
  public Model[] getImporting ()
  {
	  LinkedList importing=new LinkedList();
	  importing.addAll(qualifiedImports);
	  importing.addAll(unqualifiedImports);
    return (Model[]) importing.toArray(new Model[0]);
  }


  /** Let this model import another importable. */
  public void addImport (Model importing,boolean unqualified)
  {
	if(unqualified){
		unqualifiedImports.add (importing);
	}else{
		qualifiedImports.add (importing);
	}
    importing.importedBy.add (importing);
  }



  /** Returns whether or not this model imports a specified
      Importable.
  */
  public boolean isImporting (Model importing)
  {
      return qualifiedImports.contains(importing) || unqualifiedImports.contains(importing);
  }
  /** Returns whether or not this model imports a specified
      Importable.
  */
  public boolean isImporting (Model importing, boolean qualifiedImport)
  {
    if(qualifiedImport){
      return qualifiedImports.contains(importing);
    }
    return unqualifiedImports.contains(importing);
  }


  public void setLanguage(String v)
  {
	  language=v;
  }
  /** may return null if no language set
   */
  public String getLanguage(){
  	return language;
  }

  private String filename=null;
  public void setFileName(String filename)
  {
    this.filename=filename;
  }
  public String getFileName()
  {
    return filename;
  }
}
