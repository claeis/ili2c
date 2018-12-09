package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION RestrictionOfRefTypeRestrictedTo =
      restrictionOfRefType -- {0..*} ReferenceType;
      restrictedTo -- {0..*} AbstractClassDef;
    END RestrictionOfRefTypeRestrictedTo;
 */
public class RestrictionOfRefTypeRestrictedTo {
	public ReferenceType restrictionOfRefType;
	public AbstractClassDef restrictedTo;
	public RestrictionOfRefTypeRestrictedTo(ReferenceType restrictionOfRefType1,AbstractClassDef restrictedTo1)
	{
		restrictionOfRefType=restrictionOfRefType1;
		restrictedTo=restrictedTo1;
	}
}
