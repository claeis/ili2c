/*****************************************************************************
 *
 * RefSystemRef.java
 * -----------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** An abstract class that groups together all the different sorts of INTERLIS
    reference systems.
*/
public abstract class RefSystemRef
{
  /** A reference to a geographical reference system.
  */
  public static class CoordSystem extends RefSystemRef
  {
    MetaObject system;
    
    public CoordSystem (MetaObject system)
    {
      this.system = system;
    }
    
    public MetaObject getSystem ()
    {
      return system;
    }
  }


  /** A reference to an axis of a geographical reference system.
  */
  public static class CoordSystemAxis extends RefSystemRef
  {
    MetaObject system;
    int        axisNumber;

    public CoordSystemAxis (MetaObject system, int axisNumber)
    {
      this.system = system;
      this.axisNumber = axisNumber;
    }
    
    public MetaObject getSystem ()
    {
      return system;
    }
    
    public int getAxisNumber ()
    {
      return axisNumber;
    }
  }


  /** A reference to a coordinate system.
  */
  public static class CoordDomain extends RefSystemRef
  {
    Domain referredDomain;
    
    public CoordDomain (Domain referredDomain)
    {
      this.referredDomain = referredDomain;
    }
    
    public Domain getReferredDomain ()
    {
      return referredDomain;
    }
  }


  /** A reference to an axis of a coordinate domain definition.
  */
  public static class CoordDomainAxis extends RefSystemRef
  {
    Domain referredDomain;
    int    axisNumber;
    
    
    public CoordDomainAxis (Domain referredDomain, int axisNumber)
    {
      this.referredDomain = referredDomain;
      this.axisNumber = axisNumber;
    }
    
    public Domain getReferredDomain ()
    {
      return referredDomain;
    }
    
    public int getAxisNumber ()
    {
      return axisNumber;
    }
  }
}
