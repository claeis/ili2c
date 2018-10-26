package ch.interlis.models.IlisMeta07.ModelData;
public class Import extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.Import";
  public Import(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ImportingP="ImportingP";
  public String getImportingP() {
    ch.interlis.iom.IomObject value=getattrobj("ImportingP",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setImportingP(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ImportingP","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_ImportedP="ImportedP";
  public String getImportedP() {
    ch.interlis.iom.IomObject value=getattrobj("ImportedP",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setImportedP(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ImportedP","REF");
    structvalue.setobjectrefoid(oid);
  }
}
