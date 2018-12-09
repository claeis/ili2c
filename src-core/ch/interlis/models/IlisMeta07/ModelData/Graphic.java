package ch.interlis.models.IlisMeta07.ModelData;
public class Graphic extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta07.ModelData.Graphic";
  public Graphic(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Where="Where";
  public ch.interlis.models.IlisMeta07.ModelData.Expression getWhere() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Where",0);
    return value;
  }
  public void setWhere(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("Where")>0){
      changeattrobj("Where",0, value);
    }else{
      addattrobj("Where", value);
    }
  }
  public final static String tag_Base="Base";
  public String getBase() {
    ch.interlis.iom.IomObject value=getattrobj("Base",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setBase(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Base","REF");
    structvalue.setobjectrefoid(oid);
  }
}
