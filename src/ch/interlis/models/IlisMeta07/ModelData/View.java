package ch.interlis.models.IlisMeta07.ModelData;
public class View extends ch.interlis.models.IlisMeta07.ModelData.Class
{
  private final static String tag= "IlisMeta07.ModelData.View";
  public View(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public View_FormationKind getFormationKind() {
    String value=getattrvalue("FormationKind");
    if(value==null)throw new IllegalStateException();
    return View_FormationKind.parseXmlCode(value);
  }
  public void setFormationKind(View_FormationKind value) {
    setattrvalue("FormationKind", View_FormationKind.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression[] getFormationParameter() {
    int size=getattrvaluecount("FormationParameter");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.Expression value[]=new ch.interlis.models.IlisMeta07.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("FormationParameter",i);
    }
    return value;
  }
  public void addFormationParameter(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    addattrobj("FormationParameter", value);
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
  public boolean getTransient() {
    String value=getattrvalue("Transient");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setTransient(boolean value) {
    setattrvalue("Transient", value?"true":"false");
  }
}
