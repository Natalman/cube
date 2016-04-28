package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;

/**
 * Created by TheKingNat on 4/27/16.
 */
public class CubeForm extends JFrame implements WindowListener{
    private JPanel rootPanel;
    private JTable CubeTable;
    private JLabel NameLabel;
    private JLabel TimeLabel;
    private JTextField NameTextField;
    private JTextField TimeTextField;
    private JButton addCubeButton;
    private JButton deleteCubeButton;
    private JButton quitButton;

    CubeForm(final CubeModel cubeModelTable){

        setContentPane(rootPanel);
        pack();
        setTitle("Cubes Database Application");
        addWindowListener(this);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Customizing cubes table
        CubeTable.setGridColor(Color.BLACK);
        CubeTable.setModel(cubeModelTable);


        addCubeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Get The name of the cube, make sure it's not blank
                String NameData = NameTextField.getText();

                if (NameData == null || NameData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter the name of the new cube");
                    return;
                }


                //Getting the second performed
                double TimeData;
                TimeData = Double.parseDouble(TimeTextField.getText());

                if (TimeData == 0 ) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter the in second your performance");
                    return;
                }

                System.out.println("Adding " + NameData + " " + TimeData);
                boolean insertedRow = cubeModelTable.insertRow(NameData,TimeData);

                if (!insertedRow) {
                    JOptionPane.showMessageDialog(rootPane, "Error adding new Cubes");
                }

            }
        });
        deleteCubeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = CubeTable.getSelectedRow();

                if (currentRow == -1) {
                    JOptionPane.showMessageDialog(rootPane, "Please choose a Cube to delete");
                }
                boolean deleted = cubeModelTable.deleteRow(currentRow);
                if (deleted) {
                    CubeData.loadCubes();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting Cubes");
                }
            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CubeData.shutdown();
                System.exit(0);
            }
        });
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        CubeData.shutdown();}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

}
