package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/** handles NumericType, StructuredUnitType and CoordTypeDimensionWrapper.
 * @author ce
 */
public class VisitNumericalType implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		NumericalType obj=(obj1 instanceof CoordTypeDimensionWrapper) ? ((CoordTypeDimensionWrapper)obj1).dimension:(NumericalType)obj1;
		Object refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getUnit();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getReferenceSystem();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		CoordType coordType=null;
		NumericalType obj= null;
		if(obj1 instanceof CoordTypeDimensionWrapper){
			obj= ((CoordTypeDimensionWrapper)obj1).dimension;
			coordType=((CoordTypeDimensionWrapper)obj1).coordType;
		}else{
			obj= (NumericalType)obj1;
		}
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj instanceof NumericType){
			tag+=".NumericType";
		}else{
			tag+=".StructuredUnitType";
		}
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<isMandatory>"+cb.encodeBoolean(obj.isMandatory())+"</isMandatory>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		out.write("<isCircular>"+cb.encodeBoolean(obj.isCircular())+"</isCircular>");
		out.write("<rotation>"+encodeRotationKind(obj.getRotation(),obj.getReferenceSystem())+"</rotation>");
		if(coordType!=null){
			out.write("<coordType REF=\""+cb.encodeOid(cb.getobjid(coordType))+"\" ORDER_POS=\""
				+cb.encodeInteger(((CoordTypeDimensionWrapper)obj1).orderPos)+"\"/>");
		}
		// referenceSystem
		RefSystemRef refsys=obj.getReferenceSystem();
		if(refsys!=null){
			out.write("<referenceSystem REF=\""+cb.encodeOid(cb.getobjid(refsys))+"\"/>");
		}
		// unit
		Unit unit=obj.getUnit();
		if(unit!=null){
			out.write("<unit REF=\""+cb.encodeOid(cb.getobjid(unit))+"\"/>");
		}
		if(obj instanceof NumericType){
			if(((NumericType)obj).getMinimum()!=null){
				out.write("<minimum>"+cb.encodePrecisionDecimal(((NumericType)obj).getMinimum())+"</minimum>");
				out.write("<maximum>"+cb.encodePrecisionDecimal(((NumericType)obj).getMaximum())+"</maximum>");
			}
		}else{
			out.write("<minimum>"+cb.encodeStructDec(((StructuredUnitType)obj).getMinimum())+"</minimum>");
			out.write("<maximum>"+cb.encodeStructDec(((StructuredUnitType)obj).getMaximum())+"</maximum>");
		}
		
		out.write("</"+tag+">");
	}
	private String encodeRotationKind(int kind,Object refsys){
		String ret="none";
		if(refsys!=null){
			ret="refsys";
		}
		if(kind==NumericalType.ROTATION_CLOCKWISE){
			ret="clockwise";
		}else if(kind==NumericalType.ROTATION_COUNTERCLOCKWISE){
			ret="counterclockwise";
		}
		return ret;
	}
}
