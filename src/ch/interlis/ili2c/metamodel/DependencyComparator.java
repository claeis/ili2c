package ch.interlis.ili2c.metamodel;

/** Imposes a total ordering on Interlis Elements based on
    the dependencies.
    
    Note that the Java documentation before JDK 1.2 final stated
    incorrectly that a partial ordering would be acceptable.
*/
public class DependencyComparator
  implements java.util.Comparator, java.io.Serializable
{
  public int compare(Object o1, Object o2)
    throws ClassCastException
  {
    Element d1 = (Element) o1;
    Element d2 = (Element) o2;
  
    if ((d1 == d2) || (d1.equals(d2)))
      return 0;

    if (d1 == null)
      return -1;
    
    if (d2 == null)
      return +1;
    
    if (d1.isDependentOn(d2)) {
      //System.out.println("" + d1 + " > " + d2);
      return +1; /* d1 > d2 */
    } else if (d2.isDependentOn(d1)) {
      //System.out.println("" + d1 + " < " + d2);
      return -1; /* d2 > d1 ---> d1 < d2 */
    } else {
      /* d1 and d2 not ordered. Because we have to impose a total
         ordering, order them according to their hash value. This
         is not really safe, however, since two inequal objects
         could return the same value for hashCode().
      */
      int h1 = d1.hashCode();
      int h2 = d2.hashCode();
      if (h1 < h2)
        return -1;
      else if (h1 > h2)
        return +1;
      else
        return 0; /* this might lead to problems */
    }
  }
}
