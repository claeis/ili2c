package ch.interlis.ili2c;

/** signals a failure of an INTERLIS compiler run.
 */
public class Ili2cException extends Exception {

	public Ili2cException() {
	}

	public Ili2cException(String arg0) {
		super(arg0);
	}

	public Ili2cException(Throwable arg0) {
		super(arg0);
	}

	public Ili2cException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
