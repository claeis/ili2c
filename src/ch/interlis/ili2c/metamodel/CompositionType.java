

package ch.interlis.ili2c.metamodel;


import java.util.*;

import sun.security.action.GetLongAction;


/** The domain of a structure attribute.
*/
public class CompositionType extends Type
{
  Cardinality  cardinality = new Cardinality(0,Cardinality.UNBOUND);
  Table        componentType;
  boolean      ordered = false;
  private LinkedList	restrictedTo=new LinkedList(); // List<Table>



  /** Constructs a new composition type.
  */
  public CompositionType ()
  {
  }


  public boolean isMandatory ()
  {
    return cardinality.getMinimum()==0 ? false : true;
  }
  public void setMandatory (boolean mand)
  {
    if(mand){
      if(cardinality.getMinimum()==0)cardinality.setMinimum(1);
    }else{
      if(cardinality.getMinimum()>0)cardinality.setMinimum(0);
    }
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



  /** Returns the structure or table of whose instances an
      instance of this composition is composed of.
  */
  public Table getComponentType()
  {
    return componentType;
  }



  /** Sets the structure or table of whose instances an
      instance of this composition is composed of.
  */
  public void setComponentType (Table componentType)
    throws java.beans.PropertyVetoException
  {
    Table oldValue = this.componentType;
    Table newValue = componentType;


    if (newValue == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));


    if (oldValue == newValue)
      return;


    if (newValue != null)
    {
      Iterator iter = newValue.getAttributes ();
      while (iter.hasNext())
      {
        Object obj = iter.next();
        if (!(obj instanceof AttributeDef))
          continue;


        Type attrDomain = ((AttributeDef) obj).getDomain ();
        if (attrDomain != null)
          attrDomain = attrDomain.resolveAliases();


        if (attrDomain instanceof AreaType)
          throw new IllegalArgumentException (formatMessage (
            "err_compositionType_areaAttr",
            newValue.toString(), obj.toString()));
      }
    }


    fireVetoableChange ("componentType", oldValue, newValue);
    if (oldValue != null)
      oldValue.componentFor.remove (this);
    if (newValue != null)
      newValue.componentFor.add (this);
    this.componentType = newValue;
    firePropertyChange ("componentType", oldValue, newValue);
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



  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.


      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  void checkTypeExtension (Type wantToExtend)
  {
    CompositionType   general;
    Table             myComponent, generalComponent;


    super.checkTypeExtension (wantToExtend);
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;


    if (!(wantToExtend instanceof CompositionType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_compositionType_ExtOther"));


    general = (CompositionType) wantToExtend;
    myComponent = this.getComponentType();
    generalComponent = general.getComponentType();


    // compare ordering only if more than one object possible
    if (this.cardinality.getMaximum()>1 && !this.isOrdered() && general.isOrdered())
      throw new IllegalArgumentException (rsrc.getString (
        "err_compositionType_UnorderedExtOrdered"));


    if ((myComponent != null)
        && (generalComponent != null)
        && !myComponent.isExtending (generalComponent))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_compositionType_componentNotExt",
        myComponent.toString(), generalComponent.toString()));
    }


    if (!general.cardinality.isGeneralizing(this.cardinality))
      throw new IllegalArgumentException (formatMessage (
        "err_compositionType_cardExtMismatch",
        this.cardinality.toString(), general.cardinality.toString()));
  }
  public void addRestrictedTo(Table structure)
  {
	  restrictedTo.add(structure);
	  // check if structure is a valid extension
	  if(!componentType.getScopedName(null).equals("INTERLIS.ANYSTRUCTURE") && !structure.isExtending(componentType)){
		throw new Ili2cSemanticException (getSourceLine(),formatMessage (
			"err_compositionType_restriction",
			structure.toString(), componentType.toString()));


	  }
  }
  public java.util.Iterator iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }
}
