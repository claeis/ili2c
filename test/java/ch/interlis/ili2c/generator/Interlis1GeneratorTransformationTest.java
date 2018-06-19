package ch.interlis.ili2c.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import ch.interlis.ili2c.generator.TransformationParameter;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;
import ch.interlis.ili2c.metamodel.AbstractCoordType;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.CoordType;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.LineType;
import ch.interlis.ili2c.metamodel.LocalAttribute;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;

public class Interlis1GeneratorTransformationTest {

	private static final String NEW_MODEL_NAME = "LineType_200";
	private static final String OUTPUT_ILI_FILE = "out.ili";
	private static final String ILI_FILE = "test\\data\\interlis1generator\\TrafoParam1.ili";

	@Test
	public void transforamtionIli1c() throws IOException {
		// Read ili
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));

		// setup transformation parameters
		TransformationParameter trafoParam = new TransformationParameter();
		trafoParam.setDiff_x(10.0);
		trafoParam.setDiff_y(20.0);
		trafoParam.setFactor_x(2.0);
		trafoParam.setFactor_y(3.0);
		trafoParam.setEpsgCode(200);
		trafoParam.setNewModelName(NEW_MODEL_NAME);

		// generate new (transformed) Ili file
		FileWriter out = new FileWriter(new File(OUTPUT_ILI_FILE));
		ch.interlis.ili2c.generator.Interlis1Generator.generate(out, td, trafoParam);
		out.close();

		// Read generated ili file
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(OUTPUT_ILI_FILE));
		assertNotNull(newTd);
		
		//Model Name Control
		Element modelEle = newTd.getElement(NEW_MODEL_NAME);
		assertEquals(NEW_MODEL_NAME, modelEle.getName());
		
		//Control of the TopicA -> Point2D
		modelEle = newTd.getElement(NEW_MODEL_NAME + ".TopicA.Point2D");
		Domain dd = (Domain) modelEle;
		Type type = dd.getType();

		NumericalType[] numericalType = ((AbstractCoordType) type).getDimensions();

		NumericType ntyp = (NumericType) numericalType[0];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());

		ntyp = (NumericType) numericalType[1];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());
		
		//Control of the ClassA1->point Control
		modelEle = newTd.getElement(NEW_MODEL_NAME + ".TopicA.ClassA1.point");
		AttributeDef attrDef = (AttributeDef) modelEle;
		Type attrType = attrDef.getDomain();
		
		NumericalType[] absNumType = ((AbstractCoordType) attrType).getDimensions();
		
		ntyp = (NumericType) absNumType[0];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());
		
		ntyp = (NumericType) absNumType[1];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());
		
		//Control of the Control Point Domain 
		modelEle = newTd.getElement(NEW_MODEL_NAME + ".TopicA.ClassA1.Geometry");
		LocalAttribute lclAttribute = (LocalAttribute)modelEle;
		type = lclAttribute.getDomain();
		LineType lineType = (LineType) type;
		Domain controlPointDomain = lineType.getControlPointDomain();
		Type controlPointType = controlPointDomain.getType();
		
		numericalType = ((CoordType) controlPointType).getDimensions();
		
		ntyp = (NumericType) absNumType[0];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());
		
		ntyp = (NumericType) absNumType[1];
		assertEquals("10.000", ntyp.getMinimum().toString());
		assertEquals("620.000", ntyp.getMaximum().toString());
	}

}
