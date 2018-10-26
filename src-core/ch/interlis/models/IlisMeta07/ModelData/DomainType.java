package ch.interlis.models.IlisMeta07.ModelData;
public class DomainType extends ch.interlis.models.IlisMeta07.ModelData.Type
{
  public final static String tag= "IlisMeta07.ModelData.DomainType";
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
}
