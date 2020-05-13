package ch.interlis.ili2c.metamodel;

/** Implements the syntax-rule AssociationPath.
 */
public class AssociationPath extends PathEl
{
	private RoleDef targetRole=null;
	public AssociationPath(RoleDef targetRole){
		this.targetRole=targetRole;
	}
	// next should return an AssociationDef
        public String getName(){
          return "\\"+targetRole.getName();
        }
	public Viewable getViewable()
	{
		return (Viewable)targetRole.getContainer();
	}
	public RoleDef getTargetRole(){
		return targetRole;
	}

}


