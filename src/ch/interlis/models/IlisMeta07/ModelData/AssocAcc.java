package ch.interlis.models.IlisMeta07.ModelData;
public class AssocAcc extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.AssocAcc";
  public AssocAcc(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
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
  public String getAssocAcc() {
    ch.interlis.iom.IomObject value=getattrobj("AssocAcc",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAssocAcc(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("AssocAcc","REF");
    structvalue.setobjectrefoid(oid);
  }
}
