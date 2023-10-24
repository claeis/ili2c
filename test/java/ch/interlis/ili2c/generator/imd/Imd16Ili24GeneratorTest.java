package ch.interlis.ili2c.generator.imd;

import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.ObjectEvent;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Imd16Ili24GeneratorTest {

    private static final String OUT_FILE = "Simple24-out.imd";
    private static final String SIMPLE24_ILI = "test/data/imdgenerator/Simple24.ili";
    private static final String ILIS_META16_ILI = "standard/IlisMeta16.ili";
    private static final HashMap<String, IomObject> metaObjects = new HashMap<String, IomObject>();

    @BeforeClass
    public static void setUp() throws Exception {
        // compile simple24 model to imd16
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(SIMPLE24_ILI, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        ili2cConfig.setOutputFile(OUT_FILE);
        ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
        TransferDescription simple24iliTd = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(simple24iliTd);

        // compile ilisMeta16 model
        TransferDescription ilisMeta16TransferDescription = CompilerTestHelper.getTransferDescription(ILIS_META16_ILI);
        assertNotNull(ilisMeta16TransferDescription);

        // read imd16 file and set transfer description
        Xtf24Reader reader = new Xtf24Reader(new File(OUT_FILE));
        reader.setModel(ilisMeta16TransferDescription);

        // gather all objects from imd16 file
        IoxEvent event;
        do {
            event = reader.read();
            if (event instanceof ObjectEvent) {
                IomObject iomObj = ((ObjectEvent) event).getIomObject();
                if (iomObj.getobjectoid() != null) {
                    metaObjects.put(iomObj.getobjectoid(), iomObj);
                }
            }
        } while (!(event instanceof EndTransferEvent));
    }

    @Test
    public void containsClass() {
        assertTrue(metaObjects.containsKey("Simple24.TestA.ClassA1"));
    }

    @Test
    public void genericDomain() {
        assertTrue(metaObjects.containsKey("Simple24.GenericDomain"));
        IomObject iomObject = metaObjects.get("Simple24.GenericDomain");
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("true", generic);
    }

    @Test
    public void nonGenericDomain() {
        assertTrue(metaObjects.containsKey("Simple24.Coord"));
        IomObject iomObject = metaObjects.get("Simple24.Coord");
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("false", generic);
    }

    @Test
    public void textType() {
        assertTrue(metaObjects.containsKey("Simple24.TestA.ClassA1.attr1.TYPE"));
        IomObject iomObject = metaObjects.get("Simple24.TestA.ClassA1.attr1.TYPE");
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("false", generic);
    }

    @Test
    public void context() {
        assertTrue(metaObjects.containsKey("Simple24.default"));
        IomObject contextObject = metaObjects.get("Simple24.default");
        String name = contextObject.getattrvalue("Name");
        assertEquals("default", name);
    }

    @Test
    public void genericDef() {
        assertTrue(metaObjects.containsKey("Simple24.default.GenericDomain"));
        IomObject genericDef = metaObjects.get("Simple24.default.GenericDomain");
        IomObject context = genericDef.getattrobj("Context", 0);
        IomObject genericDomain = genericDef.getattrobj("GenericDomain", 0);

        assertEquals("Simple24.default", context.getobjectrefoid());
        assertEquals("Simple24.GenericDomain", genericDomain.getobjectrefoid());
    }

    @Test
    public void concreteForGeneric() {
        String[] concretes = { "Coord", "Coord2" };
        for (String concrete : concretes) {
            assertTrue(metaObjects.containsKey("Simple24.default.GenericDomain." + concrete));
            IomObject concreteForGeneric = metaObjects.get("Simple24.default.GenericDomain." + concrete);
            IomObject genericDef = concreteForGeneric.getattrobj("GenericDef", 0);
            IomObject concreteDomain = concreteForGeneric.getattrobj("ConcreteDomain", 0);

            assertEquals("Simple24.default.GenericDomain", genericDef.getobjectrefoid());
            assertEquals("Simple24." + concrete, concreteDomain.getobjectrefoid());
        }
    }

    @Test
    public void modelAttributes24() {
        assertTrue(metaObjects.containsKey("Simple24"));
        IomObject simpleModel = metaObjects.get("Simple24");

        assertTrue(metaObjects.containsKey("ModelAttributes24"));
        IomObject attributesModel = metaObjects.get("ModelAttributes24");

        assertNotEquals("true", simpleModel.getattrvalue("NoIncrementalTransfer"));
        assertEquals("true", attributesModel.getattrvalue("NoIncrementalTransfer"));

        assertNull(simpleModel.getattrvalue("CharSetIANAName"));
        assertEquals("UTF-8", attributesModel.getattrvalue("CharSetIANAName"));

        assertNull(simpleModel.getattrvalue("xmlns"));
        assertEquals("http://www.interlis.ch/custom/namespace", attributesModel.getattrvalue("xmlns"));
    }
}
