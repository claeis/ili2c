/*****************************************************************************
 *
 * PrecisionDecimal.java
 * ---------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;



/** A BigDecimal that is associated with a scaling as well.
    This is used to store INTERLIS decimal numbers when
    both scaling and precision need to be preserved.


    @version   April 7, 1999
    @author    Sascha Brawer
*/
public class PrecisionDecimal extends java.math.BigDecimal
{
  protected int exponent = 0;


  static private int exponentPos(String s){
    int ret;
    ret=s.indexOf('S');
    if(ret==-1){
      ret=s.indexOf('e');
    }
    if(ret==-1){
      ret=s.indexOf('E');
    }
    return ret;
  }
  /** Constructs a precision decimal number from its
      string representation as it would appear in an
      INTERLIS 1 or 2.1 description file.


      @param s The string representation, for example
               <code>123.400e-3</code> (ILI2.1) or <code>123.400S-3</code> (ILI1).
  */
  public PrecisionDecimal (String s)
  {


    super(exponentPos(s) == -1 ? s : s.substring(0, exponentPos(s)));

    int sp=exponentPos(s);

    if (sp >= 0)
    {
      exponent = Integer.parseInt (s.substring (sp + 1));
    }

    //
    // set accuracy
    //
	int accuracy=0;
    int pp=s.indexOf('.');
    // decimal point found?
    if(pp>=0){
    	if(sp>=0){
			if(pp<sp){
				accuracy=sp-pp-1;
			}else{
				// ignore it
			}
		}else{
			accuracy=s.length()-pp-1;
		}
    }
    setScale(accuracy);
  }


  public int getAccuracy()
  {
  	return scale();
  }

  /** Returns the number after the 'S'.
  */
  public int getExponent ()
  {
    return exponent;
  }



  /** Returns the string representation of this number
      as it would appear in an INTERLIS 2.1 description file.
  */
  public String toString ()
  {
    if (exponent == 0)
      return super.toString();
    else
      return super.toString() + "e" + Integer.toString (exponent);
  }
  /** Returns the string representation of this number
      as it would appear in an INTERLIS 1 description file.
  */
  public String toIli1String ()
  {
    if (exponent == 0)
      return super.toString();
    else
      return super.toString() + "S" + Integer.toString (exponent);
  }



  /** Returns the number of this PrecisionDecimal as
      double. For example, 123.4S3 is returned as
      123400.0.
  */
  public double doubleValue()
  {
    return (this.movePointRight (exponent)).doubleValue();
  }
}
