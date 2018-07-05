package ch.interlis.ili2c.metamodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;

public class ViewableGetAttributesAndRoles2Test {
    private static final String ILI_FILE = "test/data/metamodel/ViewableGetAttributesAndRoles2.ili";
    @Test
    public void baseClass() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));
        assertNotNull(td);
        Viewable topicAclassB=(Viewable) td.getElement("ViewableGetAttributesAndRoles2.topicA.ClassB");
        Iterator propIt=topicAclassB.getAttributesAndRoles2();
        ViewableTransferElement prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicA.ClassB.attr_b1",((Element) prop.obj).getScopedName());
        prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicA.ClassB.attr_b2",((Element) prop.obj).getScopedName());
        prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicA.b2c.c",((Element) prop.obj).getScopedName());     
        assertFalse(propIt.hasNext());
    }
    @Test
    public void extendedClass() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));
        assertNotNull(td);
        Viewable topicBclassB=(Viewable) td.getElement("ViewableGetAttributesAndRoles2.topicB.ClassB");
        Iterator propIt=topicBclassB.getAttributesAndRoles2();
        ViewableTransferElement prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicA.ClassB.attr_b1",((Element) prop.obj).getScopedName());
        prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicB.ClassB.attr_b2",((Element) prop.obj).getScopedName());
        prop = (ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicB.b2c.c", ((Element) prop.obj).getScopedName());
        prop = (ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicB.ClassB.attr_b3", ((Element) prop.obj).getScopedName());
        assertFalse(propIt.hasNext());
        AssociationDef topicBb2c=(AssociationDef) td.getElement("ViewableGetAttributesAndRoles2.topicB.b2c");
        assertTrue(topicBb2c.isLightweight());
    }
    @Test
    public void extendsClass() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));
        assertNotNull(td);
        Viewable topicBclassB=(Viewable) td.getElement("ViewableGetAttributesAndRoles2.topicC.ClassBp");
        Iterator propIt=topicBclassB.getAttributesAndRoles2();
        ViewableTransferElement prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicC.ClassB.attr_b1",((Element) prop.obj).getScopedName());
        prop=(ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicC.ClassBp.attr_b2",((Element) prop.obj).getScopedName());
        prop = (ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicC.b2cp.c", ((Element) prop.obj).getScopedName());
        prop = (ViewableTransferElement) propIt.next();
        assertEquals("ViewableGetAttributesAndRoles2.topicC.ClassBp.attr_b3", ((Element) prop.obj).getScopedName());
        assertFalse(propIt.hasNext());
    }
}
