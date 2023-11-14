package ch.interlis.ili2c.metamodel;

import java.util.List;

public class GraphicParameterDef extends AbstractLeafElement {

    @Override
    protected void linkTranslationOf(Element baseElement)
    {
        super.linkTranslationOf(baseElement);
        Type type=getDomain();
        Type baseType=((GraphicParameterDef) baseElement).getDomain();
        if(type.getClass().equals(baseType.getClass())) {
            type.linkTranslationOf(baseType);
        }
    }
  @Override
  public void checkIntegrity (List<Ili2cSemanticException> errs)
          throws java.lang.IllegalStateException
  {
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      Type type=getDomain();
      GraphicParameterDef baseElement=(GraphicParameterDef)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      
      Type baseType=baseElement.getDomain();
      if(type.getClass()!=baseType.getClass()){
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_graphicparamType",getScopedName(),baseElement.getScopedName())));
          return;
      }
      if (type instanceof TypeAlias){
          if(((TypeAlias)type).getAliasing().getTranslationOfOrSame()!=((TypeAlias)baseType).getAliasing().getTranslationOfOrSame()){
              errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_graphicparamType",getScopedName(),((GraphicParameterDef) baseElement).getScopedName())));
              return;
          }
      }
      try {
          type.checkTranslationOf(errs,getScopedName(),baseElement.getScopedName());
          if(type instanceof AbstractCoordType) {
              String crs=((AbstractCoordType) type).getCrs(this);
              String originCrs=((AbstractCoordType) baseType).getCrs(baseElement);
              if(crs==null && originCrs==null) {
                  
              }else {
                  if(crs==null || originCrs==null) {
                      throw new Ili2cSemanticException();
                  }
                  if(!crs.equals(originCrs)) {
                      throw new Ili2cSemanticException();
                  }
              }
          }
      }catch(Ili2cSemanticException ex) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_graphicparamType",getScopedName(),baseElement.getScopedName())));
      }
  }

    private String name=null;
    public void setName(String name){
      this.name=name;
    }
    public String getName(){
      return name;
    }

  private Type domain=null;
  public Type getDomain ()
  {
    return domain;
  }
  public void setDomain (Type domain)
  {
    this.domain=domain;
  }

}
