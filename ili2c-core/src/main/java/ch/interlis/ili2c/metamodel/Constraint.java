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

import java.util.List;

/** An abstract class that groups together all kinds of constraints
    that can be specified in INTERLIS.
*/
public abstract class Constraint extends AbstractLeafElement
{
  protected Evaluable condition = null;
  protected String name=null;
  protected int constraintIdx=0;
  
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
		selfStanding=value;	}
	public boolean isSelfStanding()
	{
		return selfStanding;
	}


	@Override
	public String getName() {
		if(name==null){
			String nameFromMetaValue=getMetaValue(Ili2cMetaAttrs.ILI2C_CONSTRAINT_NAME);
			if(nameFromMetaValue==null){
				return "Constraint"+constraintIdx;
			}
			return nameFromMetaValue;
		}
		return name;
	}
	  public void setNameIdx (int idx){
		  constraintIdx=idx;
	  }
	  public void setName (String name)
			    throws java.beans.PropertyVetoException
			  {
			    String oldValue = this.name;
			    String newValue = name;
			    if(name!=null){
				    checkNameSanity(name, /* empty names acceptable? */ false);
				    checkNameUniqueness(name, Constraint.class, null,
				      "err_duplicateFunctionName");
			    }


			    fireVetoableChange("name", oldValue, newValue);
			    this.name = newValue;
			    firePropertyChange("name", oldValue, newValue);
			  }

	  @Override
	  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
	    throws java.lang.IllegalStateException
	  {
	      super.checkTranslationOf(errs,name,baseName);
	      Constraint baseElement=(Constraint)getTranslationOf();
	      if(baseElement==null) {
	          return;
	      }
	      
	      Ili2cSemanticException err=Evaluable.checkTranslation(getCondition(), baseElement.getCondition(), getSourceLine(), "err_diff_constraintConditionMismatch");
	      if(err!=null) {
	          errs.add(err);
	      }
	      
	  }
}
