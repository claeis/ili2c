package ch.interlis.ili2c;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.AttributeRef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class ObjectPathTest {

	@Test
	public void simpleOk() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/ObjectPath/ObjectPath.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			
		}
		assertNotNull(td);
		Table structA=(Table) td.getElement("ModelA.TopicA.StructA");
		assertNotNull(structA);
        Table classB=(Table) td.getElement("ModelA.TopicA.ClassB");
        assertNotNull(classB);
		AttributeDef attrA=(AttributeDef) structA.getElement(AttributeDef.class, "attrA");
		assertNotNull(attrA);
		AttributeDef attrB=(AttributeDef) classB.getElement(AttributeDef.class, "attrB");
		assertNotNull(attrB);
		
		ObjectPath path=ch.interlis.ili2c.Main.parseObjectOrAttributePath(classB,"attrB->attrA");
		assertEquals(classB,path.getRoot());
		PathEl[] pathEl=path.getPathElements();
		assertEquals(2,pathEl.length);
		assertEquals(attrB,((AttributeRef)pathEl[0]).getAttr());
        assertEquals(attrA,((AttributeRef)pathEl[1]).getAttr());
		
	}
    @Test(expected=Ili2cException.class)
    public void simpleFail() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/ObjectPath/ObjectPath.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
            
        }
        assertNotNull(td);
        Table structA=(Table) td.getElement("ModelA.TopicA.StructA");
        assertNotNull(structA);
        Table classB=(Table) td.getElement("ModelA.TopicA.ClassB");
        assertNotNull(classB);
        AttributeDef attrA=(AttributeDef) structA.getElement(AttributeDef.class, "attrA");
        assertNotNull(attrA);
        AttributeDef attrB=(AttributeDef) classB.getElement(AttributeDef.class, "attrB");
        assertNotNull(attrB);
        
        ObjectPath path=ch.interlis.ili2c.Main.parseObjectOrAttributePath(classB,"attrB->attrA=="); // unexpected token ==
        
    }

}
