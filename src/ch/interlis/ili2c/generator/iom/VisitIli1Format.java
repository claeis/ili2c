package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitIli1Format implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Ili1Format obj=(Ili1Format)obj1;
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Ili1Format obj=(Ili1Format)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Ili1Format";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isFree>"+cb.encodeBoolean(obj.isFree)+"</isFree>");
		if(!obj.isFree){
			out.write("<lineSize>"+cb.encodeInteger(obj.lineSize)+"</lineSize>");
			out.write("<tidSize>"+cb.encodeInteger(obj.tidSize)+"</tidSize>");
		}
		out.write("<blankCode>"+cb.encodeInteger(obj.blankCode)+"</blankCode>");
		out.write("<undefinedCode>"+cb.encodeInteger(obj.undefinedCode)+"</undefinedCode>");
		out.write("<continueCode>"+cb.encodeInteger(obj.continueCode)+"</continueCode>");
		if(obj.font!=null){
			out.write("<font>"+cb.encodeString(obj.font)+"</font>");
		}
		out.write("<tidKind>"+encodeTidKind(obj.tidKind)+"</tidKind>");
		if(obj.tidKind==Ili1Format.TID_EXPLANATION){
			out.write("<tidExplanation>"+cb.encodeString(obj.tidExplanation)+"</tidExplanation>");
		}
		out.write("</"+tag+">");
	}

	private String encodeTidKind(int kind){
		String ret;
		if(kind==Ili1Format.TID_I16){
			ret="TID_I16";
		}else if(kind==Ili1Format.TID_I32){
			ret="TID_I32";
		}else if(kind==Ili1Format.TID_ANY){
			ret="TID_ANY";
		}else if(kind==Ili1Format.TID_EXPLANATION){
			ret="TID_EXPLANATION";
		}else{
			throw new IllegalArgumentException();
		}
		return ret;
	}
}
