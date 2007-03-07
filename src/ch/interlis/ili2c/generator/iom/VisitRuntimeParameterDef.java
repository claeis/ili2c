package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitRuntimeParameterDef implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		GraphicParameterDef obj=(GraphicParameterDef)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getDomain();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		GraphicParameterDef obj=(GraphicParameterDef)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".RuntimeParameterDef";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		if(obj.getDomain()!=null){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getDomain()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
