
package ch.interlis.ili2c.metamodel;


/** A coordinate type. */
public class CoordType extends AbstractCoordType
{
  protected CoordType()
  {
	  
  }
  public CoordType (NumericalType[] dimensions, int nullAxis, int piHalfAxis)
  {
	  super(dimensions, nullAxis, piHalfAxis);
  }


  public CoordType (NumericalType[] dimensions)
  {
    this (dimensions, 0, 0);
  }

    public CoordType clone() {
        return (CoordType) super.clone();
    }
}
