package ch.interlis.models.IlisMeta07.ModelTranslation;
public class DocTextTranslation extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelTranslation.DocTextTranslation";
  public DocTextTranslation() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getText() {
    String value=getattrvalue("Text");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setText(String value) {
    setattrvalue("Text", value);
  }
}
