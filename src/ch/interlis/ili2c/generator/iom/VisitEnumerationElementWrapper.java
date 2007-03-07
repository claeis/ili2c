package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitEnumerationElementWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		EnumerationElementWrapper obj=(EnumerationElementWrapper)obj1;
		Object refobj=obj.element.getSubEnumeration();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		EnumerationElementWrapper obj=(EnumerationElementWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Enumeration_Element";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.element.getName())+"</name>");
		out.write("<enumeration REF=\""+cb.encodeOid(cb.getobjid(obj.enumeration))
			+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderPos)+"\"/>");
		if(obj.element.getSubEnumeration()!=null){
			out.write("<subEnumeration REF=\""+cb.encodeOid(cb.getobjid(obj.element.getSubEnumeration()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
