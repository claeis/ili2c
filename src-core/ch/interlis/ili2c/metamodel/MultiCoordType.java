
package ch.interlis.ili2c.metamodel;
import ch.ehi.basics.logging.EhiLogger;


/** A coordinate type. */
public class MultiCoordType extends AbstractCoordType
{
  protected MultiCoordType()
  {
	  
  }
  public MultiCoordType (NumericalType[] dimensions, int nullAxis, int piHalfAxis)
  {
	  super(dimensions, nullAxis, piHalfAxis);
  }


  public MultiCoordType (NumericalType[] dimensions)
  {
    this (dimensions, 0, 0);
  }

    public MultiCoordType clone() {
        return (MultiCoordType) super.clone();
    }
}
