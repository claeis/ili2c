package ch.interlis.models.IlisMeta07.ModelData;
public class PathEl_SpecIndex{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private PathEl_SpecIndex(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(PathEl_SpecIndex value) {
     return value.value;
  }
  static public PathEl_SpecIndex parseXmlCode(String value) {
     return (PathEl_SpecIndex)valuev.get(value);
  }
  static public PathEl_SpecIndex First=new PathEl_SpecIndex("First");
  static public PathEl_SpecIndex Last=new PathEl_SpecIndex("Last");
}
