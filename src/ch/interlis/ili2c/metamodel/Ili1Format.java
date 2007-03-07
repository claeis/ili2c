package ch.interlis.ili2c.metamodel;

/**
 * @author ce
 */
public class Ili1Format {
	public boolean isFree=true;
	public int lineSize = 0;
	public int tidSize = 0;
	public int blankCode = 0x5f;
	public int undefinedCode = 0x40;
	public int continueCode = 0x5c;
	public String font=null;
	public int tidKind;
	public static final int TID_I16=1;
	public static final int TID_I32=2;
	public static final int TID_ANY=3;
	public static final int TID_EXPLANATION=4;
	public String tidExplanation=null;
	
}
