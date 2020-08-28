/*****************************************************************************
 *
 * BaseType.java
 * -------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  February 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/

package ch.interlis.ili2c.metamodel;

import java.beans.beancontext.BeanContextChildSupport;

/** BaseType is an abstract class which serves as a common
    superclass for all Interlis base type domains.

    @version   January 28, 1999
    @author    Sascha Brawer
    @author    Gordan Vosicki - Normalized clone().
*/
public abstract class BaseType extends Type
{
  protected BaseType()
  {
    bccs = new BeanContextChildSupport(this);
  }


    public BaseType clone() {
        BaseType cloned = (BaseType) super.clone();

        bccs = new BeanContextChildSupport(this);
        return cloned;
    }

}
