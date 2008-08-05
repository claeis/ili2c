package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OBJECT OF ..' function argument
 * and the implicit particles (base-viewables) of vies
 *
 */
public class ObjectType extends Type
{
	private Viewable ref;
	private boolean objects=false;
	private java.util.ArrayList restrictedTo=new java.util.ArrayList();
	public ObjectType(Viewable ref)
	{
		this.ref=ref;
	}
	public ObjectType(Viewable ref,boolean objects)
	{
		this.ref=ref;
		this.objects=objects;
	}
        public Viewable getRef()
        {
          return ref;
        }
		public boolean isObjects() {
			return objects;
		}
		  public void addRestrictedTo(AbstractClassDef classOrAssociation)
		  {
			  restrictedTo.add(classOrAssociation);
		  }
		  public java.util.Iterator iteratorRestrictedTo()
		  {
		  	return restrictedTo.iterator();
		  }
}


