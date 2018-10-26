package ch.interlis.models.IlisMeta16.ModelData;
public class MultiValue extends ch.interlis.models.IlisMeta16.ModelData.TypeRelatedType
{
  public final static String tag= "IlisMeta16.ModelData.MultiValue";
  public MultiValue(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Ordered="Ordered";
  public boolean getOrdered() {
    String value=getattrvalue("Ordered");
    return value!=null && value.equals("true");
  }
  public void setOrdered(boolean value) {
    setattrvalue("Ordered", value?"true":"false");
  }
  public final static String tag_Multiplicity="Multiplicity";
  public ch.interlis.models.IlisMeta16.ModelData.Multiplicity getMultiplicity() {
    ch.interlis.models.IlisMeta16.ModelData.Multiplicity value=(ch.interlis.models.IlisMeta16.ModelData.Multiplicity)getattrobj("Multiplicity",0);
    return value;
  }
  public void setMultiplicity(ch.interlis.models.IlisMeta16.ModelData.Multiplicity value) {
    if(getattrvaluecount("Multiplicity")>0){
      changeattrobj("Multiplicity",0, value);
    }else{
      addattrobj("Multiplicity", value);
    }
  }
}
