/*****************************************************************************
 *
 * ReferenceSystem.java
 * --------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** A reference system.
    
    @author <a href="sb@adasys.ch">Sascha Brawer</a>, Adasys AG, CH-8006 Zurich
    @version July 23, 1999
*/
public class ReferenceSystem extends MetaObject
{
  /** Constructs a new ReferenceSystem given its name and the table
      to which it belongs.
  */
  public ReferenceSystem (String name, Table table)
  {
    super (name, table);
  }
}
