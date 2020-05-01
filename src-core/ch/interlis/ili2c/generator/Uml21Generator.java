package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;
import ch.interlis.ili2c.metamodel.RoleDef.Kind;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import ch.ehi.basics.logging.EhiLogger;

/** A class used to generate an INTERLIS model description as XMI.
*/
public class Uml21Generator
{
	//public static String umlNs="http://schema.omg.org/spec/UML/2.1" ;
	public static String umlNs="http://www.eclipse.org/uml2/3.0.0/UML" ;
	public static String umlPfx="uml:" ;
	public static String xmiNs="http://schema.omg.org/spec/XMI/2.1" ;
	public static String xsiNs="http://www.w3.org/2001/XMLSchema-instance" ;
	public static String iliNs="http://www.interlis.ch/INTERLIS2.3/UML2" ;
	public static String ecoreIliProfileLocation="INTERLIS.profile.uml#ch.interlis.ili23.ecore.uml2";
	public static String idTextType="C16095C6-1D80-49ab-9A0B-5847A355489B"; // Verschiedene Arten von Text
	public static String idEnumerationType="279A049B-2BCC-4fb5-9C8F-3B22EF3EE0ED"; // Verschiedene Arten von Aufzaehlungen
	public static String idEnumTreeValueType="064231DE-6F7D-43bb-A0B9-B4C37906E012"; // Weitere Art von Aufzaehlung
	public static String idAlignmentType="B5F4B6BC-41B7-400b-84DF-64474E7B3F07"; // Vordefinierte Aufzaehlung (fuer ili1-Kompatibilitaet)
	public static String idBooleanType="D2A6D48C-97F0-44d0-910A-7C55943BECA7"; // Vordefinierte Aufzaehlung (True/False)
	public static String idNumericType="39FDCDA0-DAE3-4c14-B2DC-C4F0F4DAAAF8"; // Ganze Zahlen, Fliesskommazahlen, Festkommazahlen (inkl. min/max, Masseinheit)
	public static String idFormattedType="36B2DF93-856E-4997-B610-61C456A6BD1B"; // Anwenderdefinierbare serialisierte Datentypen
	public static String idTimeType="FBAA3B46-D164-4ecf-96C5-31D69315FDDF"; // Zeit inkl. min/max
	public static String idDateType="259523F4-AAC6-4fe2-8B34-B9113604AB8C"; // Datum inkl. min/max
	public static String idDateTimeType="5904195D-E562-4262-9F6B-7104FA1AB70D"; // Datum + Zeit inkl. min/max
	public static String idOIDType="19ACF3BB-5A8B-4b73-9C38-D37C3DC8B207"; // Anwenderdefinierbare Arten von Objekt-Identifikatoren
	public static String idBlackboxType="93ADB54A-B82F-421c-8C50-8881FFD61F42"; // Beliebige XML-Fragmente oder binaere Objekte
	public static String idClassType="F1B17749-EACA-4ed1-A9EC-40B63C2A94B0"; // Klassenname (primaer fuer Metamodelle relevant)
	public static String idAttributePathType="5AD53796-0781-4f5d-AEFA-B11FD46DD5AB"; // Attributname (primaer fuer Metamodelle relevant)
	public static String idCoordinateType="45397FCA-66E2-4ac8-A8A7-89D3A573E44B"; // Koordinate (mit verschiedenen Konsistenzbedingungen)
	public static String idPolylineType="F466480C-C0A2-47de-A0FC-D9D3932DB96E"; // Linie (mit verschiedenen Konsistenzbedingungen)
	public static String idSurfaceType="D874C9DB-F082-4bd4-B4F1-DACD75BBFDD6"; // Einzelflaeche (mit verschiedenen Konsistenzbedingungen)
	public static String idAreaType="E06837E3-F58C-49bb-8F82-22F666FB4CCF"; // Flaechennetz (= Einzelflaeche + vordefinierte Konsistenzbedingung)

	private javax.xml.stream.XMLStreamWriter xout=null;
	private TransferDescription td=null;
  private Uml21Generator()
  {
  }

  
	public static void generate( java.io.File outFile, ch.interlis.ili2c.metamodel.TransferDescription td ) 
	{
		try {
			new Uml21Generator().doit(outFile, td );
		} catch (IOException e) {
			EhiLogger.logError(e);
		} catch (XMLStreamException e) {
			EhiLogger.logError(e);
		}
	}
	private void doit( java.io.File outFile, ch.interlis.ili2c.metamodel.TransferDescription td ) throws IOException, XMLStreamException 
	{
		this.td=td;
			Writer out=new BufferedWriter(new FileWriter(outFile));
			javax.xml.stream.XMLOutputFactory output = javax.xml.stream.XMLOutputFactory
			.newInstance();

			xout = output.createXMLStreamWriter(out);
			xout.writeStartDocument("UTF-8", "1.0");
			xout.setPrefix("xmi", xmiNs);
			xout.setPrefix("uml", umlNs);
			xout.setPrefix("ili", iliNs);
			xout.setPrefix("xsi", xsiNs);
			xout.writeStartElement(xmiNs, "XMI");
			xout.writeNamespace("xsi", xsiNs);
			xout.writeNamespace("xmi", xmiNs);
			xout.writeNamespace("uml", umlNs);
			xout.writeNamespace("ili", iliNs);
			xout.writeAttribute(xmiNs, "version","2.1");
			xout.writeAttribute(xsiNs, "schemaLocation", iliNs+" "+ecoreIliProfileLocation);
			{
				xout.writeStartElement(xmiNs,"Documentation"); 
				xout.writeAttribute("exporter","ili2c"); //="Enterprise Architect" 
				xout.writeAttribute("exporterVersion",TransferDescription.getVersion());
				xout.writeEndElement();
			}
			
			{ //<uml:Model xmi:type="uml:Model" name="EA_Model" visibility="public">
				xout.writeStartElement(umlNs,"Model"); 
				xout.writeAttribute("name","INTERLIS_Model");
				xout.writeAttribute("visibility","public");
				{ // <packagedElement xmi:type="uml:Package" xmi:id="EAPK_8C0D78A6_FDC6_4d15_9529_BFC10FEF2093" name="Model" visibility="public">
					xout.writeStartElement("packagedElement"); 
					xout.writeAttribute(xmiNs, "type",umlPfx+"Package");
					xout.writeAttribute(xmiNs, "id","1");
					xout.writeAttribute("name","import");
					xout.writeAttribute("visibility","public");
					
					Iterator modeli=td.iterator();
					while(modeli.hasNext()){
						Object modelo=modeli.next();
						if(modelo instanceof Model){
							Model model=(Model)modelo;
							{
								visitModelDef(model);
							}
						}
					}
					
					xout.writeEndElement();
				}
				
				xout.writeEndElement();
			}
			writeStereotypes();
			xout.writeEndElement();
			xout.flush();
	}


	private void writePrimitiveType(String id, String name) throws XMLStreamException
	{
		xout.writeStartElement("packagedElement");
		xout.writeAttribute(xmiNs, "type",umlPfx+ "PrimitiveType");
		xout.writeAttribute(xmiNs, "id",id);
		xout.writeAttribute("name",name);
		xout.writeAttribute("visibility","public");
		xout.writeEndElement();
		
	}
	private void visitModelDef(Model model) throws XMLStreamException {
		xout.writeStartElement("packagedElement"); 
		xout.writeAttribute(xmiNs, "type",umlPfx+"Package");
		xout.writeAttribute(xmiNs, "id",model.getName());
		xout.writeAttribute("name",model.getName());
		xout.writeAttribute("visibility","public");
	    xout.writeStartElement("profileApplication");
	    {
			xout.writeStartElement("eAnnotations");
			xout.writeAttribute("source",
					"http://www.eclipse.org/uml2/2.0.0/UML");
			xout.writeStartElement("references");
			xout.writeAttribute("href",ecoreIliProfileLocation);
			xout.writeEndElement();
			xout.writeEndElement();
			xout.writeStartElement("appliedProfile");
			xout.writeAttribute("href",
					"INTERLIS.profile.uml#ch.interlis.ili23.uml2");
			xout.writeEndElement();
			applyStereotype(model);
	    }
	    xout.writeEndElement();
		
		if(model==td.INTERLIS){
			writePrimitiveType(idTextType,"TextType");
			writePrimitiveType(idEnumerationType,"EnumerationType");
			writePrimitiveType(idEnumTreeValueType,"EnumTreeValueType");
			writePrimitiveType(idAlignmentType,"AlignmentType");
			writePrimitiveType(idBooleanType,"BooleanType");
			writePrimitiveType(idNumericType,"NumericType");
			writePrimitiveType(idFormattedType,"FormattedType");
			writePrimitiveType(idTimeType,"TimeType");
			writePrimitiveType(idDateType,"DateType");
			writePrimitiveType(idDateTimeType,"DateTimeType");
			writePrimitiveType(idOIDType,"OIDType");
			writePrimitiveType(idBlackboxType,"BlackboxType");
			writePrimitiveType(idClassType,"ClassType");
			writePrimitiveType(idAttributePathType,"AttributePathType");
			writePrimitiveType(idCoordinateType,"CoordinateType");
			writePrimitiveType(idPolylineType,"PolylineType");
			writePrimitiveType(idSurfaceType,"SurfaceType");
			writePrimitiveType(idAreaType,"AreaType");
		}
		Iterator elei=model.iterator();
		while(elei.hasNext()){
			Object ele=elei.next();
			if(ele instanceof Topic){
				Topic topic=(Topic)ele;
				{
					visitTopicDef(topic);
				}
			}else if(ele instanceof Domain){
				Domain domain=(Domain)ele;
				{
					visitDomainDef(domain);
				}
			}else if(ele instanceof Unit){
				visitUnitDef((Unit)ele);
			}else if(ele instanceof Table){
				visitAbstractClassDef((Table)ele);
			}
		}
		xout.writeEndElement();
	}
	private void visitTopicDef(Topic topic) throws XMLStreamException {
		xout.writeStartElement("packagedElement"); 
		xout.writeAttribute(xmiNs, "type",umlPfx+"Package");
		xout.writeAttribute(xmiNs, "id",topic.getScopedName(null));
		xout.writeAttribute("name",topic.getName());
		xout.writeAttribute("visibility","public");
		applyStereotype(topic);
		
		Iterator elei=topic.iterator();
		while(elei.hasNext()){
			Object ele=elei.next();
			if(ele instanceof Table){
				Table aclass=(Table)ele;
				{
					visitAbstractClassDef(aclass);
				}
			}else if(ele instanceof AssociationDef){
				AssociationDef assoc=(AssociationDef)ele;
				visitAbstractClassDef(assoc);
			}else if(ele instanceof Unit){
				visitUnitDef((Unit)ele);
			}else if(ele instanceof Domain){
				Domain domain=(Domain)ele;
				{
					visitDomainDef(domain);
				}
			}
		}
		xout.writeEndElement();
	}
	private void visitUnitDef(Unit def) throws XMLStreamException {
		xout.writeStartElement("packagedElement"); 
		xout.writeAttribute(xmiNs, "type",umlPfx+"Class");
		xout.writeAttribute(xmiNs, "id",def.getScopedName(null));
		xout.writeAttribute("name",def.getName());
		xout.writeAttribute("visibility","public");
		if(def.isAbstract()){
			xout.writeAttribute("isAbstract","true");
		}
		applyStereotype(def);
		if(def.getExtending()!=null){
            xout.writeStartElement("generalization");
			xout.writeAttribute("general",def.getExtending().getScopedName(null));
			xout.writeEndElement();
		}
		xout.writeEndElement();
		
	}
	private void visitDomainDef(Domain def) throws XMLStreamException {
		if(def.getType() instanceof EnumerationType){
			EnumerationType enumType=(EnumerationType)def.getType();
			xout.writeStartElement("packagedElement"); 
			xout.writeAttribute(xmiNs, "type",umlPfx+"Enumeration");
			xout.writeAttribute(xmiNs, "id",def.getScopedName(null));
			xout.writeAttribute("name",def.getName());
			xout.writeAttribute("visibility","public");
	          java.util.ArrayList ev=new java.util.ArrayList();
	          XSDGenerator.buildEnumList(ev,"",enumType.getConsolidatedEnumeration());
	          Iterator iter=ev.iterator();
	          while(iter.hasNext()){
	            String value=(String)iter.next();
			    //  <ownedLiteral xmi:id="_BdvhgOhBEeGh5Pn-M-NfAA" name="ElementA"/>
	            xout.writeStartElement("ownedLiteral");
				xout.writeAttribute("name",value);
				xout.writeEndElement();
	          }
			xout.writeEndElement();
		}else{
			xout.writeStartElement("packagedElement"); 
			xout.writeAttribute(xmiNs, "type",umlPfx+"PrimitiveType");
			xout.writeAttribute(xmiNs, "id",def.getScopedName(null));
			xout.writeAttribute("name",def.getName());
			xout.writeAttribute("visibility","public");
			if(def.getExtending()==null){
				Type type=def.getType();
				String typeId=null;
				if(type instanceof AttributePathType){
					typeId=idAttributePathType;
				}else if(type instanceof BlackboxType){
					typeId=idBlackboxType;
				}else if(type instanceof CoordType){
					typeId=idCoordinateType;
				}else if(type instanceof EnumerationType){
					typeId=idEnumerationType;
				}else if(type instanceof EnumTreeValueType){
					typeId=idEnumTreeValueType;
				}else if(type instanceof FormattedType){
					typeId=idFormattedType;
				}else if(type instanceof NumericType){
					typeId=idNumericType;
				}else if(type instanceof StructuredUnitType){
					typeId=idFormattedType;
				}else if(type instanceof TextType){
					typeId=idTextType;
				}else if(type instanceof BasketType){
					typeId=idTextType;
				}else if(type instanceof ClassType){
					typeId=idClassType;
				}else if(type instanceof EnumValType){
					throw new IllegalStateException();
				}else if(type instanceof PolylineType){
					typeId=idPolylineType;
				}else if(type instanceof SurfaceType){
					typeId=idSurfaceType;
				}else if(type instanceof AreaType){
					typeId=idAreaType;
				}else if(type instanceof MetaobjectType){
					throw new IllegalStateException();
				}else if(type instanceof ObjectType){
					throw new IllegalStateException();
				}else if(type instanceof OIDType){
					typeId=idOIDType;
				}else if(type instanceof ReferenceType){
					throw new IllegalStateException();
				}else{
					throw new IllegalStateException();
				}
	            xout.writeStartElement("generalization");
				xout.writeAttribute("general",typeId);
				xout.writeEndElement();
				
			}else{
	            xout.writeStartElement("generalization");
				xout.writeAttribute("general",def.getExtending().getScopedName(null));
				xout.writeEndElement();
			}
			
			xout.writeEndElement();
		}
		
	}
	private void visitAbstractClassDef(AbstractClassDef def) throws XMLStreamException {
		String memberEnds="";
		boolean isAssocClass=false;
		if(def instanceof AssociationDef)
		{
			Iterator iter = def.getAttributesAndRoles2();
			String sep="";
			while (iter.hasNext()) {
				ViewableTransferElement obj = (ViewableTransferElement)iter.next();
				if (obj.obj instanceof AttributeDef) {
					AttributeDef attr = (AttributeDef) obj.obj;
					isAssocClass=true;
				}
				if(obj.obj instanceof RoleDef && obj.embedded==false){
					RoleDef role = (RoleDef) obj.obj;
					memberEnds=memberEnds+sep+getRoleDefXmiId((AssociationDef)def,role);
					sep=" ";
				}
			}
		}
		xout.writeStartElement("packagedElement"); 
		if(def instanceof AssociationDef){
			if(isAssocClass){
				xout.writeAttribute(xmiNs, "type",umlPfx+"AssociationClass");
			}else{
				xout.writeAttribute(xmiNs, "type",umlPfx+"Association");
			}
		}else if(def instanceof Table){
			if(((Table)def).isIdentifiable()){
				xout.writeAttribute(xmiNs, "type",umlPfx+"Class");
			}else{
				xout.writeAttribute(xmiNs, "type",umlPfx+"DataType");
			}
		}else{
			throw new IllegalArgumentException();
		}
		
		xout.writeAttribute(xmiNs, "id",def.getScopedName(null));
		xout.writeAttribute("name",def.getName());
		xout.writeAttribute("visibility","public");
		if(def.isAbstract()){
			xout.writeAttribute("isAbstract","true");
		}
		if(def instanceof AssociationDef)
		{
			xout.writeAttribute("memberEnd",memberEnds);
		}
		if(def.getExtending()!=null){
            xout.writeStartElement("generalization");
			xout.writeAttribute("general",def.getExtending().getScopedName(null));
			xout.writeEndElement();
		}
		Iterator iter = def.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ViewableTransferElement obj = (ViewableTransferElement)iter.next();
			if (obj.obj instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) obj.obj;
				if(attr.getContainer()==def){
					// only those attributes that are defined in this class
					visitAttributeDef(attr);
				}
			}
			if(obj.obj instanceof RoleDef && obj.embedded==false){
				RoleDef role = (RoleDef) obj.obj;
				xout.writeStartElement("ownedEnd"); 
				xout.writeAttribute("name",role.getName());
				xout.writeAttribute("visibility","public");
				xout.writeAttribute(xmiNs, "id",getRoleDefXmiId((AssociationDef)def,role));
				String aggregation="none";
				if(role.hasOneOppEnd()){
					RoleDef oppend = role.getOppEnd();
					if(oppend.getKind()==Kind.eAGGREGATE){
						aggregation="shared";
					}else if(oppend.getKind()==Kind.eCOMPOSITE){
						aggregation="composite";
					}
				}else{
					// DATALOSS: AggregationKind
				}
				xout.writeAttribute("aggregation",aggregation); 
				xout.writeAttribute("type",role.getDestination().getScopedName(null));
				if(role.getContainer()!=def){
					xout.writeAttribute("redefinedProperty",getRoleDefXmiId((AssociationDef) def.getExtending(),role));
				}

				
				Cardinality card=role.getCardinality();
				
				xout.writeStartElement("upperValue");
				xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralUnlimitedNatural");
				if(card.getMaximum()==Cardinality.UNBOUND){
					xout.writeAttribute("value","*");
				}else{
					xout.writeAttribute("value",Long.toString(card.getMaximum()));
				}
			    xout.writeEndElement();

				xout.writeStartElement("lowerValue");
				xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralInteger");
				xout.writeAttribute("value",Long.toString(card.getMinimum()));
			    xout.writeEndElement();
			    
				xout.writeEndElement();
			}
		}
		xout.writeEndElement();
	}

	private String getRoleDefXmiId(AssociationDef assoc,RoleDef role) {
		return assoc.getScopedName(null)+"."+role.getName();
	}
	private String getAttributeDefXmiId(AttributeDef attr) {
		return attr.getContainer().getScopedName(null)+"."+attr.getName();
	}
	private void visitAttributeDef(AttributeDef def) throws XMLStreamException {
		xout.writeStartElement("ownedAttribute");
		xout.writeAttribute("name",def.getName());
		xout.writeAttribute(xmiNs, "id",getAttributeDefXmiId(def));
		Type type=def.getDomain();
		if(type instanceof CompositionType){
	    	CompositionType composition=(CompositionType)type;
	    	Table part=composition.getComponentType();
		    Cardinality card=composition.getCardinality();
			xout.writeAttribute("type",part.getScopedName(null));
			if(def.getExtending()!=null){
				xout.writeAttribute("redefinedProperty",getAttributeDefXmiId((AttributeDef) def.getExtending()));
			}
			
			xout.writeStartElement("upperValue");
			xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralUnlimitedNatural");
			if(card.getMaximum()==Cardinality.UNBOUND){
				xout.writeAttribute("value","*");
			}else{
				xout.writeAttribute("value",Long.toString(card.getMaximum()));
			}
		    xout.writeEndElement();

			xout.writeStartElement("lowerValue");
			xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralInteger");
			xout.writeAttribute("value",Long.toString(card.getMinimum()));
		    xout.writeEndElement();
			
		}else{
			String typeId=null;
			if(type instanceof TypeAlias){
				typeId=((TypeAlias)type).getAliasing().getScopedName(null);
			}else{
				if(type instanceof AttributePathType){
					typeId=idAttributePathType;
				}else if(type instanceof BlackboxType){
					typeId=idBlackboxType;
				}else if(type instanceof CoordType){
					typeId=idCoordinateType;
				}else if(type instanceof EnumerationType){
					typeId=idEnumerationType;
				}else if(type instanceof EnumTreeValueType){
					typeId=idEnumTreeValueType;
				}else if(type instanceof FormattedType){
					typeId=idFormattedType;
				}else if(type instanceof NumericType){
					typeId=idNumericType;
				}else if(type instanceof StructuredUnitType){
					typeId=idFormattedType;
				}else if(type instanceof TextType){
					typeId=idTextType;
				}else if(type instanceof BasketType){
					typeId=idTextType;
				}else if(type instanceof ClassType){
					typeId=idClassType;
				}else if(type instanceof EnumValType){
					throw new IllegalStateException();
				}else if(type instanceof PolylineType){
					typeId=idPolylineType;
				}else if(type instanceof SurfaceType){
					typeId=idSurfaceType;
				}else if(type instanceof AreaType){
					typeId=idAreaType;
				}else if(type instanceof MetaobjectType){
					throw new IllegalStateException();
				}else if(type instanceof ObjectType){
					throw new IllegalStateException();
				}else if(type instanceof OIDType){
					typeId=idOIDType;
				}else if(type instanceof ReferenceType){
					// TODO
                    //typeId=idTextType;
					throw new IllegalStateException("ReferenceType not yet implemented");
				}else{
					throw new IllegalStateException();
				}
			}
			xout.writeAttribute("type",typeId);
			if(def.getExtending()!=null){
				xout.writeAttribute("redefinedProperty",getAttributeDefXmiId((AttributeDef) def.getExtending()));
			}
			
			xout.writeStartElement("upperValue");
			xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralUnlimitedNatural");
			xout.writeAttribute("value","1");
		    xout.writeEndElement();

			xout.writeStartElement("lowerValue");
			xout.writeAttribute(xmiNs, "type",umlPfx+"LiteralInteger");
			if (!def.getDomain().isMandatoryConsideringAliases()) {
				xout.writeAttribute("value","1");
			}else{
				xout.writeAttribute("value","0");
			}
		    xout.writeEndElement();
		}
	    
	    xout.writeEndElement();
	
	}
	private java.util.ArrayList applies=new java.util.ArrayList();
	private void applyStereotype(Element ele)
	{
		applies.add(ele);
	}
	private void writeStereotypes() throws XMLStreamException
	{
		String idPrefix="ili";
		Iterator elei=applies.iterator();
		int id=1;
		while(elei.hasNext()){
			Element ele=(Element)elei.next();
			if(ele instanceof Model){
				xout.writeStartElement(iliNs,"ModelDef");
				xout.writeAttribute(xmiNs,"id",idPrefix+java.util.UUID.randomUUID().toString()); 
				xout.writeAttribute("base_Package",ele.getScopedName(null));
				xout.writeEndElement();
				id++;
			}else if(ele instanceof Topic){
					xout.writeStartElement(iliNs,"TopicDef");
					xout.writeAttribute(xmiNs,"id",idPrefix+java.util.UUID.randomUUID().toString()); 
					xout.writeAttribute("base_Package",ele.getScopedName(null));
					xout.writeEndElement();
					id++;
			}else if(ele instanceof Unit){
				xout.writeStartElement(iliNs,"UnitDef");
				xout.writeAttribute(xmiNs,"id",idPrefix+java.util.UUID.randomUUID().toString()); 
				xout.writeAttribute("base_Class",ele.getScopedName(null));
				xout.writeEndElement();
				id++;
			}
		}
	}
}

