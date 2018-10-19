package ch.interlis.models.DatasetIdx16;
public class MultilingualText extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.MultilingualText";
  public MultilingualText() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_LocalisedText="LocalisedText";
  public int sizeLocalisedText() {return getattrvaluecount("LocalisedText");}
  public ch.interlis.models.DatasetIdx16.LocalisedText[] getLocalisedText() {
    int size=getattrvaluecount("LocalisedText");
    ch.interlis.models.DatasetIdx16.LocalisedText value[]=new ch.interlis.models.DatasetIdx16.LocalisedText[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.LocalisedText)getattrobj("LocalisedText",i);
    }
    return value;
  }
  public void addLocalisedText(ch.interlis.models.DatasetIdx16.LocalisedText value) {
    addattrobj("LocalisedText", value);
  }
}
