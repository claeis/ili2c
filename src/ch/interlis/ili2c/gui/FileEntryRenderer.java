package ch.interlis.ili2c.gui;

import javax.swing.*;
import java.awt.*;
import ch.interlis.ili2c.config.FileEntry;

public class FileEntryRenderer extends DefaultListCellRenderer
{

     //final static ImageIcon longIcon = new ImageIcon("long.gif");
     //final static ImageIcon shortIcon = new ImageIcon("short.gif");

     // This is the only method defined by ListCellRenderer.
     // We just reconfigure the JLabel each time we're called.

     public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
       /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      /* We additionally set the JLabels icon property here.
         */
         String s = ((FileEntry)value).getFilename();
         setText(s);
	//setIcon((s.length > 10) ? longIcon : shortIcon);

        return this;
     }
}