package ch.interlis.models.IlisMeta16.ModelData;
public class ClassRef extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ClassRef";
  public ClassRef() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Ref="Ref";
  public String getRef() {
    ch.interlis.iom.IomObject value=getattrobj("Ref",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Ref","REF");
    structvalue.setobjectrefoid(oid);
  }
}
