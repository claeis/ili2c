package ch.interlis.models.IlisMeta07.ModelData;
public class EnumTreeValueType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.EnumTreeValueType";
  public EnumTreeValueType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ET="ET";
  public String getET() {
    ch.interlis.iom.IomObject value=getattrobj("ET",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setET(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ET","REF");
    structvalue.setobjectrefoid(oid);
  }
}
