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

public class MetaData22Test {
	
	private static final String TEST_OUT="test/data/ili22/metadata/";
	
	// This test checks a basic use of meta objects
	@Ignore
	@Test
	public void checksBasicUseOfMetaObj() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checksBasicUseOfMetaObj/checksBasicUseOfMetaObj.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"checksBasicUseOfMetaObj/meta.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects if a undefined meta object is referenced.
	@Ignore
	@Test
	public void detectUndefinedMetaObjectIsReferenced_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectUndefinedMetaObjectIsReferenced/detectUndefinedMetaObjectIsReferenced.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"detectUndefinedMetaObjectIsReferenced/meta.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
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
		assertEquals("There is no object \"refsysB\" in any data set defined by model.Basket.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks a qualified MetaDataUseRef
	@Ignore
	@Test
	public void checkQualifiedMetaDataUseRef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkQualifiedMetaDataUseRef/checkQualifiedMetaDataUseRef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"checkQualifiedMetaDataUseRef/meta.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks a basic use of COORDSYSTEM meta objects
	@Ignore
	@Test
	public void checkBasicUseOfCOORDSYSTEMMetaObj() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkBasicUseOfCOORDSYSTEMMetaObj/checkBasicUseOfCOORDSYSTEMMetaObj.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"checkBasicUseOfCOORDSYSTEMMetaObj/meta.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks a use of a extended MetaDataUseDef
	@Ignore
	@Test
	public void checkUseOfExtMetaData() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkUseOfExtMetaData/checkUseOfExtMetaData.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"checkUseOfExtMetaData/meta.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}