package ch.interlis.ili2c.config;

import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.generator.TransformationParameter;
import ch.interlis.ili2c.config.BoidEntry;


public class Configuration implements Cloneable
{

	@Override
	public Object clone() throws CloneNotSupportedException {
		Configuration ret= (Configuration)super.clone();
		ret.fileEntry=new java.util.ArrayList<FileEntry>();
		for(FileEntry f:fileEntry){
			ret.fileEntry.add((FileEntry)f.clone());
		}
		ret.boidEntry=new java.util.ArrayList<BoidEntry>();
		for(BoidEntry f:boidEntry){
			ret.boidEntry.add((BoidEntry)f.clone());
		}
		return ret;
	}

  public int getSizeBoidEntry()

    {
    return boidEntry.size();
    }

  public BoidEntry getBoidEntry(int index)
    {
    return (BoidEntry)boidEntry.get(index);
    }

  public int getSizeFileEntry()
    {
    return fileEntry.size();
    }

  public FileEntry getFileEntry(int index)
    {
    return (FileEntry)fileEntry.get(index);
    }

  public void addFileEntry(int idx, FileEntry entry)
    {
    fileEntry.add(idx,entry);
    return;
    }

  private java.util.List<FileEntry> fileEntry = new java.util.ArrayList<FileEntry>();

  /** add a FileEntry.
   *
   *  @see #removeFileEntry
   *  @see #containsFileEntry
   *  @see #iteratorFileEntry
   *  @see #clearFileEntry
   */
  public void addFileEntry(FileEntry fileEntry1)
  {
    fileEntry.add(fileEntry1);

    return;
  }

  /** disconnect a FileEntry.
   *  @see #addFileEntry
   */
  public FileEntry removeFileEntry(FileEntry fileEntry1)
  {
    FileEntry ret=null;
    if(fileEntry1==null || !fileEntry.contains(fileEntry1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = fileEntry1;
    fileEntry.remove(fileEntry1);

    return ret;
  }

  /** tests if a given FileEntry is connected.
   *  @see #addFileEntry
   */
  public boolean containsFileEntry(FileEntry fileEntry1)
  {
    return fileEntry.contains(fileEntry1);
  }

  /** used to enumerate all connected FileEntrys.
   *  @see #addFileEntry
   */
  public java.util.Iterator iteratorFileEntry()
  {
    return fileEntry.iterator();
  }

  /** disconnect all FileEntrys.
   *  @see #addFileEntry
   */
  public void clearFileEntry()
  {
    fileEntry.clear();

  }

  private java.util.List<BoidEntry> boidEntry = new java.util.ArrayList<BoidEntry>();

  /** add a BoidEntry.
   *
   *  @see #removeBoidEntry
   *  @see #containsBoidEntry
   *  @see #iteratorBoidEntry
   *  @see #clearBoidEntry
   */
  public void addBoidEntry(BoidEntry boidEntry1)
  {
    boidEntry.add(boidEntry1);

    return;
  }

  /** disconnect a BoidEntry.
   *  @see #addBoidEntry
   */
  public BoidEntry removeBoidEntry(BoidEntry boidEntry1)
  {
    BoidEntry ret=null;
    if(boidEntry1==null || !boidEntry.contains(boidEntry1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = boidEntry1;
    boidEntry.remove(boidEntry1);

    return ret;
  }

  /** tests if a given BoidEntry is connected.
   *  @see #addBoidEntry
   */
  public boolean containsBoidEntry(BoidEntry boidEntry1)
  {
    return boidEntry.contains(boidEntry1);
  }

  /** used to enumerate all connected BoidEntrys.
   *  @see #addBoidEntry
   */
  public java.util.Iterator iteratorBoidEntry()
  {
    return boidEntry.iterator();
  }

  /** disconnect all BoidEntrys.
   *  @see #addBoidEntry
   */
  public void clearBoidEntry()
  {
    boidEntry.clear();
  }

  private int outputKind = GenerateOutputKind.NOOUTPUT;
  
  private TransformationParameter params = null;
  
  private String language;
  private String nlsxmlFilename;

  public String getLanguage() {
	return language;
}

public void setLanguage(String language) {
	this.language = language;
}

public String getNlsxmlFilename() {
	return nlsxmlFilename;
}

public void setNlsxmlFilename(String xmlFolderName) {
	this.nlsxmlFilename = xmlFolderName;
}

/** get current value of outputKind
   *  @see #setOutputKind
   */
  public  int getOutputKind()
  {
    return outputKind;
  }

  /** set current value of outputKind
   *  @see #getOutputKind
   */
  public  void setOutputKind(int value1)
  {
    if(outputKind != value1){
      outputKind = value1;

    }
  }

  private String outputFile;

  /** get current value of outputFile
   *  @see #setOutputFile
   */
  public  String getOutputFile()
  {
    return outputFile;
  }

  /** set current value of outputFile
   *  @see #getOutputFile
   */
  public  void setOutputFile(String value1)
  {
    if(outputFile != value1){
      outputFile = value1;

    }
  }

  private boolean generateWarnings = true;

  /** get current value of generateWarnings
   *  @see #setGenerateWarnings
   */
  public  boolean isGenerateWarnings()
  {
    return generateWarnings;
  }

  /** set current value of generateWarnings
   *  @see #isGenerateWarnings
   */
  public  void setGenerateWarnings(boolean value1)
  {
    if(generateWarnings != value1){
      generateWarnings = value1;

    }
  }
  private boolean incPredefModel = false;

  /** get current value of incPredefModel
   *  @see #setIncPredefModel
   */
  public  boolean isIncPredefModel()
  {
    return incPredefModel;
  }

  /** set current value of incPredefModel
   *  @see #isIncPredefModel
   */
  public  void setIncPredefModel(boolean value1)
  {
    if(incPredefModel != value1){
      incPredefModel = value1;

    }
  }

  private boolean checkMetaObjs = false;
  public  boolean isCheckMetaObjs()
  {
    return checkMetaObjs;
  }

  public  void setCheckMetaObjs(boolean value1)
  {
    if(checkMetaObjs != value1){
      checkMetaObjs = value1;
    }
  }
  private boolean autoCompleteModelList=false;

  /** tests, if compiler looks automatically after required models.
   */
public boolean isAutoCompleteModelList() {
	return autoCompleteModelList;
}

public void setAutoCompleteModelList(boolean autoCompleteModelList) {
	this.autoCompleteModelList = autoCompleteModelList;
}

public TransformationParameter getParams() {
	return params;
}

public void setParams(TransformationParameter params) {
	this.params = params;
}
}

