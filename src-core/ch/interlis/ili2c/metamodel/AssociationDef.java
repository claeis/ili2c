package ch.interlis.ili2c.metamodel;
import java.util.*;



/** An association. Please refer to the INTERLIS reference manual to
    learn about the concept of associations.
 */
public class AssociationDef extends AbstractClassDef<Element>
{
	protected List<RoleDef> roles = new LinkedList<RoleDef>();
	private Cardinality cardinality=null;
	private boolean identifiable = false;


	private Viewable<?> derivedFrom;
	/** Define the view from which this association is derived. This
	*   association has no instances.
	*/
	public void setDerivedFrom(Viewable<?> ref)
	{
		derivedFrom=ref;
	}

	public Viewable<?> getDerivedFrom()
	{
		return derivedFrom;
	}

  public AssociationDef()
  {
  }
  protected Collection<Element> createElements(){
    return new AbstractCollection<Element>()
    {
      public Iterator<Element> iterator ()
      {
        Iterator<Element>[] it = new Iterator[]
        {
          roles.iterator(),
		  attributes.iterator(),
          constraints.iterator()
        };
        return new CombiningIterator(it);
      }



      public int size ()
      {
        return attributes.size()
          + roles.size()
          + constraints.size();
      }



      public boolean add(Element o)
      {


        if (o instanceof Constraint) {
        	((Constraint) o).setNameIdx(constraints.size()+1);
            return constraints.add((Constraint) o);
        }


        if (o instanceof LocalAttribute){
	  LocalAttribute ad =(LocalAttribute)o;
          /* A non-abstract AssociationDef can not contain an abstract
             attribute. */
          if (ad.isAbstract() && !isAbstract()) {
            throw new Ili2cSemanticException (ad.getSourceLine(),formatMessage (
              "err_abstractAttrInConcreteContainer",
              AssociationDef.this.toString()));
        }
	  // check that there is no role with the same name
	  for (Iterator<Element> iter = getAttributesAndRoles(); iter.hasNext(); )
	  {
	      Element baseAttr = iter.next();
	      if(baseAttr instanceof RoleDef){
	    	  if(baseAttr.getName().equals(ad.getName())){

                throw new Ili2cSemanticException (ad.getSourceLine(),formatMessage (
                  "err_association_nonuniqueAttributeDef",
                  ad.getName(),
                  AssociationDef.this.toString()));
	    	  }
	      }else{
	    	  // check for attrs with the same name is already done in parser.findOverridingAttribute()
	      }
	  }
          // check if there is not alreay a role pointing to this with the same name
	  for (Iterator<RoleDef> iter = getOpposideRoles(); iter.hasNext(); )
	  {
	      RoleDef role = iter.next();
	      if(role.getName().equals(ad.getName())){
                throw new Ili2cSemanticException (ad.getSourceLine(),formatMessage (
                  "err_abstractClassDef_AttributeNameConflictInTarget",
                  ad.getName(),
                  AssociationDef.this.toString()));
	      }
	  }
	  
      // check extref in structattrs in CLASS/ASSOCIATION
      if(!AssociationDef.this.isAbstract() && AssociationDef.this.getContainer() instanceof Topic){
      	AbstractPatternDef.checkTopicDepOfAttr((Topic)AssociationDef.this.getContainer(),ad,ad.getName());
      }

	  return attributes.add(ad);
	}


        if (o instanceof RoleDef){
	  RoleDef role =(RoleDef)o;
	  if((getExtending()!=null || isExtended())&& !role.isExtended()){
	          throw new Ili2cSemanticException (role.getSourceLine(),
	            formatMessage("err_association_nonewrole",role.getName()));
	  }

          Element conflicting = getElement (RoleDef.class,role.getName());
          if ((conflicting != null) && (conflicting != o)
            && !role.isExtended()){
        	  setDirty(true);
            throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
              "err_association_nonuniqueRoleDef",
              role.getName(),
              AssociationDef.this.toString()));
          }
          /* A non-abstract AssociationDef can not contain an abstract
             RoleDef. */
          if (role.isAbstract() && !isAbstract()) {
              setDirty(true);
            throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
              "err_abstractRoleInConcreteContainer",
              AssociationDef.this.toString()));
        }

          return roles.add(role);
	}



        if (o == null) {
            throw new IllegalArgumentException (
                rsrc.getString ("err_nullNotAcceptable"));
        }


        throw new ClassCastException();
      }
    };
  }

	public Iterator<RoleDef> getRolesIterator(){
		return roles.iterator();
	}
	public List<RoleDef> getRoles(){
		List<RoleDef> ret=new ArrayList<RoleDef>(roles);
		return ret;
	}

  /** Returns a string that consists of either <code>CLASS</code>
      or <code>STRUCTURE</code> followed by a space and the fully
      scoped name of this class/structure.
  */
  public String toString ()
  {
    return "ASSOCIATION "
      + getScopedName(null);
  }


  /** returns the (calculated) name of the association.
   * If the name of an AssociationDef ist not given, it is calculated
   * by joining the name of the roles.
   */
  public String getName()
  {
  	if(name!=""){
  		return name;
  	}
    Iterator<RoleDef> iter = roles.iterator();
    StringBuilder assocName=new StringBuilder();
    while (iter.hasNext()){
	    RoleDef role= iter.next();
	    assocName.append(role.getName());
    }
  	return assocName.toString();
  }


  /** @exception java.lang.IllegalArgumentException if this
                 Table is contained in a Model and
                 <code>abst</code> is <code>false</code>.


      @exception java.lang.IllegalArgumentException if this
                 Table is final and <code>abst</code> is
                 <code>true</code>.
  */
  public void setAbstract(boolean abst)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _abstract;
    boolean newValue = abst;


    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue) {
        return;
    }


    /* Table in models must be ABSTRACT. */
    if ((newValue == false)
        && (getContainer (Topic.class) == null)
        && (getContainer (Model.class) != null))
    {
      throw new IllegalArgumentException (formatMessage ("err_association_concreteOutsideTopic",
                                                         this.toString ()));
    }


    super.setAbstract (newValue);
  }



  /** Causes this table to extend another table.


      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param extending  The new table being extended, or
                        <code>null</code> if this table is
                        going to be independent of other tables.


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
                 the class <code>Table</code>.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException
  {
    /* The cast in the assignment to newValue throws a ClassCastException
       if extending is neither null nor an instance of Table.
       This is exactly what the API documentation specifies.
    */
    AssociationDef oldValue = (AssociationDef) this.extending;
    AssociationDef newValue = (AssociationDef) extending;


    if (oldValue == newValue) {
        return; /* nothing has to be done */
    }


    super.setExtending (newValue);
  }





  /** Performs certain integrity checks, including checks for the
      elements of a container. Unfortunately, some checks
      can only be performed when all modifications are done.
      <p>If a table <i>T</i> is extending another table <i>Base</i>
      without changing the name
      (<code>T.getName().equals (Base.getName())</code>),


      <ol>
      <li>the topic of <i>T</i> must extend the topic of
      <i>Base</i>, and</li>


      <li>there must not be any extensions of
      <i>Base</i> in topic of <i>Base</i>, and</li>


      <li>there must not be any extensions of
      <i>Base</i> in topic of <i>T</i>.</li>
      </ol>


      <p>In addition, this method checks that a class does not
      contain multiple area attributes.


      @exception java.lang.IllegalStateException if the integrity
                 is not given.
  */
  @Override
  public void checkIntegrity (List<Ili2cSemanticException> errs)
    throws java.lang.IllegalStateException
  {
    super.checkIntegrity (errs);


    if (extending!=null && isExtended())
    {
      Topic myTopic = (Topic) getContainer (Topic.class);
      Topic extTopic = (Topic) extending.getContainer (Topic.class);


      if (myTopic == null) {
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedOutsideTopic",
          this.toString(),
          extending.toString(),
          this.toString()));
    }


      if (extTopic == null) {
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedOutsideTopic",
          this.toString(),
          extending.toString(),
          extending.toString()));
    }


      if ((myTopic != null)
          && !(myTopic.isExtending (extTopic))) {
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedButTopicsDont",
          this.toString(), extending.toString(),
          myTopic.toString(), extTopic.toString()));
    }


      if (myTopic != null)
      {
        Iterator iter = myTopic.iterator();
        while (iter.hasNext())
        {
          Object obj = iter.next();
          if (!(obj instanceof AssociationDef)) {
            continue;
        }


          AssociationDef tab = (AssociationDef) obj;
          if ((tab != this)
              && (tab != extending)
              && tab.isExtending (extending))
          {
            throw new IllegalStateException (formatMessage (
              "err_association_extendedButOtherDoesToo",
              this.toString(), extending.toString(),
              myTopic.toString(), tab.toString()));
          }
        }
      }


      if (extTopic != null)
      {
        Iterator iter = extTopic.iterator();
        while (iter.hasNext())
        {
          Object obj = iter.next();
          if (!(obj instanceof AssociationDef)) {
            continue;
        }
          AssociationDef tab = (AssociationDef) obj;
          if ((tab != this)
              && (tab != extending)
              && tab.isExtending (extending))
          {
            throw new IllegalStateException (formatMessage (
              "err_association_extendedButOtherDoesToo",
              this.toString(), extending.toString(),
              extTopic.toString(), tab.toString()));
          }
        }
      }
    } /* ((extending != null) && name.equals (extending.name)) */


    Iterator referencableIter = getReferencableTables().iterator();
    while (referencableIter.hasNext())
    {
      AssociationDef referencable = (AssociationDef) referencableIter.next();
      if (this.isExtending (referencable)) {
        throw new IllegalStateException (formatMessage (
          "err_association_cyclicRelationalStructure",
          this.toString(), referencable.toString()));
    }
    }


  }
  public void fixupRoles()
  {
  	if(isDirty()){
  		return;
  	}
  	if(getContainer()==null){
  		throw new IllegalStateException("AssociationDef without container");
  	}
    Iterator rolei;
	// if extended assoc?
	if(getExtending()!=null){
		AssociationDef base=(AssociationDef)getExtending();
		rolei = roles.iterator();
		while (rolei.hasNext()){
			RoleDef role= (RoleDef)rolei.next();
			Element baseEle=base.getElement(RoleDef.class,role.getName());
			if(baseEle==null){
				throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
				  "err_association_noRoleToExtend",role.getName(),base.getScopedName(null)));
			}
			RoleDef baserole=(RoleDef)baseEle;
			role.setExtending(baserole);
		}
	}else{
		// no base definition
		// at least two roles required
		if(roles.size()<2){
			throw new Ili2cSemanticException (getSourceLine(),formatMessage (
			  "err_association_twoRoleDef"));
		}
	}

        // for each role, create backlink from target class
    	rolei = roles.iterator();
	while (rolei.hasNext()){
		RoleDef role= (RoleDef)rolei.next();
		Iterator desti=role.iteratorDestination();
		while(desti.hasNext()){
			AbstractClassDef targetClass=(AbstractClassDef)desti.next();
			if(role.isExtended()) {
			    // baseRole already known to targetClass
			}else {
	            if(targetClass.getContainer()==getContainer()){
	                targetClass.addTargetForRole(role);
	            }else{
	                targetClass.addNonNavigableTargetForRole(role);
	            }
			}
		}
	}

	// check, that there is only one aggregation or composition
	rolei = getAttributesAndRoles();
	int aggc=0;
	while (rolei.hasNext())
	{
	      Object obj = rolei.next();
	      if (obj instanceof RoleDef)
	      {
		RoleDef role = (RoleDef) obj;
		if(role.getKind()>RoleDef.Kind.eASSOCIATE){
			aggc+=1;
		}
	      }
       }
       if(aggc>1){
        throw new Ili2cSemanticException (getSourceLine(),formatMessage (
          "err_association_multipleAggregations"));
       }

  }

  private boolean extended=false;
  /** to distinguish between EXTENDS and EXTENDED
   */
  public void setExtended(boolean extended){
  	this.extended=extended;
  }
  public boolean isExtended(){
  	return extended;
  }
  /** returns the roledef where this link is embedded to
   * the roledefs target class
   * or null if this association has a standalone link object.
   */
  public RoleDef getRoleWhereEmbedded(){
	int rolec=0;
	RoleDef role1=null;
	RoleDef role2=null;
	Iterator rolei = getAttributesAndRoles(); // returns extended roledefs
	while (rolei.hasNext())
	{
	  Object obj = rolei.next();
	  if (obj instanceof RoleDef)
	  {
		rolec++;
		if(role1==null){
	  role1 = (RoleDef) obj;
		}else if(role2==null){
	  role2 = (RoleDef) obj;
		}
	  }
	}
	// association with more than two roles?
	if(rolec>2){
	  return null;
	}

	// get base roledefs
	RoleDef role1Base=role1.getRootExtending();
	if(role1Base==null) {
        role1Base=role1;
    }
	RoleDef role2Base=role2.getRootExtending();
	if(role2Base==null) {
        role2Base=role2;
    }

	// have both association ends a cardinality greater than 1?
	if(role1Base.getCardinality().getMaximum()>1 && role2Base.getCardinality().getMaximum()>1){
	  return null;
	}

	RoleDef ret=null;
	RoleDef retBase=null;
	if(role1Base.getCardinality().getMaximum()==1){
		ret=role2;
		retBase=role2Base;
	}else{
		ret=role1;
		retBase=role1Base;
	}

	// target of (base-)role not in the same topic as (base-)association?
	AssociationDef base=(AssociationDef)getRootExtending();
	if(base==null) {
        base=this;
    }
	if(retBase.getDestination().getContainer()!=base.getContainer()){
		return null;
	}

	// association is lightweight
	return ret;
  }
  /** tests, if this association has no link object,
   *  but is embedded in a association end object.
   */
  public boolean isLightweight(){
  	return getRoleWhereEmbedded()!=null;
  }

  public void setCardinality(Cardinality v)
  {
	  cardinality=v;
  }
  public boolean containsCardinality()
  {
	  return cardinality!=null;
  }
  public Cardinality getDefinedCardinality()
  {
	  return cardinality;
  }

	/**
	 * @return
	 */
	public boolean isIdentifiable() {
		return identifiable;
	}
	@Override
	  public Domain getOid() {
		  
		  AssociationDef def=this;
		  while(def!=null){
				Domain oidDomain=def.getDefinedOid();
				if(oidDomain==null && def.isIdentifiable()){
					Topic topic=(Topic)def.getContainer(Topic.class);
					if(topic!=null){
						oidDomain=topic.getOid();
					}
				}
				if(oidDomain!=null && !(oidDomain instanceof NoOid)){
					return oidDomain;
				}
			  def=(AssociationDef)def.getExtending();
		  }
			return null;
		}
	

	/**
	 * @param b
	 */
	public void setIdentifiable(boolean b) {
		identifiable = b;
	}
    @Override
    public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
      throws java.lang.IllegalStateException
    {
        super.checkTranslationOf(errs,name,baseName);
        AssociationDef baseElement=(AssociationDef)getTranslationOf();
        if(baseElement==null) {
            return;
        }
        
        if(isIdentifiable()!=baseElement.isIdentifiable()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_oidMismatch")));
        }
        Cardinality card=getDefinedCardinality();
        Cardinality baseCard=baseElement.getDefinedCardinality();
        if(card!=null && baseCard!=null) {
            if(card.equals(baseCard)) {
                // ok
            }else {
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_cardinalityMismatch")));
            }
        }else {
            if(card==null && baseCard==null) {
                // ok
            }else {
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_cardinalityMismatch")));
            }
        }
        Ili2cSemanticException err=null;
        err=checkElementRef(getDerivedFrom(),baseElement.getDerivedFrom(),getSourceLine(),"err_diff_derviedFromMismatch");
        if(err!=null) {
            errs.add(err);
        }
    }

}
