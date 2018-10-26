package ch.interlis.models.IlisMeta07.ModelData;
public class EnumAssignment extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "IlisMeta07.ModelData.EnumAssignment";
  public EnumAssignment() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_ValueToAssign="ValueToAssign";
  public ch.interlis.models.IlisMeta07.ModelData.Expression getValueToAssign() {
    ch.interlis.models.IlisMeta07.ModelData.Expression value=(ch.interlis.models.IlisMeta07.ModelData.Expression)getattrobj("ValueToAssign",0);
    return value;
  }
  public void setValueToAssign(ch.interlis.models.IlisMeta07.ModelData.Expression value) {
    if(getattrvaluecount("ValueToAssign")>0){
      changeattrobj("ValueToAssign",0, value);
    }else{
      addattrobj("ValueToAssign", value);
    }
  }
  public final static String tag_MinEnumValue="MinEnumValue";
  public String getMinEnumValue() {
    ch.interlis.iom.IomObject value=getattrobj("MinEnumValue",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setMinEnumValue(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("MinEnumValue","REF");
    structvalue.setobjectrefoid(oid);
  }
  public final static String tag_MaxEnumValue="MaxEnumValue";
  public String getMaxEnumValue() {
    ch.interlis.iom.IomObject value=getattrobj("MaxEnumValue",0);
    if(value==null)throw new IllegalStateException();
    String oid=value.getobjectrefoid();
    if(oid==null)throw new IllegalStateException();
    return oid;
  }
  public void setMaxEnumValue(String oid) {
    ch.interlis.iom.IomObject structvalue=addattrobj("MaxEnumValue","REF");
    structvalue.setobjectrefoid(oid);
  }
}
