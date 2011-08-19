package ch.interlis.models.IlisMeta07.ModelTranslation;
public class METranslation extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelTranslation.METranslation";
  public METranslation() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getOf() {
    ch.interlis.iom.IomObject value=getattrobj("Of",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setOf(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Of","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getTranslatedName() {
    String value=getattrvalue("TranslatedName");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setTranslatedName(String value) {
    setattrvalue("TranslatedName", value);
  }
  public ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation[] getTranslatedDoc() {
    int size=getattrvaluecount("TranslatedDoc");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation value[]=new ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation)getattrobj("TranslatedDoc",i);
    }
    return value;
  }
  public void addTranslatedDoc(ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation value) {
    addattrobj("TranslatedDoc", value);
  }
}
