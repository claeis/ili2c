package ch.interlis.ili2c.Interlis24;

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

public class Refhb24Test {
	
	private static final String TEST_OUT="test/data/ili24/refhb/";
	
	@Test
	public void roadsExgm2ien() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
        ili2cConfig.addFileEntry(new FileEntry(TEST_OUT + "RoadsExdm2ben.ili", FileEntryKind.ILIMODELFILE));
        ili2cConfig.addFileEntry(new FileEntry(TEST_OUT + "AbstractSymbology.ili", FileEntryKind.ILIMODELFILE));
        ili2cConfig.addFileEntry(new FileEntry(TEST_OUT + "RoadsExdm2ien.ili", FileEntryKind.ILIMODELFILE));
        ili2cConfig.addFileEntry(new FileEntry(TEST_OUT + "StandardSymbology.ili", FileEntryKind.ILIMODELFILE));
		ili2cConfig.addFileEntry(new FileEntry(TEST_OUT + "RoadsExgm2ien.ili", FileEntryKind.ILIMODELFILE));
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}