package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitRefSystemRef implements Visitor, ObjWriter {
	public void visitObject(Object obj, VisitorCallback cb) {
		if(obj instanceof RefSystemRef.CoordDomain){
			Object refobj=((RefSystemRef.CoordDomain)obj).getReferredDomain();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}else if(obj instanceof RefSystemRef.CoordDomainAxis){
			Object refobj=((RefSystemRef.CoordDomainAxis)obj).getReferredDomain();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}else if(obj instanceof RefSystemRef.CoordSystem){
			Object refobj=((RefSystemRef.CoordSystem)obj).getSystem();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}else if(obj instanceof RefSystemRef.CoordSystemAxis){
			Object refobj=((RefSystemRef.CoordSystemAxis)obj).getSystem();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}else{
			throw new IllegalArgumentException();
		}
	}

	public void writeObject(Writer out, Object obj, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj);
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof RefSystemRef.CoordDomain){
			tag=tag+".RefSystemRef_CoordDomain";
		}else if(obj instanceof RefSystemRef.CoordDomainAxis){
			tag=tag+".RefSystemRef_CoordDomainAxis";
		}else if(obj instanceof RefSystemRef.CoordSystem){
			tag=tag+".RefSystemRef_CoordSystem";
		}else if(obj instanceof RefSystemRef.CoordSystemAxis){
			tag=tag+".RefSystemRef_CoordSystemAxis";
		}else{
			throw new IllegalArgumentException();
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		if(obj instanceof RefSystemRef.CoordDomain){
			RefSystemRef.CoordDomain refsys=((RefSystemRef.CoordDomain)obj);
			out.write("<referredDomain REF=\""+cb.encodeOid(cb.getobjid(refsys.getReferredDomain()))+"\"/>");
		}else if(obj instanceof RefSystemRef.CoordDomainAxis){
			RefSystemRef.CoordDomainAxis refsys=((RefSystemRef.CoordDomainAxis)obj);
			out.write("<axisNumber>"+cb.encodeInteger(refsys.getAxisNumber())+"</axisNumber>");
			out.write("<referredDomain REF=\""+cb.encodeOid(cb.getobjid(refsys.getReferredDomain()))+"\"/>");
		}else if(obj instanceof RefSystemRef.CoordSystem){
			RefSystemRef.CoordSystem refsys=((RefSystemRef.CoordSystem)obj);
			out.write("<system REF=\""+cb.encodeOid(cb.getobjid(refsys.getSystem()))+"\"/>");
		}else if(obj instanceof RefSystemRef.CoordSystemAxis){
			RefSystemRef.CoordSystemAxis refsys=((RefSystemRef.CoordSystemAxis)obj);
			out.write("<axisNumber>"+cb.encodeInteger(refsys.getAxisNumber())+"</axisNumber>");
			out.write("<system REF=\""+cb.encodeOid(cb.getobjid(refsys.getSystem()))+"\"/>");
		}else{
			throw new IllegalArgumentException();
		}
		out.write("</"+tag+">");
	}
}
