package ch.interlis.ili2c.metamodel;


public class BlackboxType extends BaseType {

  static final public int eXML=1;
  static final public int eBINARY=2;
  private int kind=0;
  public BlackboxType()
  {
  }
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with)) {
        return false;
    }

    return true;
  }
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null)) {
        return;
    }
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }
public int getKind() {
	return kind;
}
public void setKind(int i) {
	kind = i;
}


    public BlackboxType clone() {
        return (BlackboxType) super.clone();
    }

}
