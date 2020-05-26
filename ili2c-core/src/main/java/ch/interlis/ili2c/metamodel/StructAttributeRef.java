package ch.interlis.ili2c.metamodel;


public class StructAttributeRef extends AbstractAttributeRef
{
	public static final long eFIRST=-1;
	public static final long eLAST=-2;
	  private long index;
	  private AttributeDef attr;
	  public StructAttributeRef(AttributeDef attr,long index){
		  this.attr=attr;
		  this.index=index;
	  }
    public String getName ()
    {
      return attr.getName()+"[" + index + "]";
    }
    public Viewable getViewable(){
      return ((CompositionType)attr.getDomainResolvingAliases()).getComponentType();
    }
    public Type getDomain(){
      return attr.getDomain();
    }
	public AttributeDef getAttr(){
		return attr;
	}
	public long getIndex(){
		return index;
	}
    @Override
    public Ili2cSemanticException checkTranslation(PathEl otherPathEl,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(otherPathEl,sourceLine);
        if(ret!=null) {
            return ret;
        }
        StructAttributeRef other=(StructAttributeRef)otherPathEl;
        if(!Element.equalElementRef(attr, other.attr)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),other.getName()));
        }
        if(index!= other.index) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),other.getName()));
        }
        return null;
    }
}


