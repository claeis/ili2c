package ch.interlis.models.IlisMeta07.ModelData;
public class ExplicitAssocAccess extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.ExplicitAssocAccess";
  public ExplicitAssocAccess(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getAssocAccOf() {
    ch.interlis.iom.IomObject value=getattrobj("AssocAccOf",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAssocAccOf(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("AssocAccOf","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
  public String getOriginRole() {
    ch.interlis.iom.IomObject value=getattrobj("OriginRole",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setOriginRole(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("OriginRole","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getTargetRole() {
    ch.interlis.iom.IomObject value=getattrobj("TargetRole",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setTargetRole(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("TargetRole","REF");
    structvalue.setobjectrefoid(oid);
  }
}
