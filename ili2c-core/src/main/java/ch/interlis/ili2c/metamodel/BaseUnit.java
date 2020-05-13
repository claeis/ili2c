/*****************************************************************************
 *
 * BaseUnit.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  April 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** Base units form the base for derived and composed units;
    they do not depend themselves on others. Typical base units
    include the SI base units, such as kilograms or meters.
    These are defined as part of the predefined model INTERLIS.
    In addition, the abstract "dimensions", such as length
    or mass, are base units, too.

    @version   April 12, 1999
    @author    Sascha Brawer, sb@adasys.ch
*/
public class BaseUnit extends Unit
{
  /** Constructs a new base unit with an empty name. */
  public BaseUnit()
  {
  }


  /** Causes this base unit to extend another base unit.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param extending  The new unit being extended, or
                        <code>null</code> if this unit is
                        going to be independent of other units.

      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is not a base units.
                 Base units can only extend other base units.

      @exception java.lang.IllegalArgumentException if
                 the resulting extension graph would contain
                 cycles. For instance, if <code>A</code> extends
                 <code>B</code> and <code>B</code> extends
                 <code>C</code>, the call <code>C.setExtending(A)</code>
                 would throw an exception.

      @exception java.lang.ClassCastException if <code>extending</code>
                 is neither <code>null</code> nor an instance of
                 the class <code>Unit</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Unit extending)
    throws java.beans.PropertyVetoException
  {
    if ((extending != null) && (!(extending instanceof BaseUnit))) {
        throw new IllegalArgumentException (formatMessage (
            "err_baseUnitExtendingNonBase",
            this.toString(),
            extending.toString()));
    }

    super.setExtending (extending);
  }
}