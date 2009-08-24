/*****************************************************************************
 *
 * Type.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.*;


/** Type is an abstract class which serves as a common
    superclass for all Interlis Types (= domain definitions).

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public abstract class Type
  extends AbstractLeafElement
  implements Cloneable
{
  protected Type       extending = null;
  protected Set        extendedBy = new HashSet(2);
  protected boolean    mandatory = false;


  protected Type()
  {
  }



  public Object clone()
    throws java.lang.CloneNotSupportedException
  {
    Type cloned = (Type) super.clone();

    cloned.extendedBy = new HashSet (2);
    if (cloned.extending != null)
    {
      cloned.extending.extendedBy.add (cloned);
    }

    return cloned;
  }


  /** An abstract type is one that does describe sufficiently
      the set of possible values.

      @return Whether or not this type is abstract.
  */
  public boolean isAbstract ()
  {
    return false;
  }


  public boolean isMandatory ()
  {
    return mandatory;
  }



  public boolean isMandatoryConsideringAliases ()
  {
    return isMandatory();
  }



  /** @exception IllegalArgumentException If <code>mand</code> is
  				 <code>false</code> and Type extends
  				 a mandatory Type.
  */
  public void setMandatory (boolean mand)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.mandatory;
    boolean newValue = mand;

    fireVetoableChange("mandatory", oldValue, newValue);
    mandatory = mand;
    firePropertyChange("mandatory", oldValue, newValue);
  }

  public Element getExtending()
  {
  	return extending;
  }

  public void setExtending (Type extending)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.extending;
    Type newValue = extending;

    /* Check whether there is anything to do. The JavaBeans
       specification strongly recommends this check to avoid
       certain infinite loops which might occur otherwise.
    */
    if (oldValue == newValue)
      return;

    /* Ensure that the extension graph will be acyclic. */
    if ((newValue != null) && (newValue.isExtendingIndirectly (this)))
      throw new IllegalArgumentException(formatMessage(
        "err_cyclicExtension", this.toString(), newValue.toString()));


    /* Check that the argument is valid. */
    checkTypeExtension (newValue);



    /* Give interested parties a chance to oppose to the change. */
	fireVetoableChange ("extending", oldValue, newValue);

    /* Perform the change. */
    if (oldValue != null)
      oldValue.extendedBy.remove (this);
    this.extending = newValue;
    if (newValue != null)
      newValue.extendedBy.add (this);


    /* Inform interested parties about the change. */
    firePropertyChange ("extending", oldValue, newValue);
  }



  /** @return whether or not <code>typ</code> is extending
              <code>dd</code>
  */
  boolean isExtendingIndirectly (Type typ)
  {
    for (Type parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == typ)
        return true;
    }
    return false;
  }



  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  
  abstract void checkTypeExtension (Type wantToExtend);
  /*
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
  } 
  */



  /** If this type is a TypeAlias, resolve the chain of aliases
      until a non-alias type is found. If this type is not an alias,
      this type is returned directly.

      @see TypeAlias#resolveAliases()
  */
  public Type resolveAliases()
  {
    return this;
  }

  public static final Type findReal (Type potentialAlias)
  {
    if (potentialAlias instanceof TypeAlias)
      return findReal (potentialAlias.resolveAliases ());

    return potentialAlias;
  }


}
