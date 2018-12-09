/*****************************************************************************
 *
 * Selection.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** A selection that is part of the definition of a Viewable.
*/
public class Selection extends AbstractLeafElement
{
  private Viewable      viewdef;

  /** Constructs a new Selection
  */
  public Selection(Viewable viewdef)
  {
    this.viewdef = viewdef;
  }


  /** Returns the View that this selection is part of.
  */
  public Viewable getSelected ()
  {
    return viewdef;
  }

}
