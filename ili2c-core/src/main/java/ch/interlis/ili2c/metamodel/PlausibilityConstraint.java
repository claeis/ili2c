/*****************************************************************************
 *
 * PlausibilityConstraint.java
 * ---------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** A constraint that does not have to be satisfied for every instance
    of a table, but only for a certain percentage.

    @author    Sascha Brawer
*/
public class PlausibilityConstraint extends Constraint
{
  protected double     percentage = 50;
  protected int        direction = DIRECTION_AT_LEAST;

  public static final int DIRECTION_AT_LEAST = 0;
  public static final int DIRECTION_AT_MOST = 1;

  /** Constructs a new plausibility constraint.
  */
  public PlausibilityConstraint()
  {
  }

  /** Returns whether the condition must be satisfied for
      at least or at most the specified percentage.
  */
  public int getDirection()
  {
    return direction;
  }


  /** Sets whether the constraint requires at least or at most
      a certain percentage of the instances to fulfill the
      condition.

      <p>In JavaBeans terminology, the <code>direction</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param direction The new direction.
             <code>DIRECTION_AT_LEAST</code> indicates
             that at least a certain percentage of the
             instances have to fulfill the condition.
             <code>DIRECTION_AT_MOST</code> indicates
             the opposite.

      @exception java.lang.IllegalArgumentException if
                 <code>direction</code> is neither
                 <code>DIRECTION_AT_LEAST</code> nor
                 <code>DIRECTION_AT_MOST</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setDirection(int direction)
    throws java.beans.PropertyVetoException
  {
    int oldValue = this.direction;
    int newValue = direction;

    if (oldValue == newValue)
      return;

    if ((newValue != DIRECTION_AT_LEAST) && (newValue != DIRECTION_AT_MOST))
      throw new IllegalArgumentException("direction");

    fireVetoableChange("direction", new Integer(oldValue), new Integer(newValue));
    this.direction = direction;
    firePropertyChange("direction", new Integer(oldValue), new Integer(newValue));
  }


  /** Returns the current value of the <code>percentage</code>
      property.
  */
  public double getPercentage()
  {
    return percentage;
  }


  /** Sets the value of the <code>percentage</code> property.

      <p>In JavaBeans terminology, the <code>percentage</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param percentage The new percentage value.

      @exception java.lang.IllegalArgumentException if
                 <code>percentage</code> is less than zero or
                 greater than 100, because these values do
                 not make sense.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setPercentage(double percentage)
    throws java.beans.PropertyVetoException
  {
    Double oldValue = new Double(this.percentage);
    Double newValue = new Double(percentage);

    if ((percentage < 0) || (percentage > 100))
      throw new IllegalArgumentException(rsrc.getString(
                       "err_percentageBetween0and100"));

    fireVetoableChange("percentage", oldValue, newValue);
    this.percentage = percentage;
    firePropertyChange("percentage", oldValue, newValue);
  }
  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      PlausibilityConstraint baseElement=(PlausibilityConstraint)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      
      if(percentage!=baseElement.percentage) {
          Ili2cSemanticException err=new Ili2cSemanticException( getSourceLine(), Element.formatMessage("err_diff_plausibilityConstraintPercentageMismatch",Double.toString(percentage),Double.toString(baseElement.percentage)));
          errs.add(err);
      }
      if(direction!=baseElement.direction) {
          Ili2cSemanticException err=new Ili2cSemanticException( getSourceLine(), Element.formatMessage("err_diff_plausibilityConstraintDirectionMismatch"));
          errs.add(err);
      }
      
  }
  
}
