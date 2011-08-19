package ch.interlis.models.IlisMeta07.ModelData;
public class AttrOrParam extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.AttrOrParam";
  public AttrOrParam(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public AttrOrParam_SubdivisionKind getSubdivisionKind() {
    String value=getattrvalue("SubdivisionKind");
    if(value==null)throw new IllegalStateException();
    return AttrOrParam_SubdivisionKind.parseXmlCode(value);
  }
  public void setSubdivisionKind(AttrOrParam_SubdivisionKind value) {
    setattrvalue("SubdivisionKind", AttrOrParam_SubdivisionKind.toXmlCode(value));
  }
  public boolean getTransient() {
    String value=getattrvalue("Transient");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setTransient(boolean value) {
    setattrvalue("Transient", value?"true":"false");
  }
  public ch.interlis.models.IlisMeta07.ModelData.Expression[] getDerivates() {
    int size=getattrvaluecount("Derivates");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.Expression value[]=new ch.interlis.models.IlisMeta07.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("Derivates",i);
    }
    return value;
  }
  public void addDerivates(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    addattrobj("Derivates", value);
  }
  public String getAttrParent() {
    ch.interlis.iom.IomObject value=getattrobj("AttrParent",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAttrParent(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("AttrParent","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
  public String getParamParent() {
    ch.interlis.iom.IomObject value=getattrobj("ParamParent",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setParamParent(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("ParamParent","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
  public String getType() {
    ch.interlis.iom.IomObject value=getattrobj("Type",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Type","REF");
    structvalue.setobjectrefoid(oid);
  }
}
