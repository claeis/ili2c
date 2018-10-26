package ch.interlis.models.IlisMeta07.ModelData;
public class SignParamAssignment extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.SignParamAssignment";
  public SignParamAssignment() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Param="Param";
  public String getParam() {
    ch.interlis.iom.IomObject value=getattrobj("Param",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setParam(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Param","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_Assignment="Assignment";
  public ch.interlis.models.IlisMeta07.ModelData.Expression getAssignment() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Assignment",0);
    return value;
  }
  public void setAssignment(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("Assignment")>0){
      changeattrobj("Assignment",0, value);
    }else{
      addattrobj("Assignment", value);
    }
  }
}
