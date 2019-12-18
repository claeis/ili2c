/*****************************************************************************
 *
 * NumericalType.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

import ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystemAxis;

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
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      NumericalType   origin=(NumericalType)getTranslationOf();

      if (origin == null){
          return;
      }

      if(this.unit == origin.unit) {
          // ok
      }else {
          if(this.unit==null || origin.unit==null) {
              throw new Ili2cSemanticException();
          }
          if(unit.getTranslationOfOrSame()!=origin.unit.getTranslationOfOrSame()) {
              throw new Ili2cSemanticException();
          }
      }

      if(getRotation()!=origin.getRotation()){
          throw new Ili2cSemanticException();
      }
      if(isCircular()!=origin.isCircular()){
          throw new Ili2cSemanticException();
      }
      RefSystemRef refSys = getReferenceSystem();
      RefSystemRef originRefSys = origin.getReferenceSystem();
      if(refSys==null && originRefSys==null) {
          // equal, ok
      }else {
          if(refSys==null || originRefSys==null) {
              // not equal
              throw new Ili2cSemanticException();
          }else {
              if(!refSys.getClass().equals(originRefSys.getClass())){
                  throw new Ili2cSemanticException();
              }
              if(refSys instanceof RefSystemRef.CoordDomain) {
                  if(!Element.equalElementRef(((RefSystemRef.CoordDomain) refSys).getReferredDomain(), ((RefSystemRef.CoordDomain) originRefSys).getReferredDomain())) {
                      throw new Ili2cSemanticException();
                  }
              }else if(refSys instanceof RefSystemRef.CoordDomainAxis) {
                  if(!Element.equalElementRef(((RefSystemRef.CoordDomainAxis) refSys).getReferredDomain(), ((RefSystemRef.CoordDomainAxis) originRefSys).getReferredDomain())) {
                      throw new Ili2cSemanticException();
                  }
                  if(((RefSystemRef.CoordDomainAxis) refSys).getAxisNumber()!= ((RefSystemRef.CoordDomainAxis) originRefSys).getAxisNumber()) {
                      throw new Ili2cSemanticException();
                  }
              }else if(refSys instanceof RefSystemRef.CoordSystem) {
                  MetaObject metaObj = ((RefSystemRef.CoordSystem) refSys).getSystem();
                  MetaObject originMetaObj = ((RefSystemRef.CoordSystem) originRefSys).getSystem();
                  if(!Element.equalElementRef(metaObj.getTable(),originMetaObj.getTable())) {
                      throw new Ili2cSemanticException();
                  }
                  if(!metaObj.getName().equals(originMetaObj.getName())) {
                      throw new Ili2cSemanticException();
                  }
              }else if(refSys instanceof RefSystemRef.CoordSystemAxis) {
                  MetaObject metaObj = ((RefSystemRef.CoordSystemAxis) refSys).getSystem();
                  MetaObject originMetaObj = ((RefSystemRef.CoordSystemAxis) originRefSys).getSystem();
                  if(!Element.equalElementRef(metaObj.getTable(),originMetaObj.getTable())) {
                      throw new Ili2cSemanticException();
                  }
                  if(!metaObj.getName().equals(originMetaObj.getName())) {
                      throw new Ili2cSemanticException();
                  }
                  if(((RefSystemRef.CoordSystemAxis) refSys).getAxisNumber()!= ((RefSystemRef.CoordSystemAxis) originRefSys).getAxisNumber()) {
                      throw new Ili2cSemanticException();
                  }
              }
              
          }
      }
  }
}
