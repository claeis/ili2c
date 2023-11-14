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

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxFactoryCollection;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxReader;
import ch.interlis.ili2c.metamodel.TransferDescription;

/**
 * @author ce
 * @version $Revision: 1.0 $ $Date: 28.03.2007 $
 */
public class ItfAddDefValueReader implements IoxReader {
	private IoxReader reader=null;
	private Converter converter=null;
	public ItfAddDefValueReader(IoxReader reader,TransferDescription td,boolean readEnumValAsItfCode)
	{
		this.reader=reader;
		converter=new Converter(td,readEnumValAsItfCode);
	}
	public void close() throws IoxException {
		if(reader!=null){
			reader.close();
			reader=null;
		}
	}
	public IoxEvent read() throws IoxException {
		IoxEvent event=reader.read();
		if(event!=null && event instanceof ObjectEvent){
			// add default values
			converter.convert(((ObjectEvent)event).getIomObject());
		}
		return event;
	}
	public IomObject createIomObject(String type, String oid) throws IoxException {
		return reader.createIomObject(type, oid);
	}
	public IoxFactoryCollection getFactory() throws IoxException {
		return reader.getFactory();
	}
	public void setFactory(IoxFactoryCollection factory) throws IoxException {
		reader.setFactory(factory);
	}

}
