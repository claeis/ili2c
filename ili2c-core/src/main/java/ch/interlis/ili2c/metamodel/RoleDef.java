package ch.interlis.ili2c.metamodel;


import java.util.*;


public class RoleDef extends AbstractLeafElement
	implements Extendable
{
	private RoleDef extending;
	private Set<RoleDef> extendedBy=new HashSet<RoleDef>(2);
	private boolean _abstract;
	private boolean _final;
	private boolean hiding=false;
	private boolean extended;
	private boolean ordered;
	private int kind=Kind.eASSOCIATE;
	private Cardinality cardinality;
	private ArrayList<ReferenceType> endv = new ArrayList<ReferenceType>(); // list<ReferenceType tableOrAssociationDef>
	private ObjectPath derivedFrom;
	private String name;
        private boolean isIli23;

	public class Kind
	{
		// a weaker aggregation kind should be a smaller value
		public final static int eASSOCIATE=1;
		public final static int eAGGREGATE=2;
		public final static int eCOMPOSITE=3;
		/** do not instantiate */
		private Kind(){};
	}

	public RoleDef() {
	    // This constructor is not used (was private), but Javabeans require a PUBLIC constructor.
	    // Can be removed if the Javabeans stuff is removed
	    this(true);
	}

	public RoleDef(boolean isIli23) {
		this.isIli23 = isIli23;
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
	    boolean oldValue = _abstract;  // FIXME: missing Javabeans support (or remove). **GV1012
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
	    boolean oldValue = _final;  // FIXME: missing Javabeans support (or remove). **GV1012
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
		if(endv.isEmpty())return null;
		return endv.get(0);
	}
	public Iterator<ReferenceType> iteratorReference()
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
		return endv.isEmpty() ? null : endv.get(0).getReferred();
	}
	public Iterator<AbstractClassDef> iteratorDestination()
	{
		ArrayList<AbstractClassDef> destv = new ArrayList<AbstractClassDef>(endv.size());

		for (int i = 0; i < endv.size(); ++i) {
		    destv.add(endv.get(i).getReferred());
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
			throw new Ili2cSemanticException(getSourceLine(),
				formatMessage ("err_cantExtendFinal", base.toString()));
		}

	  	// check cardinality
		if(cardinality!=null){
	    		if (!base.getCardinality().isGeneralizing(this.getCardinality())){
	      			throw new Ili2cSemanticException (getSourceLine(),formatMessage (
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
    			  throw new Ili2cSemanticException (getSourceLine(),rsrc.getString (
    			    "err_role_UnorderedExtOrdered"));
		}
		// check aggregation kind
    		if (this.kind<base.kind){
    			  throw new Ili2cSemanticException (getSourceLine(),rsrc.getString (
    			    "err_role_WeakerExtStronger"));
		}
		// check destination
		if (!getDestination().isExtending (base.getDestination()))
    		{
     			 throw new Ili2cSemanticException (getSourceLine(),formatMessage (
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
	public Set<RoleDef> getExtensions ()
	{
	    Set<RoleDef> result = new HashSet<RoleDef>();
	    getExtensions_recursiveHelper(result);
	    return result;
	}
	private final void getExtensions_recursiveHelper(Set<RoleDef> s)
	{
		s.add (this);
		Iterator<RoleDef> iter = extendedBy.iterator();
		while (iter.hasNext()){
			iter.next().getExtensions_recursiveHelper(s);
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
	Iterator<Element> rolei = ((AssociationDef) getContainer()).getAttributesAndRoles();
	int i=0;
	while (rolei.hasNext()) {
	    Element obj = rolei.next();
		if (obj instanceof RoleDef) {
			role[i++] = (RoleDef) obj;
		}
	}
	return (role[0] == this) ? role[1] : role[0];
  }
  /** tests if this belongs to an association with no more than two roles.
   */
  public boolean hasOneOppEnd(){
	Iterator<Element> rolei =
		((AssociationDef) this.getContainer()).getAttributesAndRoles();
	int rolec=0;
	while (rolei.hasNext()) {
	    Element obj = rolei.next();
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
public String toString ()
{
  Container<?> cont = getContainer(Viewable.class);

  if (cont == null) {
      return getName();
  }

  return cont.getScopedName(null) + ":" + getName();
}
@Override
protected void linkTranslationOf(Element baseElement)
{
    super.linkTranslationOf(baseElement);

    Iterator<ReferenceType> depIt=iteratorReference();
    Iterator<ReferenceType> baseDepIt=((RoleDef)baseElement).iteratorReference();
    while(true) {
        if(!depIt.hasNext() || !baseDepIt.hasNext()) {
            break;
        }
        ReferenceType dep=depIt.next();
        ReferenceType baseDep=baseDepIt.next();
        dep.linkTranslationOf(baseDep);
    }
    
}
@Override
public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
  throws java.lang.IllegalStateException
{
    super.checkTranslationOf(errs,name,baseName);
    RoleDef baseElement=(RoleDef)getTranslationOf();
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
    err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseRoleMismatch");
    if(err!=null) {
        errs.add(err);
    }
    if(isHiding()!=baseElement.isHiding()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInHiding",getScopedName(),baseElement.getScopedName())));
    }
    if(isOrdered()!=baseElement.isOrdered()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInOrdered",getScopedName(),baseElement.getScopedName())));
    }
    if(getKind()!=baseElement.getKind()) {
        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_roleKindMismatch",getScopedName(),baseElement.getScopedName())));
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
    Iterator<ReferenceType> depIt=iteratorReference();
    Iterator<ReferenceType> baseDepIt=baseElement.iteratorReference();
    while(true) {
        if(!depIt.hasNext() || !baseDepIt.hasNext()) {
            if(depIt.hasNext()!=baseDepIt.hasNext()) {
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_referencedClassMismatch")));
            }
            break;
        }
        ReferenceType dep=depIt.next();
        ReferenceType baseDep=baseDepIt.next();
        dep.checkTranslationOf(errs,getScopedName(),baseElement.getScopedName());
    }
    
    err=Evaluable.checkTranslation(getDerivedFrom(),baseElement.getDerivedFrom(),getSourceLine(),"err_diff_derviedFromMismatch");
    if(err!=null) {
        errs.add(err);
    }
}

}
