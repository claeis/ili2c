package ch.interlis.models.IlisMeta07.ModelData;
public class CompoundExpr_Operation{
  static private java.util.HashMap valuev=new java.util.HashMap();
  private String value=null;
  private CompoundExpr_Operation(String value) {
    this.value=value;
    valuev.put(value,this);
  }
  static public String toXmlCode(CompoundExpr_Operation value) {
     return value.value;
  }
  static public CompoundExpr_Operation parseXmlCode(String value) {
     return (CompoundExpr_Operation)valuev.get(value);
  }
  static public CompoundExpr_Operation And=new CompoundExpr_Operation("And");
  static public CompoundExpr_Operation Or=new CompoundExpr_Operation("Or");
  static public CompoundExpr_Operation Mult=new CompoundExpr_Operation("Mult");
  static public CompoundExpr_Operation Div=new CompoundExpr_Operation("Div");
  static public CompoundExpr_Operation Relation_Equal=new CompoundExpr_Operation("Relation.Equal");
  static public CompoundExpr_Operation Relation_NotEqual=new CompoundExpr_Operation("Relation.NotEqual");
  static public CompoundExpr_Operation Relation_LessOrEqual=new CompoundExpr_Operation("Relation.LessOrEqual");
  static public CompoundExpr_Operation Relation_GreaterOrEqual=new CompoundExpr_Operation("Relation.GreaterOrEqual");
  static public CompoundExpr_Operation Relation_Less=new CompoundExpr_Operation("Relation.Less");
  static public CompoundExpr_Operation Relation_Greater=new CompoundExpr_Operation("Relation.Greater");
}
