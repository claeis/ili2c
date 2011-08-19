package ch.interlis.models.IlisMeta07.ModelData;
public class FunctionCall extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  private final static String tag= "IlisMeta07.ModelData.FunctionCall";
  public FunctionCall() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public String getFunction() {
    ch.interlis.iom.IomObject value=getattrobj("Function",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setFunction(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Function","REF");
    structvalue.setobjectrefoid(oid);
  }
  public ch.interlis.models.IlisMeta07.ModelData.ActualArgument[] getArguments() {
    int size=getattrvaluecount("Arguments");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.ActualArgument value[]=new ch.interlis.models.IlisMeta07.ModelData.ActualArgument[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.ActualArgument)getattrobj("Arguments",i);
    }
    return value;
  }
  public void addArguments(ch.interlis.models.IlisMeta07.ModelData.ActualArgument value) {
    addattrobj("Arguments", value);
  }
}
