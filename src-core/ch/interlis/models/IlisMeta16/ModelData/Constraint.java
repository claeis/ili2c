package ch.interlis.models.IlisMeta16.ModelData;
public class Constraint extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.Constraint";
  public Constraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ToClass="ToClass";
  public String getToClass() {
    ch.interlis.iom.IomObject value=getattrobj("ToClass",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setToClass(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ToClass","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_ToDomain="ToDomain";
  public String getToDomain() {
    ch.interlis.iom.IomObject value=getattrobj("ToDomain",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setToDomain(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ToDomain","REF");
    structvalue.setobjectrefoid(oid);
  }
}
