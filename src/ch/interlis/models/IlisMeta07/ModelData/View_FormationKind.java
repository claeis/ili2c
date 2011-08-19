package ch.interlis.models.IlisMeta07.ModelData;
public class View_FormationKind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private View_FormationKind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(View_FormationKind value) {
     return value.value;
  }
  static public View_FormationKind parseXmlCode(String value) {
     return (View_FormationKind)valuev.get(value);
  }
  static public View_FormationKind Projection=new View_FormationKind("Projection");
  static public View_FormationKind Join=new View_FormationKind("Join");
  static public View_FormationKind Union=new View_FormationKind("Union");
  static public View_FormationKind Aggregation_All=new View_FormationKind("Aggregation.All");
  static public View_FormationKind Aggregation_Equal=new View_FormationKind("Aggregation.Equal");
  static public View_FormationKind Inspection_Normal=new View_FormationKind("Inspection.Normal");
  static public View_FormationKind Inspection_Area=new View_FormationKind("Inspection.Area");
}
