package ch.interlis.ili2c.metamodel;


public class PathElParent extends PathEl
{
    private DecompositionView base;
    public PathElParent(Viewable base)
    {
      this.base=(DecompositionView)base;
    }

    /** Returns the String <code>PARENT</code>.
    */
    public String getName ()
    {
      return "PARENT";
    }
	public Viewable getViewable()
	{
          ObjectPath inspectedAttr=base.getDecomposedAttribute();
          return inspectedAttr.getRoot();
	}
}
