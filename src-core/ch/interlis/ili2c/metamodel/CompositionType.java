

package ch.interlis.ili2c.metamodel;


import java.util.*;


/** The domain of a structure attribute.
*/
public class CompositionType extends Type
{
  Table        componentType;
  private LinkedList<Table> restrictedTo = new LinkedList<Table>();



  /** Constructs a new composition type.
  */
  public CompositionType ()
  {
	  cardinality = new Cardinality(0,Cardinality.UNBOUND);
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
      Iterator<?> iter = newValue.getAttributes ();
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


    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;


    if (!(wantToExtend instanceof CompositionType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_compositionType_ExtOther"));


    general = (CompositionType) wantToExtend;
    myComponent = this.getComponentType();
    generalComponent = general.getComponentType();

    if ((myComponent != null)
        && (generalComponent != null)
        && !myComponent.isExtending (generalComponent))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_compositionType_componentNotExt",
        myComponent.toString(), generalComponent.toString()));
    }

    checkCardinalityExtension(general);
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      CompositionType baseElement=(CompositionType)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      
      Ili2cSemanticException err=null;
      err=checkElementRef(getComponentType(),baseElement.getComponentType(),getSourceLine(),"err_diff_referencedClassMismatch");
      if(err!=null) {
          errs.add(err);
      }
      Iterator<Table> depIt=iteratorRestrictedTo();
      Iterator<Table> baseDepIt=baseElement.iteratorRestrictedTo();
      while(true) {
          if(!depIt.hasNext() || !baseDepIt.hasNext()) {
              if(depIt.hasNext()!=baseDepIt.hasNext()) {
                  errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_referencedClassMismatch")));
              }
              break;
          }
          Table dep=depIt.next();
          Table baseDep=baseDepIt.next();
          err=checkElementRef(dep,baseDep,getSourceLine(),"err_diff_referencedClassMismatch");
          if(err!=null) {
              errs.add(err);
          }
      }
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
  public java.util.Iterator<Table> iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }


    public CompositionType clone() {
        CompositionType cloned = (CompositionType) super.clone();

        if (restrictedTo != null) {
            cloned.restrictedTo = (LinkedList<Table>) restrictedTo.clone();
        }
        return cloned;
    }

}
