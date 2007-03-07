package ch.interlis.ili2c.metamodel;

/** represents the METAOBJECT [OF ClassRef] branch in the syntax rule ParameterDef
 *
 */
public class MetaobjectType extends Type
{
  private Table ref=null;
  public void setReferred(Table ref)
  {
    this.ref=ref;
  }
  /** returns the ClassDef referenced by this parameter
   */
  public Table getReferred()
  {
    return ref;
  }
}
