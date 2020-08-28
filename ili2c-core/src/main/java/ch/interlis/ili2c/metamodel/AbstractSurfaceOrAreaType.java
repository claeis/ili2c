/*****************************************************************************
 *
 * SurfaceOrAreaType.java
 * ----------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.beans.PropertyVetoException;
import java.util.List;


/** An abstract class that groups SurfaceType and AreaType, because these
    two have many things in common.
*/
public abstract class AbstractSurfaceOrAreaType extends LineType
{
  /* holds line attributes */
  protected Table lineAttributeStructure = null;





  protected AbstractSurfaceOrAreaType ()
  {
  }


  /** Returns the abstract structure (an extension of INTERLIS.SurfaceLine)
      that specifies the line attributes.
  */
  public Table getLineAttributeStructure ()
  {
    return lineAttributeStructure;
  }



  private Table implicitSurfaceEdge=null;
  /** Returns the concrete structure (an extension of INTERLIS.SurfaceEdge)
      that is inserted by the compiler to hold the surface boundary.
  */
  public Table getImplicitSurfaceEdgeStructure ()
    throws java.beans.PropertyVetoException
  {
    if(implicitSurfaceEdge==null){
      implicitSurfaceEdge=new Table();
	/*
	  STRUCTURE SurfaceEdge =
	    Geometry: DIRECTED POLYLINE;
	    LineAttrs: ANYSTRUCTURE;
	  END SurfaceEdge;
	*/
      implicitSurfaceEdge.setName ("SurfaceEdge");
      implicitSurfaceEdge.setIdentifiable (false);
      LocalAttribute surfaceEdge_geometry = new LocalAttribute ();
      PolylineType geomType = new PolylineType ();
      geomType.setDirected (true);
      geomType.setControlPointDomain(getControlPointDomain());
      geomType.setLineForms(getLineForms());
      geomType.setMaxOverlap(getMaxOverlap());
      surfaceEdge_geometry.setName ("Geometry");
      surfaceEdge_geometry.setDomain (geomType);
      implicitSurfaceEdge.add (surfaceEdge_geometry);
      LocalAttribute surfaceEdge_lineAttrs = new LocalAttribute ();
      CompositionType typ = new CompositionType ();
      if(getLineAttributeStructure()!=null){
        typ.setComponentType(getLineAttributeStructure());
      }else{
        // create an empty STRCUTURE if no line attributes defined
        Table dummyla=new Table();
        dummyla.setIdentifiable (false);
        typ.setComponentType(dummyla);
      }
      typ.setCardinality(new Cardinality(1,1));
      surfaceEdge_lineAttrs.setName ("LineAttrs");
      surfaceEdge_lineAttrs.setDomain (typ);
      implicitSurfaceEdge.add (surfaceEdge_lineAttrs);
    }
    return implicitSurfaceEdge;
  }

  private Table implicitSurfaceBoundary=null;

  /** Returns the concrete structure (an extension of INTERLIS.SurfaceBoundary)
      that is inserted by the compiler to hold the individual lines that
      form together the surface.
  */
  public Table getImplicitSurfaceBoundaryStructure ()
    throws java.beans.PropertyVetoException
  {
    if(implicitSurfaceBoundary==null){
      implicitSurfaceBoundary=new Table();
	/*
	  STRUCTURE SurfaceBoundary =
	    Lines: LIST OF SurfaceEdge;
	  END SurfaceBoundary;
	*/
      implicitSurfaceBoundary.setName ("SurfaceBoundary");
      implicitSurfaceBoundary.setIdentifiable (false);
      CompositionType ct = new CompositionType ();
      ct.setComponentType (getImplicitSurfaceEdgeStructure());
      ct.setOrdered (true);
      LocalAttribute surfaceBoundary_Lines;
      surfaceBoundary_Lines = new LocalAttribute ();
      surfaceBoundary_Lines.setName ("Lines");
      surfaceBoundary_Lines.setDomain (ct);
      implicitSurfaceBoundary.add (surfaceBoundary_Lines);
    }
    return implicitSurfaceBoundary;
  }

  /** Sets the value of the <code>lineAttributeStructure</code> property.
      LineTypes are allowed to specify a structure for its line
      attributes.

      @param lineAttributeStructure  The structure which defines
             the attributes used for this LineType.
  */
  public void setLineAttributeStructure (Table lineAttributeStructure)
    throws java.beans.PropertyVetoException
  {
    TransferDescription td;

    Table oldValue = this.lineAttributeStructure;
    Table newValue = lineAttributeStructure;

    if (oldValue == newValue)
      return;

    if (newValue == null)
      throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));

    td = (TransferDescription) newValue.getContainer (TransferDescription.class);

    try
    {
      fireVetoableChange ("lineAttributeStructure", oldValue, newValue);
    }
    catch (PropertyVetoException pvex)
    {
      throw pvex;
    }
    this.lineAttributeStructure = newValue;
    firePropertyChange ("lineAttributeStructure", oldValue, newValue);
  }

  /** Changes the maximal overlap and performs a number of additional
      actions to what any LineType does.

      <ol><li>The call is forwarded to the Geometry attribute of
              the implicit line structure.</li>
          <li>The abstractness of that Geometry attribute is
              set to the abstractness of this type.</li>
          <li>The abstractness of the implicit line structure
              is set to the abstractness of its Geometry
              attribute.</li>
      </ol>
  */
  public void setMaxOverlap (PrecisionDecimal maxOverlap)
    throws java.beans.PropertyVetoException
  {
    super.setMaxOverlap (maxOverlap);
  }



  /** Changes the line forms and performs a number of additional
      actions to what any LineType does.

      <ol><li>The call is forwarded to the Geometry attribute of
              the implicit line structure.</li>
          <li>The abstractness of that Geometry attribute is
              set to the abstractness of this type.</li>
          <li>The abstractness of the implicit line structure
              is set to the abstractness of its Geometry
              attribute.</li>
      </ol>
  */
  public void setLineForms (LineForm[] lineForms)
    throws java.beans.PropertyVetoException
  {
    super.setLineForms (lineForms);
  }


  /** Changes the control point domain and performs a number of additional
      actions to what any LineType does.

      <ol><li>The call is forwarded to the Geometry attribute of
              the implicit line structure.</li>
          <li>The abstractness of that Geometry attribute is
              set to the abstractness of this type.</li>
          <li>The abstractness of the implicit line structure
              is set to the abstractness of its Geometry
              attribute.</li>
      </ol>
  */
  public void setControlPointDomain (Domain controlPointDomain)
    throws java.beans.PropertyVetoException
  {
    super.setControlPointDomain (controlPointDomain);
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
    super.checkTypeExtension (wantToExtend);
    if (wantToExtend instanceof AbstractSurfaceOrAreaType)
    {
      Table shouldExtend;

      shouldExtend = ((AbstractSurfaceOrAreaType) wantToExtend).lineAttributeStructure;
      if ((lineAttributeStructure != null) && (shouldExtend != null)
        && !lineAttributeStructure.isExtending (shouldExtend))
      {
        throw new IllegalArgumentException (formatMessage (
          "err_lineType_lineAttrNotExtInherited",
          lineAttributeStructure.toString (),
          shouldExtend.toString ()));
      }
    }
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      AbstractSurfaceOrAreaType origin=(AbstractSurfaceOrAreaType)getTranslationOf();
      if(origin==null) {
          return;
      }
      if(!Element.equalElementRef(getLineAttributeStructure(), origin.getLineAttributeStructure())) {
          throw new Ili2cSemanticException();
      }
  }
}
