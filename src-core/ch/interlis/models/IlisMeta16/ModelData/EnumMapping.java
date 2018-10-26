package ch.interlis.models.IlisMeta16.ModelData;
public class EnumMapping extends ch.interlis.models.IlisMeta16.ModelData.Factor
{
  public final static String tag= "IlisMeta16.ModelData.EnumMapping";
  public EnumMapping() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_EnumValue="EnumValue";
  public ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor getEnumValue() {
    ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value=(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor)getattrobj("EnumValue",0);
    return value;
  }
  public void setEnumValue(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor value) {
    if(getattrvaluecount("EnumValue")>0){
      changeattrobj("EnumValue",0, value);
    }else{
      addattrobj("EnumValue", value);
    }
  }
  public final static String tag_Cases="Cases";
  public int sizeCases() {return getattrvaluecount("Cases");}
  public ch.interlis.models.IlisMeta16.ModelData.EnumAssignment[] getCases() {
    int size=getattrvaluecount("Cases");
    ch.interlis.models.IlisMeta16.ModelData.EnumAssignment value[]=new ch.interlis.models.IlisMeta16.ModelData.EnumAssignment[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.EnumAssignment)getattrobj("Cases",i);
    }
    return value;
  }
  public void addCases(ch.interlis.models.IlisMeta16.ModelData.EnumAssignment value) {
    addattrobj("Cases", value);
  }
}
