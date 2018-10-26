package ch.interlis.models.IlisMeta07.ModelData;
public class BlackboxType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.BlackboxType";
  public BlackboxType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public BlackboxType_Kind getKind() {
    String value=getattrvalue("Kind");
    return BlackboxType_Kind.parseXmlCode(value);
  }
  public void setKind(BlackboxType_Kind value) {
    setattrvalue("Kind", BlackboxType_Kind.toXmlCode(value));
  }
}
