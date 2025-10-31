package ch.interlis.ili2c.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;

import org.junit.Test;

import antlr.Token;
import antlr.TokenStreamRecognitionException;

public class IliScanLexerTest {

    @Test
    public void unterminatedBlockComment_throwsTokenStreamRecognitionException() throws Exception {
        IliScanLexer lexer = new IliScanLexer(new StringReader("/** Unterminated comment"));

        try {
            while (true) {
                Token token = lexer.nextToken();
                if (token.getType() == Token.EOF_TYPE) {
                    break;
                }
            }
            fail("Expected unterminated comment to trigger a TokenStreamException");
        } catch (antlr.TokenStreamException ex) {
            assertEquals("Unterminated comment", ex.getMessage());
        }
    }
}
