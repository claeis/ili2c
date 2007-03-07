package ch.interlis.ili2c.generator.iom;

/**
 * @author ce
 */
public interface VisitorCallback {
	/** adds an object to be visited. Called by Visitor implementations.
	 */
	public void addPendingObject(Object obj);
}
