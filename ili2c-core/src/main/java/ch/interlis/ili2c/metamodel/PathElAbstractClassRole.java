package ch.interlis.ili2c.metamodel;
public class PathElAbstractClassRole extends PathEl {
	private RoleDef role;
	public PathElAbstractClassRole(RoleDef role)
	{
		this.role=role;
	}
	public Viewable getViewable()
	{
		return role.getDestination();
	}
        public String getName(){
          return role.getName();
        }
	public RoleDef getRole(){
		return role;
	}
    @Override
    public Ili2cSemanticException checkTranslation(PathEl otherPathEl,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(otherPathEl,sourceLine);
        if(ret!=null) {
            return ret;
        }
        PathElAbstractClassRole other=(PathElAbstractClassRole)otherPathEl;
        if(!Element.equalElementRef(role, other.role)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedRoleMismatch",getName(),other.getName()));
        }
        return null;
    }
}


