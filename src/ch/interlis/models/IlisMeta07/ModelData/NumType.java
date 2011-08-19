package ch.interlis.models.IlisMeta07.ModelData;
public class NumType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  private final static String tag= "IlisMeta07.ModelData.NumType";
  public NumType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getMin() {
    String value=getattrvalue("Min");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setMin(String value) {
    setattrvalue("Min", value);
  }
  public String getMax() {
    String value=getattrvalue("Max");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setMax(String value) {
    setattrvalue("Max", value);
  }
  public boolean getCircular() {
    String value=getattrvalue("Circular");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setCircular(boolean value) {
    setattrvalue("Circular", value?"true":"false");
  }
  public boolean getClockwise() {
    String value=getattrvalue("Clockwise");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setClockwise(boolean value) {
    setattrvalue("Clockwise", value?"true":"false");
  }
  public String getRefSys() {
    ch.interlis.iom.IomObject value=getattrobj("RefSys",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRefSys(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("RefSys","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getUnit() {
    ch.interlis.iom.IomObject value=getattrobj("Unit",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setUnit(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Unit","REF");
    structvalue.setobjectrefoid(oid);
  }
}
