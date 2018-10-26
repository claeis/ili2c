package ch.interlis.ili2c.metamodel;

import java.util.Collection;
import java.util.List;

public class GraphicParameterDef extends AbstractLeafElement {

  @Override
  public void checkIntegrity (List<Ili2cSemanticException> errs)
          throws java.lang.IllegalStateException
  {
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
