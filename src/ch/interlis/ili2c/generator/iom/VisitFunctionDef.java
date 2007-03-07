package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitFunctionDef implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Function obj=(Function)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getDomain();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		FormalArgument argv[]=obj.getArguments();
		for(int i=0;i<argv.length;i++){
			cb.addPendingObject(new FormalArgumentWrapper(obj,argv[i],i+1));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Function obj=(Function)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".FunctionDef";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		String explanation=obj.getExplanation();
		if(explanation!=null){
			out.write("<explanation>"+cb.encodeString(explanation)+"</explanation>");
		}
		if(obj.getDomain()!=null){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getDomain()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
