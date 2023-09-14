package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.AbstractLeafElement;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Constraints24Test {

	LogCollector errs = null;
	TransferDescription td = null;

	@Before
	public void setUp(){
		errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		td = CompilerTestHelper.GetTransferDescription("test/data/ili24.constraints/Constraints.ili");
	}

	@Test
	public void ili24_Constraints_Compile() throws Exception {
		assertEquals(0,errs.getErrs().size());
		List<Constraint> foundConstraints = CompilerTestHelper.GetInstancesOfType(td,"ModelA.TopicA.ClassA", Constraint.class);
		List<String> expectedConstraints = Arrays.asList(
			"ModelA.TopicA.ClassA.Constraint1",
			"ModelA.TopicA.ClassA.NamedMandatory",
			"ModelA.TopicA.ClassA.Constraint3",
			"ModelA.TopicA.ClassA.NamedPlausibility",
			"ModelA.TopicA.ClassA.Constraint5",
			"ModelA.TopicA.ClassA.Constraint6",
			"ModelA.TopicA.ClassA.NamedUnique",
			"ModelA.TopicA.ClassA.NamedUniqueBasket",
			"ModelA.TopicA.ClassA.Constraint9",
			"ModelA.TopicA.ClassA.NamedUniqueLocal",
			"ModelA.TopicA.ClassA.NamedUniqueBasketLocal",
			"ModelA.TopicA.ClassA.Constraint12",
			"ModelA.TopicA.ClassA.NamedSetConstraint",
			"ModelA.TopicA.ClassA.Constraint14",
			"ModelA.TopicA.ClassA.NamedSetConstraintBasket",
			"ModelA.TopicA.ClassA.Constraint16",
			"ModelA.TopicA.ClassA.NamedMandatory2",
			"ModelA.TopicA.ClassA.Constraint18",
			"ModelA.TopicA.ClassA.NamedPlausibility2",
			"ModelA.TopicA.ClassA.Constraint20",
			"ModelA.TopicA.ClassA.Constraint21",
			"ModelA.TopicA.ClassA.NamedUnique2",
			"ModelA.TopicA.ClassA.NamedUniqueBasket2",
			"ModelA.TopicA.ClassA.Constraint24",
			"ModelA.TopicA.ClassA.NamedUniqueLocal2",
			"ModelA.TopicA.ClassA.NamedUniqueBasketLocal2",
			"ModelA.TopicA.ClassA.Constraint27",
			"ModelA.TopicA.ClassA.NamedSetConstraint2",
			"ModelA.TopicA.ClassA.Constraint29",
			"ModelA.TopicA.ClassA.NamedSetConstraintBasket2"
		);
		assertEquals(expectedConstraints.size(), foundConstraints.size());
		for (Constraint constraint : foundConstraints) {
			assertTrue(expectedConstraints.contains(constraint.getScopedName()));
		}
	}
}
