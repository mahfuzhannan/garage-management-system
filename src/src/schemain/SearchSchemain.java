/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain;

import assets.GMButton;
import assets.GMFormattedTextField;
import assets.Res;
import com.toedter.calendar.JDateChooser;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

/**
 *
 * @author Mahfuz
 */
public class SearchSchemain extends JDialog implements ItemListener, ActionListener {
    private final SchemainModel model;
    private JLabel label;
    private JCheckBox customerIDCheck, customerNameCheck, dateCheck, timeCheck, bayCheck, mechanicCheck;
    private GMFormattedTextField customerID, firstName, lastName, beforeTime, afterTime, mechanic;
    private JDateChooser beforeDate, afterDate;
    private JComboBox bay;
    private JPanel customerIDPanel, customerNamePanel, datePanel, timePanel, bayPanel, mechanicPanel;
    private GMButton search, reset, cancel;
    private String searchQuery = ""; 
    
    public SearchSchemain(Frame owner, boolean modal, SchemainModel model) {
        super(owner, modal);
        this.model = model;
        init();
    }
    
    private void init() {
        this.getContentPane().setBackground(Res.BKG_COLOR);
        this.setSize(700, 300);
        this.setLayout(new GridBagLayout());
        
        JLabel title = new JLabel("Search");
        title.setFont(new Font("", 0, 26));
        title.setForeground(Res.FONT_COLOR);
        title.setHorizontalAlignment(JLabel.CENTER);
        
        customerIDCheck = new JCheckBox("Customer ID");
        customerIDCheck.addItemListener(this);
        customerIDCheck.setBackground(Res.BKG_COLOR);
        customerIDCheck.setForeground(Res.FONT_COLOR);
        
        customerNameCheck = new JCheckBox("Customer Name");
        customerNameCheck.addItemListener(this);
        customerNameCheck.setBackground(Res.BKG_COLOR);
        customerNameCheck.setForeground(Res.FONT_COLOR);
        
        dateCheck = new JCheckBox("Date");
        dateCheck.addItemListener(this);
        dateCheck.setBackground(Res.BKG_COLOR);
        dateCheck.setForeground(Res.FONT_COLOR);
        
        timeCheck = new JCheckBox("Time");
        timeCheck.addItemListener(this);
        timeCheck.setBackground(Res.BKG_COLOR);
        timeCheck.setForeground(Res.FONT_COLOR);
        
        bayCheck = new JCheckBox("Bay");
        bayCheck.addItemListener(this);
        bayCheck.setBackground(Res.BKG_COLOR);
        bayCheck.setForeground(Res.FONT_COLOR);
        
        mechanicCheck = new JCheckBox("Mechanic");
        mechanicCheck.addItemListener(this);
        mechanicCheck.setBackground(Res.BKG_COLOR);
        mechanicCheck.setForeground(Res.FONT_COLOR);
        
        search = new GMButton("SEARCH");
        search.addActionListener(this);
        
        reset = new GMButton("RESET");
        reset.addActionListener(this);
        
        cancel = new GMButton("CANCEL");
        cancel.addActionListener(this);
        
        setFields();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5,5,5,5);
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        this.add(title, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        this.add(customerIDCheck, gbc);
        gbc.gridx = 1;
        this.add(customerIDPanel, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        this.add(customerNameCheck, gbc);
        gbc.gridx = 1;
        this.add(customerNamePanel, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        this.add(dateCheck, gbc);
        gbc.gridx = 1;
        this.add(datePanel, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        this.add(timeCheck, gbc);
        gbc.gridx = 1;
        this.add(timePanel, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        this.add(bayCheck, gbc);
        gbc.gridx = 1;
        this.add(bayPanel, gbc);
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        this.add(mechanicCheck, gbc);
        gbc.gridx = 1;
        this.add(mechanicPanel, gbc);
        
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        btnPanel.setBackground(Res.BKG_COLOR);
        btnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        btnPanel.add(search);
        btnPanel.add(reset);
        btnPanel.add(cancel);
        
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth=4;
        this.add(btnPanel, gbc);
        
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }
    
    private void setFields() {
        label = new JLabel("Customer ID: ");
        label.setForeground(Res.FONT_COLOR);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        customerID = new GMFormattedTextField(new DefaultFormatterFactory(new DefaultFormatter()));
        
        customerIDPanel = new JPanel(new GridLayout(1,4,10,2));
        customerIDPanel.setOpaque(false);
        customerIDPanel.setVisible(false);
        customerIDPanel.add(label);
        customerIDPanel.add(customerID);
        customerIDPanel.add(new JLabel());
        customerIDPanel.add(new JLabel());
        
        label = new JLabel("First Names: ");
        label.setForeground(Res.FONT_COLOR);
        firstName = new GMFormattedTextField();
        
        customerNamePanel = new JPanel(new GridLayout(1,4,10,2));
        customerNamePanel.setOpaque(false);
        customerNamePanel.setVisible(false);
        customerNamePanel.add(label);
        customerNamePanel.add(firstName);
        
        label = new JLabel("Last Name: ");
        label.setForeground(Res.FONT_COLOR);
        lastName = new GMFormattedTextField();
        customerNamePanel.add(label);
        customerNamePanel.add(lastName);
        
        datePanel = new JPanel(new GridLayout(1,4,10,2));
        datePanel.setOpaque(false);
        datePanel.setVisible(false);
        
        label = new JLabel("After: ");
        label.setForeground(Res.FONT_COLOR);
        afterDate = new JDateChooser();
        afterDate.setDateFormatString("yyyy-MM-dd");
        datePanel.add(label);
        datePanel.add(afterDate);
        
        label = new JLabel("Before: ");
        label.setForeground(Res.FONT_COLOR);
        beforeDate = new JDateChooser();
        beforeDate.setDateFormatString("yyyy-MM-dd");
        datePanel.add(label);
        datePanel.add(beforeDate);
        
        timePanel = new JPanel(new GridLayout(1,4,10,2));
        timePanel.setOpaque(false);
        timePanel.setVisible(false);
        
        label = new JLabel("After: ");
        label.setForeground(Res.FONT_COLOR);
        afterTime = new GMFormattedTextField(new DefaultFormatterFactory(new DateFormatter(DateFormat.getTimeInstance(DateFormat.SHORT))));
        timePanel.add(label);
        timePanel.add(afterTime);
        
        label = new JLabel("Before: ");
        label.setForeground(Res.FONT_COLOR);
        beforeTime = new GMFormattedTextField(new DefaultFormatterFactory(new DateFormatter(DateFormat.getTimeInstance(DateFormat.SHORT))));
        timePanel.add(label);
        timePanel.add(beforeTime);
        
        label = new JLabel("Bay: ");
        label.setForeground(Res.FONT_COLOR);
        bay = new JComboBox(new DefaultComboBoxModel(new Integer[]{1,2,3,4,5,6}));
        
        bayPanel = new JPanel(new GridLayout(1,4,10,2));
        bayPanel.setOpaque(false);
        bayPanel.setVisible(false);
        bayPanel.add(label);
        bayPanel.add(bay);
        bayPanel.add(new JLabel());
        bayPanel.add(new JLabel());        
        
        label = new JLabel("Mechanic: ");
        label.setForeground(Res.FONT_COLOR);
        mechanic = new GMFormattedTextField();
        mechanicPanel = new JPanel(new GridLayout(1,4,10,2));
        mechanicPanel.setOpaque(false);
        mechanicPanel.setVisible(false);
        mechanicPanel.add(label);
        mechanicPanel.add(mechanic);
        mechanicPanel.add(new JLabel());
        mechanicPanel.add(new JLabel());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object itemClick = e.getItem();
        if(itemClick == customerIDCheck) {
            boolean vis = customerIDPanel.isVisible();
            customerIDPanel.setVisible(!vis);
        }
        if(itemClick == customerNameCheck) {
            boolean vis = customerNamePanel.isVisible();
            customerNamePanel.setVisible(!vis);
        }
        if(itemClick == dateCheck) {
            boolean vis = datePanel.isVisible();
            datePanel.setVisible(!vis);
        }
        if(itemClick == timeCheck) {
            boolean vis = timePanel.isVisible();
            timePanel.setVisible(!vis);
        }
        if(itemClick == bayCheck) {
            boolean vis = bayPanel.isVisible();
            bayPanel.setVisible(!vis);
        }
        if(itemClick == mechanicCheck) {
            boolean vis = mechanicPanel.isVisible();
            mechanicPanel.setVisible(!vis);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==search) {
            if(setQuery()) {
                model.setSearch(searchQuery);
                this.setVisible(false);
            }
        }
        if(e.getSource()==reset) {
            model.setSearch("AND Booking.\"Start Date\" >= Date('now') ");
            customerIDCheck.setSelected(false);
            customerNameCheck.setSelected(false);
            dateCheck.setSelected(false);
            timeCheck.setSelected(false);
            bayCheck.setSelected(false);
            mechanicCheck.setSelected(false);
            this.setVisible(false);
        }
        if(e.getSource()==cancel) {
            this.setVisible(false);
        }
    }
    
    private boolean setQuery() {
        searchQuery = "";
        if(customerIDCheck.isSelected())
            searchQuery += " AND \"Customer Accounts\".\"Customer ID\" = '"+customerID.getText()+"' ";
        if(customerNameCheck.isSelected())
            searchQuery += " AND \"Customer Accounts\".\"First Names\" LIKE '%"+firstName.getText()+"%' AND \"Customer Accounts\".\"Last Name\" LIKE '%"+lastName.getText()+"%' ";
        try {
            if(dateCheck.isSelected())
                searchQuery += " AND (Booking.\"Start Date\" BETWEEN '"+Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(afterDate.getDate()))+"' AND "
                        + "'"+Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(beforeDate.getDate()))+"') ";
        } catch(NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(timeCheck.isSelected())
            searchQuery += " AND (Booking.\"Start Time\" BETWEEN '"+afterTime.getText()+"' AND '"+beforeTime.getText()+"' ) ";
        if(bayCheck.isSelected())
            searchQuery += " AND Booking.Bay = '"+bay.getSelectedItem()+"' ";
        if(mechanicCheck.isSelected())
            searchQuery += " AND Mechanic.\"Mechanic Name\" LIKE '%"+mechanic.getText()+"%' ";
        
        return true;
    }
    
}
