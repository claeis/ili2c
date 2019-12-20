package ch.interlis.ili2c.metamodel;

import java.util.List;

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


    public TextOIDType clone() {
        return (TextOIDType) super.clone();
    }
    @Override
    protected void linkTranslationOf(Element baseElement)
    {
        super.linkTranslationOf(baseElement);
        if(type==null){
            return;
        }
        Type baseType=((TextOIDType) baseElement).type;
        type.linkTranslationOf(baseType);
    }
    @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        TextOIDType origin=(TextOIDType)getTranslationOf();
        if(origin==null) {
            return;
        }
        type.checkTranslationOf(errs, name, baseName);
    }

}
