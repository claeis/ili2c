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
package ch.interlis.ilirepository;

import java.util.ArrayList;
import java.util.Iterator;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;

/** represtens an ilimodels.xml file.
 */
public class IliFiles {
	private ArrayList<IliFile> filev=new ArrayList<IliFile>();
	/** access to list of IliFile.
	 */
	public Iterator<IliFile> iteratorFile()
	{
		return filev.iterator();
	}
	/** add a file to the list of files.
	 */
	public void addFile(IliFile file){
		filev.add(file);
	}
	/** gets the file that contains the given model.
	 * @param modelName name of model
	 * @param iliVersion iliVersion (e.g. 1.0) of searched model
	 * @return file with searched model or null if no such model in this list of files.
	 */
	public IliFile getFileWithModel(String modelName,double iliVersion)
	{
		Iterator<IliFile> filei=iteratorFile();
		while(filei.hasNext()){
			IliFile ilifile=(IliFile)filei.next();
			Iterator<IliModel> modeli=ilifile.iteratorModel();
			while(modeli.hasNext()){
				IliModel model=modeli.next();
				if(model.getName().equals(modelName) && model.getIliVersion()==iliVersion){				
					return ilifile;
				}
			}
		}
		return null;
	}
}
