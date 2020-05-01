package ch.interlis.ili2c.metamodel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class EnumTreeValueType extends BaseType {

  public EnumTreeValueType()
  {
  }
  public Set<Type> getDirectExtensions()
  {
  	return extendedBy;
  }
  private Domain enumType=null;

  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }

public Domain getEnumType() {
	return enumType;
}
public void setEnumType(Domain domain) {
	enumType = domain;
}


    public EnumTreeValueType clone() {
        return (EnumTreeValueType) super.clone();
    }
	public Enumeration getConsolidatedEnumeration() {
	      Enumeration ret=((EnumerationType)enumType.getType()).getConsolidatedEnumeration();
	      return ret;
	}

    protected void checkTranslationOf(List<Ili2cSemanticException> errs, String name, String baseName) {
        super.checkTranslationOf(errs, name, baseName);
        EnumTreeValueType baseElement = (EnumTreeValueType) getTranslationOf();
        if (baseElement == null) {
            return;
        }
        Ili2cSemanticException err=null;
        err=checkElementRef(enumType, baseElement.enumType, getSourceLine(), "err_diff_baseDomainMismatch");
        if(err!=null) {
            errs.add(err);
        }

    }
    private java.util.ArrayList<String> cachedValues=null;
    public java.util.List<String> getValues()
    {
        if(cachedValues==null){
            cachedValues=new java.util.ArrayList<String>();
            buildEnumList(cachedValues,"",getConsolidatedEnumeration());
        }
        return cachedValues;
    }
    public static void buildEnumList(java.util.List<String> accu,String prefix1,Enumeration enumer){
        Iterator iter = enumer.getElements();
        String prefix="";
        if(prefix1.length()>0){
          prefix=prefix1+".";
        }
        while (iter.hasNext()) {
          Enumeration.Element ee=(Enumeration.Element) iter.next();
          Enumeration subEnum = ee.getSubEnumeration();
          // add it to accu
          accu.add(prefix+ee.getName());
          if (subEnum != null)
          {
            // ee is not leaf, add its name to prefix and add sub elements to accu
            buildEnumList(accu,prefix+ee.getName(),subEnum);
          }
        }
    }
}
