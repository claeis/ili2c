package ch.interlis.ili2c.metamodel;
import java.util.Iterator;
import java.util.ArrayList;


public class FormattedType extends BaseType {
	  private Domain baseDomain=null;
	  private Table baseClass=null;
	  private String minimum=null;
	  private String maximum=null;
	  private ArrayList baseAttrRef=null;
	  private String prefix=null;
  public Table getBaseClass() {
		return baseClass;
	}
	public void setBaseClass(Table baseClass) {
		this.baseClass = baseClass;
	}
	public Domain getBaseDomain() {
		return baseDomain;
	}
	public void setBaseDomain(Domain baseDomain) {
		this.baseDomain = baseDomain;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
  public boolean checkStructuralEquivalence (Element with)
  {
    if (!super.checkStructuralEquivalence (with))
      return false;

    return true;
  }
  public void addBaseAttrRef(FormattedTypeBaseAttrRef ref)
  {
	  if(baseAttrRef==null){
		  baseAttrRef=new ArrayList();
	  }
	  baseAttrRef.add(ref);
  }
  public Iterator iteratorBaseAttrRef()
  {
	  if(baseAttrRef==null){
		  return new ArrayList().iterator();
	  }
	  return baseAttrRef.iterator();
  }
public String getPrefix() {
	return prefix;
}
public void setPrefix(String prefix) {
	this.prefix = prefix;
}

}
