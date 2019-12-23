package ch.interlis.ili2c.metamodel;

import java.util.List;

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
		  typeRestrictions=typev.clone();
	  }
  }
  public Type[] getTypeRestriction(){
	  if(typeRestrictions==null || typeRestrictions.length==0){
		  return null;
	  }
	  return typeRestrictions.clone();
  }
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with)) {
        return false;
    }

    return true;
  }
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null)) {
        return;
    }
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }


    public AttributePathType clone() {
        return (AttributePathType) super.clone();
    }
    @Override
    protected void linkTranslationOf(Element baseElement)
    {
        super.linkTranslationOf(baseElement);
        AttributePathType originElement=(AttributePathType)baseElement;
        if(typeRestrictions!=null && originElement.typeRestrictions!=null) {
            for(int i=0;i<typeRestrictions.length;i++) {
                Type type=typeRestrictions[i];
                if(i<originElement.typeRestrictions.length) {
                    Type originType=originElement.typeRestrictions[i];
                    if(originType.getClass().equals(type.getClass())) {
                        type.linkTranslationOf(originType);
                    }
                }
            }
        }
        if(argRestriction!=null && originElement.argRestriction!=null) {
            argRestriction.linkTranslationOf(originElement.argRestriction);
        }
    }    
    @Override
    public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        AttributePathType baseElement=(AttributePathType)getTranslationOf();
        if(baseElement==null) {
            return;
        }
        if(typeRestrictions==null && baseElement.typeRestrictions==null) {
        }else {
            if(typeRestrictions==null || baseElement.typeRestrictions==null) {
                throw new Ili2cSemanticException();
            }
            if(typeRestrictions.length!=baseElement.typeRestrictions.length) {
                throw new Ili2cSemanticException();
            }
            for(int i=0;i<typeRestrictions.length;i++) {
                Type type=typeRestrictions[i];
                Type originType=baseElement.typeRestrictions[i];
                if(!originType.getClass().equals(type.getClass())) {
                    throw new Ili2cSemanticException();
                }
                type.checkTranslationOf(errs, name, baseName);
            }
        }
        if(argRestriction==null && baseElement.argRestriction==null) {
            
        }else {
            if(argRestriction==null || baseElement.argRestriction==null) {
                throw new Ili2cSemanticException();
            }
            argRestriction.checkTranslationOf(errs, name, baseName);
        }
    }
}
