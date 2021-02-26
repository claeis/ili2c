package ch.interlis.ili2c.metamodel;

import java.beans.PropertyVetoException;
import java.util.List;



/** A view that extends another view.
 */
public class ExtendedView extends View
{
  public ExtendedView(View base){
	  try {
		setExtending(base);
	} catch (PropertyVetoException e) {
		throw new Ili2cSemanticException(getSourceLine(),e);
	}
  }
  private boolean extended=false;
  /** to distinguish between EXTENDS and EXTENDED
   */
  public void setExtended(boolean extended){
  	this.extended=extended;
  }
  public boolean isExtended(){
  	return extended;
  }
  
  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      ExtendedView baseElement=(ExtendedView)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      if(isExtended()!=baseElement.isExtended()) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInExtended",getScopedName(),baseElement.getScopedName())));
      }
      Ili2cSemanticException err=null;
      err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseViewMismatch");
      if(err!=null) {
          errs.add(err);
      }
  }
  
}
