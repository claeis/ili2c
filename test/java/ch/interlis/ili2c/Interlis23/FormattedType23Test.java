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
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.ili2c.metamodel.Viewable;

public class FormattedType23Test {

	@Test
	public void simple() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/formattedType/FormattedType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model model=(Model) td.getElement(Model.class, "FormattedType");
		Topic topicA=(Topic) model.getElement(Topic.class, "TopicA");
		Viewable classA1=(Viewable) topicA.getElement(Viewable.class, "ClassA1");
		AttributeDef attr=(AttributeDef) classA1.getElement(AttributeDef.class, "attr1");
		FormattedType type=(FormattedType) attr.getDomain();
		assertTrue(type.isValueInRange("2017-05-12"));
		assertTrue(type.isValueInRange("2010-05-12"));
		assertTrue(type.isValueInRange("2110-05-12"));
		assertFalse(type.isValueInRange("2217-05-12"));
		assertFalse(type.isValueInRange("2007-05-12"));
	}
	@Test
	public void baseDomainRestricted() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/formattedType/FormattedType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model model=(Model) td.getElement(Model.class, "FormattedType");
		Topic topicA=(Topic) model.getElement(Topic.class, "TopicA");
		Viewable classA1=(Viewable) topicA.getElement(Viewable.class, "ClassA1");
		AttributeDef attr=(AttributeDef) classA1.getElement(AttributeDef.class, "attr2");
		FormattedType type=(FormattedType) attr.getDomain();
		assertTrue(type.isValueInRange("2017-05-12"));
		assertTrue(type.isValueInRange("2010-1-1"));
		assertTrue(type.isValueInRange("2110-1-1"));
		assertFalse(type.isValueInRange("2217-05-12"));
		assertFalse(type.isValueInRange("2007-05-12"));
	}
}
