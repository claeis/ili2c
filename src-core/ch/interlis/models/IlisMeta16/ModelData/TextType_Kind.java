package ch.interlis.models.IlisMeta16.ModelData;
public class TextType_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private TextType_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(TextType_Kind value) {
     return value.value;
  }
  static public TextType_Kind parseXmlCode(String value) {
     return (TextType_Kind)valuev.get(value);
  }
  static public TextType_Kind MText=new TextType_Kind("MText");
  public final static String tag_MText="MText";
  static public TextType_Kind Text=new TextType_Kind("Text");
  public final static String tag_Text="Text";
  static public TextType_Kind Name=new TextType_Kind("Name");
  public final static String tag_Name="Name";
  static public TextType_Kind Uri=new TextType_Kind("Uri");
  public final static String tag_Uri="Uri";
}
