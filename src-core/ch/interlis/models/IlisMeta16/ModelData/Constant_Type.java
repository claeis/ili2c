package ch.interlis.models.IlisMeta16.ModelData;
public class Constant_Type{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Constant_Type(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Constant_Type value) {
     return value.value;
  }
  static public Constant_Type parseXmlCode(String value) {
     return (Constant_Type)valuev.get(value);
  }
  static public Constant_Type Undefined=new Constant_Type("Undefined");
  public final static String tag_Undefined="Undefined";
  static public Constant_Type Numeric=new Constant_Type("Numeric");
  public final static String tag_Numeric="Numeric";
  static public Constant_Type Text=new Constant_Type("Text");
  public final static String tag_Text="Text";
  static public Constant_Type Enumeration=new Constant_Type("Enumeration");
  public final static String tag_Enumeration="Enumeration";
}
