/*****************************************************************************
 *
 * RefSystemModel.java
 * -------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** An Interlis Model which is used for modelling reference systems.

    @author Sascha Brawer, sb@adasys.ch
*/
public class RefSystemModel extends Model
{
  /** Constructs a new RefSystemModel. */
  public RefSystemModel()
  {
  }

  
  /** Returns a String consisting of <code>REFSYSTEM MODEL</code> followed
              by the model's name.
  */
  public String toString()
  {
    return "REFSYSTEM MODEL " + getScopedName(/*no scope */ null);
  }
}
