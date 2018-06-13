package ch.interlis.ili2c.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

import ch.interlis.ili2c.generator.nls.Ili2TranslationXml;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;
import ch.interlis.ili2c.metamodel.AbstractCoordType;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;

/**
 * Test Program for Interlis2Generator
 */

public class Interlis2GeneratorTransformationTest {

	private static final String ILI_FILE = "test\\data\\interlis2generator\\TrafoParam.ili";
	private static final String OUTPUT_ILI_FILE = "out.ili";

	@Test
	public void transformationPoint2D() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));

		// Fill Structure
		TransformationParameter trafoParam = new TransformationParameter();
		insertValuesInTrafoParam(trafoParam);

		// create a new Ili File
		createANewIliFile(td, trafoParam);

		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(OUTPUT_ILI_FILE),
				new File(ILI_FILE));
		assertNotNull(newTd);

		Element modelEle = newTd.getElement("LineType.TopicA.Point2D");
		if (modelEle instanceof Domain) {
			Domain dd = (Domain) modelEle;
			if (dd.getType() != null) {
				Type type = dd.getType();

				if (type instanceof AbstractCoordType) {
					NumericalType[] numericalType = ((AbstractCoordType) type).getDimensions();
					for (int i = 0; i < numericalType.length; i++) {
						if (numericalType[i] instanceof NumericType) {
							NumericType ntyp = (NumericType) numericalType[i];
							if ((i % 2) == 0) {
								assertEquals("10.000", ntyp.getMinimum().toString());
								assertEquals("410.000", ntyp.getMaximum().toString());
							} else {
								assertEquals("20.000", ntyp.getMinimum().toString());
								assertEquals("620.000", ntyp.getMaximum().toString());
							}
						}
					}
				} else if (type instanceof NumericalType) {
					NumericalType ntype = (NumericalType) type;
					NumericType numericType = (NumericType) ntype;

					assertEquals("10.000", numericType.getMinimum().toString());
					assertEquals("410.000", numericType.getMaximum().toString());
					//LineType.TopicA.Coord2
				}
			}
		}
	}
	
	@Test
	public void transformationCoord2() throws Exception {
		// ili lesen
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));

		// Fill Structure
		TransformationParameter trafoParam = new TransformationParameter();
		insertValuesInTrafoParam(trafoParam);

		// create a new Ili File
		createANewIliFile(td, trafoParam);

		// neues ili lesen
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(OUTPUT_ILI_FILE),
				new File(ILI_FILE));
		assertNotNull(newTd);

		Element modelEle = newTd.getElement("LineType.TopicA.Coord2");
		if (modelEle instanceof Domain) {
			Domain dd = (Domain) modelEle;
			if (dd.getType() != null) {
				Type type = dd.getType();

				if (type instanceof AbstractCoordType) {
					NumericalType[] numericalType = ((AbstractCoordType) type).getDimensions();
					for (int i = 0; i < numericalType.length; i++) {
						if (numericalType[i] instanceof NumericType) {
							NumericType ntyp = (NumericType) numericalType[i];
							if ((i % 2) == 0) {
								assertEquals("920010.000", ntyp.getMinimum().toString());
								assertEquals("1740010.000", ntyp.getMaximum().toString());
							} else {
								assertEquals("135020.000", ntyp.getMinimum().toString());
								assertEquals("930020.000", ntyp.getMaximum().toString());
							}
						}
					}
				} else if (type instanceof NumericalType) {
					NumericalType ntype = (NumericalType) type;
					NumericType numericType = (NumericType) ntype;

					assertEquals("10.000", numericType.getMinimum().toString());
					assertEquals("410.000", numericType.getMaximum().toString());
				}
			}
		}
	}

	private void createANewIliFile(TransferDescription td, TransformationParameter trafoParam) throws IOException {
		FileWriter out = new FileWriter(new File(OUTPUT_ILI_FILE));
		new Interlis2Generator().generate(out, td, false, trafoParam);
		out.close();
	}

	private void insertValuesInTrafoParam(TransformationParameter trafoParam) {
		trafoParam.setDiff_x(10.0);
		trafoParam.setDiff_y(20.0);
		trafoParam.setFactor_x(2.0);
		trafoParam.setFactor_y(3.0);
	}

}