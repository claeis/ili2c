package ch.interlis.models.IlisMeta16.ModelData;
public class DomainType extends ch.interlis.models.IlisMeta16.ModelData.Type
{
  public final static String tag= "IlisMeta16.ModelData.DomainType";
  public DomainType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Mandatory="Mandatory";
  public boolean getMandatory() {
    String value=getattrvalue("Mandatory");
    return value!=null && value.equals("true");
  }
  public void setMandatory(boolean value) {
    setattrvalue("Mandatory", value?"true":"false");
  }
  public final static String tag_Context="Context";
  public String getContext() {
    ch.interlis.iom.IomObject value=getattrobj("Context",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setContext(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Context","REF");
    structvalue.setobjectrefoid(oid);
  }
}
