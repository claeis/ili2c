package ch.interlis.ili2c.metamodel;
import java.util.*;


public class MetaDataUseDef extends ExtendableContainer<MetaObject>
{
  protected Collection<MetaObject> createElements(){
    return new ArrayList<MetaObject>();
  }
  @Override
  public void checkIntegrity(List<Ili2cSemanticException> errs){
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
		// create proxies for real objects
		Iterator moi=dataContainer.iterator();
		while(moi.hasNext()){
			Object moo=moi.next();
			if(moo instanceof MetaObject){
				MetaObject mo=(MetaObject)moo;
				MetaObject moProxy=new MetaObject(mo.getName(),mo.getTable());
				add(moProxy);
			}
		}
	}
	public DataContainer getDataContainer()
	{
		return dataContainer;
	}
  /** Returns a list with MetaObjects in imported DataContainers
      that are polymorphic equivalents to a given INTERLIS class
      and that have a specified name. If you do not care about
      the class of the MetaObjects, pass <code>null</code>.
  */
  public List<MetaObject> findMatchingMetaObjects (Table polymorphicTo, String name)
  {
    List<MetaObject> result = new LinkedList<MetaObject>();

    /* First try: Find the element in this container. */
      Iterator<MetaObject> iter = iterator();
      while (iter.hasNext())
      {
        MetaObject mo = iter.next();
	//System.out.println(mo.getName());
	//System.out.println(mo.getTable());
	//System.out.println(polymorphicTo);
        if (((polymorphicTo != null) && mo.getTable().isExtending (polymorphicTo))
            && mo.getName().equals (name))
          result.add (mo);
      }

    /* Second try: Find the element in inherited containers.
    */
    if (extending != null && result.isEmpty()){
      return ((MetaDataUseDef)extending).findMatchingMetaObjects(polymorphicTo, name);
    }

    return result;
  }


}
