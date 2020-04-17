package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitExpressionWrapper implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		ExpressionWrapper obj=(ExpressionWrapper)obj1;
		Object refobj;
		EvaluableWrapper.visitObject(obj,cb);
		if(obj.expr instanceof Expression.Conjunction){
			Expression.Conjunction expr=(Expression.Conjunction)obj.expr;
			Evaluable argv[]=expr.getConjoined();
			for(int i=0;i<argv.length;i++){
				cb.addPendingObject(EvaluableWrapper.createExpression_Multi(obj,argv[i],i+1));
			}
		}else if(obj.expr instanceof Expression.DefinedCheck){
			Expression.DefinedCheck expr=(Expression.DefinedCheck)obj.expr;
			Evaluable arg=expr.getArgument();
			if(arg!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_Unary(obj,arg));
			}
		}else if(obj.expr instanceof Expression.Disjunction){
			Expression.Disjunction expr=(Expression.Disjunction)obj.expr;
			Evaluable argv[]=expr.getDisjoined();
			for(int i=0;i<argv.length;i++){
				cb.addPendingObject(EvaluableWrapper.createExpression_Multi(obj,argv[i],i+1));
			}
		}else if(obj.expr instanceof Expression.Equality){
			Expression.Equality expr=(Expression.Equality)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.GreaterThan){
			Expression.GreaterThan expr=(Expression.GreaterThan)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.GreaterThanOrEqual){
			Expression.GreaterThanOrEqual expr=(Expression.GreaterThanOrEqual)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.Inequality){
			Expression.Inequality expr=(Expression.Inequality)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.LessThan){
			Expression.LessThan expr=(Expression.LessThan)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.LessThanOrEqual){
			Expression.LessThanOrEqual expr=(Expression.LessThanOrEqual)obj.expr;
			Evaluable left=expr.getLeft();
			if(left!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryL(obj,left));
			}
			Evaluable right=expr.getRight();
			if(right!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_BinaryR(obj,right));
			}
		}else if(obj.expr instanceof Expression.Negation){
			Expression.Negation expr=(Expression.Negation)obj.expr;
			Evaluable arg=expr.getNegated();
			if(arg!=null){
				cb.addPendingObject(EvaluableWrapper.createExpression_Unary(obj,arg));
			}
		}else{
			throw new IllegalArgumentException();
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		ExpressionWrapper obj=(ExpressionWrapper)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		if(obj.expr instanceof Expression.Conjunction){
			tag=tag+".Expression_Multi";
		}else if(obj.expr instanceof Expression.DefinedCheck){
			tag=tag+".Expression_Unary";
		}else if(obj.expr instanceof Expression.Disjunction){
			tag=tag+".Expression_Multi";
		}else if(obj.expr instanceof Expression.Equality){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.GreaterThan){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.GreaterThanOrEqual){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.Inequality){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.LessThan){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.LessThanOrEqual){
			tag=tag+".Expression_Binary";
		}else if(obj.expr instanceof Expression.Negation){
			tag=tag+".Expression_Unary";
		}else{
			throw new IllegalArgumentException();
		}
		
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		EvaluableWrapper.writeObject(out,obj,cb);
		
		if(obj.expr instanceof Expression.Conjunction){
			out.write("<operand>"+"and"+"</operand>");
		}else if(obj.expr instanceof Expression.DefinedCheck){
			out.write("<operand>"+"DefinedCheck"+"</operand>");
		}else if(obj.expr instanceof Expression.Disjunction){
			out.write("<operand>"+"or"+"</operand>");
		}else if(obj.expr instanceof Expression.Equality){
			out.write("<operand>"+"Equality"+"</operand>");
		}else if(obj.expr instanceof Expression.GreaterThan){
			out.write("<operand>"+"GreaterThan"+"</operand>");
		}else if(obj.expr instanceof Expression.GreaterThanOrEqual){
			out.write("<operand>"+"GreaterThanOrEqual"+"</operand>");
		}else if(obj.expr instanceof Expression.Inequality){
			out.write("<operand>"+"Inequal"+"</operand>");
		}else if(obj.expr instanceof Expression.LessThan){
			out.write("<operand>"+"LessThan"+"</operand>");
		}else if(obj.expr instanceof Expression.LessThanOrEqual){
			out.write("<operand>"+"LessThanOrEqual"+"</operand>");
		}else if(obj.expr instanceof Expression.Negation){
			out.write("<operand>"+"Negation"+"</operand>");
		}
		out.write("</"+tag+">");
	}
}
