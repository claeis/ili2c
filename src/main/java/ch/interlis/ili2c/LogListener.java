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
 * @version $Revision: 1.3 $ $Date: 2007-07-03 12:56:35 $
 */
public class LogListener extends ch.ehi.basics.logging.AbstractStdListener {

	public void outputMsgLine(int arg0, int arg1, String msg) {
		System.err.println(msg);
		if(errOutput!=null){
			if(msg.endsWith("\n")){
				errOutput.append(msg);
			}else{
				errOutput.append(msg+"\n");
			}
		}
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
