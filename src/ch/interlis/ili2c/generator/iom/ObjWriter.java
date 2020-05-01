package ch.interlis.ili2c.generator.iom;

import java.io.Writer;
import java.io.IOException;

/**
 * @author ce
 */
public interface ObjWriter {
	/** called by the encoder to write an object. Implementations
	 * call getobjid(obj) to get the objects id (also the ids of referenced objects)
	 * and encodeString.
	 */
	public void writeObject(Writer out,Object obj, WriterCallback cb)
	  throws IOException;
}
