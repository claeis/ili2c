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
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliSite;

/** Crawls the web of repositories to find a given model.
 * @author ceis
 */
public class RepositoryVisitor {
	private String repositoryUri[];
	private RepositoryAccess rep=null;
	protected VisitorAction action=null;
	
	/** Initialize crawler.
	 * @param repositoryUri list of repositories to start search for models.
	 * @param rep handles access to repositories.
	 */
	public RepositoryVisitor(RepositoryAccess rep,VisitorAction action)
	{
		this.rep=rep;
		this.action=action;
	}
	public void setRepositories(String repositoryUri[]) {
	    ArrayList<String> repos=new ArrayList<String>();
	    for(String uri:repositoryUri) {
	        if(uri!=null && uri.trim().length()>0) {
	            repos.add(uri.trim());
	        }
	    }
        this.repositoryUri=repos.toArray(new String[repos.size()]);
	}

	private boolean crawlParents(HashSet<String> visitedSites, List<String> parents) throws RepositoryAccessException {
		while(!parents.isEmpty()){
			String uri=fixUri(parents.remove(0));
			if(!visitedSites.contains(uri)){
				visitedSites.add(uri);
                boolean stopVisiting=action.processRepository(uri,rep);
                if(stopVisiting) {
                    return true;
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
		return false;
	}
	
	public static String fixUri(String uri) {
        if(uri.endsWith("/")) {
            return uri;
        }
        return uri=uri+"/";
    }

    /** Gets metadata of newest model with given name.
	 * Crawls the web of repositories, until it finds the model, or there are no more repositories.
	 * @return null if model not known/found.
	 * @throws RepositoryAccessException 
	 */
	public void visitRepositories() throws RepositoryAccessException
	{
		HashSet<String> visitedSites=new HashSet<String>();
		// search in defined repos
		for(int i=0;i<repositoryUri.length;i++){
			String uri=fixUri(repositoryUri[i]);
			if(!visitedSites.contains(uri)){
				visitedSites.add(uri);
                boolean stopVisiting=action.processRepository(uri,rep);
                if(stopVisiting) {
                    return;
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
		{
	        boolean stopVisiting=crawlParents(visitedSites, parents);
	        if(stopVisiting) {
	            return;
	        }
		}
		
        
		// crawl subsidiary repositories
		while(!subsidiaries.isEmpty()){
			String uri=fixUri(subsidiaries.remove(0));
			if(!visitedSites.contains(uri)){
				visitedSites.add(uri);
				{
	                boolean stopVisiting=action.processRepository(uri,rep);
	                if(stopVisiting) {
	                    return;
	                }
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
					boolean stopVisiting=crawlParents(visitedSites, parents);
					if(stopVisiting) {
					    return;
					}
				}
			}
		}
		
	
	}

}
