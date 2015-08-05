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
			UserSettings settings) {
		
		Main.setHttpProxySystemProperties(settings);
				
		HashSet<IliFile> failedFiles=new HashSet<IliFile>();
		ArrayList<MetaEntryProblem> inconsistentMetaEntry=new ArrayList<MetaEntryProblem>();
		Iterator reposi = config.iteratorFileEntry();
		while (reposi.hasNext()) {
			FileEntry e = (FileEntry) reposi.next();
			if (e.getKind() == FileEntryKind.ILIMODELFILE) {
				String repos = e.getFilename();
				// get list of current files in repository
				RepositoryAccess reposAccess=new RepositoryAccess();
				File ilimodelsFile;
				try {
					ilimodelsFile = reposAccess.getLocalFileLocation(repos,IliManager.ILIMODELS_XML,0,null);
				} catch (RepositoryAccessException e2) {
					EhiLogger.logError(e2);
					continue;
				}
				if(ilimodelsFile==null){
					EhiLogger.logAdaption("URL <"+repos+"> contains no "+IliManager.ILIMODELS_XML+"; ignored");
					continue;
				}
				// xsd validate file
				{
				    javax.xml.transform.Source schemaFiles[] =  null;
					try{
				    javax.xml.transform.Source schemaFiles2[] =  {
					    		new StreamSource(getClass().getResource( "/IliRepository09.xsd" ).toString())
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
							parser.parse(ilimodelsFile.getAbsolutePath());
						} catch (Exception ex) {
							EhiLogger.logError("failed to validate "+IliManager.ILIMODELS_XML,ex);
						}
				    }
					
				}
				// read file
				ArrayList<ModelMetadata> modelMetadatav = RepositoryAccess.readIliModelsXml(ilimodelsFile);
				modelMetadatav = RepositoryAccess.getLatestVersions(modelMetadatav);
				IliFiles files;
				try {
					files = RepositoryAccess.createIliFiles(repos, modelMetadatav);
				} catch (RepositoryAccessException e2) {
					EhiLogger.logError(e2);
					continue;
				}
				for(Iterator<IliFile> filei=files.iteratorFile();filei.hasNext();){
					IliFile file=filei.next();
					ArrayList<String> ilimodels=new ArrayList<String>();
					for(Iterator modeli=file.iteratorModel();modeli.hasNext();){
						IliModel model=(IliModel)modeli.next();
						ilimodels.add(model.getName());
					}
						Configuration fileconfig = new Configuration();
						try {
							File iliFile = reposAccess.getLocalFileLocation(file.getRepositoryUri(),file.getPath(),0,file.getMd5());
							if(iliFile==null){
								failedFiles.add(file);
								continue;
							}
							fileconfig.addFileEntry(new ch.interlis.ili2c.config.FileEntry(
									  iliFile.getPath(),ch.interlis.ili2c.config.FileEntryKind.ILIMODELFILE));
							fileconfig.setAutoCompleteModelList(true);
							fileconfig.setGenerateWarnings(false);
							TransferDescription td=Main.runCompiler(fileconfig,settings);
							if(td==null){
								failedFiles.add(file);
							}else{
								// check entries in ilimodels.xml
								String md5=RepositoryAccess.calcMD5(iliFile);
								for(Iterator<Model> modeli=td.iterator();modeli.hasNext();){
									Model model=modeli.next();
									if(model==td.INTERLIS){
										continue;
									}
									if(model.getFileName()!=null && model.getFileName().equals(iliFile.getAbsolutePath())){
										EhiLogger.logState("check model "+model.getFileName());
										ModelMetadata_SchemaLanguage csl=null;
										if(model.getIliVersion().equals(Model.ILI1)){
											csl=ModelMetadata_SchemaLanguage.ili1;
										}else if(model.getIliVersion().equals(Model.ILI2_2)){
											csl=ModelMetadata_SchemaLanguage.ili2_2;
										}else if(model.getIliVersion().equals(Model.ILI2_3)){
											csl=ModelMetadata_SchemaLanguage.ili2_3;
										}else if(model.getIliVersion().equals(Model.ILI2_4)){ // TODO
											throw new IllegalStateException("unexpected ili version");
										}else{
											throw new IllegalStateException("unexpected ili version");
										}
										ModelMetadata modelMetadata=getModelMetadata(modelMetadatav,model.getName(),csl);
										if(modelMetadata==null){
											inconsistentMetaEntry.add(new MetaEntryProblem(null,model.getName(),"entry missing or wrong model name in ilimodels.xml for "+file.getPath()));
										}else{
											if(modelMetadata.getmd5()!=null && !modelMetadata.getmd5().equalsIgnoreCase(md5)){
												inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getobjectoid(),model.getName(),"wrong md5 value; correct would be "+md5));
											}
											if(model.getIliVersion().equals(Model.ILI2_3)){
												if(modelMetadata.getVersion()!=null && !modelMetadata.getVersion().equals(model.getModelVersion())){
													inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getobjectoid(),model.getName(),"wrong version value; correct would be "+model.getModelVersion()));
												}
												if(modelMetadata.getVersionComment()!=null && !modelMetadata.getVersionComment().equals(model.getModelVersionExpl())){
													inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getobjectoid(),model.getName(),"wrong versionComment value; correct would be "+model.getModelVersionExpl()));
												}
												if(modelMetadata.getIssuer()!=null && !modelMetadata.getIssuer().equals(model.getIssuer())){
													inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getobjectoid(),model.getName(),"wrong issuer value; correct would be "+model.getIssuer()));											
												}
											}
											HashSet<String> depsMeta=new HashSet<String>();
											HashSet<String> depsIli=new HashSet<String>();
											for(ModelName_ dep : modelMetadata.getdependsOnModel()){
												depsMeta.add(dep.getvalue());
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
												inconsistentMetaEntry.add(new MetaEntryProblem(modelMetadata.getobjectoid(),model.getName(),"wrong depends list; misssing models "+missingDeps.toString()));
											}
										}
									}
								}
							}
						} catch (RepositoryAccessException e1) {
							EhiLogger.logError(e1);
							failedFiles.add(file);
						}

				}
			}
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
		return failedFiles.size()!=0;
	}

	private static ModelMetadata getModelMetadata(
			ArrayList<ModelMetadata> modelMetadatav, String name,
			ModelMetadata_SchemaLanguage csl) {
		for(ModelMetadata modelMetadata :  modelMetadatav){
			if(modelMetadata.getName().equals(name) && modelMetadata.getSchemaLanguage().equals(csl)){
				return modelMetadata;
			}
		}
		return null;
	}

}
