package ch.interlis.models.INTERLIS;
public class VALIGNMENT{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private VALIGNMENT(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(VALIGNMENT value) {
     return value.value;
  }
  static public VALIGNMENT parseXmlCode(String value) {
     return (VALIGNMENT)valuev.get(value);
  }
  static public VALIGNMENT Top=new VALIGNMENT("Top");
  static public VALIGNMENT Cap=new VALIGNMENT("Cap");
  static public VALIGNMENT Half=new VALIGNMENT("Half");
  static public VALIGNMENT Base=new VALIGNMENT("Base");
  static public VALIGNMENT Bottom=new VALIGNMENT("Bottom");
}
