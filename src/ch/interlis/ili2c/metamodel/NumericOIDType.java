package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OID NumericType' domain
 *
 */
public class NumericOIDType extends OIDType
{
	NumericType type;
	public NumericOIDType(NumericType numericType)
	{
		type=numericType;
	}
	public Type getOIDType()
	{
		return type;
	}
}
