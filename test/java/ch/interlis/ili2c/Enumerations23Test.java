package ch.interlis.ili2c;

import static org.junit.Assert.*;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;

public class Enumerations23Test {

	@Test
	public void extendedEnum() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/enumerations/ExtendedEnum.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
	}
	@Test
	public void extendedEnumFail() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/enumerations/ExtendedEnumFail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNull(td);
	}
}
