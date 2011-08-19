package ch.interlis.models.IlisMeta07.ModelData;
public class SimpleConstraint extends ch.interlis.models.IlisMeta07.ModelData.Constraint
{
  private final static String tag= "IlisMeta07.ModelData.SimpleConstraint";
  public SimpleConstraint() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public SimpleConstraint_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return SimpleConstraint_Kind.parseXmlCode(value);
  }
  public void setKind(SimpleConstraint_Kind value) {
    setattrvalue("Kind", SimpleConstraint_Kind.toXmlCode(value));
  }
  public double getPercentage() {
    String value=getattrvalue("Percentage");
    if(value==null)throw new IllegalStateException();
    return Double.parseDouble(value);
  }
  public void setPercentage(double value) {
    setattrvalue("Percentage", Double.toString(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression getLogicalExpression() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("LogicalExpression",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setLogicalExpression(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("LogicalExpression")>0){
      changeattrobj("LogicalExpression",0, value);
    }else{
      addattrobj("LogicalExpression", value);
    }
  }
}
