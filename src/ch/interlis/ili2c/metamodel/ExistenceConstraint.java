package ch.interlis.ili2c.metamodel;


import java.util.*;

public class ExistenceConstraint extends Constraint
{
	private ObjectPath restrictedAttribute;
	private LinkedList<ObjectPath> requiredIn = new LinkedList<ObjectPath>();

	/** define the attribute that should be checked by this constraint.
	*/
	public void setRestrictedAttribute(ObjectPath path)
	{
		restrictedAttribute=path;
	}
        public ObjectPath getRestrictedAttribute()
        {
          return restrictedAttribute;
        }
	/** define an attribute in an other viewable that should hold the same value
	*/
	public void addRequiredIn(ObjectPath attribute)
	{
		requiredIn.add(attribute);
	}
        public Iterator<ObjectPath> iteratorRequiredIn()
        {
          return requiredIn.iterator();
        }
}
