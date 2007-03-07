package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around XY->RuntimeParameterValue that holds a pointer to the parent.
 * @author ce
 */
public class RuntimeParameterValueWrapper extends EvaluableWrapper {
	public ParameterValue value;
	public RuntimeParameterValueWrapper(ParameterValue value1)
	{
		value=value1;
	}
}
