
package ch.interlis.ili2c.metamodel;
import java.util.*;

public abstract class AbstractClassDef extends Viewable
{
	private Domain oid=null;
  /** backlink to RoleDef, if this ClassDef or AssociationDef is used as target
   *  of a RoleDef.
   */
  private Set targetForRoles = new HashSet(2); // set<RoleDef>
  /** adds a backlink to the given RoleDef that has this ClassDef or
   *  AssociationDef as a target.
   */
  public void addTargetForRole(RoleDef role){


    // check role name is unique with respect to attributes and roles in other roles target
    AssociationDef assoc=(AssociationDef)role.getContainer();
    RoleDef rootRole=role.getRootExtending();
    if(rootRole==null)rootRole=role;
    Iterator iter = assoc.getAttributesAndRoles();
    while (iter.hasNext()){
      Object obj = iter.next();
      if(obj instanceof RoleDef){
        RoleDef otherRole=(RoleDef)obj;
        if(otherRole==role)continue;
        AbstractClassDef targetClass=otherRole.getDestination();
        Iterator attri=targetClass.getAttributesAndRoles();
        while (attri.hasNext()){
        	Element attrOrRole = (Element)attri.next();
            String attrName=attrOrRole.getName();
            String roleName=role.getName();
            if(attrOrRole.getName().equals(role.getName())){
                throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
                  "err_abstractClassDef_nameConflictInOtherRoleTarget",
                  role.getName(),
                  otherRole.getName(),targetClass.getName()));
            }
        }
        Iterator rolei=targetClass.getOpposideRoles();
        while (rolei.hasNext()){
	      RoleDef targetOppRole = (RoleDef)rolei.next();
	      RoleDef rootTargetOppRole = targetOppRole.getRootExtending();
	      if(rootTargetOppRole==null)rootTargetOppRole=targetOppRole;
	      if(targetOppRole.getName().equals(role.getName())
                  && rootRole!=rootTargetOppRole){
                throw new Ili2cSemanticException (role.getSourceLine(),formatMessage (
                  "err_abstractClassDef_nameConflictInOtherRoleTarget",
                  role.getName(),
                  otherRole.getName(),targetClass.getName()));
	      }
        }
      }
    }

    targetForRoles.add(role);
  }
  /** get the list of roles that target this.
   *  Doesn't return inherited ones.
   * @returns list<RoleDef>
   */
  public Iterator getDefinedTargetForRoles()
  {
  	return targetForRoles.iterator();
  }
  /** get the list of roles that target this.
   *  Returns also inherited ones.
   * @returns list<RoleDef>
   */
  public Iterator getTargetForRoles()
  {
	    List result = new LinkedList ();
	    Map  mostDerived = new HashMap ();
	    for (AbstractClassDef v = this; v != null; v = (AbstractClassDef) v.getRealExtending())
	    {
	      List attrsOfV_reversed = new LinkedList (); /* a stack */
	      Iterator iter = v.targetForRoles.iterator ();
	      while (iter.hasNext ())
	      {
	        RoleDef role = (RoleDef)iter.next();
	          /* Find least derived; put it into the map if there is not
	             already a even more derived role. */
	          Extendable leastDerived = role;
	          Extendable leastDerivedParent = null;
	          while ((leastDerivedParent = (Extendable) leastDerived.getRealExtending()) != null)
	            leastDerived = leastDerivedParent;


	          if (!mostDerived.containsKey (leastDerived))
	            mostDerived.put (leastDerived, role);


	          /* If this mentioning of role is the least derived one, this
	             is the time to add it to the stack of encountered attributes. */
	          if (role == leastDerived)
	          {
	            attrsOfV_reversed.add (/* at frontmost position */ 0,
	              mostDerived.get (role));
	          }
	      }


	      Iterator attrsOfV_iter = attrsOfV_reversed.iterator();
	      while (attrsOfV_iter.hasNext())
	        result.add (/* at frontmost position */ 0, attrsOfV_iter.next());
	    }
	    return result.iterator ();
  }
  public Iterator getOpposideRoles()
  {
    List result = new LinkedList ();
    Iterator rolei=getTargetForRoles();
    while(rolei.hasNext()){
      RoleDef role=(RoleDef)rolei.next();
      AssociationDef assoc=(AssociationDef)role.getContainer();
      Iterator iter = assoc.getAttributesAndRoles();
      while (iter.hasNext()){
        Object oppRole = iter.next();
        if(oppRole instanceof RoleDef){
          if(oppRole==role)continue;
          result.add(oppRole);
        }
      }
    }

    return result.iterator ();
  }

  /** find the opposide role with the given Name.
   * @returns Null or found RoleDef.
   */
  public RoleDef findOpposideRole(String roleName){
	    if (roleName == null)
	      return null;


	    Iterator it = getOpposideRoles();
	    while (it.hasNext ())
	    {
	      RoleDef e = (RoleDef) it.next ();
	      if (roleName.equals (e.getName()))
	        return e;
	    }
      return null;
  }
  /** get the list of associations that have no link object,
   *  but are embedded into this. Doesn't return inherited ones.
   * @returns list<RoleDef>
   */
  public List getDefinedLightweightAssociations(){
	List assocv=new ArrayList();
	Iterator it = getDefinedTargetForRoles();
	while (it.hasNext ())
	{
	  RoleDef e = (RoleDef) it.next ();
	  if (e.isAssociationEmbedded()){
		assocv.add(e);
	  }
	}
	return assocv;
  }
  /** get the list of associations that have no link object,
   *  but are embedded into this. Returns also inherited ones.
   * @returns list<RoleDef>
   */
  public List getLightweightAssociations(){
    List assocv=new ArrayList();
    Iterator it = getTargetForRoles();
    while (it.hasNext ())
    {
      RoleDef e = (RoleDef) it.next ();
      if (e.isAssociationEmbedded()){
        assocv.add(e);
      }
    }

    return assocv;
  }
public Domain getOid() {
	return oid;
}
public void setOid(Domain oid) {
	this.oid = oid;
}
}
