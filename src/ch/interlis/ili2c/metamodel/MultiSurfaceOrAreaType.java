/*****************************************************************************
 *
 * SurfaceOrAreaType.java
 * ----------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** An abstract class that groups SurfaceType and AreaType, because these
    two have many things in common.
*/
public abstract class MultiSurfaceOrAreaType extends AbstractSurfaceOrAreaType
{
  protected MultiSurfaceOrAreaType ()
  {
  }
}
