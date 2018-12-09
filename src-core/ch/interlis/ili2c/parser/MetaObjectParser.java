package ch.interlis.ili2c.parser;


import ch.interlis.ili2c.metamodel.*;
import org.xml.sax.*;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;


import java.util.Map;
import java.util.Iterator;
import ch.interlis.ili2c.generator.XSDGenerator;


public final class MetaObjectParser
{
  String              fileName;
  TransferDescription td;
  ErrorListener       errorListener;
  Parser              parser;
  boolean             fatalErrorEncountered = false;
  // list of mappings from tag names (to look after in the input stream) to model, topic or class definitions
  java.util.Map       transfernameToElement = new java.util.HashMap ();

  public static boolean parse (
    TransferDescription td, String fileName)
  {
    return (new MetaObjectParser (td, fileName)).parse ();
  }

  private MetaObjectParser (TransferDescription td,
    String fileName)
  {
    this.fileName = fileName;
    this.td = td;

    // build a list of the tag names that may represent METAOBJECT instances
    Iterator iter = td.INTERLIS.METAOBJECT.getExtensions().iterator();
    while (iter.hasNext())
    {
      Viewable v = (Viewable) iter.next();
      String transferName = XSDGenerator.getTransferName (v);
      transfernameToElement.put (/* key */ transferName, /* value */ v);
    }

    /* Find all models and topics. */
    findModelsAndTopics (td, transfernameToElement);
  }


  private static final void findModelsAndTopics (Container container, Map map)
  {
    Iterator iter = container.iterator ();
    while (iter.hasNext())
    {
      ch.interlis.ili2c.metamodel.Element elt = (ch.interlis.ili2c.metamodel.Element) iter.next();

      if ((elt instanceof Topic) || (elt instanceof Model))
      {
        String transferName = XSDGenerator.getTransferName (elt);
        map.put (/* key */ transferName, /* value */ elt);
      }

      if (elt instanceof Container)
        findModelsAndTopics ((Container) elt, map);
    }
  }


  void reportException (Exception ex, int lineNumber)
  {
    ErrorListener.ErrorEvent theEvent;

    theEvent = new ErrorListener.ErrorEvent (ex, fileName, lineNumber,
      ErrorListener.ErrorEvent.SEVERITY_ERROR);
    errorListener.error (theEvent);
  }

  boolean parse ()
  {
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
    DataContainer dataContainer = null;
	Table      theMetaTable = null;
	private boolean inTransfer=false;
	private int TRANSFER_LEVEL=1;
	private boolean inDataSection=false;
	private int DATASECTION_LEVEL=2;
	private boolean inBasket=false;
	private int BASKET_LEVEL=3;
	private boolean inTable=false;
	private int TABLE_LEVEL=4;
	private boolean inName=false;
	private int NAME_LEVEL=5;

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
			ch.interlis.ili2c.metamodel.Element elt = (ch.interlis.ili2c.metamodel.Element) transfernameToElement.get (name);
                      if(elt==null){
		          throw new IllegalArgumentException ("unknown topic "+name);
		      }else if (elt instanceof Topic)
		      {
		        Topic topic = (Topic) elt;
		        String boid = atts.getValue ("BID");
		        if (boid == null){
		        	// BOID is mandatory
		          throw new IllegalArgumentException ("Attribute BID missing in basket "+name);
		        }
		        if (!boid.startsWith("x")){
		        	// leading 'x' is mandatory
		          throw new IllegalArgumentException ("Attribute BID in basket "+name+" requires a leading 'x'");
		        }
                        // remove leading 'x' to get real BID
                        boid=boid.substring(1);
		        try {
					dataContainer = new DataContainer (boid, topic,fileName);
			  		td.addMetaDataContainer(dataContainer);
			  		inBasket=true;
		        } catch (Exception ex) {
		          reportException (ex, 0);
		        }
		        return;
		      }
    	}
    	if(level==TABLE_LEVEL && inBasket){
			ch.interlis.ili2c.metamodel.Element elt = (ch.interlis.ili2c.metamodel.Element) transfernameToElement.get (name);
                      if(elt==null){
		          //throw new IllegalArgumentException ("unknown class "+name);
		      }else if (elt instanceof Table)
		      {
		        /* Make sure that operation is "OBJE". */
		        String operation = atts.getValue ("OPERATION");
		        if (operation!=null && !"INSERT".equals (operation))
		        {
		          throw new IllegalArgumentException (
		            "The OPERATION attribute for " + elt.toString() + " must be \"INSERT\". "
		            + "The INTERLIS compiler does not support incremental updating of meta objects.");
		        }
		        theMetaTable = (Table) elt;
		        inTable=true;
		      }
    	}
    	if(level==NAME_LEVEL && inTable){
    		// get value of attribute "name"
			if(name.equals("Name")){
				inName=true;
			}
    	}

    }
    public void endElement(String name)
    {

    	if(level==NAME_LEVEL && inName){
    		inName=false;
    	}else if(level==TABLE_LEVEL && inTable){
    		inTable=false;
    	}else if(level==BASKET_LEVEL && inBasket){
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
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(inName){
				String theName=new String(ch,start,length);
				//System.err.println(theName);
		        MetaObject mo;
		        if (theMetaTable.isExtending (td.INTERLIS.REFSYSTEM))
		          mo = new ReferenceSystem (theName, theMetaTable);
		        else if (theMetaTable.isExtending (td.INTERLIS.COORDSYSTEM))
		          mo = new CoordinateSystem (theName, theMetaTable);
		        else if (theMetaTable.isExtending (td.INTERLIS.AXIS))
		          mo = new Axis (theName, theMetaTable);
		        else if (theMetaTable.isExtending (td.INTERLIS.SIGN))
		          mo = new Sign (theName, theMetaTable);
		        else
		          mo = new MetaObject (theName, theMetaTable);
		        dataContainer.add (mo);
		}
	}
  };


}
