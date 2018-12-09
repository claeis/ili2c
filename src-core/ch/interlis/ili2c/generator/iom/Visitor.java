package ch.interlis.ili2c.generator.iom;

/**
 * @author ce
 */
public interface Visitor {
	/** called by the encoder to get known of referenced objects.
	 * Implementations should call addPendingObject(refObj).
	 */
	public void visitObject(Object obj,VisitorCallback cb);
}
