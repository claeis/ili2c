/*****************************************************************************
 *
 * SymbologyModel.java
 * -------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.List;

/** An Interlis Model which is used for modelling symbologies.

    @author Sascha Brawer, sb@adasys.ch
*/
public class SymbologyModel extends Model
{
  /** Constructs a new SymbologyModel. */
  public SymbologyModel()
  {
  }

  
  /** Returns a String consisting of <code>SYMBOLOGY MODEL</code> followed
              by the model's name.
  */
  public String toString()
  {
    return "SYMBOLOGY MODEL " + getScopedName(/*no scope */ null);
  }
  
  
  /** Performs certain integrity checks, including checks for the
      elements of a container. Unfortunately, some checks
      can only be performed when all modifications are done.
      A SymbologyModel must be contracted.

      @exception java.lang.IllegalStateException if the integrity
                 is not given.
  */
  @Override
  public void checkIntegrity (List<Ili2cSemanticException> errs)
    throws java.lang.IllegalStateException
  {
    super.checkIntegrity (errs);
    
    if (isIli23() && !isContracted())
    {
      throw new IllegalStateException (formatMessage (
        "err_symbologyModel_uncontracted",
        this.toString ()));
    }
  }
}
