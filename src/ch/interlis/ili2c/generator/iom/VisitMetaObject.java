package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitMetaObject implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		MetaObject obj=(MetaObject)obj1;
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		MetaObject obj=(MetaObject)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".MetaObject";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("</"+tag+">");
	}
}
