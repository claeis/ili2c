package ch.interlis.ili2c.generator;

import static org.junit.Assert.*;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.Interlis24.Attribute24Test;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.TypeAlias;

public class XSD24GeneratorTest {
	
	private static final java.io.File TEST_OUT=new java.io.File("build/xsd/");
	
    @Test
    public void dateTime() throws Exception {
        TEST_OUT.mkdirs();
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_DATE_TIME, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
        ch.interlis.ili2c.generator.XSD24Generator.generate (td, TEST_OUT);
        
        // read generated xsd
        java.io.File generatedSchemaDoc=new java.io.File(TEST_OUT,"ModelA.xsd");
        Schema schema=readSchema(generatedSchemaDoc);
        
    }
    @Test
    public void text_list() throws Exception {
        TEST_OUT.mkdirs();
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_TEXT_LIST, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNotNull(td);
        assertEquals(0,errs.getErrs().size());
        ch.interlis.ili2c.generator.XSD24Generator.generate (td, TEST_OUT);
        
        // read generated xsd
        java.io.File generatedSchemaDoc=new java.io.File(TEST_OUT,"ModelA.xsd");
        Schema schema=readSchema(generatedSchemaDoc);
        
    }

    private Schema readSchema(java.io.File generatedSchemaDoc) throws SAXException {
        javax.xml.transform.Source schemaFiles[] =  new javax.xml.transform.Source[]{
                new StreamSource("xsd/xtf/2.4/geometry.xsd"),
                new StreamSource("xsd/xtf/2.4/interlis.xsd"),
                new StreamSource(generatedSchemaDoc.getPath())};
        SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema=factory.newSchema(schemaFiles);
        return schema;
    }
}