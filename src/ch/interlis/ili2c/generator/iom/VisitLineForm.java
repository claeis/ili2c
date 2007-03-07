package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitLineForm implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		LineForm obj=(LineForm)obj1;
		Object refobj=obj.getSegmentStructure();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
			String oid=cb.getobjid(obj1);
			LineForm obj=(LineForm)obj1;
			String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".LineForm";
			out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
			out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
			out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
			if(obj.getExplanation()!=null){
				out.write("<explanation>"+cb.encodeString(obj.getExplanation())+"</explanation>");
			}
			if(obj.getSegmentStructure()!=null){
				out.write("<segmentStructure REF=\""+cb.encodeOid(cb.getobjid(obj.getSegmentStructure()))+"\"/>");
			}
			out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
			String oid=cb.getobjid(obj1);
			LineForm obj=(LineForm)obj1;
			String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
			String tag=tagPrefix+".LineForm";
		
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

			if(obj.getExplanation()!=null){
				out.write("obj->setAttrValue(ParserHandler::getTagId(\"explanation\"),X(\""+cb.encodeString(obj.getExplanation())+"\"));"+cb.newline());
			}		
		
			if(obj.getSegmentStructure()!=null){
				out.write("link=new iom_object(true);"+cb.newline());
				out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".SegmentStructureLineForm"+"\"));"+cb.newline());
				out.write("objref=new iom_objref();"+cb.newline());
				out.write("objref->setOid(X(\""+cb.getobjid(obj.getSegmentStructure())+"\"));"+cb.newline());
				out.write("link->setLinkEnd(ParserHandler::getTagId(\"lineForm\"),obj);"+cb.newline());
				out.write("link->setLinkEndR(ParserHandler::getTagId(\"segmentStructure\"),objref);"+cb.newline());
				out.write("metamodel->addObject(link);"+cb.newline());
			}
		
			out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
