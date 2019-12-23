
package ch.interlis.ili2c.metamodel;
import java.util.*;

public abstract class AbstractClassDef<E extends Element> extends Viewable<E>
{
	private Domain oid=null;
  /** backlink to RoleDef, if this ClassDef or AssociationDef is used as target
   *  of a RoleDef.
   */
  private final Set<RoleDef> targetForRoles = new HashSet<RoleDef>(2);
  /** backlink to RoleDef, if this ClassDef or AssociationDef is used as target
   *  of a RoleDef that is not in the same container ant therefore not navigable.
   */
  private final Set<RoleDef> nonNavigableTargetForRoles = new HashSet<RoleDef>(2);
  /** adds a backlink to the given RoleDef that has this ClassDef or
   *  AssociationDef as a target.
   */
  public void addTargetForRole(RoleDef role){


    // check role name is unique with respect to attributes and roles in other roles target
    AssociationDef assoc=(AssociationDef)role.getContainer();
    
    AbstractClassDef<?> targetClass=this;
    
    // for all oppends of role
    Iterator<Element> iter = assoc.getAttributesAndRoles();
    while (iter.hasNext()){
      Element obj = iter.next();
      if(obj instanceof RoleDef){
        RoleDef otherRole=(RoleDef)obj;
        if(otherRole==role) {
            continue;
        }
        
        // compare name of new role oppend with list of attributes in target class (and and list of roles if target is an association)
        Iterator<Element> attri=targetClass.getAttributesAndRoles();
        while (attri.hasNext()){
        	Element attrOrRole = attri.next();
            if(attrOrRole.getName().equals(otherRole.getName())){
                throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
                  "err_abstractClassDef_nameConflictInOtherRoleTarget",
                  otherRole.getName(),
                  role.getName(),targetClass.getName()));
            }
        }
        
        // compare name of new role oppend with list of outgoing roles in target class
        Iterator<RoleDef> rolei=targetClass.getOpposideRoles();
        while (rolei.hasNext()){
	      RoleDef targetClassOutgoingRole = rolei.next();
	      RoleDef tragetClassOutgoingRoleRoot = targetClassOutgoingRole.getRootExtending();
	      if(tragetClassOutgoingRoleRoot==null) {
            tragetClassOutgoingRoleRoot=targetClassOutgoingRole;
        }
	      RoleDef otherRoleRoot=otherRole.getRootExtending();
	      if(otherRoleRoot==null) {
	          otherRoleRoot=role;
	      }
	      if(targetClassOutgoingRole.getName().equals(otherRole.getName())
                  && otherRoleRoot!=tragetClassOutgoingRoleRoot){
                throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
                  "err_abstractClassDef_nameConflictInOtherRoleTarget",
                  otherRole.getName(),
                  role.getName(),targetClass.getName()));
	      }
        }
      }
    }

    targetForRoles.add(role);
  }
  /** get the list of roles that target this.
   *  Doesn't return inherited ones.
   * @returns Iterator<RoleDef>
   */
  public Iterator<RoleDef> getDefinedTargetForRoles()
  {
  	return targetForRoles.iterator();
  }
  public Iterator<RoleDef> getDefinedTargetForNonNavigableRoles()
  {
  	return nonNavigableTargetForRoles.iterator();
  }
  /** get the list of roles that target this.
   *  Returns also inherited ones.
   * @returns list<RoleDef>
   */
  public Iterator<RoleDef> getTargetForRoles()
  {
	    List<RoleDef> result = new LinkedList<RoleDef>();
	    Map<Extendable, RoleDef> mostDerived = new HashMap<Extendable, RoleDef>();
	    for (AbstractClassDef<?> v = this; v != null; v = (AbstractClassDef<?>) v.getRealExtending())
	    {
	      List<RoleDef> attrsOfV_reversed = new LinkedList<RoleDef>(); /* a stack */
	      Iterator<RoleDef> iter = v.getDefinedTargetForRoles();
	      while (iter.hasNext ())
	      {
	        RoleDef role = iter.next();
	          /* Find least derived; put it into the map if there is not
	             already a even more derived role. */
	          Extendable leastDerived = role;
	          Extendable leastDerivedParent = null;
	          while ((leastDerivedParent = (Extendable) leastDerived.getRealExtending()) != null) {
                leastDerived = leastDerivedParent;
            }


	          if (!mostDerived.containsKey(leastDerived)) {
                mostDerived.put(leastDerived, role);
            }


	          /* If this mentioning of role is the least derived one, this
	             is the time to add it to the stack of encountered attributes. */
	          if (role == leastDerived)
	          {
	            attrsOfV_reversed.add (/* at frontmost position */ 0,
	              mostDerived.get(role));
	          }
	      }


	      Iterator<RoleDef> attrsOfV_iter = attrsOfV_reversed.iterator();
	      while (attrsOfV_iter.hasNext()) {
            result.add (/* at frontmost position */ 0, attrsOfV_iter.next());
        }
	    }
	    return result.iterator ();
  }
  public Iterator<RoleDef> getTargetForNonNavigableRoles()
  {
	    List<RoleDef> result = new LinkedList<RoleDef>();
	    Map<Extendable, RoleDef> mostDerived = new HashMap<Extendable, RoleDef>();
	    for (AbstractClassDef<?> v = this; v != null; v = (AbstractClassDef<?>) v.getRealExtending())
	    {
	      List<RoleDef> attrsOfV_reversed = new LinkedList<RoleDef>(); /* a stack */
	      Iterator<RoleDef> iter = v.getDefinedTargetForNonNavigableRoles();
	      while (iter.hasNext ())
	      {
	        RoleDef role = iter.next();
	          /* Find least derived; put it into the map if there is not
	             already a even more derived role. */
	          Extendable leastDerived = role;
	          Extendable leastDerivedParent = null;
	          while ((leastDerivedParent = (Extendable) leastDerived.getRealExtending()) != null) {
                leastDerived = leastDerivedParent;
            }


	          if (!mostDerived.containsKey(leastDerived)) {
                mostDerived.put(leastDerived, role);
            }


	          /* If this mentioning of role is the least derived one, this
	             is the time to add it to the stack of encountered attributes. */
	          if (role == leastDerived)
	          {
	            attrsOfV_reversed.add (/* at frontmost position */ 0,
	              mostDerived.get(role));
	          }
	      }


	      Iterator<RoleDef> attrsOfV_iter = attrsOfV_reversed.iterator();
	      while (attrsOfV_iter.hasNext()) {
            result.add (/* at frontmost position */ 0, attrsOfV_iter.next());
        }
	    }
	    return result.iterator ();
  }
  public void addNonNavigableTargetForRole(RoleDef role) {
		nonNavigableTargetForRoles.add(role);
	}
  
  /** get list of outgoing roles (embedded and not embedded, including inherited ones)
   */
  public Iterator<RoleDef> getOpposideRoles()
  {
    List<RoleDef> result = new ArrayList<RoleDef>();
    Iterator<RoleDef> rolei = getTargetForRoles();
    while(rolei.hasNext()){
      RoleDef role = rolei.next();
      AssociationDef assoc=(AssociationDef)role.getContainer();
      Iterator<Element> iter = assoc.getAttributesAndRoles();
      while (iter.hasNext()){
          Element oppRole = iter.next();
        if(oppRole instanceof RoleDef){
          if (oppRole != role) {
            result.add((RoleDef) oppRole);
          }
        }
      }
    }

    return result.iterator();
  }
  public Iterator<RoleDef> getOpposideForNonNavigableRoles()
  {
    List<RoleDef> result = new ArrayList<RoleDef>();
    Iterator<RoleDef> rolei = getTargetForNonNavigableRoles();
    while(rolei.hasNext()){
      RoleDef role = rolei.next();
      AssociationDef assoc=(AssociationDef)role.getContainer();
      Iterator<Element> iter = assoc.getAttributesAndRoles();
      while (iter.hasNext()){
          Element oppRole = iter.next();
        if(oppRole instanceof RoleDef){
          if (oppRole != role) {
            result.add((RoleDef) oppRole);
          }
        }
      }
    }

    return result.iterator();
  }

  /** find the opposide role with the given Name. 
   * Will also check associations that are only visible from the given context.
   * @returns Null or found RoleDef.
   */
  public RoleDef findOpposideRole(Container context,String roleName){
	  RoleDef ret=findOpposideRole(roleName);
	  if(ret!=null){
		  return ret;
	  }

	  if(context instanceof Viewable) {
	      context=context.getContainer();
	  }
	  if(!(context instanceof Topic || context instanceof Model)){
		  throw new IllegalArgumentException("illegal context "+context.getScopedName());
	  }
	  
	    Iterator<RoleDef> rolei = getTargetForNonNavigableRoles();
	    while(rolei.hasNext()){
	      RoleDef role = rolei.next();
	      AssociationDef assoc=(AssociationDef)role.getContainer();
	      if(assoc.getContainer()==context || (context instanceof Extendable && ((Extendable) context).isExtending(assoc.getContainer()))){
		      Iterator<Element> iter = assoc.getAttributesAndRoles();
		      while (iter.hasNext()){
		          Element oppRole = iter.next();
		        if(oppRole instanceof RoleDef){
		          if (oppRole != role) {
		            if(oppRole.getName().equals(roleName)){
		            	return (RoleDef)oppRole;
		            }
		          }
		        }
		      }
	      }
	    }
	  
	  return null;
  }
  /** find the opposide role with the given Name.
   * @returns Null or found RoleDef.
   */
  public RoleDef findOpposideRole(String roleName){
	    if (roleName == null) {
            return null;
        }


	    Iterator<RoleDef> it = getOpposideRoles();
	    while (it.hasNext ())
	    {
	      RoleDef e = it.next ();
	      if (roleName.equals (e.getName())) {
            return e;
        }
	    }
      return null;
  }
  /** get the list of associations that have no link object,
   *  but are embedded into this. Doesn't return inherited ones.
   * @returns list<RoleDef>
   */
  public List<RoleDef> getDefinedLightweightAssociations(){
	List<RoleDef> assocv=new ArrayList<RoleDef>();
	Iterator<RoleDef> it = getDefinedTargetForRoles();
	while (it.hasNext ())
	{
	  RoleDef e = it.next ();
	  if (e.isAssociationEmbedded() && !e.isExtended()){
		assocv.add(e);
	  }
	}
	return assocv;
  }
  /** get the list of associations that have no link object,
   *  but are embedded into this. Returns also inherited ones.
   * @returns list<RoleDef>
   */
  public List<RoleDef> getLightweightAssociations(){
    List<RoleDef> assocv=new ArrayList<RoleDef>();
    Iterator<RoleDef> it = getTargetForRoles();
    while (it.hasNext ())
    {
      RoleDef e = it.next ();
      if (e.isAssociationEmbedded()){
        assocv.add(e);
      }
    }

    return assocv;
  }
  public Domain getOid() {
	  
	  AbstractClassDef def=this;
	  while(def!=null){
			Domain oidDomain=def.getDefinedOid();
			if(oidDomain==null){
				Topic topic=(Topic)def.getContainer(Topic.class);
				if(topic!=null){
					oidDomain=topic.getOid();
				}
			}
			if(oidDomain!=null && !(oidDomain instanceof NoOid)){
				return oidDomain;
			}
		  def=(AbstractClassDef)def.getExtending();
	  }
		return null;
	}
public Domain getDefinedOid() {
	return oid;
}
public void setOid(Domain oid) {
	this.oid = oid;
}
@Override
public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  throws java.lang.IllegalStateException
{
    super.checkTranslationOf(errs,name,baseName);
    AbstractClassDef baseElement=(AbstractClassDef)getTranslationOf();
    if(baseElement==null) {
        return;
    }
    
    if(isAbstract()!=baseElement.isAbstract()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInAbstractness",getScopedName(),baseElement.getScopedName())));
    }
    if(isFinal()!=baseElement.isFinal()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInFinality",getScopedName(),baseElement.getScopedName())));
    }
    Ili2cSemanticException err=null;
    err=checkElementRef(getDefinedOid(),baseElement.getDefinedOid(),getSourceLine(),"err_diff_oidMismatch");
    if(err!=null) {
        errs.add(err);
    }
    err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseClassMismatch");
    if(err!=null) {
        errs.add(err);
    }
}

}
