package ch.interlis.models.IlisMeta16.ModelData;
public class NumType extends ch.interlis.models.IlisMeta16.ModelData.DomainType
{
  public final static String tag= "IlisMeta16.ModelData.NumType";
  public NumType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Min="Min";
  public String getMin() {
    String value=getattrvalue("Min");
    return value;
  }
  public void setMin(String value) {
    setattrvalue("Min", value);
  }
  public final static String tag_Max="Max";
  public String getMax() {
    String value=getattrvalue("Max");
    return value;
  }
  public void setMax(String value) {
    setattrvalue("Max", value);
  }
  public final static String tag_Circular="Circular";
  public boolean getCircular() {
    String value=getattrvalue("Circular");
    return value!=null && value.equals("true");
  }
  public void setCircular(boolean value) {
    setattrvalue("Circular", value?"true":"false");
  }
  public final static String tag_Clockwise="Clockwise";
  public boolean getClockwise() {
    String value=getattrvalue("Clockwise");
    return value!=null && value.equals("true");
  }
  public void setClockwise(boolean value) {
    setattrvalue("Clockwise", value?"true":"false");
  }
  public final static String tag_RefSys="RefSys";
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
  public final static String tag_Unit="Unit";
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
