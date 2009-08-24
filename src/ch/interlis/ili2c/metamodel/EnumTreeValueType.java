package ch.interlis.ili2c.metamodel;
import java.util.Iterator;


public class EnumTreeValueType extends BaseType {

  public EnumTreeValueType()
  {
  }
  public java.util.Set getDirectExtensions()
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
  } 

public Domain getEnumType() {
	return enumType;
}
public void setEnumType(Domain domain) {
	enumType = domain;
}

}
