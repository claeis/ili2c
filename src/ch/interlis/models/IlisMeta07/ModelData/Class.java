package ch.interlis.models.IlisMeta07.ModelData;
public class Class extends ch.interlis.models.IlisMeta07.ModelData.Type
{
  private final static String tag= "IlisMeta07.ModelData.Class";
  public Class(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public Class_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return Class_Kind.parseXmlCode(value);
  }
  public void setKind(Class_Kind value) {
    setattrvalue("Kind", Class_Kind.toXmlCode(value));
  }
  public ch.interlis.models.IlisMeta07.ModelData.Multiplicity getMultiplicity() {
    ch.interlis.models.IlisMeta07.ModelData.Multiplicity value=(ch.interlis.models.IlisMeta07.ModelData.Multiplicity)getattrobj("Multiplicity",0);
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setMultiplicity(ch.interlis.models.IlisMeta07.ModelData.Multiplicity value) {
    if(getattrvaluecount("Multiplicity")>0){
      changeattrobj("Multiplicity",0, value);
    }else{
      addattrobj("Multiplicity", value);
    }
  }
  public ch.interlis.models.IlisMeta07.ModelData.Constraint[] getConstraints() {
    int size=getattrvaluecount("Constraints");
    if(size==0)throw new IllegalStateException();
    ch.interlis.models.IlisMeta07.ModelData.Constraint value[]=new ch.interlis.models.IlisMeta07.ModelData.Constraint[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.IlisMeta07.ModelData.Constraint)getattrobj("Constraints",i);
    }
    return value;
  }
  public void addConstraints(ch.interlis.models.IlisMeta07.ModelData.Constraint value) {
    addattrobj("Constraints", value);
  }
  public boolean getEmbeddedRoleTransfer() {
    String value=getattrvalue("EmbeddedRoleTransfer");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setEmbeddedRoleTransfer(boolean value) {
    setattrvalue("EmbeddedRoleTransfer", value?"true":"false");
  }
  public boolean getili1OptionalTable() {
    String value=getattrvalue("ili1OptionalTable");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setili1OptionalTable(boolean value) {
    setattrvalue("ili1OptionalTable", value?"true":"false");
  }
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
