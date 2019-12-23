package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;
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

public class TextType23Test {
	
	private static final String TEST_OUT="test/data/ili23/texttype/";

	// This test checks if the compiler detects a reference to a non existing DomainDef.
	@Test
	public void string_RefToExistingDomainDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_RefToExistingDomainDef.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals("There is neither a domain \"x\" in TOPIC model.topic nor in MODEL model.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts an unlimited length MTEXT attribute.
	@Test
	public void string_UnlimitedLengthMText() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_UnlimitedLengthMText.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
	}
	
	// This test checks if the compiler detects a multiline TEXT attribute that is extended to kind of single line.
	@Test
	public void string_MultilineTextExtendedToSingleLine_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_MultilineTextExtendedToSingleLine.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(10, compilerLogEvent.getLine());
		assertEquals("TEXT can not extend MTEXT. The kind of text (multiline/singleline) should not be changed.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects a limited length TEXT attribute that is extended to unlimited length.
	@Test
	public void string_LimitLengthExtendedToUnlimitedLength_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_LimitLenghExtendedToUnlimitedLength.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(10, compilerLogEvent.getLine());
		assertEquals("TEXT can not extend TEXT*20. The maximal length must not exceed the inherited length.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a restricted unlimited length TEXT attribute.
	@Test
	public void string_RestrictedUnlimitedTextLength() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_RestrictedUnlimitedTextLength.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
	}
	
	// This test checks if the compiler accepts an unlimited length TEXT attribute.
	@Test
	public void string_UnlimitedTextLength() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_UnlimitedTextLength.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
	}
	
	// This test checks if the compiler accepts an abstract TEXT attribute.
	@Test
	public void string_AcceptAbstractText() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"text_AcceptAbstractText.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
	}
}