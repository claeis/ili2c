package ch.interlis.models.IlisMeta16.ModelData;
public class Model extends ch.interlis.models.IlisMeta16.ModelData.Package
{
  public final static String tag= "IlisMeta16.ModelData.Model";
  public Model(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_iliVersion="iliVersion";
  public String getiliVersion() {
    String value=getattrvalue("iliVersion");
    return value;
  }
  public void setiliVersion(String value) {
    setattrvalue("iliVersion", value);
  }
  public final static String tag_Contracted="Contracted";
  public boolean getContracted() {
    String value=getattrvalue("Contracted");
    return value!=null && value.equals("true");
  }
  public void setContracted(boolean value) {
    setattrvalue("Contracted", value?"true":"false");
  }
  public final static String tag_Kind="Kind";
  public Model_Kind getKind() {
    String value=getattrvalue("Kind");
    return Model_Kind.parseXmlCode(value);
  }
  public void setKind(Model_Kind value) {
    setattrvalue("Kind", Model_Kind.toXmlCode(value));
  }
  public final static String tag_Language="Language";
  public String getLanguage() {
    String value=getattrvalue("Language");
    return value;
  }
  public void setLanguage(String value) {
    setattrvalue("Language", value);
  }
  public final static String tag_At="At";
  public String getAt() {
    String value=getattrvalue("At");
    return value;
  }
  public void setAt(String value) {
    setattrvalue("At", value);
  }
  public final static String tag_Version="Version";
  public String getVersion() {
    String value=getattrvalue("Version");
    return value;
  }
  public void setVersion(String value) {
    setattrvalue("Version", value);
  }
  public final static String tag_NoIncrementalTransfer="NoIncrementalTransfer";
  public boolean getNoIncrementalTransfer() {
    String value=getattrvalue("NoIncrementalTransfer");
    return value!=null && value.equals("true");
  }
  public void setNoIncrementalTransfer(boolean value) {
    setattrvalue("NoIncrementalTransfer", value?"true":"false");
  }
  public final static String tag_CharSetIANAName="CharSetIANAName";
  public String getCharSetIANAName() {
    String value=getattrvalue("CharSetIANAName");
    return value;
  }
  public void setCharSetIANAName(String value) {
    setattrvalue("CharSetIANAName", value);
  }
  public final static String tag_xmlns="xmlns";
  public String getxmlns() {
    String value=getattrvalue("xmlns");
    return value;
  }
  public void setxmlns(String value) {
    setattrvalue("xmlns", value);
  }
  public final static String tag_ili1Transfername="ili1Transfername";
  public String getili1Transfername() {
    String value=getattrvalue("ili1Transfername");
    return value;
  }
  public void setili1Transfername(String value) {
    setattrvalue("ili1Transfername", value);
  }
  public final static String tag_ili1Format="ili1Format";
  public ch.interlis.models.IlisMeta16.ModelData.Ili1Format getili1Format() {
    ch.interlis.models.IlisMeta16.ModelData.Ili1Format value=(ch.interlis.models.IlisMeta16.ModelData.Ili1Format)getattrobj("ili1Format",0);
    return value;
  }
  public void setili1Format(ch.interlis.models.IlisMeta16.ModelData.Ili1Format value) {
    if(getattrvaluecount("ili1Format")>0){
      changeattrobj("ili1Format",0, value);
    }else{
      addattrobj("ili1Format", value);
    }
  }
}
