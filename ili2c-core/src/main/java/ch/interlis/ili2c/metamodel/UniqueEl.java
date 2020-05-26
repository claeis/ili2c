package ch.interlis.ili2c.metamodel;

public class UniqueEl
{
  private java.util.ArrayList<ObjectPath> attrv=new java.util.ArrayList<ObjectPath>();
  public void addAttribute(ObjectPath attribute)
  {
    attrv.add(attribute);
  }
  public java.util.Iterator<ObjectPath> iteratorAttribute()
  {
    return attrv.iterator();
  }
  public ObjectPath[] getAttributes() {
      return attrv.toArray(new ObjectPath[attrv.size()]);
  }
}
