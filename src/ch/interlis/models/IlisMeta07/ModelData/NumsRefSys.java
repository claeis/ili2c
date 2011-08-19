package ch.interlis.models.IlisMeta07.ModelData;
public class NumsRefSys extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.NumsRefSys";
  public NumsRefSys(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public int getAxis() {
    String value=getattrvalue("Axis");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setAxis(int value) {
    setattrvalue("Axis", Integer.toString(value));
  }
}
