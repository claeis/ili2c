package ch.interlis.models.IlisMeta16.ModelData;
public class Unit_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Unit_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Unit_Kind value) {
     return value.value;
  }
  static public Unit_Kind parseXmlCode(String value) {
     return (Unit_Kind)valuev.get(value);
  }
  static public Unit_Kind BaseU=new Unit_Kind("BaseU");
  public final static String tag_BaseU="BaseU";
  static public Unit_Kind DerivedU=new Unit_Kind("DerivedU");
  public final static String tag_DerivedU="DerivedU";
  static public Unit_Kind ComposedU=new Unit_Kind("ComposedU");
  public final static String tag_ComposedU="ComposedU";
}
