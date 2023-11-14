package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitPathElWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		PathElWrapper obj=(PathElWrapper)obj1;
		Object refobj=null;
		if(obj.element instanceof AttributeRef){
			refobj=((AttributeRef)obj.element).getAttr();
		}else if(obj.element instanceof AxisAttributeRef){
			refobj=((AxisAttributeRef)obj.element).getAttr();			
		}else if(obj.element instanceof StructAttributeRef){
			refobj=((StructAttributeRef)obj.element).getAttr();			
		}else if(obj.element instanceof AssociationPath){
			refobj=((AssociationPath)obj.element).getTargetRole();			
		}else if(obj.element instanceof PathElAbstractClassRole){
			refobj=((PathElAbstractClassRole)obj.element).getRole();			
		}else if(obj.element instanceof PathElAssocRole){
			refobj=((PathElAssocRole)obj.element).getRole();			
		}else if(obj.element instanceof PathElRefAttr){
			refobj=((PathElRefAttr)obj.element).getAttr();			
		}
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		PathElWrapper obj=(PathElWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj.element instanceof AggregationRef){
			tag=tag+".AggregationRef";
		}else if(obj.element instanceof AttributeRef){
			tag=tag+".AttributeRef";
		}else if(obj.element instanceof AxisAttributeRef){
			tag=tag+".AxisAttributeRef";
		}else if(obj.element instanceof StructAttributeRef){
			tag=tag+".StructAttributeRef";
		}else if(obj.element instanceof AssociationPath){
			tag=tag+".AssociationPath";
		}else if(obj.element instanceof PathElAbstractClassRole){
			tag=tag+".PathElAbstractClassRole";
		}else if(obj.element instanceof PathElAssocRole){
			tag=tag+".PathElAssocRole";
		}else if(obj.element instanceof PathElBase){
			tag=tag+".PathElBase";
		}else if(obj.element instanceof PathElParent){
			tag=tag+".PathElParent";
		}else if(obj.element instanceof PathElRefAttr){
			tag=tag+".PathElRefAttr";
		}else if(obj.element instanceof PathElThis){
			tag=tag+".PathElThis";
		}else if(obj.element instanceof ThisArea){
			tag=tag+".ThisArea";
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");

		Object refobj=obj.objectPath;
		out.write("<ObjectPath REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderPos)+"\"/>");
		if(obj.element instanceof AttributeRef){
			refobj=((AttributeRef)obj.element).getAttr();
			out.write("<attr REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof AxisAttributeRef){
			out.write("<axis>"+cb.encodeInteger(((AxisAttributeRef)obj.element).getAxisNumber())+"</axis>");
			refobj=((AxisAttributeRef)obj.element).getAttr();			
			out.write("<attr REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof StructAttributeRef){
			out.write("<index>"+cb.encodeLong(((StructAttributeRef)obj.element).getIndex())+"</index>");
			refobj=((StructAttributeRef)obj.element).getAttr();			
			out.write("<attr REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof AssociationPath){
			refobj=((AssociationPath)obj.element).getTargetRole();			
			out.write("<targetRole REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof PathElAbstractClassRole){
			refobj=((PathElAbstractClassRole)obj.element).getRole();			
			out.write("<role REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof PathElAssocRole){
			refobj=((PathElAssocRole)obj.element).getRole();			
			out.write("<role REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof PathElBase){
			out.write("<baseName>"+cb.encodeString(((PathElBase)obj.element).getName())+"</baseName>");
		}else if(obj.element instanceof PathElRefAttr){
			refobj=((PathElRefAttr)obj.element).getAttr();			
			out.write("<attr REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
		}else if(obj.element instanceof ThisArea){
			out.write("<isThat>"+cb.encodeBoolean(((ThisArea)obj.element).isThat())+"</isThat>");
		}

		out.write("</"+tag+">");
	}

}
