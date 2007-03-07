package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitStructuredUnit_PartWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		StructuredUnit_PartWrapper obj=(StructuredUnit_PartWrapper)obj1;
		Object refobj;
		refobj=obj.part.getUnit();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		StructuredUnit_PartWrapper obj=(StructuredUnit_PartWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".StructuredUnit_Part";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<min>"+cb.encodePrecisionDecimal(obj.part.getMinimum())+"</min>");
		out.write("<max>"+cb.encodePrecisionDecimal(obj.part.getMaximum())+"</max>");
		out.write("<structuredUnit REF=\""+cb.encodeOid(cb.getobjid(obj.structuredUnit))
			+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderpos)+"\"/>");
		out.write("<unit REF=\""+cb.encodeOid(cb.getobjid(obj.part.getUnit()))+"\"/>");
		out.write("</"+tag+">");
	}
}
