package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitRestrictionOfCompTypeRestrictedTo implements Visitor, ObjWriter{
	public void visitObject(Object obj1, VisitorCallback cb) {
		RestrictionOfCompTypeRestrictedTo obj=(RestrictionOfCompTypeRestrictedTo)obj1;
		cb.addPendingObject(obj.restrictionOfCompType);
		cb.addPendingObject(obj.restrictedTo);
	}
	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		RestrictionOfCompTypeRestrictedTo obj=(RestrictionOfCompTypeRestrictedTo)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".RestrictionOfCompTypeRestrictedTo";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<restrictionOfCompType REF=\""+cb.encodeOid(cb.getobjid(obj.restrictionOfCompType))+"\"/>");
		out.write("<restrictedTo REF=\""+cb.encodeOid(cb.getobjid(obj.restrictedTo))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		RestrictionOfCompTypeRestrictedTo obj=(RestrictionOfCompTypeRestrictedTo)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".RestrictionOfCompTypeRestrictedTo";
		
		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.restrictionOfCompType)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"restrictionOfCompType\"),objref);"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.restrictedTo)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"restrictedTo\"),objref);"+cb.newline());

		out.write("metamodel->addObject(link);"+cb.newline());
		
	}

}
