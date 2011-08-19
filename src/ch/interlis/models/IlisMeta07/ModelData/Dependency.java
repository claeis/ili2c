package ch.interlis.models.IlisMeta07.ModelData;
public class Dependency extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.Dependency";
  public Dependency(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getUsing() {
    ch.interlis.iom.IomObject value=getattrobj("Using",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setUsing(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Using","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getDependent() {
    ch.interlis.iom.IomObject value=getattrobj("Dependent",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setDependent(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Dependent","REF");
    structvalue.setobjectrefoid(oid);
  }
}
