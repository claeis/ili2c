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
  importVocab=Ili2Parser;
}
{
  private IliFile iliFile=null;
  private IliModel model=null;
  static public void mergeFile(IliFile iliFile1,
    java.io.InputStream stream
    )
  {
  	try{
		Ili2Lexer lexer = new Ili2Lexer (stream);
		Ili2ModelScan parser = new Ili2ModelScan(lexer);
		parser.iliFile=iliFile1;
		  parser.file();
		if(parser.model!=null){
			parser.iliFile.addModel(parser.model);
		}
  	}
  	catch(RecognitionException ex){
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
}
file 
: ("INTERLIS"       // INTERLIS 2.x
		| ("TRANSFER" NAME SEMI) // INTERLIS 1
	)
	((("MODEL" n:NAME)
		{
			if(model!=null){
				iliFile.addModel(model);
			}
			model=new IliModel();
			model.setName(n.getText());
			//EhiLogger.debug(n.getText());
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
