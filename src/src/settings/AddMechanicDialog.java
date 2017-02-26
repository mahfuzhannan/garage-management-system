/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import assets.GMButton;
import assets.GMFormattedTextField;
import assets.Res;
import common.Database;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author mafz
 */
public class AddMechanicDialog extends JDialog{
    
    private JLabel label;
    private Integer id;
    private GMFormattedTextField mechanicName, rate;
    private GMButton add, cancel;
    private JComboBox bay;
    private boolean isUpdate = false;

    public AddMechanicDialog(final boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.setModal(true);
        this.setLayout(new BorderLayout(5, 5));
        this.getContentPane().setBackground(Res.BKG_COLOR);
        
        JPanel fieldPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        fieldPanel.setBackground(Res.BKG_COLOR);
        fieldPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        label = new JLabel("Name: ");
        label.setForeground(Res.FONT_COLOR);
        mechanicName = new GMFormattedTextField();
        fieldPanel.add(label);
        fieldPanel.add(mechanicName);

        label = new JLabel("Rate: ");
        label.setForeground(Res.FONT_COLOR);
        rate = new GMFormattedTextField(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
        fieldPanel.add(label);
        fieldPanel.add(rate);
        
        label = new JLabel("Bay: ");
        label.setForeground(Res.FONT_COLOR);
        bay = new JComboBox(new DefaultComboBoxModel(new Integer[]{1,2,3,4,5,6}));
        fieldPanel.add(label);
        fieldPanel.add(bay);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.setBackground(Res.BKG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        add = new GMButton("ADD");
        if(isUpdate)
            add.setText("UPDATE");
        cancel = new GMButton("CANCEL");
        
        buttonPanel.add(add);
        buttonPanel.add(cancel);
        
        label = new JLabel("Mechanic");
        label.setOpaque(false);
        label.setFont(new Font("", 0, 26));
        label.setForeground(Res.FONT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, BorderLayout.NORTH);
        this.add(fieldPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        // add listeners
        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean valuesAdded = true;
                if(mechanicName.getText() == null || rate.getText() == null)
                    valuesAdded = false;
                if(valuesAdded) {
                    String query = "INSERT INTO Mechanic (\"Mechanic Name\", \"Hourly Rate\", Bay) "
                            + "VALUES ('"+mechanicName.getText()+"', '"+rate.getText()+"', '"+bay.getSelectedItem()+"');";
                    if(isUpdate)
                        query = "UPDATE Mechanic SET \"Mechanic Name\" = '"+mechanicName.getText()+"', "
                                + "\"Hourly Rate\" = '"+rate.getText()+"', Bay = '"+bay.getSelectedItem()+"' "
                                + "WHERE \"Mechanic ID\" = '"+id+"';";
                    try {
                        Database.executeUpdate(query);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Mechanic not added");
                    }
                    dispose();
                }
            }
        });
        
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        
        
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void setMechanicName(String name) {
        mechanicName.setText(name);
    }

    public void setRate(Integer rate) {
        this.rate.setText(rate.toString());
    }

    public void setBay(Integer bay) {
        this.bay.setSelectedItem(bay);
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    
}
