package ch.interlis.models.IlisMeta07.ModelData;
public class SimpleConstraint extends ch.interlis.models.IlisMeta07.ModelData.Constraint
{
  public final static String tag= "IlisMeta07.ModelData.SimpleConstraint";
  public SimpleConstraint() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public SimpleConstraint_Kind getKind() {
    String value=getattrvalue("Kind");
    return SimpleConstraint_Kind.parseXmlCode(value);
  }
  public void setKind(SimpleConstraint_Kind value) {
    setattrvalue("Kind", SimpleConstraint_Kind.toXmlCode(value));
  }
  public final static String tag_Percentage="Percentage";
  public double getPercentage() {
    String value=getattrvalue("Percentage");
    return Double.parseDouble(value);
  }
  public void setPercentage(double value) {
    setattrvalue("Percentage", Double.toString(value));
  }
  public final static String tag_LogicalExpression="LogicalExpression";
  public ch.interlis.models.IlisMeta07.ModelData.Expression getLogicalExpression() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("LogicalExpression",0);
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
