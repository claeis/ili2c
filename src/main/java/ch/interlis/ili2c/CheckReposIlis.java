package ch.interlis.ili2c;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.ehi.iox.objpool.ObjectPoolManager;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.PredefinedModel;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.IliManager;
import ch.interlis.ilirepository.impl.RepositoryAccess;
import ch.interlis.ilirepository.impl.RepositoryAccessException;
import ch.interlis.ilirepository.impl.ModelMetadata;

public class CheckReposIlis {
	private class MetaEntryProblem {
		private String modelName=null;
		private String tid=null;
		private String msg=null;
		public MetaEntryProblem(String tid1,String modelName1,String msg1){
			this.modelName=modelName1;
			this.tid=tid1;
			this.msg=msg1;
		}
		public String getModelName() {
			return modelName;
		}
		public String getTid() {
			return tid;
		}
		public String getMsg() {
			return msg;
		}
	}

	private boolean validationErrors=false;
	public boolean checkRepoIlis(Configuration config,
			Settings settings) {
		
		Main.setHttpProxySystemProperties(settings);
				
        HashSet<IliFile> failedFiles=new HashSet<IliFile>();
        ArrayList<MetaEntryProblem> inconsistentMetaEntry=new ArrayList<MetaEntryProblem>();
		File tmpFolder=ObjectPoolManager.getCacheTmpFilename();;
		try {
	        Iterator reposi = config.iteratorFileEntry();
	        while (reposi.hasNext()) {
	            FileEntry e = (FileEntry) reposi.next();
	            if (e.getKind() == FileEntryKind.ILIMODELFILE) {
	                String repos = e.getFilename();
	                // get list of current files in repository
	                RepositoryAccess reposAccess=new RepositoryAccess();
	                File ilimodelsXmlFile;
	                try {
	                    ilimodelsXmlFile = reposAccess.getLocalFileLocation(repos,IliManager.ILIMODELS_XML,0,null);
	                } catch (RepositoryAccessException e2) {
	                    EhiLogger.logError(e2);
	                    continue;
	                }
	                if(ilimodelsXmlFile==null){
	                    EhiLogger.logAdaption("URL <"+repos+"> contains no "+IliManager.ILIMODELS_XML+"; ignored");
	                    continue;
	                }
	                // xsd validate file
	                {
	                    javax.xml.transform.Source schemaFiles[] =  null;
	                    try{
	                    javax.xml.transform.Source schemaFiles2[] =  {
	                                new StreamSource(getClass().getResource( "/IliRepository.xsd" ).toString())
	                        };
	                        schemaFiles=schemaFiles2;
	                    }catch(java.lang.NullPointerException ex){
	                        EhiLogger.logError("failed to create schema",ex);
	                    }
	                    Schema schema=null;
	                    if(schemaFiles!=null){
	                        SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	                        try {
	                            schema = factory.newSchema(schemaFiles);
	                        } catch (SAXException ex) {
	                            EhiLogger.logError("failed to read schema",ex);
	                        }
	                    }
	                    if(schema!=null){
	                        javax.xml.validation.ValidatorHandler validator = schema
	                                .newValidatorHandler();
	                        validator.setErrorHandler(new org.xml.sax.ErrorHandler(){
	                            public void error(SAXParseException ex)
	                                    throws SAXException {
	                                EhiLogger.logError(IliManager.ILIMODELS_XML+":"+ex.getLineNumber()+":"+ex.getColumnNumber()+":"+ex.getMessage());
	                                validationErrors=true;
	                            }

	                            public void fatalError(SAXParseException ex)
	                                    throws SAXException {
	                                EhiLogger.logError(ex);
	                            }

	                            public void warning(SAXParseException ex)
	                                    throws SAXException {
	                                EhiLogger.logError(ex);
	                            }
	                        });
	                        javax.xml.stream.XMLInputFactory inputFactory = javax.xml.stream.XMLInputFactory.newInstance();
	                        FileInputStream inputFile;
	                        XMLEventReader reader;
	                        try {
	                            //inputFile = new java.io.FileInputStream(ilimodelsFile);
	                            //reader = inputFactory.createXMLEventReader(inputFile);
	                            //validator.validate(new javax.xml.transform.stax.StAXSource(reader));
	                            
	                            XMLReader parser = XMLReaderFactory.createXMLReader();
	                            
	                            parser.setErrorHandler(validator.getErrorHandler());
	                            parser.setContentHandler(validator);
	                            parser.parse(ilimodelsXmlFile.getAbsolutePath());
	                        } catch (Exception ex) {
	                            EhiLogger.logError("failed to validate "+IliManager.ILIMODELS_XML,ex);
	                        }
	                    }
	                    
	                }
                    ch.interlis.ilirepository.IliManager manager=new ch.interlis.ilirepository.IliManager();
                    ArrayList<String> modeldirv = new ArrayList<String>();
                    String ilidirs = settings.getValue(Ili2cSettings.ILIDIRS);
                    String modeldirs[] = ilidirs.split(Ili2cSettings.ILIDIR_SEPARATOR);
                    for(String m:modeldirs) {
                        if(!m.startsWith("%")) {
                            modeldirv.add(m);
                        }
                    }
                    manager.setRepositories((String[]) modeldirv.toArray(new String[1]));
	                // read file
	                IliFiles ilimodelsFiles=null;
	                List<ModelMetadata> ilimodelsEntries = null;
	                try {
	                    ilimodelsEntries = RepositoryAccess.readIliModelsXml2(ilimodelsXmlFile);
	                    ilimodelsEntries = RepositoryAccess.getLatestVersions2(ilimodelsEntries);
	                    ilimodelsFiles = RepositoryAccess.createIliFiles2(repos, ilimodelsEntries);
	                } catch (RepositoryAccessException e2) {
	                    EhiLogger.logError(e2);
	                    continue;
	                }
	                for(Iterator<IliFile> filei=ilimodelsFiles.iteratorFile();filei.hasNext();){
	                    IliFile ilimodelsFile=filei.next();
	                    EhiLogger.logState("check file <"+ilimodelsFile.getPath()+"> in <"+ilimodelsFile.getRepositoryUri()+">");
	                    List<String> modelsInFile=new ArrayList<String>();
                        ArrayList<String> requiredModels=new ArrayList<String>();
                        double iliversion=0.0;
	                    for(Iterator modeli=ilimodelsFile.iteratorModel();modeli.hasNext();){
	                        IliModel model=(IliModel)modeli.next();
	                        iliversion=model.getIliVersion();
	                        modelsInFile.add(model.getName());
	                        for(String reqModel:(Iterable<String>)model.getDependencies()) {
	                            if(reqModel.equals(PredefinedModel.INTERLIS)) {
	                                continue; // ignore here; report error later
	                            }
	                            if(!requiredModels.contains(reqModel)) {
	                                requiredModels.add(reqModel);
	                            }
	                        }
	                    }
	                    Iterator<String> requiredModelIt=requiredModels.iterator();
	                    for(;requiredModelIt.hasNext();) {
	                        String requiredModel=requiredModelIt.next();
	                        if(modelsInFile.contains(requiredModel)) {
	                            requiredModelIt.remove();
	                        }
	                    }
	                        Configuration fileconfig = new Configuration();
	                        try {
	                            File localIliFileInIliCache = reposAccess.getLocalFileLocation(ilimodelsFile.getRepositoryUri(),ilimodelsFile.getPath(),0,ilimodelsFile.getMd5());
	                            if(localIliFileInIliCache==null){
	                                EhiLogger.logError("File <"+ilimodelsFile.getPath()+"> not found");
	                                failedFiles.add(ilimodelsFile);
	                                continue;
	                            }
	                            File localIliFile=new File(tmpFolder,localIliFileInIliCache.getName());
	                            try {
	                                RepositoryAccess.copyFile(localIliFile,localIliFileInIliCache);
	                                
	                                fileconfig.setAutoCompleteModelList(false);
	                                if(requiredModels.size()>0){
	                                    // get list of required files based on ilimodels.xml entries
	                                    Configuration fconfig = manager.getConfig(requiredModels,iliversion);
	                                    Iterator fi = fconfig.iteratorFileEntry();
	                                    while (fi.hasNext()) {
	                                        fileconfig.addFileEntry((FileEntry) fi.next());
	                                    }
	                                }
                                    fileconfig.addFileEntry(new ch.interlis.ili2c.config.FileEntry(
                                            localIliFile.getAbsolutePath(),ch.interlis.ili2c.config.FileEntryKind.ILIMODELFILE));
                                    ch.interlis.ili2c.Ili2c.logIliFiles(fileconfig);
	                                fileconfig.setGenerateWarnings(false);
	                                TransferDescription td=Main.runCompiler(fileconfig,settings);
	                                if(td==null){
	                                    failedFiles.add(ilimodelsFile);
	                                }else{
	                                    // check entries in ilimodels.xml
	                                    String md5=RepositoryAccess.calcMD5(localIliFile);
	                                    for(Iterator<Model> modeli=td.iterator();modeli.hasNext();){
	                                        Model model=modeli.next();
	                                        if(model==td.INTERLIS){
	                                            continue;
	                                        }
	                                        if(model.getFileName()!=null && model.getFileName().equals(localIliFile.getAbsolutePath())){
	                                            EhiLogger.logState("check entry of model "+model.getName());
	                                            String csl=null;
	                                            if(model.getIliVersion().equals(Model.ILI1)){
	                                                csl=ModelMetadata.ili1;
	                                            }else if(model.getIliVersion().equals(Model.ILI2_2)){
	                                                csl=ModelMetadata.ili2_2;
	                                            }else if(model.getIliVersion().equals(Model.ILI2_3)){
	                                                csl=ModelMetadata.ili2_3;
	                                            }else if(model.getIliVersion().equals(Model.ILI2_4)){
	                                                csl=ModelMetadata.ili2_4;
	                                            }else{
	                                                throw new IllegalStateException("unexpected ili version");
	                                            }
	                                            ModelMetadata modelMetadata=RepositoryAccess.findModelMetadata2(ilimodelsEntries,model.getName(),csl);
	                                            if(modelMetadata==null){
	                                                inconsistentMetaEntry.add(new MetaEntryProblem(null,model.getName(),"entry missing or wrong model name in ilimodels.xml for "+ilimodelsFile.getPath()));
	                                            }else{
	                                                if(modelMetadata.getMd5()!=null && !modelMetadata.getMd5().equalsIgnoreCase(md5)){
	                                                    inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),"wrong md5 value; correct would be "+md5));
	                                                }
	                                                if(model.getIliVersion().equals(Model.ILI2_3) || model.getIliVersion().equals(Model.ILI2_4)){
	                                                    if(modelMetadata.getVersion()!=null && !modelMetadata.getVersion().equals(model.getModelVersion())){
	                                                        inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),"wrong version value; correct would be "+model.getModelVersion()));
	                                                    }
	                                                    if(modelMetadata.getVersionComment()!=null && !modelMetadata.getVersionComment().equals(model.getModelVersionExpl())){
	                                                        inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),"wrong versionComment value; correct would be "+model.getModelVersionExpl()));
	                                                    }
	                                                    if(modelMetadata.getIssuer()!=null && !modelMetadata.getIssuer().equals(model.getIssuer())){
	                                                        inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),"wrong issuer value; correct would be "+model.getIssuer()));                                          
	                                                    }
	                                                }
	                                                HashSet<String> depsMeta=new HashSet<String>();
	                                                HashSet<String> depsIli=new HashSet<String>();
	                                                for(String dep : modelMetadata.getDependsOnModel()){
	                                                    if(dep.equals(PredefinedModel.INTERLIS)) {
                                                            inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),PredefinedModel.INTERLIS+" must not be listed as dependsOnModel"));                                          
	                                                        continue;
	                                                    }
	                                                    depsMeta.add(dep);
	                                                }
	                                                String sep="";
	                                                StringBuilder missingDeps=new StringBuilder();
	                                                for(Model dep : model.getImporting()){
	                                                    String depIli=dep.getName();
	                                                    depsIli.add(depIli);
	                                                    if(!depIli.equals("INTERLIS") && !depsMeta.contains(depIli)){
	                                                        missingDeps.append(sep);
	                                                        missingDeps.append(depIli);
	                                                        sep=",";
	                                                    }
	                                                }
	                                                if(missingDeps.length()>0){
	                                                    inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getOid(),model.getName(),"wrong depends list; misssing models "+missingDeps.toString()));
	                                                }
	                                            }
	                                        }
	                                    }
	                                }
	                            } catch (Ili2cException e1) {
	                                EhiLogger.logError(e1);
	                                failedFiles.add(ilimodelsFile);
                                }finally {
	                                localIliFile.delete();
	                            }
	                        } catch (RepositoryAccessException e1) {
	                            EhiLogger.logError(e1);
	                            failedFiles.add(ilimodelsFile);
	                        }

	                }
	            }
	        }
		}finally {
		 tmpFolder.delete();   
		}
		if(inconsistentMetaEntry.size()>0){
			Collections.sort(inconsistentMetaEntry,new Comparator<MetaEntryProblem>(){
				public int compare(MetaEntryProblem arg0, MetaEntryProblem arg1) {
					int c1=arg0.getModelName().compareTo(arg1.getModelName());
					if(c1==0){
						if(arg0.getTid()==null){
							if(arg1.getTid()==null){
								return 0;
							}
							return -1;
						}
						if(arg1.getTid()==null){
							return 1;
						}
						return arg0.getTid().compareTo(arg1.getTid());
					}
					return c1;
				}

				
			});
			for(MetaEntryProblem prb:inconsistentMetaEntry){
				if(prb.getTid()==null){
					EhiLogger.logError(prb.getModelName()+": "+prb.getMsg());
				}else{
					EhiLogger.logError(prb.getModelName()+"(TID=\""+prb.getTid()+"\"): "+prb.getMsg());
				}
			}
		}
		if(failedFiles.size()!=0){
			StringBuilder failed=new StringBuilder();
			String sep="";
			for(IliFile f:failedFiles){
				failed.append(sep);
				failed.append(f.getPath());
				sep=", ";
			}
			EhiLogger.logError("compile failed with files: "+failed);
			if(validationErrors){
				EhiLogger.logError("syntax errors in "+IliManager.ILIMODELS_XML);
			}
		}
		return !failedFiles.isEmpty() || !inconsistentMetaEntry.isEmpty();
	}

}
