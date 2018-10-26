package ch.interlis.ili2c.metamodel;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Combines several iterators to a single one. A meta-iterator
    object is passed to the constructor of this class; the results
    of its next() method must implement the java.util.Iterator
    interface.

    @author   <a href="mailto:sb@adasys.ch">Sascha Brawer</a>
    @version  February 8, 1999
    @see      java.util.Iterator
*/
public final class CombiningIterator<T> implements Iterator<T>
{
  private Iterator<Iterator<T>> metaIter;
  private Iterator<T> curIter;

  /** @param metaIter An iterator that iterates over the
                      iterators to be combined.
  */
  public CombiningIterator(Iterator<Iterator<T>> metaIter) {
    this.metaIter = metaIter;
    curIter = null;
  }

  public CombiningIterator(Iterator<T>[] iters) {
    this(java.util.Arrays.asList(iters).iterator());
  }

  public boolean hasNext() {
    while ((curIter == null) || (!curIter.hasNext())) {
      if (metaIter.hasNext())
        curIter = metaIter.next();
      else
        return false;
    }
    return true;
  }

  public T next() throws NoSuchElementException {
    if (!hasNext())
      throw new NoSuchElementException();

    return curIter.next();
  }

  public void remove()
    throws IllegalStateException, UnsupportedOperationException
  {
    if (curIter == null)
      throw new IllegalStateException();
    else
      curIter.remove();
  }
}
