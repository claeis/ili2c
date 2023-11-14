package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around XY->Constant that holds a pointer to the parent.
 * @author ce
 */
public class ConstantWrapper  extends EvaluableWrapper {
	public Constant cnst;
	public ConstantWrapper(Constant cnst1)
	{
		cnst=cnst1;
	}
}
