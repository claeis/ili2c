package ch.interlis.ili2c.gui;

import java.io.*;

public class UserSettings extends ch.ehi.basics.settings.Settings {
	// default Settings filename
	public final static String SETTINGS_FILE = System.getProperty("user.home") + "/.ili2c";

	public final static String WORKING_DIRECTORY = "WORKING_DIRECTORY";
    public static final String ILI_LANGUAGE_VERSION = "ch.interlis.ili2c.iliLangVersion";
	public final static String ILIDIRS = "ch.interlis.ili2c.ilidirs";
	public final static String HTTP_PROXY_HOST = "ch.interlis.ili2c.http_proxy_host";
	public final static String HTTP_PROXY_PORT = "ch.interlis.ili2c.http_proxy_port";
	public final static String TEMP_REPOS_URI = "ch.interlis.ili2c.tempReposUri";
	public final static String TEMP_REPOS_ILIFILES = "ch.interlis.ili2c.tempReposIliFiles";
	public final static String CUSTOM_ILI_RESOLVER = "ch.interlis.ili2c.customIliResolver";
	public final static String ILIDIRS_PATHMAP = "ch.interlis.ili2c.pathMap";
    public final static String CUSTOM_ILI_MANAGER = "ch.interlis.ili2c.customIliManager";
    
    public static final String ILI_DIR = "%ILI_DIR";
    public static final String JAR_DIR = "%JAR_DIR";
    public static final String JAR_MODELS = "standard";
    public static final String ILI_REPOSITORY = "http://models.interlis.ch/";
    public static final String ILIDIR_SEPARATOR = ";";
    public static final String MODELS_SEPARATOR = ";";
    public static final String DEFAULT_ILIDIRS = ILI_DIR + ILIDIR_SEPARATOR + ILI_REPOSITORY + ILIDIR_SEPARATOR + JAR_DIR;


	public static UserSettings load() 
	{

	UserSettings userSettings = new UserSettings();
	try{
		java.io.File f=new java.io.File(SETTINGS_FILE);
		if(f.exists()){
			userSettings.load(f);
		}
		String ilidirs=userSettings.getIlidirs();
		if(ilidirs==null){
			userSettings.setIlidirs(DEFAULT_ILIDIRS);
		}
		String wd=userSettings.getWorkingDirectory();
		if(wd==null){
			userSettings.setWorkingDirectory(System.getProperty("user.home"));
		}
	}catch(IOException e){
		ch.ehi.basics.logging.EhiLogger.logError("failed to load settings",e);
	}
	return userSettings;
}
/**
 * Saves the UserSettings.
 */
public void save(String appName) {
	try {
	    store(new java.io.File(SETTINGS_FILE),appName);
	} catch(IOException e) {
		ch.ehi.basics.logging.EhiLogger.logError("failed to save settings",e);
	}
}
/**
 * Gets the workingDirectory property (java.lang.String) value.
 * @return The workingDirectory property value.
 * @see #setWorkingDirectory
 */
public java.lang.String getWorkingDirectory() {
	return getValue(WORKING_DIRECTORY);
}
public void setWorkingDirectory(java.lang.String workingDirectory) {
	setValue(WORKING_DIRECTORY, workingDirectory);
}
public java.lang.String getIlidirs() {
	return getValue(ILIDIRS);
}
public void setIlidirs(java.lang.String workingDirectory) {
	setValue(ILIDIRS, workingDirectory);
}
public java.lang.String getHttpProxyHost() {
	return getValue(HTTP_PROXY_HOST);
}
public void setHttpProxyHost(java.lang.String workingDirectory) {
	setValue(HTTP_PROXY_HOST, workingDirectory);
}
public java.lang.String getHttpProxyPort() {
	return getValue(HTTP_PROXY_PORT);
}
public void setHttpProxyPort(java.lang.String workingDirectory) {
	setValue(HTTP_PROXY_PORT, workingDirectory);
}

}
