package ch.interlis.ili2c.metamodel;


public abstract class PathEl
{

  /** returns the Viewable reached by applying this path element.
   * @return reached Viewable
   */
  public Viewable<?> getViewable(){return null;};
  abstract public String getName();
  public Ili2cSemanticException checkTranslation(PathEl other,int sourceLine) {return null;}
}
