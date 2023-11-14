package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitDerivedUnit implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		DerivedUnit obj=(DerivedUnit)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		if(obj instanceof NumericallyDerivedUnit){
			NumericallyDerivedUnit.Factor factorv[]=((NumericallyDerivedUnit)obj).getConversionFactors();
			for(int i=0;i<factorv.length;i++){
				cb.addPendingObject(new NumericallyDerivedUnit_FactorWrapper((NumericallyDerivedUnit)obj,factorv[i]));
			}
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		DerivedUnit obj=(DerivedUnit)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof FunctionallyDerivedUnit){
			tag=tag+".FunctionallyDerivedUnit";
		}else if(obj instanceof NumericallyDerivedUnit){
			tag=tag+".NumericallyDerivedUnit";
		}else{
			tag=tag+".DerivedUnit";
		}
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
		if(obj instanceof FunctionallyDerivedUnit){
			out.write("<explanation>"+cb.encodeString(((FunctionallyDerivedUnit)obj).getExplanation())+"</explanation>");
		}
		out.write("</"+tag+">");
	}
}
