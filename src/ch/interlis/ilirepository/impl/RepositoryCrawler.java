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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliSite;

/** Crawls the web of repositories to find a given model.
 * @author ceis
 */
public class RepositoryCrawler {
	private String repositoryUri[];
	private RepositoryAccess rep=new RepositoryAccess();
	
	/** Initialize crawler.
	 * @param repositoryUri list of repositories to start search for models.
	 * @param rep handles access to repositories.
	 */
	public void setup(String repositoryUri[],RepositoryAccess rep)
	{
		this.rep=rep;
		this.repositoryUri=repositoryUri;
	}

	private IliFile crawlParents(String modelName, double iliVersion,HashSet<String> visitedSites, ArrayList<String> parents,boolean logLookup) {
		while(!parents.isEmpty()){
			String uri=parents.remove(0);
			if(!visitedSites.contains(uri)){
				visitedSites.add(uri);
				logRepositoryScan(uri,modelName,iliVersion,logLookup);
				IliFile iliFile=getIliFileMetadataShallow(modelName, iliVersion,uri);
				if(iliFile!=null){
					return iliFile;
				}
				IliSite iliSite = rep.getIliSite(uri);
				if(iliSite==null){
					// site offline
				}else{
					Iterator<String> parenti=iliSite.iteratorParentSite();
					while(parenti.hasNext()){
						String parent=parenti.next();
						// add parent to queue
						parents.add(parent);
					}
				}
			}
		}
		return null;
	}

	private void logRepositoryScan(String uri,String model,double version,boolean dologging) {
		if(dologging){
			if(version==0.0){
				EhiLogger.logState("lookup model <"+model+"> in repository <"+uri+">");
			}else{
				EhiLogger.logState("lookup model <"+model+"> "+version+" in repository <"+uri+">");
			}
		}
	}

	/** Gets metadata of newest model with given name.
	 * Crawls the web of repositories, until it finds the model, or there are no more repositories.
	 * @return null if model not known/found.
	 */
	public IliFile getIliFileMetadataDeep(String modelName,double iliVersion,boolean logLookup)
	{
		HashSet<String> visitedSites=new HashSet<String>();
		// search in defined repos
		for(int i=0;i<repositoryUri.length;i++){
			String uri=repositoryUri[i];
			if(!visitedSites.contains(uri)){
				logRepositoryScan(uri,modelName,iliVersion,logLookup);
				visitedSites.add(uri);
				IliFile iliFile=getIliFileMetadataShallow(modelName, iliVersion,uri);
				if(iliFile!=null){
					return iliFile;
				}
			}
		}
		
		ArrayList<String> parents=new ArrayList<String>();
		ArrayList<String> subsidiaries=new ArrayList<String>();
		for(int i=0;i<repositoryUri.length;i++){
			String uri=repositoryUri[i];
			IliSite iliSite = rep.getIliSite(uri);
			if(iliSite==null){
				// site offline
			}else{
				Iterator<String> parenti=iliSite.iteratorParentSite();
				while(parenti.hasNext()){
					String parent=parenti.next();
					// add parent to queue
					parents.add(parent);
				}
				Iterator<String> subsidiaryi=iliSite.iteratorSubsidiarySite();
				while(subsidiaryi.hasNext()){
					String subsidiary=subsidiaryi.next();
					// add subsidiary to queue
					subsidiaries.add(subsidiary);
				}
			}
		}
		// crawl direct parent repositories
		IliFile ret=crawlParents(modelName, iliVersion,visitedSites, parents,logLookup);
		if(ret!=null){
			return ret;
		}
		// crawl subsidiary repositories
		while(!subsidiaries.isEmpty()){
			String uri=subsidiaries.remove(0);
			if(!visitedSites.contains(uri)){
				visitedSites.add(uri);
				logRepositoryScan(uri,modelName,iliVersion,logLookup);
				IliFile iliFile=getIliFileMetadataShallow(modelName, iliVersion,uri);
				if(iliFile!=null){
					return iliFile;
				}
				IliSite iliSite = rep.getIliSite(uri);
				if(iliSite==null){
					// site offline
				}else{
					Iterator<String> subsidiaryi=iliSite.iteratorSubsidiarySite();
					while(subsidiaryi.hasNext()){
						String subsidiary=(String)subsidiaryi.next();
						// add subsidiary to queue
						subsidiaries.add(subsidiary);
					}
					// crawl parents of subsidiaries repositories
					Iterator<String> parenti=iliSite.iteratorParentSite();
					while(parenti.hasNext()){
						String parent=(String)parenti.next();
						// add parent to queue
						parents.add(parent);
					}
					IliFile ret2=crawlParents(modelName, iliVersion,visitedSites, parents,logLookup);
					if(ret2!=null){
						return ret2;
					}
				}
			}
		}
		
		return null;
	}

	/** check a repository for a given modelName without looking further.
	 * @param modelName model name
	 * @param uri repository
	 * @return null if not in this repository
	 */
	private IliFile getIliFileMetadataShallow(String modelName,double iliVersion, String uri) {
		IliFiles iliFiles=rep.getIliFiles(uri);
		if(iliFiles==null){
			// site offline
		}else{
			IliFile iliFile=null;
			if(iliVersion==0.0){
				iliFile=iliFiles.getFileWithModel(modelName,2.3);
				if(iliFile==null){
					iliFile=iliFiles.getFileWithModel(modelName,1.0);
				}
				if(iliFile==null){
					iliFile=iliFiles.getFileWithModel(modelName,2.2);
				}
			}else{
				iliFile=iliFiles.getFileWithModel(modelName,iliVersion);
			}
			if(iliFile!=null){
				return iliFile;
			}
		}
		return null;
	}

}
