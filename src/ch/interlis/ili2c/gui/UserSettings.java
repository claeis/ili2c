package ch.interlis.ili2c.gui;

import java.io.*;

public class UserSettings extends java.util.Properties {
	// values for Key-Values
	private final static String TRUE = "TRUE";
	private final static String FALSE = "FALSE";
	private final static String HOME_DIRECTORY = "user.home";
	// default Settings filename
	private final static String SETTINGS_FILE = System.getProperty(HOME_DIRECTORY) + "/.ili2c";

	// Property Keys (non-NLS)
	// @see getKeySet()
	private final static String WORKING_DIRECTORY = "WORKING_DIRECTORY";
/**
 * UserSettings constructor comment.
 */
private UserSettings() {
	super();
}
/**
 * Instantiates and loads the UserSettings.
 * @see getKeySet()
 */
protected static UserSettings createDefault() {
	UserSettings userSettings = new UserSettings();
	userSettings.setWorkingDirectory(System.getProperty(HOME_DIRECTORY));

    return userSettings;
}
/**
 * @return all keys managed by Settings.
 * @see class definition
 * @see createDefault()
 */
private static java.util.Set getKeySet() {
	java.util.Set set = new java.util.HashSet();
	set.add(WORKING_DIRECTORY);

	return set;
}
/**
 * Gets the workingDirectory property (java.lang.String) value.
 * @return The workingDirectory property value.
 * @see #setWorkingDirectory
 */
public java.lang.String getWorkingDirectory() {
	return getProperty(WORKING_DIRECTORY);
}
/**
 * Instantiates and loads the UserSettings.
 */
public static UserSettings load() /*throws FileNotFoundException, IOException, ClassNotFoundException*/ {

	UserSettings userSettings = createDefault();
	try {
	    FileInputStream inputStream = new FileInputStream(SETTINGS_FILE);
		UserSettings tmp = new UserSettings();
		tmp.load(inputStream);

		// try to reuse given keys
		// copy mechanism makes sure newer Versions of this Class
		// with additional keys cause no problems
		java.util.Iterator keys = getKeySet().iterator();
		while (keys.hasNext()) {
			String key = (String)keys.next();
			if (tmp.containsKey(key)) {
				userSettings.setProperty(key, tmp.getProperty(key));
			}
		}

	} catch(Throwable e) {
	}

    return userSettings;
}
/**
 * Saves the UserSettings.
 */
public void save() {
	try {
	    FileOutputStream outputStream = new FileOutputStream(SETTINGS_FILE);
	    super.store(outputStream, "ili2c");
	} catch(IOException e) {
		System.err.println(e.getLocalizedMessage());
	}
}
public void setWorkingDirectory(java.lang.String workingDirectory) {
	setProperty(WORKING_DIRECTORY, workingDirectory);
}
}
