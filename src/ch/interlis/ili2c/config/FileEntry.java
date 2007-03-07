// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

// -beg- preserve=no 3C6260200387 package "FileEntry"
package ch.interlis.ili2c.config;
// -end- 3C6260200387 package "FileEntry"

// -beg- preserve=no 3C6260200387 autoimport "FileEntry"

// -end- 3C6260200387 autoimport "FileEntry"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 3C6260200387 import "FileEntry"

// -end- 3C6260200387 import "FileEntry"

public class FileEntry
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C6260200387 detail_begin "FileEntry"

  // -end- 3C6260200387 detail_begin "FileEntry"

  // -beg- preserve=no 3C6261690299 head3C6260200387 "FileEntry"
  public FileEntry(String filename, int kind)
  // -end- 3C6261690299 head3C6260200387 "FileEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C6261690299 throws3C6260200387 "FileEntry"

    // -end- 3C6261690299 throws3C6260200387 "FileEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C6261690299 body3C6260200387 "FileEntry"
    this.kind=kind;
    this.filename=filename;
    return;
    // -end- 3C6261690299 body3C6260200387 "FileEntry"
    }

  // -beg- preserve=no 3C62613E02B6 var3C6260200387 "filename"
  private String filename;
  // -end- 3C62613E02B6 var3C6260200387 "filename"

  /** get current value of filename
   *  @see #setFilename
   */
  // -beg- preserve=no 3C62613E02B6 get_head3C6260200387 "filename"
  public  String getFilename()
  // -end- 3C62613E02B6 get_head3C6260200387 "filename"
  {
    // -beg- preserve=no 3C62613E02B6 get_body3C6260200387 "filename"
    return filename;
    // -end- 3C62613E02B6 get_body3C6260200387 "filename"
  }

  /** set current value of filename
   *  @see #getFilename
   */
  // -beg- preserve=no 3C62613E02B6 set_head3C6260200387 "filename"
  public  void setFilename(String value1)
  // -end- 3C62613E02B6 set_head3C6260200387 "filename"
  {
    // -beg- preserve=no 3C62613E02B6 set_body3C6260200387 "filename"
    if(filename != value1){
      filename = value1;
      
    }
    // -end- 3C62613E02B6 set_body3C6260200387 "filename"
  }

  // -beg- preserve=no 3C626152019C var3C6260200387 "kind"
  private int kind;
  // -end- 3C626152019C var3C6260200387 "kind"

  /** get current value of kind
   *  @see #setKind
   */
  // -beg- preserve=no 3C626152019C get_head3C6260200387 "kind"
  public  int getKind()
  // -end- 3C626152019C get_head3C6260200387 "kind"
  {
    // -beg- preserve=no 3C626152019C get_body3C6260200387 "kind"
    return kind;
    // -end- 3C626152019C get_body3C6260200387 "kind"
  }

  /** set current value of kind
   *  @see #getKind
   */
  // -beg- preserve=no 3C626152019C set_head3C6260200387 "kind"
  public  void setKind(int value1)
  // -end- 3C626152019C set_head3C6260200387 "kind"
  {
    // -beg- preserve=no 3C626152019C set_body3C6260200387 "kind"
    if(kind != value1){
      kind = value1;
      
    }
    // -end- 3C626152019C set_body3C6260200387 "kind"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C6260200387 detail_end "FileEntry"

  // -end- 3C6260200387 detail_end "FileEntry"

}

