package ch.interlis.models.IlisMeta16.ModelData;
public class Class_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Class_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Class_Kind value) {
     return value.value;
  }
  static public Class_Kind parseXmlCode(String value) {
     return (Class_Kind)valuev.get(value);
  }
  static public Class_Kind Structure=new Class_Kind("Structure");
  public final static String tag_Structure="Structure";
  static public Class_Kind _class=new Class_Kind("Class");
  public final static String tag__class="Class";
  static public Class_Kind View=new Class_Kind("View");
  public final static String tag_View="View";
  static public Class_Kind Association=new Class_Kind("Association");
  public final static String tag_Association="Association";
}
