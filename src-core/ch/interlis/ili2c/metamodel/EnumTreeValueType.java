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
}
