/*****************************************************************************
 *
 * ExtendableContainer.java
 * ------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.*;
import ch.ehi.basics.logging.EhiLogger;

/** A Container that can be extended.
*/
public abstract class ExtendableContainer<E extends Element> extends Container<E> implements Extendable
{
  protected boolean  _final = false;
  protected boolean  _abstract = false;

  protected ExtendableContainer<E>  extending = null;

  /** The containers which are extended by this container.
  */
  protected Set<ExtendableContainer<E>> extendedBy = new HashSet<ExtendableContainer<E>>(2);



  /** Causes this container to extend another container.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param extending  The new container being extended, or
                        <code>null</code> if this container is
                        going to be independent of other containers.

      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is a final element; final
                 elements can not be extended.

      @exception java.lang.IllegalArgumentException if
                 the resulting extension graph would contain
                 cycles. For instance, if <code>A</code> extends
                 <code>B</code> and <code>B</code> extends
                 <code>C</code>, the call <code>C.setExtending(A)</code>
                 would throw an exception.

      @exception java.lang.ClassCastException if <code>extending</code>
                 is neither <code>null</code> nor an instance of
                 the class <code>ExtendableContainer</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException
  {
  	if(isAlias()){
  		((ExtendableContainer)getReal()).setExtending (extending);
  		return;
  	}else{

	    /* The cast in the assignment to newValue throws a ClassCastException
	       if extending is neither null nor an instance of ExtendableContainer.
	       This is exactly what the API documentation specifies.
	    */
		ExtendableContainer oldValue = this.extending;
		ExtendableContainer newValue = (ExtendableContainer) extending;

	    /* Check whether there is anything to do. The JavaBeans
	       specification strongly recommends this check to avoid
	       certain infinite loops which might occur otherwise.
	    */
	    if (oldValue == newValue)
	      return;

	    /* Can't extend a FINAL object. */
	    if ((newValue != null) && newValue.isFinal())
	      throw new IllegalArgumentException(
	        formatMessage("err_cantExtendFinal", newValue.toString()));

	    /* Ensure that the extension graph will be acyclic. */
		if ((newValue != null) && newValue.isExtending (this))
	      throw new IllegalArgumentException (formatMessage(
	        "err_cyclicExtension", this.toString(), newValue.toString()));

	    /* Give interested parties a chance to oppose to the change. */
		fireVetoableChange ("extending", oldValue, newValue);

	    /* Perform the change. */
	    if (oldValue != null)
	      oldValue.extendedBy.remove(this);
	    this.extending = newValue;
	    if (newValue != null)
	      newValue.extendedBy.add(this);

	    /* Inform interested parties about the change. */
	    firePropertyChange("extending", oldValue, newValue);
  	}
  }


  /** @return The currently extended container, or <code>null</code>
              if this container does not extend any other container.

      @see #setExtending(ch.interlis.Extendable)
      @see #isExtending(ch.interlis.Extendable)
  */
  public Element getExtending ()
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).getExtending();
  	}else{
	    return extending;
  	}
  }
  /** gets the root of the inheritance tree or null if this is a root.
   */
  public ExtendableContainer getRootExtending ()
  {
	ExtendableContainer ret=(ExtendableContainer)getExtending();
	  if(ret!=null){
		  while(true){
			  Element ret1=ret.getExtending();
			  if(ret1==null){
				  break;
			  }
			  ret=(ExtendableContainer)ret1;
		  }
	  }
	  return ret;
  }
	public Element getRealExtending()
	{
	  	if(isAlias()){
	  		return ((ExtendableContainer)getReal()).getRealExtending();
	  	}else{
			Element ext=getExtending();
			return (ext!=null) ? ext.getReal() : null;
	  	}
	}


  /** Determines whether or not <code>this</code> is extending
      <code>extendee</code>, be it through direct or through
      indirect extension. In other words, this function corresponds
      to the reflexive-transitive closure of the relation formed
      by the <code>extending</code> property.

      @param extendee  The potentially extended Extendable.
  */
  public boolean isExtending (Element extendee)
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).isExtending(extendee);
  	}else{
	    for (ExtendableContainer parent = this; parent != null;
	         parent = parent.extending)
	    {
	      if (parent == extendee)
	        return true;
	    }

	    return false;
  	}
  }


  /** Returns whether this container is abstract or can have
      instances.

      @return <code>true</code> if the container is abstract,
              <code>false</code> if instances are acceptable.

      @see #setAbstract(boolean)
  */
  public boolean isAbstract()
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).isAbstract();
  	}else{
	    return _abstract;
  	}
  }


  /** Sets the value of the <code>abstract</code> property;
      an abstract element can not be instantiated.

      <p>In JavaBeans terminology, the <code>abstract</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param abst Pass <code>true</code> to make the element
                  abstract, pass <code>false</code> to make
                  the container instantiable.

      @exception java.lang.IllegalArgumentException if this
                 element is final and <code>abst</code> is
                 <code>true</code>, because it would not make
                 sense to declare an element as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
  	if(isAlias()){
  		((ExtendableContainer)getReal()).setAbstract(abst);
  		return;
  	}else{
	    boolean oldValue = _abstract;
	    boolean newValue = abst;

	    /* Check for cases in which there is nothing to do. */
	    if (oldValue == newValue)
	      return;

	    /* Can not be ABSTRACT and FINAL at the same time. */
	    if ((newValue == true) && isFinal())
	      throw new IllegalArgumentException(
	          rsrc.getString("err_abstractFinal"));

	    fireVetoableChange ("abstract", oldValue, newValue);
	    _abstract = newValue;
	    firePropertyChange ("abstract", oldValue, newValue);
  	}
  }



  /** Returns the value of the <code>final</code> property
      which determines whether or not an element can be extended
      by other elements.

      @return <code>true</code> if the element has been declared
              to be final; <code>false</code> if the element
              is allowed to be extended by other elements.
  */
  public boolean isFinal ()
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).isFinal();
  	}else{
	    return _final;
  	}
  }


  /** Sets the value of the <code>final</code> property;
      a final element can not be extended by other elements.

      <p>In JavaBeans terminology, the <code>final</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param fin Pass <code>true</code> to make the element
                 final, pass <code>false</code> to allow for
                 extensions.

      @exception java.lang.IllegalArgumentException if this
                 element is abstract and <code>fin</code> is
                 <code>true</code>, because it would not make
                 sense to declare an element as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.lang.IllegalArgumentException if
                 <code>fin</code> is <code>true</code> and
                 there exist alreay any elements E for which
                 <code>E.isExtending (this)</code> would be
                 true. In other words, it is not possible
                 to declare an element to be final while
                 there exist extensions.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>final</code> property
                 and does not agree with the change.
  */
  public void setFinal (boolean fin)
    throws java.beans.PropertyVetoException
  {
  	if(isAlias()){
  		((ExtendableContainer)getReal()).setFinal(fin);
  		return;
  	}else{
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
  }


  /** Determines whether or not <code>this</code> is dependent on
      <code>other</code>. Dependent elements are required to follow
      their base elements in an Interlis description file.

      <ol><li>No Element is dependent on itself.</li>
      <li>If an Element <i>A</i> extends another element <i>B</i>,
          be it directly or indirectly, <i>A</i> is dependent on <i>B</i>.</li>
      </ol>

      @param other The Element of which it is determined whether
                   <code>this</code> does depend on.

      @return <code>true</code> if this Element depends on <code>other</code>,
              <code>false</code> otherwise.
  */
  public boolean isDependentOn (Element other)
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).isDependentOn(other);
  	}else{
	    if ((other instanceof Extendable) && isExtending (other))
	      return true;

	    return false;
  	}
  }


  /** Finds a contained Interlis Element given its class and name.
      An Element matches if both its name equals <code>name</code>
      and its class is the same as or a subclass of </code>klass</code>.
      If an element is not declared as part of this container,
      but is inherited from an extended container instead, that element
      will be returned as well.

      @param klass   The class of the Element in question.
      @param name    The name of the Element in question.

      @return An Interlis Element, or <code>null</code> if there is
              no Element with the specified name and class.

      @exception java.lang.NullPointerException if <code>klass</code>
                 is <code>null</code>
  */
  public Element getElement (Class klass, String name)
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).getElement(klass, name);
  	}else{
	    Element elt;

	    /* First try: Find the element in this container. */
	    elt = super.getElement(klass, name);
	    if (elt != null)
	      return elt;

	    /* Second try: Find the element in inherited containers.
	       The recursion should not cause stack overflows
	       because the extension graphs are not very deep.
	    */
	    if (extending != null)
	      return extending.getElement(klass, name);

	    /* There is no such element. */
	    return null;
  	}
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
  public Set<ExtendableContainer<E>> getExtensions()
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).getExtensions();
  	}else{
	    Set<ExtendableContainer<E>> result = new HashSet<ExtendableContainer<E>>();
	    getExtensions_recursiveHelper (result);
	    return result;
  	}
  }
  public Set<ExtendableContainer<E>> getDirectExtensions()
  {
	  // return a copy
	  return new HashSet<ExtendableContainer<E>>(extendedBy);
  }


  /** @see getExtensions() */
  private final void getExtensions_recursiveHelper(Set<ExtendableContainer<E>> s)
  {
    s.add (this);
    Iterator<ExtendableContainer<E>> iter = extendedBy.iterator();
    while (iter.hasNext())
      iter.next().getExtensions_recursiveHelper(s);
  }

  public boolean checkStructuralEquivalence (Element with)
  {
  	if(isAlias()){
  		return ((ExtendableContainer)getReal()).checkStructuralEquivalence (with);
  	}else{
	    if (!super.checkStructuralEquivalence (with))
	      return false;

	    boolean fine = true;
	    if (this.isAbstract() != ((ExtendableContainer) with).isAbstract())
	    {
	      EhiLogger.logError(formatMessage ("err_diff_mismatchInAbstractness", this.toString(), with.toString()));
	      fine = false;
	    }

	    if (this.isFinal() != ((ExtendableContainer) with).isFinal())
	    {
	      EhiLogger.logError(formatMessage ("err_diff_mismatchInFinality", this.toString(), with.toString()));
	      fine = false;
	    }

	    return fine;
  	}
  }
}
