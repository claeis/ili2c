package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitOIDType implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		OIDType obj=(OIDType)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getOIDType();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		OIDType obj=(OIDType)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof TextOIDType){
			tag+=".TextOIDType";
		}else if(obj instanceof NumericOIDType){
			tag+=".NumericOIDType";
		}else{
			tag+=".AnyOIDType";
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		if(obj instanceof TextOIDType){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getOIDType()))+"\"/>");
		}else if(obj instanceof NumericOIDType){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getOIDType()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		OIDType obj=(OIDType)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix;
		if(obj instanceof TextOIDType){
			tag+=".TextOIDType";
		}else if(obj instanceof NumericOIDType){
			tag+=".NumericOIDType";
		}else{
			tag+=".AnyOIDType";
		}
		
		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isMandatory\"),X(\""+cb.encodeBoolean(obj.isMandatory())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isAbstract\"),X(\""+cb.encodeBoolean(obj.isAbstract())+"\"));"+cb.newline());		
		
		if(obj.getExtending()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ExtendedByBaseType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getExtending())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"extendedBy\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"base\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		if(obj instanceof TextOIDType){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".TypeTextOIDType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getOIDType())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"textOIDType\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"type\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}else if(obj instanceof NumericOIDType){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".TypeNumericOIDType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getOIDType())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"numericOIDType\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"type\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}
		
		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
