package ch.interlis.ili2c.metamodel;

/** constants to represent values of syntax rule Properties<...>
 */ 
public class Properties {
	public static final int eUNDEFINED=0x0000;
	public static final int eABSTRACT=0x0001;
	public static final int eFINAL=0x0002;
	public static final int eEXTENDED=0x0004;
	public static final int eORDERED=0x0008;
	public static final int eDATA=0x0010;
	public static final int eVIEW=0x0020;
	public static final int eBASE=0x0040;
	public static final int eGRAPHIC=0x0080;
	public static final int eEXTERNAL=0x0100;
	public static final int eTRANSIENT=0x0200;
	public static final int eOID=0x0400;
	public static final int eHIDING=0x0800;
	public static final int eGENERIC=0x1000;
	/** do not instantiate 
	 */
	private Properties(){};
}