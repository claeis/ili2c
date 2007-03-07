package ch.interlis.ili2c.metamodel;
public class PathElAssocRole extends PathEl {
	private RoleDef role;
	public PathElAssocRole(RoleDef role)
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
}


