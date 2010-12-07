/*****************************************************************************
 *
 * NumericType.java
 * ----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.math.BigDecimal;


/** A numeric type with a minimum and maximum value,
    an optional unit and reference system and optional
    specifications about rotation and circularity. */
public class NumericType extends NumericalType
{
  protected PrecisionDecimal minimum;
  protected PrecisionDecimal maximum;
  
  
  
  /** Constructs a new numeric type. 
  */
  public NumericType()
  {
    minimum = maximum = null;
  }
  
  
  /** Returns the minimally acceptable value for an instance
      of this numeric type.
  */
  public PrecisionDecimal getMinimum()
  {
    return minimum;
  }
  
  /** Returns the maximally acceptable value for an instance
      of this numeric type.
  */
  public PrecisionDecimal getMaximum()
  {
    return maximum;
  }
  
  
  /** Constructs a new NumericType given its minimum and maximum.
  
      @exception java.lang.IllegalArgumentException if either
                 <code>minimum</code> or <code>maximum</code>
                 is <code>null</code>.
                 
      @exception java.lang.IllegalArgumentException if
                 <code>minimum</code> is greater than <code>maximum</code>.
  */
  public NumericType (PrecisionDecimal minimum, PrecisionDecimal maximum)
  {
    if ((minimum == null) || (maximum == null))
      throw new Ili2cSemanticException (rsrc.getString (
        "err_nullNotAcceptable"));
      
    if (minimum.compareTo(maximum) == +1)
    {
      throw new Ili2cSemanticException(
        rsrc.getString ("err_numericType_minGreaterMax"));
    }
    
    /* Check whether precision is the same. */
    if (minimum.getAccuracy() != maximum.getAccuracy())
      throw new Ili2cSemanticException (rsrc.getString (
        "err_numericType_precisionMismatch"));
    
    this.minimum = minimum;
    this.maximum = maximum;
  }
  
  
  /** Returns a string that designates this numeric type.
      If minimum and maximum have not been specified, the
      result is "NUMERIC"; otherwise, the minimum and
      maximum are separated by two dots, as required
      by the INTERLIS syntax.
  */
  public String toString()
  {
    if ((minimum == null) || (maximum == null))
      return "NUMERIC";
    else
      return minimum.toString() + ".." + maximum.toString();
  }
  

  /** An abstract type is one that does describe sufficiently
      the set of possible values. A numeric type is abstract
      if its minimum and maximum value are not specified.
      
      @return Whether or not this type is abstract.
  */
  public boolean isAbstract ()
  {
    if ((minimum == null) || (maximum == null))
      return true;
    
    Unit unit = getUnit(); /* considering inherited units */
    if ((unit != null) && (unit.isAbstract()))
      return true;
    
    return false;
  }
  
  
    
  public void setUnit (Unit unit)
    throws java.beans.PropertyVetoException
  {
    Unit oldValue = this.unit;
    Unit newValue = unit;
    
    if (oldValue == newValue)
      return;
    
    if ((newValue != null) && (this.minimum != null) && newValue.isAbstract())
      throw new Ili2cSemanticException (formatMessage (
        "err_numericType_concreteWithAbstractUnit",
        newValue.toString()));

    fireVetoableChange("unit", oldValue, newValue);
    this.unit = newValue;
    firePropertyChange("unit", oldValue, newValue);
  }
  
  
  public Unit getUnit()
  {
    Type realExt;
    
    if (extending != null)
      realExt = extending.resolveAliases();
    else
      realExt = null;
    
    if ((unit == null) && (realExt instanceof NumericType))
    {
      return ((NumericType) realExt).getUnit();
    }

    return unit;
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
    NumericType   general;
    
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    
    if (!(wantToExtend instanceof NumericType))
      throw new Ili2cSemanticException (rsrc.getString (
        "err_numericType_ExtOther"));

    general = (NumericType) wantToExtend;
    if (this.isAbstract() && !general.isAbstract())
      throw new Ili2cSemanticException (rsrc.getString (
        "err_numericType_abstractExtConcrete"));
    
    if ((minimum != null) && (maximum != null)
        && (general.minimum != null) && (general.maximum != null))
    {
      BigDecimal min_rounded, max_rounded;
      
      min_rounded = new BigDecimal(minimum.toString()).setScale (general.minimum.getExponent(),
                                      BigDecimal.ROUND_HALF_UP);
      max_rounded = new BigDecimal(maximum.toString()).setScale (general.maximum.getExponent(),
                                      BigDecimal.ROUND_HALF_UP);
      
      if (min_rounded.compareTo (general.minimum) == -1)
        throw new Ili2cSemanticException (formatMessage (
          "err_numericType_minLessInheritedMin",
          Integer.toString (general.minimum.getExponent()),
          minimum.toString(),
          min_rounded.toString(),
          general.minimum.toString()));

      if (max_rounded.compareTo (general.maximum) == +1)
        throw new Ili2cSemanticException (formatMessage (
          "err_numericType_maxGreaterInheritedMax",
          Integer.toString (general.maximum.getExponent()),
          maximum.toString(),
          max_rounded.toString(),
          general.maximum.toString()));
    }
    
    if (this.unit != null)
    {
      Unit generalUnit = general.getUnit();
      
      if (!general.isAbstract() && (generalUnit == null))
        throw new Ili2cSemanticException (rsrc.getString (
          "err_numericType_withUnitExtWithoutUnit"));
      
      if ((generalUnit != null)
          && generalUnit.isAbstract()
          && !this.unit.isExtendingIndirectly(generalUnit))
      {
        throw new Ili2cSemanticException (formatMessage (
          "err_numericType_unitNotExtAbstractBaseUnit",
          this.unit.toString(),
          generalUnit.toString()));
      }
      
      if ((generalUnit != null)
          && !generalUnit.isAbstract()
          && (generalUnit != this.unit))
      {
        throw new Ili2cSemanticException (formatMessage (
          "err_numericType_unitExtConcreteBaseUnit",
          this.unit.toString(),
          generalUnit.toString()));
      }
    }

    String errorString = null;
    switch (this.getRotation())
    {
      case ROTATION_NONE:
        switch (general.getRotation())
        {
        case ROTATION_CLOCKWISE:
          errorString = "err_numericType_noneExtCw";
          break;
        
        case ROTATION_COUNTERCLOCKWISE:
          errorString = "err_numericType_noneExtCcw";
          break;
        }
        break;
      
      case ROTATION_CLOCKWISE:
        switch (general.getRotation())
        {
        case ROTATION_NONE:
          errorString = "err_numericType_cwExtNone";
          break;
        
        case ROTATION_COUNTERCLOCKWISE:
          errorString = "err_numericType_cwExtCcw";
          break;
        }
        break;


      case ROTATION_COUNTERCLOCKWISE:
        switch (general.getRotation())
        {
        case ROTATION_NONE:
          errorString = "err_numericType_ccwExtNone";
          break;
        
        case ROTATION_CLOCKWISE:
          errorString = "err_numericType_ccwExtCw";
          break;
        }
        break;
    } /* switch (this.getRotation ()) */
    
    if (errorString != null)
      throw new Ili2cSemanticException (rsrc.getString (errorString));
  }
}
