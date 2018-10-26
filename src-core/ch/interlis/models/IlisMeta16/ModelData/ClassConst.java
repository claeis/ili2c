package ch.interlis.models.IlisMeta16.ModelData;
public class ClassConst extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.ClassConst";
  public ClassConst() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag__class="Class";
  public String get_class() {
    ch.interlis.iom.IomObject value=getattrobj("Class",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void set_class(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Class","REF");
    structvalue.setobjectrefoid(oid);
  }
}
