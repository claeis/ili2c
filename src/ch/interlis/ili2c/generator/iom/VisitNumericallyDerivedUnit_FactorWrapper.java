package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitNumericallyDerivedUnit_FactorWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		NumericallyDerivedUnit_FactorWrapper obj=(NumericallyDerivedUnit_FactorWrapper)obj1;
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		NumericallyDerivedUnit_FactorWrapper obj=(NumericallyDerivedUnit_FactorWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".NumericallyDerivedUnit_Factor";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<operator>"+encodeOperator(obj.factor.getConversionOperator())+"</operator>");
		out.write("<value>"+cb.encodePrecisionDecimal(obj.factor.getConversionFactor())+"</value>");
		out.write("<numericallyDerivedUnit REF=\""+cb.encodeOid(cb.getobjid(obj.numericallyDerivedUnit))+"\"/>");
		out.write("</"+tag+">");
	}
	private String encodeOperator(char op){
		String ret="times";
		if(op=='/'){
			ret="over";
		}
		return ret;
	}
}
