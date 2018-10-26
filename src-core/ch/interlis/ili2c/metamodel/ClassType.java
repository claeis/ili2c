package ch.interlis.ili2c.metamodel;

import java.util.Iterator;
import java.util.LinkedList;

/** a type used to describe a 'ClassType' domain
 *
 */
public class ClassType extends Type
{
  private final LinkedList<Viewable<?>> restrictedTo = new LinkedList<Viewable<?>>();
  /** true if this is a 'STRUCTURE ...' like ClassType
  */
  private boolean structure=false;
  public boolean isStructure()
  {
    return structure;
  }
  public void setStructure(boolean isStructure)
  {
	  structure=isStructure;
  }
  public void addRestrictedTo(Viewable<?> classOrAssociation)
  {
	  restrictedTo.add(classOrAssociation);
  }
  public Iterator<Viewable<?>> iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }
  void checkTypeExtension (Type wantToExtend)
  {
    if ((wantToExtend == null)
      || ((wantToExtend = wantToExtend.resolveAliases()) == null)) {
        return;
    }
    if (!(wantToExtend.getClass().equals(this.getClass()))){
        throw new Ili2cSemanticException (rsrc.getString (
        "err_type_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }


  public ClassType clone() {
      return (ClassType) super.clone();
  }

}


