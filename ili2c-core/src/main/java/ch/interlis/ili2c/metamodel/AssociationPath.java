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
    @Override
    public Ili2cSemanticException checkTranslation(PathEl otherPathEl,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(otherPathEl,sourceLine);
        if(ret!=null) {
            return ret;
        }
        AssociationPath other=(AssociationPath)otherPathEl;
        if(!Element.equalElementRef(targetRole, other.targetRole)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedRoleMismatch",getName(),other.getName()));
        }
        return null;
    }

}


