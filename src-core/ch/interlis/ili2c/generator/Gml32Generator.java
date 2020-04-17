package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.io.IndentPrintWriter;

/** writes a GML 3.2-application schema
 * 
 * @author ce
 */
public final class Gml32Generator
{
    public static final String ILIGML_XMLNSBASE="http://www.interlis.ch/INTERLIS2.3/GML32";
    public static final String BASKETMEMBER="member";
    public static final String TRANSFER="TRANSFER";
    public static final String TRANSFERMEMBER="baskets";
    public static final String ORDER_POS="ORDER_POS";
    public static final String LINK_DATA="LINK_DATA";
    
  IndentPrintWriter   ipw;
  TransferDescription td;
  String outdir;
  int                 numErrors = 0;
	//private static String PRBLMTAG="preGML32:";

  static ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());


  private Gml32Generator(TransferDescription td, String outdir)
  {
    this.td = td;
    this.outdir = outdir;
    setupNameMapping();
  }

  public static int generate (TransferDescription td,String outdir)
  {
    Gml32Generator d = new Gml32Generator (td,outdir);
   // d.findItemsToDeclare (td);
    d.printXSD (td);
    return d.numErrors;
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
  private void printXSD (TransferDescription td)
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
  /** create mapping from qualified ili name to def.
   * @return map<String iliQualifiedName,Viewable|Attribute def>
   */
  public static HashMap createName2DefMapping(TransferDescription td)
  {
	  HashMap def2name=createDef2NameMapping(td);
	  HashMap ret=new HashMap();
	  Iterator defi=def2name.keySet().iterator();
	  while(defi.hasNext()){
		  Element def=(Element)defi.next();
		  if(def instanceof AttributeDef){
			  ret.put(def.getContainer().getScopedName(null)+"."+def.getName(), def);
		  }else{
			  ret.put(def.getScopedName(null), def);
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
							     {
								     String className=aclass.getName();
								     if(name2def.containsKey(className)){
								    	 	String scopedName=topicName+"."+className;
									    	name2def.put(scopedName, aclass);							    	 
									    	def2name.put(aclass, scopedName);
								     }else{
									    	name2def.put(className, aclass);
									    	def2name.put(aclass, className);
								     }
							     }
							     if(aclass instanceof Viewable){
							    	 	Viewable v=(Viewable) aclass;
							    		Iterator iter = v.getAttributesAndRoles2();
							    		while (iter.hasNext()) {
							    			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
						    				AttributeDef attrib = null;
							    			if (obj.obj instanceof AttributeDef) {
							    				AttributeDef attr = (AttributeDef) obj.obj;
							    				if(attr.getExtending()==null && attr.getContainer()==v && !attr.isTransient()){
							    					// define only new attrs (==not EXTENDED)
							    				    Type type= attr.getDomain();
							    				    if(type instanceof TypeAlias){
							    				    	Domain realDomain=((TypeAlias)type).getAliasing();
							    			        	if(realDomain.getType() instanceof AreaType){
							    			    			// remember to create linetable
							    			        		attrib=attr;
							    			        	}
							    				    }else if (type instanceof AreaType){
						    			    			// remember to create linetable
						    			        		attrib=attr;
							    				    }
							    				}
							    			}
							    			if(attrib!=null){
											     String className=v.getName()+"."+attrib.getName();
											     if(name2def.containsKey(className)){
											    	 	String scopedName=topicName+"."+className;
												    	name2def.put(scopedName, attrib);							    	 
												    	def2name.put(attrib, scopedName);
											     }else{
												    	name2def.put(className, attrib);
												    	def2name.put(attrib, className);
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
  private void printModel(Model model)
  {
	// setup output
	String filename=outdir+"/"+model.getName()+".xsd";
	try{
		ipw=new IndentPrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF-8")));
	}catch(java.io.IOException ex){
		EhiLogger.logError(ex);
		return;
	}
	
	// init globals
	currentModel=model;
	codelists=new ArrayList();
	areaAttrs=new ArrayList();
	
	// start output
	ipw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
	ipw.indent();
	ipw.println("xmlns=\""+ILIGML_XMLNSBASE+"/"+model.getName()+"\""
	  +" targetNamespace=\""+ILIGML_XMLNSBASE+"/"+model.getName()+"\""
	  +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\"");
		// xmlns declartion of GML
		ipw.println("xmlns:gml=\"http://www.opengis.net/gml/3.2\"");
		// xmlns declaration of base ILIGML schema
		ipw.println("xmlns:INTERLIS=\""+ILIGML_XMLNSBASE+"/INTERLIS\"");
		ipw.println("xmlns:ili2c=\"http://www.interlis.ch/ili2c\"");
		// xmlns declartion of imported ili-models
		Model importedModels[]=model.getImporting();
		for(int modeli=0;modeli<importedModels.length;modeli++){
			if(importedModels[modeli]!=td.INTERLIS){
				ipw.println("xmlns:"+importedModels[modeli].getName()+"=\""+ILIGML_XMLNSBASE+"/"+importedModels[modeli].getName()+"\"");
			}
		}
	ipw.println(">");
	ipw.unindent();

    // compiler info
	ipw.println ("<xsd:annotation>");
	ipw.indent ();
	ipw.print("<xsd:appinfo source=\"http://www.interlis.ch/ili2c/ili2cversion\">");
	ipw.print(TransferDescription.getVersion());
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

	
	// import of GML schema
	ipw.println("<xsd:import namespace=\"http://www.opengis.net/gml/3.2\"/>");
	// import of base ILIGML schema
	ipw.println("<xsd:import namespace=\""+ILIGML_XMLNSBASE+"/INTERLIS\"/>");
	// import schemas of imported ili-models
	for(int modeli=0;modeli<importedModels.length;modeli++){
		if(importedModels[modeli]!=td.INTERLIS){
			ipw.println("<xsd:import namespace=\""+ILIGML_XMLNSBASE+"/"+importedModels[modeli].getName()+"\"/>");
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
	declareCodelists();
	ipw.println ("</xsd:schema>");
	ipw.close();
  }

  private void declareTopic (Topic topic)
  {

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

	// member
	 ipw.println ("<xsd:complexType name=\""+getName(topic)+"MemberType\">");
	 ipw.indent ();
	 ipw.println ("<xsd:complexContent>");
	 ipw.indent ();
	 ipw.println ("<xsd:extension base=\"gml:AbstractFeatureMemberType\">");
	 ipw.indent ();
	ipw.println ("<xsd:sequence>");
	ipw.indent ();
	ipw.println ("<xsd:choice>");
	ipw.indent ();
	 /* Find which viewables are going to be elements of this topic. */
	 iter = topic.getViewables().iterator();
	 while (iter.hasNext()) {
		 Object obj = iter.next();
	      if ((obj instanceof Viewable) && !AbstractPatternDef.suppressViewableInTransfer((Viewable)obj)){
			 Viewable v = (Viewable) obj;
			 ipw.println("<xsd:element ref=\"" + getScopedName(v) + "\"/>");
		 }
	 }
	 // add line tables
	  Iterator attri=areaAttrs.iterator();
	  while(attri.hasNext()){
		  AttributeDef attr=(AttributeDef)attri.next();
		  AbstractClassDef aclass=(AbstractClassDef)attr.getContainer();
		  if(aclass.getContainer()==topic){
			  String linetableName=getName(aclass)+"."+getTransferName(attr);
			  ipw.println("<xsd:element ref=\"" + linetableName + "\"/>");
		  }
	  }
	ipw.unindent();
	ipw.println ("</xsd:choice>");
	ipw.unindent();
	ipw.println ("</xsd:sequence>");
	 ipw.unindent();
	 ipw.println ("</xsd:extension>");
	 ipw.unindent();
	 ipw.println("</xsd:complexContent>");
	 ipw.unindent();
	 ipw.println ("</xsd:complexType>");
	
   ipw.println("<xsd:element name=\""+getName(topic)+"\" type=\""+getName(topic)+"Type\" substitutionGroup=\"gml:AbstractFeature\"/>");
    ipw.println ("<xsd:complexType name=\""+getName(topic)+"Type\">");
	ipw.indent ();
    ipw.println ("<xsd:complexContent>");
	ipw.indent ();
    ipw.println ("<xsd:extension base=\"gml:AbstractFeatureType\">");
	ipw.indent ();
	ipw.println ("<xsd:sequence>");
	ipw.indent ();
	ipw.println ("<xsd:element name=\""+BASKETMEMBER+"\" type=\""+getName(topic)+"MemberType\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
	ipw.unindent ();
	ipw.println ("</xsd:sequence>");
	ipw.println ("<xsd:attributeGroup ref=\"gml:AggregationAttributeGroup\"/>");
	ipw.unindent ();
	ipw.println ("</xsd:extension>");
	ipw.unindent ();
	ipw.println ("</xsd:complexContent>");
	ipw.unindent ();
	ipw.println ("</xsd:complexType>");

  }



  private void declareAbstractClassDef(Viewable v)
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
  	String baseElement="gml:AbstractFeature";
  	Viewable baseType=(Viewable)v.getExtending();
  	if(baseType!=null){
  		baseElement=getScopedName(baseType);
  	}
	ipw.println("<xsd:element name=\""+getName(v)+"\" type=\""+getName(v)+"Type\" substitutionGroup=\""+baseElement+"\"/>");
	ipw.println("<xsd:complexType  name=\"" + getName(v) + "Type\">");
	ipw.indent ();
	ipw.println ("<xsd:complexContent>");
  	ipw.indent ();
  	String baseXsdType="gml:AbstractFeatureType";
	if(baseType!=null){
		baseXsdType=getScopedName(baseType)+"Type";
	}
	ipw.println ("<xsd:extension base=\""+baseXsdType+"\">");
	ipw.indent ();
	ipw.println("<xsd:sequence>");
	ipw.indent();
	/* Find which attributes are going to be elements of this class. */
	Iterator iter = v.getAttributesAndRoles2();
	while (iter.hasNext()) {
		ViewableTransferElement obj = (ViewableTransferElement)iter.next();
		if (obj.obj instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) obj.obj;
			if(attr.getExtending()==null && attr.getContainer()==v && !attr.isTransient()){
				// define only new attrs (==not EXTENDED)
				declareAttribute(attr);
			}
		}
		if(obj.obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj.obj;
			
			// not an embedded role and roledef not defined in a lightweight association?
			if (!obj.embedded && !((AssociationDef)v).isLightweight() && v.getExtending()==null){
				if(role.getExtending()==null){
					if(role.isOrdered()){
						ipw.println("<xsd:element name=\""+ getTransferName(role)+ "\">");
					}else{
						ipw.println("<xsd:element name=\""+ getTransferName(role)+ "\" type=\"gml:ReferenceType\">");
					}
					ipw.indent();
					ipw.println("<xsd:annotation>");
						ipw.indent();
						ipw.println("<xsd:appinfo>");
							ipw.indent();
							ipw.println("<gml:targetElement>"+getScopedName(role.getDestination())+"</gml:targetElement>");
							ipw.unindent();
						ipw.println("</xsd:appinfo>");
						ipw.unindent();
					ipw.println("</xsd:annotation>");
					if(role.isOrdered()){
						ipw.println("<xsd:complexType>");
						ipw.indent();
						ipw.println("<xsd:sequence/>");
						ipw.println("<xsd:attributeGroup ref=\"gml:OwnershipAttributeGroup\"/>");
						ipw.println("<xsd:attributeGroup ref=\"gml:AssociationAttributeGroup\"/>");
						ipw.println("<xsd:attribute ref=\"INTERLIS:"+ORDER_POS+"\"/>");					
						ipw.unindent();
						ipw.println("</xsd:complexType>");
					}
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
						if(oppend.isOrdered()){
							ipw.println("<xsd:element name=\""+ getTransferName(role)+"\""+ minOccurs+">");
						}else{
							ipw.println("<xsd:element name=\""+ getTransferName(role)+ "\" type=\"gml:ReferenceType\""+minOccurs+">");
						}
						ipw.indent();
						ipw.println("<xsd:annotation>");
							ipw.indent();
							ipw.println("<xsd:appinfo>");
								ipw.indent();
								ipw.println("<gml:targetElement>"+getScopedName(role.getDestination())+"</gml:targetElement>");
								ipw.unindent();
							ipw.println("</xsd:appinfo>");
							ipw.unindent();
						ipw.println("</xsd:annotation>");
						if(oppend.isOrdered()){
							ipw.println("<xsd:complexType>");
							ipw.indent();
							ipw.println("<xsd:sequence/>");
							ipw.println("<xsd:attributeGroup ref=\"gml:OwnershipAttributeGroup\"/>");
							ipw.println("<xsd:attributeGroup ref=\"gml:AssociationAttributeGroup\"/>");
							ipw.println("<xsd:attribute ref=\"INTERLIS:"+ORDER_POS+"\"/>");					
							ipw.unindent();
							ipw.println("</xsd:complexType>");
						}
						//ipw.println("<!-- "+PRBLMTAG+" unable to express aggregation kind -->");
						//ipw.println("<!-- "+PRBLMTAG+" unable to express cardinality -->");
						ipw.unindent();
						ipw.println("</xsd:element>");
						if(!roleOwner.isFinal() || roleOwner.getAttributes().hasNext()){
							ipw.println("<xsd:element name=\""+ getTransferName(role)+ "."+LINK_DATA+"\" minOccurs=\"0\">");
							ipw.indent();
							ipw.println("<xsd:complexType>");
							ipw.indent();
							ipw.println("<xsd:sequence>");
							ipw.indent();
							ipw.println("<xsd:element ref=\""+ getScopedName(roleOwner)+ "\"/>");
							ipw.unindent();
							ipw.println("</xsd:sequence>");
							ipw.unindent();
							ipw.println("</xsd:complexType>");
							ipw.unindent();
							ipw.println("</xsd:element>");
						}
					}
				}
			}
		}
	}
	ipw.unindent();
	ipw.println("</xsd:sequence>");
	ipw.unindent();
	ipw.println("</xsd:extension>");
	ipw.unindent();
	ipw.println("</xsd:complexContent>");
	ipw.unindent();
	ipw.println("</xsd:complexType>");

  }

  private void declareDomainDef (Domain domain)
  {
  	Type type=domain.getType();
	//if (type instanceof CoordType){
	//}else if (type instanceof PolylineType){
	//}else if (type instanceof SurfaceOrAreaType){
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
    		base="INTERLIS:HALIGNMENT";
    	}else if(realDomain==td.INTERLIS.VALIGNMENT){
    		base="INTERLIS:VALIGNMENT";
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
    }else{
        declareType(type,domain);
    }
  }
  private ArrayList areaAttrs=null; // array<AttributeDef attr>
  private void declareLinetables(){
	  Iterator attri=areaAttrs.iterator();
	  while(attri.hasNext()){
		  AttributeDef attr=(AttributeDef)attri.next();
		  AbstractClassDef aclass=(AbstractClassDef)attr.getContainer();
		  String linetableName=getName(aclass)+"."+getTransferName(attr);
		  	String baseElement="gml:AbstractFeature";
			ipw.println("<xsd:element name=\""+linetableName+"\" type=\""+linetableName+"Type\" substitutionGroup=\""+baseElement+"\"/>");
			ipw.println("<xsd:complexType  name=\"" + linetableName + "Type\">");
			ipw.indent ();
			ipw.println ("<xsd:complexContent>");
		  	ipw.indent ();
		  	String baseXsdType="gml:AbstractFeatureType";
			ipw.println ("<xsd:extension base=\""+baseXsdType+"\">");
			ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent();
			ipw.println("<xsd:element name=\"geometry\" type=\"gml:CurvePropertyType\"/>");
			AreaType type=(AreaType)attr.getDomainResolvingAliases();
			Table lineattrs=type.getLineAttributeStructure();
			if(lineattrs!=null){
				ipw.println("<xsd:element name=\"lineattr\">");
				ipw.indent();
				ipw.println("<xsd:complexType>");
				ipw.indent();
				ipw.println("<xsd:sequence>");
				ipw.indent();
				ipw.println("<xsd:element ref=\""+getScopedName(lineattrs)+"\"/>");
				ipw.unindent();
				ipw.println("</xsd:sequence>");
				ipw.println("<xsd:attributeGroup ref=\"gml:OwnershipAttributeGroup\"/>");
				ipw.unindent();
				ipw.println("</xsd:complexType>");
				ipw.unindent();
				ipw.println("</xsd:element>");
			}
			
			ipw.unindent();
			ipw.println("</xsd:sequence>");
			ipw.unindent();
			ipw.println("</xsd:extension>");
			ipw.unindent();
			ipw.println("</xsd:complexContent>");
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
    		base="INTERLIS:HALIGNMENT";
    	}else if(realDomain==td.INTERLIS.VALIGNMENT){
    		base="INTERLIS:VALIGNMENT";
    	}else if(realDomain==td.INTERLIS.ANYOID){
    		base="xsd:token";
    	}else if(realDomain==td.INTERLIS.I32OID){
    		base="INTERLIS:I32OID";
    	}else if(realDomain==td.INTERLIS.STANDARDOID){
    		base="INTERLIS:STANDARDOID";
    	}else if(realDomain==td.INTERLIS.UUIDOID){
    		base="INTERLIS:UUIDOID";
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
        		areaAttrs.add(attribute);
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
		if (type instanceof CoordType){
			ipw.println(
				"<xsd:element name=\""
					+ getTransferName(attribute)
					+ "\""
					+ minOccurs
					+ " type=\"gml:PointPropertyType\""
					+ ">");
			ipw.indent();
			//ipw.println("<!-- "+PRBLMTAG+" unable to express domain of values -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express unit of values -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express CRS -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express rotation -->");
			ipw.unindent();
			ipw.println("</xsd:element>");
		}else if (type instanceof PolylineType){
			ipw.println(
				"<xsd:element name=\""
					+ getTransferName(attribute)
					+ "\""
					+ minOccurs
					+ " type=\"gml:CurvePropertyType\""
					+ ">");
			ipw.indent();
			//ipw.println("<!-- "+PRBLMTAG+" unable to express domain/unit/crs of control points -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express allowed line forms -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express line attributes -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express overlaps -->");
			ipw.unindent();
			ipw.println("</xsd:element>");
		}else if (type instanceof SurfaceType){
			ipw.println(
				"<xsd:element name=\""
					+ getTransferName(attribute)
					+ "\""
					+ minOccurs
					+ " type=\"gml:SurfacePropertyType\""
					+ ">");
			ipw.indent();
			//ipw.println("<!-- "+PRBLMTAG+" unable to express domain/unit/crs of control points -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express allowed line forms -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express line attributes -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express overlaps -->");
			//if(type instanceof AreaType){
			//	ipw.println("<!-- "+PRBLMTAG+" unable to express AREA constraint -->");
			//}
			ipw.unindent();
			ipw.println("</xsd:element>");
		}else if (type instanceof AreaType){
        	String base=getScopedName(((AreaType)type).getControlPointDomain());
      	  	ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\""+base+"\""+minOccurs+">");
			ipw.indent();
			//ipw.println("<!-- "+PRBLMTAG+" unable to express domain/unit/crs of control points -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express allowed line forms -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express line attributes -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express overlaps -->");
			//if(type instanceof AreaType){
			//	ipw.println("<!-- "+PRBLMTAG+" unable to express AREA constraint -->");
			//}
			ipw.unindent();
			ipw.println("</xsd:element>");
			// remember to create linetable
       		areaAttrs.add(attribute);
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
			ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"gml:ReferenceType\""+minOccurs+">");
				ipw.indent ();
				ipw.println("<xsd:annotation>");
					ipw.indent ();
					ipw.println("<xsd:appinfo>");
						ipw.indent ();
						ipw.println("<gml:targetElement>"+getScopedName(((ReferenceType)type).getReferred())+"</gml:targetElement>");
						ipw.unindent ();
					ipw.println("</xsd:appinfo>");
					ipw.unindent ();
				ipw.println("</xsd:annotation>");
				ipw.unindent ();
			ipw.println ("</xsd:element>");
		}else if (type instanceof EnumerationType && !attribute.isFinal()){
  	      ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"gml:CodeWithAuthorityType\""+minOccurs+"/>");
	      codelists.add(attribute);
		}else if (type instanceof EnumTreeValueType && !attribute.isFinal()){
	  	      ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"gml:CodeWithAuthorityType\""+minOccurs+"/>");
		      codelists.add(attribute);
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
  private ArrayList codelists=null; // array<Domain | AttributeDef>
  private void declareCodelists()
  {
	  if(codelists.size()>0){
		  
		  
			ipw.println ("<xsd:annotation>");
			ipw.indent ();
			
			ipw.println("<xsd:appinfo source=\"http://www.interlis.ch/ili2c\">");
			ipw.indent();

			String modelName=currentModel.getName();
			int oid=1;
			Iterator codelisti=codelists.iterator();
			while(codelisti.hasNext()){
				Element codelisto=(Element)codelisti.next();
				String enumName=null;
				Type type=null;
				if(codelisto instanceof Domain){
					Domain domain=(Domain)codelisto;
					enumName=getName(domain);
					type=domain.getType();
				}else{
					AttributeDef attr=(AttributeDef)codelisto;
					enumName=getName(attr.getContainer())+"."+getTransferName(attr);
					type=attr.getDomain();
				}
				ipw.println("<gml:Dictionary gml:id=\"o"+ oid++ +"\">");
				ipw.indent();
				ipw.println("<gml:identifier codeSpace=\""+ILIGML_XMLNSBASE+"/"+modelName+"\">"+enumName+"</gml:identifier>");
				String codeSpace=ILIGML_XMLNSBASE+"/"+modelName+"/"+enumName;
				
				java.util.ArrayList ev = new java.util.ArrayList();
				if(type instanceof EnumerationType){
					buildEnumList(ev, "", ((EnumerationType)type).getConsolidatedEnumeration());					
				}else if(type instanceof EnumTreeValueType){
					buildEnumValList(ev, "", ((EnumTreeValueType)type).getConsolidatedEnumeration());
				}else{
					throw new IllegalStateException("unexpected type "+type);
				}
				Iterator iter = ev.iterator();
				while (iter.hasNext()) {
					String enumVal = (String) iter.next();
					ipw.println("<gml:dictionaryEntry>");
					ipw.indent();
					ipw.println("<gml:Definition gml:id=\"o"+ oid++ +"\">");
					ipw.indent();
					ipw.println("<gml:identifier codeSpace=\""+codeSpace+"\">"+enumVal+"</gml:identifier>");
					ipw.unindent();
					ipw.println("</gml:Definition>");
					ipw.unindent();
					ipw.println("</gml:dictionaryEntry>");
				}
				
				ipw.unindent();
				ipw.println("</gml:Dictionary>");
			
			}

			ipw.unindent();
			ipw.println ("</xsd:appinfo>");
			ipw.unindent();
			ipw.println ("</xsd:annotation>");
		  
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
			declarePolylineValue(domain,(PolylineType)type);
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof SurfaceOrAreaType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			declarePolylineValue(domain,(SurfaceOrAreaType)type);
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if (type instanceof CoordType){
		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			ipw.println ("<xsd:complexContent>");
			ipw.indent ();
			String base;
			if(domain!=null && domain.getExtending()!=null){
				base=getScopedName(domain.getExtending());
			}else{
				base="gml:PointPropertyType";
			}
			ipw.println ("<xsd:restriction base=\""+base+"\">");
			ipw.indent ();
			ipw.println("<xsd:sequence>");
			ipw.indent ();
			ipw.println("<xsd:element ref=\"gml:Point\"/>");
			ipw.unindent ();
			ipw.println("</xsd:sequence>");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express domain of values -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express unit of values -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express CRS -->");
			//ipw.println("<!-- "+PRBLMTAG+" unable to express rotation -->");
			ipw.unindent ();
			ipw.println ("</xsd:restriction>");
			ipw.unindent ();
			ipw.println ("</xsd:complexContent>");
		    ipw.unindent ();
	    ipw.println ("</xsd:complexType>");
    }else if(type instanceof EnumerationType){
    	if(domain!=null && !domain.isFinal()){
    		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			ipw.println ("<xsd:simpleContent>");
			ipw.indent ();
			String base;
			if(domain!=null && domain.getExtending()!=null){
				base=getScopedName(domain.getExtending());
			}else{
				base="gml:CodeWithAuthorityType";
			}
			ipw.println ("<xsd:restriction base=\""+base+"\">");
			ipw.indent ();
			ipw.println ("<xsd:attribute name=\"codeSpace\" type=\"xsd:anyURI\" use=\"required\"/>");
			ipw.unindent ();
			ipw.println ("</xsd:restriction>");
			ipw.unindent ();
			ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
		    ipw.println ("</xsd:complexType>");
		    codelists.add(domain);
    	}else{
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
    	}
    }else if(type instanceof EnumTreeValueType){
    	if(domain!=null && !domain.isFinal()){
    		ipw.println ("<xsd:complexType"+typeName+">");
		    ipw.indent ();
			ipw.println ("<xsd:simpleContent>");
			ipw.indent ();
			String base;
			if(domain!=null && domain.getExtending()!=null){
				base=getScopedName(domain.getExtending());
			}else{
				base="gml:CodeWithAuthorityType";
			}
			ipw.println ("<xsd:restriction base=\""+base+"\">");
			ipw.indent ();
			ipw.println ("<xsd:attribute name=\"codeSpace\" type=\"xsd:anyURI\" use=\"required\"/>");
			ipw.unindent ();
			ipw.println ("</xsd:restriction>");
			ipw.unindent ();
			ipw.println ("</xsd:simpleContent>");
		    ipw.unindent ();
		    ipw.println ("</xsd:complexType>");
		    codelists.add(domain);
    	}else{
    	      ipw.println ("<xsd:simpleType"+typeName+">");
    	        ipw.indent ();
    	        ipw.println ("<xsd:restriction base=\"xsd:normalizedString\">");
    	          ipw.indent ();
    	          java.util.ArrayList ev=new java.util.ArrayList();
    	          buildEnumValList(ev,"",((EnumTreeValueType)type).getConsolidatedEnumeration());
    	          Iterator iter=ev.iterator();
    	          while(iter.hasNext()){
    	            String value=(String)iter.next();
    	            ipw.println ("<xsd:enumeration value=\""+value+"\"/>");
    	          }
    	          ipw.unindent ();
    	        ipw.println ("</xsd:restriction>");
    	        ipw.unindent ();
    	      ipw.println ("</xsd:simpleType>");
    	}
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



  private void declarePolylineValue(Domain domain,LineType type)
  {
	ipw.println ("<xsd:complexContent>");
	ipw.indent ();
	String base;
	if(domain!=null && domain.getExtending()!=null){
		base=getScopedName(domain.getExtending());
	}else{
		if (type instanceof SurfaceOrAreaType){
			base="gml:SurfacePropertyType";
		}else{
			base="gml:CurvePropertyType";
		}
	}
	ipw.println ("<xsd:restriction base=\""+base+"\">");
	ipw.indent ();
	ipw.println("<xsd:sequence>");
	ipw.indent ();
	if (type instanceof SurfaceOrAreaType){
		ipw.println("<xsd:element ref=\"gml:Polygon\"/>"); // gml:AbstractSurface
	}else{
		ipw.println("<xsd:element ref=\"gml:AbstractCurve\"/>"); // 
	}
	ipw.unindent ();
	ipw.println("</xsd:sequence>");
	//ipw.println("<!-- "+PRBLMTAG+" unable to express domain/unit/crs of control points -->");
	//ipw.println("<!-- "+PRBLMTAG+" unable to express allowed line forms -->");
	//ipw.println("<!-- "+PRBLMTAG+" unable to express line attributes -->");
	//ipw.println("<!-- "+PRBLMTAG+" unable to express overlaps -->");
	//if(type instanceof AreaType){
	//	ipw.println("<!-- "+PRBLMTAG+" unable to express AREA constraint -->");
	//}
	ipw.unindent ();
	ipw.println ("</xsd:restriction>");
	ipw.unindent ();
	ipw.println ("</xsd:complexContent>");
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
  private void buildEnumValList(java.util.List accu,String prefix1,ch.interlis.ili2c.metamodel.Enumeration enumer){
      Iterator iter = enumer.getElements();
      String prefix="";
      if(prefix1.length()>0){
        prefix=prefix1+".";
      }
      while (iter.hasNext()) {
        ch.interlis.ili2c.metamodel.Enumeration.Element ee=(ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next();
        ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
        // add ee to accu
        accu.add(prefix+ee.getName());
        if (subEnum != null)
        {
          // ee is not leaf, add its name to prefix and add sub elements to accu
          buildEnumList(accu,prefix+ee.getName(),subEnum);
        }
      }
  }

}
