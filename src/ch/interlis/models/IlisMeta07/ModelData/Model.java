package ch.interlis.models.IlisMeta07.ModelData;
public class Model extends ch.interlis.models.IlisMeta07.ModelData.Package
{
  private final static String tag= "IlisMeta07.ModelData.Model";
  public Model(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getiliVersion() {
    String value=getattrvalue("iliVersion");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setiliVersion(String value) {
    setattrvalue("iliVersion", value);
  }
  public boolean getContracted() {
    String value=getattrvalue("Contracted");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setContracted(boolean value) {
    setattrvalue("Contracted", value?"true":"false");
  }
  public Model_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return Model_Kind.parseXmlCode(value);
  }
  public void setKind(Model_Kind value) {
    setattrvalue("Kind", Model_Kind.toXmlCode(value));
  }
  public String getLanguage() {
    String value=getattrvalue("Language");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setLanguage(String value) {
    setattrvalue("Language", value);
  }
  public String getAt() {
    String value=getattrvalue("At");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setAt(String value) {
    setattrvalue("At", value);
  }
  public String getVersion() {
    String value=getattrvalue("Version");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setVersion(String value) {
    setattrvalue("Version", value);
  }
  public String getili1Transfername() {
    String value=getattrvalue("ili1Transfername");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setili1Transfername(String value) {
    setattrvalue("ili1Transfername", value);
  }
  public ch.interlis.models.IlisMeta07.ModelData.Ili1Format getili1Format() {
    ch.interlis.models.IlisMeta07.ModelData.Ili1Format value=(ch.interlis.models.IlisMeta07.ModelData.Ili1Format)getattrobj("ili1Format",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setili1Format(ch.interlis.models.IlisMeta07.ModelData.Ili1Format value) {
    if(getattrvaluecount("ili1Format")>0){
      changeattrobj("ili1Format",0, value);
    }else{
      addattrobj("ili1Format", value);
    }
  }
}
