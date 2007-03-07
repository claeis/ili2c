package ch.interlis.ili2c.metamodel;

import java.util.*;

/** a type used to describe a 'ClassType' domain
 *
 */
public class ClassType extends Type
{
  private LinkedList	restrictedTo=new LinkedList(); // List<Viewable>
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
  public void addRestrictedTo(Viewable classOrAssociation)
  {
	  restrictedTo.add(classOrAssociation);
  }
  public java.util.Iterator iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }
}


