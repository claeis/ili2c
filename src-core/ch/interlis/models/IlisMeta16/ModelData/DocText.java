package ch.interlis.models.IlisMeta16.ModelData;
public class DocText extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.DocText";
  public DocText() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Name="Name";
  public String getName() {
    String value=getattrvalue("Name");
    return value;
  }
  public void setName(String value) {
    setattrvalue("Name", value);
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
