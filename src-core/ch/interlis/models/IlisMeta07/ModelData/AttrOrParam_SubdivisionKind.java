package ch.interlis.models.IlisMeta07.ModelData;
public class AttrOrParam_SubdivisionKind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private AttrOrParam_SubdivisionKind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(AttrOrParam_SubdivisionKind value) {
     return value.value;
  }
  static public AttrOrParam_SubdivisionKind parseXmlCode(String value) {
     return (AttrOrParam_SubdivisionKind)valuev.get(value);
  }
  static public AttrOrParam_SubdivisionKind NoSubDiv=new AttrOrParam_SubdivisionKind("NoSubDiv");
  public final static String tag_NoSubDiv="NoSubDiv";
  static public AttrOrParam_SubdivisionKind SubDiv=new AttrOrParam_SubdivisionKind("SubDiv");
  public final static String tag_SubDiv="SubDiv";
  static public AttrOrParam_SubdivisionKind ContSubDiv=new AttrOrParam_SubdivisionKind("ContSubDiv");
  public final static String tag_ContSubDiv="ContSubDiv";
}
