package ch.interlis.models.IliRepository20;
public class ModelName_ extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IliRepository20.ModelName_";
  public ModelName_() {
    super(tag,null);
  }
  protected ModelName_(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_value="value";
  public String getvalue() {
    String value=getattrvalue("value");
    return value;
  }
  public void setvalue(String value) {
    setattrvalue("value", value);
  }
}
