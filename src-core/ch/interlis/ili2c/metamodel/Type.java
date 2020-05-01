/*****************************************************************************
 *
 * Type.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.*;


/** Type is an abstract class which serves as a common
    superclass for all Interlis Types (= domain definitions).

    @version   January 28, 1999
    @author    Sascha Brawer
    @author    Gordan Vosicki - Normalized clone().
*/
public abstract class Type
  extends AbstractLeafElement
  implements Cloneable
{
  protected Type       extending = null;
  protected Set<Type>  extendedBy = new HashSet<Type>(2);
  protected Cardinality  cardinality = new Cardinality(0,1);
  private boolean      ordered = false;


  protected Type()
  {
  }

    public boolean isBoolean() {
        Type type=this;
        while (type instanceof TypeAlias) {
            Domain domain = ((TypeAlias) type).getAliasing();
            TransferDescription td = (TransferDescription) domain.getContainer(TransferDescription.class);
            if (domain == td.INTERLIS.BOOLEAN) {
                return true;
            }
            type=domain.getType();
        }
        return false;
    }

    public Type clone() {
        Type cloned = null;

        try {
            cloned = (Type) super.clone();
            cloned.extendedBy = new HashSet<Type>(2);

            if (cloned.extending != null) {
                cloned.extending.extendedBy.add(cloned);
            }
            cloned.cardinality = cardinality.clone();
        } catch (CloneNotSupportedException e) {
            // Never happens because the object is cloneable
        }
        return cloned;
    }


  /** An abstract type is one that does describe sufficiently
      the set of possible values.

      @return Whether or not this type is abstract.
  */
  public boolean isAbstract (StringBuilder err)
  {
    return false;
  }
  public boolean isAbstract ()
  {
    return isAbstract(new StringBuilder());
  }

  /** Returns whether or not this composition type is ordered.
  */
  public boolean isOrdered()
  {
    return ordered;
  }




/** Sets whether or not this composition type is ordered.
  */
  public void setOrdered(boolean ordered)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.ordered;
    boolean newValue = ordered;


	if (oldValue == newValue)
	  return;


    fireVetoableChange("ordered", oldValue, newValue);
    this.ordered = newValue;
    firePropertyChange("ordered", oldValue, newValue);
  }

  /** Returns the cardinality of the composition.
  */
  public Cardinality getCardinality ()
  {
    return cardinality;
  }



  /** Sets the cardinality of the composition.
  */
  public void setCardinality (Cardinality cardinality)
    throws java.beans.PropertyVetoException
  {
    Cardinality oldValue = this.cardinality;
    Cardinality newValue = cardinality;


    if (newValue == null)
      throw new IllegalArgumentException();


    if (newValue.equals(oldValue)){
      return;
    }


    fireVetoableChange ("cardinality", oldValue, newValue);
    this.cardinality = newValue;
    firePropertyChange ("cardinality", oldValue, newValue);
  }
  
  public boolean isMandatory ()
  {
    return cardinality.getMinimum()==0 ? false : true;
  }

  public boolean isMandatoryConsideringAliases ()
  {
    return isMandatory();
  }



  /** @exception IllegalArgumentException If <code>mand</code> is
  				 <code>false</code> and Type extends
  				 a mandatory Type.
  */
  public void setMandatory (boolean mand)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = isMandatory();
    boolean newValue = mand;

    fireVetoableChange("mandatory", oldValue, newValue);
    if(mand){
        if(cardinality.getMinimum()==0)cardinality.setMinimum(1);
      }else{
        if(cardinality.getMinimum()>0)cardinality.setMinimum(0);
      }
    firePropertyChange("mandatory", oldValue, newValue);
  }

  public Element getExtending()
  {
  	return extending;
  }

  public void setExtending (Type extending)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.extending;
    Type newValue = extending;

    /* Check whether there is anything to do. The JavaBeans
       specification strongly recommends this check to avoid
       certain infinite loops which might occur otherwise.
    */
    if (oldValue == newValue)
      return;

    /* Ensure that the extension graph will be acyclic. */
    if ((newValue != null) && (newValue.isExtendingIndirectly (this)))
      throw new IllegalArgumentException(formatMessage(
        "err_cyclicExtension", this.toString(), newValue.toString()));


    /* Check that the argument is valid. */
    checkTypeExtension (newValue);



    /* Give interested parties a chance to oppose to the change. */
	fireVetoableChange ("extending", oldValue, newValue);

    /* Perform the change. */
    if (oldValue != null)
      oldValue.extendedBy.remove (this);
    this.extending = newValue;
    if (newValue != null)
      newValue.extendedBy.add (this);


    /* Inform interested parties about the change. */
    firePropertyChange ("extending", oldValue, newValue);
  }



  /** @return whether or not <code>typ</code> is extending
              <code>dd</code>
  */
  boolean isExtendingIndirectly (Type typ)
  {
    for (Type parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == typ)
        return true;
    }
    return false;
  }



  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */

  abstract void checkTypeExtension (Type wantToExtend);
  void checkCardinalityExtension (Type general)
  {
	  
	    // compare ordering only if more than one object possible
	    if (this.cardinality.getMaximum()>1 && !this.isOrdered() && general.isOrdered())
	      throw new IllegalArgumentException (rsrc.getString (
	        "err_compositionType_UnorderedExtOrdered"));
	    
	    if (!general.cardinality.isGeneralizing(this.cardinality))
	        throw new IllegalArgumentException (formatMessage (
	          "err_compositionType_cardExtMismatch",
	          this.cardinality.toString(), general.cardinality.toString()));
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      Type   origin=(Type)getTranslationOf();

      if (origin == null){
          return;
      }
      if(isAbstract()!=origin.isAbstract()) {
          throw new Ili2cSemanticException();
      }
      if(!getCardinality().equals(origin.getCardinality())) {
          throw new Ili2cSemanticException();
      }
      if(isOrdered()!=origin.isOrdered()) {
          throw new Ili2cSemanticException();
      }
  } 


  /** If this type is a TypeAlias, resolve the chain of aliases
      until a non-alias type is found. If this type is not an alias,
      this type is returned directly.

      @see TypeAlias#resolveAliases()
  */
  public Type resolveAliases()
  {
    return this;
  }

  public static final Type findReal (Type potentialAlias)
  {
    if (potentialAlias instanceof TypeAlias)
      return findReal (potentialAlias.resolveAliases ());

    return potentialAlias;
  }


}
