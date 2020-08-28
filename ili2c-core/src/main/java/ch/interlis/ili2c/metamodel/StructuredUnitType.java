
package ch.interlis.ili2c.metamodel;


/** A base type designating data in a structured unit, for example
    <em>time of the day</em>.
*/
public class StructuredUnitType extends NumericalType
{
  protected Constant.Structured minimum;
  protected Constant.Structured maximum;

  /** @exception java.lang.IllegalArgumentException if either
                 <code>minimum</code> or <code>maximum</code>
                 is <code>null</code>.

      @exception java.lang.IllegalArgumentException if
                 <code>minimum</code> is greater than <code>maximum</code>.
  */
  public StructuredUnitType (Constant.Structured minimum, Constant.Structured maximum)
  {
    this.minimum = minimum;
    this.maximum = maximum;

    if ((minimum == null) || (maximum == null))
      throw new IllegalArgumentException (rsrc.getString (
        "err_nullNotAcceptable"));
  }


  public Constant.Structured getMinimum()
  {
    return minimum;
  }


  public Constant.Structured getMaximum()
  {
    return maximum;
  }


  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append (minimum);
    sb.append ("..");
    sb.append (maximum);
    sb.append (" [");
    sb.append (unit == null ? "null" : unit.getScopedName (null));
    sb.append ("]");

    return sb.toString ();
  }


  /** An abstract type is one that does describe sufficiently
      the set of possible values. A structured unit type is abstract
      if its unit is abstract. However, abstract units are not
      accepted by setUnit(), so, the result is never true.

      @return Whether or not this type is abstract.
  */
  @Override
  public boolean isAbstract (StringBuilder err)
  {
    Unit unit = getUnit(); /* considering inherited units */
    if ((unit != null) && (unit.isAbstract())){
    	err.append("UnitDef is abstract");
      return true;
    }

    return false;
  }


  public void setUnit (Unit unit)
    throws java.beans.PropertyVetoException
  {
    Unit oldValue = this.unit;
    Unit newValue = unit;

    if (oldValue == newValue)
      return;

    if ((newValue != null) && newValue.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_structuredUnitType_abstractUnit",
        newValue.toString()));

    fireVetoableChange("unit", oldValue, newValue);
    this.unit = newValue;
    firePropertyChange("unit", oldValue, newValue);
  }


  public Unit getUnit()
  {
    return unit;
  }


  public void setReferenceSystem (RefSystemRef referenceSystem)
    throws java.beans.PropertyVetoException
  {
    RefSystemRef oldValue = this.referenceSystem;
    RefSystemRef newValue = referenceSystem;

    if (oldValue == newValue)
      return;

    fireVetoableChange ("referenceSystem", oldValue, newValue);
    this.referenceSystem = newValue;
    firePropertyChange ("referenceSystem", oldValue, newValue);
  }


  public RefSystemRef getReferenceSystem ()
  {
    return referenceSystem;
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
    StructuredUnitType   general;

    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    if (!(wantToExtend instanceof StructuredUnitType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_structuredUnitType_extOther"));

    general = (StructuredUnitType) wantToExtend;
    if (this.isAbstract() && !general.isAbstract())
      throw new IllegalArgumentException (rsrc.getString (
        "err_structuredUnitType_abstractExtConcrete"));

    if (this.unit != null)
    {
      Unit generalUnit = general.getUnit();

      if ((generalUnit != null)
          && !this.unit.isExtendingIndirectly (generalUnit))
      {
        throw new IllegalArgumentException (formatMessage (
          "err_structuredUnitType_unitNotExtBaseUnit",
          this.unit.toString(),
          generalUnit.toString()));
      }
    }
    checkCardinalityExtension(wantToExtend);
  }


    public StructuredUnitType clone() {
        return (StructuredUnitType) super.clone();
    }

}
