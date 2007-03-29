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
import java.util.*;
import javax.swing.JTextArea;

/**
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2007-03-29 15:36:02 $
 */
public class LogListener implements ch.ehi.basics.logging.LogListener {

	private boolean errors=false;
	public void logEvent(LogEvent event){
		switch(event.getEventKind()){
			case LogEvent.ERROR:
				errors=true;
				break;
			default:
				break;
		}
		ArrayList msgv=StdListener.formatOutput(event,true,!EhiLogger.getInstance().getTraceFiler());
		Iterator msgi=msgv.iterator();
		while(msgi.hasNext()){
			String msg=(String)msgi.next();
			System.err.println(msg);
			if(errOutput!=null){
				if(msg.endsWith("\n")){
					errOutput.append(msg);
				}else{
					errOutput.append(msg+"\n");
				}
			}
		}
	}
	/** have there been errors logged?
	 */
	public static boolean hasSeenErrors(){
		return getInstance().errors;
	}
	/** LogListener is a singleton.
	 */
	private LogListener()
	{
	}
	private static LogListener instance=null;
	/** get single instance.
	 */
	public static LogListener getInstance()
	{
		if(instance==null){
			instance=new LogListener();
		}
		return instance;
	}
	private JTextArea  errOutput=null;
	public static void setSwingOutput(JTextArea  err)
	{
		getInstance().errOutput=err;
	}
}
