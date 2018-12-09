/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
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


public abstract class AbstractPatternDef<E extends Element> extends ExtendableContainer<E> {

  protected List<E> contents = new ArrayList<E>();
  protected class ElementDelegate extends AbstractCollection<E>
  {
    public Iterator<E> iterator ()
    {
      return contents.iterator ();
    }



    public int size()
    {
      return contents.size () ;
    }



	public boolean add (E o)
	{
		if(check(o)){
			return contents.add(o);
		}
		return false;
	}
    private boolean check (E e)
    {
        /* In all sorts of model: UNIT, DOMAIN, FUNCTION, LINE FORM, STRUCTURE,
           abstract TABLE */
        if (e instanceof MetaDataUseDef)
	{
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_nonuniqueMetaDataUseDefName",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }
          return true;
	}


	if (e instanceof Unit)
        {
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_duplicateUnitName",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }


          return true;
        }


        if (e instanceof Domain)
        {
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_duplicateDomainName",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }


          return true;
        }


        if (e instanceof AssociationDef){
          AssociationDef toInsert = (AssociationDef) e;
          Element conflicting = getElement (e.getName());
          if ((conflicting != null) && (conflicting != e)
            && !toInsert.isExtended() && !toInsert.isExtending(conflicting)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_association_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }
          return true;
        }



        if (e instanceof Table)
        {
          Table toInsert = (Table) e;

          Element.checkNameSanity (toInsert.getName(), /* empty ok? */ false);

          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)
            && !toInsert.isExtended() && !toInsert.isExtending(conflicting)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_table_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }


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


        if (e instanceof View)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_view_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }
          return true;
        }
		if (e instanceof Function)
		{
			Model enclosingModel = (Model) getContainer(Model.class);
			if (enclosingModel.isIli23() && !enclosingModel.isContracted() && !(enclosingModel instanceof PredefinedModel)) {
                throw new Ili2cSemanticException (formatMessage (
                	"err_model_functionButNoContract",
                	e.toString(),
                	enclosingModel.toString()));
            }

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
        if (e instanceof Projection)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_projection_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }
          return true;
        }
	if (e instanceof Graphic)
        {
          Element conflicting = getElement ( e.getName());
          if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (formatMessage (
              "err_graphic_nonunique",
              e.getName(),
              AbstractPatternDef.this.toString()));
        }
          return true;
        }


        throw new Ili2cSemanticException (formatMessage (
          "err_container_cannotContain",
          AbstractPatternDef.this.toString(), e.toString()));
    }
  };

  public void addBefore(E o,Object next)
  {
	  if(((ElementDelegate)elements).check(o)){
		    try {
			      ((Element) o).setBeanContext(this);
		    } catch (java.beans.PropertyVetoException pve) {
			      throw new IllegalArgumentException(pve.getLocalizedMessage());
		    }
		    int idx=contents.indexOf(next);
		    if(idx>-1){
				  contents.add(idx,o);
		    }else{
				  contents.add(o);
		    }
	  }
  }
  public static void checkTopicDepOfAttr(AbstractPatternDef<?> thisTopic,
			AttributeDef attr, String attrPath) {
	  Type type=attr.getDomain();
	  if(type instanceof CompositionType){
		  // check sub-structure
		  CompositionType bagType=(CompositionType) type;
		  Iterator<Table> structi=bagType.iteratorRestrictedTo();
		  if(structi.hasNext()){
			  while(structi.hasNext()){
				  Table struct = structi.next();
				  for(Iterator<?> attri=struct.getAttributes();attri.hasNext();){
					  AttributeDef subAttr=(AttributeDef)attri.next();
					  checkTopicDepOfAttr(thisTopic,subAttr,attrPath+"/"+subAttr.getName());
				  }
			  }
		  }else{
			  Table struct = bagType.getComponentType();
			  for(Iterator<?> attri=struct.getAttributes();attri.hasNext();){
				  AttributeDef subAttr=(AttributeDef)attri.next();
				  checkTopicDepOfAttr(thisTopic,subAttr,attrPath+"/"+subAttr.getName());
			  }
		  }
	  }else if(type instanceof ReferenceType){
		  ReferenceType ref=(ReferenceType)type;
		  boolean external=ref.isExternal();
		  Iterator<AbstractClassDef> targeti=ref.iteratorRestrictedTo();
		  if(targeti.hasNext()){
			  while(targeti.hasNext()){
				  AbstractClassDef<?> target=targeti.next();
				  checkRefTypeTarget(thisTopic, attrPath, (AbstractClassDef<?>) attr.getContainer(),target, external);
			  }
		  }else{
			  AbstractClassDef<?> target=ref.getReferred();
			  checkRefTypeTarget(thisTopic, attrPath, (AbstractClassDef<?>) attr.getContainer(),target, external);
		  }
	  }
  }
public static void checkRefTypeTarget(AbstractPatternDef<?> thisTopic,
		String attrPath, AbstractClassDef<?> struct,AbstractClassDef<?> target, boolean external) {
	  Container<?> targetTopic = target.getContainer();
		// target in a topic and targets topic not a base of this topic 
		if(targetTopic!=null && thisTopic!=null && !thisTopic.isExtending(targetTopic)){
			if(!external){
				// must be external
				if(attrPath==null){
					throw new Ili2cSemanticException (formatMessage ("err_refattr_externalreq"));
				}else{
					throw new Ili2cSemanticException (formatMessage ("err_refattr_externalreq2",attrPath,struct.getScopedName(null)));
				}
			}else{
				  if(targetTopic!=thisTopic){
				    if(!thisTopic.isDependentOn(targetTopic)){
						if(attrPath==null){
					    	throw new Ili2cSemanticException (formatMessage ("err_refattr_topicdepreq",
									thisTopic.getName(),
									targetTopic.getScopedName(null)));
						}else{
					    	throw new Ili2cSemanticException (formatMessage ("err_refattr_topicdepreq2",
									thisTopic.getName(),
									targetTopic.getScopedName(null),attrPath,struct.getScopedName(null)));
						}
				    }
				  }
			}
		}
}
public void addAfter(E o,Object previous)
  {
	  if(((ElementDelegate)elements).check(o)){
		    try {
			      ((Element) o).setBeanContext(this);
		    } catch (java.beans.PropertyVetoException pve) {
			      throw new IllegalArgumentException(pve.getLocalizedMessage());
		    }
		    int idx=contents.indexOf(previous);
		    if(idx>-1 && idx+1<contents.size()){
				  contents.add(idx+1,o);
		    }else{
				  contents.add(o);
		    }
	  }
  }

  public AbstractPatternDef(){
  }
  protected Collection<E> createElements(){
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
  public List<Viewable<?>> getViewables ()
  {
    List<Viewable<?>> result = new ArrayList<Viewable<?>>();
    HashSet<Viewable<?>> hiddenViewables  = new HashSet<Viewable<?>>();


    // walk topic hierarchy up from the leaf (this) to the base topic
    for (AbstractPatternDef t = this; t != null; t = (AbstractPatternDef) t.getRealExtending())
    {
      Iterator<Element> iter=t.iterator();
      List<Viewable<?>> tresult=new ArrayList<Viewable<?>>();

      while (iter.hasNext ())
      {
        Element obj = iter.next();

        if (obj instanceof Viewable)
        {
			//
			// if viewable is not overridden by a viewable already seen, add it to the list
			//
			// only ClassDef and AssociationDef may be overridden, so add the corresponding base ClassDef
			// of ClassDefs that are EXTENDED to the list of hiddenViewables, and then check every viewable
			// to be in this list
			if (obj instanceof Table && ((Table) obj).isExtended()) {
				hiddenViewables.add((Viewable<?>) ((Table) obj).getExtending());
			} else if (obj instanceof AssociationDef && ((AssociationDef) obj).isExtended()) {
				hiddenViewables.add((Viewable<?>) ((AssociationDef) obj).getExtending());
			}
			if (!hiddenViewables.contains(obj)) {
				tresult.add((Viewable<?>) obj);
			}
        }
      }
      // add items of new topic in front of result list
      tresult.addAll(result);
      result=tresult;


    }


    return result;
  }
  public List<Viewable<?>> getTransferViewables()
  {
	    List<Viewable<?>> viewables = getViewables();
            List<Viewable<?>> result = new ArrayList<Viewable<?>>(viewables.size()); // Minimize (re)allocations. **GV1012
	    Iterator<Viewable<?>> iter = viewables.iterator();
	    while (iter.hasNext())
	    {
	      Viewable<?> v = iter.next();
	      if (!suppressViewableInTransfer(v))
	      {
				result.add(v);
	      }
	    }
	    return result;
  }
  static public boolean suppressViewableInTransfer(Viewable<?> v)
  {
    if (v == null) {
        return true;
    }


    if (v.isAbstract()) {
        return true;
    }

    if(v instanceof AssociationDef){
		AssociationDef assoc=(AssociationDef)v;
    	if(assoc.isLightweight()){
    		return true;
    	}
		if(assoc.getDerivedFrom()!=null){
			return true;
		}
    }

    Topic topic;
    if ((v instanceof View) && ((topic=(Topic)v.getContainer (Topic.class)) != null)
	    && !topic.isViewTopic()) {
        return true;
    }


    /* STRUCTUREs do not need to be printed with their INTERLIS container,
       but where they are used. */
    if ((v instanceof Table) && !((Table)v).isIdentifiable()) {
        return true;
    }


    return false;
  }


  /** find an element with the given name in the given namespace
   */
  private Element getElement(String name){
	    if (name != null) {
	        for (Element e : contents) {
	              if (name.equals(e.getName())) {
	                  return e;
	              }
	        }

            // get Element from inherited Container
         	if (extending != null) {
                return ((AbstractPatternDef)extending).getElement(name);
            }
	    }

      return null;
  }


}


