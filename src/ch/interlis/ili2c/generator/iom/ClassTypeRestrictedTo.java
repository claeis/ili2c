package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION ClassTypeRestrictedTo =
      classType -- {0..*} ClassType;
      restrictedTo -- {0..*} Viewable;
    END ClassTypeRestrictedTo;
 */
public class ClassTypeRestrictedTo {
	public ClassType classType;
	public Viewable restrictedTo;
	public ClassTypeRestrictedTo(ClassType classType1,Viewable restrictedTo1)
	{
		classType=classType1;
		restrictedTo=restrictedTo1;
	}
}
