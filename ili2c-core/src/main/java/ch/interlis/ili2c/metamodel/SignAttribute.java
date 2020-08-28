/*****************************************************************************
 *
 * SignAttribute.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.*;

/** A sign attribute describes the evaluation and assignment of parameters of
*   a SIGN class. A sign attribute is part of a graphic definition.
*/
public class SignAttribute extends ExtendableContainer<Element>
{
  protected String            name = "";
  protected Table             generating = null;

  protected Collection<Element> createElements(){
      return new ArrayList<Element>();
  }
  @Override
  public void checkIntegrity(List<Ili2cSemanticException> errs){
  }
  /** Sets the value of the <code>name</code> property.
      Sign attributes are identified and used by specifying
      their name.

      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this sign attribute.

      @exception java.lang.IllegalArgumentException if
                 <code>name</code> is <code>null</code>,
                 an empty String, too long or does otherwise
                 not conform to the syntax of acceptable
                 INTERLIS names.

      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another sign attribute. The
                 only acceptable conflict is with the attribute
                 that this attribute directly extends.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName(String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    /* Make sure that the new name is not null, empty,
       too long, etc. */
    checkNameSanity(newValue, /* empty ok? */ false);

    /* Make sure that the new name does not conflict
       with the name of another SignAttribute, except the
       one that this object is extending directly.
    */
    checkNameUniqueness (newValue, SignAttribute.class,
      getRealExtending(),
      "err_signAttr_duplicateName");

    /* JavaBeans requires that the value be changed between
       firing VetoableChangeEvent and PropertyChangeEvent
       objects. */
    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }


  /** Returns the value of the <code>name</code> property
      which indicates the name of this sign attribute without
      any scope prefixes.

      @see #setName(java.lang.String)
      @see #getScopedName(ch.interlis.Container)
  */
  public String getName ()
  {
    return name;
  }

  public String toString ()
  {
    return getScopedName (null);
  }

  public Table getGenerating ()
  {
    return generating;
  }

	/** defines the SIGN class for this sign attribute. The SIGN class
	*   defines the set of parameters to be assigned by this SignAttributes
	*   SignInstructions.
	*/
  public void setGenerating (Table generating)
    throws java.beans.PropertyVetoException
  {
    Table oldValue = this.generating;
    Table newValue = generating;
    TransferDescription td;

    if (oldValue == newValue)
      return;

    td = (TransferDescription) getContainer (TransferDescription.class);
    if ((newValue != null) && !newValue.isExtending (td.INTERLIS.SIGN))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_signAttr_ofNonSignTab",
        newValue.toString(), td.INTERLIS.SIGN.toString()));
    }

    fireVetoableChange ("generating", oldValue, newValue);
    this.generating = newValue;
    firePropertyChange ("generating", oldValue, newValue);
  }


  public SignInstruction[] getInstructions ()
  {
    return (SignInstruction[]) toArray(new SignInstruction[size()]);
  }

	/** defines the set of SignInstructions to be used to assign values to
	*   parameters.
	*/
  public void setInstructions (SignInstruction[] instructions)
    throws java.beans.PropertyVetoException
  {
    clear();
    for(SignInstruction i: instructions) {
        add(i);
    }
  }
}
