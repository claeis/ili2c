package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION RestrictionOfCompTypeRestrictedTo =
      restrictionOfCompType -- {0..*} CompositionType;
      restrictedTo -- {0..*} Table;
    END RestrictionOfCompTypeRestrictedTo;
 */
public class RestrictionOfCompTypeRestrictedTo {
	public CompositionType restrictionOfCompType;
	public Table restrictedTo;
	public RestrictionOfCompTypeRestrictedTo(CompositionType restrictionOfCompType1,Table restrictedTo1)
	{
		restrictionOfCompType=restrictionOfCompType1;
		restrictedTo=restrictedTo1;
	}
}
