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

public class SurfaceType23Test {
	
	private static final String TEST_OUT="test/data/ili23/surfacetype/";
	
	// This test checks if the compiler accepts a basic line attribute
	@Test
	public void surface_BasicLineAttr() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"surface_BasicLineAttr.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
	}
	
	// This test checks if the compiler detects a missing overlaps
	@Ignore
	@Test
	public void surface_MissingOverlaps() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"surface_MissingOverlaps.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
	}
	
	// This test checks if the compiler detects a class as a line attribute
	@Test
	public void surface_ClassAsLineAttr() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"surface_ClassAsLineAttr.ili", FileEntryKind.ILIMODELFILE);
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
			assertEquals(20, compilerLogEvent.getLine());
			assertEquals("Referenced element \"model.topic.LAttrs\" should be a STRUCTURE.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects an SURFACE with an illegal OVERLAP extension.
	@Ignore
	@Test
	public void surface_IllegalOverlapExtension_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"surface_IllegalOverlapExtension.ili", FileEntryKind.ILIMODELFILE);
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
			assertEquals("TRANSIENT mode of attribute \"attr\" requires to be the same as of base attribute.", compilerLogEvent.getRawEventMsg());
		}
	}
}