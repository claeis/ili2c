package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitTransferDescription implements Visitor, ObjWriter {
	public class MetadataMapping {
		public String basketname;
		public String boid;
		public TransferDescription td;
		public MetadataMapping(String basketname,String boid,TransferDescription td){
			this.basketname=basketname;
			this.boid=boid;
			this.td=td;
		}
	}
	public void visitObject(Object obj1, VisitorCallback cb) {
		TransferDescription obj=(TransferDescription)obj1;
		Iterator elei=obj.iterator();
		while(elei.hasNext()){
			Object refobj=elei.next();
			cb.addPendingObject(refobj);
		}
		elei=obj.iteratorMetaDataContainer();
		while(elei.hasNext()){
			Object refobj=elei.next();
			cb.addPendingObject(refobj);
		}
		HashMap map=obj.getBasketname2boid();
		elei=map.keySet().iterator();
		while(elei.hasNext()){
			String basketname=(String)elei.next();
			String boid=(String)map.get(basketname);
			cb.addPendingObject(new MetadataMapping(basketname,boid,obj));
		}
		Object refobj=obj.getIli1Format();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		TransferDescription obj=(TransferDescription)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".TransferDescription";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		Object refobj=obj.getIli1Format();
		if(refobj!=null){
			out.write("<ili1Format REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}
		out.write("</"+tag+">");
	}
}
