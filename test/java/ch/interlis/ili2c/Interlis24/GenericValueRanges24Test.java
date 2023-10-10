package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenericValueRanges24Test {

    private static final String TEST_OUT = "test/data/ili24/genericValueRanges/";

    @Test
    public void contextInSameModel() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(TEST_OUT + "contextInSameModel.ili");
        assertNotNull(td);
    }

    @Test
    public void contextInDifferentModel() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(TEST_OUT + "contextInDifferentModel.ili");
        assertNotNull(td);
    }

    @Test
    public void invalidDimensions() {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);

        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT + "invalidDimension_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
        }

        assertEquals(1, errs.getErrs().size());

        String expected = "Expected coord type to have 3 dimensions but it has 2 dimensions.";
        assertTrue("expected: " + expected, errs.getErrs().get(0).getEventMsg().endsWith(expected));
    }

    @Test
    public void invalidRange() {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);

        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT + "invalidRange_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
        }

        assertEquals(1, errs.getErrs().size());

        String expected = "When rounded to 0 digits after the dot, the maximum must not exceed the inherited value. However, 200 gets rounded to 200, which is greater than 100.";
        assertTrue("expected: " + expected, errs.getErrs().get(0).getEventMsg().endsWith(expected));
    }

    @Test
    public void contextDomainNotGeneric() {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);

        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT + "contextDomainNotGeneric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
        }

        assertEquals(1, errs.getErrs().size());
        String expected = "The domain CoordA of context Context1 must be generic.";
        assertTrue("expected: " + expected, errs.getErrs().get(0).getEventMsg().endsWith(expected));
    }
}
