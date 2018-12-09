/*****************************************************************************
 *
 * ParameterAssignment.java
 * ------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** An assignment to a parameter.

    @version   July 15, 1999
    @author    Sascha Brawer
*/
public class ParameterAssignment extends AbstractLeafElement
{
  protected Parameter  assigned;
  protected Evaluable  value;


  /** Constructs a new ParameterAssignment that specifies
      which value is assigned to which parameter.
  */
  public ParameterAssignment (Parameter assigned, Evaluable value)
  {
    this.assigned = assigned;
    this.value = value;

    Type assignedType = null;
    if ((value != null) && (assigned != null))
      assignedType = assigned.getType ();

    if (assignedType != null)
      value.checkAssignment (assigned, assignedType);
  }

  /** Returns the parameter that is being assigned.
  */
  public Parameter getAssigned ()
  {
    return assigned;
  }


  /** Returns the value that gets assigned to the parameter.
  */
  public Evaluable getValue ()
  {
    return value;
  }
}
