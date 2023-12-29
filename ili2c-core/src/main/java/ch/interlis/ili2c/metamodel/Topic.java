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
public class Topic extends AbstractPatternDef<Element>
{
	/** is this a VIEW TOPIC
	*/
	private boolean viewTopic;
  protected String   name = "";
  private Domain oid=null;
  private Domain basketOid=null;
  private ArrayList<Domain> deferredGenerics=new ArrayList<Domain>();

  protected List<Topic> dependsOn = new LinkedList<Topic>();


  public Topic()
  {
	  viewTopic=false;
  }



  public String getName ()
  {
    return name;
  }


  @Override
  public String getScopedName(Container<?> scope)
  {
    Model enclosingModel, scopeModel;


    enclosingModel = (Model) getContainer(Model.class);
    /* A topic which is not embedded in a model is weird, but possible
       due to using the JavaBeans component model which requires us to
       allow for an empty constructor. Therefore, a Topic object's
       lifetime includes a (typically short) period in which it
       has an empty bean context.
    */
    if (enclosingModel == null) {
        return getName();
    }


    if (scope != null) {
        scopeModel = (Model) scope.getContainerOrSame(Model.class);
    } else {
        scopeModel = null;
    }


    if (enclosingModel == scopeModel) {
        return getName();
    } else {
        return enclosingModel.getName() + "." + getName();
    }
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
    checkNameUniqueness(newValue, Topic.class, getRealExtending(),
      "err_topic_duplicateName");


    if (newValue.equals(oldValue)) {
        return;
    }


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
    super.setExtending (extending);
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
    if (dependee == this) {
        throw new IllegalArgumentException (formatMessage ("err_topic_dependentOnSelf",
                                                             this.toString ()));
    }


    if (dependee == null) {
        throw new IllegalArgumentException();
    }


    dependsOn.add(dependee);
  }



  /** Allows to find out on which topics this topic does depend.
      @see #makeDependentOn(ch.interlis.Topic)
  */
  public Iterator<Topic> getDependentOn()
  {
    return dependsOn.iterator();
  }



  public boolean isDependentOn (Element other)
  {
    if ((other instanceof Topic) && dependsOn.contains (other)) {
        return true;
    }


    return super.isDependentOn (other);
  }



  boolean containsConcreteExtensionOfTable (Table abstractTable)
  {
    Iterator<Viewable<?>> iter = getViewables().iterator();
    while (iter.hasNext())
    {
        Element obj = iter.next();
      if (obj instanceof Table)
      {
        Table tab = (Table) obj;
        if (!tab.isAbstract() && tab.isExtending (abstractTable)) {
            return true;
        }
      }
    }
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
  public void setBasketOid(Domain type)
  {
	  this.basketOid=type;
  }
  public Domain getBasketOid()
  {
	  return basketOid;
  }
  public void addDeferredGeneric(Domain generic)
  {
      deferredGenerics.add(generic);
  }
  public Domain[] getDefferedGenerics()
  {
      return deferredGenerics.toArray(new Domain[deferredGenerics.size()]);
  }



@Override
public void checkIntegrity(List<Ili2cSemanticException> errs) throws IllegalStateException {
    super.checkIntegrity(errs);
    checkIntegrityAbstract(errs);
}



private void checkIntegrityAbstract(List<Ili2cSemanticException> errs) {
    if(isAbstract()) {
        return;
    }
    Iterator<Viewable<?>> iter = getViewables().iterator();
    while (iter.hasNext())
    {
        Element obj = iter.next();
          if (obj instanceof Table)
          {
            Table tab = (Table) obj;
            if (tab.isIdentifiable() && tab.isAbstract()) {
                // check that there is a concrete extension in this topic
                if(!containsConcreteExtensionOfTable(tab)) {
                    errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_topic_abstractElement",getScopedName(),tab.getName())));
                }
            }
          }
    }

    Set<Domain> usedGenericDomains = getUsedGenericDomains(this);
    
    // check whether all deferred generics have a context definition
    Model model = (Model) getContainer(Model.class);
    for (Domain domain : deferredGenerics) {
        Domain[] resolved = model.resolveGenericDomain(domain);
        if (resolved == null) {
            errs.add(new Ili2cSemanticException(getSourceLine(), formatMessage("err_topic_deferredGenericMissingContext", getScopedName(), domain.getName())));
        }

        if (!usedGenericDomains.contains(domain)) {
            errs.add(new Ili2cSemanticException(getSourceLine(), formatMessage("err_topic_deferredGenericUnused", getScopedName(), domain.getName())));
        }
    }

    for (Domain domain : usedGenericDomains) {
        Domain[] resolved = model.resolveGenericDomain(domain);
        if (resolved == null) {
            errs.add(new Ili2cSemanticException(getSourceLine(), formatMessage("err_topic_genericMissingContext", getScopedName(), domain.toString())));
        } else if (resolved.length > 1 && !deferredGenerics.contains(domain)) {
            errs.add(new Ili2cSemanticException(getSourceLine(), formatMessage("err_topic_deferredGenericMissing", getScopedName(), domain.getName())));
        }
    }
}


private Set<Domain> getUsedGenericDomains(Topic topic) {
    HashSet<Element> accu=new HashSet<Element>();
    HashSet<Model> accuScope=new HashSet<Model>();
    visitTopic(accu,accuScope,topic);
    for(Model m:accuScope) {
        visitModel(accu,accuScope,m);
    }

    Set<Domain> ret=new HashSet<Domain>();
    for(Element e:accu) {
        if(e instanceof AttributeDef) {
            AttributeDef attr=(AttributeDef)e;
            Type type=attr.getDomain();
            if(type instanceof TypeAlias) {
                Domain domain=((TypeAlias)type).getAliasing();
                type=domain.getType();
                if(type instanceof AbstractCoordType && ((AbstractCoordType) type).isGeneric()) {
                    ret.add(domain);
                }else if(type instanceof LineType) {
                    domain=((LineType)type).getControlPointDomain();
                    type=domain.getType();
                    if(type instanceof AbstractCoordType && ((AbstractCoordType) type).isGeneric()) {
                        ret.add(domain);
                    }
                }
            }else if(type instanceof AbstractCoordType && ((AbstractCoordType) type).isGeneric()) {
                throw new IllegalStateException("unexpected GENERIC CoordType at attr "+attr.getScopedName());
            }else if(type instanceof LineType) {
                Domain domain=((LineType)type).getControlPointDomain();
                type=domain.getType();
                if(type instanceof AbstractCoordType && ((AbstractCoordType) type).isGeneric()) {
                    ret.add(domain);
                }
            }
        }
    }
    return ret;
}
private void visitModel(HashSet<Element> accu,HashSet<Model> accuScope, Model model)
{
    if(model.equals(PredefinedModel.getInstance())){
        return;
    }
    Iterator topici=model.iterator();
    while(topici.hasNext()){
        Object tObj=topici.next();
        if(tObj instanceof Viewable){
            visitViewable(accu,accuScope,(Viewable)tObj);
        }
    }
}
private void visitTopic(HashSet<Element> visitedElements,HashSet<Model> accuScope,Topic def)
{
    if(visitedElements.contains(def)){
        return;
    }
    visitedElements.add(def);
    Iterator classi=def.iterator();
    while(classi.hasNext()){
        Object classo=classi.next();
        if(classo instanceof Viewable){
            visitViewable(visitedElements,accuScope,(Viewable)classo);
        }
    }
    // base topic
    Topic base=(Topic)def.getExtending();
    if(base!=null){
        visitTopic(visitedElements,accuScope,base);
    }
    
    // parent model
    Model model=(Model)def.getContainer(Model.class);
    if(!accuScope.contains(model)){
        accuScope.add(model);
    }
}
private void visitViewable(HashSet<Element> visitedElements,HashSet<Model> accuScope,Viewable def)
{
    if(visitedElements.contains(def)){
        return;
    }
    visitedElements.add(def);
    
    //  generateViewable(def);
    Iterator attri=def.iterator();
    while(attri.hasNext()){
        Object attro=attri.next();
        if(attro instanceof AttributeDef){
            AttributeDef attr=(AttributeDef)attro;
            if(!visitedElements.contains(attr)) {
                visitedElements.add(attr);
            }
            Type type=attr.getDomain();
            if(type instanceof CompositionType){
                CompositionType compType=(CompositionType)type;
                visitViewable(visitedElements,accuScope,compType.getComponentType());
                Iterator resti=compType.iteratorRestrictedTo();
                while(resti.hasNext()){
                    Viewable rest=(Viewable)resti.next();
                    visitViewable(visitedElements,accuScope,rest);
                }
            }
        }
    }
    // base viewable
    Viewable base=(Viewable)def.getExtending();
    if(base!=null){
        visitViewable(visitedElements,accuScope,base);
    }
}



@Override
public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  throws java.lang.IllegalStateException
{
    super.checkTranslationOf(errs,name,baseName);
    Topic baseElement=(Topic)getTranslationOf();
    if(baseElement==null) {
        return;
    }
    
    if(isAbstract()!=baseElement.isAbstract()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInAbstractness",getScopedName(),baseElement.getScopedName())));
    }
    if(isFinal()!=baseElement.isFinal()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInFinality",getScopedName(),baseElement.getScopedName())));
    }
    if(isViewTopic()!=baseElement.isViewTopic()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchViewTopic",getScopedName(),baseElement.getScopedName())));
    }
    Ili2cSemanticException err=null;
    err=checkElementRef(getBasketOid(),baseElement.getBasketOid(),getSourceLine(),"err_diff_bidMismatch");
    if(err!=null) {
        errs.add(err);
    }
    err=checkElementRef(getOid(),baseElement.getOid(),getSourceLine(),"err_diff_oidMismatch");
    if(err!=null) {
        errs.add(err);
    }
    err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseTopicMismatch");
    if(err!=null) {
        errs.add(err);
    }
    if(dependsOn.size()!=baseElement.dependsOn.size()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_dependencyTopicMismatch")));
    }
    ArrayList<Topic> depTopics=new ArrayList<Topic>(dependsOn);
    Collections.sort(depTopics,new TranslatedElementNameComparator());
    ArrayList<Topic> baseDepTopics=new ArrayList<Topic>(baseElement.dependsOn);
    Collections.sort(baseDepTopics,new TranslatedElementNameComparator());

    for(int depi=0;depi<depTopics.size();depi++) {
        Topic dep=depTopics.get(depi);
        Topic baseDep=baseDepTopics.get(depi);
        err=checkElementRef(dep,baseDep,getSourceLine(),"err_diff_dependencyTopicMismatch");
        if(err!=null) {
            errs.add(err);
        }
    }
}
}
