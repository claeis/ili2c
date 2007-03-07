// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

// -beg- preserve=no 3C626019003D package "BoidEntry"
package ch.interlis.ili2c.config;
// -end- 3C626019003D package "BoidEntry"

// -beg- preserve=no 3C626019003D autoimport "BoidEntry"

// -end- 3C626019003D autoimport "BoidEntry"

// import declarations
// please fill in/modify the following section
// -beg- preserve=no 3C626019003D import "BoidEntry"

// -end- 3C626019003D import "BoidEntry"

public class BoidEntry
{
  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C626019003D detail_begin "BoidEntry"

  // -end- 3C626019003D detail_begin "BoidEntry"

  // -beg- preserve=no 3C626067005E head3C626019003D "BoidEntry"
  public BoidEntry(String metaDataUseDef, String boid)
  // -end- 3C626067005E head3C626019003D "BoidEntry"
    // declare any checked exceptions
    // please fill in/modify the following section
    // -beg- preserve=no 3C626067005E throws3C626019003D "BoidEntry"

    // -end- 3C626067005E throws3C626019003D "BoidEntry"
    {
    // please fill in/modify the following section
    // -beg- preserve=yes 3C626067005E body3C626019003D "BoidEntry"
    this.boid=boid;
    this.metaDataUseDef=metaDataUseDef;
    return;
    // -end- 3C626067005E body3C626019003D "BoidEntry"
    }

  // -beg- preserve=no 3C62602F03E2 var3C626019003D "metaDataUseDef"
  private String metaDataUseDef;
  // -end- 3C62602F03E2 var3C626019003D "metaDataUseDef"

  /** get current value of metaDataUseDef
   *  @see #setMetaDataUseDef
   */
  // -beg- preserve=no 3C62602F03E2 get_head3C626019003D "metaDataUseDef"
  public  String getMetaDataUseDef()
  // -end- 3C62602F03E2 get_head3C626019003D "metaDataUseDef"
  {
    // -beg- preserve=no 3C62602F03E2 get_body3C626019003D "metaDataUseDef"
    return metaDataUseDef;
    // -end- 3C62602F03E2 get_body3C626019003D "metaDataUseDef"
  }

  /** set current value of metaDataUseDef
   *  @see #getMetaDataUseDef
   */
  // -beg- preserve=no 3C62602F03E2 set_head3C626019003D "metaDataUseDef"
  public  void setMetaDataUseDef(String value1)
  // -end- 3C62602F03E2 set_head3C626019003D "metaDataUseDef"
  {
    // -beg- preserve=no 3C62602F03E2 set_body3C626019003D "metaDataUseDef"
    if(metaDataUseDef != value1){
      metaDataUseDef = value1;

    }
    // -end- 3C62602F03E2 set_body3C626019003D "metaDataUseDef"
  }

  // -beg- preserve=no 3C626042039A var3C626019003D "boid"
  private String boid;
  // -end- 3C626042039A var3C626019003D "boid"

  /** get current value of boid
   *  @see #setBoid
   */
  // -beg- preserve=no 3C626042039A get_head3C626019003D "boid"
  public  String getBoid()
  // -end- 3C626042039A get_head3C626019003D "boid"
  {
    // -beg- preserve=no 3C626042039A get_body3C626019003D "boid"
    return boid;
    // -end- 3C626042039A get_body3C626019003D "boid"
  }

  /** set current value of boid
   *  @see #getBoid
   */
  // -beg- preserve=no 3C626042039A set_head3C626019003D "boid"
  public  void setBoid(String value1)
  // -end- 3C626042039A set_head3C626019003D "boid"
  {
    // -beg- preserve=no 3C626042039A set_body3C626019003D "boid"
    if(boid != value1){
      boid = value1;

    }
    // -end- 3C626042039A set_body3C626019003D "boid"
  }

  // declare/define something only in the code
  // please fill in/modify the following section
  // -beg- preserve=no 3C626019003D detail_end "BoidEntry"

  // -end- 3C626019003D detail_end "BoidEntry"

}

