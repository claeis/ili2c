package ch.interlis.models.IlisMeta07.ModelData;
public class MetaBasketDef extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.MetaBasketDef";
  public MetaBasketDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public MetaBasketDef_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return MetaBasketDef_Kind.parseXmlCode(value);
  }
  public void setKind(MetaBasketDef_Kind value) {
    setattrvalue("Kind", MetaBasketDef_Kind.toXmlCode(value));
  }
  public String getMetaDataTopic() {
    ch.interlis.iom.IomObject value=getattrobj("MetaDataTopic",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setMetaDataTopic(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("MetaDataTopic","REF");
    structvalue.setobjectrefoid(oid);
  }
}
