package ch.interlis.models.IlisMeta16.ModelData;
public class MetaObjectDef extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.MetaObjectDef";
  public MetaObjectDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_IsRefSystem="IsRefSystem";
  public boolean getIsRefSystem() {
    String value=getattrvalue("IsRefSystem");
    return value!=null && value.equals("true");
  }
  public void setIsRefSystem(boolean value) {
    setattrvalue("IsRefSystem", value?"true":"false");
  }
  public final static String tag__class="Class";
  public String get_class() {
    ch.interlis.iom.IomObject value=getattrobj("Class",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void set_class(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Class","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_MetaBasketDef="MetaBasketDef";
  public String getMetaBasketDef() {
    ch.interlis.iom.IomObject value=getattrobj("MetaBasketDef",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setMetaBasketDef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("MetaBasketDef","REF");
    structvalue.setobjectrefoid(oid);
  }
}
