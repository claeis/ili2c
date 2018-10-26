package ch.interlis.models.IlisMeta07.ModelData;
public class RuntimeParamRef extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  public final static String tag= "IlisMeta07.ModelData.RuntimeParamRef";
  public RuntimeParamRef() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_RuntimeParam="RuntimeParam";
  public String getRuntimeParam() {
    ch.interlis.iom.IomObject value=getattrobj("RuntimeParam",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRuntimeParam(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("RuntimeParam","REF");
    structvalue.setobjectrefoid(oid);
  }
}
