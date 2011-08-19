package ch.interlis.models.IlisMeta07.ModelData;
public class ExtendableME extends ch.interlis.models.IlisMeta07.ModelData.MetaElement
{
  private final static String tag= "IlisMeta07.ModelData.ExtendableME";
  public ExtendableME(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getAbstract() {
    String value=getattrvalue("Abstract");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setAbstract(boolean value) {
    setattrvalue("Abstract", value?"true":"false");
  }
  public boolean getFinal() {
    String value=getattrvalue("Final");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setFinal(boolean value) {
    setattrvalue("Final", value?"true":"false");
  }
  public String getSuper() {
    ch.interlis.iom.IomObject value=getattrobj("Super",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setSuper(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("Super","REF");
    structvalue.setobjectrefoid(oid);
  }
}
