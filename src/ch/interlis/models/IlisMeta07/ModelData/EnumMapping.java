package ch.interlis.models.IlisMeta07.ModelData;
public class EnumMapping extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  private final static String tag= "IlisMeta07.ModelData.EnumMapping";
  public EnumMapping() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor getEnumValue() {
    ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value=(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor)getattrobj("EnumValue",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setEnumValue(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value) {
    if(getattrvaluecount("EnumValue")>0){
      changeattrobj("EnumValue",0, value);
    }else{
      addattrobj("EnumValue", value);
    }
  }
  public ch.interlis.models.IlisMeta07.ModelData.EnumAssignment[] getCases() {
    int size=getattrvaluecount("Cases");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.EnumAssignment value[]=new ch.interlis.models.IlisMeta07.ModelData.EnumAssignment[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.EnumAssignment)getattrobj("Cases",i);
    }
    return value;
  }
  public void addCases(ch.interlis.models.IlisMeta07.ModelData.EnumAssignment value) {
    addattrobj("Cases", value);
  }
}
