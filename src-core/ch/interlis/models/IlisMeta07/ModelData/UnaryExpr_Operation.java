package ch.interlis.models.IlisMeta07.ModelData;
public class UnaryExpr_Operation{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private UnaryExpr_Operation(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(UnaryExpr_Operation value) {
     return value.value;
  }
  static public UnaryExpr_Operation parseXmlCode(String value) {
     return (UnaryExpr_Operation)valuev.get(value);
  }
  static public UnaryExpr_Operation Not=new UnaryExpr_Operation("Not");
  public final static String tag_Not="Not";
  static public UnaryExpr_Operation Defined=new UnaryExpr_Operation("Defined");
  public final static String tag_Defined="Defined";
}
