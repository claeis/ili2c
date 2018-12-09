package ch.interlis.models.IlisMeta16.ModelData;
public class Ili1Format_tidKind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Ili1Format_tidKind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Ili1Format_tidKind value) {
     return value.value;
  }
  static public Ili1Format_tidKind parseXmlCode(String value) {
     return (Ili1Format_tidKind)valuev.get(value);
  }
  static public Ili1Format_tidKind TID_I16=new Ili1Format_tidKind("TID_I16");
  public final static String tag_TID_I16="TID_I16";
  static public Ili1Format_tidKind TID_I32=new Ili1Format_tidKind("TID_I32");
  public final static String tag_TID_I32="TID_I32";
  static public Ili1Format_tidKind TID_ANY=new Ili1Format_tidKind("TID_ANY");
  public final static String tag_TID_ANY="TID_ANY";
  static public Ili1Format_tidKind TID_EXPLANATION=new Ili1Format_tidKind("TID_EXPLANATION");
  public final static String tag_TID_EXPLANATION="TID_EXPLANATION";
}
