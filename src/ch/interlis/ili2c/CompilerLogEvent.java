/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ch.interlis.ili2c;

import ch.ehi.basics.logging.*;

/**
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2007-03-29 15:36:02 $
 */
public class CompilerLogEvent extends StdLogEvent {
	private int line = 0;
	private String filename = null;
	public CompilerLogEvent(int kind,String msg,Throwable ex,StackTraceElement origin) {
		super(kind,msg,ex,origin);
	}
	public CompilerLogEvent(String filename,int line,int kind,String msg,Throwable ex,StackTraceElement origin) {
		super(kind,msg,ex,origin);
		this.filename=filename;
		this.line=line;
	}
	/** Returns the name of the file in which the error has occured.
		If the error has not occured in a specific file, the result
		is <code>null</code>.
	*/
	public String getFilename()
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
	/** errors (program errors or input errors)
	 */
	static public void logError(String filename,int line,String errmsg){
		EhiLogger.getInstance().logEvent(new CompilerLogEvent(filename,line,LogEvent.ERROR,errmsg,null,getCallerOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String filename,int line,String errmsg,Throwable ex){
		EhiLogger.getInstance().logEvent(new CompilerLogEvent(filename,line,LogEvent.ERROR,errmsg,ex,getCallerOrigin()));
	}
	/** errors (program errors or input errors)
	 */
	static public void logError(String filename,int line,Throwable ex){
		EhiLogger.getInstance().logEvent(new CompilerLogEvent(filename,line,LogEvent.ERROR,null,ex,getCallerOrigin()));
	}
	/** inform user about adaptions taken by the code (e.g. ignoring some supefluous input)
	 */ 
	static public void logAdaption(String filename,int line,String adaption){
		EhiLogger.getInstance().logEvent(new CompilerLogEvent(filename,line,LogEvent.ADAPTION,adaption,null,getCallerOrigin()));
	}
	private static boolean passWarnings=true;
	public static void enableWarnings(boolean val)
	{
		passWarnings=val;	
	}
	static public void logWarning(String filename,int line,String adaption){
		if(passWarnings){
			EhiLogger.getInstance().logEvent(new CompilerLogEvent(filename,line,LogEvent.ADAPTION,adaption,null,getCallerOrigin()));
		}
	}
	/** gets the origin of a call to logError() functions.
	 */
	static private StackTraceElement getCallerOrigin(){
		Throwable tr=new Throwable();
		StackTraceElement stack[]=tr.getStackTrace();
		// stack[0]: getOrigin()
		// stack[1]: logError()
		// stack[2]: user code
		if(2<stack.length){
			StackTraceElement st=stack[2]; 
			return st;
		}
		return null;
	}

	public String getEventMsg() {
		String msg=super.getEventMsg();
		if(msg==null){
			msg=getException().getLocalizedMessage();
			if(msg!=null){
				msg=msg.trim();
				if(msg.length()==0){
					msg=null;
				}
			}
			if(msg==null){
				msg=getException().getClass().getName();
			}
		}
		String fn=getFilename();
		int line=getLine();
		return (fn!=null?fn+":":"")+(line!=0?Integer.toString(line)+":":"")+msg;
	}
	public String getRawEventMsg() {
		return super.getEventMsg();
	}

}
