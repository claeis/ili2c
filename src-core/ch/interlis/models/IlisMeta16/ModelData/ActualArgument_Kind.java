package ch.interlis.models.IlisMeta16.ModelData;
public class ActualArgument_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private ActualArgument_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(ActualArgument_Kind value) {
     return value.value;
  }
  static public ActualArgument_Kind parseXmlCode(String value) {
     return (ActualArgument_Kind)valuev.get(value);
  }
  static public ActualArgument_Kind Expression=new ActualArgument_Kind("Expression");
  public final static String tag_Expression="Expression";
  static public ActualArgument_Kind AllOf=new ActualArgument_Kind("AllOf");
  public final static String tag_AllOf="AllOf";
}
