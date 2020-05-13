// Copyright (c) 2002, Eisenhut Informatik
// All rights reserved.
// $Date: 2007-03-07 08:36:07 $
// $Revision: 1.2 $
//

package ch.interlis.ili2c.gui;
import ch.interlis.ili2c.config.*;

import javax.swing.AbstractListModel;

public class FileEntriesAdapter extends AbstractListModel
{
  private Configuration model;
  public FileEntriesAdapter(Configuration model){
    this.model=model;
  }
  public void setModel(Configuration model){
    this.model=model;
    fireContentsChanged(this,0,getSize());
  }
  public int getSize(){
    return model.getSizeFileEntry();
  }
  public Object getElementAt(int index){
    return model.getFileEntry(index);
  }
  public void addElement(FileEntry entry){
    model.addFileEntry(entry);
    fireIntervalAdded(this,getSize()-1,getSize()-1);
  }
  public void add(int idx,FileEntry entry){
    model.addFileEntry(idx,entry);
    fireIntervalAdded(this,idx,idx);

  }
  public FileEntry remove(int idx){
    FileEntry e=model.getFileEntry(idx);
    model.removeFileEntry(e);
    fireIntervalRemoved(this,idx,idx);
    return e;
  }
}

