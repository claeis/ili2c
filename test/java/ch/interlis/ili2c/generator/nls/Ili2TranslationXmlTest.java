package ch.interlis.ili2c.generator.nls;

import ch.interlis.ili2c.generator.nls.ElementType;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXml;
import ch.interlis.ili2c.generator.nls.ModelElements;
import ch.interlis.ili2c.generator.nls.TranslationElement;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.TransferDescription;

/**
 *  Test Program for Ili2TranslationXml
 * */
public class Ili2TranslationXmlTest {
	
	private static final String FILEPATH = "test\\data\\ili2translationxml\\EnumOk.ili";
	
	/** Es ueberprueft, ob das MODEL korrekt in das xml file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void model() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		int modelAcount=0;
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA")) {
					assertEquals("EnumOkA", ele.getName_de());
					assertEquals("EnumOkB", ele.getName_fr());
                    assertEquals(null, ele.getName_it());
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
		    if(scopedNameNode.getTextContent().equals("EnumOkA")) {
		      nameItNode=node.getChildNodes().item(4);
		      break;
		    }
		}
		assertEquals("name_it",nameItNode.getNodeName());
        assertEquals("-",nameItNode.getTextContent());
	}

	/**
	 * Es ueberprueft, ob das TOPIC korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void topic() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA")) {
					assertEquals("TopicA", ele.getName_de());
					assertEquals("TopicB", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.METAOBJECT.dispName")) {
					assertEquals("Topic A", ele.getName_de());
					assertEquals("Topic B", ele.getName_fr());
					assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
					return;
				}
			}
		}
		fail("METAATTRIBUTE can not be found!");
	}
	
	/**
	 * Es ueberprueft, ob das STRUCTURE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void structure() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.StructureA")) {
					assertEquals("StructureA", ele.getName_de());
					assertEquals("StructureB", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA")) {
					assertEquals("ClassA", ele.getName_de());
					assertEquals("ClassB", ele.getName_fr());
					assertEquals(ElementType.CLASS,ele.getElementType());
					return;
				}
			}
		}
		fail("CLASS can not be found!");
	}

	/**
	 * Es ueberprueft, ob das ATTRIBUTE korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void attribute() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.attrA")) {
					assertEquals("attrA", ele.getName_de());
					assertEquals("attrB", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.attrA.a2")) {
					assertEquals("a2", ele.getName_de());
					assertEquals("b2", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.attrA.a2.a21")) {
					assertEquals("a21", ele.getName_de());
					assertEquals("b21", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.attrA.a2.METAOBJECT.dispName")) {
					assertEquals("a 2", ele.getName_de());
					assertEquals("b 2", ele.getName_fr());
					assertEquals(ElementType.METAATTRIBUTE, ele.getElementType());
					return;
				}
			}
		}
		fail("ENUMERATION ELEMENT METAATTRIBUTE can not be found!");
	}

	/**
	 * Es ueberprueft, ob das ASSOCIATION korrekt in das XML file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void association() throws Exception {
		// ili lesen und xml schreiben
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.roleA1roleA2")) {
					assertEquals("roleA1roleA2", ele.getName_de());
					assertEquals("roleB1roleB2", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.roleA1roleA2.roleA1")) {
					assertEquals("roleA1", ele.getName_de());
					assertEquals("roleB1", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.Constraint1")) {
					assertEquals("Constraint1", ele.getName_de());
					assertEquals("Constraint1", ele.getName_fr());
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
		final String xmlFileName = FILEPATH + ".xml";
		Ili2TranslationXml.writeModelElementsAsXML(readAllModels(new File(FILEPATH)), new File(xmlFileName));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(xmlFileName));
		
		for (TranslationElement ele : modelElements) {
			if (ele.getScopedName() != null) {
				if (ele.getScopedName().equals("EnumOkA.TopicA.ClassA.UniqueConstraintA")) {
					assertEquals("UniqueConstraintA", ele.getName_de());
					assertEquals("UniqueConstraintB", ele.getName_fr());
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
		settings.setValue(UserSettings.ILIDIRS, UserSettings.DEFAULT_ILIDIRS);
		HashMap<String, String> pathMap = new HashMap<String, String>();
		pathMap.put(UserSettings.ILI_DIR, currentDir);
		settings.setTransientObject(UserSettings.ILIDIRS_PATHMAP, pathMap);
		TransferDescription td = null;
		td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig, settings);
		return td;
	}
	
}
