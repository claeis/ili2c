/*****************************************************************************
 *
 * Graphic.java
 * ------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;
import java.util.*;


/** A description of an INTERLIS graphic, as specified by
    the <code>GRAPHIC</code> keyword in INTERLIS-2.

    @author Sascha Brawer, sb@adasys.ch
 */
public class Graphic extends ExtendableContainer<Element>
{
  protected String   name = "";

  protected List<SignAttribute> contents = new LinkedList<SignAttribute>();
  protected List<Selection>     selections = new LinkedList<Selection>();

  protected Viewable basedOn = null;

  public Graphic ()
  {
  }
  protected Collection<Element> createElements(){
    return new AbstractCollection<Element>()
    {
      public Iterator<Element> iterator() {
        return new CombiningIterator<Element>(
          new Iterator[]
          {
            selections.iterator(),
            contents.iterator()
          }
        );
      }


      public int size()
      {
        return selections.size() + contents.size();
      }


      public boolean add(Element o)
      {
        if (o == null)
          throw new IllegalArgumentException(
            rsrc.getString("err_nullNotAcceptable"));

        if (o instanceof SignAttribute)
          return contents.add((SignAttribute) o);

        if (o instanceof Selection)
          return selections.add((Selection) o);

        throw new ClassCastException();
      }
    };
  }


  /** Returns the value of the <code>name</code> property
      which indicates the name of this graphic without any scope
      prefixes.

      @see #setName(java.lang.String)
      @see #getScopedName(ch.interlis.Container)
  */
  public String getName ()
  {
    return name;
  }


  /** Returns a String that designates this graphic. This is useful
      to generate error messages, window titles etc. However,
      you probably would not want to use this method for generating
      Interlis description files because the lack of support for
      name scoping rules; use <code>getScopedName</code> instead.

      @return A String consisting of the letters <code>GRAPHIC</code>
              followed by the fully scoped name of this graphic,
              separated by a single space character.

      @see #getScopedName(ch.interlis.Container)
  */
  public String toString()
  {
    return "GRAPHIC " + getScopedName (null);
  }


  /** Sets the value of the <code>name</code> property.
      Graphicss are identified and used by specifying their name.

      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this graphic.

      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.

      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another graphic. The
                 only acceptable conflict is with the graphic that
                 this graphic directly extends.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    /* Make sure that the new name is not null, empty,
       too long, etc. */
    checkNameSanity (newValue, /* empty ok? */ false);

    /* Make sure that the new name does not conflict
       with the name of another Graphic, except the
       one that this object is extending directly.
    */
    checkNameUniqueness (newValue, Graphic.class, getRealExtending(),
      "err_graphic_duplicateName");

    /* JavaBeans requires that the value be changed between
       firing VetoableChangeEvent and PropertyChangeEvent
       objects. */
    fireVetoableChange ("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange ("name", oldValue, newValue);
  }


  /** Sets the value of the <code>abstract</code> property;
      there can not be instances of an abstract graphic.

      <p>In JavaBeans terminology, the <code>abstract</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param abst Pass <code>true</code> to make the graphic
                  abstract, pass <code>false</code> to make
                  the graphic instantiable.

      @exception java.lang.IllegalArgumentException if this
                 graphic is final and <code>abst</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
    if ((abst == false) && (basedOn != null) && basedOn.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_graphic_concreteWhileBasedOnAbstract",
        this.toString(), basedOn.toString()));

    super.setAbstract (abst);
  }


  /** Determines whether or not <code>this</code> is dependent on
      <code>other</code>. Dependent elements are required to follow
      their base elements in an Interlis description file.

      <ol><li>No Element is dependent on itself.</li>
      <li>If an Element <i>A</i> extends another element <i>B</i>,
          be it directly or indirectly, <i>A</i> is dependent
          on <i>B</i>.</li>
      <li>If a Graphic <i>G</i> is based on a Viewable <i>V</i>,
          <i>G</i> is dependent on <i>V</i>.
      </ol>

      @param other The Element of which it is determined whether
                   <code>this</code> does depend on.

      @return <code>true</code> if this Element depends on <code>other</code>,
              <code>false</code> otherwise.
  */
  public boolean isDependentOn (Element e)
  {
    if ((basedOn != null) && basedOn.isDependentOn (e))
      return true;

    return super.isDependentOn (e);
  }


  public Viewable getBasedOn ()
  {
    return basedOn;
  }


  public void setBasedOn (Viewable basedOn)
    throws java.beans.PropertyVetoException
  {
    Viewable oldValue = this.basedOn;
    Viewable newValue = basedOn;

    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue)
      return;

    if ((newValue instanceof Table) && !((Table) newValue).isIdentifiable())
      throw new IllegalArgumentException (formatMessage (
        "err_graphic_basedOnStructure",
        this.toString(), newValue.toString()));

    if ((newValue != null) && !this.isAbstract() && newValue.isAbstract())
      throw new IllegalArgumentException (formatMessage (
        "err_graphic_concreteBasedOnAbstract",
        this.toString(), newValue.toString()));

    Viewable extendingBasedOn;
    if (extending != null)
      extendingBasedOn = ((Graphic) extending).basedOn;
    else
      extendingBasedOn = null;

    if ((extendingBasedOn != null)
        && (newValue != null)
        && !newValue.isExtending (extendingBasedOn))
    {
      throw new IllegalArgumentException (formatMessage (
        "err_graphic_basedOnNotExtendingInheritedBasedOn",
        this.toString (),
        newValue.toString (),
        extending.toString (),
        extendingBasedOn.toString ()));
    }

    fireVetoableChange ("basedOn", oldValue, newValue);
    this.basedOn = newValue;
    firePropertyChange ("basedOn", oldValue, newValue);
  }


  /** Returns the name space of evaluables (right-hand side of assignments)
      mentioned somewhere inside this Container.

      <p>Certain elements of Containers, for instance constraints or selections,
      refer to attributes. The name space of these attributes is though not
      necessarily identical to the container.  For example, attribute names
      mentioned in a Graphic refer to the Viewable on which the Graphic
      is based, not the Graphic itself.
  */
  public Viewable getRHSNameSpace ()
  {
    return basedOn;
  }
}
