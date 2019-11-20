package ch.interlis.ili2c.Interlis10;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Main;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Translation10Test {

	@Test
	public void simpleOk() throws Exception {
		Settings settings=new Settings();
		Ili2cMetaAttrs metaAttrs=new Ili2cMetaAttrs();
		metaAttrs.setMetaAttrValue("ModelB", Ili2cMetaAttrs.ILI2C_TRANSLATION_OF, "ModelA");
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili10/translation/ModelAsimpleOk.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		fileEntry=new FileEntry("test/data/ili10/translation/ModelBsimpleOk.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings,metaAttrs);
		assertNotNull(td);
		Model modelA=(Model) td.getElement(Model.class, "ModelA");
		assertNotNull(modelA);
		Topic topicA=(Topic) modelA.getElement(Topic.class, "TopicA");
		assertNotNull(topicA);
		Table classA=(Table) topicA.getElement(Table.class, "ClassA");
		assertNotNull(classA);
		AttributeDef attrA=(AttributeDef) classA.getElement(AttributeDef.class, "attrA");
		assertNotNull(attrA);
		
		Model modelB=(Model) td.getElement(Model.class, "ModelB");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "TopicB");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "ClassB");
		assertNotNull(classB);
		AttributeDef attrB=(AttributeDef) classB.getElement(AttributeDef.class, "attrB");
		assertNotNull(attrB);


		assertEquals(modelA,modelB.getTranslationOf());
		assertEquals(topicA,topicB.getTranslationOf());
		assertEquals(classA,classB.getTranslationOf());
		assertEquals(attrA,attrB.getTranslationOf());
	}
	@Test
	public void attrTypeFail() throws Exception {
		Settings settings=new Settings();
		Ili2cMetaAttrs metaAttrs=new Ili2cMetaAttrs();
		metaAttrs.setMetaAttrValue("AttrTypeFail", Ili2cMetaAttrs.ILI2C_TRANSLATION_OF, "ModelA");
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili10/translation/ModelAsimpleOk.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		fileEntry=new FileEntry("test/data/ili10/translation/AttrTypeFail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings,metaAttrs);
		assertNull(td);
	}

}
