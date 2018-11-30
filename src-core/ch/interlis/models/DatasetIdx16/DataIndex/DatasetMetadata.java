package ch.interlis.models.DatasetIdx16.DataIndex;
public class DatasetMetadata extends ch.interlis.models.DatasetIdx16.Metadata
{
  public final static String tag= "DatasetIdx16.DataIndex.DatasetMetadata";
  public DatasetMetadata(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_title="title";
  /** Title of data e.g. "Amtliche Vermessung"
   */
  public ch.interlis.models.DatasetIdx16.MultilingualText gettitle() {
    ch.interlis.models.DatasetIdx16.MultilingualText value=(ch.interlis.models.DatasetIdx16.MultilingualText)getattrobj("title",0);
    return value;
  }
  /** Title of data e.g. "Amtliche Vermessung"
   */
  public void settitle(ch.interlis.models.DatasetIdx16.MultilingualText value) {
    if(getattrvaluecount("title")>0){
      changeattrobj("title",0, value);
    }else{
      addattrobj("title", value);
    }
  }
  public final static String tag_shortDescription="shortDescription";
  /** A short description of this data.
   */
  public ch.interlis.models.DatasetIdx16.MultilingualMText getshortDescription() {
    ch.interlis.models.DatasetIdx16.MultilingualMText value=(ch.interlis.models.DatasetIdx16.MultilingualMText)getattrobj("shortDescription",0);
    return value;
  }
  /** A short description of this data.
   */
  public void setshortDescription(ch.interlis.models.DatasetIdx16.MultilingualMText value) {
    if(getattrvaluecount("shortDescription")>0){
      changeattrobj("shortDescription",0, value);
    }else{
      addattrobj("shortDescription", value);
    }
  }
  public final static String tag_keywords="keywords";
  /** Comma seperated list of keywords/tags associated with this data e.g. "DM01"
   */
  public String getkeywords() {
    String value=getattrvalue("keywords");
    return value;
  }
  /** Comma seperated list of keywords/tags associated with this data e.g. "DM01"
   */
  public void setkeywords(String value) {
    setattrvalue("keywords", value);
  }
  public final static String tag_categories="categories";
  /** List of category codes associated with this data.
   */
  public int sizecategories() {return getattrvaluecount("categories");}
  public ch.interlis.models.DatasetIdx16.Code_[] getcategories() {
    int size=getattrvaluecount("categories");
    ch.interlis.models.DatasetIdx16.Code_ value[]=new ch.interlis.models.DatasetIdx16.Code_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.Code_)getattrobj("categories",i);
    }
    return value;
  }
  /** List of category codes associated with this data.
   */
  public void addcategories(ch.interlis.models.DatasetIdx16.Code_ value) {
    addattrobj("categories", value);
  }
  public final static String tag_servicer="servicer";
  /** Who manages/maintains this data e.g. "http://www.swisstopo.admin.ch/"
   */
  public String getservicer() {
    String value=getattrvalue("servicer");
    return value;
  }
  /** Who manages/maintains this data e.g. "http://www.swisstopo.admin.ch/"
   */
  public void setservicer(String value) {
    setattrvalue("servicer", value);
  }
  public final static String tag_technicalContact="technicalContact";
  /** Who should be adressed with technical questions e.g. "mailto:info@bfe.admin.ch"
   */
  public String gettechnicalContact() {
    String value=getattrvalue("technicalContact");
    return value;
  }
  /** Who should be adressed with technical questions e.g. "mailto:info@bfe.admin.ch"
   */
  public void settechnicalContact(String value) {
    setattrvalue("technicalContact", value);
  }
  public final static String tag_furtherInformation="furtherInformation";
  /** Reference to document or website with further information, for example UML diagrams, software etc.
   */
  public String getfurtherInformation() {
    String value=getattrvalue("furtherInformation");
    return value;
  }
  /** Reference to document or website with further information, for example UML diagrams, software etc.
   */
  public void setfurtherInformation(String value) {
    setattrvalue("furtherInformation", value);
  }
  public final static String tag_furtherMetadata="furtherMetadata";
  /** Reference to machine readable data with further information about the data
   */
  public String getfurtherMetadata() {
    String value=getattrvalue("furtherMetadata");
    return value;
  }
  /** Reference to machine readable data with further information about the data
   */
  public void setfurtherMetadata(String value) {
    setattrvalue("furtherMetadata", value);
  }
  public final static String tag_knownWMS="knownWMS";
  /** any known WMS that serves this data
   */
  public int sizeknownWMS() {return getattrvaluecount("knownWMS");}
  public ch.interlis.models.DatasetIdx16.WebService_[] getknownWMS() {
    int size=getattrvaluecount("knownWMS");
    ch.interlis.models.DatasetIdx16.WebService_ value[]=new ch.interlis.models.DatasetIdx16.WebService_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.WebService_)getattrobj("knownWMS",i);
    }
    return value;
  }
  /** any known WMS that serves this data
   */
  public void addknownWMS(ch.interlis.models.DatasetIdx16.WebService_ value) {
    addattrobj("knownWMS", value);
  }
  public final static String tag_knownWFS="knownWFS";
  /** any knwon WFS that serves this data
   */
  public int sizeknownWFS() {return getattrvaluecount("knownWFS");}
  public ch.interlis.models.DatasetIdx16.WebService_[] getknownWFS() {
    int size=getattrvaluecount("knownWFS");
    ch.interlis.models.DatasetIdx16.WebService_ value[]=new ch.interlis.models.DatasetIdx16.WebService_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.WebService_)getattrobj("knownWFS",i);
    }
    return value;
  }
  /** any knwon WFS that serves this data
   */
  public void addknownWFS(ch.interlis.models.DatasetIdx16.WebService_ value) {
    addattrobj("knownWFS", value);
  }
  public final static String tag_atomWS="atomWS";
  /** any known ATOM service that serves this data
   */
  public int sizeatomWS() {return getattrvaluecount("atomWS");}
  public ch.interlis.models.DatasetIdx16.WebService_[] getatomWS() {
    int size=getattrvaluecount("atomWS");
    ch.interlis.models.DatasetIdx16.WebService_ value[]=new ch.interlis.models.DatasetIdx16.WebService_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.WebService_)getattrobj("atomWS",i);
    }
    return value;
  }
  /** any known ATOM service that serves this data
   */
  public void addatomWS(ch.interlis.models.DatasetIdx16.WebService_ value) {
    addattrobj("atomWS", value);
  }
  public final static String tag_furtherWS="furtherWS";
  /** any known other type of WS that provides or processes this data
   */
  public int sizefurtherWS() {return getattrvaluecount("furtherWS");}
  public ch.interlis.models.DatasetIdx16.WebService_[] getfurtherWS() {
    int size=getattrvaluecount("furtherWS");
    ch.interlis.models.DatasetIdx16.WebService_ value[]=new ch.interlis.models.DatasetIdx16.WebService_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.WebService_)getattrobj("furtherWS",i);
    }
    return value;
  }
  /** any known other type of WS that provides or processes this data
   */
  public void addfurtherWS(ch.interlis.models.DatasetIdx16.WebService_ value) {
    addattrobj("furtherWS", value);
  }
  public final static String tag_knownPortal="knownPortal";
  /** any known portal where a user could view or download this data
   */
  public int sizeknownPortal() {return getattrvaluecount("knownPortal");}
  public ch.interlis.models.DatasetIdx16.WebSite_[] getknownPortal() {
    int size=getattrvaluecount("knownPortal");
    ch.interlis.models.DatasetIdx16.WebSite_ value[]=new ch.interlis.models.DatasetIdx16.WebSite_[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.WebSite_)getattrobj("knownPortal",i);
    }
    return value;
  }
  /** any known portal where a user could view or download this data
   */
  public void addknownPortal(ch.interlis.models.DatasetIdx16.WebSite_ value) {
    addattrobj("knownPortal", value);
  }
  public final static String tag_files="files";
  /** the data (one entry per file-format)
   */
  public int sizefiles() {return getattrvaluecount("files");}
  public ch.interlis.models.DatasetIdx16.DataFile[] getfiles() {
    int size=getattrvaluecount("files");
    ch.interlis.models.DatasetIdx16.DataFile value[]=new ch.interlis.models.DatasetIdx16.DataFile[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.DataFile)getattrobj("files",i);
    }
    return value;
  }
  /** the data (one entry per file-format)
   */
  public void addfiles(ch.interlis.models.DatasetIdx16.DataFile value) {
    addattrobj("files", value);
  }
  public final static String tag_baskets="baskets";
  /** Basktes that togethere are this dataset.
   */
  public int sizebaskets() {return getattrvaluecount("baskets");}
  public ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata[] getbaskets() {
    int size=getattrvaluecount("baskets");
    ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata value[]=new ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata)getattrobj("baskets",i);
    }
    return value;
  }
  /** Basktes that togethere are this dataset.
   */
  public void addbaskets(ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata value) {
    addattrobj("baskets", value);
  }
}
