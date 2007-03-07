package ch.interlis.ili2c.metamodel;


import java.util.*;


public abstract class AbstractPatternDef extends ExtendableContainer {

  protected List     contents = new ArrayList ();
  protected class ElementDelegate extends AbstractCollection
  {
    public Iterator iterator ()
    {
      return contents.iterator ();
    }



    public int size()
    {
      return contents.size () ;
    }



	public boolean add (Object o)
	{
		if(check(o)){
			return contents.add(o);
		}
		return false;
	}
    private boolean check (Object o)
    {
        /* In all sorts of model: UNIT, DOMAIN, FUNCTION, LINE FORM, STRUCTURE,
           abstract TABLE */
        Element e=(Element)o;
        if (o instanceof MetaDataUseDef)
	{
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_nonuniqueMetaDataUseDefName",
              e.getName(),
              AbstractPatternDef.this.toString()));
          return true;
	}


	if (o instanceof Unit)
        {
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_duplicateUnitName",
              e.getName(),
              AbstractPatternDef.this.toString()));


          return true;
        }


        if (o instanceof Domain)
        {
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_duplicateDomainName",
              e.getName(),
              AbstractPatternDef.this.toString()));


          return true;
        }



        if (o instanceof AssociationDef){
          AssociationDef toInsert = (AssociationDef) o;
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e)
            && !toInsert.isExtended() && !toInsert.isExtending(conflicting))
            throw new Ili2cSemanticException (formatMessage (
              "err_association_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
          return true;
        }



        if (o instanceof Table)
        {

          Table toInsert = (Table) o;
          Viewable hiding;


          Element.checkNameSanity (toInsert.getName(), /* empty ok? */ false);


          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)
            && !toInsert.isExtended() && !toInsert.isExtending(conflicting))
            throw new Ili2cSemanticException (formatMessage (
              "err_table_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));


          if (!toInsert.isAbstract()
              && toInsert.isIdentifiable()
              && (AbstractPatternDef.this) instanceof Topic)
          {
            if (((Topic)(AbstractPatternDef.this)).isViewTopic())
            {
              throw new Ili2cSemanticException (
                formatMessage ("err_topic_addNormalTableInViewTopic",
                               toInsert.toString (),
                               AbstractPatternDef.this.toString ()));
            }
          }


          return true;
        }


	// ConstraintsDef are directly added by the parser to the ClassDef
        //if (o instanceof ConstraintsDef)


        if (o instanceof View)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_view_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
          return true;
        }
		if (o instanceof Function)
		{
			Model enclosingModel = (Model) getContainer(Model.class);
			if (!enclosingModel.isContracted() && !(enclosingModel instanceof PredefinedModel))
			  throw new Ili2cSemanticException (formatMessage (
				"err_model_functionButNoContract",
				e.toString(),
				enclosingModel.toString()));
			
			Element conflicting = getElement (e.getName());
			if ((conflicting != null) && (conflicting != e)){
				throw new Ili2cSemanticException (formatMessage (
				  "err_function_duplicateName",
				  e.getName(),
				  AbstractPatternDef.this.toString(),
				  conflicting.toString()));
			}
			return true;
		}
        if (o instanceof Projection)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_projection_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
          return true;
        }
	if (o instanceof Graphic)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e))
            throw new Ili2cSemanticException (formatMessage (
              "err_graphic_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
          return true;
        }


        throw new Ili2cSemanticException (formatMessage (
          "err_container_cannotContain",
          AbstractPatternDef.this.toString(), o.toString()));
    }
  };

  public void addPreLast (Object o)
  {
	  if(((ElementDelegate)elements).check(o)){
		  contents.add(contents.size()-1,o);
	  }
  }

  public AbstractPatternDef(){
  }
  protected Collection createElements(){
      return new ElementDelegate();
  }

  /** Walks the inheritance hierarchy to find out about all the
      viewables (tables and views) that are defined for this Topic.
      Inherited viewables are part of the result, but only those
      that are not overwritten.


      <p>The iterator will return the viewables in the order
      of their first definition. In the example below, the iterator
      returned by this call would first return <code>A.P</code>,
      then <code>A.Q</code>, then <code>B.R</code>, then A.Q.


<p><code><pre>TOPIC A =
  CLASS P =
  END P;


  CLASS Q =
  END Q;
END A;


TOPIC B EXTENDS A =
  CLASS R EXTENDS P =
  END R;


  CLASS Q (EXTENDED) =
  END Q;
END B;<br></pre></code>


      @return An iterator for all the tables of this Topic.
  */
  public List getViewables ()
  {
    List result = new LinkedList ();
    HashSet hiddenViewables  = new HashSet();


    // walk topic hierarchy up from the leaf (this) to the base topic
    for (AbstractPatternDef t = this; t != null; t = (AbstractPatternDef) t.getRealExtending())
    {
      Iterator iter=t.iterator();


      LinkedList tresult=new LinkedList();


      while (iter.hasNext ())
      {
        Element obj = (Element)iter.next();


        if (obj instanceof Viewable)
        {
			//
			// if viewable is not overridden by a viewable already seen, add it to the list
			//
			// only ClassDef and AssociationDef may be overridden, so add the corresponding base ClassDef
			// of ClassDefs that are EXTENDED to the list of hiddenViewables, and then check every viewable
			// to be in this list
			if(obj instanceof Table && ((Table)obj).isExtended()){
				hiddenViewables.add(((Table)obj).getExtending());
			}else if(obj instanceof AssociationDef && ((AssociationDef)obj).isExtended()){
				hiddenViewables.add(((AssociationDef)obj).getExtending());
			}
			if(!hiddenViewables.contains(obj)){
				tresult.add(obj);
			}
        }
      }
      // add items of new topic in front of result list
      tresult.addAll(result);
      result=tresult;


    }


    return result;
  }


  /** find an element with the given name in the given namespace
   */
  private Element getElement(String name){
	    if (name == null)
	      return null;
            Iterator it;
            it = contents.iterator();
	    while (it.hasNext ())
	    {
	      Element e = (Element) it.next ();
	      if (name.equals (e.getName()))
	        return e;
	    }

            // get Element from inherited Container
         	if (extending != null)
	          return ((AbstractPatternDef)extending).getElement(name);

      return null;
  }


}


