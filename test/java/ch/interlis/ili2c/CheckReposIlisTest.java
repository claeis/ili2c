package ch.interlis.ili2c;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;

public class CheckReposIlisTest {
	
	public static final String TEST_REPOS="test/data/compiler/reposcheck_interlis";
	
    @Test
    public void dependsOnModelINTERLIS_Fail() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_REPOS);
            FileEntry fileEntry=new FileEntry(TEST_REPOS, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            boolean failed = new CheckReposIlis().checkRepoIlis(ili2cConfig, settings);
            assertEquals(true,failed);
            assertEquals("ModelA(TID=\"0\"): INTERLIS must not be listed as dependsOnModel",logs.getErrs().get(0).getEventMsg());
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
	
}