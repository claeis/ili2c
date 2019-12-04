package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.Main;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Enumerations23Test {
	
	private static final String TEST_OUT="test/data/ili23/enumerations/";
	
	@Test
	public void extendedEnum() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedEnum.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
	}
	@Test
	public void extendedEnumFail() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedEnumFail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNull(td);
	}
	
	// This test checks an extended enumeration type.
	@Ignore
	@Test
	public void enumeration_ExtendedEnumerationTypeFails() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_ExtendedEnumerationTypeFails.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(2,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(13, compilerLogEvent.getLine());
			assertEquals("Can not add horizontal extension \"mittel\" because of FINAL in base definition..", compilerLogEvent.getRawEventMsg());
		}
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(1);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(30, compilerLogEvent.getLine());
			assertEquals("Can not add horizontal extension \"hell\" because of FINAL in base definition..", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks an extended enumeration type
	@Test
	public void enumeration_ExtendedEnumerationType() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_ExtendedEnumerationType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks uniqueness of elements in an extened enumeration type
	@Test
	public void enumeration_UniqueElementsInExtendEnum() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_UniqueElementsInExtendEnum.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(11, compilerLogEvent.getLine());
		assertEquals("Duplicate EnumElement name \"rot\".", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks a basic enumeration type
	@Test
	public void enumeration_BasicEnumType() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_BasicEnumType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks uniqueness of elements in a basic enumeration type
	@Test
	public void enumeration_UniquenessElementsInBasicEnum() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_UniquenessElementsInBasicEnum.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(9, compilerLogEvent.getLine());
		assertEquals("Duplicate EnumElement name \"rot\".", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks uniqueness of XSD elements in a basic enumeration type
	@Ignore
	@Test
	public void enumeration_XSDUniquenessElementsInBasicEnum_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"enumeration_XSDUniquenessElementsInBasicEnum.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(9, compilerLogEvent.getLine());
		assertEquals("Duplicate EnumElement name \"rot\".", compilerLogEvent.getRawEventMsg());
	}
}
