/*****************************************************************************
 *
 * CoordinateSystem.java
 * ---------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** A coordinate system.
    
    @author <a href="sb@adasys.ch">Sascha Brawer</a>, Adasys AG, CH-8006 Zurich
    @version July 23, 1999
*/
public class CoordinateSystem extends MetaObject
{
  /** Constructs a new CoordinateSystem given its name
      and a table of which it is an instance.
  */ 
  public CoordinateSystem (String name, Table table)
  {
    super (name, table);
  }
}
