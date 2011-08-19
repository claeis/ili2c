package ch.interlis.models.IlisMeta07.ModelTranslation;
public class Translation extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelTranslation.Translation";
  public Translation(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getLanguage() {
    String value=getattrvalue("Language");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setLanguage(String value) {
    setattrvalue("Language", value);
  }
  public ch.interlis.models.IlisMeta07.ModelTranslation.METranslation[] getTranslations() {
    int size=getattrvaluecount("Translations");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelTranslation.METranslation value[]=new ch.interlis.models.IlisMeta07.ModelTranslation.METranslation[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelTranslation.METranslation)getattrobj("Translations",i);
    }
    return value;
  }
  public void addTranslations(ch.interlis.models.IlisMeta07.ModelTranslation.METranslation value) {
    addattrobj("Translations", value);
  }
}
