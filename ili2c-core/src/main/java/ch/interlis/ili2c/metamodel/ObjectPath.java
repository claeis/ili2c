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
    @Override
    public boolean isLogical() {
        if(isAttributePath() && getType().isBoolean()) {
            return true;
        }
        return false;
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
	@Override
	public Type getType()
	{
		PathEl last=getLastPathEl();
		if(last instanceof AbstractAttributeRef){
	        return ((AbstractAttributeRef)last).getDomain();
		}
		return new ObjectType(last.getViewable());
	}
    public boolean isAttributePath()
    {
        PathEl last=getLastPathEl();
        if(last instanceof AbstractAttributeRef){
            return true;
        }
        return false;
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
        public PathEl getLastPathEl(){
            return path[path.length-1];
          }
        @Override
        public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
        {
            Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
            if(ret!=null) {
                return ret;
            }
            ObjectPath other=(ObjectPath)otherEv;
            if(!Element.equalElementRef(getRoot(), other.getRoot())){
                return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_objectPathMismatch"));
            }
            if(path.length!=other.path.length) {
                return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_objectPathMismatch"));
            }
            for(int pathi=0;pathi<path.length;pathi++) {
                if(path[pathi].getClass()!=other.path[pathi].getClass()) {
                    return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_objectPathMismatch"));
                }
                ret=path[pathi].checkTranslation(other.path[pathi],sourceLine);
                if(ret!=null) {
                    return ret;
                }
            }
            return null;
        }
        
}
