package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitFormalArgumentWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		FormalArgumentWrapper obj=(FormalArgumentWrapper)obj1;
		Object refobj=obj.functionDef;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.argument.getType();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		FormalArgumentWrapper obj=(FormalArgumentWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".FormalArgument";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.argument.getName())+"</name>");
		out.write("<functionDef REF=\""+cb.encodeOid(cb.getobjid(obj.functionDef))+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderPos)+"\"/>");
		if(obj.argument.getType()!=null){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.argument.getType()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
