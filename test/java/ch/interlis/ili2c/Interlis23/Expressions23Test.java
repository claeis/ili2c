package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Expressions23Test {
	
	private static final String TEST_OUT="test/data/ili23/expressions/";

    @Test
    public void logical_simple() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_simple.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        assertEquals(0, errs.getErrs().size());
    }
    @Test
    public void logical_simple_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_simple_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("logical expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void logical_boolean() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_boolean.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        assertEquals(0, errs.getErrs().size());
    }
    @Test
    public void logical_defined() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_defined.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        assertEquals(0, errs.getErrs().size());
    }
    @Test
    public void logical_function() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        EhiLogger.getInstance().setTraceFilter(false);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_function.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        assertEquals(0, errs.getErrs().size());
    }
    @Test
    public void logical_function_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_function_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("logical expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void logical_not() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_not.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void logical_not_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_not_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("logical expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void logical_and() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_and.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void logical_and_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_and_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("logical expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void logical_or() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_or.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void logical_or_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "logical_or_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("logical expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_coord() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_coord.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_coord_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_coord_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_function() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_function.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_function_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_function_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_enum() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enum.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_enum_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enum_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_enum_const() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enum_const.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_enum_const_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enum_const_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("\"#r\" is not a member of the EnumerationType.", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_enumExtended_const() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enumExtended_const.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_enumExtended_const_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enumExtended_const_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("\"#b\" is not a member of the EnumerationType.", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_enumAllOf_const() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enumAllOf_const.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_enumAllOf_const_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_enumAllOf_const_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("\"#r\" is not a member of the EnumerationType.", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_formatted() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_formatted.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_formatted_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_formatted_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("Unexpected format of formatted value \"15\".", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_object() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_object.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_object_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_object_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_text() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_text.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_text_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_text_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_struct() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_struct.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_struct_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_struct_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_oid() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_oid.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_oid_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_oid_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_attributePathType() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_attributePathType.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_attributePathType_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_attributePathType_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void equals_classType() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_classType.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void equals_classType_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "equals_classType_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_coord() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_coord.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_coord_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_coord_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_enum() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_enum.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_enum_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_enum_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_enum_const() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_enum_const.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_enum_const_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_enum_const_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("\"#r\" is not a member of the EnumerationType.", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_formatted() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_formatted.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_formatted_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_formatted_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("Unexpected format of formatted value \"15\".", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_object() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_object.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_object_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_object_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void notEquals_text() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_text.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void notEquals_text_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "notEquals_text_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_enum() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void less_enum_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_enumAllOf_const() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enumAllOf_const.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void less_enumAllOf_const_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enumAllOf_const_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("\"#r\" is not a member of the EnumerationType.", logEvent.getRawEventMsg());
    }
    @Test
    public void less_enum_domain() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum_domain.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void less_enum_domain_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum_domain_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_enum_circular_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum_circular_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_enum_unordered_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_enum_unordered_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_coord_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_coord_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void less_formatted() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_formatted.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void less_formatted_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_formatted_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("Unexpected format of formatted value \"15\".", logEvent.getRawEventMsg());
    }
    @Test
    public void less_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void less_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "less_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void lessEquals_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "lessEquals_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void lessEquals_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "lessEquals_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void greaterEquals_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "greaterEquals_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void greaterEquals_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "greaterEquals_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
    @Test
    public void greater_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "greater_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
    @Test
    public void greater_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "greater_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("incompatible datatypes", logEvent.getRawEventMsg());
    }
	
	// This test checks THISAREA, THATAREA
	@Test
	public void expressions_ThisAreaThatArea() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ThisAreaThatArea.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts an association path threw a composition role without a cardinality.
	@Test
	public void expressions_CompositionRoleWithoutCard() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_CompositionRoleWithoutCard.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a THATAREA path element in a non area inspection view.
	@Test
	public void expressions_ThatAreaInNonAreaView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ThatAreaInNonAreaView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("An object path element THATAREA must not be used in VIEW Test.Base.VB.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects a THISAREA path element in a non area inspection view.
	@Test
	public void expressions_ThisAreaInNonAreaView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ThisAreaInNonAreaView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("An object path element THISAREA must not be used in VIEW Test.Base.VB.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects a PARENT path element in a area inspection view.
	@Test
	public void expressions_ParentElementInAreaView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ParentElementInAreaView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("An object path element PARENT must not be used in VIEW Test.Base.VB.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects a PARENT path element in a non inspection view.
	@Ignore
	@Test
	public void expressions_ParentPathInNonInspectionView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ParentPathInNonInspectionView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(22, compilerLogEvent.getLine());
		assertEquals("An object path element PARENT must not be used in VIEW Test.Base.VB.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a PARENT path element of an inspection of a sub-substructure.
	@Ignore
	@Test
	public void expressions_ParentPathEleOfSubStruct() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ParentPathEleOfSubStruct.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a PARENT path element.
	@Test
	public void expressions_ParentPathElement() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ParentPathElement.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}	
	
	// This test checks if the compiler accepts a base name path element to a unrenamed viewable.
	@Test
	public void expressions_BasenameToUnrenamedViewable() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_BasenameToUnrenamedViewable.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a base name path element.
	@Test
	public void expressions_BasenamePathElement() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_BasenamePathElement.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a THIS path element.
	@Test
	public void expressions_AcceptTHISElement() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_AcceptTHISElement.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a role name path element from an association.
	@Test
	public void expressions_ChecksRolenameOfAssociation() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_ChecksRolenameOfAssociation.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a role name element from a class that may point to multiple objects.
	@Ignore
	@Test
	public void expressions_DetectRoleToMultiObjects_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_DetectRoleToMultiObjects.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(22, compilerLogEvent.getLine());
		assertEquals("Role a may point to multiple objects.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a basic object path with a role name element from a class.
	@Test
	public void expressions_AcceptingBasicObjectWithRoleOfClass() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_AcceptingBasicObjectWithRoleOfClass.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an association path that may point to multiple objects.
	@Ignore
	@Test
	public void expressions_DetectAssocPathToMultiObjects_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_DetectAssocPathToMultiObjects.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(23, compilerLogEvent.getLine());
		assertEquals("Role a may point to multiple objects.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a basic object path with an association path element.
	@Test
	public void expressions_AcceptingObjectsWithAssocPathEle() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_AcceptingObjectsWithAssocPathEle.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic object path with a reference attribute name.
	@Test
	public void expressions_AcceptBasicObjectWithRefAttrname() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"expressions_AcceptBasicObjectWithRefAttrname.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}