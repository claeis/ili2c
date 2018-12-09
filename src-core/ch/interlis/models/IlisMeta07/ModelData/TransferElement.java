package ch.interlis.models.IlisMeta07.ModelData;
public class TransferElement extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.TransferElement";
  public TransferElement(String oid) {
    super(tag,oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_TransferClass="TransferClass";
  public String getTransferClass() {
    ch.interlis.iom.IomObject value=getattrobj("TransferClass",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setTransferClass(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("TransferClass","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_TransferElement="TransferElement";
  public String getTransferElement() {
    ch.interlis.iom.IomObject value=getattrobj("TransferElement",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setTransferElement(String oid,long orderPos) {
    ch.interlis.iom.IomObject structvalue=addattrobj("TransferElement","REF");
    structvalue.setobjectrefoid(oid);
    structvalue.setobjectreforderpos(orderPos);
  }
}
