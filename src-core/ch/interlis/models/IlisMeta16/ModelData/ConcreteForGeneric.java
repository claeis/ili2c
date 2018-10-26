package ch.interlis.models.IlisMeta16.ModelData;
public class ConcreteForGeneric extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.ConcreteForGeneric";
  public ConcreteForGeneric(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_GenericDef="GenericDef";
  public String getGenericDef() {
    ch.interlis.iom.IomObject value=getattrobj("GenericDef",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setGenericDef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("GenericDef","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_ConcreteDomain="ConcreteDomain";
  public String getConcreteDomain() {
    ch.interlis.iom.IomObject value=getattrobj("ConcreteDomain",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setConcreteDomain(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ConcreteDomain","REF");
    structvalue.setobjectrefoid(oid);
  }
}
