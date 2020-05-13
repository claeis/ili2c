package ch.interlis.ili2c.metamodel;


/** An expression that evaluates to a constant depending on a comparision
    with an enumerated value. This is somewhat comparable to the
    <code>switch</code> expression in languages such as C or Java,
    but it denotes (in C terminology) an r-value, not a statement.
*/
public class ConditionalExpression extends Evaluable
{
  /** A single condition of a ConditionalExpression. This corresponds
      to a single <code>case</code> statement in languages such as C, Modula-2
      or Java.
  */
  public static class Condition
  {
    protected Constant          value;
    protected Constant.EnumConstOrRange  condition;

    public Condition (Constant value, Constant.EnumConstOrRange condition)
    {
      this.value = value;
      this.condition = condition;
    }

    public Constant getValue ()
    {
      return value;
    }

    public Constant.EnumConstOrRange getCondition ()
    {
      return condition;
    }
  }


  protected ObjectPath   attribute;
  protected Condition[] conditions;

  public ConditionalExpression (ObjectPath attribute, Condition[] conditions)
  {
    this.attribute = attribute;
    this.conditions = conditions;
  }

  @Override
  public boolean isLogical() {
      return false;
  }

  public ObjectPath getAttribute ()
  {
    return attribute;
  }


  public Condition[] getConditions ()
  {
    return conditions;
  }


  /** Checks whether it is possible to assign this Evaluable to
      the Element <code>target</code>, whose type is <code>targetType</code>.
      If so, nothing happens.

      @param target The Element whose value is going to be changed by executing the assignment.


      @param targetType The type of that Element.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not be assigned to the specified target.
                 The message of the exception indicates the reason; it is
                 a localized string that is intended for being displayed
                 to the user.
  */
  void checkAssignment (Element target, Type targetType)
  {
    super.checkAssignment (target, targetType);

    for (int i = 0; i < conditions.length; i++)
    {
      if (conditions[i] == null)
        continue;

      Constant value = conditions[i].getValue();
      if (value == null)
        continue;

      value.checkAssignment (target, targetType);
    }
  }
}
