package ch.interlis.models.IlisMeta07.ModelData;
public class BlackboxType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  private final static String tag= "IlisMeta07.ModelData.BlackboxType";
  public BlackboxType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public BlackboxType_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return BlackboxType_Kind.parseXmlCode(value);
  }
  public void setKind(BlackboxType_Kind value) {
    setattrvalue("Kind", BlackboxType_Kind.toXmlCode(value));
  }
}
