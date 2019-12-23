/*****************************************************************************
 *
 * ReferenceType.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.*;


/** Denotes a reference to an instance of a table. For example, the value of a
    relational attribute must be a reference; therefore, RelAttribute restricts
    its domain to instances of ReferenceType (and instances of TypeAlias provided
    they are aliasing a ReferenceType).

    @version   January 28, 1999
    @author    Sascha Brawer
    @author    Gordan Vosicki - Added cloning support
*/
public class ReferenceType extends Type
{
  AbstractClassDef        referred;
  private LinkedList<AbstractClassDef> restrictedTo = new LinkedList<AbstractClassDef>();



  public ReferenceType()
  {
  }



  public AbstractClassDef getReferred ()
  {
    return referred;
  }



  /** Returns a String describing this ReferenceType that might
      be useful for debugging.
  */
  public String toString()
  {
    return "<ReferenceType to " + referred + ">";
  }


  public void setReferred (AbstractClassDef referred)
    throws java.beans.PropertyVetoException
  {
    AbstractClassDef oldValue = this.referred;
    AbstractClassDef newValue = referred;


	if (oldValue == newValue)
	  return;

    fireVetoableChange("referred", oldValue, newValue);
    this.referred = newValue;
    firePropertyChange("referred", oldValue, newValue);
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
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    if (!(wantToExtend instanceof ReferenceType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_referenceType_ExtOther"));

    ReferenceType   general = (ReferenceType) wantToExtend;

    if ((this.referred != null)
        && (general.referred != null)
        && !this.referred.isExtending (general.referred))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_referenceType_nonExtending",
        this.referred.toString(),
        general.referred.toString()));
    }
  }
  public void addRestrictedTo(AbstractClassDef classOrAssociation)
  {
	  restrictedTo.add(classOrAssociation);
	  // check if structure is a valid extension
	  if(referred!=((TransferDescription)referred.getContainer(TransferDescription.class)).INTERLIS.ANYCLASS
	  	&& !classOrAssociation.isExtending(referred)){
		throw new IllegalArgumentException (formatMessage (
			"err_referenceType_restriction",
			classOrAssociation.toString(), referred.toString()));

	  }
  }
  public Iterator<AbstractClassDef> iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }
  private boolean external=false;
  public void setExternal(boolean external)
  {
	  this.external=external;
  }
  public boolean isExternal()
  {
  	return external;
  }


  public ReferenceType clone() {
      ReferenceType cloned = (ReferenceType) super.clone();

      cloned.restrictedTo = (LinkedList<AbstractClassDef>) restrictedTo.clone();
      return cloned;
  }

  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      ReferenceType baseElement=(ReferenceType)getTranslationOf();
      if(baseElement==null) {
          return;
      }

      if(isExternal()!=baseElement.isExternal()) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInExternal",name,baseName)));
      }
      
      Ili2cSemanticException err=null;
      err=checkElementRef(getReferred(),baseElement.getReferred(),getSourceLine(),"err_diff_referencedClassMismatch");
      if(err!=null) {
          errs.add(err);
      }
      Iterator<AbstractClassDef> depIt=iteratorRestrictedTo();
      Iterator<AbstractClassDef> baseDepIt=baseElement.iteratorRestrictedTo();
      while(true) {
          if(!depIt.hasNext() || !baseDepIt.hasNext()) {
              if(depIt.hasNext()!=baseDepIt.hasNext()) {
                  errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_referencedClassMismatch")));
              }
              break;
          }
          AbstractClassDef dep=depIt.next();
          AbstractClassDef baseDep=baseDepIt.next();
          err=checkElementRef(dep,baseDep,getSourceLine(),"err_diff_referencedClassMismatch");
          if(err!=null) {
              errs.add(err);
          }
      }
      
  }
}
