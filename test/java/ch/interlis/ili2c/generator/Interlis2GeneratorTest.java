package ch.interlis.ili2c.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import ch.interlis.ili2c.generator.nls.Ili2TranslationXml;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;
import ch.interlis.ili2c.generator.nls.ModelElements;
import ch.interlis.ili2c.metamodel.AssociationDef;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.Enumeration;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.FormalArgument;
import ch.interlis.ili2c.metamodel.Function;
import ch.interlis.ili2c.metamodel.LineForm;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.MetaDataUseDef;
import ch.interlis.ili2c.metamodel.Parameter;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.ComposedUnit;

/**
 *  Test Program for Interlis2Generator
 * */

public class Interlis2GeneratorTest {

	private static final String ILI_FILE = "test\\data\\interlis2generator\\EnumOk_de.ili";
	private static final String NLSXML_FILE = ILI_FILE + ".xml";
	private static final String OUTPUT_ILI_FILE = "out.ili";

	/**
	 * Es ueberprueft, ob die MODEL korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void model() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB");
		assertNotNull(modelEle);
		assertEquals(DataModel.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, ((DataModel) modelEle).getLanguage());
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA", modelEle.getTranslationOf().getScopedName());
	}
	
    /**
     * Es ueberprueft, ob die FUNCTION korrekt in das ili file geschrieben wurde.
     * @throws Exception      
     * */
    @Test
    public void function() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.FunctionB");
        assertNotNull(modelEle);
        assertEquals(Function.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.FunctionA", modelEle.getTranslationOf().getScopedName());
        
        FormalArgument[] arguments = ((Function) modelEle).getArguments();
        String scopedName = modelEle.getScopedName() + "." + arguments[0].getScopedName();
        assertEquals("EnumOkB.FunctionB.dummyB", scopedName);
        assertEquals(Function.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        
        Function function = arguments[0].getFunction();
        Element baseLanguageElement = function.getTranslationOf();
        
        Function fnc = (Function) baseLanguageElement;
        FormalArgument[] argumentsSubElement = fnc.getArguments();
        String baseElement = Ili2TranslationXml.getElementInRootLanguage(fnc).getScopedName() + "." + argumentsSubElement[0].getName();
        
        assertEquals("EnumOkA.FunctionA.dummy", baseElement);
    }
    
    /**
     * Es ueberprueft, ob die UNIT korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void unit() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.m3secB");
        assertNotNull(modelEle);
        assertEquals(ComposedUnit.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.m3secA", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die GRAPHIC korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void graphic() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.GrafikB");
        assertNotNull(modelEle);
        assertEquals(Topic.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.GrafikA", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die LINE FORM korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void lineForm() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.LineFormB");
        assertNotNull(modelEle);
        assertEquals(LineForm.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.LineFormA", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die META DATA BASKET korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void metaDataBasket() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.BCoordSysB");
        assertNotNull(modelEle);
        assertEquals(MetaDataUseDef.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.BCoordSysA", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die VIEW korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void view() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB_V");
        assertNotNull(modelEle);
        assertEquals(Projection.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.ClassA_V", modelEle.getTranslationOf().getScopedName());
        
        modelEle = newTd.getElement("EnumOkB.TopicB.ClassB_V.B");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.ClassA_V.A", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die PARAMETER korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void parameter() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.TopicB.SymbolB.PosB");
        assertNotNull(modelEle);
        assertEquals(Parameter.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.SymbolA.PosA", modelEle.getTranslationOf().getScopedName());
    }
	
    /**
     * Es ueberprueft, ob die Domain korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void domain() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das Domain der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.Coord2B");
        assertNotNull(modelEle);
        assertEquals(Domain.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.Coord2", modelEle.getTranslationOf().getScopedName());
    }
    
    /**
     * Es ueberprueft, ob die Structur korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void structure() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // xml lesen
        ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das Domain der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkB.KlothoideB");
        assertNotNull(modelEle);
        assertEquals(Table.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.Klothoide", modelEle.getTranslationOf().getScopedName());
        
        modelEle = newTd.getElement("EnumOkB.KlothoideB.aB");
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.Klothoide.a", modelEle.getTranslationOf().getScopedName());
        
        modelEle = newTd.getElement("EnumOkB.LocalisedUriFR");
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.LocalisedUriDE", modelEle.getTranslationOf().getScopedName());
    }

	/**
	 * Es ueberprueft, ob das TOPIC korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void topic() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));		// neues ili mit namen aus xml schreiben
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Topic der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB");
		assertNotNull(modelEle);
		assertEquals(Topic.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die KLASSE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void classTest() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));		// neues ili mit namen aus xml schreiben
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Class der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB");
		assertNotNull(modelEle);
		assertEquals(Table.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.ClassA", modelEle.getTranslationOf().getScopedName());
		
        modelEle = newTd.getElement("EnumOkB.TopicB.Document");
        assertNotNull(modelEle);
        assertEquals(Table.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.Dokument", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die ATTRIBUTE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void attribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Attribute der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.attrB");
		assertNotNull(modelEle);
		assertEquals(LocalAttribute.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.ClassA.attrA", modelEle.getTranslationOf().getScopedName());
		
		modelEle = newTd.getElement("EnumOkB.TopicB.Document.Titre");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.Dokument.Titel", modelEle.getTranslationOf().getScopedName());
        
        modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.geomB");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
        assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
        assertNotNull(modelEle.getTranslationOf());
        assertEquals("EnumOkA.TopicA.ClassA.geomA", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob das ENUMERATION SUBELEMENT korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void enumElementB21() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das enumElement der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.attrB");
		assertEquals(LocalAttribute.class, modelEle.getClass());
		assertTrue(hasEnumElement(modelEle, "b2.b21"));
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertTrue(hasEnumElement(modelEle.getTranslationOf(), "a2.a21"));
	}
	
	/**
	 * Es ueberprueft, ob die TOPIC META ATTRIBUTE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void topicMetaAttribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB");
		assertNotNull(modelEle);
		assertTrue(hasMetaElement(modelEle,"Topic B", "dispName"));		
		assertEquals(Topic.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("Topic A", modelEle.getTranslationOf().getMetaValues().getValue("dispName"));
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_WITHOUT_EXPLICIT_NAME korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void constraintWithoutExplicitName() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.Constraint1");
		assertNotNull(modelEle);
		assertEquals(MandatoryConstraint.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.ClassA.Constraint1", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_WITH_EXPLICIT_NAME korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintWithExplicitName() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.UniqueConstraintB");
		assertNotNull(modelEle);
		assertEquals(UniquenessConstraint.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.ClassA.UniqueConstraintA", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die ROLE korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void role() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.roleB1roleB2.roleB1");
		assertNotNull(modelEle);
		assertEquals(RoleDef.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.roleA1roleA2.roleA1", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die ASSOCIATION korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void association() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.roleB1roleB2");
		assertNotNull(modelEle);
		assertEquals(AssociationDef.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.roleA1roleA2", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_IN_ASSOCIATION korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintInAssociation() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.roleB1roleB2.Constraint1");
		assertNotNull(modelEle);
		assertEquals(MandatoryConstraint.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertEquals("EnumOkA.TopicA.roleA1roleA2.Constraint1", modelEle.getTranslationOf().getScopedName());
	}
	
	/**
	 * Es ueberprueft, ob die ENUM_ELEMENT_META_ATTRIBUTE korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void enumElementMetaAttribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// xml lesen
		ModelElements modelElements = Ili2TranslationXml.readModelElementsXml(new File(NLSXML_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewLanguage(out, td, modelElements, Ili2TranslationXml.FR);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkB.TopicB.ClassB.attrB");
		assertNotNull(modelEle);
		assertTrue(getMetaValueFromEnumerationElement(modelEle, "b 2", "b2"));
		assertEquals(LocalAttribute.class, modelEle.getClass());
		assertEquals(Ili2TranslationXml.FR, Ili2TranslationXml.getLanguage(modelEle));
		assertNotNull(modelEle.getTranslationOf());
		assertTrue(getMetaValueFromEnumerationElement(modelEle.getTranslationOf(), "a 2", "a2"));
	}
	
	/**
	 * Checking if MetaAttribute is in EnumerationElement
	 * 
	 * @param modelEle Related ModelEle
	 * @param expectedMetaValue expected Meta Value
	 * @param wantedEnumerationElement Enumeration Element
	 * */
	private boolean getMetaValueFromEnumerationElement(Element modelEle, String expectedMetaValue, String wantedEnumerationElement) {
		if (modelEle instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) modelEle;
			if (attr.getDomain() instanceof EnumerationType) {
				EnumerationType enumType = (EnumerationType) attr.getDomain();
				Enumeration enumeration = enumType.getEnumeration();
				Iterator<ch.interlis.ili2c.metamodel.Enumeration.Element> enumarationIterator = enumeration.getElements();
				
				while (enumarationIterator.hasNext()) {
					Enumeration.Element enumEle = enumarationIterator.next();
					if (enumEle.getName().equals(wantedEnumerationElement) && enumEle.getMetaValues() != null) {
					    String dispname = enumEle.getMetaValues().getValue("dispName");
					    if (dispname.equals(expectedMetaValue)) {
					        return true;
					    }						    
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether metaAttName and expectedMetaValue are the same.
	 * 
	 * @param modelEle Related Model Element
	 * @param expectedMetaValue expected Meta Value
	 * @param metaAttName Meta Attribute Name
	 * */
	private boolean hasMetaElement(Element modelEle, String expectedMetaValue, String metaAttName) {
		if (modelEle.getMetaValues().getValue(metaAttName).equals(expectedMetaValue)) {
			return true;
		}
		return false;
	}
	
	/**
	 * if ModelEle has a Enum Element then it check to same between enum Element Name and Enumeration Type 
	 * 
	 * @param modelEle Related Model Element
	 * @param enumElementName expected Element Name
	 * */
	private boolean hasEnumElement(Element modelEle, String enumElementName) {
		if (modelEle instanceof Domain) {
			Domain domain = (Domain) modelEle;
			if (domain.getType() instanceof EnumerationType) {
				return hasEnumElement((EnumerationType) domain.getType(), enumElementName);
			}
		} else if (modelEle instanceof AttributeDef) {
			AttributeDef attr = (AttributeDef) modelEle;
			if (attr.getDomain() instanceof EnumerationType) {
				return hasEnumElement((EnumerationType) attr.getDomain(), enumElementName);
			}
		}
		return false;
	}
	
	/**
	 * check if EnumElement contains a EnumElementName.
	 * 
	 * @param et expected Element Name
	 * @param enumElementName Related Enumeration Type
	 * */
	private boolean hasEnumElement(EnumerationType et, String enumElementName) {
		Enumeration enumeration = et.getEnumeration();
		ArrayList<String> elements=new ArrayList<String>();
		EnumerationType.buildEnumList(elements, "", enumeration);
		if (elements.contains(enumElementName) == true) {
			return true;
		}
		return false;
	}


}