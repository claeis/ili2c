/*****************************************************************************
 *
 * LineType.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** An abstract class that groups the INTERLIS line types such as
    AreaType, PolylineType and SurfaceType.
*/
public abstract class LineType extends Type
{
  protected PrecisionDecimal    maxOverlap = null;
  protected LineForm[] lineForms = new LineForm[0];
  protected Domain controlPointDomain = null;

  /** An concrete type is one that does describe sufficiently
      the set of possible values.

      @return Whether or not this type is abstract.
  */
  @Override
  public boolean isAbstract (StringBuilder err)
  {
    if (getLineForms().length == 0){
    	err.append("missing line form");
      return true;
    }
    if (getControlPointDomain() == null){
    	err.append("missing VERTEX");
      return true;
    }

    if (getControlPointDomain().isAbstract()){
    	err.append("DomainDef "+getControlPointDomain().getName()+" is abstract");
      return true;
    }

    return false;
  }


  public PrecisionDecimal getMaxOverlap ()
  {
	if(maxOverlap==null && extending!=null){
		return ((LineType)extending.resolveAliases()).getMaxOverlap();
	}
    return maxOverlap;
  }
  public PrecisionDecimal getDefinedMaxOverlap ()
  {
    return maxOverlap;
  }


  public void setMaxOverlap (PrecisionDecimal maxOverlap)
    throws java.beans.PropertyVetoException
  {
    PrecisionDecimal oldValue = this.maxOverlap;
    PrecisionDecimal newValue = maxOverlap;

    if (oldValue == newValue)
      return;

    if ((extending != null) && (newValue != null))
      throw new IllegalArgumentException (rsrc.getString (
        "err_lineType_overlapInExtension"));

    fireVetoableChange ("maxOverlap", oldValue, newValue);
    this.maxOverlap = newValue;
    firePropertyChange ("maxOverlap", oldValue, newValue);
  }


  public LineForm[] getLineForms()
  {
		if(lineForms.length==0 && extending!=null){
			return ((LineType)extending.resolveAliases()).getLineForms();
		}
    return lineForms;
  }
  public LineForm[] getDefinedLineForms()
  {
    return lineForms;
  }


  public void setLineForms (LineForm[] lineForms)
    throws java.beans.PropertyVetoException
  {
    LineForm[] oldValue = this.lineForms;
    LineForm[] newValue = lineForms;

    if (oldValue == newValue)
      return;

    if (newValue == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

    if (extending != null)
      throw new IllegalStateException ("This order is not implemented; call setExtending() later");

    fireVetoableChange ("lineForms", oldValue, newValue);
    this.lineForms = newValue;
    firePropertyChange ("lineForms", oldValue, newValue);
  }


  public Domain getControlPointDomain()
  {
		if(controlPointDomain==null && extending!=null){
			return ((LineType)(extending.resolveAliases())).getControlPointDomain();
		}
    return controlPointDomain;
  }
  public Domain getDefinedControlPointDomain()
  {
    return controlPointDomain;
  }


  public void setControlPointDomain (Domain controlPointDomain)
    throws java.beans.PropertyVetoException
  {
    Domain oldValue = this.controlPointDomain;
    Domain newValue = controlPointDomain;
    Type newValueType;

    if (oldValue == newValue)
      return;

    if (extending != null)
      throw new IllegalStateException (
        "This order is not implemented; call setExtending() later");

    if (newValue == null)
      newValueType = null;
    else
      newValueType = newValue.getType ();

    if (newValueType != null)
      newValueType = newValueType.resolveAliases ();

    if ((newValueType != null) && !(newValueType instanceof CoordType))
      throw new IllegalArgumentException (formatMessage (
        "err_lineType_vertexNotCoordType", newValue.toString()));

    fireVetoableChange ("controlPointDomain", oldValue, newValue);
    this.controlPointDomain = newValue;
    firePropertyChange ("controlPointDomain", oldValue, newValue);
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
    LineType   general;

    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    general = (LineType) wantToExtend;
    if (general.lineForms.length > 0)
    {
      for (int i = 0; i < lineForms.length; i++)
      {
        boolean found = false;
        for (int j = 0; j < general.lineForms.length; j++)
        {
          if (lineForms[i] == general.lineForms[j])
            found = true;
        }

        if (!found)
          throw new IllegalArgumentException (formatMessage (
            "err_lineType_addedlineFormToInherited", lineForms[i].toString()));
      }
    }

    if ((controlPointDomain != null)
      && (general.controlPointDomain != null)
      && !controlPointDomain.isExtendingIndirectly (general.controlPointDomain))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_lineType_controlPtDomainNotExtending",
        controlPointDomain.toString(),
        general.controlPointDomain.toString()));
    }
    PrecisionDecimal generalMaxOverlap=general.getMaxOverlap();
    if(maxOverlap!=null && generalMaxOverlap!=null && generalMaxOverlap.compareTo(maxOverlap)==-1){
    	throw new Ili2cSemanticException (formatMessage ("err_lineType_moreOverlapInExtension"));
    }
  }

  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      LineType origin=(LineType)getTranslationOf();
      if(origin==null) {
          return;
      }
      if(!PrecisionDecimal.equals(maxOverlap,origin.maxOverlap)) {
          throw new Ili2cSemanticException();
      }
      if(lineForms==null && origin.lineForms==null) {
          
      }else{
          if(lineForms==null || origin.lineForms==null) {
              throw new Ili2cSemanticException();
          }
          if(lineForms.length!=origin.lineForms.length) {
              throw new Ili2cSemanticException();
          }
          ArrayList<LineForm> lf=new ArrayList<LineForm>(lineForms.length);
          Collections.addAll(lf, lineForms);
          ArrayList<LineForm> originLf=new ArrayList<LineForm>(origin.lineForms.length);
          Collections.addAll(originLf, origin.lineForms);
          Collections.sort(lf,new TranslatedElementNameComparator());
          Collections.sort(originLf,new TranslatedElementNameComparator());
          for(int i=0;i<lineForms.length;i++) {
              if(!Element.equalElementRef(lf.get(i),originLf.get(i))) {
                  throw new Ili2cSemanticException();
              }
              if(!Element.equalElementRef(lf.get(i).getSegmentStructure(),originLf.get(i).getSegmentStructure())) {
                  throw new Ili2cSemanticException();
              }
          }
      }
      if(this.controlPointDomain == origin.controlPointDomain) {
          // ok
      }else {
          if(this.controlPointDomain==null || origin.controlPointDomain==null) {
              throw new Ili2cSemanticException();
          }
          if(controlPointDomain.getTranslationOfOrSame()!=origin.controlPointDomain.getTranslationOfOrSame()) {
              throw new Ili2cSemanticException();
          }
      }
  }

  public LineType clone() {
      return (LineType) super.clone();
  }
  /** maximum stroke to use when removing ARCs.
   * @return maximum stroke
   */
  public double getP()
  {
		double p;
		CoordType coordType=(CoordType)getControlPointDomain().getType();
		NumericalType dimv[]=coordType.getDimensions();
		int accuracy=((NumericType)dimv[0]).getMaximum().getAccuracy();
		if(accuracy==0){
			p=0.5;
		}else{
			p=Math.pow(10.0,-accuracy);
			//EhiLogger.debug("accuracy "+accuracy+", p "+p);
		}
	  return p;
  }

}
