package ch.interlis.ili2c;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.models.ILIREPOSITORY20;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ilirepository.impl.RepositoryVisitor;
import ch.interlis.ilirepository.impl.ModelMetadata;
import ch.interlis.ilirepository.impl.ModelLister;
import ch.interlis.ilirepository.impl.RepositoryAccess;
import ch.interlis.ilirepository.impl.RepositoryAccessException;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class ListModels2 {

	public boolean listModels(Configuration config,
			UserSettings settings, boolean onlyLatestVersions) {
		boolean failed=false;
		Main.setHttpProxySystemProperties(settings);
		RepositoryAccess reposAccess=new RepositoryAccess();
		// TODO reposAccess.setCache(localdir);
		List<String> repos=new ArrayList<String>();
		if(config.getSizeFileEntry()==0) {
		    // use default
		    repos.add(UserSettings.ILI_REPOSITORY);
		}else {
	        Iterator reposi = config.iteratorFileEntry();
	        while (reposi.hasNext()) {
	            FileEntry e = (FileEntry) reposi.next();
	            if (e.getKind() == FileEntryKind.ILIMODELFILE) {
	                repos.add(e.getFilename());
	            }
	        }
		}
        ModelLister modelLister=new ModelLister();
        modelLister.setIgnoreDuplicates(true);
		RepositoryVisitor visitor=new RepositoryVisitor( reposAccess,modelLister);
		visitor.setRepositories(repos.toArray(new String[repos.size()]));
        try {
            visitor.visitRepositories();
        } catch (RepositoryAccessException ex) {
            EhiLogger.logError(ex);
            return false;
        }
        List<ModelMetadata> mergedModelMetadatav=modelLister.getResult2();
        if(onlyLatestVersions) {
            mergedModelMetadatav=RepositoryAccess.getLatestVersions2(mergedModelMetadatav);
        }
		
				
		// write new ilimodels.xml
		java.io.OutputStream outStream=null;

		XtfWriterBase ioxWriter=null;
		try{
	        if (config.getOutputFile()==null || "-".equals(config.getOutputFile())) {
	            outStream = System.out;
	        } else {
	            File destFile=new File(config.getOutputFile());
	            outStream=new java.io.FileOutputStream(destFile);
	        }
			
			ioxWriter = new XtfWriterBase( outStream,  ILIREPOSITORY20.getIoxMapping(),"2.3");
			ioxWriter.setModels(new XtfModel[]{ILIREPOSITORY20.getXtfModel()});
			StartTransferEvent startTransferEvent = new StartTransferEvent();
			startTransferEvent.setSender( Main.APP_NAME+"-"+ch.interlis.ili2c.metamodel.TransferDescription.getVersion() );
			ioxWriter.write( startTransferEvent );
			StartBasketEvent startBasketEvent = new StartBasketEvent( ILIREPOSITORY20.RepositoryIndex, "b1");
			ioxWriter.write( startBasketEvent );
			int tid=1;
			for(ModelMetadata model:mergedModelMetadatav){
			    ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata iomModel=RepositoryAccess.mapToIom20(model);
			    iomModel.setobjectoid(Integer.toString(tid));
				ioxWriter.write(new ObjectEvent(iomModel));
				tid++;
			}
			
			ioxWriter.write( new EndBasketEvent() );
			ioxWriter.write( new EndTransferEvent() );
			
			ioxWriter.flush();
		}catch(java.io.FileNotFoundException ex){
			EhiLogger.logError(ex);
			failed=true;
		} catch (IoxException ex) {
			EhiLogger.logError(ex);
			failed=true;
		}finally{
			if(ioxWriter!=null){
				try {
					ioxWriter.close();
				} catch (IoxException ex) {
					EhiLogger.logError(ex);
				}
				ioxWriter=null;
			}
			if(outStream!=null && outStream!=System.out){
				try{
					outStream.close();				
				}catch(java.io.IOException ex){
					EhiLogger.logError(ex);
				}
				outStream=null;
			}
		}
		return failed;
	}

	private String escapeReposUri(String uri) {
		String urilc=uri.toLowerCase();
		boolean isHttps= urilc.startsWith("https://");
		boolean isHttp=urilc.startsWith("http://");
		if(isHttp || isHttps){
			if(isHttps){
				uri=uri.substring("https://".length()); 
			}else{
				uri=uri.substring("http://".length()); 
			}
		}
		return RepositoryAccess.escapeUri(uri);
	}
}
