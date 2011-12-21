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
public final class ETF1Generator
{
    public static final String ILIGML_XMLNSBASE="http://www.interlis.ch/INTERLIS2.3/GML32";
    public static final String BASKETMEMBER="member";
    public static final String TRANSFER="TRANSFER";
    public static final String TRANSFERMEMBER="baskets";
    public static final String ORDER_POS="ORDER_POS";
    public static final String REF="ref";
    public static final String LINK_DATA="LINK_DATA";
    
  IndentPrintWriter   ipw;
  TransferDescription td;
  String outdir;
  int                 numErrors = 0;
	//private static String PRBLMTAG="preGML32:";

  static ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());


  private ETF1Generator(TransferDescription td, String outdir)
  {
    this.td = td;
    this.outdir = outdir;
    setupNameMapping();
  }

  public static int generate (TransferDescription td,String outdir)
  {
    ETF1Generator d = new ETF1Generator (td,outdir);
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
		  ret.put(def.getScopedName(null), def2name.get(def));
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
	surfaceOrAreaAttrs=new ArrayList();
	
	// start output
	ipw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
	ipw.indent();
	ipw.println("xmlns=\""+getNsName(model)+"\""
	  +" targetNamespace=\""+getNsName(model)+"\""
	  +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\"");
		ipw.println("xmlns:esd=\"http://esd.ehi.ch/etf/1\"");
		ipw.println("xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"");
		// xmlns declartion of imported ili-models
		Model importedModels[]=model.getImporting();
		for(int modeli=0;modeli<importedModels.length;modeli++){
			if(importedModels[modeli]!=td.INTERLIS){
				ipw.println("xmlns:"+importedModels[modeli].getName()+"=\""+getNsName(importedModels[modeli])+"\"");
			}
		}
	ipw.println(">");
	ipw.unindent();

	if(false){
		ipw.println ("<xsd:annotation>");
		
	    // compiler info
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
	}

	
	if(false){
		// import of GML schema
		ipw.println("<xsd:import namespace=\"http://www.opengis.net/gml/3.2\"/>");
		// import of base ILIGML schema
		ipw.println("<xsd:import namespace=\""+ILIGML_XMLNSBASE+"/INTERLIS\"/>");
	}
	// import schemas of imported ili-models
	for(int modeli=0;modeli<importedModels.length;modeli++){
		if(importedModels[modeli]!=td.INTERLIS){
			ipw.println("<xsd:import namespace=\""+getNsName(importedModels[modeli])+"\"/>");
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

  private String getNsName(Model model)
  {
	  return ILIGML_XMLNSBASE+"/"+model.getName();
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

  }


  private boolean isMultiValueWrapper(Viewable v)
  {
	  Domain domain=getMultiValueDomain(v);
	  return domain!=null ? true : false;
  }
  private Domain getMultiValueDomain(Viewable v)
  {
	  if(v instanceof AssociationDef){
		  return null;
	  }
	  Table aclass=(Table)v;
	  if(aclass.isIdentifiable()){
		  return null;
	  }
	  Iterator attri=v.getAttributes();
	  AttributeDef attr=null;
	  if(!attri.hasNext()){
		  return null;
	  }
	  attr=(AttributeDef)attri.next();
	  
	  // more than one attribute?
	  if(attri.hasNext()){
		  return null;
	  }
	  if(!attr.getName().equals("value")){
		  return null;
	  }
	  Type type=attr.getDomain();
	  if(!(type instanceof TypeAlias)){
		  return null;
	  }
	  Domain domain=((TypeAlias)type).getAliasing();
	  if(!(domain.getName()+"_").equals(v.getName())){
		  return null;
	  }
	  return domain;
  }
  private void declareAbstractClassDef(Viewable v)
  {
	  if(v instanceof AssociationDef){ 
		  AssociationDef assoc=(AssociationDef)v;
		  if(assoc.getDerivedFrom()!=null){
			  // derived; skip it
			  return;
		  }
		  if(!v.getAttributes().hasNext()){
			  // no attributes
			  // skip it
			  return;
		  }
	  }
	  if(isMultiValueWrapper(v)){
		  return;
	  }
  	Viewable baseType=(Viewable)v.getExtending();
  	if(baseType!=null){
  		String baseElement=getScopedName(baseType);
  		ipw.println("<xsd:element name=\""+getName(v)+"\" type=\""+getName(v)+"Type\" substitutionGroup=\""+baseElement+"\"/>");
  	}else{
  		ipw.println("<xsd:element name=\""+getName(v)+"\" type=\""+getName(v)+"Type\"/>");
  	}
	ipw.println("<xsd:complexType  name=\"" + getName(v) + "Type\">");
	ipw.indent ();
	if(baseType!=null){
		String baseXsdType=getScopedName(baseType)+"Type";
		ipw.println ("<xsd:complexContent>");
	  	ipw.indent ();
		ipw.println ("<xsd:extension base=\""+baseXsdType+"\">");
		ipw.indent ();
	}
	ipw.println("<xsd:sequence>");
	ipw.indent();
	/* Find which attributes are going to be elements of this class. */
	Iterator iter = getDefinedAttributesAndRoles(v);
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
			
			// not an embedded role?
			if (!obj.embedded){
				if(role.getExtending()==null){
					if(role.isOrdered()){
						ipw.println("<xsd:element name=\""+ getTransferName(role)+ ">");
					}else{
						ipw.println("<xsd:element name=\""+ getTransferName(role)+ "\" type=\"xsd:IDREF\">");
					}
					ipw.indent();
					ipw.println("<xsd:annotation>");
						ipw.indent();
						ipw.println("<xsd:appinfo>");
							ipw.indent();
							ipw.println("<esd:targetElement>"+getScopedName(role.getDestination())+"</esd:targetElement>");
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
						String maxOccurs = "";
						if (card.getMaximum()==Cardinality.UNBOUND) {
							maxOccurs = " maxOccurs=\"unbounded\"";
						}else if(card.getMaximum()>1){
							maxOccurs = " maxOccurs=\""+card.getMaximum()+"\"";
						}
						if(oppend.getKind()==RoleDef.Kind.eCOMPOSITE){
							ipw.println("<xsd:element name=\""+ getTransferName(role)+"\""+ minOccurs+ maxOccurs+">");
							ipw.indent();
							ipw.println("<xsd:complexType>");
							ipw.indent();
							ipw.println("<xsd:sequence>");
							ipw.println("<xsd:element ref=\""+getScopedName(role.getDestination())+"\"/>");
							ipw.println("</xsd:sequence>");
							ipw.unindent();
							ipw.println("</xsd:complexType>");
							ipw.unindent();
							ipw.println("</xsd:element>");
						}else if(oppend.getKind()==RoleDef.Kind.eAGGREGATE){
							EhiLogger.logAdaption(roleOwner.getScopedName(null)+": aggregate ignored");
						}else{
							if(oppend.isOrdered()){
								ipw.println("<xsd:element name=\""+ getTransferName(role)+"\""+ minOccurs+ maxOccurs+">");
							}else{
								ipw.println("<xsd:element name=\""+ getTransferName(role)+ "\""+minOccurs+maxOccurs+">");
							}
							ipw.indent();
							ipw.println("<xsd:annotation>");
								ipw.indent();
								ipw.println("<xsd:appinfo>");
									ipw.indent();
									ipw.println("<esd:targetElement>"+getScopedName(role.getDestination())+"</esd:targetElement>");
									ipw.unindent();
								ipw.println("</xsd:appinfo>");
								ipw.unindent();
							ipw.println("</xsd:annotation>");
								ipw.println("<xsd:complexType>");
								ipw.indent();
								ipw.println("<xsd:sequence/>");
								ipw.println("<xsd:attribute name=\""+REF+"\" type=\"xsd:IDREF\"/>");					
								if(oppend.isOrdered()){
								ipw.println("<xsd:attribute name=\""+ORDER_POS+"\" type=\"xsd:positiveInteger\"/>");					
								}
								ipw.unindent();
								ipw.println("</xsd:complexType>");
							//ipw.println("<!-- "+PRBLMTAG+" unable to express aggregation kind -->");
							//ipw.println("<!-- "+PRBLMTAG+" unable to express cardinality -->");
							ipw.unindent();
							ipw.println("</xsd:element>");
							
						}
						if(roleOwner.getAttributes().hasNext()){
							EhiLogger.logAdaption(roleOwner.getScopedName(null)+": assoc attrs ignored");
							if(false){
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
	}
	ipw.unindent();
	ipw.println("</xsd:sequence>");
	if(baseType!=null){
		ipw.unindent();
		ipw.println("</xsd:extension>");
		ipw.unindent();
		ipw.println("</xsd:complexContent>");
	}else{
		/* ipw.println ("<xsd:attribute ref=\"xml:id\"/>"); */
		ipw.println ("<xsd:attribute name=\"id\" type=\"xsd:ID\"/>");
	}
	ipw.unindent();
	ipw.println("</xsd:complexType>");
  }

  public Iterator getDefinedAttributesAndRoles(Viewable athis)
  {
		List result=new ArrayList(); // of Element
		Viewable v=athis;
		// for all, at this level defined/extended, attributes and roles
		Iterator[] it = new Iterator[]
		{
			v.getRolesIterator(),
		  v.getDefinedAttributes()
		};
		Iterator attri=new CombiningIterator(it);
		//Iterator attri=v.iterator();
		while(attri.hasNext()){
			Object obj=attri.next();
			if(obj instanceof AttributeDef){
				AttributeDef attr=(AttributeDef)obj;
				result.add(new ViewableTransferElement(obj));
			}else if(obj instanceof RoleDef){
				RoleDef role=(RoleDef)obj;
				result.add(new ViewableTransferElement(obj));
			}
		}
		if(v instanceof AbstractClassDef){
			// for all, at this level defined/extended, embedded associations
			Iterator embi= ((AbstractClassDef) v).getDefinedTargetForRoles();
			List embv = new ArrayList();
			while(embi.hasNext()){
				RoleDef emb=(RoleDef)embi.next();
				if(emb.getKind()==RoleDef.Kind.eCOMPOSITE){
					embv.add(emb);
				}else if(emb.getKind()==RoleDef.Kind.eAGGREGATE){
					embv.add(emb);
				}else{
					if(emb.getOppEnd().getKind()==RoleDef.Kind.eASSOCIATE){
						embv.add(emb);
					}
				}
			}
			// sort them according to name of opposide role
			java.util.Collections.sort(embv,new java.util.Comparator(){
				public int compare(Object o1,Object o2){
					RoleDef role1=((RoleDef)o1).getOppEnd();
					RoleDef role2=((RoleDef)o2).getOppEnd();
					return role1.getName().compareTo(role2.getName());
				}
			});
			attri=embv.iterator();
			while (attri.hasNext()) {
				RoleDef role = (RoleDef) attri.next();
				RoleDef oppend = role.getOppEnd();
				result.add(new ViewableTransferElement(oppend,true));
			}
		}
	return result.iterator ();
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
  private ArrayList surfaceOrAreaAttrs=null; // array<AttributeDef attr>
  private void declareLinetables(){
	  Iterator attri=surfaceOrAreaAttrs.iterator();
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
			ipw.println("<xsd:element name=\"mainTable\" type=\"gml:ReferenceType\">");
			ipw.indent();
			ipw.println("<xsd:annotation>");
			ipw.indent();
			ipw.println("<xsd:appinfo>");
			ipw.indent();
			ipw.println("<gml:targetElement>"+getName(aclass)+"</gml:targetElement>");
			ipw.unindent();
			ipw.println("</xsd:appinfo>");
			ipw.unindent();
			ipw.println("</xsd:annotation>");
			ipw.unindent();
			ipw.println("</xsd:element>");
			ipw.println("<xsd:element name=\"geometry\" type=\"gml:CurvePropertyType\"/>");
			SurfaceOrAreaType type=(SurfaceOrAreaType)attr.getDomainResolvingAliases();
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
        	if(realDomain.getType() instanceof SurfaceOrAreaType){
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
		}else if (type instanceof SurfaceOrAreaType){
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
			// remember to create linetable
       		surfaceOrAreaAttrs.add(attribute);
		}else if (type instanceof CompositionType){
			CompositionType composition=(CompositionType)type;
			Table part=composition.getComponentType();
			Cardinality card=composition.getCardinality();
			minOccurs="";
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
			if(isMultiValueWrapper(part)){
				Domain valueDomain=getMultiValueDomain(part);
				ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\""+getScopedName(valueDomain)+"\""+minOccurs+maxOccurs+"/>");
			}else{
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
			}
		}else if (type instanceof ReferenceType){
			ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"xsd:IDREF\""+minOccurs+">");
				ipw.indent ();
				ipw.println("<xsd:annotation>");
					ipw.indent ();
					ipw.println("<xsd:appinfo>");
						ipw.indent ();
						ipw.println("<esd:targetElement>"+getScopedName(((ReferenceType)type).getReferred())+"</esd:targetElement>");
						ipw.unindent ();
					ipw.println("</xsd:appinfo>");
					ipw.unindent ();
				ipw.println("</xsd:annotation>");
				ipw.unindent ();
			ipw.println ("</xsd:element>");
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
			          ipw.indent ();
			          ipw.println ("<xsd:minInclusive value=\""+((NumericType)type).getMinimum().doubleValue()+"\"/>");
			          ipw.println ("<xsd:maxInclusive value=\""+((NumericType)type).getMaximum().doubleValue()+"\"/>");
			          ipw.unindent ();
		            ipw.println ("</xsd:restriction>");
				}else{
					ipw.println ("<xsd:restriction base=\"xsd:decimal\">");
			          ipw.indent ();
			          ipw.println ("<xsd:minInclusive value=\""+((NumericType)type).getMinimum().toString()+"\"/>");
			          ipw.println ("<xsd:maxInclusive value=\""+((NumericType)type).getMaximum().toString()+"\"/>");
			          ipw.unindent ();
		            ipw.println ("</xsd:restriction>");
				}
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

}
