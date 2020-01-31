package ch.interlis.ili2c.Interlis23.translation;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Attribute10Test {
    @Test
    public void attributeComplete() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Ili2cMetaAttrs ili2cMetaAttrs=new Ili2cMetaAttrs();
        ili2cMetaAttrs.setMetaAttrValue("ModelB", Ili2cMetaAttrs.ILI2C_TRANSLATION_OF, "ModelA");
        Configuration ili2cConfig=new Configuration();
        ili2cConfig.setGenerateWarnings(false);
        ili2cConfig.setLanguage("");
        FileEntry fileEntry=null;
        fileEntry=new FileEntry("test/data/ili23/translation/attribute10Complete_de.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        fileEntry=new FileEntry("test/data/ili23/translation/attribute10Complete_fr.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,null,ili2cMetaAttrs);
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
    }
    @Test
    public void attributeExplFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Ili2cMetaAttrs ili2cMetaAttrs=new Ili2cMetaAttrs();
        ili2cMetaAttrs.setMetaAttrValue("ModelB", Ili2cMetaAttrs.ILI2C_TRANSLATION_OF, "ModelA");
        Configuration ili2cConfig=new Configuration();
        ili2cConfig.setGenerateWarnings(false);
        ili2cConfig.setLanguage("");
        FileEntry fileEntry=null;
        fileEntry=new FileEntry("test/data/ili23/translation/attribute10ExplFail_de.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        fileEntry=new FileEntry("test/data/ili23/translation/attribute10ExplFail_fr.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,null,ili2cMetaAttrs);
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
}
