package ch.interlis.models.IlisMeta07.ModelData;
public class EnumType_Order{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private EnumType_Order(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(EnumType_Order value) {
     return value.value;
  }
  static public EnumType_Order parseXmlCode(String value) {
     return (EnumType_Order)valuev.get(value);
  }
  static public EnumType_Order Unordered=new EnumType_Order("Unordered");
  public final static String tag_Unordered="Unordered";
  static public EnumType_Order Ordered=new EnumType_Order("Ordered");
  public final static String tag_Ordered="Ordered";
  static public EnumType_Order Circular=new EnumType_Order("Circular");
  public final static String tag_Circular="Circular";
}
