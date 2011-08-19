package ch.interlis.models.IlisMeta07.ModelData;
public class ReferenceType extends ch.interlis.models.IlisMeta07.ModelData.ClassRelatedType
{
  private final static String tag= "IlisMeta07.ModelData.ReferenceType";
  public ReferenceType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getExternal() {
    String value=getattrvalue("External");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setExternal(boolean value) {
    setattrvalue("External", value?"true":"false");
  }
}
