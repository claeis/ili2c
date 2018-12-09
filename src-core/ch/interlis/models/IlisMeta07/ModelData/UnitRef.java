package ch.interlis.models.IlisMeta07.ModelData;
public class UnitRef extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  public final static String tag= "IlisMeta07.ModelData.UnitRef";
  public UnitRef() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Unit="Unit";
  public String getUnit() {
    ch.interlis.iom.IomObject value=getattrobj("Unit",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setUnit(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Unit","REF");
    structvalue.setobjectrefoid(oid);
  }
}
