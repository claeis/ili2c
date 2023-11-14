package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;
import java.io.IOException;
import java.io.Writer;

/** Wrapper around XY->Evaluable that holds a pointer to the parent.
 * @author ce
 */
public abstract class EvaluableWrapper {
	private static EvaluableWrapper createEvaluableWrapper(Evaluable refobj) {
		EvaluableWrapper ret=null;
		if(refobj instanceof ObjectPath){
			ret=new ObjectPathWrapper((ObjectPath)refobj);
		}else if(refobj instanceof Constant){
			ret=new ConstantWrapper((Constant)refobj);
		}else if(refobj instanceof Expression){
			ret=new ExpressionWrapper((Expression)refobj);
		}else if(refobj instanceof FunctionCall){
			ret=new FunctionCallWrapper((FunctionCall)refobj);
		}else if(refobj instanceof ParameterValue){
			ret=new RuntimeParameterValueWrapper((ParameterValue)refobj);
		}else{
			throw new IllegalArgumentException();
		}
		return ret;
	}
	
	public Constraint mandatoryConstraint=null;
	public static EvaluableWrapper createMandatoryConstraint(Constraint mandatoryConstraint1,Evaluable obj) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.mandatoryConstraint=mandatoryConstraint1;
		return ret;
	}
	public FunctionCallWrapper functionCall=null;
	public int argument_orderPos=0;
	public static EvaluableWrapper createFunctionCall(FunctionCallWrapper functionCall1,Evaluable obj,int orderPos) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.functionCall=functionCall1;
		ret.argument_orderPos=orderPos;
		return ret;
	}

	public ExpressionWrapper expression_Multi=null;
	public int joined_orderPos=0;
	public static EvaluableWrapper createExpression_Multi(ExpressionWrapper expression_Multi1,Evaluable obj,int orderPos) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.expression_Multi=expression_Multi1;
		ret.joined_orderPos=orderPos;
		return ret;
	}
	
	public ExpressionWrapper expression_Unary=null;
	public static EvaluableWrapper createExpression_Unary(ExpressionWrapper expression_Unary1,Evaluable obj) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.expression_Unary=expression_Unary1;
		return ret;
	}

	public ExpressionWrapper expression_BinaryL=null;
	public static EvaluableWrapper createExpression_BinaryL(ExpressionWrapper expression_BinaryL1,Evaluable obj) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.expression_BinaryL=expression_BinaryL1;
		return ret;
	}
	
	public ExpressionWrapper expression_BinaryR=null;
	public static EvaluableWrapper createExpression_BinaryR(ExpressionWrapper expression_BinaryR1,Evaluable obj) {
		EvaluableWrapper ret=createEvaluableWrapper(obj);
		ret.expression_BinaryR=expression_BinaryR1;
		return ret;
	}

	public static void visitObject(EvaluableWrapper obj, VisitorCallback cb) {
		// add parent object of Evaluable to pending objects
		Object refobj=obj.functionCall;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.mandatoryConstraint;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.expression_Multi;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.expression_Unary;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.expression_BinaryL;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.expression_BinaryR;
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}
	public static void writeObject(Writer out, EvaluableWrapper obj, WriterCallback cb)
		throws IOException {
			// write reference to parent of Evaluable
			Object refobj;
			refobj=obj.expression_BinaryL;
			if(refobj!=null){
				out.write("<expression_BinaryL REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
			refobj=obj.expression_BinaryR;
			if(refobj!=null){
				out.write("<expression_BinaryR REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
			refobj=obj.expression_Multi;
			if(refobj!=null){
				out.write("<expression_Multi REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\" ORDER_POS=\""+cb.encodeInteger(obj.joined_orderPos)+"\"/>");
			}
			refobj=obj.expression_Unary;
			if(refobj!=null){
				out.write("<expression_Unary REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
			refobj=obj.functionCall;
			if(refobj!=null){
				out.write("<functionCall REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"  ORDER_POS=\""+cb.encodeInteger(obj.argument_orderPos)+"\"/>");
			}
			refobj=obj.mandatoryConstraint;
			if(refobj!=null){
				out.write("<mandatoryConstraint REF=\""+cb.encodeOid(cb.getobjid(refobj))+"\"/>");
			}
			
	}
}
