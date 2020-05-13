/*****************************************************************************
 *
 * TypeAlias.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** Type aliases refer to another type.

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public class TypeAlias extends Type
{
  public TypeAlias ()
  {
  }

  public String toString()
  {
    if (aliasing == null)
      return "<<<Alias to nothing>>>";
    else
      return aliasing.toString();
  }


  protected Domain aliasing;


  /** An abstract type is one that does describe sufficiently
      the set of possible values. A type alias is abstract
      if the aliased domain is abstract. Otherwise, it
      is concrete.

      @return The result of <code>aliasing.isAbstract()</code>.
  */
  @Override
  public boolean isAbstract (StringBuilder err)
  {
    if (aliasing == null)
      return false;

    if (aliasing.isAbstract()){
    	err.append("DomainDef "+aliasing.getName()+" is abstract");
      return true;
    }

    Type aliasingType = aliasing.getType();
    if ((aliasingType != null) && aliasingType.isAbstract(err))
      return true;

    return false;
  }


  public boolean isMandatoryConsideringAliases ()
  {
    if (isMandatory ())
      return true;

    /* aliasing can be null in case of parsing errors
       from which the parse could recover
    */
    if (aliasing != null)
      return aliasing.getType().isMandatoryConsideringAliases ();
    else
      return false;
  }


  public Domain getAliasing ()
  {
    return aliasing;
  }


  public TypeAlias clone() {
    TypeAlias cloned = (TypeAlias) super.clone();

    if (cloned.aliasing != null)
      cloned.aliasing.aliasedBy.add(cloned);

    return cloned;
  }


  public void setAliasing (Domain aliasing)
    throws java.beans.PropertyVetoException
  {
    Domain oldValue = this.aliasing;
    Domain newValue = aliasing;

    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;


	fireVetoableChange ("aliasing", oldValue, newValue);
	if (oldValue != null)
	  oldValue.aliasedBy.remove (this);
	if (newValue != null)
	  newValue.aliasedBy.add (this);
    this.aliasing = newValue;
    firePropertyChange ("aliasing", oldValue, newValue);
  }


  public boolean isDependentOn (Element e)
  {
    if (e == aliasing)
      return true;

    if ((aliasing != null) && (aliasing.isDependentOn(e)))
      return true;

    return super.isDependentOn(e);
  }


  /** Resolve the chain of aliases until a non-alias type is found.
  */
  public Type resolveAliases()
  {
    if (aliasing == null)
      return null;

    Type t = aliasing.getType();
    if (t == null)
      return null;

    return t.resolveAliases();
  }


  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  void checkTypeExtension (Type wantToExtend)
  {
    if ((aliasing != null) && (aliasing.getType() != null))
      aliasing.getType().checkTypeExtension (wantToExtend);
  }

}
