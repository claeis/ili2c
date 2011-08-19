package ch.interlis.models.IlisMeta07.ModelData;
public class PathEl extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.PathEl";
  public PathEl() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public PathEl_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return PathEl_Kind.parseXmlCode(value);
  }
  public void setKind(PathEl_Kind value) {
    setattrvalue("Kind", PathEl_Kind.toXmlCode(value));
  }
  public String getRef() {
    ch.interlis.iom.IomObject value=getattrobj("Ref",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setRef(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Ref","REF");
    structvalue.setobjectrefoid(oid);
  }
  public int getNumIndex() {
    String value=getattrvalue("NumIndex");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setNumIndex(int value) {
    setattrvalue("NumIndex", Integer.toString(value));
  }
  public PathEl_SpecIndex getSpecIndex() {
    String value=getattrvalue("SpecIndex");
    if(value==null)throw new IllegalStateException();
    return PathEl_SpecIndex.parseXmlCode(value);
  }
  public void setSpecIndex(PathEl_SpecIndex value) {
    setattrvalue("SpecIndex", PathEl_SpecIndex.toXmlCode(value));
  }
}
