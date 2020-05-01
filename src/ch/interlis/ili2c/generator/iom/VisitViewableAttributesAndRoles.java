package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION ViewableAttributesAndRoles =
      viewable -- {0..*} Viewable;
      attributesAndRoles -- {0..*} AbstractLeafElement;
    END ViewableAttributesAndRoles;
 */
public class VisitViewableAttributesAndRoles implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ViewableAttributesAndRoles obj=(ViewableAttributesAndRoles)obj1;
		cb.addPendingObject(obj.viewable);
		cb.addPendingObject(obj.attributesAndRoles);
	}
	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ViewableAttributesAndRoles obj=(ViewableAttributesAndRoles)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".ViewableAttributesAndRoles";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<viewable REF=\""+cb.encodeOid(cb.getobjid(obj.viewable))+"\"/>");
		out.write("<attributesAndRoles REF=\""+cb.encodeOid(cb.getobjid(obj.attributesAndRoles))+"\" ORDER_POS=\""+cb.encodeInteger(obj.orderPos)+"\"/>");
		out.write("</"+tag+">");
	}
}
