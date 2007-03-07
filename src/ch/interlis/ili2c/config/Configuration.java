// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

// -beg- preserve=no 3C625EF103C7 package "Configuration"
package ch.interlis.ili2c.config;
// -end- 3C625EF103C7 package "Configuration"

// -beg- preserve=no 3C625EF103C7 autoimport "Configuration"
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.BoidEntry;
// -end- 3C625EF103C7 autoimport "Configuration"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 3C625EF103C7 import "Configuration"

// -end- 3C625EF103C7 import "Configuration"

public class Configuration
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C625EF103C7 detail_begin "Configuration"

  // -end- 3C625EF103C7 detail_begin "Configuration"

  // -beg- preserve=no 3C626800005A head3C625EF103C7 "getSizeBoidEntry"
  public int getSizeBoidEntry()
  // -end- 3C626800005A head3C625EF103C7 "getSizeBoidEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C626800005A throws3C625EF103C7 "getSizeBoidEntry"

    // -end- 3C626800005A throws3C625EF103C7 "getSizeBoidEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C626800005A body3C625EF103C7 "getSizeBoidEntry"
    return boidEntry.size();
    // -end- 3C626800005A body3C625EF103C7 "getSizeBoidEntry"
    }

  // -beg- preserve=no 3C6268080279 head3C625EF103C7 "getBoidEntry"
  public BoidEntry getBoidEntry(int index)
  // -end- 3C6268080279 head3C625EF103C7 "getBoidEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C6268080279 throws3C625EF103C7 "getBoidEntry"

    // -end- 3C6268080279 throws3C625EF103C7 "getBoidEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C6268080279 body3C625EF103C7 "getBoidEntry"
    return (BoidEntry)boidEntry.get(index);
    // -end- 3C6268080279 body3C625EF103C7 "getBoidEntry"
    }

  // -beg- preserve=no 3C628CD60054 head3C625EF103C7 "getSizeFileEntry"
  public int getSizeFileEntry()
  // -end- 3C628CD60054 head3C625EF103C7 "getSizeFileEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C628CD60054 throws3C625EF103C7 "getSizeFileEntry"

    // -end- 3C628CD60054 throws3C625EF103C7 "getSizeFileEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C628CD60054 body3C625EF103C7 "getSizeFileEntry"
    return fileEntry.size();
    // -end- 3C628CD60054 body3C625EF103C7 "getSizeFileEntry"
    }

  // -beg- preserve=no 3C628CDA00C8 head3C625EF103C7 "getFileEntry"
  public FileEntry getFileEntry(int index)
  // -end- 3C628CDA00C8 head3C625EF103C7 "getFileEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C628CDA00C8 throws3C625EF103C7 "getFileEntry"

    // -end- 3C628CDA00C8 throws3C625EF103C7 "getFileEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C628CDA00C8 body3C625EF103C7 "getFileEntry"
    return (FileEntry)fileEntry.get(index);
    // -end- 3C628CDA00C8 body3C625EF103C7 "getFileEntry"
    }

  // -beg- preserve=no 3C628E6D03DE head3C625EF103C7 "addFileEntry"
  public void addFileEntry(int idx, FileEntry entry)
  // -end- 3C628E6D03DE head3C625EF103C7 "addFileEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C628E6D03DE throws3C625EF103C7 "addFileEntry"

    // -end- 3C628E6D03DE throws3C625EF103C7 "addFileEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C628E6D03DE body3C625EF103C7 "addFileEntry"
    fileEntry.add(idx,entry);
    return;
    // -end- 3C628E6D03DE body3C625EF103C7 "addFileEntry"
    }

  // -beg- preserve=no 3C626253035E code3C625EF103C7 "fileEntry"
  private java.util.List fileEntry = new java.util.ArrayList();
  // -end- 3C626253035E code3C625EF103C7 "fileEntry"

  /** add a FileEntry.
   *
   *  @see #removeFileEntry
   *  @see #containsFileEntry
   *  @see #iteratorFileEntry
   *  @see #clearFileEntry
   */
  // -beg- preserve=no 3C626253035E add_head3C625EF103C7 "Configuration::addFileEntry"
  public void addFileEntry(FileEntry fileEntry1)
  // -end- 3C626253035E add_head3C625EF103C7 "Configuration::addFileEntry"
  {
    // -beg- preserve=no 3C626253035E add_body3C625EF103C7 "Configuration::addFileEntry"
    fileEntry.add(fileEntry1);

    return;
    // -end- 3C626253035E add_body3C625EF103C7 "Configuration::addFileEntry"
  }

  /** disconnect a FileEntry.
   *  @see #addFileEntry
   */
  // -beg- preserve=no 3C626253035E remove_head3C625EF103C7 "Configuration::removeFileEntry"
  public FileEntry removeFileEntry(FileEntry fileEntry1)
  // -end- 3C626253035E remove_head3C625EF103C7 "Configuration::removeFileEntry"
  {
    // -beg- preserve=no 3C626253035E remove_body3C625EF103C7 "Configuration::removeFileEntry"
    FileEntry ret=null;
    if(fileEntry1==null || !fileEntry.contains(fileEntry1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = fileEntry1;
    fileEntry.remove(fileEntry1);

    return ret;
    // -end- 3C626253035E remove_body3C625EF103C7 "Configuration::removeFileEntry"
  }

  /** tests if a given FileEntry is connected.
   *  @see #addFileEntry
   */
  // -beg- preserve=no 3C626253035E test_head3C625EF103C7 "Configuration::containsFileEntry"
  public boolean containsFileEntry(FileEntry fileEntry1)
  // -end- 3C626253035E test_head3C625EF103C7 "Configuration::containsFileEntry"
  {
    // -beg- preserve=no 3C626253035E test_body3C625EF103C7 "Configuration::containsFileEntry"
    return fileEntry.contains(fileEntry1);
    // -end- 3C626253035E test_body3C625EF103C7 "Configuration::containsFileEntry"
  }

  /** used to enumerate all connected FileEntrys.
   *  @see #addFileEntry
   */
  // -beg- preserve=no 3C626253035E get_all_head3C625EF103C7 "Configuration::iteratorFileEntry"
  public java.util.Iterator iteratorFileEntry()
  // -end- 3C626253035E get_all_head3C625EF103C7 "Configuration::iteratorFileEntry"
  {
    // -beg- preserve=no 3C626253035E get_all_body3C625EF103C7 "Configuration::iteratorFileEntry"
    return fileEntry.iterator();
    // -end- 3C626253035E get_all_body3C625EF103C7 "Configuration::iteratorFileEntry"
  }

  /** disconnect all FileEntrys.
   *  @see #addFileEntry
   */
  // -beg- preserve=no 3C626253035E remove_all_head3C625EF103C7 "Configuration::clearFileEntry"
  public void clearFileEntry()
  // -end- 3C626253035E remove_all_head3C625EF103C7 "Configuration::clearFileEntry"
  {
    // -beg- preserve=no 3C626253035E remove_all_body3C625EF103C7 "Configuration::clearFileEntry"
    fileEntry.clear();

    // -end- 3C626253035E remove_all_body3C625EF103C7 "Configuration::clearFileEntry"
  }

  // -beg- preserve=yes 3C6262560088 code3C625EF103C7 "boidEntry"
  private java.util.List boidEntry = new java.util.ArrayList();
  // -end- 3C6262560088 code3C625EF103C7 "boidEntry"

  /** add a BoidEntry.
   *
   *  @see #removeBoidEntry
   *  @see #containsBoidEntry
   *  @see #iteratorBoidEntry
   *  @see #clearBoidEntry
   */
  // -beg- preserve=no 3C6262560088 add_head3C625EF103C7 "Configuration::addBoidEntry"
  public void addBoidEntry(BoidEntry boidEntry1)
  // -end- 3C6262560088 add_head3C625EF103C7 "Configuration::addBoidEntry"
  {
    // -beg- preserve=no 3C6262560088 add_body3C625EF103C7 "Configuration::addBoidEntry"
    boidEntry.add(boidEntry1);

    return;
    // -end- 3C6262560088 add_body3C625EF103C7 "Configuration::addBoidEntry"
  }

  /** disconnect a BoidEntry.
   *  @see #addBoidEntry
   */
  // -beg- preserve=no 3C6262560088 remove_head3C625EF103C7 "Configuration::removeBoidEntry"
  public BoidEntry removeBoidEntry(BoidEntry boidEntry1)
  // -end- 3C6262560088 remove_head3C625EF103C7 "Configuration::removeBoidEntry"
  {
    // -beg- preserve=no 3C6262560088 remove_body3C625EF103C7 "Configuration::removeBoidEntry"
    BoidEntry ret=null;
    if(boidEntry1==null || !boidEntry.contains(boidEntry1)){
      throw new java.lang.IllegalArgumentException("cannot remove null or unknown object");
    }
    ret = boidEntry1;
    boidEntry.remove(boidEntry1);

    return ret;
    // -end- 3C6262560088 remove_body3C625EF103C7 "Configuration::removeBoidEntry"
  }

  /** tests if a given BoidEntry is connected.
   *  @see #addBoidEntry
   */
  // -beg- preserve=no 3C6262560088 test_head3C625EF103C7 "Configuration::containsBoidEntry"
  public boolean containsBoidEntry(BoidEntry boidEntry1)
  // -end- 3C6262560088 test_head3C625EF103C7 "Configuration::containsBoidEntry"
  {
    // -beg- preserve=no 3C6262560088 test_body3C625EF103C7 "Configuration::containsBoidEntry"
    return boidEntry.contains(boidEntry1);
    // -end- 3C6262560088 test_body3C625EF103C7 "Configuration::containsBoidEntry"
  }

  /** used to enumerate all connected BoidEntrys.
   *  @see #addBoidEntry
   */
  // -beg- preserve=no 3C6262560088 get_all_head3C625EF103C7 "Configuration::iteratorBoidEntry"
  public java.util.Iterator iteratorBoidEntry()
  // -end- 3C6262560088 get_all_head3C625EF103C7 "Configuration::iteratorBoidEntry"
  {
    // -beg- preserve=no 3C6262560088 get_all_body3C625EF103C7 "Configuration::iteratorBoidEntry"
    return boidEntry.iterator();
    // -end- 3C6262560088 get_all_body3C625EF103C7 "Configuration::iteratorBoidEntry"
  }

  /** disconnect all BoidEntrys.
   *  @see #addBoidEntry
   */
  // -beg- preserve=no 3C6262560088 remove_all_head3C625EF103C7 "Configuration::clearBoidEntry"
  public void clearBoidEntry()
  // -end- 3C6262560088 remove_all_head3C625EF103C7 "Configuration::clearBoidEntry"
  {
    // -beg- preserve=no 3C6262560088 remove_all_body3C625EF103C7 "Configuration::clearBoidEntry"
    boidEntry.clear();

    // -end- 3C6262560088 remove_all_body3C625EF103C7 "Configuration::clearBoidEntry"
  }

  // -beg- preserve=no 3C6263180358 var3C625EF103C7 "outputKind"
  private int outputKind = GenerateOutputKind.NOOUTPUT;
  // -end- 3C6263180358 var3C625EF103C7 "outputKind"

  /** get current value of outputKind
   *  @see #setOutputKind
   */
  // -beg- preserve=no 3C6263180358 get_head3C625EF103C7 "outputKind"
  public  int getOutputKind()
  // -end- 3C6263180358 get_head3C625EF103C7 "outputKind"
  {
    // -beg- preserve=no 3C6263180358 get_body3C625EF103C7 "outputKind"
    return outputKind;
    // -end- 3C6263180358 get_body3C625EF103C7 "outputKind"
  }

  /** set current value of outputKind
   *  @see #getOutputKind
   */
  // -beg- preserve=no 3C6263180358 set_head3C625EF103C7 "outputKind"
  public  void setOutputKind(int value1)
  // -end- 3C6263180358 set_head3C625EF103C7 "outputKind"
  {
    // -beg- preserve=no 3C6263180358 set_body3C625EF103C7 "outputKind"
    if(outputKind != value1){
      outputKind = value1;

    }
    // -end- 3C6263180358 set_body3C625EF103C7 "outputKind"
  }

  // -beg- preserve=no 3C6263270345 var3C625EF103C7 "outputFile"
  private String outputFile;
  // -end- 3C6263270345 var3C625EF103C7 "outputFile"

  /** get current value of outputFile
   *  @see #setOutputFile
   */
  // -beg- preserve=no 3C6263270345 get_head3C625EF103C7 "outputFile"
  public  String getOutputFile()
  // -end- 3C6263270345 get_head3C625EF103C7 "outputFile"
  {
    // -beg- preserve=no 3C6263270345 get_body3C625EF103C7 "outputFile"
    return outputFile;
    // -end- 3C6263270345 get_body3C625EF103C7 "outputFile"
  }

  /** set current value of outputFile
   *  @see #getOutputFile
   */
  // -beg- preserve=no 3C6263270345 set_head3C625EF103C7 "outputFile"
  public  void setOutputFile(String value1)
  // -end- 3C6263270345 set_head3C625EF103C7 "outputFile"
  {
    // -beg- preserve=no 3C6263270345 set_body3C625EF103C7 "outputFile"
    if(outputFile != value1){
      outputFile = value1;

    }
    // -end- 3C6263270345 set_body3C625EF103C7 "outputFile"
  }

  // -beg- preserve=no 3C626346010F var3C625EF103C7 "generateWarnings"
  private boolean generateWarnings = true;
  // -end- 3C626346010F var3C625EF103C7 "generateWarnings"

  /** get current value of generateWarnings
   *  @see #setGenerateWarnings
   */
  // -beg- preserve=no 3C626346010F get_head3C625EF103C7 "generateWarnings"
  public  boolean isGenerateWarnings()
  // -end- 3C626346010F get_head3C625EF103C7 "generateWarnings"
  {
    // -beg- preserve=no 3C626346010F get_body3C625EF103C7 "generateWarnings"
    return generateWarnings;
    // -end- 3C626346010F get_body3C625EF103C7 "generateWarnings"
  }

  /** set current value of generateWarnings
   *  @see #isGenerateWarnings
   */
  // -beg- preserve=no 3C626346010F set_head3C625EF103C7 "generateWarnings"
  public  void setGenerateWarnings(boolean value1)
  // -end- 3C626346010F set_head3C625EF103C7 "generateWarnings"
  {
    // -beg- preserve=no 3C626346010F set_body3C625EF103C7 "generateWarnings"
    if(generateWarnings != value1){
      generateWarnings = value1;

    }
    // -end- 3C626346010F set_body3C625EF103C7 "generateWarnings"
  }

  // -beg- preserve=no 3C626352035B var3C625EF103C7 "incPredefModel"
  private boolean incPredefModel = false;
  // -end- 3C626352035B var3C625EF103C7 "incPredefModel"

  /** get current value of incPredefModel
   *  @see #setIncPredefModel
   */
  // -beg- preserve=no 3C626352035B get_head3C625EF103C7 "incPredefModel"
  public  boolean isIncPredefModel()
  // -end- 3C626352035B get_head3C625EF103C7 "incPredefModel"
  {
    // -beg- preserve=no 3C626352035B get_body3C625EF103C7 "incPredefModel"
    return incPredefModel;
    // -end- 3C626352035B get_body3C625EF103C7 "incPredefModel"
  }

  /** set current value of incPredefModel
   *  @see #isIncPredefModel
   */
  // -beg- preserve=no 3C626352035B set_head3C625EF103C7 "incPredefModel"
  public  void setIncPredefModel(boolean value1)
  // -end- 3C626352035B set_head3C625EF103C7 "incPredefModel"
  {
    // -beg- preserve=no 3C626352035B set_body3C625EF103C7 "incPredefModel"
    if(incPredefModel != value1){
      incPredefModel = value1;

    }
    // -end- 3C626352035B set_body3C625EF103C7 "incPredefModel"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 3C625EF103C7 detail_end "Configuration"
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

  // -end- 3C625EF103C7 detail_end "Configuration"

}

