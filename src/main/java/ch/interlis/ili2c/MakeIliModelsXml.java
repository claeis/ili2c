package ch.interlis.ili2c;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.tools.StringUtility;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.Ili2cMetaAttrs;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.ilirepository.impl.RepositoryAccess;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;
import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata_SchemaLanguage;
import ch.ehi.iox.ilisite.IliRepository09.ModelName_;
import ch.ehi.iox.ilisite.ILIREPOSITORY09;

/** Tool to create an ilimodels.xml file in a given local folder tree.
 * update (default) md5 und dependsOnModel fuer bestehende Modelle nachfuehren
 *        neue Modelle ergaenzen (alle Infos)
 *        nicht mehr existierende Modelle loeschen
 * updateAll alle Infos fuer bestehende Modelle aus dem ili uebernehmen 
 * rewrite bestehende Datei zuerst loeschen und dann neu scannen und erzeugen
 * @author ceis
 */
public class MakeIliModelsXml {
	public static final String DEFAULT_ILIDIRS="http://models.interlis.ch/;"+UserSettings.JAR_DIR;
	/** name of application as shown to user.
	 */
	public static final String APP_NAME="mkilimodelsxml";

	private String version=null;
	private String ilidirs=DEFAULT_ILIDIRS;

	private long newtid=0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MakeIliModelsXml().mymain(args);
	}
	public void mymain(String[] args) {
		
		String httpProxyHost=null;
		String httpProxyPort=null;
		try {
			String outFile=null;
			int argi=0;
			for(;argi<args.length;argi++){
				String arg=args[argi];
				if (args[argi].equals("--proxy"))
				{
					argi++;
					httpProxyHost=args[argi];
					continue;
				}
				if (args[argi].equals("--proxyPort"))
				{
					argi++;
					httpProxyPort=args[argi];
					continue;
				}
				if (args[argi].equals("--ilidirs"))
				{
					argi++;
					ilidirs=args[argi];
					continue;
				}
				if(arg.equals("--trace")){
					EhiLogger.getInstance().setTraceFilter(false); 
				}else if (arg.equals("--quiet")){
					  ch.ehi.basics.logging.StdListener.getInstance().skipInfo(true);
					  continue;
				}else if(arg.equals("--version")){
					printVersion();
					return;
				}else if(arg.equals("--o")){
						argi++;
						outFile=args[argi];
						continue;
				}else if(arg.equals("--help")){
						printHelp();
						return;
					
				}else if(arg.startsWith("-")){
					EhiLogger.logAdaption(arg+": unknown option; ignored");
				}else{
					break;
				}
			}
			if(argi==args.length){
				printHelp();
				return;
			}
			if(argi+1!=args.length){
				printHelp();
				return;
			}
			String repositoryRoot=args[argi];
			
			if(outFile==null){
				outFile=repositoryRoot+java.io.File.separator+"ilimodels.xml";
			}
			
			// if file exists
			HashMap<String,ArrayList<ModelMetadata>> oldfiles=null;
			java.io.File out=new java.io.File(outFile);
			if(out.exists()){
				// read existing file
				oldfiles=readIliModelsXml(out);
			}
			if(oldfiles==null){
				oldfiles=new HashMap<String,ArrayList<ModelMetadata>>();
			}
			
			// scan folder
			ArrayList<ModelMetadata> models=new ArrayList<ModelMetadata>(scanIliFileDir(new File(repositoryRoot)));
			HashMap<String,ArrayList<ModelMetadata>> newfiles=createFilelist(models);
			
			// first pass
			HashSet<ModelMetadata> newModels=new HashSet<ModelMetadata>();
			if(true){
					ArrayList<ModelMetadata> updatedModels=new ArrayList<ModelMetadata>();
					Iterator modeli=models.iterator();
					while(modeli.hasNext()){
						ModelMetadata model=(ModelMetadata)modeli.next();
						ModelMetadata theOldModel=null;
						// if model exists in oldfilelist
						if(oldfiles.containsKey(model.getFile())){
							// use old entry
							// update it
							ArrayList<ModelMetadata> oldmodels=oldfiles.get(model.getFile());
							for(ModelMetadata oldmodel : oldmodels){
								if(oldmodel.getName().equals(model.getName())){
									theOldModel=oldmodel;
									break;
								}
							}
							if(theOldModel!=null){
								theOldModel.setmd5(model.getmd5());
								theOldModel.setattrundefined("dependsOnModel");
								ModelName_[] deps=model.getdependsOnModel();
								for(ModelName_ dep:deps){
									theOldModel.adddependsOnModel(dep);
								}
								model=theOldModel;
							}else{
								// new entry (but in a old file)
								newModels.add(model);
								// give it a new tid
								model.setobjectoid(Long.toString(newtid));
								newtid++;
							}
						}else{
							// new entry
							newModels.add(model);
							// give it a new tid
							model.setobjectoid(Long.toString(newtid));
							newtid++;
						}
						updatedModels.add(model);
					}
					models=updatedModels;
			}
			
			// second pass
			{
				// create temp IliFiles
				HashMap<String, ArrayList<ModelMetadata>> files=createFilelist(newModels);
				for(String theNewFile : files.keySet()){
					IliFiles tempIliFiles=RepositoryAccess.createIliFiles(repositoryRoot,newModels);
					// compile
					UserSettings settings = new UserSettings();
					settings.setHttpProxyHost(httpProxyHost);
					settings.setHttpProxyPort(httpProxyPort);
					settings.setIlidirs(repositoryRoot+";"+ilidirs);
					settings.setTransientObject(UserSettings.TEMP_REPOS_ILIFILES, tempIliFiles);
					settings.setTransientObject(UserSettings.TEMP_REPOS_URI, repositoryRoot);
					Configuration config = new Configuration();
					FileEntry file = new FileEntry(new File(repositoryRoot,theNewFile).getAbsolutePath(),
							FileEntryKind.ILIMODELFILE);
					config.addFileEntry(file);
					config.setAutoCompleteModelList(true);
					config.setGenerateWarnings(false);
					config.setOutputKind(GenerateOutputKind.NOOUTPUT);

					// compile models
					TransferDescription td = ch.interlis.ili2c.Main.runCompiler(config, settings);
					if (td == null) {
						// compiler failed; skip file
					}else{
						// update all model infos of this file
						ArrayList<ModelMetadata> modelv=files.get(theNewFile);
						for(ModelMetadata model: modelv){
							// find model in td
							ch.interlis.ili2c.metamodel.Model modelDef=(ch.interlis.ili2c.metamodel.Model)td.getElement(ch.interlis.ili2c.metamodel.Model.class, model.getName());
							if(modelDef!=null){
								String title=modelDef.getDocumentation();
								if(title!=null){
									int titleEnd=title.indexOf('.');
									if(titleEnd>0){
										String doc=StringUtility.purge(title.substring(titleEnd+1));
										title=StringUtility.purge(title.substring(0,titleEnd+1));
										if(doc!=null){
											model.setshortDescription(doc);
										}
										if(title!=null){
											model.setTitle(title);
										}
									}else{
										model.setTitle(title);
									}
								}
								String technicalContact=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_TECHNICAL_CONTACT);
								if(technicalContact!=null){
									model.settechnicalContact(technicalContact);
								}
								String precursorVersion=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_PRECURSOR_VERSION);
								if(precursorVersion!=null){
									model.setprecursorVersion(precursorVersion);
								}
								String furtherInfo=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_FURTHER_INFORMATION);
								if(furtherInfo!=null){
									model.setfurtherInformation(furtherInfo);
								}
								String furtherMetadata=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_FURTHER_METADATA);
								if(furtherMetadata!=null){
									model.setfurtherMetadata(furtherMetadata);
								}
								String idGeoIV=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_ID_GEO_IV);
								String tags=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_TAGS);
								if(tags==null){
									tags=idGeoIV;
								}else if(idGeoIV!=null){
									tags=tags+","+idGeoIV;
								}
								if(tags!=null){
									model.setTags(tags);
								}
								String original=modelDef.getMetaValue(Ili2cMetaAttrs.ILIMODELSXML_ORIGINAL);
								if(original!=null){
									model.setOriginal(original);
								}
								if(model.getSchemaLanguage().equals(ModelMetadata_SchemaLanguage.ili2_3)){
									String issuer=modelDef.getIssuer();
									model.setIssuer(issuer);
									String version=modelDef.getModelVersion();
									model.setVersion(version);
									String versionExpl=modelDef.getModelVersionExpl();
									if(versionExpl!=null){
										model.setVersionComment(versionExpl);
									}
								}
								
							}
						}
					}
					
				}
			}
			
			// write metadata
			if(true){
				java.io.OutputStream outStream=null;
				XtfWriterBase ioxWriter=null;
				try{
					outStream=new java.io.FileOutputStream(outFile);
					ioxWriter = new XtfWriterBase( outStream,  ILIREPOSITORY09.getIoxMapping(),"2.3");
					ioxWriter.setModels(new XtfModel[]{ILIREPOSITORY09.getXtfModel()});
					StartTransferEvent startTransferEvent = new StartTransferEvent();
					startTransferEvent.setSender( APP_NAME+"-"+getVersion() );
					ioxWriter.write( startTransferEvent );
					StartBasketEvent startBasketEvent = new StartBasketEvent( ILIREPOSITORY09.RepositoryIndex, "b1");
					ioxWriter.write( startBasketEvent );
					Iterator modeli=models.iterator();
					while(modeli.hasNext()){
						ModelMetadata model=(ModelMetadata)modeli.next();
						ioxWriter.write(new ObjectEvent(model));
					}
					
					ioxWriter.write( new EndBasketEvent() );
					ioxWriter.write( new EndTransferEvent() );
					
					ioxWriter.flush();
				}catch(java.io.FileNotFoundException ex){
					EhiLogger.logError(ex);
					System.exit(1);
				}finally{
					if(ioxWriter!=null){
						ioxWriter.close();
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
			}
		} catch (Exception ex) {
			EhiLogger.logError(APP_NAME+": failed",ex);
			System.exit(1);
		}
	}
	/** scans a directory for ili-files.
	 * @param startdir directory to scan
	 * @return set<ModelMetadata>
	 */
	public static HashSet<ModelMetadata> scanIliFileDir(File startdir)
	throws IOException
	{
		int oid=1;
		if(!startdir.isDirectory()){
			throw new IllegalArgumentException(startdir+" is not a folder/directory");
		}
		java.net.URI starturi=startdir.toURI();
		HashSet<ModelMetadata> ret=new HashSet<ModelMetadata>();
		ArrayList<File> dirs=new ArrayList<File>();
		dirs.add(startdir);
		ch.ehi.basics.view.GenericFileFilter filter=new ch.ehi.basics.view.GenericFileFilter("INTERLIS models (*.ili)","ili");
		while(!dirs.isEmpty()){
			File dir=dirs.remove(0);
			File filev[]=dir.listFiles();
			for(int i=0;i<filev.length;i++){
				if(filev[i].isDirectory()){
					dirs.add(filev[i]);
					continue;
				}
				if(filter.accept(filev[i])){
					IliFile iliFile=ch.interlis.ili2c.ModelScan.scanIliFile(filev[i]);
					if(iliFile!=null){
						java.net.URI relPath=starturi.relativize(filev[i].toURI());
						String md5=RepositoryAccess.calcMD5(filev[i]);
						String fileVersion = getFileVersion(filev[i]);
						Iterator<IliModel> modeli=iliFile.iteratorModel();
						while(modeli.hasNext()){
							IliModel iliModel=modeli.next();
							ModelMetadata model=new ModelMetadata(Integer.toString(oid++));
							model.setFile(relPath.toString());
							model.setName(iliModel.getName());
							double cslVersion=iliModel.getIliVersion();
							if(cslVersion==1.0){
								model.setSchemaLanguage(ModelMetadata_SchemaLanguage.ili1);
							}else if(cslVersion==2.2){
								model.setSchemaLanguage(ModelMetadata_SchemaLanguage.ili2_2);
							}else if(cslVersion==2.3){
								model.setSchemaLanguage(ModelMetadata_SchemaLanguage.ili2_3);
							}else{
								throw new IllegalStateException("unexpected ili version");
							}
							model.setpublishingDate(fileVersion);
							model.setVersion(fileVersion);
							model.setmd5(md5);
							Iterator<String> depi=iliModel.getDependencies().iterator();
							while(depi.hasNext()){
								String depModelName=depi.next();
								ModelName_ depModel=new ModelName_();
								depModel.setvalue(depModelName);
								model.adddependsOnModel(depModel);
							}
							ret.add(model);
						}
					}
				}
			}
		}
		return ret;
	}
	private static String getFileVersion(File file) {
		String fileVersion= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(file.lastModified()));
		String fileName=ch.ehi.basics.view.GenericFileFilter.stripFileExtension(file.getName());
		if(fileName.length()>=10){
			String dateString = fileName.substring(fileName.length()-10);
			java.util.Date date=null;
			date=new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateString,new ParsePosition(0));
			if(date==null){
				date=new java.text.SimpleDateFormat("yyyyMMdd").parse(dateString,new ParsePosition(2));
			}
			if(date!=null){
				fileVersion= new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
			}
		}
		
		return fileVersion;
	}
	private void printHelp() {
		printVersion ();
		System.err.println();
		printDescription ();
		System.err.println();
		printUsage ();
		System.err.println();
		System.err.println("OPTIONS");
		System.err.println();
		//System.err.println("-gui                  start GUI.");
		System.err.println("--o filename           name of outputfile.");
		System.err.println("--trace                enable trace messages.");
  	    System.err.println("--quiet               Suppress info messages.");
  	  System.err.println("--ilidirs "+ilidirs+" list of directories with ili-files.");
	  System.err.println("--proxy host          proxy server to access model repositories.");
	  System.err.println("--proxyPort port      proxy port to access model repositories.");
		System.err.println("--help                 Display this help text.");
		System.err.println("--version              Display the version of "+APP_NAME);
		System.err.println();
	}
	protected void printVersion ()
	{
	  System.err.println(APP_NAME+", Version "+getVersion());
	  System.err.println("  Developed by Eisenhut Informatik AG, CH-3401 Burgdorf");
	}


	protected void printDescription ()
	{
	  System.err.println("DESCRIPTION");
	  System.err.println("  Reads INTERLIS models and generates a suitable ilimodels.xml file (according to IliRepository09.");
	}


	protected void printUsage()
	{
	  System.err.println ("USAGE");
	  System.err.println("  java -cp "+ch.interlis.ili2c.Main.APP_JAR+" "+MakeIliModelsXml.class.getName()+" [Options] folder-with-ili-files");
	}
	/** get version of program.
	 * @return version e.g. "1.0.0"
	 */
	public String getVersion() {
		  if(version==null){
		      version=TransferDescription.getVersion();
		  }
		  return version;
	}

	/** read an ilimodels.xml file
	 * 
	 * @param uri uri of the repository without ilimodels.xml
	 * @return null if the repository doesn't exist
	 */
	private HashMap<String,ArrayList<ModelMetadata>> readIliModelsXml(File file)
	//throws RepositoryAccessException
	{
		if(file==null){
			return null;
		}
		// read file
		ArrayList<ModelMetadata> models=new ArrayList<ModelMetadata>(); // array<ModelMetadata>
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
						 models.add(model);
							try {
								long tidInt=Long.parseLong(model.getobjectoid());
								if(tidInt>newtid){
									newtid=tidInt;
								}
							} catch (NumberFormatException e) {
								// ignore it
							}
					 }
				 }
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
		} catch (IoxException e) {
			throw new IllegalStateException(e);
		}finally{
			newtid=((newtid/10)+1)*10; // start with next 10
			if(reader!=null){
				try {
					reader.close();
				} catch (IoxException e) {
					throw new IllegalStateException(e);
				}
				reader=null;
			}
		}
		HashMap<String, ArrayList<ModelMetadata>> files = createFilelist(models);
		return files;
	}
	private HashMap<String, ArrayList<ModelMetadata>> createFilelist(
			java.util.Collection<ModelMetadata> models) {
		HashMap<String,ArrayList<ModelMetadata>> files=new HashMap<String,ArrayList<ModelMetadata>>();
		Iterator<ModelMetadata> modeli=models.iterator();
		while(modeli.hasNext()){
			ModelMetadata model=modeli.next();
				String filename=model.getFile();
				ArrayList<ModelMetadata> modelv=null;
				if(!files.containsKey(filename)){
					modelv=new ArrayList<ModelMetadata>();
					files.put(filename, modelv);
				}else{
					modelv=files.get(filename);
				}
				modelv.add(model);
		}
		return files;
	}
}
