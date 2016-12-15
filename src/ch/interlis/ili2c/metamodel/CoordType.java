
package ch.interlis.ili2c.metamodel;
import ch.ehi.basics.logging.EhiLogger;


/** A coordinate type. */
public class CoordType extends AbstractCoordType
{
  protected CoordType()
  {
	  
  }
  public CoordType (NumericalType[] dimensions, int nullAxis, int piHalfAxis)
  {
  }


  public CoordType (NumericalType[] dimensions)
  {
    this (dimensions, 0, 0);
  }

    public CoordType clone() {
        return (CoordType) super.clone();
    }
}
