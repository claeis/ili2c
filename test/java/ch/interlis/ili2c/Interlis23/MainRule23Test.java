package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class MainRule23Test {
	
	public static final String TEST_OUT="test/data/ili23/mainRule/";
	
	// This test checks if the compiler ensures model name uniqueness.
	@Test
	public void mainRules_UniqueModelName() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"mainRules_UniqueModelName.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(6, compilerLogEvent.getLine());
		assertEquals("The name of the model conflicts with MODEL model. Model names must be unique; please use a different name.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test check if the compiler detects an illegal INTERLIS version.
	@Test
	public void mainRules_IllegalIliVersion() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"mainRules_IllegalIliVersion.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		LogEvent logEvent= errs.getErrs().get(0);
		assertEquals("Only INTERLIS version 1, 2.2, 2.3 and 2.4 can be read by this version of the compiler.", logEvent.getEventMsg());
	}
	
	// This test checks the main syntax rule of INTERLIS 2.3.
	@Test
	public void mainRules_SyntaxIli23() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"mainRules_SyntaxIli23.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
    @Test
    public void mainRules_eof_comment() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        ili2cConfig.setAutoCompleteModelList(true);
        FileEntry fileEntryB=new FileEntry(TEST_OUT+"mainRules_eof_commentB.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntryB);
        TransferDescription td=null;
        EhiLogger.getInstance().setTraceFilter(false);
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(td);
    }
}