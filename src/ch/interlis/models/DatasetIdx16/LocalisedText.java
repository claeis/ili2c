package ch.interlis.models.DatasetIdx16;
public class LocalisedText extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.LocalisedText";
  public LocalisedText() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Language="Language";
  public String getLanguage() {
    String value=getattrvalue("Language");
    return value;
  }
  public void setLanguage(String value) {
    setattrvalue("Language", value);
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
