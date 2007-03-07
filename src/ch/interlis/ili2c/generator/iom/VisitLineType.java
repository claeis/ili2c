package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitLineType implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		LineType obj=(LineType)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getControlPointDomain();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		LineForm lfv[]=obj.getLineForms();
		for(int i=0;i<lfv.length;i++){
			cb.addPendingObject(new LineTypeLineForm(obj,lfv[i]));
		}
		if(obj instanceof SurfaceOrAreaType){
			refobj=((SurfaceOrAreaType)obj).getLineAttributeStructure();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LineType obj=(LineType)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof PolylineType){
			tag+=".PolylineType";
		}else if(obj instanceof SurfaceType){
			tag+=".SurfaceType";
		}else if(obj instanceof AreaType){
			tag+=".AreaType";
		}else{
			throw new IllegalArgumentException();
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		if(obj.getMaxOverlap()!=null){
			out.write("<maxOverlap>"+cb.encodePrecisionDecimal(obj.getMaxOverlap())+"</maxOverlap>");
		}

		if(obj.getControlPointDomain()!=null){
			out.write("<controlPointDomain REF=\""+cb.encodeOid(cb.getobjid(obj.getControlPointDomain()))+"\"/>");
		}
		if(obj instanceof PolylineType){
			out.write("<isDirected>"+cb.encodeBoolean(((PolylineType)obj).isDirected())+"</isDirected>");
		}else{
			if(((SurfaceOrAreaType)obj).getLineAttributeStructure()!=null){
				out.write("<lineAttributeStructure REF=\""+cb.encodeOid(cb.getobjid(((SurfaceOrAreaType)obj).getLineAttributeStructure()))+"\"/>");
			}
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LineType obj=(LineType)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix;
		if(obj instanceof PolylineType){
			tag+=".PolylineType";
		}else if(obj instanceof SurfaceType){
			tag+=".SurfaceType";
		}else if(obj instanceof AreaType){
			tag+=".AreaType";
		}else{
			throw new IllegalArgumentException();
		}
		
		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isMandatory\"),X(\""+cb.encodeBoolean(obj.isMandatory())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isAbstract\"),X(\""+cb.encodeBoolean(obj.isAbstract())+"\"));"+cb.newline());		
		
		if(obj.getExtending()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ExtendedByBaseType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getExtending())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"extendedBy\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"base\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		if(obj.getMaxOverlap()!=null){
			out.write("obj->setAttrValue(ParserHandler::getTagId(\"maxOverlap\"),X(\""+cb.encodePrecisionDecimal(obj.getMaxOverlap())+"\"));"+cb.newline());		
		}
		
		if(obj.getControlPointDomain()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".LineTypeControlPointDomain"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getControlPointDomain())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"lineType\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"controlPointDomain\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		if(obj instanceof PolylineType){
			out.write("obj->setAttrValue(ParserHandler::getTagId(\"isDirected\"),X(\""+cb.encodeBoolean(((PolylineType)obj).isDirected())+"\"));"+cb.newline());		
		}else{
			if(((SurfaceOrAreaType)obj).getLineAttributeStructure()!=null){
				out.write("<lineAttributeStructure REF=\""+cb.encodeOid(cb.getobjid(((SurfaceOrAreaType)obj).getLineAttributeStructure()))+"\"/>");
				out.write("link=new iom_object(true);"+cb.newline());
				out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".LineAttributeStructureSurfaceOrAreaType"+"\"));"+cb.newline());
				out.write("objref=new iom_objref();"+cb.newline());
				out.write("objref->setOid(X(\""+cb.getobjid(((SurfaceOrAreaType)obj).getLineAttributeStructure())+"\"));"+cb.newline());
				out.write("link->setLinkEnd(ParserHandler::getTagId(\"surfaceOrAreaType\"),obj);"+cb.newline());
				out.write("link->setLinkEndR(ParserHandler::getTagId(\"lineAttributeStructure\"),objref);"+cb.newline());
				out.write("metamodel->addObject(link);"+cb.newline());
			}
		}
		
		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
