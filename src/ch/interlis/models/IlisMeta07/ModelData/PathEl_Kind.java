package ch.interlis.models.IlisMeta07.ModelData;
public class PathEl_Kind{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private PathEl_Kind(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(PathEl_Kind value) {
     return value.value;
  }
  static public PathEl_Kind parseXmlCode(String value) {
     return (PathEl_Kind)valuev.get(value);
  }
  static public PathEl_Kind This=new PathEl_Kind("This");
  static public PathEl_Kind ThisArea=new PathEl_Kind("ThisArea");
  static public PathEl_Kind ThatArea=new PathEl_Kind("ThatArea");
  static public PathEl_Kind Parent=new PathEl_Kind("Parent");
  static public PathEl_Kind ReferenceAttr=new PathEl_Kind("ReferenceAttr");
  static public PathEl_Kind AssocPath=new PathEl_Kind("AssocPath");
  static public PathEl_Kind Role=new PathEl_Kind("Role");
  static public PathEl_Kind ViewBase=new PathEl_Kind("ViewBase");
  static public PathEl_Kind Attribute=new PathEl_Kind("Attribute");
  static public PathEl_Kind MetaObject=new PathEl_Kind("MetaObject");
}
