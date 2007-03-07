package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around FunctionDef->FormalArgument that holds a pointer to the parent.
 * @author ce
 */
public class FormalArgumentWrapper {
	public Function functionDef;
	public FormalArgument argument;
	public int orderPos;
	public FormalArgumentWrapper(Function functionDef1,FormalArgument argument1,int orderPos1)
	{
		functionDef=functionDef1;
		argument=argument1;
		orderPos=orderPos1;
	}
}
