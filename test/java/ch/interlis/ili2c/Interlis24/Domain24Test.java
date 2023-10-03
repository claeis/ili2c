package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Domain24Test {
    LogCollector errs = null;
    TransferDescription td = null;

    @Before
    public void setUp() {
        errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        td = CompilerTestHelper.getTransferDescription("test/data/ili24/domain/domain.ili");
    }

    @Test
    public void compile_Ok() {
        assertEquals(0, errs.getErrs().size());
        List<Domain> domains = CompilerTestHelper.getInstancesOfType(td, "ModelA.TopicA", Domain.class);
        assertEquals(3, domains.size());
    }
}
