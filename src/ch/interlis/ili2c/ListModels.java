package ch.interlis.ili2c;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
import ch.interlis.ilirepository.impl.RepositoryVisitor;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;

public class ListModels {

	public boolean listModels(Configuration config,
			UserSettings settings, boolean onlyLatestVersions) {
		boolean failed=false;
		Main.setHttpProxySystemProperties(settings);
		RepositoryAccess reposAccess=new RepositoryAccess();
		// TODO reposAccess.setCache(localdir);
		List<String> repos=new ArrayList<String>();
        Iterator reposi = config.iteratorFileEntry();
        while (reposi.hasNext()) {
            FileEntry e = (FileEntry) reposi.next();
            if (e.getKind() == FileEntryKind.ILIMODELFILE) {
                repos.add(e.getFilename());
            }
        }
		RepositoryVisitor visitor=new RepositoryVisitor();
		visitor.setup(repos.toArray(new String[repos.size()]), reposAccess);
        List<ModelMetadata> mergedModelMetadatav=null;
        try {
            mergedModelMetadatav = visitor.getIliFileMetadataDeep(true);
        } catch (RepositoryAccessException ex) {
            EhiLogger.logError(ex);
            return false;
        }
        if(onlyLatestVersions) {
            mergedModelMetadatav=RepositoryAccess.getLatestVersions(mergedModelMetadatav);
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
			
			ioxWriter = new XtfWriterBase( outStream,  ILIREPOSITORY09.getIoxMapping(),"2.3");
			ioxWriter.setModels(new XtfModel[]{ILIREPOSITORY09.getXtfModel()});
			StartTransferEvent startTransferEvent = new StartTransferEvent();
			startTransferEvent.setSender( Main.APP_NAME+"-"+Main.getVersion() );
			ioxWriter.write( startTransferEvent );
			StartBasketEvent startBasketEvent = new StartBasketEvent( ILIREPOSITORY09.RepositoryIndex, "b1");
			ioxWriter.write( startBasketEvent );
			int tid=1;
			for(ModelMetadata model:mergedModelMetadatav){
			    model.setobjectoid(Integer.toString(tid));
				ioxWriter.write(new ObjectEvent(model));
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
