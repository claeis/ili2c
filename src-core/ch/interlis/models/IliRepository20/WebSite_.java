package ch.interlis.models.IliRepository20;
public class WebSite_ extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IliRepository20.WebSite_";
  public WebSite_() {
    super(tag,null);
  }
  protected WebSite_(String oid) {
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
