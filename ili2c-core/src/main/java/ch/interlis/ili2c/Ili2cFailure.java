package ch.interlis.ili2c;

/** signals a failure of an INTERLIS compiler run.
 */
public class Ili2cFailure extends Ili2cException {

	public Ili2cFailure() {
	}

	public Ili2cFailure(String arg0) {
		super(arg0);
	}

	public Ili2cFailure(Throwable arg0) {
		super(arg0);
	}

	public Ili2cFailure(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
