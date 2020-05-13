/*****************************************************************************
 *
 * TypeModel.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** An Interlis Model which is used for modelling data types and units.

    @author Sascha Brawer, sb@adasys.ch
*/
public class TypeModel extends Model
{
  /** Constructs a new TypeModel. */
  public TypeModel()
  {
  }


  /** Returns a String consisting of <code>TYPE MODEL</code> followed
              by the model's name.
  */
  public String toString()
  {
    return "TYPE MODEL " + getScopedName(/*no scope */ null);
  }
}
