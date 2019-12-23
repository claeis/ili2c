package ch.interlis.ili2c.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    public NumericOIDType clone() {
        return (NumericOIDType) super.clone();
    }
    @Override
    protected void linkTranslationOf(Element baseElement)
    {
        super.linkTranslationOf(baseElement);
        if(type==null){
            return;
        }
        Type baseType=((NumericOIDType) baseElement).type;
        type.linkTranslationOf(baseType);
    }
    @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        NumericOIDType origin=(NumericOIDType)getTranslationOf();
        if(origin==null) {
            return;
        }
        type.checkTranslationOf(errs, name, baseName);
    }

}
