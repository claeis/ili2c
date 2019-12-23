package ch.interlis.ili2c.metamodel;

import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;


public class FormattedType extends BaseType {
	  private Domain baseDomain=null;
	  private Table baseClass=null;
	  private String minimum=null;
	  private String maximum=null;
	  private ArrayList<FormattedTypeBaseAttrRef> baseAttrRef;
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
			StringBuilder fmt=new StringBuilder();
			FormattedType lastParent=null;
			Iterator<FormattedTypeBaseAttrRef> baseAttri = iteratorBaseAttrRef();
			while(baseAttri.hasNext()){
				FormattedTypeBaseAttrRef baseAttrRef = baseAttri.next();
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
			StringBuilder fmt=new StringBuilder();
			FormattedType lastParent=null;
			Iterator<FormattedTypeBaseAttrRef> baseAttri = iteratorBaseAttrRef();
			while(baseAttri.hasNext()){
				FormattedTypeBaseAttrRef baseAttrRef = baseAttri.next();
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
		StringBuilder fmt=new StringBuilder();
		FormattedType lastParent=null;
		Iterator<FormattedTypeBaseAttrRef> baseAttri = format.iteratorBaseAttrRef();
		while(baseAttri.hasNext()){
			FormattedTypeBaseAttrRef baseAttrRef = baseAttri.next();
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
		ArrayList<FormattedTypeBaseAttrRef> bases=new ArrayList<FormattedTypeBaseAttrRef>();
		for(Iterator<FormattedTypeBaseAttrRef> basei=iteratorBaseAttrRef();basei.hasNext();){
			FormattedTypeBaseAttrRef base=basei.next();
			bases.add(base);
		}
		boolean isMin=true;
		boolean isMax=true;
		for(int i=0;i<val.length;i++){
			if(!isMin){
				int minCompare = val[i].compareTo((((NumericType)bases.get(i).getAttr().getDomainResolvingAliases()).getMinimum()));
				if(minCompare==-1){
					return false;
				}
			}else{
				int minCompare = val[i].compareTo(minimum[i]);
				if(minCompare==-1){
					return false;
				}else if(minCompare==0){
					isMin=true;
				}else{
					isMin=false;
				}
			}
			if(!isMax){
				int maxCompare = val[i].compareTo((((NumericType)bases.get(i).getAttr().getDomainResolvingAliases()).getMaximum()));
				if(maxCompare==1){
					return false;
				}
			}else{
				int maxCompare = val[i].compareTo(maximum[i]);
				if(maxCompare==1){
					return false;
				}else if(maxCompare==0){
					isMax=true;
				}else{
					isMax=false;
				}
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
		  baseAttrRef=new ArrayList<FormattedTypeBaseAttrRef>();
	  }
	  baseAttrRef.add(ref);
  }
  public Iterator<FormattedTypeBaseAttrRef> iteratorDefinedBaseAttrRef()
  {
	  if(baseAttrRef==null){
	      List<FormattedTypeBaseAttrRef> empty = Collections.emptyList();
	      return empty.iterator();
	  }
	  return baseAttrRef.iterator();
  }
  public Iterator<FormattedTypeBaseAttrRef> iteratorBaseAttrRef()
  {
		if(getDefinedBaseDomain()!=null){
			// 'FORMAT' FormattedType-DomainRef
			Domain baseDomain=getDefinedBaseDomain();
			FormattedType structFormatType=(FormattedType)baseDomain.getType();
			return structFormatType.iteratorBaseAttrRef();
		}
  		ArrayList<FormattedTypeBaseAttrRef> ret = new ArrayList<FormattedTypeBaseAttrRef>();
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
		StringBuilder fmt=new StringBuilder();
		FormattedType lastParent=null;
		Iterator<FormattedTypeBaseAttrRef> baseAttri = iteratorBaseAttrRef();
		while(baseAttri.hasNext()){
			FormattedTypeBaseAttrRef baseAttrRef = baseAttri.next();
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
	    checkCardinalityExtension(wantToExtend);
	  }


    public FormattedType clone() {
        FormattedType cloned = (FormattedType) super.clone();

        if (baseAttrRef != null) {
            int sz = baseAttrRef.size();

            cloned.baseAttrRef = new ArrayList<FormattedTypeBaseAttrRef>(sz);
            for (int i = 0; i < sz; ++i) {
                cloned.baseAttrRef.add(baseAttrRef.get(i).newParent(cloned));
            }
        }
        return cloned;
    }
    @Override
    protected void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
    {
        super.checkTranslationOf(errs,name,baseName);
        FormattedType   origin=(FormattedType)getTranslationOf();

        if (origin == null){
            return;
        }

        if(minimum==null && origin.minimum==null) {
        }else {
            if(minimum==null || origin.minimum==null) {
                throw new Ili2cSemanticException();
            }
            if (!minimum.equals(origin.minimum)) {
                throw new Ili2cSemanticException();
            }
        }
        if(maximum==null && origin.maximum==null) {
        }else {
            if(maximum==null || origin.maximum==null) {
                throw new Ili2cSemanticException();
            }
            if (!maximum.equals(origin.maximum)) {
                throw new Ili2cSemanticException();
            }
        }
        if(prefix==null && origin.prefix==null) {
        }else {
            if(prefix==null || origin.prefix==null) {
                throw new Ili2cSemanticException();
            }
            if (!prefix.equals(origin.prefix)) {
                throw new Ili2cSemanticException();
            }
        }
        if(!Element.equalElementRef(baseDomain,origin.baseDomain)) {
            throw new Ili2cSemanticException();
        }
        if(!Element.equalElementRef(baseClass,origin.baseClass)) {
            throw new Ili2cSemanticException();
        }
        if(baseAttrRef==null && origin.baseAttrRef==null) {
            
        }else {
            if(baseAttrRef==null || origin.baseAttrRef==null) {
                throw new Ili2cSemanticException();
            }
            if(baseAttrRef.size()!=origin.baseAttrRef.size()) {
                throw new Ili2cSemanticException();
            }
            for(int i=0;i<baseAttrRef.size();i++) {
                FormattedTypeBaseAttrRef attr = baseAttrRef.get(i);
                FormattedTypeBaseAttrRef originAttr = origin.baseAttrRef.get(i);
                if(!Element.equalElementRef(attr.getAttr(),originAttr.getAttr())){
                    throw new Ili2cSemanticException();
                }
                if(!Element.equalElementRef(attr.getFormatted(),originAttr.getFormatted())){
                    throw new Ili2cSemanticException();
                }
                if(attr.getIntPos()!=originAttr.getIntPos()){
                    throw new Ili2cSemanticException();
                }
                if(attr.getPostfix()==null && originAttr.getPostfix()==null){
                }else {
                    if(attr.getPostfix()==null || originAttr.getPostfix()==null){
                        throw new Ili2cSemanticException();
                    }
                    if(!attr.getPostfix().equals(originAttr.getPostfix())){
                        throw new Ili2cSemanticException();
                    }
                }
                
            }
        }
    }

}
