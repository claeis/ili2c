package ch.interlis.models.IlisMeta07.ModelData;
public class Multiplicity extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.Multiplicity";
  public Multiplicity() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public int getMin() {
    String value=getattrvalue("Min");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setMin(int value) {
    setattrvalue("Min", Integer.toString(value));
  }
  public int getMax() {
    String value=getattrvalue("Max");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setMax(int value) {
    setattrvalue("Max", Integer.toString(value));
  }
}
