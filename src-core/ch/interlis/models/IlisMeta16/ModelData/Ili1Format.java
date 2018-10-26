package ch.interlis.models.IlisMeta16.ModelData;
public class Ili1Format extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta16.ModelData.Ili1Format";
  public Ili1Format() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_isFree="isFree";
  public boolean getisFree() {
    String value=getattrvalue("isFree");
    return value!=null && value.equals("true");
  }
  public void setisFree(boolean value) {
    setattrvalue("isFree", value?"true":"false");
  }
  public final static String tag_LineSize="LineSize";
  public int getLineSize() {
    String value=getattrvalue("LineSize");
    return Integer.parseInt(value);
  }
  public void setLineSize(int value) {
    setattrvalue("LineSize", Integer.toString(value));
  }
  public final static String tag_tidSize="tidSize";
  public int gettidSize() {
    String value=getattrvalue("tidSize");
    return Integer.parseInt(value);
  }
  public void settidSize(int value) {
    setattrvalue("tidSize", Integer.toString(value));
  }
  public final static String tag_blankCode="blankCode";
  public int getblankCode() {
    String value=getattrvalue("blankCode");
    return Integer.parseInt(value);
  }
  public void setblankCode(int value) {
    setattrvalue("blankCode", Integer.toString(value));
  }
  public final static String tag_undefinedCode="undefinedCode";
  public int getundefinedCode() {
    String value=getattrvalue("undefinedCode");
    return Integer.parseInt(value);
  }
  public void setundefinedCode(int value) {
    setattrvalue("undefinedCode", Integer.toString(value));
  }
  public final static String tag_continueCode="continueCode";
  public int getcontinueCode() {
    String value=getattrvalue("continueCode");
    return Integer.parseInt(value);
  }
  public void setcontinueCode(int value) {
    setattrvalue("continueCode", Integer.toString(value));
  }
  public final static String tag_Font="Font";
  public String getFont() {
    String value=getattrvalue("Font");
    return value;
  }
  public void setFont(String value) {
    setattrvalue("Font", value);
  }
  public final static String tag_tidKind="tidKind";
  public Ili1Format_tidKind gettidKind() {
    String value=getattrvalue("tidKind");
    return Ili1Format_tidKind.parseXmlCode(value);
  }
  public void settidKind(Ili1Format_tidKind value) {
    setattrvalue("tidKind", Ili1Format_tidKind.toXmlCode(value));
  }
  public final static String tag_tidExplanation="tidExplanation";
  public String gettidExplanation() {
    String value=getattrvalue("tidExplanation");
    return value;
  }
  public void settidExplanation(String value) {
    setattrvalue("tidExplanation", value);
  }
}
