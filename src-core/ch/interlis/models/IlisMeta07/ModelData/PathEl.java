package ch.interlis.models.IlisMeta07.ModelData;
public class PathEl extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.PathEl";
  public PathEl() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public PathEl_Kind getKind() {
    String value=getattrvalue("Kind");
    return PathEl_Kind.parseXmlCode(value);
  }
  public void setKind(PathEl_Kind value) {
    setattrvalue("Kind", PathEl_Kind.toXmlCode(value));
  }
  public final static String tag_Ref="Ref";
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
  public final static String tag_NumIndex="NumIndex";
  public int getNumIndex() {
    String value=getattrvalue("NumIndex");
    return Integer.parseInt(value);
  }
  public void setNumIndex(int value) {
    setattrvalue("NumIndex", Integer.toString(value));
  }
  public final static String tag_SpecIndex="SpecIndex";
  public PathEl_SpecIndex getSpecIndex() {
    String value=getattrvalue("SpecIndex");
    return PathEl_SpecIndex.parseXmlCode(value);
  }
  public void setSpecIndex(PathEl_SpecIndex value) {
    setattrvalue("SpecIndex", PathEl_SpecIndex.toXmlCode(value));
  }
}
