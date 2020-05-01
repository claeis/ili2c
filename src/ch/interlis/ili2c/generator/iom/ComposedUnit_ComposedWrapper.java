package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around ComposedUnit->ComposedUnit_Composed that holds a pointer to the parent.
 * @author ce
 */
public class ComposedUnit_ComposedWrapper {
	public ComposedUnit composedUnit;
	public ComposedUnit.Composed factor;
	public ComposedUnit_ComposedWrapper(ComposedUnit composedUnit1, ComposedUnit.Composed factor1)
	{
		composedUnit=composedUnit1;
		factor=factor1;
	}
}
