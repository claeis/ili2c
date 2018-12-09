package ch.interlis.models.IlisMeta16.ModelData;
public class SimpleConstraint_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private SimpleConstraint_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(SimpleConstraint_Kind value) {
     return value.value;
  }
  static public SimpleConstraint_Kind parseXmlCode(String value) {
     return (SimpleConstraint_Kind)valuev.get(value);
  }
  static public SimpleConstraint_Kind MandC=new SimpleConstraint_Kind("MandC");
  public final static String tag_MandC="MandC";
  static public SimpleConstraint_Kind LowPercC=new SimpleConstraint_Kind("LowPercC");
  public final static String tag_LowPercC="LowPercC";
  static public SimpleConstraint_Kind HighPercC=new SimpleConstraint_Kind("HighPercC");
  public final static String tag_HighPercC="HighPercC";
}
