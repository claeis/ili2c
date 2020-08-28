package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitCompositionType implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		CompositionType obj=(CompositionType)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getCardinality();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getComponentType();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		Iterator elei=obj.iteratorRestrictedTo();
		while(elei.hasNext()){
			Table restrictedTo=(Table)elei.next();
			cb.addPendingObject(new RestrictionOfCompTypeRestrictedTo(obj,restrictedTo));
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		CompositionType obj=(CompositionType)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".CompositionType";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		out.write("<isOrdered>"+cb.encodeBoolean(obj.isOrdered())+"</isOrdered>");
		if(obj.getCardinality()!=null){
			out.write("<cardinality REF=\""+cb.encodeOid(cb.getobjid(obj.getCardinality()))+"\"/>");
		}
		if(obj.getComponentType()!=null){
			out.write("<component REF=\""+cb.encodeOid(cb.getobjid(obj.getComponentType()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		CompositionType obj=(CompositionType)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".CompositionType";
		
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

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isOrdered\"),X(\""+cb.encodeBoolean(obj.isOrdered())+"\"));"+cb.newline());		

		if(obj.getCardinality()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".CompositionTypeCardinality"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getCardinality())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"compositionType\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"cardinality\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}
		
		if(obj.getComponentType()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".CompositionTypeComponent"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getComponentType())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"compositionType\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"component\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
