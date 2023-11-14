/* This file is part of the INTERLIS-compiler.
 * For more information, please see <http://www.umleditor.org/>.
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
package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;

import java.io.Writer;
import java.util.*;

import ch.ehi.basics.io.IndentPrintWriter;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;

/** writes a XML-schema according to the ILi specification (ch. 3)
 * 
 * @author ce
 */
public final class XSDGenerator
{
  IndentPrintWriter   ipw;
  TransferDescription td;
  int                 numErrors = 0;
  private Settings settings=null;

    // Settings
    public static final String SETTING_FULL_XTF_CAPABILITIES="ch.interlis.ili2c.generator.XSDGenerator.fullCapabilities";
    
  static ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());


  private XSDGenerator (Writer out, TransferDescription td,Settings settings)
  {
    ipw = new IndentPrintWriter (out);
    this.td = td;
    this.settings=settings;
  }



  private void finish()
  {
    ipw.close();
  }



  public static int generate (Writer out, TransferDescription td)
  {
    XSDGenerator d = new XSDGenerator (out, td,new Settings());
   // d.findItemsToDeclare (td);
    d.printXSD (td);
    d.finish();
    return d.numErrors;
  }
  public static int generate (Writer out, TransferDescription td,Settings settings)
  {
    XSDGenerator d = new XSDGenerator (out, td,settings);
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
	  
	  boolean addAllInterlisTypes=false;
	  boolean addAliasTable=false;
	  Model lastModel=td.getLastModel();
	  if(lastModel!=null){
		  addAllInterlisTypes=getMetaValueBoolean(lastModel,Ili2cMetaAttrs.ILI2C_ILI23XSD_ADDALLINTERLISTYPESDEFAULT,false);
		  addAliasTable=getMetaValueBoolean(lastModel,Ili2cMetaAttrs.ILI2C_ILI23XSD_ADDALIASTABLEDEFAULT,false);
	  }
		ipw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
      +" xmlns=\"http://www.interlis.ch/INTERLIS2.3\" xmlns:ili2c=\"http://www.interlis.ch/ili2c\""
      +" targetNamespace=\"http://www.interlis.ch/INTERLIS2.3\""
      +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\""
      +">");
	    ipw.indent ();
	    
		ipw.println ("<xsd:annotation>");
		ipw.indent ();
		ipw.print("<xsd:appinfo source=\"http://www.interlis.ch/ili2c/ili2cversion\">");
		ipw.print(TransferDescription.getVersion());
		ipw.println ("</xsd:appinfo>");
		{
			Iterator modeli=sortMetamodelElements(td.iterator()).iterator();
			while(modeli.hasNext()){
				Object modelo=modeli.next();
				if(modelo instanceof Model){
					Model model=(Model)modelo;
					if(model==td.INTERLIS){
						// skip built-in model
						continue;
					}
					ipw.println("<xsd:appinfo source=\"http://www.interlis.ch/ili2c\">");
					ipw.indent();
					ipw.println("<ili2c:model>"+model.getName()+"</ili2c:model>");
					ipw.println("<ili2c:modelVersion>"+model.getModelVersion()+"</ili2c:modelVersion>");
					if(model.getModelVersionExpl()!=null){
						ipw.println("<ili2c:modelVersionExplanation>"+model.getModelVersionExpl()+"</ili2c:modelVersionExplanation>");
					}
					ipw.println("<ili2c:modelAt>"+model.getIssuer()+"</ili2c:modelAt>");
					String textMinimalCharset=model.getMetaValue(Ili2cMetaAttrs.ILI2C_TEXTMINIMALCHARSET);
					if(textMinimalCharset!=null){
						ipw.println("<ili2c:textMinimalCharset>"+textMinimalCharset+"</ili2c:textMinimalCharset>");
					}else{
						ipw.println("<ili2c:textMinimalCharset>ili23AnnexB</ili2c:textMinimalCharset>");
					}
					ipw.unindent();
					ipw.println ("</xsd:appinfo>");
				}
			}
		}
		ipw.unindent ();
		ipw.println ("</xsd:annotation>");

	    ipw.println ("<xsd:element name=\"TRANSFER\" type=\"Transfer\"/>");

	    ipw.println ("<xsd:simpleType name=\"IliID\">");
		    ipw.indent ();
                    ipw.println ("<xsd:restriction base=\"xsd:string\">");
  		      ipw.indent ();
                      ipw.println ("<xsd:pattern value=\"([a-zA-Z_][0-9a-zA-Z_\\-\\.]*:)?[0-9a-zA-Z_][0-9a-zA-Z_\\-\\.]*\"/>");
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
	    
	    if(addAliasTable){
            generateAliasTable();
	    }



		ipw.println ("<xsd:complexType name=\"Models\">");
			ipw.indent ();
			ipw.println ("<xsd:sequence>");
				ipw.indent ();
				ipw.println ("<xsd:element name=\"MODEL\" type=\"Model\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
				ipw.unindent ();
			ipw.println ("</xsd:sequence>");
			ipw.unindent ();
		ipw.println ("</xsd:complexType>");
		
		ipw.println ("<xsd:complexType name=\"Model\">");
			ipw.indent ();
			ipw.println ("<xsd:attribute name=\"NAME\" type=\"INTERLIS.NAME\" use=\"required\"/>");
			ipw.println ("<xsd:attribute name=\"VERSION\" type=\"xsd:string\" use=\"required\"/>");
			ipw.println ("<xsd:attribute name=\"URI\" type=\"xsd:anyURI\" use=\"required\"/>");
			ipw.unindent ();
		ipw.println ("</xsd:complexType>");
	
	    if(addAliasTable){
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
			ipw.println ("<xsd:attribute name=\"TAG\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"ATTR\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"FROM\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"TO\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");


	    ipw.println ("<xsd:complexType name=\"Delentry\">");
		    ipw.indent ();
		    ipw.println ("<xsd:attribute name=\"TAG\" type=\"xsd:string\" use=\"required\"/>");
			ipw.println ("<xsd:attribute name=\"ATTR\" type=\"xsd:string\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
	    	
	    }

	    ipw.println ("<xsd:complexType name=\"RoleType\">");
		    ipw.indent ();
		    ipw.println ("<xsd:simpleContent>");
			    ipw.indent ();
			    ipw.println ("<xsd:extension base=\"xsd:string\">");
    			      ipw.indent ();
		              ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\" use=\"required\"/>");
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
				    Iterator modeli = sortMetamodelElements(td.iterator ()).iterator();
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

	    modeli = sortMetamodelElements(td.iterator ()).iterator();
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
	    	if(addAllInterlisTypes || refCoordValue){
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
	    	}
	    	if(addAllInterlisTypes || refArcPoint){
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
	    	}
	    	
	        declareDomainDef(td.INTERLIS.NAME); // define always because of MODELS

	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.METAOBJECT)){
	            declareAbstractClassDef(td.INTERLIS.METAOBJECT);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.METAOBJECT_TRANSLATION)){
	            declareAbstractClassDef(td.INTERLIS.METAOBJECT_TRANSLATION);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.AXIS)){
	            declareAbstractClassDef(td.INTERLIS.AXIS);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.REFSYSTEM)){
	            declareAbstractClassDef(td.INTERLIS.REFSYSTEM);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.COORDSYSTEM)){
	            declareAbstractClassDef(td.INTERLIS.COORDSYSTEM);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.SCALSYSTEM)){
	            declareAbstractClassDef(td.INTERLIS.SCALSYSTEM);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.SIGN)){
	            declareAbstractClassDef(td.INTERLIS.SIGN);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.TIMESYSTEMS.CALENDAR)){
				declareAbstractClassDef(td.INTERLIS.TIMESYSTEMS.CALENDAR);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.TIMESYSTEMS.TIMEOFDAYSYS)){
				declareAbstractClassDef(td.INTERLIS.TIMESYSTEMS.TIMEOFDAYSYS);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.TimeOfDay)){
				declareAbstractClassDef(td.INTERLIS.TimeOfDay);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.UTC)){
				declareAbstractClassDef(td.INTERLIS.UTC);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.GregorianDate)){
				declareAbstractClassDef(td.INTERLIS.GregorianDate);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.GregorianDateTime)){
				declareAbstractClassDef(td.INTERLIS.GregorianDateTime);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.HALIGNMENT)){
	    		declareDomainDef(td.INTERLIS.HALIGNMENT);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.VALIGNMENT)){
	            declareDomainDef(td.INTERLIS.VALIGNMENT);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.URI)){
	            declareDomainDef(td.INTERLIS.URI);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.INTERLIS_1_DATE)){
	            declareDomainDef(td.INTERLIS.INTERLIS_1_DATE);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.STANDARDOID)){
	            declareDomainDef(td.INTERLIS.STANDARDOID);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.I32OID)){
	            declareDomainDef(td.INTERLIS.I32OID);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.UUIDOID)){
	            declareDomainDef(td.INTERLIS.UUIDOID);
	    	}
	    	if(addAllInterlisTypes || referencedTypes.contains(td.INTERLIS.GregorianYear)){
				declareDomainDef(td.INTERLIS.GregorianYear);
	    	}

		    ipw.println ("<xsd:complexType name=\"HeaderSection\">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
				ipw.println ("<xsd:element name=\"MODELS\" type=\"Models\"/>");

			    if(addAliasTable){
			    	ipw.println ("<xsd:element name=\"ALIAS\" type=\"Alias\" minOccurs=\"0\"/>");
			    }
			    if(refOidSpaces){
			    	ipw.println ("<xsd:element name=\"OIDSPACES\" type=\"OidSpaces\" minOccurs=\"0\"/>");
			    }
		    	ipw.println ("<xsd:element name=\"COMMENT\" type=\"xsd:string\" minOccurs=\"0\"/>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.println ("<xsd:attribute name=\"VERSION\" type=\"xsd:decimal\" use=\"required\" fixed=\"2.3\"/>");
		    ipw.println ("<xsd:attribute name=\"SENDER\" type=\"xsd:string\" use=\"required\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
	    
	    if(refOidSpaces){
			ipw.println ("<xsd:complexType name=\"OidSpaces\">");
			ipw.indent ();
			ipw.println ("<xsd:sequence>");
				ipw.indent ();
				ipw.println ("<xsd:element name=\"OIDSPACE\" type=\"OidSpace\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
				ipw.unindent ();
			ipw.println ("</xsd:sequence>");
			ipw.unindent ();
		ipw.println ("</xsd:complexType>");
			
		ipw.println ("<xsd:complexType name=\"OidSpace\">");
			ipw.indent ();
			ipw.println ("<xsd:attribute name=\"NAME\" type=\"xsd:string\" use=\"required\"/>");
			ipw.println ("<xsd:attribute name=\"OIDDOMAIN\" type=\"xsd:string\" use=\"required\"/>");
			ipw.unindent ();
		ipw.println ("</xsd:complexType>");
	    	
	    }
	  	if(addAllInterlisTypes || refIncrementalTypes){
		    ipw.println ("<xsd:simpleType name=\"OperationType\">");
		    ipw.indent ();
	                ipw.println ("<xsd:restriction base=\"xsd:string\">");
	                ipw.indent ();
	                  ipw.println ("<xsd:enumeration value=\"INSERT\"/>");
	                  ipw.println ("<xsd:enumeration value=\"UPDATE\"/>");
	                  ipw.println ("<xsd:enumeration value=\"DELETE\"/>");
	                ipw.unindent ();
	                ipw.println ("</xsd:restriction>");
		    ipw.unindent ();
		    ipw.println ("</xsd:simpleType>");
		    ipw.println ("<xsd:simpleType name=\"TransferKindType\">");
		    ipw.indent ();
	                ipw.println ("<xsd:restriction base=\"xsd:string\">");
	                ipw.indent ();
	                  ipw.println ("<xsd:enumeration value=\"FULL\"/>");
	                  ipw.println ("<xsd:enumeration value=\"INITIAL\"/>");
	                  ipw.println ("<xsd:enumeration value=\"UPDATE\"/>");
	                ipw.unindent ();
	                ipw.println ("</xsd:restriction>");
		    ipw.unindent ();
		    ipw.println ("</xsd:simpleType>");
	  	}
	  	if(addAllInterlisTypes || refConsistencyType){
		    ipw.println ("<xsd:simpleType name=\"ConsistencyType\">");
		    ipw.indent ();
	                ipw.println ("<xsd:restriction base=\"xsd:string\">");
	                ipw.indent ();
	                  ipw.println ("<xsd:enumeration value=\"COMPLETE\"/>");
	                  ipw.println ("<xsd:enumeration value=\"INCOMPLETE\"/>");
	                  ipw.println ("<xsd:enumeration value=\"INCONSISTENT\"/>");
	                  ipw.println ("<xsd:enumeration value=\"ADAPTED\"/>");
	                ipw.unindent ();
	                ipw.println ("</xsd:restriction>");
		    ipw.unindent ();
		    ipw.println ("</xsd:simpleType>");
	  	}
	    	
	    ipw.unindent ();
    ipw.println ("</xsd:schema>");
    ipw.flush();

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
                      ipw.println ("<DELENTRY TAG=\""+getTransferName(xv)+"\" ATTR=\""+getTransferName(attr)+"\"/>");
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
                                ipw.println ("<DELENTRY TAG=\""+getTransferName(xv)+"\" ATTR=\""+getTransferName(attr)+"\"/>");
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

  private ArrayList sortMetamodelElements(Iterator it)
  {
	  ArrayList ret=new ArrayList();
	  for(Object o=null; it.hasNext();){
		  o=it.next();
		  ret.add(o);
	  }
	  return sortMetamodelElements(ret);
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

  private void reportError(String msg)
  {
	EhiLogger.logError(msg);
	numErrors++;
  }
  private boolean getMetaValueBoolean(Element topic,String metaAttrName,boolean defVal)
  {
	  String val=topic.getMetaValue(metaAttrName);
	  if(val!=null){
		  if(val.equals("true")){
			  return true;
		  }
		  if(val.equals("false")){
			  return false;
		  }
		  reportError(topic.getScopedName(null)+": unexpected value <"+val+"> for "+metaAttrName);
	  }
	  return defVal;
  }
  private boolean getInheritedMetaValueBoolean(Topic topic,String metaAttrName,boolean defVal)
  {
	  String val=topic.getMetaValue(metaAttrName);
	  if(val==null){
		  val=topic.getContainer().getMetaValue(metaAttrName);
	  }
	  if(val!=null){
		  if(val.equals("true")){
			  return true;
		  }
		  if(val.equals("false")){
			  return false;
		  }
		  reportError(topic.getScopedName(null)+": unexpected value <"+val+"> for "+metaAttrName);
	  }
	  return defVal;
  }
  protected void declareTopic (Topic topic)
  {

	boolean supportSourceBasketId=getInheritedMetaValueBoolean(topic,Ili2cMetaAttrs.ILI2C_ILI23XML_SUPPORTSOURCEBASKETID,false);
  	boolean supportIncrementalTransfer=getInheritedMetaValueBoolean(topic,Ili2cMetaAttrs.ILI2C_ILI23XML_SUPPORTINCRMENTALTRANSFER,false);
  	boolean supportInconsistentTransfer=getInheritedMetaValueBoolean(topic,Ili2cMetaAttrs.ILI2C_ILI23XML_SUPPORTINCONSISTENTTRANSFER,false);
    boolean supportPolymorphicRead=getInheritedMetaValueBoolean(topic,Ili2cMetaAttrs.ILI2C_ILI23XML_SUPPORTPOLYMORPHICREAD,false);
    if(supportIncrementalTransfer){
    	refIncrementalTypes=true;
    }
    if(supportInconsistentTransfer){
    	refConsistencyType=true;
    }
    
    Domain basketOid=topic.getBasketOid();
    if(basketOid!=null){
    	refOidSpaces=true;
    }
    Domain objectOid=topic.getOid();
    if(objectOid!=null){
    	refOidSpaces=true;
    }
   Iterator iter = topic.iterator();
   while (iter.hasNext())
   {
     Object obj = iter.next();
     if(obj instanceof Domain){
      declareDomainDef((Domain)obj);
     }
     if(obj instanceof AbstractClassDef){
    	 if(obj instanceof AssociationDef){
    		 AssociationDef assoc=(AssociationDef)obj;
    		 if(assoc.isLightweight()
    			 && !assoc.getAttributes().hasNext()
 				 && !assoc.getLightweightAssociations().iterator().hasNext()){
    			 // assoc is encode as just a ref
    			 // complexType is superficial, will not be referenced
    		 }else{
    			 // assoc is not just a ref, complexType will be referenced
     			declareAbstractClassDef(assoc);
    		 }
    	 }else{
    			declareAbstractClassDef((AbstractClassDef)obj);
    	 }
     }else if(topic.isViewTopic() && obj instanceof View){
			declareViewDef((View)obj);
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
			      if ((obj instanceof Viewable) && !AbstractPatternDef.suppressViewableInTransfer((Viewable)obj))
			      {
						Viewable v = (Viewable) obj;		
	                	if(supportSourceBasketId || supportIncrementalTransfer || supportInconsistentTransfer){
			    	        ipw.println ("<xsd:element name=\""+getTransferName(v)+"\">");
					          ipw.indent ();
			                          ipw.println ("<xsd:complexType>");
					            ipw.indent ();
				                    ipw.println ("<xsd:complexContent>");
					              ipw.indent ();
				                      ipw.println ("<xsd:extension base=\""+getTransferName(v)+"\">");
					                ipw.indent ();
					                	if(supportSourceBasketId){
					    	    			ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
					                	}
				    	    		    if(supportIncrementalTransfer){
					    	    			ipw.println ("<xsd:attribute name=\"OPERATION\" type=\"OperationType\"/>");
				    	    		    }
				    	    		    if(supportInconsistentTransfer){
					    	    			ipw.println ("<xsd:attribute name=\"CONSISTENCY\" type=\"ConsistencyType\"/>");
				    	    		    }
					                ipw.unindent ();
				                      ipw.println ("</xsd:extension>");
				                      ipw.unindent ();
				                    ipw.println ("</xsd:complexContent>");
				                    ipw.unindent ();
				                  ipw.println ("</xsd:complexType>");
				                  ipw.unindent ();
				    	        ipw.println ("</xsd:element>");
						}else{
			    	        ipw.println ("<xsd:element name=\""+getTransferName(v)+"\" type=\""+getTransferName(v)+"\"/>");
						}
			      }
			    }
			    ipw.unindent ();
		    ipw.println ("</xsd:choice>");
		    ipw.unindent ();
	    ipw.println ("</xsd:sequence>");
	    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\" use=\"required\"/>");
	    if(supportPolymorphicRead){
		    ipw.println ("<xsd:attribute name=\"TOPICS\" type=\"xsd:string\"/>");
	    }
	    if(supportIncrementalTransfer){
		    ipw.println ("<xsd:attribute name=\"KIND\" type=\"TransferKindType\"/>");
		    ipw.println ("<xsd:attribute name=\"STARTSTATE\" type=\"xsd:string\"/>");
		    ipw.println ("<xsd:attribute name=\"ENDSTATE\" type=\"xsd:string\"/>");
	    }
	    if(supportInconsistentTransfer){
			ipw.println ("<xsd:attribute name=\"CONSISTENCY\" type=\"ConsistencyType\"/>");
	    }
	    ipw.unindent ();
    ipw.println ("</xsd:complexType>");

  }



private void declareViewDef(View v) {
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
			if(!attr.isTransient()){
				Type proxyType=attr.getDomain();
				if(proxyType!=null && (proxyType instanceof ObjectType)){
					// skip implicit particles (base-viewables) of views
				}else{
					declareAttribute(attr);
					
				}
			}
		}
		if(obj.obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj.obj;

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
					ipw.println("<xsd:attribute name=\"REF\" type=\"IliID\" use=\"required\"/>");
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
	
	// ViewObjects have an ID
	ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");

	ipw.unindent();
	ipw.println("</xsd:complexType>");
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
			if(!attr.isTransient()){
				declareAttribute(attr);
			}
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
					ipw.println("<xsd:attribute name=\"REF\" type=\"IliID\" use=\"required\"/>");
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
	if(!(v instanceof AssociationDef && ((AssociationDef)v).isLightweight())
		&&	!((v instanceof Table) && !((Table)v).isIdentifiable())){
		if(v instanceof AssociationDef){
			AssociationDef assoc=(AssociationDef)v;
			if(assoc.isIdentifiable()){
				// only some LinkObjects have an ID
				ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");
			}else{
				Domain oid=assoc.getDefinedOid();
				/* ignore topic level definition of oid
				 * model should define "ASSOCATION  (OID) = ..." to get an ID
				if(oid==null){
					Topic topic=(Topic)assoc.getContainer(Topic.class);
					if(topic!=null){
						oid=topic.getOid();
					}
				} */
				if(oid!=null && !(oid instanceof NoOid)){
					ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");
			    	refOidSpaces=true;
				}
			}
		}else{
			// Objects have an ID
			ipw.println ("<xsd:attribute name=\"TID\" type=\"IliID\" use=\"required\"/>");
		    Domain oid=((AbstractClassDef)v).getDefinedOid();
			if(oid!=null && !(oid instanceof NoOid)){
		    	refOidSpaces=true;
		    }

		}
	}
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
		Type type = attribute.getDomain();
		if(type==null){
			Evaluable[] ev = (((LocalAttribute)attribute).getBasePaths());
			type=((ObjectPath)ev[0]).getType();
		}
		if (!type.isMandatoryConsideringAliases()) {
			minOccurs = " minOccurs=\"0\"";
		}
		if (type instanceof TypeAlias) {
			if (((TypeAlias) type).getAliasing() == td.INTERLIS.BOOLEAN) {
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(attribute)
						+ "\" type=\"xsd:boolean\""
						+ minOccurs
						+ "/>");
			}else if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlDate) {
					ipw.println(
						"<xsd:element name=\""
							+ getTransferName(attribute)
							+ "\" type=\"xsd:date\""
							+ minOccurs
							+ "/>");
			}else if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlDateTime) {
					ipw.println(
						"<xsd:element name=\""
							+ getTransferName(attribute)
							+ "\" type=\"xsd:dateTime\""
							+ minOccurs
							+ "/>");
			}else if (((TypeAlias) type).getAliasing() == td.INTERLIS.XmlTime) {
					ipw.println(
						"<xsd:element name=\""
							+ getTransferName(attribute)
							+ "\" type=\"xsd:time\""
							+ minOccurs
							+ "/>");
			} else {
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(attribute)
						+ "\" type=\""
						+ getDomainRef(((TypeAlias) type).getAliasing())
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
				ipw.println ("<xsd:element name=\"COORD\" type=\"CoordValue\"/>"); refCoordValue=true;
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
						Iterator exti=sortMetamodelElements(extv).iterator();
						while(exti.hasNext()){
							Table ext=(Table)exti.next();
							ipw.println ("<xsd:element name=\""+getTransferName(ext)+"\" type=\""+getStructureRef(ext)+"\"/>");
						}
						ipw.unindent ();
					ipw.println ("</xsd:choice>");
			    }else{
					ipw.println ("<xsd:element name=\""+getTransferName(part)+"\" type=\""+getStructureRef(part)+"\""+minOccurs+maxOccurs+"/>");
			    }
		        ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof ReferenceType){
	    ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
	        ipw.println ("<xsd:attribute name=\"REF\" type=\"IliID\" use=\"required\"/>");
		    ipw.println ("<xsd:attribute name=\"BID\" type=\"IliID\"/>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
	}else if (type instanceof OIDType){
		ipw.println ("<xsd:complexType"+typeName+">");
			ipw.indent ();
			ipw.println ("<xsd:attribute name=\"OID\" type=\"IliID\" use=\"required\"/>");
			ipw.unindent ();
		ipw.println ("</xsd:complexType>");
		refOidSpaces=true;
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
				ipw.println ("<xsd:minInclusive value=\""+((NumericType)type).getMinimum()+"\"/>");
				ipw.println ("<xsd:maxInclusive value=\""+((NumericType)type).getMaximum()+"\"/>");
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
        if(((TextType)type).isNormalized()){
			ipw.println ("<xsd:restriction base=\"xsd:normalizedString\">");
        }else{
			ipw.println ("<xsd:restriction base=\"xsd:string\">");
        }
        if(((TextType)type).getMaxLength()>0){
			ipw.indent ();
			ipw.println ("<xsd:maxLength value=\""+((TextType)type).getMaxLength()+"\"/>");
			ipw.unindent ();
        }
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
	}else if(type instanceof FormattedType){
		FormattedType ftype=(FormattedType)type;
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
		  Domain baseDomain = ((FormattedType) type).getDefinedBaseDomain();
		  if(baseDomain==td.INTERLIS.XmlDate){
			  ipw.println ("<xsd:restriction base=\"xsd:date\">");
			  ipw.indent ();
				  PrecisionDecimal minv[]=ftype.valueOf(ftype.getMinimum());
				  PrecisionDecimal maxv[]=ftype.valueOf(ftype.getMaximum());
				  String min = getDateValue(minv);
				  String max = getDateValue(maxv);
				  ipw.println ("<xsd:minInclusive value=\""+min+"\"/>");
				  ipw.println ("<xsd:maxInclusive value=\""+max+"\"/>");
			  ipw.unindent ();
			  ipw.println ("</xsd:restriction>");
		  }else if(baseDomain==td.INTERLIS.XmlDateTime){
			  ipw.println ("<xsd:restriction base=\"xsd:dateTime\">");
			  ipw.indent ();
			  PrecisionDecimal minv[]=ftype.valueOf(ftype.getMinimum());
			  PrecisionDecimal maxv[]=ftype.valueOf(ftype.getMaximum());
			  String min = getDateValue(minv)+"T"+getTimeValue(minv,3);
			  String max = getDateValue(maxv)+"T"+getTimeValue(maxv,3);
			  ipw.println ("<xsd:minInclusive value=\""+min+"\"/>");
			  ipw.println ("<xsd:maxInclusive value=\""+max+"\"/>");
			  ipw.unindent ();
			  ipw.println ("</xsd:restriction>");			  
		  }else if(baseDomain==td.INTERLIS.XmlTime){
			  ipw.println ("<xsd:restriction base=\"xsd:time\">");
			  ipw.indent ();
			  PrecisionDecimal minv[]=ftype.valueOf(ftype.getMinimum());
			  PrecisionDecimal maxv[]=ftype.valueOf(ftype.getMaximum());
			  String min = getTimeValue(minv,0);
			  String max = getTimeValue(maxv,0);
			  ipw.println ("<xsd:minInclusive value=\""+min+"\"/>");
			  ipw.println ("<xsd:maxInclusive value=\""+max+"\"/>");
			  ipw.unindent ();
			  ipw.println ("</xsd:restriction>");
		  }else{
			  ipw.println ("<xsd:restriction base=\"xsd:normalizedString\">");
				  ipw.indent ();
				  // TODO add regexp facet
				  ipw.unindent ();
			  ipw.println ("</xsd:restriction>");
		  }
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof BlackboxType){
		BlackboxType ftype=(BlackboxType)type;
	    ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
		    ipw.println ("<xsd:sequence>");
			    ipw.indent ();
			    if(ftype.getKind()==BlackboxType.eBINARY){
				    ipw.println ("<xsd:element name=\"BINBLBOX\">");
			    }else{
				    ipw.println ("<xsd:element name=\"XMLBLBOX\">");
			    }
				    ipw.indent ();
				    ipw.println ("<xsd:complexType>");
					    if(ftype.getKind()==BlackboxType.eBINARY){
						    ipw.indent ();
						    ipw.println ("<xsd:simpleContent>");
							    ipw.indent ();
							    ipw.println ("<xsd:extension base=\"xsd:base64Binary\"/>");
							    ipw.unindent ();
						    ipw.println ("</xsd:simpleContent>");
						    ipw.unindent ();
					    }else{
						    ipw.indent ();
						    ipw.println ("<xsd:sequence>");
							    ipw.indent ();
							    ipw.println ("<xsd:any minOccurs=\"0\" maxOccurs=\"unbounded\" processContents=\"skip\"/>");
							    ipw.unindent ();
						    ipw.println ("</xsd:sequence>");
						    ipw.unindent ();
					    }
				    ipw.println ("</xsd:complexType>");
				    ipw.unindent ();
			    ipw.println ("</xsd:element>");
			    ipw.unindent ();
		    ipw.println ("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
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



private String getDateValue(PrecisionDecimal[] minv) {
	java.text.DecimalFormat nnnn=new java.text.DecimalFormat("0000"); 
	  java.text.DecimalFormat nn=new java.text.DecimalFormat("00"); 
	  String min=nnnn.format(new java.math.BigDecimal(minv[0].toString()))
	  +"-"
	  +nn.format(new java.math.BigDecimal(minv[1].toString()))
	  +"-"
	  +nn.format(new java.math.BigDecimal(minv[2].toString()))
	  ;
	return min;
}
private String getTimeValue(PrecisionDecimal[] minv,int offset) {
	java.text.DecimalFormat ss=new java.text.DecimalFormat("00.000"); 
	  java.text.DecimalFormat nn=new java.text.DecimalFormat("00"); 
	  String min=nn.format(new java.math.BigDecimal(minv[0+offset].toString()))
	  +":"
	  +nn.format(new java.math.BigDecimal(minv[1+offset].toString()))
	  +":"
	  +ss.format(new java.math.BigDecimal(minv[2+offset].toString()))
	  ;
	return min;
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
						      ipw.println ("<xsd:element name=\""+getTransferName(part)+"\" type=\""+getStructureRef(part)+"\"/>");
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
						ipw.println ("<xsd:element name=\"COORD\" type=\"CoordValue\"/>"); refCoordValue=true;
                                                Set lfv=new HashSet(Arrays.asList(domain.getLineForms()));
                                                if(lfv.contains(td.INTERLIS.ARCS)){
						  ipw.println ("<xsd:element name=\"ARC\" type=\"ArcPoint\"/>");
						  						refArcPoint=true;
                                                }
                                                Iterator it=lfv.iterator();
                                                while(it.hasNext()){
                                                  LineForm lf=(LineForm)it.next();
                                                  if(lf==td.INTERLIS.ARCS || lf==td.INTERLIS.STRAIGHTS){
                                                    continue;
                                                  }
                                                Table segmentStruct=lf.getSegmentStructure();
						ipw.println ("<xsd:element name=\""+getTransferName(segmentStruct)+"\" type=\""+getStructureRef(segmentStruct)+"\"/>");
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

  public static void buildEnumList(java.util.List accu,String prefix1,ch.interlis.ili2c.metamodel.Enumeration enumer){
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
  private boolean refConsistencyType=false;
  private boolean refIncrementalTypes=false;
  private boolean refCoordValue=false;
  private boolean refArcPoint=false;
  private boolean refOidSpaces=false;
  private HashSet referencedTypes=new HashSet();
  private String getDomainRef(Domain ele)
  {
	  referencedTypes.add(ele);
	  return getTransferName(ele);
  }
  private String getStructureRef(Viewable ele)
  {
	  referencedTypes.add(ele);
	  return getTransferName(ele);
  }
}
