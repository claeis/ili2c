/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.ehi.iox.adddefval;

import ch.interlis.ili2c.metamodel.*;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.EnumCodeMapper;

import java.util.*;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.parser.Ili1Undef;

/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 26.03.2007 $
 */
public class Converter {
	//	map<String classname, map<String attr,ReferencedTextLength | String defvalue>
	private HashMap defvalueSettings=new HashMap(); 
	//	map<String classname, TargetObjects>
	private HashMap textTables=new HashMap(); 
	private boolean readEnumValAsItfCode=false;
	public Converter(TransferDescription td)
	{
		this(td,false);
	}
	public Converter(TransferDescription td,boolean readEnumValAsItfCode)
	{
		this.readEnumValAsItfCode=readEnumValAsItfCode;
		Iterator modeli=td.iterator();
		while(modeli.hasNext()){
			Object modelo=modeli.next();
			if(modelo instanceof Model){
				Model model=(Model)modelo;
				Iterator topici=model.iterator();
				while(topici.hasNext()){
					Object topico=topici.next();
					if(topico instanceof Topic){
						Topic topic=(Topic)topico;
						Iterator tablei=topic.iterator();
						while(tablei.hasNext()){
							Object tableo=tablei.next();
							if(tableo instanceof AbstractClassDef){
								AbstractClassDef table=(AbstractClassDef)tableo;
								visitAbstractClassDef(table);
							}
						}
					}
				}
			}
		}
	}
	private void visitAbstractClassDef(AbstractClassDef table)
	{
		RoleDef refAttr=null;
		Iterator attri=table.getAttributesAndRoles2();
		while(attri.hasNext()){
			ViewableTransferElement attro=(ViewableTransferElement)attri.next();
			if(attro.obj instanceof AttributeDef){
			}else if(attro.obj instanceof RoleDef){
				refAttr=(RoleDef)attro.obj;
				break;
			}
		}
		attri=table.getAttributesAndRoles2();
		while(attri.hasNext()){
			ViewableTransferElement attro=(ViewableTransferElement)attri.next();
			if(attro.obj instanceof AttributeDef){
				AttributeDef attr=(AttributeDef)attro.obj;
				String expl=attr.getExplanation();
				if(expl!=null && expl.trim().startsWith("undefiniert")){
					if(attr.getDomain().isMandatory()){
						EhiLogger.logAdaption(attr.toString()+": mandatory attribute; default value statement ignored ("+expl+")");
					}else{
						//EhiLogger.debug(attr.toString()+": "+expl);
						Evaluable value=Ili1Undef.parseValueIfUndefined(attr.toString(),expl);
						//EhiLogger.debug("defvalue: "+value);
						if(value!=null){
							if(value instanceof LengthOfReferencedText && refAttr==null){
								EhiLogger.logAdaption(attr.toString()+": no reference attribute in table; default value statement ignored ("+expl+")");
							}else{
								addDefValue(table,attr,refAttr,value);
							}
						}
					}
				}
			}
		}
	}
	private EnumCodeMapper enumTypes=new EnumCodeMapper();
	private void addDefValue(AbstractClassDef table,AttributeDef attr,RoleDef refattr,Evaluable value)
	{
		String className=table.getScopedName(null);
		HashMap defvalues=null;
		if(defvalueSettings.containsKey(className)){
			defvalues=(HashMap)defvalueSettings.get(className); 
		}else{
			defvalues=new HashMap();
			defvalueSettings.put(className,defvalues); 
		}
		if(value instanceof Constant.Enumeration){
			String vals[]=((Constant.Enumeration)value).getValue();
			StringBuffer val=new StringBuffer();
			String sep="";
			for(int i=0;i<vals.length;i++){
				val.append(sep);
				val.append(vals[i]);
				sep=".";
			}
			String v=val.toString();
			ch.interlis.ili2c.metamodel.Type type=attr.getDomainResolvingAliases();
			String itfCode=v;
			if(readEnumValAsItfCode){
				itfCode=enumTypes.mapXtfCode2ItfCode((ch.interlis.ili2c.metamodel.EnumerationType)type, v);
				if(itfCode==null){
					EhiLogger.logAdaption(className+" : unexpected default value <"+v+"> for attribute "+attr.getName()+"; read without default value");
					itfCode=v;
				}
			}
			defvalues.put(attr.getName(),itfCode);
		}else if(value instanceof Constant.Numeric){
			String v=((Constant.Numeric)value).getValue().toIli1String();
			defvalues.put(attr.getName(),v);
		}else if(value instanceof Constant.Text){
			String v=((Constant.Text)value).getValue();
			defvalues.put(attr.getName(),v);
		}else if(value instanceof LengthOfReferencedText){
			if(refattr==null){
				throw new IllegalArgumentException("no reference attribute");
			}
			TargetObjects targetObjects=addTarget(refattr.getDestination());
			ReferencedTextLength v=null;
			v=new ReferencedTextLength();
			v.setRefattr(refattr);
			v.setTargetObjects(targetObjects);
			defvalues.put(attr.getName(),v);
		}else{
			throw new IllegalArgumentException(value.getClass().getName());
		}
	}
	private TargetObjects addTarget(AbstractClassDef table)
	{
		String className=table.getScopedName(null);
		if(textTables.containsKey(className)){
			return (TargetObjects)textTables.get(className);
		}
		AttributeDef txtAttr=null;
		Iterator attri=table.getAttributes();
		while(attri.hasNext()){
			AttributeDef attr=(AttributeDef)attri.next();
			if(attr.getDomainResolvingAliases() instanceof TextType){
				txtAttr=attr;
				break;
			}
		}
		if(txtAttr==null){
			throw new IllegalArgumentException("no TEXT attribute in table "+table.getScopedName(null));
		}
		//EhiLogger.debug("txtAttr "+txtAttr);
		TargetObjects targetObjects=new TargetObjects(txtAttr);
		textTables.put(className,targetObjects);
		return targetObjects;
	}
	public void convert(IomObject iomObj)
	{
		// get classname
		String className=iomObj.getobjecttag();
		// any attributes with default value settings?
		if(defvalueSettings.containsKey(className)){
			HashMap defvalues=(HashMap)defvalueSettings.get(className); 
			// for all of them
			Iterator attri=defvalues.keySet().iterator();
			while(attri.hasNext()){
				String attr=(String)attri.next();
				// if attr undefined
				if(iomObj.getattrvalue(attr)==null){
					// set default value
					Object valueo=defvalues.get(attr);
					if(valueo instanceof String){
						//EhiLogger.debug("set attr "+attr+"="+(String)valueo);
						iomObj.setattrvalue(attr,(String)valueo);
					}else if(valueo instanceof ReferencedTextLength){
						ReferencedTextLength refLen=(ReferencedTextLength)valueo;
						TargetObjects targetObjects=refLen.getTargetObjects();
						String refAttr=refLen.getRefattr().getName();
						String ref=getRef(iomObj,refAttr);
						String value=targetObjects.getTextLength(ref);
						if(value==null){
							EhiLogger.logError(className+" "+iomObj.getobjectoid()+": dangling reference (attribute "+refAttr+"="+ref+")");
						}else{
							if(!value.equals("@")){
								//EhiLogger.debug("set attr "+attr+"="+value);
								iomObj.setattrvalue(attr,value);
							}
						}
					}
				}
			}
		}
		if(textTables.containsKey(className)){
			TargetObjects targetObjs=(TargetObjects)textTables.get(className);
			String txtAttr=targetObjs.getTxtAttr().getName();
			String txt=iomObj.getattrvalue(txtAttr);
			String txtLen="@";
			if(txt!=null){
				txtLen=Integer.toString(txt.length());
			}
			targetObjs.setTextLength(iomObj.getobjectoid(),txtLen);
		}
	}
	private String getRef(IomObject iomObj,String refAttr)
	{
		IomObject ref=iomObj.getattrobj(refAttr,0);
		if(ref==null){
			return null;
		}
		return ref.getobjectrefoid();
	}
}
