package ch.interlis.ili2c.metamodel;


public class AttributeRef extends AbstractAttributeRef
{
	private AttributeDef attr;
	public AttributeRef(AttributeDef attr)
	{
		this.attr=attr;
	}
	public AttributeDef getAttr(){
		return attr;
	}
    public String getName()
    {
      return attr.getName();
    }

    public Viewable getViewable(){
      return ((CompositionType)attr.getDomainResolvingAliases()).getComponentType();
    }
    public Type getDomain(){
      return attr.getDomain();
    }
    @Override
    public Ili2cSemanticException checkTranslation(PathEl other,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(other,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(!Element.equalElementRef(attr, ((AttributeRef)other).attr)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),((AttributeRef)other).getName()));
        }
        return null;
    }

}
