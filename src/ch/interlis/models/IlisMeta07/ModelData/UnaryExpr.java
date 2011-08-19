package ch.interlis.models.IlisMeta07.ModelData;
public class UnaryExpr extends ch.interlis.models.IlisMeta07.ModelData.Expression
{
  private final static String tag= "IlisMeta07.ModelData.UnaryExpr";
  public UnaryExpr() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public UnaryExpr_Operation getOperation() {
    String value=getattrvalue("Operation");
    if(value==null)throw new IllegalStateException();
    return UnaryExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(UnaryExpr_Operation value) {
    setattrvalue("Operation", UnaryExpr_Operation.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression getSubExpression() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("SubExpression",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setSubExpression(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("SubExpression")>0){
      changeattrobj("SubExpression",0, value);
    }else{
      addattrobj("SubExpression", value);
    }
  }
}
