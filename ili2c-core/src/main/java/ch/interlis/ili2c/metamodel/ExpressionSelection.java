package ch.interlis.ili2c.metamodel;

public class ExpressionSelection extends Selection
{
  protected Evaluable     condition;
  
  /** Constructs a new Selection given the selected View
      and the selection condition.
  */
  public ExpressionSelection (Viewable selected, Evaluable condition)
  {
    super(selected);
    this.condition = condition;
  }
  
  
  /** Returns the selection condition.
  */
  public Evaluable getCondition ()
  {
    return condition;
  }

}
