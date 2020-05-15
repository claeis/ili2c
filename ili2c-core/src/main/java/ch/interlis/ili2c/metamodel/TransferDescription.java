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

import ch.ehi.basics.settings.Settings;


/** The basic container that holds all the models encountered during a parse;
*/
public class TransferDescription extends Container<Model>
{
  private static String version = null;
    
  protected String   name = "";
  private Map<String,Element> name2ele=null;


  /** The models in this transfer description. */
  protected List<Model> contents = new LinkedList<Model>();


	private HashMap<String, DataContainer> metaDataBaskets = new HashMap<String, DataContainer>(); // Map<String boid,DataContainer basket>
	private HashMap<String, String> basketname2boid = new HashMap<String, String>(); // Map<String qualifiedBasketName,String boid>


  public final PredefinedModel INTERLIS = PredefinedModel.getInstance();


  public TransferDescription ()
  {
    // add predefined model
    add (INTERLIS);
  }
   protected Collection<Model> createElements(){
    return new AbstractCollection<Model>() {
      public Iterator<Model> iterator()
      {
        return contents.iterator();
      }



      public int size()
      {
        return contents.size();
      }



      public boolean add(Model o)
      {
        if (o instanceof Model) {  // Kept to preserve binary compatibility with pre-1.5.
          Model model = o;
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


  public Iterator<DataContainer> iteratorMetaDataContainer()
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
  public HashMap<String, String> getBasketname2boid()
  {
  	return (HashMap<String, String>) basketname2boid.clone();
  }
  /** @returns null if no basket known
   *
   */
  public DataContainer getMetaDataContainer(String qualifiedBasketName)
  {
	String boid=basketname2boid.get(qualifiedBasketName);
	if(boid==null){
		return null;
	}
	return metaDataBaskets.get(boid);
  }
  private Ili1Format ili1Format=null;
public static final String MIMETYPE_ITF = "application/interlis;version=1.0";
public static final String MIMETYPE_XTF = "application/interlis+xml;version=2.3";
  public Ili1Format getIli1Format(){
  	return ili1Format;
  }
  public void setIli1Format(Ili1Format ili1Format1){
	ili1Format=ili1Format1;
  }
  public Model getLastModel()
  {
  	Model mainModel = null;
	Iterator<Model> modeli = iterator();
	while (modeli.hasNext()) {
	    mainModel = modeli.next();
	}
	return mainModel;
  }
  public Model[] getModelsFromLastFile()
  {
    String mainModelFileName = getLastModel().getFileName();
    if(mainModelFileName==null) {
        return new Model[0];
    }
    ArrayList<Model> models=new ArrayList<Model>();
    Iterator<Model> modeli = iterator();
    while (modeli.hasNext()) {
        Model model = modeli.next();
        if(mainModelFileName.equals((model.getFileName()))) {
            models.add(model);
        }
    }
    return models.toArray(new Model[models.size()]);
  }
  public Element getElement(String scopedName) {
      if(name2ele==null) {
          name2ele=new HashMap<String,Element>();
          Iterator transIter = iterator();
          while(transIter.hasNext()){
              Element transElem=(Element)transIter.next();
              visitElement(transElem);
          }
      }
      return name2ele.get(scopedName);
  }
  private void visitElement(Element el)
  {
      String scopedName=el.getScopedName();
      name2ele.put(scopedName, el);
      if(el instanceof Container){
          Container c = (Container) el;
          Iterator it = c.iterator();
          while(it.hasNext()){
              visitElement((Element)it.next());
          }
      }
  }
  public static String getVersion() {
        if (version == null) {
            java.util.ResourceBundle resVersion = java.util.ResourceBundle.getBundle("ch.interlis.ili2c.Version");
            StringBuilder ret = new StringBuilder(20);
            ret.append(resVersion.getString("version"));
            ret.append('-');
            ret.append(resVersion.getString("versionCommit"));
            version = ret.toString();
        }
        return version;
    }
  
}
