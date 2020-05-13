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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.tools.TopoSort;
import ch.ehi.basics.view.GenericFileFilter;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ili2c.ModelScan;
import ch.interlis.ilirepository.impl.RepositoryCrawler;
import ch.interlis.ilirepository.impl.RepositoryVisitor;
import ch.interlis.ilirepository.impl.DataFinder;
import ch.interlis.ilirepository.impl.RepositoryAccess;
import ch.interlis.ilirepository.impl.RepositoryAccessException;

/** Enables cached access to a web of model repositories.
 * Usage:
 * <pre><code>
 * import ch.interlis.ili2c.Ili2c;
 * import ch.interlis.ili2c.config.Configuration;
 * import ch.interlis.ili2c.metamodel.TransferDescription;
 * // create repository manager
 * IliManager manager=new IliManager();
 * // set list of repositories to search
 * String repositories[]=new String[]{"http://models.interlis.ch/"};
 * manager.setRepositories(repositories);
 * // get list of ili-files for a given model
 * ArrayList modelNames=new ArrayList();
 * modelNames.add("DM01");
 * Configuration config=manager.getConfig(modelNames,1.0); // 1.0=ili 1 modell
 * // compile models
 * TransferDescritpion td=Ili2c.runCompiler(config);
 * </code></pre>
 * @author ceis
 */
public class IliManager implements ReposManager {
	/** name of ilisite.xml file.
	 */
	public static final String ILISITE_XML = "ilisite.xml";
	/** name of ilimodels.xml file.
	 */
	public static final String ILIMODELS_XML = "ilimodels.xml";
    /** name of ilidata.xml file.
     */
    public static final String ILIDATA_XML = "ilidata.xml";
    private long iliMaxTTL=604800000L; // max time (7 days) to live in ms for a file in the cache
    private long dataMaxTTL=43200000L; // max time (24h) to live in ms for a file in the cache
	private RepositoryAccess rep=new RepositoryAccess();
	private RepositoryCrawler iliCrawler=new RepositoryCrawler(rep);
	private DataFinder dataFinder=new DataFinder();
    private RepositoryVisitor dataCrawler=new RepositoryVisitor(rep,dataFinder);
	/** sets the list of repositories to start search for models.
	 */
	public void setRepositories(String uriv[]){
		for(String uri:uriv){
			EhiLogger.traceState("uri <"+uri+">");
		}
		iliCrawler.setRepositories(uriv);
        dataCrawler.setRepositories(uriv);
	}
	public void setIliFiles(String uri,IliFiles iliFiles){
		if(uri!=null && iliFiles!=null){
			EhiLogger.traceState("uri <"+uri+">");
			rep.setIliFiles(uri, iliFiles);
		}
	}
	public IliResolver getResolver() {
		return rep.getResolver();
	}
	public void setResolver(IliResolver resolver) {
		rep.setResolver(resolver);
	}
	/** sets the folder used to cache files from remote repositories.
	 */
	public void setCache(File localdir){
		rep.setCache(localdir);
	}
	/** creates a compiler configuration, given a set of local ili-filepaths.
	 * @param requiredIliFileNames list<String iliFilename> list of local filenames
	 */
	public Configuration getConfigWithFiles(ArrayList<String> requiredIliFileNames)
	throws Ili2cException
	{
		return getConfigWithFiles(requiredIliFileNames,null);
	}
    public Configuration getConfigWithFiles(ArrayList<String> requiredIliFileNames,Ili2cMetaAttrs metaAttrs)
    throws Ili2cException
    {
        return getConfigWithFiles(requiredIliFileNames,metaAttrs,0.0);
    }
	public Configuration getConfigWithFiles(ArrayList<String> requiredIliFileNames,Ili2cMetaAttrs metaAttrs,double version)
	throws Ili2cException
	{
		if(requiredIliFileNames.isEmpty()){
			throw new Ili2cException("no ili files given");
		}
		HashSet<IliFile> ilifiles=new HashSet<IliFile>();
		HashSet<IliFile> toVisitFiles=new HashSet<IliFile>(); // set<IliFile>
		//HashSet<File> requiredFiles=new HashSet<File>(); // set<File>
		
		// scan given files and report about duplicate models
		Iterator<String> reqFileIt=requiredIliFileNames.iterator();
		HashSet<String> availablemodels=new HashSet<String>();
		while(reqFileIt.hasNext()){
			String fname=reqFileIt.next();
			if(GenericFileFilter.getFileExtension(fname)!=null){
				File file=new File(fname);
				if(!file.exists()){
					throw new Ili2cException(fname + ": " + "There is no such file.");
				}
				IliFile iliFile=ModelScan.scanIliFile(file);
				if(iliFile==null){
					throw new Ili2cException(fname + ": " + "Failed to scan ili-file.");
				}
				if(iliFile!=null){
					boolean skipThisFile=false;
					for(Iterator<IliModel> modeli=iliFile.iteratorModel();modeli.hasNext();){
						IliModel model=modeli.next();
						if(version==0.0){
							version=model.getIliVersion();
						}else{
							if(version!=model.getIliVersion()){
								skipThisFile=true;
								EhiLogger.logAdaption("different ili version; file ignored "+iliFile.getFilename());
								break;
							}
						}
						if(availablemodels.contains(model.getName())){
							skipThisFile=true;
							EhiLogger.logAdaption("duplicate model; file ignored "+iliFile.getFilename());
							break;
						}
						if(metaAttrs!=null){
							String translationOf=metaAttrs.getMetaAttrValue(model.getName(), Ili2cMetaAttrs.ILI2C_TRANSLATION_OF);
							if(translationOf!=null){
								model.addDepenedency(translationOf);
							}
						}
					}
					if(!skipThisFile){
						//requiredFiles.add(file);
						ilifiles.add(iliFile);
						toVisitFiles.add(iliFile);
						for(Iterator<IliModel> modeli=iliFile.iteratorModel();modeli.hasNext();){
							IliModel model=modeli.next();
							availablemodels.add(model.getName());
						}
					}
				}
			}else{
				// just modelname given
				if(version==0){
					// first model
					String firstModel=fname;
					IliModel model=null;
					IliFile ilifile=iliCrawler.getIliFileMetadataDeep(firstModel,0.0,true);
					if(ilifile!=null){
						Iterator<IliModel> modeli=ilifile.iteratorModel();
						while(modeli.hasNext()){
							IliModel model1=modeli.next();
							if(model1.getName().equals(firstModel)){				
								version=model1.getIliVersion();
								model=model1;
								ilifiles.add(ilifile);
								toVisitFiles.add(ilifile);
								for(Iterator<IliModel> modeli2=ilifile.iteratorModel();modeli2.hasNext();){
									IliModel modelEle=modeli2.next();
									availablemodels.add(modelEle.getName());
								}
								if(metaAttrs!=null){
									String translationOf=metaAttrs.getMetaAttrValue(model.getName(), Ili2cMetaAttrs.ILI2C_TRANSLATION_OF);
									if(translationOf!=null){
										model.addDepenedency(translationOf);
									}
								}
								break;
							}
						}
					}
					if(model==null){				
						// could not find model
						throw new Ili2cException(firstModel+": model not found");
					}
					
				}else{
					String modelname=fname;
					if(!availablemodels.contains(modelname)){
						IliModel model=null;
						IliFile ilifile=iliCrawler.getIliFileMetadataDeep(modelname,version,true);
						if(ilifile!=null){
							Iterator<IliModel> modeli=ilifile.iteratorModel();
							while(modeli.hasNext()){
								IliModel model1=modeli.next();
								if(model1.getName().equals(modelname)){				
									if(version!=model1.getIliVersion()){
										throw new Ili2cException(modelname+": unexpected version "+model1.getIliVersion()+" found");
									}
									model=model1;
									ilifiles.add(ilifile);
									toVisitFiles.add(ilifile);
									for(Iterator<IliModel> modeli2=ilifile.iteratorModel();modeli2.hasNext();){
										IliModel modelEle=modeli2.next();
										availablemodels.add(modelEle.getName());
									}
									if(metaAttrs!=null){
										String translationOf=metaAttrs.getMetaAttrValue(model.getName(), Ili2cMetaAttrs.ILI2C_TRANSLATION_OF);
										if(translationOf!=null){
											model.addDepenedency(translationOf);
										}
									}
									break;
								}
							}
						}
						if(model==null){				
							// could not find model
							throw new Ili2cException(modelname+": model not found");
						}
						
					}
				}
				
			}
		}

		return createConfig(toVisitFiles,version);
	}
	/** creates a compiler configuration, given a set of model names and ili-version.
	 * @param requiredModels list<String modelname> list of model names
	 * @param iliVersion ili version (e.g. 1.0) or 0.0 for auto-detection.
	 * @return compiler configuration
	 * @throws Ili2cException
	 */
	public Configuration getConfig(ArrayList<String> requiredModels,double iliVersion)
	throws Ili2cException
	{
		HashSet<IliFile> toVisitFiles=new HashSet<IliFile>();
		if(requiredModels==null || requiredModels.size()==0) {
            throw new Ili2cException("no models given to look for");
		}
		// auto determine version?
		if(iliVersion==0.0){
			// get version of first model
			String firstModel=requiredModels.get(0);
			IliModel model=null;
			IliFile ilifile=iliCrawler.getIliFileMetadataDeep(firstModel,0.0,true);
			if(ilifile!=null){
				Iterator<IliModel> modeli=ilifile.iteratorModel();
				while(modeli.hasNext()){
					IliModel model1=modeli.next();
					if(model1.getName().equals(firstModel)){				
						iliVersion=model1.getIliVersion();
						model=model1;
						toVisitFiles.add(ilifile);
						break;
					}
				}
			}
			if(model==null){				
				// could not find model
				throw new Ili2cException(firstModel+": model not found");
			}
		}
		
		
		// build list of files with required models
		Iterator<String> it=requiredModels.iterator();
		HashSet<String> missingModels=new HashSet<String>();
		StringBuffer errmsg=new StringBuffer();
		String sep="";
		while(it.hasNext()){
			String model=it.next();
			if(model==null){
				continue;
			}
			IliFile file=getFromSet(toVisitFiles,model,iliVersion);
			if(file==null){
				file=iliCrawler.getIliFileMetadataDeep(model,iliVersion,true);
				if(file==null){
					if(!missingModels.contains(model)){
						missingModels.add(model);
						errmsg.append(sep);
						errmsg.append(model);
						sep=", ";
					}
				}else{
					toVisitFiles.add(file);
				}
			}
		}	
		if(!missingModels.isEmpty()){
			errmsg.append(": model(s) not found");
			throw new Ili2cException(errmsg.toString());
		}
		
		// build config
		return createConfig(toVisitFiles,iliVersion);
	}
	/**
	 * 
	 * @param toVisitFiles set<IliFile iliFile>
	 * @return
	 */
	private Configuration createConfig(HashSet<IliFile> toVisitFiles,double iliVersion)
	throws Ili2cException
	{	
		if(toVisitFiles.isEmpty()){
			throw new IllegalStateException("toVisitFiles.isEmpty()");
		}
		HashSet<IliFile> visitedFiles=new HashSet<IliFile>();
		TopoSort reqFiles=new TopoSort();
		HashSet<String> missingModels=new HashSet<String>();
		StringBuffer errmsg=new StringBuffer();
		String sep="";
		while(!toVisitFiles.isEmpty()){
			IliFile ilifile=toVisitFiles.iterator().next();
			reqFiles.add(ilifile);

			Iterator<IliModel> modeli=ilifile.iteratorModel();
			while(modeli.hasNext()){
				IliModel model=modeli.next();
				Iterator<String> depi=model.getDependencies().iterator();
				while(depi.hasNext()){
					String dep=depi.next();
					if(dep.equals("INTERLIS")){
						continue;
					}
					//EhiLogger.debug("model "+model.getName()+", dep "+dep);
					IliFile supplier=getFromSet(visitedFiles,dep, iliVersion);
					if(supplier==null){
						supplier=getFromSet(toVisitFiles,dep, iliVersion);
					}
					if(supplier==null){
						supplier=iliCrawler.getIliFileMetadataDeep(dep,iliVersion,true);
					}
					if(supplier==null){
						if(!missingModels.contains(dep)){
							errmsg.append(sep);
							errmsg.append(dep);
							sep=", ";
							missingModels.add(dep);
						}
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
		if(!missingModels.isEmpty()){
			errmsg.append(": model(s) not found");
			throw new Ili2cException(errmsg.toString());
		}
		if(!reqFiles.sort()){
			StringBuffer loopele=new StringBuffer();
			Iterator<IliFile> resi=reqFiles.getResult().iterator();
			sep="";
			while(resi.hasNext()){
			  IliFile res=resi.next();
			  loopele.append(sep);
			  loopele.append(res.getFilename().getName());
			  sep="->";
			}
			throw new Ili2cException("loop in ili-files: "+loopele.toString());
		}
		Iterator<IliFile> resi=reqFiles.getResult().iterator();
		Configuration config=new Configuration();
		while(resi.hasNext()){
		  IliFile res=resi.next();
		  // fetch file from repository to cache
		  File iliFile=null;
			try {
				iliFile = rep.getLocalFileLocation(res.getRepositoryUri(),res.getPath(),iliMaxTTL,res.getMd5());
				if(iliFile==null){
					if(res.getRepositoryUri()==null){
						throw new Ili2cException("failed to get ili file "+res.getPath());
					}
					throw new Ili2cException("failed to get ili file "+res.getRepositoryUri()+"/"+res.getPath());
				}
			} catch (RepositoryAccessException e) {
				if(res.getRepositoryUri()==null){
					throw new Ili2cException("failed to get ili file "+res.getPath(),e);
				}
				throw new Ili2cException("failed to get ili file "+res.getRepositoryUri()+"/"+res.getPath(),e);
			}
		  config.addFileEntry(new ch.interlis.ili2c.config.FileEntry(
				  iliFile.getPath(),ch.interlis.ili2c.config.FileEntryKind.ILIMODELFILE));
		}
		return config;
	}
	private IliFile getFromSet(HashSet<IliFile> ilifiles,String modelName,double iliVersion)
	{
		Iterator<IliFile> filei=ilifiles.iterator();
		while(filei.hasNext()){
			IliFile ilifile=filei.next();
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
	/* (non-Javadoc)
     * @see ch.interlis.ilirepository.ReposManager#getDatasetIndex(java.lang.String, java.lang.String[])
     */
	public List<Dataset> getDatasetIndex(String bid,String topics[]) throws RepositoryAccessException {
	    dataFinder.setCriteria(bid,topics);
	    dataCrawler.visitRepositories();
	    return dataFinder.getResult();
	}
	/* (non-Javadoc)
     * @see ch.interlis.ilirepository.ReposManager#getLocalFileOfRemoteDataset(ch.interlis.ilirepository.Dataset, java.lang.String)
     */
	public File[] getLocalFileOfRemoteDataset(Dataset dataset,String fileformat) throws Ili2cException {

	    ch.interlis.models.DatasetIdx16.DataFile fileMetadata=null;
	    for(ch.interlis.models.DatasetIdx16.DataFile file:dataset.getMetadata().getfiles()){
	        if(fileformat.equals(file.getfileFormat())) {
	            fileMetadata=file;
	        }
	    }
	    if(fileMetadata==null) {
            throw new Ili2cException("no format <"+fileformat+"> of dataset <"+dataset.getMetadata().getid()+">");
	    }
        ch.interlis.models.DatasetIdx16.File paths[]=fileMetadata.getfile();
        File iliFile[]=new File[paths.length];
	    for(int fileidx=0;fileidx<paths.length;fileidx++) {
	        String path=paths[fileidx].getpath();
	        // fetch file from repository to cache
	          try {
	              iliFile[fileidx] = rep.getLocalFileLocation(dataset.getUri(),path,dataMaxTTL,paths[fileidx].getmd5());
	              if(iliFile[fileidx]==null){
	                  if(dataset.getUri()==null){
	                      throw new Ili2cException("failed to get data file "+path);
	                  }
	                  throw new Ili2cException("failed to get data file "+dataset.getUri()+"/"+path);
	              }
	          } catch (RepositoryAccessException e) {
	              if(dataset.getUri()==null){
	                  throw new Ili2cException("failed to get data file "+path,e);
	              }
	              throw new Ili2cException("failed to get data file "+dataset.getUri()+"/"+path,e);
	          }
	    }
        return iliFile;
	}
	static public void main(String args[])
	{
		EhiLogger.getInstance().setTraceFilter(false);
		IliManager m=new IliManager();
		m.setRepositories(new String[]{"C:/tmp/test23","http://models.interlis.ch"});
		ArrayList<String> requiredModels=new ArrayList<String>();
		ArrayList<File> requiredFiles=new ArrayList<File>();
		requiredModels.add("CatalogueObjects_V1");
		//requiredModels.add("Test23_erweitert");
		try {
			Configuration config=m.getConfig(requiredModels,0.0);
			if(config!=null){
				ch.interlis.ili2c.Ili2c.logIliFiles(config);
			}
		} catch (Ili2cException e) {
			EhiLogger.logError(e);
		}
		
	}
}

