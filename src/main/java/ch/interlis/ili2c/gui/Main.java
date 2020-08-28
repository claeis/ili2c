/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ch.interlis.ili2c.gui;

import java.util.*;
import java.text.DateFormat;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.ErrorListener;
import ch.interlis.ili2c.config.*;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.TextAreaListener;
import ch.ehi.basics.view.*;

public class Main {
        private UserSettings settings;
        final static String CHECKPANEL = "Check/Generate";
        //final static String COMPAREPANEL = "Compare";
        TextAreaListener errlistener=null;
        JFrame frame;
        JPanel cards;
        Container checkPane;
        Container comparePane;
        FileEntriesAdapter inputFileList;
        JList inputFileListUi;
        JTextField outputFileUi;
        JRadioButton warnButton;
        JRadioButton errButton;
        JComboBox outputKind;
        JTextArea  errOutput;
        JPopupMenu errPopup;
        JButton runButton;
        JCheckBox autoCompleteCB;
        
        FileChooser fc;
	JLabel fileLabel = new JLabel("Filename");

        /** current configuration file
         */
         Configuration config=new Configuration();
        java.io.File ilcFile=null;

  public boolean showDialog(){
    run();
    return true;
  }
  public void run(){
        frame = new JFrame();
        updateTitle();

        checkPane=buildCheckPane();
        Container runPane=buildRunPane();
		frame.getRootPane().setDefaultButton(runButton);


        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           checkPane, runPane);
        //splitPane.setOneTouchExpandable(true);
        //splitPane.setDividerLocation(150);
		Container contentPane = frame.getContentPane();
        contentPane.add(splitPane, BorderLayout.CENTER);

        //Finish setting up the frame, and show it.
        //frame.addWindowListener(...);
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            settings.save(ch.interlis.ili2c.Main.APP_NAME);
	    	System.exit(0);
          }
        });

        buildMenu();

        updateUi();
        errlistener=new TextAreaListener();
        errlistener.setOutputArea(errOutput);
        ch.ehi.basics.logging.EhiLogger.getInstance().addListener(errlistener);
		frame.setSize(508,490);
        frame.pack();
        frame.setVisible(true);
  }

  private void runCompiler(){
	  TransferDescription td=null;
	  try{
		  EhiLogger.logState(ch.interlis.ili2c.Main.APP_NAME+"-"+TransferDescription.getVersion());
		  td=ch.interlis.ili2c.Main.runCompiler(config,settings);
	  }catch(Throwable e){
		  EhiLogger.logError(e);
	  }
      Date today;
      String dateOut;
      DateFormat dateFormatter;

      dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                             DateFormat.SHORT,
                             Locale.getDefault());
      today = new Date();
      dateOut = dateFormatter.format(today);
      String failed= td==null ? "failed":"done";
      errOutput.append("--- compiler run "+failed+" "+dateOut+"\n");
  }
  private void updateTitle(){
    if(ilcFile!=null){
      frame.setTitle(ch.interlis.ili2c.Main.APP_NAME+" - "+ilcFile.getAbsolutePath());
    }else{
      frame.setTitle(ch.interlis.ili2c.Main.APP_NAME);
    }
  }
  private void buildMenu(){
    //in the constructor for a JFrame subclass:
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;

    //Create the menu bar.
    menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);

    //Build the first menu.
    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);
    //menu.getAccessibleContext().setAccessibleDescription(
    //        "The only menu in this program that has menu items");
    menuBar.add(menu);

    //a group of JMenuItems
    //menuItem = new JMenuItem("A text-only menu item",
    //                         KeyEvent.VK_T);
    menuItem = new JMenuItem("New");
    //menuItem.setAccelerator(KeyStroke.getKeyStroke(
    //        KeyEvent.VK_1, ActionEvent.ALT_MASK));
    //menuItem.getAccessibleContext().setAccessibleDescription(
    //        "This doesn't really do anything");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
              setConfig(new Configuration());
              ilcFile = null;
              updateTitle();
          }
        });
    menu.add(menuItem);

    menuItem = new JMenuItem("Open...");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){

            fc.resetChoosableFileFilters();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileFilter(new GenericFileFilter("INTERLIS-compiler configuration (*.ilc)","ilc"));
            fc.setCurrentDirectory(new File(settings.getWorkingDirectory()));
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                settings.setWorkingDirectory(fc.getCurrentDirectory().getAbsolutePath());
                // read file
                File file=fc.getSelectedFile();
                try{
                  Configuration c=PersistenceService.readConfig(file.getAbsolutePath());
                  setConfig(c);
                  ilcFile = file;
                  updateTitle();
                }catch(FileNotFoundException ex){
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    ch.interlis.ili2c.Main.APP_NAME+" error",
                    JOptionPane.ERROR_MESSAGE);
                }catch(IOException ex){
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    ch.interlis.ili2c.Main.APP_NAME+" error",
                    JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Open command cancelled by user
            }
          }
        });
    menu.add(menuItem);
    menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
			updateConfigFromUi();
            if(ilcFile==null){
              saveAs();
            }else{
                try{
                  PersistenceService.writeConfig(ilcFile.getAbsolutePath(),config);
                }catch(IOException ex){
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    ch.interlis.ili2c.Main.APP_NAME+" error",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
          }
        });
    menu.add(menuItem);
    menuItem = new JMenuItem("Save as...");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
			updateConfigFromUi();
            saveAs();
          }
        });
    menu.add(menuItem);
    menu.addSeparator();
    menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
		    System.exit(0);
          }
        });

    menu.add(menuItem);

    menu = new JMenu("Tools");
    menu.setMnemonic(KeyEvent.VK_T);
    menuBar.add(menu);
    menuItem = new JMenuItem("Model Repositories...");
	menuItem.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent e){
			RepositoriesDialog dlg=new RepositoriesDialog(frame);
			dlg.setIlidirs(settings.getIlidirs());
			dlg.setHttpProxyHost(settings.getHttpProxyHost());
			dlg.setHttpProxyPort(settings.getHttpProxyPort());
			if(dlg.showDialog()==RepositoriesDialog.OK_OPTION){
				String ilidirs=dlg.getIlidirs();
				if(ilidirs==null){
					ilidirs=UserSettings.DEFAULT_ILIDIRS;
				}
				settings.setIlidirs(ilidirs);
				settings.setHttpProxyHost(dlg.getHttpProxyHost());
				settings.setHttpProxyPort(dlg.getHttpProxyPort());
			}
		}
	});
    menu.add(menuItem);
    
    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menuBar.add(menu);
    menuItem = new JMenuItem(ch.interlis.ili2c.Main.APP_NAME+" Help...");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
		    // show help
                        String ili2cHome=ch.interlis.ili2c.Main.getIli2cHome();
                        BrowserControl.displayURL("file://"+ili2cHome+"/doc/index.html");
        }
        });
    menu.add(menuItem);
    menuItem = new JMenuItem("About...");
	menuItem.addActionListener(new ActionListener(){
		public void actionPerformed(java.awt.event.ActionEvent e){
			AboutDialog dlg=new AboutDialog(frame);
			dlg.show();
		}
	});
    menu.add(menuItem);


  }
  void setConfig(Configuration config){
    this.config = config;
    inputFileList.setModel(config);
    // update other ui elements
    updateUi();
  }
  void updateUi(){
    int kind=config.getOutputKind();
    outputKind.setSelectedItem(new Integer(kind));
    if(kind==GenerateOutputKind.NOOUTPUT){
      outputFileUi.setEditable(false);
    }else{
      outputFileUi.setEditable(true);
      outputFileUi.setText(config.getOutputFile());
    }
    if(config.isGenerateWarnings()){
      warnButton.setSelected(true);
    }else{
      errButton.setSelected(true);
    }
	autoCompleteCB.setSelected(config.isAutoCompleteModelList());
  }
  /** read all values from GUI elements that have no action listeners
   */
  void updateConfigFromUi(){
      config.setOutputFile(outputFileUi.getText());
  }
  void saveAs(){
            fc.resetChoosableFileFilters();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setFileFilter(new GenericFileFilter("INTERLIS-compiler configuration (*.ilc)","ilc"));
            fc.setCurrentDirectory(new File(settings.getWorkingDirectory()));
            int returnVal = fc.showSaveDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                settings.setWorkingDirectory(fc.getCurrentDirectory().getAbsolutePath());
                // write file
                File file=fc.getSelectedFile();
                try{
                  PersistenceService.writeConfig(file.getAbsolutePath(),config);
                  ilcFile = file;
                  updateTitle();
                }catch(IOException ex){
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    ch.interlis.ili2c.Main.APP_NAME+" error",
                    JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Save As command cancelled by user
            }
  }
  public Container buildRunPane()
  {
    GridBagConstraints cnstr = null;
    JPanel compilerPane = new JPanel();
    compilerPane.setLayout(new GridBagLayout());
    cnstr = new GridBagConstraints();
    cnstr.gridy=0;
    cnstr.anchor=java.awt.GridBagConstraints.NORTHWEST;

    cnstr.gridwidth=2;
    JLabel logLabel=new JLabel("Compiler messages");
    compilerPane.add(logLabel,cnstr);
    cnstr.gridwidth=1;

        //compilerPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        errOutput=new JTextArea();

    //...where the GUI is constructed:
    //Create the popup menu.
    errPopup = new JPopupMenu();
    JMenuItem menuItem = new JMenuItem("Clear message log");
    menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            errOutput.setText("");
          }
    });
    errPopup.add(menuItem);

    //Add listener to components that can bring up popup menus.
    MouseListener popupListener = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            errPopup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
    };

    errOutput.addMouseListener(popupListener);

        JScrollPane errScroller = new JScrollPane(errOutput);
        errScroller.setPreferredSize(new Dimension(250, 80));
        errScroller.setMinimumSize(new Dimension(250, 80));

    cnstr.gridy+=1;
    cnstr.fill=java.awt.GridBagConstraints.BOTH;
    cnstr.weightx=1.0;
    cnstr.weighty=1.0;
        compilerPane.add(errScroller,cnstr);


        JPanel buttonPane = new JPanel();
        runButton = new JButton("Run compiler");
        runButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
			updateConfigFromUi();
			if(config.getOutputKind()!=GenerateOutputKind.NOOUTPUT && !"-".equals(config.getOutputFile())){
				File outfile=new File(config.getOutputFile());
				if(outfile.exists()){
					// ask user to overwrite
					int n;
					if(config.getOutputKind()==GenerateOutputKind.ILI1FMTDESC
							|| config.getOutputKind()==GenerateOutputKind.GML32
							|| config.getOutputKind()==GenerateOutputKind.ILIGML2
							|| config.getOutputKind()==GenerateOutputKind.ETF1){
						n = JOptionPane.showConfirmDialog(
							frame,
							"Overwrite files in "+outfile.getAbsolutePath()+"?",
							ch.interlis.ili2c.Main.APP_NAME,
							JOptionPane.YES_NO_OPTION);
					}else{
						n = JOptionPane.showConfirmDialog(
							frame,
							"Overwrite file "+outfile.getAbsolutePath()+"?",
							ch.interlis.ili2c.Main.APP_NAME,
							JOptionPane.YES_NO_OPTION);
					}
					if(n==JOptionPane.NO_OPTION){
						return;
					}

				}
			}
            runCompiler();
          }
        });
        runButton.setMnemonic(KeyEvent.VK_R);

        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        //buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(runButton);
		JButton clrmsgButton = new JButton("Clear message log");
		clrmsgButton.addActionListener(new ActionListener(){
	  		public void actionPerformed(java.awt.event.ActionEvent e){
				errOutput.setText("");
	  		}
		});
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(clrmsgButton);
    cnstr.fill=java.awt.GridBagConstraints.NONE;
    cnstr.weightx=0.0;
    cnstr.weighty=0.0;
    cnstr.gridx=1;
        compilerPane.add(buttonPane,cnstr);
        return compilerPane;
  }
  public Container buildCheckPane(){
    GridBagConstraints cnstr = null;

    //
    // input
    //
    JPanel inputPane=new JPanel();
    inputPane.setLayout(new GridBagLayout());
    cnstr = new GridBagConstraints();
    cnstr.gridy=0;
    cnstr.anchor=java.awt.GridBagConstraints.NORTHWEST;

    cnstr.gridwidth=2;
    autoCompleteCB=new JCheckBox("auto complete fileset");
    autoCompleteCB.addActionListener(new ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent e){
          config.setAutoCompleteModelList(autoCompleteCB.isSelected());
        }
      });
    inputPane.add(autoCompleteCB,cnstr);
    cnstr.gridy+=1;
    
    
    cnstr.gridwidth=2;
    JLabel label = new JLabel("Files");
    inputPane.add(label,cnstr);
    cnstr.gridwidth=1;
    //inputPane.add(Box.createRigidArea(new Dimension(0,5)));
        inputFileList=new FileEntriesAdapter(config);
        inputFileListUi=new JList(inputFileList);
        inputFileListUi.setCellRenderer(new FileEntryRenderer());
        JScrollPane listScroller = new JScrollPane(inputFileListUi);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setMinimumSize(new Dimension(250, 80));
        //listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
    cnstr.gridy+=1;
    cnstr.fill=java.awt.GridBagConstraints.BOTH;
    cnstr.weightx=1.0;
    cnstr.weighty=1.0;
        inputPane.add(listScroller,cnstr);
    cnstr.fill=java.awt.GridBagConstraints.NONE;
    cnstr.weightx=0.0;
    cnstr.weighty=0.0;
    cnstr.gridx=1;
        inputPane.add(buildInputFilesButtonPane(),cnstr);
    cnstr.gridx=0;


    //inputPane.add(Box.createRigidArea(new Dimension(0,5)));

    //
    // output
    //
    JPanel outputPane=new JPanel();
    outputPane.setLayout(new GridBagLayout());
    cnstr = new GridBagConstraints();
    cnstr.gridy=0;
    cnstr.anchor=java.awt.GridBagConstraints.NORTHWEST;

    label = new JLabel("Kind of output");
    outputPane.add(label,cnstr);

    Integer comboBoxItems[] = new Integer[]{
    		 new Integer(GenerateOutputKind.NOOUTPUT)
      , new Integer(GenerateOutputKind.ILI1)
      , new Integer(GenerateOutputKind.ILI2) 
      , new Integer(GenerateOutputKind.XMLSCHEMA)
	  , new Integer(GenerateOutputKind.GML32)
   	  , new Integer(GenerateOutputKind.ILIGML2)
      , new Integer(GenerateOutputKind.ILI1FMTDESC)
	  , new Integer(GenerateOutputKind.IMD)
      , new Integer(GenerateOutputKind.IMD16)
	  , new Integer(GenerateOutputKind.UML21)
	  , new Integer(GenerateOutputKind.IOM)
	  , new Integer(GenerateOutputKind.ETF1)
      };
    JPanel cbp = new JPanel();
    outputKind = new JComboBox(comboBoxItems);
    outputKind.setRenderer(new KindListCellRenderer());
    outputKind.setEditable(false);
        outputKind.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int kind=((Integer)outputKind.getSelectedItem()).intValue();

			outputFileUi.setEditable(true);
			fileLabel.setText("Filename");
            
			if(kind==GenerateOutputKind.NOOUTPUT){
			  outputFileUi.setEditable(false);
			}else if(kind==GenerateOutputKind.GML32){
			   outputFileUi.setEditable(true);
			   fileLabel.setText("Output directory");
			}else if(kind==GenerateOutputKind.ILIGML2){
				   outputFileUi.setEditable(true);
				   fileLabel.setText("Output directory");
			}else if(kind==GenerateOutputKind.ETF1){
				   outputFileUi.setEditable(true);
				   fileLabel.setText("Output directory");
            }else{
            }
            config.setOutputKind(kind);
          }
        });
    cbp.add(outputKind);
    cnstr.gridy+=1;
    outputPane.add(cbp,cnstr);

    cnstr.gridy+=1;
    outputPane.add(fileLabel,cnstr);

    JPanel x=new JPanel();
    outputFileUi=new JTextField();
    outputFileUi.setColumns(30);
    x.add(outputFileUi);
    JButton browseButton=new JButton("Browse...");
        browseButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
          	boolean useDir=false;
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.resetChoosableFileFilters();
            if(config.getOutputKind()==GenerateOutputKind.XMLSCHEMA){
              fc.setFileFilter(GenericFileFilter.createXmlSchemaFilter());
            }else if(config.getOutputKind()==GenerateOutputKind.ILI1FMTDESC){
              fc.setFileFilter(new GenericFileFilter("INTERLIS 1 format (*.fmt)","fmt"));
			}else if(config.getOutputKind()==GenerateOutputKind.IOM){
			  fc.setFileFilter(GenericFileFilter.createXmlFilter());
			}else if(config.getOutputKind()==GenerateOutputKind.IMD){
				  fc.setFileFilter(GenericFileFilter.createXmlFilter());
			}else if(config.getOutputKind()==GenerateOutputKind.IMD16){
				  fc.setFileFilter(GenericFileFilter.createXmlFilter());
			}else if(config.getOutputKind()==GenerateOutputKind.UML21){
				  fc.setFileFilter(new GenericFileFilter("UML/XMI format (*.uml)","uml"));
			}else if(config.getOutputKind()==GenerateOutputKind.GML32){
			  useDir=true;
			}else if(config.getOutputKind()==GenerateOutputKind.ILIGML2){
				  useDir=true;
			}else if(config.getOutputKind()==GenerateOutputKind.ETF1){
				  useDir=true;
            }else{
              fc.setFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
            }
            fc.setCurrentDirectory(new File(settings.getWorkingDirectory()));
            // file entries available?
            if(config.getSizeFileEntry()>0){
              // offer user name of last file as default output file name
              // (without extension!)
              // get name of last file in list
              String name = new File(
                  config.getFileEntry(config.getSizeFileEntry()-1).getFilename()
                  ).getName();
              // remove extension from name
              int i = name.lastIndexOf('.');
              if (i >= 0 &&  i < name.length()) {
                  name = name.substring(0,i);
              }
              fc.setSelectedFile(new File(settings.getWorkingDirectory(),name));
            }
			int returnVal;
            if(useDir){
				returnVal = fc.showOutputDirDialog(frame);
            }else{
				returnVal = fc.showSaveDialog(frame);
            }
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                settings.setWorkingDirectory(fc.getCurrentDirectory().getAbsolutePath());
                java.io.File file = fc.getSelectedFile();
                // user selected a file
                outputFileUi.setText(file.getAbsolutePath());
            } else {
                // Open command cancelled by user
            }
          }
        });

    x.add(browseButton);
    cnstr.gridy+=1;
    outputPane.add(x,cnstr);

    // Create the radio buttons.
    warnButton = new JRadioButton("Report errors and warnings");
    //birdButton.setMnemonic(KeyEvent.VK_B);
    //birdButton.setActionCommand(birdString);
    warnButton.setSelected(true);
        warnButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            if(warnButton.isSelected()){
              config.setGenerateWarnings(true);
            }
          }
        });

    errButton = new JRadioButton("Report only errors");
    //catButton.setMnemonic(KeyEvent.VK_C);
    //catButton.setActionCommand(catString);
        errButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            if(errButton.isSelected()){
              config.setGenerateWarnings(false);
            }
          }
        });

    // Group the radio buttons.
    ButtonGroup group = new ButtonGroup();
    group.add(warnButton);
    group.add(errButton);
    cnstr.gridy+=1;
    outputPane.add(warnButton,cnstr);
    cnstr.gridy+=1;
    outputPane.add(errButton,cnstr);

    //
    // tabbed pane
    //
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Input",inputPane);
    tabbedPane.addTab("Output",outputPane);
    tabbedPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    return tabbedPane;
  }

  public Container buildInputFilesButtonPane(){
        JPanel filesButtonPane = new JPanel();
        JButton addIliFileButton = new JButton("Add Model (.ili)...");
        addIliFileButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){

            fc.resetChoosableFileFilters();
            fc.setFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
            fc.setCurrentDirectory(new File(settings.getWorkingDirectory()));
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                settings.setWorkingDirectory(fc.getCurrentDirectory().getAbsolutePath());
                java.io.File file = fc.getSelectedFile();
                // user selected a file
                FileEntry entry=new FileEntry(file.getAbsolutePath(),FileEntryKind.ILIMODELFILE);
                inputFileList.addElement(entry);
            } else {
                // Open command cancelled by user
            }
          }
        });
        JButton removeFileButton = new JButton("Remove");
        removeFileButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int idxv[]=inputFileListUi.getSelectedIndices();
            // items selected?
            if(idxv.length>0){
              // remove them from model
              for(int i=idxv.length-1;i>=0;i--){
                inputFileList.remove(idxv[i]);
              }
              if(inputFileList.getSize()>0){
                if(idxv[0]<inputFileList.getSize()){
                  inputFileListUi.setSelectedIndex(idxv[0]);
                }else{
                  inputFileListUi.setSelectedIndex(inputFileList.getSize()-1);
                }
              }
            }
          }
        });
        JButton moveFileUpButton = new JButton("Move up");
        moveFileUpButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int idx=inputFileListUi.getSelectedIndex();
            // item selected?
            if(idx>0){
              // move it up in model
              FileEntry elt=inputFileList.remove(idx);
              inputFileList.add(idx-1,elt);
              inputFileListUi.setSelectedIndex(idx-1);
            }
          }
        });
        JButton moveFileDownButton = new JButton("Move down");
        moveFileDownButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int idx=inputFileListUi.getSelectedIndex();
            // item selected?
            if(idx!=-1 && idx+1<inputFileList.getSize()){
              // move it down in model
              FileEntry elt=inputFileList.remove(idx);
              inputFileList.add(idx+1,elt);
              inputFileListUi.setSelectedIndex(idx+1);
            }
          }
        });
        filesButtonPane.setLayout(new BoxLayout(filesButtonPane, BoxLayout.Y_AXIS));
        filesButtonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        filesButtonPane.add(addIliFileButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(removeFileButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(moveFileUpButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(moveFileDownButton);
        return filesButtonPane;
  }
  public Main(){
     fc=new FileChooser();
     config.setAutoCompleteModelList(true);
  }
  
  private class KindListCellRenderer extends JLabel implements ListCellRenderer{

		public Component getListCellRendererComponent(JList arg0, Object obj,
				int arg2, boolean arg3, boolean arg4) {
			int kind=((Integer)obj).intValue();
			setText(GenerateOutputKind.getDescription(kind));
			return this;
		}
  	
  	
  };
  
  
  public static void main(String[] args) {
    Main instance=new Main();
    instance.settings = UserSettings.load();
    ch.interlis.ili2c.Main.setDefaultIli2cPathMap(instance.settings);
    
    instance.run();

  }






}
