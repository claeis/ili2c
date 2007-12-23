package ch.interlis.ili2c.metamodel;


import java.util.*;


public class RoleDef extends AbstractLeafElement
	implements Extendable
{
	private RoleDef extending;
	private Set extendedBy=new HashSet(2); // Set<RoleDef>
	private boolean _abstract;
	private boolean _final;
	private boolean hiding=false;
	private boolean extended;
	private boolean ordered;
	private int kind=Kind.eASSOCIATE;
	private Cardinality cardinality;
	private ArrayList endv=new ArrayList(); // list<ReferenceType tableOrAssociationDef>
	private ObjectPath derivedFrom;
	private String name;

	public class Kind
	{
		// a weaker aggregation kind should be a smaller value
		public final static int eASSOCIATE=1;
		public final static int eAGGREGATE=2;
		public final static int eCOMPOSITE=3;
		/** do not instantiate */
		private Kind(){};
	}
	private RoleDef(){};
	private boolean isIli23=true;
	public RoleDef(boolean isIli23){
		this.isIli23=isIli23;
	}
	public void setExtended(boolean v)
	{
		extended=v;
	}
	public boolean isExtended()
	{
		return extended;
	}
	public void setAbstract(boolean abst)
	{
	    boolean oldValue = _abstract;
	    boolean newValue = abst;

		/* Can not be ABSTRACT and FINAL at the same time. */
		if ((newValue == true) && isFinal()){
			throw new IllegalArgumentException(
				rsrc.getString("err_abstractFinal")
				);
		}

		_abstract = newValue;
	}
	public boolean isAbstract()
	{
		return _abstract;
	}
	public void setFinal(boolean fin)
	{
	    boolean oldValue = _final;
	    boolean newValue = fin;


	    /* Can not be ABSTRACT and FINAL at the same time. */
	    if ((newValue == true) && isAbstract())
	      throw new IllegalArgumentException(
		  rsrc.getString("err_abstractFinal"));


	    /* Can't make this final as long as there exists another
	       object that extends this one. */
	    if ((newValue == true) && !extendedBy.isEmpty())
	      throw new IllegalArgumentException(formatMessage(
		"err_cantMakeExtendedFinal",
		this.toString(),
		extendedBy.iterator().next().toString()));

	    _final = newValue;
	}
	public boolean isFinal()
	{
		return _final;
	}
	public void setOrdered(boolean v)
	{
		ordered=v;
	}
	public boolean isOrdered()
	{
		return ordered;
	}
	public void setKind(int v)
	{
		kind=v;
	}
	public int getKind()
	{
		return kind;
	}
	public void setCardinality(Cardinality v)
	{
		cardinality=v;
	}
	public boolean containsCardinality()
	{
		return cardinality!=null;
	}
	public Cardinality getCardinality()
	{
		if(getExtending()!=null && cardinality==null){
			return ((RoleDef)getExtending()).getCardinality();
		}
		if(cardinality==null){
			if(getKind()==Kind.eCOMPOSITE){
				if(isIli23){
					return new Cardinality(0,1);
				}
				return new Cardinality(1,1);
			}
			return new Cardinality(0,Cardinality.UNBOUND);
		}
		return cardinality;
	}
	public Cardinality getDefinedCardinality()
	{
		return cardinality;
	}
	/** @deprecated
	 */
	public ReferenceType getReference()
	{
		if(endv.size()==0)return null;
		return (ReferenceType)endv.get(0);
	}
	public Iterator iteratorReference()
	{
		return endv.iterator();
	}
	/** @deprecated
	 */
	public void setReference(ReferenceType ref)
	{
		endv.clear();
		endv.add(ref);
                // backlink target class to this.
                // // end.getReferred().addTargetForRole(this);
                // this is done in
                // AssociationDef.fixupRoles(), because in case of an annonyous
                // associationdef the base associationdef is
                // not known.
	}
	public void addReference(ReferenceType ref)
	{
		endv.add(ref);
                // backlink target class to this.
                // // end.getReferred().addTargetForRole(this);
                // this is done in
                // AssociationDef.fixupRoles(), because in case of an annonyous
                // associationdef the base associationdef is
                // not known.
	}
	/** @deprecated
	 */
	public AbstractClassDef getDestination()
	{
		if(endv.size()==0)return null;
		return ((ReferenceType)endv.get(0)).getReferred();
	}
	public Iterator iteratorDestination()
	{
		ArrayList destv=new ArrayList(endv.size());
		Iterator endi=endv.iterator();
		while(endi.hasNext()){
			ReferenceType end=(ReferenceType)endi.next();
			destv.add(end.getReferred());
		}
		return destv.iterator();
	}
	public boolean isExternal()
	{
		return getReference().isExternal();
	}
	public void setDerivedFrom(ObjectPath v)
	{
		derivedFrom=v;
	}
	public ObjectPath getDerivedFrom()
	{
		return derivedFrom;
	}
	public void setName(String v)
	{
		name=v;
	}
	public String getName()
	{
		return name;
	}
	public boolean isExtending (Element ext)
	{
	    for (RoleDef parent = this; parent != null;
		 parent = parent.extending)
	    {
	      if (parent == ext)
		return true;
	    }

	    return false;
	}
	public void setExtending (Element ext)
	{
		RoleDef base=(RoleDef)ext;
		if (base.isFinal()){
			throw new IllegalArgumentException(
				formatMessage ("err_cantExtendFinal", base.toString()));
		}

	  	// check cardinality
		if(cardinality!=null){
	    		if (!base.getCardinality().isGeneralizing(this.getCardinality())){
	      			throw new IllegalArgumentException (formatMessage (
			        "err_role_cardExtMismatch",
			        this.getCardinality().toString(), base.getCardinality().toString()));
			}
		}
		// check ordering
		    // compare ordering only if more than one object possible
		Cardinality card;
		if(cardinality!=null){
			card=cardinality;
		}else{
			card=base.getCardinality();
		}
    		if (card.getMaximum()>1 && !this.isOrdered()
			&& base.isOrdered()){
    			  throw new IllegalArgumentException (rsrc.getString (
    			    "err_role_UnorderedExtOrdered"));
		}
		// check aggregation kind
    		if (this.kind<base.kind){
    			  throw new IllegalArgumentException (rsrc.getString (
    			    "err_role_WeakerExtStronger"));
		}
		// check destination
		if (!getDestination().isExtending (base.getDestination()))
    		{
     			 throw new IllegalArgumentException (formatMessage (
     			   "err_role_componentNotExt",
     			   getDestination().toString(), base.getDestination().toString()));
    		}
		if(extending!=null){
			extending.extendedBy.remove(this);
		}
		extending=base;
		extending.extendedBy.add(this);
	}
	public Element getExtending ()
	{
		return extending;
	}
	/** gets the root of the inheritance tree.
	 */
	public RoleDef getRootExtending ()
	{
		RoleDef ret=(RoleDef)getExtending();
		if(ret!=null){
			while(true){
				Element ret1=ret.getExtending();
				if(ret1==null){
					break;
				}
				ret=(RoleDef)ret1;
			}
		}
		return ret;
	}
	public Element getRealExtending()
	{
		Element ext=getExtending();
		return (ext!=null) ? ext.getReal() : null;
	}
	public Set getExtensions ()
	{
	    Set result = new HashSet ();
	    getExtensions_recursiveHelper (result);
	    return result;
	}
	private final void getExtensions_recursiveHelper (Set s)
	{
		s.add (this);
		Iterator iter = extendedBy.iterator();
		while (iter.hasNext()){
			((RoleDef) iter.next()).getExtensions_recursiveHelper (s);
		}
	}
  /** tests, if the association that this role is an end of,
   *  is embedded into the object that this role points to.
   */
  public boolean isAssociationEmbedded(){
    AssociationDef container=(AssociationDef)getContainer();
    return container.getRoleWhereEmbedded()==this;
  }
  /** defines the position of the reference attribute in the source table.
   * The first attribute has index 0. Only used if this represents a Ili1 reference attribute.
   * @returns the index or -1 in case this is not a Ili1 reference attribute
   */
  private int ili1AttrIdx=-1;
  public void setIli1AttrIdx(int ili1AttrIdx){
    this.ili1AttrIdx=ili1AttrIdx;
  }
  public int getIli1AttrIdx(){
    return ili1AttrIdx;
  }
  /** returns an exception if this belongs to an association with more than two roles.
   * may return a roledef of a base association if the is an extended roledef.
   */
  public RoleDef getOppEnd(){
	RoleDef role[] = new RoleDef[2];
	role[0] = null;
	role[1] = null;
	Iterator rolei =
		((AssociationDef) this.getContainer()).getAttributesAndRoles();
	int i=0;
	while (rolei.hasNext()) {
		Object obj = rolei.next();
		if (obj instanceof RoleDef) {
			role[i]=(RoleDef) obj;
			i++;
		}
	}
	if (role[0] == this) {
		return role[1];
	}
	return role[0];
  }
  /** tests if this belongs to an association with no more than two roles.
   */
  public boolean hasOneOppEnd(){
	Iterator rolei =
		((AssociationDef) this.getContainer()).getAttributesAndRoles();
	int rolec=0;
	while (rolei.hasNext()) {
		Object obj = rolei.next();
		if (obj instanceof RoleDef) {
			rolec++;
		}
	}
	return rolec==2;
  }
public boolean isHiding() {
	return hiding;
}
public void setHiding(boolean hiding) {
	this.hiding = hiding;
}
}
