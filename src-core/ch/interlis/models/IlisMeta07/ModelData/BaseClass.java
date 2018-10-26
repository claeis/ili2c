package ch.interlis.models.IlisMeta07.ModelData;
public class BaseClass extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.BaseClass";
  public BaseClass(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_CRT="CRT";
  public String getCRT() {
    ch.interlis.iom.IomObject value=getattrobj("CRT",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setCRT(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("CRT","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_BaseClass="BaseClass";
  public String getBaseClass() {
    ch.interlis.iom.IomObject value=getattrobj("BaseClass",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setBaseClass(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("BaseClass","REF");
    structvalue.setobjectrefoid(oid);
  }
}
