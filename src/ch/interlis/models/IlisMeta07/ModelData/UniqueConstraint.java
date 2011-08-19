package ch.interlis.models.IlisMeta07.ModelData;
public class UniqueConstraint extends ch.interlis.models.IlisMeta07.ModelData.Constraint
{
  private final static String tag= "IlisMeta07.ModelData.UniqueConstraint";
  public UniqueConstraint() {
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
  public UniqueConstraint_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return UniqueConstraint_Kind.parseXmlCode(value);
  }
  public void setKind(UniqueConstraint_Kind value) {
    setattrvalue("Kind", UniqueConstraint_Kind.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor[] getUniqueDef() {
    int size=getattrvaluecount("UniqueDef");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value[]=new ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor)getattrobj("UniqueDef",i);
    }
    return value;
  }
  public void addUniqueDef(ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor value) {
    addattrobj("UniqueDef", value);
  }
}
