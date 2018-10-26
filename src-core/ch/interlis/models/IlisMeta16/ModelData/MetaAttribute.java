package ch.interlis.models.IlisMeta16.ModelData;
public class MetaAttribute extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.MetaAttribute";
  public MetaAttribute(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Name="Name";
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  public final static String tag_Value="Value";
  public String getValue() {
    String value=getattrvalue("Value");
    return value;
  }
  public void setValue(String value) {
    setattrvalue("Value", value);
  }
  public final static String tag_MetaElement="MetaElement";
  public String getMetaElement() {
    ch.interlis.iom.IomObject value=getattrobj("MetaElement",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setMetaElement(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("MetaElement","REF");
    structvalue.setobjectrefoid(oid);
  }
}
