package ch.interlis.ili2c.generator.nls;

import ch.interlis.ili2c.generator.nls.ElementType;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cSettings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXml;
import ch.interlis.ili2c.generator.nls.ModelElements;
import ch.interlis.ili2c.generator.nls.TranslationElement;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;

/**
 *  Test Program for Ili2TranslationXml
 * */
public class Ili2TranslationXmlTest {
	
	private static final String ILI_UNIQUE_CONSTRAINT_FR = "UniqueConstraint_FR";
    private static final String ILI_UNIQUE_CONSTRAINT_DE = "UniqueConstraint_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_UNIQUE_CONSTRAINT_DE = "EnumOk_DE.Topic_DE.Class_DE.UniqueConstraint_DE";
    private static final String ILI_CONSTRAINT1 = "Constraint1";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_CONSTRAINT1 = "EnumOk_DE.Topic_DE.Class_DE.Constraint1";
    private static final String ILI_ROLE1_IT = "role1_IT";
    private static final String ILI_ROLE1_FR = "role1_FR";
    private static final String ILI_ROLE1_DE = "role1_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_ROLE1_D_EROLE2_DE_ROLE1_DE = "EnumOk_DE.Topic_DE.role1_DErole2_DE.role1_DE";
    private static final String ILI_ROLE1_FR_ROLE2_FR = "role1_FRrole2_FR";
    private static final String ILI_ROLE1_DE_ROLE2_DE = "role1_DErole2_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_ROLE1_DE_ROLE2_DE = "EnumOk_DE.Topic_DE.role1_DErole2_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_METAOBJECT_DISP_NAME_FR = "EnumOk_DE.Topic_DE.Class_DE.attr_DE.a2_DE.METAOBJECT.dispName_fr";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_METAOBJECT_DISP_NAME_DE = "EnumOk_DE.Topic_DE.Class_DE.attr_DE.a2_DE.METAOBJECT.dispName_de";
    private static final String ILI_A21_IT = "it21";
    private static final String ILI_A21_FR = "b21_FR";
    private static final String ILI_A21_DE = "a21_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_A21_DE = "EnumOk_DE.Topic_DE.Class_DE.attr_DE.a2_DE.a21_DE";
    private static final String ILI_A2_IT = "it2";
    private static final String ILI_A2_FR = "b2_FR";
    private static final String ILI_A2_DE = "a2_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE = "EnumOk_DE.Topic_DE.Class_DE.attr_DE.a2_DE";
    private static final String ILI_ATTR_FR = "attr_FR";
    private static final String ILI_ATTR_DE = "attr_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE = "EnumOk_DE.Topic_DE.Class_DE.attr_DE";
    private static final String ILI_IT = "it";
    private static final String ILI_FR = "fr";
    private static final String ILI_DE = "de";
    private static final String ILI_ENUM_OK_IT = "EnumOk_IT";
    private static final String ILI_ARG_FR = "arg_FR";
    private static final String ILI_ARG_DE = "arg_DE";
    private static final String ILI_ENUM_OK_DE_FUNCTION_DE_ARG_DE = "EnumOk_DE.Function_DE.arg_DE";
    private static final String ILI_FUNCTION_FR = "Function_FR";
    private static final String ILI_FUNCTION_DE = "Function_DE";
    private static final String ILI_ENUM_OK_DE_FUNCTION_DE = "EnumOk_DE.Function_DE";
    private static final String ILI_CLASS_IT = "Class_IT";
    private static final String ILI_CLASS_FR = "Class_FR";
    private static final String ILI_CLASS_DE = "Class_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE = "EnumOk_DE.Topic_DE.Class_DE";
    private static final String ILI_STRUCTURE_FR = "Structure_FR";
    private static final String ILI_STRUCTURE_DE = "Structure_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_STRUCTURE_DE = "EnumOk_DE.Topic_DE.Structure_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_METAOBJECT_DISP_NAME_FR = "EnumOk_DE.Topic_DE.METAOBJECT.dispName_fr";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE_METAOBJECT_DISP_NAME_DE = "EnumOk_DE.Topic_DE.METAOBJECT.dispName_de";
    private static final String ILI_TOPIC_FR = "Topic_FR";
    private static final String ILI_TOPIC_DE = "Topic_DE";
    private static final String ILI_ENUM_OK_DE_TOPIC_DE = "EnumOk_DE.Topic_DE";
    private static final String ILI_ENUM_OK_FR = "EnumOk_FR";
    private static final String ILI_ENUM_OK_DE = "EnumOk_DE";
    private static final String SRCPATH = "test/data/ili2translationxml/";
    private static final String SRC_GENERAL = SRCPATH+"EnumOk.ili";
	
	/** Es ueberprueft, ob das MODEL korrekt in das xml file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void model() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		int modelAcount=0;
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE)) {
					assertEquals(ILI_ENUM_OK_DE, ele.getName_de());
					assertEquals(ILI_ENUM_OK_FR, ele.getName_fr());
                    assertEquals(null, ele.getName_en());
					assertEquals("Das ist Dokumentation zum Modell in DE", ele.getDocumentation_de());
					assertEquals("Das ist Dokumentation zum Modell in FR", ele.getDocumentation_fr());
					assertEquals(ElementType.MODEL, ele.getElementType());
					modelAcount++;
				}
			}
		}
		assertEquals(1,modelAcount);
		// verify empty column name_it is written as "-"
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(new File(xmlFileName));
		NodeList elements=doc.getElementsByTagName("element");
		Node nameItNode=null;
		for(int elei=0;elei<elements.getLength();elei++) {
		    Node node=elements.item(elei);
		    Node scopedNameNode=node.getFirstChild();
		    if(scopedNameNode.getTextContent().equals(ILI_ENUM_OK_DE)) {
		      nameItNode=node.getChildNodes().item(5);
		      break;
		    }
		}
		assertEquals("name_en",nameItNode.getNodeName());
        assertEquals("-",nameItNode.getTextContent());
	}

	/**
	 * Es ueberprueft, ob das TOPIC korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void topic() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE)) {
					assertEquals(ILI_TOPIC_DE, ele.getName_de());
					assertEquals(ILI_TOPIC_FR, ele.getName_fr());
					assertEquals(ElementType.TOPIC, ele.getElementType());
					return;
				}
			}
		}
		fail("TOPIC can not be found!");
	}
	/**
	 * Es ueberprueft, ob das METAATTRIBUTE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void topicMetaAttribute() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		boolean de_found=false;
        boolean fr_found=false;
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_METAOBJECT_DISP_NAME_DE)) {
					assertEquals("Topic DE", ele.getName_de());
					assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
			        de_found=true;
				}
                if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_METAOBJECT_DISP_NAME_FR)) {
                    assertEquals("Topic FR", ele.getName_de());
                    assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
                    fr_found=true;
                }
			}
		}
		if(!de_found || !fr_found) {
	        fail("METAATTRIBUTE can not be found!");
		}
	}
	
	/**
	 * Es ueberprueft, ob das STRUCTURE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void structure() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_STRUCTURE_DE)) {
					assertEquals(ILI_STRUCTURE_DE, ele.getName_de());
					assertEquals(ILI_STRUCTURE_FR, ele.getName_fr());
					assertEquals(ElementType.STRUCTURE, ele.getElementType());
					return;
				}
			}
		}
		fail("STRUCTURE can not be found!");
	}

	/**
	 * Es ueberprueft, ob das CLASS korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void classTest() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE)) {
					assertEquals(ILI_CLASS_DE, ele.getName_de());
					assertEquals(ILI_CLASS_FR, ele.getName_fr());
                    assertEquals(ILI_CLASS_IT, ele.getName_it());
					assertEquals(ElementType.CLASS,ele.getElementType());
					return;
				}
			}
		}
		fail("CLASS can not be found!");
	}
    @Test
    public void functionTest() throws Exception {
        // ili lesen und xml schreiben
        final String xmlFileName = SRC_GENERAL + ".xml";
        Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
        
        {
            TranslationElement ele=getByScopedName(modelElements,ILI_ENUM_OK_DE_FUNCTION_DE);
            assertNotNull(ele);
            assertEquals(ILI_FUNCTION_DE, ele.getName_de());
            assertEquals(ILI_FUNCTION_FR, ele.getName_fr());
            assertEquals(ElementType.FUNCTION,ele.getElementType());
        }
        {
            TranslationElement ele=getByScopedName(modelElements,ILI_ENUM_OK_DE_FUNCTION_DE_ARG_DE);
            assertNotNull(ele);
            assertEquals(ILI_ARG_DE, ele.getName_de());
            assertEquals(ILI_ARG_FR, ele.getName_fr());
            assertEquals(ElementType.FORMAL_ARGUMENT,ele.getElementType());
        }
    }

	private TranslationElement getByScopedName(ModelElements modelElements, String scopedName) {
        for (TranslationElement ele : modelElements) {
            if (ele.getScopedName() != null) {
                if (ele.getScopedName().equals(scopedName)) {
                    return ele;
                }
            }
        }
        return null;
    }
    @Test
    public void getElementInRootLanguageTest() throws Exception {
        // compile
        TransferDescription td=compileIliModel(new File(SRC_GENERAL));
        Model model_IT=(Model) td.getElement(ILI_ENUM_OK_IT);
        Model model_FR=(Model) td.getElement(ILI_ENUM_OK_FR);
        Model model_DE=(Model) td.getElement(ILI_ENUM_OK_DE);
        Assert.assertEquals(model_DE,Ili2TranslationXml.getElementInRootLanguage(model_IT));
        Assert.assertEquals(model_DE,Ili2TranslationXml.getElementInRootLanguage(model_FR));
        Assert.assertEquals(model_DE,Ili2TranslationXml.getElementInRootLanguage(model_DE));
    }
    @Test
    public void convertModelsTest() throws Exception {
        // compile
        TransferDescription td=compileIliModel(new File(SRC_GENERAL));
        Model models[]=new Model[]{(Model) td.getElement(ILI_ENUM_OK_IT),
                (Model) td.getElement(ILI_ENUM_OK_FR),
                (Model) td.getElement(ILI_ENUM_OK_DE)};
        Ili2TranslationXml convert=new Ili2TranslationXml();
        java.util.List<NlsModelElement> nlsEles=convert.convertModels(models);
        NlsModelElement nlsEle=findNlsEle(nlsEles,ILI_ENUM_OK_DE);
        Assert.assertEquals(ILI_ENUM_OK_DE,nlsEle.getName(ILI_DE));
        Assert.assertEquals(ILI_ENUM_OK_FR,nlsEle.getName(ILI_FR));
        Assert.assertEquals(ILI_ENUM_OK_IT,nlsEle.getName(ILI_IT));
        
    }

    private NlsModelElement findNlsEle(List<NlsModelElement> nlsEles, String scopedName) {
        for(NlsModelElement nlsEle:nlsEles) {
            if(nlsEle.getScopedName().equals(scopedName)) {
                return nlsEle;
            }
        }
        return null;
    }

    /**
	 * Es ueberprueft, ob das ATTRIBUTE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void attribute() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE)) {
					assertEquals(ILI_ATTR_DE, ele.getName_de());
					assertEquals(ILI_ATTR_FR, ele.getName_fr());
					assertEquals("Das ist Dokumentation in DE", ele.getDocumentation_de());
					assertEquals("Das ist Dokumentation in FR", ele.getDocumentation_fr());
					assertEquals(ElementType.ATTRIBUTE, ele.getElementType());
					return;
				}
			}
		}
		fail("ATTRIBUTE can not be found!");
	}

	/**
	 * Es ueberprueft, ob das ENUMERATION_ELEMENT korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void enumeration() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE)) {
					assertEquals(ILI_A2_DE, ele.getName_de());
					assertEquals(ILI_A2_FR, ele.getName_fr());
                    assertEquals(ILI_A2_IT, ele.getName_it());
					assertEquals("enum docu zu a2", ele.getDocumentation_de());
					assertEquals("enum docu zu b2", ele.getDocumentation_fr());
					assertEquals(ElementType.ENUMERATION_ELEMENT, ele.getElementType());
					return;
				}
			}
		}
		fail("ENUMERATION ELEMENT can not be found!");
	}

	/**
	 * Es ueberprueft, ob das ENUMERATION_SUBELEMENT korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void enumerationSubEnum() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_A21_DE)) {
					assertEquals(ILI_A21_DE, ele.getName_de());
					assertEquals(ILI_A21_FR, ele.getName_fr());
                    assertEquals(ILI_A21_IT, ele.getName_it());
					assertEquals(ElementType.ENUMERATION_ELEMENT, ele.getElementType());
					return;
				}
			}
		}
		fail("ENUMERATION SUBELEMENT can not be found!");
	}
	
	/**
	 * Es ueberprueft, ob die ENUMERATION ELEMENT META ATTRIBUTE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void enumerationElementMetaAttribute() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
        boolean de_found=false;
        boolean fr_found=false;
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_METAOBJECT_DISP_NAME_DE)) {
					assertEquals("a 2", ele.getName_de());
					assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
                    de_found=true;
				}
                if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_ATTR_DE_A2_DE_METAOBJECT_DISP_NAME_FR)) {
                    assertEquals("b 2", ele.getName_de());
                    assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
                    fr_found=true;
                }
			}
		}
        if(!de_found || !fr_found) {
            fail("ENUMERATION ELEMENT METAATTRIBUTE can not be found!");
        }
	}

	/**
	 * Es ueberprueft, ob das ASSOCIATION korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void association() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_ROLE1_DE_ROLE2_DE)) {
					assertEquals(ILI_ROLE1_DE_ROLE2_DE, ele.getName_de());
					assertEquals(ILI_ROLE1_FR_ROLE2_FR, ele.getName_fr());
					assertEquals(ElementType.ASSOCIATION, ele.getElementType());
					return;
				}
			}
		}
		fail("ASSOCIATION can not be found!");
	}
	/**
	 * DE -> Es ueberprueft, ob die ROLE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void role() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_ROLE1_D_EROLE2_DE_ROLE1_DE)) {
					assertEquals(ILI_ROLE1_DE, ele.getName_de());
					assertEquals(ILI_ROLE1_FR, ele.getName_fr());
                    assertEquals(ILI_ROLE1_IT, ele.getName_it());
					assertEquals(ElementType.ROLE, ele.getElementType());
					return;
				}
			}
		}
		fail("ROLE can not be found!");
	}
	
	/**
	 * DE -> Es ueberprueft, ob der CONSTRAINT ohne expliziten Namen korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintWithoutExplicitName() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_CONSTRAINT1)) {
					assertEquals(ILI_CONSTRAINT1, ele.getName_de());
					assertEquals(ILI_CONSTRAINT1, ele.getName_fr());
					assertEquals(ElementType.CONSTRAINT, ele.getElementType());
					return;
				}
			}
		}
		fail("Constraint without explicit name can not be found!");
	}

	/**
	 * DE -> Es ueberprueft, ob die CONSTRAINT mit expliziten Namen korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintWithExplicitName() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = SRC_GENERAL + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(SRC_GENERAL)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals(ILI_ENUM_OK_DE_TOPIC_DE_CLASS_DE_UNIQUE_CONSTRAINT_DE)) {
					assertEquals(ILI_UNIQUE_CONSTRAINT_DE, ele.getName_de());
					assertEquals(ILI_UNIQUE_CONSTRAINT_FR, ele.getName_fr());
					assertEquals(ElementType.CONSTRAINT, ele.getElementType());
					return;
				}
			}
		}
		fail("Constraint with explicit name can not be found!");
	}
	/**
	 * All models are read in this method
	 * 
	 * @param iliFile path of the source file
	 * @return all collected elements are returned
	 */
	public static ModelElements readAllModels(File iliFile) {
	
		TransferDescription td = compileIliModel(iliFile);
	
		if (td == null) {
			return null;
		}

		return new Ili2TranslationXml().convertTransferDescription2ModelElements(td);
	}
	/**
	 * with TransferDescription we can get ili data from the source file.
	 * 
	 * @param iliFile The path of the source file.
	 * @return  it include all configurations, settings and all models.
	 */
	public static TransferDescription compileIliModel(File iliFile) {
		return compileIliModel(iliFile, null);
	}
	/**
	 * With compileIliModel Function we can get the Ili data.
	 * 
	 * @param iliFile1 we can read ili file with the file path
	 * @param iliFile2 we can read ili file with another file path
	 * @return it include all configurations, settings and all models.
	 * */
	public static TransferDescription compileIliModel(File iliFile1, File iliFile2) {
		Configuration ili2cConfig = new ch.interlis.ili2c.config.Configuration();
		ili2cConfig.setAutoCompleteModelList(true);
		ili2cConfig.addFileEntry(new FileEntry(iliFile1.getAbsolutePath(), FileEntryKind.ILIMODELFILE));
		if (iliFile2 != null) {
			ili2cConfig.addFileEntry(new FileEntry(iliFile2.getAbsolutePath(), FileEntryKind.ILIMODELFILE));
		}
	
		Settings settings = new Settings();
		String currentDir = iliFile1.getAbsoluteFile().getParent();
		settings.setValue(Ili2cSettings.ILIDIRS, Ili2cSettings.DEFAULT_ILIDIRS);
		HashMap<String, String> pathMap = new HashMap<String, String>();
		pathMap.put(Ili2cSettings.ILI_DIR, currentDir);
		settings.setTransientObject(Ili2cSettings.ILIDIRS_PATHMAP, pathMap);
		TransferDescription td = null;
		td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig, settings);
		return td;
	}
	
}
