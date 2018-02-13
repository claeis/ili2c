package ch.interlis.ili2c;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;

public class Constraints23Test {

	@Test
	public void uniquePreConditionOk() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/constraints/UniquePreCondition.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "UniquePreCondition");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "TopicA");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "ClassF");
		assertNotNull(classB);
		java.util.Iterator<AbstractLeafElement> eles=classB.iterator();
		for(;eles.hasNext();){
			AbstractLeafElement ele = eles.next();
			if(ele instanceof UniquenessConstraint){
				assertNotNull(((UniquenessConstraint) ele).getPreCondition());
			}
		}
	}
}
