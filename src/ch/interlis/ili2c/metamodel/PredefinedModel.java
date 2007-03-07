/*****************************************************************************
 *
 * PredefinedModel.java
 * --------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;


/** An Interlis Model which contains the pre-defined elements
    that are part of the language specification.


    @author Sascha Brawer, sb@adasys.ch
*/
public class PredefinedModel extends DataModel
{
  public final LineForm STRAIGHTS = new LineForm ("STRAIGHTS");
  public final LineForm ARCS = new LineForm ("ARCS");


  public final Unit ANYUNIT = createModelInterlisBaseUnit (
    "ANYUNIT", "ANYUNIT", null, /* abstract */ true);

  public final Unit DIMENSIONLESS = createModelInterlisBaseUnit (
    "DIMENSIONLESS", "DIMENSIONLESS", ANYUNIT, /* abstract */ true);

  public final Unit LENGTH = createModelInterlisBaseUnit (
    "LENGTH", "LENGTH", ANYUNIT, /* abstract */ true);

  public final Unit MASS = createModelInterlisBaseUnit (
    "MASS", "MASS", ANYUNIT, /* abstract */ true);

  public final Unit TIME = createModelInterlisBaseUnit (
    "TIME", "TIME", ANYUNIT, /* abstract */ true);

  public final Unit ELECTRIC_CURRENT = createModelInterlisBaseUnit (
    "ELECTRIC_CURRENT", "ELECTRIC_CURRENT",
    ANYUNIT, /* abstract */ true);

  public final Unit TEMPERATURE = createModelInterlisBaseUnit (
    "TEMPERATURE", "TEMPERATURE",
    ANYUNIT, /* abstract */ true);

  public final Unit AMOUNT_OF_MATTER = createModelInterlisBaseUnit (
    "AMOUNT_OF_MATTER", "AMOUNT_OF_MATTER",
    ANYUNIT, /* abstract */ true);

  public final Unit ANGLE = createModelInterlisBaseUnit (
    "ANGLE", "ANGLE",
    ANYUNIT, /* abstract */ true);

  public final Unit SOLID_ANGLE = createModelInterlisBaseUnit (
    "SOLID_ANGLE", "SOLID_ANGLE",
    ANYUNIT, /* abstract */ true);

  public final Unit LUMINOUS_INTENSITY = createModelInterlisBaseUnit (
    "LUMINOUS_INTENSITY", "LUMINOUS_INTENSITY",
    ANYUNIT, /* abstract */ true);

  public final Unit MONEY = createModelInterlisBaseUnit (
    "MONEY", "MONEY",
    ANYUNIT, /* abstract */ true);

  public final Unit METER = createModelInterlisBaseUnit (
    "m", "METER",
    LENGTH, /* abstract */ false);

  public final Unit KILOGRAM = createModelInterlisBaseUnit (
    "kg", "KILOGRAM",
    MASS, /* abstract */ false);

  public final Unit SECOND = createModelInterlisBaseUnit (
    "s", "SECOND",
    TIME, /* abstract */ false);

  public final Unit AMPERE = createModelInterlisBaseUnit (
    "A", "AMPERE",
    ELECTRIC_CURRENT, /* abstract */ false);

  public final Unit DEGREE_KELVIN = createModelInterlisBaseUnit (
    "K", "DEGREE_KELVIN",
    TEMPERATURE, /* abstract */ false);

  public final Unit MOLE = createModelInterlisBaseUnit (
    "mol", "MOLE",
    AMOUNT_OF_MATTER, /* abstract */ false);

  public final Unit RADIAN = createModelInterlisBaseUnit (
    "rad", "RADIAN",
    ANGLE, /* abstract */ false);

  public final Unit STERADIAN = createModelInterlisBaseUnit (
    "sr", "STERADIAN",
    SOLID_ANGLE, /* abstract */ false);

  public final Unit CANDELA = createModelInterlisBaseUnit (
    "cd", "CANDELA",
    LUMINOUS_INTENSITY, /* abstract */ false);

  public final Domain URI = new Domain ("URI", new TextType (1023),
    /* extending */ null, /* abstract */ false, /* final */ true);

  public final Domain NAME = new Domain ("NAME", new TextType (255),
    /* extending */ null, /* abstract */ false, /* final */ true);

  public final Domain INTERLIS_1_DATE = new Domain (
    "INTERLIS_1_DATE", new TextType (8),
    /* extending */ null, /* abstract */ false, /* final */ true);

  public final Domain BOOLEAN;
  public final Domain HALIGNMENT;
  public final Domain VALIGNMENT;

  public final Domain ANYOID = new Domain ("ANYOID", new AnyOIDType(),
    /* extending */ null, /* abstract */ true, /* final */ false);

  public final Domain I32OID = new Domain ("I32OID",
  	new NumericOIDType(
		new NumericType(
			new PrecisionDecimal(0,0)
			,new PrecisionDecimal(2147483647,0)
		)),
    /* extending */ null, /* abstract */ false, /* final */ false);

  public final Domain STANDARDOID = new Domain ("STANDARDOID", new TextOIDType(
  	new TextType(16)),
    /* extending */ null, /* abstract */ false, /* final */ false);

  /*    LineCoord (ABSTRACT) = COORD NUMERIC, NUMERIC;
  */
  public final Domain LINE_COORD = new Domain ("LineCoord",
  	new CoordType(new NumericalType[] {new NumericType(),new NumericType() }
		),
    /* extending */ null, /* abstract */ true, /* final */ false);

  public final Function myClass = new Function();
  public final Function isSubClass = new Function();
  public final Function isOfClass = new Function();
  public final Function elementCount = new Function();
  public final Function convertUnit = new Function();

  public final Table ANYCLASS = new Table ();
  public final Table ANYSTRUCTURE = new Table ();

  public final Table LINE_SEGMENT = new Table ();
  //public final Table START_SEGMENT = new Table ();
  //public final Table STRAIGHT_SEGMENT = new Table ();
  //public final Table ARC_SEGMENT = new Table ();
  public final Table SURFACE_EDGE = new Table ();
  public final Table SURFACE_BOUNDARY = new Table ();
  public final Table LINE_GEOMETRY = new Table ();
  public final Table BASKET = new Table ();
  public final Table METAOBJECT = new Table ();
  public final Table METAOBJECT_TRANSLATION = new Table ();
  public final Table AXIS = new Table ();
  public final Table REFSYSTEM = new Table ();
  public final Table COORDSYSTEM = new Table ();
  public final Table SCALSYSTEM = new Table ();
  public final Table SIGN = new Table ();


  public PredefinedModel ()
  {
    elements = new ElementDelegate() {
    };
    name = "INTERLIS";


    add (STRAIGHTS);
    add (ARCS);


    add (ANYUNIT);
    add (DIMENSIONLESS);
    add (LENGTH);
    add (MASS);
    add (TIME);
    add (ELECTRIC_CURRENT);
    add (TEMPERATURE);
    add (AMOUNT_OF_MATTER);
    add (ANGLE);
    add (SOLID_ANGLE);
    add (LUMINOUS_INTENSITY);
    add (MONEY);


    add (METER);
    add (KILOGRAM);
    add (SECOND);
    add (AMPERE);
    add (DEGREE_KELVIN);
    add (MOLE);
    add (RADIAN);
    add (STERADIAN);
    add (CANDELA);


    add (URI);
    add (NAME);
    add (INTERLIS_1_DATE);

  List ev=new ArrayList();
  ev.add(new Enumeration.Element ("false"));
  ev.add(new Enumeration.Element ("true"));
  BOOLEAN = new Domain ("BOOLEAN",
    new EnumerationType (
      new Enumeration (ev),
      /* ordered */ true,
      /* circular */ false),
    /* extending */ null, /* abstract*/ false, /* final */ true);

    add (BOOLEAN);

  ev=new ArrayList();
  ev.add(new Enumeration.Element ("Left"));
  ev.add(new Enumeration.Element ("Center"));
  ev.add(new Enumeration.Element ("Right"));
  HALIGNMENT = new Domain ("HALIGNMENT",
    new EnumerationType (
      new Enumeration (ev),
      /* ordered */ true,
      /* circular */ false),
    /* extending */ null, /* abstract*/ false, /* final */ true);
    add (HALIGNMENT);

  ev=new ArrayList();
  ev.add(new Enumeration.Element ("Top"));
  ev.add(new Enumeration.Element ("Cap"));
  ev.add(new Enumeration.Element ("Half"));
  ev.add(new Enumeration.Element ("Base"));
  ev.add(new Enumeration.Element ("Bottom"));
  VALIGNMENT = new Domain ("VALIGNMENT",
    new EnumerationType (
      new Enumeration (ev),
      /* ordered */ true,
      /* circular */ false),
    /* extending */ null, /* abstract*/ false, /* final */ true);
    add (VALIGNMENT);

  add(ANYOID);
  add(I32OID);
  add(STANDARDOID);

  add(LINE_COORD);

    try {
      Type typ;


/* FUNCTION myClass (Object: OBJECT OF ANYSTRUCTURE): STRUCTURE;
Liefert die Klasse des Objektes.
*/
      myClass.setName("myClass");
      myClass.setArguments(new FormalArgument[]{new FormalArgument("Object",new ObjectType(ANYSTRUCTURE))});
      ClassType clt=new ClassType();
      clt.setStructure(true);
      myClass.setDomain(clt);
      add(myClass);

/* FUNCTION isSubClass (potSubClass: STRUCTURE; potSuperClass: STRUCTURE): BOOLEAN;
Liefert true, wenn die Klasse des ersten Argumentes der Klasse oder
einer Unterklasse des zweiten Ar-gumentes entspricht.
*/
      isSubClass.setName("isSubClass");
      clt=new ClassType();
      clt.setStructure(true);
      ClassType clt2=new ClassType();
      clt2.setStructure(true);
      isSubClass.setArguments(new FormalArgument[]{
        new FormalArgument("potSubClass",clt)
        ,new FormalArgument("potSuperClass",clt2)
        });
      TypeAlias tt = new TypeAlias ();
      ((TypeAlias) tt).setAliasing (BOOLEAN);
      isSubClass.setDomain(tt);
      add(isSubClass);

/*FUNCTION isOfClass (Object: OBJECT OF ANYSTRUCTURE; Class: STRUCTURE): BOOLEAN;
Liefert true, wenn das Objekt des ersten Argumentes zur Klasse
oder zu einer Unterklasse des zweiten Argumentes gehört.
*/
      isOfClass.setName("isOfClass");
      clt=new ClassType();
      clt.setStructure(true);
      isOfClass.setArguments(new FormalArgument[]{
        new FormalArgument("Object",new ObjectType(ANYSTRUCTURE))
        ,new FormalArgument("Class",clt)
        });
      tt = new TypeAlias ();
      ((TypeAlias) tt).setAliasing (BOOLEAN);
      isOfClass.setDomain(tt);
      add(isOfClass);

/* FUNCTION elementCount (bag: BAG OF ANYSTRUCTURE): NUMERIC;
Liefert die Anzahl Elemente, die der Bag (oder die Liste) enthält.
*/
      elementCount.setName("elementCount");
      CompositionType ct = new CompositionType ();
      ct.setComponentType (ANYSTRUCTURE);
      ct.setOrdered (false);
      elementCount.setArguments(new FormalArgument[]{
        new FormalArgument("bag",ct)
        });
      elementCount.setDomain(new NumericType());
      add(elementCount);

/* FUNCTION convertUnit (from: NUMERIC): NUMERIC;
Rechnet den numerischen Wert des Parameters "from" in den numerischen
Rückgabewert um und be-rücksichtigt dabei die Einheiten, die mit dem
Parameter und mit der Verwendung des Resultatwertes (typischerweise mit
dem Attribut, dem das Resultat zugewiesen wird) verbunden sind. Diese
Funktion darf nur angewendet werden, wenn die Argumente von "from" und
vom Rückgabeparameter verträglich sind, d.h. wenn ihre Einheiten von
einer gemeinsamen Einheit abgeleitet werden.
*/
      convertUnit.setName("convertUnit");
      convertUnit.setArguments(new FormalArgument[]{
        new FormalArgument("from",new NumericType())
        });
      convertUnit.setDomain(new NumericType());
      add(convertUnit);


      ANYCLASS.setName ("ANYCLASS");
      ANYCLASS.setAbstract (true);
      add (ANYCLASS);

      ANYSTRUCTURE.setName ("ANYSTRUCTURE");
      ANYSTRUCTURE.setAbstract (true);
      ANYSTRUCTURE.setIdentifiable (false);
      add (ANYSTRUCTURE);

        /*
	  STRUCTURE LineSegment (ABSTRACT) =
	    SegmentEndPoint: MANDATORY LineCoord;
	  END LineSegment;
  	*/
      LINE_SEGMENT.setName ("LineSegment");
      LINE_SEGMENT.setIdentifiable (false);
      LINE_SEGMENT.setAbstract (true);
      LocalAttribute linesegment_ep = new LocalAttribute ();
      linesegment_ep.setName ("SegmentEndPoint");
      linesegment_ep.setAbstract(true);
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (LINE_COORD);
      typ.setMandatory (true);
      linesegment_ep.setDomain (typ);
      LINE_SEGMENT.add (linesegment_ep);
      add (LINE_SEGMENT);

	/*
	  STRUCTURE StartSegment EXTENDS LineSegment (FINAL) =
	  END StartSegment;
	*/
	/*
	  STRUCTURE StraightSegment EXTENDS LineSegment (FINAL) =
	  END StraightSegment;
	*/
	/*
	  STRUCTURE ArcSegment EXTENDS LineSegment (FINAL) =
	    ArcPoint: MANDATORY LineCoord;
	    Radius: Length;
	  END ArcSegment;
	*/

	/*
	  STRUCTURE SurfaceEdge =
	    Geometry: DIRECTED POLYLINE;
	    LineAttrs: STRUCTURE;
	  END SurfaceEdge;
	*/
      SURFACE_EDGE.setName ("SurfaceEdge");
      SURFACE_EDGE.setAbstract(true);
      SURFACE_EDGE.setIdentifiable (false);
      LocalAttribute surfaceEdge_geometry = new LocalAttribute ();
      surfaceEdge_geometry.setAbstract(true);
      PolylineType geomType = new PolylineType ();
      geomType.setDirected (true);
      surfaceEdge_geometry.setName ("Geometry");
      surfaceEdge_geometry.setDomain (geomType);
      SURFACE_EDGE.add (surfaceEdge_geometry);
      LocalAttribute surfaceEdge_lineAttrs = new LocalAttribute ();
      typ = new ClassType ();
      ((ClassType)typ).setStructure(true);
      surfaceEdge_lineAttrs.setName ("LineAttrs");
      surfaceEdge_lineAttrs.setDomain (typ);
      SURFACE_EDGE.add (surfaceEdge_lineAttrs);
      add (SURFACE_EDGE);

	/*
	  STRUCTURE SurfaceBoundary =
	    Lines: LIST OF SurfaceEdge;
	  END SurfaceBoundary;
	*/
      SURFACE_BOUNDARY.setName ("SurfaceBoundary");
      SURFACE_BOUNDARY.setIdentifiable (false);
      ct = new CompositionType ();
      ct.setComponentType (SURFACE_EDGE);
      ct.setOrdered (true);
      LocalAttribute surfaceBoundary_Lines;
      surfaceBoundary_Lines = new LocalAttribute ();
      surfaceBoundary_Lines.setName ("Lines");
      surfaceBoundary_Lines.setDomain (ct);
      SURFACE_BOUNDARY.add (surfaceBoundary_Lines);
      add (SURFACE_BOUNDARY);

	/*
	  STRUCTURE LineGeometry =
	    Segments: LIST OF LineSegment;
	  MANDATORY CONSTRAINT isOfClass (Segments[FIRST],INTERLIS.StartSegment)
	  END LineGeometry;
	*/
      LINE_GEOMETRY.setName ("LineGeometry");
      LINE_GEOMETRY.setIdentifiable (false);
      ct = new CompositionType ();
      ct.setComponentType (LINE_SEGMENT);
      ct.setOrdered (true);
      LocalAttribute lineGeometry_segments;
      lineGeometry_segments = new LocalAttribute ();
      lineGeometry_segments.setName ("Segments");
      lineGeometry_segments.setDomain (ct);
      LINE_GEOMETRY.add(lineGeometry_segments);
      add(LINE_GEOMETRY);

	/*
	  STRUCTURE Basket (ABSTRACT) =
	    Model: MANDATORY NAME;
	    Topic: MANDATORY NAME;
	    Kind: MANDATORY (Data, View, Base, Graphic);
	    Ident (ABSTRACT): MANDATORY ANYOID;
	  END Basket;
	*/
      BASKET.setName ("BASKET");
      BASKET.setAbstract (true);
      LocalAttribute basket_model = new LocalAttribute ();
      basket_model.setName ("Model");
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (NAME);
      typ.setMandatory (true);
      basket_model.setDomain (typ);
      BASKET.add (basket_model);
      LocalAttribute basket_topic = new LocalAttribute ();
      basket_topic.setName ("Topic");
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (NAME);
      typ.setMandatory (true);
      basket_topic.setDomain (typ);
      BASKET.add (basket_topic);
      LocalAttribute basket_kind = new LocalAttribute ();
      basket_kind.setName ("Kind");
      ev=new ArrayList();
      ev.add(new Enumeration.Element ("Data"));
      ev.add(new Enumeration.Element ("View"));
      ev.add(new Enumeration.Element ("Base"));
      ev.add(new Enumeration.Element ("Graphic"));
      typ = new EnumerationType (
      new Enumeration(ev),
      /* ordered */ false,
      /* circular */ false);
      typ.setMandatory (true);
      basket_kind.setDomain (typ);
      BASKET.add (basket_kind);
      LocalAttribute basket_ident = new LocalAttribute ();
      basket_ident.setName ("Ident");
      basket_ident.setAbstract(true);
      typ = new AnyOIDType();
      typ.setMandatory (true);
      basket_ident.setDomain (typ);
      BASKET.add (basket_ident);
      add(BASKET);


      METAOBJECT.setName ("METAOBJECT");
      METAOBJECT.setAbstract (true);
      LocalAttribute metaobject_name = new LocalAttribute ();
      metaobject_name.setName ("Name");
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (NAME);
      typ.setMandatory (true);
      metaobject_name.setDomain (typ);
      METAOBJECT.add (metaobject_name);
      add (METAOBJECT);

	  /* CLASS METAOBJECT_TRANSLATION =
	       Name: MANDATORY NAME;
	       NameInBaseLanguage: MANDATORY NAME;
	       UNIQUE Name;
	       UNIQUE NameInBaseLanguage;
	     END METAOBJECT_TRANSLATION;
	  */
      METAOBJECT_TRANSLATION.setName ("METAOBJECT_TRANSLATION");
      METAOBJECT_TRANSLATION.setAbstract (true);
      LocalAttribute metaobject_tanslation_name = new LocalAttribute ();
      metaobject_tanslation_name.setName ("Name");
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (NAME);
      typ.setMandatory (true);
      metaobject_tanslation_name.setDomain (typ);
      METAOBJECT_TRANSLATION.add (metaobject_tanslation_name);
      LocalAttribute metaobject_nameInBaseLanguage = new LocalAttribute ();
      metaobject_nameInBaseLanguage.setName ("NameInBaseLanguage");
      typ = new TypeAlias();
      ((TypeAlias) typ).setAliasing (NAME);
      typ.setMandatory (true);
      metaobject_nameInBaseLanguage.setDomain (typ);
      METAOBJECT_TRANSLATION.add (metaobject_nameInBaseLanguage);
      add (METAOBJECT_TRANSLATION);

      AXIS.setName ("AXIS");
      AXIS.setIdentifiable(false);
      Parameter axis_Unit = new Parameter();
      axis_Unit.setName ("Unit");
      axis_Unit.setType (typ = new NumericType ());
      ((NumericType) typ).setUnit (ANYUNIT);
      AXIS.add(axis_Unit);
      add (AXIS);

      REFSYSTEM.setName ("REFSYSTEM");
      REFSYSTEM.setAbstract (true);
      REFSYSTEM.setExtending (METAOBJECT);
      add (REFSYSTEM);


      /* TABLE COORDSYSTEM (ABSTRACT) EXTENDS REFSYSTEM =
           Axis: LIST {1..3} OF AXIS;
         END COORDSYSTEM;
      */
      COORDSYSTEM.setName ("COORDSYSTEM");
      COORDSYSTEM.setAbstract (true);
      COORDSYSTEM.setExtending (REFSYSTEM);
      typ = new CompositionType ();
      ((CompositionType) typ).setComponentType (AXIS);
      ((CompositionType) typ).setCardinality (new Cardinality (1, 3));
      ((CompositionType) typ).setOrdered (true);
      LocalAttribute coordsystem_axis = new LocalAttribute ();
      coordsystem_axis.setName ("Axis");
      coordsystem_axis.setDomain (typ);
      COORDSYSTEM.add (coordsystem_axis);
      add (COORDSYSTEM);

      SCALSYSTEM.setName ("SCALSYSTEM");
      SCALSYSTEM.setAbstract (true);
      SCALSYSTEM.setExtending (REFSYSTEM);
      Parameter scalsystem_Unit = new Parameter();
      scalsystem_Unit.setName ("Unit");
      scalsystem_Unit.setType (typ = new NumericType ());
      ((NumericType) typ).setUnit (ANYUNIT);
      SCALSYSTEM.add (scalsystem_Unit);
      add (SCALSYSTEM);


      /* TABLE SIGN (ABSTRACT) EXTENDS METAOBJECT =
         PARAMETER
           Name: NAME;
         END SIGN;
      */
      SIGN.setName ("SIGN");
      SIGN.setAbstract (true);
      SIGN.setExtending (METAOBJECT);
      typ = new MetaobjectType();
      ((MetaobjectType) typ).setReferred(SIGN);
      Parameter sign_param = new Parameter ();
      sign_param.setName ("Sign");
      sign_param.setType (typ);
      SIGN.add (sign_param);
      add (SIGN);

    } catch (Exception ex) {
      throw new RuntimeException (ex.toString());
    }
  }



  /** @exception java.lang.UnsupportedOperationException
                 The name of the pre-defined model is always
                 "INTERLIS" and can't be changed.
  */
  public void setName (String name)
  {
    throw new UnsupportedOperationException ();
  }



  private Unit createModelInterlisBaseUnit (String name, String docName, Unit extending, boolean abstr)
  {
    try {
      Unit result = new BaseUnit ();


      result.setName (name);
      result.setDocName (docName);
      result.setAbstract (abstr);
      result.setExtending (extending);
      return result;
    } catch (Exception ex) {
      ex.printStackTrace ();
      return null;
    }
  }
}
