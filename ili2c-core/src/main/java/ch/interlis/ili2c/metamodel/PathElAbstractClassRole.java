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
}


