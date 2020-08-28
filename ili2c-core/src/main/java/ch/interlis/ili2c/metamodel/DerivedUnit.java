/*****************************************************************************
 *
 * DerivedUnit.java
 * ----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  April 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** DerivedUnit is an abstract class which serves as a common
    superclass for all Interlis derived unit definitions.

    @author    Sascha Brawer, sb@adasys.ch
*/
public abstract class DerivedUnit extends Unit
{

  /** Constructs a new derived unit.
  */
  protected DerivedUnit()
  {
  }


  protected void checkExtending(Unit newValue)
  {

    /* Make sure there won't be cyclic graphs. */
    if ((newValue != null) && (newValue.isDependentOn(this))) {
      throw new IllegalArgumentException("The unit \""
        + this.getName() + "\" can not be based on \""
        + newValue.getName() +
        ", because the latter depends on the former.");
    }

    if ((newValue != null) && newValue.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_derivingAbstractUnit", newValue.toString()));

  }


  public void setAbstract (boolean _abst)
    throws java.beans.PropertyVetoException
  {
    if (_abst)
      throw new IllegalArgumentException (formatMessage (
        "err_abstractDerivedUnit", this.toString()));

    super.setAbstract (false);
  }


  public boolean isDependentOn(Element e)
  {
   if ((extending != null) && extending.isDependentOn(e))
     return true;

   return super.isDependentOn(e);
  }
}
