/*****************************************************************************
 *
 * Unit.java
 * ---------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.util.*;

/** Unit is an abstract class which serves as a common
    superclass for all Interlis unit definitions.

    @version   April 7, 1999
    @author    Sascha Brawer
*/
public abstract class Unit extends AbstractLeafElement implements Extendable
{
  protected String        name = "";
  protected String        docName = "";
  protected Unit		  extending = null;
  protected Set<Unit>     extendedBy = new HashSet<Unit>(2);
  protected boolean       _abstract = false;
  protected boolean       _final = false;

  public Unit() {
  }


  public String toString()
  {
    return "UNIT " + getScopedName(null);
  }


  public String getName() {
    return name;
  }


  public String getDocName() {
    return docName;
  }

  public void setName(String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;

    boolean noDocName = "".equals(docName);

    checkNameSanity(name, /* empty ok? */ false);

    /* JavaBeans requires that the value be changed between
       firing VetoableChangeEvent and PropertyChangeEvent
       objects. */
    fireVetoableChange("name", oldValue, newValue);
    if (noDocName)
      fireVetoableChange("docName", "", newValue);

    this.name = newValue;
    if (noDocName)
      this.docName = newValue;

    firePropertyChange("name", oldValue, newValue);
    if (noDocName)
      firePropertyChange("docName", "", newValue);
  }

  public void setDocName(String docName)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.docName;
    String newValue = docName;

    if (docName == null)
      throw new IllegalArgumentException();

    fireVetoableChange("docName", oldValue, newValue);
    this.docName = newValue;
    firePropertyChange("docName", oldValue, newValue);
  }


  public Element getExtending() {
    return extending;
  }

	public Element getRealExtending()
	{
		Element ext=getExtending();
		return (ext!=null) ? ext.getReal() : null;
	}
  public Set<Unit> getExtensions ()
  {
    Set<Unit> result = new HashSet<Unit>();
    getExtensions_recursiveHelper(result);
    return result;
  }


  /** @see getExtensions() */
  private final void getExtensions_recursiveHelper(Set<Unit> s)
  {
    s.add(this);
    Iterator<Unit> iter = extendedBy.iterator();
    while (iter.hasNext())
      iter.next().getExtensions_recursiveHelper(s);
  }
  public boolean isExtending (Element extendee)
  {
    for (Unit parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == extendee)
        return true;
    }

    return false;
  }

  /** @return whether or not <code>this</code> is extending
              <code>u</code>.
  */
  public boolean isExtendingIndirectly(Unit u) {
    for (Unit parent = this; parent != null;
         parent = parent.extending)
    {
      if (parent == u)
        return true;
    }
    return false;
  }


  public boolean isDependentOn(Element e) {
    if (e instanceof Unit) {
      boolean i = isExtendingIndirectly((Unit) e);
      return i;
    }

    return false;
  }


  protected Unit findCommonBase (Unit u)
  {
    for (Unit p = u; p != null; p = p.extending)
    {
      if (p.isExtendingIndirectly (this))
        return p;

      if (this.isExtendingIndirectly (p))
        return this;
    }

    return null;
  }


  /** Sets the Unit which <code>this</code> extends.

      @param ext The new Unit being extended, or <code>null</code>.
      @exception java.lang.IllegalArgumentException if <code>ext</code>
                 is declared as <code>final</code>.
  */
  public void setExtending(Element ext1)
    throws java.beans.PropertyVetoException
  {
  	Unit ext = (Unit)ext1;
	Unit oldValue = extending;
	Unit newValue = ext;

    if (oldValue == newValue)
      return; /* nothing needs to be done */

	checkExtending(ext);

    fireVetoableChange("extending", oldValue, newValue);

    if (extending != null)
      extending.extendedBy.remove(this);
    extending = ext;
    if (ext != null)
      ext.extendedBy.add(this);

    firePropertyChange("extending", oldValue, newValue);
  }

	/** template method
	 */
	protected void checkExtending(Unit ext){
		if ((ext != null) && (ext._final))
		  throw new IllegalArgumentException (formatMessage (
			"err_cantExtendFinal", ext.toString()));


		if ((ext != null) && (!ext.isAbstract()))
		  throw new IllegalArgumentException (formatMessage (
			"err_extendingConcreteUnit", ext.getScopedName(null)));

		/* Ensure that the extension graph will be acyclic. */
		if ((ext != null) && ext.isExtendingIndirectly(this))
		  throw new IllegalArgumentException("The unit \""
			+ getName() + "\" can not extend \""
			+ ext.getName() + "\", because \""
			+ getName() + "\" is already extending \""
			+ ext.getName() + "\"; cyclic extension graphs are not allowed.");

	}
  public boolean isAbstract()
  {
    return _abstract;
  }


  public void setAbstract(boolean abs)
    throws java.beans.PropertyVetoException
  {
    if (abs == _abstract)
      return;

    /* Set value and inform interested listeners. */
    fireVetoableChange("abstract", /* old */ !abs, /* new */ abs);
    _abstract = abs;
    firePropertyChange("abstract", /* old */ !abs, /* new */ abs);
  }


  /** Returns the value of the <code>final</code> property
      which determines whether or not a unit can be extended
      by other units.

      @return <code>true</code> if the unit has been declared
              as <code>FINAL</code>; <code>false</code> if
              the unit can be extended by other units.
  */
  public boolean isFinal()
  {
    return _final;
  }


  /** Sets the value of the <code>final</code> property;
      a final unit can not be extended by other units.

      <p>In JavaBeans terminology, the <code>final</code>
      property is both <em>bound</em> and <em>constrained</em>.
      This means that any interested party can ask for being
      informed about changes of the property value by registering
      as a <code>PropertyChangeListener</code>. In addition,
      subscribers may oppose to changes by registering as a
      <code>VetoableChangeListener</code>.

      @param fin Pass <code>true</code> to make the unit
                 final, pass <code>false</code> to allow for
                 extensions.

      @exception java.lang.IllegalArgumentException if this
                 unit is abstract and <code>fin</code> is
                 <code>true</code>, because it would not make
                 sense to declare anything as both
                 <code>ABSTRACT</code> and <code>FINAL</code>.

      @exception java.lang.IllegalArgumentException if
                 <code>fin</code> is <code>true</code> and
                 there exists another unit which extends this
                 unit.

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
    if (oldValue == newValue)
      return;

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

    fireVetoableChange("final", oldValue, newValue);
    _final = newValue;
    firePropertyChange("final", oldValue, newValue);
  }
  @Override
  public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    throws java.lang.IllegalStateException
  {
      super.checkTranslationOf(errs,name,baseName);
      Unit baseElement=(Unit)getTranslationOf();
      if(baseElement==null) {
          return;
      }
      if(!getClass().equals(baseElement.getClass())) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchUnitDef",getScopedName(),baseElement.getScopedName())));
          return;
      }
      if(isAbstract()!=baseElement.isAbstract()) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInAbstractness",getScopedName(),baseElement.getScopedName())));
      }
      if(isFinal()!=baseElement.isFinal()) {
          errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInFinality",getScopedName(),baseElement.getScopedName())));
      }
      Ili2cSemanticException err=null;
      err=checkElementRef(getExtending(),baseElement.getExtending(),getSourceLine(),"err_diff_baseUnitMismatch");
      if(err!=null) {
          errs.add(err);
      }
  }
}
