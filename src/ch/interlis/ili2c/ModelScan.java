/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.interlis.ili2c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.tools.TopoSort;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ili2c.modelscan.IliModel;
import ch.interlis.ili2c.parser.Ili2ModelScan;


/**
 * @author ce
 * @version $Revision: 1.7 $ $Date: 2008-02-28 17:20:54 $
 */
public class ModelScan {

    public static void main(String[] args) {
	EhiLogger.getInstance().setTraceFilter(false);
	ArrayList<String> dirName = new ArrayList<String>(Arrays.asList(args[0].split(UserSettings.ILIDIR_SEPARATOR)));
	String model = args[1];
	ArrayList<String> models = new ArrayList<String>();
	models.add(model);
	try {
	    getConfig(dirName, models);
	} catch (Ili2cException ex) {
	    EhiLogger.logError(ex);
	}
    }


    public static IliFile scanIliFile(File file) {
	IliFile iliFile = new IliFile();
	String streamName = file.getAbsolutePath();
	iliFile.setFilename(file);
	FileInputStream stream = null;

	try {
	    stream = new FileInputStream(file);
	    Ili2ModelScan.mergeFile(iliFile, stream);
	    stream.close();
	    return iliFile;
	} catch (FileNotFoundException fnfex) {
	    EhiLogger.logError(streamName + ":" + "There is no such file.");
	} catch (Exception ex) {
	    EhiLogger.logError(ex);
	}
	return null;
    }


    /**
     * get INTERLIS version of an ili-file.
     */
    public static double getIliFileVersion(File file) {
	String streamName = file.getAbsolutePath();
	java.io.Reader stream = null;
	try {
	    stream = new java.io.InputStreamReader(new FileInputStream(file),"UTF-8");
	    double version = Ili2ModelScan.getIliVersion(stream);
	    stream.close();
	    return version;
	} catch (FileNotFoundException fnfex) {
	    EhiLogger.logError(streamName + ":" + "There is no such file.");
	} catch (Exception ex) {
	    EhiLogger.logError(ex);
	}
	return 0.0;
    }


    /**
     * scans a directory for ili-files.
     *
     * @param dir directory to scan
     * @param skipFiles skip this files. set<File iliFile>
     * @return set<IliFile>
     */
    public static HashSet scanIliFileDir(File dir, HashSet skipFiles) throws IOException {
	HashSet ret = new HashSet();
	if (!dir.exists()) {
	    EhiLogger.logAdaption("Folder " + dir.getAbsoluteFile() + " doesn't exist; ignored");
	    return ret;
	}
	if (!dir.isDirectory()) {
	    EhiLogger.logAdaption(dir.getAbsoluteFile() + " isn't a folder; ignored");
	    return ret;
	}
	File filev[] = dir.listFiles(new ch.ehi.basics.view.GenericFileFilter("INTERLIS models (*.ili)", "ili"));
	for (int i = 0; i < filev.length; i++) {
	    if (filev[i].isDirectory()) {
		continue;
	    }
	    boolean ignoreFile = false;
	    if (skipFiles != null) {
		Iterator skipFilei = skipFiles.iterator();
		while (skipFilei.hasNext()) {
		    File skipFile = (File) skipFilei.next();
		    if (filev[i].getCanonicalFile().equals(skipFile.getCanonicalFile())) {
			ignoreFile = true;
			break;
		    }
		}
	    }
	    if (!ignoreFile) {
		IliFile iliFile = scanIliFile(filev[i]);
		if (iliFile != null) {
		    ret.add(iliFile);
		}
	    }
	}
	return ret;
    }


    public static Configuration getConfig(ArrayList ilipathv, ArrayList requiredModels) throws Ili2cException {
	return getConfig(ilipathv, requiredModels, 0.0);
    }


    public static Configuration getConfig(ArrayList ilipathv, ArrayList requiredModels, double iliVersion)
	    throws Ili2cException {
	ArrayList ilifiles = new ArrayList();

	// scan directories for ili-files
	for (int i = 0; i < ilipathv.size(); i++) {
	    String ilipath = (String) ilipathv.get(i);
	    try {
		ilifiles.addAll(scanIliFileDir(new File(ilipath), null));
	    } catch (IOException e) {
		throw new Ili2cException("failed to scan folder " + ilipath, e);
	    }
	}

	// auto determine version?
	if (iliVersion == 0.0) {
	    // get version of first model
	    // scan threw list of models
	    String firstModel = (String) requiredModels.get(0);
	    Iterator it = ilifiles.iterator();
	    while (it.hasNext()) {
		IliFile ilifile = (IliFile) it.next();
		Iterator modeli = ilifile.iteratorModel();
		while (modeli.hasNext()) {
		    IliModel model = (IliModel) modeli.next();
		    if (model.getName().equals(firstModel)) {
			iliVersion = model.getIliVersion();
			break;
		    }
		}
		if (iliVersion != 0.0) {
		    break;
		}
	    }
	}

	// build map of modelname -> ilifile
	HashMap models = new HashMap();
	Iterator it = ilifiles.iterator();
	while (it.hasNext()) {
	    IliFile ilifile = (IliFile) it.next();
	    Iterator modeli = ilifile.iteratorModel();
	    while (modeli.hasNext()) {
		IliModel model = (IliModel) modeli.next();
		if (model.getIliVersion() == iliVersion) {
		    models.put(model.getName(), ilifile);
		}
	    }
	}

	// build list of files with required models
	HashSet toVisitFiles = new HashSet();
	it = requiredModels.iterator();
	HashSet missingModels = new HashSet();
	boolean err = false;
	while (it.hasNext()) {
	    String model = (String) it.next();
	    if (model == null) {
		continue;
	    }
	    IliFile file = (IliFile) models.get(model);
	    if (file == null) {
		if (!missingModels.contains(model)) {
		    EhiLogger.logError(model + ": model not found");
		    missingModels.add(model);
		}
		err = true;
	    } else {
		toVisitFiles.add(file);
	    }
	}
	if (err) {
	    return null;
	}

	// build config
	return createConfig(toVisitFiles, models);
    }


    /**
     * create compile configuration, given a set of ilifilenames and a set of
     * paths with additional ilifiles
     *
     * @param ilipaths list<String dirName> String
     *        ilidirv[]=ilipaths.split(";");
     * @param requiredIliFiles list<String iliFilename>
     * @return
     */
    public static Configuration getConfigWithFiles(ArrayList ilipaths, ArrayList requiredIliFileNames)
	    throws Ili2cException {
	if (requiredIliFileNames.isEmpty()) {
	    throw new Ili2cException("no ili files given");
	}
	HashSet ilifiles = new HashSet();
	HashSet toVisitFiles = new HashSet(); // set<IliFile>
	HashSet requiredFiles = new HashSet(); // set<File>

	// scan given files and report about duplicate models
	Iterator reqFileIt = requiredIliFileNames.iterator();
	HashSet availablemodels = new HashSet();
	double version = 0.0;
	while (reqFileIt.hasNext()) {
	    String fname = (String) reqFileIt.next();
	    File file = new File(fname);
	    if (!file.exists()) {
		throw new Ili2cException(fname + ": " + "There is no such file.");
	    }
	    IliFile iliFile = scanIliFile(file);
	    if (iliFile == null) {
		throw new Ili2cException(fname + ": " + "Failed to scan ili-file.");
	    }
	    if (iliFile != null) {
		boolean skipThisFile = false;
		for (Iterator modeli = iliFile.iteratorModel(); modeli.hasNext();) {
		    IliModel model = (IliModel) modeli.next();
		    if (version == 0.0) {
			version = model.getIliVersion();
		    } else {
			if (version != model.getIliVersion()) {
			    skipThisFile = true;
			    EhiLogger.logAdaption("different ili version; file ignored " + iliFile.getFilename());
			    break;
			}
		    }
		    if (availablemodels.contains(model.getName())) {
			skipThisFile = true;
			EhiLogger.logAdaption("duplicate model; file ignored " + iliFile.getFilename());
			break;
		    }
		}
		if (!skipThisFile) {
		    requiredFiles.add(file);
		    ilifiles.add(iliFile);
		    toVisitFiles.add(iliFile);
		    for (Iterator modeli = iliFile.iteratorModel(); modeli.hasNext();) {
			IliModel model = (IliModel) modeli.next();
			availablemodels.add(model.getName());
		    }
		}
	    }
	}

	// check if fileset complete
	if (!isFileSetComplete(ilifiles, availablemodels)) {
	    // add files in given directories
	    for (Iterator i = ilipaths.iterator(); i.hasNext();) {
		String ilidir = (String) i.next();
		HashSet set = null;
		try {
		    set = scanIliFileDir(new File(ilidir), requiredFiles);
		} catch (IOException ex) {
		    throw new Ili2cException("failed to scan folder " + ilidir, ex);
		}
		if (set != null && !set.isEmpty()) {
		    ilifiles.addAll(set);
		}
	    }
	}

	// build map of modelname -> ilifile
	HashMap models = new HashMap();
	Iterator it = ilifiles.iterator();
	while (it.hasNext()) {
	    IliFile ilifile = (IliFile) it.next();
	    Iterator modeli = ilifile.iteratorModel();
	    while (modeli.hasNext()) {
		IliModel model = (IliModel) modeli.next();
		if (model.getIliVersion() == version) {
		    models.put(model.getName(), ilifile);
		}
	    }
	}
	return createConfig(toVisitFiles, models);
    }


    static private boolean isFileSetComplete(HashSet ilifiles, HashSet availablemodels) {
	Iterator it = ilifiles.iterator();
	while (it.hasNext()) {
	    IliFile ilifile = (IliFile) it.next();
	    Iterator modeli = ilifile.iteratorModel();
	    while (modeli.hasNext()) {
		IliModel model = (IliModel) modeli.next();
		Iterator depi = model.getDependencies().iterator();
		while (depi.hasNext()) {
		    String dep = (String) depi.next();
		    if (!availablemodels.contains(dep)) {
			return false;
		    }
		}
	    }
	}
	return true;
    }


    /**
     * @param toVisitFiles set<IliFile iliFile>
     * @param models map<String modelName,IliFile iliFile>
     * @return
     */
    public static Configuration createConfig(HashSet toVisitFiles, HashMap models) {
	if (toVisitFiles.isEmpty()) {
	    throw new IllegalStateException("toVisitFiles.isEmpty()");
	}
	HashSet visitedFiles = new HashSet();
	TopoSort reqFiles = new TopoSort();
	HashSet missingModels = new HashSet();
	boolean modelsIncomplete = false;
	while (!toVisitFiles.isEmpty()) {
	    IliFile ilifile = (IliFile) toVisitFiles.iterator().next();
	    reqFiles.add(ilifile);

	    Iterator modeli = ilifile.iteratorModel();
	    while (modeli.hasNext()) {
		IliModel model = (IliModel) modeli.next();
		Iterator depi = model.getDependencies().iterator();
		while (depi.hasNext()) {
		    String dep = (String) depi.next();
		    IliFile supplier = (IliFile) models.get(dep);
		    if (supplier == null) {
			if (!missingModels.contains(dep)) {
			    EhiLogger.logError(dep + ": model not found");
			    missingModels.add(dep);
			}
			modelsIncomplete = true;
		    } else {
			if (!visitedFiles.contains(supplier)) {
			    // add file with supplier model
			    toVisitFiles.add(supplier);
			}
			if (supplier != ilifile) {
			    reqFiles.addcond(supplier, ilifile);
			}
		    }
		}
	    }
	    toVisitFiles.remove(ilifile);
	    visitedFiles.add(ilifile);
	}
	if (modelsIncomplete) {
	    return null;
	}
	if (!reqFiles.sort()) {
	    StringBuilder loopele = new StringBuilder();
	    Iterator resi = reqFiles.getResult().iterator();
	    String sep = "";
	    while (resi.hasNext()) {
		IliFile res = (IliFile) resi.next();
		loopele.append(sep);
		loopele.append(res.getFilename().getName());
		sep = "->";
	    }
	    EhiLogger.logError("loop in ili-files: " + loopele.toString());
	    return null;
	}
	Iterator resi = reqFiles.getResult().iterator();
	Configuration config = new Configuration();
	while (resi.hasNext()) {
	    IliFile res = (IliFile) resi.next();
	    File iliFile = res.getFilename();
	    // EhiLogger.debug(iliFile.getAbsolutePath());
	    config.addFileEntry(new ch.interlis.ili2c.config.FileEntry(iliFile.getAbsolutePath(),
		    ch.interlis.ili2c.config.FileEntryKind.ILIMODELFILE));
	}
	return config;
    }
}
