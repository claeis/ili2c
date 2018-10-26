package ch.interlis.models.IlisMeta07.ModelData;
public class Role_Strongness{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private Role_Strongness(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(Role_Strongness value) {
     return value.value;
  }
  static public Role_Strongness parseXmlCode(String value) {
     return (Role_Strongness)valuev.get(value);
  }
  static public Role_Strongness Assoc=new Role_Strongness("Assoc");
  public final static String tag_Assoc="Assoc";
  static public Role_Strongness Aggr=new Role_Strongness("Aggr");
  public final static String tag_Aggr="Aggr";
  static public Role_Strongness Comp=new Role_Strongness("Comp");
  public final static String tag_Comp="Comp";
}
