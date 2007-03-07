package ch.interlis.ili2c.metamodel;


public abstract class AbstractAttributeRef extends PathEl
{
  /** returns the type of the (may be implicit) attribute that is referenced
   * by this path element.
   */
  public abstract Type getDomain();
}
