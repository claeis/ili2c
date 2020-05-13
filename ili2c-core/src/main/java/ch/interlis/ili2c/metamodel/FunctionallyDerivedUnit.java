/*****************************************************************************
 *
 * FunctionallyDerivedUnit.java
 * ----------------------------
 *
 * Copyright (C) 1999 Adasys AG, Kronenstr. 38, 8006 Zurich, Switzerland
 *
 * Revision 0.2  April 1999    Sascha Brawer <sb@adasys.ch>
 *
 *****************************************************************************/
 
package ch.interlis.ili2c.metamodel;

/** A FunctionallyDerivedUnit is a derivation unit whose derivation
    can not be expressed in Interlis.
    
    @version   April 12, 1999
    @author    Sascha Brawer, sb@adasys.ch
*/
public class FunctionallyDerivedUnit extends DerivedUnit
{
  protected String explanation = "";
  
  public FunctionallyDerivedUnit()
  {
  }
  
  /** Determines the value of the <code>explanation</code> property.
      Each FunctionallyDerivedUnit must be provided with an explanation
      describing what it does and how it works. These explanations are
      not interpreted by the processor. Instead, they allow for
      semi-formalized documentation.
  */
  public String getExplanation()
  {
    return explanation;
  }


  /** Sets the value of the <code>explanation</code> property.
      Each FunctionallyDerivedUnit must be provided with an explanation
      describing what it does and how it works. These explanations are
      not interpreted by the processor. Instead, they allow for
      semi-formalized documentation.

      <p>Since the <code>explanation</code> property is both <em>bound</em>
      and <em>constrained</em>, an interested party can listen and oppose
      to any changes of its value.
      
      @param explanation    The new explanation.

      @exception java.beans.PropertyVetoException if some VetoableChangeListener
                 has registered for changes of the <code>explanation</code> property
                 and does not agree with the change.
  */
  public void setExplanation (String explanation)
    throws java.beans.PropertyVetoException
  {
    String oldValue = this.explanation;
    String newValue = explanation;
        
    fireVetoableChange("explanation", oldValue, newValue);
    this.explanation = newValue;
    firePropertyChange("explanation", oldValue, newValue);
  }
}
