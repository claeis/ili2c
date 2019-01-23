package ch.interlis.ili2c;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class UnicodeDoc23Test {

	@Test
	public void simpleOk() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/class/UnicodeDoc.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
		Table classA=(Table) td.getElement("UnicodeDoc.TopicA.ClassA");
		assertNotNull(classA);
		assertEquals("Doc\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC\u00E0\u00F6\u00E9\u00E8",classA.getDocumentation());
	}

}
