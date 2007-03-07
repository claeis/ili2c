package ch.interlis.ili2c.metamodel;

import java.util.Collection;

public class GraphicParameterDef extends AbstractLeafElement {

  public void checkIntegrity(){
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
