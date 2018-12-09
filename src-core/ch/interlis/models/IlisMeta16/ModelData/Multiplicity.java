package ch.interlis.models.IlisMeta16.ModelData;
public class Multiplicity extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Multiplicity";
  public Multiplicity() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Min="Min";
  public int getMin() {
    String value=getattrvalue("Min");
    return Integer.parseInt(value);
  }
  public void setMin(int value) {
    setattrvalue("Min", Integer.toString(value));
  }
  public final static String tag_Max="Max";
  public int getMax() {
    String value=getattrvalue("Max");
    return Integer.parseInt(value);
  }
  public void setMax(int value) {
    setattrvalue("Max", Integer.toString(value));
  }
}
