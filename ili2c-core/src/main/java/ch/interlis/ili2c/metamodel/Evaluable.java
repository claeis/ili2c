package ch.interlis.ili2c.metamodel;

public abstract class Evaluable
{
    /** true, if definition somewhat incomplete due to (compile) errors.
     */
    private boolean dirty=false;
    abstract public boolean isLogical();
    public Type getType() {
        return null;
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
  }
  public Ili2cSemanticException checkTranslation(Evaluable other,int sourceLine)
  {
      return null;
  }
  static Ili2cSemanticException checkTranslation(Evaluable ele1, Evaluable ele2, int sourceLine, String msg) {
      if(ele1==null && ele2==null) {
          return null;
      }else if(ele1==null || ele2==null) {
          return new Ili2cSemanticException (sourceLine,Element.formatMessage(msg));
      }
      if(ele1.getClass()!=ele2.getClass()){
          return new Ili2cSemanticException (sourceLine,Element.formatMessage(msg));
      }
      return ele1.checkTranslation(ele2,sourceLine);
  }

public boolean isDirty() {
    return dirty;
}

public void setDirty(boolean dirty) {
    this.dirty = dirty;
}
}
