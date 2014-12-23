package ch.interlis.ili2c.metamodel;

import java.util.ArrayList;
import java.util.Iterator;


/** a type used to describe an 'OBJECT OF ..' function argument
 * and the implicit particles (base-viewables) of vies
 *
 */
public class ObjectType extends Type
{
	private final Viewable<?> ref;
	private boolean objects=false;
	private ArrayList<AbstractClassDef<?>> restrictedTo = new ArrayList<AbstractClassDef<?>>();
	private boolean allOf=false;

	
	public ObjectType(Viewable<?> ref)
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
		  public void addRestrictedTo(AbstractClassDef<?> classOrAssociation)
		  {
			  restrictedTo.add(classOrAssociation);
		  }
		  public Iterator<AbstractClassDef<?>> iteratorRestrictedTo()
		  {
		  	return restrictedTo.iterator();
		  }
		  void checkTypeExtension (Type wantToExtend)
		  {
		    if ((wantToExtend == null)
		      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
		      return;
		    if (!(wantToExtend.getClass().equals(this.getClass()))){
		        throw new Ili2cSemanticException (rsrc.getString (
		        "err_type_ExtOther"));
		    }
		  }


    public ObjectType clone() {
        ObjectType cloned = (ObjectType) super.clone();

        cloned.restrictedTo = (ArrayList<AbstractClassDef<?>>) restrictedTo.clone();
        return cloned;
    }
    /** true if this is the type of an ALL OF construct in a ViewDef
     */
	public boolean isAllOf() {
		return allOf;
	}
	public void setAllOf(boolean allOf) {
		this.allOf = allOf;
	}

}


