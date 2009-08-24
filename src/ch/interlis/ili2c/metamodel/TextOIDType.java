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
	  void checkTypeExtension (Type wantToExtend)
	  {
	    if ((wantToExtend == null)
	      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
	      return;
	    if (!(wantToExtend instanceof AnyOIDType) && !(wantToExtend instanceof TextOIDType)){
	        throw new Ili2cSemanticException (rsrc.getString (
	        "err_textOidType_ExtOther"));
	    }
	  } 
}
