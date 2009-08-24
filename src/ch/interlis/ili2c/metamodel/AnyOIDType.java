package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OID ANY' domain
 *
 */
public class AnyOIDType extends OIDType
{
	public AnyOIDType()
	{
	}
	public Type getOIDType()
	{
		return null;
	}
	  void checkTypeExtension (Type wantToExtend)
	  {
	    if ((wantToExtend == null)
	      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
	      return;
	    if (!(wantToExtend instanceof AnyOIDType)){
	        throw new Ili2cSemanticException (rsrc.getString (
	        "err_anyOidType_ExtOther"));
	    }
	  } 
}
