package ch.interlis.ili2c;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import ch.interlis.ili2c.Interlis23.MainRule23Test;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;

public class ModelScanTest {
    @Test
    @Ignore("see issue#55")
    public void scanFile_eof_comment() {
        IliFile iliFile = ModelScan.scanIliFile(new java.io.File(MainRule23Test.TEST_OUT,"mainRules_eof_commentA.ili"));
        assertNotNull(iliFile);
        assertEquals(2.3,iliFile.getIliVersion(),0.01);
        assertEquals("modelA_eof",((IliModel)iliFile.iteratorModel().next()).getName());
    }

}
