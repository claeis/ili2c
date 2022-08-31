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
import java.util.WeakHashMap;
import java.util.Collections;
import java.util.HashSet;

/** An abstract class that groups together all containers that
    can be imported.
*/
public abstract class Importable<E extends Element> extends Container<E>
{
  protected String   name;

  /** The model definitions which depend on this <code>Model</code>.
   */
  protected Set<Model> importedBy = Collections.newSetFromMap(new WeakHashMap<Model, Boolean>());
}
