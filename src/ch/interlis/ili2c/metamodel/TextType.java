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

/** TextType holds the information associated with an Interlis
    TextType.
    
    @version   January 28, 1999
    @author    Sascha Brawer
*/
public class TextType extends BaseType
{
  /** The maximal number of characters allowed for a field of this type.
      A value of -1 means "unspecified".
  */
  protected int    maxLength;
  
  public TextType ()
  {
    maxLength = -1;
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
    if (maxLength != -1)
      return "TEXT*" + Integer.toString (maxLength);
    else
      return "TEXT";
  }


  /** An abstract type is one that does describe sufficiently
      the set of possible values. A TextType is abstract
      if its maximal length is not specified.
      
      @return Whether or not this type is abstract.
  */
  public boolean isAbstract ()
  {
    return maxLength == -1;
  }
  
  
  /** @return The maximal number of characters that is allowed for a
              field of this type, as specified in the description language.
      @beaninfo
       description: The maximal number of characters that is allowed for a field of this type.
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
    TextType   general;
    
    super.checkTypeExtension (wantToExtend);
    if ((wantToExtend == null)
        || ((wantToExtend = wantToExtend.resolveAliases()) == null))
      return;
    
    if (!(wantToExtend instanceof TextType))
      throw new IllegalArgumentException (rsrc.getString (
        "err_textType_ExtOther"));

    general = (TextType) wantToExtend;
    if (general.maxLength != -1)
    {
      if (this.maxLength == -1)
        throw new IllegalArgumentException (formatMessage (
          "err_textType_abstractExtConcrete", general.toString()));
      
      if (this.maxLength > general.maxLength)
        throw new IllegalArgumentException (formatMessage (
          "err_textType_longerExtShorter",
          this.toString(), general.toString()));
    }
  }
}
