package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OID ...' domain
 *
 */
public abstract class OIDType extends Type
{
	public OIDType()
	{
	}
	abstract public Type getOIDType();
}
