package ch.interlis.models.IlisMeta16.ModelTranslation;
public class DocTextTranslation extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelTranslation.DocTextTranslation";
  public DocTextTranslation() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Text="Text";
  public String getText() {
    String value=getattrvalue("Text");
    return value;
  }
  public void setText(String value) {
    setattrvalue("Text", value);
  }
}
