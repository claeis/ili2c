/*****************************************************************************
 *
 * ErrorListener.java
 * ------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 1.0  August 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

/** ErrorListener is an interface that allows interested parties to be
    informed about errors while parsing or generating INTERLIS
    description files. For example, the method addParsed of the
    class ch.interlis.TransferDescription takes an object that
    implements the ErrorListener interface.

    @version   August 31, 1999
    @author    Sascha Brawer
*/
public interface ErrorListener
{
  /** An object that contains the relevant information about
      an error that has occured.
  */
  public static class ErrorEvent
  {
    public static final int SEVERITY_ERROR = 5;
    public static final int SEVERITY_WARNING = 3;
    public static final int SEVERITY_NOTE = 1;

    protected String message = null;
    protected int line = 0;
    protected String filename = null;
    protected int severity = SEVERITY_ERROR;
    protected Throwable exception=null;

    public ErrorEvent (String message, String filename, int line, int severity)
    {
      this.message = message;
      this.filename = filename;
      this.line = line;
      this.severity = severity;
    }


    public ErrorEvent (Throwable t, String filename, int line, int severity)
    {
    	exception=t;
      this.message = t.getLocalizedMessage ();
      this.filename = filename;
      this.line = line;
      this.severity = severity;
    }


    public ErrorEvent (String message, Element origin, int severity)
    {
      this.message = message;
      this.severity = severity;
    }


    /** Returns the name of the file in which the error has occured.
        If the error has not occured in a specific file, the result
        is <code>null</code>.
    */
    public String getFileName ()
    {
      return filename;
    }


    /** Returns the line number where the error has occured. If the
        error has not occured at a specific line, the result
        is zero.
    */
    public int getLine ()
    {
      return line;
    }


    /** Returns a localized message that describes the error.
    */
    public String getMessage ()
    {
      return message;
    }


    /** Constructs a string that indicates file name,
        line number and error message. The individual
        items are separated by colons.
    */
    public String toString()
    {
      StringBuilder buf = new StringBuilder(100);

      if (filename != null)
      {
        buf.append (filename);
        buf.append (':');
      }

      if (line != 0)
      {
        buf.append(line);
        buf.append (':');
      }

      buf.append (message);
      return buf.toString();
    }


    /** Returns the severity of the error. The value
        is one of <code>SEVERITY_ERROR</code>,
        <code>SEVERITY_WARNING</code> or
        <code>SEVERITY_NOTE</code>.
    */
    public int getSeverity ()
    {
      return severity;
    }

    public Throwable getException()
    {
    	return exception;
    }
  };


  /** Called when an error has occured. If several errors occur,
      for example while parsing an INTERLIS description with
      many errors, this method is called multiple times.

      @param error An ErrorEvent that describes the error that
                   has occured. Use the methods of ErrorEvent
                   to find out more about the circumstances
                   of the error.
  */
  public void error (ErrorEvent error);
}
