package ch.interlis.models.IlisMeta07.ModelData;
public class TypeRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.TypeRestriction";
  public TypeRestriction(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_TRTR="TRTR";
  public String getTRTR() {
    ch.interlis.iom.IomObject value=getattrobj("TRTR",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setTRTR(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("TRTR","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_TypeRestriction="TypeRestriction";
  public String getTypeRestriction() {
    ch.interlis.iom.IomObject value=getattrobj("TypeRestriction",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setTypeRestriction(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("TypeRestriction","REF");
    structvalue.setobjectrefoid(oid);
  }
}
