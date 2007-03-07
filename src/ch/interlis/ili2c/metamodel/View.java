/*****************************************************************************
 *
 * View.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;



/** A view is a "calculated" table whose attributes are
    derived from other tables or views.


    While a view has attributes in a way comparable to a table,
    its attributes are not original. Instead, they result from
    combining and selecting those of other tables or views.


    @author Sascha Brawer, sb@adasys.ch
 */
public abstract class View extends Viewable
{
  protected List     selections = new LinkedList ();
  private boolean propTransient=false;

  /** Creates a new View without any attributes. */
  public View()
  {
  }
  protected Collection createElements(){
    return new AbstractCollection()
    {
      public Iterator iterator ()
      {
        return new CombiningIterator(new Iterator[] {
          selections.iterator(),
          attributes.iterator(),
          constraints.iterator()
        });
      }


      public int size()
      {
        return selections.size() + attributes.size() + constraints.size();
      }


      public boolean add (Object o)
      {
        if (o == null)
          throw new IllegalArgumentException(
            rsrc.getString("err_nullNotAcceptable"));


        if (o instanceof Selection)
          return selections.add (o);


        if (o instanceof LocalAttribute)
          return attributes.add (o);



        if (o instanceof Constraint)
          return constraints.add (o);


        throw new ClassCastException();
      }
    };
  }



  /** Returns a String that designates this view. This is useful
      to generate error messages, window titles etc. However,
      you probably would not want to use this method for generating
      Interlis description files because the lack of support for
      name scoping rules; use <code>getScopedName</code> instead.


      @return A String consisting of the letters <code>VIEW</code>
              followed by the fully scoped name of this view.


      @see #getScopedName(ch.interlis.Container)
  */
  public String toString()
  {
    return "VIEW " + getScopedName (null);
  }



  protected void checkForDuplicateNames (ViewableAlias[] aliases)
    throws java.lang.IllegalArgumentException
  {
    for (int i = 0; i < aliases.length; i++)
    {
      String curName = aliases[i].getName();
      for (int j = 0; j < i; j++)
        if (curName.equals (aliases[j].getName()))
        {
          throw new IllegalArgumentException (
            formatMessage ("err_view_dupAlias", curName,
                           aliases[i].getAliasing().toString(),
                           aliases[j].getAliasing().toString()));
        }
    }
  }



  /** Causes this view to extend another view.


      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param extending  The new view being extended, or
                        <code>null</code> if this view is
                        going to be independent of other views.


      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is not <code>null</code>,
                 as Views are not extensible.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change. Actually,
                 this should not happen in the case of Views,
                 because they can't really change the value
                 of the <code>extending</code> property.
                 Nevertheless, it must be declared in order
                 to inherit from Viewable.
  */
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException
  {
    if (extending != null)
      throw new IllegalArgumentException (
        formatMessage ("err_view_notExtensible", this.toString(),
                       extending.toString()));


    super.setExtending ((View) extending);
  }



  /** Determines whether or not <code>this</code> is dependent on
      <code>other</code>. Dependent elements are required to follow
      their base elements in an Interlis description file.


      <ol>
      <li>If an Element <i>A</i> extends another element <i>B</i>,
          be it directly or indirectly, <i>A</i> is dependent on <i>B</i>.</li>
      <li>If a View is selecting a Viewable, it is dependent on
          that Viewable.</li>
      </ol>


      @param other The Element of which it is determined whether
                   <code>this</code> does depend on.


      @return <code>true</code> if this Element depends on <code>other</code>,
              <code>false</code> otherwise.
  */
  public boolean isDependentOn (Element other)
  {
    Iterator iter = selections.iterator ();
    while (iter.hasNext ())
    {
      Selection sel = (Selection) iter.next ();
      if (sel == other)
        return true;


      Viewable selected = sel.getSelected ();
      if ((selected != null) && ((selected == other) || selected.isDependentOn (other)))
        return true;
    }
    return super.isDependentOn (other);
  }
  public void setTransient(boolean transient1){
	  propTransient=transient1;
  }
}
