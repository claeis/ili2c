/*****************************************************************************
 *
 * UniquenessConstraint.java
 * -------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


/** A constraint that is satisfied if and only if each instance of
    an attribute path has an unique value.

    @author    Sascha Brawer
*/
public class UniquenessConstraint extends Constraint
{
	private ObjectPath prefix=null;
	private UniqueEl elements=null;
        private boolean local=false;
	public UniquenessConstraint()
	{
	}
	public void setElements(UniqueEl elements)
	{
		this.elements=elements;
  	}
  	public UniqueEl getElements(){
  		return elements;
  	}
 	public void setPrefix(ObjectPath structAttr)
 	{
 		prefix=structAttr;
 	}
 	public ObjectPath getPrefix(){
 		return prefix;
 	}
   public void setLocal(boolean isLocal)
   {
    local=isLocal;
   }
   public boolean getLocal()
   {
    return local;
   }
   private Evaluable preCondition=null;
public Evaluable getPreCondition() {
	return preCondition;
}
public void setPreCondition(Evaluable preCondition) {
	this.preCondition = preCondition;
}
}
