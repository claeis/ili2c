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

public class Model24Test {
	
	private static final String TEST_OUT="test/data/ili24/model/";
	
//	test/data/ili24/model/ModelA.ili:18:Duplicate EnumElement name "gelb".
//	test/data/ili24/model/ModelA.ili:76:unexpected token: LINE
//	test/data/ili24/model/ModelA.ili:76:expecting ':', found 'GrenzlinieEigenschaften'
//	test/data/ili24/model/ModelA.ili:118:Duplicate EnumElement name "gelb".
//	test/data/ili24/model/ModelA.ili:123:Duplicate EnumElement name "gelb".
	
	//TODO add comment
	@Ignore
	@Test
	public void ili24_ModelA() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ModelA.ili", FileEntryKind.ILIMODELFILE);
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