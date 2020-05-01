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

import java.util.ArrayList;


/** An Interlis Model which contains the pre-defined elements
    that are part of the language specification.


    @author Sascha Brawer, sb@adasys.ch
*/
public class PredefinedModel extends DataModel
{
  private static PredefinedModel instance=null;
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

  public final Domain BOOLEAN=new Domain();
  public final Domain HALIGNMENT=new Domain();
  public final Domain VALIGNMENT=new Domain();

  public final Domain ANYOID = new Domain ("ANYOID", new AnyOIDType(),
    /* extending */ null, /* abstract */ true, /* final */ false);

  public final Domain I32OID = new Domain ("I32OID",
  	new NumericOIDType(
		new NumericType(
			new PrecisionDecimal("0")
			,new PrecisionDecimal("2147483647")
		)),
    /* extending */ null, /* abstract */ false, /* final */ false);

  public final Domain STANDARDOID = new Domain ("STANDARDOID", new TextOIDType(
  	new TextType(16)),
    /* extending */ null, /* abstract */ false, /* final */ false);

  public final Domain UUIDOID = new Domain ("UUIDOID", new TextOIDType(
	  new TextType(36)),
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
  public final Function objectCount = new Function();
  public final Function len = new Function();
  public final Function lenM = new Function();
  public final Function trim = new Function();
  public final Function trimM = new Function();
  public final Function isEnumSubVal = new Function();
  public final Function inEnumRange = new Function();
  public final Function convertUnit = new Function();
  public final Function areAreas = new Function();

  public final Table ANYCLASS = new Table ();
  public final Table ANYSTRUCTURE = new Table ();

  public final Table METAOBJECT = new Table ();
  public final Table METAOBJECT_TRANSLATION = new Table ();
  public final Table AXIS = new Table ();
  public final Table REFSYSTEM = new Table ();
  public final Table COORDSYSTEM = new Table ();
  public final Table SCALSYSTEM = new Table ();
  public final Table SIGN = new Table ();

  public final TopicTIMESYSTEMS TIMESYSTEMS = new TopicTIMESYSTEMS();
  public final Unit Minute = createModelInterlisNumUnit (
	"min", "Minute",
	SECOND, new PrecisionDecimal("60"));
	public final Unit Hour = createModelInterlisNumUnit (
	  "h", "Hour",
	  Minute, new PrecisionDecimal("60"));
	public final Unit Day = createModelInterlisNumUnit (
	  "d", "Day",
	  Hour, new PrecisionDecimal("24"));
	public final Unit Month = createModelInterlisBaseUnit (
	  "M", "Month",
	  TIME, /* abstract */ false);
	public final Unit Year = createModelInterlisBaseUnit (
	  "Y", "Year",
	  TIME, /* abstract */ false);

	public final MetaDataUseDef BaseTimeSystems = new MetaDataUseDef();
	public final Table TimeOfDay = new Table ();
	public final Table UTC = new Table ();
	public final Domain GregorianYear=new Domain("GregorianYear"
		,new NumericType(new PrecisionDecimal("1582"),new PrecisionDecimal("2999"))
		,/* extending */ null, /* abstract */ false, /* final */ false);
	public final Table GregorianDate = new Table ();
	public final Table GregorianDateTime = new Table ();
	public final Domain XmlTime=new Domain("XMLTime"
		,new FormattedType()
		,/* extending */ null, /* abstract */ false, /* final */ false);
	public final Domain XmlDate=new Domain("XMLDate"
		,new FormattedType()
		,/* extending */ null, /* abstract */ false, /* final */ false);
	public final Domain XmlDateTime=new Domain("XMLDateTime"
		,new FormattedType()
		,XmlDate, /* abstract */ false, /* final */ false);

  public final Table LINE_SEGMENT = new Table ();
  public final Table START_SEGMENT = new Table ();
  public final Table STRAIGHT_SEGMENT = new Table ();
  public final Table ARC_SEGMENT = new Table ();
  public final Table SURFACE_EDGE = new Table ();
  public final Table SURFACE_BOUNDARY = new Table ();
  public final Table LINE_GEOMETRY = new Table ();


  private PredefinedModel ()
  {
    elements = new ElementDelegate() {
    };
    name = "INTERLIS";
    setLanguage("en");
	setContracted(true);
	setIssuer("http://www.interlis.ch");
	setModelVersion("20060126");
  }
  public static PredefinedModel getInstance() {
      if(instance==null) {
          instance=new PredefinedModel();
          instance.setupModel();
      }
      return instance;
  }
  private void setupModel()
  {
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

  ArrayList<Enumeration.Element> ev=new ArrayList<Enumeration.Element>(2);
  ev.add(new Enumeration.Element ("false"));
  ev.add(new Enumeration.Element ("true"));
  try {
	  BOOLEAN.setName("BOOLEAN");
	  BOOLEAN.setAbstract(false);
	  BOOLEAN.setFinal(true);
	  BOOLEAN.setType(
	    new EnumerationType (
	      new Enumeration (ev),
	      /* ordered */ true,
	      /* circular */ false));

    add (BOOLEAN);

  ev=new ArrayList<Enumeration.Element>(3);
  ev.add(new Enumeration.Element ("Left"));
  ev.add(new Enumeration.Element ("Center"));
  ev.add(new Enumeration.Element ("Right"));
  HALIGNMENT.setName("HALIGNMENT");
  HALIGNMENT.setAbstract(false);
  HALIGNMENT.setFinal(true);
  HALIGNMENT.setType(
    new EnumerationType (
      new Enumeration (ev),
      /* ordered */ true,
      /* circular */ false));
    add (HALIGNMENT);

	  ev=new ArrayList<Enumeration.Element>(5);
	  ev.add(new Enumeration.Element ("Top"));
	  ev.add(new Enumeration.Element ("Cap"));
	  ev.add(new Enumeration.Element ("Half"));
	  ev.add(new Enumeration.Element ("Base"));
	  ev.add(new Enumeration.Element ("Bottom"));
	  VALIGNMENT.setName("VALIGNMENT");
	  VALIGNMENT.setAbstract(false);
	  VALIGNMENT.setFinal(true);
	  VALIGNMENT.setType(
	    new EnumerationType (
	      new Enumeration (ev),
	      /* ordered */ true,
	      /* circular */ false));
    add (VALIGNMENT);

  add(ANYOID);
  add(I32OID);
  add(STANDARDOID);
  add(UUIDOID);

  add(LINE_COORD);

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
      tt.setAliasing (BOOLEAN);
      isSubClass.setDomain(tt);
      add(isSubClass);

/*FUNCTION isOfClass (Object: OBJECT OF ANYSTRUCTURE; Class: STRUCTURE): BOOLEAN;
Liefert true, wenn das Objekt des ersten Argumentes zur Klasse
oder zu einer Unterklasse des zweiten Argumentes gehoert.
*/
      isOfClass.setName("isOfClass");
      clt=new ClassType();
      clt.setStructure(true);
      isOfClass.setArguments(new FormalArgument[]{
        new FormalArgument("Object",new ObjectType(ANYSTRUCTURE))
        ,new FormalArgument("Class",clt)
        });
      tt = new TypeAlias ();
      tt.setAliasing (BOOLEAN);
      isOfClass.setDomain(tt);
      add(isOfClass);

/* FUNCTION elementCount (bag: BAG OF ANYSTRUCTURE): NUMERIC;
Liefert die Anzahl Elemente, die der Bag (oder die Liste) enthaelt.
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

/* FUNCTION objectCount (Objects: OBJECTS OF ANYCLASS): NUMERIC;
Liefert die Anzahl Objekte, welche die gegebene Objektmentmenge enthaelt
*/
      objectCount.setName("objectCount");
      objectCount.setArguments(new FormalArgument[]{
        new FormalArgument("Objects",new ObjectType (ANYCLASS,true))
        });
      objectCount.setDomain(new NumericType());
      add(objectCount);

      /* FUNCTION len (TextVal: TEXT): NUMERIC;
         FUNCTION lenM (TextVal: MTEXT): NUMERIC;
         Liefert die Laenge des Textes als Anzahl Zeichen.
      */
      len.setName("len");
      len.setArguments(new FormalArgument[]{
        new FormalArgument("TextVal",new TextType(true))
        });
      len.setDomain(new NumericType());
      add(len);

      lenM.setName("lenM");
      lenM.setArguments(new FormalArgument[]{
        new FormalArgument("TextVal",new TextType(false))
        });
      lenM.setDomain(new NumericType());
      add(lenM);

      /* FUNCTION trim (TextVal: TEXT): TEXT;
         FUNCTION trimM (TextVal: MTEXT): MTEXT;
         Liefert den um Leerzeichen am Anfang und Ende befreiten Text.
      */
      trim.setName("trim");
      trim.setArguments(new FormalArgument[]{
        new FormalArgument("TextVal",new TextType(true))
        });
      trim.setDomain(new TextType(true));
      add(trim);

      trimM.setName("trimM");
      trimM.setArguments(new FormalArgument[]{
        new FormalArgument("TextVal",new TextType(false))
        });
      trimM.setDomain(new TextType(false));
      add(trimM);

      /* FUNCTION isEnumSubVal (SubVal: ENUMTREEVAL; NodeVal: ENUMTREEVAL): BOOLEAN;
         Liefert true, wenn SubVal ein Unterelement, also ein Unterknoten oder ein Blatt, des Knotens NodeVal ist.
      */
      isEnumSubVal.setName("isEnumSubVal");
      isEnumSubVal.setArguments(new FormalArgument[]{
        new FormalArgument("SubVal",new EnumValType(false)),
        new FormalArgument("NodeVal",new EnumValType(false))
        });
      tt = new TypeAlias ();
      tt.setAliasing (BOOLEAN);
      isEnumSubVal.setDomain(tt);
      add(isEnumSubVal);


      /* FUNCTION inEnumRange (Enum: ENUMVAL; MinVal: ENUMTREEVAL; MaxVal: ENUMTREEVAL): BOOLEAN;
         Liefert true, wenn die Aufzaehlung zu der Enum gehoert, geordnet ist und im Bereich von MinVal und Max-
         Val liegt. Unterelemente von MinVal oder MaxVal gelten als dazu gehoerig.
      */
      inEnumRange.setName("inEnumRange");
      inEnumRange.setArguments(new FormalArgument[]{
        new FormalArgument("Enum",new EnumValType(true)),
        new FormalArgument("MinVal",new EnumValType(false)),
        new FormalArgument("MaxVal",new EnumValType(false))
        });
      tt = new TypeAlias ();
      tt.setAliasing (BOOLEAN);
      inEnumRange.setDomain(tt);
      add(inEnumRange);

/* FUNCTION convertUnit (from: NUMERIC): NUMERIC;
Rechnet den numerischen Wert des Parameters "from" in den numerischen
Rueckgabewert um und beruecksichtigt dabei die Einheiten, die mit dem
Parameter und mit der Verwendung des Resultatwertes (typischerweise mit
dem Attribut, dem das Resultat zugewiesen wird) verbunden sind. Diese
Funktion darf nur angewendet werden, wenn die Argumente von "from" und
vom Rueckgabeparameter vertraeglich sind, d.h. wenn ihre Einheiten von
einer gemeinsamen Einheit abgeleitet werden.
*/
      convertUnit.setName("convertUnit");
      convertUnit.setArguments(new FormalArgument[]{
        new FormalArgument("from",new NumericType())
        });
      convertUnit.setDomain(new NumericType());
      add(convertUnit);
      /* FUNCTION areAreas (Objects: OBJECTS OF ANYCLASS; SurfaceBag: ATTRIBUTE OF @ Objects RESTRICTION (BAG OF ANYSTRUCTURE); SurfaceAttr: ATTRIBUTE OF @ SurfaceBag RESTRICTION (SURFACE)): BOOLEAN;
         Prueft, ob die Flaechen gemaess Objektmenge (erster Parameter) und Attribut (dritter Parameter) eine Gebietseinteilung
         bilden. Sind die Flaechen direkt Teil der Objektklasse, soll fuer SurfaceBag UNDEFINED,
         sonst der Pfad zum Strukturattribut mit der Struktur, welche das Flaechenattribut enthaelt, angegeben werden.
      */
      areAreas.setName("areAreas");
      FormalArgument arg1=new FormalArgument("Objects",new ObjectType (ANYCLASS,true));
  	  AttributePathType arg2type=new AttributePathType();
      {
	      ct = new CompositionType ();
	      ct.setComponentType (ANYSTRUCTURE);
	      ct.setOrdered (false);
		Type[] typev={ct};
    	arg2type.setArgRestriction(arg1);
    	arg2type.setTypeRestriction(typev);
      }
	  AttributePathType arg3type=new AttributePathType();
      {
		Type[] typev={new SurfaceType()};
    	arg3type.setArgRestriction(arg1);
    	arg3type.setTypeRestriction(typev);
      }
      areAreas.setArguments(new FormalArgument[]{
        arg1,
        new FormalArgument("SurfaceBag",arg2type),
        new FormalArgument("SurfaceAttr",arg3type)
        });
      tt = new TypeAlias ();
      tt.setAliasing (BOOLEAN);
      areAreas.setDomain(tt);
      add(areAreas);

      ANYCLASS.setName ("ANYCLASS");
      add (ANYCLASS);

      ANYSTRUCTURE.setName ("ANYSTRUCTURE");
      ANYSTRUCTURE.setIdentifiable (false);
      add (ANYSTRUCTURE);


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

  {
  	TIMESYSTEMS.setName("TIMESYSTEMS");
	add(TIMESYSTEMS);
  	/*
	CLASS CALENDAR EXTENDS INTERLIS.SCALSYSTEM =
		PARAMETER
			Unit(EXTENDED): NUMERIC [TIME];
	END CALENDAR;
	*/
	TIMESYSTEMS.CALENDAR=new Table();
	TIMESYSTEMS.CALENDAR.setName("CALENDAR");
	TIMESYSTEMS.CALENDAR.setExtending(SCALSYSTEM);
	Parameter overriding =  (Parameter) TIMESYSTEMS.CALENDAR.getRealElement (
	  Parameter.class, "Unit");
	Parameter calendar_Unit = new Parameter();
	calendar_Unit.setName ("Unit");
	calendar_Unit.setType (typ = new NumericType ());
	((NumericType) typ).setUnit (TIME);
	calendar_Unit.setExtending(overriding);
	TIMESYSTEMS.CALENDAR.add (calendar_Unit);
	TIMESYSTEMS.add(TIMESYSTEMS.CALENDAR);
	/*
	CLASS TIMEOFDAYSYS EXTENDS INTERLIS.SCALSYSTEM =
		PARAMETER
			Unit(EXTENDED): NUMERIC [TIME];
	END TIMEOFDAYSYS;
	*/
	TIMESYSTEMS.TIMEOFDAYSYS=new Table();
	TIMESYSTEMS.TIMEOFDAYSYS.setName("TIMEOFDAYSYS");
	TIMESYSTEMS.TIMEOFDAYSYS.setExtending(SCALSYSTEM);
	overriding =  (Parameter) TIMESYSTEMS.TIMEOFDAYSYS.getRealElement (
	  Parameter.class, "Unit");
	Parameter timeofdaysys_Unit = new Parameter();
	timeofdaysys_Unit.setName ("Unit");
	timeofdaysys_Unit.setType (typ = new NumericType ());
	((NumericType) typ).setUnit (TIME);
	timeofdaysys_Unit.setExtending(overriding);
	TIMESYSTEMS.TIMEOFDAYSYS.add (timeofdaysys_Unit);
	TIMESYSTEMS.add(TIMESYSTEMS.TIMEOFDAYSYS);
  }

  add(Minute);
  add(Hour);
  add(Day);
  add(Month);
  add(Year);

  /*
  REFSYSTEM BASKET BaseTimeSystems ~ INTERLIS.TIMESYSTEMS
  OBJECTS OF CALENDAR: GregorianCalendar
  OBJECTS OF TIMEOFDAYSYS: UTC;
  */
  BaseTimeSystems.setName("BaseTimeSystems");
  BaseTimeSystems.setTopic(TIMESYSTEMS);
  add(BaseTimeSystems); // TODO metaobjs
	MetaObject mo=new MetaObject("GregorianCalendar",TIMESYSTEMS.CALENDAR);
	BaseTimeSystems.add(mo);
	mo=new MetaObject("UTC",TIMESYSTEMS.TIMEOFDAYSYS);
	BaseTimeSystems.add(mo);


  /*
  STRUCTURE TimeOfDay (ABSTRACT) =
  Hours: 0..23 CIRCULAR [h];
  CONTINUOUS SUBDIVISION Minutes: 0..59 CIRCULAR [min];
  CONTINUOUS SUBDIVISION Seconds: 0.000 .. 59.999 CIRCULAR [s];
  END TimeOfDay;
  */
  TimeOfDay.setName("TimeOfDay");
  TimeOfDay.setAbstract(true);
  TimeOfDay.setIdentifiable(false);
  {
	typ=new NumericType(new PrecisionDecimal("0"),new PrecisionDecimal("23"));
	((NumericType) typ).setUnit (Hour);
	((NumericType) typ).setCircular(true);
	LocalAttribute timeofday_hours = new LocalAttribute ();
	timeofday_hours.setName ("Hours");
	timeofday_hours.setDomain (typ);
	TimeOfDay.add (timeofday_hours);

	typ=new NumericType(new PrecisionDecimal("0"),new PrecisionDecimal("59"));
	((NumericType) typ).setUnit (Minute);
	((NumericType) typ).setCircular(true);
	LocalAttribute timeofday_minutes = new LocalAttribute ();
	timeofday_minutes.setName ("Minutes");
	timeofday_minutes.setDomain (typ);
	timeofday_minutes.setSubdivision(true);
	timeofday_minutes.setContinuous(true);
	TimeOfDay.add (timeofday_minutes);

	typ=new NumericType(new PrecisionDecimal("0.000"),new PrecisionDecimal("59.999"));
	((NumericType) typ).setUnit (SECOND);
	((NumericType) typ).setCircular(true);
	LocalAttribute timeofday_seconds = new LocalAttribute ();
	timeofday_seconds.setName ("Seconds");
	timeofday_seconds.setDomain (typ);
	timeofday_seconds.setSubdivision(true);
	timeofday_seconds.setContinuous(true);
	TimeOfDay.add (timeofday_seconds);
  }
  add(TimeOfDay);

  /*
  STRUCTURE UTC EXTENDS TimeOfDay =
  Hours(EXTENDED): {UTC};
  END UTC;
  */
  UTC.setName("UTC");
  UTC.setIdentifiable(false);
  UTC.setExtending(TimeOfDay);
  add(UTC); // TODO ref to MetaObj

  /*
  DOMAIN
  GregorianYear = 1582 .. 2999 [Y] {GregorianCalendar};
  */
  add(GregorianYear); // TODO ref to MetaObj

  /*
  STRUCTURE GregorianDate =
  Year: GregorianYear;
  SUBDIVISION Month: 1..12 [M];
  SUBDIVISION Day: 1..31 [d];
  END GregorianDate;
  */
  GregorianDate.setName("GregorianDate");
  GregorianDate.setIdentifiable(false);
  {
	typ=new TypeAlias();
	((TypeAlias) typ).setAliasing(GregorianYear);
	LocalAttribute gregoriandate_year = new LocalAttribute ();
	gregoriandate_year.setName ("Year");
	gregoriandate_year.setDomain (typ);
	GregorianDate.add (gregoriandate_year);

	typ=new NumericType(new PrecisionDecimal("1"),new PrecisionDecimal("12"));
	((NumericType) typ).setUnit (Month);
	LocalAttribute gregoriandate_month = new LocalAttribute ();
	gregoriandate_month.setName ("Month");
	gregoriandate_month.setDomain (typ);
	gregoriandate_month.setSubdivision(true);
	GregorianDate.add (gregoriandate_month);

	typ=new NumericType(new PrecisionDecimal("1"),new PrecisionDecimal("31"));
	((NumericType) typ).setUnit (Day);
	LocalAttribute gregoriandate_day = new LocalAttribute ();
	gregoriandate_day.setName ("Day");
	gregoriandate_day.setDomain (typ);
	gregoriandate_day.setSubdivision(true);
	GregorianDate.add (gregoriandate_day);
  }
  add(GregorianDate);

  /*
  STRUCTURE GregorianDateTime EXTENDS GregorianDate =
	 SUBDIVISION Hours: 0..23 CIRCULAR [h] {UTC};
	 CONTINUOUS SUBDIVISION Minutes: 0..59 CIRCULAR [min];
	 CONTINUOUS SUBDIVISION Seconds: 0.000 .. 59.999 CIRCULAR [INTERLIS.s];
  END GreogorianDateTime;
  */
  GregorianDateTime.setName("GregorianDateTime");
  GregorianDateTime.setIdentifiable(false);
  GregorianDateTime.setExtending(GregorianDate);
  {
	typ=new NumericType(new PrecisionDecimal("0"),new PrecisionDecimal("23"));
	((NumericType) typ).setUnit (Hour);
	((NumericType) typ).setCircular(true);
	LocalAttribute gregoriandatetime_hours = new LocalAttribute ();
	gregoriandatetime_hours.setName ("Hours");
	gregoriandatetime_hours.setDomain (typ);
	gregoriandatetime_hours.setSubdivision(true);
	GregorianDateTime.add (gregoriandatetime_hours);

	typ=new NumericType(new PrecisionDecimal("0"),new PrecisionDecimal("59"));
	((NumericType) typ).setUnit (Minute);
	((NumericType) typ).setCircular(true);
	LocalAttribute gregoriandatetime_minutes = new LocalAttribute ();
	gregoriandatetime_minutes.setName ("Minutes");
	gregoriandatetime_minutes.setDomain (typ);
	gregoriandatetime_minutes.setSubdivision(true);
	gregoriandatetime_minutes.setContinuous(true);
	GregorianDateTime.add (gregoriandatetime_minutes);

	typ=new NumericType(new PrecisionDecimal("0.000"),new PrecisionDecimal("59.999"));
	((NumericType) typ).setUnit (SECOND);
	((NumericType) typ).setCircular(true);
	LocalAttribute gregoriandatetime_seconds = new LocalAttribute ();
	gregoriandatetime_seconds.setName ("Seconds");
	gregoriandatetime_seconds.setDomain (typ);
	gregoriandatetime_seconds.setSubdivision(true);
	gregoriandatetime_seconds.setContinuous(true);
	GregorianDateTime.add (gregoriandatetime_seconds);
  }
  add(GregorianDateTime); // TODO set metaobjref UTC

  /*
  DOMAIN XMLTime = FORMAT BASED ON UTC ( Hours/2 ":" Minutes ":" Seconds );
  */
  {
	  FormattedType format=(FormattedType)XmlTime.getType();
	  format.setBaseStruct(UTC);
	  FormattedTypeBaseAttrRef baseAttr=null;
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)UTC.getElement(LocalAttribute.class, "Hours")
	  	,2);
	  baseAttr.setPostfix(":");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)UTC.getElement(LocalAttribute.class, "Minutes")
	  	,0);
	  baseAttr.setPostfix(":");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)UTC.getElement(LocalAttribute.class, "Seconds")
	  	,0);
	  format.addBaseAttrRef(baseAttr);
  }
  add(XmlTime);

  /*
  DOMAIN XMLDate = FORMAT BASED ON GregorianDate ( Year "-" Month "-" Day );
  */
  {
	  FormattedType format=(FormattedType)XmlDate.getType();
	  format.setBaseStruct(GregorianDate);
	  FormattedTypeBaseAttrRef baseAttr=null;
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDate.getElement(LocalAttribute.class, "Year")
	  	,0);
	  baseAttr.setPostfix("-");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDate.getElement(LocalAttribute.class, "Month")
	  	,0);
	  baseAttr.setPostfix("-");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDate.getElement(LocalAttribute.class, "Day")
	  	,0);
	  format.addBaseAttrRef(baseAttr);
  }
  add(XmlDate);

  /*
  DOMAIN XMLDateTime EXTENDS XMLDate = FORMAT BASED ON GregorianDateTime
	( INHERITANCE "T" Hours/2 ":" Minutes ":" Seconds );

  */
  {
	  FormattedType format=(FormattedType)XmlDateTime.getType();
	  format.setBaseStruct(GregorianDateTime);
	  format.setPrefix("T");
	  FormattedTypeBaseAttrRef baseAttr=null;
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDateTime.getElement(LocalAttribute.class, "Hours")
	  	,2);
	  baseAttr.setPostfix(":");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDateTime.getElement(LocalAttribute.class, "Minutes")
	  	,0);
	  baseAttr.setPostfix(":");
	  format.addBaseAttrRef(baseAttr);
	  baseAttr=new FormattedTypeBaseAttrRef(format
	  	,(LocalAttribute)GregorianDateTime.getElement(LocalAttribute.class, "Seconds")
	  	,0);
	  format.addBaseAttrRef(baseAttr);
  }
  add(XmlDateTime); // TODO define FormatType specifics



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
	START_SEGMENT.setName("StartSegment");
	START_SEGMENT.setExtending(LINE_SEGMENT);
	START_SEGMENT.setFinal(true);
	START_SEGMENT.setIdentifiable(false);
	add(START_SEGMENT);
  /*
	STRUCTURE StraightSegment EXTENDS LineSegment (FINAL) =
	END StraightSegment;
  */
	STRAIGHT_SEGMENT.setName("StraightSegment");
	STRAIGHT_SEGMENT.setExtending(LINE_SEGMENT);
	STRAIGHT_SEGMENT.setFinal(true);
	STRAIGHT_SEGMENT.setIdentifiable(false);
	add(STRAIGHT_SEGMENT);
  /*
	STRUCTURE ArcSegment EXTENDS LineSegment (FINAL) =
	  ArcPoint: MANDATORY LineCoord;
	  Radius: Length;
	END ArcSegment;
  */
	ARC_SEGMENT.setName("ArcSegment");
	ARC_SEGMENT.setExtending(LINE_SEGMENT);
	ARC_SEGMENT.setFinal(true);
	ARC_SEGMENT.setIdentifiable(false);
	add(ARC_SEGMENT);
  /*
	STRUCTURE SurfaceEdge =
	  Geometry: DIRECTED POLYLINE;
	  LineAttrs: ANYSTRUCTURE;
	END SurfaceEdge;
  */
	SURFACE_EDGE.setName ("SurfaceEdge");
	SURFACE_EDGE.setIdentifiable (false);
	LocalAttribute surfaceEdge_geometry = new LocalAttribute ();
	PolylineType geomType = new PolylineType();
	geomType.setDirected (true);
	surfaceEdge_geometry.setName ("Geometry");
	surfaceEdge_geometry.setDomain (geomType,true);
	SURFACE_EDGE.add (surfaceEdge_geometry);
	LocalAttribute surfaceEdge_lineAttrs = new LocalAttribute ();
	typ = new CompositionType ();
	((CompositionType) typ).setComponentType (ANYSTRUCTURE);
	((CompositionType) typ).setCardinality (new Cardinality (0, 1));
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
	lineGeometry_segments.setDomain (ct,true);
	LINE_GEOMETRY.add(lineGeometry_segments);
	add(LINE_GEOMETRY);

    } catch (Exception ex) {
		//ex.printStackTrace ();
	  throw new RuntimeException (ex);
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



  private BaseUnit createModelInterlisBaseUnit (String name, String docName, Unit extending, boolean abstr)
  {
    try {
      BaseUnit result = new BaseUnit ();


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
  private NumericallyDerivedUnit createModelInterlisNumUnit (String name, String docName, Unit extending, PrecisionDecimal fact)
  {
	try {
		NumericallyDerivedUnit result = new NumericallyDerivedUnit ();


	  result.setName (name);
	  result.setDocName (docName);
	  result.setExtending (extending);
	  NumericallyDerivedUnit.Factor factv[]=new NumericallyDerivedUnit.Factor[1];
	  factv[0]=new NumericallyDerivedUnit.Factor ('*', fact);
	  result.setConversionFactors(factv);
	  return result;
	} catch (Exception ex) {
	  ex.printStackTrace ();
	  return null;
	}
  }
}
