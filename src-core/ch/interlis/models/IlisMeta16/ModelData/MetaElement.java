package ch.interlis.models.IlisMeta16.ModelData;
public class MetaElement extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.MetaElement";
  public MetaElement(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Name="Name";
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  public void setName(String value) {
    setattrvalue("Name", value);
  }
  public final static String tag_Documentation="Documentation";
  public int sizeDocumentation() {return getattrvaluecount("Documentation");}
  public ch.interlis.models.IlisMeta16.ModelData.DocText[] getDocumentation() {
    int size=getattrvaluecount("Documentation");
    ch.interlis.models.IlisMeta16.ModelData.DocText value[]=new ch.interlis.models.IlisMeta16.ModelData.DocText[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.DocText)getattrobj("Documentation",i);
    }
    return value;
  }
  public void addDocumentation(ch.interlis.models.IlisMeta16.ModelData.DocText value) {
    addattrobj("Documentation", value);
  }
  public final static String tag_ElementInPackage="ElementInPackage";
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
