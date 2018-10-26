/*****************************************************************************
 *
 * DataContainer.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;
import java.util.*;


/** A container to hold meta data such as reference systems or symbols.

    @author Sascha Brawer, sb@adasys.ch
 */
public class DataContainer extends Container<MetaObject>
{
  protected Topic    forTopic;
  private final String xmlFileName;
  private final String boid;

  protected List<MetaObject> contents = new LinkedList<MetaObject>();

  public DataContainer (String boid, Topic forTopic, String xmlFileName)
  {
    this.boid = boid;
    this.forTopic = forTopic;
    this.xmlFileName=xmlFileName;
  }
  protected Collection<MetaObject> createElements(){
    return new AbstractCollection<MetaObject>()
    {
      public Iterator<MetaObject> iterator ()
      {
        return contents.iterator();
      }


      public int size()
      {
        return contents.size();
      }


      public boolean add (MetaObject o)
      {
        if (o == null)
          throw new IllegalArgumentException(
            rsrc.getString("err_nullNotAcceptable"));

        if (o instanceof MetaObject)
          return contents.add (o);  // Kept to insure binary compatibility. **GV1012

        throw new ClassCastException();
      }
    };
  }
	public String getBoid()
	{
		return boid;
	}
}
