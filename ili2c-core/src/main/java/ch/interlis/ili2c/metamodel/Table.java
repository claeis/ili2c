/*****************************************************************************
 *
 * Table.java
 * ----------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;
import java.util.*;

import ch.ehi.basics.logging.EhiLogger;



/** A table. Please refer to the INTERLIS reference manual to
    learn about the concept of tables.


    @author Sascha Brawer, sb@adasys.ch
 */
public class Table extends AbstractClassDef<AbstractLeafElement>
{
	private boolean ili1Optional=false;
	private boolean ili1LineAttrStruct=false;
  /** Indicates whether or not table instances are identifiable.
      This is the difference between an Interlis <code>TABLE</code>
      and an Interlis <code>STRUCTURE</code>. The default value
      is <code>true</code>, which corresponds to <code>TABLE</code>.
  */
  protected boolean identifiable = true;



  /** Indicates whether or not this class has been created implicitly
      by the compiler to deal with Surfaces and Areas.
  */
  boolean implicit = false;

  /** backlink to CompositionType, if this ClassDef is used as part of
   *  a structure attribute.
   */
  Set<CompositionType> componentFor = new HashSet<CompositionType>(2);

  protected List<Parameter> parameters = new LinkedList<Parameter>();


  protected void throwUponUnwantedAttribute (AttributeDef attr)
  {
  }



  public Table()
  {
  }
  protected Collection<AbstractLeafElement> createElements(){
    return new AbstractCollection<AbstractLeafElement>()
    {
      public Iterator<AbstractLeafElement> iterator ()
      {
        Iterator<AbstractLeafElement>[] it = new Iterator[]
        {
          attributes.iterator(),
          constraints.iterator(),
          parameters.iterator()
        };
        return new CombiningIterator<AbstractLeafElement>(it);
      }



      public int size ()
      {
        return attributes.size()
          + parameters.size()
          + constraints.size();
      }



      public boolean add(AbstractLeafElement o)
      {
        if ((o instanceof UniquenessConstraint) && !Table.this.isIdentifiable()
        		&& !((UniquenessConstraint)o).getLocal())
          throw new Ili2cSemanticException (formatMessage (
            "err_structure_unique",
            Table.this.toString ()));

        if (o instanceof Constraint){
        	((Constraint) o).setNameIdx(constraints.size()+1);
          return constraints.add((Constraint) o);
        }
        if (o instanceof Parameter)
          return parameters.add((Parameter) o);

        /* Note that this check applies for LocalAttribute,
           RelAttribute, CompositionAttribute,
           FunctionAttribute, ... */
        if (o instanceof AttributeDef)
        {
          AttributeDef ad = (AttributeDef) o;

          /* A non-abstract TABLE/STRUCTURE can not contain an abstract
             attribute. */
          if (ad.isAbstract() && !Table.this.isAbstract()){
              throw new Ili2cSemanticException (formatMessage (
                "err_abstractAttrInConcreteContainer",
                Table.this.toString()));
          }
          // check if there is not already a role pointing to this with the same name
	  Iterator<RoleDef> iter = getOpposideRoles();
	  while (iter.hasNext())
	  {
	      RoleDef role = iter.next();
	      if(role.getName().equals(ad.getName())){
                throw new Ili2cSemanticException (formatMessage (
                  "err_abstractClassDef_AttributeNameConflictInTarget",
                  ad.getName(),
                  Table.this.toString()));
	      }
	  }

	      // check extref in structattrs in CLASS/ASSOCIATION
	      if(Table.this.isIdentifiable() && !Table.this.isAbstract() && Table.this.getContainer() instanceof Topic){
	      	AbstractPatternDef.checkTopicDepOfAttr((Topic)Table.this.getContainer(),ad,ad.getName());
	      }

          throwUponUnwantedAttribute (ad);
        }


        if (o instanceof LocalAttribute)
          return attributes.add((LocalAttribute) o);

        if (o == null)
          throw new Ili2cSemanticException (
            rsrc.getString ("err_nullNotAcceptable"));


        throw new ClassCastException();
      }
    };
  }



  /** Returns a string that consists of either <code>CLASS</code>
      or <code>STRUCTURE</code> followed by a space and the fully
      scoped name of this class/structure.
  */
  public String toString ()
  {
  	if(isAlias()){
  		return ((Table)getReal()).toString();
  	}else{
	    return (isIdentifiable() ? "CLASS " : "STRUCTURE ")
 	     + getScopedName(null);
  	}
  }



  /** Some structures, such as those for the geometry of a
      surface or area, are not explicitly specified by the
      description, but are inserted by the INTERLIS compiler
      instead.


      @return <code>true</code> if this table has been inserted
      implicitly by the compiler into the description.
  */
  public boolean isImplicit ()
  {
  	if(isAlias()){
  		return ((Table)getReal()).isImplicit();
  	}else{
	    return implicit;
  	}
  }



  public boolean isIdentifiable ()
  {
  	if(isAlias()){
  		return ((Table)getReal()).isIdentifiable();
  	}else{
	    return identifiable;
  	}
  }



  public void setIdentifiable (boolean identifiable)
    throws java.beans.PropertyVetoException
  {
  	if(isAlias()){
  		((Table)getReal()).setIdentifiable(identifiable);
  		return;
  	}else{
	    boolean oldValue = this.identifiable;
	    boolean newValue = identifiable;


	    /* Check for cases in which there is nothing to do. */
	    if (oldValue == newValue)
	      return;


	    fireVetoableChange("identifiable", oldValue, newValue);
	    this.identifiable = newValue;
	    firePropertyChange("identifiable", oldValue, newValue);
  	}
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
  	if(isAlias()){
  		((Table)getReal()).setAbstract(abst);
  		return;
  	}else{
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
	      throw new IllegalArgumentException (formatMessage ("err_table_concreteOutsideTopic",
	                                                         this.toString ()));
	    }


	    super.setAbstract (newValue);
  	}
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
  	if(isAlias()){
  		((Table)getReal()).setExtending(extending);
  		return;
  	}else{
	    /* The cast in the assignment to newValue throws a ClassCastException
	       if extending is neither null nor an instance of Table.
	       This is exactly what the API documentation specifies.
	    */
	    Table oldValue = (Table) this.extending;
	    Table newValue = (Table) extending;


	    if (oldValue == newValue)
	      return; /* nothing has to be done */


	    if ((newValue != null)
	        && !isIdentifiable()
	        && newValue.isIdentifiable())
	    {
	      throw new IllegalArgumentException (formatMessage (
	        "err_structureCantExtendTable",
	        this.toString(),
	        newValue.toString()));
	    }


	    super.setExtending (newValue);
  	}
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
	if (getExtending() != null) {
		Topic myTopic = (Topic) getContainer(Topic.class);
		// is this an EXTENDED class?
		if (isExtended()) {
			Topic baseTopic = (Topic) getExtending().getContainer(Topic.class);

			// this class defined at model level?
			if (myTopic == null)
				throw new IllegalArgumentException(
					formatMessage(
						"err_table_extendedOutsideTopic",
						this.toString(),
						getExtending().toString(),
						this.toString()));
			// base class defined at model level?
			if (baseTopic == null)
				throw new IllegalArgumentException(
					formatMessage(
						"err_table_extendedOutsideTopic",
						this.toString(),
						getExtending().toString(),
						getExtending().toString()));

			// is this class in a topic that does not extend the topic of the base class?
			if (!(myTopic.isExtending(baseTopic)))
				throw new IllegalArgumentException(
					formatMessage(
						"err_table_extendedButTopicsDont",
						this.toString(),
						getExtending().toString(),
						myTopic.toString(),
						baseTopic.toString()));

			// is there a class in the same topic as this class, that
			// extends the base class as well?
			Iterator iter = myTopic.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof Table) {
    				Table tab = (Table) obj;
    				if ((tab != this)
    					&& isExtendedFromBase((Table) tab.getExtending())) {

    					throw new IllegalStateException(
    						formatMessage(
    							"err_table_extendedButOtherDoesToo",
    							this.toString(),
    							getExtending().toString(),
    							myTopic.toString(),
    							tab.toString()));

    				}
    			}
			}

			// is there a class in a base topic, that
			// extends the base class as well?
			baseTopic = (Topic) myTopic.getExtending();
			while (baseTopic != null) {
				iter = baseTopic.iterator();
				while (iter.hasNext()) {
					Object obj = iter.next();
					if (!(obj instanceof Table)) {
						continue;
					}
					Table tab = (Table) obj;
					if ((tab != this)
						&& (tab != getExtending())
						&& isExtendedFromBase((Table) tab.getExtending())) {
						throw new IllegalStateException(
							formatMessage(
								"err_table_extendedButOtherDoesToo",
								this.toString(),
								getExtending().toString(),
								baseTopic.toString(),
								tab.toString()));

					}
				}
				baseTopic = (Topic) baseTopic.getExtending();
			}
		} else {
			// this extends class from base topic and has the same name;
			// but is not EXTENDED
			// check that base topics contain no class
			// with same name as this
			Topic baseTopic = null;
			// this not at model level?
			if(myTopic!=null){
				baseTopic=(Topic) myTopic.getExtending();
			}
			Topic topicOfBase = (Topic) getExtending().getContainer(Topic.class);
			// check all base topics
			if (baseTopic != null && topicOfBase!=null) {
				if (getName().equals(getExtending().getName())) {
					throw new IllegalStateException(
						formatMessage(
							"err_table_extendsWithSameName",
							this.toString(),
							getExtending().toString(),
							myTopic.toString(),
							baseTopic.toString()
							));

				}
			}
		}
	}


  	if(isAlias()){
  		((Table)getReal()).checkIntegrity(errs);
  		return;
  	}else{
	    super.checkIntegrity (errs);



	    Iterator<Table> referencableIter = getReferencableTables().iterator();
	    while (referencableIter.hasNext())
	    {
	      Table referencable = referencableIter.next();
	      if (this.isExtending (referencable))
	        throw new IllegalStateException (formatMessage (
	          "err_table_cyclicRelationalStructure",
	          this.toString(), referencable.toString()));
	    }


  	}
  }

  /** Determines whether or not <code>this</code> is a EXTENDED
	  <code>base</code>, be it through direct or through
	  indirect extension.
  */
  public boolean isExtendedFromBase (Table base)
  {
	Table parent = this;
	if(parent.extending!=null && parent.extended){
		parent = (Table) parent.extending;
	}else{
		parent=null;
	}
	while (parent != null) {
		if (parent == base) {
			return true;
		}
		if(parent.extending!=null && parent.extended){
			parent = (Table) parent.extending;
		}else{
			parent=null;
		}
	}

	return false;
  }

  private boolean extended=false;


  /** to distinguish between EXTENDS and EXTENDED
   */
  public void setExtended(boolean extended){
  	if(isAlias()){
  		((Table)getReal()).setExtended(extended);
  		return;
  	}else{
	  	this.extended=extended;
  	}
  }
  public boolean isExtended(){
  	if(isAlias()){
  		return ((Table)getReal()).isExtended();
  	}else{
	  	return extended;
  	}
  }
	public ElementAlias createAlias(String alias)
	{
		Table newele=new Table();
		newele.setAlias(alias);
		newele.setNext(this);
		return newele;
	}



	public boolean isIli1Optional() {
		return ili1Optional;
	}



	public void setIli1Optional(boolean ili1Optional) {
		this.ili1Optional = ili1Optional;
	}



	public boolean isIli1LineAttrStruct() {
		return ili1LineAttrStruct;
	}



	public void setIli1LineAttrStruct(boolean ili1LineAttrStruct) {
		this.ili1LineAttrStruct = ili1LineAttrStruct;
	}

	@Override
	public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
	  throws java.lang.IllegalStateException
	{
	    super.checkTranslationOf(errs,name,baseName);
	    Table baseElement=(Table)getTranslationOf();
	    if(baseElement==null) {
	        return;
	    }
	    
	    if(isIdentifiable()!=baseElement.isIdentifiable()) {
	        errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchStructure",getScopedName(),baseElement.getScopedName())));
	    }
	}

}
