package ch.interlis.models.IlisMeta07.ModelData;
public class TypeRelatedType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.TypeRelatedType";
  public TypeRelatedType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_BaseType="BaseType";
  public String getBaseType() {
    ch.interlis.iom.IomObject value=getattrobj("BaseType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setBaseType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("BaseType","REF");
    structvalue.setobjectrefoid(oid);
  }
}
