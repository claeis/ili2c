package ch.interlis.ili2c.metamodel;
import java.util.Iterator;
import java.util.ArrayList;
import ch.ehi.basics.logging.EhiLogger;


public class FormattedType extends BaseType {
	  private Domain baseDomain=null;
	  private Table baseClass=null;
	  private String minimum=null;
	  private String maximum=null;
	  private ArrayList baseAttrRef=null;
	  private String prefix=null;
  public Table getDefinedBaseStruct() {
		return baseClass;
	}
  	public Table getBaseStruct(){
		Table baseStruct=null;
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			FormattedType structFormatType=(FormattedType)baseDomain.getType();
			return structFormatType.getBaseStruct();
		}
		baseStruct=getDefinedBaseStruct();
		// if this attribute or domaindef has no baseClass?
		if(baseStruct==null){
			// try super
			baseStruct=((FormattedType)getExtending()).getBaseStruct();
		}
		return baseStruct;
  	}
	public void setBaseStruct(Table baseClass) {
		this.baseClass = baseClass;
	}
	public Domain getDefinedBaseDomain() {
		return baseDomain;
	}
	public void setBaseDomain(Domain baseDomain) {
		this.baseDomain = baseDomain;
	}
	public String getDefinedMaximum() {
		return maximum;
	}
	public String getMaximum() {
		if(maximum!=null){
			return maximum;
		}
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			FormattedType structFormatType=(FormattedType)baseDomain.getType();
			return structFormatType.getMaximum();
		}
		String max=null;
		FormattedType baseFormatType=((FormattedType)getExtending());
		while(baseFormatType!=null){
			max=baseFormatType.maximum;
			if(max!=null){
				break;
			}
			baseFormatType=(FormattedType)baseFormatType.getExtending();
		}
		if(max==null){
			// build max from struct
			StringBuffer fmt=new StringBuffer();
			FormattedType lastParent=null;
			Iterator baseAttri=iteratorBaseAttrRef();
			while(baseAttri.hasNext()){
				ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef baseAttrRef=(ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef)baseAttri.next();
				if(lastParent!=baseAttrRef.getParent()){
					lastParent=baseAttrRef.getParent();
					String prefix=lastParent.getDefinedPrefix();
					if(prefix!=null){
						fmt.append(prefix);
					}
				}
				if(baseAttrRef.getFormatted()!=null){
					fmt.append(((FormattedType)baseAttrRef.getFormatted().getType()).getMaximum());
				}else{
					fmt.append(((NumericType)baseAttrRef.getAttr().getDomainResolvingAliases()).getMaximum().toString());
				}
				if(baseAttrRef.getPostfix()!=null){
					fmt.append(baseAttrRef.getPostfix());
				}
			}
			max=fmt.toString();
		}
		return max;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	public String getDefinedMinimum() {
		return minimum;
	}
	public String getMinimum() {
		if(minimum!=null){
			return minimum;
		}
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			FormattedType structFormatType=(FormattedType)baseDomain.getType();
			return structFormatType.getMinimum();
		}
		String min=null;
		FormattedType baseFormatType=((FormattedType)getExtending());
		while(baseFormatType!=null){
			min=baseFormatType.minimum;
			if(min!=null){
				break;
			}
			baseFormatType=(FormattedType)baseFormatType.getExtending();
		}
		if(min==null){
			// build min from struct
			StringBuffer fmt=new StringBuffer();
			FormattedType lastParent=null;
			Iterator baseAttri=iteratorBaseAttrRef();
			while(baseAttri.hasNext()){
				ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef baseAttrRef=(ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef)baseAttri.next();
				if(lastParent!=baseAttrRef.getParent()){
					lastParent=baseAttrRef.getParent();
					String prefix=lastParent.getDefinedPrefix();
					if(prefix!=null){
						fmt.append(prefix);
					}
				}
				if(baseAttrRef.getFormatted()!=null){
					fmt.append(((FormattedType)baseAttrRef.getFormatted().getType()).getMinimum());
				}else{
					fmt.append(((NumericType)baseAttrRef.getAttr().getDomainResolvingAliases()).getMinimum().toString());
				}
				if(baseAttrRef.getPostfix()!=null){
					fmt.append(baseAttrRef.getPostfix());
				}
			}
			min=fmt.toString();
		}
		return min;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public PrecisionDecimal[] valueOf(String value) 
	{
		String regexp=getRegExp();
		//EhiLogger.debug("value "+value);
		//EhiLogger.debug("regexp "+regexp);
		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile(regexp);
		java.util.regex.Matcher matcher=pattern.matcher(value);
		if(!matcher.matches()){
			throw new NumberFormatException();
		}
		PrecisionDecimal ret[]=new PrecisionDecimal[matcher.groupCount()];
		for(int i=1;i<=matcher.groupCount();i++){
			//EhiLogger.debug(matcher.group(i));
			ret[i-1]=new PrecisionDecimal(matcher.group(i));
		}
		return ret;
	}
	public String getRegExp() 
	{
		FormattedType format=this;
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			format=(FormattedType)baseDomain.getType();
		}
		StringBuffer fmt=new StringBuffer();
		FormattedType lastParent=null;
		Iterator baseAttri=format.iteratorBaseAttrRef();
		while(baseAttri.hasNext()){
			ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef baseAttrRef=(ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef)baseAttri.next();
			if(lastParent!=baseAttrRef.getParent()){
				lastParent=baseAttrRef.getParent();
				String prefix=lastParent.getDefinedPrefix();
				if(prefix!=null){
					fmt.append(prefix);
				}
			}
			if(baseAttrRef.getFormatted()!=null){
				fmt.append(((FormattedType)baseAttrRef.getFormatted().getType()).getRegExp());
			}else{
				fmt.append("([\\+\\-0-9\\.]+)");
			}
			if(baseAttrRef.getPostfix()!=null){
				fmt.append(baseAttrRef.getPostfix());
			}
		}
		return fmt.toString();
	}
	public boolean isValueInRange(String value) {
		PrecisionDecimal minimum[] = valueOf(getMinimum());
		PrecisionDecimal maximum[] = valueOf(getMaximum());
		PrecisionDecimal val[] = valueOf(value);
		for(int i=0;i<val.length;i++){
			if(val[i].compareTo(minimum[i])==-1 || val[i].compareTo(maximum[i])==1){
				return false;
			}
		}
		return true;
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
  public Iterator iteratorDefinedBaseAttrRef()
  {
	  if(baseAttrRef==null){
		  return new ArrayList().iterator();
	  }
	  return baseAttrRef.iterator();
  }
  public Iterator iteratorBaseAttrRef()
  {
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			FormattedType structFormatType=(FormattedType)baseDomain.getType();
			return structFormatType.iteratorBaseAttrRef();
		}
  		ArrayList ret=new ArrayList();
  		if(baseAttrRef!=null){
  			ret.addAll(baseAttrRef);
  		}
		FormattedType baseFormatType=((FormattedType)getExtending());
		while(baseFormatType!=null){
			if(baseFormatType.baseAttrRef!=null){
				ret.addAll(0, baseFormatType.baseAttrRef);
			}
			baseFormatType=((FormattedType)baseFormatType.getExtending());
		}
		return ret.iterator();
	}
public String getDefinedPrefix() {
	return prefix;
}
public String getPrefix() {
	if(prefix!=null){
		return prefix;
	}
	if(getDefinedBaseDomain()!=null){
		// 'FORMAT' FormattedType-DomainRef
		Domain baseDomain=getDefinedBaseDomain();
		FormattedType structFormatType=(FormattedType)baseDomain.getType();
		return structFormatType.getPrefix();
	}
	FormattedType baseFormatType=((FormattedType)getExtending());
	if(baseFormatType==null){
		return null;
	}
	return baseFormatType.getPrefix();
}
public void setPrefix(String prefix) {
	this.prefix = prefix;
}

	public String getFormat()
	{
		StringBuffer fmt=new StringBuffer();
		FormattedType lastParent=null;
		Iterator baseAttri=iteratorBaseAttrRef();
		while(baseAttri.hasNext()){
			ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef baseAttrRef=(ch.interlis.ili2c.metamodel.FormattedTypeBaseAttrRef)baseAttri.next();
			if(lastParent!=baseAttrRef.getParent()){
				lastParent=baseAttrRef.getParent();
				String prefix=lastParent.getDefinedPrefix();
				if(prefix!=null){
					fmt.append("\""+prefix+"\"");
				}
			}
			fmt.append(baseAttrRef.getAttr().getName());
			if(baseAttrRef.getFormatted()!=null){
				fmt.append("/");
				fmt.append(baseAttrRef.getFormatted().getScopedName(null));
			}else if(baseAttrRef.getIntPos()>0){
				fmt.append("/");
				fmt.append(baseAttrRef.getIntPos());
			}
			if(baseAttrRef.getPostfix()!=null){
				fmt.append("\""+baseAttrRef.getPostfix()+"\"");
			}
		}
		return fmt.toString();
	}
	  void checkTypeExtension (Type wantToExtend)
	  {
	    if ((wantToExtend == null)
	      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
	      return;
	    if (!(wantToExtend.getClass().equals(this.getClass()))){
	        throw new Ili2cSemanticException (rsrc.getString (
	        "err_type_ExtOther"));
	    }
	  } 
}
