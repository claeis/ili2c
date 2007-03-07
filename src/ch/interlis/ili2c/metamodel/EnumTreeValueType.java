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

public Domain getEnumType() {
	return enumType;
}
public void setEnumType(Domain domain) {
	enumType = domain;
}

}
