package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Main;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.FormattedType;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.metamodel.LineType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.PrecisionDecimal;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.Viewable;

public class LineType23Test {

	@Test
	public void simple() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/lineType/LineType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Viewable classA1=(Viewable) td.getElement("LineType.TopicA.ClassA1");
		AttributeDef attr=(AttributeDef) classA1.getElement(AttributeDef.class, "Geometry");
		LineType type=(LineType) attr.getDomain();
		PrecisionDecimal maxOverlaps=type.getMaxOverlap();
		assertEquals(new PrecisionDecimal("0.001"),maxOverlaps);
	}
	
    @Test
    public void overlapTooSmallFail() throws Exception {
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/lineType/OverlapTooSmallFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNull(td);
    }
}