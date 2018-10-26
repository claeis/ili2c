package ch.interlis.models.DatasetIdx16;
public class ModelLink extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.ModelLink";
  public ModelLink() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_name="name";
  public String getname() {
    String value=getattrvalue("name");
    return value;
  }
  public void setname(String value) {
    setattrvalue("name", value);
  }
  public final static String tag_locationHint="locationHint";
  public String getlocationHint() {
    String value=getattrvalue("locationHint");
    return value;
  }
  public void setlocationHint(String value) {
    setattrvalue("locationHint", value);
  }
  public final static String tag_versionHint="versionHint";
  public String getversionHint() {
    String value=getattrvalue("versionHint");
    return value;
  }
  public void setversionHint(String value) {
    setattrvalue("versionHint", value);
  }
}
