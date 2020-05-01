/* This file is part of the INTERLIS-Compiler.
 * For more information, please see <http://www.interlis.ch/>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.interlis.ili2c.metamodel;


import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextChildSupport;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.ResourceBundle;

import ch.ehi.basics.logging.EhiLogger;

/** Element is an abstract class which serves as a common
    superclass for all Interlis elements.

    @version   January 28, 1999
    @author    Sascha Brawer (mailto:sb@adasys.ch)
*/
public abstract class Element implements BeanContextChild,ElementAlias {
  /** A resource bundle with localizable error messages and
      other useful texts.
  */
  static final ResourceBundle rsrc
      = ResourceBundle.getBundle(ErrorMessages.class.getName(),
                                 java.util.Locale.getDefault());

	private String documentation=null;
	private ch.ehi.basics.settings.Settings metaValues=null;
	/** true, if definition somewhat incomplete due to (compile) errors.
	 */
	private boolean dirty=false;
	/** source line in ili-file.
	 */
	private int sourceLine=0;
	/** definition index
	 */
	private final int defidx;
	private static int _defidx=0;

	public Element()
	{
		defidx=_defidx++;
	}
  static final String formatMessage(String msg, Object[] args) {
    try {
      java.text.MessageFormat mess = new java.text.MessageFormat(
        rsrc.getString(msg));
      return mess.format(args);
    } catch (Exception ex) {
      return "Internal compiler error [" + ex.getLocalizedMessage() + "]";
    }
  }
  static Ili2cSemanticException checkElementRef(Element ele1, Element ele2, int sourceLine, String msg) {
      if(!equalElementRef(ele1,ele2)) {
          return new Ili2cSemanticException (sourceLine,formatMessage(msg));
      }
      return null;
  }
  static boolean equalElementRef(Element ele1, Element ele2) {
      if(ele1==null && ele2==null) {
      }else if(ele1==null || ele2==null) {
          return false;
      }else if(ele1.getTranslationOfOrSame()!=ele2.getTranslationOfOrSame()) {
          return false;
      }
      return true;
  }


  static final String formatMessage(String msg)
  {
    return formatMessage(msg, "");
  }

  public static final String formatMessage(String msg, String arg)
  {
    return formatMessage(msg, new Object[] { arg });
  }



  static final String formatMessage(String msg, String arg1, String arg2)
  {
    return formatMessage(msg, new Object[] { arg1, arg2 });
  }



  static final String formatMessage(String msg, String arg1, String arg2, String arg3)
  {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3 });
  }



  static final String formatMessage(
    String msg, String arg1, String arg2, String arg3, String arg4)
  {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3, arg4 });
  }



  static final String formatMessage(
    String msg, String arg1, String arg2, String arg3, String arg4, String arg5)
  {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3, arg4, arg5 });
  }



  static final String formatMessage(
    String msg, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6)
  {
    return formatMessage(msg, new Object[] { arg1, arg2, arg3, arg4, arg5, arg6 });
  }



  /** @return Whether <code>other</code> is dependent on this Element.
              Dependent elements follow their base elements in an
              Interlis description file. No Element is dependent on
              itself.
  */
  public boolean isDependentOn (Element other)
  {
    return false;
  }



  /** Support for implementing the BeanContextChild interface. The
      implementation of the methods of that interface are delegated
      to this instance.

      @see java.beans.beancontext.BeanContextChild
  */
  protected BeanContextChildSupport bccs;

  public final void setBeanContext(BeanContext bc)
    throws PropertyVetoException
  {
    bccs.setBeanContext(bc);
  }


  public final BeanContext getBeanContext()
  {
    return bccs.getBeanContext();
  }



  /** Add a PropertyChangeListener which will be invoked when
      any property has changed.


      <p>FIXME: The implementation does not really work yet.
      This is due to a missing feature in
      java.beans.beancontextchild.BeanContextChildSupport; this
      has been filed as a feature request to Sun on May 2, 1999.

      @param listener      The PropertyChangeListener to be added
  */
  public void addPropertyChangeListener (PropertyChangeListener listener)
  {
    bccs.addPropertyChangeListener (null, listener);
  }



  /** Add a PropertyChangeListener for a specific property.
      The listener will be invoked only when that specific property
      has changed.


      @param propertyName  The name of the property to listen on.
      @param listener      The PropertyChangeListener to be added
  */
  public void addPropertyChangeListener(
    String propertyName, PropertyChangeListener listener)
  {
    bccs.addPropertyChangeListener(propertyName, listener);
  }



  /** Removes a property change listener from this bean child. */
  public void removePropertyChangeListener(
    String propertyName, PropertyChangeListener listener)
  {
    bccs.removePropertyChangeListener(propertyName, listener);
  }


  /** Adds a vetoable change listener to this bean child. */
  public void addVetoableChangeListener(
    String propertyName, VetoableChangeListener listener)
  {
    bccs.addVetoableChangeListener(propertyName, listener);
  }



  /** Removes a vetoable change listener from this bean child. */
  public void removeVetoableChangeListener(
    String propertyName, VetoableChangeListener listener)
  {
    bccs.removeVetoableChangeListener(propertyName, listener);
  }


  /** Notify registered objects about a change in an boolean property.
      According to the JavaBeans proptocol, the caller
      must change the property to its new value <em>before</em>
      calling this method.
  */
  protected void firePropertyChange(String propertyName,
    boolean oldValue, boolean newValue)
  {
    bccs.firePropertyChange(propertyName,
      new Boolean(oldValue), new Boolean(newValue));
  }



  /** Notify registered objects about a change in an integer property.
      According to the JavaBeans proptocol, the caller
      must change the property to its new value <em>before</em>
      calling this method.
  */
  protected void firePropertyChange(String propertyName,
    int oldValue, int newValue)
  {
    bccs.firePropertyChange(propertyName,
      new Integer(oldValue), new Integer(newValue));
  }



  /** Notify registered objects about a change in a property.
      According to the JavaBeans proptocol, the caller
      must change the property to its new value <em>before</em>
      calling this method.
  */
  protected void firePropertyChange(String propertyName,
    Object oldValue, Object newValue)
  {
    bccs.firePropertyChange(propertyName,
      oldValue, newValue);
  }



  /** Notify registered objects about a change in a property.
      According to the JavaBeans proptocol, the caller
      must change the property to its new value <em>after</em>
      calling this method.
  */
  protected void fireVetoableChange(String propertyName,
    boolean oldValue, boolean newValue)
  throws java.beans.PropertyVetoException
  {
    bccs.fireVetoableChange(propertyName,
      new Boolean(oldValue), new Boolean(newValue));
  }



  /** Notify registered objects about a change in a property.
      According to the JavaBeans proptocol, the caller
      must change the property to its new value <em>after</em>
      calling this method.
  */
  protected void fireVetoableChange(String propertyName,
    Object oldValue, Object newValue)
  throws java.beans.PropertyVetoException
  {
    bccs.fireVetoableChange(propertyName,
      oldValue, newValue);
  }


  /** Helper function to retrieve all instances of a given class
      in a collection.
  */
  protected static <E> E[] retrieveAllInstancesOf (Iterator<?> it, Class<E> clazz)
  {
    List<E> l = new LinkedList<E>();

    while (it.hasNext()) {
      Object o = it.next();
      if (clazz.isInstance(o)) {
        l.add((E) o);
    }
    }
    return l.toArray((E[]) java.lang.reflect.Array.newInstance(clazz, l.size()));
  }



  /** Walks the bean contexts of this Element until a container is found
      that is an instance of a specific class. This is useful, for
      example, to retrieve the model which contains a table definition.

      @return The containing bean context, or <code>null</code> if none
              is found. For example, <code>getContainer(Topic.class)</code>
              would be <code>null</code> for an abstract table which is
              specified by a model.

      @exception java.lang.ClassCastException if a non-<code>null</code>
              bean context is found, but <code>klass</code> is not a
              subclass of <code>ch.interlis.Container</code>
  */
  public final Container<?> getContainer (Class<?> klass)
  {
    BeanContext bc = getBeanContext();

    while (bc != null) {
      if (klass.isInstance(bc)) {
        return (Container<?>) bc;
    }
      bc = bc.getBeanContext();
    }

    return null;
  }



  /** Returns the container of this INTERLIS element.
      If the bean context of <code>this</code> is not an instance of
      ch.interlis.Container, the result is <code>null</code>.
  */
  public final Container getContainer ()
  {
    BeanContext bc = getBeanContext();

    if (bc instanceof Container) {
        return (Container) bc;
    } else {
        return null;
    }
  }


  /** Determines the name of this Element. Elements that have no name
      return null.
  */
  public String getName ()
  {
    return null;
  }



  /** Determines a string that can be used to refer to an element
      from a specified scope.  Pass <code>null</code> to get
      a fully qualified name.
  */
  public String getScopedName(Container<?> scope)
  {

		Model enclosingModel = (Model) getContainer(Model.class);
		if (enclosingModel == null){
			return getName();
		}
		Topic enclosingTopic = (Topic) getContainer(Topic.class);
		Viewable enclosingClass = (Viewable) getContainer(Viewable.class);

		if(scope==null){
			return getContainer().getScopedName()+"."+getName();
		}

		Model scopeModel = (Model) scope.getContainerOrSame(Model.class);
		Topic scopeTopic = (Topic) scope.getContainerOrSame(Topic.class);
		Viewable scopeClass= (Viewable) scope.getContainerOrSame(Viewable.class);

		// is scope a class?
		if(scopeClass!=null){
			// same class?
			if(scopeClass==enclosingClass){
				// attribute in same class
				return getName();
			}
		}
		// is scope a topic?
		if(scopeTopic!=null){
			// same topic?
			if(scopeTopic==enclosingTopic){
				// is this a class?
				if(enclosingClass==null){
					// class in same topic
					return getName();
				}
				// if this is an attribute and scope not a class?
				if(scopeClass==null){
					return enclosingClass.getName()+"."+getName();
				}
			}
		}
		// is scope a topic?
		if(scopeModel!=null){
			// same model and a topic?
			if(scopeModel==enclosingModel && enclosingTopic==null){
				// topic in same model
				return getName();
			}
		}
		return getContainer().getScopedName()+"."+getName();
  }
  
  public String getScopedName ()
  {
    return getScopedName(null);
  }



  /** Throws an exception if a name is not a valid Interlis name.
  */
  /*
      FIXME: Improper Unicode Support; should use Java charset
             converters to convert to ISO 8859-1. Otherwise, a
             string such as A + combining dieresis might not be
             acceptable, although it should be. Currently, no
             real checking is done anyway, so this code needs
             overhaul.

             Low priority because parser does not use Unicode;
             all chars are in ISO 8859-1.
  */
  protected static final void checkNameSanity (String s, boolean emptyStringIsOK)
    throws IllegalArgumentException
  {
    if (s == null) {
        throw new IllegalArgumentException();
    }


    int len = s.length();
    if (!emptyStringIsOK && (len == 0)) {
        throw new IllegalArgumentException();
    }

    for (int i = 0; i < len; i++)
    {
      char curChar = s.charAt (i);

      if (curChar == ' ') {
        throw new IllegalArgumentException (formatMessage ("err_name_withSpaces", s));
    }
    }
  }



  /** Helper function for several setName methods.

      @param proposedName the proposed name for this Element

      @param klass        the class to check uniqueness with. Typical
                          parameters are View.class, Table.class etc.;
                          note that you should not pass "this.getClass()",
                          otherwise you would only check uniqueness
                          against instances of the very same class.
                          For example, a JoinView would want to check
                          name uniqueness against Viewable, not against
                          JoinView.

      @param acceptable   If you would accept conflicts with one specific
                          elements, pass that element here. This is used
                          for Interlis items that are declared as EXTENDED.
                          Pass null if the element has not been declared
                          as EXTENDED.

      @param errorKey     The key for the error message, for example
                          "err_duplicateViewName".

      @exception java.lang.IllegalArgumentException if there is
                 a name conflict.
  */
  protected void checkNameUniqueness (String proposedName, Class klass,
    Element acceptable, String errorKey)
  {
    Container container;
    Element   conflicting;

    container = getContainer(Container.class);


    /* If "this" is currently not embedded in a container,
       name conflicts can not occur. */
    if (container == null) {
        return;
    }

    conflicting = container.getRealElement(klass, proposedName);
    if ((conflicting == null) || (conflicting == this)
        || (conflicting == acceptable)) {
        return;
    }

    throw new IllegalArgumentException(formatMessage(
      errorKey, proposedName,
      container.toString(), conflicting.toString()));
  }


  /** Determines a String that can be used to display
      an error message for a specified INTERLIS Element.
  */
  public static final String makeErrorName (Element elt)
  {
    if (elt == null) {
        return rsrc.getString ("err_dumpErrorName");
    } else {
        return elt.toString();
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
  public void checkIntegrity (List<Ili2cSemanticException> errs)
    throws java.lang.IllegalStateException
  {
  }



  /* Finds the most embedded container that contains both
     this and elt. If there is no such container, because
     this and elt belong to entirely different containment
     hierarchies, the result is <code>null</code>. If
     elt is <code>null</code>, the result is <code>null</code>.
  */
  public Container getCommonContainer (Element elt)
  {
    if (elt == null) {
        return null;
    }

    Container myCtr, eltCtr, commonContainer;

    myCtr = this.getContainer();

    while (myCtr != null)
    {
      eltCtr = elt.getContainer();

      while (eltCtr != null)
      {
        if (eltCtr == myCtr) {
            return myCtr;
        }

        eltCtr = eltCtr.getContainer ();
      }

      myCtr = myCtr.getContainer ();
    }

    return null;
  }


  /** Determines whether this Element has been declared before another Element
      in the INTERLIS description.  It is valid to call this method even
      if the description has been edited, for example with a graphical
      editor -- in that case, the order of a generated INTERLIS description
      matters. The method implementation works as follows:

      <ol><li>Look for the container that contains both <code>this</code>
              and <code>elt</code>, be it directly or indirectly. If there
              is no common container, return <code>false</code>.</li>

          <li>Iterate through the directly contained elements in the
              common container. For each directly contained element <em>E</em>:


              <ul><li>If <em>E</em> is equal to <code>this</code>, or if <em>E</em>
                      contains <code>this</code> (directly or indirectly),
                      <code>this</code> comes before <code>elt</code>
                      in an INTERLIS description. Return <code>true</code>.</li>


                  <li>If <em>E</em> is equal to <code>elt</code>, or if <em>E</em>
                      contains <code>elt</code> (directly or indirectly),
                      <code>elt</code> comes before <code>this</code>
                      in an INTERLIS description. Return <code>false</code>.</li>
              </ul>

          <li>One of the two above conditions always holds in case there
              exists a common container -- either <code>elt</code> or
              <code>this</code> comes first.</ol>
      </old>

  */
  public boolean isDeclaredBefore (Element elt)
  {
    /* Find the common container. */
    Container commonContainer = getCommonContainer (elt);

    /* If "this" is in an entirely different containment hierarchy
       as elt, or if elt is null, "this" is not declared before elt. */
    if (commonContainer == null) {
        return false;
    }

    Iterator<Element> iter = commonContainer.iterator ();
    while (iter.hasNext())
    {
      Element curElt = iter.next();

      /* If "this" or some container that contains "this" comes first,
         "this" is declared before "elt".
      */
      if ((curElt == this)
          || ((curElt instanceof Container)
              && ((Container) curElt).containsIndirectly (this))) {
        return true;
    }

      /* If "elt" or some container that contains "elt" comes first,
         "this" is not declared before "elt".
      */
      if ((curElt == elt)
          || ((curElt instanceof Container)
              && ((Container) curElt).containsIndirectly (elt))) {
        return false;
    }
    }

    /* If we reach this point, something is wrong with getCommonContainer. */
    throw new IllegalStateException ("Internal error; check ch.interlis.Element.isDeclaredBefore");
  }


  /** Determines whether this Element can access another Element.

      <code>this</code> is being allowed to access <code>other</code>
      if and only if:

      <ul><li><code>other</code> is the same as <code>this</code>;</li>
          <li>or
              <ul><li><code>other.isDeclaredBefore(this)</code> returns <code>true</code>,</li>
              <li>and
                  <ul><li>the model of <code>this</code> imports the model
                          of <code>other</code>,</li>
                      <li>or both <code>this</code> and <code>other</code>
                          are in the same model.</li>
                  </ul></li>
              </ul></li>
      </ul>

      @see #isDeclaredBefore(ch.interlis.Element)


      @exception java.lang.IllegalArgumentException
                 if <code>other</code> is <code>null</code>
  */
  public boolean canAccess (Element other)
  {
    Model myModel, otherModel;

    if (other == null) {
        throw new IllegalArgumentException ();
    }

    if (this == other) {
        return true;
    }

    if (!other.isDeclaredBefore(this)) {
        return false;
    }

    if (this instanceof Model) {
        myModel = (Model) this;
    } else {
        myModel = (Model) this.getContainer (Model.class);
    }


    if (other instanceof Model) {
        otherModel = (Model) other;
    } else {
        otherModel = (Model) other.getContainer (Model.class);
    }

    /* This should not happen with parsed Elements, but the answer
       in this unlikely case is pretty obvious. */
    if ((myModel == null) || (otherModel == null)) {
        return false;
    }

    return (myModel == otherModel)
      || myModel.isImporting (otherModel);
  }


  /** Determines the set of elements that can occur as polymorphic
      equivalents for some Extendable, when only those Elements are
      taken into account that this Element is allowed to access.
      The reflexive transitive closure of the extension is calculated
      and filtered to contain only those which this Element can access.

      @return A new Set that belongs to the caller; the caller
              is thus free to modify it according to its needs.
              Changes in the result will not have any effect
              on the extension hierarchy or accessibility
              conditions.


      @see #canAccess(ch.interlis.Element)
      @see ch.interlis.Extendable.getExtensions()
  */
  public Set<Extendable> getAccessiblePolymorphicEquivalents (Extendable forExtendable)
  {
    Set<Extendable> result;

    if (forExtendable == null) {
        return new HashSet<Extendable>();
    }

    /* calculate the transitive closure of extending relation */
    result = forExtendable.getExtensions();


    /* make it the reflexive transitive closure */
    result.add (forExtendable);

    Iterator iter = result.iterator();
    while (iter.hasNext())
    {
      Element elt = (Element) iter.next();

      if (!canAccess (elt)) {
        iter.remove();
    }
    }

    return result;
  }


  /** Determines whether or not this Element is structurally equivalent with
      another Element. If the two are not equivalent, an ErrorListener gets
      notified. This is being used for the Interlis compiler's model equivalence
      checking facility.

      @return <code>true</code> if <code>this</code> and <code>with</code> are
              structurally equivalent, <code>false</code> otherwise.
  */
  public boolean checkStructuralEquivalence (Element with)
  {
  	if(isAlias()){
  		return getReal().checkStructuralEquivalence (with);
  	}else{

	    /* This can only occur due to parsing errors. Do not report additional
	       errors in that case.
	    */
	    if (with == null) {
            return false;
        }

	    /* Class must be the same for structural equivalence. */
	    if (this.getClass() != with.getClass())
	    {
	      EhiLogger.logError(formatMessage ("err_diff_mismatchInClass", this.toString(), with.toString()));
	      return false;
	    }

	    return true;
  	}
  }


  protected boolean checkStructuralEquivalenceOfArrays (
    Element with, Element[] myArray, Element[] withArray,
    String numElementMismatchMessageKey)
  {
    /* This can only occur due to parsing errors. Do not report additional
       errors in that case.
    */
    if ((myArray == null) || (withArray == null) || (with == null)) {
        return false;
    }

    if (myArray.length != withArray.length)
    {
      EhiLogger.logError(formatMessage (numElementMismatchMessageKey, this.toString(), with.toString()));
      return false;
    }

    boolean fine = true;
    for (int i = 0; i < myArray.length; i++)
    {
      if ((myArray[i] == null) != (withArray[i] == null)) {
        fine = false;
    }

      if (myArray[i] != null)
      {
        if (!myArray[i].checkStructuralEquivalence (withArray[i])) {
            fine = false;
        }
      }
    }
    return fine;
  }
  	//
  	// implementation of ElementAlias
  	//
  	/** implement chain of alias elements
  	 */
  	private Element nextElement;
	public Element getNext(){
		return nextElement;
	}
	protected void setNext(Element nextElement){
		this.nextElement=nextElement;
	}
	public Element getReal(){
		Element real=this;
		while(real.getNext()!=null){
			real=real.getNext();
		}
		return real;
	}
	private String alias;
	public String getAlias(){
		return alias;
	}
	public void setAlias(String alias){
		this.alias=alias;
	}
	public boolean isAlias(){
		return alias!=null;
	}
	public ElementAlias createAlias(String alias){
		return null;
	}


public String getDocumentation() {
	return documentation;
}
public void setDocumentation(String string) {
	documentation = string;
}

	/**
	 * @return true if definition incomplete due to errors
	 */
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean b) {
		dirty = b;
	}


	public int getSourceLine() {
		return sourceLine;
	}


	public void setSourceLine(int sourceLine) {
		this.sourceLine = sourceLine;
	}


	public ch.ehi.basics.settings.Settings getMetaValues() {
		if(metaValues==null){
			metaValues=new ch.ehi.basics.settings.Settings();
		}
		return metaValues;
	}


	public void setMetaValues(ch.ehi.basics.settings.Settings metaValues) {
		this.metaValues = metaValues;
	}
	public String getMetaValue(String name)
	{
		return getMetaValues().getValue(name);
	}
	public void setMetaValue(String name,String value)
	{
		getMetaValues().setValue(name,value);
	}
	public Object getTransientMetaValue(String name)
	{
		return getMetaValues().getTransientObject(name);
	}
	public void setTransientMetaValue(String name,Object value)
	{
		getMetaValues().setTransientObject(name,value);
	}
	public int getDefidx() {
		return defidx;
	}
  	private Element baseLanguageElement=null;
  	public Element getTranslationOf()
  	{
  		return baseLanguageElement;
  	}
  	public Element getTranslationOfOrSame()
  	{
  		if(baseLanguageElement==null){
  			return this;
  		}
  		return baseLanguageElement.getTranslationOfOrSame();
  	}
  	protected void linkTranslationOf(Element baseElement)
  	{
  	    if(baseElement==this) {
  	        throw new IllegalArgumentException("base element must be different from translated element "+this.getScopedName());
  	    }
  		this.baseLanguageElement=baseElement;
  	}
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
    }

}
