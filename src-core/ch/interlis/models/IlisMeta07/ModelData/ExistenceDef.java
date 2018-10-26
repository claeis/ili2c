package ch.interlis.models.IlisMeta07.ModelData;
public class ExistenceDef extends ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor
{
  public final static String tag= "IlisMeta07.ModelData.ExistenceDef";
  public ExistenceDef() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Viewable="Viewable";
  public String getViewable() {
    ch.interlis.iom.IomObject value=getattrobj("Viewable",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setViewable(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Viewable","REF");
    structvalue.setobjectrefoid(oid);
  }
}
