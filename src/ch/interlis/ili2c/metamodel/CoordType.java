
package ch.interlis.ili2c.metamodel;


/** A coordinate type. */
public class CoordType extends BaseType
{
  protected int nullAxis;
  protected int piHalfAxis;
  protected NumericalType[] dimensions;
  
  public CoordType (NumericalType[] dimensions, int nullAxis, int piHalfAxis)
  {
    this.nullAxis = nullAxis;
    this.piHalfAxis = piHalfAxis;
    this.dimensions = dimensions;
    
    if (dimensions == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));
    
    for (int i = 0; i < dimensions.length; i++)
    {
      /* no dimension can be null */
      if (dimensions[i] == null)
        throw new IllegalArgumentException (rsrc.getString (
          "err_nullNotAcceptable"));
      
      /* if the unit is structured, it must be continuous */
      Unit u = dimensions[i].getUnit();
      if ((u instanceof StructuredUnit) && !((StructuredUnit) u).isContinuous())
        throw new IllegalArgumentException (formatMessage (
          "err_coordType_structuredUnitNotContinuous",
          u.toString()));
    }
     
    if ((nullAxis < 0) || (nullAxis > dimensions.length))
      throw new IllegalArgumentException (formatMessage (
        "err_rotationDefInvalid",
        Integer.toString (nullAxis),
        Integer.toString (dimensions.length)));

    if ((piHalfAxis < 0) || (piHalfAxis > dimensions.length))
      throw new IllegalArgumentException (formatMessage (
        "err_rotationDefInvalid",
        Integer.toString (piHalfAxis),
        Integer.toString (dimensions.length)));
    
    if ((nullAxis == piHalfAxis) && (nullAxis != 0))
      throw new IllegalArgumentException (rsrc.getString ("err_rotationDefSame"));
  }
  
  
  public CoordType (NumericalType[] dimensions)
  {
    this (dimensions, 0, 0);
  }
  
  public NumericalType[] getDimensions()
  {
    return dimensions;
  }
  
  
  public int getNullAxis ()
  {
    return nullAxis;
  }
  
  public int getPiHalfAxis ()
  {
    return piHalfAxis;
  }
  
  /** An abstract type is one that does describe sufficiently
      the set of possible values. A CoordType is abstract
      if one of its dimensions (which are instances of NumericalType)
      is abstract.
      
      @return Whether or not this type is abstract.
      @see ch.interlis.NumericType#isAbstract()
      @see ch.interlis.StructuredUnitType#isAbstract()
  */
  public boolean isAbstract ()
  {
    for (int i = 0; i < dimensions.length; i++)
      if (dimensions[i].isAbstract())
        return true;
        
    return false;
  }
  
  public boolean checkStructuralEquivalence (Element with, ErrorListener listener)
  {
    if (!super.checkStructuralEquivalence (with, listener))
      return false;
    
    boolean fine = true;
    CoordType other = (CoordType) with;

    if (this.getNullAxis() != other.getNullAxis())
    {
      listener.error (new ErrorListener.ErrorEvent (
        formatMessage ("err_diff_coordType_nullAxis", this.toString(), other.toString()),
        /* origin of error */ this,
        ErrorListener.ErrorEvent.SEVERITY_ERROR));
      fine = false;
    }

    if (this.getPiHalfAxis() != other.getPiHalfAxis())
    {
      listener.error (new ErrorListener.ErrorEvent (
        formatMessage ("err_diff_coordType_piHalfAxis", this.toString(), other.toString()),
        /* origin of error */ this,
        ErrorListener.ErrorEvent.SEVERITY_ERROR));
      fine = false;
    }
   
    NumericalType[] myDims = this.getDimensions();
    NumericalType[] otherDims = other.getDimensions();
    
    fine &= checkStructuralEquivalenceOfArrays (with, this.getDimensions(), other.getDimensions(),
                                                listener, "err_diff_coordType_numDimensions");
    return fine;
  }
}