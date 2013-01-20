package ch.interlis.ili2c.metamodel;


public class ObjectPath extends Evaluable
{
	private Viewable root;
	private PathEl[] path;
	/** construct an explicitly specified objectpath.
	*/
	public ObjectPath(Viewable start,PathEl[] explicit)
	{
		root=start;
		path=explicit;
	}
	public Viewable getRoot(){
		return root;
	}
	/** Returns the viewable which is reached by following all elements of
	*   the object path.
	*/
	public Viewable getViewable()
	{
		return path[path.length-1].getViewable();
	}
	/** If this is an AttributePath, it returns the type of the attribute, which is reached
	*   by following all elements of
	*   the path.
	*/
	public Type getType()
	{
		PathEl last=path[path.length-1];
		if(!(last instanceof AbstractAttributeRef)){
			throw new IllegalArgumentException(Element.formatMessage(
				"err_objectPath_notAnAttributePath",toString()));
		}
		return ((AbstractAttributeRef)last).getDomain();
	}
	/** Returns the objectpath as string, as specified by the INTERLIS 2
	*   syntax.
	*/
	public String toString()
	{
		if(path==null){
			return "";
		}
		StringBuilder str=new StringBuilder();
		String sep="";
		for(int i=0;i<path.length;i++){
			str.append(sep);sep="->";
			str.append(path[i].getName());
		}
		return str.toString();
	}
        public PathEl[] getPathElements(){
          return path;
        }
}
