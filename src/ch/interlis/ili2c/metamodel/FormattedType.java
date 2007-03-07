package ch.interlis.ili2c.metamodel;
import java.util.Iterator;


public class FormattedType extends BaseType {

  public FormattedType()
  {
  }  
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
}
