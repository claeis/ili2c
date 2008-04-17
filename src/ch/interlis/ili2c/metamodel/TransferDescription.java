/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


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
            throw new Ili2cSemanticException(model.getSourceLine(),formatMessage (
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
  public Model getLastModel()
  {
  	Model mainModel=null;
	Iterator modeli=iterator();
	while(modeli.hasNext()){
		Object modelo=modeli.next();
		if(modelo instanceof Model){
			mainModel=(Model)modelo;
		}
	}
	return mainModel;
  }

}
