package ch.interlis.ili2c.metamodel;

public class PathElRefAttr extends PathEl {
	private AttributeDef refAttr;
	public PathElRefAttr(AttributeDef refAttr)
	{
		this.refAttr=refAttr;
	}
	public Viewable getViewable()
	{
		return ((ReferenceType)refAttr.getDomainResolvingAliases()).getReferred();
	}
        public String getName(){
          return refAttr.getName();
        }
	public AttributeDef getAttr(){
		return refAttr;
	}
}


