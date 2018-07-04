package ch.interlis.ili2c.metamodel;


import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;


/** An abstract class that groups together all constructs
    that can form the base of a view.
*/
public abstract class Viewable<E extends Element> extends ExtendableContainer<E>
{
  protected String   name = "";
  protected List<LocalAttribute> attributes = new ArrayList<LocalAttribute>();
  protected List<Constraint> constraints = new LinkedList<Constraint>();


  /** Returns the value of the <code>name</code> property
      which indicates the name of this Viewable without any scope
      prefixes.


      @see #getScopedName(ch.interlis.Container)
  */
  public String getName ()
  {
  	if(isAlias()){
  		return getAlias();
  	}else{
	    return name;
  	}
  }



  /** Sets the value of the <code>name</code> property.
      Viewables are identified and used by specifying their name.


      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param name The new name for this Viewable.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.


      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another view or table. The
                 only acceptable conflict is with the view that
                 this view extends directly.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
   	if(isAlias()){
  		setAlias(name);
  		return;
  	}else{
	    String oldValue = this.name;
	    String newValue = name;


	    /* Make sure that the new name is not null, empty,
	       too long, etc. */
	    checkNameSanity(newValue, /* empty ok? */ false);


	    /* Make sure that the new name does not conflict
	       with the name of another Viewable, except the
	       one that this object is extending directly.
	    */
	    checkNameUniqueness(newValue, Viewable.class, getRealExtending(),
	      "err_duplicateViewName");


	    /* JavaBeans requires that the value be changed between
	       firing VetoableChangeEvent and PropertyChangeEvent
	       objects. */
	    fireVetoableChange ("name", oldValue, newValue);
	    this.name = newValue;
	    firePropertyChange ("name", oldValue, newValue);
  	}
  }



  /** Returns a dot-separated name sequence which correctly
      designates this Viewable in a specified name space.


      @param scope The naming context in question. If you
                   pass <code>null</code>, a fully scoped
                   name is returned.
  */
  public String getScopedName (Container scope)
  {
	    Model  enclosingModel, scopeModel;
	    AbstractPatternDef  enclosingTopic, scopeTopic;


	    enclosingModel = (Model) getContainer(Model.class);
	    enclosingTopic = (AbstractPatternDef) getContainer(AbstractPatternDef.class);


	    /* A Viewable which is not embeded in a model is weird, but possible
	       due to using the JavaBeans component model which requires us to
	       allow for an empty constructor. Therefore, a Table object's
	       lifetime includes a (typically short) period in which it
	       has an empty bean context.
	    */
	    if (enclosingModel == null) {
            return getName();
        }


	    if (scope != null)
	    {
	      scopeModel = (Model) scope.getContainerOrSame(Model.class);
	      scopeTopic = (AbstractPatternDef) scope.getContainerOrSame(AbstractPatternDef.class);
	    }
	    else
	    {
	      scopeModel = null;
	      scopeTopic = null;
	    }


	    if ((enclosingModel == scopeModel) && (enclosingTopic == scopeTopic)) {
            return getName();
        }


	    if (enclosingTopic == null) {
            return enclosingModel.getName() + "." + getName();
        } else {
            return enclosingModel.getName()
                + "." + enclosingTopic.getName()
                + "." + getName();
        }
  }

  public Iterator<LocalAttribute> getDefinedAttributes()
  {
	  return attributes.iterator();
  }



  /** Walks the inheritance hierarchy to find out about all the
      attributes that are defined for this Viewable. Inherited
      attributes are part of the result, but only those that are
      not overwritten.


      <p>The iterator will return the attributes in the order
      of their first definition. In the example below, the iterator
      returned by this call would first return <code>B:p</code>,
      then <code>A:q</code>, then <code>B:r</code>.


<p><code><pre>TABLE A =
  p: NUMERIC;
  q: NUMERIC;
END A;


TABLE B EXTENDS A =
  r: NUMERIC;
  p (EXTENDED): NUMERIC;
END B;<br></pre></code>


      @return An iterator for all the attributes of this Viewable.
  */
  public Iterator<Extendable> getAttributes()
  {
   	if(isAlias()){
  		return ((Viewable)getReal()).getAttributes();
  	}else{
	    List<Extendable> result = new LinkedList<Extendable> ();
	    Map<Extendable, Extendable>  mostDerived = new HashMap<Extendable, Extendable> ();
	    for (Viewable<E> v = this; v != null; v = (Viewable<E>) v.getRealExtending())
	    {
	      List<Extendable> attrsOfV_reversed = new LinkedList<Extendable> (); /* a stack */
	      Iterator<E> iter = v.iterator ();
	      while (iter.hasNext ())
	      {
	        Object obj = iter.next();
	        if (obj instanceof AttributeDef)
	        {
	          Extendable attr = (Extendable) obj;
	          /* Find least derived; put it into the map if there is not
	             already a even more derived attribute. */
	          Extendable leastDerived = attr;
	          Extendable leastDerivedParent = null;
	          while ((leastDerivedParent = (Extendable) leastDerived.getRealExtending()) != null) {
                leastDerived = leastDerivedParent;
            }


	          if (!mostDerived.containsKey (leastDerived)) {
                mostDerived.put (leastDerived, attr);
            }


	          /* If this mentioning of attr is the least derived one, this
	             is the time to add it to the stack of encountered attributes. */
	          if (attr == leastDerived)
	          {
	            attrsOfV_reversed.add (/* at frontmost position */ 0,
	              mostDerived.get (attr));
	          }
	        }
	      }


	      Iterator<Extendable> attrsOfV_iter = attrsOfV_reversed.iterator();
	      while (attrsOfV_iter.hasNext()) {
            result.add (/* at frontmost position */ 0, attrsOfV_iter.next());
        }
	    }
	    return result.iterator ();
  	}
  }
  public Iterator<Element> getAttributesAndRoles()
  {
   	if(isAlias()){
  		return ((Viewable)getReal()).getAttributesAndRoles();
  	}else{
  		List<Element> result = new ArrayList<Element>(); // of AttributeDef/RoleDef
		List<Viewable> baseviewv = new ArrayList<Viewable>(); // list of bases of v; first element is root, last is this
		for (Viewable<E> v = this; v != null; v = (Viewable<E>) v.getRealExtending())
		{
			baseviewv.add(0,v);
		}
  		Iterator<Viewable> baseviewi = baseviewv.iterator();
  		while(baseviewi.hasNext()){
  			Viewable v=baseviewi.next();
			// for all, at this level defined/extended, attributes and roles
  			Iterator attri=v.iterator();
  			while(attri.hasNext()){
  				Object obj=attri.next();
  				if(obj instanceof AttributeDef){
  					AttributeDef attr=(AttributeDef)obj;
  					int idx=0;
  					boolean found=false;
					for(Iterator<Element> resi=result.iterator();resi.hasNext();idx++){
						Object res=resi.next();
						// extended/specialized attribute?
						if((res instanceof AttributeDef && ((AttributeDef)res).getName().equals(attr.getName()))){
							found=true;
							result.set(idx,attr);
							break;
						}
					}
					// new attribute?
					if(!found){
						result.add(attr);
					}
  				}else if(obj instanceof RoleDef){
					RoleDef role=(RoleDef)obj;
					int idx=0;
					boolean found=false;
					for(Iterator<Element> resi=result.iterator();resi.hasNext();idx++){
						Object res=resi.next();
						// extended/specialized role?
						if((res instanceof RoleDef && ((RoleDef)res).getName().equals(role.getName()))){
							found=true;
							result.set(idx,role);
							break;
						}
					}
					// new role?
					if(!found){
						result.add(role);
					}
  				}
  			}
			// for all, at this level defined/extended, embedded associations
  		}

	    return result.iterator ();
  	}
  }

  public Iterator<ViewableTransferElement> getAttributesAndRoles2()
  {
	if(isAlias()){
		return ((Viewable)getReal()).getAttributesAndRoles2();
	}else{
		List<ViewableTransferElement> result=new ArrayList<ViewableTransferElement>(); // of Element
		List<Viewable> baseviewv = new ArrayList<Viewable>(); // list of bases of v; first element is root, last is this
		for (Viewable<E> v = this; v != null; v = (Viewable<E>) v.getRealExtending())
		{
			baseviewv.add(0,v);
		}
		Iterator<Viewable> baseviewi = baseviewv.iterator();
		while(baseviewi.hasNext()){
			Viewable v = baseviewi.next();
			// for all, at this level defined/extended, attributes and roles
			Iterator[] it = new Iterator[]
			{
  				v.getRolesIterator(),
			  v.attributes.iterator()
			};
			Iterator attri=new CombiningIterator(it);
			//Iterator attri=v.iterator();
			while(attri.hasNext()){
				Object obj=attri.next();
				if(obj instanceof AttributeDef){
					AttributeDef attr=(AttributeDef)obj;
					int idx=0;
					boolean found=false;
					for(Iterator<ViewableTransferElement> resi=result.iterator();resi.hasNext();idx++){
						Object res=resi.next();
						// extended/specialized attribute?
						if((((ViewableTransferElement)res).obj instanceof AttributeDef && ((AttributeDef)((ViewableTransferElement)res).obj).getName().equals(attr.getName()))){
							found=true;
							ViewableTransferElement ele=result.get(idx);
							ele.obj=attr;
							break;
						}
					}
					// new attribute?
					if(!found){
						result.add(new ViewableTransferElement(obj));
					}
				}else if(obj instanceof RoleDef){
					RoleDef role=(RoleDef)obj;
					int idx=0;
					boolean found=false;
					for(Iterator<ViewableTransferElement> resi=result.iterator();resi.hasNext();idx++){
						Object res=resi.next();
						// extended/specialized role?
						if((((ViewableTransferElement)res).obj instanceof RoleDef && ((RoleDef)((ViewableTransferElement)res).obj).getName().equals(role.getName()))){
							found=true;
							ViewableTransferElement ele=result.get(idx);
							ele.obj=role;
							break;
						}
					}
					// new role?
					if(!found){
						result.add(new ViewableTransferElement(obj));
					}
				}
			}
			if(v instanceof AbstractClassDef){
				// for all, at this level defined/extended, embedded associations
				List embv = ((AbstractClassDef) v).getDefinedLightweightAssociations();
				// sort them according to name of opposide role
				java.util.Collections.sort(embv,new java.util.Comparator(){
					public int compare(Object o1,Object o2){
						RoleDef role1=((RoleDef)o1).getOppEnd();
						RoleDef role2=((RoleDef)o2).getOppEnd();
						return role1.getName().compareTo(role2.getName());
					}
				});
				attri=embv.iterator();
				while (attri.hasNext()) {
					RoleDef role = (RoleDef) attri.next();
					role=getSpecificRoleDef(baseviewv, role);
					RoleDef oppend = role.getOppEnd();
                    result.add(new ViewableTransferElement(oppend,true));
				}
			}
		}
		return result.iterator ();
	}
  }
  private static RoleDef getSpecificRoleDef(List<Viewable> baseviewv,RoleDef roleDef) {
      for(int idx=baseviewv.size();idx>0;idx--) {
          Viewable v=baseviewv.get(idx-1);
          if(v instanceof AbstractClassDef) {
              AbstractClassDef aclass=(AbstractClassDef)v;
              for(RoleDef extRoleDef:roleDef.getExtensions()) {
                  Iterator<AbstractClassDef> targetIt=extRoleDef.iteratorDestination();
                  while(targetIt.hasNext()) {
                      AbstractClassDef target=targetIt.next();
                      if(target==aclass) {
                          return extRoleDef;
                      }
                  }
              }
          }
      }
      return roleDef;
  }
  public Iterator<ViewableTransferElement> getDefinedAttributesAndRoles2()
  {
	if(isAlias()){
		return ((Viewable)getReal()).getDefinedAttributesAndRoles2();
	}else{
		List<ViewableTransferElement> result=new ArrayList<ViewableTransferElement>(); // of Element
			Viewable<E> v=this;
			// for all, at this level defined/extended, attributes and roles
			Iterator[] it = new Iterator[]
			{
				v.getRolesIterator(),
			  v.attributes.iterator()
			};
			Iterator attri=new CombiningIterator(it);
			//Iterator attri=v.iterator();
			while(attri.hasNext()){
				Object obj=attri.next();
				if(obj instanceof AttributeDef){
					AttributeDef attr=(AttributeDef)obj;
					result.add(new ViewableTransferElement(obj));
				}else if(obj instanceof RoleDef){
					RoleDef role=(RoleDef)obj;
					result.add(new ViewableTransferElement(obj));
				}
			}
			if(v instanceof AbstractClassDef){
				// for all, at this level defined/extended, embedded associations
				List embv = ((AbstractClassDef<E>) v).getDefinedLightweightAssociations();
				// sort them according to name of opposide role
				java.util.Collections.sort(embv,new java.util.Comparator(){
					public int compare(Object o1,Object o2){
						RoleDef role1=((RoleDef)o1).getOppEnd();
						RoleDef role2=((RoleDef)o2).getOppEnd();
						return role1.getName().compareTo(role2.getName());
					}
				});
				attri=embv.iterator();
				while (attri.hasNext()) {
					RoleDef role = (RoleDef) attri.next();
					RoleDef oppend = role.getOppEnd();
					result.add(new ViewableTransferElement(oppend,true));
				}
			}
		return result.iterator ();
	}
  }
  /** gets RoleDef's defined by this.
   * This is a hotspot for getAttributesAndRoles2().
   */
	public Iterator getRolesIterator(){
		return null;
	}

  /** Resolves an alias name for a base (the name before the tilde
      in INTERLIS-2 views).


      @param alias The alias name of the base table.


      @return A ViewableAlias, or <code>null</code> if the name
              could not be resolved.


      @see ch.interlis.ViewableAlias
  */
  public ViewableAlias resolveBaseAlias (String alias)
  {
   	if(isAlias()){
  		return ((Viewable)getReal()).resolveBaseAlias(alias);
  	}else{
          if(getName().equals(alias)){
            return new ViewableAlias(alias,this);
          }
          return null;
  	}
  }



  /** Performs certain integrity checks. First, the integrity of the
      container is ensured. Then, if this Viewable is not declared as
      abstract, it is checked whether there is any abstract attribute
      that has not been extended by a concrete attribute. In addition,
      it is ensured that there is only one single aggregative attribute
      per Viewable.


      @exception java.lang.IllegalStateException if the integrity
                 is not given.
  */
  @Override
  public void checkIntegrity(List<Ili2cSemanticException> errs)
    throws java.lang.IllegalStateException
  {
   	if(isAlias()){
  		((Viewable)getReal()).checkIntegrity(errs);
  		return;
  	}else{
	    super.checkIntegrity (errs);


	    boolean myAbstractness = this.isAbstract ();
	    Iterator attrs = getAttributes ();
	    while (attrs.hasNext())
	    {
	      AttributeDef attr = (AttributeDef) attrs.next ();
	      if ((myAbstractness == false) && attr.isAbstract ())
	      {
                boolean skip=false;
                Model model=(Model)attr.getContainer(Model.class);
                if(model instanceof PredefinedModel){
                  PredefinedModel pd=(PredefinedModel)model;
                  if(pd.LINE_SEGMENT==attr.getContainer(Table.class)){
                    skip=true;
                  }
                }
                if(!skip){
	          throw new IllegalStateException (formatMessage (
	          "err_viewable_inheritedAttrAbstract",
	          attr.getName (),
	          this.toString (),
  	          attr.getContainer(Container.class).toString()));
                  }
	      }


	    }
  	}
  }



  /** @return A collection of those tables that can be referenced
              by following relational attributes.
  */
  public Collection<Table> getReferencableTables()
  {
   	if(isAlias()){
  		return ((Viewable<?>)getReal()).getReferencableTables();
  	}else{
	    Collection<Table> coll = new HashSet<Table>(5);
	    determineReferencableTables (coll);
	    return coll;
  	}
  }



  void determineReferencableTables(Collection<Table> coll)
  {
   	if(isAlias()){
  		((Viewable<?>)getReal()).determineReferencableTables(coll);
  		return;
  	}else{
	    Iterator<?> iter = getAttributes();
	    while (iter.hasNext ())
	    {
	      Object obj = iter.next();
	      Table targetTable = null;


	      //if (obj instanceof RelAttribute)
	      //  targetTable = ((RelAttribute) obj).getTargetTable ();
	      //else if (obj instanceof CompositionAttribute)
	      //  targetTable = ((CompositionAttribute) obj).getTargetTable ();
	      // TODO get targetTable from attr.domain


	      if ((targetTable != null) && coll.add(targetTable)) {
            targetTable.determineReferencableTables (coll);
        }
	    }
  	}
  }



  /** Determines a collection containing those <code>STRUCTURE</code>s
      that might possibly occur as composition elements, i.e. that might
      need to be transferred with an instance of this Viewable.


      @param startAttribute one of the composition attributes of this Viewable.
  */
  public Set<Viewable> getPossibleComponents(AttributeDef startAttribute)
  {
   	if(isAlias()){
  		return ((Viewable)getReal()).getPossibleComponents(startAttribute);
  	}else{
	    Set<Viewable> theResult = new HashSet<Viewable> ();


	    CompositionType ct = (CompositionType) Type.findReal (startAttribute.getDomain());
	    if ((ct != null) && (ct.getComponentType() != null)) {
            calcCompositionClosure (ct.getComponentType(), theResult);
        }
	    return theResult;
  	}
  }



  /** Determines all Viewables that can be reached from this Viewable through
      recursively following all its composition attributes. The parameter
      <em>v</em> is a current step in the recursive computation.


      <p><em>v</em> is added to the set of reachable viewables <em>s</em>
      if all of the following conditions hold:


      <ol><li><em>s</em> does not already contain <em>v</em>;</li>


          <li>the model of <em>this</em> imports or is the same as
              the model of <em>v</em>;</li>


          <li><em>v</em> is declared before <em>this</em>.</li>
      </ol>


      In addition to adding <em>v</em> to the set <em>s</em>,
      calcCompositionClosure is called recursively with <em>v</em> set to
      each polymorphically equivalent extension of the component class of
      every composition attribute of <em>v</em>.


      <p>The algorithm terminates because at some time, the set <em>s</em>
      contains all reachable Viewables. The algorithm is thus a typical
      fix point algorithm, comparable to those known from compiler
      construction.  In addition, the algorithm would terminate because
      the reachability graph is not allowed to by cyclic.


      @param v A Viewable that is the component type of some composition
               attribute of <code>this</code>.


      @param s A Set that will be filled.
  */
  void calcCompositionClosure (Viewable v, Set<Viewable> s)
  {
   	if(isAlias()){
  		((Viewable<E>)getReal()).calcCompositionClosure ( v, s);
  		return;
  	}else{
	    if (v == null) {
            return;
        }


	    /* If v is already member of s, there is no need to walk through its
	       attributs another time.
	    */
	    if (s.contains (v)) {
            return;
        }


	    /* If the Viewable comes from a different model, make sure it has been imported. */
	    Model vModel = (Model) v.getContainerOrSame (Model.class);
	    Model myModel = (Model) this.getContainerOrSame (Model.class);
	    if ((vModel == null) || (myModel == null)) {
            return;
        }
	    if ((vModel != myModel) && !myModel.isImporting(vModel)) {
            return;
        }


	    /* Look only at those that are declared before "declaredBefore". */
	    if (!v.isDeclaredBefore (this)) {
            return;
        }



	    s.add (v);


	    Iterator<Extendable> attrs = v.getAttributes (); /* Includes inherited attributes. */
	    while (attrs.hasNext ())
	    {
	      AttributeDef theAttrib = (AttributeDef) attrs.next();
	      Type         theType = Type.findReal (theAttrib.getDomain());
	      Table        componentStruct;


	      if (!(theType instanceof CompositionType)) {
            continue;
        }


	      componentStruct = ((CompositionType) theType).getComponentType();
	      if (componentStruct == null) {
            continue;
        }


	      calcCompositionClosure (componentStruct, s);
	    } /* while (attrs.hasNext()) */


	    Set<ExtendableContainer<E>> extensions = v.getExtensions();
	    extensions.add (v);
	    Iterator<ExtendableContainer<E>> extensionsIter = extensions.iterator();
	    while (extensionsIter.hasNext())
	    {
	      Viewable component = (Viewable) extensionsIter.next();
	      calcCompositionClosure (component, s);
	    } /* while (extensionsIter.hasNext()) */
  	}
  }



  /** Returns the name space of evaluables (right-hand side of assignments)
      mentioned somewhere inside this Container.


      <p>Certain elements of Containers, for instance constraints or selections,
      refer to attributes. The name space of these attributes is though not
      necessarily identical to the container.  For example, attribute names
      mentioned in a Graphic refer to the Viewable on which the Graphic
      is based, not the Graphic itself.
  */
  public Viewable<E> getRHSNameSpace ()
  {
   	if(isAlias()){
  		return ((Viewable<E>)getReal()).getRHSNameSpace ();
  	}else{
	    return this;
  	}
  }
}
