package ch.interlis.ili2c.metamodel;
import java.util.Iterator;


public class AttributePathType extends BaseType {

  public AttributePathType()
  {
  }  
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
}
