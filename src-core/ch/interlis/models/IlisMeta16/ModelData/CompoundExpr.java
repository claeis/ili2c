package ch.interlis.models.IlisMeta16.ModelData;
public class CompoundExpr extends ch.interlis.models.IlisMeta16.ModelData.Expression
{
  public final static String tag= "IlisMeta16.ModelData.CompoundExpr";
  public CompoundExpr() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Operation="Operation";
  public CompoundExpr_Operation getOperation() {
    String value=getattrvalue("Operation");
    return CompoundExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(CompoundExpr_Operation value) {
    setattrvalue("Operation", CompoundExpr_Operation.toXmlCode(value));
  }
  public final static String tag_SubExpressions="SubExpressions";
  public int sizeSubExpressions() {return getattrvaluecount("SubExpressions");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getSubExpressions() {
    int size=getattrvaluecount("SubExpressions");
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("SubExpressions",i);
    }
    return value;
  }
  public void addSubExpressions(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("SubExpressions", value);
  }
}
