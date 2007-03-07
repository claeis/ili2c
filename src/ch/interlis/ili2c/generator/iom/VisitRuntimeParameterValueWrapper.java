package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitRuntimeParameterValueWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		RuntimeParameterValueWrapper obj=(RuntimeParameterValueWrapper)obj1;
		Object refobj;
		EvaluableWrapper.visitObject(obj,cb);
		refobj=obj.value.getParameter();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		RuntimeParameterValueWrapper obj=(RuntimeParameterValueWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".RuntimeParameterValue";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		EvaluableWrapper.writeObject(out,obj,cb);
		Object refobj=obj.value.getParameter();
		if(refobj!=null){
			out.write("<paramDef REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
