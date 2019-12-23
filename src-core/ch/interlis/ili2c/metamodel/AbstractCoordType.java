
package ch.interlis.ili2c.metamodel;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;


/** A coordinate type. */
public abstract class AbstractCoordType extends BaseType
{
  protected boolean _generic=false;
  protected int nullAxis;
  protected int piHalfAxis;
  protected NumericalType[] dimensions;
  protected String crs=null;
  protected AbstractCoordType()
  {
	  
  }
  public AbstractCoordType (NumericalType[] dimensions, int nullAxis, int piHalfAxis)
  {
    this.nullAxis = nullAxis;
    this.piHalfAxis = piHalfAxis;
    this.dimensions = dimensions;

    if (dimensions == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

    for (int i = 0; i < dimensions.length; i++)
    {
      /* no dimension can be null */
      if (dimensions[i] == null)
        throw new IllegalArgumentException (rsrc.getString (
          "err_nullNotAcceptable"));

      /* if the unit is structured, it must be continuous */
      Unit u = dimensions[i].getUnit();
      if ((u instanceof StructuredUnit) && !((StructuredUnit) u).isContinuous())
        throw new IllegalArgumentException (formatMessage (
          "err_coordType_structuredUnitNotContinuous",
          u.toString()));
    }

    if ((nullAxis < 0) || (nullAxis > dimensions.length))
      throw new IllegalArgumentException (formatMessage (
        "err_rotationDefInvalid",
        Integer.toString (nullAxis),
        Integer.toString (dimensions.length)));

    if ((piHalfAxis < 0) || (piHalfAxis > dimensions.length))
      throw new IllegalArgumentException (formatMessage (
        "err_rotationDefInvalid",
        Integer.toString (piHalfAxis),
        Integer.toString (dimensions.length)));

    if ((nullAxis == piHalfAxis) && (nullAxis != 0))
      throw new IllegalArgumentException (rsrc.getString ("err_rotationDefSame"));
  }


  public AbstractCoordType (NumericalType[] dimensions)
  {
    this (dimensions, 0, 0);
  }

  public NumericalType[] getDimensions()
  {
    return dimensions;
  }


  public int getNullAxis ()
  {
    return nullAxis;
  }

  public int getPiHalfAxis ()
  {
    return piHalfAxis;
  }

  /** An abstract type is one that does describe sufficiently
      the set of possible values. A CoordType is abstract
      if one of its dimensions (which are instances of NumericalType)
      is abstract.

      @return Whether or not this type is abstract.
      @see ch.interlis.NumericType#isAbstract()
      @see ch.interlis.StructuredUnitType#isAbstract()
  */
  @Override
  public boolean isAbstract (StringBuilder err)
  {
	  if(_generic){
		  return false;
	  }
    for (int i = 0; i < dimensions.length; i++)
      if (dimensions[i].isAbstract(err))
        return true;

    return false;
  }

  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    if (!(wantToExtend instanceof AbstractCoordType)){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_coordType_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    boolean fine = true;
    AbstractCoordType other = (AbstractCoordType) with;

    if (this.getNullAxis() != other.getNullAxis())
    {
      EhiLogger.logError(formatMessage ("err_diff_coordType_nullAxis", this.toString(), other.toString()));
      fine = false;
    }

    if (this.getPiHalfAxis() != other.getPiHalfAxis())
    {
      EhiLogger.logError(formatMessage ("err_diff_coordType_piHalfAxis", this.toString(), other.toString()));
      fine = false;
    }

    fine &= checkStructuralEquivalenceOfArrays (with, this.getDimensions(), other.getDimensions(),
                               "err_diff_coordType_numDimensions");
    return fine;
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      AbstractCoordType origin=(AbstractCoordType)getTranslationOf();
      if(origin==null) {
          return;
      }
      if(nullAxis!=origin.nullAxis) {
          throw new Ili2cSemanticException();
      }
      if(piHalfAxis!=origin.piHalfAxis) {
          throw new Ili2cSemanticException();
      }
      if(crs==null && origin.crs==null) {
          
      }else {
          if(crs==null || origin.crs==null) {
              throw new Ili2cSemanticException();
          }
          if(!crs.equals(origin.crs)) {
              throw new Ili2cSemanticException();
          }
          
      }
      if(_generic!=origin._generic) {
          throw new Ili2cSemanticException();
      }
      if(dimensions.length!=origin.dimensions.length) {
          throw new Ili2cSemanticException();
      }
      for(NumericalType dim:dimensions) {
          dim.checkTranslationOf(errs,name,baseName);
      }
  }
  @Override
  protected void linkTranslationOf(Element baseElement)
  {
      super.linkTranslationOf(baseElement);
      if(dimensions.length==((AbstractCoordType)baseElement).dimensions.length) {
          for(int dimi=0;dimi<dimensions.length;dimi++) {
              NumericalType dim=dimensions[dimi];
              NumericalType baseDim=((AbstractCoordType)baseElement).dimensions[dimi];
              dim.linkTranslationOf(baseDim);
          }
      }
  }

    public AbstractCoordType clone() {
        return (AbstractCoordType) super.clone();
    }
	public boolean isGeneric() {
		return _generic;
	}
	public void setGeneric(boolean generic) {
		this._generic = generic;
	}
	public String getCrs() {
		return crs;
	}
	public String getCrs(Element domainOrAttrDef) {
		String crs=getCrs();
		if(crs==null) {
			crs=domainOrAttrDef.getMetaValue(Ili2cMetaAttrs.ILI2C_CRS);
			if(crs==null) {
			    Model model=(Model)domainOrAttrDef.getContainer(Model.class);
	            crs=model.getMetaValue(Ili2cMetaAttrs.ILI2C_CRS);
			}
		}
		return crs;
	}
	public void setCrs(String crs) {
		this.crs = crs;
	}

}
