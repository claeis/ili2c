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
    private static HashMap<String, IomObject> metaObjects = new HashMap<String, IomObject>();

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
        assertTrue(metaObjects.containsKey("Simple24.TestA.GenericDomain"));
        IomObject iomObject = metaObjects.get("Simple24.TestA.GenericDomain");
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("true", generic);
    }

    @Test
    public void nonGenericDomain() {
        assertTrue(metaObjects.containsKey("Simple24.TestA.Coord"));
        IomObject iomObject = metaObjects.get("Simple24.TestA.Coord");
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
}
