package ch.interlis.ili2c.Interlis23.translation;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Translation23Test {

    @Test
    public void simpleOk() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/simpleOk.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
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
    public void topicCountLessFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/topicCountLessFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
    @Test
    public void topicCountMoreFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/topicCountMoreFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
    @Test
    public void attrCountLessFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/attrCountLessFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
    @Test
    public void attrCountMoreFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/attrCountMoreFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
    @Test
    public void attrDomainRefFail() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/attrDomainRefFail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
    }
    @Test
    public void enumOk() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/translation/enumOk.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNotNull(td);
        Model modelA=(Model) td.getElement(Model.class, "ModelA");
        assertNotNull(modelA);
        Topic topicA=(Topic) modelA.getElement(Topic.class, "TopicA");
        assertNotNull(topicA);
        Table classA=(Table) topicA.getElement(Table.class, "ClassA");
        assertNotNull(classA);
        AttributeDef attrA=(AttributeDef) classA.getElement(AttributeDef.class, "attrA");
        assertNotNull(attrA);
        EnumerationType typeA=(EnumerationType) attrA.getDomain();
        
        Model modelB=(Model) td.getElement(Model.class, "ModelB");
        assertNotNull(modelB);
        Topic topicB=(Topic) modelB.getElement(Topic.class, "TopicB");
        assertNotNull(topicB);
        Table classB=(Table) topicB.getElement(Table.class, "ClassB");
        assertNotNull(classB);
        AttributeDef attrB=(AttributeDef) classB.getElement(AttributeDef.class, "attrB");
        assertNotNull(attrB);
        EnumerationType typeB=(EnumerationType) attrB.getDomain();


        assertEquals(modelA,modelB.getTranslationOf());
        assertEquals(topicA,topicB.getTranslationOf());
        assertEquals(classA,classB.getTranslationOf());
        assertEquals(attrA,attrB.getTranslationOf());
        assertEquals(typeA.getEnumeration().getElement(0),typeB.getEnumeration().getElement(0).getTranslationOf());
    }


}
