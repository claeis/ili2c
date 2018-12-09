package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION ViewableAttributesAndRoles =
      viewable -- {0..*} Viewable;
      attributesAndRoles (ORDERED) -- {0..*} AbstractLeafElement;
    END ViewableAttributesAndRoles;
 */
public class ViewableAttributesAndRoles {
	public Viewable viewable;
	public AbstractLeafElement attributesAndRoles;
	public int orderPos;
	public ViewableAttributesAndRoles(Viewable viewable1,AbstractLeafElement attributesAndRoles1,int orderPos1)
	{
		viewable=viewable1;
		attributesAndRoles=attributesAndRoles1;
		orderPos=orderPos1;
	}
}
