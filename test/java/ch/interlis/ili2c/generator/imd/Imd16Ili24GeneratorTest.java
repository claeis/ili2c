package ch.interlis.ili2c.generator.imd;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class Imd16Ili24GeneratorTest {

    private static final String OUT_FILE = "Simple24-out.imd";
    private static final String SIMPLE24_ILI = "test/data/imdgenerator/Simple24.ili";
    private static final String ILIS_META16_ILI = "standard/IlisMeta16.ili";
    private static final HashMap<String, List<IomObject>> metaObjects = new HashMap<String, List<IomObject>>();

    private static final String MODEL_SIMPLE24 = "MODEL.Simple24";
    private static final String MODEL_DEFERREDGENERICS24 = "MODEL.DeferredGeneric24";

    @BeforeClass
    public static void setUp() throws Exception {
        //EhiLogger.getInstance().setTraceFilter(false);
        // compile simple24 model to imd16
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(SIMPLE24_ILI, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        ili2cConfig.setOutputFile(OUT_FILE);
        ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
        TransferDescription simple24iliTd = ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
        assertNotNull(simple24iliTd);

        // compile ilisMeta16 model
        TransferDescription ilisMeta16TransferDescription = CompilerTestHelper.getTransferDescription(ILIS_META16_ILI);
        assertNotNull(ilisMeta16TransferDescription);

        // read imd16 file and set transfer description
        Xtf24Reader reader = new Xtf24Reader(new File(OUT_FILE));
        reader.setModel(ilisMeta16TransferDescription);

        // gather all objects from imd16 file
        IoxEvent event;
        String currentBasketKey = null;
        do {
            event = reader.read();
            if (event instanceof StartBasketEvent) {
                currentBasketKey = ((StartBasketEvent) event).getBid();
                metaObjects.put(currentBasketKey, new ArrayList<IomObject>());
            } else if (event instanceof EndBasketEvent) {
                currentBasketKey = null;
            } else if (event instanceof ObjectEvent) {
                IomObject iomObj = ((ObjectEvent) event).getIomObject();
                if (currentBasketKey == null) {
                    fail("Object without basket: " + iomObj.getobjecttag());
                }
                metaObjects.get(currentBasketKey).add(iomObj);
            }
        } while (!(event instanceof EndTransferEvent));
    }

    private List<IomObject> getMetaObjectsWithTag(String model, String key) {
        List<IomObject> result = new ArrayList<IomObject>();
        for (IomObject iomObj : metaObjects.get(model)) {
            if (key.equals(iomObj.getobjecttag())) {
                result.add(iomObj);
            }
        }
        return result;
    }

    private IomObject getMetaObjectByTid(String key) {
        List<IomObject> result = new ArrayList<IomObject>();
        for (List<IomObject> modelObjects : metaObjects.values()) {
            for (IomObject iomObj : modelObjects) {
                if (key.equals(iomObj.getobjectoid())) {
                    result.add(iomObj);
                }
            }
        }
        switch (result.size()) {
            case 0:
                return null;
            case 1:
                return result.get(0);
            default:
                fail("Multiple objects with TID " + key + " found.");
                return null;
        }
    }

    @Test
    public void containsClass() {
        assertNotNull(getMetaObjectByTid("Simple24.TestA.ClassA1"));
    }

    @Test
    public void genericDomain() {
        IomObject iomObject = getMetaObjectByTid("Simple24.GenericDomain");
        assertNotNull(iomObject);
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("true", generic);
    }

    @Test
    public void nonGenericDomain() {
        IomObject iomObject = getMetaObjectByTid("Simple24.Coord");
        assertNotNull(iomObject);
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("false", generic);
    }

    @Test
    public void textType() {
        IomObject iomObject = getMetaObjectByTid("Simple24.TestA.ClassA1.attr1.TYPE");
        assertNotNull(iomObject);
        String generic = iomObject.getattrvalue("Generic");
        assertEquals("false", generic);
    }

    @Test
    public void context() {
        IomObject contextObject = getMetaObjectByTid("Simple24.default");
        assertNotNull(contextObject);
        String name = contextObject.getattrvalue("Name");
        assertEquals("default", name);
    }

    @Test
    public void contextGenericDef() {
        IomObject genericDef = getMetaObjectByTid("Simple24.default.GenericDomain");
        assertNotNull(genericDef);
        IomObject context = genericDef.getattrobj("Context", 0);
        IomObject genericDomain = genericDef.getattrobj("GenericDomain", 0);

        assertEquals("Simple24.default", context.getobjectrefoid());
        assertEquals("Simple24.GenericDomain", genericDomain.getobjectrefoid());
    }

    @Test
    public void topicNoDeferredGenericDef() {
        assertNull(getMetaObjectByTid("Simple24.TestA.BASKET.GenericDomain"));
    }

    @Test
    public void topicDeferredGenericDef() {
        IomObject deferredGenericDef = getMetaObjectByTid("DeferredGeneric24.TestA.BASKET.GenericDomain");
        assertNotNull(deferredGenericDef);
        IomObject context = deferredGenericDef.getattrobj("Context", 0);
        IomObject genericDomain = deferredGenericDef.getattrobj("GenericDomain", 0);

        assertEquals("DeferredGeneric24.TestA.BASKET", context.getobjectrefoid());
        assertEquals("Simple24.GenericDomain", genericDomain.getobjectrefoid());
    }

    @Test
    public void concreteForGeneric() {
        String[] expectedConcretes = {"Coord", "Coord2"};

        List<IomObject> concreteForGenerics = getMetaObjectsWithTag(MODEL_SIMPLE24, "IlisMeta16.ModelData.ConcreteForGeneric");
        assertEquals(expectedConcretes.length, concreteForGenerics.size());

        for (int i = 0; i < expectedConcretes.length; i++) {
            IomObject concreteForGeneric = concreteForGenerics.get(i);
            IomObject genericDef = concreteForGeneric.getattrobj("GenericDef", 0);
            IomObject concreteDomain = concreteForGeneric.getattrobj("ConcreteDomain", 0);

            assertEquals("Simple24.default.GenericDomain", genericDef.getobjectrefoid());
            assertEquals("Simple24." + expectedConcretes[i], concreteDomain.getobjectrefoid());
        }
    }

    @Test
    public void topicConcreteForGeneric() {
        String[] expectedConcretes = {"Coord", "Coord2"};

        List<IomObject> concreteForGenerics = getMetaObjectsWithTag(MODEL_DEFERREDGENERICS24, "IlisMeta16.ModelData.ConcreteForGeneric");
        assertEquals(expectedConcretes.length, concreteForGenerics.size());

        for (int i = 0; i < expectedConcretes.length; i++) {
            IomObject concreteForGeneric = concreteForGenerics.get(i);
            IomObject genericDef = concreteForGeneric.getattrobj("GenericDef", 0);
            IomObject concreteDomain = concreteForGeneric.getattrobj("ConcreteDomain", 0);

            assertEquals("DeferredGeneric24.TestA.BASKET.GenericDomain", genericDef.getobjectrefoid());
            assertEquals("Simple24." + expectedConcretes[i], concreteDomain.getobjectrefoid());
        }
    }

    @Test
    public void modelAttributes24() {
        IomObject simpleModel = getMetaObjectByTid("Simple24");
        assertNotNull(simpleModel);

        IomObject attributesModel = getMetaObjectByTid("ModelAttributes24");
        assertNotNull(attributesModel);

        assertNotEquals("true", simpleModel.getattrvalue("NoIncrementalTransfer"));
        assertEquals("true", attributesModel.getattrvalue("NoIncrementalTransfer"));

        assertNull(simpleModel.getattrvalue("CharSetIANAName"));
        assertEquals("UTF-8", attributesModel.getattrvalue("CharSetIANAName"));

        assertNull(simpleModel.getattrvalue("xmlns"));
        assertEquals("http://www.interlis.ch/custom/namespace", attributesModel.getattrvalue("xmlns"));
    }

    @Test
    public void multiCoordType() {
        IomObject coordType = getMetaObjectByTid("Simple24.Coord");
        assertNotNull(coordType);
        assertEquals("false", coordType.getattrvalue("Multi"));

        IomObject multiCoordType = getMetaObjectByTid("Simple24.MultiCoord");
        assertNotNull(multiCoordType);
        assertEquals("true", multiCoordType.getattrvalue("Multi"));
    }

    @Test
    public void multiLineType() {
        IomObject lineType = getMetaObjectByTid("Simple24.Line");
        assertNotNull(lineType);
        assertEquals("false", lineType.getattrvalue("Multi"));

        IomObject multiLineType = getMetaObjectByTid("Simple24.MultiLine");
        assertNotNull(multiLineType);
        assertEquals("true", multiLineType.getattrvalue("Multi"));
    }

    @Test
    public void multiSurfaceType() {
        IomObject surfaceType = getMetaObjectByTid("Simple24.Surface");
        assertNotNull(surfaceType);
        assertEquals("false", surfaceType.getattrvalue("Multi"));

        IomObject multiSurfaceType = getMetaObjectByTid("Simple24.MultiSurface");
        assertNotNull(multiSurfaceType);
        assertEquals("true", multiSurfaceType.getattrvalue("Multi"));
    }

    @Test
    public void implicationExpression() {
        IomObject constraint = getMetaObjectByTid("Simple24.TestA.ClassA1.implicationConstraint");
        assertNotNull(constraint);

        IomObject expression = constraint.getattrobj("LogicalExpression", 0);
        assertNotNull(expression);
        assertEquals("Implication", expression.getattrvalue("Operation"));
    }

    @Test
    public void multiplyAndDivideExpressions() {
        IomObject constraint = getMetaObjectByTid("Simple24.TestA.ClassA1.multDivConstraint");
        assertNotNull(constraint);
        IomObject logicalExpression = constraint.getattrobj("LogicalExpression", 0);
        assertNotNull(logicalExpression);

        IomObject subExpression1 = logicalExpression.getattrobj("SubExpressions", 0);
        assertNotNull(subExpression1);
        assertEquals("Nested", subExpression1.getattrvalue("Operation"));

        IomObject multiplyExpression = subExpression1.getattrobj("SubExpression", 0);
        assertNotNull(multiplyExpression);
        assertEquals("Mult", multiplyExpression.getattrvalue("Operation"));

        IomObject subExpression2 = logicalExpression.getattrobj("SubExpressions", 1);
        assertNotNull(subExpression2);
        assertEquals("Nested", subExpression2.getattrvalue("Operation"));
        
        IomObject divideExpression = subExpression2.getattrobj("SubExpression", 0);
        assertNotNull(divideExpression);
        assertEquals("Div", divideExpression.getattrvalue("Operation"));
    }

    @Test
    public void addAndSubtractExpressions() {
        IomObject constraint = getMetaObjectByTid("Simple24.TestA.ClassA1.addSubConstraint");
        assertNotNull(constraint);
        IomObject logicalExpression = constraint.getattrobj("LogicalExpression", 0);
        assertNotNull(logicalExpression);

        IomObject subExpression1 = logicalExpression.getattrobj("SubExpressions", 0);
        assertNotNull(subExpression1);
        assertEquals("Nested", subExpression1.getattrvalue("Operation"));

        IomObject addExpression = subExpression1.getattrobj("SubExpression", 0);
        assertNotNull(addExpression);
        assertEquals("Add", addExpression.getattrvalue("Operation"));

        IomObject subExpression2 = logicalExpression.getattrobj("SubExpressions", 1);
        assertNotNull(subExpression2);
        assertEquals("Nested", subExpression2.getattrvalue("Operation"));
        
        IomObject subtractExpression = subExpression2.getattrobj("SubExpression", 0);
        assertNotNull(subtractExpression);
        assertEquals("Sub", subtractExpression.getattrvalue("Operation"));
    }

    @Test
    public void mandatoryConstraint() {
        IomObject mandatoryConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedMandatory");
        assertNotNull(mandatoryConstraint);
        assertEquals("NamedMandatory", mandatoryConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", mandatoryConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("MandC", mandatoryConstraint.getattrvalue("Kind"));
        assertNotNull(mandatoryConstraint.getattrobj("LogicalExpression", 0));
    }

    @Test
    public void plausibilityConstraint() {
        IomObject plausibilityConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedPlausibility");
        assertNotNull(plausibilityConstraint);
        assertEquals("NamedPlausibility", plausibilityConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", plausibilityConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("HighPercC", plausibilityConstraint.getattrvalue("Kind"));
        assertEquals("80.0", plausibilityConstraint.getattrvalue("Percentage"));
        assertNotNull(plausibilityConstraint.getattrobj("LogicalExpression", 0));
    }

    @Test
    public void existenceConstraint() {
        IomObject existenceConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedExistenceConstraint");
        assertNotNull(existenceConstraint);
        assertEquals("NamedExistenceConstraint", existenceConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", existenceConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertNotNull(existenceConstraint.getattrobj("Attr", 0));
        assertEquals("Simple24.TestA.ClassA1.attr1", existenceConstraint.getattrobj("Attr", 0).getattrobj("PathEls", 0).getattrobj("Ref", 0).getobjectrefoid());
        assertEquals(1, existenceConstraint.getattrvaluecount("RequiredIn"));

        IomObject existenceDef = existenceConstraint.getattrobj("RequiredIn", 0);
        assertEquals("Simple24.TestA.ClassOther1", existenceDef.getattrobj("ExistsIn", 0).getobjectrefoid());
        assertNotNull(existenceDef.getattrobj("Attr", 0));
        assertEquals("Simple24.TestA.ClassOther1.attrOther", existenceDef.getattrobj("Attr", 0).getattrobj("PathEls", 0).getattrobj("Ref", 0).getobjectrefoid());
    }

    @Test
    public void existenceConstraintOutsideClass() {
        IomObject existenceConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedExistenceConstraintOutsideClass");
        assertNotNull(existenceConstraint);
        assertEquals("NamedExistenceConstraintOutsideClass", existenceConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", existenceConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertNotNull(existenceConstraint.getattrobj("Attr", 0));
        assertEquals("Simple24.TestA.ClassA1.attr1", existenceConstraint.getattrobj("Attr", 0).getattrobj("PathEls", 0).getattrobj("Ref", 0).getobjectrefoid());
        assertEquals(2, existenceConstraint.getattrvaluecount("RequiredIn"));

        IomObject existenceDef = existenceConstraint.getattrobj("RequiredIn", 0);
        assertEquals("Simple24.TestA.ClassOther1", existenceDef.getattrobj("ExistsIn", 0).getobjectrefoid());
        assertNotNull(existenceDef.getattrobj("Attr", 0));
        assertEquals("Simple24.TestA.ClassOther1.attrOther", existenceDef.getattrobj("Attr", 0).getattrobj("PathEls", 0).getattrobj("Ref", 0).getobjectrefoid());

        existenceDef = existenceConstraint.getattrobj("RequiredIn", 1);
        assertEquals("Simple24.TestA.ClassOther2", existenceDef.getattrobj("ExistsIn", 0).getobjectrefoid());
        assertNotNull(existenceDef.getattrobj("Attr", 0));
        assertEquals("Simple24.TestA.ClassOther2.attrOther", existenceDef.getattrobj("Attr", 0).getattrobj("PathEls", 0).getattrobj("Ref", 0).getobjectrefoid());
    }

    @Test
    public void uniqueConstraintGlobal() {
        IomObject uniqueConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedUnique");
        assertNotNull(uniqueConstraint);
        assertEquals("NamedUnique", uniqueConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", uniqueConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("GlobalU", uniqueConstraint.getattrvalue("Kind"));
        assertNotNull(uniqueConstraint.getattrobj("UniqueDef", 0));
    }

    @Test
    public void uniqueConstraintBasket() {
        IomObject uniqueConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedUniqueBasket");
        assertNotNull(uniqueConstraint);
        assertEquals("NamedUniqueBasket", uniqueConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", uniqueConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("BasketU", uniqueConstraint.getattrvalue("Kind"));
        assertNotNull(uniqueConstraint.getattrobj("UniqueDef", 0));
    }

    @Test
    public void uniqueConstraintLocal() {
        IomObject uniqueConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedUniqueLocal");
        assertNotNull(uniqueConstraint);
        assertEquals("NamedUniqueLocal", uniqueConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", uniqueConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("LocalU", uniqueConstraint.getattrvalue("Kind"));
        assertNotNull(uniqueConstraint.getattrobj("UniqueDef", 0));
    }

    @Test
    public void setConstraintGlobal() {
        IomObject setConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedSetConstraint");
        assertNotNull(setConstraint);
        assertEquals("NamedSetConstraint", setConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", setConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("false", setConstraint.getattrvalue("PerBasket"));
        assertNotNull(setConstraint.getattrobj("Constraint", 0));
    }

    @Test
    public void setConstraintBasket() {
        IomObject setConstraint = getMetaObjectByTid("Simple24.TestA.ClassA1.NamedSetConstraintBasket");
        assertNotNull(setConstraint);
        assertEquals("NamedSetConstraintBasket", setConstraint.getattrvalue("Name"));
        assertEquals("Simple24.TestA.ClassA1", setConstraint.getattrobj("ToClass", 0).getobjectrefoid());
        assertEquals("true", setConstraint.getattrvalue("PerBasket"));
        assertNotNull(setConstraint.getattrobj("Constraint", 0));
    }

    @Test
    public void domainConstraint() {
        IomObject domainConstraint = getMetaObjectByTid("Simple24.DomainConstraintThis.Length");
        assertNotNull(domainConstraint);
        assertEquals("Length", domainConstraint.getattrvalue("Name"));
        assertEquals("Simple24.DomainConstraintThis", domainConstraint.getattrobj("ToDomain", 0).getobjectrefoid());
        assertNotNull(domainConstraint.getattrobj("LogicalExpression", 0));
    }

    @Test
    public void listOfPrimitiveType() {
        IomObject attribute = getMetaObjectByTid("Simple24.TestA.ClassA1.listOfPrimitive");
        IomObject multiValue = getMetaObjectByTid("Simple24.TestA.ClassA1.listOfPrimitive.MVT");
        IomObject type = getMetaObjectByTid("Simple24.TestA.ClassA1.listOfPrimitive.TYPE");
        assertNotNull(attribute);
        assertNotNull(multiValue);
        assertNotNull(type);

        assertEquals("Simple24.TestA.ClassA1.listOfPrimitive.MVT", attribute.getattrobj("Type", 0).getobjectrefoid());
        assertEquals("listOfPrimitive", attribute.getattrvalue("Name"));

        assertEquals("Simple24.TestA.ClassA1.listOfPrimitive.TYPE", multiValue.getattrobj("BaseType", 0).getobjectrefoid());
        assertEquals("Simple24.TestA.ClassA1.listOfPrimitive", multiValue.getattrobj("LTParent", 0).getobjectrefoid());
        assertEquals("MVT", multiValue.getattrvalue("Name"));
        assertEquals("true", multiValue.getattrvalue("Ordered"));
        assertEquals("0", multiValue.getattrobj("Multiplicity", 0).getattrvalue("Min"));
    }

    @Test
    public void bagOfPrimitiveType() {
        IomObject attribute = getMetaObjectByTid("Simple24.TestA.ClassA1.bagOfPrimitive");
        IomObject multiValue = getMetaObjectByTid("Simple24.TestA.ClassA1.bagOfPrimitive.MVT");
        IomObject type = getMetaObjectByTid("Simple24.Coord");
        assertNotNull(attribute);
        assertNotNull(multiValue);
        assertNotNull(type);

        assertEquals("Simple24.TestA.ClassA1.bagOfPrimitive.MVT", attribute.getattrobj("Type", 0).getobjectrefoid());
        assertEquals("bagOfPrimitive", attribute.getattrvalue("Name"));

        assertEquals("Simple24.Coord", multiValue.getattrobj("BaseType", 0).getobjectrefoid());
        assertEquals("Simple24.TestA.ClassA1.bagOfPrimitive", multiValue.getattrobj("LTParent", 0).getobjectrefoid());
        assertEquals("MVT", multiValue.getattrvalue("Name"));
        assertEquals("false", multiValue.getattrvalue("Ordered"));
        assertEquals("0", multiValue.getattrobj("Multiplicity", 0).getattrvalue("Min"));
    }
}
