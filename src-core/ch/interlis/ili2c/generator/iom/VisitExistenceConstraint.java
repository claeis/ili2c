package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitExistenceConstraint implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ExistenceConstraint obj=(ExistenceConstraint)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getRestrictedAttribute();
		if(refobj!=null){
			ObjectPathWrapper objpath=new ObjectPathWrapper((ObjectPath)refobj);
			objpath.existenceConstraintBase=obj;
			cb.addPendingObject(objpath);
		}
		Iterator attri=obj.iteratorRequiredIn();
		while(attri.hasNext()){
			refobj=attri.next();			
			ObjectPathWrapper objpath=new ObjectPathWrapper((ObjectPath)refobj);
			objpath.existenceConstraintReferrer=obj;
			cb.addPendingObject(objpath);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ExistenceConstraint obj=(ExistenceConstraint)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".ExistenceConstraint";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("</"+tag+">");
	}

}
