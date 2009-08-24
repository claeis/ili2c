package ch.interlis.ili2c.metamodel;
import java.util.Iterator;


public class AttributePathType extends BaseType {

	private Type[] typeRestrictions=null;
	private ObjectPath attrRestriction=null;
	private FormalArgument argRestriction=null;
  public AttributePathType()
  {
  }  
  public void setAttrRestriction(ObjectPath attrRestr){
	  attrRestriction=attrRestr;
  }
  public ObjectPath getAttrRestriction(){
	  if(attrRestriction!=null && argRestriction!=null){
		  throw new IllegalStateException("attrRestriction!=null && argRestriction!=null");
	  }
	  return attrRestriction;
  }
  public void setArgRestriction(FormalArgument argRestr){
	  argRestriction=argRestr;
  }
  public FormalArgument getArgRestriction(){
	  if(attrRestriction!=null && argRestriction!=null){
		  throw new IllegalStateException("attrRestriction!=null && argRestriction!=null");
	  }
	  return argRestriction;
  }
  public void setTypeRestriction(Type[] typev){
	  if(typev==null || typev.length==0){
		  typeRestrictions=null;
	  }else{
		  typeRestrictions=(Type[])typev.clone();
	  }
  }
  public Type[] getTypeRestriction(){
	  if(typeRestrictions==null || typeRestrictions.length==0){
		  return null;
	  }
	  return (Type[])typeRestrictions.clone();
  }
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
  } 
}
