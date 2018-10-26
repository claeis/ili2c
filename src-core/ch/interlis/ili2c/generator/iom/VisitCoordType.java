package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitCoordType implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		CoordType obj=(CoordType)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		NumericalType dimensions[]=obj.getDimensions();
		for(int i=0;i<dimensions.length;i++){
			cb.addPendingObject(new CoordTypeDimensionWrapper(obj,dimensions[i],i+1));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		CoordType obj=(CoordType)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".CoordType";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		out.write("<nullAxis>"+cb.encodeInteger(obj.getNullAxis())+"</nullAxis>");
		out.write("<piHalfAxis>"+cb.encodeInteger(obj.getPiHalfAxis())+"</piHalfAxis>");
		out.write("</"+tag+">");
	}
}
