package ch.interlis.models.DatasetIdx16;
public class BoundingBox extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.BoundingBox";
  public BoundingBox() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_westlimit="westlimit";
  public double getwestlimit() {
    String value=getattrvalue("westlimit");
    return Double.parseDouble(value);
  }
  public void setwestlimit(double value) {
    setattrvalue("westlimit", Double.toString(value));
  }
  public final static String tag_southlimit="southlimit";
  public double getsouthlimit() {
    String value=getattrvalue("southlimit");
    return Double.parseDouble(value);
  }
  public void setsouthlimit(double value) {
    setattrvalue("southlimit", Double.toString(value));
  }
  public final static String tag_eastlimit="eastlimit";
  public double geteastlimit() {
    String value=getattrvalue("eastlimit");
    return Double.parseDouble(value);
  }
  public void seteastlimit(double value) {
    setattrvalue("eastlimit", Double.toString(value));
  }
  public final static String tag_northlimit="northlimit";
  public double getnorthlimit() {
    String value=getattrvalue("northlimit");
    return Double.parseDouble(value);
  }
  public void setnorthlimit(double value) {
    setattrvalue("northlimit", Double.toString(value));
  }
}
