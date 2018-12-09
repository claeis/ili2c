package ch.interlis.models.IlisMeta07.ModelData;
public class Argument extends ch.interlis.models.IlisMeta07.ModelData.MetaElement
{
  public final static String tag= "IlisMeta07.ModelData.Argument";
  public Argument(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public Argument_Kind getKind() {
    String value=getattrvalue("Kind");
    return Argument_Kind.parseXmlCode(value);
  }
  public void setKind(Argument_Kind value) {
    setattrvalue("Kind", Argument_Kind.toXmlCode(value));
  }
  public final static String tag_Function="Function";
  public String getFunction() {
    ch.interlis.iom.IomObject value=getattrobj("Function",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setFunction(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Function","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
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
