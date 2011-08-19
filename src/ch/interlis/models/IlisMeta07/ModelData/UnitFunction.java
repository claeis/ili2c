package ch.interlis.models.IlisMeta07.ModelData;
public class UnitFunction extends ch.interlis.models.IlisMeta07.ModelData.Factor
{
  private final static String tag= "IlisMeta07.ModelData.UnitFunction";
  public UnitFunction() {
    super();
  }
  public String getobjecttag() {
    return tag;
  }
  public String getExplanation() {
    String value=getattrvalue("Explanation");
    if(value==null)throw new IllegalStateException();
    return value;
  }
  public void setExplanation(String value) {
    setattrvalue("Explanation", value);
  }
}
