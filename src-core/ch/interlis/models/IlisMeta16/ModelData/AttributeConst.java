package ch.interlis.models.IlisMeta16.ModelData;
public class AttributeConst extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.AttributeConst";
  public AttributeConst() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Attribute="Attribute";
  public String getAttribute() {
    ch.interlis.iom.IomObject value=getattrobj("Attribute",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAttribute(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Attribute","REF");
    structvalue.setobjectrefoid(oid);
  }
}
