package ch.interlis.models.IlisMeta07.ModelData;
public class DataUnit extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.DataUnit";
  public DataUnit(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getViewUnit() {
    String value=getattrvalue("ViewUnit");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setViewUnit(boolean value) {
    setattrvalue("ViewUnit", value?"true":"false");
  }
  public String getDataUnitName() {
    String value=getattrvalue("DataUnitName");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setDataUnitName(String value) {
    setattrvalue("DataUnitName", value);
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
}
