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

}
