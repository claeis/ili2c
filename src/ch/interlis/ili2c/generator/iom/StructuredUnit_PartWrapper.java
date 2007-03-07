package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around StructuredUnit->StructuredUnit.Part that holds a pointer to the parent.
 * @author ce
 */
public class StructuredUnit_PartWrapper {
	public StructuredUnit structuredUnit;
	public StructuredUnit.Part part;
	public int orderpos;
	public StructuredUnit_PartWrapper(StructuredUnit structuredUnit1,StructuredUnit.Part part1,int orderpos1)
	{
		structuredUnit=structuredUnit1;
		part=part1;
		orderpos=orderpos1;
	}
}
