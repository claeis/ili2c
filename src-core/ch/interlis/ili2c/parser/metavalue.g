header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.metamodel.*;
	import java.util.*;
	import ch.ehi.basics.logging.EhiLogger;
}

class MetaValue extends Parser;

options
{
  codeGenMakeSwitchThreshold = 3;
  codeGenBitsetTestThreshold = 4;
  buildAST=false;
  defaultErrorHandler=false;
}
{
  static public ch.ehi.basics.settings.Settings parseMetaValues(String metaValueText)
  throws ANTLRException
  {
  	ch.ehi.basics.settings.Settings ret=null;
		MetaValueLexer lexer = new MetaValueLexer (new java.io.StringReader(metaValueText));
		MetaValue parser = new MetaValue(lexer);
		ret=parser.metaValues();
	return ret;
  }
}

metaValues 
	returns [ch.ehi.basics.settings.Settings c]
	{
	c=null;
	String v=null;
	}
: (n:VALUE EQUALS v=avalue
	{ 
	c=new ch.ehi.basics.settings.Settings(true);
	c.setValue(n.getText(),v);
	}
 (SEMI (
 		n2:VALUE EQUALS v=avalue
		{ 
		c.setValue(n2.getText(),v);
		}
	)?
 )*
 )? EOF
;

avalue returns [String c]
{ 
	c=null;
}
: s:STRING { c=s.getText(); }
| p:VALUE {c=p.getText();}
;

class MetaValueLexer extends Lexer;
options {
  charVocabulary = '\u0000'..'\uFFFE'; // set the vocabulary to be all 8 bit binary values
  k=5;                   // number of lookahead characters
  testLiterals = false;  // do not test for literals by default
}

// Whitespace -- ignored
WS
  : (
      ' '
    | '\t'
    | '\f'

    // handle newlines
    | (
        options { generateAmbigWarnings=false; } :

        "\r\n"  // DOS
        | '\r'    // Macintosh
        | '\n'    // Unix
      )
      { newline(); }
    )+
    { $setType(Token.SKIP); }
  ;

EQUALS options { paraphrase = "'='"; }
  : '='
  ;

SEMI options { paraphrase = "';'"; }
  : ';'
  ;


COMMA options { paraphrase = "','"; }
  : ','
  ;


protected ESC
  : '\\'
    ( '"' | '\\' | 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT )
  ;
protected HEXDIGIT
  : '0' .. '9'
    | 'a' .. 'f'
    | 'A' .. 'F'
  ;


STRING
  : '"'!
    ( ESC | ~( '"' | '\\' ) )*
    '"'!
  ;


VALUE
    :  (   ~('\t'|'\f'|'\r'|'\n'|' '|'='|';'|','|'"') )+
    ;

