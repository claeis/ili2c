package ch.interlis.ili2c.Interlis24;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.ExistenceConstraint;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.PlausibilityConstraint;
import ch.interlis.ili2c.metamodel.SetConstraint;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.UniqueEl;
import ch.interlis.ili2c.metamodel.UniquenessConstraint;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
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
		td = CompilerTestHelper.getTransferDescription("test/data/ili24/constraints/Constraints.ili");
	}

	@Test
	public void constraintsCompile() {
		assertEquals(0,errs.getErrs().size());
		List<Constraint> foundConstraints = CompilerTestHelper.getInstancesOfType(td,"ModelA.TopicA.ClassA", Constraint.class);
		List<String> foundConstraintsNames = new ArrayList<String>();
		for (Constraint constraint: foundConstraints) {
			foundConstraintsNames.add(constraint.getScopedName());
		}

		List<String> expectedConstraints = Arrays.asList(
			"ModelA.TopicA.ClassA.Constraint1",
			"ModelA.TopicA.ClassA.NamedMandatory",
			"ModelA.TopicA.ClassA.Constraint3",
			"ModelA.TopicA.ClassA.NamedPlausibility",
			"ModelA.TopicA.ClassA.Constraint5",
			"ModelA.TopicA.ClassA.NamedExistenceConstraint",
			"ModelA.TopicA.ClassA.Constraint7",
			"ModelA.TopicA.ClassA.Constraint8",
			"ModelA.TopicA.ClassA.NamedUnique",
			"ModelA.TopicA.ClassA.NamedUniqueBasket",
			"ModelA.TopicA.ClassA.Constraint11",
			"ModelA.TopicA.ClassA.NamedUniqueLocal",
			"ModelA.TopicA.ClassA.NamedUniqueBasketLocal",
			"ModelA.TopicA.ClassA.Constraint14",
			"ModelA.TopicA.ClassA.NamedSetConstraint",
			"ModelA.TopicA.ClassA.Constraint16",
			"ModelA.TopicA.ClassA.NamedSetConstraintBasket",
			"ModelA.TopicA.ClassA.Constraint18",
			"ModelA.TopicA.ClassA.NamedMandatory2",
			"ModelA.TopicA.ClassA.Constraint20",
			"ModelA.TopicA.ClassA.NamedPlausibility2",
			"ModelA.TopicA.ClassA.Constraint22",
			"ModelA.TopicA.ClassA.NamedExistenceConstraint2",
			"ModelA.TopicA.ClassA.Constraint24",
			"ModelA.TopicA.ClassA.Constraint25",
			"ModelA.TopicA.ClassA.NamedUnique2",
			"ModelA.TopicA.ClassA.NamedUniqueBasket2",
			"ModelA.TopicA.ClassA.Constraint28",
			"ModelA.TopicA.ClassA.NamedUniqueLocal2",
			"ModelA.TopicA.ClassA.NamedUniqueBasketLocal2",
			"ModelA.TopicA.ClassA.Constraint31",
			"ModelA.TopicA.ClassA.NamedSetConstraint2",
			"ModelA.TopicA.ClassA.Constraint33",
			"ModelA.TopicA.ClassA.NamedSetConstraintBasket2"
		);
		assertArrayEquals(expectedConstraints.toArray(), foundConstraintsNames.toArray());
	}

	@Test
	public void mandatoryConstraint(){
		MandatoryConstraint constraint = (MandatoryConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint1");
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void mandatoryConstraintNamed(){
		MandatoryConstraint constraint = (MandatoryConstraint) td.getElement("ModelA.TopicA.ClassA.NamedMandatory");
		assertEquals("NamedMandatory", constraint.getName());
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void plausibilityConstraint(){
		PlausibilityConstraint constraint = (PlausibilityConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint3");
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void plausibilityConstraintNamed(){
		PlausibilityConstraint constraint = (PlausibilityConstraint) td.getElement("ModelA.TopicA.ClassA.NamedPlausibility");
		assertEquals("NamedPlausibility", constraint.getName());
		assertEquals(Expression.GreaterThan.class, constraint.getCondition().getClass());
	}

	@Test
	public void existenceConstraint(){
		ExistenceConstraint constraint = (ExistenceConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint5");
		assertEquals("attr1", constraint.getRestrictedAttribute().getLastPathEl().getName());
		assertEquals("attrOther", constraint.iteratorRequiredIn().next().getLastPathEl().getName());
	}

	@Test
	public void existenceConstraintNamed(){
		ExistenceConstraint constraint = (ExistenceConstraint) td.getElement("ModelA.TopicA.ClassA.NamedExistenceConstraint");
		assertEquals("NamedExistenceConstraint", constraint.getName());
		assertEquals("attr1", constraint.getRestrictedAttribute().getLastPathEl().getName());
		assertEquals("attrOther", constraint.iteratorRequiredIn().next().getLastPathEl().getName());
	}

	@Test
	public void uniquenessConstraint(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint7");
		assertFalse(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintBasket(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint8");
		assertTrue(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintNamed(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUnique");
		assertFalse(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals("NamedUnique", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintNamedBasket(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueBasket");
		assertTrue(constraint.getBasket());
		assertFalse(constraint.getLocal());
		assertEquals("NamedUniqueBasket", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint11");
		assertTrue(constraint.getLocal());
		assertFalse(constraint.getBasket());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintNamedLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueLocal");
		assertTrue(constraint.getLocal());
		assertFalse(constraint.getBasket());
		assertEquals("NamedUniqueLocal", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void uniquenessConstraintNamedBasketLocal(){
		UniquenessConstraint constraint = (UniquenessConstraint) td.getElement("ModelA.TopicA.ClassA.NamedUniqueBasketLocal");
		assertTrue(constraint.getBasket());
		assertTrue(constraint.getLocal());
		assertEquals("NamedUniqueBasketLocal", constraint.getName());
		assertEquals(UniqueEl.class, constraint.getElements().getClass());
	}

	@Test
	public void setConstraint(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint14");
		assertFalse(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void setConstraintNamed(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.NamedSetConstraint");
		assertEquals("NamedSetConstraint", constraint.getName());
		assertFalse(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void setConstraintBasket(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.Constraint16");
		assertTrue(constraint.getBasket());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

	@Test
	public void setConstraintNamedBasket(){
		SetConstraint constraint = (SetConstraint) td.getElement("ModelA.TopicA.ClassA.NamedSetConstraintBasket");
		assertTrue(constraint.getBasket());
		assertEquals("NamedSetConstraintBasket", constraint.getName());
		assertEquals(Expression.GreaterThanOrEqual.class, constraint.getCondition().getClass());
	}

}
