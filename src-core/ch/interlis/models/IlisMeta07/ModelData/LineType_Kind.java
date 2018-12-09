package ch.interlis.models.IlisMeta07.ModelData;
public class LineType_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private LineType_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(LineType_Kind value) {
     return value.value;
  }
  static public LineType_Kind parseXmlCode(String value) {
     return (LineType_Kind)valuev.get(value);
  }
  static public LineType_Kind Polyline=new LineType_Kind("Polyline");
  public final static String tag_Polyline="Polyline";
  static public LineType_Kind DirectedPolyline=new LineType_Kind("DirectedPolyline");
  public final static String tag_DirectedPolyline="DirectedPolyline";
  static public LineType_Kind Surface=new LineType_Kind("Surface");
  public final static String tag_Surface="Surface";
  static public LineType_Kind Area=new LineType_Kind("Area");
  public final static String tag_Area="Area";
}
