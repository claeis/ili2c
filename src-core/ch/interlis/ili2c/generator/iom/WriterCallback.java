package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.Constant;

/**
 * @author ce
 */
public interface WriterCallback {
	public String getobjid(Object obj);
	public String encodeOid(String value);
	public String encodeString(String value);
	public String encodeBoolean(boolean value);
	public String encodeInteger(int value);
	public String encodeLong(long value);
	public String encodePrecisionDecimal(PrecisionDecimal value);
	public String encodeStructDec(Constant.Structured value);
	public String encodeDouble(double value);
	public String newline();
}
