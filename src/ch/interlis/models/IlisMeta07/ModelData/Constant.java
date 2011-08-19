package ch.interlis.models.IlisMeta07.ModelData;
public class Constant extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  private final static String tag= "IlisMeta07.ModelData.Constant";
  public Constant() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public String getValue() {
    String value=getattrvalue("Value");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setValue(String value) {
    setattrvalue("Value", value);
  }
  public Constant_Type getType() {
    String value=getattrvalue("Type");
    if(value==null)throw new IllegalStateException();
    return Constant_Type.parseXmlCode(value);
  }
  public void setType(Constant_Type value) {
    setattrvalue("Type", Constant_Type.toXmlCode(value));
  }
}
