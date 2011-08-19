package ch.interlis.models.IlisMeta07.ModelData;
public class Role extends ch.interlis.models.IlisMeta07.ModelData.ReferenceType
{
  private final static String tag= "IlisMeta07.ModelData.Role";
  public Role(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public Role_Strongness getStrongness() {
    String value=getattrvalue("Strongness");
    if(value==null)throw new IllegalStateException();
    return Role_Strongness.parseXmlCode(value);
  }
  public void setStrongness(Role_Strongness value) {
    setattrvalue("Strongness", Role_Strongness.toXmlCode(value));
  }
  public boolean getOrdered() {
    String value=getattrvalue("Ordered");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setOrdered(boolean value) {
    setattrvalue("Ordered", value?"true":"false");
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
  public boolean getEmbeddedTransfer() {
    String value=getattrvalue("EmbeddedTransfer");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setEmbeddedTransfer(boolean value) {
    setattrvalue("EmbeddedTransfer", value?"true":"false");
  }
  public String getAssociation() {
    ch.interlis.iom.IomObject value=getattrobj("Association",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setAssociation(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Association","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
}
