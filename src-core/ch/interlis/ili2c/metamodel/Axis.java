/*****************************************************************************
 *
 * Axis.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** An INTERLIS Axis.
*/
public class Axis extends MetaObject
{
  /** Constructs an Axis given its name at a table of which
      it is an instance.
  */
  public Axis (String name, Table table)
  {
    super (name, table);
  }
}
