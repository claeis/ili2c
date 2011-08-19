package ch.interlis.models.IlisMeta07.ModelData;
public class LineType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  private final static String tag= "IlisMeta07.ModelData.LineType";
  public LineType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public LineType_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return LineType_Kind.parseXmlCode(value);
  }
  public void setKind(LineType_Kind value) {
    setattrvalue("Kind", LineType_Kind.toXmlCode(value));
  }
  public String getMaxOverlap() {
    String value=getattrvalue("MaxOverlap");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setMaxOverlap(String value) {
    setattrvalue("MaxOverlap", value);
  }
  public String getCoordType() {
    ch.interlis.iom.IomObject value=getattrobj("CoordType",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setCoordType(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("CoordType","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getLAStructure() {
    ch.interlis.iom.IomObject value=getattrobj("LAStructure",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setLAStructure(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("LAStructure","REF");
    structvalue.setobjectrefoid(oid);
  }
}
