package ch.interlis.ili2c;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class StandardModelTest {
	
    private static final String TEST_LOCAL="standard";
	
    @Test
    public void AbstractSymbology() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/AbstractSymbology.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void CoordSys() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/CoordSys.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void DatasetIdx16() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/DatasetIdx16.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void IliRepository09() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/IliRepository09.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void IliRepository20() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/IliRepository20.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void IliSite09() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/IliSite09.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void IlisMeta07() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/IlisMeta07.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void IlisMeta16() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/IlisMeta16.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void MinimalRuntimeSystem01() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/MinimalRuntimeSystem01.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void StandardSymbology() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/StandardSymbology.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void Time() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/Time.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
    @Test
    public void Units() throws Exception {
        LogCollector logs=new LogCollector();
        try {
            EhiLogger.getInstance().addListener(logs);
            Configuration ili2cConfig=new Configuration();
            Settings settings=new Settings();
            settings.setValue(Ili2cSettings.ILIDIRS,TEST_LOCAL);
            FileEntry fileEntry=new FileEntry(TEST_LOCAL+"/Units.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setAutoCompleteModelList(true);
            TransferDescription td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
            assertNotNull(td);
        }finally {
            EhiLogger.getInstance().removeListener(logs);
        }
    }
	
}