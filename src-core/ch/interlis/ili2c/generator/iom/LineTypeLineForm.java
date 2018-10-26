package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
    ASSOCIATION LineTypeLineForm =
      lineType -- {0..*} LineType;
      lineForm -- {0..*} LineForm;
    END LineTypeLineForm;
 */
public class LineTypeLineForm {
	public LineType lineType;
	public LineForm lineForm;
	public LineTypeLineForm(LineType lineType1,LineForm lineForm1)
	{
		lineType=lineType1;
		lineForm=lineForm1;
	}
}
