package ch.interlis.models.IlisMeta16.ModelData;
public class FunctionDef extends ch.interlis.models.IlisMeta16.ModelData.MetaElement
{
  public final static String tag= "IlisMeta16.ModelData.FunctionDef";
  public FunctionDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Explanation="Explanation";
  public String getExplanation() {
    String value=getattrvalue("Explanation");
    return value;
  }
  public void setExplanation(String value) {
    setattrvalue("Explanation", value);
  }
  public final static String tag_ResultType="ResultType";
  public String getResultType() {
    ch.interlis.iom.IomObject value=getattrobj("ResultType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setResultType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ResultType","REF");
    structvalue.setobjectrefoid(oid);
  }
}
