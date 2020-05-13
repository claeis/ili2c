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
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
  }


  public MetaobjectType clone() {
      return (MetaobjectType) super.clone();
  }

}
