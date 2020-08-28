header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.modelscan.*;
	import java.util.*;
	import ch.ehi.basics.logging.EhiLogger;
}

class Ili2ModelScan extends Parser;

options
{
  codeGenMakeSwitchThreshold = 3;
  codeGenBitsetTestThreshold = 4;
  buildAST=false;
  //importVocab=Ili2Parser;
}
{
  private IliFile iliFile=null;
  private IliModel model=null;
  static public void mergeFile(IliFile iliFile1,
    java.io.Reader stream
    )
  {
  	mergeFile(iliFile1,new IliScanLexer (stream));
  }
  static public void mergeFile(IliFile iliFile1,
    java.io.InputStream stream
    )
  {
  	mergeFile(iliFile1,new IliScanLexer (stream));
  }
  static public void mergeFile(IliFile iliFile1,
    IliScanLexer lexer
    )
  {
    try{
		Ili2ModelScan parser = new Ili2ModelScan(lexer);
		parser.iliFile=iliFile1;
		  parser.file();
		if(parser.model!=null){
			parser.iliFile.addModel(parser.model);
		}
    }catch(RecognitionException ex){
  		EhiLogger.logError(ex);
    }catch(antlr.TokenStreamRecognitionException ex){
    	if(ex.recog instanceof antlr.NoViableAltForCharException){
		// ignore unexpected char's
	}else{
		EhiLogger.logError(ex);
	}
     }catch(TokenStreamException ex){
		EhiLogger.logError(ex);
     }
  }
  static public double getIliVersion(java.io.Reader stream)
  {
  	return getIliVersion(new IliScanLexer (stream));
  }
  static public double getIliVersion(java.io.InputStream stream)
  {
  	return getIliVersion(new IliScanLexer (stream));
  }
  static public double getIliVersion(IliScanLexer lexer
    )
  {
  	try{
		Ili2ModelScan parser = new Ili2ModelScan(lexer);
		double version=parser.version();
		return version;
  	}
  	catch(Exception ex){
		// ignore errors
	}
	return 0.0;
  }
}
file 
{double version=0.0;
}
: (("INTERLIS" v:DEC  {version=Double.parseDouble(v.getText());}    // INTERLIS 2.x
   )
   | ("TRANSFER" NAME SEMI {version=1.0;} // INTERLIS 1
   )
	)
	((("MODEL" n:NAME)
		{
			if(model!=null){
				iliFile.addModel(model);
			}
			model=new IliModel();
			model.setName(n.getText());
			model.setIliVersion(version);
			//EhiLogger.debug(iliFile.getFilename().toString() +", "+n.getText());
		}
	| ("TRANSLATION" "OF" trsl:NAME
		{
			String name=trsl.getText();
			//EhiLogger.debug("  "+name);
			if(model!=null){
				model.addDepenedency(name);
			}
		}
		)
	| ("IMPORTS" ("UNQUALIFIED")? (imp1:NAME
		{
			String name=imp1.getText();
			//EhiLogger.debug("  "+name);
			if(model!=null){
				model.addDepenedency(name);
			}
		}
		|"INTERLIS") 
		(options {
		        warnWhenFollowAmbig=false;
		    }
		: COMMA ("UNQUALIFIED")? (imp2:NAME
		{
			String name=imp2.getText();
			//EhiLogger.debug("  "+name);
			if(model!=null){
				model.addDepenedency(name);
			}
		}
		|"INTERLIS"))*)
	 | .
	 )
	)*
	
;
version 
	returns[double v]
	{
	v=0.0;
	}
: ("INTERLIS" dec:DEC       // INTERLIS 2.x
	{v= Double.parseDouble(dec.getText());}
)
| ("TRANSFER" NAME SEMI // INTERLIS 1
	{v= 1.0;}
)
;


class IliScanLexer extends Lexer;
options {
  charVocabulary = '\u0000'..'\uFFFE'; // set the vocabulary to be all 8 bit binary values
  k=5;                   // number of lookahead characters
  testLiterals = false;  // do not test for literals by default
}


tokens {
  PLUS;
  MINUS;
}

{
  public boolean isIli1=false;
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

// Single Line comment -- ignored
SL_COMMENT
  : "!!"!
    ( ~('\n'|'\r') )*
    ( '\n' | '\r' ( '\n' )? )
    { $setType(Token.SKIP); newline(); }
  ;

  
/* multiple line comments
 are ignored.
   This code has been copied from the ANTLR example grammar for
   the Java syntax.
*/
ML_COMMENT
  : "/*" 
    ( /* '\r' '\n' can be matched in one alternative or by matching
         '\r' in one iteration and '\n' in another.  I am trying to
         handle any flavor of newline that comes in, but the language
         that allows both "\r\n" and "\r" and "\n" to all be valid
         newline is ambiguous.  Consequently, the resulting grammar
         must be ambiguous.  I'm shutting this warning off.
      */
      options {
        generateAmbigWarnings=false;
      }
      :
      { LA(2) != '/' }? '*'
      | ML_COMMENT
      | '\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r')
    )*
    "*/"
    {$setType(Token.SKIP);}
  ;


// see multiple-line comments in ANTLR example grammar for Java syntax
EXPLANATION
  : "//"!
    (
      /* '\r' '\n' can be matched in one alternative or by matching
         '\r' in one iteration and '\n' in another.  I am trying to
         handle any flavor of newline that comes in, but the language
         that allows both "\r\n" and "\r" and "\n" to all be valid
         newline is ambiguous.  Consequently, the resulting grammar
         must be ambiguous.  I'm shutting this warning off.
      */
      options { generateAmbigWarnings=false; } :

      { LA(2)!='/' }? '/'
      | '\r' '\n'		{newline();}
      | '\r'			{newline();}
      | '\n'			{newline();}
      | ~('/'|'\n'|'\r')
    )*
    "//"!
  ;


LPAREN options { paraphrase = "'('"; }
  : '('
  ;


RPAREN options { paraphrase = "')'"; }
  : ')'
  ;


LBRACE options { paraphrase = "'['"; }
  : '['
  ;


RBRACE options { paraphrase = "']'"; }
  : ']'
  ;


LCURLY options { paraphrase = "'{'"; }
  : '{'
  ;

RCURLY options { paraphrase = "'}'"; }
  : '}'
  ;


STAR options { paraphrase = "'*'"; }
  : '*'
  ;

SLASH options { paraphrase = "'/'"; }
  : '/'
  ;

BACKSLASH options { paraphrase = "'\\'"; }
  : '\\'
  ;


PERCENT options { paraphrase = "'%'"; }
  : '%'
  ;

AT options { paraphrase = "'@'"; }
  : '@'
  ;


HASH options { paraphrase = "'#'"; }
  : '#'
  ;


TILDE options { paraphrase = "'~'"; }
  : '~'
  ;


LESS options { paraphrase = "'<'"; }
  : '<'
  ;

LESSEQUAL options { paraphrase = "'<='"; }
  : "<="
  ;

GREATERGREATER options { paraphrase = "'>>'"; }
  : ">>"
  ;
GREATER options { paraphrase = "'>'"; }
  : '>'
  ;

GREATEREQUAL options { paraphrase = "'>='"; }
  : ">="
  ;


SEMI options { paraphrase = "';'"; }
  : ';'
  ;


EQUALS options { paraphrase = "'='"; }
  : '='
  ;


EQUALSEQUALS options { paraphrase = "'=='"; }
  : "=="
  ;


LESSGREATER options { paraphrase = "'<>'"; }
  : "<>"
  ;


BANGEQUALS options { paraphrase = "'!='"; }
  : "!="
  ;


COLONEQUALS options { paraphrase = "':='"; }
  : ":="
  ;


DOT options { paraphrase = "'.'"; }
  : '.'
  ;


DOTDOT options { paraphrase = "'..'"; }
  : ".."
  ;


COLON options { paraphrase = "':'"; }
  : ':'
  ;


COMMA options { paraphrase = "','"; }
  : ','
  ;


LESSMINUS options { paraphrase = "'<-'"; }
  : "<-"
  ;


POINTSTO options { paraphrase = "'->'"; }
  : "->"
  ;


AGGREGATE options { paraphrase = "'-<>'"; }
  : "-<>"
  ;


ASSOCIATE options { paraphrase = "'--'"; }
	: "--"
	;

COMPOSITE options { paraphrase = "'-<#>'"; }
	: "-<#>"
	;

protected ESC
  : '\\'
    ( '"' | '\\' | 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT )
  ;


STRING
  : '"'!
    ( ESC | ~( '"' | '\\' ) )*
    '"'!
  ;


protected DIGIT
    :   '0' .. '9'
    ;

protected LETTER
    :   'a' .. 'z' | 'A' .. 'Z'
    ;


protected POSINT
  : ( DIGIT )+
  ;


protected NUMBER
  : ( '+'! | '-')? POSINT
  ;


protected ILI1_SCALING
  : 'S'  NUMBER
  ;

protected SCALING
  : ('E'|'e')  NUMBER
  ;

protected ILI1_DEC
  : NUMBER
    ( DOT POSINT )?
    ( ILI1_SCALING )?
  ;

/*
Dec = ( Number [ '.' PosNumber ] | Float ).
Float = [ '+' | '-' ] '0.' (('1'|'2'|...|'9') [PosNumber] | (* '0' *))
        Scaling.
*/

protected DEC
  : 
  (( '+' | '-')? '0' DOT ('1'..'9' POSINT | ('0')+) ('e' | 'E')) => NUMBER DOT POSINT SCALING
  | NUMBER
    ( DOT POSINT )?
     ;      



protected HEXDIGIT
  : DIGIT
    | 'a' .. 'f'
    | 'A' .. 'F'
  ;


HEXNUMBER
  :  '0' ( 'x' | 'X' ) (HEXDIGIT)+
  ;


NUMERICSTUFF
  : ( POSINT DOTDOT ) => POSINT { $setType(POSINT); }
  | ( NUMBER DOTDOT ) => NUMBER { $setType(NUMBER); }
  | ( NUMBER ( '.' | {isIli1}? 'S' | {!isIli1}? ('e' | 'E') ) ) => ({isIli1}? ILI1_DEC { $setType(ILI1_DEC); } | {!isIli1}? DEC { $setType(DEC); })
  | '+'! ( POSINT {$setType(NUMBER);} | {$setType(PLUS);})
  | '-' ( POSINT {$setType(NUMBER);} | {$setType(MINUS);})
  | POSINT {$setType(POSINT);}
  ;


NAME
options { testLiterals = true; }
  :  LETTER
     ( LETTER | '_' | DIGIT )*
  ;
