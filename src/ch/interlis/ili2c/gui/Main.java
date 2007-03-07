package ch.interlis.ili2c.gui;

import java.util.*;
import java.text.DateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ili2c.metamodel.ErrorListener;
import ch.interlis.ili2c.parser.Ili2Parser;
import ch.interlis.ili2c.config.*;
import ch.ehi.basics.view.*;

public class Main {
        private UserSettings settings;
        final static String CHECKPANEL = "Check/Generate";
        //final static String COMPAREPANEL = "Compare";
        JFrame frame;
        JPanel cards;
        Container checkPane;
        Container comparePane;
        FileEntriesAdapter inputFileList;
        JList inputFileListUi;
        JButton addXmlFileButton;
        BoidEntriesAdapter inputBoidList;
        JList inputBoidListUi;
        JButton addBoidButton;
        JButton editBoidButton;
        JButton removeBoidButton;
        JTextField outputFileUi;
        JRadioButton warnButton;
        JRadioButton errButton;
        JCheckBox predefButton;
        JCheckBox checkMetaObjsButton;
        JComboBox outputKind;
        JTextArea  errOutput;
        JPopupMenu errPopup;
        JButton runButton;

        FileChooser fc;
	// iboto (Java generator) controls
	// added by Swen Ruetimann
	//JCheckBox definePackagePrefixBox;
	JTextField packagePrefixField;
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
        // the following leads to a blocked dialog; dont't know why
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
            settings.save();
	    	System.exit(0);
          }
        });

        buildMenu();

        updateUi();
		frame.setSize(508,490);
        frame.pack();
        frame.setVisible(true);
  }
  private class MyErrorListener implements ch.interlis.ili2c.metamodel.ErrorListener
  {
    int numSupressed = 0;
    int severityLevel = 0;

    public void setSeverityLevel (int level)
    {
      severityLevel = level;
    }

    public void error (ErrorListener.ErrorEvent evt)
    {
      if (evt.getSeverity () < severityLevel)
        numSupressed = numSupressed + 1;
      else{
        errOutput.append(evt.toString ());
        errOutput.append("\n");
        if(evt.getException()!=null){
			evt.getException().printStackTrace(ch.interlis.ili2c.metamodel.Trace.getTraceStream());
        }
      }
    }
  };

  private void runCompiler(){
      TransferDescription   desc = new TransferDescription ();
      MyErrorListener el = new MyErrorListener();
      if(config.isGenerateWarnings()){
        el.setSeverityLevel (ErrorListener.ErrorEvent.SEVERITY_WARNING);
      }else{
        el.setSeverityLevel (ErrorListener.ErrorEvent.SEVERITY_ERROR);
      }
      boolean emitPredefined=config.isIncPredefModel();
      boolean checkMetaObjs=config.isCheckMetaObjs();

        // boid  to basket mappings
        Iterator boidi=config.iteratorBoidEntry();
        while(boidi.hasNext()){
          BoidEntry e=(BoidEntry)boidi.next();
          desc.addMetadataMapping(e.getMetaDataUseDef(),e.getBoid());
        }


        // model and metadata files
        Iterator filei=config.iteratorFileEntry();
        while(filei.hasNext()){
          FileEntry e=(FileEntry)filei.next();
          if(e.getKind()==FileEntryKind.METADATAFILE){
            /* Don't continue if there is a fatal error. */
            if(!ch.interlis.ili2c.parser.MetaObjectParser.parse (
              desc, e.getFilename(), /* error listener */ el)){
            	return;
            }

          }else{
            String streamName = e.getFilename();
            FileInputStream stream = null;
            try {
              stream = new FileInputStream(streamName);
            } catch (java.io.FileNotFoundException ex) {
              //System.err.println (args[i] + ":" + "There is no such file.");
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    "ili2c error",
                    JOptionPane.ERROR_MESSAGE);
              return;
            } catch (Exception ex) {
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    "ili2c error",
                    JOptionPane.ERROR_MESSAGE);
              return;
            }

            if (!Ili2Parser.parseIliFile (desc,streamName, stream, el,checkMetaObjs))
               return;
          }
        }

        // output options
        BufferedWriter out=null;
        try{
        switch(config.getOutputKind()){
          case GenerateOutputKind.NOOUTPUT:
            break;
          case GenerateOutputKind.ILI1:
            if("-".equals(config.getOutputFile())){
              out=new BufferedWriter(new OutputStreamWriter(System.out));;
            }else{
              try{
                out = new BufferedWriter(new FileWriter(config.getOutputFile()));
              }catch(IOException ex){
                    JOptionPane.showMessageDialog(frame,
                      ex.getLocalizedMessage(),
                      "ili2c error",
                      JOptionPane.ERROR_MESSAGE);
                    return;
              }
            }
            ch.interlis.ili2c.generator.Interlis1Generator.generate(out, desc);
            break;
          case GenerateOutputKind.ILI2:
            if("-".equals(config.getOutputFile())){
              out=new BufferedWriter(new OutputStreamWriter(System.out));;
            }else{
              try{
                out = new BufferedWriter(new FileWriter(config.getOutputFile()));
              }catch(IOException ex){
                    JOptionPane.showMessageDialog(frame,
                      ex.getLocalizedMessage(),
                      "ili2c error",
                      JOptionPane.ERROR_MESSAGE);
                    return;
              }
            }
           ch.interlis.ili2c.generator.Interlis2Generator.generate(
              out, desc, emitPredefined);
            break;
          case GenerateOutputKind.XMLSCHEMA:
            if("-".equals(config.getOutputFile())){
              out=new BufferedWriter(new OutputStreamWriter(System.out));;
            }else{
              try{
                out = new BufferedWriter(new FileWriter(config.getOutputFile()));
              }catch(IOException ex){
                    JOptionPane.showMessageDialog(frame,
                      ex.getLocalizedMessage(),
                      "ili2c error",
                      JOptionPane.ERROR_MESSAGE);
                    return;
              }
            }
            ch.interlis.ili2c.generator.XSDGenerator.generate (
              out, desc);
            break;
          case GenerateOutputKind.ILI1FMTDESC:
            if("-".equals(config.getOutputFile())){
              out=new BufferedWriter(new OutputStreamWriter(System.out));;
            }else{
              try{
                out = new BufferedWriter(new FileWriter(config.getOutputFile()));
              }catch(IOException ex){
                    JOptionPane.showMessageDialog(frame,
                      ex.getLocalizedMessage(),
                      "ili2c error",
                      JOptionPane.ERROR_MESSAGE);
                    return;
              }
            }
            ch.interlis.ili2c.generator.Interlis1Generator.generateFmt(out, desc);
            break;
          case GenerateOutputKind.JAVA:
            ch.interlis.ili2c.generator.java.JavaGenerator.generate(desc,config.getOutputFile(),packagePrefixField.getText());
            break;
		  case GenerateOutputKind.GML32:
			  ch.interlis.ili2c.generator.Gml32Generator.generate(desc,config.getOutputFile(),el);
			  break;
			case GenerateOutputKind.IOM:
			  if("-".equals(config.getOutputFile())){
				out=new BufferedWriter(new OutputStreamWriter(System.out));;
			  }else{
				try{
				  out = new BufferedWriter(new FileWriter(config.getOutputFile()));
				}catch(IOException ex){
					  JOptionPane.showMessageDialog(frame,
						ex.getLocalizedMessage(),
						"ili2c error",
						JOptionPane.ERROR_MESSAGE);
					  return;
				}
			  }
			  ch.interlis.ili2c.generator.iom.IomGenerator.generate(out, desc,el);
			  break;
          default:
            // ignore
            break;
        }
		}finally{
			if(out!=null){
				try{
				out.close();
				}catch(IOException ex){
					  JOptionPane.showMessageDialog(frame,
						ex.getLocalizedMessage(),
						"ili2c error",
						JOptionPane.ERROR_MESSAGE);
					  return;
				}
			}
			out=null;
		}

  }
  private void updateTitle(){
    if(ilcFile!=null){
      frame.setTitle("ili2c - "+ilcFile.getAbsolutePath());
    }else{
      frame.setTitle("ili2c");
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
            fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS-compiler configuration (*.ilc)","ilc"));
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
                    "ili2c error",
                    JOptionPane.ERROR_MESSAGE);
                }catch(IOException ex){
                  JOptionPane.showMessageDialog(frame,
                    ex.getLocalizedMessage(),
                    "ili2c error",
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
                    "ili2c error",
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

    menu = new JMenu("Help");
    menu.setMnemonic(KeyEvent.VK_H);
    menuBar.add(menu);
    menuItem = new JMenuItem("ili2c Help...");
        menuItem.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
		    // show help
                        String ili2cHome;
                        String classpath = System
				.getProperty("java.class.path");
			int index = classpath.toLowerCase()
				.indexOf("ili2c.jar");
			int start = classpath.lastIndexOf(File
				.pathSeparator,index) + 1;
			if(index > start)
			{
				ili2cHome = classpath.substring(start,
					index - 1);
			}else{
                           ili2cHome =System.getProperty("user.dir");
                        }
                        BrowserControl.displayURL(ili2cHome+"/doc/index.html");
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
    inputBoidList.setModel(config);
    // update other ui elements
    updateUi();
  }
  void updateUi(){
    int kind=config.getOutputKind();
    outputKind.setSelectedIndex(kind-1);
    if(kind==GenerateOutputKind.NOOUTPUT){
      outputFileUi.setEditable(false);
	}else if(kind == GenerateOutputKind.JAVA){
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
    predefButton.setSelected(config.isIncPredefModel());
    checkMetaObjsButton.setSelected(config.isCheckMetaObjs());
    updateUicheckMetaObj(config.isCheckMetaObjs());
  }
  void updateUicheckMetaObj(boolean checkMetaObj){
    if(checkMetaObj){
              inputBoidListUi.setEnabled(true);
              addXmlFileButton.setEnabled(true);
              addBoidButton.setEnabled(true);
              editBoidButton.setEnabled(true);
              removeBoidButton.setEnabled(true);
    }else{
              inputBoidListUi.setEnabled(false);
              addXmlFileButton.setEnabled(false);
              addBoidButton.setEnabled(false);
              editBoidButton.setEnabled(false);
              removeBoidButton.setEnabled(false);
    }
  }
  /** read all values from GUI elements that have no action listeners
   */
  void updateConfigFromUi(){
      config.setOutputFile(outputFileUi.getText());
  }
  void saveAs(){
            fc.resetChoosableFileFilters();
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS-compiler configuration (*.ilc)","ilc"));
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
                    "ili2c error",
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
							|| config.getOutputKind()==GenerateOutputKind.GML32){
						n = JOptionPane.showConfirmDialog(
							frame,
							"Overwrite files in "+outfile.getAbsolutePath()+"?",
							"ili2c",
							JOptionPane.YES_NO_OPTION);
					}else{
						n = JOptionPane.showConfirmDialog(
							frame,
							"Overwrite file "+outfile.getAbsolutePath()+"?",
							"ili2c",
							JOptionPane.YES_NO_OPTION);
					}
					if(n==JOptionPane.NO_OPTION){
						return;
					}

				}
			}
            runCompiler();
            Date today;
            String dateOut;
            DateFormat dateFormatter;

            dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                   DateFormat.SHORT,
                                   Locale.getDefault());
            today = new Date();
            dateOut = dateFormatter.format(today);

            errOutput.append("--- compiler run done "+dateOut+"\n");
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

    checkMetaObjsButton = new JCheckBox("check if referenced metaobjects exist");
    checkMetaObjsButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            config.setCheckMetaObjs(checkMetaObjsButton.isSelected());
            updateUicheckMetaObj(config.isCheckMetaObjs());
          }
        });

    cnstr.gridwidth=2;
    cnstr.gridy+=1;
    inputPane.add(checkMetaObjsButton,cnstr);

    label = new JLabel("Mappings from a basket name in a model to an existing basket in a data file");
    cnstr.gridy+=1;
    cnstr.gridwidth=2;
    inputPane.add(label,cnstr);
    cnstr.gridwidth=1;
    //inputPane.add(Box.createRigidArea(new Dimension(0,5)));

        inputBoidList=new BoidEntriesAdapter(config);
        inputBoidListUi=new JList(inputBoidList);
        inputBoidListUi.setCellRenderer(new BoidEntryRenderer());
        listScroller = new JScrollPane(inputBoidListUi);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setMinimumSize(new Dimension(250, 80));
    cnstr.gridy+=1;
    cnstr.fill=java.awt.GridBagConstraints.BOTH;
    cnstr.weightx=1.0;
    cnstr.weighty=1.0;
        inputPane.add(listScroller,cnstr);
    cnstr.fill=java.awt.GridBagConstraints.NONE;
    cnstr.weightx=0.0;
    cnstr.weighty=0.0;
    cnstr.gridx=1;
        inputPane.add(buildInputBoidButtonPane(),cnstr);
    cnstr.gridx=0;

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

    String comboBoxItems[] = {
      "Generate no output"
      , "Generate an INTERLIS 1 model"
      , "Generate an INTERLIS 2 model"
      , "Generate an XML-Schema"
      , "Generate an ILI1 FMT-Description"
      , "Generate JAVA classes"
	  , "Generate a GML-Schema"
	  , "Generate Model as INTERLIS-Transfer (XTF)"
      };
    JPanel cbp = new JPanel();
    outputKind = new JComboBox(comboBoxItems);
    outputKind.setEditable(false);
        outputKind.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int kind=outputKind.getSelectedIndex()+1;

			packagePrefixField.setEditable(false);
			outputFileUi.setEditable(true);
			fileLabel.setText("Filename");
            
			if(kind==GenerateOutputKind.NOOUTPUT){
			  outputFileUi.setEditable(false);
			}else if(kind==GenerateOutputKind.JAVA){
			   packagePrefixField.setEditable(true);
			   fileLabel.setText("Output directory");
			}else if(kind==GenerateOutputKind.GML32){
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
              fc.addChoosableFileFilter(GenericFileFilter.createXmlSchemaFilter());
            }else if(config.getOutputKind()==GenerateOutputKind.ILI1FMTDESC){
              fc.addChoosableFileFilter(new GenericFileFilter("ILI1 Format (*.fmt)","fmt"));
			}else if(config.getOutputKind()==GenerateOutputKind.IOM){
			  fc.addChoosableFileFilter(GenericFileFilter.createXmlFilter());
			}else if(config.getOutputKind()==GenerateOutputKind.JAVA){
			  useDir=true;
			}else if(config.getOutputKind()==GenerateOutputKind.GML32){
			  useDir=true;
            }else{
              fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
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


    predefButton = new JCheckBox("Include the predefined MODEL INTERLIS");
    //chinButton.setMnemonic(KeyEvent.VK_C);
    //chinButton.setSelected(true);
        predefButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            config.setIncPredefModel(predefButton.isSelected());
          }
        });
    cnstr.gridy+=1;
    outputPane.add(predefButton,cnstr);

	// Start: ibito extension by Ruetimann, Swen
	packagePrefixField = new JTextField("gensrc");
	packagePrefixField.setColumns(30);
	packagePrefixField.setEditable(false);
	cnstr.gridy +=1;
	outputPane.add( new JLabel("Package prefix"),cnstr );
	cnstr.gridy +=1;
	outputPane.add(packagePrefixField, cnstr );
	// End: ibito extension by Ruetimann, Swen

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
            fc.addChoosableFileFilter(new GenericFileFilter("INTERLIS models (*.ili)","ili"));
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
        addXmlFileButton = new JButton("Add Metadata basket (.xml)...");
        addXmlFileButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){

            fc.resetChoosableFileFilters();
            fc.addChoosableFileFilter(new GenericFileFilter("Metadata baskets (*.xml)","xml"));
            fc.setCurrentDirectory(new File(settings.getWorkingDirectory()));
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                settings.setWorkingDirectory(fc.getCurrentDirectory().getAbsolutePath());
                java.io.File file = fc.getSelectedFile();
                // user selected a file
                FileEntry entry=new FileEntry(file.getAbsolutePath(),FileEntryKind.METADATAFILE);
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
        filesButtonPane.add(addXmlFileButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(removeFileButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(moveFileUpButton);
        filesButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        filesButtonPane.add(moveFileDownButton);
        return filesButtonPane;
  }
  public Container buildInputBoidPane(){
        JPanel boidPane = new JPanel();
        boidPane.setLayout(new BoxLayout(boidPane, BoxLayout.X_AXIS));
        inputBoidList=new BoidEntriesAdapter(config);
        inputBoidListUi=new JList(inputBoidList);
        inputBoidListUi.setCellRenderer(new BoidEntryRenderer());
        JScrollPane listScroller = new JScrollPane(inputBoidListUi);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setMinimumSize(new Dimension(250, 80));
        listScroller.setAlignmentX(Container.LEFT_ALIGNMENT);
        boidPane.add(listScroller);
        boidPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        boidPane.add(buildInputBoidButtonPane());
        return boidPane;
  }
  public Container buildInputBoidButtonPane(){
        JPanel boidButtonPane = new JPanel();
        addBoidButton = new JButton("Add mapping...");
        addBoidButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
                BoidDialog d=new BoidDialog(frame);
                if(d.showDialog()==BoidDialog.OK_OPTION){
                  BoidEntry entry=new BoidEntry(d.getBasket(),d.getBoid());
                  inputBoidList.addElement(entry);
                }
          }
        });
        editBoidButton = new JButton("Edit mapping...");
        editBoidButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int idx=inputBoidListUi.getSelectedIndex();
            if(idx>=0){
                BoidDialog d=new BoidDialog(frame);
                BoidEntry entry=(BoidEntry)inputBoidList.getElementAt(idx);
                d.setBoid(entry.getBoid());
                d.setBasket(entry.getMetaDataUseDef());
                if(d.showDialog()==BoidDialog.OK_OPTION){
                  entry.setBoid(d.getBoid());
                  entry.setMetaDataUseDef(d.getBasket());
                  inputBoidList.elementAtChanged(idx);
                }
            }
          }
        });
        removeBoidButton = new JButton("Remove");
        removeBoidButton.addActionListener(new ActionListener(){
          public void actionPerformed(java.awt.event.ActionEvent e){
            int[] idxv=inputBoidListUi.getSelectedIndices();
            if(idxv.length>0){
              // remove them from model
              for(int i=idxv.length-1;i>=0;i--){
                inputBoidList.remove(idxv[i]);
              }
              if(inputBoidList.getSize()>0){
                if(idxv[0]<inputBoidList.getSize()){
                  inputBoidListUi.setSelectedIndex(idxv[0]);
                }else{
                  inputBoidListUi.setSelectedIndex(inputBoidList.getSize()-1);
                }
              }
            }
          }
        });
        boidButtonPane.setLayout(new BoxLayout(boidButtonPane, BoxLayout.Y_AXIS));
        boidButtonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        boidButtonPane.add(addBoidButton);
        boidButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        boidButtonPane.add(editBoidButton);
        boidButtonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        boidButtonPane.add(removeBoidButton);
        return boidButtonPane;
  }
  public Main(){
     fc=new FileChooser();
  }
  public static void main(String[] args) {
    Main instance=new Main();
    instance.settings = UserSettings.load();

    instance.run();

  }






}