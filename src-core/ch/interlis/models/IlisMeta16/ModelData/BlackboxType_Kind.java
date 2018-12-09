package ch.interlis.models.IlisMeta16.ModelData;
public class BlackboxType_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private BlackboxType_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(BlackboxType_Kind value) {
     return value.value;
  }
  static public BlackboxType_Kind parseXmlCode(String value) {
     return (BlackboxType_Kind)valuev.get(value);
  }
  static public BlackboxType_Kind Binary=new BlackboxType_Kind("Binary");
  public final static String tag_Binary="Binary";
  static public BlackboxType_Kind Xml=new BlackboxType_Kind("Xml");
  public final static String tag_Xml="Xml";
}
