package ch.interlis.ili2c.parser;


import ch.interlis.ili2c.metamodel.ErrorListener;

import org.xml.sax.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;


import java.util.Map;
import java.util.Iterator;
import ch.interlis.ili2c.generator.XSDGenerator;


public final class MetaObjectTopicScanner
{
  String              fileName;
  ErrorListener       errorListener;
  Parser              parser;
  boolean             fatalErrorEncountered = false;
  // list of tag names that are baskets and therefore qualified topic names
  java.util.Set       topics = new java.util.HashSet ();


  void reportException (Exception ex, int lineNumber)
  {
    ErrorListener.ErrorEvent theEvent;

    theEvent = new ErrorListener.ErrorEvent (ex, fileName, lineNumber,
      ErrorListener.ErrorEvent.SEVERITY_ERROR);
    errorListener.error (theEvent);
  }

  /** returns a list of qualified names of topics inside the
   * parsed xml file.
   * @returns set<String qualifiedTopicName>
   */
  public java.util.Set getTopics(){
    return topics;
  }

  public boolean parse (String fileName,
    ch.interlis.ili2c.metamodel.ErrorListener errorListener)
  {
    this.fileName = fileName;
    this.errorListener = errorListener;

    ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent theEvent = null;
    org.xml.sax.HandlerBase myHandler = new MyHandler ();

    try {
      // ce SAXParserFactory spf = SAXParserFactory.newInstance ();


/*      String validation = System.getProperty ("javax.xml.parsers.validation", "false");
      if (validation.equalsIgnoreCase("true"))
        spf.setValidating (true);
*/
      // ce spf.setValidating (true);

      // ce parser = spf.newParser ();
		String parserClass = "org.apache.xerces.parsers.SAXParser";
		parser = ParserFactory.makeParser(parserClass);
      parser.setDocumentHandler (myHandler);
      parser.setErrorHandler (myHandler);


      parser.parse ("file:" + new java.io.File (fileName).getAbsolutePath ());
    } catch (SAXParseException err) {
      theEvent = new ErrorListener.ErrorEvent (
        /* message */ err.getMessage(),
        /* file name */ err.getSystemId (),
        /* line number */ err.getLineNumber (),
        /* severity */ ErrorListener.ErrorEvent.SEVERITY_ERROR);
      errorListener.error (theEvent);
      return false;
    } catch (SAXException e) {
      Exception	x = e;
      if (e.getException () != null)
        x = e.getException ();
      reportException (x, 0);
      return false;
    } catch (Exception t) {
      reportException (t, 0);
      return false;
    }
    return !fatalErrorEncountered;
  }


  class MyHandler extends org.xml.sax.HandlerBase
  {

    public void warning (SAXParseException ex)
    {
      errorListener.error (new ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent (
        ex.getMessage(), ex.getSystemId (), ex.getLineNumber (),
        /* severity */ ErrorListener.ErrorEvent.SEVERITY_WARNING));
    }


    public void error (SAXParseException ex)
    {
      errorListener.error (new ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent (
        ex.getMessage(), ex.getSystemId (), ex.getLineNumber (),
        /* severity */ ErrorListener.ErrorEvent.SEVERITY_ERROR));
    }


    public void fatalError (SAXParseException ex)
    {
      fatalErrorEncountered = true;
      errorListener.error (new ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent (
        ex.getMessage(), ex.getSystemId (), ex.getLineNumber (),
        /* severity */ ErrorListener.ErrorEvent.SEVERITY_ERROR));
    }


	private int level=0;
	private boolean inTransfer=false;
	private int TRANSFER_LEVEL=1;
	private boolean inDataSection=false;
	private int DATASECTION_LEVEL=2;
	private boolean inBasket=false;
	private int BASKET_LEVEL=3;

    public void startElement (java.lang.String name, AttributeList atts)
    {
    	level++;
    	if(level==TRANSFER_LEVEL && name.equals("TRANSFER")){
    		inTransfer=true;
    		return;
    	}
    	if(level==DATASECTION_LEVEL && inTransfer && name.equals("DATASECTION")){
    		inDataSection=true;
    		return;
    	}
    	if(level==BASKET_LEVEL && inDataSection){
                      topics.add(name);
    	}
    }
    public void endElement(String name)
    {

    	if(level==BASKET_LEVEL && inBasket){
    		inBasket=false;
    	}else if(level==DATASECTION_LEVEL && inDataSection){
    		inDataSection=false;
    	}else if(level==TRANSFER_LEVEL && inTransfer){
    		inTransfer=false;
    	}
    	level--;
    }
	/**
	 * @see HandlerBase#characters(char[], int, int)
	 */
/*	public void characters(char[] ch, int start, int length) throws SAXException {
	}*/
  };


}
