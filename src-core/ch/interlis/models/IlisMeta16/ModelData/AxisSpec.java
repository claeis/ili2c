package ch.interlis.models.IlisMeta16.ModelData;
public class AxisSpec extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.AxisSpec";
  public AxisSpec(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_CoordType="CoordType";
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
  public final static String tag_Axis="Axis";
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
