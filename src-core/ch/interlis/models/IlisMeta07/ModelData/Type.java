package ch.interlis.models.IlisMeta07.ModelData;
public class Type extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta07.ModelData.Type";
  public Type(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_LFTParent="LFTParent";
  public String getLFTParent() {
    ch.interlis.iom.IomObject value=getattrobj("LFTParent",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setLFTParent(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("LFTParent","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_LTParent="LTParent";
  public String getLTParent() {
    ch.interlis.iom.IomObject value=getattrobj("LTParent",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setLTParent(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("LTParent","REF");
    structvalue.setobjectrefoid(oid);
  }
}
