package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around Enumeration->Element that holds a pointer to the parent enumeration.
 * @author ce
 */
public class EnumerationElementWrapper {
	public Enumeration enumeration;
	public Enumeration.Element element;
	public int orderPos;
	public EnumerationElementWrapper(Enumeration enumeration1,Enumeration.Element element1,int orderPos1)
	{
		enumeration=enumeration1;
		element=element1;
		orderPos=orderPos1;
	}
}
