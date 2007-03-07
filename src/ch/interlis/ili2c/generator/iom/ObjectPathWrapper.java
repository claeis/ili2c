package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/** Wrapper around XY->ObjectPath that holds a pointer to the parent.
 * @author ce
 */
public class ObjectPathWrapper extends EvaluableWrapper {
	public ObjectPath objectpath;
	public UniquenessConstraint uniquenessConstraint=null;
	public UniquenessConstraint prefixedUniquenessConstraint=null;
	public ExistenceConstraint existenceConstraintBase=null;
	public ExistenceConstraint existenceConstraintReferrer=null;
	public int orderPos=0;
	public ObjectPathWrapper(ObjectPath objectpath1)
	{
		objectpath=objectpath1;
	}
}
