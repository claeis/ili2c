package ch.interlis.models.IlisMeta07.ModelData;
public class Argument_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Argument_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Argument_Kind value) {
     return value.value;
  }
  static public Argument_Kind parseXmlCode(String value) {
     return (Argument_Kind)valuev.get(value);
  }
  static public Argument_Kind Type=new Argument_Kind("Type");
  static public Argument_Kind EnumVal=new Argument_Kind("EnumVal");
  static public Argument_Kind EnumTreeVal=new Argument_Kind("EnumTreeVal");
}
