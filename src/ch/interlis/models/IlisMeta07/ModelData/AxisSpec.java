package ch.interlis.models.IlisMeta07.ModelData;
public class AxisSpec extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.AxisSpec";
  public AxisSpec(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getCoordType() {
    ch.interlis.iom.IomObject value=getattrobj("CoordType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setCoordType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("CoordType","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getAxis() {
    ch.interlis.iom.IomObject value=getattrobj("Axis",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAxis(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Axis","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
}
