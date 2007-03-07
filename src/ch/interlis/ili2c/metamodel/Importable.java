/*****************************************************************************
 *
 * Importable.java
 * ---------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.Set;
import java.util.HashSet;

/** An abstract class that groups together all containers that
    can be imported.
*/
public abstract class Importable extends Container
{
  protected String   name;

  /** The model definitions which depend on this <code>Model</code>.
   */
  protected Set      importedBy = new HashSet(2);
}
