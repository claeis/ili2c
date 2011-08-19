package ch.interlis.models.IlisMeta07.ModelData;
public class Ili1Format extends ch.interlis.iom_j.Iom_jObject
{
  private final static String tag= "IlisMeta07.ModelData.Ili1Format";
  public Ili1Format() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getisFree() {
    String value=getattrvalue("isFree");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setisFree(boolean value) {
    setattrvalue("isFree", value?"true":"false");
  }
  public int getLineSize() {
    String value=getattrvalue("LineSize");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setLineSize(int value) {
    setattrvalue("LineSize", Integer.toString(value));
  }
  public int gettidSize() {
    String value=getattrvalue("tidSize");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void settidSize(int value) {
    setattrvalue("tidSize", Integer.toString(value));
  }
  public int getblankCode() {
    String value=getattrvalue("blankCode");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setblankCode(int value) {
    setattrvalue("blankCode", Integer.toString(value));
  }
  public int getundefinedCode() {
    String value=getattrvalue("undefinedCode");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setundefinedCode(int value) {
    setattrvalue("undefinedCode", Integer.toString(value));
  }
  public int getcontinueCode() {
    String value=getattrvalue("continueCode");
    if(value==null)throw new IllegalStateException();
    return Integer.parseInt(value);
  }
  public void setcontinueCode(int value) {
    setattrvalue("continueCode", Integer.toString(value));
  }
  public String getFont() {
    String value=getattrvalue("Font");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setFont(String value) {
    setattrvalue("Font", value);
  }
  public Ili1Format_tidKind gettidKind() {
    String value=getattrvalue("tidKind");
    if(value==null)throw new IllegalStateException();
    return Ili1Format_tidKind.parseXmlCode(value);
  }
  public void settidKind(Ili1Format_tidKind value) {
    setattrvalue("tidKind", Ili1Format_tidKind.toXmlCode(value));
  }
  public String gettidExplanation() {
    String value=getattrvalue("tidExplanation");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void settidExplanation(String value) {
    setattrvalue("tidExplanation", value);
  }
}
