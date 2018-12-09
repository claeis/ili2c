package ch.interlis.models.DatasetIdx16.DataIndex;
public class BasketMetadata extends ch.interlis.models.DatasetIdx16.Metadata
{
  public final static String tag= "DatasetIdx16.DataIndex.BasketMetadata";
  public BasketMetadata() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_localId="localId";
  /** Id (transient or stable) of the basket, that is local (unique) to the dataset.
   */
  public String getlocalId() {
    String value=getattrvalue("localId");
    return value;
  }
  /** Id (transient or stable) of the basket, that is local (unique) to the dataset.
   */
  public void setlocalId(String value) {
    setattrvalue("localId", value);
  }
}
