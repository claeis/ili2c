package ch.interlis.ili2c.Interlis10;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class ScopedNameTest {

	@Test
	public void simpleOk() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/class/simpleOk.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
		Model modelA=(Model) td.getElement(Model.class, "ModelA");
		assertNotNull(modelA);
		Model modelB=(Model) td.getElement(Model.class, "ModelB");
		assertNotNull(modelB);
		Topic topicA=(Topic) modelA.getElement(Topic.class, "TopicA");
		assertNotNull(topicA);
		Topic topicA2=(Topic) modelA.getElement(Topic.class, "TopicA2");
		assertNotNull(topicA2);
		Table classA=(Table) topicA.getElement(Table.class, "ClassA");
		assertNotNull(classA);
		Table classA1=(Table) topicA.getElement(Table.class, "ClassA1");
		assertNotNull(classA1);
		AttributeDef attrA=(AttributeDef) classA.getElement(AttributeDef.class, "attrA");
		assertNotNull(attrA);
		AttributeDef attrA0=(AttributeDef) classA.getElement(AttributeDef.class, "attrA0");
		assertNotNull(attrA0);
		
		assertEquals("ModelA", modelA.getScopedName());
		assertEquals("ModelA.TopicA", topicA.getScopedName());
		assertEquals("ModelA.TopicA.ClassA", classA.getScopedName());
		assertEquals("ModelA.TopicA.ClassA.attrA", attrA.getScopedName());
		
		assertEquals("ModelA", modelA.getScopedName(modelA));
		assertEquals("TopicA", topicA.getScopedName(modelA));
		assertEquals("ModelA.TopicA.ClassA", classA.getScopedName(modelA));
		assertEquals("ModelA.TopicA.ClassA.attrA", attrA.getScopedName(modelA));
		
		assertEquals("ModelA", modelA.getScopedName(topicA));
		assertEquals("TopicA", topicA.getScopedName(topicA));
		assertEquals("ClassA", classA.getScopedName(topicA));
		assertEquals("ClassA.attrA", attrA.getScopedName(topicA));
		
		assertEquals("ModelA", modelA.getScopedName(classA));
		assertEquals("TopicA", topicA.getScopedName(classA));
		assertEquals("ClassA", classA.getScopedName(classA));
		assertEquals("attrA", attrA.getScopedName(classA));
		
		assertEquals("ModelA", modelA.getScopedName(modelB));
		assertEquals("ModelA.TopicA", topicA.getScopedName(modelB));
		assertEquals("ModelA.TopicA.ClassA", classA.getScopedName(modelB));
		assertEquals("ModelA.TopicA.ClassA.attrA", attrA.getScopedName(modelB));
		
		assertEquals("TopicA", topicA.getScopedName(topicA2));
		assertEquals("ModelA.TopicA.ClassA", classA.getScopedName(topicA2));
		assertEquals("ModelA.TopicA.ClassA.attrA", attrA.getScopedName(topicA2));
		
		assertEquals("ClassA", classA.getScopedName(classA1));
		assertEquals("ModelA.TopicA.ClassA.attrA", attrA.getScopedName(classA1));
        assertEquals(attrA,td.getElement("ModelA.TopicA.ClassA.attrA"));
        assertEquals(null,td.getElement("ModelA.TopicA.ClassA._attrA"));
		
	}

}
