package ch.interlis.ili2c.metamodel;


/** A line form declaration, as expressed by the LINE FORM construct
    in INTERLIS-2.
*/
public class LineForm extends AbstractLeafElement
{
  private String name = null;
  private String explanation = null;
  private Table segmentStructure=null;

  /** Constructs a new LineForm. Before you can add a new LineForm
      to a Model, you have to set its name.
      Otherwise, the model will reject the LineForm being added.
  */
  public LineForm ()
  {
  }


  public LineForm (String name)
  {
    this();
    checkNameSanity(name, /* empty names acceptable? */ false);
    this.name = name;
  }


  public String toString ()
  {
    return "LINE FORM " + getScopedName (null);
  }



  /** Returns a dot-separated name sequence which correctly
      designates this line form in a specified name space.

      @param scope The naming context in question. If you
                   pass <code>null</code>, a fully scoped
                   name is returned.
  */
  public String getScopedName (Container scope)
  {
    Model enclosingModel, scopeModel;

    enclosingModel = (Model) getContainer(Model.class);
    /* A line form which is not embeded in a model is weird, but possible
       due to using the JavaBeans component model which requires us to
       allow for an empty constructor.
    */
    if (enclosingModel == null)
      return getName();

    if (scope != null)
      scopeModel = (Model) scope.getContainerOrSame(Model.class);
    else
      scopeModel = null;

    if (enclosingModel == scopeModel)
      return getName();
    else
      return enclosingModel.getName() + "." + getName();
  }


  /** Determines the current value of the <code>name</code> property.
      Line forms are identified and used by specifying their name.

      @see #setName(java.lang.String)
  */
  public String getName()
  {
    return name;
  }



  /** Sets the value of the <code>name</code> property.
      Line forms are identified and used by specifying their name.


      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this LineForm.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.


      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another function.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    checkNameSanity(name, /* empty names acceptable? */ false);
    checkNameUniqueness(name, LineForm.class, null,
      "err_lineForm_duplicateName");


    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }



  /** Determines the value of the <code>explanation</code> property.
      Each line form can optionally be provided with an explanation
      describing what it is. These explanations are
      not interpreted by the processor. Instead, they allow for
      semi-formalized documentation.
  */
  public String getExplanation()
  {
    return explanation;
  }
  public void setExplanation(String explanation)
  {
    this.explanation=explanation;
  }

  public void setSegmentStructure(Table segmentStructure)
  {
	this.segmentStructure=segmentStructure;
  }
  public Table getSegmentStructure()
  {
	return segmentStructure;
  }


}
