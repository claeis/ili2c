/*****************************************************************************
 *
 * Constraint.java
 * ---------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

import java.util.*;

/** An abstract class that groups together all kinds of constraints
    that can be specified in INTERLIS.
*/
public abstract class Constraint extends AbstractLeafElement
{
  protected Evaluable condition = null;

  /** Returns the condition that must hold for this
      constraint to be satisfied.
  */
  public Evaluable getCondition ()
  {
    return condition;
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
                 <code>condition</code> is <code>null</code>.
                 
      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setCondition (Evaluable condition)
    throws java.beans.PropertyVetoException
  {
    Evaluable oldValue = this.condition;
    Evaluable newValue = condition;
    
    if (newValue == null)
      throw new IllegalArgumentException(rsrc.getString("err_nullNotAcceptable"));
    
    if (oldValue == newValue)
      return;
    
    fireVetoableChange("condition", oldValue, newValue);
    this.condition = newValue;
    firePropertyChange("condition", oldValue, newValue);
  }

	private boolean selfStanding=false;
	public void setSelfStanding(boolean value)
	{
		selfStanding=value;
	public boolean isSelfStanding()
	{
		return selfStanding;
	}

}