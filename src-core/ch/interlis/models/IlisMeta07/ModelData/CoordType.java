package ch.interlis.models.IlisMeta07.ModelData;
public class CoordType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.CoordType";
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
}
