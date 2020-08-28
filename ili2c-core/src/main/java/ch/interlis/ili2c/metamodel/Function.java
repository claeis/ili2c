/*****************************************************************************
 *
 * Function.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** A Function declaration, as expressed by the FUNCTION construct
    in INTERLIS-2.

    @author <a href="sb@adasys.ch">Sascha Brawer</a>, Adasys AG, CH-8006 Zurich
    @version June 30, 1999
*/
public class Function extends AbstractLeafElement
{
    private static final FormalArgument[] NO_ARGS = new FormalArgument[0];
  protected String name = null;
  protected Type domain = null;
  protected FormalArgument[] arguments = NO_ARGS;
  protected String explanation = null;

  /** Constructs a new Function. Before you can add a new function
      to a Model, you have to set its name, arguments and domain.
      Otherwise, the model will reject the function being added.
  */
  public Function()
  {
  }


  /** Determines the current value of the <code>name</code> property.
      Functions are identified and used by specifying their name.

      @see #setName(java.lang.String)
  */
  public String getName()
  {
    return name;
  }


  /** Sets the value of the <code>name</code> property.
      Functions are identified and used by specifying their name.

      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this Function.

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
  public void setName(String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    checkNameSanity(name, /* empty names acceptable? */ false);
    checkNameUniqueness(name, Function.class, null,
      "err_duplicateFunctionName");

    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }

  public String toString ()
  {
    return "FUNCTION " + getScopedName (null);
  }


  /** Determines the value of the <code>explanation</code> property.
      Each Function can optionally be provided with an explanation
      describing what it does and how it works. These explanations are
      not interpreted by the processor. Instead, they allow for
      semi-formalized documentation.
  */
  public String getExplanation()
  {
    return explanation;
  }


  /** Sets the value of the <code>explanation</code> property.
      Each Function can optionally be provided with an explanation
      describing what it does and how it works. These explanations are
      not interpreted by the processor. Instead, they allow for
      semi-formalized documentation.

      <p>Since the <code>explanation</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.

      @param explanation    The new explanation. Pass <code>null</code> to
                            delete the explanation of a Function; the
                            generators will then declare the function
                            without any explanation at all.

      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>explanation</code> property
                 and does not agree with the change.
  */
  public void setExplanation(String explanation)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.explanation;
    String newValue = explanation;

    fireVetoableChange("explanation", oldValue, newValue);
    this.explanation = newValue;
    firePropertyChange("explanation", oldValue, newValue);
  }


  public FormalArgument[] getArguments ()
  {
    return arguments;
  }
  /** finds a formal argument.
   * @param name of formal argument
   * @return formal argument or null if none with given name
   */
  public FormalArgument getArgument(String name){
	  for(int argi=0;argi<arguments.length;argi++){
		  if(arguments[argi].getName().equals(name)){
			  return arguments[argi];
		  }
	  }
	  return null;
  }
  public void setArguments (FormalArgument[] arguments)
    throws java.beans.PropertyVetoException
  {
    FormalArgument[] oldValue = this.arguments;
    FormalArgument[] newValue = arguments;

    if (oldValue == newValue)
      return;

    fireVetoableChange ("arguments", oldValue, newValue);
    this.arguments = newValue;
    if(newValue!=null){
        for(int i=0;i<newValue.length;i++){
        	newValue[i].setFunction(this);
        }
    }
    if(oldValue!=null){
        for(int i=0;i<oldValue.length;i++){
        	oldValue[i].setFunction(this);
        }
    }
    firePropertyChange ("arguments", oldValue, newValue);
  }


  /** Determines the current value of the <code>domain</code> property.
      The declaration of INTERLIS Functions includes their <em>domain</em>,
      or (as it is called in some programming languages)
      <em>return type</em>.
  */
  public Type getDomain()
  {
    return domain;
  }


  /** Sets the value of the <code>domain</code> property.
      The declaration of INTERLIS Functions includes their <em>domain</em>,
      or (as it is called in some programming languages)
      <em>return type</em>.

      <p>Since the <code>domain</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.

      @param domain   The new domain for this Function.

      @exception java.lang.IllegalArgumentException if <code>domain</code>
                 is <code>null</code>.

      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>domain</code> property
                 and does not agree with the change.
  */
  public void setDomain (Type domain)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.domain;
    Type newValue = domain;

    if (newValue == null)
      throw new IllegalArgumentException("null is not acceptable as "
        + "domain of a FUNCTION");

    fireVetoableChange ("domain", oldValue, newValue);
    this.domain = newValue;
    firePropertyChange ("domain", oldValue, newValue);
  }

}
