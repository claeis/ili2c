/*****************************************************************************
 *
 * AreaType.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


/** The type which is represented in INTERLIS using the keyword MULTIAREA.
*/
public class MultiAreaType extends MultiSurfaceOrAreaType
{
  /** Constructs a new AreaType.
  */
  public MultiAreaType ()
  {
  }



  /** Returns <code>MULTIAREA</code>. */
  public String toString ()
  {
    return "MULTIAREA";
  }

  void checkTypeExtension (Type wantToExtend)
  {
    super.checkTypeExtension (wantToExtend);
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    
    if (!(wantToExtend instanceof MultiSurfaceOrAreaType)){
      throw new IllegalArgumentException (rsrc.getString (
        "err_areaType_ExtOther"));
    }
    checkCardinalityExtension(wantToExtend);
  }
  @Override
  public boolean isAbstract(StringBuilder err) {
  	if(getMaxOverlap()==null){
  		err.append("missing WITHOUT OVERLAPS");
  		return true;
  	}
  	return super.isAbstract(err);
  }

}
