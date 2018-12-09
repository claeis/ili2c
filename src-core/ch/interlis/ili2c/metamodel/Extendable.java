/*****************************************************************************
 *
 * Extendable.java
 * ---------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** A common interface for INTERLIS elements that can be extended.
    Examples include Tables, Views, Topics, etc.
    Type doesn't, but should also implement this interface.
*/
public interface Extendable
{
  /** Walks the extension hierarchy to determine whether or not <code>this</code>
      is extending <code>ext</code>, be the extension directly or indirectly.
      Any Extendable is extending itself. In other words, this function
      corresponds to the <em>reflexive-transitive closure</em> of the relation
      determined by the <code>setExtending</code> operation.
  */
  public boolean isExtending (Element ext);

  /** Causes this Extendable to extend another one.
    
      <p>Implementing classes follow the JavaBeans component model.
      In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @exception java.lang.IllegalArgumentException if <code>extending</code>
                 is not a valid argument. For example, a Table can not
                 extend a Topic. Some implementing classes impose
                 additional constraints, such as that a final Element
                 can not be extended.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException;


  /** Retrieves which Extendable this Extendable is currently extending.
  
      @return The currently extended container, or <code>null</code>
              if this container does not extend any other container.
      
      @see #setExtending(ch.interlis.Extendable)
      @see #isExtending(ch.interlis.Extendable)
  */
  public Element getExtending ();
  public Element getRealExtending ();


  /** Calculates a Set of all Extendables that are extending
      this Extendable, be it through direct extension or be
      it in several steps.  The result consists of
      all directly extending objects plus all that directly
      extend those, plus ..., etc. In other words,
      this method calculates the transitive closure
      of the <em>extending</em> relation.
      
      @return A new Set that belongs to the caller; the caller
              is thus free to modify it according to its needs.
              Changes in the result will not have any effect
              on the <em>extending</em> property.
  */
  public java.util.Set getExtensions ();
}
