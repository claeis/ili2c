package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitLineTypeLineForm implements Visitor, ObjWriter{
	public void visitObject(Object obj1, VisitorCallback cb) {
		LineTypeLineForm obj=(LineTypeLineForm)obj1;
		cb.addPendingObject(obj.lineType);
		cb.addPendingObject(obj.lineForm);
	}
	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LineTypeLineForm obj=(LineTypeLineForm)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".LineTypeLineForm";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<lineType REF=\""+cb.encodeOid(cb.getobjid(obj.lineType))+"\"/>");
		out.write("<lineForm REF=\""+cb.encodeOid(cb.getobjid(obj.lineForm))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		LineTypeLineForm obj=(LineTypeLineForm)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".LineTypeLineForm";
		
		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.lineType)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"lineType\"),objref);"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.lineForm)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"lineForm\"),objref);"+cb.newline());

		out.write("metamodel->addObject(link);"+cb.newline());
		
	}

}
