/*****************************************************************************
 *
 * ComposedUnit.java
 * -----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  April 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** ComposedUnit is a class for composed Interlis units, for
    example <code>m/s</code>.

    @version   April 12, 1999
    @author    Sascha Brawer, sb@adasys.ch
*/
public class ComposedUnit extends Unit
{
  /** A composed unit is composed of several instances of Composed;
      one single Composed denotes one such composition element,
      such as "per second".
  */
  public static class Composed
  {
    protected char compositionOperator;
    protected Unit unit;

    /** Constructs a new Composed given a composition operator
        and a unit.

        @param compositionOperator The composition operator.
               Only '*' and '/' are acceptable.

        @exception java.lang.IllegalArgumentException if
                   compositionOperator is not one of
                   <code>'*'</code> or <code>'/'</code>.
    */
    public Composed (char compositionOperator, Unit unit)
    {
      this.compositionOperator = compositionOperator;
      this.unit = unit;

      if ((compositionOperator != '*') && (compositionOperator != '/'))
        throw new IllegalArgumentException (
          ch.interlis.ili2c.metamodel.Element.rsrc.getString ("err_illegalUnitCompositionOperator"));
    }


    /** Returns the unit which is part of the composition element.
        For example, the result of <code>getUnit()</code> on the
        <i>per second</i> Composed (as part of, say, <i>meters per
        second</i>) would be the unit that denotes seconds.
    */
    public Unit getUnit()
    {
      return unit;
    }

    /** @return either <code>'*'</code> or <code>'/'</code>. */
    public char getCompositionOperator()
    {
      return compositionOperator;
    }
  };


  protected Composed[] composedUnits = new Composed[0];


  /** Constructs a new composed unit. */
  public ComposedUnit()
  {
  }

  /** Returns how this composed unit is composed. */
  public Composed[] getComposedUnits ()
  {
    return composedUnits;
  }


  /** Sets how this composed unit is composed.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>composedUnits</code> property
                 and does not agree with the change.
  */
  public void setComposedUnits (Composed[] composedUnits)
    throws java.beans.PropertyVetoException
  {
    Composed[] oldValue = this.composedUnits;
    Composed[] newValue = composedUnits;

    if (oldValue == newValue)
      return;

    /* FIXME: Check for dependencies: Not item in composedUnits should
       be dependent on "this". Low priority because this can not happen
       while parsing.
    */

    /* FIXME: Check inherited. Not implemented in this version because
       the parser uses another order --> low priority.
    */
    if (extending != null)
      throw new IllegalArgumentException ("This order not implemented yet. Works only with units that do not extend another one.");

    for (int i = 0; i < composedUnits.length; i++)
    {
      if (this.isAbstract() && !composedUnits[i].getUnit().isAbstract())
        throw new IllegalArgumentException (formatMessage (
          "err_abstractUnitComposedOfConcrete",
          this.toString(), composedUnits[i].getUnit().toString()));

      if (!this.isAbstract() && composedUnits[i].getUnit().isAbstract())
        throw new IllegalArgumentException (formatMessage (
          "err_concreteUnitComposedOfAbstract",
          this.toString(), composedUnits[i].getUnit().toString()));
    }


    /* Set value and inform interested listeners. */
    fireVetoableChange("composedUnits", oldValue, newValue);
    this.composedUnits = composedUnits;
    firePropertyChange("composedUnits", oldValue, newValue);
  }




  /** Returns whether or not this Element depends on another
      Element. A composed unit depends on its base unit and
      on all units of which it is composed. Finally, it
      depends on all units it is extending.
  */
  public boolean isDependentOn (Element e)
  {
   if ((extending != null) && extending.isDependentOn(e))
     return true;

   for (int i = 0; i < composedUnits.length; i++)
   {
     Composed c = composedUnits[i];
     if (c.getUnit().isDependentOn(e))
       return true;
   }

   return super.isDependentOn(e);
  }


  protected void checkExtending (Unit ext)
  {

	/* Make sure there won't be cyclic dependency graphs. */
	if ((ext != null) && (ext.isDependentOn(this))) {
	  throw new IllegalArgumentException("The unit \""
		+ this.getName() + "\" can not be based on \""
		+ ext.getName() +
		", because the latter depends on the former.");
	}

    if (ext != null)
    {
      if (!(ext instanceof ComposedUnit))
        throw new IllegalArgumentException (formatMessage (
          "err_composedUnitExtendingNonComposed",
          this.toString(), ext.toString()));

      ComposedUnit e = (ComposedUnit) ext;

      if (this.composedUnits.length != e.composedUnits.length)
        throw new IllegalArgumentException (formatMessage (
          "err_composedUnitExtendingUnequalCardinality",
          this.toString(), e.toString()));

      for (int i = 0; i < composedUnits.length; i++)
      {
        if (!this.composedUnits[i].getUnit().isExtendingIndirectly (e.composedUnits[i].getUnit()))
          throw new IllegalArgumentException (formatMessage (
            "err_unitCantExtendBecauseOfPart",
            this.toString(),
            e.toString(),
            this.composedUnits[i].getUnit().toString(),
            e.composedUnits[i].getUnit().toString()));

        if (this.composedUnits[i].getCompositionOperator()
            != e.composedUnits[i].getCompositionOperator())
        {
          if (this.composedUnits[i].getCompositionOperator() == '/')
            throw new IllegalArgumentException (formatMessage (
              "err_unitCantExtendBecauseOfDiv",
              this.toString(), e.toString(),
              e.composedUnits[i].getUnit().toString()));
          else
            throw new IllegalArgumentException (formatMessage (
              "err_unitCantExtendBecauseOfMult",
              this.toString(), e.toString(),
              e.composedUnits[i].getUnit().toString()));
        }
      }
    }
  }


  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
    if (abst == false)
    {
      for (int i = 0; i < composedUnits.length; i++)
      {
        if ((composedUnits[i] != null)
          && (composedUnits[i].getUnit() != null)
          && composedUnits[i].getUnit().isAbstract())
        {
          throw new IllegalArgumentException (formatMessage (
            "err_concreteUnitComposedOfAbstract",
            this.toString(), composedUnits[i].getUnit().toString()));
        }
      }
    }
    else
    {
      for (int i = 0; i < composedUnits.length; i++)
      {
        if ((composedUnits[i] != null)
          && (composedUnits[i].getUnit() != null)
          && !composedUnits[i].getUnit().isAbstract())
        {
          throw new IllegalArgumentException (formatMessage (
            "err_abstractUnitComposedOfConcrete",
            this.toString(), composedUnits[i].getUnit().toString()));
        }
      }
    }

    super.setAbstract (abst);
  }
  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      ComposedUnit baseElement=(ComposedUnit)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      Ili2cSemanticException err=null;
      if(composedUnits.length!=baseElement.composedUnits.length) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
      }else {
          for(int i=0;i<composedUnits.length;i++) {
              if(i>0) {
                 if(composedUnits[i].compositionOperator!=baseElement.composedUnits[i].compositionOperator) {
                     errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
                 }
              }
              if(composedUnits[i].unit.getTranslationOfOrSame()!=baseElement.composedUnits[i].unit.getTranslationOfOrSame()) {
                  errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
              }
          }
          
      }
  }
}

