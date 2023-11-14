package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitEnumeration implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Enumeration obj=(Enumeration)obj1;
		Iterator elei=obj.getElements();
		int orderPos=1;
		while(elei.hasNext()){
			Enumeration.Element refobj=(Enumeration.Element)elei.next();
			cb.addPendingObject(new EnumerationElementWrapper(obj,refobj,orderPos));
			orderPos++;
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Enumeration obj=(Enumeration)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Enumeration";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		out.write("</"+tag+">");
	}
}
