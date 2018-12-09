package ch.interlis.models.IlisMeta16.ModelData;
public class ExistenceConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.ExistenceConstraint";
  public ExistenceConstraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Attr="Attr";
  public ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor getAttr() {
    ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value=(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor)getattrobj("Attr",0);
    return value;
  }
  public void setAttr(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value) {
    if(getattrvaluecount("Attr")>0){
      changeattrobj("Attr",0, value);
    }else{
      addattrobj("Attr", value);
    }
  }
  public final static String tag_ExistsIn="ExistsIn";
  public int sizeExistsIn() {return getattrvaluecount("ExistsIn");}
  public ch.interlis.models.IlisMeta16.ModelData.ExistenceDef[] getExistsIn() {
    int size=getattrvaluecount("ExistsIn");
    ch.interlis.models.IlisMeta16.ModelData.ExistenceDef value[]=new ch.interlis.models.IlisMeta16.ModelData.ExistenceDef[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.ExistenceDef)getattrobj("ExistsIn",i);
    }
    return value;
  }
  public void addExistsIn(ch.interlis.models.IlisMeta16.ModelData.ExistenceDef value) {
    addattrobj("ExistsIn", value);
  }
}
