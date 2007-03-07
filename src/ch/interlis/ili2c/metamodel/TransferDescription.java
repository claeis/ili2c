/*****************************************************************************
 *
 * TransferDescription.java
 * ------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;


/** The basic container that holds all the models encountered during a parse;
*/
public class TransferDescription extends Container
{
  protected String   name = "";


  /** The models in this transfer description. */
  protected List      contents = new LinkedList();


	private HashMap metaDataBaskets=new HashMap(); // Map<String boid,DataContainer basket>
	private HashMap basketname2boid=new HashMap(); // Map<String qualifiedBasketName,String boid>


  public final PredefinedModel INTERLIS = new PredefinedModel ();


  public TransferDescription ()
  {
    // add predefined model
    add (INTERLIS);
    INTERLIS.setupModel();
  }
   protected Collection createElements(){
    return new AbstractCollection() {
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
        if (o instanceof Model)
        {
          Model model = (Model) o;
          Model conflicting = (Model) getElement (Model.class, model.getName());
          if (conflicting != null)
            throw new IllegalArgumentException (formatMessage (
              "err_model_duplicateName",
              conflicting.toString ()));


          return contents.add(o);
        }


        throw new ClassCastException();
      }
    };


  }



  public String getName ()
  {
    return name;
  }



  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;


    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }



  public String toString ()
  {
    return "TRANSFER " + getName ();
  }


  public Iterator iteratorMetaDataContainer()
  {
	return metaDataBaskets.values().iterator();
  }
  public void addMetaDataContainer(DataContainer concreteBasket)
  {
	metaDataBaskets.put(concreteBasket.getBoid(),concreteBasket);
  }
  public void addMetadataMapping(String qualifiedBasketName,String boid)
  {
	basketname2boid.put(qualifiedBasketName,boid);
  }
  public HashMap getBasketname2boid()
  {
  	return new HashMap(basketname2boid);
  }
  /** @returns null if no basket known
   *
   */
  public DataContainer getMetaDataContainer(String qualifiedBasketName)
  {
	String boid=(String)basketname2boid.get(qualifiedBasketName);
	if(boid==null){
		return null;
	}
	return (DataContainer)metaDataBaskets.get(boid);
  }
  private Ili1Format ili1Format=null;
  public Ili1Format getIli1Format(){
  	return ili1Format;
  }
  public void setIli1Format(Ili1Format ili1Format1){
	ili1Format=ili1Format1;
  }

}
