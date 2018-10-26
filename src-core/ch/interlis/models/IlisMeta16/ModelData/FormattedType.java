package ch.interlis.models.IlisMeta16.ModelData;
public class FormattedType extends ch.interlis.models.IlisMeta16.ModelData.NumType
{
  public final static String tag= "IlisMeta16.ModelData.FormattedType";
  public FormattedType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Format="Format";
  public String getFormat() {
    String value=getattrvalue("Format");
    return value;
  }
  public void setFormat(String value) {
    setattrvalue("Format", value);
  }
  public final static String tag_Struct="Struct";
  public String getStruct() {
    ch.interlis.iom.IomObject value=getattrobj("Struct",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setStruct(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Struct","REF");
    structvalue.setobjectrefoid(oid);
  }
}
