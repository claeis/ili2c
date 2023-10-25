package ch.interlis.ili2c.generator;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.instanceOf;

public class ILI24GeneratorConstraintsTest {

    private static final String ILI_CONSTRAINTS = "test/data/ili24/constraints/Constraints.ili";
    private static final String ILI_DOMAINS = "test/data/ili24/domain/domain.ili";

    private void assertInterlis2GeneratorPrintConstraint(String interlisFilePath, String constraintScopedName, String ExpectedOutput) {
        // compile model
        LogCollector errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        TransferDescription td = CompilerTestHelper.getTransferDescription(interlisFilePath);
        Assert.assertNotNull(td);
        Assert.assertEquals(0, errs.getErrs().size());

        // get constraint, used as input for generator
        Element element = td.getElement(constraintScopedName);
        Assert.assertThat(element, instanceOf(Constraint.class));

        // setup generator
        StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator generator = Interlis2Generator.generateElements24(syntaxBuffer, td);
        generator.printConstraint((Constraint) element);

        // assert result
        Assert.assertEquals(ExpectedOutput, syntaxBuffer.toString());
    }

    @Test
    public void mandatoryConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.Constraint1",
                "MANDATORY CONSTRAINT attr1 > 1;\r\n");
    }

    @Test
    public void mandatoryConstraintNamed() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedMandatory",
                "MANDATORY CONSTRAINT NamedMandatory: attr1 > 1;\r\n");
    }

    @Test
    public void plausabilityConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.Constraint3",
                "CONSTRAINT >= 80.0% attr1 > 2;\r\n");
    }

    @Test
    public void plausabilityConstraintNamed() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedPlausibility",
                "CONSTRAINT NamedPlausibility: <= 80.0% attr1 > 2;\r\n");
    }

    @Test
    public void uniqueConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.Constraint7",
                "UNIQUE attr1;\r\n");
    }

    @Test
    public void uniqueConstraintNamedBasket() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedUniqueBasket",
                "UNIQUE (BASKET) NamedUniqueBasket: attr1;\r\n");
    }

    @Test
    public void uniqueConstraintNamedLocal() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedUniqueLocal",
                "UNIQUE NamedUniqueLocal: (LOCAL) attr2: structAttr;\r\n");
    }

    @Test
    public void setConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.Constraint13",
                "SET CONSTRAINT\r\n  INTERLIS.objectCount (ALL) >= 0;\r\n");
    }

    @Test
    public void setConstraintNamedBasket() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedSetConstraintBasket",
                "SET CONSTRAINT (BASKET) NamedSetConstraintBasket:\r\n  INTERLIS.objectCount (ALL) >= 0;\r\n");
    }

    @Test
    public void existanceConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.Constraint5",
                "EXISTENCE CONSTRAINT attr1 REQUIRED IN ModelA.TopicA.ClassOther:attrOther;\r\n");
    }

    @Test
    public void existanceConstraintNamed() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_CONSTRAINTS,
                "ModelA.TopicA.ClassA.NamedExistenceConstraint",
                "EXISTENCE CONSTRAINT NamedExistenceConstraint: attr1 REQUIRED IN ModelA.TopicA.ClassOther:attrOther;\r\n");
    }

    @Test
    public void domainConstraint() {
        assertInterlis2GeneratorPrintConstraint(
                ILI_DOMAINS,
                "ModelA.TopicA.DomainTextRestricted.Values",
                "Values: THIS == \"SomeConstant\" OR THIS == \"OtherConstant\"");
    }
}
