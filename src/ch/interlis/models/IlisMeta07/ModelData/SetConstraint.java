package ch.interlis.models.IlisMeta07.ModelData;
public class SetConstraint extends ch.interlis.models.IlisMeta07.ModelData.Constraint
{
  private final static String tag= "IlisMeta07.ModelData.SetConstraint";
  public SetConstraint() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression getWhere() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Where",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setWhere(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("Where")>0){
      changeattrobj("Where",0, value);
    }else{
      addattrobj("Where", value);
    }
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression getConstraint() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Constraint",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setConstraint(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("Constraint")>0){
      changeattrobj("Constraint",0, value);
    }else{
      addattrobj("Constraint", value);
    }
  }
}
