package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around XY->Expression that holds a pointer to the parent.
 * @author ce
 */
public class ExpressionWrapper extends EvaluableWrapper {
	public Expression expr;
	public ExpressionWrapper(Expression expr1)
	{
		expr=expr1;
	}
}
