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


/** An abstract class that groups together all the different
    kinds of models.
*/
public abstract class Model extends Importable<Element>
{
  protected List<Element> contents = new LinkedList<Element>();
  protected List<GraphicParameterDef> runtimeParameters = new LinkedList<GraphicParameterDef>();
  protected List<ModelImport> imports = new ArrayList<ModelImport>();
  private final List<Contract> contracts=new LinkedList<Contract>();
  private String language=null;
  private boolean contracted=false;
  private String issuer=null;
  private String modelVersion=null;
  private String modelVersionExpl=null;
  private Boolean noIncrementalTransfer=null;
  private String charSetIANAName=null;
  private String xmlns=null;
  private String iliVersion=ILI2_3;
	static public final String ILI1="1";
	static public final String ILI2_2="2.2";
	static public final String ILI2_3="2.3";
	static public final String ILI2_4="2.4";

	  private class ModelImport {
		  private Model model;
		  private boolean unqualified;
		  public ModelImport(Model model1,boolean unqualified1)
		  {
			  model=model1;
			  unqualified=unqualified1;
		  }
		public Model getModel() {
			return model;
		}
		public boolean isUnqualified() {
			return unqualified;
		}
	  }
  protected class ElementDelegate extends AbstractCollection<Element>
  {
    @Override
    public Iterator<Element> iterator ()
    {
      return new CombiningIterator<Element> ( new Iterator[]
                                     {
                                       runtimeParameters.iterator (),
                                       contents.iterator (),
                                     });
    }



    @Override
    public int size()
    {
      return runtimeParameters.size () + contents.size () ;
    }



    @Override
    public boolean add(Element o)
    {
		if(!checkChildElement(o)){
			return false;
		}
        if (o instanceof GraphicParameterDef)
        {
          return runtimeParameters.add((GraphicParameterDef) o);
        }
		return contents.add(o);


    }

	public boolean checkChildElement (Element e)
	{
		/* In all sorts of model: UNIT, DOMAIN, FUNCTION, LINE FORM, STRUCTURE,
		   abstract TABLE */
		if (e instanceof MetaDataUseDef)
	{
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_nonuniqueMetaDataUseDefName",
			  e.getName(),
			  Model.this.toString()));
        }
		  return true;
	}


	if (e instanceof Unit)
		{
		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_duplicateUnitName",
			  e.getName(),
			  Model.this.toString()));
        }


		  return true;
		}


		if (e instanceof Function)
		{


		  if (isIli23() && (!isContracted() && !(Model.this instanceof PredefinedModel))) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_model_functionButNoContract",
			  e.toString(),
			  Model.this.toString()));
        }


		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_function_duplicateName",
			  e.getName(),
			  Model.this.toString(),
			  conflicting.toString()));
        }


		  return true;
		}



		if (e instanceof LineForm)
		{


		  if (!isContracted() && !(Model.this instanceof PredefinedModel)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_lineForm_inUnconctractedModel",
			  Model.this.toString()));
        }


		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_lineForm_duplicateName",
			  e.getName(),
			  Model.this.toString(),
			  conflicting.toString()));
        }


		  return true;
		}


		if (e instanceof Domain)
		{
		  Element conflicting = getElement (e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_duplicateDomainName",
			  e.getName(),
			  Model.this.toString()));
        }


		  return true;
		}



		if (e instanceof GraphicParameterDef)
		{
		  Element conflicting = getRuntimeParameter ( e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_graphicparam_nonunique",
			  e.getName(),
			  Model.this.toString()));
        }


		  return true;
		}


		if (e instanceof Topic)
		{
		  if (Model.this instanceof TypeModel) {
            throw new Ili2cSemanticException (e.getSourceLine(), formatMessage (
			  "err_typeModel_addTopic",
			  e.toString (),
			  Model.this.toString ()));
        }
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_topic_nonunique",
			  e.getName(),
			  Model.this.toString()));
        }


		  return true;
		}


	if(e instanceof Table){
		  Element conflicting = getElement ( e.getName());
		  if ((conflicting != null) && (conflicting != e)) {
            throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
			  "err_table_nonunique",
			  e.getName(),
			  Model.this.toString()));
        }
		return true;
	}

    if(e instanceof ContextDefs){
        Element conflicting = getElement ( e.getName());
        if ((conflicting != null) && (conflicting != e)) {
          throw new Ili2cSemanticException (e.getSourceLine(),formatMessage (
            "err_contextdefs_nonunique",
            e.getName(),
            Model.this.toString()));
      }
      return true;
  }

		throw new ClassCastException (formatMessage (
		  "err_container_cannotContain",
		  Model.this.toString(), e.toString()));
	}



  };

  public void addPreLast(Element o)
  {
	  if(!((ElementDelegate)elements).checkChildElement(o)){
		  return;
	  }
	    try {
		      o.setBeanContext(this);
	    } catch (java.beans.PropertyVetoException pve) {
		      throw new IllegalArgumentException(pve.getLocalizedMessage());
	    }

	  if (o instanceof GraphicParameterDef)
	  {
		runtimeParameters.add((GraphicParameterDef) o);
	  }
	  contents.add(contents.size()-1,o);


  }
  public void addBefore(Element o, Element last)
  {
	  if(!((ElementDelegate)elements).checkChildElement(o)){
		  return;
	  }
	    try {
		      o.setBeanContext(this);
	    } catch (java.beans.PropertyVetoException pve) {
		      throw new IllegalArgumentException(pve.getLocalizedMessage());
	    }

	  if (o instanceof GraphicParameterDef)
	  {
		runtimeParameters.add((GraphicParameterDef) o);
	  }
	    int idx=contents.indexOf(last);
	    if(idx>-1){
			  contents.add(idx,o);
	    }else{
			  contents.add(o);
	    }


  }


  /** Constructs a new model.


      @exception java.lang.IllegalArgumentException if the
                 name parameter is <code>null</code> or an empty string.
   */
  protected Model ()
  {
    this.name = "";
  }
  @Override
protected Collection<Element> createElements(){
    return new ElementDelegate();
  }

	public Element getImportedElement(Class<?> aclass, String name) {
		Element e = getElement(aclass, name);
		if (e != null) {
			return e;
		}
		for (ModelImport mi : imports) {
			if (mi.isUnqualified()) {
				Model i = mi.getModel();
				e = i.getElement(aclass, name);
				if (e != null) {
					return e;
				}
			}
		}

		return null;
	}

  /** find an element with the given name in the type namespace
   */
  private Element getElement(String name){
      if (name != null) {
          for (Element e : contents) {
               if (name.equals(e.getName())) {
                   return e;
               }
          }
      }
      return null;
  }
  /** find an runtime parameter with the given name
   */
  private Element getRuntimeParameter(String name){
      if (name != null) {
	  for (GraphicParameterDef e : runtimeParameters) {
	       if (name.equals(e.getName())) {
	           return e;
	       }
	  }
      }
      return null;
  }


  /* The semantic rules of Interlis ensure that a model <i>M</i>
     is dependent on a model <i>I</i> if and only if <i>M</i>
     imports <i>I</i>.
  */
  @Override
public boolean isDependentOn (Element e)
  {
    if (e instanceof Model) {
        return isImporting((Model) e);
    }


    return false;
  }



  /** Returns the name of this model.
  */
  @Override
public String getName ()
  {
    return name;
  }



  /* FIXME: Should check for name uniqueness. */
  public void setName (String name)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.name;
    String newValue = name;


    checkNameSanity(newValue, /* empty ok? */ false);


    fireVetoableChange("name", oldValue, newValue);


    this.name = newValue;
    firePropertyChange("name", oldValue, newValue);
  }



  /** Returns whether or not this Model is subject to a contract.
      @return <code>false</code> iff. the contract issuer's name
              is empty.
  */
  public boolean isContracted ()
  {
    return contracted || !contracts.isEmpty();
  }
  public void setContracted(boolean contracted1)
  {
	contracted=contracted1;
  }
public void setIssuer(String issuer1)
{
	issuer=issuer1;
}
public String getIssuer()
{
	return issuer;
}
public void setModelVersion(String modelVersion1)
{
	modelVersion=modelVersion1;
}
public String getModelVersion()
{
	return modelVersion;
}
public void setModelVersionExpl(String modelVersionExpl1)
{
	modelVersionExpl=modelVersionExpl1;
}
public String getModelVersionExpl()
{
	return modelVersionExpl;
}


  public void addContract(Contract contract)
  {
	  contracts.add(contract);
  }


  public Contract[] getContracts()
  {
	  return contracts.toArray(new Contract[contracts.size()]);  // Avoid double object creation. **GV1012
  }



  /** Returns the Importables that this model imports.
  */
	public Model[] getImporting() {
		ArrayList<Model> importing = new ArrayList<Model>();
		for (ModelImport mi : imports) {
			importing.add(mi.getModel());
		}
		return importing.toArray(new Model[importing.size()]);
	}

  /** Let this model import another importable. */
	public void addImport(Model importing, boolean unqualified) {
		imports.add(new ModelImport(importing, unqualified));
		importing.importedBy.add(importing);
	}


  /** Returns whether or not this model imports a specified
      Importable.
  */
  public boolean isImporting(Model importing)
  {
		for (ModelImport mi : imports) {
			if(mi.getModel()==importing){
				return true;
			}
		}
		return false;
  }
  /** Returns whether or not this model imports a specified
      Importable.
  */
  public boolean isImporting(Model importing, boolean qualifiedImport)
  {
		for (ModelImport mi : imports) {
			if(mi.isUnqualified()==!qualifiedImport && mi.getModel()==importing){
				return true;
			}
		}
		return false;
  }


  public void setLanguage(String v)
  {
	  language=v;
  }
  /** may return null if no language set
   */
  public String getLanguage(){
  	return language;
  }

  private String filename=null;
  public void setFileName(String filename)
  {
    this.filename=filename;
  }
  public String getFileName()
  {
    return filename;
  }
/**
 * @return
 */
public String getIliVersion() {
	return iliVersion;
}

/**
 * @param string
 */
public void setIliVersion(String string) {
	iliVersion = string;
}

public boolean isIli23() {
	return ILI2_3.equals(iliVersion);
}
public Boolean getNoIncrementalTransfer() {
	return noIncrementalTransfer;
}
public void setNoIncrementalTransfer(boolean noIncrementalTransfer) {
	this.noIncrementalTransfer = noIncrementalTransfer;
}
public String getCharSetIANAName() {
	return charSetIANAName;
}
public void setCharSetIANAName(String charSetIANAName) {
	this.charSetIANAName = charSetIANAName;
}
public String getXmlns() {
	return xmlns;
}
public void setXmlns(String xmlns) {
	this.xmlns = xmlns;
}
	private String translationOf=null;
	private String translationOfVersion=null;
	public void setTranslationOf(String baseModel,String baseVersion)
	{
		translationOf=baseModel;
		translationOfVersion=baseVersion;
		// link in checkIntegrity()
	}
	@Override
	public void checkIntegrity(List<Ili2cSemanticException> errs) throws java.lang.IllegalStateException {
		super.checkIntegrity(errs);
		if(translationOf!=null){
			Element baseModel=getContainer().getElement(Model.class,translationOf);
			if(baseModel==null){
		        throw new IllegalStateException (formatMessage (
		  	          "err_noSuchModel",translationOf));
			}
			if(baseModel==this) {
	            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_model_differentName",getScopedName())));
	            return;
			}
			linkTranslationOf(baseModel);
			checkTranslationOf(errs,getName(),baseModel.getName());
		}
		
	}
	  /** gets the possible concrete domains for a generic domain according to a context definition.
	   */
	  public Domain[] resolveGenericDomain(Domain generic) {
	      // search in this model
	      {
	          Domain concretes[]=getConcreteDomains(generic);
	          if(concretes!=null) {
	              return concretes;
	          }
	      }
	      // search in the (direct and indirect) imported models
	      Set<Model> visitedModels=new HashSet<Model>();
	      return resolveGenericDomain_deepFirstSearch(generic,this,visitedModels);
	  }
    private static Domain[] resolveGenericDomain_deepFirstSearch(Domain generic,Model model,Set<Model> visitedModels) {
        Model imps[]=model.getImporting();
        for(int i=imps.length-1;i>=0;i--) {
            Model imp=imps[i];
              if(!visitedModels.contains(imp)) {
                  visitedModels.add(imp);
                  Domain concretes[]=imp.getConcreteDomains(generic);
                  if(concretes!=null) {
                      return concretes;
                  }
                  concretes=resolveGenericDomain_deepFirstSearch(generic,imp,visitedModels);
                  if(concretes!=null) {
                      return concretes;
                  }
              }
	      }
        return null;
    }
      public Domain[] getConcreteDomains(Domain generic) {
          Iterator eleIt=iterator();
          while(eleIt.hasNext()) {
              Object ele=eleIt.next();
              if(ele instanceof ContextDefs) {
                  ContextDefs defs=(ContextDefs)ele;
                  Iterator<ContextDef> defIt=defs.iterator();
                  while(defIt.hasNext()) {
                      ContextDef def=defIt.next();
                      if(def.getGeneric()==generic) {
                          return def.getConcretes();
                      }
                  }
              }
          }
          return null;
      }
    public Domain mapGenericDomain(Domain genericCoordDomain, Map<String, String> genericDomains) {
        String genericQname=genericCoordDomain.getScopedName();
        String concreteQname=genericDomains.get(genericQname);
        if(concreteQname==null) {
            throw new IllegalArgumentException("no assignment of a concrete domain for the generic domain "+genericQname);
        }
        Element ele = ((TransferDescription)getContainer()).getElement(concreteQname);
        if(ele==null || !(ele instanceof Domain)) {
            throw new IllegalArgumentException("no concrete domain "+concreteQname+" (for the generic domain "+genericQname+")");
        }
        return (Domain)ele;
    }
	
    @Override
    public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
      throws java.lang.IllegalStateException
    {
        super.checkTranslationOf(errs,name,baseName);
        Model baseElement=(Model)getTranslationOf();
        if(baseElement==null) {
            return;
        }
        
        if(isContracted()!=baseElement.isContracted()) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_mismatchInContracted",getScopedName(),baseElement.getScopedName())));
        }
        if(!getClass().equals(baseElement.getClass())) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_kindModelMismatch",getScopedName(),baseElement.getScopedName())));
        }
        Ili2cSemanticException err=null;
        Model imps[]=getImporting();
        Model baseImps[]=baseElement.getImporting();
        if(imps.length!=baseImps.length) {
            errs.add(new Ili2cSemanticException (getSourceLine(),formatMessage("err_diff_importsMismatch")));
        }else {
            ArrayList<Model> impModels=new ArrayList<Model>(imps.length);
            Collections.addAll(impModels, imps);
            Collections.sort(impModels,new TranslatedElementNameComparator());
            ArrayList<Model> baseimpModels=new ArrayList<Model>(baseImps.length);
            Collections.addAll(baseimpModels, baseImps);
            Collections.sort(baseimpModels,new TranslatedElementNameComparator());
            
            for(int impi=0;impi<imps.length;impi++) {
                Model imp=impModels.get(impi);
                Model baseImp=baseimpModels.get(impi);
                err=checkElementRef(imp,baseImp,getSourceLine(),"err_diff_importsMismatch");
                if(err!=null) {
                    errs.add(err);
                }
            }
            
        }
    }
}
