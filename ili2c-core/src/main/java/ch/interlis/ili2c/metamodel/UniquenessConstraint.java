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

import java.util.List;

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
@Override
public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  throws java.lang.IllegalStateException
{
    super.checkTranslationOf(errs,name,baseName);
    UniquenessConstraint baseElement=(UniquenessConstraint)getTranslationOf();
    if(baseElement==null) {
        return;
    }
    
    Ili2cSemanticException err=Evaluable.checkTranslation(preCondition, baseElement.preCondition, getSourceLine(), "err_diff_uniqueConstraintPreConditionMismatch");
    if(err!=null) {
        errs.add(err);
    }
    err=Evaluable.checkTranslation(prefix, baseElement.prefix, getSourceLine(), "err_diff_uniqueConstraintPrefixMismatch");
    if(err!=null) {
        errs.add(err);
    }
    if(local!=baseElement.local) {
        err=new Ili2cSemanticException(getSourceLine(), Element.formatMessage("err_diff_uniqueConstraintLocalMismatch"));
        errs.add(err);
    }
    ObjectPath[] elev=elements.getAttributes();
    ObjectPath[] otherElev=baseElement.elements.getAttributes();
    if(elev.length!=otherElev.length) {
        err=new Ili2cSemanticException(getSourceLine(), Element.formatMessage("err_diff_uniqueConstraintUniqueElMismatch"));
        errs.add(err);
    }else {
        for(int ri=0;ri<elev.length;ri++) {
            err=Evaluable.checkTranslation(elev[ri], otherElev[ri], getSourceLine(), "err_diff_uniqueConstraintUniqueElMismatch");
            if(err!=null) {
                errs.add(err);
            }
        }
    }
}
}
