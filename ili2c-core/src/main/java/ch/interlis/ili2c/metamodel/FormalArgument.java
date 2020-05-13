package ch.interlis.ili2c.metamodel;

public class FormalArgument extends AbstractLeafElement
{
	private Type domain;
	private String name;
	private Function func;
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
	public Function getFunction(){
		return func;
	}
	public void setFunction(Function func){
		this.func=func;
	}
}
