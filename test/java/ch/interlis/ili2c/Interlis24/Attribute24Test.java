package ch.interlis.ili2c.Interlis24;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Cardinality;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.TextType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeAlias;

public class Attribute24Test {
	
	private static final String TEST_OUT="test/data/ili24/attribute/";
    public static final String ILI_DATE_TIME = TEST_OUT+"dateTime.ili";
    public static final String ILI_TEXT_LIST = TEST_OUT+"text_List.ili";
	
    @Test
    public void attributeRef_RefAttrInClass() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"attributeRef_RefAttrInClass.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
    }
    @Test
    public void text_List() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(ILI_TEXT_LIST, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
        AttributeDef attrA=(AttributeDef) td.getElement("ModelA.TopicA.ClassA.attrA");
        assertEquals(TextType.class,attrA.getDomain().getClass());
        assertEquals(0,attrA.getDomain().getCardinality().getMinimum());
        assertEquals(Cardinality.UNBOUND,attrA.getDomain().getCardinality().getMaximum());
    }
    @Test
    public void dateTime() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(ILI_DATE_TIME, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
        
        Domain domainDate=(Domain) td.getElement("ModelA.DomainDate");
        assertEquals(td.INTERLIS.XmlDate,((TypeAlias)(domainDate.getType())).getAliasing());
        
        Domain domainDateTime=(Domain) td.getElement("ModelA.DomainDateTime");
        assertEquals(td.INTERLIS.XmlDateTime,((TypeAlias)(domainDateTime.getType())).getAliasing());
        
        Domain domainTimeOfDay=(Domain) td.getElement("ModelA.DomainTimeOfDay");
        assertEquals(td.INTERLIS.XmlTime,((TypeAlias)(domainTimeOfDay.getType())).getAliasing());
        
        AttributeDef attrDate=(AttributeDef) td.getElement("ModelA.TopicA.ClassA.attrDate");
        assertEquals(td.INTERLIS.XmlDate,((TypeAlias)(attrDate.getDomain())).getAliasing());
        
        AttributeDef attrDateTime=(AttributeDef) td.getElement("ModelA.TopicA.ClassA.attrDateTime");
        assertEquals(td.INTERLIS.XmlDateTime,((TypeAlias)(attrDateTime.getDomain())).getAliasing());
        
        AttributeDef attrTimeOfDay=(AttributeDef) td.getElement("ModelA.TopicA.ClassA.attrTimeOfDay");
        assertEquals(td.INTERLIS.XmlTime,((TypeAlias)(attrTimeOfDay.getDomain())).getAliasing());
        
    }
}