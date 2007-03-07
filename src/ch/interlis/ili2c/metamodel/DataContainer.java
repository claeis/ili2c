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
public class DataContainer extends Container
{
  protected Topic    forTopic;
  private String xmlFileName;
  private String boid;

  protected List     contents = new LinkedList();

  public DataContainer (String boid, Topic forTopic, String xmlFileName)
  {
    this.boid = boid;
    this.forTopic = forTopic;
    this.xmlFileName=xmlFileName;
  }
  protected Collection createElements(){
    return new AbstractCollection()
    {
      public Iterator iterator ()
      {
        return contents.iterator();
      }


      public int size()
      {
        return contents.size();
      }


      public boolean add (Object o)
      {
        if (o == null)
          throw new IllegalArgumentException(
            rsrc.getString("err_nullNotAcceptable"));

        if (o instanceof MetaObject)
          return contents.add (o);

        throw new ClassCastException();
      }
    };
  }
	public String getBoid()
	{
		return boid;
	}
}
