/*****************************************************************************
 *
 * NumericalType.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


/** An abstract superclass that groups together the structured and the
    non-structured numeric types.
*/
public abstract class NumericalType extends BaseType
{
  protected Unit unit = null;
  protected RefSystemRef referenceSystem = null;
  
  public static final int ROTATION_NONE = 0;
  public static final int ROTATION_CLOCKWISE = 1;
  public static final int ROTATION_COUNTERCLOCKWISE = 2;
  
  protected int rotation = ROTATION_NONE;
  
  protected boolean circular = false;

  
  /** Returns the unit that is associated with this NumericalType.
  */
  public abstract Unit getUnit ();


  /** Sets the unit that is associated with this NumericalType.

      <p>In JavaBeans terminology, the <code>unit</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.
      
      @param unit The new unit, or <code>null</code> if this
                  NumericalType shall not have a unit.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>unit</code> property
                 and does not agree with the change.
  */
  public abstract void setUnit (Unit unit)
   throws java.beans.PropertyVetoException;
  
  
  /** Sets the value of the <code>circular</code> property.
      See the INTERLIS reference manual for a description
      what circularity means in the context of numerical types.

      <p>In JavaBeans terminology, the <code>circular</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.
      
      @param circular Whether or not this NumericalType is circular.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>circular</code> property
                 and does not agree with the change.
  */
  public void setCircular (boolean circular)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.circular;
    boolean newValue = circular;
    
    if (oldValue == newValue)
      return;
      
    fireVetoableChange("circular", oldValue, newValue);
    this.circular = newValue;
    firePropertyChange("circular", oldValue, newValue);
  }


  /** Returns whether or not this NumericalType is circular. */
  public boolean isCircular()
  {
    return circular;
  }


  /** Changes the reference system.
      
      <p>In JavaBeans terminology, the <code>referenceSystem</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.
      
      @param referenceSystem The new reference system, or
                             <code>null</code> if this type
                             shall not have any reference
                             system.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>referenceSystem</code> property
                 and does not agree with the change.
  */
  public void setReferenceSystem (RefSystemRef referenceSystem)
    throws java.beans.PropertyVetoException
  {
    RefSystemRef oldValue = this.referenceSystem;
    RefSystemRef newValue = referenceSystem;
    
    if (oldValue == newValue)
      return;
    
    fireVetoableChange ("referenceSystem", oldValue, newValue);
    this.referenceSystem = newValue;
    firePropertyChange ("referenceSystem", oldValue, newValue);
  }



  /** Returns the reference system for this NumericalType.
  
      @return The reference systzem, or <code>null</code> if none
              has been defined for this type.
  */
  public RefSystemRef getReferenceSystem ()
  {
    return referenceSystem;
  }


  public int getRotation()
  {
    return rotation;
  }
  
  
  public void setRotation (int rotation)
    throws java.beans.PropertyVetoException
  {
    int oldValue = this.rotation;
    int newValue = rotation;
    
    if (oldValue == newValue)
      return;
    
    if ((rotation != ROTATION_NONE)
        && (rotation != ROTATION_COUNTERCLOCKWISE)
        && (rotation != ROTATION_CLOCKWISE))
    {
      throw new IllegalArgumentException("illegal value passed to "
        + getClass().getName() + ".setRotation()");
    }
    
    fireVetoableChange ("rotation", new Integer (oldValue), new Integer (newValue));
    this.rotation = rotation;
    firePropertyChange ("rotation", new Integer (oldValue), new Integer (newValue));
  }
}
