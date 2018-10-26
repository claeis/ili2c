package ch.interlis.models.IlisMeta16.ModelData;
public class Class extends ch.interlis.models.IlisMeta16.ModelData.Type
{
  public final static String tag= "IlisMeta16.ModelData.Class";
  public Class(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public Class_Kind getKind() {
    String value=getattrvalue("Kind");
    return Class_Kind.parseXmlCode(value);
  }
  public void setKind(Class_Kind value) {
    setattrvalue("Kind", Class_Kind.toXmlCode(value));
  }
  public final static String tag_Multiplicity="Multiplicity";
  public ch.interlis.models.IlisMeta16.ModelData.Multiplicity getMultiplicity() {
    ch.interlis.models.IlisMeta16.ModelData.Multiplicity value=(ch.interlis.models.IlisMeta16.ModelData.Multiplicity)getattrobj("Multiplicity",0);
    return value;
  }
  public void setMultiplicity(ch.interlis.models.IlisMeta16.ModelData.Multiplicity value) {
    if(getattrvaluecount("Multiplicity")>0){
      changeattrobj("Multiplicity",0, value);
    }else{
      addattrobj("Multiplicity", value);
    }
  }
  public final static String tag_EmbeddedRoleTransfer="EmbeddedRoleTransfer";
  public boolean getEmbeddedRoleTransfer() {
    String value=getattrvalue("EmbeddedRoleTransfer");
    return value!=null && value.equals("true");
  }
  public void setEmbeddedRoleTransfer(boolean value) {
    setattrvalue("EmbeddedRoleTransfer", value?"true":"false");
  }
  public final static String tag_ili1OptionalTable="ili1OptionalTable";
  public boolean getili1OptionalTable() {
    String value=getattrvalue("ili1OptionalTable");
    return value!=null && value.equals("true");
  }
  public void setili1OptionalTable(boolean value) {
    setattrvalue("ili1OptionalTable", value?"true":"false");
  }
  public final static String tag_Oid="Oid";
  public String getOid() {
    ch.interlis.iom.IomObject value=getattrobj("Oid",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setOid(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Oid","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_View="View";
  public String getView() {
    ch.interlis.iom.IomObject value=getattrobj("View",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setView(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("View","REF");
    structvalue.setobjectrefoid(oid);
  }
}
