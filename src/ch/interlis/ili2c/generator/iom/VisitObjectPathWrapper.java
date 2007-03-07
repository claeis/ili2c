package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitObjectPathWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ObjectPathWrapper obj=(ObjectPathWrapper)obj1;
		EvaluableWrapper.visitObject(obj,cb);
		Object refobj=obj.uniquenessConstraint;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.prefixedUniquenessConstraint;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.existenceConstraintBase;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.existenceConstraintReferrer;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		PathEl elev[]=obj.objectpath.getPathElements();
		for(int i=0;i<elev.length;i++){
			cb.addPendingObject(new PathElWrapper(obj,elev[i],i+1));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ObjectPathWrapper obj=(ObjectPathWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".ObjectPath";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		EvaluableWrapper.writeObject(out,obj,cb);
		Object refobj=obj.uniquenessConstraint;
		if(refobj!=null){
			out.write("<UniquenessConstraint REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderPos)+"\"/>");
		}
		refobj=obj.prefixedUniquenessConstraint;
		if(refobj!=null){
			out.write("<prefixedUniquenessConstraint REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		refobj=obj.existenceConstraintBase;
		if(refobj!=null){
			out.write("<existenceConstraintBase REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		refobj=obj.existenceConstraintReferrer;
		if(refobj!=null){
			out.write("<existenceConstraintReferrer REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		out.write("</"+tag+">");
	}

}
