package ch.interlis.models.IlisMeta16.ModelData;
public class Model_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Model_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Model_Kind value) {
     return value.value;
  }
  static public Model_Kind parseXmlCode(String value) {
     return (Model_Kind)valuev.get(value);
  }
  static public Model_Kind NormalM=new Model_Kind("NormalM");
  public final static String tag_NormalM="NormalM";
  static public Model_Kind TypeM=new Model_Kind("TypeM");
  public final static String tag_TypeM="TypeM";
  static public Model_Kind RefSystemM=new Model_Kind("RefSystemM");
  public final static String tag_RefSystemM="RefSystemM";
  static public Model_Kind SymbologyM=new Model_Kind("SymbologyM");
  public final static String tag_SymbologyM="SymbologyM";
}
