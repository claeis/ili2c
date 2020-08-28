package ch.interlis.ili2c.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      ClassType baseElement=(ClassType)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      
      if(structure!=baseElement.structure) {
          throw new Ili2cSemanticException ();
      }
      if(restrictedTo.size()!=baseElement.restrictedTo.size()) {
          throw new Ili2cSemanticException ();
      }
      ArrayList<Viewable> depTopics=new ArrayList<Viewable>(restrictedTo);
      Collections.sort(depTopics,new TranslatedElementNameComparator());
      ArrayList<Viewable> baseDepTopics=new ArrayList<Viewable>(baseElement.restrictedTo);
      Collections.sort(baseDepTopics,new TranslatedElementNameComparator());

      for(int depi=0;depi<depTopics.size();depi++) {
          Viewable dep=depTopics.get(depi);
          Viewable baseDep=baseDepTopics.get(depi);
          if(!Element.equalElementRef(dep,baseDep)) {
              throw new Ili2cSemanticException ();
          }
      }
  }
}


