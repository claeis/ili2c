package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitModel implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Model obj=(Model)obj1;
		Iterator elei=obj.iterator();
		while(elei.hasNext()){
			Object refobj=elei.next();
			cb.addPendingObject(refobj);
		}
		// TODO contracts
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Model obj=(Model)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof PredefinedModel){
			tag=tag+".PredefinedModel";
		}else if(obj instanceof RefSystemModel){
			tag=tag+".RefSystemModel";
		}else if(obj instanceof SymbologyModel){
			tag=tag+".SymbologyModel";
		}else if(obj instanceof TypeModel){
			tag=tag+".TypeModel";
		}else{
			tag=tag+".DataModel";
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		if(obj.getFileName()!=null){
			out.write("<filename>"+cb.encodeString(obj.getFileName())+"</filename>");
		}
		if(obj.getLanguage()!=null){
			out.write("<language>"+cb.encodeString(obj.getLanguage())+"</language>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Model obj=(Model)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix;
		if(obj instanceof PredefinedModel){
			tag=tag+".PredefinedModel";
		}else if(obj instanceof RefSystemModel){
			tag=tag+".RefSystemModel";
		}else if(obj instanceof SymbologyModel){
			tag=tag+".SymbologyModel";
		}else if(obj instanceof TypeModel){
			tag=tag+".TypeModel";
		}else{
			tag=tag+".DataModel";
		}
		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"name\"),X(\""+obj.getName()+"\"));"+cb.newline());

		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ContainerElements"+"\"));"+cb.newline());
		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.getContainer())+"\"));"+cb.newline());
		out.write("link->setLinkEnd(ParserHandler::getTagId(\"elements\"),obj);"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"container\"),objref);"+cb.newline());
		out.write("metamodel->addObject(link);"+cb.newline());

		if(obj.getFileName()!=null){
			out.write("obj->setAttrValue(ParserHandler::getTagId(\"filename\"),X(\""+cb.encodeString(obj.getFileName())+"\"));"+cb.newline());
		}
		
		if(obj.getLanguage()!=null){
			out.write("obj->setAttrValue(ParserHandler::getTagId(\"language\"),X(\""+cb.encodeString(obj.getLanguage())+"\"));"+cb.newline());
		}
		
		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
