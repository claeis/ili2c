package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitConstantWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ConstantWrapper obj=(ConstantWrapper)obj1;
		Object refobj;
		EvaluableWrapper.visitObject(obj,cb);
		if(obj.cnst instanceof Constant.Numeric){
			Constant.Numeric cnst=(Constant.Numeric)obj.cnst;
			refobj=cnst.getUnit();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}
		if(obj.cnst instanceof Constant.Structured){
			Constant.Structured cnst=(Constant.Structured)obj.cnst;
			refobj=cnst.getUnit();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}
		if(obj.cnst instanceof Constant.ReferenceToMetaObject){
			Constant.ReferenceToMetaObject cnst=(Constant.ReferenceToMetaObject)obj.cnst;
			refobj=cnst.getReferred();
			if(refobj!=null){
				cb.addPendingObject(refobj);
			}
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ConstantWrapper obj=(ConstantWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj.cnst instanceof Constant.Enumeration){
			tag=tag+".Constant_Enumeration";
		}else if(obj.cnst instanceof Constant.EnumerationRange){
			tag=tag+".Constant_EnumerationRange";
		}else if(obj.cnst instanceof Constant.Numeric){
			tag=tag+".Constant_Numeric";
		}else if(obj.cnst instanceof Constant.ReferenceToMetaObject){
			tag=tag+".Constant_ReferenceToMetaObject";
		}else if(obj.cnst instanceof Constant.Structured){
			tag=tag+".Constant_Structured";
		}else if(obj.cnst instanceof Constant.Text){
			tag=tag+".Constant_Text";
		}else if(obj.cnst instanceof Constant.Undefined){
			tag=tag+".Constant_Undefined";
		}else{
			throw new IllegalArgumentException();
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		EvaluableWrapper.writeObject(out,obj,cb);
		if(obj.cnst instanceof Constant.Enumeration){
			out.write("<value>"+encodeEnum(((Constant.Enumeration)obj.cnst).getValue())+"</value>");
		}else if(obj.cnst instanceof Constant.EnumerationRange){
			out.write("<from>"+encodeEnum(((Constant.EnumerationRange)obj.cnst).getCommonPrefix())+"."+((Constant.EnumerationRange)obj.cnst).getFrom()+"</from>");
			out.write("<to>"+encodeEnum(((Constant.EnumerationRange)obj.cnst).getCommonPrefix())+"."+((Constant.EnumerationRange)obj.cnst).getTo()+"</to>");
		}else if(obj.cnst instanceof Constant.Numeric){
			out.write("<value>"+cb.encodePrecisionDecimal(((Constant.Numeric)obj.cnst).getValue())+"</value>");
			Object refobj=((Constant.Numeric)obj.cnst).getUnit();
			if(refobj!=null){
				out.write("<Unit REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
		}else if(obj.cnst instanceof Constant.ReferenceToMetaObject){
			Constant.ReferenceToMetaObject cnst=(Constant.ReferenceToMetaObject)obj.cnst;
			Object refobj=cnst.getReferred();
			if(refobj!=null){
				out.write("<metaObject REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
		}else if(obj.cnst instanceof Constant.Structured){
			out.write("<value>"+((Constant.Structured)obj.cnst).toString()+"</value>");
			Object refobj=((Constant.Structured)obj.cnst).getUnit();
			if(refobj!=null){
				out.write("<Unit REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
		}else if(obj.cnst instanceof Constant.Text){
			out.write("<value>"+cb.encodeString(((Constant.Text)obj.cnst).getValue())+"</value>");
		}else if(obj.cnst instanceof Constant.Undefined){
		}else{
			throw new IllegalArgumentException();
		}
		out.write("</"+tag+">");
	}
	private String encodeEnum(String[] value){
		StringBuilder buf = new StringBuilder(100);
		for (int i = 0; i < value.length; i++)
		{
		  if (i > 0)
			buf.append ('.');
		  buf.append (value[i]);
		}
		return buf.toString ();
	}
}
