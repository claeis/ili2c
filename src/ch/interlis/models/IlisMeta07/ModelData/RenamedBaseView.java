package ch.interlis.models.IlisMeta07.ModelData;
public class RenamedBaseView extends ch.interlis.models.IlisMeta07.ModelData.ExtendableME
{
  private final static String tag= "IlisMeta07.ModelData.RenamedBaseView";
  public RenamedBaseView(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public boolean getOrNull() {
    String value=getattrvalue("OrNull");
    if(value==null)throw new IllegalStateException();
    return value.equals("true");
  }
  public void setOrNull(boolean value) {
    setattrvalue("OrNull", value?"true":"false");
  }
  public String getBaseView() {
    ch.interlis.iom.IomObject value=getattrobj("BaseView",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setBaseView(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("BaseView","REF");
    structvalue.setobjectrefoid(oid);
  }
  public String getView() {
    ch.interlis.iom.IomObject value=getattrobj("View",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setView(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("View","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
}
