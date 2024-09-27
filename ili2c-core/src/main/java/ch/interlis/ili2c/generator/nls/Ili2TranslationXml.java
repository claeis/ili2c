package ch.interlis.ili2c.generator.nls;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.interlis.ili2c.metamodel.AbstractClassDef;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Enumeration;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.ExpressionSelection;
import ch.interlis.ili2c.metamodel.FormalArgument;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.Graphic;
import ch.interlis.ili2c.metamodel.LineForm;
import ch.interlis.ili2c.metamodel.MetaDataUseDef;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Parameter;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SignAttribute;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Unit;
import ch.interlis.ili2c.metamodel.View;

/**
 * Reads data from the ili file and creates a new XML document
 */

public class Ili2TranslationXml {

	private java.util.Map<String,NlsModelElement> elements = new java.util.HashMap<String,NlsModelElement>();
	public static final String METAOBJECT = ".METAOBJECT.";
	public static final String FR = "fr";
	public static final String IT = "it";
	public static final String EN = "en";
	public static final String DE = "de";

	/**
	 * it converts Transfer Description to Model Elements
	 * 
	 * @param td source data
	 * @param iliFile path of the source file
	 * @return all collected elements are returned
	 */
	public ModelElements convertTransferDescription2ModelElements(TransferDescription td) {

        Model models[]=td.getModelsFromLastFile();
        List<NlsModelElement> nlsEles=convertModels(models);
        ModelElements ret=new ModelElements();
        for(NlsModelElement nlsEle:nlsEles) {
            {
                TranslationElement ele=new TranslationElement();
                ele.setScopedName(nlsEle.getScopedName());
                ele.setElementType(nlsEle.getElementType());
                ele.setName_de(makeNotNull(nlsEle.getName(DE)));
                ele.setDocumentation_de(makeNotNull(nlsEle.getDocumentation(DE)));
                ele.setName_it(makeNotNull(nlsEle.getName(IT)));
                ele.setDocumentation_it(makeNotNull(nlsEle.getDocumentation(IT)));
                ele.setName_fr(makeNotNull(nlsEle.getName(FR)));
                ele.setDocumentation_fr(makeNotNull(nlsEle.getDocumentation(FR)));
                ele.setName_en(makeNotNull(nlsEle.getName(EN)));
                ele.setDocumentation_en(makeNotNull(nlsEle.getDocumentation(EN)));
                ret.add(ele);
            }
            List<String> metaAttrNames=nlsEle.getMetaAttrs();
            for(String metaAttrName:metaAttrNames) {
                String metaAttrValue=nlsEle.getMetaAttr(metaAttrName);
                if(metaAttrValue!=null) {
                    TranslationElement ele=new TranslationElement();
                    ele.setScopedName(getMetaAttributeScopedName(nlsEle.getScopedName(),metaAttrName));
                    ele.setElementType(ElementType.METAATTRIBUTE);
                    ele.setName_de(metaAttrValue);
                    ret.add(ele);
                }
            }
        }
		return ret;
	}
    private String makeNotNull(String val) {
        return val==null?"":val;
    }
    /**
	 * it writes Model Elements as a XML File
	 * 
	 * @param eles Structure of the XML
	 * @param fileName Destination of XML File 
	 * @throws Exception
	 */
	public static void writeModelElementsAsXML(ModelElements eles, File fileName) throws Exception {
		Class jaxbContextPath[] = { ModelElements.class };
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbContextPath);
		Marshaller ms = jaxbContext.createMarshaller();
		ms.marshal(eles, fileName);
	}

    public List<NlsModelElement> convertModels(Model models[]) {
        for(Model model:models) {
            Model rootModel=(Model) getElementInRootLanguage(model);
            NlsModelElement text = getNlsModelElement(rootModel.getScopedName());
            text.setElementType(getElementType(model));
            setModelElementAllLanguages(text, model);
            visitContainer(model);
        }
        
        java.util.List<NlsModelElement> ret=new java.util.ArrayList<NlsModelElement>();
        java.util.List<String> iliNames=new java.util.ArrayList<String>();
        iliNames.addAll(elements.keySet());
        java.util.Collections.sort(iliNames);
        for(String iliName:iliNames) {
            NlsModelElement nlsEle=elements.get(iliName);
            ret.add(nlsEle);
        }
        
        return ret;
    }

	/**
	 * it returns as a String the name of the Element
	 * 
	 * @param model related model element
	 * @return it returns related matches Element otherwise it returns empty String
	 */
	private String getElementType(Object model) {
		if (model instanceof Model) {
			return ElementType.MODEL;
		} else if (model instanceof AttributeDef) {
			return ElementType.ATTRIBUTE;
		} else if (model instanceof RoleDef) {
			return ElementType.ROLE;
		} else if (model instanceof Function) {
			return ElementType.FUNCTION;
		} else if (model instanceof Parameter) {
			return ElementType.PARAMETER;
        } else if (model instanceof FormalArgument) {
            return ElementType.FORMAL_ARGUMENT;
		} else if (model instanceof Domain) {
			return ElementType.DOMAIN;
		} else if (model instanceof LineForm) {
			return ElementType.LINE_FORM;
		} else if (model instanceof Unit) {
			return ElementType.UNIT;
		} else if (model instanceof Topic) {
			return ElementType.TOPIC;
		} else if (model instanceof MetaDataUseDef) {
			return ElementType.META_DATA_BASKET;
		} else if (model instanceof AbstractClassDef) {
			return findElementType(model);
		} else if (model instanceof AssociationDef) {
			return ElementType.ASSOCIATION;
		} else if (model instanceof View) {
			return ElementType.VIEW;
		} else if (model instanceof Graphic) {
			return ElementType.GRAPHIC;
		} else if (model instanceof Constraint) {
			return ElementType.CONSTRAINT;
		} else if (model instanceof ExpressionSelection) {
			return ElementType.EXPRESSION_SELECTION;
		} else if (model instanceof SignAttribute) {
			return ElementType.SIGN_ATTRIBUTE;
		} else if (model instanceof Enumeration.Element) {
			return ElementType.ENUMERATION_ELEMENT;
		} else {
			return "";
		}
	}

	/**
	 * it gives as a return parameter the Name of the Element type
	 * 
	 * @param model related model Element
	 * @return it returns name of the related matches Element as a String
	 * @throws IllegalArgumentException if do not matches name of the related Element
	 */
	private String findElementType(Object model) {
		AbstractClassDef abstractClassDefiniton = (AbstractClassDef) model;
		if (abstractClassDefiniton instanceof Table) {
			Table table = (Table) abstractClassDefiniton;
			if (table.isIdentifiable()) {
				return ElementType.CLASS;
			} else {
				return ElementType.STRUCTURE;
			}
		} else if (abstractClassDefiniton instanceof AssociationDef) {
			return ElementType.ASSOCIATION;
		} else {
			throw new IllegalArgumentException("Unexpected AbstractClassDef");
		}
	}

	/**
	 * Get Element in root language or itself.
	 * 
	 * @param ele  the Element to get the root translation of it. 
	 * @return the element in the root language or the element itself (if it is not translated).
	 */
	public static Element getElementInRootLanguage(Element ele) {
		Element baseLanguageElement = ele.getTranslationOf();
		while(baseLanguageElement != null) {
			ele = baseLanguageElement;
			baseLanguageElement = ele.getTranslationOf();
		}

		return ele;
	}

	/**
	 * inserts model element's data into the structure with all Languages
	 * 
	 * @param translationElement Structure of the XML
	 * @param modelElement related model Element
	 */
	private void setModelElementAllLanguages(NlsModelElement translationElement, Element modelElement) {
		String language = getLanguage(modelElement);
		setModelElementOneLanguage(translationElement, modelElement, language);
		Element baseLanguageElement = modelElement.getTranslationOf();
		// alle sprachen zu model
		while (baseLanguageElement != null) {
			language = getLanguage(baseLanguageElement);
			setModelElementOneLanguage(translationElement, baseLanguageElement, language);
			Element translatedElement = baseLanguageElement;
			baseLanguageElement = translatedElement.getTranslationOf();
		}
	}

	/**
	 * we can get the Language from element
	 * 
	 * @param ele Element to be picked up
	 * @return it gives return the name of the Element language
	 */
	public static String getLanguage(Element ele) {
		if (ele instanceof Model) {
			return ((Model) ele).getLanguage();
		}
		return ((Model) ele.getContainer(Model.class)).getLanguage();
	}

	/**
	 * Convert Model Element to TranslationElement
	 * 
	 * @param text Structure of the XML
	 * @param model related model Element
	 * @param language Language of the Element
	 */
	private void setModelElementOneLanguage(NlsModelElement text, Element model, String language) {
        if (model.getName() != null) {
            text.setName(language,model.getName());
        }
        if (model.getDocumentation() != null) {
            text.setDocumentation(language,model.getDocumentation());
        }
        visitMetaAttributes(text,model);
	}

	/**
	 * it gets from the TranslatedElementName related data with language and Insert
	 * in the Structure
	 * 
	 * @param text Structure of the XML(Structure of the ModelElements)
	 * @param Name Name of the Element
	 * @param Language Language of the Element
	 */
	private void setTranslationElementName(NlsModelElement text, String name, String language) {
        text.setName(language,name);
	}

	/**
	 * converts an Enumeration.Element to a TranslationElement.
	 * 
	 * @param nlsEle Structure of the XML
	 * @param element Enumeration element
	 * @param language Language of the Element
	 */
	private void visitEnumerationElement(NlsModelElement nlsEle, ch.interlis.ili2c.metamodel.Enumeration.Element element,
			String language) {
        nlsEle.setName(language,element.getName());
        nlsEle.setDocumentation(language,element.getDocumentation());
	}

	/**
	 * All Elements are assigned into the related fields.
	 * 
	 * @param Model related model Element
	 */
	private void visitContainer(Container model) {
		Iterator<Element> funcI = model.iterator();
		while (funcI.hasNext()) {
			Element ele = funcI.next();
			NlsModelElement dto = new NlsModelElement();
			dto.setScopedName(getElementInRootLanguage(ele).getScopedName());
			setModelElementAllLanguages(dto, ele);
			dto.setElementType(getElementType(ele));
			elements.put(dto.getScopedName(),dto);

			if (ele instanceof Container) {
				visitContainer((Container) ele);
			} else if (ele instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) ele;
				// If exist
				if (attr.getDomain() instanceof EnumerationType) {
					String text = getElementInRootLanguage(ele).getScopedName();
					visitEnumarationType((EnumerationType) attr.getDomain(), text, attr);
				}
			} else if (ele instanceof Domain) {
				Domain domain = (Domain) ele;
				if (domain.getType() instanceof EnumerationType) {
					String text = getElementInRootLanguage(ele).getScopedName();
					visitEnumarationType((EnumerationType) domain.getType(), text, domain);
				}
			} else if (ele instanceof Function) {
			    Function srcFunction = (Function) ele;
                final String srcEleLanguage = getLanguage(srcFunction);
                final String srcFunctionScopedName = getElementInRootLanguage(srcFunction).getScopedName();
			    FormalArgument[] srcArguments = srcFunction.getArguments();
			    for (int i = 0; i < srcArguments.length; i++) {
	                NlsModelElement translationElement = new NlsModelElement();
                    translationElement.setScopedName(srcFunctionScopedName + "." + getElementInRootLanguage(srcArguments[i]).getName());
                    translationElement.setElementType(getElementType(srcArguments[i]));
                    setModelElementOneLanguage(translationElement, srcArguments[i], srcEleLanguage);
			        
			        Element baseLanguageElement = srcFunction.getTranslationOf();
			        while (baseLanguageElement != null) {
                        Function baseLanguageFunction = (Function) baseLanguageElement;
                        FormalArgument[] baseLanguageArguments = baseLanguageFunction.getArguments();
                        setModelElementOneLanguage(translationElement, baseLanguageArguments[i], getLanguage(baseLanguageElement));
                        baseLanguageElement = baseLanguageFunction.getTranslationOf();
			        }
			        elements.put(translationElement.getScopedName(),translationElement);
			    }
			}
		}

	}

    private void visitMetaAttributes(NlsModelElement nlsEle,Element ele) {
        for (String metaAttrName : ele.getMetaValues().getValues()) {
            String metaAttrValue = ele.getMetaValue(metaAttrName);
            nlsEle.setMetaAttr(metaAttrName,metaAttrValue);
        }
    }
    private NlsModelElement getNlsModelElement(String scopedName) {
        if(elements.containsKey(scopedName)) {
            return elements.get(scopedName);
        }
        NlsModelElement nlsEle=new NlsModelElement();
        nlsEle.setScopedName(scopedName);
        elements.put(nlsEle.getScopedName(),nlsEle);
        return nlsEle;
    }

	/**
	 * it gives return, ScopedName of the Meta Attribute
	 * 
	 * @param eleScopedName ScopedName of the Element
	 * @param metaAttrName value of the metaAttribute
	 * @return it returns the full scoped name of the metaAttribute
	 */
	public static String getMetaAttributeScopedName(String eleScopedName, String metaAttrName) {
		return eleScopedName + Ili2TranslationXml.METAOBJECT + metaAttrName;
	}
	
	/**
	 * Prepare to get All Enumeration data
	 * 
	 * @param et Related Enumeration Elements
	 * @param scopedNamePrefix Scope Name
	 * @param domainOrAttr Model Element
	 */
	private void visitEnumarationType(EnumerationType et, String scopedNamePrefix, Element domainOrAttr) {
		Enumeration enumeration = et.getEnumeration();
		visitEnumaration(enumeration, scopedNamePrefix, domainOrAttr);
	}

	/**
	 * it gets all Enumeration and SubEnumeration data and insert into Structure with language
	 * 
	 * @param enumeration Related Enumeration Elements
	 * @param scopedNamePrefix Scope Name
	 * @param domainOrAttr Model Element
	 */
	private void visitEnumaration(Enumeration enumeration, String scopedNamePrefix, Element domainOrAttr) {
		Iterator<ch.interlis.ili2c.metamodel.Enumeration.Element> enumarationIterator = enumeration.getElements();

		while (enumarationIterator.hasNext()) {
			Enumeration.Element enumEle = enumarationIterator.next();
			NlsModelElement nlsEle = new NlsModelElement();

			// ScopedName
			String scopedName = scopedNamePrefix + "." + getEnumerationElementInRootLanguage(enumEle).getName();
			nlsEle.setScopedName(scopedName);

			String language = getLanguage(domainOrAttr);
			visitEnumerationElement(nlsEle, enumEle, language);
            visitEnumerationElementMetaAttributes(nlsEle,enumEle);
			Enumeration.Element baseLanguageElement = enumEle.getTranslationOf();
			Element baseLanguageModelElement = domainOrAttr.getTranslationOf();
			while (baseLanguageElement != null) {
				String baseLanguage = getLanguage(baseLanguageModelElement);
				visitEnumerationElement(nlsEle, baseLanguageElement, baseLanguage);
	            visitEnumerationElementMetaAttributes(nlsEle,baseLanguageElement);
				baseLanguageElement = baseLanguageElement.getTranslationOf();
				baseLanguageModelElement = baseLanguageModelElement.getTranslationOf();
			}

			nlsEle.setElementType(getElementType(enumEle));
			elements.put(nlsEle.getScopedName(),nlsEle);
			if (enumEle.getSubEnumeration() != null) {
				visitEnumaration(enumEle.getSubEnumeration(), scopedName, domainOrAttr);
			}
		}
	}

	private void visitEnumerationElementMetaAttributes(NlsModelElement nlsEle,ch.interlis.ili2c.metamodel.Enumeration.Element ele) {
        for (String metaAttrName : ele.getMetaValues().getValues()) {
            String metaAttrValue = ele.getMetaValues().getValue(metaAttrName);
            nlsEle.setMetaAttr(metaAttrName,metaAttrValue);
        }
    }

	/**
	 * check if element contains a root language.
	 * 
	 * @param ele check the Element if it contains the root elements.
	 * @return root language element
	 */
	private static ch.interlis.ili2c.metamodel.Enumeration.Element getEnumerationElementInRootLanguage(
			Enumeration.Element ele) {
		ch.interlis.ili2c.metamodel.Enumeration.Element baseLanguageElement = ele.getTranslationOf();
		if (baseLanguageElement != null) {
			ele = baseLanguageElement;
			baseLanguageElement = ele.getTranslationOf();
		}

		return ele;
	}

	/**
	 * According to File parameter it reads Model Element from the XML
	 * 
	 * @param File path of the source file
	 * @return it returns a modelElements
	 */
	public static ModelElements readModelElementsXml(File file) throws Exception {
		Unmarshaller um = Ili2TranslationXml.createUnmarshaller();
		ModelElements modelElements = (ModelElements) um.unmarshal(file);
		return modelElements;
	}

	/**
	 * Settings for the XML Data so it will be ready to Read XML Folder
	 * 
	 * @return Unmarshaller XML Settings parameter
	 */
	private static Unmarshaller createUnmarshaller() throws JAXBException {
		Class jaxbContextPath[] = { ModelElements.class };
		JAXBContext jaxbContext = JAXBContext.newInstance(jaxbContextPath);
		Unmarshaller um = jaxbContext.createUnmarshaller();
		return um;
	}

}
