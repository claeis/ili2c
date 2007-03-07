package ch.interlis.ili2c.metamodel;
public class PathElBase extends PathEl {
	private Viewable base;
        private String baseName;
	public PathElBase(String baseName,Viewable baseView)
	{
              this.baseName=baseName;
		base=baseView;
	}
	public Viewable getViewable()
	{
		return base;
	}
        public String getName ()
        {
          return baseName;
        }
}


