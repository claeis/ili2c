package ch.interlis.models.IlisMeta16.ModelData;
public class CoordType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.CoordType";
  public CoordType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_NullAxis="NullAxis";
  public int getNullAxis() {
    String value=getattrvalue("NullAxis");
    return Integer.parseInt(value);
  }
  public void setNullAxis(int value) {
    setattrvalue("NullAxis", Integer.toString(value));
  }
  public final static String tag_PiHalfAxis="PiHalfAxis";
  public int getPiHalfAxis() {
    String value=getattrvalue("PiHalfAxis");
    return Integer.parseInt(value);
  }
  public void setPiHalfAxis(int value) {
    setattrvalue("PiHalfAxis", Integer.toString(value));
  }
  public final static String tag_Multi="Multi";
  public boolean getMulti() {
    String value=getattrvalue("Multi");
    return value!=null && value.equals("true");
  }
  public void setMulti(boolean value) {
    setattrvalue("Multi", value?"true":"false");
  }
}
