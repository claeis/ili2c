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
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Parameter;
import ch.interlis.ili2c.metamodel.Projection;
import ch.interlis.ili2c.metamodel.RefSystemModel;
import ch.interlis.ili2c.metamodel.RoleDef;
import ch.interlis.ili2c.metamodel.SymbologyModel;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeModel;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.ComposedUnit;

/**
 *  Test Program for Interlis2Generator
 * */

public class Interlis2GeneratorTest {

    private static final String SRC_FOLDER = "test/data/interlis2generator/";
    private static final String OUTPUT_FOLDER = "build/test/";
	private static final String ILI_FILE = SRC_FOLDER+"EnumOk_de.ili";
	private static final String OUTPUT_ILI_FILE = "out.ili";

	@org.junit.BeforeClass
	public static void setup()
	{
	    new java.io.File(OUTPUT_FOLDER).mkdir();
	}
	/**
	 * Es ueberprueft, ob die MODEL korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void model() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		Element modelEle = newTd.getElement("EnumOkA");
		assertNotNull(modelEle);
		assertEquals(DataModel.class, modelEle.getClass());
        assertEquals(DataModel.ILI2_3,((DataModel)modelEle).getIliVersion());
	}
    @Test
    public void modelKind() throws Exception {
        final String TEST_ILI="ModelKind.ili";
        final String TEST_ILI_OUT=OUTPUT_FOLDER+TEST_ILI;
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(SRC_FOLDER+TEST_ILI));
        // neues ili schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(TEST_ILI_OUT),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(TEST_ILI_OUT));
        assertNotNull(newTd);

        Model modelEle=null;
        modelEle = (Model)newTd.getElement("TypeModel");
        assertNotNull(modelEle);
        assertEquals(TypeModel.class, modelEle.getClass());
        modelEle = (Model) newTd.getElement("RefSystemModel");
        assertNotNull(modelEle);
        assertEquals(RefSystemModel.class, modelEle.getClass());
        modelEle = (Model) newTd.getElement("SymbologyModel");
        assertNotNull(modelEle);
        assertEquals(SymbologyModel.class, modelEle.getClass());
        assertTrue(modelEle.isContracted());
    }

    @Test
    public void model_IliBeispiel() throws Exception {
        final String TEST_ILI="Kap0_IliBeispiel.ili";
        final String TEST_ILI_OUT=OUTPUT_FOLDER+TEST_ILI;
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(SRC_FOLDER+TEST_ILI));
        // neues ili schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(TEST_ILI_OUT),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(TEST_ILI_OUT));
        assertNotNull(newTd);

        Model modelEle=null;
        modelEle = (Model)newTd.getElement("Bund");
        assertNotNull(modelEle);
        modelEle = (Model)newTd.getElement("Bern");
        assertNotNull(modelEle);
        modelEle = (Model)newTd.getElement("SimpleSignsSymbology");
        assertNotNull(modelEle);
        modelEle = (Model)newTd.getElement("SimpleGrafik");
        assertNotNull(modelEle);

    }

    @Test
    public void constraintsOf() throws Exception {
        final String TEST_ILI="ConstraintsOf.ili";
        final String TEST_ILI_OUT=OUTPUT_FOLDER+TEST_ILI;
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(SRC_FOLDER+TEST_ILI));
        // neues ili schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(TEST_ILI_OUT),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(TEST_ILI_OUT));
        assertNotNull(newTd);

        Element modelEle=null;
        modelEle = newTd.getElement("ModelA.TopicA.ClassB.C1");
        assertNotNull(modelEle);

    }
    
    /**
     * Es ueberprueft, ob die FUNCTION korrekt in das ili file geschrieben wurde.
     * @throws Exception      
     * */
    @Test
    public void function() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.FunctionA");
        assertNotNull(modelEle);
        assertEquals(Function.class, modelEle.getClass());
        
        FormalArgument[] arguments = ((Function) modelEle).getArguments();
        assertEquals("dummy", arguments[0].getName());
    }
    
    /**
     * Es ueberprueft, ob die UNIT korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void unit() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.m3secA");
        assertNotNull(modelEle);
        assertEquals(ComposedUnit.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die GRAPHIC korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void graphic() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.GrafikA");
        assertNotNull(modelEle);
        assertEquals(Topic.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die LINE FORM korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void lineForm() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.LineFormA");
        assertNotNull(modelEle);
        assertEquals(LineForm.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die META DATA BASKET korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void metaDataBasket() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.BCoordSysA");
        assertNotNull(modelEle);
        assertEquals(MetaDataUseDef.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die VIEW korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void view() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA_V");
        assertNotNull(modelEle);
        assertEquals(Projection.class, modelEle.getClass());
        
        modelEle = newTd.getElement("EnumOkA.TopicA.ClassA_V.A");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die PARAMETER korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void parameter() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.TopicA.SymbolA.PosA");
        assertNotNull(modelEle);
        assertEquals(Parameter.class, modelEle.getClass());
    }
	
    /**
     * Es ueberprueft, ob die Domain korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void domain() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das Domain der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.Coord2");
        assertNotNull(modelEle);
        assertEquals(Domain.class, modelEle.getClass());
    }
    
    /**
     * Es ueberprueft, ob die Structur korrekt in das ili file geschrieben wurde.
     * @throws Exception 
     * */
    @Test
    public void structure() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
        // neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
        assertNotNull(newTd);

        // testen, dass im neuen ili fuer das Domain der name_fr aus dem xml steht
        Element modelEle = newTd.getElement("EnumOkA.Klothoide");
        assertNotNull(modelEle);
        assertEquals(Table.class, modelEle.getClass());
        
        modelEle = newTd.getElement("EnumOkA.Klothoide.a");
        assertNotNull(modelEle);
        
        modelEle = newTd.getElement("EnumOkA.LocalisedUriDE");
        assertNotNull(modelEle);
    }

	/**
	 * Es ueberprueft, ob das TOPIC korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void topic() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Topic der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA");
		assertNotNull(modelEle);
		assertEquals(Topic.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die KLASSE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void classTest() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Class der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA");
		assertNotNull(modelEle);
		assertEquals(Table.class, modelEle.getClass());
		
        modelEle = newTd.getElement("EnumOkA.TopicA.Dokument");
        assertNotNull(modelEle);
        assertEquals(Table.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die ATTRIBUTE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void attribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das Attribute der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.attrA");
		assertNotNull(modelEle);
		assertEquals(LocalAttribute.class, modelEle.getClass());
		
		modelEle = newTd.getElement("EnumOkA.TopicA.Dokument.Titel");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
        
        modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.geomA");
        assertNotNull(modelEle);
        assertEquals(LocalAttribute.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob das ENUMERATION SUBELEMENT korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void enumElementB21() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das enumElement der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.attrA");
		assertEquals(LocalAttribute.class, modelEle.getClass());
		assertTrue(hasEnumElement(modelEle, "a2.a21"));
	}
	
	/**
	 * Es ueberprueft, ob die TOPIC META ATTRIBUTE korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void topicMetaAttribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA");
		assertNotNull(modelEle);
		assertTrue(hasMetaElement(modelEle,"Topic A", "dispName"));		
		assertEquals(Topic.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_WITHOUT_EXPLICIT_NAME korrekt in das ili file geschrieben wurde.
     * @throws Exception 
	 * */
	@Test
	public void constraintWithoutExplicitName() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.Constraint1");
		assertNotNull(modelEle);
		assertEquals(MandatoryConstraint.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_WITH_EXPLICIT_NAME korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintWithExplicitName() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.UniqueConstraintA");
		assertNotNull(modelEle);
		assertEquals(UniquenessConstraint.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die ROLE korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void role() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.roleA1roleA2.roleA1");
		assertNotNull(modelEle);
		assertEquals(RoleDef.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die ASSOCIATION korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void association() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.roleA1roleA2");
		assertNotNull(modelEle);
		assertEquals(AssociationDef.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die CONSTRAINT_IN_ASSOCIATION korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void constraintInAssociation() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.roleA1roleA2.Constraint1");
		assertNotNull(modelEle);
		assertEquals(MandatoryConstraint.class, modelEle.getClass());
	}
	
	/**
	 * Es ueberprueft, ob die ENUM_ELEMENT_META_ATTRIBUTE korrekt in das ili file geschrieben wurde.
	 * @throws Exception 
	 * */
	@Test
	public void enumElementMetaAttribute() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.ILI_FILE));
		// neues ili mit namen aus xml schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generate(out, td, false);
		out.close();
		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(Interlis2GeneratorTest.OUTPUT_ILI_FILE), new File(Interlis2GeneratorTest.ILI_FILE));
		assertNotNull(newTd);

		// testen, dass im neuen ili fuer das model der name_fr aus dem xml steht
		Element modelEle = newTd.getElement("EnumOkA.TopicA.ClassA.attrA");
		assertNotNull(modelEle);
		assertTrue(getMetaValueFromEnumerationElement(modelEle, "a 2", "a2"));
		assertEquals(LocalAttribute.class, modelEle.getClass());
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