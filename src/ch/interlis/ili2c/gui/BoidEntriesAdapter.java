// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

package ch.interlis.ili2c.gui;

import javax.swing.AbstractListModel;
import ch.interlis.ili2c.config.*;

public class BoidEntriesAdapter extends AbstractListModel
{
  private Configuration model;
  public BoidEntriesAdapter(Configuration model){
    this.model=model;
  }
  public void setModel(Configuration model){
    this.model=model;
    fireContentsChanged(this,0,getSize());
  }
  public int getSize(){
    return model.getSizeBoidEntry();
  }
  public Object getElementAt(int index){
    return model.getBoidEntry(index);
  }
  public void elementAtChanged(int index){
	fireContentsChanged(this,index,index);
  }
  public void addElement(BoidEntry entry){
    model.addBoidEntry(entry);
    fireIntervalAdded(this,getSize()-1,getSize()-1);
  }
  public BoidEntry remove(int idx){
    BoidEntry e=model.getBoidEntry(idx);
    model.removeBoidEntry(e);
    fireIntervalRemoved(this,idx,idx);
    return e;
  }
}

