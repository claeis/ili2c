package ch.interlis.models.DatasetIdx16;
public class BasketLink extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.BasketLink";
  public BasketLink() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_id="id";
  public String getid() {
    String value=getattrvalue("id");
    return value;
  }
  public void setid(String value) {
    setattrvalue("id", value);
  }
  public final static String tag_repository="repository";
  public String getrepository() {
    String value=getattrvalue("repository");
    return value;
  }
  public void setrepository(String value) {
    setattrvalue("repository", value);
  }
}
