package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitStructuredUnit implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		StructuredUnit obj=(StructuredUnit)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getFirstUnit();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		StructuredUnit.Part partv[]=obj.getParts();
		for(int i=0;i<partv.length;i++){
			cb.addPendingObject(new StructuredUnit_PartWrapper(obj,partv[i],i+1));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		StructuredUnit obj=(StructuredUnit)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".StructuredUnit";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		String docName=obj.getDocName();
		if(docName!=null){
			out.write("<docName>"+cb.encodeString(docName)+"</docName>");
		}
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		out.write("<isContinuous>"+cb.encodeBoolean(obj.isContinuous())+"</isContinuous>");
		out.write("<firstPart REF=\""+cb.encodeOid(cb.getobjid(obj.getFirstUnit()))+"\"/>");
		out.write("</"+tag+">");
	}
}
