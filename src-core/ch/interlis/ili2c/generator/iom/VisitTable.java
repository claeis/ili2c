package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitTable implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Table obj=(Table)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Iterator elei=obj.iterator();
		while(elei.hasNext()){
			refobj=elei.next();
			cb.addPendingObject(refobj);
		}
		refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Iterator attri=obj.getAttributesAndRoles2();
		int orderPos=1;
		while(attri.hasNext()){
			ViewableTransferElement attr=(ViewableTransferElement)attri.next();			
			cb.addPendingObject(new ViewableAttributesAndRoles(obj,(AbstractLeafElement)attr.obj,orderPos));
			orderPos++;
		}
		Iterator rolei=obj.getLightweightAssociations().iterator();
		while(rolei.hasNext()){
			RoleDef role=(RoleDef)rolei.next();			
			cb.addPendingObject(new TargetLightweightAssociation(obj,role));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Table obj=(Table)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Table";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		out.write("<isExtended>"+cb.encodeBoolean(obj.isExtended())+"</isExtended>");
		out.write("<isStructure>"+cb.encodeBoolean(!obj.isIdentifiable())+"</isStructure>");
		out.write("</"+tag+">");
	}
}
