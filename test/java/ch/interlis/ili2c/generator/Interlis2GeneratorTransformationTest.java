package ch.interlis.ili2c.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;

import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;
import ch.interlis.ili2c.metamodel.AbstractCoordType;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.LineType;
import ch.interlis.ili2c.metamodel.NumericType;
import ch.interlis.ili2c.metamodel.NumericalType;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.Type;
import ch.interlis.ili2c.metamodel.TypeAlias;

/**
 * Test Program for Interlis2Generator
 */

public class Interlis2GeneratorTransformationTest {

	private static final String NEW_MODEL_NAME = "LineType_200";
	private static final String ILI_FILE = "test\\data\\interlis2generator\\TrafoParam.ili";
	private static final String OUTPUT_ILI_FILE = "out.ili";

	@Test
	public void transformation() throws Exception {
		// Read ili
		TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));

		// Fill Structure
		TransformationParameter trafoParam = new TransformationParameter();
		trafoParam.setDiff_x(10.0);
		trafoParam.setDiff_y(20.0);
		trafoParam.setFactor_x(2.0);
		trafoParam.setFactor_y(3.0);
		trafoParam.setEpsgCode(200);
		trafoParam.setImportModels(new TransformationParameter.ModelTransformation[] {
				new TransformationParameter.ModelTransformation("GeometryCHLV95_V1", "GeometryCHLV03_V1")
		});
		trafoParam.setNewModelName(NEW_MODEL_NAME);

		// create a new Ili File
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
		new Interlis2Generator().generateWithNewCrs(out, td, trafoParam);
		out.close();

		// Read new Ili
		TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(OUTPUT_ILI_FILE), new File(ILI_FILE));
		assertNotNull(newTd);

		{	
			Element modelEle = newTd.getElement(NEW_MODEL_NAME+".TopicA.Point2D");
			Domain dd = (Domain) modelEle;
			Type type = dd.getType();

			// Does it has the right meta-values?
			assertEquals("test", getMetaValue("gugus",  dd.getMetaValues()));
			assertEquals("200", getMetaValue("CRS",  dd.getMetaValues()));

			NumericalType[] numericalType = ((AbstractCoordType) type).getDimensions();
			{
				NumericType ntyp = (NumericType) numericalType[0];
				assertEquals("10.000", ntyp.getMinimum().toString());
				assertEquals("410.000", ntyp.getMaximum().toString());
			}
			{
				NumericType ntyp = (NumericType) numericalType[1];
				assertEquals("20.000", ntyp.getMinimum().toString());
				assertEquals("620.000", ntyp.getMaximum().toString());
			}
		}
		{
			Element modelEle = newTd.getElement(NEW_MODEL_NAME+".TopicA.ClassA1.Geometry");
			AttributeDef dd = (AttributeDef) modelEle;
			Domain type = ((LineType)dd.getDomain()).getControlPointDomain();

			assertEquals("GeometryCHLV95_V1.Coord2", type.getScopedName());
		}
		{
			Element modelEle = newTd.getElement(NEW_MODEL_NAME+".TopicA.ClassA1.point");
			AttributeDef dd = (AttributeDef) modelEle;
			Type type = dd.getDomain();

			// Does it has the right meta-values?
			assertEquals("test2", getMetaValue("gugus2",  dd.getMetaValues()));
			assertEquals("200", getMetaValue("CRS",  dd.getMetaValues()));

			NumericalType[] numericalType = ((AbstractCoordType) type).getDimensions();
			{
				NumericType ntyp = (NumericType) numericalType[0];
				assertEquals("10.000", ntyp.getMinimum().toString());
				assertEquals("410.000", ntyp.getMaximum().toString());
			}
			{
				NumericType ntyp = (NumericType) numericalType[1];
				assertEquals("20.000", ntyp.getMinimum().toString());
				assertEquals("620.000", ntyp.getMaximum().toString());
			}
		}
	}

	private String getMetaValue(String meta, Settings metaValues) {
		Iterator<String> metaValuesI = metaValues.getValuesIterator();
		while (metaValuesI.hasNext()) {
			String name = (String) metaValuesI.next();
			if (name.equals(meta)) {
				return metaValues.getValue(name);
			} else if (name.equals(meta)) {
				String metaValue = metaValues.getValue(name);
				String[] values = metaValue.split(":");
				return values[1].toString();
			}
		}
		return null;
	}

}