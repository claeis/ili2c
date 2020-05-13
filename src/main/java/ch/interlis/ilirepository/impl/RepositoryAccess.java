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
package ch.interlis.ilirepository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliResolver;
import ch.interlis.ilirepository.IliSite;
import ch.interlis.ilirepository.IliManager;
import ch.interlis.iox.IoxException;
import ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata;

/** Handles access to a file in a repository.
 * Hides the caching, downloading of files.
 * @author ceis
 */
public class RepositoryAccess {
    public static final String ILI_CACHE = "ILI_CACHE";
    private HashMap<String,IliFiles> reposIliFiles=new HashMap<String,IliFiles>();
    private HashMap<String,List<DatasetMetadata>> reposIliData=new HashMap<String,List<DatasetMetadata>>();
	private HashMap<String,IliSite> reposIliSite=new HashMap<String,IliSite>();
	private long metaMaxTTL=86400000L; // max time to live in ms for a file in the cache
	private File localCache=null;
	private IliResolver resolver=null;
    public RepositoryAccess() {
        String userDefinedCacheFolder=System.getenv(ILI_CACHE);
        if(userDefinedCacheFolder!=null && userDefinedCacheFolder.trim().length()>0) {
            localCache=new File(userDefinedCacheFolder);
        }else {
            localCache=new File(System.getProperty("user.home"),".ilicache");
        }
    }
	
	public IliResolver getResolver() {
		return resolver;
	}
	public void setResolver(IliResolver resolver) {
		this.resolver = resolver;
	}

	/** Sets the local folder to be used for caching remote files.
	 */
	public void setCache(File localdir){
		localCache=localdir;
	}
	/** Sets the list of ili-files for a given repository.
	 * @param uri local or remote repository
	 * @param iliFiles list of ili-files or null if no such repository or offline.
	 */
	public void setIliFiles(String uri,IliFiles iliFiles)
	{
		reposIliFiles.put(uri,iliFiles);
	}
	/** Gets the list of ili-files for a given repository.
	 * @param uri local or remote repository
	 * @return list of ili-files or null if no such repository or offline.
	 */
	public IliFiles getIliFiles(String uri)
	{
		if(!reposIliFiles.containsKey(uri)){
			if(isLegacyDir(uri)){
				// scan directory
				try {
					EhiLogger.traceState("scan ili-files in folder <"+uri+">...");
					HashSet<IliFile> files=ch.interlis.ili2c.ModelScan.scanIliFileDir(new File(uri), null);
					Iterator<IliFile> filei=files.iterator();
					IliFiles iliFiles=new IliFiles();
					while(filei.hasNext()){
						iliFiles.addFile((IliFile)filei.next());
					}
					reposIliFiles.put(uri, iliFiles);
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}else{
				IliFiles iliFiles;
				try {
					EhiLogger.traceState("read "+IliManager.ILIMODELS_XML+" from <"+uri+">...");
					iliFiles = readIlimodelsXmlAsIliFiles(uri);
				} catch (RepositoryAccessException e) {
					handleRepositoryAccessException(e,uri);
					iliFiles=null;
				}
				reposIliFiles.put(uri, iliFiles);
			}
		}
		IliFiles iliFiles=reposIliFiles.get(uri);
		return iliFiles;
	}
	@Deprecated
    public IliFiles getModelMetadata(String uri)
    {
        return getIliFiles(uri);
    }
    public List<DatasetMetadata> getIliData(String uri)
    {
        if(!reposIliData.containsKey(uri)){
            List<DatasetMetadata> iliFiles;
            try {
                EhiLogger.traceState("read "+IliManager.ILIDATA_XML+" from <"+uri+">...");
                iliFiles = readIliDataXmlLatest(uri);
            } catch (RepositoryAccessException e) {
                handleRepositoryAccessException(e,uri);
                iliFiles=null;
            }
            reposIliData.put(uri, iliFiles);
        }
        List<DatasetMetadata> iliFiles=reposIliData.get(uri);
        return iliFiles;
    }
	/** Gets the site metadata for a given repository.
	 * @param uri local or remote repository
	 * @return site metadata or null if no such repository or offline.
	 */
	public IliSite getIliSite(String uri) 
	{
		if(!reposIliSite.containsKey(uri)){
			IliSite iliSite;
			try {
				EhiLogger.traceState("read "+IliManager.ILISITE_XML+" from <"+uri+">...");
				iliSite = readIliSiteXml(uri);
			} catch (RepositoryAccessException e) {
				handleRepositoryAccessException(e,uri);
				iliSite=null;
			}
			reposIliSite.put(uri, iliSite);
		}
		IliSite iliSite=reposIliSite.get(uri);
		return iliSite;
	}
	
	private void handleRepositoryAccessException(RepositoryAccessException e,String uri)
	{
		if(reposIliSite.containsKey(uri) && reposIliFiles.containsKey(uri)){
			return;
		}
		EhiLogger.logAdaption("repository <"+uri+"> ignored; "+toString(e));
		if(!reposIliSite.containsKey(uri)){
			reposIliSite.put(uri, null);
		}
		if(reposIliFiles.containsKey(uri)){
			reposIliFiles.put(uri, null);
		}
	}
	public static String toString(Throwable ex)
	{
		StringBuilder ret=new StringBuilder();
		String msg=ex.getLocalizedMessage();
		if(msg!=null){
			msg=msg.trim();
			if(msg.length()==0){
				msg=null;
			}
		}
		if(msg==null){
			msg=ex.getClass().getName();
		}
		ret.append(msg);
		Throwable ex2=ex.getCause();
		if(ex2!=null){
			ret.append("; ");
			ret.append(toString(ex2));
		}
		if(ex instanceof java.sql.SQLException){
			java.sql.SQLException exTarget=(java.sql.SQLException)ex;
			java.sql.SQLException exTarget2=exTarget.getNextException();
			if(exTarget2!=null){
				ret.append("; ");
				ret.append(toString(exTarget2));
			}
		}
		if(ex instanceof InvocationTargetException){
			InvocationTargetException exTarget=(InvocationTargetException)ex;
			Throwable exTarget2=exTarget.getTargetException();
			if(exTarget2!=null){
				ret.append("; ");
				ret.append(toString(exTarget2));
			}
		}
		return ret.toString();
	}
	private boolean isLegacyDir(String uri)
	{
		String urilc=uri.toLowerCase();
		if(urilc.startsWith("http:") || urilc.startsWith("https:")){
			return false;
		}
		File file=new File(uri,IliManager.ILIMODELS_XML);
		if(file.exists()){
			return false;
		}
		return true;
	}
	@Deprecated
	public static IliFiles createIliFiles(String uri,java.util.Collection<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelv)
	throws RepositoryAccessException
	{
		
		// read "file"
		
		// build map of model-names to model-metadata
		HashMap<String,ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>> models=new HashMap<String,ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>>(); // map<String modelName,array<ModelMetadata>>
		for(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model:modelv){
			 String name=model.getName();
			 if(!models.containsKey(name)){
				 models.put(name,new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>());
			 }
			 ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> versions=models.get(name);
			 versions.add(model);
		}

		IliFiles ret=new IliFiles();
		HashMap<String,IliFile> files=new HashMap<String,IliFile>();
		Iterator<String> modeli=models.keySet().iterator();
		while(modeli.hasNext()){
			String name=modeli.next();
			ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions=models.get(name);
			if(modelVersions==null){
				continue;
			}
			ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage cslv[]=new ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage[]{ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili1,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_2,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_3};  
			for(int csli=0;csli<cslv.length;csli++){
			    ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model=getCslVersion(modelVersions,cslv[csli]);
				if(model==null){
					continue;
				}
				String filename=model.getFile();
				IliFile ilifile=null;
				if(!files.containsKey(filename)){
					ilifile=new IliFile();
					ilifile.setPath(filename);
					ilifile.setRepositoryUri(uri);
					files.put(filename, ilifile);
					ret.addFile(ilifile);
				}else{
					ilifile=files.get(filename);
				}
				IliModel iliModel=new IliModel();
				ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl=model.getSchemaLanguage();
				if(csl==ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili1){
					iliModel.setIliVersion(1.0);
				}else if(csl==ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_2){
					iliModel.setIliVersion(2.2);
				}else if(csl==ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_3){
					iliModel.setIliVersion(2.3);
				}
				iliModel.setName(model.getName());
				ch.ehi.iox.ilisite.IliRepository09.ModelName_ deps[]=model.getdependsOnModel();
				for(int depi=0;depi<deps.length;depi++){
					String depName=ch.ehi.basics.tools.StringUtility.purge(deps[depi].getvalue());
					// empty? (e.g. because of invalid xml)
					if(depName!=null){
						//EhiLogger.debug("depName "+depName);
						iliModel.addDepenedency(depName);
					}
				}
				ilifile.addModel(iliModel);
			}
		}
		return ret;
	}
    public static IliFiles createIliFiles2(String uri,java.util.Collection<ModelMetadata> modelv)
    throws RepositoryAccessException
    {
        
        // read "file"
        
        // build map of model-names to model-metadata
        HashMap<String,ArrayList<ModelMetadata>> models=new HashMap<String,ArrayList<ModelMetadata>>(); // map<String modelName,array<ModelMetadata>>
        for(ModelMetadata model:modelv){
             String name=model.getName();
             if(!models.containsKey(name)){
                 models.put(name,new ArrayList<ModelMetadata>());
             }
             ArrayList<ModelMetadata> versions=models.get(name);
             versions.add(model);
        }

        IliFiles ret=new IliFiles();
        HashMap<String,IliFile> files=new HashMap<String,IliFile>();
        Iterator<String> modeli=models.keySet().iterator();
        while(modeli.hasNext()){
            String name=modeli.next();
            ArrayList<ModelMetadata> modelVersions=models.get(name);
            if(modelVersions==null){
                continue;
            }
            String cslv[]=ModelMetadata.all;  
            for(int csli=0;csli<cslv.length;csli++){
                ModelMetadata model=getCslVersion2(modelVersions,cslv[csli]);
                if(model==null){
                    continue;
                }
                String filename=model.getFile();
                IliFile ilifile=null;
                if(!files.containsKey(filename)){
                    ilifile=new IliFile();
                    ilifile.setPath(filename);
                    ilifile.setRepositoryUri(uri);
                    files.put(filename, ilifile);
                    ret.addFile(ilifile);
                }else{
                    ilifile=files.get(filename);
                }
                IliModel iliModel=new IliModel();
                String csl=model.getSchemaLanguage();
                if(csl.equals(ModelMetadata.ili1)){
                    iliModel.setIliVersion(1.0);
                }else if(csl.equals(ModelMetadata.ili2_2)){
                    iliModel.setIliVersion(2.2);
                }else if(csl.equals(ModelMetadata.ili2_3)){
                    iliModel.setIliVersion(2.3);
                }else if(csl.equals(ModelMetadata.ili2_4)){
                    iliModel.setIliVersion(2.4);
                }
                iliModel.setName(model.getName());
                String deps[]=model.getDependsOnModel();
                for(int depi=0;depi<deps.length;depi++){
                    String depName=ch.ehi.basics.tools.StringUtility.purge(deps[depi]);
                    // empty? (e.g. because of invalid xml)
                    if(depName!=null){
                        //EhiLogger.debug("depName "+depName);
                        iliModel.addDepenedency(depName);
                    }
                }
                ilifile.addModel(iliModel);
            }
        }
        return ret;
    }
	/** read an ilimodels.xml file
	 * 
	 * @param uri uri of the repository without ilimodels.xml
	 * @return null if the repository doesn't exist
	 */
	private IliFiles readIlimodelsXmlAsIliFiles(String uri)
	throws RepositoryAccessException
	{
		List<ModelMetadata> modelv =  readIlimodelsXml2(uri);
		if(modelv==null) {
		    return null;
		}
		modelv=getLatestVersions2(modelv);
		return createIliFiles2(uri, modelv);
	}
    @Deprecated
    public List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> readIlimodelsXml(String uri) throws RepositoryAccessException {
        File file=getLocalFileLocation(uri,IliManager.ILIMODELS_XML,metaMaxTTL,null);
		if(file==null){
			return null;
		}
		// read file
		List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelv = readIliModelsXml(file);
        return modelv;
    }
    public List<ModelMetadata> readIlimodelsXml2(String uri) throws RepositoryAccessException {
        File file=getLocalFileLocation(uri,IliManager.ILIMODELS_XML,metaMaxTTL,null);
        if(file==null){
            return null;
        }
        // read file
        List<ModelMetadata> modelv = readIliModelsXml2(file);
        return modelv;
    }

    public List<DatasetMetadata> readIliDataXmlLatest(String uri) throws RepositoryAccessException {
        List<DatasetMetadata> result=new ArrayList<DatasetMetadata>();
        List<DatasetMetadata> datav = readIliDataXml(uri);
        if(datav==null) {
            // no ilidata.xml in repository
            return result;
        }
        List<String> datasetIds=new ArrayList<String>();
        java.util.Map<String,List<DatasetMetadata>> versionsPerId=new HashMap<String,List<DatasetMetadata>>();  
        // group by datasetId
        for(DatasetMetadata data:datav) {
            String datasetId = data.getid();
            List<DatasetMetadata> versions=versionsPerId.get(datasetId);
            if(versions==null) {
                datasetIds.add(datasetId);
                versions=new ArrayList<DatasetMetadata>();
                versionsPerId.put(datasetId, versions);
            }
            versions.add(data);
        }
        // find latest version per datasetId
        for(String datasetId:datasetIds) {
            List<DatasetMetadata> versions=versionsPerId.get(datasetId);
            DatasetMetadata latest=getLatestVersion(versions);
            if(latest!=null) {
                result.add(latest);
            }
        }
        return result;
        
    }
    public List<DatasetMetadata> readIliDataXml(String uri) throws RepositoryAccessException {
        File file=getLocalFileLocation(uri,IliManager.ILIDATA_XML,metaMaxTTL,null);
        if(file==null){
            return null;
        }
        // read file
        List<DatasetMetadata> modelv = readIliDataXmlLocalFile(file);
        return modelv;
    }
    
	static private final String REGEX_ILI_NAME="[a-zA-Z]([a-zA-Z0-9_]*)";
    @Deprecated
	static public List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> readIliModelsXml(File file) 
	throws RepositoryAccessException
	{
		ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelv=new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>();
		ch.interlis.iom_j.xtf.XtfReader reader=null;
		try {
			reader=new ch.interlis.iom_j.xtf.XtfReader(file);
			reader.getFactory().registerFactory(ch.ehi.iox.ilisite.ILIREPOSITORY09.getIoxFactory());
            reader.getFactory().registerFactory(ch.interlis.models.ILIREPOSITORY20.getIoxFactory());
			ch.interlis.iox.IoxEvent event=null;
			do{
				 event=reader.read();
				 if(event instanceof ch.interlis.iox.ObjectEvent){
					 ch.interlis.iom.IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
                     if(iomObj instanceof ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata){
                         ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata model20=(ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata)iomObj;
                         iomObj=mapToIom09(mapFromIom20(model20));
                         if(iomObj==null) {
                             EhiLogger.logAdaption("TID="+model20.getobjectoid()+": ignored; failed to migrate");
                             continue;
                         }
                     }
					 if(iomObj instanceof ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata){
						 ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model=(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata)iomObj;
						 {
							 String mName=model.getName();
							 if(mName==null || mName.trim().length()==0){
								 EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no modelname");
								 continue;
							 }
							 if(!mName.matches(REGEX_ILI_NAME)){
								 EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; <"+mName+"> is not a valid modelname");
								 continue;
							 }
							 ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage mCsl=model.getSchemaLanguage();
							 if(mCsl==null){
								 EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no schemalanguage");
								 continue;
							 }
							 String mFilename=model.getFile();
							 if(mFilename==null || mFilename.trim().length()==0){
								 EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no filename");
								 continue;
							 }
						 }
						 modelv.add(model);
					 }else{
						 EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; unknown class <"+iomObj.getobjecttag()+">");
					 }
				 }
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
		} catch (IoxException e) {
			throw new RepositoryAccessException("failed to read "+IliManager.ILIMODELS_XML,e);
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IoxException e) {
					throw new RepositoryAccessException(e);
				}
				reader=null;
			}
		}
		return modelv;
	}
    static public List<ModelMetadata> readIliModelsXml2(File file) 
    throws RepositoryAccessException
    {
        ArrayList<ModelMetadata> modelv=new ArrayList<ModelMetadata>();
        ch.interlis.iom_j.xtf.XtfReader reader=null;
        try {
            reader=new ch.interlis.iom_j.xtf.XtfReader(file);
            reader.getFactory().registerFactory(ch.ehi.iox.ilisite.ILIREPOSITORY09.getIoxFactory());
            reader.getFactory().registerFactory(ch.interlis.models.ILIREPOSITORY20.getIoxFactory());
            ch.interlis.iox.IoxEvent event=null;
            do{
                 event=reader.read();
                 if(event instanceof ch.interlis.iox.ObjectEvent){
                     ch.interlis.iom.IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
                     ModelMetadata model=null;
                     if(iomObj instanceof ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata){
                         ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model09=(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata)iomObj;
                         model=mapFromIom09(model09);
                         if(model==null) {
                             EhiLogger.logAdaption("TID="+model09.getobjectoid()+": ignored; failed to map");
                             continue;
                         }
                     }else if(iomObj instanceof ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata) {
                         ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata model20=(ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata)iomObj;
                         model=mapFromIom20(model20);
                         if(model==null) {
                             EhiLogger.logAdaption("TID="+model20.getobjectoid()+": ignored; failed to map");
                             continue;
                         }
                     }else {
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; unknown class <"+iomObj.getobjecttag()+">");
                         continue;
                     }
                     String mName=model.getName();
                     if(mName==null || mName.trim().length()==0){
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no modelname");
                         continue;
                     }
                     if(!mName.matches(REGEX_ILI_NAME)){
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; <"+mName+"> is not a valid modelname");
                         continue;
                     }
                     String mCsl=model.getSchemaLanguage();
                     if(mCsl==null){
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no schemalanguage");
                         continue;
                     }
                     String mFilename=model.getFile();
                     if(mFilename==null || mFilename.trim().length()==0){
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; no filename");
                         continue;
                     }
                     modelv.add(model);
                 }
            }while(!(event instanceof ch.interlis.iox.EndTransferEvent));
        } catch (IoxException e) {
            throw new RepositoryAccessException("failed to read "+IliManager.ILIMODELS_XML,e);
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IoxException e) {
                    throw new RepositoryAccessException(e);
                }
                reader=null;
            }
        }
        return modelv;
    }
    public static ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata mapToIom20(ModelMetadata model) {
        if(model==null) {
            return null;
        }
        ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata model20=new ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata(model.getOid()); 
        final boolean browseOnly = model.isBrowseOnly();
        model20.setbrowseOnly(browseOnly);
        final String furtherInformation = model.getFurtherInformation();
        if(furtherInformation!=null)model20.setfurtherInformation(furtherInformation);
        final String furtherMetadata = model.getFurtherMetadata();
        if(furtherMetadata!=null)model20.setfurtherMetadata(furtherMetadata);
        final String file = model.getFile();
        if(file!=null)model20.setFile(file);
        final String issuer = model.getIssuer();
        if(issuer!=null)model20.setIssuer(issuer);
        final String md5 = model.getMd5();
        if(md5!=null)model20.setmd5(md5);
        final String name = model.getName();
        if(name!=null)model20.setName(name);
        final String original = model.getOriginal();
        if(original!=null)model20.setOriginal(original);
        final String precursorVersion = model.getPrecursorVersion();
        if(precursorVersion!=null)model20.setprecursorVersion(precursorVersion);
        final String publishingDate = model.getPublishingDate();
        if(publishingDate!=null)model20.setpublishingDate(publishingDate);
        String csl=model.getSchemaLanguage();
        if(csl!=null)model20.setSchemaLanguage(csl);
        final String shortDescription = model.getShortDescription();
        if(shortDescription!=null)model20.setshortDescription(shortDescription);
        final String tags = model.getTags();
        if(tags!=null)model20.setTags(tags);
        final String technicalContact = model.getTechnicalContact();
        if(technicalContact!=null)model20.settechnicalContact(technicalContact);
        final String title = model.getTitle();
        if(title!=null)model20.setTitle(title);
        final String version = model.getVersion();
        if(version!=null)model20.setVersion(version);
        final String versionComment = model.getVersionComment();
        if(versionComment!=null)model20.setVersionComment(versionComment);
        final String nameLanguage = model.getNameLanguage();
        if(nameLanguage!=null)model20.setNameLanguage(nameLanguage);
        for(String dep:model.getDependsOnModel()) {
            ch.interlis.models.IliRepository20.ModelName_ dep20=new ch.interlis.models.IliRepository20.ModelName_();
            dep20.setvalue(dep);
            model20.adddependsOnModel(dep20);
        }
        for(String dep:model.getDerivedModel()) {
            ch.interlis.models.IliRepository20.ModelName_ dep20=new ch.interlis.models.IliRepository20.ModelName_();
            dep20.setvalue(dep);
            model20.addderivedModel(dep20);
        }
        for(String dep:model.getFollowupModel()) {
            ch.interlis.models.IliRepository20.ModelName_ dep20=new ch.interlis.models.IliRepository20.ModelName_();
            dep20.setvalue(dep);
            model20.addfollowupModel(dep20);
        }
        for(String dep:model.getKnownPortal()) {
            ch.interlis.models.IliRepository20.WebSite_ dep20=new ch.interlis.models.IliRepository20.WebSite_();
            dep20.setvalue(dep);
            model20.addknownPortal(dep20);
        }
        for(String dep:model.getKnownWFS()) {
            ch.interlis.models.IliRepository20.WebService_ dep20=new ch.interlis.models.IliRepository20.WebService_();
            dep20.setvalue(dep);
            model20.addknownWFS(dep20);
        }
        for(String dep:model.getKnownWMS()) {
            ch.interlis.models.IliRepository20.WebService_ dep20=new ch.interlis.models.IliRepository20.WebService_();
            dep20.setvalue(dep);
            model20.addknownWMS(dep20);
        }
        return model20;
    }
    public static ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata mapToIom09(ModelMetadata model) {
        if(model==null) {
            return null;
        }
        ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model09=new ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata(model.getOid()); 
        final boolean browseOnly = model.isBrowseOnly();
        model09.setbrowseOnly(browseOnly);
        final String furtherInformation = model.getFurtherInformation();
        if(furtherInformation!=null)model09.setfurtherInformation(furtherInformation);
        final String furtherMetadata = model.getFurtherMetadata();
        if(furtherMetadata!=null)model09.setfurtherMetadata(furtherMetadata);
        final String file = model.getFile();
        if(file!=null)model09.setFile(file);
        final String issuer = model.getIssuer();
        if(issuer!=null)model09.setIssuer(issuer);
        final String md5 = model.getMd5();
        if(md5!=null)model09.setmd5(md5);
        final String name = model.getName();
        if(name!=null)model09.setName(name);
        final String original = model.getOriginal();
        if(original!=null)model09.setOriginal(original);
        final String precursorVersion = model.getPrecursorVersion();
        if(precursorVersion!=null)model09.setprecursorVersion(precursorVersion);
        final String publishingDate = model.getPublishingDate();
        if(publishingDate!=null)model09.setpublishingDate(publishingDate);
        ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl=ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.parseXmlCode(model.getSchemaLanguage());
        if(csl==null) {
            return null;
        }
        model09.setSchemaLanguage(csl);
        final String shortDescription = model.getShortDescription();
        if(shortDescription!=null)model09.setshortDescription(shortDescription);
        final String tags = model.getTags();
        if(tags!=null)model09.setTags(tags);
        final String technicalContact = model.getTechnicalContact();
        if(technicalContact!=null)model09.settechnicalContact(technicalContact);
        final String title = model.getTitle();
        if(title!=null)model09.setTitle(title);
        final String version = model.getVersion();
        if(version!=null)model09.setVersion(version);
        final String versionComment = model.getVersionComment();
        if(versionComment!=null)model09.setVersionComment(versionComment);
        for(String dep:model.getDependsOnModel()) {
            ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep09=new ch.ehi.iox.ilisite.IliRepository09.ModelName_();
            dep09.setvalue(dep);
            model09.adddependsOnModel(dep09);
        }
        for(String dep:model.getDerivedModel()) {
            ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep09=new ch.ehi.iox.ilisite.IliRepository09.ModelName_();
            dep09.setvalue(dep);
            model09.addderivedModel(dep09);
        }
        for(String dep:model.getFollowupModel()) {
            ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep09=new ch.ehi.iox.ilisite.IliRepository09.ModelName_();
            dep09.setvalue(dep);
            model09.addfollowupModel(dep09);
        }
        for(String dep:model.getKnownPortal()) {
            ch.ehi.iox.ilisite.IliRepository09.WebSite_ dep09=new ch.ehi.iox.ilisite.IliRepository09.WebSite_();
            dep09.setvalue(dep);
            model09.addknownPortal(dep09);
        }
        for(String dep:model.getKnownWFS()) {
            ch.ehi.iox.ilisite.IliRepository09.WebService_ dep09=new ch.ehi.iox.ilisite.IliRepository09.WebService_();
            dep09.setvalue(dep);
            model09.addknownWFS(dep09);
        }
        for(String dep:model.getKnownWMS()) {
            ch.ehi.iox.ilisite.IliRepository09.WebService_ dep09=new ch.ehi.iox.ilisite.IliRepository09.WebService_();
            dep09.setvalue(dep);
            model09.addknownWMS(dep09);
        }
        return model09;
    }
    public static ModelMetadata mapFromIom20(ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata model20) {
        if(model20==null) {
            return null;
        }
        ModelMetadata model=new ModelMetadata();
        model.setOid(model20.getobjectoid());
        final boolean browseOnly = model20.getbrowseOnly();
        model.setBrowseOnly(browseOnly);
        final String furtherInformation = model20.getfurtherInformation();
        if(furtherInformation!=null)model.setFurtherInformation(furtherInformation);
        final String furtherMetadata = model20.getfurtherMetadata();
        if(furtherMetadata!=null)model.setFurtherMetadata(furtherMetadata);
        final String file = model20.getFile();
        if(file!=null)model.setFile(file);
        final String issuer = model20.getIssuer();
        if(issuer!=null)model.setIssuer(issuer);
        final String md5 = model20.getmd5();
        if(md5!=null)model.setMd5(md5);
        final String name = model20.getName();
        if(name!=null)model.setName(name);
        final String original = model20.getOriginal();
        if(original!=null)model.setOriginal(original);
        final String precursorVersion = model20.getprecursorVersion();
        if(precursorVersion!=null)model.setPrecursorVersion(precursorVersion);
        final String publishingDate = model20.getpublishingDate();
        if(publishingDate!=null)model.setPublishingDate(publishingDate);
        String csl=model20.getSchemaLanguage();
        model.setSchemaLanguage(csl);
        final String shortDescription = model20.getshortDescription();
        if(shortDescription!=null)model.setShortDescription(shortDescription);
        final String tags = model20.getTags();
        if(tags!=null)model.setTags(tags);
        final String technicalContact = model20.gettechnicalContact();
        if(technicalContact!=null)model.setTechnicalContact(technicalContact);
        final String title = model20.getTitle();
        if(title!=null)model.setTitle(title);
        final String version = model20.getVersion();
        if(version!=null)model.setVersion(version);
        final String versionComment = model20.getVersionComment();
        if(versionComment!=null)model.setVersionComment(versionComment);
        final String nameLanguage = model20.getNameLanguage();
        if(nameLanguage!=null)model.setNameLanguage(nameLanguage);
        for(ch.interlis.models.IliRepository20.ModelName_ dep:model20.getdependsOnModel()) {
            model.addDependsOnModel(dep.getvalue());
        }
        for(ch.interlis.models.IliRepository20.ModelName_ dep:model20.getderivedModel()) {
            model.addDerivedModel(dep.getvalue());
        }
        for(ch.interlis.models.IliRepository20.ModelName_ dep:model20.getfollowupModel()) {
            model.addFollowupModel(dep.getvalue());
        }
        for(ch.interlis.models.IliRepository20.WebSite_ dep:model20.getknownPortal()) {
            model.addKnownPortal(dep.getvalue());
        }
        for(ch.interlis.models.IliRepository20.WebService_ dep:model20.getknownWFS()) {
            model.addKnownWFS(dep.getvalue());
        }
        for(ch.interlis.models.IliRepository20.WebService_ dep:model20.getknownWMS()) {
            model.addKnownWMS(dep.getvalue());
        }
        return model;
    }
    public static ModelMetadata mapFromIom09(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model09) {
        if(model09==null) {
            return null;
        }
        ModelMetadata model=new ModelMetadata();
        model.setOid(model09.getobjectoid());
        final boolean browseOnly = model09.getbrowseOnly();
        model.setBrowseOnly(browseOnly);
        final String furtherInformation = model09.getfurtherInformation();
        if(furtherInformation!=null)model.setFurtherInformation(furtherInformation);
        final String furtherMetadata = model09.getfurtherMetadata();
        if(furtherMetadata!=null)model.setFurtherMetadata(furtherMetadata);
        final String file = model09.getFile();
        if(file!=null)model.setFile(file);
        final String issuer = model09.getIssuer();
        if(issuer!=null)model.setIssuer(issuer);
        final String md5 = model09.getmd5();
        if(md5!=null)model.setMd5(md5);
        final String name = model09.getName();
        if(name!=null)model.setName(name);
        final String original = model09.getOriginal();
        if(original!=null)model.setOriginal(original);
        final String precursorVersion = model09.getprecursorVersion();
        if(precursorVersion!=null)model.setPrecursorVersion(precursorVersion);
        final String publishingDate = model09.getpublishingDate();
        if(publishingDate!=null)model.setPublishingDate(publishingDate);
        String csl=ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.toXmlCode(model09.getSchemaLanguage());
        model.setSchemaLanguage(csl);
        final String shortDescription = model09.getshortDescription();
        if(shortDescription!=null)model.setShortDescription(shortDescription);
        final String tags = model09.getTags();
        if(tags!=null)model.setTags(tags);
        final String technicalContact = model09.gettechnicalContact();
        if(technicalContact!=null)model.setTechnicalContact(technicalContact);
        final String title = model09.getTitle();
        if(title!=null)model.setTitle(title);
        final String version = model09.getVersion();
        if(version!=null)model.setVersion(version);
        final String versionComment = model09.getVersionComment();
        if(versionComment!=null)model.setVersionComment(versionComment);
        for(ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep:model09.getdependsOnModel()) {
            model.addDependsOnModel(dep.getvalue());
        }
        for(ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep:model09.getderivedModel()) {
            model.addDerivedModel(dep.getvalue());
        }
        for(ch.ehi.iox.ilisite.IliRepository09.ModelName_ dep:model09.getfollowupModel()) {
            model.addFollowupModel(dep.getvalue());
        }
        for(ch.ehi.iox.ilisite.IliRepository09.WebSite_ dep:model09.getknownPortal()) {
            model.addKnownPortal(dep.getvalue());
        }
        for(ch.ehi.iox.ilisite.IliRepository09.WebService_ dep:model09.getknownWFS()) {
            model.addKnownWFS(dep.getvalue());
        }
        for(ch.ehi.iox.ilisite.IliRepository09.WebService_ dep:model09.getknownWMS()) {
            model.addKnownWMS(dep.getvalue());
        }
        return model;
    }

    static public List<DatasetMetadata> readIliDataXmlLocalFile(File file) 
    throws RepositoryAccessException
    {
        ArrayList<DatasetMetadata> datav=new ArrayList<DatasetMetadata>();
        ch.interlis.iom_j.xtf.XtfReader reader=null;
        try {
            reader=new ch.interlis.iom_j.xtf.XtfReader(file);
            reader.getFactory().registerFactory(ch.interlis.models.DATASETIDX16.getIoxFactory());
            ch.interlis.iox.IoxEvent event=null;
            do{
                 event=reader.read();
                 if(event instanceof ch.interlis.iox.ObjectEvent){
                     ch.interlis.iom.IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
                     if(iomObj instanceof DatasetMetadata){
                         DatasetMetadata model=(DatasetMetadata)iomObj;
                         {
                         }
                         datav.add(model);
                     }else if(iomObj instanceof ch.interlis.models.DatasetIdx16.DataIndex.Metadata){
                         // ignore it
                     }else{
                         EhiLogger.logAdaption("TID="+iomObj.getobjectoid()+": ignored; unknown class <"+iomObj.getobjecttag()+">");
                     }
                 }
            }while(!(event instanceof ch.interlis.iox.EndTransferEvent));
        } catch (IoxException e) {
            throw new RepositoryAccessException("failed to read "+IliManager.ILIDATA_XML,e);
        }finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IoxException e) {
                    throw new RepositoryAccessException(e);
                }
                reader=null;
            }
        }
        return datav;
    }
	/** read an ilisite.xml file
	 * 
	 * @param uri uri of the repository (without ilisite.xml)
	 * @return null if the repository doesn't exist
	 */
	private IliSite readIliSiteXml(String uri)
	throws RepositoryAccessException
	{
		// read it (local location or from cache)
		File file=getLocalFileLocation(uri,IliManager.ILISITE_XML,metaMaxTTL,null);
		if(file==null){
			return null;
		}
		// read file
		ch.interlis.iom_j.xtf.XtfReader reader=null;
		try {
			reader=new ch.interlis.iom_j.xtf.XtfReader(file);
			reader.getFactory().registerFactory(ch.ehi.iox.ilisite.ILISITE09.getIoxFactory());
			ch.interlis.iox.IoxEvent event=null;
			do{
				 event=reader.read();
				 if(event instanceof ch.interlis.iox.ObjectEvent){
					 ch.interlis.iom.IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
					 if(iomObj instanceof ch.ehi.iox.ilisite.IliSite09.SiteMetadata.Site){
						 ch.ehi.iox.ilisite.IliSite09.SiteMetadata.Site site=(ch.ehi.iox.ilisite.IliSite09.SiteMetadata.Site)iomObj;
						 IliSite ret=new IliSite();
						 ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ parents[]=site.getparentSite();
						 if(parents!=null) {
	                         for(int parenti=0;parenti<parents.length;parenti++){
	                             RepositoryLocation_ parent = parents[parenti];
	                             if(parent!=null) {
	                                 ret.addParentSite(parent.getvalue());
	                             }
	                         }
						 }
						 ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_ subsidiaries[]=site.getsubsidiarySite();
						 if(subsidiaries!=null) {
	                         for(int subsidiaryi=0;subsidiaryi<subsidiaries.length;subsidiaryi++){
	                             RepositoryLocation_ subsidiary = subsidiaries[subsidiaryi];
	                             if(subsidiary!=null) {
	                                 ret.addSubsidiarySite(subsidiary.getvalue());
	                             }
	                         }
						 }
						 return ret;
					 }
				 }
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
		} catch (IoxException e) {
			throw new RepositoryAccessException("failed to read "+IliManager.ILISITE_XML,e);
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IoxException e) {
					throw new RepositoryAccessException(e);
				}
				reader=null;
			}
		}
		return null;
	}
    @Deprecated
	public static List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> getLatestVersions(List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions1)
	{
		HashMap<String,ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>> models=new HashMap<String,ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>>(); // map<String modelName,array<ModelMetadata>>
		for(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model:modelVersions1){
			 String name=model.getName();
			 if(!models.containsKey(name)){
				 models.put(name,new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>());
			 }
			 ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> versions=models.get(name);
			 versions.add(model);
		}

		ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> ret=new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>();
		HashMap<String,IliFile> files=new HashMap<String,IliFile>();
		Iterator<String> modeli=models.keySet().iterator();
		while(modeli.hasNext()){
			String name=modeli.next();
			ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions=models.get(name);
			ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage cslv[]=new ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage[]{ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili1,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_2,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage.ili2_3};  
			for(int csli=0;csli<cslv.length;csli++){
			    ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model=getLatestVersion(modelVersions,cslv[csli]);
				if(model==null){
					continue;
				}
				ret.add(model);
			}
		}
		return ret;
		
	}
    public static List<ModelMetadata> getLatestVersions2(List<ModelMetadata> modelVersions1)
    {
        HashMap<String,ArrayList<ModelMetadata>> models=new HashMap<String,ArrayList<ModelMetadata>>(); // map<String modelName,array<ModelMetadata>>
        for(ModelMetadata model:modelVersions1){
             String name=model.getName();
             if(!models.containsKey(name)){
                 models.put(name,new ArrayList<ModelMetadata>());
             }
             ArrayList<ModelMetadata> versions=models.get(name);
             versions.add(model);
        }

        ArrayList<ModelMetadata> ret=new ArrayList<ModelMetadata>();
        HashMap<String,IliFile> files=new HashMap<String,IliFile>();
        Iterator<String> modeli=models.keySet().iterator();
        while(modeli.hasNext()){
            String name=modeli.next();
            ArrayList<ModelMetadata> modelVersions=models.get(name);
            String cslv[]=ModelMetadata.all;  
            for(int csli=0;csli<cslv.length;csli++){
                ModelMetadata model=getLatestVersion2(modelVersions,cslv[csli]);
                if(model==null){
                    continue;
                }
                ret.add(model);
            }
        }
        return ret;
        
    }
	
    @Deprecated
	private static ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata getCslVersion(List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions1,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl)
	{
		ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions=new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>();
		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model=null;

		// remove different csl
		for(Iterator<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> i=modelVersions1.iterator();i.hasNext();){
			model=i.next();
			if(model.getSchemaLanguage().equals(csl)){
				modelVersions.add(model);
			}
		}
		if(modelVersions.isEmpty()){
			// not a model with given csl
			return null;
		}
		if(modelVersions.size()!=1){
			EhiLogger.logAdaption("model "+model.getName()+": multiple versions; use version "+modelVersions.get(0).getVersion());
			return null;
		}
		return modelVersions.get(0);
	}
    private static ModelMetadata getCslVersion2(List<ModelMetadata> modelVersions1,String csl)
    {
        ArrayList<ModelMetadata> modelVersions=new ArrayList<ModelMetadata>();
        ModelMetadata model=null;

        // remove different csl
        for(Iterator<ModelMetadata> i=modelVersions1.iterator();i.hasNext();){
            model=i.next();
            if(model.getSchemaLanguage().equals(csl)){
                modelVersions.add(model);
            }
        }
        if(modelVersions.isEmpty()){
            // not a model with given csl
            return null;
        }
        if(modelVersions.size()!=1){
            EhiLogger.logAdaption("model "+model.getName()+": multiple versions; use version "+modelVersions.get(0).getVersion());
            return null;
        }
        return modelVersions.get(0);
    }
    @Deprecated
	private static ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata getLatestVersion(List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions1,ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl)
	{
		ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelVersions=new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>();
		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata model=null;

		// remove different csl
		for(Iterator<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> i=modelVersions1.iterator();i.hasNext();){
			model=i.next();
			if(model.getSchemaLanguage().equals(csl)){
				modelVersions.add(model);
			}
		}
		if(modelVersions.isEmpty()){
			// not a model with given csl
			return null;
		}
		
		// find first version and eliminate other versions without duplicate
		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata first=null;
		for(Iterator<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> i=modelVersions.iterator();i.hasNext();){
			model=i.next();
			if(model.getprecursorVersion()==null){
				if(first==null){
					if(!model.getbrowseOnly()){
						first=model;
					}
					i.remove();
				}else{
					if(!model.getbrowseOnly()){
						EhiLogger.logAdaption("model "+model.getName()+": duplicate version without precursor; version "+model.getVersion()+" ignored");
					}
					i.remove();
				}
			}
		}
		if(first==null){
			EhiLogger.logAdaption("model "+model.getName()+": no version without precursor; model ignored");
			return null;
		}
		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata lastVersion=first;
		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata current=first;
		while(!modelVersions.isEmpty()){
			// find next version
			ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata next=null;
			for(Iterator<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> i=modelVersions.iterator();i.hasNext();){
				model=i.next();
				if(current.getVersion().equals(model.getprecursorVersion())){
					if(next==null){
						// next version found
						next=model;
						i.remove();
					}else{
						// another next version found; ignore it
						EhiLogger.logAdaption("model "+model.getName()+": duplicate version with same precursor; version "+model.getVersion()+" ignored");
						i.remove();
					}
				}
			}
			if(next==null){
				// no next version found
				break;
			}
			current=next;
			if(!current.getbrowseOnly()){
				lastVersion=current;
			}
		}
		if(!modelVersions.isEmpty()){
			EhiLogger.logAdaption("model "+lastVersion.getName()+": broken version chain; use version "+lastVersion.getVersion()+"; others ignored");
		}
		return lastVersion;
	}
    private static ModelMetadata getLatestVersion2(List<ModelMetadata> modelVersions1,String csl)
    {
        ArrayList<ModelMetadata> modelVersions=new ArrayList<ModelMetadata>();
        ModelMetadata model=null;

        // remove different csl
        for(Iterator<ModelMetadata> i=modelVersions1.iterator();i.hasNext();){
            model=i.next();
            if(model.getSchemaLanguage().equals(csl)){
                modelVersions.add(model);
            }
        }
        if(modelVersions.isEmpty()){
            // not a model with given csl
            return null;
        }
        
        // find first version and eliminate other versions without duplicate
        ModelMetadata first=null;
        for(Iterator<ModelMetadata> i=modelVersions.iterator();i.hasNext();){
            model=i.next();
            if(model.getPrecursorVersion()==null){
                if(first==null){
                    if(!model.isBrowseOnly()){
                        first=model;
                    }
                    i.remove();
                }else{
                    if(!model.isBrowseOnly()){
                        EhiLogger.logAdaption("model "+model.getName()+": duplicate version without precursor; version "+model.getVersion()+" ignored");
                    }
                    i.remove();
                }
            }
        }
        if(first==null){
            EhiLogger.logAdaption("model "+model.getName()+": no version without precursor; model ignored");
            return null;
        }
        ModelMetadata lastVersion=first;
        ModelMetadata current=first;
        while(!modelVersions.isEmpty()){
            // find next version
            ModelMetadata next=null;
            for(Iterator<ModelMetadata> i=modelVersions.iterator();i.hasNext();){
                model=i.next();
                if(current.getVersion().equals(model.getPrecursorVersion())){
                    if(next==null){
                        // next version found
                        next=model;
                        i.remove();
                    }else{
                        // another next version found; ignore it
                        EhiLogger.logAdaption("model "+model.getName()+": duplicate version with same precursor; version "+model.getVersion()+" ignored");
                        i.remove();
                    }
                }
            }
            if(next==null){
                // no next version found
                break;
            }
            current=next;
            if(!current.isBrowseOnly()){
                lastVersion=current;
            }
        }
        if(!modelVersions.isEmpty()){
            EhiLogger.logAdaption("model "+lastVersion.getName()+": broken version chain; use version "+lastVersion.getVersion()+"; others ignored");
        }
        return lastVersion;
    }
    @Deprecated
    private static DatasetMetadata getLatestVersion(List<DatasetMetadata> datasetVersions1)
    {
        ArrayList<DatasetMetadata> datasetVersions=new ArrayList<DatasetMetadata>(datasetVersions1);
        DatasetMetadata dataset=null;
        
        // find first version and eliminate other versions without duplicate
        DatasetMetadata first=null;
        for(Iterator<DatasetMetadata> i=datasetVersions.iterator();i.hasNext();){
            dataset=i.next();
            if(dataset.getprecursorVersion()==null){
                if(first==null){
                    first=dataset;
                    i.remove();
                }else{
                    EhiLogger.logAdaption("dataset "+dataset.getid()+": duplicate version without precursor; version "+dataset.getversion()+" ignored");
                    i.remove();
                }
            }
        }
        if(first==null){
            EhiLogger.logAdaption("dataset "+dataset.getid()+": no version without precursor; dataset ignored");
            return null;
        }
        DatasetMetadata lastVersion=first;
        DatasetMetadata current=first;
        while(!datasetVersions.isEmpty()){
            // find next version
            DatasetMetadata next=null;
            for(Iterator<DatasetMetadata> i=datasetVersions.iterator();i.hasNext();){
                dataset=i.next();
                if(current.getversion().equals(dataset.getprecursorVersion())){
                    if(next==null){
                        // next version found
                        next=dataset;
                        i.remove();
                    }else{
                        // another next version found; ignore it
                        EhiLogger.logAdaption("dataset "+dataset.getid()+": duplicate version with same precursor; version "+dataset.getversion()+" ignored");
                        i.remove();
                    }
                }
            }
            if(next==null){
                // no next version found
                break;
            }
            current=next;
            lastVersion=current;
        }
        if(!datasetVersions.isEmpty()){
            EhiLogger.logAdaption("dataset "+lastVersion.getid()+": broken version chain; use version "+lastVersion.getversion()+"; others ignored");
        }
        return lastVersion;
    }
	/** Gets access to a file.
	 * If the file is in a remote repository, it will be downloaded to the cache and path to the version in the cache is returned.
	 * If the file is already in the cache, the repository will be checked for never version depending on age of cached file and md5 digest.
	 * @param uri remote or local repository
	 * @param filename file/path to download from repository. If null, uri includes the file name.
	 * @param maxTTL max age of file in cache before downloading it again.
	 * @param md5 digest of remote file or null. If cached version has a different digest, download remote file again.
	 * @return local file or null if it doesn't exist in remote repository
	 * @throws RepositoryAccessException
	 */
	public File getLocalFileLocation(String uri,String filename,long maxTTL,String md5)
	throws RepositoryAccessException
{
	if(uri==null){
		if(filename!=null){
			return new File(filename);
		}
		return null;
	}
	if(resolver!=null && resolver.resolvesUri(uri)){
		// translate uri to location in cache
		String localFileName = escapeUri(uri);
		File ret=new File(localCache,localFileName);
		if(filename!=null){
			ret=new File(ret,filename);
		}
		boolean fetchFromServer=true;
		boolean localFileExists=false;
		if(!ret.exists()){
			fetchFromServer=true;
		}else{
			localFileExists=true;
			fetchFromServer=false;
			if(!fetchFromServer && (maxTTL==0 || ret.lastModified()+maxTTL<System.currentTimeMillis())){
				fetchFromServer=true;
			}
			if(!fetchFromServer && md5!=null && !calcMD5(ret).equals(md5)){
				fetchFromServer=true;
			}
		}
		// if not in cache
		if(fetchFromServer){
			// fetch from server
			java.io.BufferedInputStream in=null;
			java.io.OutputStream fos=null;
			try{
				try {
					in=new java.io.BufferedInputStream(resolver.resolveIliFile(uri,filename));
				} catch (FileNotFoundException e) {
					return null;
				} catch (IOException e) {
					if(localFileExists){
						EhiLogger. logAdaption(e.toString()+"; use local copy of remote file "+filename);
						return ret;
					}
					throw new RepositoryAccessException(e);
				}
				// create directory
				java.io.File dir=ret.getParentFile();
				if(!dir.exists()){
					boolean created=dir.mkdirs();
					if(!created){
						throw new IllegalArgumentException("failed to create folder "+uri);
					}
				}
				// save to cache
				try {
					fos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(ret));
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException("failed to create file in cache",e);
				}
			    try {
					byte[] buf = new byte[1024];
					int i = 0;
					while ((i = in.read(buf)) != -1) {
					    fos.write(buf, 0, i);
					}
				} catch (IOException e) {
					throw new IllegalArgumentException("failed to save file to cache",e);
				}
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						EhiLogger.logError(e);
					}
					in=null;
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						EhiLogger.logError(e);
					}
					fos=null;
				}
			}
		}
		return ret;
	}
	// if http
	String urilc=uri.toLowerCase();
	boolean isHttps= urilc.startsWith("https:");
	boolean isHttp=urilc.startsWith("http:");
	if(isHttp || isHttps){
		// translate uri to location in cache
		String localFileName=null;
		{
			String urib=null;
			if(isHttps){
				urib=uri.substring("https:".length()); 
			}else{
				urib=uri.substring("http:".length()); 
			}
			localFileName=escapeUri(urib);
		}
		File ret=new File(localCache,localFileName);
		if(filename!=null){
			ret=new File(ret,filename);
		}
		boolean fetchFromServer=true;
		boolean localFileExists=false;
		if(!ret.exists()){
			fetchFromServer=true;
		}else{
			localFileExists=true;
			fetchFromServer=false;
			if(!fetchFromServer && (maxTTL==0 || ret.lastModified()+maxTTL<System.currentTimeMillis())){
				fetchFromServer=true;
			}
			if(!fetchFromServer && md5!=null && !calcMD5(ret).equals(md5)){
				fetchFromServer=true;
			}
		}
		// if not in cache
		if(fetchFromServer){
			// fetch from http server (handle redirects)
			java.net.URL url=null;
			try {
				url=new java.net.URI(uri).toURL();
				if(filename!=null){
					// incomplete url of directory?
					if(!uri.endsWith("/")){
						// fix it
						url=new java.net.URI(uri+"/").toURL();
					}
					url=new java.net.URL(url,filename);
				}
				EhiLogger.traceState("fetching <"+url+"> ...");
			} catch (MalformedURLException e) {
				throw new RepositoryAccessException(e);
			} catch (URISyntaxException e) {
				throw new RepositoryAccessException(e);
			}
			java.net.URLConnection conn=null;
			try {
				//
				// java  -Dhttp.proxyHost=myproxyserver.com  -Dhttp.proxyPort=80 MyJavaApp
				//
				// System.setProperty("http.proxyHost", "myProxyServer.com");
				// System.setProperty("http.proxyPort", "80");
				//
				// System.setProperty("java.net.useSystemProxies", "true");
				//
				// since 1.5 
				// Proxy instance, proxy ip = 123.0.0.1 with port 8080
				// Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("123.0.0.1", 8080));
				// URL url = new URL("http://www.yahoo.com");
				// HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
				// uc.connect();
				// 
				conn = url.openConnection();
			} catch (IOException e) {
				if(localFileExists){
					EhiLogger.logAdaption(e.toString()+"; use local copy of remote file "+filename);
					return ret;
				}
				throw new RepositoryAccessException(e);
			}
			java.io.BufferedInputStream in=null;
			java.io.OutputStream fos=null;
			try{
				try {
					in=new java.io.BufferedInputStream(conn.getInputStream());
				} catch (FileNotFoundException e) {
					return null;
				//} catch (java.net.UnknownHostException e) {
				//	return null;
				} catch (IOException e) {
					if(localFileExists){
						EhiLogger. logAdaption(e.toString()+"; use local copy of remote file "+filename);
						return ret;
					}
					throw new RepositoryAccessException(e);
				}
				// create directory
				java.io.File dir=ret.getParentFile();
				if(!dir.exists()){
					boolean created=dir.mkdirs();
					if(!created){
						throw new IllegalArgumentException("failed to create folder "+uri);
					}
				}
				// save to cache
				try {
					fos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(ret));
				} catch (FileNotFoundException e) {
					throw new IllegalArgumentException("failed to create file in cache",e);
				}
			    try {
					byte[] buf = new byte[1024];
					int i = 0;
					while ((i = in.read(buf)) != -1) {
					    fos.write(buf, 0, i);
					}
				} catch (IOException e) {
					throw new IllegalArgumentException("failed to save downloaded file to cache",e);
				}
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						EhiLogger.logError(e);
					}
					in=null;
				}
				if(fos!=null){
					try {
						fos.close();
					} catch (IOException e) {
						EhiLogger.logError(e);
					}
					fos=null;
				}
			}
		}
		return ret;
	}
	// uri is a local file 
	File ret=new File(uri);
	if(filename!=null){
		ret=new File(ret,filename);
	}
	if(!ret.exists()){
		return null;
	}
	return ret;
}
    @Deprecated
	public static ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata findModelMetadata(
    		List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelMetadatav, String name,
    		ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl) {
    	for(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata modelMetadata :  modelMetadatav){
    		if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl)){
    			return modelMetadata;
    		}
    	}
    	return null;
    }
    public static ModelMetadata findModelMetadata2(
            List<ModelMetadata> modelMetadatav, String name,
            String csl) {
        for(ModelMetadata modelMetadata :  modelMetadatav){
            if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl)){
                return modelMetadata;
            }
        }
        return null;
    }
    @Deprecated
    public static ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata findModelMetadata(
            List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> modelMetadatav, String name,
            ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage csl,String version) {
        for(ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata modelMetadata :  modelMetadatav){
            if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl) && modelMetadata.getVersion().equals(version)){
                return modelMetadata;
            }
        }
        return null;
    }
    public static ModelMetadata findModelMetadata2(
            List<ModelMetadata> modelMetadatav, String name,
            String csl,String version) {
        for(ModelMetadata modelMetadata :  modelMetadatav){
            if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl) && modelMetadata.getVersion().equals(version)){
                return modelMetadata;
            }
        }
        return null;
    }
    public static String escapeUri(String uri) {
		StringBuffer localFileName=new StringBuffer();
		{
			// escape characters
			// win < > : " / \ | ? * %
			for(int i=0;i<uri.length();i++){
				char c=uri.charAt(i);
				if(c=='<'
						|| c=='>'
						|| c==':'
						|| c=='"'
						|| c=='\\'
						|| c=='|'
						|| c=='?'
						|| c=='*'
						|| c=='%'
						|| c=='&'){
					localFileName.append('&');
					String str=Integer.toHexString(c);
					localFileName.append("0000".substring(str.length())+str);
				}else{
					localFileName.append(c);
				}
			}
		}
		return localFileName.toString();
	}
	/** Calculates the digest of a local file.
	 * @param file local file
	 * @return md5 digest of local file content.
	 */
	public static String calcMD5(File file)
	{
		java.security.MessageDigest md;
		try {
			md = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		java.io.InputStream is;
		try {
			is = new java.io.FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
		String signature = null;
		try {
			byte[] buffer = new byte[1024];
		     int numRead;
		     do {
		      numRead = is.read(buffer);
		      if (numRead > 0) {
		        md.update(buffer, 0, numRead);
		        }
		      } while (numRead != -1);
			signature=String.format("%032x",new java.math.BigInteger(1,md.digest()));
		}catch(IOException e){
			EhiLogger.logError(e);
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				EhiLogger.logError(e);
			}
		}
		return signature;
	}
	public static void copyFile(File target,File src) {
		java.io.BufferedInputStream in=null;
		java.io.OutputStream fos=null;
		try{
			try {
				in=new java.io.BufferedInputStream(new FileInputStream(src));
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("failed to open file",e);
			}
			// create directory
			java.io.File dir=target.getParentFile();
			if(!dir.exists()){
				boolean created=dir.mkdirs();
				if(!created){
					throw new IllegalArgumentException("failed to create folder "+dir);
				}
			}
			try {
				fos = new java.io.BufferedOutputStream(new java.io.FileOutputStream(target));
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("failed to open file",e);
			}
		    try {
				byte[] buf = new byte[1024];
				int i = 0;
				while ((i = in.read(buf)) != -1) {
				    fos.write(buf, 0, i);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("failed to copy file",e);
			}
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					EhiLogger.logError(e);
				}
				in=null;
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					EhiLogger.logError(e);
				}
				fos=null;
			}
		}
		
	}

}
