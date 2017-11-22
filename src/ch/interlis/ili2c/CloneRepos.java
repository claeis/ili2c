package ch.interlis.ili2c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.iox.ilisite.ILIREPOSITORY09;
import ch.ehi.iox.ilisite.IliRepository09.ModelName_;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliManager;
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

public class CloneRepos {

	public boolean cloneRepos(Configuration config,
			UserSettings settings) {
		boolean failed=false;
		Main.setHttpProxySystemProperties(settings);
		if(config.getOutputFile()==null) {
			EhiLogger.logError("no output folder given");
			return true;
		}
		ArrayList<ModelMetadata> mergedModelMetadatav = new ArrayList<ModelMetadata>();
		Iterator reposi = config.iteratorFileEntry();
		File destFolder=new File(config.getOutputFile());
		destFolder.mkdir();
		EhiLogger.logState("outputFolder <"+destFolder.getPath()+">");
		RepositoryAccess reposAccess=new RepositoryAccess();
		// TODO reposAccess.setCache(localdir);
		while (reposi.hasNext()) {
			FileEntry e = (FileEntry) reposi.next();
			if (e.getKind() == FileEntryKind.ILIMODELFILE) {
				String repos = e.getFilename();
				String localReposPath = escapeReposUri(repos);
				// get list of current files in repository
				File ilimodelsFile;
				try {
					ilimodelsFile = reposAccess.getLocalFileLocation(repos,IliManager.ILIMODELS_XML,0,null);
				} catch (RepositoryAccessException e2) {
					EhiLogger.logError(e2);
					failed=true;
					continue;
				}
				if(ilimodelsFile==null){
					EhiLogger.logAdaption("URL <"+repos+"> contains no "+IliManager.ILIMODELS_XML+"; ignored");
					continue;
				}
				// read file
				IliFiles files=null;
				ArrayList<ModelMetadata> modelMetadatav = null;
				try {
					modelMetadatav = RepositoryAccess.readIliModelsXml(ilimodelsFile);
					modelMetadatav = RepositoryAccess.getLatestVersions(modelMetadatav);
					files = RepositoryAccess.createIliFiles(repos, modelMetadatav);
				} catch (RepositoryAccessException e2) {
					EhiLogger.logError(e2);
					failed=true;
					continue;
				}
				for(Iterator<IliFile> filei=files.iteratorFile();filei.hasNext();){
					IliFile file=filei.next();
					ArrayList<String> ilimodels=new ArrayList<String>();
					for(Iterator modeli=file.iteratorModel();modeli.hasNext();){
						IliModel model=(IliModel)modeli.next();
						ilimodels.add(model.getName());
					}
						try {
							File srcFile = reposAccess.getLocalFileLocation(repos,file.getPath(),0,file.getMd5());
							// clone file
							File destFile=new File(new File(destFolder,localReposPath),file.getPath());
							RepositoryAccess.copyFile(destFile, srcFile);
						} catch (RepositoryAccessException e1) {
							EhiLogger.logError(e1);
							failed=true;
						}

				}
				for(ModelMetadata md:modelMetadatav) {
					String file=md.getFile();
					md.setFile(new File(localReposPath,file).getPath());
					mergedModelMetadatav.add(md);
				}
			}
		}
		// write new ilimodels.xml
		java.io.OutputStream outStream=null;
		XtfWriterBase ioxWriter=null;
		try{
			outStream=new java.io.FileOutputStream(new File(destFolder,IliManager.ILIMODELS_XML));
			ioxWriter = new XtfWriterBase( outStream,  ILIREPOSITORY09.getIoxMapping(),"2.3");
			ioxWriter.setModels(new XtfModel[]{ILIREPOSITORY09.getXtfModel()});
			StartTransferEvent startTransferEvent = new StartTransferEvent();
			startTransferEvent.setSender( Main.APP_NAME+"-"+Main.getVersion() );
			ioxWriter.write( startTransferEvent );
			StartBasketEvent startBasketEvent = new StartBasketEvent( ILIREPOSITORY09.RepositoryIndex, "b1");
			ioxWriter.write( startBasketEvent );
			for(ModelMetadata model:mergedModelMetadatav){
				ioxWriter.write(new ObjectEvent(model));
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
			if(outStream!=null){
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
