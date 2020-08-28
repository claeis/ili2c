package ch.interlis.ili2c.metamodel;

/** this is a placeholder to introduce another name for a given element.
 */
public interface ElementAlias {
	/** returns the Element to which this is an alias. May return an alias.
	 */
	public Element getNext();
	/** returns the non-alias Element to which this is an alias
	 */
	public Element getReal();
	/** returns the name defined by this alias
	 */
	public String getAlias();
	/** sets the alternative name defined by this alias
	 */
	public void setAlias(String alias);
	/** checks if this element is an alias
	 */
	public boolean isAlias();
	/** create an alias element of this
	 */
	public ElementAlias createAlias(String alias);

}

