package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class IliDoc23Test {

    @Test
    public void unterminatedDocComment_Fail() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/comments/unterminatedDocComment.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
            fail("compiler should fail because of unterminated comment");
        }catch(Ili2cFailure ex){
        }
        assertNull(td);
        assertTrue(errs.getErrs().size()>0);
        CompilerLogEvent logEvent=(CompilerLogEvent)errs.getErrs().get(0);
        assertEquals(4, logEvent.getLine());
        assertTrue(logEvent.getRawEventMsg().contains("Unterminated comment"));
    }
}
