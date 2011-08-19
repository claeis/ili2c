package ch.interlis.models.IlisMeta07.ModelData;
public class MetaElement extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.MetaElement";
  public MetaElement(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getName() {
    String value=getattrvalue("Name");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  public ch.interlis.models.IlisMeta07.ModelData.DocText[] getDocumentation() {
    int size=getattrvaluecount("Documentation");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.DocText value[]=new ch.interlis.models.IlisMeta07.ModelData.DocText[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.DocText)getattrobj("Documentation",i);
    }
    return value;
  }
  public void addDocumentation(ch.interlis.models.IlisMeta07.ModelData.DocText value) {
    addattrobj("Documentation", value);
  }
  public String getElementInPackage() {
    ch.interlis.iom.IomObject value=getattrobj("ElementInPackage",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setElementInPackage(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ElementInPackage","REF");
    structvalue.setobjectrefoid(oid);
  }
}
