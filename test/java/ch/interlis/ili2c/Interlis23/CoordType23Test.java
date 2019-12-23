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

public class CoordType23Test {
	
	private static final String TEST_OUT_COORD="test/data/ili23/coordtype/";

	// This test checks if the compiler detects a COORD domaindef with different precision.
	@Ignore
	@Test
	public void coord_DomainDefWithDiffPrecision_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT_COORD+"coord_DomainDefWithDiffPrecision.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(2,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(8, compilerLogEvent.getLine());
			assertEquals("Both minimum and maximum value must have the same precision.", compilerLogEvent.getRawEventMsg());
		}
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(1);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(7, compilerLogEvent.getLine());
			assertEquals("DOMAIN LKoord must be declared ABSTRACT. It specifies an abstract type.", compilerLogEvent.getRawEventMsg());
		}
	}
}