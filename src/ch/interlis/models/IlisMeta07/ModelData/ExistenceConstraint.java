package ch.interlis.models.IlisMeta07.ModelData;
public class ExistenceConstraint extends ch.interlis.models.IlisMeta07.ModelData.Constraint
{
  private final static String tag= "IlisMeta07.ModelData.ExistenceConstraint";
  public ExistenceConstraint() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor getAttr() {
	    ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value=(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor)getattrobj("Attr",0);
	    if(value==null)throw new IllegalStateException();
	    return value;
  }
  public void setAttr(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value) {
	    if(getattrvaluecount("Attr")>0){
	      changeattrobj("Attr",0, value);
	    }else{
	      addattrobj("Attr", value);
	    }
  }
  public ch.interlis.models.IlisMeta07.ModelData.ExistenceDef[] getExistsIn() {
    int size=getattrvaluecount("ExistsIn");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.ExistenceDef value[]=new ch.interlis.models.IlisMeta07.ModelData.ExistenceDef[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.ExistenceDef)getattrobj("ExistsIn",i);
    }
    return value;
  }
  public void addExistsIn(ch.interlis.models.IlisMeta07.ModelData.ExistenceDef value) {
    addattrobj("ExistsIn", value);
  }
}
