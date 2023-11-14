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

    @Test
    public void multipleContextsExtending() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(TEST_OUT + "multipleContextsExtending.ili");
        assertNotNull(td);
    }

    @Test
    public void multipleContextsNotExtending() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "multipleContextsNotExtending_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("The domain Coord2_SmallArea_CHLV95 must match or extend a domain of the existing context for generic type Coord2.", 1, errs);
    }

    @Test
    public void deferredDomainNoContext() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "deferredDomainNoContext_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("ModelA.TopicA has no accessible context definition for deferred generic Coord2.", 1, errs);
    }

    @Test
    public void invalidDeferredDomain() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "invalidDeferredDomain_fail.ili");

        assertEquals(3, errs.getErrs().size());
        assertContainsError("There is no domain \"InexistentDomain\" in MODEL ModelA.", 1, errs);
        assertContainsError("ModelA.TopicA has no accessible context definition for deferred generic InexistentDomain.", 1, errs);
        assertContainsError("ModelA.TopicA has an unused deferred generic definition for InexistentDomain.", 1, errs);
    }

    @Test
    public void deferredMissing() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "deferredMissing_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("ModelA.TopicA is missing a deferred generic definition for Coord2.", 1, errs);
    }

    @Test
    public void deferredUnusedType() {
        LogCollector errs = CompilerTestHelper.getCompileErrors(TEST_OUT + "deferredUnusedType_fail.ili");

        assertEquals(1, errs.getErrs().size());
        assertContainsError("ModelA.TopicA has an unused deferred generic definition for Coord2.", 1, errs);
    }
}
