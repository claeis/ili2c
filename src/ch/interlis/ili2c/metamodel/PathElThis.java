package ch.interlis.ili2c.metamodel;


public class PathElThis extends PathEl
{
	private Viewable base;
    public PathElThis(Viewable base)
    {
	    this.base=base;
    }
    public Viewable getViewable()
    {
	    return base;
    }
    /** Returns the String <code>THIS</code>.
    */
    public String getName ()
    {
      return "THIS";
    }
}
