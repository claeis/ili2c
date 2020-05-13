header
{
	package ch.interlis.ili2c.parser;
	import ch.interlis.ili2c.metamodel.*;
	import java.util.*;
	import ch.ehi.basics.logging.EhiLogger;
}

class Ili1Undef extends Parser;

options
{
  codeGenMakeSwitchThreshold = 3;
  codeGenBitsetTestThreshold = 4;
  buildAST=false;
  defaultErrorHandler=false;
}
{
  static public Evaluable parseValueIfUndefined(String attrName,String explanation)
  {
  	try{
		Ili2LexerClone lexer = new Ili2LexerClone (new java.io.StringReader(explanation));
		Ili1Undef parser = new Ili1Undef(lexer);
		return parser.defValue();
  	}catch(RecognitionException ex){
  		EhiLogger.logError(attrName+": syntax error in default value specification ("+ex.getLocalizedMessage()+")");
	}catch(antlr.TokenStreamRecognitionException ex){
    		if(ex.recog instanceof antlr.NoViableAltForCharException){
			// ignore unexpected char's
		}else{
			EhiLogger.logError(attrName,ex);
		}
	}catch(TokenStreamException ex){
		EhiLogger.logError(attrName,ex);
	}
	return null;
  }
}
defValue 
	returns [Evaluable c]
	{
	c=null;
	}
: "undefiniert" EQUALS 
	(c=constant 
	| "letztes" "Zeichen"
		{c=new LengthOfReferencedText();
		}
	  )
;

protected constant
	returns [Constant c]
	{
	  c = null;
	}
	:
		c=numericConst
	|	c=textConst
	|	c=enumerationConst
	;

protected textConst
	returns [Constant c]
	{
		c=null;
	}
	: s:STRING {c=new Constant.Text(s.getText());}
	;

protected enumerationConst
	returns[Constant.Enumeration c]
	{
	List mentionedNames=new ArrayList();
	int lin=0;
	c=null;
	}
	: 
	( lin=enumNameList[mentionedNames] 
		{
		c = new Constant.Enumeration(mentionedNames);
		}
	)
	;

protected enumNameList[List namList]
 returns [int lin]
 {
 lin=0;
 }
  : firstName:NAME
    { namList.add(firstName.getText());
    lin=firstName.getLine();
    }
    enumNameListHelper[namList]
  ;

/* this rule is a helper to rule enumNameList
* it uses a syntactic predicate to avoid nondeterminism between
* DOT NAME and DOT OTHERS
*/
protected enumNameListHelper[List namList]
  : (DOT NAME )=> DOT n:NAME {namList.add(n.getText());} enumNameListHelper[namList]
  | /* empty */
  ;
protected numericConst
	returns[Constant c]
	{ 
	PrecisionDecimal val;
	c=null;
	}
	: val=decConst 
	{
		c = new Constant.Numeric (val);
	}
	;

protected decConst
	returns [PrecisionDecimal dec]
	{
	 dec=null;
	}
	: "PI" { dec = PrecisionDecimal.PI; }
	| "LNBASE" {dec = PrecisionDecimal.LNBASE;}
	| dec = decimal
	;
protected decimal
	returns [PrecisionDecimal dec]
	{
		dec = null;
	}
	:	d:DEC
    	{ dec = new PrecisionDecimal(d.getText()); }
	|	p:POSINT
    	{ dec = new PrecisionDecimal(p.getText()); }
	|	n:NUMBER
	    { dec = new PrecisionDecimal(n.getText()); }
	;
class Ili2LexerClone extends Lexer;
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

ILI_DOC
  : "/**"
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
      | ILI_DOC
      | '\r' '\n'  {newline();}
      | '\r'       {newline();}
      | '\n'       {newline();}
      | ~('*'|'\n'|'\r')
    )*
    "*/"
  ;

/* multiple line comments
 are ignored.
   This code has been copied from the ANTLR example grammar for
   the Java syntax.
*/
ML_COMMENT
  : "/*" ~'*'
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
