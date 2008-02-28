/*****************************************************************************
 *
 * UnionView.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;



/** A view that is based on unioning a number of Viewables.


    @author Sascha Brawer, sb@adasys.ch
    @version 1.0 - July 1, 1999
 */
public class UnionView extends UnextendableView
{
  protected ViewableAlias[] united = new ViewableAlias[0];


  public UnionView()
  {
  }



  /** Returns which Viewables are united to form this UnionView. */
  public ViewableAlias[] getUnited()
  {
    return united;
  }



  /** Sets which Viewables are united to form this UnionView.


      @exception java.lang.IllegalArgumentException if
                 <code>united</code> is <code>null</code>.
  */
  public void setUnited (ViewableAlias[] united)
    throws java.beans.PropertyVetoException
  {
    ViewableAlias[] oldValue = this.united;
    ViewableAlias[] newValue = united;


    if (oldValue == newValue)
      return;


    if (newValue == null)
      throw new IllegalArgumentException(rsrc.getString("err_nullNotAcceptable"));


    checkForDuplicateNames (newValue);


    fireVetoableChange("united", oldValue, newValue);
    this.united = newValue;
    firePropertyChange("united", oldValue, newValue);
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
    for (int i = 0; i < united.length; i++)
      if (alias.equals (united[i].getName()))
        return united[i];


    return null;
  }



  public void setAbstract (boolean abst)
    throws java.beans.PropertyVetoException
  {
    super.setAbstract (abst);
  }
}
