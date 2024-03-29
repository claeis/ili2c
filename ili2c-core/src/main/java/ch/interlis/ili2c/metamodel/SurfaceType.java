/*****************************************************************************
 *
 * SurfaceType.java
 * ----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;


/** The type which is represented in INTERLIS using the keyword
    <code>SURFACE</code>.
*/
public class SurfaceType extends SurfaceOrAreaType
{
	private boolean isIli1=false;
  /** Constructs a new SurfaceType.
  */
  public SurfaceType ()
  {
  }
  public SurfaceType (boolean isIli1)
  {
	  this.isIli1=isIli1;
  }
  

  /** Returns "SURFACE".
  */
  public String toString ()
  {
    return "SURFACE";
  }


  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.
      
      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  @Override
  void checkTypeExtension (Type wantToExtend)
  {
    super.checkTypeExtension (wantToExtend);
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    
    if (!(wantToExtend instanceof SurfaceType)){
      throw new IllegalArgumentException (rsrc.getString (
        "err_surfaceType_ExtOther"));
    }
  }
  @Override
  public boolean isAbstract(StringBuilder err) {
  	if(!isIli1 && getMaxOverlap()==null){
  		err.append("missing WITHOUT OVERLAPS");
  		return true;
  	}
  	return super.isAbstract(err);
  }
  
}
