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

import java.io.*;
import ch.ehi.basics.tools.TopoSort;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.parser.Ili2ModelScan;
import ch.interlis.ili2c.modelscan.*;
import ch.interlis.ili2c.config.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author ce
 * @version $Revision: 1.1.1.1 $ $Date: 2007-03-07 07:51:49 $
 */
public class ModelScan {

	public static void main(String[] args) {
		EhiLogger.getInstance().setTraceFiler(false);
		String dirName = args[0];
		String model = args[1];
		ArrayList models=new ArrayList();
		models.add(model);
		getConfig(dirName,models);
	}
	public static IliFile scanIliFile(File file){
		IliFile iliFile=new IliFile();
		String streamName=file.getAbsolutePath();
		iliFile.setFilename(file);
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			Ili2ModelScan.mergeFile( iliFile,stream);
			stream.close();
			return iliFile;
		} catch (java.io.FileNotFoundException fnfex) {
		  EhiLogger.logError(streamName + ":" + "There is no such file.");
		} catch (Exception ex) {
			EhiLogger.logError(ex);
		}
		return null;
	}
	public static HashSet scanIliFileDir(File dir,HashSet skipModels,HashSet newModels){
		HashSet ret=new HashSet();
		if(!dir.exists()){
			EhiLogger.logAdaption(dir.getAbsoluteFile()+" doesn't exist; ignored");
			return ret;
		}
		if(!dir.isDirectory()){
			EhiLogger.logAdaption(dir.getAbsoluteFile()+" isn't a directory; ignored");
			return ret;
		}
		File filev[]=dir.listFiles(new ch.ehi.basics.view.GenericFileFilter("INTERLIS models (*.ili)","ili"));
		for(int i=0;i<filev.length;i++){
			if(filev[i].isDirectory()){
				continue;
			}
			IliFile iliFile=scanIliFile(filev[i]);
			if(iliFile!=null){
				boolean skipThisFile=false;
				for(Iterator modeli=iliFile.iteratorModel();modeli.hasNext();){
					IliModel model=(IliModel)modeli.next();
					if(skipModels.contains(model.getName()) || newModels.contains(model.getName())){
						skipThisFile=true;
						break;
					}
				}
				if(skipThisFile){
					EhiLogger.logAdaption("duplicate model; file ignored "+iliFile.getFilename());
				}else{
					ret.add(iliFile);
					for(Iterator modeli=iliFile.iteratorModel();modeli.hasNext();){
						IliModel model=(IliModel)modeli.next();
						newModels.add(model.getName());
					}
				}
			}
		}
		return ret;
	}
	public static Configuration getConfig(ArrayList ilipathv,ArrayList requiredModels){
		HashSet ilifiles=new HashSet();
		HashSet availablemodels=new HashSet();
		for(int i=0;i<ilipathv.size();i++){
			HashSet newmodels=new HashSet();
			ilifiles.addAll(scanIliFileDir(new File((String)ilipathv.get(i)),availablemodels,newmodels));
			availablemodels.addAll(newmodels);
		}
		// build map of modelname -> ilifile
		HashMap models=new HashMap();
		Iterator it=ilifiles.iterator();
		while(it.hasNext()){
			IliFile ilifile=(IliFile)it.next();
			Iterator modeli=ilifile.iteratorModel();
			while(modeli.hasNext()){
				IliModel model=(IliModel)modeli.next();
				models.put(model.getName(),ilifile);				
			}
		}
		HashSet toVisitFiles=new HashSet();
		it=requiredModels.iterator();
		boolean err=false;
		while(it.hasNext()){
			String model=(String)it.next();
			if(model==null){
				continue;
			}
			IliFile file=(IliFile)models.get(model);
			if(file==null){
				EhiLogger.logError(model+": model not found");
				err=true;
			}else{
				toVisitFiles.add(file);
			}
		}	
		if(err){
			return null;
		}
		return createConfig(toVisitFiles,models);
	}
	public static Configuration getConfig(String ilipaths,ArrayList requiredModels){
		ArrayList ilipathv = new ArrayList(java.util.Arrays.asList(ilipaths.split(";")));
		return getConfig(ilipathv,requiredModels);
	}
	/** create compile configuration, given a set of ilifilenames and a set of paths with additional ilifiles
	 * @param ilipaths list<String dirName> 		
	 * String ilidirv[]=ilipaths.split(";");
	 * @param requiredIliFiles list<String iliFilename>
	 * @return
	 */
	public static Configuration getConfigWithFiles(ArrayList ilipaths,ArrayList requiredIliFiles){
		HashSet ilifiles=new HashSet();
		HashSet toVisitFiles=new HashSet();
		Iterator reqFileIt=requiredIliFiles.iterator();
		HashSet availablemodels=new HashSet();
		while(reqFileIt.hasNext()){
			String fname=(String)reqFileIt.next();
			IliFile iliFile=scanIliFile(new File(fname));
			if(iliFile!=null){
				boolean skipThisFile=false;
				for(Iterator modeli=iliFile.iteratorModel();modeli.hasNext();){
					IliModel model=(IliModel)modeli.next();
					if(availablemodels.contains(model.getName())){
						skipThisFile=true;
						break;
					}
				}
				if(skipThisFile){
					EhiLogger.logAdaption("duplicate model; file ignored "+iliFile.getFilename());
				}else{
					ilifiles.add(iliFile);
					toVisitFiles.add(iliFile);
					for(Iterator modeli=iliFile.iteratorModel();modeli.hasNext();){
						IliModel model=(IliModel)modeli.next();
						availablemodels.add(model.getName());
					}
				}
			}
		}
		for(Iterator i=ilipaths.iterator();i.hasNext();){
			HashSet newmodels=new HashSet();
			HashSet set=scanIliFileDir(new File((String)i.next()),availablemodels,newmodels);
			if(set!=null && !set.isEmpty()){
				ilifiles.addAll(set);
				availablemodels.addAll(newmodels);
			}
		}

		// build map of modelname -> ilifile
		HashMap models=new HashMap();
		Iterator it=ilifiles.iterator();
		while(it.hasNext()){
			IliFile ilifile=(IliFile)it.next();
			Iterator modeli=ilifile.iteratorModel();
			while(modeli.hasNext()){
				IliModel model=(IliModel)modeli.next();
				models.put(model.getName(),ilifile);				
			}
		}
		return createConfig(toVisitFiles,models);
	}
	/**
	 * 
	 * @param toVisitFiles set<IliFile iliFile>
	 * @param models map<String modelName,IliFile iliFile>
	 * @return
	 */
	private static Configuration createConfig(HashSet toVisitFiles,HashMap models)
	{	
		if(toVisitFiles.isEmpty()){
			throw new IllegalStateException("toVisitFiles.isEmpty()");
		}
		HashSet visitedFiles=new HashSet();
		TopoSort reqFiles=new TopoSort();
		boolean modelsIncomplete=false;
		while(!toVisitFiles.isEmpty()){
			IliFile ilifile=(IliFile)toVisitFiles.iterator().next();
			reqFiles.add(ilifile);

			Iterator modeli=ilifile.iteratorModel();
			while(modeli.hasNext()){
				IliModel model=(IliModel)modeli.next();
				Iterator depi=model.getDependencies().iterator();
				while(depi.hasNext()){
					String dep=(String)depi.next();
					IliFile supplier=(IliFile)models.get(dep);
					if(supplier==null){
						EhiLogger.logError("missing model "+dep);
						modelsIncomplete=true;
					}else{
						if(!visitedFiles.contains(supplier)){
							// add file with supplier model
							toVisitFiles.add(supplier);
						}
						if(supplier!=ilifile){
							reqFiles.addcond(supplier,ilifile);
						}
					}
				}
			}
			toVisitFiles.remove(ilifile);
			visitedFiles.add(ilifile);
		}
		if(modelsIncomplete){
			return null;
		}
		if(!reqFiles.sort()){
			StringBuffer loopele=new StringBuffer();
			Iterator resi=reqFiles.getResult().iterator();
			String sep="";
			while(resi.hasNext()){
			  IliFile res=(IliFile)resi.next();
			  loopele.append(sep);
			  loopele.append(res.getFilename().getName());
			  sep="->";
			}
			EhiLogger.logError("loop in ili-files: "+loopele.toString());
			return null;
		}
		Iterator resi=reqFiles.getResult().iterator();
		Configuration config=new Configuration();
		while(resi.hasNext()){
		  IliFile res=(IliFile)resi.next();
		  File iliFile=res.getFilename();
		  EhiLogger.debug(iliFile.getAbsolutePath());
		  config.addFileEntry(new ch.interlis.ili2c.config.FileEntry(
			  iliFile.getAbsolutePath(),ch.interlis.ili2c.config.FileEntryKind.ILIMODELFILE));
		}
		return config;
	}
}
