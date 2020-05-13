package ch.interlis.ili2c.metamodel;

/**
 * @author ce
 */
public class Ili1Format {
	public static final int DEFAULT_BLANK_CODE = 0x5f;
	public static final  int DEFAULT_UNDEFINED_CODE = 0x40;
	public  static final int DEFAULT_CONTINUE_CODE = 0x5c;
	public boolean isFree=true;
	public int lineSize = 0;
	public int tidSize = 0;
	public int blankCode = DEFAULT_BLANK_CODE;
	public int undefinedCode = DEFAULT_UNDEFINED_CODE;
	public int continueCode = DEFAULT_CONTINUE_CODE;
	public String font=null;
	public int tidKind;
	public static final int TID_I16=1;
	public static final int TID_I32=2;
	public static final int TID_ANY=3;
	public static final int TID_EXPLANATION=4;
	public String tidExplanation=null;
	
}
