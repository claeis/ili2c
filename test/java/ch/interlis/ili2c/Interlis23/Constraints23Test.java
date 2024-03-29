package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.Main;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import ch.interlis.iox.IoxException;

public class Constraints23Test {

	private static final String TEST_OUT="test/data/ili23/constraints/";
	
	@Test
	public void uniqueGlobal_PreCondition() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueGlobal_PreCondition.ili", FileEntryKind.ILIMODELFILE);
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
	
	// This test checks a basic mandatory constraint.
	@Test
	public void mandatory_BasicMandatoryConstraint() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"mandatory_BasicMandatoryConstraint.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "model");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "topic");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "aclass");
		assertNotNull(classB);
	}
	
	// This test checks a basic existence constraint.
	@Test
	public void existence_BasicExistenceConstraint() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"existence_BasicExistenceConstraint.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "model");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "topic");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "aclass");
		assertNotNull(classB);
	}
    @Test
    public void existence_TypeIncompatibility() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"existence_TypeIncompatibility.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("The attribute type of \"otherAttr\" does not match \"attr\".", logEvent.getRawEventMsg());
    }
	
	// This test checks a basic local uniqueness constraint.
	@Test
	public void uniqueLocal_Basic() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueLocal_Basic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "model");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "topic");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "aclass");
		assertNotNull(classB);
	}
	
	// This test checks a basic global uniqueness constraint.
	@Test
	public void uniqueGlobal_Basic() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueGlobal_Basic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "model");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "topic");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "aclass");
		assertNotNull(classB);
	}
    @Test
    public void uniqueGlobal_Path() throws Exception {
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueGlobal_Path.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNotNull(td);
    }
    @Test
    public void uniqueGlobal_AttrPath_Fail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueGlobal_AttrPath_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("unexpected cardinality of attribute \"ModelA.TopicA.ClassB:attrB2\".", logEvent.getRawEventMsg());
    }
    @Test
    public void uniqueGlobal_RolePath_Fail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueGlobal_RolePath_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("unexpected cardinality of role \"ModelA.TopicA.b2c:c\".", logEvent.getRawEventMsg());
    }
	
	// This test checks a basic ConstraintsDef.
	@Test
	public void constraintDef_Basic() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"constraintDef_Basic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "model");
		assertNotNull(modelB);
		Topic topicB=(Topic) modelB.getElement(Topic.class, "topic");
		assertNotNull(topicB);
		Table classB=(Table) topicB.getElement(Table.class, "aclass");
		assertNotNull(classB);
	}
    @Test
    public void setBasic_Fail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Settings settings=new Settings();
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"setBasic_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("There can not be a SET CONSTRAINT in a STRUCTURE model.topic.StructA.", logEvent.getRawEventMsg());
        
    }
	
	// This test checks a local unique constraint defined in a structure.
	@Test
	public void uniqueLocal_DefinedInStructure() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"uniqueLocal_DefinedInStructure.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		assertNotNull(td);
		Model modelB=(Model) td.getElement(Model.class, "Localization");
		assertNotNull(modelB);
		Table classA=(Table) modelB.getElement(Table.class, "LocalizedText");
		assertNotNull(classA);
		Table classB=(Table) modelB.getElement(Table.class, "MultilingualText");
		assertNotNull(classB);
	}
	
	// This test checks a existence constraint that requires a DEPENDS ON.
	@Test
	public void existence_RequireADependsOn_Fail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"existence_RequireADependsOn.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try {
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		}catch(Exception e) {
		}
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        assertEquals("This reference to a viewable requires a topic dependency between topicB and topicA", logEvent.getRawEventMsg());
	}
	
	// This test checks that a ConstraintsDef references a viewable in the same topic.
	@Test
	public void constraintDef_RefViewableInSameTopic() throws Exception {
		Settings settings=new Settings();
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"constraintDef_RefViewableInSameTopic.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try {
		td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig,settings);
		}catch(Exception e) {
		}
		assertNotNull(td);
	}
}
