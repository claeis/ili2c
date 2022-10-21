package ch.interlis.ili2c;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class CompilerTest {
	
	private static final String TEST_REPOS="test/data/compiler/repos";
    private static final String TEST_LOCAL="test/data/compiler/local";
	
    @Test
    public void reposIliFileDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_REPOS);
            FileEntry fileEntry=new FileEntry(TEST_REPOS+"/compileFailsB.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(logs.getErrs().get(0).getEventMsg().endsWith("unexpected token: ;"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
    @Test
    public void reposIliFileMissingDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_REPOS);
            FileEntry fileEntry=new FileEntry(TEST_REPOS+"/compileFailsD.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(LogCollector.getMessage(logs.getErrs().get(0)).endsWith("compileFailsC: model(s) not found"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
    @Test
    public void reposModelNameDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_REPOS);
            FileEntry fileEntry=new FileEntry("compileFailsB", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(logs.getErrs().get(0).getEventMsg().endsWith("unexpected token: ;"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
    @Test
    public void reposModelNameDependsOnINTELIS() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,CheckReposIlisTest.TEST_REPOS);
            FileEntry fileEntry=new FileEntry("ModelA", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
    @Test
    public void localIliFileDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/compileFailsB.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(logs.getErrs().get(0).getEventMsg().endsWith("unexpected token: ;"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void localIliFileMissingDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/compileFailsD.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(LogCollector.getMessage(logs.getErrs().get(0)).endsWith("compileFailsC: model(s) not found"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
        
    }
    @Test
    public void localModelNameDependentModelFails() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry("compileFailsB", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNull(td);
            assertTrue(logs.getErrs().get(0).getEventMsg().endsWith("unexpected token: ;"));
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
	
}