package ch.interlis.ili2c.metamodel;

public class Contract
{
	private String issuer;
	private String explanation;
	public Contract(String issuer)
	{
		this.issuer=issuer;
		this.explanation=null;
	}
	public Contract(String issuer,String explanation)
	{
		this.issuer=issuer;
		this.explanation=explanation;
	}
	public String getIssuer(){
		return issuer;
	}
	/** may return null
	 */
	public String getExplanation(){
		return explanation;
	}
}
