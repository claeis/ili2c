package ch.interlis.ili2c.Interlis24;

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
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Expressions24Test {
	
	private static final String TEST_OUT="test/data/ili24/expressions/";

    @Test
    public void plus_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "plus_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        MandatoryConstraint constraint = (MandatoryConstraint)td.getElement("ModelA.TopicA.ClassA.Constraint1");
        Expression.GreaterThan expression = (Expression.GreaterThan) constraint.getCondition();
        assertTrue(expression.getLeft() instanceof Expression.Addition);
    }
    @Test
    public void plus_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "plus_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("numeric expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void minus_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "minus_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        MandatoryConstraint constraint = (MandatoryConstraint)td.getElement("ModelA.TopicA.ClassA.Constraint1");
        Expression.GreaterThan expression = (Expression.GreaterThan) constraint.getCondition();
        assertTrue(expression.getLeft() instanceof Expression.Subtraction);
    }
    @Test
    public void minus_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "minus_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("numeric expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void mul_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "mul_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        MandatoryConstraint constraint = (MandatoryConstraint)td.getElement("ModelA.TopicA.ClassA.Constraint1");
        Expression.GreaterThan expression = (Expression.GreaterThan) constraint.getCondition();
        assertTrue(expression.getLeft() instanceof Expression.Multiplication);
    }
    @Test
    public void mul_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "mul_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("numeric expression required.", logEvent.getRawEventMsg());
    }
    @Test
    public void div_numeric() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "div_numeric.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
        MandatoryConstraint constraint = (MandatoryConstraint)td.getElement("ModelA.TopicA.ClassA.Constraint1");
        Expression.GreaterThan expression = (Expression.GreaterThan) constraint.getCondition();
        assertTrue(expression.getLeft() instanceof Expression.Division);
    }
    @Test
    public void div_numeric_fail() throws Exception {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(TEST_OUT + "div_numeric_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td = null;
        td = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNull(td);
        assertEquals(1, errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("numeric expression required.", logEvent.getRawEventMsg());
    }
}