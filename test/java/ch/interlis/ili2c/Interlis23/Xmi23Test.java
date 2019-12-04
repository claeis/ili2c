package ch.interlis.ili2c.Interlis23;

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

public class Xmi23Test {
	
	private static final String TEST_OUT="test/data/ili23/xmi/";
	
	//TODO add comment
	@Ignore
	@Test
	public void xmi_MoPublic() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		
		FileEntry fileEntry=new FileEntry(TEST_OUT+"mopublic/CoordSys.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		
		FileEntry fileEntry2=new FileEntry(TEST_OUT+"mopublic/Lookup_tables_v1.2.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry2);
		
		FileEntry fileEntry3=new FileEntry(TEST_OUT+"mopublic/MOpublic_en_v1.2.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry3);
		
		FileEntry fileEntry4=new FileEntry(TEST_OUT+"mopublic/MOpublic95_en_v1.2.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry4);
		
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}