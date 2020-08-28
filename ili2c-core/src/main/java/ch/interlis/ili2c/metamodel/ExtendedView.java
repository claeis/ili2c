package ch.interlis.ili2c.metamodel;

import java.beans.PropertyVetoException;



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
}
