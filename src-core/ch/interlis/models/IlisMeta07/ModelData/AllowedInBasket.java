package ch.interlis.models.IlisMeta07.ModelData;
public class AllowedInBasket extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.AllowedInBasket";
  public AllowedInBasket(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_OfDataUnit="OfDataUnit";
  public String getOfDataUnit() {
    ch.interlis.iom.IomObject value=getattrobj("OfDataUnit",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setOfDataUnit(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("OfDataUnit","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_ClassInBasket="ClassInBasket";
  public String getClassInBasket() {
    ch.interlis.iom.IomObject value=getattrobj("ClassInBasket",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setClassInBasket(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ClassInBasket","REF");
    structvalue.setobjectrefoid(oid);
  }
}
