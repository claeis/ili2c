package ch.interlis.models.IlisMeta16.ModelData;
public class ARefRestriction extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ARefRestriction";
  public ARefRestriction(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ARef="ARef";
  public String getARef() {
    ch.interlis.iom.IomObject value=getattrobj("ARef",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setARef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ARef","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_Type="Type";
  public String getType() {
    ch.interlis.iom.IomObject value=getattrobj("Type",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Type","REF");
    structvalue.setobjectrefoid(oid);
  }
}
