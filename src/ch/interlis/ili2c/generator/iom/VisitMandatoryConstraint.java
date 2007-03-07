package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitMandatoryConstraint implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Constraint obj=(Constraint)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Evaluable eval=obj.getCondition();
		if(eval!=null){
			cb.addPendingObject(EvaluableWrapper.createMandatoryConstraint(obj,eval));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Constraint obj=(Constraint)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof PlausibilityConstraint){
			tag=tag+".PlausibilityConstraint";
		}else{
			tag=tag+".MandatoryConstraint";
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		if(obj instanceof PlausibilityConstraint){
			out.write("<direction>"+encodeDirection(((PlausibilityConstraint)obj).getDirection())+"</direction>");
			out.write("<percentage>"+cb.encodeDouble(((PlausibilityConstraint)obj).getPercentage())+"</percentage>");
		}
		out.write("</"+tag+">");
	}
	private String encodeDirection(int dir){
		String ret="atLeast";
		if(dir==PlausibilityConstraint.DIRECTION_AT_MOST){
			ret="atMost";
		}
		return ret;
		
	}
}
