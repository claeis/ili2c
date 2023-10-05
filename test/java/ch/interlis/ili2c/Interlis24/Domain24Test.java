package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.DomainConstraint;
import ch.interlis.ili2c.metamodel.Evaluable;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.FunctionCall;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.ValueRefThis;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class Domain24Test {
    LogCollector errs = null;
    TransferDescription td = null;

    @Before
    public void setUp() {
        errs = new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        td = CompilerTestHelper.getTransferDescription("test/data/ili24/domain/domain.ili");
    }

    @Test
    public void compile_Ok() {
        assertEquals(0, errs.getErrs().size());
        assertNotNull(td);
    }

    @Test
    public void compile_Fail() {
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry("test/data/ili24/domain/domain_fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            td = null;
            td = Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
            // Compile should fail
        }
        assertEquals(4, errs.getErrs().size());
        assertNull(td);

        List<String> errorMessages = new ArrayList<String>();
        for (LogEvent event : errs.getErrs()) {
            errorMessages.add(((CompilerLogEvent) event).getRawEventMsg());
        }

        assertEquals("incompatible datatypes", errorMessages.get(0));
        assertEquals("Name AttrText is not applicable to CLASS INTERLIS.ANYCLASS.", errorMessages.get(1));
        assertEquals("Name AttrText is not applicable to CLASS ModelA.TopicA.ClassC.", errorMessages.get(2));
        assertEquals("Referenced element \"STRUCTURE ModelA.TopicA.StructA\" should be a CLASS.", errorMessages.get(3));
    }

    @Test
    public void withoutConstraints() {
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainText");
        assertEquals(0, domain.size());
        assertEquals("TEXT*30", domain.getType().toString());
    }

    @Test
    public void constantRestriction() {
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainTextRestricted");
        assertEquals(1, domain.size());
        assertEquals("TEXT*30", domain.getType().toString());

        DomainConstraint constraint = domain.iterator().next();
        assertEquals("Values", constraint.getName());
        Evaluable condition = constraint.getCondition();
        assertTrue(condition instanceof Expression.Disjunction);
        Expression.Disjunction disjunction = (Expression.Disjunction) condition;

        for (int i = 0; i < 2; i++) {
            assertTrue(disjunction.getDisjoined()[i] instanceof Expression.Equality);
            Expression.Equality equality = (Expression.Equality) disjunction.getDisjoined()[i];
            assertTrue(equality.getLeft() instanceof ValueRefThis);
            assertTrue(equality.getRight() instanceof Constant);
        }
    }

    @Test
    public void constantConstraint() {
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainConstraintConstant");
        assertEquals(1, domain.size());

        DomainConstraint constraint = domain.iterator().next();
        assertEquals("Constant", constraint.getName());
        Evaluable condition = constraint.getCondition();
        assertTrue(condition instanceof Expression.GreaterThan);

        Expression.GreaterThan greaterThan = (Expression.GreaterThan) condition;
        assertTrue(greaterThan.getLeft() instanceof FunctionCall);
        assertTrue(greaterThan.getRight() instanceof Constant);

        FunctionCall functionCall = (FunctionCall) greaterThan.getLeft();
        assertTrue(functionCall.getArguments()[0] instanceof Constant);
    }

    @Test
    public void thisConstraint() {
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainConstraintThis");
        assertEquals(1, domain.size());

        DomainConstraint constraint = domain.iterator().next();
        assertEquals("Length", constraint.getName());
        Evaluable condition = constraint.getCondition();
        assertTrue(condition instanceof Expression.GreaterThan);

        Expression.GreaterThan greaterThan = (Expression.GreaterThan) condition;
        assertTrue(greaterThan.getLeft() instanceof FunctionCall);
        assertTrue(greaterThan.getRight() instanceof Constant);

        FunctionCall functionCall = (FunctionCall) greaterThan.getLeft();
        assertTrue(functionCall.getArguments()[0] instanceof ValueRefThis);
    }

    @Test
    public void multipleConstraints() {
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainMultiConstraints");
        assertEquals(2, domain.size());

        Iterator<DomainConstraint> constraints = domain.iterator();
        DomainConstraint constraintMin = constraints.next();
        DomainConstraint constraintMax = constraints.next();

        assertEquals("Min", constraintMin.getName());
        assertEquals("Max100", constraintMax.getName());

        assertTrue(constraintMin.getCondition() instanceof Expression.GreaterThan);
        assertTrue(constraintMax.getCondition() instanceof Expression.LessThanOrEqual);
    }

    @Test
    public void extendedConstraints() {
        Domain domainParent = (Domain) td.getElement("ModelA.TopicA.DomainMultiConstraints");
        Domain domain = (Domain) td.getElement("ModelA.TopicA.DomainExtends");
        assertEquals(1, domain.size());
        assertSame(domainParent, domain.getExtending());
    }
}
