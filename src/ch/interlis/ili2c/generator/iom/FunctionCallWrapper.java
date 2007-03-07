package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around XY->FunctionCall that holds a pointer to the parent.
 * @author ce
 */
public class FunctionCallWrapper extends EvaluableWrapper {
	public FunctionCall fc;
	public FunctionCallWrapper(FunctionCall fc1)
	{
		fc=fc1;
	}
}
