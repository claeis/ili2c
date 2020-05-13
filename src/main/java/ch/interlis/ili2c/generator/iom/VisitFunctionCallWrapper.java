package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitFunctionCallWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		FunctionCallWrapper obj=(FunctionCallWrapper)obj1;
		EvaluableWrapper.visitObject(obj,cb);
		Object refobj;
		refobj=obj.fc.getFunction();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Evaluable argv[]=obj.fc.getArguments();
		for(int i=0;i<argv.length;i++){
			cb.addPendingObject(EvaluableWrapper.createFunctionCall(obj,argv[i],i+1));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		FunctionCallWrapper obj=(FunctionCallWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".FunctionCall";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		EvaluableWrapper.writeObject(out,obj,cb);
		Object refobj=obj.fc.getFunction();
		if(refobj!=null){
			out.write("<FunctionDef REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		out.write("</"+tag+">");
	}

}
