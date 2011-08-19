package ch.interlis.models.IlisMeta07.ModelData;
public class ObjectType extends ch.interlis.models.IlisMeta07.ModelData.ClassRelatedType
{
  private final static String tag= "IlisMeta07.ModelData.ObjectType";
  public ObjectType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getMultiple() {
    String value=getattrvalue("Multiple");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setMultiple(boolean value) {
    setattrvalue("Multiple", value?"true":"false");
  }
}
