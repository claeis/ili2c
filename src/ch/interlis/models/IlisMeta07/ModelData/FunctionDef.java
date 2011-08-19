package ch.interlis.models.IlisMeta07.ModelData;
public class FunctionDef extends ch.interlis.models.IlisMeta07.ModelData.MetaElement
{
  private final static String tag= "IlisMeta07.ModelData.FunctionDef";
  public FunctionDef(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getExplanation() {
    String value=getattrvalue("Explanation");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setExplanation(String value) {
    setattrvalue("Explanation", value);
  }
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
