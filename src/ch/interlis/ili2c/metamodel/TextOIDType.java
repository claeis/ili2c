package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OID TextType' domain
 *
 */
public class TextOIDType extends OIDType
{
	Type type;
	public TextOIDType(Type textType)
	{
		type=textType;
	}
	public Type getOIDType()
	{
		return type;
	}
}
