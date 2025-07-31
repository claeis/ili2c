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
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.PathEl;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class ExpressionTest {

	@Test
	public void simpleOk() throws Exception {
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry("test/data/ili23/expressions/standalone_expr.ili", FileEntryKind.ILIMODELFILE);
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
		
		Evaluable expr=ch.interlis.ili2c.Main.parseExpression(structA,PredefinedModel.getInstance().BOOLEAN.getType(),"attrA==\"Test\"","inline string");
		assertEquals(Expression.Equality.class,expr.getClass());
		
	}
    @Test
    public void simpleFail() throws Exception {
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry("test/data/ili23/expressions/standalone_expr.ili", FileEntryKind.ILIMODELFILE);
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
        
        try {
            Evaluable expr=ch.interlis.ili2c.Main.parseExpression(structA,PredefinedModel.getInstance().BOOLEAN.getType(),"attrA=\"Test\"","inline string"); // unexpceted token =
            fail();
        }catch(Ili2cException ex) {
            // ok
        }
    }

}
