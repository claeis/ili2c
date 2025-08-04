package ch.interlis.ilirepository.impl;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class FetchFromHttpServerTest {
    @Test
    public void simple() throws Exception
    {
        RepositoryAccess repos=new RepositoryAccess();
        File localFile=repos.getLocalFileLocation("https://models.interlis.ch/refhb23/MiniCoordSysData-20200320.xml");
        Assert.assertTrue(localFile.exists());
    }
}
