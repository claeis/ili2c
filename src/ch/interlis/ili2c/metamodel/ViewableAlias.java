package ch.interlis.ili2c.metamodel;


/** An alias for a Viewable. For instance, <code>JOIN VIEW</code>s
    allow to "locally rename" (i.e. make an alias) the joined viewables.
    The same Viewable can be given several aliases. Thus, the same
    viewable can be joined several times.
*/
public class ViewableAlias extends Evaluable
{
  private Viewable   aliasing;
  private String     name;
  private boolean includeNull;

  /** Creates a new ViewableAlias for a given viewable.

      @param name The alias name (the part before the tilde),
                  or <code>null</code> if no special alias name
                  is used to rename a Viewable.

      @param aliasing The Viewable being aliased.
  */
  public ViewableAlias (String name, Viewable aliasing)
  {
    this.aliasing = aliasing;
    this.name = name;
  }

  /** Returns the alias name, or the name of the aliased Viewable.
  */
  public String getName ()
  {
    return name!=null ? name : aliasing.getName();
  }


  /** Returns the Viewable for which this object is an alias for. */
  public Viewable getAliasing ()
  {
    return aliasing;
  }

  /** include empty object into join, if no matching one defined */
  public void setIncludeNull(boolean v){
	  includeNull=v;
  }
}
