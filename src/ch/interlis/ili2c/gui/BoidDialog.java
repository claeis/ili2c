package ch.interlis.ili2c.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class BoidDialog extends JDialog {
    public final static int OK_OPTION=1;
    public final static int CANCEL_OPTION=2;
    private int pressedButton=CANCEL_OPTION;
    private String basket="";
    private String boid="";
    private JTextField basketUi=new JTextField();
    private JTextField boidUi=new JTextField();
    public BoidDialog(Frame aFrame) {
        super(aFrame, /* modal */ true);

        setTitle("Enter a mapping from basket name in model to an existing basket in a data file");
        JPanel pane=new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

          JPanel dataPane = new JPanel();
          JPanel data1Pane = new JPanel();
          dataPane.setLayout(new BoxLayout(dataPane, BoxLayout.Y_AXIS));
          JLabel basketLbl=new JLabel("MetaDataUseRef");
          dataPane.add(basketLbl);
          dataPane.add(Box.createRigidArea(new Dimension(0,5)));
          basketUi.setColumns(50);
          data1Pane.add(basketUi);
          dataPane.add(data1Pane);
          dataPane.add(Box.createRigidArea(new Dimension(0,5)));
          JLabel boidLbl=new JLabel("BID");
          dataPane.add(boidLbl);
          dataPane.add(Box.createRigidArea(new Dimension(0,5)));
          JPanel data2Pane = new JPanel();
          boidUi.setColumns(50);
          data2Pane.add(boidUi);
          dataPane.add(data2Pane);
          dataPane.add(Box.createVerticalGlue());
          dataPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
          pane.add(dataPane);

          JPanel buttonPane = new JPanel();
          buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
          buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
          buttonPane.add(Box.createHorizontalGlue());
          JButton ok=new JButton("Ok");
          ok.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  pressedButton=OK_OPTION;
                  basket=basketUi.getText();
                  boid=boidUi.getText();
                  dispose();
              }
          });
          buttonPane.add(ok);
          buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
          JButton cancel=new JButton("Cancel");
          cancel.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  pressedButton=CANCEL_OPTION;
                  dispose();
              }
          });
          buttonPane.add(cancel);
          pane.add(buttonPane);

        getRootPane().setDefaultButton(ok);
        getContentPane().add(pane);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack(); // realize it
    }
    public int showDialog(){
      show();
      return pressedButton;
    }
    public String getBoid(){
      return boid;
    }
    public void setBoid(String boid1){
      boid=boid1;
      boidUi.setText(boid);
    }
    public String getBasket(){
      return basket;
    }
    public void setBasket(String basket1){
      basket=basket1;
      basketUi.setText(basket);
    }
}
