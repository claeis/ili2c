/*****************************************************************************
 *
 * SignInstruction.java
 * --------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.*;

/** An instruction which indicates how to assign values to the parameters
*   of a specific SIGN class. The set of objects to be used by this instruction
*   may be narrowed by a logical expression.
*/
public class SignInstruction extends Container
{

  protected Evaluable              restrictor;

  public SignInstruction (
    Evaluable             restrictor,
    ParameterAssignment[] assignments)
  {
    this.restrictor = restrictor;
    for(int i=0;i<assignments.length;i++){
	add(assignments[i]);
    }
  }

  protected Collection createElements(){
    return new ArrayList();
  }
  public Evaluable getRestrictor ()
  {
    return restrictor;
  }


  public ParameterAssignment[] getAssignments ()
  {
    return (ParameterAssignment[])toArray(new ParameterAssignment[0]);
  }
};
