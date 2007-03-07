package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;
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
  private void printModel(Model model)
  {
	String filename=outdir+"/"+model.getName()+".xsd";
	try{
		ipw=new IndentPrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(filename)));
	}catch(java.io.IOException ex){
		EhiLogger.logError(ex);
		return;
	}
	ipw.println("<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
	ipw.indent();
	ipw.println("xmlns=\"http://www.interlis.ch/INTERLIS2.3/GML32\""
	  +" targetNamespace=\"http://www.interlis.ch/INTERLIS2.3/GML32\""
	  +" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\"");
	ipw.println("xmlns:gml=\"http://www.opengis.net/gml/3.2\"");
	ipw.println(">");
	ipw.unindent();
	//ipw.println("<import namespace=\"http://www.opengis.net/gml\" schemaLocation=\"../ feature.xsd\"/>");
	ipw.println("<xsd:import namespace=\"http://www.opengis.net/gml/3.2\"/>");
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
	declareCurveSegmentWithLineAttr();
	ipw.println ("</xsd:schema>");
	ipw.close();
  }
  private boolean suppressModel (Model model)
  {
    if (model == null)
      return true;


    if (model == td.INTERLIS)
      return true;


    if ((model instanceof TypeModel) || (model instanceof PredefinedModel))
      return true;


    return false;
  }



  private boolean suppressTopic (Topic topic)
  {
    if (topic == null)
      return true;


    if (topic.isAbstract ())
      return true;


    return false;
  }
  private boolean suppressTopicInAliasTable (Topic topic)
  {
    return suppressTopic(topic);
  }


  private boolean suppressViewableInTopic (Viewable v)
  {
    if (v == null)
      return true;


    if (v.isAbstract())
      return true;

    if(v instanceof AssociationDef && isLightweightAssociation(((AssociationDef)v))){
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

  private boolean suppressViewableInAliasTable(Viewable v)
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



  private void declareTopic (Topic topic)
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
     // no LineForm at topic level
     //if(obj instanceof LineForm){
   }

	// member
	 ipw.println ("<xsd:complexType name=\""+getTransferName(topic)+"MemberType\">");
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
		 if ((obj instanceof Viewable) && !suppressViewableInTopic((Viewable) obj)) {
			 Viewable v = (Viewable) obj;
			 ipw.println("<xsd:element ref=\"" + getTransferName(v) + "\"/>");
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
	 // TODO: add owns fixed to "true" 
	 ipw.println ("</xsd:complexType>");
	
   ipw.println("<xsd:element name=\""+getTransferName(topic)+"\" type=\""+getTransferName(topic)+"Type\"/>");
    ipw.println ("<xsd:complexType name=\""+getTransferName(topic)+"Type\">");
	ipw.indent ();
	ipw.println ("<xsd:sequence>");
	ipw.indent ();
	ipw.println ("<xsd:element name=\"member\" type=\""+getTransferName(topic)+"MemberType\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>");
	ipw.unindent ();
	ipw.println ("</xsd:sequence>");
	ipw.println ("<xsd:attributeGroup ref=\"gml:AggregationAttributeGroup\"/>");
	ipw.unindent ();
	ipw.println ("</xsd:complexType>");

  }



  private void declareAbstractClassDef(Viewable v)
  {
  	String baseElement="gml:AbstractFeature";
  	Viewable baseType=(Viewable)v.getExtending();
  	if(baseType!=null){
  		baseElement=getTransferName(baseType);
  	}
	ipw.println("<xsd:element name=\""+getTransferName(v)+"\" type=\""+getTransferName(v)+"Type\" substitutionGroup=\""+baseElement+"\"/>");
	ipw.println("<xsd:complexType  name=\"" + getTransferName(v) + "Type\">");
	ipw.indent ();
	ipw.println ("<xsd:complexContent>");
  	ipw.indent ();
  	String baseXsdType="gml:AbstractFeatureType";
	if(baseType!=null){
		baseXsdType=getTransferName(baseType)+"Type";
	}
	ipw.println ("<xsd:extension base=\""+baseXsdType+"\">");
	ipw.indent ();
	ipw.println("<xsd:sequence>");
	ipw.indent();
	/* Find which attributes are going to be elements of this class. */
	Iterator iter = v.iterator();
	while (iter.hasNext()) {
		Object obj = iter.next();
		if (obj instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) obj;
			if(attr.getExtending()==null){
				// define only new attrs (==not EXTENDED)
				declareAttribute(attr);
			}
		}
		if(obj instanceof RoleDef){
			RoleDef role = (RoleDef) obj;

			// roledef not defined in a lightweight association?
			// lightweigth according to this encoding!
			if (!isLightweightAssociation((AssociationDef)v)){
				ipw.println(
					"<xsd:element name=\""
						+ getTransferName(role)
						+ "\" type=\"gml:ReferenceType\">");
				ipw.indent();
				ipw.println("<xsd:annotation>");
					ipw.indent();
					ipw.println("<xsd:appinfo>");
						ipw.indent();
						ipw.println("<gml:targetElement>"+getTransferName(role.getDestination())+"</gml:targetElement>");
						ipw.unindent();
					ipw.println("</xsd:appinfo>");
					ipw.unindent();
				ipw.println("</xsd:annotation>");
   				//ipw.println("<!-- "+PRBLMTAG+" unable to express ordering kind -->");
				//ipw.println("<!-- "+PRBLMTAG+" unable to express aggregation kind -->");
				//ipw.println("<!-- "+PRBLMTAG+" unable to express cardinality -->");
				ipw.unindent();
				ipw.println("</xsd:element>");
			}
		}
	}
	// TODO implement lightweight associations
	ipw.unindent();
	ipw.println("</xsd:sequence>");
	ipw.unindent();
	ipw.println("</xsd:extension>");
	ipw.unindent();
	ipw.println("</xsd:complexContent>");
	ipw.unindent();
	ipw.println("</xsd:complexType>");

  }

  private boolean isLightweightAssociation(AssociationDef v)
  {
  	return false;
  }
  private void declareDomainDef (Domain domain)
  {
  	Type type=domain.getType();
	//if (type instanceof CoordType){
	//}else if (type instanceof PolylineType){
	//}else if (type instanceof SurfaceOrAreaType){
	//}  	
    declareType(type,domain);
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
    	Type realType=realDomain.getType();
        if(realDomain==td.INTERLIS.BOOLEAN){
      	  ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"xsd:boolean\""+minOccurs+"/>");
        }else{
      	  ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\""+getTransferName(((TypeAlias)type).getAliasing())+"\""+minOccurs+"/>");
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
				ipw.println ("<xsd:sequence>");
					ipw.indent ();
					ipw.println ("<xsd:element ref=\""+getTransferName(part)+"\"/>");
					ipw.unindent ();
				ipw.println ("</xsd:sequence>");
				ipw.unindent ();
			ipw.println ("</xsd:element>");
		}else if (type instanceof ReferenceType){
			ipw.println ("<xsd:element name=\""+getTransferName(attribute)+"\" type=\"gml:ReferenceType\""+minOccurs+">");
				ipw.indent ();
				ipw.println("<xsd:annotation>");
					ipw.indent ();
					ipw.println("<xsd:appinfo>");
						ipw.indent ();
						ipw.println("<gml:targetElement>"+getTransferName(((ReferenceType)type).getReferred())+"</gml:targetElement>");
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
      typeName=" name=\""+getTransferName(domain)+"\"";
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
				base=getTransferName(domain.getExtending());
			}else{
				base="gml:PointPropertyType";
			}
			ipw.println ("<xsd:restriction base=\""+base+"\">");
			ipw.indent ();
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
        if(domain!=null && domain.isFinal()){
          ipw.indent ();
          java.util.ArrayList ev=new java.util.ArrayList();
          buildEnumList(ev,"",((EnumerationType)type).getConsolidatedEnumeration());
          Iterator iter=ev.iterator();
          while(iter.hasNext()){
            String value=(String)iter.next();
            ipw.println ("<xsd:enumeration value=\""+value+"\"/>");
          }
          ipw.unindent ();
		}else{
			//ipw.println("<!-- "+PRBLMTAG+" unable to express elements of an extendable enumeration -->");
		}
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(type instanceof NumericType){
      ipw.println ("<xsd:simpleType"+typeName+">");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:double\">");
        if(!type.isAbstract()){
          ipw.indent ();
          ipw.println ("<xsd:minInclusive value=\""+((NumericType)type).getMinimum().doubleValue()+"\"/>");
          ipw.println ("<xsd:maxInclusive value=\""+((NumericType)type).getMaximum().doubleValue()+"\"/>");
          ipw.unindent ();
        }
        ipw.println ("</xsd:restriction>");
        ipw.unindent ();
      ipw.println ("</xsd:simpleType>");
    }else if(type instanceof TextType){
      ipw.println ("<xsd:simpleType"+typeName+">");
        ipw.indent ();
        ipw.println ("<xsd:restriction base=\"xsd:normalizedString\">");
        if(((TextType)type).getMaxLength()>0){
          ipw.indent ();
          ipw.println ("<xsd:maxLength value=\""+((TextType)type).getMaxLength()+"\"/>");
          ipw.unindent ();
		}
        ipw.println ("</xsd:restriction>");
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
		base=getTransferName(domain.getExtending());
	}else{
		if (type instanceof SurfaceOrAreaType){
			base="gml:SurfacePropertyType";
		}else{
			base="gml:CurvePropertyType";
		}
	}
	ipw.println ("<xsd:restriction base=\""+base+"\">");
	ipw.indent ();
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
	if (type instanceof SurfaceOrAreaType){
	    Table part=((SurfaceOrAreaType)type).getLineAttributeStructure();
	    if(part!=null){
          addCurveSegmentWithLineAttr(type);
	    }
	}
  }
  private void declareLineForm(LineForm form)
  {
  }
  private HashSet lineAttrs=new HashSet();
  private void addCurveSegmentWithLineAttr(LineType domain){
  	
  }
  private void declareCurveSegmentWithLineAttr(){
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
