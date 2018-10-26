package ch.interlis.models.IlisMeta16.ModelData;
public class AttrOrParam extends ch.interlis.models.IlisMeta16.ModelData.ExtendableME
{
  public final static String tag= "IlisMeta16.ModelData.AttrOrParam";
  public AttrOrParam(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_SubdivisionKind="SubdivisionKind";
  public AttrOrParam_SubdivisionKind getSubdivisionKind() {
    String value=getattrvalue("SubdivisionKind");
    return AttrOrParam_SubdivisionKind.parseXmlCode(value);
  }
  public void setSubdivisionKind(AttrOrParam_SubdivisionKind value) {
    setattrvalue("SubdivisionKind", AttrOrParam_SubdivisionKind.toXmlCode(value));
  }
  public final static String tag_Transient="Transient";
  public boolean getTransient() {
    String value=getattrvalue("Transient");
    return value!=null && value.equals("true");
  }
  public void setTransient(boolean value) {
    setattrvalue("Transient", value?"true":"false");
  }
  public final static String tag_Derivates="Derivates";
  public int sizeDerivates() {return getattrvaluecount("Derivates");}
  public ch.interlis.models.IlisMeta16.ModelData.Expression[] getDerivates() {
    int size=getattrvaluecount("Derivates");
    ch.interlis.models.IlisMeta16.ModelData.Expression value[]=new ch.interlis.models.IlisMeta16.ModelData.Expression[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta16.ModelData.Expression)getattrobj("Derivates",i);
    }
    return value;
  }
  public void addDerivates(ch.interlis.models.IlisMeta16.ModelData.Expression value) {
    addattrobj("Derivates", value);
  }
  public final static String tag_AttrParent="AttrParent";
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
  public final static String tag_ParamParent="ParamParent";
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
  public final static String tag_Type="Type";
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
