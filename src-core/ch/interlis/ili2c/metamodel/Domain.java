/*****************************************************************************
 *
 * Domain.java
 * -----------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.Set;
import java.beans.PropertyVetoException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;


/** A domain declaration, as expressed by the DOMAIN construct
    in INTERLIS-2.

    @author <a href="sb@adasys.ch">Sascha Brawer</a>, Adasys AG, CH-8006 Zurich
*/
public class Domain extends AbstractLeafElement
{
  protected String name = null;
  protected Type type = null;
  protected boolean    _abstract = false;
  protected boolean    _final = false;
  protected boolean    _mandatory = false;

  protected Domain extending = null;
  protected Set<Domain> extendedBy = new HashSet<Domain>(2);
  protected Set<Type> aliasedBy = new HashSet<Type>(2);


  public Domain ()
  {
  }


  public Domain (String name, Type type, Domain extending, boolean _abstract, boolean _final)
  {
    checkNameSanity(name, /* empty names acceptable? */ false);
    this.name = name;

    try
    {
      setAbstract (_abstract);
      setType (type);
      setFinal (_final);
      setExtending (extending);
    }
    catch (java.beans.PropertyVetoException ex)
    {
      /* This can not happen because nobody has has the chance
         to register. */
      throw new IllegalStateException (ex.toString());
    }
  }


  /** Determines the current value of the <code>name</code> property.
      Domains are identified and used by specifying their name.

      @see #setName(java.lang.String)
  */
  public String getName()
  {
    return name;
  }



  /** Sets the value of the <code>name</code> property.
      Domains are identified and used by specifying their name.


      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this Domain.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.


      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another function.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    checkNameSanity(name, /* empty names acceptable? */ false);
    checkNameUniqueness(name, Domain.class, null,
      "err_duplicateFunctionName");


    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }

  public String toString()
  {
    return "DOMAIN " + getScopedName (null);
  }





  /** Determines the value of the <code>type</code> property.
      Each Domain specifies a type that indicates
      the admissible values.
  */
  public Type getType ()
  {
    return type;
  }



  /** Sets the value of the <code>type</code> property.
      Each Domain specifies a type that indicates
      the admissible values.


      <p>Since the <code>type</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.

      @param type    The new type.


      @exception java.lang.IllegalArgumentException if <code>type</code>
                 is <code>null</code>.


      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>explanation</code> property
                 and does not agree with the change.
  */
  public void setType (Type type)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.type;
    Type newValue = type;


    if (newValue == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

    fireVetoableChange("type", oldValue, newValue);
    this.type = newValue;
    firePropertyChange("type", oldValue, newValue);
    
    StringBuilder err=new StringBuilder();
    if ((type != null) && type.isAbstract(err) && !this.isAbstract())
        throw new Ili2cSemanticException (formatMessage ("err_domainMustBeAbstractDueToType",
          this.toString(),err.toString()));
  }



  /** Returns whether this domain is abstract or can have
      instances.

      @return <code>true</code> if the domain is abstract,
              <code>false</code> if instances are acceptable.

      @see #setAbstract(boolean)
  */
  public boolean isAbstract()
  {
    return _abstract;
  }



  /** Sets the value of the <code>abstract</code> property;
      an abstract domain can not be instantiated.


      <p>In JavaBeans terminology, the <code>abstract</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param abst Pass <code>true</code> to make the domain
                  abstract, pass <code>false</code> to allow
                  for concrete instances.


      @exception java.lang.IllegalArgumentException if this
                 domain is final and <code>abst</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setAbstract(boolean abs)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _abstract;
    boolean newValue = abs;

    if (oldValue == newValue)
      return;

    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isFinal())
      throw new IllegalArgumentException(
          rsrc.getString("err_abstractFinal"));


    firePropertyChange("abstract", oldValue, newValue);
    _abstract = newValue;
    fireVetoableChange("abstract", oldValue, newValue);
  }



  /** Returns whether this Domain is final or can be extended.

      @return <code>true</code> if the Type is final,
              <code>true</code> if extensions are possible.
  */
  public boolean isFinal()
  {
    return _final;
  }


  /** Sets the value of the <code>final</code> property. A final
      type can not be extended anymore.


      <p>Since the <code>final</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.

      @param fin Whether or not this Type is going to be final.

      @exception java.lang.IllegalArgumentException if there still
                 exist Types that extend this type.


      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>final</code> property
                 and does not agree with the change.
  */
  public void setFinal(boolean fin)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _final;
    boolean newValue = fin;

    if (oldValue == newValue)
      return;


    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isAbstract())
      throw new IllegalArgumentException(
          rsrc.getString("err_abstractFinal"));


    if (fin && !extendedBy.isEmpty())
      throw new IllegalArgumentException(
        "A type must not be declared FINAL while there "
        + "exist extending types.");

    /* Set value and inform interested listeners. */
    fireVetoableChange("final", oldValue, newValue);
    _final = newValue;
    firePropertyChange("final", oldValue, newValue);
  }



  /** @return The currently extended domain, or <code>null</code>
              if this domain does not extend any other domain.

      @see #setExtending(ch.interlis.Domain)
  */
  public Domain getExtending ()
  {
    return extending;
  }



  /** @return whether or not <code>this</code> is extending
              <code>dd</code>
  */
  public boolean isExtendingIndirectly (Domain dd)
  {
    for (Domain parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == dd)
        return true;
    }
    return false;
  }



  /** Causes this domain to extend another domain.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param extending  The domain view being extended, or
                        <code>null</code> if this domain is
                        going to be independent of other domains.


      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is a final domain; final
                 domains can not be extended.


      @exception java.lang.IllegalArgumentException if
                 the resulting extension graph would contain
                 cycles. For instance, if <code>A</code> extends
                 <code>B</code> and <code>B</code> extends
                 <code>C</code>, the call <code>C.setExtending(A)</code>
                 would throw an exception.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Domain extending)
    throws java.beans.PropertyVetoException
  {
	Domain oldValue = this.extending;
	Domain newValue = extending;

    /* Check whether there is anything to do. The JavaBeans
       specification strongly recommends this check to avoid
       certain infinite loops which might occur otherwise.
    */
    if (oldValue == newValue)
      return;

    /* Can't extend a FINAL object. */
    if ((newValue != null) && newValue.isFinal ())
      throw new IllegalArgumentException(
        formatMessage("err_cantExtendFinal", newValue.toString()));

    /* Ensure that the extension graph will be acyclic. */
	if ((newValue != null) && newValue.isExtendingIndirectly (this))
      throw new IllegalArgumentException(formatMessage(
        "err_cyclicExtension", this.toString(), newValue.toString()));


    /* Give interested parties a chance to oppose to the change. */
	fireVetoableChange ("extending", oldValue, newValue);

    /* Let the type decide whether it wants to extend extending.type.
       Good change for complaints. The JavaBeans protocol allows for
       exceptions after the fireVetoableChange above.
    */
    if (type != null)
    {
      if (extending == null)
        type.setExtending (null);
      else
        type.setExtending (extending.getType());
    }

    /* Perform the change. */
    if (oldValue != null)
      oldValue.extendedBy.remove (this);
    this.extending = newValue;
    if (newValue != null)
      newValue.extendedBy.add (this);


    /* Inform interested parties about the change. */
    firePropertyChange ("extending", oldValue, newValue);
  }



  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    Type myType = this.getType ();
    Type otherType = ((Domain) with).getType ();
    if (myType == null)
    {
      /* Yes, this could be expressed in a shorter way. But this is probably more legible. */
      if (otherType == null)
        return true;
      else
        return false;
    }

    if (!myType.checkStructuralEquivalence (otherType))
    {
      EhiLogger.logError(formatMessage ("err_diff_domainType", this.toString(), with.toString()));
      return false;
    }

    if (this.isAbstract() != ((Domain) with).isAbstract())
    {
      EhiLogger.logError(formatMessage ("err_diff_mismatchInAbstractness", this.toString(), with.toString()));
      return false;
    }


    if (this.isFinal() != ((Domain) with).isFinal())
    {
      EhiLogger.logError(formatMessage ("err_diff_mismatchInFinality", this.toString(), with.toString()));
      return false;
    }

    return true;
  }
	@Override
  	protected void linkTranslationOf(Element baseElement)
  	{
	    super.linkTranslationOf(baseElement);
		Type type=getType();
		Type baseType=((Domain) baseElement).getType();
		if(type.getClass()!=baseType.getClass()){
	        throw new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_domainType",getScopedName(),((Domain) baseElement).getScopedName()));
		}
		if (type instanceof TypeAlias){
			if(((TypeAlias)type).getAliasing().getTranslationOfOrSame()!=((TypeAlias)baseType).getAliasing().getTranslationOfOrSame()){
		        throw new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_domainType",getScopedName(),((Domain) baseElement).getScopedName()));
			}
		}
		type.linkTranslationOf(baseType);
  	}
	@Override
	public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
	  throws java.lang.IllegalStateException
	{
	    super.checkTranslationOf(errs,name,baseName);
	    Domain baseElement=(Domain)getTranslationOf();
	    if(baseElement==null) {
	        return;
	    }
	    
	    if(isAbstract()!=baseElement.isAbstract()) {
	        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInAbstractness",getScopedName(),baseElement.getScopedName())));
	    }
	    if(isFinal()!=baseElement.isFinal()) {
	        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInFinality",getScopedName(),baseElement.getScopedName())));
	    }
        /* do not validate MANDATORY here, because it is propagated to the type and validated there
         * if(isDefinedMandatory()!=baseElement.isDefinedMandatory()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInMandatory",getScopedName(),baseElement.getScopedName())));
        }*/
	    Ili2cSemanticException err=null;
	    err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseDomainMismatch");
	    if(err!=null) {
	        errs.add(err);
	    }
	    Type type=getType();
        try {
            type.checkTranslationOf(errs,getScopedName(),baseElement.getScopedName());
            if(type instanceof AbstractCoordType) {
                String crs=((AbstractCoordType) type).getCrs(this);
                String originCrs=((AbstractCoordType) baseElement.getType()).getCrs(baseElement);
                if(crs==null && originCrs==null) {
                    
                }else {
                    if(crs==null || originCrs==null) {
                        throw new Ili2cSemanticException();
                    }
                    if(!crs.equals(originCrs)) {
                        throw new Ili2cSemanticException();
                    }
                }
            }
        }catch(Ili2cSemanticException ex) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_domainType",getScopedName(),baseElement.getScopedName())));
        }
	    
	}


    public boolean isDefinedMandatory() {
        return _mandatory;
    }


    public void setDefinedMandatory(boolean _mandatory) throws PropertyVetoException {
        this._mandatory = _mandatory;
        if(type!=null) {
            type.setMandatory(true);
        }
    }

}
