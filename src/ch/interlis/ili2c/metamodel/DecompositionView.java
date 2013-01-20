/*****************************************************************************
 *
 * DecompositionView.java
 * ----------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;



/** A special type of View that is based on decomposing a composition attribute
    of another Viewable.


    @author Sascha Brawer, sb@adasys.ch
 */
public class DecompositionView extends UnextendableView
{
	private ViewableAlias renamedViewable=null;
  protected ObjectPath decomposedAttribute = null;


  boolean areaDecomposition = false;


  /** Construct a new SelectionView.
  */
  public DecompositionView ()
  {
  }



  /** Returns the decomposed attribute.
  */
  public ObjectPath getDecomposedAttribute ()
  {
    return decomposedAttribute;
  }



  /** Sets the decomposed attribute.


      <p>In JavaBeans terminology, the <code>decomposedAttribute</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>decomposedAttribute</code> property
                 and does not agree with the change.


      @exception java.lang.IllegalArgumentException if decomposedAttribute
                 does not lead to an attribute whose domain is either
                 a composition, a surface or an area.


      @exception java.lang.IllegalArgumentException if this decomposition
                 view is an area decomposition,
                 but the decomposed attribute does not have a
                 domain that is an instance of ch.interlis.AreaType.
  */
  public void setDecomposedAttribute (ObjectPath decomposedAttribute)
    throws java.beans.PropertyVetoException
  {
    ObjectPath oldValue = this.decomposedAttribute;
    ObjectPath newValue = decomposedAttribute;

    if (oldValue == newValue)
      return;


    checkStateConsistency (newValue, areaDecomposition);


    fireVetoableChange ("decomposedAttribute", oldValue, newValue);
    this.decomposedAttribute = newValue;
    firePropertyChange ("decomposedAttribute", oldValue, newValue);
  }


  /** Returns whether or not this DecompositionView is an area decomposition.
      Area decompositions return each line only once.
  */
  public boolean isAreaDecomposition ()
  {
    return areaDecomposition;
  }



  /** Sets whether this decomposition is an area decomposition.


      <p>In JavaBeans terminology, the <code>areaDecomposition</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>areaDecomposition</code> property
                 and does not agree with the change.


      @exception java.lang.IllegalArgumentException if
                 <code>areaDecomposition</code> is <code>true</code>,
                 but the decomposed attribute does not have a
                 domain that is an instance of ch.interlis.AreaType.
  */
  public void setAreaDecomposition (boolean areaDecomposition)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.areaDecomposition;
    boolean newValue = areaDecomposition;


    if (oldValue == newValue)
      return;


    checkStateConsistency (decomposedAttribute, newValue);


    fireVetoableChange ("areaDecomposition", oldValue, newValue);
    this.areaDecomposition = newValue;
    firePropertyChange ("areaDecomposition", oldValue, newValue);
  }



  private void checkStateConsistency (
    ObjectPath  decomposedAttribute,
    boolean        areaDecomposition)
  {
    Type decomposedType = null;


    if (decomposedAttribute != null)
      decomposedType = Type.findReal (decomposedAttribute.getType ());


    if (decomposedType == null)
      return;


    if (!(decomposedType instanceof SurfaceOrAreaType)
        && !(decomposedType instanceof CompositionType)
        && !(decomposedType instanceof PolylineType)
        )
    {
      throw new IllegalArgumentException (formatMessage (
        "err_decompositionView_notDecomposable",
        decomposedAttribute.toString()));
    }


    if (areaDecomposition && !(decomposedType instanceof AreaType))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_decompositionView_areaDecompNotOnArea",
        this.toString(), decomposedAttribute.toString()));
    }
  }



public ViewableAlias getRenamedViewable() {
	return renamedViewable;
}



public void setRenamedViewable(ViewableAlias renamedViewable) {
	this.renamedViewable = renamedViewable;
}

}
