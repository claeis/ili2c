package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around NumericallyDerivedUnit->NumericallyDerivedUnit.Factor that holds a pointer to the parent.
 * @author ce
 */
public class NumericallyDerivedUnit_FactorWrapper {
	public NumericallyDerivedUnit numericallyDerivedUnit;
	public NumericallyDerivedUnit.Factor factor;
	public NumericallyDerivedUnit_FactorWrapper(NumericallyDerivedUnit numericallyDerivedUnit1, NumericallyDerivedUnit.Factor factor1)
	{
		numericallyDerivedUnit=numericallyDerivedUnit1;
		factor=factor1;
	}
}
