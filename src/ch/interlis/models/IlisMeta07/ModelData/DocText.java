package ch.interlis.models.IlisMeta07.ModelData;
public class DocText extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.DocText";
  public DocText() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public String getName() {
    String value=getattrvalue("Name");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setName(String value) {
    setattrvalue("Name", value);
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
