/*****************************************************************************
 *
 * AttributeDef.java
 * -----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.*;


/** AttributeDef is an abstract class which serves as a common
    superclass for all Interlis attribute definitions.

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public abstract class AttributeDef
  extends AbstractLeafElement
  implements Extendable
{
  protected String        name;
  protected AttributeDef  extending;
  protected boolean       _final;
  protected boolean       _abstract;
  protected Set           extendedBy = new HashSet(2);
  protected Type          domain;

  protected AttributeDef ()
  {
    name = "";
    extending = null;
    _final = false;
    _abstract = false;
    domain = null;

  }



  /** Returns the value of the <code>name</code> property
      which indicates the name of this attribute without
      any scope prefixes.


      @see #setName(java.lang.String)
  */
  public String getName ()
  {
    return name;
  }



  public String toString ()
  {
    Container cont = getContainer(Viewable.class);

    if (cont == null)
      return getName();

    return cont.getScopedName(null) + ":" + getName();
  }



  /** Sets the value of the <code>name</code> property.
      Attributes are identified and used by specifying their name.


      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this attribute.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.


      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another attribute. The
                 only acceptable conflict is with the AttributeDef
                 that this attribute directly extends.

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

    checkNameSanity(name, /* empty ok? */ false);

    /* Make sure that the new name does not conflict
       with the name of another Viewable, except the
       one that this object is extending directly.
    */
    checkNameUniqueness (newValue, AttributeDef.class, (AttributeDef) getRealExtending(),
                         "err_attributeDef_duplicateName");





    fireVetoableChange ("name", oldValue, newValue);

    this.name = newValue;
    firePropertyChange ("name", oldValue, newValue);
  }


  public Element getExtending ()
  {
    return extending;
  }
	public Element getRealExtending()
	{
		Element ext=getExtending();
		return (ext!=null) ? ext.getReal() : null;
	}



  /** @return whether or not <code>this</code> is extending
              <code>ad</code>
  */
  public boolean isExtendingIndirectly (Element ext)
  {
    for (AttributeDef parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == ext)
        return true;
    }
    return false;
  }


  public boolean isDependentOn (Element e)
  {
    if (e instanceof AttributeDef) {
      boolean i = isExtendingIndirectly((AttributeDef) e);
      return i;
    }

    return false;
  }



  /* Documentation Note
     ------------------
     Make sure to propagate manually any changes
     to the documentation for the AttributeDef subclasses.
  */
  /** Sets the AttributeDef which <code>this</code> extends. This
      call will make this AttributeDef's domain extend the domain
      of <code>ext</code> as well.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param ext The new AttributeDef being extended, or <code>null</code>
                 if none is being extended.


      @exception java.lang.IllegalArgumentException if <code>ext</code>
                 is declared as <code>final</code>.


      @exception java.lang.IllegalArgumentException if <code>ext</code>
                 is extending this AttributeDef, be it directly or
                 indirectly. Cyclic extension graphs are not permitted.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element ext)
    throws java.beans.PropertyVetoException
  {
    AttributeDef oldValue = extending;
    AttributeDef newValue = (AttributeDef) ext;


    if (oldValue == newValue)
      return; /* nothing needs to be done */

    if ((newValue != null) && newValue.isFinal())
      throw new IllegalArgumentException(
        formatMessage ("err_cantExtendFinal", newValue.toString()));

    /* Ensure that the extension graph will be acyclic. */
    if ((newValue != null) && newValue.isExtendingIndirectly(this))
    {
      throw new IllegalArgumentException (
        formatMessage ("err_cyclicExtension", this.toString(),
                       newValue.toString()));
    }


    if (domain != null)
    {
      if (newValue == null)
        domain.checkTypeExtension (null);
      else
        domain.checkTypeExtension (newValue.getDomain ());
    }



    fireVetoableChange ("extending", oldValue, newValue);

	if (domain != null)
	{
	  if (newValue == null)
		domain.setExtending (null);
	  else
		domain.setExtending (newValue.getDomain());
	}

    if (oldValue != null)
      oldValue.extendedBy.remove(this);
    extending = newValue;
    if (newValue != null)
      newValue.extendedBy.add(this);


    firePropertyChange ("extending", oldValue, newValue);
  }


  /** Returns whether this attribute is abstract or can have
      instances.

      @return <code>true</code> if the view is abstract,
              <code>false</code> if instances are acceptable.

      @see #setAbstract(boolean)
  */
  public boolean isAbstract ()
  {
    return _abstract;
  }



  /** Sets the value of the <code>abstract</code> property;
      an abstract attribute can not have instances.


      <p>In JavaBeans terminology, the <code>abstract</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param abst Pass <code>true</code> to make the attribute
                  abstract, pass <code>false</code> to make
                  the attribute instantiable.


      @exception java.lang.IllegalArgumentException if this
                 attribute is final and <code>abst</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setAbstract(boolean abst)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _abstract;
    boolean newValue = abst;

    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;

    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isFinal())
      throw new IllegalArgumentException(
          rsrc.getString("err_abstractFinal"));


    if ((newValue == false) && (domain != null) && domain.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_attributeDef_concreteWithAbstractDomain", this.toString()));


    fireVetoableChange("abstract", oldValue, newValue);
    _abstract = newValue;
    firePropertyChange("abstract", oldValue, newValue);
  }



  public boolean isFinal()
  {
    return _final;
  }


  /** Sets the value of the <code>final</code> property;
      a final attribute can not be extended by other
      attributes.


      <p>In JavaBeans terminology, the <code>final</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param fin Pass <code>true</code> to make the attribute
                 final, pass <code>false</code> to allow for
                 extensions.

      @exception java.lang.IllegalArgumentException if this
                 attribute is abstract and <code>fin</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.lang.IllegalArgumentException if
                 <code>fin</code> is <code>true</code> and
                 there exists another attribute which extends
                 this view.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>final</code> property
                 and does not agree with the change.
  */
  public void setFinal(boolean fin)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _final;
    boolean newValue = fin;

    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;

    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isAbstract())
      throw new IllegalArgumentException(
          rsrc.getString("err_abstractFinal"));



    /* Can't make this final as long as there exists another
       object that extends this one. */
    if ((newValue == true) && !extendedBy.isEmpty())
      throw new IllegalArgumentException(formatMessage(
        "err_cantMakeExtendedFinal",
        this.toString(),
        extendedBy.iterator().next().toString()));

    /* Set value and inform interested listeners. */
    fireVetoableChange("final", oldValue, newValue);
    _final = newValue;
    firePropertyChange("final", oldValue, newValue);
  }



  /** Walks the extension hierarchy to determine whether or not <code>this</code>
      is extending <code>ext</code>, be the extension directly or indirectly.
      Any Extendable is extending itself. In other words, this function
      corresponds to the <em>reflexive-transitive closure</em> of the relation
      determined by the <code>setExtending</code> operation.
  */
  public boolean isExtending (Element ext)
  {
    for (AttributeDef parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == ext)
        return true;
    }

    return false;
  }



  /** Calculates a Set of all Extendables that are extending
      this Extendable, be it through direct extension or be
      it in several steps.  The result consists of
      all directly extending objects plus all that directly
      extend those, plus ..., etc. In other words,
      this method calculates the transitive closure
      of the <em>extending</em> relation.

      @return A new Set that belongs to the caller; the caller
              is thus free to modify it according to its needs.
              Changes in the result will not have any effect
              on the <em>extending</em> property.
  */
  public Set getExtensions ()
  {
    Set result = new HashSet ();
    getExtensions_recursiveHelper (result);
    return result;
  }



  /** @see getExtensions() */
  private final void getExtensions_recursiveHelper (Set s)
  {
    s.add (this);
    Iterator iter = extendedBy.iterator();
    while (iter.hasNext())
      ((AttributeDef) iter.next()).getExtensions_recursiveHelper (s);
  }

  public Type getDomain ()
  {
    return domain;
  }



  public Type getDomainResolvingAliases ()
  {
    return Type.findReal (domain);
  }


  /* Documentation Note
     ------------------
     Make sure to propagate manually any changes
     to the documentation for the AttributeDef subclasses.
  */
  /** Changes the domain of an attribute. The domain is a restriction
      on the set of possible values that this attribute can have.

      <p>In JavaBeans terminology, the <code>domain</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param domain The new domain of this attribute.


      @exception java.lang.IllegalArgumentException if
                 <code>domain</code> is <code>null</code>.


      @exception java.lang.IllegalArgumentException if
                 <code>domain</code> is abstract, but this
                 attribute is not abstract. Only abstract
                 attributes can have abstract domains.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setDomain (Type domain)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.domain;
    Type newValue = domain;
    Type realNewValue = null;

    if (newValue == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

    if (oldValue == newValue)
      return;

    if ((newValue != null)
        && newValue.isAbstract()
        && !this.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_attributeDef_domainIsAbstractButAttrIsNot", this.toString()));

    fireVetoableChange ("domain", oldValue, newValue);


    if (extending == null)
      newValue.setExtending (null);
    else
      newValue.setExtending (extending.getDomain());


    this.domain = newValue;
    firePropertyChange ("domain", oldValue, newValue);
  }


  public boolean checkStructuralEquivalence (Element with, ErrorListener listener)
  {
    if (!super.checkStructuralEquivalence (with, listener))
      return false;

    Type myDomain = this.getDomain ();
    Type otherDomain = ((AttributeDef) with).getDomain ();

    /* Probably only for myDomain == otherDomain == null */
    if (myDomain == otherDomain)
      return true;

    if (myDomain == null)
      return false;

    if (!myDomain.checkStructuralEquivalence (otherDomain, listener))
    {
      listener.error (new ErrorListener.ErrorEvent (
        formatMessage ("err_diff_attributeType", this.toString(), with.toString()),
        /* origin of error */ this,
        ErrorListener.ErrorEvent.SEVERITY_ERROR));
      return false;
    }

    return true;
  }

}