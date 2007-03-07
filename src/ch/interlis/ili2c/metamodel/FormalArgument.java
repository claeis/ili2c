package ch.interlis.ili2c.metamodel;

public class FormalArgument extends AbstractLeafElement
{
	private Type domain;
	private String name;
	public FormalArgument(String name,Type domain)
	{
		this.domain=domain;
		this.name=name;
	}
	public Type getType()
	{
		return domain;
	}
	public String getName(){
		return name;
	}
}
