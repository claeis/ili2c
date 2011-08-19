package ch.interlis.models.IlisMeta07.ModelData;
public class CondSignParamAssignment extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.CondSignParamAssignment";
  public CondSignParamAssignment() {
    super(tag,null);
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
  public ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment[] getAssignments() {
    int size=getattrvaluecount("Assignments");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment value[]=new ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment)getattrobj("Assignments",i);
    }
    return value;
  }
  public void addAssignments(ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment value) {
    addattrobj("Assignments", value);
  }
}
