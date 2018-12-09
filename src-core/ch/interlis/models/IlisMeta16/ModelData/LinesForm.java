package ch.interlis.models.IlisMeta16.ModelData;
public class LinesForm extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.LinesForm";
  public LinesForm(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_LineType="LineType";
  public String getLineType() {
    ch.interlis.iom.IomObject value=getattrobj("LineType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setLineType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("LineType","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_LineForm="LineForm";
  public String getLineForm() {
    ch.interlis.iom.IomObject value=getattrobj("LineForm",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setLineForm(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("LineForm","REF");
    structvalue.setobjectrefoid(oid);
  }
}
