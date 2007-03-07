package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;
import java.io.Writer;
import java.util.*;


public final class XSDGenerator
{
  IndentPrintWriter   ipw;
  TransferDescription td;
  int                 numErrors = 0;


  static ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());


  private XSDGenerator (Writer out, TransferDescription td)
  {
    ipw = new IndentPrintWriter (out);
    this.td = td;
  }



  private void finish()
  {
    ipw.close();
  }



  public static int generate (Writer out, TransferDescription td)
  {
    XSDGenerator d = new XSDGenerator (out, td);
   // d.findItemsToDeclare (td);
    d.printXSD (td);
    d.finish();
    return d.numErrors;
  }


  /** Returns the name used in the transfer for a given INTERLIS Element.


      @exception java.lang.IllegalArgumentException if elt is not an instance of
                 Model, Topic, Viewable or Attribute.
  */
  public static String getTransferName (ch.interlis.ili2c.metamodel.Element elt)
  {
    if (elt instanceof Model)
      return elt.getName();


    if (elt instanceof Topic)
      return elt.getScopedName (null);


    if (elt instanceof Viewable)
      return elt.getScopedName (null);


    if (elt instanceof AttributeDef)
    {
		return elt.getName();
    }


    if (elt instanceof RoleDef)
    {
		return elt.getName();
    }


    throw new IllegalArgumentException ();
  }

  private void printXSD (TransferDescription td)
  {
    ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
      +" xmlns=\"http://www.interlis.ch/INTERLIS2.2\""
      +" targetNamespace=\"http://www.interlis.ch/INTERLIS2.2\""
      +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\""
      +">");
	    ipw.indent ();

	    ipw.indent ();
	    ipw.println ("<xsd:element name=\"TRANSFER\" type=\"Transfer\"/>");

	    ipw.println ("<xsd:simpleType name=\"IliID\">");
		    ipw.indent ();
                    ipw.println ("<xsd:restriction base=\"xsd:string\">");
  		      ipw.indent ();
                      ipw.println ("<xsd:pattern value=\"x[0-9a-zA-Z]*\"/>");
		      ipw.unindent ();
                    ipw.println ("</xsd:restriction>");
		    ipw.unindent ();
	    ipw.println ("</xsd:simpleType>");

	    ipw.println ("<xsd:complexType name=\"Transfer\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
		    	ipw.println ("<xsd:element name=\"HEADERSECTION\" type=\"HeaderSection\"/>");
		    	ipw.println ("<xsd:element name=\"DATASECTION\" type=\"DataSection\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"HeaderSection\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
		    	ipw.println ("<xsd:element name=\"ALIAS\" type=\"Alias\"/>");
		    	ipw.println ("<xsd:element name=\"COMMENT\" type=\"xsd:anyType\" minOccurs=\"0\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.println ("<xsd:attribute name=\"VERSION\" type=\"xsd:decimal\" use=\"required\" fixed=\"2.2\"/>");
		    ipw.println ("<xsd:attribute name=\"SENDER\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Alias\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
		    	ipw.println ("<xsd:element name=\"ENTRIES\" type=\"Entries\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Entries\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
			    ipw.println ("<xsd:choice maxOccurs=\"unbounded\">");
				    ipw.indent ();
			    	ipw.println ("<xsd:element name=\"TAGENTRY\" type=\"Tagentry\"/>");
			    	ipw.println ("<xsd:element name=\"VALENTRY\" type=\"Valentry\"/>");
			    	ipw.println ("<xsd:element name=\"DELENTRY\" type=\"Delentry\"/>");
				    ipw.unindent ();
			    ipw.println ("</xsd:choice>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.println ("<xsd:attribute name=\"FOR\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Tagentry\">");
		    ipw.indent ();
		    ipw.println ("<xsd:attribute name=\"FROM\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"TO\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Valentry\">");
		    ipw.indent ();
		    ipw.println ("<xsd:attribute name=\"ATTR\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"FROM\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"TO\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Delentry\">");
		    ipw.indent ();
		    ipw.println ("<xsd:attribute name=\"TAG\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


		ipw.println ("<xsd:complexType name=\"BasketValue\">");
		    ipw.indent ();
		    ipw.println ("<xsd:attribute name=\"TOPIC\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"KIND\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


		ipw.println ("<xsd:complexType name=\"CoordValue\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
  		            ipw.println ("<xsd:element name=\"C1\" type=\"xsd:double\"/>");
  		            ipw.println ("<xsd:element name=\"C2\" type=\"xsd:double\" minOccurs=\"0\"/>");
  		            ipw.println ("<xsd:element name=\"C3\" type=\"xsd:double\" minOccurs=\"0\"/>");
        		    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


		ipw.println ("<xsd:complexType name=\"ArcPoint\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
  		            ipw.println ("<xsd:element name=\"C1\" type=\"xsd:double\"/>");
  		            ipw.println ("<xsd:element name=\"C2\" type=\"xsd:double\"/>");
  		            ipw.println ("<xsd:element name=\"C3\" type=\"xsd:double\" minOccurs=\"0\"/>");
  		            ipw.println ("<xsd:element name=\"A1\" type=\"xsd:double\"/>");
  		            ipw.println ("<xsd:element name=\"A2\" type=\"xsd:double\"/>");
  		            ipw.println ("<xsd:element name=\"R\" type=\"xsd:double\"/>");
        		    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"RoleType\">");
		    ipw.indent ();
		    ipw.println ("<xsd:simpleContent>");
			    ipw.indent ();
			    ipw.println ("<xsd:extension base=\"xsd:string\">");
    			      ipw.indent ();
		              ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\"/>");
		              ipw.println ("<xsd:attribute name=\"EXTREF\" type=\"IliID\"/>");
  		              ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
		              ipw.println ("<xsd:attribute name=\"NEXT_TID\" type=\"IliID\"/>");
                              ipw.unindent ();
			    ipw.println ("</xsd:extension>");
			    ipw.unindent ();
		    ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ();


	    /* Declare DATASECTION. */
	    ipw.println ("<xsd:complexType name=\"DataSection\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
			    ipw.println ("<xsd:choice minOccurs=\"0\" maxOccurs=\"unbounded\">");
				    ipw.indent ();
				    Iterator modeli = td.iterator ();
				    while (modeli.hasNext ())
				    {
				      Object mObj = modeli.next ();
				      if ((mObj instanceof Model) && !suppressModel ((Model) mObj))
				      {
				      	Model model=(Model)mObj;
				      	Iterator topici=model.iterator();
				      	while(topici.hasNext()){
				      		Object tObj=topici.next();
							if ((tObj instanceof Topic) && !suppressTopic ((Topic) tObj)){
								Topic topic=(Topic)tObj;
						    	ipw.println ("<xsd:element name=\""+getTransferName(topic)+"\">");
								    ipw.indent ();
									declareBasket(topic);
								    ipw.unindent ();
						    	ipw.println ("</xsd:element>");
				      		}
				      	}
				      }
				    }
				    ipw.unindent ();
			    ipw.println ("</xsd:choice>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.unindent ();
    ipw.println ("</xsd:schema>");


/*    ipw.println ();
    ipw.println ("<TRANSFER>");
    ipw.indent ();
    ipw.println ("<HEADERSECTION VERSION=\"2.0\" DATASET=\"Insert_DataSet_Name\">");
    ipw.indent ();
    ipw.println ("<ALIAS>");
    ipw.indent ();


    Iterator aliasIter = td.getIndirectElements().iterator();
    while (aliasIter.hasNext())
    {
      Element elt = (Element) aliasIter.next();


      if (!(elt instanceof Extendable))
        continue;


      Element from = (Element) elt;
      Element to = (Element) ((Extendable) elt).getExtending ();


      if (to != null)
      {
        ipw.print ("<ALIASENTRY FROM=\"");
        ipw.print (getTransferName (from));
        ipw.print ("\" TO=\"");
        ipw.print (getTransferName (to));
        ipw.println ("\"/>");
      }
    }
    ipw.unindent ();
    ipw.println ("</ALIAS>");
    ipw.unindent ();
    ipw.println ("</HEADERSECTION>");
    ipw.println ("<DATASECTION/>");
    ipw.unindent ();
    ipw.println ("</TRANSFER>");
*/
  }





  protected boolean suppressModel (Model model)
  {
    if (model == null)
      return true;


    if (model == td.INTERLIS)
      return true;


    if ((model instanceof TypeModel) || (model instanceof PredefinedModel))
      return true;


    return false;
  }



  protected boolean suppressTopic (Topic topic)
  {
    if (topic == null)
      return true;


    if (topic.isAbstract ())
      return true;


    return false;
  }



  protected boolean suppressViewable (Viewable v)
  {
    if (v == null)
      return true;


    if (v.isAbstract())
      return true;

    if(v instanceof AssociationDef && ((AssociationDef)v).isLightweight()){
      return true;
    }

    Topic topic;
    if ((v instanceof View) && ((topic=(Topic)v.getContainer (Topic.class)) != null)
	    && !topic.isViewTopic())
      return true;


    /* STRUCTUREs do not need to be printed with their INTERLIS container,
       but where they are used. */
    if ((v instanceof Table) && !((Table)v).isIdentifiable())
      return true;


    return false;
  }





  protected void declareBasket (Topic topic)
  {


    ipw.println ("<xsd:complexType>");
	    ipw.indent ();
	    ipw.println ("<xsd:sequence>");
		    ipw.indent ();
		    ipw.println ("<xsd:choice minOccurs=\"0\" maxOccurs=\"unbounded\">");
			    ipw.indent ();
			    /* Find which viewables are going to be elements of this topic. */
			    Iterator iter = topic.getViewables().iterator();
			    while (iter.hasNext())
			    {
			      Object obj = iter.next();
			      if ((obj instanceof Viewable) && !suppressViewable ((Viewable)obj))
			      {
					Viewable v = (Viewable) obj;
			    	ipw.println ("<xsd:element name=\""+getTransferName(v)+"\">");
					    ipw.indent ();
						declareObject(topic,v, /* isStructureValue */ false, /* isLineAttribute */ false);
					    ipw.unindent ();
			    	ipw.println ("</xsd:element>");
			      }
			    }
			    ipw.unindent ();
		    ipw.println ("</xsd:choice>");
		    ipw.unindent ();
	    ipw.println ("</xsd:sequence>");
	    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\" use=\"required\"/>");
	    ipw.println ("<xsd:attribute name=\"TOPICS\" type=\"xsd:string\"/>");
	    ipw.println ("<xsd:attribute name=\"KIND\" type=\"xsd:string\"/>");
	    ipw.println ("<xsd:attribute name=\"STARTSTATE\" type=\"xsd:string\"/>");
	    ipw.println ("<xsd:attribute name=\"ENDSTATE\" type=\"xsd:string\"/>");
	    ipw.unindent ();
    ipw.println ("</xsd:complexType>");


  }



  protected void declareObject (Topic currentTopic,Viewable v,boolean isStructureValue,boolean isLineAttribute)
  {
    ipw.println ("<xsd:complexType>");
	    ipw.indent ();
	    ipw.println ("<xsd:sequence>");
		    ipw.indent ();
		    /* Find which attributes are going to be elements of this class. */
		    Iterator iter = v.getAttributesAndRoles();
		    while (iter.hasNext())
		    {
		      Object obj = iter.next();
		      if (obj instanceof AttributeDef)
		      {
				AttributeDef attr = (AttributeDef) obj;
		      	if(!(isLineAttribute && attr.getName()=="Geometry")){
			    String minOccurs="";
			    if(!attr.getDomain().isMandatoryConsideringAliases()){
			    	minOccurs=" minOccurs=\"0\"";
			    }
			    	ipw.println ("<xsd:element name=\""+getTransferName(attr)+"\""+minOccurs+">");
					    ipw.indent ();
						declareAttribute(currentTopic,attr);
					    ipw.unindent ();
			    	ipw.println ("</xsd:element>");
		      	}
		      }
		      if (obj instanceof RoleDef && !((AssociationDef)v).isLightweight())
		      {
			RoleDef role = (RoleDef) obj;
		    	ipw.println ("<xsd:element name=\""+getTransferName(role)+"\" type=\"RoleType\"/>");
		      }
		    }
                    if(v instanceof AbstractClassDef){
                      iter=((AbstractClassDef)v).getLightweightAssociations().iterator();
  		      while (iter.hasNext())
  		      {
  			RoleDef role = (RoleDef) iter.next();
                        RoleDef oppend = getOppEnd(role);
			    Cardinality card=oppend.getCardinality();
			    String minOccurs="";
			    if(card.getMinimum()==0){
			    	minOccurs=" minOccurs=\"0\"";
			    }
  		    	ipw.println ("<xsd:element name=\""+getTransferName(oppend)+"\""+minOccurs+">");
  			    ipw.indent ();
  		            ipw.println ("<xsd:complexType>");
		              ipw.indent ();
		              ipw.println ("<xsd:sequence>");
			        ipw.indent ();
			        ipw.println ("<xsd:element name=\""+getTransferName(role.getContainer())+"\">");
                                  ipw.indent ();
  			          declareObject(currentTopic,(AssociationDef)role.getContainer(), /* isStructureValue */ true, /* isLineAttribute */ false);
			          ipw.unindent ();
			        ipw.println ("</xsd:element>");
  			        ipw.unindent ();
		              ipw.println ("</xsd:sequence>");
                              ipw.unindent ();
  		            ipw.println ("</xsd:complexType>");
		            ipw.unindent ();
  		    	ipw.println ("</xsd:element>");
                      }
                    }
		    ipw.unindent ();
	    ipw.println ("</xsd:sequence>");
	    // class or struct is an identifieable object?
	    if(!isStructureValue){
	    	// Objects have an ID and an operation flag to allow incremental update
		    ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
		    ipw.println ("<xsd:attribute name=\"OPERATION\" type=\"xsd:string\"/>");
	    }
            if(v instanceof AssociationDef && ((AssociationDef)v).isLightweight()){
		    ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\"/>");
		    ipw.println ("<xsd:attribute name=\"EXTREF\" type=\"IliID\"/>");
		    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
                    ipw.println ("<xsd:attribute name=\"NEXT_TID\" type=\"IliID\"/>");
            }
	    ipw.unindent ();
    ipw.println ("</xsd:complexType>");
  }



  protected void declareAttribute (Topic currentTopic,AttributeDef attribute)
  {
    Type domain= attribute.getDomainResolvingAliases();
    if (domain instanceof PolylineType){
		ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"POLYLINE\">");
				    ipw.indent ();
					declarePolylineValue(currentTopic,(PolylineType)domain);
				    ipw.unindent ();
				ipw.println ("</xsd:element>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (domain instanceof SurfaceOrAreaType){
		ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"SURFACE\">");
				    ipw.indent ();
					ipw.println ("<xsd:complexType>");
					    ipw.indent ();
					    ipw.println ("<xsd:sequence>");
						    ipw.indent ();
							ipw.println ("<xsd:element name=\"BOUNDARY\" maxOccurs=\"unbounded\">");
							    ipw.indent ();
								ipw.println ("<xsd:complexType>");
								    ipw.indent ();
								    ipw.println ("<xsd:sequence>");
									    ipw.indent ();
										ipw.println ("<xsd:element name=\"POLYLINE\" maxOccurs=\"unbounded\">");
										    ipw.indent ();
											declarePolylineValue(currentTopic,(SurfaceOrAreaType)domain);
										    ipw.unindent ();
										ipw.println ("</xsd:element>");
									    ipw.unindent ();
								    ipw.println ("</xsd:sequence>");
								    ipw.unindent ();
							    ipw.println ("</xsd:complexType>");
							    ipw.unindent ();
							ipw.println ("</xsd:element>");
						    ipw.unindent ();
					    ipw.println ("</xsd:sequence>");
					    ipw.unindent ();
				    ipw.println ("</xsd:complexType>");
				    ipw.unindent ();
				ipw.println ("</xsd:element>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (domain instanceof CoordType){
		ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"COORD\" type=\"CoordValue\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (domain instanceof CompositionType){
    	CompositionType composition=(CompositionType)domain;
    	Table part=composition.getComponentType();
		ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
			    Cardinality card=composition.getCardinality();
			    String minOccurs="";
			    if(card.getMinimum()!=1){
			    	minOccurs=" minOccurs=\""+Long.toString(card.getMinimum())+"\"";
			    }
			    String maxOccurs="";
			    if(card.getMaximum()!=1){
			    	if(card.getMaximum()==Cardinality.UNBOUND){
				    	maxOccurs=" maxOccurs=\"unbounded\"";
			    	}else{
				    	maxOccurs=" maxOccurs=\""+Long.toString(card.getMaximum())+"\"";
			    	}
			    }
				ipw.println ("<xsd:element name=\""+getTransferName(part)+"\""+minOccurs+maxOccurs+">");
				    ipw.indent ();
				    declareObject(currentTopic,part, /* isStructureValue */ true, /* isLineAttribute */ false);
				    ipw.unindent ();
				ipw.println ("</xsd:element>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (domain instanceof ReferenceType){
	    ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:simpleContent>");
			    ipw.indent ();
			    ipw.println ("<xsd:extension base=\"xsd:string\">");
			      ipw.indent ();
		              ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\"/>");
		              ipw.println ("<xsd:attribute name=\"EXTREF\" type=\"IliID\"/>");
        		      ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
			      ipw.unindent ();
			    ipw.println ("</xsd:extension>");
			    ipw.unindent ();
		    ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (domain instanceof BasketType){
		ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"BASKETVALUE\" type=\"BasketValue\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if(domain instanceof EnumerationType){
      ipw.println ("<xsd:simpleType>");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:string\">");
          ipw.indent ();
          java.util.ArrayList ev=new java.util.ArrayList();
          buildEnumList(ev,"",((EnumerationType)domain).getEnumeration());
          Iterator iter=ev.iterator();
          while(iter.hasNext()){
            String value=(String)iter.next();
            ipw.println ("<xsd:enumeration value=\""+value+"\"/>");
          }
          ipw.unindent ();
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(domain instanceof NumericType){
      ipw.println ("<xsd:simpleType>");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:double\">");
          ipw.indent ();
          ipw.println ("<xsd:minInclusive value=\""+((NumericType)domain).getMinimum().doubleValue()+"\"/>");
          ipw.println ("<xsd:maxInclusive value=\""+((NumericType)domain).getMaximum().doubleValue()+"\"/>");
          ipw.unindent ();
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(domain instanceof TextType){
      ipw.println ("<xsd:simpleType>");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:string\">");
          ipw.indent ();
          ipw.println ("<xsd:maxLength value=\""+((TextType)domain).getMaxLength()+"\"/>");
          ipw.unindent ();
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else{
	    ipw.println ("<xsd:complexType>");
		    ipw.indent ();
		    ipw.println ("<xsd:simpleContent>");
			    ipw.indent ();
			    ipw.println ("<xsd:extension base=\"xsd:string\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
	}
  }



  private void declarePolylineValue(Topic currentTopic,LineType domain)
  {
	ipw.println ("<xsd:complexType>");
	    ipw.indent ();
                        boolean hasLineAttr=false;
			if (domain instanceof SurfaceOrAreaType){
			    Table part=((SurfaceOrAreaType)domain).getLineAttributeStructure();
			    if(part!=null){
                                hasLineAttr=true;
                	        ipw.println ("<xsd:sequence>");
	                       	        ipw.indent ();
					ipw.println ("<xsd:element name=\"LINEATTR\">");
					    ipw.indent ();
						ipw.println ("<xsd:complexType>");
						    ipw.indent ();
						    ipw.println ("<xsd:sequence>");
							    ipw.indent ();
								ipw.println ("<xsd:element name=\""+getTransferName(part)+"\">");
								    ipw.indent ();
								    declareObject(currentTopic,part,/* isStructureValue */ true, /* isLineAttribute */ true);
								    ipw.unindent ();
								ipw.println ("</xsd:element>");
							    ipw.unindent ();
						    ipw.println ("</xsd:sequence>");
						    ipw.unindent ();
					    ipw.println ("</xsd:complexType>");
					    ipw.unindent ();
					ipw.println ("</xsd:element>");
			    }
			}
				        ipw.println ("<xsd:choice minOccurs=\"2\" maxOccurs=\"unbounded\">");
					    ipw.indent ();
						ipw.println ("<xsd:element name=\"P\" type=\"CoordValue\"/>");
                                                Set lfv=new HashSet(Arrays.asList(domain.getLineForms()));
                                                if(lfv.contains(td.INTERLIS.ARCS)){
						  ipw.println ("<xsd:element name=\"ARC\" type=\"ArcPoint\"/>");
                                                }
                                                Iterator it=lfv.iterator();
                                                while(it.hasNext()){
                                                  LineForm lf=(LineForm)it.next();
                                                  if(lf==td.INTERLIS.ARCS || lf==td.INTERLIS.STRAIGHTS){
                                                    continue;
                                                  }
                                                Table segmentStruct=lf.getSegmentStructure();
						ipw.println ("<xsd:element name=\""+getTransferName(segmentStruct)+"\">");
						    ipw.indent ();
					            declareObject(currentTopic,segmentStruct,/* isStructureValue */ true, /* isLineAttribute */ false);
						    ipw.unindent ();
						ipw.println ("</xsd:element>");

                                                }

					    ipw.unindent ();
				        ipw.println ("</xsd:choice>");
                        if(hasLineAttr){
			                ipw.unindent ();
		                ipw.println ("</xsd:sequence>");
                        }
	    ipw.unindent ();
    ipw.println ("</xsd:complexType>");
  }

  private RoleDef getOppEnd(RoleDef oneRole){
    RoleDef role1=null;
    RoleDef role2=null;
    Iterator rolei = ((AssociationDef)oneRole.getContainer()).getAttributesAndRoles();
    while (rolei.hasNext())
    {
      Object obj = rolei.next();
      if (obj instanceof RoleDef)
      {
        if(role1==null){
	  role1 = (RoleDef) obj;
        }else if(role2==null){
	  role2 = (RoleDef) obj;
        }
      }
    }
    if(role1==oneRole){
      return role2;
    }
    return role1;
  }

  private void buildEnumList(java.util.List accu,String prefix1,ch.interlis.ili2c.metamodel.Enumeration enumer){
      Iterator iter = enumer.getElements();
      String prefix="";
      if(prefix1.length()>0){
        prefix=prefix1+".";
      }
      while (iter.hasNext()) {
        ch.interlis.ili2c.metamodel.Enumeration.Element ee=(ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next();
        ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
        if (subEnum != null)
        {
          // ee is not leaf, add its name to prefix and add sub elements to accu
          buildEnumList(accu,prefix+ee.getName(),subEnum);
        }else{
          // ee is a leaf, add it to accu
          accu.add(prefix+ee.getName());
        }
      }
  }

}
