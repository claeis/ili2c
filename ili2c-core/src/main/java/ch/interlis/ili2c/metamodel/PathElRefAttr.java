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
    @Override
    public Ili2cSemanticException checkTranslation(PathEl otherPathEl,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(otherPathEl,sourceLine);
        if(ret!=null) {
            return ret;
        }
        PathElRefAttr other=(PathElRefAttr)otherPathEl;
        if(!Element.equalElementRef(refAttr, other.refAttr)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),other.getName()));
        }
        return null;
    }
}


