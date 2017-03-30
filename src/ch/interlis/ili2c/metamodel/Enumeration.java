/*****************************************************************************
 *
 * Enumeration.java
 * ----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;


import java.util.*;
import ch.ehi.basics.settings.Settings;



/** EnumerationType holds the information associated with
    Interlis enumeration types.

    @version   January 28, 1999
    @author    Sascha Brawer
    @author    Gordan Vosicki - Added cloning support
*/
public class Enumeration implements Cloneable
{
  public static class Element implements Cloneable {
    protected String name = "";
    protected Enumeration subEnum = null;
	private String documentation=null;
	private ch.ehi.basics.settings.Settings metaValues=null;
	private int sourceLine=0;
  	private Element baseLanguageElement=null;
    /** copy constructor
     */
    public Element(Element src)
    {
      name=src.name;
      documentation=src.documentation;
      if(src.subEnum!=null){
        subEnum=new Enumeration(src.subEnum);
      }
      sourceLine=src.sourceLine;
      metaValues=new ch.ehi.basics.settings.Settings(src.metaValues);
      baseLanguageElement=src.baseLanguageElement;
    }

    public Element (String name)
    {
      this.name = name;
    }


    public Element (String name, Enumeration subEnum)
    {
      this.name = name;
      this.subEnum = subEnum;
    }


    public String getName ()
    {
      return name;
    }


    public Enumeration getSubEnumeration ()
    {
      return subEnum;
    }
    public void setSubEnumeration (Enumeration newSubEnum)
    {
      subEnum=newSubEnum;
    }

    public String toString ()
    {
      if (subEnum == null)
        return name;
      else
        return name + " " + subEnum.toString();
    }
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String string) {
		documentation = string;
	}

	public int getSourceLine() {
		return sourceLine;
	}

	public void setSourceLine(int sourceLine) {
		this.sourceLine = sourceLine;
	}
	public ch.ehi.basics.settings.Settings getMetaValues() {
		if(metaValues==null){
			metaValues=new ch.ehi.basics.settings.Settings();
		}
		return metaValues;
	}
	public void setMetaValues(ch.ehi.basics.settings.Settings metaValues) {
		this.metaValues = metaValues;
	}
  	public Element getTranslationOf()
  	{
  		return baseLanguageElement;
  	}
  	public Element getTranslationOfOrSame()
  	{
  		if(baseLanguageElement==null){
  			return this;
  		}
  		return baseLanguageElement.getTranslationOfOrSame();
  	}
  	protected void linkTranslationOf(Element baseElement)
  	{
  		this.baseLanguageElement=baseElement;
  		if(subEnum==null){
  			if(baseElement.subEnum!=null){
  				throw new Ili2cSemanticException(sourceLine,ch.interlis.ili2c.metamodel.Element.formatMessage("err_diff_enumType_unequalSubEnum",name,baseElement.name));
  			}
  		}else{
  			// ASSERT subEnum!=null
  			if(baseElement.subEnum==null){
  				throw new Ili2cSemanticException(ch.interlis.ili2c.metamodel.Element.formatMessage("err_diff_enumType_unequalSubEnum",name,baseElement.name));
  			}
  	        if(subEnum.elements.size()!=baseElement.subEnum.elements.size()){
  				throw new Ili2cSemanticException(sourceLine,ch.interlis.ili2c.metamodel.Element.formatMessage("err_diff_enumType_unequalSubEnum",name,baseElement.name));
  	        }
  			subEnum.linkTranslationOf(baseElement.subEnum);
  		}
  	}


        public Element clone() {
            Element cloned = null;

            try {
                cloned = (Element) super.clone();
                if (subEnum != null) {
                    cloned.subEnum = subEnum.clone();
                }
                if (metaValues != null) {
                    cloned.metaValues = new Settings(metaValues);
                }
                cloned.baseLanguageElement=baseLanguageElement;
            } catch (CloneNotSupportedException e) {
                // Never happens because the object is cloneable
            }
            return cloned;
        }
  }

  private List<Element> elements;
  private boolean is_Final=false;
private int sourceLine;

  public Enumeration(List<Element> elements)
  {
    this.elements = elements;
  }
  /** copy constructor
   * @deprecated Should be replaced by {@link #clone()}.
   */
  public Enumeration(Enumeration src)
  {
    int sz = src.elements.size();

    elements = new ArrayList<Element>(sz);
    for (int i = 0, s = sz; i < s; i++) {
        elements.add(src.getElement(i).clone());
    }
    is_Final=src.is_Final;
  }


  public void addElement(Element newele)
  {
	  Iterator<Element> elei = elements.iterator();
	  while(elei.hasNext()){
		  Element ele = elei.next();
		  if(ele.getName().equals(newele.getName())){
			  throw new Ili2cSemanticException(newele.getSourceLine(),ch.interlis.ili2c.metamodel.Element.formatMessage("err_enumerationType_DupEle",newele.getName()));
		  }
	  }
      elements.add(newele);
  }


    public Iterator<Element> getElements() {
        return elements.iterator();
    }


    public Element getElement(int index) {
        return elements.get(index);
    }


    public int size() {
        return elements.size();
    }


    public int treeSize() {
        int sz = size();
        int ts = sz;

        for (int i = 0; i < sz; ++i) {
            Enumeration e = getElement(i).subEnum;

            if (e != null) {
                ts += e.treeSize() - 1;
            }
        }
        return ts;
    }


  public void setFinal(boolean newValue)
  {
    is_Final=newValue;
  }
  public boolean isFinal()
  {
    return is_Final;
  }
  public String toString ()
  {
    StringBuilder buf = new StringBuilder(50);

    buf.append ('(');
    for (int i = 0; i < elements.size(); i++)
    {
      if (i > 0)
        buf.append (", ");
      buf.append (elements.get(i).toString());
    }
    if(is_Final){
      if (elements.size() > 0){
        buf.append (" : ");
      }
      buf.append ("FINAL");
    }
    buf.append (')');

    return buf.toString ();
  }

	public void setSourceLine(int sourceLine) {
		this.sourceLine = sourceLine;
	}
	protected void linkTranslationOf(Enumeration baseEnum)
	{
        int sz = elements.size();
        if(sz!=baseEnum.elements.size()){
			throw new Ili2cSemanticException(sourceLine,ch.interlis.ili2c.metamodel.Element.formatMessage("err_diff_enumType_unequalNumberOfElts"));
        }
        for(int i = 0 ; i < sz; i++){
    		this.elements.get(i).linkTranslationOf(baseEnum.elements.get(i));
        }
	}

    public Enumeration clone() {
        Enumeration cloned = null;

        try {
            int sz = elements.size();

            cloned = (Enumeration) super.clone();
            cloned.elements = new ArrayList<Element>(sz);

            for(int i = 0 ; i < sz; i++){
                cloned.elements.add(getElement(i).clone());
            }
        } catch (CloneNotSupportedException e) {
            // Never happens because the object is cloneable
        }
        return cloned;
    }

}
