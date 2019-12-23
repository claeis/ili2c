package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class InterlisManual23Test {
	
	private static final String TEST_OUT="test/data/ili23/interlismanual/";
	
	// Appendix J (CoordSys) from the INTERLIS reference manual
	@Test
	public void ilimanual_CoordSysAppendix() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"coordsys/ilimanual_CoordSysAppendix.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"coordsys/ilimanual_CoordSysAppendixEx.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// Appendix K (Symbology) from the INTERLIS reference manual
	@Ignore
	@Test
	public void ilimanual_AbstractSymbology() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"symbology/ilimanual_AbstractSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"symbology/ilimanual_StandardSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// Appendix J (CoordSys) from the INTERLIS reference manual
	@Test
	public void ilimanual_CoordSys() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ilimanual_CoordSys.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// Appendix H (Time) from the INTERLIS reference manual
	@Ignore
	@Test
	public void ilimanual_TimeAppendix() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"time/ilimanual_TimeAppendix.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"time/Units.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}	
	
	// Appendix G (Units) from the INTERLIS reference manual
	@Test
	public void ilimanual_Units() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ilimanual_Units.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// Appendix C (Roads example) from the INTERLIS reference manual
	@Ignore
	@Test
	public void ilimanual_RoadsAppendix() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"roads/AbstractSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"roads/RoadsExdm2ben.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		FileEntry fileEntry3=new FileEntry(TEST_OUT+"roads/RoadsExdm2ien.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry3);
		FileEntry fileEntry4=new FileEntry(TEST_OUT+"roads/RoadsExgm2ien.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry4);
		FileEntry fileEntry5=new FileEntry(TEST_OUT+"roads/StandardSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry5);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}