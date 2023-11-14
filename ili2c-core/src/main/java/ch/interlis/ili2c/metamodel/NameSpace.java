/*****************************************************************************
 *
 * NameSpace.java
 * --------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


/** A NameSpace is a utility class that describes the name space currently
    used for parsing and generation. Note that the class has been added
    very late in the process of developing the INTERLIS compiler and
    is thus not used at all places where it might be suitable. A clean
    name space concept is definitely something that should be done
    in later releases of the INTERLIS compiler (and language specification).

    @author Sascha Brawer, sb@adasys.ch
 */
public class NameSpace
{
  Container   lvalues;
  Container   rvalues;
  Model       metaObjects;

  /** Constructs a new NameSpace where all different spaces are specified
      separately. This is used, for instance, with Graphics where
      the three namespaces are different (the symbology for the
      lvalues, the viewable on which the graphics is based for the
      rvalues, the graphics itself for the metaobjects).

      @param lvalue Name space of lvalues (the left-hand side of an assignment).
      @param rvalue Name space of rvalues (constants, evaluables, right-hand side
                    of an assignment).
      @param metaObjects Name space for resolving meta object names.
  */
  public NameSpace (Container lvalues, Container rvalues, Model metaObjects)
  {
    this.lvalues = lvalues;
    this.rvalues = rvalues;
    this.metaObjects = metaObjects;
  }

  /** Returns a descriptive String that might be useful for debugging. */
  public String toString ()
  {
    return "ch.interlis.NameSpace[lval=" + lvalues
      + ",rval=" + rvalues + ",meta=" + metaObjects + "]";
  }


  /** Constructs a new NameSpace where one Container servers as name space
      for everything.
  */
  public NameSpace (Container ns)
  {
    this (ns, ns, (Model) ns.getContainerOrSame (Model.class));
  }

  /** Returns the Container that serves as name space for the left-hand
      side (target) of assignments.
  */
  public Container getLValues ()
  {
    return lvalues;
  }

  /** Returns the Container that serves as name space for the right-hand
      side (source) of assignments. Things that are evaluated, such as
      constants, are in this name space.
  */
  public Container getRValues ()
  {
    return rvalues;
  }

  /** Returns the Model that serves as name space for meta objects.
  */
  public Model getMetaObjectNS ()
  {
    return metaObjects;
  }
}
