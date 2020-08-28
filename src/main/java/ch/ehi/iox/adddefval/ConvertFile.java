/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package ch.ehi.iox.adddefval;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.*;
import ch.interlis.ili2c.metamodel.TransferDescription;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

/** Implementiert die Konvertierungsfunktion.
 * @author ce
 * @version $Revision: 1.0 $ $Date: 15.12.2006 $
 */
public class ConvertFile {
	/** Input-Stream.
	 */
	private IoxReader ioxReader=null;
	/** Output-Stream.
	 */
	private IoxWriter ioxWriter=null;
	private Converter converter=null;
	/** Hauptfunktion.
	 * Liest aus inFile und schreibt in outFile.
	 * @param inFile Name der Eingabedatei (INTERLIS ITF).
	 * @param outFile Name der Ausgabedatei (INTERLIS ITF).
	 */
	public void doit(File inFile,File outFile,TransferDescription td)
	{
		converter=new Converter(td);
		try{
			// open txt file
			try{
				ioxWriter=new ch.interlis.iom_j.itf.ItfWriter(outFile,td);
			}catch(IoxException ex){
				EhiLogger.logError(ex);
				throw new IllegalArgumentException("failed to open output file <"+outFile.getAbsolutePath()+">");
			}
			try{
				ioxReader=new ch.interlis.iom_j.itf.ItfReader(inFile);
				((ch.interlis.iom_j.itf.ItfReader)ioxReader).setModel(td);
			}catch(IoxException ex){
				EhiLogger.logError(ex);
				throw new IllegalArgumentException("failed to open input file <"+inFile.getAbsolutePath()+">");
			}
			try{
				// loop threw baskets
				IoxEvent event;
				while(true){
					event=ioxReader.read();
					//EhiLogger.debug("event "+event.getClass().getName());
					if(event instanceof ObjectEvent){
						// add default values
						converter.convert(((ObjectEvent)event).getIomObject());
					}else if(event instanceof StartBasketEvent){
						EhiLogger.logState(((StartBasketEvent)event).getType()+"...");
					}
					ioxWriter.write(event);
					if(event instanceof EndTransferEvent){
						break;
					}
				}
			}catch(IoxException ex){
				EhiLogger.logError(ex);
			}
		}finally{
			if(ioxWriter!=null){
				try{
					ioxWriter.close();
				}catch(IoxException ex){
					EhiLogger.logError(ex);
				}
				ioxWriter=null;
			}
			if(ioxReader!=null){
				try{
					ioxReader.close();
				}catch(IoxException ex){
					EhiLogger.logError(ex);
				}
				ioxReader=null;
			}
		}
		
	}
}
