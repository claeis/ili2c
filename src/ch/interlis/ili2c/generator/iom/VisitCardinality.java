package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitCardinality implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Cardinality obj=(Cardinality)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Cardinality";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<min>"+cb.encodeLong(obj.getMinimum())+"</min>");
		long max=obj.getMaximum();
		if(max!=Cardinality.UNBOUND){
			out.write("<max>"+cb.encodeLong(max)+"</max>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Cardinality obj=(Cardinality)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Cardinality";

		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"min\"),X(\""+cb.encodeLong(obj.getMinimum())+"\"));"+cb.newline());

		long max=obj.getMaximum();
		if(max!=Cardinality.UNBOUND){
			out.write("obj->setAttrValue(ParserHandler::getTagId(\"max\"),X(\""+cb.encodeLong(max)+"\"));"+cb.newline());
		}		
		
		out.write("metamodel->addObject(obj);"+cb.newline());
	}
}
