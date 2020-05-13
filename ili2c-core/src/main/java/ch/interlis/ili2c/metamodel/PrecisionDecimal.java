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
*/
public class PrecisionDecimal
{
	public final static PrecisionDecimal PI = new PrecisionDecimal(Math.PI);
	public final static PrecisionDecimal LNBASE = new PrecisionDecimal(Math.E);
  private int exponent = 0;
  private int accuracy=0;
  private final java.math.BigInteger intVal;

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
  public PrecisionDecimal (double v)
  {
	  this(Double.toString(v));
  }
  /** Constructs a precision decimal number from its
      string representation as it would appear in an
      INTERLIS 1 or 2.1 description file.


      @param s The string representation, for example
               <code>123.400e-3</code> (ILI2.1) or <code>123.400S-3</code> (ILI1).
  */
  public PrecisionDecimal (String s)
  {



    int sp=exponentPos(s);

    if (sp >= 0)
    {
      exponent = Integer.parseInt (s.substring (sp + 1));
  		s= s.substring(0, exponentPos(s));
    }
    intVal=(new java.math.BigDecimal(s)).unscaledValue();

    //
    // set accuracy
    //
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
    }else{
    	accuracy=0;
    }
  }


  public int getAccuracy()
  {
  	return accuracy;
  }

  /** Returns the number after the 'S'.
  */
  public int getExponent ()
  {
    return exponent;
  }

  public java.math.BigInteger getUnscaledValue()
  {
	  return intVal;
  }

  /** Returns the string representation of this number
      as it would appear in an INTERLIS 2.1 description file.
  */
  public String toString ()
  {
      return layoutChars(false) ;
  }
  private String layoutChars(boolean isIli1)
  {
	  int scale=getAccuracy();
      char ac[];
      ac = intVal.abs().toString().toCharArray();
      StringBuilder stringbuilder = new StringBuilder(ac.length + 14);
      if(intVal.signum() < 0)
          stringbuilder.append('-');
      if(scale > 0)
      {
          int i = scale - ac.length;
          if(i >= 0)
          {
        	  // 0.00..0+unscaledValue
              stringbuilder.append('0');
              stringbuilder.append('.');
              for(; i > 0; i--)
                  stringbuilder.append('0');

              stringbuilder.append(ac);
          } else
          {
        	  // unsc '.' aledValue
              stringbuilder.append(ac, 0, -i);
              stringbuilder.append('.');
              stringbuilder.append(ac, -i, scale);
          }
      } else if(scale<0) {
          stringbuilder.append(ac[0]);
          if(ac.length > 1)
          {
              stringbuilder.append('.');
              stringbuilder.append(ac, 1, ac.length - 1);
          }
      }else{ // scale==0
          stringbuilder.append(ac);
      }
      if(exponent!=0){
          stringbuilder.append(isIli1 ? 'S' : 'e');
          stringbuilder.append(Integer.toString (exponent));

      }
      return stringbuilder.toString();
  }

  /** Returns the string representation of this number
      as it would appear in an INTERLIS 1 description file.
  */
  public String toIli1String ()
  {
    return layoutChars(true);
  }



  /** Returns the number of this PrecisionDecimal as
      double. For example, 123.4S3 is returned as
      123400.0.
  */
  public double doubleValue()
  {
    return Double.parseDouble(layoutChars(false));
  }
  public int compareTo(PrecisionDecimal other)
  {
	  if(exponent==other.exponent && accuracy==other.accuracy){
		  return intVal.compareTo(other.intVal);
	  }
      int i = intVal.signum() - other.intVal.signum();
      if(i != 0)
          return i <= 0 ? -1 : 1;
	  return Double.compare(doubleValue(), other.doubleValue());
  }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PrecisionDecimal)) {
            return false;
        }
        return equals(this, (PrecisionDecimal) obj);
    }

    public static boolean equals(PrecisionDecimal a, PrecisionDecimal b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.compareTo(b) == 0) {
            return true;
        }
        return false;
    }

    static public void main(String args[]){
	 test("1");
	 test("1.0");
	 test("1e1");
	 test("10.0e1");
	 test("10.00e1");
	 test("10.0e-2");
	 test("0.000000000000000");
	 test("9999999999999999.000000000000000");
	 test("99999999999999990000000000000000");

  }
private static void test(String valStr) {
	PrecisionDecimal test1=new PrecisionDecimal(valStr);
	System.out.println(valStr+": Accuracy "+test1.getAccuracy()+", Exponent "+test1.getExponent()+", unscaledValue "+test1.intVal);
	 System.out.println(valStr+": "+test1.toString()+", double "+test1.doubleValue());
}
}
