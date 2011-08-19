package ch.interlis.models;
public class INTERLIS_{
  private INTERLIS_() {}
  public final static String MODEL= "INTERLIS";
  public final static String TIMESYSTEMS= "INTERLIS.TIMESYSTEMS";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("INTERLIS","http://www.interlis.ch","20060126"); }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    mapping.defineClass("INTERLIS.METAOBJECT", new String[]{   "Name"
      });
    mapping.defineClass("INTERLIS.REFSYSTEM", new String[]{   "Name"
      });
    mapping.defineClass("INTERLIS.SCALSYSTEM", new String[]{   "Name"
      });
    mapping.defineClass("INTERLIS.ANYCLASS", new String[]{  });
    mapping.defineClass("INTERLIS.GregorianDate", new String[]{   "Year"
      ,"Month"
      ,"Day"
      });
    mapping.defineClass("INTERLIS.METAOBJECT_TRANSLATION", new String[]{   "Name"
      ,"NameInBaseLanguage"
      });
    mapping.defineClass("INTERLIS.TIMESYSTEMS.CALENDAR", new String[]{   "Name"
      });
    mapping.defineClass("INTERLIS.LineSegment", new String[]{   "SegmentEndPoint"
      });
    mapping.defineClass("INTERLIS.GregorianDateTime", new String[]{   "Year"
      ,"Month"
      ,"Day"
      });
    mapping.defineClass("INTERLIS.LineGeometry", new String[]{   "Segments"
      });
    mapping.defineClass("INTERLIS.SIGN", new String[]{   "Name"
      });
    mapping.defineClass("INTERLIS.ANYSTRUCTURE", new String[]{  });
    mapping.defineClass("INTERLIS.TimeOfDay", new String[]{   "Hours"
      ,"Minutes"
      ,"Seconds"
      });
    mapping.defineClass("INTERLIS.UTC", new String[]{   "Hours"
      ,"Minutes"
      ,"Seconds"
      });
    mapping.defineClass("INTERLIS.AXIS", new String[]{  });
    mapping.defineClass("INTERLIS.SurfaceBoundary", new String[]{   "Lines"
      });
    mapping.defineClass("INTERLIS.SurfaceEdge", new String[]{   "Geometry"
      ,"LineAttrs"
      });
    mapping.defineClass("INTERLIS.COORDSYSTEM", new String[]{   "Name"
      ,"Axis"
      });
    mapping.defineClass("INTERLIS.TIMESYSTEMS.TIMEOFDAYSYS", new String[]{   "Name"
      });
    return mapping;
  }
}
