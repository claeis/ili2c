/*****************************************************************************
 *
 * Cardinality.java
 * ----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** Immutable class that specifies the cardinality of a relation.
 * FIXME The class has a "setMinimum(long) method and is therefore not immutable:
 *       Change documentation or remove setMinimum().
 *       If the class becomes immutable, clone() is not needed anymore.

    Let <em>I</em> be an instance of an INTERLIS table with a relational
    attribute. For <em>I</em> to be valid, at least a specified minimum
    and at most a specified maximum number of instances of the relational
    attribute's domain must refer concurrently to <em>I</em>. These
    numbers are indicated by the Cardinality object associated with
    the relational attribute.

    @see RelAttribute#setCardinality(Cardinality)
    @see RelAttribute#getCardinality()

    @author Sascha Brawer, sb@adasys.ch
    @version 1.0 - July 1, 1999
*/
public class Cardinality implements Cloneable
{

  long min;
  long max;

  /** Constructs a Cardinality that allows from zero
      to any number of objects. In Interlis, this
      would be written as either <code>{*}</code>
      or <code>{0..*}</code>.
  */
  public Cardinality() {
    min = 0;
    max = UNBOUND;
  }

  public static final long UNBOUND=Long.MAX_VALUE;

  /** Constructs a Cardinality given its minimum and maximum extent.

      @param min The minimal cardinality.
      @param max The maximal cardinality. Pass java.lang.Long.MAX_VALUE
                 for unlimited maximal cardinality.
      @exception java.lang.IllegalArgumentException if
                 <code>min</code> exceeds <code>max</code>,
                 or if either one is negative.
  */
  public Cardinality(long min, long max) {
    if ((min < 0) || (max < 0))
      throw new IllegalArgumentException(
        ch.interlis.ili2c.metamodel.Element.rsrc.getString("err_cardNegative"));

    if (min > max)
      throw new IllegalArgumentException(
        ch.interlis.ili2c.metamodel.Element.rsrc.getString("err_cardMaxLessMin"));

    this.min = min;
    this.max = max;
  }


  /** Returns the minimum possible number of instances considered
      acceptable by this Cardinality. A return value of
      <code>java.lang.Long.MAX_VALUE</code> means that any number
      of instances greater or equal <code>getMinimum()</code> is
      acceptable.
  */
  public long getMinimum() {
    return min;
  }
  public void setMinimum(long min) {
    this.min=min;
  }


  /** Returns the maximum possible number of instances considered
      acceptable by this Cardinality.
  */
  public long getMaximum() {
    return max;
  }


  /* Two Cardinality obejcts are considered equal if both have the
     same minimum and maximum values.

     Obeys the general contract of java.lang.Object.equals
  */
  public boolean equals(Object o) {
    if ((o == null) || !(o instanceof Cardinality))
      return false;

    Cardinality other = (Cardinality) o;
    return (this.min == other.min) && (this.max == other.max);
  }


  /* Obeys the general contract of java.lang.Object.hashCode */
  public int hashCode()
  {
    return ((int) min) + ((int) max);
  }


  /** Determines whether this cardinality is generalizing another
      cardinality. Any cardinality is generalizing itself.

      A is generalizing B if and only if A.min <= B.min and
      A.max >= B.max.

      @return true if <code>this</code> is a generalization of
              <code>restricted</code>.
  */
  public boolean isGeneralizing(Cardinality restricted)
  {
    return (this.min <= restricted.min   )
      && (this.max >= restricted.max);
  }


  private static String makeString(long i)
  {
    if (i == UNBOUND)
      return "*";
    else
      return Long.toString(i);
  }


  /** Creates a String object designating this Cardinality.
      That string can be displayed to the user or incorporated
      into a machine-generated INTERLIS description, because
      the returned value corresponds to the INTERLIS syntax
      for Cardinalities.

      @return A string such as "{2..4}", "{3}", "{*}" or "{42..*}".
  */
  public String toString() {
    if ((min == 0) && (max == UNBOUND))
      return "{0..*}";
    else if (min == max)
      return "{" + makeString(min) + "}";
    else
      return "{" + makeString(min) + ".." + makeString(max) + "}";
  }


  public Cardinality clone() {
      Cardinality cloned = null;

      try {
          cloned = (Cardinality) super.clone();

      } catch (CloneNotSupportedException e) {
          // Never happens because the object is cloneable
      }
      return cloned;
  }

}
