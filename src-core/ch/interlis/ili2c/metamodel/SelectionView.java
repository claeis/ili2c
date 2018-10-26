/*****************************************************************************
 *
 * SelectionView.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


/** A special type of View that is based on selecting another Viewable.

    @author Sascha Brawer, sb@adasys.ch
 */
public class SelectionView extends UnextendableView
{
  protected ViewableAlias selected = null;

  /** Construct a new SelectionView.
  */
  public SelectionView ()
  {
  }


  /** Return the selected Viewable.
  */
  public ViewableAlias getSelected()
  {
    return selected;
  }


  /** Set the selected Viewable.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>selected</code> property
                 and does not agree with the change.
  */
  public void setSelected (ViewableAlias selected)
    throws java.beans.PropertyVetoException
  {
    ViewableAlias oldValue = this.selected;
    ViewableAlias newValue = selected;

    if (oldValue == newValue)
      return;

    fireVetoableChange("selected", oldValue, newValue);
    this.selected = newValue;
    firePropertyChange("selected", oldValue, newValue);
  }


  /* Note: The text below is mostly a copy of the documentation
           for ch.interlis.View.setExtending() -- be sure
           to double check there in case you change anything
           here.
  */
  /** Causes this selection view to extend another selection
      view. The attributes of the extended view can be addressed
      as if they were specified by this view.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param extending  The new selection view being extended, or
                        <code>null</code> if this view is
                        going to be independent of other views.

      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is not a SelectionView.

      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is a final view; final
                 views can not be extended.

      @exception java.lang.IllegalArgumentException if
                 the resulting extension graph would contain
                 cycles. For instance, if <code>A</code> extends
                 <code>B</code> and <code>B</code> extends
                 <code>C</code>, the call <code>C.setExtending(A)</code>
                 would throw an exception.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (View extending)
    throws java.beans.PropertyVetoException
  {
    if ((extending != null) && !(extending instanceof SelectionView))
      throw new IllegalArgumentException(formatMessage(
        "err_selectionViewExtendingOther",
        this.toString(), extending.toString()));

    super.setExtending(extending);
  }


  /** Resolves an alias name for a base (the name before the tilde
      in INTERLIS-2 views).

      @param alias The alias name of the base table.

      @return The ViewableAlias, or <code>null</code> if the name
              could not be resolved.

      @see ch.interlis.ViewableAlias
  */
  public ViewableAlias resolveBaseAlias (String alias)
  {
    if ((selected != null) && alias.equals (selected.getName()))
      return selected;

    return super.resolveBaseAlias (alias);
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
    if (selected == null)
      return null;

    return selected.getAliasing ();
  }
}
