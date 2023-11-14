/*****************************************************************************
 *
 * StructuredUnit.java
 * -------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

import java.util.*;

/** A unit that has several components, such as <em>time of the day</em> which
    can be separated in parts for <em>hour</em>, <em>minute</em> and
    <em>seconds</em>.
    
    @author    Sascha Brawer, sb@adasys.ch
*/
public class StructuredUnit extends Unit
{
  /** A component of a structured unit, such as <em>minute</em> in <em>time of
      the day</em>.
  */
  public static class Part
  {
    protected Unit unit;
    protected PrecisionDecimal min, max;
    
    public Part (Unit unit, PrecisionDecimal min, PrecisionDecimal max)
    {
      this.unit = unit;
      this.min = min;
      this.max = max;
      
      if ((min == null) || (max == null))
        throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

      if (min.compareTo (max) == +1)
        throw new IllegalArgumentException (rsrc.getString ("err_structuredUnit_minGreaterMax"));
      
      if (min.doubleValue() < 0)
        throw new IllegalArgumentException (rsrc.getString ("err_structuredUnit_negMin"));
      
      if (max.doubleValue() < 0)
        throw new IllegalArgumentException (rsrc.getString ("err_structuredUnit_negMax"));
      
      if (min.getExponent() != 0)
        throw new IllegalArgumentException (rsrc.getString ("err_structuredUnit_scaledMin"));

      if (max.getExponent() != 0)
        throw new IllegalArgumentException (rsrc.getString ("err_structuredUnit_scaledMax"));
    }
    
    
    public Unit getUnit()
    {
      return unit;
    }


    public PrecisionDecimal getMinimum ()
    {
      return min;
    }


    public PrecisionDecimal getMaximum ()
    {
      return max;
    }
  };

  
  protected Unit firstUnit = null;
  protected Part[] parts = new Part[0];
  protected boolean continuous = false;
  
  /** Constructs a new structured unit.
  */
  public StructuredUnit()
  {
  }


  /** Returns the parts or components of this structured unit.
  */
  public Part[] getParts ()
  {
    return parts;
  }
  
  
  /** Sets the parts or components of this structured unit.
  */
  public void setParts (Part[] parts)
    throws java.beans.PropertyVetoException
  {
    Part[] oldValue = this.parts;
    Part[] newValue = parts;
    Unit   commonBase;
    
    if (oldValue == newValue)
      return;

    if (firstUnit == null)
      commonBase = newValue[0].getUnit();
    else
      commonBase = firstUnit;
    

    /* FIXME: Check for dependencies: No unit in parts should
       be dependent on "this". Low priority because this can not happen
       while parsing.
    */
    
    /* FIXME: Check inherited. Not implemented in this version because
       the parser uses another order. Low priority, as this will not
       happen while parsing.
    */
    if (extending != null)
      throw new IllegalArgumentException ("This order not implemented yet. Call setExtending() later.");
    
    for (int i = 0; i < parts.length; i++)
    {
      Unit u = parts[i].getUnit();
      if ((u != null) && u.isAbstract())
        throw new IllegalArgumentException (formatMessage (
          "err_abstractStructUnitPart",
          this.toString(), parts[i].getUnit().toString()));
      
      if (i < parts.length - 1)
      {
        if (((parts[i].getMinimum() != null) && (parts[i].getMinimum().getExponent() != 0))
            || ((parts[i].getMaximum() != null) && (parts[i].getMaximum().getExponent() != 0)))
        {
          throw new IllegalArgumentException (formatMessage (
            "err_structuredUnit_fractionalPart",
            this.toString()));
        }
      }
      
      commonBase = parts[i].getUnit().findCommonBase (commonBase);
      while ((commonBase != null) && !(commonBase instanceof BaseUnit))
        commonBase = commonBase.extending;
      /* now: either commonBase instanceof BaseUnit, or commonBase == null */
      
      if (commonBase == null)
        throw new IllegalArgumentException (formatMessage (
          "err_structUnitPartsWithoutCommonBase",
          this.toString()));
    }
    
    
    /* Set value and inform interested listeners. */
    fireVetoableChange ("parts", oldValue, newValue);
    this.parts = newValue;
    firePropertyChange ("parts", oldValue, newValue);
  }

  
  
  public Unit getFirstUnit()
  {
    return firstUnit;
  }


  public void setFirstUnit (Unit firstUnit)
    throws java.beans.PropertyVetoException
  {
    Unit oldValue = this.firstUnit;
    Unit newValue = firstUnit;
    
    if (oldValue == newValue)
      return;

    if (firstUnit.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_abstractStructUnitPart",
        this.toString(), firstUnit.toString()));
      

    /* Set value and inform interested listeners. */
    fireVetoableChange("firstUnit", oldValue, newValue);
    this.firstUnit = newValue;
    firePropertyChange("firstUnit", oldValue, newValue);
  }

  
  public boolean isDependentOn(Element e)
  {
   if ((firstUnit != null) && firstUnit.isDependentOn(e))
     return true;

   for (int i = 0; i < parts.length; i++)
   {
     Part p = parts[i];
     if (p.getUnit().isDependentOn(e))
       return true;
   }

   return super.isDependentOn(e);
  }
  

  public void setExtending (Unit extending)
    throws java.beans.PropertyVetoException
  {
    if (extending != null)
      throw new IllegalArgumentException (formatMessage (
        "err_cantExtendStructUnit",
        this.toString(), extending.toString()));
    
    super.setExtending (extending);
  }
  
  
  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
    if (abst == true)
      throw new IllegalArgumentException (formatMessage (
        "err_abstractStructUnit",
        this.toString()));
    
    super.setAbstract (abst);
  }
  
  
  /** Determines whether or not the structured unit is continuous.
      A continuous unit is one which is defined for all values.
      For example, time-of-day is continuous because all hours
      have 60 minutes and all minutes have 60 seconds. In contrast,
      day-of-year is not continuous, as not all months have
      31 days.
      
      <p>In JavaBeans terminology, the <code>continuous</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.
      
      @param fin Pass <code>true</code> to make the structured
                 unit continuous, pass <code>false</code> to make
                 it non-continuous.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>continuous</code> property
                 and does not agree with the change.
  */
  public void setContinuous (boolean continuous)
    throws java.beans.PropertyVetoException
  {
    boolean newValue = continuous;
    boolean oldValue = this.continuous;
    
    if (oldValue == newValue)
      return;

    /* Set value and inform interested listeners. */
    fireVetoableChange ("continuous", oldValue, newValue);
    this.continuous = newValue;
    firePropertyChange ("continuous", oldValue, newValue);
  }
  
  
  
  /** Returns whether or not the structured unit is continuous.
      A continuous unit is one which is defined for all values.
      For example, time-of-day is continuous because all hours
      have 60 minutes and all minutes have 60 seconds. In contrast,
      day-of-year is not continuous, as not all months have
      31 days.
  */
  public boolean isContinuous ()
  {
    return continuous;
  }
}
