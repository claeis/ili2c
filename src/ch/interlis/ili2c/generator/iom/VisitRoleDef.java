package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitRoleDef implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		RoleDef obj=(RoleDef)obj1;
		Iterator elei;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getCardinality();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getDestination();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		// binary association?
		if(obj.hasOneOppEnd()){
			// visit oppend role
			refobj=obj.getOppEnd();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}
		refobj=obj.getDerivedFrom();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		// TODO restrictedTo
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		RoleDef obj=(RoleDef)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".RoleDef";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("<kind>"+encodeRoleKind(obj.getKind())+"</kind>");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		out.write("<isExtended>"+cb.encodeBoolean(obj.isExtended())+"</isExtended>");
		out.write("<isOrdered>"+cb.encodeBoolean(obj.isOrdered())+"</isOrdered>");
		out.write("<isExternal>"+cb.encodeBoolean(obj.isExternal())+"</isExternal>");
		out.write("<ili1AttrIdx>"+cb.encodeInteger(obj.getIli1AttrIdx())+"</ili1AttrIdx>");
		if(obj.getCardinality()!=null){
			out.write("<cardinality REF=\""+cb.encodeOid(cb.getobjid(obj.getCardinality()))+"\"/>");
		}
		if(obj.getDerivedFrom()!=null){
			out.write("<derivedFrom REF=\""+cb.encodeOid(cb.getobjid(obj.getDerivedFrom()))+"\"/>");
		}
		// binary association?
		if(obj.hasOneOppEnd()){
			out.write("<oppend REF=\""+cb.encodeOid(cb.getobjid(obj.getOppEnd()))+"\"/>");
		}
		out.write("<target REF=\""+cb.encodeOid(cb.getobjid(obj.getDestination()))+"\"/>");
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		RoleDef obj=(RoleDef)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".RoleDef";

		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"name\"),X(\""+obj.getName()+"\"));"+cb.newline());

		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ContainerElements"+"\"));"+cb.newline());
		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.getContainer())+"\"));"+cb.newline());
		out.write("link->setLinkEnd(ParserHandler::getTagId(\"elements\"),obj);"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"container\"),objref);"+cb.newline());
		out.write("metamodel->addObject(link);"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"kind\"),X(\""+encodeRoleKind(obj.getKind())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isFinal\"),X(\""+cb.encodeBoolean(obj.isFinal())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isAbstract\"),X(\""+cb.encodeBoolean(obj.isAbstract())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isExtended\"),X(\""+cb.encodeBoolean(obj.isExtended())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isOrdered\"),X(\""+cb.encodeBoolean(obj.isOrdered())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isExternal\"),X(\""+cb.encodeBoolean(obj.isExternal())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"ili1AttrIdx\"),X(\""+cb.encodeInteger(obj.getIli1AttrIdx())+"\"));"+cb.newline());
		
		if(obj.getCardinality()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".CardinalityRoleDef"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getCardinality())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"roleDef\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"cardinality\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}
		
		if(obj.getDerivedFrom()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".RoleDefDerivedFrom"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getDerivedFrom())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"roleDef\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"derivedFrom\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		// binary association?
		if(obj.hasOneOppEnd()){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".RoleDef2RoleDef"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getOppEnd())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"thisend\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"oppend\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".TargetAssociation"+"\"));"+cb.newline());
		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.getDestination())+"\"));"+cb.newline());
		out.write("link->setLinkEnd(ParserHandler::getTagId(\"association\"),obj);"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"target\"),objref);"+cb.newline());
		out.write("metamodel->addObject(link);"+cb.newline());
		
		out.write("metamodel->addObject(obj);"+cb.newline());
	}

	private String encodeRoleKind(int kind){
		String ret="association";
		if(kind==RoleDef.Kind.eAGGREGATE){
			ret="aggregation";
		}else if(kind==RoleDef.Kind.eCOMPOSITE){
			ret="composition";
		}
		return ret;
	}
}
