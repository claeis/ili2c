package ch.interlis.ili2c.gui;

import javax.swing.JDialog;

/**
 * @author ce
 */
public class AboutDialog extends JDialog {

	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JPanel infoPanel = null;
	private javax.swing.JPanel buttonPanel = null;
	private javax.swing.JButton okButton = null;
	private javax.swing.JLabel programVersion = null;
	private javax.swing.JLabel iliVersion = null;
	private javax.swing.JLabel jLabel4 = null;
	private javax.swing.JLabel jLabel5 = null;
	private javax.swing.JLabel javaVersion = null;
	private javax.swing.JLabel javaVMversion = null;
	/**
	 * This method initializes 
	 * 
	 */
	public AboutDialog(java.awt.Frame owner) {
		super(owner,true);
		initialize();
		getProgramVersion().setText(ch.interlis.ili2c.metamodel.TransferDescription.getVersion());
		getJavaVersion().setText(System.getProperty("java.version"));
		getJavaVMversion().setText(System.getProperty("java.vm.version"));
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getJContentPane());
        this.setSize(238, 170);
        this.setTitle("About ili2c");
			
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getInfoPanel(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Program version");
		}
		return jLabel;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText("INTERLIS version");
		}
		return jLabel1;
	}
	/**
	 * This method initializes infoPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getInfoPanel() {
		if(infoPanel == null) {
			infoPanel = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints2 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints4 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints5 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();
			consGridBagConstraints8.gridx = 0;
			consGridBagConstraints8.gridy = 3;
			consGridBagConstraints5.gridx = 1;
			consGridBagConstraints5.gridy = 1;
			consGridBagConstraints6.gridx = 1;
			consGridBagConstraints6.gridy = 0;
			consGridBagConstraints2.gridx = 0;
			consGridBagConstraints2.gridy = 0;
			consGridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints2.insets = new java.awt.Insets(0,0,5,12);
			consGridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints7.gridx = 0;
			consGridBagConstraints7.gridy = 2;
			consGridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints7.insets = new java.awt.Insets(0,0,5,12);
			consGridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints10.gridx = 1;
			consGridBagConstraints10.gridy = 3;
			consGridBagConstraints9.gridx = 1;
			consGridBagConstraints9.gridy = 2;
			consGridBagConstraints4.gridx = 0;
			consGridBagConstraints4.gridy = 1;
			consGridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints4.insets = new java.awt.Insets(0,0,5,12);
			consGridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints8.insets = new java.awt.Insets(0,0,5,12);
			consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
			consGridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
			infoPanel.setLayout(new java.awt.GridBagLayout());
			infoPanel.add(getJLabel(), consGridBagConstraints2);
			infoPanel.add(getJLabel1(), consGridBagConstraints4);
			infoPanel.add(getProgramVersion(), consGridBagConstraints6);
			infoPanel.add(getIliVersion(), consGridBagConstraints5);
			infoPanel.add(getJLabel4(), consGridBagConstraints7);
			infoPanel.add(getJLabel5(), consGridBagConstraints8);
			infoPanel.add(getJavaVersion(), consGridBagConstraints9);
			infoPanel.add(getJavaVMversion(), consGridBagConstraints10);
		}
		return infoPanel;
	}
	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getButtonPanel() {
		if(buttonPanel == null) {
			buttonPanel = new javax.swing.JPanel();
			buttonPanel.add(getOkButton(), null);
		}
		return buttonPanel;
	}
	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getOkButton() {
		if(okButton == null) {
			okButton = new javax.swing.JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					dispose();
				}
			});
		}
		return okButton;
	}
	/**
	 * This method initializes programVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getProgramVersion() {
		if(programVersion == null) {
			programVersion = new javax.swing.JLabel();
			programVersion.setText("JLabel");
		}
		return programVersion;
	}
	/**
	 * This method initializes iliVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getIliVersion() {
		if(iliVersion == null) {
			iliVersion = new javax.swing.JLabel();
			iliVersion.setText("2.3");
		}
		return iliVersion;
	}
	/**
	 * This method initializes jLabel4
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel4() {
		if(jLabel4 == null) {
			jLabel4 = new javax.swing.JLabel();
			jLabel4.setText("Java version");
		}
		return jLabel4;
	}
	/**
	 * This method initializes jLabel5
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel5() {
		if(jLabel5 == null) {
			jLabel5 = new javax.swing.JLabel();
			jLabel5.setText("Java-VM version");
		}
		return jLabel5;
	}
	/**
	 * This method initializes javaVersion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJavaVersion() {
		if(javaVersion == null) {
			javaVersion = new javax.swing.JLabel();
			javaVersion.setText("JLabel");
		}
		return javaVersion;
	}
	/**
	 * This method initializes javaVMversion
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJavaVMversion() {
		if(javaVMversion == null) {
			javaVMversion = new javax.swing.JLabel();
			javaVMversion.setText("JLabel");
		}
		return javaVMversion;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="167,30"
