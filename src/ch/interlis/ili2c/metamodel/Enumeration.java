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



/** EnumerationType holds the information associated with
    Interlis enumeration types.

    @version   January 28, 1999
    @author    Sascha Brawer
*/
public class Enumeration
{
  public static class Element {
    protected String name = "";
    protected Enumeration subEnum = null;
	private String documentation=null;
	private int sourceLine=0;
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
  }

  private List elements; // list<Enumeration.Element>
  private boolean is_Final=false;

  public Enumeration(List elements)
  {
    this.elements = elements;
  }
  /** copy constructor
   *
   */
  public Enumeration(Enumeration src)
  {
    elements=new ArrayList();
    for(int i=0;i<src.elements.size();i++){
      elements.add(new Element((Element)src.elements.get(i)));
    }
    is_Final=src.is_Final;
  }


  public void addElement(Element newele)
  {
	  Iterator elei=elements.iterator();
	  while(elei.hasNext()){
		  Element ele=(Element)elei.next();
		  if(ele.getName().equals(newele.getName())){
			  throw new Ili2cSemanticException(newele.getSourceLine(),ch.interlis.ili2c.metamodel.Element.formatMessage("err_enumerationType_DupEle",newele.getName()));
		  }
	  }
      elements.add(newele);
  }
  public java.util.Iterator getElements()
  {
    return elements.iterator();
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
    StringBuffer buf = new StringBuffer(50);

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
}
