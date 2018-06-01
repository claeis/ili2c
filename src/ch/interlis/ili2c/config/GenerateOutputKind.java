// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

// -beg- preserve=no 3C6262B9035B package "GenerateOutputKind"
package ch.interlis.ili2c.config;
// -end- 3C6262B9035B package "GenerateOutputKind"

// -beg- preserve=no 3C6262B9035B autoimport "GenerateOutputKind"

// -end- 3C6262B9035B autoimport "GenerateOutputKind"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 3C6262B9035B import "GenerateOutputKind"

// -end- 3C6262B9035B import "GenerateOutputKind"

public class GenerateOutputKind
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C6262B9035B detail_begin "GenerateOutputKind"

  // -end- 3C6262B9035B detail_begin "GenerateOutputKind"

  // -beg- preserve=no 3C6262F00288 var3C6262B9035B "NOOUTPUT"
  public static final int NOOUTPUT = 1;
  // -end- 3C6262F00288 var3C6262B9035B "NOOUTPUT"

  // -beg- preserve=no 3C6262F801DF var3C6262B9035B "ILI1"
  public static final int ILI1 = 2;
  // -end- 3C6262F801DF var3C6262B9035B "ILI1"

  // -beg- preserve=no 3C62630101A6 var3C6262B9035B "ILI2"
  public static final int ILI2 = 3;
  // -end- 3C62630101A6 var3C6262B9035B "ILI2"

  // -beg- preserve=no 3C6263050382 var3C6262B9035B "XMLSCHEMA"
  public static final int XMLSCHEMA = 4;
  // -end- 3C6263050382 var3C6262B9035B "XMLSCHEMA"

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=yes 3C6262B9035B detail_end "GenerateOutputKind"
  public static final int ILI1FMTDESC = 5;
  public static final int GML32 = 6;
  public static final int IOM = 7;
  public static final int ETF1 = 8;
  public static final int IMD = 9;
  public static final int UML21 = 10;

  public static final int ILIGML2 = 11;
  public static final int IMD16 = 12;
  public static final int XMLNLS = 13;
  
  private static final String kindTexts[] = {
          "Generate no output"
        , "Generate an INTERLIS 1 model"
        , "Generate an INTERLIS 2 model"
        , "Generate an XTF XML-Schema"
        , "Generate an ILI1 FMT-Description"
  	  , "Generate a GML/eCH-118-1.0 XML-Schema"
  	  , "deprecated (IOM)"
  	  , "deprecated (ETF)"
  	  , "Generate Model as IlisMeta07-Transfer"
  	  , "Generate Model as UML/XMI Transfer"
  	  , "Generate a GML/eCH-118-2.0 XML-Schema"
  	  , "Generate Model as IlisMeta16-Transfer"
  	  , "Generate Model as XML-Translationfile"
        };
  public static String getDescription(int kind){
	  return kindTexts[kind-1];
  }
  // -end- 3C6262B9035B detail_end "GenerateOutputKind"

}

