package ch.interlis.models.IlisMeta07.ModelData;
public class LineForm extends ch.interlis.models.IlisMeta07.ModelData.MetaElement
{
  public final static String tag= "IlisMeta07.ModelData.LineForm";
  public LineForm(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Structure="Structure";
  public String getStructure() {
    ch.interlis.iom.IomObject value=getattrobj("Structure",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setStructure(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Structure","REF");
    structvalue.setobjectrefoid(oid);
  }
}
