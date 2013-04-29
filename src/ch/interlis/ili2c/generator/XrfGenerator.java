package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.*;
import ch.interlis.iom_j.itf.ModelUtilities;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.io.IndentPrintWriter;

/** writes a IRF schema
 * 
 * @author ce
 */
public final class XrfGenerator
{
    public static final String XRF_XMLNSBASE="http://www.interlis.ch/irf/1.0";
    public static final String GEOM_XMLNSBASE="http://www.interlis.ch/geometry/1.0";
    public static final String TRANSFER="TRANSFER";
    public static final String ORDER_POS="ORDER_POS";
    public static final String REF="REF";
    
  IndentPrintWriter   ipw;
  TransferDescription td;
  java.io.File outdir;
  int                 numErrors = 0;

  public static void generate(TransferDescription td,java.io.File outfolder) throws NotSupportedByIliRelational
  {
		XrfGenerator gen=new XrfGenerator();
		gen.outdir=outfolder;
		gen.td=td;
		gen.setupNameMapping();
		gen.printXSD(td);
  }
  public static void main(String args[])
  {
		Configuration config=new Configuration();
		java.io.File out=null;
		for(int i=0;i<args.length;i++){
			if(args[i].equals("--trace")){
				EhiLogger.getInstance().setTraceFilter(false);
			}else if(args[i].equals("--out")){
				i++;
				out=new java.io.File(args[i]);
			}else if(args[i].startsWith("-")){
				EhiLogger.logError("unkonwn option "+args[i]);
				break;
			}else{
				config.addFileEntry(new FileEntry(args[i],FileEntryKind.ILIMODELFILE));				
			}
		}
		if(out==null){
			EhiLogger.logError("ili2xsd --out folder file.ili");
			return;
		}
		config.setOutputKind(ch.interlis.ili2c.config.GenerateOutputKind.NOOUTPUT);
		config.setGenerateWarnings(false);
		TransferDescription td=ch.interlis.ili2c.Main.runCompiler(config);
		try {
			generate(td,out);
		} catch (NotSupportedByIliRelational e) {
			EhiLogger.logError("failed to generate xsd",e);
		}
  }

  /** Returns the name used in the transfer for a given INTERLIS Element.


      @exception java.lang.IllegalArgumentException if elt is not an instance of
                 Model, Topic, Viewable or Attribute.
  */
  private static String getTransferName (ch.interlis.ili2c.metamodel.Element elt)
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
  private Model currentModel=null;
  private void printXSD (TransferDescription td) throws NotSupportedByIliRelational
  {
		Iterator modeli = td.iterator();
		while (modeli.hasNext()) {
			Object mObj = modeli.next();
			if ((mObj instanceof Model)
				&& !(mObj instanceof PredefinedModel)) {
					Model model = (Model) mObj;
					printModel(model);
			}
		}
	  
  }

  HashMap def2name=null; // map<Element def,String name>
  private void setupNameMapping()
  {
	  def2name=createDef2NameMapping(td);
  }
  /** create mapping from qualified ili name to name.
   * @return map<String iliQualifiedName,String name>
   */
  public static HashMap createName2NameMapping(TransferDescription td)
  {
	  HashMap def2name=createDef2NameMapping(td);
	  HashMap ret=new HashMap();
	  Iterator defi=def2name.keySet().iterator();
	  while(defi.hasNext()){
		  Element def=(Element)defi.next();
		  if(def instanceof AttributeDef){
			  ret.put(def.getContainer().getScopedName(null)+"."+def.getName(), def2name.get(def));
		  }else{
			  ret.put(def.getScopedName(null), def2name.get(def));
		  }
	  }
	  return ret;
  }
  /** create mapping from definition to name.
   * @return map<Element def,String name>
   */
  public static HashMap createDef2NameMapping(TransferDescription td)
  {

	  HashMap def2name=new HashMap(); // map<Element def,String name>
		Iterator modeli = td.iterator();
		while (modeli.hasNext()) {
			Object modelo = modeli.next();
			if (modelo instanceof Model) {
					Model model = (Model) modelo;
					HashMap name2def=new HashMap();
					Iterator topici = model.iterator();
					while (topici.hasNext()) {
						Element topic = (Element)topici.next();
					    String topicName=topic.getName();
				    	name2def.put(topicName, topic);
				    	def2name.put(topic, topicName);
					}
					topici = model.iterator();
					while (topici.hasNext()) {
						Object topico = topici.next();
						if (topico instanceof Topic) {
							Topic topic = (Topic) topico;
						    String topicName=topic.getName();
							   Iterator classi = topic.iterator();
							   while (classi.hasNext())
							   {
							     Element aclass = (Element)classi.next();
							     String className=aclass.getName();
							     if(name2def.containsKey(className)){
							    	 	String scopedName=topicName+"."+className;
								    	name2def.put(scopedName, aclass);							    	 
								    	def2name.put(aclass, scopedName);
							     }else{
								    	name2def.put(className, aclass);
								    	def2name.put(aclass, className);
							     }
							     if(aclass instanceof AbstractClassDef){
							    	 AbstractClassDef ili2cClass=(AbstractClassDef)aclass;
								     Iterator attri=ili2cClass.getDefinedAttributes();
								     while(attri.hasNext()){
								    	 AttributeDef attr=(AttributeDef)attri.next();
								    	 if(attr.getExtending()==null){
								    		 if(attr.getDomainResolvingAliases() instanceof AreaType){
								    			 String helperTableName=def2name.get(aclass)+"_"+attr.getName();
										    	name2def.put(helperTableName, attr);
										    	def2name.put(attr, helperTableName);
								    		 }
								    	 }
								     }
							    	 
							     }
							   }
						}
					}
			}
		}
		return def2name;
  }
  private String getScopedName(Element elt)
  {
	  if(!def2name.containsKey(elt)){
		  throw new IllegalArgumentException("unexpected model element: "+elt.getScopedName(null));
	  }
	  Model eleModel=(Model)elt.getContainer(Model.class);
	  String eleName=(String)def2name.get(elt);
	  if(eleModel==currentModel){
		  return eleName;
	  }
	  return eleModel.getName()+":"+eleName;
  }
  private String getName(Element elt)
  {
	  if(!def2name.containsKey(elt)){
		  throw new IllegalArgumentException("unexpected model element: "+elt.getScopedName(null));
	  }
	  String eleName=(String)def2name.get(elt);
	  return eleName;
  }
  private void printModel(Model model) throws NotSupportedByIliRelational
  {
	// setup output
	java.io.File filename=new java.io.File(outdir,model.getName()+".xsd");
	try{
		ipw=new IndentPrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8")));
	}catch(java.io.IOException ex){
		EhiLogger.logError(ex);
		return;
	}
	
	// init globals
	currentModel=model;
	surfaceOrAreaAttrs=new ArrayList();
	
	// start output
	ipw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
	ipw.indent();
	ipw.println("xmlns=\""+XRF_XMLNSBASE+"/"+model.getName()+"\"");
	ipw.println("targetNamespace=\""+XRF_XMLNSBASE+"/"+model.getName()+"\"");
		// xmlns declartion of geometry schema
		ipw.println("xmlns:"+getGeomXmlnsNc()+"=\""+GEOM_XMLNSBASE+"\"");
		// xmlns declaration of base ITX schema
		ipw.println("xmlns:"+getIliXmlnsNc()+"=\""+XRF_XMLNSBASE+"/INTERLIS\"");
		ipw.println("xmlns:ili2c=\"http://www.interlis.ch/ili2c\"");
		// xmlns declartion of imported ili-models
		Model importedModels[]=model.getImporting();
		for(int modeli=0;modeli<importedModels.length;modeli++){
			if(importedModels[modeli]!=td.INTERLIS){
				ipw.println("xmlns:"+importedModels[modeli].getName()+"=\""+XRF_XMLNSBASE+"/"+importedModels[modeli].getName()+"\"");
			}
		}
	ipw.println("elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">");
	ipw.unindent();

    // compiler info
	ipw.println ("<xsd:annotation>");
	ipw.indent ();
	ipw.print("<xsd:appinfo source=\"http://www.interlis.ch/ili2c/ili2cversion\">");
	ipw.print(ch.interlis.ili2c.Main.getVersion());
	ipw.println ("</xsd:appinfo>");
	
	// ilimodel info
	ipw.println("<xsd:appinfo source=\"http://www.interlis.ch/ili2c\">");
	ipw.indent();
	ipw.println("<ili2c:model>"+model.getName()+"</ili2c:model>");
	if(model.getIliVersion().equals(Model.ILI1)){
		// ili1 model has no issuer+version
	}else{
		ipw.println("<ili2c:modelVersion>"+model.getModelVersion()+"</ili2c:modelVersion>");
		if(model.getModelVersionExpl()!=null){
			ipw.println("<ili2c:modelVersionExplanation>"+model.getModelVersionExpl()+"</ili2c:modelVersionExplanation>");
		}
		ipw.println("<ili2c:modelAt>"+model.getIssuer()+"</ili2c:modelAt>");
	}
	ipw.unindent();
	ipw.println ("</xsd:appinfo>");
	ipw.unindent();
	ipw.println ("</xsd:annotation>");

	
	// import of geometry schema
	ipw.println("<xsd:import namespace=\""+GEOM_XMLNSBASE+"\"/>");
	// import of base ILIGML schema
	ipw.println("<xsd:import namespace=\""+XRF_XMLNSBASE+"/INTERLIS\"/>");
	// import schemas of imported ili-models
	for(int modeli=0;modeli<importedModels.length;modeli++){
		if(importedModels[modeli]!=td.INTERLIS){
			ipw.println("<xsd:import namespace=\""+XRF_XMLNSBASE+"/"+importedModels[modeli].getName()+"\"/>");
		}
	}
	Iterator topici = model.iterator();
	while (topici.hasNext()) {
		Object tObj = topici.next();
		// model level domaindefs
		if (tObj instanceof Domain) {
			declareDomainDef((Domain) tObj);
		}
		// modellevel classes
		if (tObj instanceof AbstractClassDef) {
			declareAbstractClassDef((AbstractClassDef) tObj);
		}
		// modellevel lineforms
		if(tObj instanceof LineForm){
		   declareLineForm((LineForm)tObj);
		}
		if (tObj instanceof Topic) {
			Topic topic = (Topic) tObj;
			declareTopic(topic);
		}
	}
	declareLinetables();
	ipw.println ("</xsd:schema>");
	ipw.close();
  }

  private void declareTopic (Topic topic) throws NotSupportedByIliRelational
  {
		if(topic.getExtending()!=null){
			throw new NotSupportedByIliRelational("extended topics not supported ("+topic.getScopedName(null)+")");
		}

   Iterator iter = topic.iterator();
   
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof Domain) {
				declareDomainDef((Domain) obj);
			}
			if (obj instanceof AbstractClassDef) {
				declareAbstractClassDef((AbstractClassDef) obj);
			}
			// no LineForm at topic level
			// if(obj instanceof LineForm){
		}

	
   ipw.println("<xsd:element name=\""+getName(topic)+"\" type=\""+getName(topic)+"Type\"/>");
    ipw.println ("<xsd:complexType name=\""+getName(topic)+"Type\">");
	ipw.indent ();
	ipw.println ("<xsd:sequence>");
	ipw.indent ();
	if(true){
		iter=ModelUtilities.getItfTables(td, topic).iterator();
		while(iter.hasNext()){
			 Object obj = iter.next();
		      if ((obj instanceof Viewable) && !AbstractPatternDef.suppressViewableInTransfer((Viewable)obj)){
				 Viewable v = (Viewable) obj;
				 ipw.println("<xsd:element ref=\"" + getScopedName(v) + "\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
			 }else if(obj instanceof AttributeDef && ((AttributeDef)obj).getDomainResolvingAliases() instanceof AreaType){
				 AttributeDef attr=(AttributeDef)obj;
				  AbstractClassDef aclass=(AbstractClassDef)(attr).getContainer();
				  String linetableName=getName(aclass)+"_"+getTransferName(attr);
				  ipw.println("<xsd:element ref=\"" + linetableName + "\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
			 }
		}
	}else{
		 iter = topic.getViewables().iterator();
		 while (iter.hasNext()) {
			 Object obj = iter.next();
		      if ((obj instanceof Viewable) && !AbstractPatternDef.suppressViewableInTransfer((Viewable)obj)){
				 Viewable v = (Viewable) obj;
				 ipw.println("<xsd:element ref=\"" + getScopedName(v) + "\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
			 }
		 }
		 // add line tables
		  Iterator attri=surfaceOrAreaAttrs.iterator();
		  while(attri.hasNext()){
			  AttributeDef attr=(AttributeDef)attri.next();
			  AbstractClassDef aclass=(AbstractClassDef)attr.getContainer();
			  if(aclass.getContainer()==topic){
				  String linetableName=getName(aclass)+"_"+getTransferName(attr);
				  ipw.println("<xsd:element ref=\"" + linetableName + "\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
			  }
		  }
		
	}

	ipw.unindent ();
	ipw.println ("</xsd:sequence>");
	ipw.println ("<xsd:attribute name=\"BID\" type=\""+getIliXmlns()+"BID\"/>");
	ipw.unindent ();
	ipw.println ("</xsd:complexType>");

  }



  private void declareAbstractClassDef(Viewable v) throws NotSupportedByIliRelational
  {
	  if(v instanceof AssociationDef){ 
		  AssociationDef assoc=(AssociationDef)v;
		  if(assoc.getDerivedFrom()!=null){
			  // derived; skip it
			  return;
		  }
		  if(v.isFinal() && !v.getAttributes().hasNext() && assoc.isLightweight()){
			  // final lightweight without attributes
			  // skip it
			  return;
		  }
	  }
		if(v.getExtending()!=null){
			throw new NotSupportedByIliRelational("extended viewable not supported ("+v.getScopedName(null)+")");
		}
	ipw.println("<xsd:element name=\""+getName(v)+"\" type=\""+getName(v)+"Type\"/>");
	ipw.println("<xsd:complexType  name=\"" + getName(v) + "Type\">");
	ipw.indent ();
	ipw.println("<xsd:sequence>");
	ipw.indent();
	/* Find which attributes are going to be elements of this class. */
	Iterator iter=null;
	if(td.getIli1Format()!=null){
		iter=ModelUtilities.getIli1AttrList((AbstractClassDef)v).iterator();
	}else{
		iter = v.getAttributesAndRoles2();
	}
	while (iter.hasNext()) {
		ViewableTransferElement obj = (ViewableTransferElement)iter.next();
		if (obj.obj instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) obj.obj;
			if(attr.getExtending()!=null){
				throw new NotSupportedByIliRelational("extended attributes not supported ("+v.getScopedName(null)+"."+attr.getName()+")");
			}
			if(attr.getExtending()==null && attr.getContainer()==v && !attr.isTransient()){
				// define only new attrs (==not EXTENDED)
				declareAttribute(attr);
			}
		}
		if(obj.obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj.obj;
			if(role.getExtending()!=null){
				throw new NotSupportedByIliRelational("extended roles not supported ("+v.getScopedName(null)+"."+role.getName()+")");
			}
			
			// not an embedded role and roledef not defined in a lightweight association?
			if (!obj.embedded && !((AssociationDef)v).isLightweight() && v.getExtending()==null){
				if(role.getExtending()==null){
					ipw.println("<xsd:element name=\""+ getTransferName(role)+ ">");
					ipw.indent();
					ipw.println("<xsd:complexType>");
					ipw.indent();
					//ipw.println("<xsd:sequence/>");
					ipw.println("<xsd:attribute name=\""+REF+"\" type=\""+getIliXmlns()+REF+"\"/>");					
					if(role.isOrdered()){
						ipw.println("<xsd:attribute name=\""+ORDER_POS+"\" type=\""+getIliXmlns()+ORDER_POS+"\"/>");					
					}
					ipw.unindent();
					ipw.println("</xsd:complexType>");
					//ipw.println("<!-- "+PRBLMTAG+" unable to express aggregation kind -->");
					//ipw.println("<!-- "+PRBLMTAG+" unable to express cardinality -->");
					ipw.unindent();
					ipw.println("</xsd:element>");
				}
			}
			// a role of an embedded association?
			if(obj.embedded){
				AssociationDef roleOwner = (AssociationDef) role.getContainer();
				if(roleOwner.getDerivedFrom()==null){
					// role is oppend;
					RoleDef oppend=role.getOppEnd();
					if(oppend.getExtending()==null && oppend.getDestination()==v){
						Cardinality card = role.getCardinality();
						String minOccurs = "";
						if (card.getMinimum() == 0) {
							minOccurs = " minOccurs=\"0\"";
						}
						ipw.println("<xsd:element name=\""+ getTransferName(role)+"\""+ minOccurs+">");
						ipw.indent();
						ipw.println("<xsd:complexType>");
						ipw.indent();
						if(roleOwner.getAttributes().hasNext()){
							ipw.println("<xsd:sequence>");
							ipw.indent();
							ipw.println("<xsd:element ref=\""+ getScopedName(roleOwner)+ "\"/>");
							ipw.unindent();
							ipw.println("</xsd:sequence>");
						}
						ipw.println("<xsd:attribute name=\""+REF+"\" type=\""+getIliXmlns()+REF+"\"/>");					
						if(oppend.isOrdered()){
							ipw.println("<xsd:attribute name=\""+ORDER_POS+"\" type=\""+getIliXmlns()+ORDER_POS+"\"/>");					
						}
						ipw.unindent();
						ipw.println("</xsd:complexType>");
						//ipw.println("<!-- "+PRBLMTAG+" unable to express aggregation kind -->");
						//ipw.println("<!-- "+PRBLMTAG+" unable to express cardinality -->");
						ipw.unindent();
						ipw.println("</xsd:element>");
					}
				}
			}
		}
	}
	ipw.unindent();
	ipw.println("</xsd:sequence>");
	ipw.println ("<xsd:attribute name=\"TID\" type=\""+getIliXmlns()+"TID\"/>");
	ipw.unindent();
	ipw.println("</xsd:complexType>");

  }

  private void declareDomainDef (Domain domain) throws NotSupportedByIliRelational
  {
	  if(domain.getExtending()!=null){
			throw new NotSupportedByIliRelational("extended domains not supported ("+domain.getScopedName(null)+")");
	  }
  	Type type=domain.getType();
	//if (type instanceof CoordType){
	//}else if (type instanceof PolylineType){
	//}  	
    if(type instanceof TypeAlias){
    	Domain realDomain=((TypeAlias)type).getAliasing();
    	Type realType=realDomain.getType();
    	/*
    	URI (FINAL) = TEXT*1023;
    	NAME (FINAL) = TEXT*255;
    	INTERLIS_1_DATE (FINAL) = TEXT*8;
    	BOOLEAN (FINAL) = (
    	HALIGNMENT (FINAL) = (
    	VALIGNMENT (FINAL) = (
    	ANYOID = OID ANY;
    	I32OID = OID 0 .. 2147483647;
    	STANDARDOID = OID TEXT*16;
    	UUIDOID = OID TEXT*36;
    	LineCoord (ABSTRACT) = COORD NUMERIC, NUMERIC;
    	GregorianYear = 1582 .. 2999
    	XMLTime = FORMAT BASED ON UTC ( Hours/2 ":" Minutes ":" Seconds );
    	XMLDate = FORMAT BASED ON GregorianDate ( Year "-" Month "-" Day );
    	XMLDateTime EXTENDS XMLDate = FORMAT BASED ON GregorianDateTime
    	*/
    	// only the following can apprea in a DomainDef
    	// URI, NAME, INTERLIS_1_DATE, BOOLEAN, HALIGNMENT, VALIGNMENT
    	String base=null;
    	String facets=null;
    	if(realDomain==td.INTERLIS.URI){
    		base="xsd:anyURI";
    		facets="<xsd:maxLength value=\"1023\"/>";
    	}else if(realDomain==td.INTERLIS.NAME){
    		base="xsd:token";
    		facets="<xsd:maxLength value=\"255\"/><xsd:pattern value=\"[a-zA-Z][a-zA-Z0-9_]*\"/>";
    	}else if(realDomain==td.INTERLIS.INTERLIS_1_DATE){
    		base="xsd:token";
    		facets="<xsd:maxLength value=\"8\"/><xsd:pattern value=\"[0-9]*\"/>";
    	}else if(realDomain==td.INTERLIS.BOOLEAN){
    		base="xsd:boolean";
    	}else if(realDomain==td.INTERLIS.HALIGNMENT){
    		base=getIliXmlns()+"HALIGNMENT";
    	}else if(realDomain==td.INTERLIS.VALIGNMENT){
    		base=getIliXmlns()+"VALIGNMENT";
        }else{
        	throw new IllegalArgumentException(realDomain.getScopedName(null)+": type "+type.getClass()+" not yet supported");
        }
    	  ipw.println("<xsd:simpleType name=\""+getName(domain)+"\">");
      	  ipw.indent();
      	  if(facets==null){
          	  ipw.println("<xsd:restriction base=\""+base+"\"/>");
      	  }else{
          	  ipw.println("<xsd:restriction base=\""+base+"\">");
          	  ipw.indent();
          	  ipw.println(facets);
          	  ipw.unindent();
          	  ipw.println("</xsd:restriction>");
      	  }
      	  ipw.unindent();
      	  ipw.println("</xsd:simpleType>");
    }else if (type instanceof AreaType){
			ipw.println("<xsd:complexType name=\"" + getName(domain) + "\">");
			ipw.indent();
			ipw.println("<xsd:sequence>");
			ipw.indent();
			ipw.println("<xsd:element ref=\"geom:point\"/>");
			ipw.unindent();
			ipw.println("</xsd:sequence>");
			ipw.unindent();
			ipw.println("</xsd:complexType>");
    }else{
        declareType(type,domain);
    }
  }
  private ArrayList surfaceOrAreaAttrs=null; // array<AttributeDef attr>
  private void declareLinetables() throws NotSupportedByIliRelational{
	  Iterator attri=surfaceOrAreaAttrs.iterator();
	  while(attri.hasNext()){
		  AttributeDef attr=(AttributeDef)attri.next();
		  AbstractClassDef aclass=(AbstractClassDef)attr.getContainer();
		  String linetableName=getName(aclass)+"_"+getTransferName(attr);
			ipw.println("<xsd:element name=\""+linetableName+"\" type=\""+linetableName+"Type\"/>");
			ipw.println("<xsd:complexType  name=\"" + linetableName + "Type\">");
			ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent();

			SurfaceOrAreaType type=(SurfaceOrAreaType)attr.getDomainResolvingAliases();
			Table lineattrs=type.getLineAttributeStructure();
			if(lineattrs!=null){
				Iterator iter=lineattrs.getAttributesAndRoles2();
				while (iter.hasNext()) {
					ViewableTransferElement obj = (ViewableTransferElement)iter.next();
					if (obj.obj instanceof AttributeDef) {
						AttributeDef lattr = (AttributeDef) obj.obj;
						if(lattr.getExtending()!=null){
							throw new NotSupportedByIliRelational("extended attributes not supported ("+lineattrs.getScopedName(null)+"."+lattr.getName()+")");
						}
						if(!lattr.isTransient()){
							// define only new attrs (==not EXTENDED)
							declareAttribute(lattr);
						}
					}
					if(obj.obj instanceof RoleDef){
						throw new IllegalStateException("unexpected RoleDef");
					}
				}
				
			}
			
			ipw.println("<xsd:element name=\"_geometry\">");
		    ipw.indent ();
			ipw.println ("<xsd:complexType>");
		    ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent ();
			ipw.println("<xsd:element ref=\"geom:polyline\"/>");
			ipw.unindent ();
			ipw.println("</xsd:sequence>");
		    ipw.unindent ();
		    ipw.println ("</xsd:complexType>");
		    ipw.unindent ();
			ipw.println("</xsd:element>");
						
			ipw.unindent();
			ipw.println("</xsd:sequence>");
			ipw.println ("<xsd:attribute name=\"TID\" type=\""+getIliXmlns()+"TID\"/>");
			ipw.unindent();
			ipw.println("</xsd:complexType>");
		  
	  }
  }
  private void declareAttribute (AttributeDef attribute)
  {
        String minOccurs="";
        if(!attribute.getDomain().isMandatoryConsideringAliases()){
       	  minOccurs=" minOccurs=\"0\"";
        }
    Type type= attribute.getDomain();
    if(type instanceof TypeAlias){
    	Domain realDomain=((TypeAlias)type).getAliasing();
    	
    	String base=null;
    	String facets=null;
    	if(realDomain==td.INTERLIS.URI){
    		base="xsd:anyURI";
    		facets="<xsd:maxLength value=\"1023\"/>";
    	}else if(realDomain==td.INTERLIS.NAME){
    		base="xsd:token";
    		facets="<xsd:maxLength value=\"255\"/><xsd:pattern value=\"[a-zA-Z][a-zA-Z0-9_]*\"/>";
    	}else if(realDomain==td.INTERLIS.INTERLIS_1_DATE){
    		base="xsd:token";
    		facets="<xsd:maxLength value=\"8\"/><xsd:pattern value=\"[0-9]*\"/>";
    	}else if(realDomain==td.INTERLIS.BOOLEAN){
    		base="xsd:boolean";
    	}else if(realDomain==td.INTERLIS.HALIGNMENT){
    		base=getIliXmlns()+"HALIGNMENT";
    	}else if(realDomain==td.INTERLIS.VALIGNMENT){
    		base=getIliXmlns()+"VALIGNMENT";
    	}else if(realDomain==td.INTERLIS.ANYOID){
    		base="xsd:token";
    	}else if(realDomain==td.INTERLIS.I32OID){
    		base=getIliXmlns()+"I32OID";
    	}else if(realDomain==td.INTERLIS.STANDARDOID){
    		base=getIliXmlns()+"STANDARDOID";
    	}else if(realDomain==td.INTERLIS.UUIDOID){
    		base=getIliXmlns()+"UUIDOID";
    	//}else if(realDomain==td.INTERLIS.LineCoord){
        	/*
        	LineCoord (ABSTRACT) = COORD NUMERIC, NUMERIC;
        	*/
    	//	base="INTERLIS:VALIGNMENT";
    	}else if(realDomain==td.INTERLIS.GregorianYear){
    		base="xsd:gYear";
    	}else if(realDomain==td.INTERLIS.XmlTime){
    		base="xsd:time";
    	}else if(realDomain==td.INTERLIS.XmlDate){
    		base="xsd:date";
    	}else if(realDomain==td.INTERLIS.XmlDateTime){
    		base="xsd:dateTime";
        }else{
        	base=getScopedName(realDomain);
        	if(realDomain.getType() instanceof AreaType){
    			// remember to create linetable
        		surfaceOrAreaAttrs.add(attribute);
        	}
        }
    	if(facets==null){
        	  ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\""+base+"\""+minOccurs+"/>");
    	}else{
      	  ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\""+minOccurs+">");
       	  ipw.indent();
      	  ipw.println("<xsd:simpleType>");
      	  ipw.indent();
      	  ipw.println("<xsd:restriction base=\""+base+"\">");
      	  ipw.indent();
      	  ipw.println(facets);
      	  ipw.unindent();
      	  ipw.println("</xsd:restriction>");
      	  ipw.unindent();
      	  ipw.println("</xsd:simpleType>");
      	  ipw.unindent();
      	  ipw.println("</xsd:element>");
    	}
    }else{
		if (type instanceof AreaType){
			ipw.println(
				"<xsd:element name=\""
					+ getTransferName(attribute)
					+ "\""
					+ minOccurs
					+ ">");
			ipw.indent();
			declareType(((AreaType)type).getControlPointDomain().getType(),null);
			ipw.unindent();
			ipw.println("</xsd:element>");
			// remember to create linetable
       		surfaceOrAreaAttrs.add(attribute);
		}else if (type instanceof CompositionType){
			CompositionType composition=(CompositionType)type;
			Table part=composition.getComponentType();
			Cardinality card=composition.getCardinality();
			minOccurs="";
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
			ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\""+minOccurs+maxOccurs+">");
			ipw.indent ();
			ipw.println ("<xsd:complexType>");
				ipw.indent ();
				ipw.println ("<xsd:sequence>");
					ipw.indent ();
					ipw.println ("<xsd:element ref=\""+getScopedName(part)+"\"/>");
					ipw.unindent ();
				ipw.println ("</xsd:sequence>");
				ipw.unindent ();
			ipw.println ("</xsd:complexType>");
			ipw.unindent ();
			ipw.println ("</xsd:element>");
		}else if (type instanceof ReferenceType){
				ipw.println("<xsd:element name=\"" + getTransferName(attribute)
						+ minOccurs + ">");
				ipw.indent();
					ipw.println("<xsd:complexType>");
					ipw.indent();
					ipw.println("<xsd:attribute name=\""+REF+"\" type=\""+getIliXmlns()+REF+"\"/>");					
					ipw.unindent();
					ipw.println("</xsd:complexType>");
				ipw.unindent();
				ipw.println("</xsd:element>");
		}else{
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
  }
  private void declareType (Type type,Domain domain)
  {
    String typeName="";
    if(domain!=null){
      typeName=" name=\""+getName(domain)+"\"";
    }
    if (type instanceof PolylineType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent ();
			ipw.println("<xsd:element ref=\"geom:polyline\"/>");
			ipw.unindent ();
			ipw.println("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof AreaType){
    	throw new IllegalArgumentException("AreaType unexpected");
    }else if (type instanceof SurfaceType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent ();
			ipw.println("<xsd:element ref=\"geom:surface\"/>");
			ipw.unindent ();
			ipw.println("</xsd:sequence>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof CoordType){
		ipw.println ("<xsd:complexType"+typeName+">");
			ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent ();
			ipw.println("<xsd:element ref=\"geom:point\"/>");
			ipw.unindent ();
			ipw.println("</xsd:sequence>");
			ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if(type instanceof EnumerationType){
	      ipw.println ("<xsd:simpleType"+typeName+">");
	        ipw.indent ();
	        ipw.println ("<xsd:restriction base=\"xsd:normalizedString\">");
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
				if(min.getExponent()!=0){
					ipw.println ("<xsd:restriction base=\"xsd:double\">");
				}else{
					ipw.println ("<xsd:restriction base=\"xsd:decimal\">");
				}
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
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
			Domain baseDomain=((FormattedType)type).getDefinedBaseDomain();
			if(baseDomain==td.INTERLIS.XmlDate){
				base="xsd:date";
			}else if(baseDomain==td.INTERLIS.XmlDateTime){
				base="xsd:dateTime";
			}else if(baseDomain==td.INTERLIS.XmlTime){
				base="xsd:time";
			}else{
				baseDomain=null;
				base="xsd:normalizedString";
			}
		  ipw.println("<xsd:restriction base=\""+base+"\">");
		  if(baseDomain==null){
			  ipw.println("<xsd:pattern value=\""+((FormattedType)type).getRegExp()+"\"/>");
		  }
		  ipw.println ("</xsd:restriction>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof BlackboxType){
		if(((BlackboxType)type).getKind()==BlackboxType.eXML){
			ipw.println ("<xsd:complexType "+typeName+">");
			ipw.indent();
			ipw.println ("<xsd:sequence>");
			ipw.indent();
			ipw.println ("<xsd:any namespace=\"##any\" minOccurs=\"0\" maxOccurs=\"unbounded\" processContents=\"lax\"/>");
			ipw.unindent();
			ipw.println ("</xsd:sequence>");
			ipw.unindent();
			ipw.println ("</xsd:complexType>");
		}else{
			ipw.println ("<xsd:simpleType "+typeName+">");
			ipw.indent();
			ipw.println ("<xsd:restriction base=\"xsd:base64Binary\">");
			ipw.println ("</xsd:restriction>");
			ipw.unindent();
			ipw.println ("</xsd:simpleType>");
		}
	}else if(type instanceof ClassType){
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
				base="xsd:normalizedString";
		  ipw.println("<xsd:restriction base=\""+base+"\"/>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof AttributePathType){
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
				base="xsd:normalizedString";
		  ipw.println("<xsd:restriction base=\""+base+"\"/>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof NumericOIDType){
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
				base="xsd:int";
		  ipw.println("<xsd:restriction base=\""+base+"\"/>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof TextOIDType){
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
				base="xsd:token";
		  ipw.println("<xsd:restriction base=\""+base+"\"/>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
	}else if(type instanceof StructuredUnitType){ // available only in ili2.2 models
		ipw.println ("<xsd:simpleType"+typeName+">");
		  ipw.indent ();
			String base;
			base="xsd:token";
		  ipw.println("<xsd:restriction base=\""+base+"\"/>");
		  ipw.unindent ();
		ipw.println ("</xsd:simpleType>");
    }else{
    	throw new IllegalArgumentException("type "+type.getClass()+" not yet supported");
	}
  }

  private void declareLineForm(LineForm form)
  {
	  EhiLogger.logAdaption("User defined line form "+form.getScopedName(null)+" not yet supported");
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
  private String getIliXmlns()
  {
	  return getIliXmlnsNc()+":";
  }
  private String getGeomXmlns()
  {
	  return getGeomXmlnsNc()+":";
  }
  private String getIliXmlnsNc()
  {
	  return "ili";
  }
  private String getGeomXmlnsNc()
  {
	  return "geom";
  }
}
