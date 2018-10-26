package ch.interlis.ili2c.metamodel;

import java.util.LinkedList;

public class BaseRestriction extends Selection
{
  private final String[] base;
  private final LinkedList<Viewable<?>> restrictedTo;
  private ViewableAlias alias=null;
  /** Constructs a new Selection given the selected View
      and the selection condition.
  */
  public BaseRestriction(Viewable<?> selected, String[] base)
  {
	super(selected);
	this.base=base;
        alias=getSelected().resolveBaseAlias(base[0]);
        if(alias==null){
            throw new IllegalArgumentException (formatMessage (
            "err_selbase_unknownbase",
            getSelected().toString(), base[0]));
        }
	this.restrictedTo = new LinkedList<Viewable<?>>();
  }

  public void addRestrictedTo(Viewable restrictedTo)
  {
        if(!restrictedTo.isExtending(alias.getAliasing())){
            throw new IllegalArgumentException (formatMessage (
            "err_selbase_unrealtedrestriction",
            alias.getAliasing().toString(), restrictedTo.toString()));
        }
	this.restrictedTo.add(restrictedTo);
  }

}
