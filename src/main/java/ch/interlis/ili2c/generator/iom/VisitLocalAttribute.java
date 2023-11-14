package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitLocalAttribute implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		LocalAttribute obj=(LocalAttribute)obj1;
		Iterator elei;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getDomain();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Object refobjv[]=obj.getBasePaths();
		if(refobjv!=null){
			for(int i=0;i<refobjv.length;i++){
				cb.addPendingObject(refobjv[i]);
			}
		}
		refobj=obj.getDomain();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LocalAttribute obj=(LocalAttribute)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".LocalAttribute";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		if(obj.getDomain()!=null){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getDomain()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LocalAttribute obj=(LocalAttribute)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".LocalAttribute";

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

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isFinal\"),X(\""+cb.encodeBoolean(obj.isFinal())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isAbstract\"),X(\""+cb.encodeBoolean(obj.isAbstract())+"\"));"+cb.newline());
		
		
		if(obj.getExtending()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ExtendedByBaseAttributeDef"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getExtending())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"extendedBy\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"base\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		if(obj.getDomain()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".TypedAttributeType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getDomain())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"typedAttribute\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"type\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		out.write("metamodel->addObject(obj);"+cb.newline());
	}
}
