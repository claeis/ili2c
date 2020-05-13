/*****************************************************************************
 *
 * PolylineType.java
 * -----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** The type which is represented in INTERLIS using the keywords
    <code>DIRECTED POLYLINE</code> and <code>POLYLINE</code>.
*/
public class PolylineType extends LineType
{
  protected boolean directed = false;

  /** Constructs a new, undirected PolylineType.
  */
  public PolylineType ()
  {
  }


  /** Returns "DIRECTED POLYLINE" or "POLYLINE", depending
      on whether or not the PolylineType is directed.
  */
  public String toString ()
  {
    if (directed)
      return "DIRECTED POLYLINE";
    else
      return "POLYLINE";
  }


  /** Returns whether or not this PolylineType is directed.
  */
  public boolean isDirected ()
  {
    return directed;
  }


  /** Makes this PolylineType directed or undirected.

      <p>In JavaBeans terminology, the <code>directed</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>directed</code> property
                 and does not agree with the change.
  */
  public void setDirected (boolean directed)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = this.directed;
    boolean newValue = directed;

    if (oldValue == newValue)
      return;

    fireVetoableChange("directed", oldValue, newValue);
    this.directed = newValue;
    firePropertyChange("directed", oldValue, newValue);
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
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    if (!(wantToExtend instanceof PolylineType))
    {
      throw new IllegalArgumentException (
        rsrc.getString ("err_polylineType_ExtOther"));
    }
    if (this.isDirected ())
    {
    }
    else
    {
      if (((PolylineType) wantToExtend).isDirected())
      {
        throw new IllegalArgumentException (
          rsrc.getString ("err_polylineType_ExtDirPoly"));
      }
    }
    checkCardinalityExtension(wantToExtend);
  }
  private Table implicitLineGeometry=null;
  /** Returns the concrete structure (an extension of INTERLIS.LineGeometry)
      that is inserted by the compiler to hold the surface boundary.
  */
  public Table getImplicitLineGeometryStructure ()
    throws java.beans.PropertyVetoException
  {
    if(implicitLineGeometry==null){
        Table lineSegment=new Table();
/*	    try {
	      lineSegment.setBeanContext(this);
	    } catch (java.beans.PropertyVetoException pve) {
	      throw new IllegalArgumentException(pve.getLocalizedMessage());
	    }
*/
        /*
	  STRUCTURE LineSegment (ABSTRACT) =
	    SegmentEndPoint: MANDATORY LineCoord;
	  END LineSegment;
  	*/
      LocalAttribute lineSegment_ep = new LocalAttribute ();
      lineSegment_ep.setName ("SegmentEndPoint");
      lineSegment_ep.setDomain (getControlPointDomain().getType());
      //lineSegment_ep.setMandatory(true);
      lineSegment.add (lineSegment_ep);

      implicitLineGeometry=new Table();
	/*
	  STRUCTURE LineGeometry =
	    Segments: LIST OF LineSegment;
	  MANDATORY CONSTRAINT isOfClass (Segments[FIRST],INTERLIS.StartSegment)
	  END LineGeometry;
	*/
      implicitLineGeometry.setName ("LineGeometry");
      implicitLineGeometry.setIdentifiable (false);
      CompositionType ct = new CompositionType ();
      ct.setComponentType (lineSegment);
      ct.setOrdered (true);
      LocalAttribute lineGeometry_segments;
      lineGeometry_segments = new LocalAttribute ();
      lineGeometry_segments.setName ("Segments");
      lineGeometry_segments.setDomain (ct);
      implicitLineGeometry.add(lineGeometry_segments);
    }
    return implicitLineGeometry;
  }
  @Override
  protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  {
      super.checkTranslationOf(errs,name,baseName);
      PolylineType origin=(PolylineType)getTranslationOf();
      if(origin==null) {
          return;
      }
      if(isDirected()!=origin.isDirected()) {
          throw new Ili2cSemanticException();
      }
  }
}
