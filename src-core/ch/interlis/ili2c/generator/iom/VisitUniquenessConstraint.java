package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitUniquenessConstraint implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		UniquenessConstraint obj=(UniquenessConstraint)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getPrefix();
		if(refobj!=null){
			ObjectPathWrapper objpath=new ObjectPathWrapper((ObjectPath)refobj);
			objpath.prefixedUniquenessConstraint=obj;
			cb.addPendingObject(objpath);
		}
		Iterator attri=obj.getElements().iteratorAttribute();
		int orderPos=1;
		while(attri.hasNext()){
			refobj=attri.next();			
			ObjectPathWrapper objpath=new ObjectPathWrapper((ObjectPath)refobj);
			objpath.uniquenessConstraint=obj;
			objpath.orderPos=orderPos++;
			cb.addPendingObject(objpath);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		UniquenessConstraint obj=(UniquenessConstraint)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".UniquenessConstraint";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("<isLocal>"+cb.encodeBoolean(obj.getLocal())+"</isLocal>");
		out.write("</"+tag+">");
	}

}
