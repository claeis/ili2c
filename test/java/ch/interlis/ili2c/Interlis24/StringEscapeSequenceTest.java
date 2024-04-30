package ch.interlis.ili2c.Interlis24;

import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.metamodel.Constant;
import ch.interlis.ili2c.metamodel.Expression;
import ch.interlis.ili2c.metamodel.MandatoryConstraint;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringEscapeSequenceTest {
    private static final String DATA_DIRECTORY = "test/data/ili24/string/";
    private static final String CLASS_NAME = "ModelA.TopicA.ClassA";
    private TransferDescription td;

    @Before
    public void setup() {
        td = CompilerTestHelper.getTransferDescription(DATA_DIRECTORY + "StringEscapeSequence.ili");
    }

    @Test
    public void quotes() {
        Constant.Text text = getRightTextConst(CLASS_NAME + ".quotes");
        assertEquals("some \" quote", text.getValue());
    }

    @Test
    public void backslash() {
        Constant.Text text = getRightTextConst(CLASS_NAME + ".backslash");
        assertEquals("\\", text.getValue());
    }

    @Test
    public void quotesAndBackslash() {
        Constant.Text text = getRightTextConst(CLASS_NAME + ".quotesAndBackslash");
        assertEquals("\\\"", text.getValue());
    }

    @Test
    public void unicode() {
        Constant.Text text = getRightTextConst(CLASS_NAME + ".unicode");
        assertEquals("\u2b1c", text.getValue());
    }

    @Test
    public void unicodeSurrogate() {
        Constant.Text text = getRightTextConst(CLASS_NAME + ".unicodeSurrogate");
        assertEquals("\ud83c\udf09", text.getValue());
    }

    private Constant.Text getRightTextConst(String constraintName) {
        MandatoryConstraint constraint = (MandatoryConstraint) td.getElement(constraintName);
        Expression.Equality equals = (Expression.Equality) constraint.getCondition();
        return (Constant.Text) equals.getRight();
    }
}
