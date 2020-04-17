package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;
import java.io.Writer;
import java.util.*;

/** writes a XML-schema according to the ILi specification (ch. 3)
 * 
 * @author ce
 */
public final class XSD22Generator
{
  IndentPrintWriter   ipw;
  TransferDescription td;
  int                 numErrors = 0;


  static ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());


  private XSD22Generator (Writer out, TransferDescription td)
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
    XSD22Generator d = new XSD22Generator (out, td);
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

    if (elt instanceof Domain)
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
	ipw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
      +" xmlns=\"http://www.interlis.ch/INTERLIS2.2\""
      +" targetNamespace=\"http://www.interlis.ch/INTERLIS2.2\""
      +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\""
      +">");
	    ipw.indent ();
		ipw.println ("<xsd:annotation>");
		ipw.indent ();
		ipw.print("<xsd:appinfo source=\"http://www.interlis.ch/ili2c/ili2cversion\">");
		ipw.print(TransferDescription.getVersion());
		ipw.println ("</xsd:appinfo>");
		ipw.unindent ();
		ipw.println ("</xsd:annotation>");

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

            generateAliasTable();

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
  		            ipw.println ("<xsd:element name=\"R\" type=\"xsd:double\" minOccurs=\"0\"/>");
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
		              ipw.println ("<xsd:attribute name=\"ORDER_POS\" type=\"xsd:positiveInteger\"/>");
                              ipw.unindent ();
			    ipw.println ("</xsd:extension>");
			    ipw.unindent ();
		    ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


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
                                                  // type and element names are in a different symbol space, and
                                                  // can therefore have the same name
						  ipw.println ("<xsd:element name=\""+getTransferName(topic)+"\" type=\""+getTransferName(topic)+"\"/>");
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

            // declare XML-Schema types
            declareDomainDef(td.INTERLIS.HALIGNMENT);
            declareDomainDef(td.INTERLIS.VALIGNMENT);
            declareDomainDef(td.INTERLIS.URI);
            declareDomainDef(td.INTERLIS.NAME);
            declareDomainDef(td.INTERLIS.INTERLIS_1_DATE);
            declareAbstractClassDef(td.INTERLIS.METAOBJECT);
            declareAbstractClassDef(td.INTERLIS.METAOBJECT_TRANSLATION);
            declareAbstractClassDef(td.INTERLIS.AXIS);
            declareAbstractClassDef(td.INTERLIS.REFSYSTEM);
            declareAbstractClassDef(td.INTERLIS.COORDSYSTEM);
            declareAbstractClassDef(td.INTERLIS.SCALSYSTEM);
            declareAbstractClassDef(td.INTERLIS.SIGN);

	    modeli = td.iterator ();
	    while (modeli.hasNext ())
	    {
	      Object mObj = modeli.next ();
	      if ((mObj instanceof Model) && !(mObj instanceof PredefinedModel))
	      {
	      	Model model=(Model)mObj;
	      	Iterator topici=model.iterator();
	      	while(topici.hasNext()){
	      		Object tObj=topici.next();
			if (tObj instanceof Topic){
			  Topic topic=(Topic)tObj;
		  	  declareTopic(topic);
	      		}
                    // model level domaindefs
                     if(tObj instanceof Domain){
                      declareDomainDef((Domain)tObj);
                     }
                     // modellevel classes
                     if(tObj instanceof AbstractClassDef){
                	declareAbstractClassDef((AbstractClassDef)tObj);
                     }
	      	}
	      }
	    }

	    ipw.unindent ();
    ipw.println ("</xsd:schema>");


  }

  private void generateAliasTable()
  {
    ipw.println("<!-- ALIAS TABLE ");
    ipw.indent();
    
    //
    // enumerate all models
    //
    Iterator modeli = td.iterator ();
    while (modeli.hasNext ())
    {
      Object mObj = modeli.next ();
      if ((mObj instanceof Model) && !suppressModel ((Model) mObj))
      {
	Model model=(Model)mObj;
        ipw.println ("<ENTRIES FOR=\""+getTransferName(model)+"\">");
        ipw.indent();
        
        //
        // enumerate all elements of current model
        //
	Iterator topici=model.iterator();
        java.util.Set modelelev=new java.util.HashSet();
	while(topici.hasNext()){
	  Object tObj=topici.next();
          //ipw.println("-- modellevel elements");
          if ((tObj instanceof Viewable) && !suppressViewableInAliasTable((Viewable)tObj)){
	    Viewable v = (Viewable) tObj;
            modelelev.add(v);
            ipw.println ("<TAGENTRY FROM=\""+getTransferName(v)+"\" TO=\""+getTransferName(v)+"\"/>");
            Iterator xvi=sortMetamodelElements(v.getExtensions()).iterator();
            while(xvi.hasNext()){
              Viewable xv=(Viewable)xvi.next();
              if(xv!=v){ // v.getExtensions() liefert auch sich selbst d.h. v
                if(!suppressViewableInAliasTable(xv)){
                  modelelev.add(xv);
                  ipw.println ("<TAGENTRY FROM=\""+getTransferName(xv)+"\" TO=\""+getTransferName(v)+"\"/>");
                }
                // alle weiteren Attribute loeschen
                Iterator attri=xv.iterator();
                while(attri.hasNext()){
                  Object attro=attri.next();
                  if(attro instanceof AttributeDef){
                    AttributeDef attr=(AttributeDef)attro;
                    if(attr.getExtending()==null){
                      ipw.println ("<DELENTRY TAG=\""+getTransferName(xv)+"."+getTransferName(attr)+"\"/>");
                    }
                  }
                }
              }
            }
          }
        }
	topici=model.iterator();
	while(topici.hasNext()){
		Object tObj=topici.next();
		if ((tObj instanceof Topic) && !suppressTopicInAliasTable ((Topic) tObj)){
		  Topic topic=(Topic)tObj;
                  //ipw.println("-- topic");
                  ipw.println ("<TAGENTRY FROM=\""+getTransferName(topic)+"\" TO=\""+getTransferName(topic)+"\"/>");
                  // fuer alle direkten oder indirekten erweiterten Themen
                  Iterator xtopici=sortMetamodelElements(topic.getExtensions()).iterator();
                  while(xtopici.hasNext()){
                    Topic xtopic=(Topic)xtopici.next();
                    if(xtopic!=topic && !suppressTopicInAliasTable(xtopic)){
                      ipw.println ("<TAGENTRY FROM=\""+getTransferName(xtopic)+"\" TO=\""+getTransferName(topic)+"\"/>");
                    }
                  }
                  //ipw.println("-- topiclevel elements");
                  // fuer alle definierten Elemente
                  java.util.Set elev=new java.util.HashSet();
                  Iterator elei=topic.iterator();
                  while(elei.hasNext()){
                    Object ele=elei.next();
                    if ((ele instanceof Viewable) && !suppressViewableInAliasTable((Viewable)ele)){
                      Viewable v = (Viewable) ele;
                      elev.add(v);
                      ipw.println ("<TAGENTRY FROM=\""+getTransferName(v)+"\" TO=\""+getTransferName(v)+"\"/>");
                      Iterator xvi=sortMetamodelElements(v.getExtensions()).iterator();
                      while(xvi.hasNext()){
                        Viewable xv=(Viewable)xvi.next();
                        if(xv!=v){
                          if(!suppressViewableInAliasTable(xv)){
                            elev.add(xv);
                            ipw.println ("<TAGENTRY FROM=\""+getTransferName(xv)+"\" TO=\""+getTransferName(v)+"\"/>");
                          }
                          // alle weiteren Attribute loeschen
                          Iterator attri=xv.iterator();
                          while(attri.hasNext()){
                            Object attro=attri.next();
                            if(attro instanceof AttributeDef){
                              AttributeDef attr=(AttributeDef)attro;
                              if(attr.getExtending()==null){
                                ipw.println ("<DELENTRY TAG=\""+getTransferName(xv)+"."+getTransferName(attr)+"\"/>");
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                  //ipw.println("-- delete elements");
                  // fuer alle direkten oder indirekten erweiterten Themen
                  xtopici=sortMetamodelElements(topic.getExtensions()).iterator();
                  while(xtopici.hasNext()){
                    Topic xtopic=(Topic)xtopici.next();
                    if(xtopic!=topic && !suppressTopicInAliasTable(xtopic)){
                      // fuer alle definierten Elemente
                      elei=xtopic.iterator();
                      while(elei.hasNext()){
                        Object ele=elei.next();
                        if ((ele instanceof Viewable) && !suppressViewableInAliasTable((Viewable)ele)){
                          Viewable v = (Viewable) ele;
                          // kein erweitertes element?
                          if(!elev.contains(v) && !modelelev.contains(v)){
                            ipw.println ("<DELENTRY TAG=\""+getTransferName(v)+"\"/>");
                          }
                        }
                      }
                    }
                  }
                }
	}
        // fuer alle direkten oder indirekten uebersetzungen

        ipw.unindent();
        ipw.println ("</ENTRIES>");
      }
    }
    ipw.unindent();
    ipw.println("    ALIAS TABLE -->");
  }


  private ArrayList sortMetamodelElements(Collection c)
  {
	  ArrayList ret=new ArrayList(c);
	  java.util.Collections.sort(ret,new java.util.Comparator(){
		  public int compare(Object o1,Object o2){
			  Element e1=(Element)o1;
			  Element e2=(Element)o2;
			  return e1.getScopedName(null).compareTo(e2.getScopedName(null));
		  }
	  });
	  return ret;
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
  protected boolean suppressTopicInAliasTable (Topic topic)
  {
    return suppressTopic(topic);
  }


  protected boolean suppressViewable (Viewable v)
  {
    if (v == null)
      return true;


    if (v.isAbstract())
      return true;

    if(v instanceof AssociationDef){
		AssociationDef assoc=(AssociationDef)v;
		//System.err.println(assoc);
    	if(assoc.isLightweight()){
    		return true;
    	}
		if(assoc.getDerivedFrom()!=null){
			return true;
		}
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

  protected boolean suppressViewableInAliasTable(Viewable v)
  {
    if (v == null)
      return true;

    if (v.isAbstract())
      return true;

    Topic topic;
    if ((v instanceof View) && ((topic=(Topic)v.getContainer (Topic.class)) != null)
	    && !topic.isViewTopic())
      return true;

    return false;
  }



  protected void declareTopic (Topic topic)
  {

   Iterator iter = topic.iterator();
   while (iter.hasNext())
   {
     Object obj = iter.next();
     if(obj instanceof Domain){
      declareDomainDef((Domain)obj);
     }
     if(obj instanceof AbstractClassDef){
	declareAbstractClassDef((AbstractClassDef)obj);
     }
   }
    ipw.println ("<xsd:complexType name=\""+getTransferName(topic)+"\">");
	    ipw.indent ();
	    ipw.println ("<xsd:sequence>");
		    ipw.indent ();
		    ipw.println ("<xsd:choice minOccurs=\"0\" maxOccurs=\"unbounded\">");
			    ipw.indent ();
			    /* Find which viewables are going to be elements of this topic. */
			    iter = topic.getViewables().iterator();
			    while (iter.hasNext())
			    {
			      Object obj = iter.next();
			      if ((obj instanceof Viewable) && !suppressViewable ((Viewable)obj))
			      {
				Viewable v = (Viewable) obj;
				
		    	        ipw.println ("<xsd:element name=\""+getTransferName(v)+"\">");
			          ipw.indent ();
	                          ipw.println ("<xsd:complexType>");
			            ipw.indent ();
		                    ipw.println ("<xsd:complexContent>");
			              ipw.indent ();
		                      ipw.println ("<xsd:extension base=\""+getTransferName(v)+"\">");
			                ipw.indent ();
                	    	        // Objects have an ID and an operation flag to allow incremental update
                		        ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");
                		        ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
                		        ipw.println ("<xsd:attribute name=\"OPERATION\" type=\"xsd:string\"/>");
			                ipw.unindent ();
		                      ipw.println ("</xsd:extension>");
		                      ipw.unindent ();
		                    ipw.println ("</xsd:complexContent>");
		                    ipw.unindent ();
		                  ipw.println ("</xsd:complexType>");
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



  protected void declareAbstractClassDef(Viewable v)
  {
	ipw.println("<xsd:complexType  name=\"" + getTransferName(v) + "\">");
	ipw.indent();
	ipw.println("<xsd:sequence>");
	ipw.indent();
	/* Find which attributes are going to be elements of this class. */
	Iterator iter = v.getAttributesAndRoles2();
	while (iter.hasNext()) {
		ViewableTransferElement obj = (ViewableTransferElement)iter.next();
		if (obj.obj instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) obj.obj;
			declareAttribute(attr);
		}
		if(obj.obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj.obj;

			// not an embedded role and roledef not defined in a lightweight association?
			if (!obj.embedded && !((AssociationDef)v).isLightweight()){
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(role)
						+ "\" type=\"RoleType\"/>");
			}
			// a role of an embedded association?
			if(obj.embedded){
				AssociationDef roleOwner = (AssociationDef) role.getContainer();
				if(roleOwner.getDerivedFrom()==null){
					// role is oppend;
					Cardinality card = role.getCardinality();
					String minOccurs = "";
					if (card.getMinimum() == 0) {
						minOccurs = " minOccurs=\"0\"";
					}
					ipw.println(
						"<xsd:element name=\""
							+ getTransferName(role)
							+ "\""
							+ minOccurs
							+ ">");
					ipw.indent();
					ipw.println("<xsd:complexType>");
					ipw.indent();
					// not just a link?
					if (roleOwner.getAttributes().hasNext()
						|| roleOwner.getLightweightAssociations().iterator().hasNext()) {
						ipw.println("<xsd:sequence>");
						ipw.indent();
						ipw.println(
							"<xsd:element name=\""
								+ getTransferName(roleOwner)
								+ "\" type=\""
								+ getTransferName(roleOwner)
								+ "\"/>");
						ipw.unindent();
						ipw.println("</xsd:sequence>");
					}
					ipw.println("<xsd:attribute name=\"REF\" type=\"IliID\"/>");
					ipw.println("<xsd:attribute name=\"EXTREF\" type=\"IliID\"/>");
					ipw.println("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
					ipw.println("<xsd:attribute name=\"ORDER_POS\" type=\"xsd:positiveInteger\"/>");
					ipw.unindent();
					ipw.println("</xsd:complexType>");
					ipw.unindent();
					ipw.println("</xsd:element>");
				}
			}
		}
	}
	ipw.unindent();
	ipw.println("</xsd:sequence>");
	/*
	       if(v instanceof AssociationDef && ((AssociationDef)v).isLightweight()){
		    ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\"/>");
		    ipw.println ("<xsd:attribute name=\"EXTREF\" type=\"IliID\"/>");
		    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
	               ipw.println ("<xsd:attribute name=\"NEXT_TID\" type=\"IliID\"/>");
	       }
	*/
	ipw.unindent();
	ipw.println("</xsd:complexType>");

  }


  protected void declareDomainDef (Domain domain)
  {
    declareType(domain.getType(),domain);
  }
  protected void declareAttribute (AttributeDef attribute)
  {
  	try{
		String minOccurs = "";
		if (!attribute.getDomain().isMandatoryConsideringAliases()) {
			minOccurs = " minOccurs=\"0\"";
		}
		Type type = attribute.getDomain();
		if (type instanceof TypeAlias) {
			if (((TypeAlias) type).getAliasing() == td.INTERLIS.BOOLEAN) {
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(attribute)
						+ "\" type=\"xsd:boolean\""
						+ minOccurs
						+ "/>");
			} else {
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(attribute)
						+ "\" type=\""
						+ getTransferName(((TypeAlias) type).getAliasing())
						+ "\""
						+ minOccurs
						+ "/>");
			}
		} else {
			ipw.println(
				"<xsd:element name=\""
					+ getTransferName(attribute)
					+ "\""
					+ minOccurs
					+ ">");
			ipw.indent();
			declareType(type, null);
			ipw.unindent();
			ipw.println("</xsd:element>");
		}
	}
	catch(java.lang.Exception ex){
		System.err.println(attribute.toString());
		ex.printStackTrace(System.err);
	}
  }
  protected void declareType (Type type,Domain domain)
  {
    String typeName="";
    if(domain!=null){
      typeName=" name=\""+getTransferName(domain)+"\"";
    }
    if (type instanceof PolylineType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"POLYLINE\">");
				    ipw.indent ();
					declarePolylineValue((PolylineType)type);
				    ipw.unindent ();
				ipw.println ("</xsd:element>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof SurfaceOrAreaType){
		ipw.println ("<xsd:complexType"+typeName+">");
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
											declarePolylineValue((SurfaceOrAreaType)type);
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
    }else if (type instanceof CoordType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"COORD\" type=\"CoordValue\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof CompositionType){
    	CompositionType composition=(CompositionType)type;
    	Table part=composition.getComponentType();
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
			    Cardinality card=composition.getCardinality();
			    String minOccurs="";
			    if(card.getMinimum()>1){
			    	// cases 0 and 1 are are handled at attribute level
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
			    // TODO: add only extensions that are in this scope
			    java.util.Set extv=part.getExtensions();
			    if(extv.size()>1){
					ipw.println ("<xsd:choice"+minOccurs+maxOccurs+">");
						ipw.indent ();
						Iterator exti=extv.iterator();
						while(exti.hasNext()){
							Table ext=(Table)exti.next();
							ipw.println ("<xsd:element name=\""+getTransferName(ext)+"\" type=\""+getTransferName(ext)+"\"/>");
						}
						ipw.unindent ();
					ipw.println ("</xsd:choice>");
			    }else{
					ipw.println ("<xsd:element name=\""+getTransferName(part)+"\" type=\""+getTransferName(part)+"\""+minOccurs+maxOccurs+"/>");
			    }
		        ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof ReferenceType){
	    ipw.println ("<xsd:complexType"+typeName+">");
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
    }else if (type instanceof BasketType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"BASKETVALUE\" type=\"BasketValue\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if(type instanceof EnumerationType){
      ipw.println ("<xsd:simpleType"+typeName+">");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:string\">");
          ipw.indent ();
          java.util.ArrayList ev=new java.util.ArrayList();
          buildEnumList(ev,"",((EnumerationType)type).getConsolidatedEnumeration());
          Iterator iter=ev.iterator();
          while(iter.hasNext()){
            String value=(String)iter.next();
            ipw.println ("<xsd:enumeration value=\""+value+"\"/>");
          }
          ipw.unindent ();
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(type instanceof NumericType){
      ipw.println ("<xsd:simpleType"+typeName+">");
        ipw.indent ();
		if(type.isAbstract()){
			ipw.println ("<xsd:restriction base=\"xsd:double\">");
			ipw.println ("</xsd:restriction>");
		}else{
			PrecisionDecimal min=((NumericType)type).getMinimum();
			if(min.getAccuracy()>0){
				ipw.println ("<xsd:restriction base=\"xsd:double\">");
				ipw.indent ();
				ipw.println ("<xsd:minInclusive value=\""+((NumericType)type).getMinimum().doubleValue()+"\"/>");
				ipw.println ("<xsd:maxInclusive value=\""+((NumericType)type).getMaximum().doubleValue()+"\"/>");
				ipw.unindent ();
				ipw.println ("</xsd:restriction>");
			}else{
				ipw.println ("<xsd:restriction base=\"xsd:integer\">");
				ipw.indent ();
				ipw.println ("<xsd:minInclusive value=\""+(int)((NumericType)type).getMinimum().doubleValue()+"\"/>");
				ipw.println ("<xsd:maxInclusive value=\""+(int)((NumericType)type).getMaximum().doubleValue()+"\"/>");
				ipw.unindent ();
				ipw.println ("</xsd:restriction>");
			}
		}
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(type instanceof TextType){
      ipw.println ("<xsd:simpleType"+typeName+">");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:string\">");
        if(((TextType)type).getMaxLength()>0){
			ipw.indent ();
			ipw.println ("<xsd:maxLength value=\""+((TextType)type).getMaxLength()+"\"/>");
			ipw.unindent ();
        }
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else{
	    ipw.println ("<xsd:complexType"+typeName+">");
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



  private void declarePolylineValue(LineType domain)
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
						      ipw.println ("<xsd:element name=\""+getTransferName(part)+"\" type=\""+getTransferName(part)+"\"/>");
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
						ipw.println ("<xsd:element name=\"COORD\" type=\"CoordValue\"/>");
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
						ipw.println ("<xsd:element name=\""+getTransferName(segmentStruct)+"\" type=\""+getTransferName(segmentStruct)+"\"/>");
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
  public static HashMap getTagMap(TransferDescription td){
	  HashMap ret=new HashMap();
	  Iterator modeli = td.iterator ();
	  while (modeli.hasNext ())
	  {
		Object mObj = modeli.next ();
		if(mObj instanceof Model){
		  Model model=(Model)mObj;
		  Iterator topici=model.iterator();
		  while(topici.hasNext()){
			Object tObj=topici.next();
			if (tObj instanceof Topic){
				Topic topic=(Topic)tObj;
				Iterator iter = topic.getViewables().iterator();
				while (iter.hasNext())
				{
				  Object obj = iter.next();
				  if(obj instanceof Viewable){
					  Viewable v=(Viewable)obj;
					  //log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
					  ret.put(v.getScopedName(null),v);
				  }
				}
			}else if(tObj instanceof Viewable){
				Viewable v=(Viewable)tObj;
				//log.logMessageString("getTransferViewables() leave <"+v+">",IFMELogFile.FME_INFORM);
				ret.put(v.getScopedName(null),v);
			}
		  }
		}
	  }
	  return ret;
  }

}
