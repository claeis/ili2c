/* This file is part of the INTERLIS-Compiler.
 * For more information, please see <http://www.interlis.ch/>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.interlis.ili2c.metamodel;


import java.util.*;

import ch.ehi.basics.logging.EhiLogger;


/** AttributeDef is an abstract class which serves as a common
    superclass for all Interlis attribute definitions.

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public abstract class AttributeDef
  extends AbstractLeafElement
  implements Extendable
{
  protected String        name;
  protected AttributeDef  extending;
  protected boolean       _final;
  protected boolean       _abstract;
  private boolean       _transient;
  protected Set<AttributeDef> extendedBy = new HashSet<AttributeDef>(2);
  protected Type          domain;
	private String ili1Explanation=null;
  protected AttributeDef ()
  {
    name = "";
    extending = null;
    _final = false;
    _abstract = false;
    domain = null;

  }



  /** Returns the value of the <code>name</code> property
      which indicates the name of this attribute without
      any scope prefixes.


      @see #setName(java.lang.String)
  */
  public String getName ()
  {
    return name;
  }



  public String toString ()
  {
    Container<?> cont = getContainer(Viewable.class);

    if (cont == null) {
        return getName();
    }

    return cont.getScopedName(null) + ":" + getName();
  }



  /** Sets the value of the <code>name</code> property.
      Attributes are identified and used by specifying their name.


      <p>In JavaBeans terminology, the <code>name</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param name The new name for this attribute.


      @exception java.lang.IllegalArgumentException if <code>name</code>
                 is <code>null</code>, an empty String, too long
                 or does otherwise not conform to the syntax of
                 acceptable INTERLIS names.


      @exception java.lang.IllegalArgumentException if the name
                 would conflict with another attribute. The
                 only acceptable conflict is with the AttributeDef
                 that this attribute directly extends.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>name</code> property
                 and does not agree with the change.
  */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    checkNameSanity(name, /* empty ok? */ false);

    /* Make sure that the new name does not conflict
       with the name of another Viewable, except the
       one that this object is extending directly.
    */
    checkNameUniqueness (newValue, AttributeDef.class, getRealExtending(),
                         "err_attributeDef_duplicateName");





    fireVetoableChange ("name", oldValue, newValue);

    this.name = newValue;
    firePropertyChange ("name", oldValue, newValue);
  }


  public Element getExtending ()
  {
    return extending;
  }
  public AttributeDef getRootExtending ()
  {
      AttributeDef ret=(AttributeDef)getExtending();
      if(ret!=null){
          while(true){
              Element ret1=ret.getExtending();
              if(ret1==null){
                  break;
              }
              ret=(AttributeDef)ret1;
          }
      }
      return ret;
  }
  
	public Element getRealExtending()
	{
		Element ext=getExtending();
		return (ext!=null) ? ext.getReal() : null;
	}



  /** @return whether or not <code>this</code> is extending
              <code>ad</code>
  */
  public boolean isExtendingIndirectly (Element ext)
  {
    for (AttributeDef parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == ext) {
        return true;
    }
    }
    return false;
  }


  public boolean isDependentOn (Element e)
  {
    if (e instanceof AttributeDef) {
      boolean i = isExtendingIndirectly(e);
      return i;
    }

    return false;
  }



  /* Documentation Note
     ------------------
     Make sure to propagate manually any changes
     to the documentation for the AttributeDef subclasses.
  */
  /** Sets the AttributeDef which <code>this</code> extends. This
      call will make this AttributeDef's domain extend the domain
      of <code>ext</code> as well.

      <p>In JavaBeans terminology, the <code>extending</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param ext The new AttributeDef being extended, or <code>null</code>
                 if none is being extended.


      @exception java.lang.IllegalArgumentException if <code>ext</code>
                 is declared as <code>final</code>.


      @exception java.lang.IllegalArgumentException if <code>ext</code>
                 is extending this AttributeDef, be it directly or
                 indirectly. Cyclic extension graphs are not permitted.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for
                 changes of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setExtending (Element ext)
    throws java.beans.PropertyVetoException
  {
    AttributeDef oldValue = extending;
    AttributeDef newValue = (AttributeDef) ext;


    if (oldValue == newValue) {
        return; /* nothing needs to be done */
    }

    if ((newValue != null) && newValue.isFinal()) {
        throw new IllegalArgumentException(
            formatMessage ("err_cantExtendFinal", newValue.toString()));
    }

    /* Ensure that the extension graph will be acyclic. */
    if ((newValue != null) && newValue.isExtendingIndirectly(this))
    {
      throw new IllegalArgumentException (
        formatMessage ("err_cyclicExtension", this.toString(),
                       newValue.toString()));
    }


    fireVetoableChange ("extending", oldValue, newValue);

	if (domain != null)
	{
	  if (newValue == null) {
        domain.setExtending (null);
    } else {
        domain.setExtending (newValue.getDomain());
    }
	}

    if (oldValue != null) {
        oldValue.extendedBy.remove(this);
    }
    extending = newValue;
    if (newValue != null) {
        newValue.extendedBy.add(this);
    }


    firePropertyChange ("extending", oldValue, newValue);
  }


  /** Returns whether this attribute is abstract or can have
      instances.

      @return <code>true</code> if the view is abstract,
              <code>false</code> if instances are acceptable.

      @see #setAbstract(boolean)
  */
  public boolean isAbstract ()
  {
    return _abstract;
  }



  /** Sets the value of the <code>abstract</code> property;
      an abstract attribute can not have instances.


      <p>In JavaBeans terminology, the <code>abstract</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param abst Pass <code>true</code> to make the attribute
                  abstract, pass <code>false</code> to make
                  the attribute instantiable.


      @exception java.lang.IllegalArgumentException if this
                 attribute is final and <code>abst</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
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

    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isFinal()) {
        throw new IllegalArgumentException(
              rsrc.getString("err_abstractFinal"));
    }


    if ((newValue == false) && (domain != null) && domain.isAbstract()) {
        throw new IllegalArgumentException (formatMessage (
            "err_attributeDef_concreteWithAbstractDomain", this.toString()));
    }


    fireVetoableChange("abstract", oldValue, newValue);
    _abstract = newValue;
    firePropertyChange("abstract", oldValue, newValue);
  }



  public boolean isFinal()
  {
    return _final;
  }


  /** Sets the value of the <code>final</code> property;
      a final attribute can not be extended by other
      attributes.


      <p>In JavaBeans terminology, the <code>final</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param fin Pass <code>true</code> to make the attribute
                 final, pass <code>false</code> to allow for
                 extensions.

      @exception java.lang.IllegalArgumentException if this
                 attribute is abstract and <code>fin</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.lang.IllegalArgumentException if
                 <code>fin</code> is <code>true</code> and
                 there exists another attribute which extends
                 this view.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>final</code> property
                 and does not agree with the change.
  */
  public void setFinal(boolean fin)
    throws java.beans.PropertyVetoException
  {
    boolean oldValue = _final;
    boolean newValue = fin;

    /* Check for cases in which there is nothing to do. */
    if (oldValue == newValue) {
        return;
    }

    /* Can not be ABSTRACT and FINAL at the same time. */
    if ((newValue == true) && isAbstract()) {
        throw new IllegalArgumentException(
              rsrc.getString("err_abstractFinal"));
    }



    /* Can't make this final as long as there exists another
       object that extends this one. */
    if ((newValue == true) && !extendedBy.isEmpty()) {
        throw new IllegalArgumentException(formatMessage(
            "err_cantMakeExtendedFinal",
            this.toString(),
            extendedBy.iterator().next().toString()));
    }

    /* Set value and inform interested listeners. */
    fireVetoableChange("final", oldValue, newValue);
    _final = newValue;
    firePropertyChange("final", oldValue, newValue);
  }



  /** Walks the extension hierarchy to determine whether or not <code>this</code>
      is extending <code>ext</code>, be the extension directly or indirectly.
      Any Extendable is extending itself. In other words, this function
      corresponds to the <em>reflexive-transitive closure</em> of the relation
      determined by the <code>setExtending</code> operation.
  */
  public boolean isExtending (Element ext)
  {
    for (AttributeDef parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == ext) {
        return true;
    }
    }

    return false;
  }



  /** Calculates a Set of all Extendables that are extending
      this Extendable, be it through direct extension or be
      it in several steps.  The result consists of
      all directly extending objects plus all that directly
      extend those, plus ..., etc. In other words,
      this method calculates the transitive closure
      of the <em>extending</em> relation.

      @return A new Set that belongs to the caller; the caller
              is thus free to modify it according to its needs.
              Changes in the result will not have any effect
              on the <em>extending</em> property.
  */
  public Set<AttributeDef> getExtensions()
  {
    Set<AttributeDef> result = new HashSet<AttributeDef>();
    getExtensions_recursiveHelper(result);
    return result;
  }



  /** @see getExtensions() */
  private final void getExtensions_recursiveHelper (Set<AttributeDef> s)
  {
    s.add (this);
    Iterator<AttributeDef> iter = extendedBy.iterator();
    while (iter.hasNext()) {
        iter.next().getExtensions_recursiveHelper (s);
    }
  }

  public Type getDomain ()
  {
    return domain;
  }



  public Type getDomainResolvingAliases ()
  {
    return Type.findReal (domain);
  }
  public Type getDomainResolvingAll()
  {
	Type type=domain;
	if(type==null && this instanceof LocalAttribute){
		Evaluable[] ev = (((LocalAttribute)this).getBasePaths());
		type=((ObjectPath)ev[0]).getType();
	}
    return Type.findReal (type);
  }


  /* Documentation Note
     ------------------
     Make sure to propagate manually any changes
     to the documentation for the AttributeDef subclasses.
  */
  /** Changes the domain of an attribute. The domain is a restriction
      on the set of possible values that this attribute can have.

      <p>In JavaBeans terminology, the <code>domain</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.


      @param domain The new domain of this attribute.


      @exception java.lang.IllegalArgumentException if
                 <code>domain</code> is <code>null</code>.


      @exception java.lang.IllegalArgumentException if
                 <code>domain</code> is abstract, but this
                 attribute is not abstract. Only abstract
                 attributes can have abstract domains.


      @exception java.beans.PropertyVetoException if some
                 VetoableChangeListener has registered for changes
                 of the <code>extending</code> property
                 and does not agree with the change.
  */
  public void setDomain (Type domain)
  throws java.beans.PropertyVetoException
  {
	  setDomain(domain,false);
  }
  public void setDomain (Type domain,boolean acceptAbstract)
    throws java.beans.PropertyVetoException
  {
    Type oldValue = this.domain;
    Type newValue = domain;

    if (newValue == null) {
        throw new IllegalArgumentException (rsrc.getString ("err_nullNotAcceptable"));
    }

    if (oldValue == newValue) {
        return;
    }

    fireVetoableChange ("domain", oldValue, newValue);


    // 20150313 do not touch this.domain.extending here (keep it the same logic as in class Domain)
    // newValue.extending is already set, or will be set, when calling this.setExtending() 
    //if (extending == null) {
    //    newValue.setExtending (null);
    //} else {
    //    newValue.setExtending (extending.getDomain());
    //}


    this.domain = newValue;
    firePropertyChange ("domain", oldValue, newValue);
    
    if(!acceptAbstract){
    	StringBuilder err=new StringBuilder();
        if ((newValue != null)
                && newValue.isAbstract(err)
                && !this.isAbstract()) {
            throw new Ili2cSemanticException (getSourceLine(),formatMessage (
                "err_attributeDef_domainIsAbstractButAttrIsNot", this.toString(),err.toString()));
        }
    }
    
  }


  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with)) {
        return false;
    }

    Type myDomain = this.getDomain ();
    Type otherDomain = ((AttributeDef) with).getDomain ();

    /* Probably only for myDomain == otherDomain == null */
    if (myDomain == otherDomain) {
        return true;
    }

    if (myDomain == null) {
        return false;
    }

    if (!myDomain.checkStructuralEquivalence (otherDomain))
    {
      EhiLogger.logError(formatMessage ("err_diff_attributeType", this.toString(), with.toString()));
      return false;
    }

    return true;
  }

	/** get ili explanation. Text in // after Type.
	 * @return null or ili1-explanation (without surrounding //).
	 */
	public String getExplanation() {
		return ili1Explanation;
	}

	public void setExplanation(String string) {
		ili1Explanation = string;
	}



	public boolean isTransient() {
		return _transient;
	}



	public void setTransient(boolean _transient) {
		this._transient = _transient;
	}

	public boolean isDomainBoolean(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		while(type instanceof TypeAlias) {
			if (((TypeAlias) type).getAliasing() == td.INTERLIS.BOOLEAN) {
				return true;
			}
			type=((TypeAlias) type).getAliasing().getType();
		}
		return false;
		
	}
	public boolean isDomainIli1Date(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		while(type instanceof TypeAlias) {
			if (((TypeAlias) type).getAliasing() == td.INTERLIS.INTERLIS_1_DATE) {
				return true;
			}
			type=((TypeAlias) type).getAliasing().getType();
		}
		return false;
	}
	public boolean isDomainIli2Date(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		if (type instanceof TypeAlias){
			while(type instanceof TypeAlias) {
				if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlDate) {
					return true;
				}
				type=((TypeAlias) type).getAliasing().getType();
			}
		}
		if(type instanceof FormattedType){
			FormattedType ft=(FormattedType)type;
			if(ft.getDefinedBaseDomain()== td.INTERLIS.XmlDate){
				return true;
			}
		}
		return false;
	}
	public boolean isDomainIli2DateTime(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		if (type instanceof TypeAlias){
			while(type instanceof TypeAlias) {
				if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlDateTime) {
					return true;
				}
				type=((TypeAlias) type).getAliasing().getType();
			}
		}
		if(type instanceof FormattedType){
			FormattedType ft=(FormattedType)type;
			if(ft.getDefinedBaseDomain()== td.INTERLIS.XmlDateTime){
				return true;
			}
		}
		return false;
	}
	public boolean isDomainIli2Time(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		if (type instanceof TypeAlias){
			while(type instanceof TypeAlias) {
				if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlTime) {
					return true;
				}
				type=((TypeAlias) type).getAliasing().getType();
			}
		}
		if(type instanceof FormattedType){
			FormattedType ft=(FormattedType)type;
			if(ft.getDefinedBaseDomain()== td.INTERLIS.XmlTime){
				return true;
			}
		}
		return false;
	}
	public boolean isDomainIliUuid(){
		TransferDescription td=(TransferDescription) getContainer(TransferDescription.class);
		Type type=getDomain();
		if (type instanceof TypeAlias){
			while(type instanceof TypeAlias) {
				if (((TypeAlias) type).getAliasing() == td.INTERLIS.UUIDOID) {
					return true;
				}
				type=((TypeAlias) type).getAliasing().getType();
			}
		}
		return false;
	}
	  @Override
	    public void setSourceLine(int sourceLine) {
	        super.setSourceLine(sourceLine);
	        if(domain!=null){
	            domain.setSourceLine(sourceLine);
	        }
	    }

	@Override
  	protected void linkTranslationOf(Element baseElement)
  	{
	    super.linkTranslationOf(baseElement);
		Type type=getDomain();
		if(type==null){
			return; // FIXME type should not be null; fix in parser/viewAttributes() (near attrib.setTypeProxy(true))
		}
		Type baseType=((AttributeDef) baseElement).getDomain();
		if(type.getClass().equals(baseType.getClass())) {
	        type.linkTranslationOf(baseType);
		}
  	}
    @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        Type type=getDomain();
        if(type==null){
            return; // FIXME type should not be null; fix in parser/viewAttributes() (near attrib.setTypeProxy(true))
        }
        AttributeDef baseElement=(AttributeDef)getTranslationOf();
        if(baseElement==null) {
            return;
        }
        if(isAbstract()!=baseElement.isAbstract()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInAbstractness",getScopedName(),baseElement.getScopedName())));
        }
        if(isFinal()!=baseElement.isFinal()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInFinality",getScopedName(),baseElement.getScopedName())));
        }
        if(isTransient()!=baseElement.isTransient()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInTransientness",getScopedName(),baseElement.getScopedName())));
        }
        if(ili1Explanation==null && baseElement.ili1Explanation==null){
        }else {
            if(ili1Explanation==null || baseElement.ili1Explanation==null){
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_attributeType",getScopedName(),baseElement.getScopedName())));
            }else if(ili1Explanation.equals(baseElement.ili1Explanation)){
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_attributeType",getScopedName(),baseElement.getScopedName())));
            }
        }
        
        Type baseType=baseElement.getDomain();
        if(type.getClass()!=baseType.getClass()){
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_attributeType",getScopedName(),baseElement.getScopedName())));
            return;
        }
        if (type instanceof TypeAlias){
            if(((TypeAlias)type).getAliasing().getTranslationOfOrSame()!=((TypeAlias)baseType).getAliasing().getTranslationOfOrSame()){
                errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_attributeType",getScopedName(),((AttributeDef) baseElement).getScopedName())));
                return;
            }
        }
        try {
            type.checkTranslationOf(errs,getScopedName(),baseElement.getScopedName());
            if(type instanceof AbstractCoordType) {
                String crs=((AbstractCoordType) type).getCrs(this);
                String originCrs=((AbstractCoordType) baseType).getCrs(baseElement);
                if(crs==null && originCrs==null) {
                    
                }else {
                    if(crs==null || originCrs==null) {
                        throw new Ili2cSemanticException();
                    }
                    if(!crs.equals(originCrs)) {
                        throw new Ili2cSemanticException();
                    }
                }
            }
        }catch(Ili2cSemanticException ex) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_attributeType",getScopedName(),baseElement.getScopedName())));
        }
    }
}
