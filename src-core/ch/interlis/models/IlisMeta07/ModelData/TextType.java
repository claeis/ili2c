package ch.interlis.models.IlisMeta07.ModelData;
public class TextType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.TextType";
  public TextType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Kind="Kind";
  public TextType_Kind getKind() {
    String value=getattrvalue("Kind");
    return TextType_Kind.parseXmlCode(value);
  }
  public void setKind(TextType_Kind value) {
    setattrvalue("Kind", TextType_Kind.toXmlCode(value));
  }
  public final static String tag_MaxLength="MaxLength";
  public int getMaxLength() {
    String value=getattrvalue("MaxLength");
    return Integer.parseInt(value);
  }
  public void setMaxLength(int value) {
    setattrvalue("MaxLength", Integer.toString(value));
  }
}
