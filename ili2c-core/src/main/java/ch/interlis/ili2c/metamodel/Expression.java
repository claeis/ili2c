/*****************************************************************************
 *
 * Expression.java
 * ---------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;


/** An abstract class that groups together all kinds of expressions.
*/
public abstract class Expression extends Evaluable
{
    @Override
    public boolean isLogical() {
        return true;
    }
    @Override
    public Type getType() {
        return PredefinedModel.getInstance().BOOLEAN.getType();
    }
  /** Denotes a negated Expression. A negated
      expression evaluates to <code>true</code>
      if the negated expression evaluates to
      <code>false</code>.
  */
  public static class Negation extends Expression
  {
    protected Evaluable negated;

    /** Constructs a new negated expression.
    */
    public Negation (Evaluable negated)
    {
      this.negated = negated;
    }

    public Evaluable getNegated ()
    {
      return negated;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Negation other=(Negation)otherEv;
        if(negated.getClass()!=other.negated.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_negationMismatch"));
        }
        ret=negated.checkTranslation(other.negated,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
    
  }


  /** Denotes a set of disjoined Expressions. A set
      of disjoined expressions evaluates to <code>true</code>
      if at least one of the members evaluates to
      <code>true</code>. The entire expression evaluates
      only to <code>false</code> if all of its members
      evaluates to <code>false</code>.

      <p>Note that INTERLIS does <em>not</em>
      impose a specific order in which disjoined expressions
      would have to be evaluated. Thus, an implementation is
      free to evaluate the disjoined expressions in any order
      it wants to. While this is different to the behavior of
      boolean operators in typical programming languages
      (such as the <code>||</code> operator in C or Java),
      it gives implementations more freedom to transform
      Expressions according to their respective needs.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class Disjunction extends Expression
  {
    protected Evaluable[] disjoined;

    /** Constructs a new disjunctive expression.

        @exception java.lang.IllegalArgumentException if
                   <code>disjoined</code> does
                   not consist of at least two elements.
    */
    public Disjunction (Evaluable[] disjoined)
    {
      if (disjoined.length < 2)
        throw new IllegalArgumentException();

      this.disjoined = disjoined;
    }

    public Evaluable[] getDisjoined() {
      return disjoined;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Disjunction other=(Disjunction)otherEv;
        if(disjoined.length!=other.disjoined.length) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_disjunctionMismatch"));
        }
        for(int pathi=0;pathi<disjoined.length;pathi++) {
            if(disjoined[pathi].getClass()!=other.disjoined[pathi].getClass()) {
                return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_disjunctionMismatch"));
            }
            ret=disjoined[pathi].checkTranslation(other.disjoined[pathi],sourceLine);
            if(ret!=null) {
                return ret;
            }
        }
        return null;
    }
  }



  /** Denotes a set of conjoined Expressions. A set
      of conjoined expressions evaluates to <code>true</code>
      if all of its members evaluates to <code>true</code>.
      If one of the conjoined expressions evaluates to
      <code>false</code>, the entire set
      evaluates to <code>false</code>.

      <p>Note that INTERLIS does <em>not</em>
      impose a specific order in which conjoined expressions
      would have to be evaluated. Thus, an implementation is
      free to evaluate the conjoined expressions in any order
      it wants to. While this is different to the behavior of
      boolean operators in typical programming languages
      (such as the <code>&&</code> operator in C or Java),
      it gives implementations more freedom to transform
      Expressions according to their respective needs.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class Conjunction extends Expression
  {
    protected Evaluable[] conjoined;

    /** Constructs a new conjunctive expression.

        @exception java.lang.IllegalArgumentException if
                   <code>conjoined</code> does
                   not consist of at least two elements.
    */
    public Conjunction (Evaluable[] conjoined)
    {
      if (conjoined.length < 2)
        throw new IllegalArgumentException();
      this.conjoined = conjoined;
    }

    public Evaluable[] getConjoined() {
      return conjoined;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Conjunction other=(Conjunction)otherEv;
        if(conjoined.length!=other.conjoined.length) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_conjunctionMismatch"));
        }
        for(int pathi=0;pathi<conjoined.length;pathi++) {
            if(conjoined[pathi].getClass()!=other.conjoined[pathi].getClass()) {
                return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_conjunctionMismatch"));
            }
            ret=conjoined[pathi].checkTranslation(other.conjoined[pathi],sourceLine);
            if(ret!=null) {
                return ret;
            }
        }
        return null;
    }
    
  }

  /** Denotes logical equality of two expressions.
      A LogicalEquality evaluates to <code>true</code>
      if both its <code>left</code> and <code>right</code>
      evaluate to the same value; it evaluates to
      <code>false</code> if one of them evaluates to
      <code>true</code> and the other one to
      <code>false</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class Equality extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    /** Constructs a new Equality.
    */
    public Equality (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }

    public Evaluable getLeft()
    {
      return left;
    }


    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Equality other=(Equality)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_equalityMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_equalityMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes logical or arithmetic inequality of two expressions.
      An Inequality evaluates to <code>false</code>
      if both its <code>left</code> and <code>right</code>
      evaluate to the same value; it evaluates to
      <code>true</code> if one of them evaluates to
      <code>true</code> and the other one to
      <code>false</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class Inequality extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    /** Constructs a new LogicalInequality.
    */
    public Inequality (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }



    public Evaluable getLeft()
    {
      return left;
    }



    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Inequality other=(Inequality)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_inequalityMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_inequalityMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes a boolean expressions that evaluates to
      <code>true</code> if its <code>left</code> is
      arithmetically greater than its <code>right</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class GreaterThan extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    public GreaterThan (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }

    public Evaluable getLeft()
    {
      return left;
    }


    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        GreaterThan other=(GreaterThan)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_greaterThanMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_greaterThanMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes a boolean expressions that evaluates to
      <code>true</code> if its <code>left</code> is
      arithmetically less than its <code>right</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class LessThan extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    public LessThan (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }

    public Evaluable getLeft()
    {
      return left;
    }


    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        LessThan other=(LessThan)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_lessThanMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_lessThanMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes a boolean expressions that evaluates to
      <code>true</code> if its <code>left</code> is
      arithmetically less than or equal to its
      <code>right</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class LessThanOrEqual extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    public LessThanOrEqual (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }

    public Evaluable getLeft()
    {
      return left;
    }


    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        LessThanOrEqual other=(LessThanOrEqual)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_lessThanOrEqualMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_lessThanOrEqualMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes a boolean expressions that evaluates to
      <code>true</code> if its <code>left</code> is
      arithmetically greater than or equal to its
      <code>right</code>.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class GreaterThanOrEqual extends Expression
  {
    protected Evaluable left;
    protected Evaluable right;

    public GreaterThanOrEqual (Evaluable left, Evaluable right)
    {
      this.left = left;
      this.right = right;
    }

    public Evaluable getLeft()
    {
      return left;
    }


    public Evaluable getRight()
    {
      return right;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        GreaterThanOrEqual other=(GreaterThanOrEqual)otherEv;
        if(left.getClass()!=other.left.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_greaterThanOrEqualMismatch"));
        }
        ret=left.checkTranslation(other.left,sourceLine);
        if(ret!=null) {
            return ret;
        }
        if(right.getClass()!=other.right.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_greaterThanOrEqualMismatch"));
        }
        ret=right.checkTranslation(other.right,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }



  /** Denotes a boolean expressions that evaluates to
      <code>true</code> if its <code>argument</code> is
      defined.

      @author Sascha Brawer, sb@adasys.ch
  */
  public static class DefinedCheck extends Expression
  {
    protected Evaluable argument;

    public DefinedCheck (Evaluable argument)
    {
      this.argument = argument;
    }

    public Evaluable getArgument ()
    {
      return argument;
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        DefinedCheck other=(DefinedCheck)otherEv;
        if(argument.getClass()!=other.argument.getClass()) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_definedCheckMismatch"));
        }
        ret=argument.checkTranslation(other.argument,sourceLine);
        if(ret!=null) {
            return ret;
        }
        return null;
    }
  }
}
