package ch.interlis.models.DatasetIdx16.DataIndex;
public class BasketMetadata extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.DataIndex.BasketMetadata";
  public BasketMetadata() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_id="id";
  /** Id of the basket e.g. "ch.bfe.kernkraftwerke.1".
   * relative to this repos.
   * DERIVED
   */
  public String getid() {
    String value=getattrvalue("id");
    return value;
  }
  /** Id of the basket e.g. "ch.bfe.kernkraftwerke.1".
   * relative to this repos.
   * DERIVED
   */
  public void setid(String value) {
    setattrvalue("id", value);
  }
  public final static String tag_originalId="originalId";
  /** Id of the basket, as known by the owner of the data.
   */
  public String getoriginalId() {
    String value=getattrvalue("originalId");
    return value;
  }
  /** Id of the basket, as known by the owner of the data.
   */
  public void setoriginalId(String value) {
    setattrvalue("originalId", value);
  }
  public final static String tag_version="version";
  /** Version of the data; e.g. "2016-11-14"
   */
  public String getversion() {
    String value=getattrvalue("version");
    return value;
  }
  /** Version of the data; e.g. "2016-11-14"
   */
  public void setversion(String value) {
    setattrvalue("version", value);
  }
  public final static String tag_versionComment="versionComment";
  /** Comment about this version (surch as "Draft")
   */
  public String getversionComment() {
    String value=getattrvalue("versionComment");
    return value;
  }
  /** Comment about this version (surch as "Draft")
   */
  public void setversionComment(String value) {
    setattrvalue("versionComment", value);
  }
  public final static String tag_precursorVersion="precursorVersion";
  /** Precursor version of this data e.g. "2015-10-18".
   * Must be in the same repository.
   */
  public String getprecursorVersion() {
    String value=getattrvalue("precursorVersion");
    return value;
  }
  /** Precursor version of this data e.g. "2015-10-18".
   * Must be in the same repository.
   */
  public void setprecursorVersion(String value) {
    setattrvalue("precursorVersion", value);
  }
  public final static String tag_followupData="followupData";
  /** Follow-up data of this data e.g. "ch.bfe.kernkraftwerke.1"
   * Might be in the different repository.
   */
  public int sizefollowupData() {return getattrvaluecount("followupData");}
  public ch.interlis.models.DatasetIdx16.BasketLink[] getfollowupData() {
    int size=getattrvaluecount("followupData");
    ch.interlis.models.DatasetIdx16.BasketLink value[]=new ch.interlis.models.DatasetIdx16.BasketLink[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.BasketLink)getattrobj("followupData",i);
    }
    return value;
  }
  /** Follow-up data of this data e.g. "ch.bfe.kernkraftwerke.1"
   * Might be in the different repository.
   */
  public void addfollowupData(ch.interlis.models.DatasetIdx16.BasketLink value) {
    addattrobj("followupData", value);
  }
  public final static String tag_derivedData="derivedData";
  /** any known derived data.
   * Might be in the different repository.
   */
  public int sizederivedData() {return getattrvaluecount("derivedData");}
  public ch.interlis.models.DatasetIdx16.BasketLink[] getderivedData() {
    int size=getattrvaluecount("derivedData");
    ch.interlis.models.DatasetIdx16.BasketLink value[]=new ch.interlis.models.DatasetIdx16.BasketLink[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.BasketLink)getattrobj("derivedData",i);
    }
    return value;
  }
  /** any known derived data.
   * Might be in the different repository.
   */
  public void addderivedData(ch.interlis.models.DatasetIdx16.BasketLink value) {
    addattrobj("derivedData", value);
  }
  public final static String tag_sourceData="sourceData";
  /** If this is derived data, the source of it.
   * Might be in the different repository.
   */
  public int sizesourceData() {return getattrvaluecount("sourceData");}
  public ch.interlis.models.DatasetIdx16.BasketLink[] getsourceData() {
    int size=getattrvaluecount("sourceData");
    ch.interlis.models.DatasetIdx16.BasketLink value[]=new ch.interlis.models.DatasetIdx16.BasketLink[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.BasketLink)getattrobj("sourceData",i);
    }
    return value;
  }
  /** If this is derived data, the source of it.
   * Might be in the different repository.
   */
  public void addsourceData(ch.interlis.models.DatasetIdx16.BasketLink value) {
    addattrobj("sourceData", value);
  }
  public final static String tag_model="model";
  /** machine readable model/schema of this data. Qualified TOPIC name
   * for INTERLIS data.
   * DERIVED
   */
  public ch.interlis.models.DatasetIdx16.ModelLink getmodel() {
    ch.interlis.models.DatasetIdx16.ModelLink value=(ch.interlis.models.DatasetIdx16.ModelLink)getattrobj("model",0);
    return value;
  }
  /** machine readable model/schema of this data. Qualified TOPIC name
   * for INTERLIS data.
   * DERIVED
   */
  public void setmodel(ch.interlis.models.DatasetIdx16.ModelLink value) {
    if(getattrvaluecount("model")>0){
      changeattrobj("model",0, value);
    }else{
      addattrobj("model", value);
    }
  }
  public final static String tag_epsgCode="epsgCode";
  /** Reference system (CRS) of the data in this basket.
   * Undefined for data without geometry (e.g. catalogs) or data that include geometry in multiple CRS.
   * DERIVED
   */
  public String getepsgCode() {
    String value=getattrvalue("epsgCode");
    return value;
  }
  /** Reference system (CRS) of the data in this basket.
   * Undefined for data without geometry (e.g. catalogs) or data that include geometry in multiple CRS.
   * DERIVED
   */
  public void setepsgCode(String value) {
    setattrvalue("epsgCode", value);
  }
  public final static String tag_resolutionScope="resolutionScope";
  /** Target level of detail as denmoniator of scale. (eg. 50000; the number below in 1:5000)
   */
  public int getresolutionScope() {
    String value=getattrvalue("resolutionScope");
    return Integer.parseInt(value);
  }
  /** Target level of detail as denmoniator of scale. (eg. 50000; the number below in 1:5000)
   */
  public void setresolutionScope(int value) {
    setattrvalue("resolutionScope", Integer.toString(value));
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
  public final static String tag_original="original";
  /** Where the owner of the data publishes it.
   * Absolute URL of repository (e.g. "http://data.geo.admin.ch/") or data-file ("http://data.geo.admin.ch/ch.bfe.kernkraftwerke/data.zip")
   */
  public String getoriginal() {
    String value=getattrvalue("original");
    return value;
  }
  /** Where the owner of the data publishes it.
   * Absolute URL of repository (e.g. "http://data.geo.admin.ch/") or data-file ("http://data.geo.admin.ch/ch.bfe.kernkraftwerke/data.zip")
   */
  public void setoriginal(String value) {
    setattrvalue("original", value);
  }
  public final static String tag_restrictions="restrictions";
  /** Restrictions (how to use/not use) on this data.
   */
  public int sizerestrictions() {return getattrvaluecount("restrictions");}
  public ch.interlis.models.DatasetIdx16.Code_[] getrestrictions() {
    int size=getattrvaluecount("restrictions");
    ch.interlis.models.DatasetIdx16.Code_ value[]=new ch.interlis.models.DatasetIdx16.Code_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.Code_)getattrobj("restrictions",i);
    }
    return value;
  }
  /** Restrictions (how to use/not use) on this data.
   */
  public void addrestrictions(ch.interlis.models.DatasetIdx16.Code_ value) {
    addattrobj("restrictions", value);
  }
  public final static String tag_qualityResults="qualityResults";
  /** results of quality checks.
   */
  public int sizequalityResults() {return getattrvaluecount("qualityResults");}
  public ch.interlis.models.DatasetIdx16.QualityResult[] getqualityResults() {
    int size=getattrvaluecount("qualityResults");
    ch.interlis.models.DatasetIdx16.QualityResult value[]=new ch.interlis.models.DatasetIdx16.QualityResult[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.QualityResult)getattrobj("qualityResults",i);
    }
    return value;
  }
  /** results of quality checks.
   */
  public void addqualityResults(ch.interlis.models.DatasetIdx16.QualityResult value) {
    addattrobj("qualityResults", value);
  }
  public final static String tag_boundary="boundary";
  /** bounding box of the actual available data.
   * DERIVED
   */
  public ch.interlis.models.DatasetIdx16.BoundingBox getboundary() {
    ch.interlis.models.DatasetIdx16.BoundingBox value=(ch.interlis.models.DatasetIdx16.BoundingBox)getattrobj("boundary",0);
    return value;
  }
  /** bounding box of the actual available data.
   * DERIVED
   */
  public void setboundary(ch.interlis.models.DatasetIdx16.BoundingBox value) {
    if(getattrvaluecount("boundary")>0){
      changeattrobj("boundary",0, value);
    }else{
      addattrobj("boundary", value);
    }
  }
}
