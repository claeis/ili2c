package ch.interlis.models.DatasetIdx16.DataIndex;
public class Metadata extends ch.interlis.models.DatasetIdx16.Metadata
{
  public final static String tag= "DatasetIdx16.DataIndex.Metadata";
  public Metadata(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_technicalContact="technicalContact";
  /** Who should be adressed with technical questions e.g. "mailto:support@kogis.ch"
   */
  public String gettechnicalContact() {
    String value=getattrvalue("technicalContact");
    return value;
  }
  /** Who should be adressed with technical questions e.g. "mailto:support@kogis.ch"
   */
  public void settechnicalContact(String value) {
    setattrvalue("technicalContact", value);
  }
}
