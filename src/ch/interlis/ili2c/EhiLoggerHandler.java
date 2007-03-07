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

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent;

/** adapts error messages of the compiler to EhiLogger.
 * @author ce
 * @version $Revision: 1.1.1.1 $ $Date: 2007-03-07 07:51:48 $
 */
public class EhiLoggerHandler implements ch.interlis.ili2c.metamodel.ErrorListener{
	/** called by the compiler for every error message
	 *
	 */
	public void error(ch.interlis.ili2c.metamodel.ErrorListener.ErrorEvent evt) {
		  String msg=evt.getMessage();
		  int line=evt.getLine();
		  String filename=evt.getFileName();
		  if(evt.getSeverity()==ErrorEvent.SEVERITY_WARNING){
		  	if(generateWarnings){
				EhiLogger.logAdaption(filename+":"+line+":"+msg);
		  	}
		  }else{
			EhiLogger.logError(filename+":"+line+":"+msg);
		  }
	}
	private boolean generateWarnings=false;
	public boolean isGenerateWarnings() {
		return generateWarnings;
	}
	public void setGenerateWarnings(boolean b) {
		generateWarnings = b;
	}

}
