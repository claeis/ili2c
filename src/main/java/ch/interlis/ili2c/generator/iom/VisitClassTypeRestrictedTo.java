package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitClassTypeRestrictedTo implements Visitor, ObjWriter{
	public void visitObject(Object obj1, VisitorCallback cb) {
		ClassTypeRestrictedTo obj=(ClassTypeRestrictedTo)obj1;
		cb.addPendingObject(obj.classType);
		cb.addPendingObject(obj.restrictedTo);
	}
	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ClassTypeRestrictedTo obj=(ClassTypeRestrictedTo)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".ClassTypeRestrictedTo";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<classType REF=\""+cb.encodeOid(cb.getobjid(obj.classType))+"\"/>");
		out.write("<restrictedTo REF=\""+cb.encodeOid(cb.getobjid(obj.restrictedTo))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ClassTypeRestrictedTo obj=(ClassTypeRestrictedTo)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".ClassTypeRestrictedTo";
		
		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.classType)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"classType\"),objref);"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.restrictedTo)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"restrictedTo\"),objref);"+cb.newline());

		out.write("metamodel->addObject(link);"+cb.newline());
		
	}

}
