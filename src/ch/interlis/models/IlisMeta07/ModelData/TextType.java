package ch.interlis.models.IlisMeta07.ModelData;
public class TextType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  private final static String tag= "IlisMeta07.ModelData.TextType";
  public TextType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public TextType_Kind getKind() {
    String value=getattrvalue("Kind");
    if(value==null)throw new IllegalStateException();
    return TextType_Kind.parseXmlCode(value);
  }
  public void setKind(TextType_Kind value) {
    setattrvalue("Kind", TextType_Kind.toXmlCode(value));
  }
  public int getMaxLength() {
    String value=getattrvalue("MaxLength");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setMaxLength(int value) {
    setattrvalue("MaxLength", Integer.toString(value));
  }
}
