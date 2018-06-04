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
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.ilisite.IliRepository09.ModelName_;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage;
import ch.ehi.iox.ilisite.IliSite09.RepositoryLocation_;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliResolver;
import ch.interlis.ilirepository.IliSite;
import ch.interlis.ilirepository.IliManager;
import ch.interlis.iox.IoxException;

/** Handles access to a file in a repository.
 * Hides the caching, downloading of files.
 * @author ceis
 */
public class RepositoryAccess {
	private HashMap<String,IliFiles> reposIliFiles=new HashMap<String,IliFiles>();
	private HashMap<String,IliSite> reposIliSite=new HashMap<String,IliSite>();
	private long metaMaxTTL=86400000L; // max time to live in ms for a file in the cache
	private File localCache=new File(System.getProperty("user.home"),".ilicache");
	private IliResolver resolver=null;
	
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
    public IliFiles getModelMetadata(String uri)
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
	private String toString(Throwable ex)
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
	public static IliFiles createIliFiles(String uri,java.util.Collection<ModelMetadata> modelv)
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
			ModelMetadata_SchemaLanguage cslv[]=new ModelMetadata_SchemaLanguage[]{ModelMetadata_SchemaLanguage.ili1,ModelMetadata_SchemaLanguage.ili2_2,ModelMetadata_SchemaLanguage.ili2_3};  
			for(int csli=0;csli<cslv.length;csli++){
				ModelMetadata model=getCslVersion(modelVersions,cslv[csli]);
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
				ModelMetadata_SchemaLanguage csl=model.getSchemaLanguage();
				if(csl==ModelMetadata_SchemaLanguage.ili1){
					iliModel.setIliVersion(1.0);
				}else if(csl==ModelMetadata_SchemaLanguage.ili2_2){
					iliModel.setIliVersion(2.2);
				}else if(csl==ModelMetadata_SchemaLanguage.ili2_3){
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
	/** read an ilimodels.xml file
	 * 
	 * @param uri uri of the repository without ilimodels.xml
	 * @return null if the repository doesn't exist
	 */
	private IliFiles readIlimodelsXmlAsIliFiles(String uri)
	throws RepositoryAccessException
	{
		List<ModelMetadata> modelv =  readIlimodelsXml(uri);
		if(modelv==null) {
		    return null;
		}
		modelv=getLatestVersions(modelv);
		return createIliFiles(uri, modelv);
	}
    public List<ModelMetadata> readIlimodelsXml(String uri) throws RepositoryAccessException {
        File file=getLocalFileLocation(uri,IliManager.ILIMODELS_XML,metaMaxTTL,null);
		if(file==null){
			return null;
		}
		// read file
		List<ModelMetadata> modelv = readIliModelsXml(file);
        return modelv;
    }
	static private final String REGEX_ILI_NAME="[a-zA-Z]([a-zA-Z0-9_]*)";
	static public List<ModelMetadata> readIliModelsXml(File file) 
	throws RepositoryAccessException
	{
		ArrayList<ModelMetadata> modelv=new ArrayList<ModelMetadata>();
		ch.interlis.iom_j.xtf.XtfReader reader=null;
		try {
			reader=new ch.interlis.iom_j.xtf.XtfReader(file);
			reader.getFactory().registerFactory(ch.ehi.iox.ilisite.ILIREPOSITORY09.getIoxFactory());
			ch.interlis.iox.IoxEvent event=null;
			do{
				 event=reader.read();
				 if(event instanceof ch.interlis.iox.ObjectEvent){
					 ch.interlis.iom.IomObject iomObj=((ch.interlis.iox.ObjectEvent)event).getIomObject();
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
							 ModelMetadata_SchemaLanguage mCsl=model.getSchemaLanguage();
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
	public static List<ModelMetadata> getLatestVersions(List<ModelMetadata> modelVersions1)
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
			ModelMetadata_SchemaLanguage cslv[]=new ModelMetadata_SchemaLanguage[]{ModelMetadata_SchemaLanguage.ili1,ModelMetadata_SchemaLanguage.ili2_2,ModelMetadata_SchemaLanguage.ili2_3};  
			for(int csli=0;csli<cslv.length;csli++){
				ModelMetadata model=getLatestVersion(modelVersions,cslv[csli]);
				if(model==null){
					continue;
				}
				ret.add(model);
			}
		}
		return ret;
		
	}
	
	private static ModelMetadata getCslVersion(List<ModelMetadata> modelVersions1,ModelMetadata_SchemaLanguage csl)
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
	private static ModelMetadata getLatestVersion(List<ModelMetadata> modelVersions1,ModelMetadata_SchemaLanguage csl)
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
	public static ModelMetadata findModelMetadata(
    		List<ModelMetadata> modelMetadatav, String name,
    		ModelMetadata_SchemaLanguage csl) {
    	for(ModelMetadata modelMetadata :  modelMetadatav){
    		if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl)){
    			return modelMetadata;
    		}
    	}
    	return null;
    }
    public static ModelMetadata findModelMetadata(
            List<ModelMetadata> modelMetadatav, String name,
            ModelMetadata_SchemaLanguage csl,String version) {
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
