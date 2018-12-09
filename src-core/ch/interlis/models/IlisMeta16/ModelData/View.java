package ch.interlis.models.IlisMeta16.ModelData;
public class View extends ch.interlis.models.IlisMeta16.ModelData.Class
{
  public final static String tag= "IlisMeta16.ModelData.View";
  public View(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_FormationKind="FormationKind";
  public View_FormationKind getFormationKind() {
    String value=getattrvalue("FormationKind");
    return View_FormationKind.parseXmlCode(value);
  }
  public void setFormationKind(View_FormationKind value) {
    setattrvalue("FormationKind", View_FormationKind.toXmlCode(value));
  }
  public final static String tag_FormationParameter="FormationParameter";
  public int sizeFormationParameter() {return getattrvaluecount("FormationParameter");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getFormationParameter() {
    int size=getattrvaluecount("FormationParameter");
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("FormationParameter",i);
    }
    return value;
  }
  public void addFormationParameter(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("FormationParameter", value);
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
  public final static String tag_Transient="Transient";
  public boolean getTransient() {
    String value=getattrvalue("Transient");
    return value!=null && value.equals("true");
  }
  public void setTransient(boolean value) {
    setattrvalue("Transient", value?"true":"false");
  }
}
