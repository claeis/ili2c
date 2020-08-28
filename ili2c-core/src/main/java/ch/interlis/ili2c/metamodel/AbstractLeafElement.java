/*****************************************************************************
 *
 * AbstractLeafElement.java
 * ------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

import java.beans.beancontext.BeanContextChildSupport;

/** An abstract class which serves as a common
    superclass for all Interlis leaf constructs.
    
    @version   January 28, 1999
    @author    Sascha Brawer (mailto:sb@adasys.ch)
*/
public abstract class AbstractLeafElement extends Element {
  protected AbstractLeafElement() {
    bccs = new BeanContextChildSupport(this);
  }
}
