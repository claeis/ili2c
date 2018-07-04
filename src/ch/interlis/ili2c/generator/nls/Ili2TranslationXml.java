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

	private ModelElements elements = new ModelElements();
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

		readAllModelsFromTransferDesc(td);
		return elements;
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

	/**
	 * it reads and filter from the incoming td data
	 * 
	 * @param td all ili models
	 * @param file read only models from this file
	 */
	private void readAllModelsFromTransferDesc(TransferDescription td) {
	    Model models[]=td.getModelsFromLastFile();
	    HashSet<Model> visitedModels=new HashSet<Model>();
	    ArrayList<Model> mostSpecificModels=new ArrayList<Model>();
	    for(int modeli=models.length-1;modeli>=0;modeli--) {
	        Model model=models[modeli];
	        if(!visitedModels.contains(model)) {
	            visitedModels.add(model);
	            mostSpecificModels.add(0,model);
	            Model translatedModel=(Model)model.getTranslationOf();
	            while(translatedModel!=null) {
	                visitedModels.add(translatedModel);
	                translatedModel=(Model)translatedModel.getTranslationOf();
	            }
	        }
	    }
		for(Model model:mostSpecificModels) {

			TranslationElement text = new TranslationElement();
			allFieldsWithSetTOEmpty(text);
			text.setScopedName(getElementInRootLanguage(model).getScopedName());

			text.setElementType(getElementType(model));
			setModelElementAllLanguages(text, model);
			printModelElement(text);

			visitAllElements(model);
		}
	}

	/**
	 * it shows whether the model is translated
	 * 
	 * @param model related model Element
	 * @return If the model has a translation then it return the name of the
	 *         translated model name otherwise it return empty String
	 */
	private String hasATranslation(Model model) {

		if (model.getTranslationOf() != null) {
			return model.getTranslationOf().getName();
		}
		return "";
	}

	/**
	 * the file path of the model is checking whether the given file path matches.
	 * 
	 * @param model related model element
	 * @param file path of the source file
	 * @return it returns true if the both file path matches otherwise it returns false
	 */
	private boolean controlOfTheFileName(Model model, String file) {

		if (model.getFileName() == null) {
			return true;
		}

		File fileNameFromModel = new File(model.getFileName());
		File fileNameFromIli = new File(file);
		if (!fileNameFromModel.getName().equals(fileNameFromIli.getName())) {
			return true;
		}
		return false;
	}

	/**
	 * in the beginning, it makes all fields of the Structure null 
	 * 
	 * @param text Structure of the data XML
	 */
	private void allFieldsWithSetTOEmpty(TranslationElement text) {
		text.setDocumentation_de("");
		text.setDocumentation_en("");
		text.setDocumentation_fr("");
		text.setDocumentation_it("");

		text.setName_de("");
		text.setName_en("");
		text.setName_fr("");
		text.setName_it("");

		text.setScopedName("");
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
		if (baseLanguageElement != null) {
			ele = baseLanguageElement;
			baseLanguageElement = ele.getTranslationOf();
		}

		return ele;
	}

	/**
	 * Insert the Structure into the ArrayList
	 * 
	 * @param text Structure of the XML with data
	 */
	private void printModelElement(TranslationElement text) {
		elements.add(text);
	}

	/**
	 * inserts model element's data into the structure with all Languages
	 * 
	 * @param text Structure of the XML
	 * @param model related model Element
	 */
	private void setModelElementAllLanguages(TranslationElement text, Element model) {
		String language = getLanguage(model);
		convertModelElement(text, model, language);
		Element baseLanguageElement = model.getTranslationOf();
		// alle sprachen zu model
		while (baseLanguageElement != null) {
			language = getLanguage(baseLanguageElement);
			convertModelElement(text, baseLanguageElement, language);
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
	private void convertModelElement(TranslationElement text, Element model, String language) {
		if (language == null) {
			if (model.getName() != null) {
				text.setName_de(model.getName());
			}
			if (model.getDocumentation() != null) {
				text.setDocumentation_de(model.getDocumentation());
			}
		} else {
			if (language.equals(Ili2TranslationXml.DE)) {
				if (model.getName() != null) {
					text.setName_de(model.getName());
				}
				if (model.getDocumentation() != null) {
					text.setDocumentation_de(model.getDocumentation());
				}
			} else if (language.equals(Ili2TranslationXml.FR)) {
				if (model.getName() != null) {
					text.setName_fr(model.getName());
				}
				if (model.getDocumentation() != null) {
					text.setDocumentation_fr(model.getDocumentation());
				}
			} else if (language.equals(Ili2TranslationXml.IT)) {
				if (model.getName() != null) {
					text.setName_it(model.getName());
				}
				if (model.getDocumentation() != null) {
					text.setDocumentation_it(model.getDocumentation());
				}
			} else if (language.equals(Ili2TranslationXml.EN)) {
				if (model.getName() != null) {
					text.setName_en(model.getName());
				}
				if (model.getDocumentation() != null) {
					text.setDocumentation_en(model.getDocumentation());
				}
			}
		}
	}

	/**
	 * it gets from the TranslatedElementName related data with language and Insert
	 * in the Structure
	 * 
	 * @param text Structure of the XML(Structure of the ModelElements)
	 * @param Name Name of the Element
	 * @param Language Language of the Element
	 */
	private void setTranslationElementName(TranslationElement text, String name, String language) {
		if (language == null) {
			text.setName_de(name);
		} else {
			if (language.equals(Ili2TranslationXml.DE)) {
				text.setName_de(name);
			} else if (language.equals(Ili2TranslationXml.FR)) {
				text.setName_fr(name);
			} else if (language.equals(Ili2TranslationXml.IT)) {
				text.setName_it(name);
			} else if (language.equals(Ili2TranslationXml.EN)) {
				text.setName_en(name);
			}
		}
	}

	/**
	 * converts an Enumeration.Element to a TranslationElement.
	 * 
	 * @param text Structure of the XML
	 * @param element Enumeration element
	 * @param language Language of the Element
	 */
	private void convertEnumerationElement(TranslationElement text, ch.interlis.ili2c.metamodel.Enumeration.Element element,
			String language) {
		if (language == null) {
			text.setName_de(element.getName());
			text.setDocumentation_de(element.getDocumentation());
		} else {
			if (language.equals(Ili2TranslationXml.DE)) {
				text.setName_de(element.getName());
				text.setDocumentation_de(element.getDocumentation());
			} else if (language.equals(Ili2TranslationXml.FR)) {
				text.setName_fr(element.getName());
				text.setDocumentation_fr(element.getDocumentation());
			} else if (language.equals(Ili2TranslationXml.IT)) {
				text.setName_it(element.getName());
				text.setDocumentation_de(element.getDocumentation());
			} else if (language.equals(Ili2TranslationXml.EN)) {
				text.setName_en(element.getName());
				text.setDocumentation_en(element.getDocumentation());
			}
		}
	}

	/**
	 * All Elements are assigned into the related fields.
	 * 
	 * @param Model related model Element
	 */
	private void visitAllElements(Container model) {
		Iterator<Element> funcI = model.iterator();
		while (funcI.hasNext()) {
			Element ele = funcI.next();
			TranslationElement dto = new TranslationElement();
			dto.setScopedName(getElementInRootLanguage(ele).getScopedName());
			setModelElementAllLanguages(dto, ele);
			dto.setElementType(getElementType(ele));
			addTranslationElementForMetaValues(ele);
			printModelElement(dto);

			if (ele instanceof Container) {
				visitAllElements((Container) ele);
			} else if (ele instanceof AttributeDef) {
				AttributeDef attr = (AttributeDef) ele;
				// If exist
				if (attr.getDomain() instanceof EnumerationType) {
					String text = getElementInRootLanguage(ele).getScopedName();
					prepareAllEnumaration((EnumerationType) attr.getDomain(), text, attr);
				}
			} else if (ele instanceof Domain) {
				Domain domain = (Domain) ele;
				if (domain.getType() instanceof EnumerationType) {
					String text = getElementInRootLanguage(ele).getScopedName();
					prepareAllEnumaration((EnumerationType) domain.getType(), text, domain);
				}
			} else if (ele instanceof Function) {
			    TranslationElement traslationElement = new TranslationElement();
			    Function function = (Function) ele;
			    FormalArgument[] arguments = function.getArguments();
			    for (int i = 0; i < arguments.length; i++) {
			        convertModelElement(traslationElement, arguments[i], getLanguage(function));
			        
			        Function function2 = arguments[i].getFunction();
			        Element baseLanguageElement = function2.getTranslationOf();
			        while (baseLanguageElement != null) {
			            if (baseLanguageElement instanceof Function) {
			                Function fnc = (Function) baseLanguageElement;
			                FormalArgument[] argumentsSubElement = fnc.getArguments();
			                for (int j = 0; j < argumentsSubElement.length; j++) {
			                    convertModelElement(traslationElement, argumentsSubElement[j], getLanguage(baseLanguageElement));
			                    traslationElement.setScopedName(getElementInRootLanguage(function).getScopedName() + "." + argumentsSubElement[j].getName());
			                    
			                    function2 = argumentsSubElement[i].getFunction();
			                    baseLanguageElement = function2.getTranslationOf();
			                }
			            }
			        }
			        printModelElement(traslationElement);
			    }
			}
		}

	}

	/**
	 * All Meta Values are assigned into the related fields
	 * 
	 * @param Related Model Element
	 */
	private void addTranslationElementForMetaValues(Element ele) {
		String eleScopedName = getElementInRootLanguage(ele).getScopedName();
		for (String metaAttrName : ele.getMetaValues().getValues()) {
			String metaAttrScopedName = getMetaAttributeScopedName(eleScopedName, metaAttrName);
			TranslationElement translationElement = new TranslationElement();
			translationElement.setElementType(ElementType.METAATTRIBUTE);
			translationElement.setScopedName(metaAttrScopedName);
			while (ele != null) {
				String language = getLanguage(ele);
				String metaAttrValue = ele.getMetaValue(metaAttrName);
				setTranslationElementName(translationElement, metaAttrValue, language);
				ele = ele.getTranslationOf();
			}
			elements.add(translationElement);
		}
	}

	/**
	 * it gives return, ScopedName of the Meta Attribute
	 * 
	 * @param eleScopedName ScopedName of the Element
	 * @param metaAttrName value of the metaAttribute
	 * @return it returns the full scoped name of the metaAttribute
	 */
	private String getMetaAttributeScopedName(String eleScopedName, String metaAttrName) {
		return eleScopedName + Ili2TranslationXml.METAOBJECT + metaAttrName;
	}
	
	/**
	 * Prepare to get All Enumeration data
	 * 
	 * @param et Related Enumeration Elements
	 * @param scopedNamePrefix Scope Name
	 * @param modelElement Model Element
	 */
	private void prepareAllEnumaration(EnumerationType et, String scopedNamePrefix, Element modelElement) {
		Enumeration enumeration = et.getEnumeration();
		printAllEnumaration(enumeration, scopedNamePrefix, modelElement);
	}

	/**
	 * it gets all Enumeration and SubEnumeration data and insert into Structure with language
	 * 
	 * @param enumeration Related Enumeration Elements
	 * @param scopedNamePrefix Scope Name
	 * @param modelEle Model Element
	 */
	private void printAllEnumaration(Enumeration enumeration, String scopedNamePrefix, Element modelEle) {
		Iterator<ch.interlis.ili2c.metamodel.Enumeration.Element> enumarationIterator = enumeration.getElements();

		while (enumarationIterator.hasNext()) {
			Enumeration.Element enumEle = enumarationIterator.next();
			TranslationElement text = new TranslationElement();

			// ScopedName
			String scopedName = scopedNamePrefix + "." + getEnumerationElementInRootLanguage(enumEle).getName();
			text.setScopedName(scopedName);

			String language = getLanguage(modelEle);
			convertEnumerationElement(text, enumEle, language);
			Enumeration.Element baseLanguageElement = enumEle.getTranslationOf();
			Element baseLanguageModelElement = modelEle.getTranslationOf();
			while (baseLanguageElement != null) {
				String baseLanguage = getLanguage(baseLanguageModelElement);
				convertEnumerationElement(text, baseLanguageElement, baseLanguage);
				baseLanguageElement = baseLanguageElement.getTranslationOf();
				baseLanguageModelElement = baseLanguageModelElement.getTranslationOf();
			}

			text.setElementType(getElementType(enumEle));
			addMetaValues(enumEle, scopedName, modelEle);
			printModelElement(text);
			if (enumEle.getSubEnumeration() != null) {
				printAllEnumaration(enumEle.getSubEnumeration(), scopedName, modelEle);
			}
		}
	}

	/**
	 * Meta values are inserted into the XML file according to language
	 * 
	 * @param ele Related All Enumeration Element
	 * @param scopedName Scope Name
	 * @param modelEle Model Element
	 */
	private void addMetaValues(ch.interlis.ili2c.metamodel.Enumeration.Element ele, String scopedName,
			Element modelEle) {
		for (String metaAttrName : ele.getMetaValues().getValues()) {
			String metaAttrScopedName = getMetaAttributeScopedName(scopedName, metaAttrName);
			TranslationElement translationElement = new TranslationElement();
			translationElement.setElementType(ElementType.METAATTRIBUTE);
			translationElement.setScopedName(metaAttrScopedName);

			String metaAttrValue = ele.getMetaValues().getValue(metaAttrName);
			String language = getLanguage(modelEle);
			setTranslationElementName(translationElement, metaAttrValue, language);

			Element baseLanguageModelElement = modelEle.getTranslationOf();
			Enumeration.Element baseLanguageElement = ele.getTranslationOf();
			while (baseLanguageElement != null) {
				String langu = getLanguage(baseLanguageModelElement);
				String metaValue = baseLanguageElement.getMetaValues().getValue(metaAttrName);
				setTranslationElementName(translationElement, metaValue, langu);
				baseLanguageElement = baseLanguageElement.getTranslationOf();
				baseLanguageModelElement = baseLanguageModelElement.getTranslationOf();
			}

			elements.add(translationElement);
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
