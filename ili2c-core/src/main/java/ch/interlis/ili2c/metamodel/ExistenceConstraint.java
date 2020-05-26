package ch.interlis.ili2c.metamodel;


import java.util.*;

public class ExistenceConstraint extends Constraint
{
	private ObjectPath restrictedAttribute;
	private LinkedList<ObjectPath> requiredIn = new LinkedList<ObjectPath>();

	/** define the attribute that should be checked by this constraint.
	*/
	public void setRestrictedAttribute(ObjectPath path)
	{
		restrictedAttribute=path;
	}
        public ObjectPath getRestrictedAttribute()
        {
          return restrictedAttribute;
        }
	/** define an attribute in an other viewable that should hold the same value
	*/
	public void addRequiredIn(ObjectPath attribute)
	{
		requiredIn.add(attribute);
	}
        public Iterator<ObjectPath> iteratorRequiredIn()
        {
          return requiredIn.iterator();
        }
        @Override
        public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
          throws java.lang.IllegalStateException
        {
            super.checkTranslationOf(errs,name,baseName);
            ExistenceConstraint baseElement=(ExistenceConstraint)getTranslationOf();
            if(baseElement==null) {
                return;
            }
            
            Ili2cSemanticException err=Evaluable.checkTranslation(restrictedAttribute, baseElement.restrictedAttribute, getSourceLine(), "err_diff_existConstraintAttributePathMismatch");
            if(err!=null) {
                errs.add(err);
            }
            if(requiredIn.size()!=baseElement.requiredIn.size()) {
                err=new Ili2cSemanticException(getSourceLine(), Element.formatMessage("err_diff_existConstraintRequiredInMismatch"));
                if(err!=null) {
                    errs.add(err);
                }
            }else {
                for(int ri=0;ri<requiredIn.size();ri++) {
                    err=Evaluable.checkTranslation(requiredIn.get(ri), baseElement.requiredIn.get(ri), getSourceLine(), "err_diff_existConstraintRequiredInMismatch");
                    if(err!=null) {
                        errs.add(err);
                    }
                }
            }
        }
}
