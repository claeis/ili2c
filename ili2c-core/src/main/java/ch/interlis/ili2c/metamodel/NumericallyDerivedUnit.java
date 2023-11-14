/*****************************************************************************
 *
 * NumericallyDerivedUnit.java
 * ---------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  April 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** A NumericallyDerivedUnit defines a unit by multiplication with
    or division by a constant numeric factor, for example
    <code>km = m * 1000</code>.

    @version   April 12, 1999
    @author    Sascha Brawer, sb@adasys.ch
*/
public class NumericallyDerivedUnit extends DerivedUnit
{
  /** A factor of a numerically derived unit, for example
      the <em>by thousand</em> part in <em>m * 1000</em>.
  */
  public static class Factor
  {
    protected char conversionOperator;
    protected PrecisionDecimal conversionFactor;

    /** Constructs a new factor given a conversion operator
        and a conversion factor.

        @param conversionOperator the conversion operator;
               must be one of '*' or '/'.

        @param conversionFactor the conversion factor; must not
               be zero.
    */
    public Factor (char conversionOperator, PrecisionDecimal conversionFactor)
    {
      this.conversionOperator = conversionOperator;
      this.conversionFactor = conversionFactor;

      if (conversionOperator == '*')
      {
        if (conversionFactor.doubleValue() == 0.0)
          throw new IllegalArgumentException (
            ch.interlis.ili2c.metamodel.Element.rsrc.getString ("err_zeroUnitConversionFactor"));
      }
      else if (conversionOperator == '/')
      {
        if (conversionFactor.doubleValue() == 0.0)
          throw new IllegalArgumentException (
            ch.interlis.ili2c.metamodel.Element.rsrc.getString ("err_zeroUnitDivisor"));
      }
      else
        throw new IllegalArgumentException (
          ch.interlis.ili2c.metamodel.Element.rsrc.getString ("err_illegalConversionOperator"));
    }


    public PrecisionDecimal getConversionFactor()
    {
      return conversionFactor;
    }

    /** @return either <code>'*'</code> or <code>'/'</code>. */
    public char getConversionOperator()
    {
      return conversionOperator;
    }
  };


  /** Constructs a new numerically derived unit. */
  public NumericallyDerivedUnit()
  {
  }


  protected Factor[] conversionFactors = new Factor[0];


  /** Specify how to convert a value of this unit into a value of the
      underlying base unit. For example, 1 km is 1000 m, so the unit
      <i>km</i> is numerically derived from the base unit <i>m</i>
      by multiplication with 1000.

      <p>In JavaBeans terminology, the <code>conversionFactors</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setConversionFactors (Factor[] conversionFactors)
    throws java.beans.PropertyVetoException
  {
    Factor[] oldValue = this.conversionFactors;
    Factor[] newValue = conversionFactors;

    if (oldValue == newValue)
      return;

    /* Set value and inform interested listeners. */
    fireVetoableChange("conversionFactors", oldValue, newValue);
    this.conversionFactors = conversionFactors;
    firePropertyChange("conversionFactors", oldValue, newValue);
  }


  /** Returns the individual factors in this numerically derived
      unit.
  */
  public Factor[] getConversionFactors()
  {
    return conversionFactors;
  }
  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      NumericallyDerivedUnit baseElement=(NumericallyDerivedUnit)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      Ili2cSemanticException err=null;
      if(getConversionFactors().length!=baseElement.getConversionFactors().length) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
      }else {
          for(int i=0;i<getConversionFactors().length;i++) {
              if(i>0) {
                 if(conversionFactors[i].conversionOperator!=baseElement.conversionFactors[i].conversionOperator) {
                     errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
                 }
              }
              if(!PrecisionDecimal.equals(conversionFactors[i].conversionFactor,baseElement.conversionFactors[i].conversionFactor)) {
                  errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
              }
          }
      }
  }  
}
