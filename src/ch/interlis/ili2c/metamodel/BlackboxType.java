package ch.interlis.ili2c.metamodel;
import java.util.Iterator;


public class BlackboxType extends BaseType {

  static final public int eXML=1; 
  static final public int eBINARY=2;
  private int kind=0; 
  public BlackboxType()
  {
  }  
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
public int getKind() {
	return kind;
}
public void setKind(int i) {
	kind = i;
}

}
