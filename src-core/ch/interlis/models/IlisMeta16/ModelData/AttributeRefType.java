package ch.interlis.models.IlisMeta16.ModelData;
public class AttributeRefType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.AttributeRefType";
  public AttributeRefType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Of="Of";
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
}
