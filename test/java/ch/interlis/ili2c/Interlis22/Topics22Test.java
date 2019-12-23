package ch.interlis.ili2c.Interlis22;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Topics22Test {
	
	private static final String TEST_OUT="test/data/ili22/topics/";
	
	// This test checks if the compiler accepts a basic topic definition.
	@Test
	public void acceptBasicTopicDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptBasicTopicDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an non-matching name at the end of the topic.
	@Test
	public void detectNonMatchingname_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectNonMatchingname.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(4, compilerLogEvent.getLine());
		assertEquals("TOPIC model.topic should end with \"END topic\", but \"END topicx\" was found.", compilerLogEvent.getRawEventMsg());
	}
	
	//This test checks if the compiler ensures class name uniqueness inside a topic.
	@Test
	public void compilerEnsuresClassnameUniqueInTopic_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"compilerEnsuresClassnameUniqueInTopic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(8, compilerLogEvent.getLine());
			assertEquals("There can not be a ClassDef \"class\" in TOPIC model.topic because there is already a model element with the same name. Use a different name.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a class and a domain with the same name inside a topic.
	@Test
	public void detectClassAndDomainWith_SamenameInTopic_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectClassAndDomainWith_SamenameInTopic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(9, compilerLogEvent.getLine());
			assertEquals("There can not be a ClassDef \"class\" in TOPIC model.topic because there is already a model element with the same name. Use a different name.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler dectets a name conflict with an inherited type from a base topic.
	@Test
	public void detectConflictInheritedTypeOfBaseTopic_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectConflictInheritedTypeOfBaseTopic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(12, compilerLogEvent.getLine());
			assertEquals("There can not be a DOMAIN \"domain\" in TOPIC model.topic because there is already a model element with the same name. Use a different name.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts unqualified class references in a extended topic to classes in a base topic.
	@Test
	public void acceptUnqualifiedClassRef_InExtendedTopic() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptUnqualifiedClassRef_InExtendedTopic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler checks topic dependencies of reference attributes.
	@Test
	public void checkTopicDependenciesOfRefAttrs_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkTopicDependenciesOfRefAttrs.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(9, compilerLogEvent.getLine());
			assertEquals("This reference attribute requires a topic dependency between topic2 and topic1", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler checks topic dependencies on views.
	@Test
	public void checkTopicDepedenciesOnViews_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkTopicDepedenciesOnViews.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(9, compilerLogEvent.getLine());
			assertEquals("This reference to a viewable requires a topic dependency between topic2 and topic1", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler checks topic dependencies on GraphicDefs.
	@Test
	public void checkTopicDependenciesOnGraphicDefs_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkTopicDependenciesOnGraphicDefs.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(9, compilerLogEvent.getLine());
			assertEquals("This reference to a viewable requires a topic dependency between topic2 and topic1", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts a BASKET domain without basket kind.
	@Test
	public void acceptBasketDomainWithoutBasketKind() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptBasketDomainWithoutBasketKind.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a BASKET domain with multiple basket kinds.
	@Ignore
	@Test
	public void detectBasketDomainWithMultipleBasketKind_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectBasketDomainWithMultipleBasketKind.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals("Invlaid kind for domain BASKET. Only DATA, VIEW, BASE, GRAPHIC or undefined.", compilerLogEvent.getRawEventMsg());
	}
}