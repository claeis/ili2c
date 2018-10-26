package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
	ASSOCIATION TargetLightweightAssociation =
	  targetOfLightweightAssoc -- {0..*} AbstractClassDef;
	  lightweightAssociations -- {0..*} RoleDef;
	END TargetLightweightAssociation;
 */
public class VisitTargetLightweightAssociation implements Visitor, ObjWriter{
	public void visitObject(Object obj1, VisitorCallback cb) {
		TargetLightweightAssociation obj=(TargetLightweightAssociation)obj1;
		cb.addPendingObject(obj.targetOfLightweightAssoc);
		cb.addPendingObject(obj.lightweightAssociations);
	}
	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		TargetLightweightAssociation obj=(TargetLightweightAssociation)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".TargetLightweightAssociation";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<targetOfLightweightAssoc REF=\""+cb.encodeOid(cb.getobjid(obj.targetOfLightweightAssoc))+"\"/>");
		out.write("<lightweightAssociations REF=\""+cb.encodeOid(cb.getobjid(obj.lightweightAssociations))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		TargetLightweightAssociation obj=(TargetLightweightAssociation)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".TargetLightweightAssociation";
		
		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.targetOfLightweightAssoc)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"targetOfLightweightAssoc\"),objref);"+cb.newline());

		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.lightweightAssociations)+"\"));"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"lightweightAssociations\"),objref);"+cb.newline());

		out.write("metamodel->addObject(link);"+cb.newline());
		
	}

}
