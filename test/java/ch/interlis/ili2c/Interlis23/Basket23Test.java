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

public class Basket23Test {
	
	private static final String TEST_OUT="test/data/ili23/basket/";

	// This test checks if the compiler detects a BASKET domain with multiple basket kinds.
	@Ignore
	@Test
	public void basket_MultipleBasketKinds_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"basket_MultipleBasketKinds.ili", FileEntryKind.ILIMODELFILE);
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
			assertEquals(6, compilerLogEvent.getLine());
			assertEquals("Invlaid kind for domain BASKET. Only DATA, VIEW, BASE, GRAPHIC or undefined.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts a BASKET domain without basket kind.
	@Ignore
	@Test
	public void basket_BasketDomainWithoutBasketKind() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"basket_BasketDomainWithoutBasketKind.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
	}
}