package ch.interlis.models.IlisMeta07.ModelData;
public class MetaBasketDef_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private MetaBasketDef_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(MetaBasketDef_Kind value) {
     return value.value;
  }
  static public MetaBasketDef_Kind parseXmlCode(String value) {
     return (MetaBasketDef_Kind)valuev.get(value);
  }
  static public MetaBasketDef_Kind SignB=new MetaBasketDef_Kind("SignB");
  public final static String tag_SignB="SignB";
  static public MetaBasketDef_Kind RefSystemB=new MetaBasketDef_Kind("RefSystemB");
  public final static String tag_RefSystemB="RefSystemB";
}
