package ch.interlis.models.IlisMeta07.ModelData;
public class DrawingRule extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.DrawingRule";
  public DrawingRule(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment[] getRule() {
    int size=getattrvaluecount("Rule");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment value[]=new ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment)getattrobj("Rule",i);
    }
    return value;
  }
  public void addRule(ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment value) {
    addattrobj("Rule", value);
  }
  public String get_class() {
    ch.interlis.iom.IomObject value=getattrobj("Class",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void set_class(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Class","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getGraphic() {
    ch.interlis.iom.IomObject value=getattrobj("Graphic",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setGraphic(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Graphic","REF");
    structvalue.setobjectrefoid(oid);
  }
}
