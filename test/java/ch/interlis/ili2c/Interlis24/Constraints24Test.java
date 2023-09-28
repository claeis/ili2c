package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.PlausibilityConstraint;
import ch.interlis.ili2c.metamodel.SetConstraint;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniqueEl;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
	public void Constraints_Compile() throws Exception {
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

	@Test
	public void MandatoryConstraint(){
		MandatoryConstraint constraint = (MandatoryConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint1");
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void MandatoryConstraintNamed(){
		MandatoryConstraint constraint = (MandatoryConstraint) td.getElement("ModelA.TopicA.ClassA.NamedMandatory");
		assertEquals("NamedMandatory", constraint.getName());
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void PlausibilityConstraint(){
		PlausibilityConstraint constraint = (PlausibilityConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint3");
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void PlausibilityConstraintNamed(){
		PlausibilityConstraint constraint = (PlausibilityConstraint) td.getElement("ModelA.TopicA.ClassA.NamedPlausibility");
		assertEquals("NamedPlausibility", constraint.getName());
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void UniquenessConstraint(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint5");
		assertFalse(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintBasket(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint6");
		assertTrue(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintNamed(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUnique");
		assertFalse(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals("NamedUnique", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintNamedBasket(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueBasket");
		assertTrue(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals("NamedUniqueBasket", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint9");
		assertTrue(constraint.getLocal());
		assertFalse(constraint.getBasket());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintNamedLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueLocal");
		assertTrue(constraint.getLocal());
		assertFalse(constraint.getBasket());
		assertEquals("NamedUniqueLocal", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void UniquenessConstraintNamedBasketLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueBasketLocal");
		assertTrue(constraint.getBasket());
		assertTrue(constraint.getLocal());
		assertEquals("NamedUniqueBasketLocal", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void SetConstraint(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint12");
		assertFalse(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void SetConstraintNamed(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.NamedSetConstraint");
		assertFalse(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void SetConstraintBasket(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint14");
		assertTrue(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void SetConstraintNamedBasket(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.NamedSetConstraintBasket");
		assertTrue(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

}