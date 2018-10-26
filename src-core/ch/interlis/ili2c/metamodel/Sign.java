/*****************************************************************************
 *
 * Sign.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** A Sign.
    
    @author <a href="sb@adasys.ch">Sascha Brawer</a>, Adasys AG, CH-8006 Zurich
    @version July 23, 1999
*/
public class Sign extends MetaObject
{
  /** Constructs a new Sign given its name the the table of which it is
      an instance.
  */
  public Sign (String name, Table table)
  {
    super (name, table);
  }
}
