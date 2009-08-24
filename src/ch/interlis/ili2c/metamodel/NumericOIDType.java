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
	  void checkTypeExtension (Type wantToExtend)
	  {
	    if ((wantToExtend == null)
	      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
	      return;
	    if (!(wantToExtend instanceof AnyOIDType) && !(wantToExtend instanceof NumericOIDType)){
	        throw new Ili2cSemanticException (rsrc.getString (
	        "err_numericOidType_ExtOther"));
	    }
	  } 
}
