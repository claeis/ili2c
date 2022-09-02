package ch.interlis.ili2c.generator;

import org.junit.Assert;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.Interlis24.Attribute24Test;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class ILI24GeneratorTest {
    @Test
    public void dateTime() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_DATE_TIME, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainDate"), null);
            Assert.assertEquals("DOMAIN DomainDate = DATE;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrDate"), null);
            Assert.assertEquals("attrDate : DATE;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }

}