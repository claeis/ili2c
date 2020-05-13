/*****************************************************************************
 *
 * TextType.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** TextType holds the information associated with an Interlis
    TextType.

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public class TextType extends BaseType
{
  /** The maximal number of characters allowed for a field of this type.
      A value of -1 means "unlimited".
  */
  protected int    maxLength;
  private boolean normalized=true;

  public TextType ()
  {
    maxLength = -1;
  }
  public TextType (boolean normalized)
  {
    maxLength = -1;
    this.normalized=normalized;
  }


  public TextType (int maxLength)
  {
    if (maxLength < 0)
      throw new IllegalArgumentException (rsrc.getString ("err_textLenNegative"));

    if (maxLength == 0)
      throw new IllegalArgumentException (rsrc.getString ("err_textType_lenZero"));

    this.maxLength = maxLength;
  }


  public String toString ()
  {
  	String kw=normalized?"TEXT":"MTEXT";
    if (maxLength != -1)
      return kw+"*" + Integer.toString (maxLength);
    else
      return kw;
  }

  @Override
  public boolean isAbstract (StringBuilder err)
  {
    return false;
  }


  /** The maximal number of characters that is allowed for a
   * field of this type, or -1 if unlimited.
   */
  public int getMaxLength ()
  {
    return maxLength;
  }




  /** Checks whether it is possible for this to extend wantToExtend.
      If so, nothing happens; especially, the extension graph is
      <em>not</em> changed.

      @exception java.lang.IllegalArgumentException If <code>this</code>
                 can not extend <code>wantToExtend</code>. The message
                 of the exception indicates the reason; it is a localized
                 string that is intended for being displayed to the user.
  */
  void checkTypeExtension (Type wantToExtend)
  {

    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;

    if (!(wantToExtend instanceof TextType))
      throw new Ili2cSemanticException (rsrc.getString (
        "err_textType_ExtOther"));

	TextType   general;
    general = (TextType) wantToExtend;
    if (general.maxLength != -1)
    {
      if (this.maxLength == -1){
		throw new Ili2cSemanticException (formatMessage (
		  "err_textType_longerExtShorter",
		  this.toString(), general.toString()));
      }

      if (this.maxLength > general.maxLength){
		throw new Ili2cSemanticException (formatMessage (
		  "err_textType_longerExtShorter",
		  this.toString(), general.toString()));
      }
    }
    if(general.normalized!=this.normalized){
		throw new Ili2cSemanticException (formatMessage (
		  "err_textType_kindMismatch",
		  this.toString(), general.toString()));
    }
  }
public boolean isNormalized() {
	return normalized;
}
public void setNormalized(boolean b) {
	normalized = b;
}


    public TextType clone() {
        return (TextType) super.clone();
    }
    @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        TextType   originDef= (TextType) getTranslationOf();
        if(originDef==null) {
            return;
        }
        if (originDef.maxLength != this.maxLength)
        {
            throw new Ili2cSemanticException();
        }
        if(originDef.normalized != this.normalized){
            throw new Ili2cSemanticException();
        }
        
    }

}
