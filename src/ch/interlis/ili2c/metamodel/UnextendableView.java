package ch.interlis.ili2c.metamodel;
import java.util.*;



public abstract class UnextendableView extends View
{
  public void setExtending (Element extending)
    throws java.beans.PropertyVetoException
  {
    if (extending != null)
      throw new Ili2cSemanticException (getSourceLine(),
        formatMessage ("err_view_notExtensible", this.toString(),
                       extending.toString()));


    super.setExtending ((UnextendableView) extending);
  }
}
