package ch.interlis.models.IlisMeta16.ModelData;
public class UnaryExpr extends ch.interlis.models.IlisMeta16.ModelData.Expression
{
  public final static String tag= "IlisMeta16.ModelData.UnaryExpr";
  public UnaryExpr() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Operation="Operation";
  public UnaryExpr_Operation getOperation() {
    String value=getattrvalue("Operation");
    return UnaryExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(UnaryExpr_Operation value) {
    setattrvalue("Operation", UnaryExpr_Operation.toXmlCode(value));
  }
  public final static String tag_SubExpression="SubExpression";
  public ch.interlis.models.IlisMeta16.ModelData.Expression getSubExpression() {
    ch.interlis.models.IlisMeta16.ModelData.Expression value=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("SubExpression",0);
    return value;
  }
  public void setSubExpression(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    if(getattrvaluecount("SubExpression")>0){
      changeattrobj("SubExpression",0, value);
    }else{
      addattrobj("SubExpression", value);
    }
  }
}
