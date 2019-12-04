package ch.interlis.ili2c.Interlis22;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class InterlisManual22Test {
	
	private static final String TEST_OUT="test/data/ili22/ilimanual/";
	
	// Appendix C (Roads example) from the INTERLIS reference manual
	@Ignore
	@Test
	public void appendixC_RefRoad() {
		final String APPENDIX="appendixc/";
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+APPENDIX+"AbstractSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		FileEntry fileEntry2=new FileEntry(TEST_OUT+APPENDIX+"RoadsExdm2ben.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		FileEntry fileEntry3=new FileEntry(TEST_OUT+APPENDIX+"RoadsExdm2ien.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry3);
		FileEntry fileEntry4=new FileEntry(TEST_OUT+APPENDIX+"RoadsExgmn2ien.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry4);
		FileEntry fileEntry5=new FileEntry(TEST_OUT+APPENDIX+"StandardSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry5);
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
	public void appendixG_Units() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"appendixg/Units.ili", FileEntryKind.ILIMODELFILE);
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
	public void appendixH_Time() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"appendixh/Time.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
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
	public void appendixJ_CoordSys() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"appendixj/CoordSys.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
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
	public void appendixK_StandardSymbology() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"appendixk/StandardSymbology.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// Appendix J (CoordSys) from the INTERLIS reference manual
	@Ignore
	@Test
	public void appendixJ_CoordSysEx() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"appendixj/CoordSysEx.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}