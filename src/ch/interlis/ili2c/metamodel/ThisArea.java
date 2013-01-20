package ch.interlis.ili2c.metamodel;


public class ThisArea extends PathEl
{
	private boolean thatArea;
	private DecompositionView base;

    public ThisArea(DecompositionView base,boolean thatArea)
    {
	    this.thatArea=thatArea;
	    this.base=base;
    }

		public Viewable getViewable()
		{
			  ObjectPath inspectedAttr=base.getDecomposedAttribute();
			  return inspectedAttr.getRoot();
		}
    public String getName ()
    {
      return thatArea ? "THATAREA" : "THISAREA";
    }
    public boolean isThat(){
    	return thatArea;
    }

}
