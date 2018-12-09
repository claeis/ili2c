package ch.interlis.models.IlisMeta07.ModelData;
public class EnumNode extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta07.ModelData.EnumNode";
  public EnumNode(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_EnumType="EnumType";
  public String getEnumType() {
    ch.interlis.iom.IomObject value=getattrobj("EnumType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setEnumType(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("EnumType","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
  public final static String tag_ParentNode="ParentNode";
  public String getParentNode() {
    ch.interlis.iom.IomObject value=getattrobj("ParentNode",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setParentNode(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ParentNode","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
}
