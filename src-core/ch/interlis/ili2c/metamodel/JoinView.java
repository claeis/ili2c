/*****************************************************************************
 *
 * JoinView.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;



/** @author Sascha Brawer, sb@adasys.ch
 */
public class JoinView extends UnextendableView
{
  protected ViewableAlias[] joining = new ViewableAlias[0];


  public JoinView()
  {
  }



  public ViewableAlias[] getJoining()
  {
    return joining;
  }



  /** @exception java.lang.IllegalArgumentException if
                 <code>joining</code> is <code>null</code>.
  */
  public void setJoining (ViewableAlias[] joining)
    throws java.beans.PropertyVetoException
  {
    ViewableAlias[] oldValue = this.joining;
    ViewableAlias[] newValue = joining;


    if (oldValue == newValue)
      return;


    if (newValue == null)
      throw new IllegalArgumentException(rsrc.getString("err_nullNotAcceptable"));


    checkForDuplicateNames (newValue);


    fireVetoableChange("joining", oldValue, newValue);
    this.joining = newValue;
    firePropertyChange("joining", oldValue, newValue);
  }



  /** Resolves an alias name for a base (the name before the tilde
      in INTERLIS-2 views).


      @param alias The alias name of the base table.


      @return The matching ViewableAlias, or <code>null</code> if the name
              could not be resolved.


      @see ch.interlis.ViewableAlias
  */
  public ViewableAlias resolveBaseAlias (String alias)
  {
    for (int i = 0; i < joining.length; i++)
      if (alias.equals (joining[i].getName()))
        return joining[i];


    return null;
  }



  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
    super.setAbstract (abst);
  }


  /* Note: The text below is mostly a copy of the documentation
           for ch.interlis.View.setExtending() -- be sure
           to double check there in case you change anything
           here.
  */
  /** Causes this join view to extend another join view. The
      attributes of the extended view can be addressed as if
      they were specified by this view.


      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param extending  The new join view being extended, or
                        <code>null</code> if this view is
                        going to be independent of other views.


      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is not a JoinView.


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
  public void setExtending(View extending)
    throws java.beans.PropertyVetoException
  {
    if ((extending != null) && !(extending instanceof JoinView))
      throw new IllegalArgumentException(formatMessage(
        "err_joinView_extendingOther",
        this.toString(), extending.toString()));


    super.setExtending(extending);
  }
}
