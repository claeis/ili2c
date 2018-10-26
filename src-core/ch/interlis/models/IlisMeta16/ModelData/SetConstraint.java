package ch.interlis.models.IlisMeta16.ModelData;
public class SetConstraint extends ch.interlis.models.IlisMeta16.ModelData.Constraint
{
  public final static String tag= "IlisMeta16.ModelData.SetConstraint";
  public SetConstraint(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Where="Where";
  public ch.interlis.models.IlisMeta16.ModelData.Expression getWhere() {
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Where",0);
    return value;
  }
  public void setWhere(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Where")>0){
      changeattrobj("Where",0, value);
    }else{
      addattrobj("Where", value);
    }
  }
  public final static String tag_Constraint="Constraint";
  public ch.interlis.models.IlisMeta16.ModelData.Expression getConstraint() {
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Constraint",0);
    return value;
  }
  public void setConstraint(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("Constraint")>0){
      changeattrobj("Constraint",0, value);
    }else{
      addattrobj("Constraint", value);
    }
  }
}
