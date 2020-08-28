package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around CoordType->Dimension that holds a pointer to the parent coordtype.
 * @author ce
 */
public class CoordTypeDimensionWrapper {
	public CoordType coordType;
	public NumericalType dimension;
	public int orderPos;
	public CoordTypeDimensionWrapper(CoordType coordType1,NumericalType dimension1,int orderPos1)
	{
		coordType=coordType1;
		dimension=dimension1;
		orderPos=orderPos1;
	}
}
