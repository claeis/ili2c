package ch.interlis.models.IlisMeta07.ModelData;
public class ReferenceType extends ch.interlis.models.IlisMeta07.ModelData.ClassRelatedType
{
  public final static String tag= "IlisMeta07.ModelData.ReferenceType";
  public ReferenceType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_External="External";
  public boolean getExternal() {
    String value=getattrvalue("External");
    return value!=null && value.equals("true");
  }
  public void setExternal(boolean value) {
    setattrvalue("External", value?"true":"false");
  }
}
