package ch.interlis.models.DatasetIdx16;
public class QualityResult extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.QualityResult";
  public QualityResult() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_summary="summary";
  /** Validation result as a short summery.
   */
  public ch.interlis.models.DatasetIdx16.MultilingualText getsummary() {
    ch.interlis.models.DatasetIdx16.MultilingualText value=(ch.interlis.models.DatasetIdx16.MultilingualText)getattrobj("summary",0);
    return value;
  }
  /** Validation result as a short summery.
   */
  public void setsummary(ch.interlis.models.DatasetIdx16.MultilingualText value) {
    if(getattrvaluecount("summary")>0){
      changeattrobj("summary",0, value);
    }else{
      addattrobj("summary", value);
    }
  }
  public final static String tag_code="code";
  /** Validation result as a code.
   */
  public String getcode() {
    String value=getattrvalue("code");
    return value;
  }
  /** Validation result as a code.
   */
  public void setcode(String value) {
    setattrvalue("code", value);
  }
  public final static String tag_information="information";
  /** Reference to document or website with validation result.
   */
  public String getinformation() {
    String value=getattrvalue("information");
    return value;
  }
  /** Reference to document or website with validation result.
   */
  public void setinformation(String value) {
    setattrvalue("information", value);
  }
  public final static String tag_data="data";
  /** Reference to machine readable data with validation result.
   */
  public String getdata() {
    String value=getattrvalue("data");
    return value;
  }
  /** Reference to machine readable data with validation result.
   */
  public void setdata(String value) {
    setattrvalue("data", value);
  }
}
