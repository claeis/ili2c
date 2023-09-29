package ch.interlis.ili2c.generator;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

public class ILI24GeneratorConstraintsTest {
    private StringWriter syntaxBuffer;
    private TransferDescription td;
    private Interlis2Generator generator;

    @Before
    public void setUp() {
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        td = CompilerTestHelper.getTransferDescription("test/data/ili24/constraints/constraints.ili");

        Assert.assertNotNull(td);
        Assert.assertEquals(0, errs.getErrs().size());

        syntaxBuffer = new java.io.StringWriter();
        generator = Interlis2Generator.generateElements24(syntaxBuffer, td);
    }

    @Test
    public void mandatoryConstraint() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.Constraint1");
        generator.printConstraint(constraint);
        Assert.assertEquals("MANDATORY CONSTRAINT attr1 > 1;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void mandatoryConstraintNamed() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.NamedMandatory");
        generator.printConstraint(constraint);
        Assert.assertEquals("MANDATORY CONSTRAINT NamedMandatory: attr1 > 1;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void plausabilityConstraint() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.Constraint3");
        generator.printConstraint(constraint);
        Assert.assertEquals("CONSTRAINT >= 80.0% attr1 > 2;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void plausabilityConstraintNamed() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.NamedPlausibility");
        generator.printConstraint(constraint);
        Assert.assertEquals("CONSTRAINT NamedPlausibility: <= 80.0% attr1 > 2;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void uniqueConstraint() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.Constraint7");
        generator.printConstraint(constraint);
        Assert.assertEquals("UNIQUE attr1;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void uniqueConstraintNamedBasketLocal() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueBasketLocal");
        generator.printConstraint(constraint);
        Assert.assertEquals("UNIQUE (BASKET) NamedUniqueBasketLocal: (LOCAL) attr2: structAttr;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void setConstraint() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.Constraint14");
        generator.printConstraint(constraint);
        Assert.assertEquals("SET CONSTRAINT\r\n  INTERLIS.objectCount (ALL) >= 0;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void setConstraintNamedBasket() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.NamedSetConstraintBasket");
        generator.printConstraint(constraint);
        Assert.assertEquals("SET CONSTRAINT (BASKET) NamedSetConstraintBasket:\r\n  INTERLIS.objectCount (ALL) >= 0;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void existanceConstraint() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.Constraint5");
        generator.printConstraint(constraint);
        Assert.assertEquals("EXISTENCE CONSTRAINT attr1 REQUIRED IN ModelA.TopicA.ClassOther:attrOther;\r\n", syntaxBuffer.toString());
    }

    @Test
    public void existanceConstraintNamed() {
        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.NamedExistenceConstraint");
        generator.printConstraint(constraint);
        Assert.assertEquals("EXISTENCE CONSTRAINT NamedExistenceConstraint: attr1 REQUIRED IN ModelA.TopicA.ClassOther:attrOther;\r\n", syntaxBuffer.toString());
    }
}
