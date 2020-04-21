package ch.interlis.ili2c.metamodel;

/** A function call, as it would be part of an expression (for instance
    in a constraint).
*/
public class FunctionCall extends Evaluable
{
  protected Function function;
  protected Evaluable[] arguments;
  
  /** Constructs a new function call given the called function
      and the number of arguments.
      
      @exception java.lang.IllegalArgumentException if the number of
                 arguments does not match the function declaration.
  */
  public FunctionCall (Function function, Evaluable[] arguments)
  {
    this.function = function;
    this.arguments = arguments;
    
    /* We currently allow to construct a call to an invalid function;
       otherwise, a number of additional error messages would
       appear.
    */
    if ((function == null) || (arguments == null))
      return;
    
    FormalArgument[] declaredParams = function.getArguments ();
    if (declaredParams == null)
      return;
    
    /* Check that the number of arguments matches the function declaration. */
    if (declaredParams.length != arguments.length)
      throw new IllegalArgumentException (Element.formatMessage (
          "err_functionCall_wrongNumberArgs",
          function.toString (),
          Integer.toString (declaredParams.length)));
  }
  
  @Override
  public boolean isLogical() {
      if(function.getDomain().isBoolean()){
          return true;
      }
      return false;
  }
  

/** Returns the function that gets called when executing this
      function call.
  */
  public Function getFunction ()
  {
    return function;
  }
  

  /** Returns the arguments that get passed to the called
      function when executing this function call.
  */
  public Evaluable[] getArguments ()
  {
    return arguments;
  }
}
