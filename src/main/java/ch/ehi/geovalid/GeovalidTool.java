package ch.ehi.geovalid;

import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;








import java.util.Locale;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.itf.ItfReader;
import ch.interlis.iom_j.itf.ItfReader2;
import ch.interlis.iom_j.itf.ModelUtilities;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox_j.IoxInvalidDataException;
import ch.interlis.iox_j.ObjectEvent;

public class GeovalidTool {
	public static final String SETTING_ILIDIRS="ch.ehi.geovalid.ilidirs";
	public static final String SETTING_DIRUSED="ch.ehi.geovalid.dirused";
	public static final String ITF_DIR="%ITF_DIR";
	public static final String JAR_DIR="%JAR_DIR";
	/** name of application as shown to user.
	 */
	public static final String APP_NAME="geovalid";
	/** name of jar file.
	 */
	public static final String APP_JAR="geovalid.jar"; 
	private static String version=null;

	public static void main(String[] args) {
		GeovalidTool p=new GeovalidTool();
		p.mymain(args);
	}
	public void mymain(String[] args) {
		Settings settings=new Settings();
		settings.setValue(SETTING_ILIDIRS, ITF_DIR+";http://models.interlis.ch/;"+JAR_DIR);
		if(false && args.length==0){
			readSettings(settings);
			//ch.ehi.xtfcheck.gui.Gui.run(settings);
			return;
		}
		String logfile=null;
		int argi=0;
		boolean doGui=false;
		for(;argi<args.length;argi++){
			String arg=args[argi];
			if(arg.equals("--trace")){
				EhiLogger.getInstance().setTraceFilter(false); 
			}else if(false && arg.equals("--gui")){
				readSettings(settings);
				doGui=true;
			}else if(arg.equals("--version")){
				printVersion();
				return;
			}else if(arg.equals("--ilidirs")){
					argi++;
					settings.setValue(SETTING_ILIDIRS, args[argi]);
					continue;
			}else if(arg.equals("--log")){
				argi++;
				logfile=args[argi];
				continue;
			}else if(arg.equals("--help")){
					printHelp(settings);
					return;
			}else if(arg.startsWith("-")){
				EhiLogger.logAdaption(arg+": unknown option; ignored");
			}else{
				break;
			}
		}
		if(false && doGui){
			//ch.ehi.xtfcheck.gui.Gui.run(settings);
			return;
		}
		if(argi==args.length){
			printHelp(settings);
			return;
		}
		String itffile=args[argi++];
		run(itffile,logfile,settings);
	}
	private final static String SETTINGS_FILE = System.getProperty("user.home") + "/.geovalid";
	public static void readSettings(Settings settings)
	{
		java.io.File file=new java.io.File(SETTINGS_FILE);
		try{
			if(file.exists()){
				settings.load(file);
			}
		}catch(java.io.IOException ex){
			EhiLogger.logError("failed to load settings from file "+SETTINGS_FILE,ex);
		}
	}
	public static void writeSettings(Settings settings)
	{
		java.io.File file=new java.io.File(SETTINGS_FILE);
		try{
			settings.store(file,"geovalid settings");
		}catch(java.io.IOException ex){
			EhiLogger.logError("failed to settings settings to file "+SETTINGS_FILE,ex);
		}
	}
	public void run(String itffile,String logfile,Settings settings)
	{	
		ch.ehi.basics.logging.ErrorTracker tracker=new ch.ehi.basics.logging.ErrorTracker();
		EhiLogger.getInstance().addListener(tracker);
		try{
			try{
				EhiLogger.logState("validating...");
				prun(itffile,logfile,settings);
			}catch(Throwable ex){
				EhiLogger.logError(ex);
			}
			if(tracker.hasSeenErrors()){
				EhiLogger.logState("validating...failed");
			}else{
				EhiLogger.logState("validating...succeeded");
			}
		}finally{
			EhiLogger.getInstance().removeListener(tracker);
		}
	}
	private void prun(String itffile,String logfile,Settings settings) throws IoxException
	{	
		ch.ehi.basics.logging.FileListener logger=null;
		if(logfile!=null){
			logger=new ch.ehi.basics.logging.FileListener(new java.io.File(logfile),true);
			EhiLogger.getInstance().addListener(logger);
		}
		EhiLogger.logState(APP_NAME+"-"+getVersion());
		EhiLogger.logState("ili2c-"+ch.interlis.ili2c.Ili2c.getVersion());
		EhiLogger.logState("itffile <"+itffile+">");
		if(logfile!=null){
			EhiLogger.logState("logfile <"+logfile+">");
		}
		ArrayList modeldirv=new ArrayList();
		String ilidirs=settings.getValue(SETTING_ILIDIRS);
		EhiLogger.logState("ilidirs <"+ilidirs+">");
		String modeldirs[]=ilidirs.split(";");
		HashSet ilifiledirs=new HashSet();
		for(int modeli=0;modeli<modeldirs.length;modeli++){
			String m=modeldirs[modeli];
			if(m.equals(ITF_DIR)){
				String ilifile=itffile;
				m=new java.io.File(ilifile).getAbsoluteFile().getParentFile().getAbsolutePath();
				if(m!=null && m.length()>0){
					if(!modeldirv.contains(m)){
						modeldirv.add(m);				
					}
				}
			}else if(m.equals(JAR_DIR)){
				m=getAppHome();
				if(m!=null){
					m=new java.io.File(m,"ilimodels").getAbsolutePath();
				}
				if(m!=null && m.length()>0){
					modeldirv.add(m);				
				}
			}else{
				if(m!=null && m.length()>0){
					modeldirv.add(m);				
				}
			}
		}		
		
		TransferDescription td=null;
		ch.interlis.ili2c.config.Configuration ili2cConfig=null;
		ArrayList<String> modelv=new ArrayList<String>();
		{
			String xmlfile=itffile;
			ArrayList<String> models;
			try {
				models = getModels(new java.io.File(itffile));
			} catch (IoxException ex) {
				EhiLogger.logError("failed to detect model of file "+xmlfile,ex);
				return;
			}
			if(models.size()==0){
				EhiLogger.logError("failed to detect model of file "+xmlfile);
				return;
			}
			modelv.addAll(models);
		}
		try {
			//ili2cConfig=ch.interlis.ili2c.ModelScan.getConfig(modeldirv, modelv);
			ch.interlis.ilirepository.IliManager modelManager=new ch.interlis.ilirepository.IliManager();
			modelManager.setRepositories((String[])modeldirv.toArray(new String[]{}));
			ili2cConfig=modelManager.getConfig(modelv, 0.0);
			ili2cConfig.setGenerateWarnings(false);
		} catch (Ili2cException ex) {
			EhiLogger.logError(ex);
			return;
		}

		try {
			ch.interlis.ili2c.Ili2c.logIliFiles(ili2cConfig);
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		} catch (Ili2cFailure ex) {
			EhiLogger.logError(ex);
			return;
		}

		java.util.HashMap itfNames=ch.interlis.iom_j.itf.ModelUtilities.getTagMap(td);
		ItfReader2 reader=null;
	    long startTime=System.currentTimeMillis();
		try{
			reader=new ItfReader2(new java.io.File(itffile),false);
			reader.setModel(td);
			IoxEvent event;
			String currentTablename=null;
			StartBasketEvent basket=null;
			do{
				event=reader.read();
				if(event instanceof StartBasketEvent){
					basket=(StartBasketEvent)event;
					EhiLogger.logState("Basket "+basket.getType()+"(oid "+basket.getBid()+")...");
				}else if(event instanceof EndBasketEvent){
		        	ArrayList<IoxInvalidDataException> dataerrs = reader.getDataErrs();
		        	if(dataerrs.size()>0){
		        		for(IoxInvalidDataException dataerr:dataerrs){
		        			EhiLogger.logError(dataerr);
		        		}
		        		reader.clearDataErrs();
		        	}
				}
			}while(!(event instanceof ch.interlis.iox.EndTransferEvent));
		}finally{
			if(reader!=null){
				reader.close();
			}
			reader=null;
			if(logger!=null){
				EhiLogger.getInstance().removeListener(logger);
				logger.close();
			}
			logger=null;
		}
	    long endTime=System.currentTimeMillis();
	      DateFormat dateFormatter= DateFormat.getDateTimeInstance(DateFormat.SHORT,
                  DateFormat.SHORT,
                  Locale.getDefault());
	    EhiLogger.traceState("duration "+(endTime-startTime)+" ms");
		
		
	}
	
	public static ArrayList<String> getModels(java.io.File itffile)
	throws ch.interlis.iox.IoxException
{
	ArrayList<String> ret=new ArrayList<String>();
	ItfReader reader=null;
	try{
		reader=new ch.interlis.iom_j.itf.ItfReader(itffile);
		IoxEvent event;
		while((event=reader.read())!=null){
			if(event instanceof StartBasketEvent){
				String topic=((StartBasketEvent)event).getType();
				String model[]=topic.split("\\.");
				ret.add(model[0]);
				return ret;
			}
		}
	}finally{
		if(reader!=null){
			reader.close();
		}
		reader=null;
	}
	return ret;
}

	private static void printHelp(Settings settings) {
		printVersion ();
		System.err.println();
		printDescription ();
		System.err.println();
		printUsage ();
		System.err.println();
		System.err.println("OPTIONS");
		System.err.println();
		//System.err.println("--gui                  start GUI.");
		System.err.println("--ilidirs "+settings.getValue(SETTING_ILIDIRS)+" list of directories with ili-files.");
		System.err.println("--log filename         log message to given file.");
		System.err.println("--trace                enable trace messages.");
		System.err.println("--help                 Display this help text.");
		System.err.println("--version              Display the version of "+APP_NAME+".");
		System.err.println();
	}
	protected static void printVersion ()
	{
	  System.err.println(APP_NAME+", Version "+getVersion());
	  System.err.println("  Developed by Eisenhut Informatik AG, CH-3401 Burgdorf");
	}


	protected static void printDescription ()
	{
	  System.err.println("DESCRIPTION");
	  System.err.println("  Validates INTERLIS transfer files.");
	}


	protected static void printUsage()
	{
	  System.err.println ("USAGE");
	  System.err.println("  java -jar "+APP_JAR+" [Options] data.itf");
	}
	/** get version of program.
	 * @return version e.g. "adddefval-1.0.0"
	 */
	public static String getVersion() {
		  if(version==null){
		java.util.ResourceBundle resVersion = java.util.ResourceBundle.getBundle(ch.ehi.basics.i18n.ResourceBundle.class2qpackageName(GeovalidTool.class)+"/Version");
			// Major version numbers identify significant functional changes.
			// Minor version numbers identify smaller extensions to the functionality.
			// Micro versions are even finer grained versions.
			StringBuffer ret=new StringBuffer(20);
		ret.append(resVersion.getString("versionMajor"));
			ret.append('.');
		ret.append(resVersion.getString("versionMinor"));
			ret.append('.');
		ret.append(resVersion.getString("versionMicro"));
			ret.append('-');
		ret.append(resVersion.getString("versionDate"));
			version=ret.toString();
		  }
		  return version;
	}
	static public String getAppHome()
	{
	  String classpath = System.getProperty("java.class.path");
	  int index = classpath.toLowerCase().indexOf(APP_JAR);
	  int start = classpath.lastIndexOf(java.io.File.pathSeparator,index) + 1;
	  if(index > start)
	  {
		  return classpath.substring(start,index - 1);
	  }
	  return null;
	}
}
