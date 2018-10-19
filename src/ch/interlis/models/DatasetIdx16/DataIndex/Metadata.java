package ch.interlis.models.DatasetIdx16.DataIndex;
public class Metadata extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.DataIndex.Metadata";
  public Metadata(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_owner="owner";
  /** Who owns this metadata e.g. "http://www.kogis.ch/"
   */
  public String getowner() {
    String value=getattrvalue("owner");
    return value;
  }
  /** Who owns this metadata e.g. "http://www.kogis.ch/"
   */
  public void setowner(String value) {
    setattrvalue("owner", value);
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
  public final static String tag_publishingDate="publishingDate";
  /** date of last publication of this version e.g. "2004-06-04". This should change even with the most minor editorial changes.
   * DERIVED
   */
  public String getpublishingDate() {
    String value=getattrvalue("publishingDate");
    return value;
  }
  /** date of last publication of this version e.g. "2004-06-04". This should change even with the most minor editorial changes.
   * DERIVED
   */
  public void setpublishingDate(String value) {
    setattrvalue("publishingDate", value);
  }
  public final static String tag_lastEditingDate="lastEditingDate";
  /** date of last editing/change of this version e.g. "2004-06-04". This should change even with the most minor editorial changes.
   * DERIVED
   */
  public String getlastEditingDate() {
    String value=getattrvalue("lastEditingDate");
    return value;
  }
  /** date of last editing/change of this version e.g. "2004-06-04". This should change even with the most minor editorial changes.
   * DERIVED
   */
  public void setlastEditingDate(String value) {
    setattrvalue("lastEditingDate", value);
  }
}
