package ch.interlis.models.IlisMeta16.ModelData;
public class ClassRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ClassRestriction";
  public ClassRestriction(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_CRTR="CRTR";
  public String getCRTR() {
    ch.interlis.iom.IomObject value=getattrobj("CRTR",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setCRTR(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("CRTR","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_ClassRestriction="ClassRestriction";
  public String getClassRestriction() {
    ch.interlis.iom.IomObject value=getattrobj("ClassRestriction",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setClassRestriction(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ClassRestriction","REF");
    structvalue.setobjectrefoid(oid);
  }
}
