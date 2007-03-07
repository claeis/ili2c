package ch.interlis.ili2c.metamodel;



/** A view that extends another view.
 */
public class ExtendedView extends View
{
  /** The base view of this one.
  */
  private View base=null;
  public ExtendedView(View base){
	  this.base=base;
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
