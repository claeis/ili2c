package ch.interlis.ili2c.metamodel;

/** The value of a GraphicParameterDef.
*/
public class ParameterValue extends Evaluable
{
  protected GraphicParameterDef param;
  
  /** Constructs a new parameter value.
  */
  public ParameterValue (GraphicParameterDef param)
  {
    this.param = param;
  }
  @Override
  public boolean isLogical() {
      return param.getDomain().isBoolean();
  }
  
  /** Returns the parameter whose value is being evaluated
      at run-time.
  */
  public GraphicParameterDef getParameter ()
  {
    return param;
  }
}
