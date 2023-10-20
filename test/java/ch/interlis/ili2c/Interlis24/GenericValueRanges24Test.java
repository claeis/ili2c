package ch.interlis.ili2c.Interlis24;

import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Test;

import static ch.interlis.ili2c.LogCollectorAssertions.assertContainsError;
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
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "invalidDimension_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("Expected coord type to have 3 dimensions but it has 2 dimensions.", 1, errs);
    }

    @Test
    public void invalidRange() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "invalidRange_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("When rounded to 0 digits after the dot, the maximum must not exceed the inherited value. However, 200 gets rounded to 200, which is greater than 100.", 1, errs);
    }

    @Test
    public void contextDomainNotGeneric() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "contextDomainNotGeneric_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("The domain CoordA of context Context1 must be generic.", 1, errs);
    }
}