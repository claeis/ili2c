package ch.interlis.models.IlisMeta07.ModelData;
public class DomainType extends ch.interlis.models.IlisMeta07.ModelData.Type
{
  private final static String tag= "IlisMeta07.ModelData.DomainType";
  public DomainType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getMandatory() {
    String value=getattrvalue("Mandatory");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setMandatory(boolean value) {
    setattrvalue("Mandatory", value?"true":"false");
  }
}
