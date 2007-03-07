package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitTypeAlias implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		TypeAlias obj=(TypeAlias)obj1;
		Object refobj=obj.getAliasing();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		TypeAlias obj=(TypeAlias)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".TypeAlias";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		if(obj.getAliasing()!=null){
			out.write("<aliasing REF=\""+cb.encodeOid(cb.getobjid(obj.getAliasing()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		TypeAlias obj=(TypeAlias)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".TypeAlias";
		
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

		if(obj.getAliasing()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".AliasingTypeAlias"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getAliasing())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"typeAlias\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"aliasing\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}
		
		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
