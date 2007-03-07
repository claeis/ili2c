package ch.interlis.ili2c.metamodel;
import java.util.*;


public class MetaDataUseDef extends ExtendableContainer
{
  protected Collection createElements(){
    return null;
  }
  public void checkIntegrity(){
  }
	/** concrete basket available by this MetaDataUseDef
	*/
	private DataContainer     dataContainer;
	/** name of this model element
	*/
	private String name;
	/** True when SIGN objects.
	*/
	private boolean signData;
	/** Topic that describes this meta data.
	*/
	private Topic topic;


  /* Support for the Collection interface; delegates to the
     dataContainer member */
  public int size() { return (dataContainer!=null) ? dataContainer.size() : 0; }
  public boolean isEmpty() { return (dataContainer!=null) ?dataContainer.isEmpty() : true; }
  public boolean contains(Object o) { return (dataContainer!=null) ?dataContainer.contains(o):false; }
  public Iterator iterator() { return (dataContainer!=null) ? dataContainer.iterator(): new LinkedList().iterator(); }
  public Object[] toArray() { return dataContainer.toArray(); }
  public Object[] toArray(Object[] a) { return dataContainer.toArray(a); }


	public void setName(String v)
	{
		name=v;
	}
	public String getName()
	{
		return name;
	}
	public void setSignData(boolean sign)
	{
		this.signData=sign;
	}
	public boolean isSignData()
	{
		return signData;
	}
	public void setTopic(Topic topic)
	{
		this.topic=topic;
	}
	public Topic getTopic()
	{
		return topic;
	}
	public void setDataContainer(DataContainer concreteBasket)
	{
		dataContainer=concreteBasket;
	}
  public String getScopedName(Container scope) {
    Model enclosingModel, scopeModel;
    Topic enclosingTopic, scopeTopic;


    enclosingModel = (Model) getContainer (Model.class);
    enclosingTopic = (Topic) getContainer (Topic.class);


    if ((enclosingModel == null) && (enclosingTopic == null))
      return getName();


    if (scope != null)
    {
      scopeModel = (Model) scope.getContainerOrSame(Model.class);
      scopeTopic = (Topic) scope.getContainerOrSame(Topic.class);
    }
    else
    {
      scopeModel = null;
      scopeTopic = null;
    }


    if ((enclosingModel == scopeModel) && (enclosingTopic == null))
      return getName();


    if ((enclosingTopic == scopeTopic) && (enclosingTopic != null))
      return getName();


    if (enclosingTopic == null)
      return enclosingModel.getName() + "." + getName();
    else
      return enclosingTopic.getScopedName(scope) + "." + getName();
  }
  /** Returns a list with MetaObjects in imported DataContainers
      that are polymorphic equivalents to a given INTERLIS class
      and that have a specified name. If you do not care about
      the class of the MetaObjects, pass <code>null</code>.
  */
  public List findMatchingMetaObjects (Table polymorphicTo, String name)
  {
    List result = new LinkedList ();


    /* First try: Find the element in this container. */
      Iterator iter = iterator();
      while (iter.hasNext())
      {
        Object obj = iter.next();
        if (!(obj instanceof MetaObject))
          continue;


        MetaObject mo = (MetaObject) obj;
	//System.out.println(mo.getName());
	//System.out.println(mo.getTable());
	//System.out.println(polymorphicTo);
        if (((polymorphicTo != null) && mo.getTable().isExtending (polymorphicTo))
            && mo.getName().equals (name))
          result.add (mo);
      }

    /* Second try: Find the element in inherited containers.
    */
    if (extending != null && result.size()==0){
      return ((MetaDataUseDef)extending).findMatchingMetaObjects(polymorphicTo, name);
    }

    return result;
  }


}
