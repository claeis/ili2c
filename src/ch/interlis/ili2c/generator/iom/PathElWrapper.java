package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around ObjectPath->PathEl that holds a pointer to the parent objectpath.
 * @author ce
 */
public class PathElWrapper {
	public ObjectPathWrapper objectPath;
	public PathEl element;
	public int orderPos;
	public PathElWrapper(ObjectPathWrapper objectPath1,PathEl element1,int orderPos1)
	{
		objectPath=objectPath1;
		element=element1;
		orderPos=orderPos1;
	}
}
