
package ch.interlis.ili2c.metamodel;

import java.util.List;

public class SetConstraint extends Constraint
{
  public SetConstraint()
  {
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
      SetConstraint baseElement=(SetConstraint)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      
      if(preCondition==null && baseElement.preCondition==null) {
      }else if(preCondition!=null && baseElement.preCondition!=null) {
          Ili2cSemanticException err=Evaluable.checkTranslation(preCondition, baseElement.preCondition, getSourceLine(), "err_diff_setConstraintPreConditionMismatch");
          if(err!=null) {
              errs.add(err);
          }
      }else {
          Ili2cSemanticException err=new Ili2cSemanticException( getSourceLine(), Element.formatMessage("err_diff_setConstraintPreConditionMismatch"));
          errs.add(err);
      }
  }
}
