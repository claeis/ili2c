package ch.interlis.ilirepository.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ReadIliModelsXmlTest {
    public static final String TEST_IN="test/data/ilimodels";
    @Test
    public void simple09() throws Exception
    {
        List<ModelMetadata> models=RepositoryAccess.readIliModelsXml2(new File(TEST_IN,"simple09.xml"));
        assertEquals(2,models.size());
    }
    @Test
    public void simple20() throws Exception
    {
        List<ModelMetadata> models=RepositoryAccess.readIliModelsXml2(new File(TEST_IN,"simple20.xml"));
        assertEquals(2,models.size());
    }
    @Test
    public void simple09_20() throws Exception
    {
        List<ModelMetadata> models=RepositoryAccess.readIliModelsXml2(new File(TEST_IN,"simple09_20.xml"));
        assertEquals(4,models.size());
    }
}
