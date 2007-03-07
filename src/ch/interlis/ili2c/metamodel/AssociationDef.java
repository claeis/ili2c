package ch.interlis.ili2c.metamodel;
import java.util.*;



/** An association. Please refer to the INTERLIS reference manual to
    learn about the concept of associations.
 */
public class AssociationDef extends AbstractClassDef
{
	protected List     roles = new LinkedList();
	private Cardinality cardinality=null;
	private boolean identifiable = false;


	private Viewable derivedFrom;
	/** Define the view from which this association is derived. This
	*   association has no instances.
	*/
	public void setDerivedFrom(Viewable ref)
	{
		derivedFrom=ref;
	}

	public Viewable getDerivedFrom()
	{
		return derivedFrom;
	}

  public AssociationDef()
  {
  }
  protected Collection createElements(){
    return new AbstractCollection()
    {
      public Iterator iterator ()
      {
        Iterator[] it = new Iterator[]
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



      public boolean add (Object o)
      {


        if (o instanceof Constraint)
          return constraints.add(o);


        if ((o instanceof LocalAttribute)){
	  LocalAttribute ad =(LocalAttribute)o;
          /* A non-abstract AssociationDef can not contain an abstract
             attribute. */
          if (ad.isAbstract() && !isAbstract())
            throw new IllegalArgumentException (formatMessage (
              "err_abstractAttrInConcreteContainer",
              AssociationDef.this.toString()));
	  // check that there is no role with the same name
	  Iterator iter = getAttributesAndRoles();
	  while (iter.hasNext())
	  {
	      Element baseAttr = (Element)iter.next();
	      if(baseAttr.getName().equals(ad.getName()) && !ad.isExtending(baseAttr)){
                throw new IllegalArgumentException (formatMessage (
                  "err_association_nonuniqueAttributeDef",
                  ad.getName(),
                  AssociationDef.this.toString()));
	      }
	  }
          // check if there is not alreay a role pointing to this with the same name
	  iter = getOpposideRoles();
	  while (iter.hasNext())
	  {
	      RoleDef role = (RoleDef)iter.next();
	      if(role.getName().equals(ad.getName())){
                throw new IllegalArgumentException (formatMessage (
                  "err_abstractClassDef_AttributeNameConflictInTarget",
                  ad.getName(),
                  AssociationDef.this.toString()));
	      }
	  }

	  return attributes.add(o);
	}


        if ((o instanceof RoleDef)){
	  RoleDef role =(RoleDef)o;
	  if((getExtending()!=null || isExtended())&& !role.isExtended()){
	          throw new IllegalArgumentException (
	            formatMessage("err_association_nonewrole",role.getName()));
	  }

          Element conflicting = getElement (RoleDef.class,role.getName());
          if ((conflicting != null) && (conflicting != o)
            && !role.isExtended())
            throw new IllegalArgumentException (formatMessage (
              "err_association_nonuniqueRoleDef",
              role.getName(),
              AssociationDef.this.toString()));

          /* A non-abstract AssociationDef can not contain an abstract
             RoleDef. */
          if (role.isAbstract() && !isAbstract())
            throw new IllegalArgumentException (formatMessage (
              "err_abstractRoleInConcreteContainer",
              AssociationDef.this.toString()));

          return roles.add(o);
	}



        if (o == null)
          throw new IllegalArgumentException (
            rsrc.getString ("err_nullNotAcceptable"));


        throw new ClassCastException();
      }
    };
  }

	Iterator getRolesIterator(){
		return roles.iterator();
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
    Iterator iter = roles.iterator();
    StringBuffer assocName=new StringBuffer();
    while (iter.hasNext()){
	    RoleDef role= (RoleDef)iter.next();
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
    if (oldValue == newValue)
      return;


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


    if (oldValue == newValue)
      return; /* nothing has to be done */


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
  public void checkIntegrity ()
    throws java.lang.IllegalStateException
  {
    super.checkIntegrity ();


    if (extending!=null && isExtended())
    {
      Topic myTopic = (Topic) getContainer (Topic.class);
      Topic extTopic = (Topic) extending.getContainer (Topic.class);


      if (myTopic == null)
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedOutsideTopic",
          this.toString(),
          extending.toString(),
          this.toString()));


      if (extTopic == null)
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedOutsideTopic",
          this.toString(),
          extending.toString(),
          extending.toString()));


      if ((myTopic != null)
          && !(myTopic.isExtending (extTopic)))
        throw new IllegalArgumentException (formatMessage (
          "err_association_extendedButTopicsDont",
          this.toString(), extending.toString(),
          myTopic.toString(), extTopic.toString()));


      if (myTopic != null)
      {
        Iterator iter = myTopic.iterator();
        while (iter.hasNext())
        {
          Object obj = iter.next();
          if (!(obj instanceof AssociationDef))
            continue;


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
          if (!(obj instanceof AssociationDef))
            continue;
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
      if (this.isExtending (referencable))
        throw new IllegalStateException (formatMessage (
          "err_association_cyclicRelationalStructure",
          this.toString(), referencable.toString()));
    }


  }
  public void fixupRoles()
  {
  	if(isDirty()){
  		return;
  	}
        Iterator iter;
	// if extended
	if(getExtending()!=null){
		AssociationDef base=(AssociationDef)getExtending();
	    	iter = roles.iterator();
    		while (iter.hasNext()){
	    		RoleDef role= (RoleDef)iter.next();
			RoleDef baserole=(RoleDef)base.getElement(RoleDef.class,role.getName());
			role.setExtending(baserole);
    		}
	}else{
		// no base definition
		// at least two roles required
		if(roles.size()<2){
			throw new IllegalStateException (formatMessage (
			  "err_association_twoRoleDef"));
		}
	}

        // for each role, create backlink from target class
    	iter = roles.iterator();
	while (iter.hasNext()){
		RoleDef role= (RoleDef)iter.next();
		AbstractClassDef targetClass=role.getDestination();
		targetClass.addTargetForRole(role);
	}

	// check, that there is only one aggregation or composition
	iter = getAttributesAndRoles();
	int aggc=0;
	while (iter.hasNext())
	{
	      Object obj = iter.next();
	      if (obj instanceof RoleDef)
	      {
		RoleDef role = (RoleDef) obj;
		if(role.getKind()>RoleDef.Kind.eASSOCIATE){
			aggc+=1;
		}
	      }
       }
       if(aggc>1){
        throw new IllegalStateException (formatMessage (
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
	if(role1Base==null)role1Base=role1;
	RoleDef role2Base=role1.getRootExtending();
	if(role2Base==null)role2Base=role2;
	
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
	if(base==null)base=this;
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

	/**
	 * @param b
	 */
	public void setIdentifiable(boolean b) {
		identifiable = b;
	}

}
