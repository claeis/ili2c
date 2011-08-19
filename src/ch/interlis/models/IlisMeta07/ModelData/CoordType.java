package ch.interlis.models.IlisMeta07.ModelData;
public class CoordType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  private final static String tag= "IlisMeta07.ModelData.CoordType";
  public CoordType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public int getNullAxis() {
    String value=getattrvalue("NullAxis");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setNullAxis(int value) {
    setattrvalue("NullAxis", Integer.toString(value));
  }
  public int getPiHalfAxis() {
    String value=getattrvalue("PiHalfAxis");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setPiHalfAxis(int value) {
    setattrvalue("PiHalfAxis", Integer.toString(value));
  }
}
