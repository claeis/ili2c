package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitComposedUnit_ComposedWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ComposedUnit_ComposedWrapper obj=(ComposedUnit_ComposedWrapper)obj1;
		Object refobj=obj.factor.getUnit();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ComposedUnit_ComposedWrapper obj=(ComposedUnit_ComposedWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".ComposedUnit_Composed";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<operator>"+encodeOperator(obj.factor.getCompositionOperator())+"</operator>");
		out.write("<composedUnit REF=\""+cb.encodeOid(cb.getobjid(obj.composedUnit))+"\"/>");
		out.write("<unit REF=\""+cb.encodeOid(cb.getobjid(obj.factor.getUnit()))+"\"/>");
		out.write("</"+tag+">");
	}
	private String encodeOperator(char op){
		String ret="times";
		if(op=='/'){
			ret="over";
		}
		return ret;
	}
}
