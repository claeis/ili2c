package ch.interlis.ili2c.metamodel;
public class PathElBase extends PathEl {
	private Viewable base;
	private Viewable current;
        private String baseName;
	public PathElBase(Viewable currentView,String baseName,Viewable baseView)
	{
		current=currentView;
		this.baseName=baseName;
		base=baseView;
	}
	public Viewable getViewable()
	{
		return base;
	}
	public Viewable getCurrentViewable()
	{
		return current;
	}
        public String getName ()
        {
          return baseName;
        }
}


