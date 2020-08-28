package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
	ASSOCIATION TargetLightweightAssociation =
	  targetOfLightweightAssoc -- {0..*} AbstractClassDef;
	  lightweightAssociations -- {0..*} RoleDef;
	END TargetLightweightAssociation;
 */
public class TargetLightweightAssociation {
	public AbstractClassDef targetOfLightweightAssoc;
	public RoleDef lightweightAssociations;
	public TargetLightweightAssociation(AbstractClassDef targetOfLightweightAssoc1,RoleDef lightweightAssociations1)
	{
		targetOfLightweightAssoc=targetOfLightweightAssoc1;
		lightweightAssociations=lightweightAssociations1;
	}
}
