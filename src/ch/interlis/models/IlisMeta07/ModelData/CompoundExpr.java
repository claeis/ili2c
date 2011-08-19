package ch.interlis.models.IlisMeta07.ModelData;
public class CompoundExpr extends ch.interlis.models.IlisMeta07.ModelData.Expression
{
  private final static String tag= "IlisMeta07.ModelData.CompoundExpr";
  public CompoundExpr() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public CompoundExpr_Operation getOperation() {
    String value=getattrvalue("Operation");
    if(value==null)throw new IllegalStateException();
    return CompoundExpr_Operation.parseXmlCode(value);
  }
  public void setOperation(CompoundExpr_Operation value) {
    setattrvalue("Operation", CompoundExpr_Operation.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression[] getSubExpressions() {
    int size=getattrvaluecount("SubExpressions");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.Expression value[]=new ch.interlis.models.IlisMeta07.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("SubExpressions",i);
    }
    return value;
  }
  public void addSubExpressions(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    addattrobj("SubExpressions", value);
  }
}
