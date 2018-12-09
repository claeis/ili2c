package ch.interlis.ili2c.metamodel;


public class UniqueEl
{
  private java.util.ArrayList attrv=new java.util.ArrayList();
  public void addAttribute(ObjectPath attribute)
  {
    attrv.add(attribute);
  }
  public java.util.Iterator iteratorAttribute()
  {
    return attrv.iterator();
  }
}
