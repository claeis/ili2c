package ch.interlis.ili2c;

import ch.interlis.ili2c.parser.InterlisString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterlisStringTest {
    @Test
    public void parseEscapedQuote() {
        assertEquals("text \"with \"\" quotes\"", InterlisString.parseEscapeSequences("text \\\"with \\\"\\\" quotes\\\""));
    }

    @Test
    public void parseEscapedBackslash() {
        assertEquals("\\ \\", InterlisString.parseEscapeSequences("\\\\ \\\\"));
    }

    @Test
    public void parseEscapedUnicode() {
        assertEquals("\u2b1c", InterlisString.parseEscapeSequences("\\u2b1c"));
        assertEquals("\ud83c\udf09", InterlisString.parseEscapeSequences("\\ud83c\\udf09"));
        assertEquals("multiple\nlines", InterlisString.parseEscapeSequences("\\u006Dultiple\\u000Alines"));
    }

    @Test
    public void parseSinglePass() {
        assertEquals("\\\"", InterlisString.parseEscapeSequences("\\\\\\\""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseIncompleteSequence() {
        InterlisString.parseEscapeSequences("incomplete sequence: \\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseInvalidSequence() {
        InterlisString.parseEscapeSequences("\\n");
    }
}
