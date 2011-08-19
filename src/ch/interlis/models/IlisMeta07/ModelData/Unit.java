package ch.interlis.models.IlisMeta07.ModelData;
public class Unit extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.Unit";
  public Unit(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public Unit_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return Unit_Kind.parseXmlCode(value);
  }
  public void setKind(Unit_Kind value) {
    setattrvalue("Kind", Unit_Kind.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression getDefinition() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Definition",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setDefinition(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("Definition")>0){
      changeattrobj("Definition",0, value);
    }else{
      addattrobj("Definition", value);
    }
  }
}
