package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitMetadataMapping implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".MetadataMapping";
		VisitTransferDescription.MetadataMapping obj=(VisitTransferDescription.MetadataMapping)obj1;
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<qualifiedBasketname>"+cb.encodeString(obj.basketname)+"</qualifiedBasketname>");
		out.write("<boid>"+cb.encodeString(obj.boid)+"</boid>");
		out.write("<TransferDescription REF=\""+cb.encodeOid(cb.getobjid(obj.td))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".MetadataMapping";
		VisitTransferDescription.MetadataMapping obj=(VisitTransferDescription.MetadataMapping)obj1;

		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"qualifiedBasketname\"),X(\""+obj.basketname+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"boid\"),X(\""+obj.boid+"\"));"+cb.newline());

		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".TransferDescriptionBasketname2boid"+"\"));"+cb.newline());
		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.td)+"\"));"+cb.newline());
		out.write("link->setLinkEnd(ParserHandler::getTagId(\"basketname2boid\"),obj);"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"TransferDescription\"),objref);"+cb.newline());
		out.write("metamodel->addObject(link);"+cb.newline());
		
		out.write("metamodel->addObject(obj);"+cb.newline());

	}
}
