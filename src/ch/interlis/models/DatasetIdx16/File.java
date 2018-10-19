package ch.interlis.models.DatasetIdx16;
public class File extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.File";
  public File() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_path="path";
  public String getpath() {
    String value=getattrvalue("path");
    return value;
  }
  public void setpath(String value) {
    setattrvalue("path", value);
  }
  public final static String tag_md5="md5";
  public String getmd5() {
    String value=getattrvalue("md5");
    return value;
  }
  public void setmd5(String value) {
    setattrvalue("md5", value);
  }
}
