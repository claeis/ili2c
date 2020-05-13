/*****************************************************************************
 *
 * Container.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;


import java.beans.beancontext.*;
import java.net.URL;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import ch.ehi.basics.logging.EhiLogger;



/** An abstract class which serves as a common
    superclass for all Interlis container constructs.


    @version   January 28, 1999
    @author    Sascha Brawer (mailto:sb@adasys.ch)
*/
public abstract class Container<E extends Element>
  extends Element
  implements BeanContext /*, Collection<E> */
{
  protected final BeanContextSupport bcs = new BeanContextSupport(this);

  // Here we have a problem for generics with the BeanContext interface which
  // extends "Collection" without type arguments.
  // It may be questionnable whether all these Java beans features, which
  // seemed to be quite a good thing in 1999, are really used today because
  // they make the metamodel quite heavyweight and do not permit to have an
  // add(E e) method.
  // IMHO all this BCS stuff should be removed from the metamodel. **GV1012

  /** The elements of this container.
  * has to be created by implementations
  */
  protected Collection<E> elements;


  protected Container()
  {
    bccs = bcs;
    elements=createElements();
  }


  /** template method to initialize the elements field
   */
  abstract protected Collection<E> createElements();


  /* Support for the Collection interface; delegates to the
     elements member */
  public int size() {
  	if(isAlias()){
  		return ((Container<E>)getReal()).size();
  	}else{
	  	return elements.size();
  	}
  }
  public boolean isEmpty() {
  	if(isAlias()){
  		return ((Container<E>)getReal()).isEmpty();
  	}else{
		return elements.isEmpty();
  	}
  }
  public boolean contains(Object o) {
  	if(isAlias()){
  		return ((Container<E>)getReal()).contains(o);
  	}else{
	  	return elements.contains(o);
  	}
  }
  public final Iterator<E> iterator() {
  	if(isAlias()){
  		return ((Container<E>)getReal()).iterator();
  	}else{
	  	return elements.iterator();
  	}
  }
  public Object[] toArray() {
  	if(isAlias()){
  		return ((Container)getReal()).toArray();
  	}else{
	  	return elements.toArray();
  	}
  }
  public Object[] toArray(Object[] a) {
  	if(isAlias()){
  		return ((Container)getReal()).toArray(a);
  	}else{
	  	return elements.toArray(a);
  	}
  }


  /** Adds a new Element to this Container.
      @exception ClassCastException if <code>o</code> is not
                 an Interlis element.
      @exception NullPointerException if <code>o</code> is
                 <code>null</code>
  */
  public boolean add(Object o) {
  	if(isAlias()){
  		return ((Container<E>)getReal()).add(o);
  	}else{
  	    E e = (E) o;
	    /* Null is not accepted. */
	    if (o == null) {
            throw new IllegalArgumentException();
        }


	    /* Only Interlis elements can be added. */
	    if (o instanceof Element) {
	    try {
	      e.setBeanContext(this);
	    } catch (java.beans.PropertyVetoException pve) {
	      throw new IllegalArgumentException(pve.getLocalizedMessage());
	    }
	    boolean wasAdded = elements.add(e);
	/*
	    if (wasAdded)
	      bcs.fireChildrenAdded(
	          new java.beans.beancontext.BeanContextMembershipEvent(this,
	          new Object[] { o }));
	*/
	    return wasAdded;
            }
            throw new ClassCastException();
  	}
  }


  public boolean remove(Object o) {
  	if(isAlias()){
  		return ((Container)getReal()).remove(o);
  	}else{
	  	return elements.remove(o);
  	}
  }
  public boolean containsAll(Collection c) {
  	if(isAlias()){
  		return ((Container)getReal()).containsAll(c);
  	}else{
	  	return elements.containsAll(c);
  	}
  }
  public boolean addAll(Collection c) {
  	if(isAlias()){
  		return ((Container)getReal()).addAll(c);
  	}else{
	  	return elements.addAll(c);
  	}
  }
  public boolean removeAll(Collection c) {
  	if(isAlias()){
  		return ((Container)getReal()).removeAll(c);
  	}else{
	  	return elements.removeAll(c);
  	}
  }
  public boolean retainAll(Collection c) {
  	if(isAlias()){
  		return ((Container)getReal()).retainAll(c);
  	}else{
	  	return elements.retainAll(c);
  	}
  }
  public void clear() {
  	if(isAlias()){
  		((Container)getReal()).clear();
  		return;
  	}else{
	  	elements.clear();
  	}
  }


  public void addBeanContextMembershipListener(
    BeanContextMembershipListener bcml)
  {
  	if(isAlias()){
  		((Container)getReal()).addBeanContextMembershipListener(bcml);
  		return;
  	}else{
	    bcs.addBeanContextMembershipListener(bcml);
  	}
  }


  public URL getResource(String name, BeanContextChild bcc)
  {
  	if(isAlias()){
  		return ((Container)getReal()).getResource(name, bcc);
  	}else{
	    return bcs.getResource(name, bcc);
  	}
  }


  public InputStream getResourceAsStream(
    String name, BeanContextChild bcc)
  {
  	if(isAlias()){
  		return ((Container)getReal()).getResourceAsStream(name,bcc);
  	}else{
	    return bcs.getResourceAsStream(name, bcc);
  	}
  }


  public Object instantiateChild(String beanName)
    throws ClassNotFoundException, java.io.IOException
  {
  	if(isAlias()){
  		return ((Container)getReal()).instantiateChild(beanName);
  	}else{
	    return bcs.instantiateChild(beanName);
  	}
  }


  public void removeBeanContextMembershipListener(
    BeanContextMembershipListener bcml)
  {
  	if(isAlias()){
  		((Container)getReal()).removeBeanContextMembershipListener(bcml);
  		return;
  	}else{
	    bcs.removeBeanContextMembershipListener(bcml);
  	}
  }



  public void setDesignTime(boolean designTime)
  {
  	if(isAlias()){
  		((Container)getReal()).setDesignTime(designTime);
  		return;
  	}else{
	    bcs.setDesignTime(designTime);
  	}
  }


  public boolean isDesignTime() {
  	if(isAlias()){
  		return ((Container)getReal()).isDesignTime();
  	}else{
	    return bcs.isDesignTime();
  	}
  }


  public boolean needsGui() {
  	if(isAlias()){
  		return ((Container)getReal()).needsGui();
  	}else{
	    return bcs.needsGui();
  	}
  }


  public void dontUseGui() {
  	if(isAlias()){
  		((Container)getReal()).dontUseGui();
  		return;
  	}else{
	    bcs.dontUseGui();
  	}
  }


  public void okToUseGui() {
  	if(isAlias()){
  		((Container)getReal()).okToUseGui();
  		return;
  	}else{
	    bcs.okToUseGui();
  	}
  }


  public boolean avoidingGui() {
  	if(isAlias()){
  		return ((Container)getReal()).avoidingGui();
  	}else{
	    return bcs.avoidingGui();
  	}
  }


  /** @deprecated Going to be out of use. */
  protected Model resolveModelName(String name) {
  	if(isAlias()){
  		return ((Container)getReal()).resolveModelName(name);
  	}else{
	    BeanContext bc = getBeanContext();
	    if (bc instanceof Container) {
            return ((Container) bc).resolveModelName(name);
        } else {
            return null;
        }
  	}
  }



  /** Finds a contained Interlis Element its class and name. An Element
      matches if both its name equals <code>name</code> and its class
      is the same as or a subclass of </code>klass</code>. Those
      subclasses that support an inheritance concept will check for
      inherited elements as well.


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
  		return ((Container)getReal()).getElement (klass, name);
  	}else{
	    /* If klass is null, the result will be null. This complies
	       with the specified documentation. If name is null, however,
	       the code below would throw an exception, which is not the
	       desired behaviour.
	    */
	    if (name == null) {
            return null;
        }


	    Iterator it = iterator();
	    while (it.hasNext ())
	    {
	      Element e = (Element) it.next ();
	      if (klass.isAssignableFrom (e.getClass())
	          && name.equals (e.getName())) {
            return e;
        }
	    }
	    return null;
  	}
  }
  public Element getRealElement (Class klass, String name)
  {
  	if(isAlias()){
  		return ((Container)getReal()).getRealElement (klass, name);
  	}else{
	  	Element ele=getElement(klass,name);
	  	if(ele!=null){
	  		ele=ele.getReal();
	  	}
	  	return ele;
  	}
  }
  /* Same as getContainer, but returns <code>this</code>
     if <code>this</code> is an instance of <code>klass</code>.


     @see Element#getContainer(java.lang.Class)
  */
  public final Container getContainerOrSame(Class klass) {
  	if(isAlias()){
  		return ((Container)getReal()).getContainerOrSame(klass);
  	}else{
	    if (klass.isAssignableFrom(this.getClass())) {
            return this;
        } else {
            return getContainer(klass);
        }
  	}
  }



  /* Determines whether this Container contains an element, either by
     direct or by indirect containment. No container contains
     itself.
  */
  public final boolean containsIndirectly (Element elt)
  {
  	if(isAlias()){
  		return ((Container)getReal()).containsIndirectly(elt);
  	}else{
	    Container curContainer;


	    curContainer = elt.getContainer (Container.class);


	    while (curContainer != null)
	    {
	      if (curContainer == this) {
            return true;
        }


	      curContainer = curContainer.getContainer (Container.class);
	    }
	    return false;
  	}
  }



  /** Calculates a set that contains everything that is contained in this
      container, be it directly or indirectly. In other words, this
      method calculates the transitive closure of the containment
      relation.
  */
  public Set getIndirectElements ()
  {
  	if(isAlias()){
  		return ((Container)getReal()).getIndirectElements();
  	}else{
	    Set result = new HashSet();
	    getIndirectElements_helper (result);
	    return result;
  	}
  }



  private final void getIndirectElements_helper (Set s)
  {
    Iterator iter = iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();


      if (!(obj instanceof Element)) {
        continue;
    }


      s.add (obj);
      if (obj instanceof Container) {
        ((Container) obj).getIndirectElements_helper (s);
    }
    }
  }



  /** Returns the name space of evaluables (right-hand side of assignments)
      mentioned somewhere inside this Container.


      <p>Certain elements of Containers, for instance constraints or selections,
      refer to attributes. The name space of these attributes is though not
      necessarily identical to the container.  For example, attribute names
      mentioned in a Graphic refer to the Viewable on which the Graphic
      is based, not the Graphic itself.
  */
  public Viewable getRHSNameSpace ()
  {
  	if(isAlias()){
  		return ((Container)getReal()).getRHSNameSpace();
  	}else{
	    Container c = getContainer();
	    if (c == null) {
            return null;
        } else {
            return c.getRHSNameSpace ();
        }
  	}
  }



  /** Performs certain integrity checks, including checks for the
      elements of a container. Unfortunately, some checks
      can only be performed when all modifications are done. An example
      is the constraint that relation attributes to an abstract table in
      a topic are only valid if there exist concrete extensions of that
      table in the same topic.


      @exception java.lang.IllegalStateException if the integrity
                 is not given.
  */
  @Override
  public void checkIntegrity (List<Ili2cSemanticException> errs)
    throws java.lang.IllegalStateException
  {
  	if(isAlias()){
  		((Container)getReal()).checkIntegrity(errs);
  		return;
  	}else{
	    super.checkIntegrity (errs);


	    Iterator iter = iterator();
	    while (iter.hasNext ())
	    {
	      Object obj = iter.next();
	      if (obj instanceof ch.interlis.ili2c.metamodel.Element) {
            ((ch.interlis.ili2c.metamodel.Element) obj).checkIntegrity (errs);
        }
	    }
  	}
  }
	@Override
  	protected void linkTranslationOf(Element baseElement)
  	{
	    super.linkTranslationOf(baseElement);


	    Iterator baseIter = ((Container)baseElement).iterator();
	    Iterator iter = iterator();
	    while (iter.hasNext ())
	    {
	    	if(!baseIter.hasNext()){
		        throw new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_unequalNumberOfElts",getScopedName(),((Element) baseElement).getScopedName()));
	    	}
	      Object baseChild = baseIter.next();
	      Object child = iter.next();
	      if (child instanceof Element) {
	    	  if(((Element) child).isDirty() || ((Element) baseChild).isDirty()){
	    		  continue;
	    	  }
	    	  if(child.getClass()!=baseChild.getClass()){
			        throw new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInClass",((Element) child).getScopedName(),((Element) baseChild).getScopedName()));
	    	  }else{
	              ((ch.interlis.ili2c.metamodel.Element) child).linkTranslationOf ((ch.interlis.ili2c.metamodel.Element)baseChild);
	    	  }
        }
	    }
    	if(baseIter.hasNext()){
	        throw new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_unequalNumberOfElts",getScopedName(),((Element) baseElement).getScopedName()));
    	}
  	}
	  @Override
	  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
	    throws java.lang.IllegalStateException
	  {
	        Container baseElement=(Container)getTranslationOf();
	        if(baseElement==null) {
	            return;
	        }
	    if(isAlias()){
	        ((Container)getReal()).checkTranslationOf(errs,name,baseName);
	        return;
	    }else{
	        super.checkTranslationOf(errs,name,baseName);


	        Iterator iter = iterator();
	        while (iter.hasNext ())
	        {
	          Object obj = iter.next();
	          if (obj instanceof ch.interlis.ili2c.metamodel.Element) {
	            ((ch.interlis.ili2c.metamodel.Element) obj).checkTranslationOf(errs,name,baseName);
	        }
	        }
	    }
	  }



  /** Checks whether of not this Container is structurally
      equivalent to another INTERLIS Element.
  */
  public boolean checkStructuralEquivalence (Element with)
  {
  	if(isAlias()){
  		return ((Container)getReal()).checkStructuralEquivalence (with);
  	}else{
	    if (!super.checkStructuralEquivalence (with)) {
            return false;
        }


	    /* It is guaranteed, when reaching this code, that "with" is an intance of
	       ch.interlis.Container; otherwise, Element.checkStructuralEquivalence would
	       have returned false. Therefore, the cast in the assignment of withSize below
	       is safe.
	    */
	    int mySize = this.size ();
	    int withSize = ((Container) with).size ();
	    if (mySize != withSize)
	    {
	      EhiLogger.logError(formatMessage ("err_diff_unequalNumberOfElts",
	                       this.toString (),
	                       Integer.toString (mySize),
	                       with.toString (),
	                       Integer.toString (withSize)));
	      return false;
	    }


	    /* Same number of elements. */
	    Iterator myIter = this.iterator ();
	    Iterator otherIter = ((Container) with).iterator ();
	    boolean allFine = true;


	    while (myIter.hasNext () && otherIter.hasNext ())
	    {
	      Element curMy = (Element) myIter.next ();
	      Element curOther = (Element) otherIter.next ();


	      if (curMy != null)
	      {
	        if (!curMy.checkStructuralEquivalence (curOther)) {
                allFine = false;
            }
	      }
	    }


	    return allFine;
  	}
  }
}
