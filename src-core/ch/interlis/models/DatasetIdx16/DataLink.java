package ch.interlis.models.DatasetIdx16;
public class DataLink extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.DataLink";
  public DataLink() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_basketId="basketId";
  public String getbasketId() {
    String value=getattrvalue("basketId");
    return value;
  }
  public void setbasketId(String value) {
    setattrvalue("basketId", value);
  }
  public final static String tag_datasetId="datasetId";
  public String getdatasetId() {
    String value=getattrvalue("datasetId");
    return value;
  }
  public void setdatasetId(String value) {
    setattrvalue("datasetId", value);
  }
  public final static String tag_localBasketId="localBasketId";
  public String getlocalBasketId() {
    String value=getattrvalue("localBasketId");
    return value;
  }
  public void setlocalBasketId(String value) {
    setattrvalue("localBasketId", value);
  }
}
