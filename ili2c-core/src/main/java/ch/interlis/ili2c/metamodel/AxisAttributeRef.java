package ch.interlis.ili2c.metamodel;


  /** A class that designates an axis as part of
      an attribute path.
  */
public class AxisAttributeRef extends AbstractAttributeRef
{
    private int axis;
    private AttributeDef attr;
    public AxisAttributeRef(AttributeDef attr,int axis){
	    this.attr=attr;
	    this.axis=axis;
    }



    /** Returns the designated axis number.
    */
    public int getAxisNumber ()
    {
      return axis;
    }
	public AttributeDef getAttr(){
		return attr;
	}


    /** Returns a String consisting of an opening brace,
        the axis number and a closing brace, as it would
        appear in an INTERLIS-2 attribute path.
    */
    public String getName ()
    {
      return attr.getName()+"[" + axis + "]";
    }
    public Viewable getViewable(){
      return null;
    }
    public Type getDomain(){
      NumericalType[] dimv=((CoordType)attr.getDomain()).getDimensions();
      return dimv[axis];
    }
    @Override
    public Ili2cSemanticException checkTranslation(PathEl otherPathEl,int sourceLine) {
        Ili2cSemanticException ret=super.checkTranslation(otherPathEl,sourceLine);
        if(ret!=null) {
            return ret;
        }
        AxisAttributeRef other=(AxisAttributeRef)otherPathEl;
        if(!Element.equalElementRef(attr, other.attr)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),other.getName()));
        }
        if(axis!= other.axis) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_referencedAttributeMismatch",getName(),other.getName()));
        }
        return null;
    }
}


