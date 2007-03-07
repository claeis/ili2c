/*****************************************************************************
 *
 * Topic.java
 * ----------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;



/** A topic. Please refer to the INTERLIS reference manual
    to learn about the topic concept in INTERLIS.


    @author Sascha Brawer, sb@adasys.ch
 */
public class Topic extends AbstractPatternDef
{
	/** is this a VIEW TOPIC
	*/
	private boolean viewTopic;
  protected String   name = "";
  private Domain oid=null;


  protected List     dependsOn = new LinkedList();


  public Topic()
  {
	  viewTopic=false;
  }



  public String getName ()
  {
    return name;
  }



  public String getScopedName(Container scope)
  {
    Model enclosingModel, scopeModel;


    enclosingModel = (Model) getContainer(Model.class);
    /* A topic which is not embeded in a model is weird, but possible
       due to using the JavaBeans component model which requires us to
       allow for an empty constructor. Therefore, a Topic object's
       lifetime includes a (typically short) period in which it
       has an empty bean context.
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



  public String toString()
  {
    return "TOPIC " + getScopedName(null);
  }



  /** Sets the value of the <code>name</code> property. Topics are
      identified and used by specifying their name.


      <p>Since the <code>name</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.


      @param name    The new name for this Topic.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of acceptable
                 INTERLIS names.


      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;


    checkNameSanity(name, /* empty ok? */ false);


    /* Make sure that the new name does not conflict
       with the name of another Topic, except the
       one that this object is extending directly.
    */
    checkNameUniqueness(newValue, Topic.class, (Topic) getRealExtending(),
      "err_topic_duplicateName");


    if (newValue.equals(oldValue))
      return;


    /* JavaBeans requires that the value be changed between
       firing VetoableChangeEvent and PropertyChangeEvent
       objects. */
    fireVetoableChange("name", oldValue, newValue);
    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }



  /** Causes this topic to extend another topic.


      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param extending  The new topic being extended, or
                        <code>null</code> if this topic is
                        going to be independent of other topics.


      @exception java.lang.IllegalArgumentException if
                 <code>extending</code> is a final element; final
                 elements can not be extended.


      @exception java.lang.IllegalArgumentException if
                 the resulting extension graph would contain
                 cycles. For instance, if <code>A</code> extends
                 <code>B</code> and <code>B</code> extends
                 <code>C</code>, the call <code>C.setExtending(A)</code>
                 would throw an exception.


      @exception java.lang.ClassCastException if <code>extending</code>
                 is neither <code>null</code> nor an instance of
                 the class <code>Topic</code>.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException
  {
    /* The cast below throws a ClassCastException
       if extending is neither null nor an instance of Topic.
       This is exactly what the API documentation specifies.
    */
    super.setExtending ((Topic) extending);
  }



  /** Makes this topic dependent on another topic. For a discussion
      about the concept of dependent topics, please consult
      the INTERLIS reference manual.


      @exception java.lang.IllegalArgumentException if
                 <code>dependee</code> is the same as this topic.


      @exception java.lang.IllegalArgumentException if
                 <code>dependee</code> is <code>null</code>.
  */
  public void makeDependentOn (Topic dependee)
  {
    if (dependee == this)
      throw new IllegalArgumentException (formatMessage ("err_topic_dependentOnSelf",
                                                         this.toString ()));


    if (dependee == null)
      throw new IllegalArgumentException();


    dependsOn.add(dependee);
  }



  /** Allows to find out on which topics this topic does depend.
      @see #makeDependentOn(ch.interlis.Topic)
  */
  public Iterator getDependentOn()
  {
    return dependsOn.iterator();
  }



  public boolean isDependentOn (Element other)
  {
    if ((other instanceof Topic) && dependsOn.contains (other))
      return true;


    return super.isDependentOn (other);
  }



  boolean containsConcreteExtensionOfTable (Table abstractTable)
  {
    if (!abstractTable.isAbstract())
      throw new IllegalArgumentException ();


    Iterator iter = iterator();
    while (iter.hasNext())
    {
      Object obj = iter.next();
      if (obj instanceof Table)
      {
        Table tab = (Table) obj;
        if (!tab.isAbstract() && tab.isExtending (abstractTable))
          return true;
      }
    }


    if (extending != null)
      return ((Topic) extending).containsConcreteExtensionOfTable (abstractTable);


    return false;
  }
  public void setViewTopic(boolean v)
  {
	  viewTopic=v;
  }
  public boolean isViewTopic()
  {
	  return viewTopic;
  }
  public void setOid(Domain type)
  {
	  this.oid=type;
  }
  public Domain getOid()
  {
	  return oid;
  }

}