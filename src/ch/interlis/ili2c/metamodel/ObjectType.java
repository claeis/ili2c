package ch.interlis.ili2c.metamodel;


/** a type used to describe an 'OBJECT OF ..' function argument
 * and the implicit particles (base-viewables) of vies
 *
 */
public class ObjectType extends Type
{
	private Viewable ref;
	private boolean objects=false;
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
}


