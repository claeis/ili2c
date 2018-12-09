package ch.interlis.models.IlisMeta07.ModelData;
public class NumsRefSys extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.NumsRefSys";
  public NumsRefSys(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Axis="Axis";
  public int getAxis() {
    String value=getattrvalue("Axis");
    return Integer.parseInt(value);
  }
  public void setAxis(int value) {
    setattrvalue("Axis", Integer.toString(value));
  }
}
