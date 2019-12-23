/*****************************************************************************
 *
 * EnumerationType.java
 * --------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.ehi.basics.logging.EhiLogger;


/** EnumerationType holds the information associated with
    Interlis enumeration types.

    @version   January 28, 1999
    @author    Sascha Brawer
    @author    Gordan Vosicki - Added cloning support
*/
public class EnumerationType extends BaseType {
  protected boolean ordered = false;
  protected boolean circular = false;
  protected Enumeration enumeration = null;

  public EnumerationType()
  {
  }



  public EnumerationType (Enumeration enumeration, boolean ordered, boolean circular)
  {
    this.ordered = ordered;
    this.circular = circular;
    this.enumeration = enumeration;
  }
  @Override
	public void setSourceLine(int sourceLine) {
		super.setSourceLine(sourceLine);
		if(enumeration!=null){
			enumeration.setSourceLine(sourceLine);
		}
	}


  /** Sets the value of the <code>ordered</code> property.

      <p>Since the <code>ordered</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.



      @param ordered    The new value for the <code>ordered</code>
                        property. Pass <code>true</code> for "ordered"
                        and <code>false</code> for "unordered".  If
                        a circular EnumerationType is changed to
                        unordered, its <code>circular</code> property
                        will change to <code>false</code> as well.

      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>circular</code> property
                 and does not agree with the change.
  */
  public void setOrdered(boolean ordered)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.ordered;
    boolean newValue = ordered;


    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;


    fireVetoableChange("ordered", oldValue, newValue);
    if ((newValue == false) && isCircular()) {
      /* If this object is going to be unordered, it can't be
         circular anymore. */
      fireVetoableChange("circular", true, false);
      circular = false;
      firePropertyChange("circular", true, false);
    }
    this.ordered = newValue;
    firePropertyChange("ordered", oldValue, newValue);
  }



  public boolean isOrdered()
  {
    return ordered;
  }



  public boolean isCircular()
  {
    return circular;
  }



  /** Sets the value of the <code>circular</code> property.
      The elements of a <em>circular enumeration</em> are ordered,
      as it is the case for <em>ordered</em> enumerations.
      In addition, the successor of the last element is the first,
      and the predecessor of the first element is the last.
      <p>Since the <code>circular</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.

      @see #isCircular()


      @param circular   The new value for the <code>circular</code>
                        property. Pass <code>true</code> for "circular"
                        and <code>false</code> for "not circular".

      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>circular</code> property
                 and does not agree with the change.
  */
  public void setCircular(boolean circular)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.circular;
    boolean newValue = circular;


    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;


    fireVetoableChange("circular", oldValue, newValue);
    if ((newValue == true) && !isOrdered()) {
      /* If this object is going to be circular, it must be
         ordered, too. */
      fireVetoableChange("ordered", false, true);
      ordered = true;
      firePropertyChange("ordered", false, true);
    }
    this.circular = newValue;
    firePropertyChange("circular", oldValue, newValue);
  }



  public Enumeration getEnumeration ()
  {
    return enumeration;
  }
  public Enumeration getConsolidatedEnumeration ()
  {
    if(extending==null){
      return new Enumeration(enumeration);
    }
    Enumeration ret=((EnumerationType)extending.resolveAliases()).getConsolidatedEnumeration();
    if(enumeration.isFinal()){
      ret.setFinal(true);
    }
    // add elements defined by this
    Iterator<Enumeration.Element> elei = enumeration.getElements();
    while(elei.hasNext()){
      Enumeration.Element ele = elei.next();
      mergeTree(ret,ele);
    }
    return ret;
  }
  static void mergeTree(Enumeration tree,Enumeration.Element newele){
    Iterator<Enumeration.Element> desti = tree.getElements();
    while(desti.hasNext()){
      Enumeration.Element dest = desti.next();
      if(dest.getName().equals(newele.getName())){
        // found
        // has newele a substree?
        if(newele.getSubEnumeration()!=null){
          if(dest.getSubEnumeration()!=null){
            // merge subtree
            if(newele.getSubEnumeration().isFinal()){
              dest.getSubEnumeration().setFinal(true);
            }
            Iterator<Enumeration.Element> elei = newele.getSubEnumeration().getElements();
            while(elei.hasNext()){
              Enumeration.Element ele = elei.next();
              mergeTree(dest.getSubEnumeration(),ele);
            }
          }else{
            // dest hasn't yet a subtree
            // add subtree as a copy
            dest.setSubEnumeration(new Enumeration(newele.getSubEnumeration()));
          }
        }
        return;
      }
    }
    // not found
    // add element as a copy
    tree.addElement(new Enumeration.Element(newele));
  }

  public void setEnumeration (Enumeration enumeration)
    throws java.beans.PropertyVetoException
  {
    Enumeration oldValue = this.enumeration;
    Enumeration newValue = enumeration;

    fireVetoableChange("enumeration", oldValue, newValue);
    this.enumeration = newValue;
    firePropertyChange("enumeration", oldValue, newValue);
    cachedValues=null;
  }

  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  @Override
  void checkTypeExtension (Type wantToExtend)
  {
	  checkTypeExtension(wantToExtend, true);
  }
  public void checkTypeExtension (Type wantToExtend,boolean allowDuplicateLeafs)
  {
    EnumerationType   general;
    Enumeration       thisEnum;
    Enumeration       generalEnum;

    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    if (!(wantToExtend instanceof EnumerationType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_enumerationType_ExtOther"));

    if(wantToExtend==this){
    	return;
    }
    general = (EnumerationType) wantToExtend;
    thisEnum = this.enumeration;
    generalEnum = general.getConsolidatedEnumeration();

    // check paths
    // check horizontal extension
    // check elements defined by this
    Iterator elei=thisEnum.getElements();
    while(elei.hasNext()){
      Enumeration.Element ele=(Enumeration.Element)elei.next();
      checkTree(generalEnum,ele,allowDuplicateLeafs);
    }
    checkCardinalityExtension(wantToExtend);

  }
  public Set getDirectExtensions()
  {
  	return extendedBy;
  }
  static void checkTree(Enumeration baseTree,Enumeration.Element extEle,boolean allowDuplicateLeafs){
    Iterator<Enumeration.Element> baseElei=baseTree.getElements();
    while(baseElei.hasNext()){
      Enumeration.Element baseEle = baseElei.next();
      if(baseEle.getName().equals(extEle.getName())){
        // found
        // substree?
        if(extEle.getSubEnumeration()!=null
            && baseEle.getSubEnumeration()!=null){
            // check subtree
            Iterator<Enumeration.Element> elei=extEle.getSubEnumeration().getElements();
            while(elei.hasNext()){
              Enumeration.Element ele = elei.next();
              checkTree(baseEle.getSubEnumeration(),ele,allowDuplicateLeafs);
            }
        }else if(extEle.getSubEnumeration()==null){
        	if(!allowDuplicateLeafs){
            	// illegal duplicate element in extension
                throw new Ili2cSemanticException(extEle.getSourceLine(),formatMessage (
                        "err_enumerationType_DupEle",extEle.getName()));
        	}
        }
        return;
      }
    }
    // not found, therfore new element
    // no additional elements allowed
    if(baseTree.isFinal()){
      throw new Ili2cSemanticException (extEle.getSourceLine(),formatMessage (
        "err_enumerationType_ExtFinal",extEle.getName()));
    }
  }

  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    EnumerationType other = (EnumerationType) with;
    boolean fine = true;
    if (this.isCircular() != other.isCircular())
    {
      EhiLogger.logError(formatMessage ("err_diff_enumType_mismatchInCircularity",
                       this.toString(),
                       with.toString()));
      fine = false;
    }

    if (this.isOrdered() != other.isOrdered())
    {
      EhiLogger.logError(formatMessage ("err_diff_enumType_mismatchInOrderedness",
                       this.toString(),
                       with.toString()));
      fine = false;
    }


    if (!areEnumerationsStructurallyEquivalent(this.getEnumeration(), other.getEnumeration()))
    {
      fine = false;
    }


    return fine;
  }


  private boolean areEnumerationsStructurallyEquivalent (Enumeration a, Enumeration b)
  {
    if (a == b) /* checks for the case where a, b both are null */
      return true;

    if ((a == null) || (b == null))
      return false;

    Iterator<Enumeration.Element> iter_a = a.getElements();
    Iterator<Enumeration.Element> iter_b = b.getElements();

    while (iter_a.hasNext() && iter_b.hasNext())
    {
      Enumeration.Element elt_a = iter_a.next();
      Enumeration.Element elt_b = iter_b.next();

      if ((elt_a == null) != (elt_b == null))
        return false;

      if ((elt_a != null) && (elt_b != null)
          && !areEnumerationsStructurallyEquivalent (elt_a.getSubEnumeration (),
                                                  elt_b.getSubEnumeration ()))
      {
        return false;
      }
    }

    if (iter_a.hasNext() != iter_b.hasNext())
      return false;

    return true;
  }


  public EnumerationType clone() {
      EnumerationType cloned = (EnumerationType) super.clone();

      if (enumeration != null) {
          cloned.enumeration = enumeration.clone();
      }
      return cloned;
  }

  private java.util.ArrayList<String> cachedValues=null;
  public java.util.List<String> getValues()
  {
	  if(cachedValues==null){
		  cachedValues=new java.util.ArrayList<String>();
	      buildEnumList(cachedValues,"",getConsolidatedEnumeration());
	  }
      return cachedValues;
  }
  public static void buildEnumList(java.util.List<String> accu,String prefix1,Enumeration enumer){
      Iterator iter = enumer.getElements();
      String prefix="";
      if(prefix1.length()>0){
        prefix=prefix1+".";
      }
      while (iter.hasNext()) {
        Enumeration.Element ee=(Enumeration.Element) iter.next();
        Enumeration subEnum = ee.getSubEnumeration();
        if (subEnum != null)
        {
          // ee is not leaf, add its name to prefix and add sub elements to accu
          buildEnumList(accu,prefix+ee.getName(),subEnum);
        }else{
          // ee is a leaf, add it to accu
          accu.add(prefix+ee.getName());
        }
      }
  }
	@Override
  	protected void linkTranslationOf(Element baseElement)
  	{
	    super.linkTranslationOf(baseElement);
		enumeration.linkTranslationOf(((EnumerationType)baseElement).enumeration);
  	}  
	  @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs, String name, String baseName) {
        super.checkTranslationOf(errs, name, baseName);
        EnumerationType baseElement = (EnumerationType) getTranslationOf();
        if (baseElement == null) {
            return;
        }

        if (isCircular() != baseElement.isCircular()) {
            throw new Ili2cSemanticException();
        }
        if (isOrdered() != baseElement.isOrdered()) {
            throw new Ili2cSemanticException();
        }
        if (enumeration.isFinal() != baseElement.enumeration.isFinal()) {
            throw new Ili2cSemanticException();
        }
    }
}
