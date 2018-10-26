package ch.interlis.models.IlisMeta07.ModelData;
public class EnumType extends ch.interlis.models.IlisMeta07.ModelData.DomainType
{
  public final static String tag= "IlisMeta07.ModelData.EnumType";
  public EnumType(String oid) {
    super(oid);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_Order="Order";
  public EnumType_Order getOrder() {
    String value=getattrvalue("Order");
    return EnumType_Order.parseXmlCode(value);
  }
  public void setOrder(EnumType_Order value) {
    setattrvalue("Order", EnumType_Order.toXmlCode(value));
  }
}
