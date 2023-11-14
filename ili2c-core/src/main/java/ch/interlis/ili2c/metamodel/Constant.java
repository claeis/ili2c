/*****************************************************************************
 *
 * Constant.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 *****************************************************************************/


package ch.interlis.ili2c.metamodel;


import java.util.List;


/** An abstract class which groups together all constants.
    Constants are used in INTERLIS expressions, for example
    to specify a constraint on an attribute value.
*/
public abstract class Constant extends Evaluable
{
  Constant()
  {
  };
  @Override
  public boolean isLogical() {
      return false;
  }


  /** The constant for the <code>UNDEFINED</code> value.
  */
  public static class Undefined extends Constant
  {
    /** Returns <code>UNDEFINED</code>.
    */
    public String toString()
    {
      return "UNDEFINED";
    }



    /** Checks whether it is possible to assign this Evaluable to
        the Element <code>target</code>, whose type is <code>targetType</code>.
        If so, nothing happens.


        @param target The Element whose value is going to be changed by executing the assignment.


        @param targetType The type of that Element.


        @exception java.lang.IllegalArgumentException If <code>this</code>
                   can not be assigned to the specified target.
                   The message of the exception indicates the reason; it is
                   a localized string that is intended for being displayed
                   to the user.
    */
    void checkAssignment (Element target, Type targetType)
    {
      boolean anyMandatory = false;
      Type curType;


      super.checkAssignment (target, targetType);
      if (targetType == null)
        return;


      curType = targetType;


      while ((curType != null) && !anyMandatory)
      {
        anyMandatory |= curType.isMandatory ();
        if (curType instanceof TypeAlias)
        {
          Domain aliasing = ((TypeAlias) curType).getAliasing();
          curType = aliasing == null ? null : aliasing.getType ();
        }
        else
          break;
      }


      if (anyMandatory)
      {
        throw new IllegalArgumentException (Element.formatMessage (
          "err_constUndefined_assignToMandatory", target.toString()));
      }
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        return null;
    }
  } /* Constant.Undefined */



  /** A constant Text.
  */
  public static class Text extends Constant
  {
    String value;

    @Override
    public Type getType() {
        return new TextType();
    }

    /** Constructs a new text constant.
    */
    public Text (String value)
    {
      this.value = value;
    }



    /** Returns the value of the text constant, without
        quotes.
    */
    public String getValue ()
    {
      return value;
    }



    /** Returns a string consisting of the constant's value
        in double quotes.
    */
    public String toString ()
    {
      return "\"" + value + "\"";
    }



    /** Checks whether it is possible to assign this Evaluable to
        the Element <code>target</code>, whose type is <code>targetType</code>.
        If so, nothing happens.


        @param target The Element whose value is going to be changed by executing the assignment.


        @param targetType The type of that Element.


        @exception java.lang.IllegalArgumentException If <code>this</code>
                   can not be assigned to the specified target.
                   The message of the exception indicates the reason; it is
                   a localized string that is intended for being displayed
                   to the user.
    */
    void checkAssignment (Element target, Type targetType)
    {
      super.checkAssignment (target, targetType);
      targetType = Type.findReal (targetType);


      if (targetType == null)
      {
        /* Do nothing, as "null" means an error while parsing from which
           the parser could recover.
        */
      }
      else if (targetType instanceof TextType)
      {
        int maxLen = ((TextType) targetType).getMaxLength ();
        if ((value.length() > maxLen) && (maxLen > 0))
          throw new IllegalArgumentException (Element.formatMessage (
            "err_constText_assignTooLong",
            target.toString (),
            this.toString (),
            Integer.toString (maxLen)));
      }
      else
        throw new IllegalArgumentException (Element.formatMessage (
          "err_constText_assignOther", target.toString(), this.toString()));
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Text other=(Text)otherEv;
        if(!value.equals(other.value)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
        }
        return null;
    }
  }


  public static class Class extends Constant
  {
	  private Viewable value=null;
	  public Class(Viewable value)
	  {
		  this.value=value;
	  }
	  public Viewable getValue()
	  {
		  return value;
	  }
      @Override
      public Type getType() {
          return new ClassType();
      }
      @Override
      public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
      {
          Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
          if(ret!=null) {
              return ret;
          }
          Class other=(Class)otherEv;
          if(!Element.equalElementRef(value, other.value)){
              return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
          }
          return null;
      }
      public String toString ()
      {
        return ">"+value.getScopedName();
      }

  }
  public static class AttributePath extends Constant
  {
	  private AttributeDef value=null;
	  public AttributePath(AttributeDef attr)
	  {
		  value=attr;
	  }
	  public AttributeDef getValue()
	  {
		  return value;
	  }
	  @Override
	  public Type getType() {
	      return new AttributePathType();
	  }
	    @Override
	    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
	    {
	        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
	        if(ret!=null) {
	            return ret;
	        }
	        AttributePath other=(AttributePath)otherEv;
	        if(!Element.equalElementRef(value, other.value)){
	            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
	        }
	        return null;
	    }
	    public String toString ()
	    {
	      return ">>"+value.getName();
	    }

  }
  /** A numeric constant.
  */
  public static class Numeric extends Constant
  {
    PrecisionDecimal value;
    Unit   unit;



    /** Constructs a new unit-less numeric constant given its value.
    */
    public Numeric (PrecisionDecimal value)
    {
      this.value = value;
      this.unit = null;
    }



    /** Constructs a new numeric constant given its value and unit.
    */
    public Numeric (PrecisionDecimal value, Unit unit)
    {
      this.value = value;
      this.unit = unit;
    }



    /** Returns the value of a numeric constant.
    */
    public PrecisionDecimal getValue()
    {
      return value;
    }
    @Override 
    public Type getType()
    {
        return new NumericType();
    }



    /** Returns the unit of a numeric constant. For a unit-less
        numeric constant, <code>null</code> is returned.
    */
    public Unit getUnit ()
    {
      return unit;
    }



    /** Checks whether it is possible to assign this Evaluable to
        the Element <code>target</code>, whose type is <code>targetType</code>.
        If so, nothing happens.


        @param target The Element whose value is going to be changed by executing the assignment.


        @param targetType The type of that Element.


        @exception java.lang.IllegalArgumentException If <code>this</code>
                   can not be assigned to the specified target.
                   The message of the exception indicates the reason; it is
                   a localized string that is intended for being displayed
                   to the user.
    */
    void checkAssignment (Element target, Type targetType)
    {
      super.checkAssignment (target, targetType);
      targetType = Type.findReal (targetType);
      if ((targetType != null) && !(targetType instanceof NumericType))
      {
        throw new IllegalArgumentException (Element.formatMessage (
          "err_numericConst_assignOther",
          target.toString(),
          value.toString()));
      }
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Numeric other=(Numeric)otherEv;
        if(!value.equals(other.value)) {
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
        }
        if(!Element.equalElementRef(unit, other.unit)){
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
        }
        return null;
    }
    public String toString ()
    {
      return value.toString()+ (unit!=null ? "["+unit.getName()+"]" : "");
    }
    
  } /* Constant.Numeric */



  /** An abstract class that groups together
      the two classes <code>ch.interlis.Constant.Enumeration</code>
      and <code>ch.interlis.Constant.EnumRange</code>. These
      classes are grouped together because both have
      to do with enumerations.
  */
  public abstract static class EnumConstOrRange extends Constant
  {
  } /* Constant.EnumConstOrRange */



  /** A class for a fixed constant enumeration, such as
      <code>#day.weekday.monday</code>.
  */
  public static class Enumeration extends EnumConstOrRange
  {
    public static final String OTHERS=new String("OTHERS");
    String[] value;
    Type type=null;


    /** Constructs a new constant enumeration given the
        elements that appear separated by dots in an
        INTERLIS description file.
    */
    public Enumeration (List<String> value)
    {
      this.value = value.toArray(new String[value.size()]);
    }



    /** Returns the enumeration value as an array of strings.
    */
    public String[] getValue ()
    {
      return value;
    }
    @Override
    public boolean isLogical() {
        if(value.length==1) {
            if(value[0].equals("true") || value[0].equals("false")) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Type getType() {
        if(type==null) {
            return new EnumerationType();
        }
        return type;
    }
    public void setType(Type type)
    {
        this.type=type;
    }



    /** Returns the enumeration value as one single string
        that has a hash sign as its prefix and
        in which the individual elements are separated by
        dots.
    */
    public String toString ()
    {
      StringBuilder buf = new StringBuilder(100);
      buf.append ('#');
      for (int i = 0; i < value.length; i++)
      {
        if (i > 0)
          buf.append ('.');
        buf.append (value[i]);
      }
      return buf.toString ();
    }
    public String getScopedName()
    {
      StringBuilder buf = new StringBuilder(100);
      for (int i = 0; i < value.length; i++)
      {
        if (i > 0)
          buf.append ('.');
        buf.append (value[i]);
      }
      return buf.toString ();
    }


    /** Checks whether it is possible to assign this Evaluable to
        the Element <code>target</code>, whose type is <code>targetType</code>.
        If so, nothing happens.


        @param target The Element whose value is going to be changed by executing the assignment.


        @param targetType The type of that Element.


        @exception java.lang.IllegalArgumentException If <code>this</code>
                   can not be assigned to the specified target.
                   The message of the exception indicates the reason; it is
                   a localized string that is intended for being displayed
                   to the user.
    */
    void checkAssignment (Element target, Type targetType)
    {
      super.checkAssignment (target, targetType);
      targetType = Type.findReal (targetType);
      if ((targetType != null) && !(targetType instanceof EnumerationType))
      {
        throw new IllegalArgumentException (Element.formatMessage (
          "err_enumConst_assignOther", target.toString(), this.toString()));
      }
    }
    @Override
    public Ili2cSemanticException checkTranslation(Evaluable otherEv,int sourceLine)
    {
        Ili2cSemanticException ret=super.checkTranslation(otherEv,sourceLine);
        if(ret!=null) {
            return ret;
        }
        Enumeration other=(Enumeration)otherEv;
        List<String> values=getTypeValues();
        int thisIdx=values.indexOf(getScopedName());
        List<String> otherValues=other.getTypeValues();
        int otherIdx=otherValues.indexOf(other.getScopedName());
        if(thisIdx!=otherIdx){
            return new Ili2cSemanticException(sourceLine,Element.formatMessage("err_diff_constantMismatch",toString(),other.toString()));
        }
        return null;
    }
    private List<String> getTypeValues(){
        if(type instanceof EnumerationType) {
            return ((EnumerationType) type).getValues();
        }else if(type instanceof EnumTreeValueType) {
            return ((EnumTreeValueType) type).getValues();
        }
        throw new IllegalStateException("unexpected type "+type.getScopedName());
    }
  } /* Constant.Enumeration */



  /** A class for a range of constant enumerations, such as
      <code>#day.weekday.monday .. #day.weekday.wednesday</code>.
  */
  public static class EnumerationRange extends EnumConstOrRange
  {
    String[] commonPrefix;
    String   from;
    String   to;


    /** Constructs a new range of enumeration constants.


        @param commonPrefix the common prefix of both constants,
                            for example an array consisting of
                            the two elements <code>"day"</code>
                            and <code>"weekday"</code>.


        @param from         the "from" part of the range,
                            for example <code>"monday"</code>.


        @param to           the "to" part of the range,
                            for example <code>"wednesday"</code>.
    */
    public EnumerationRange (String[] commonPrefix, String from, String to)
    {
      this.commonPrefix = commonPrefix;
      this.from = from;
      this.to = to;
    }


    public String[] getCommonPrefix ()
    {
      return commonPrefix;
    }


    public String getFrom()
    {
      return from;
    }


    public String getTo ()
    {
      return to;
    }
  } /* Constant.EnumerationRange */



  /** A structured constant, such as "12:34".
  */
  public static class Structured extends Constant
  {
    String value;
    Unit unit;


    /** Constructs a new structured constant given the
        constant value as a string.
    */
    public Structured (String value)
    {
      this.value = value;
    }


    public Structured (String value,Unit un)
    {
      this.value = value;
      this.unit=un;
    }


    public String toString ()
    {
      return value;
    }
	/** Returns the unit of a structured constant.
	*/
	public Unit getUnit ()
	{
	  return unit;
	}
  } /* Constant.Structured */



  /** A constant reference to a MetaObject. */
  public static class ReferenceToMetaObject extends Constant
  {
    MetaObject referred;


    /** Constructs a new constant reference to a MetaObject. */
    public ReferenceToMetaObject (MetaObject referred)
    {
      this.referred = referred;
    }


    /** Returns a String useful for debugging.


        @return <code>"&lt;Constant.ReferenceToMetaObject
                      <i>referredMetaObject</i>&gt;</code>
    */
    public String toString ()
    {
      return "<Constant.ReferenceToMetaObject " + referred + ">";
    }


    public MetaObject getReferred ()
    {
      return referred;
    }



    /** Checks whether it is possible to assign this Evaluable to
        the Element <code>target</code>, whose type is <code>targetType</code>.
        If so, nothing happens.


        @param target The Element whose value is going to be changed by executing the assignment.


        @param targetType The type of that Element.


        @exception java.lang.IllegalArgumentException If <code>this</code>
                   can not be assigned to the specified target.
                   The message of the exception indicates the reason; it is
                   a localized string that is intended for being displayed
                   to the user.
    */
    void checkAssignment (Element target, Type targetType)
    {
      super.checkAssignment (target, targetType);
      targetType = Type.findReal (targetType);


      if ((target == null) || (targetType == null) || (referred == null))
      {
        /* Do nothing, as "null" means an error while parsing from which
           the parser could recover.
        */
        return;
      }


      if (!(targetType instanceof MetaobjectType))
      {
        throw new IllegalArgumentException (Element.formatMessage (
          "err_constRefMeta_assignOther",
          target.toString(), referred.toString()));
      }


      /* Check whether referred is polymorphic equivalent to targetType. */
      if (referred == null)
        return;


      Table targetClass = ((MetaobjectType) targetType).getReferred();
      Table sourceClass = referred.getTable();
      if ((targetClass == null) || (sourceClass == null))
        return;


      if (!sourceClass.isExtending (targetClass))
        throw new IllegalArgumentException (Element.formatMessage (
          "err_constRefMeta_sourceNotExtTarget",
          target.toString(), referred.toString(),
          targetClass.toString(), sourceClass.toString()));
    }
  } /* Constant.ReferenceToMetaObject */
}
