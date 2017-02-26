/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schemain;

import assets.GMButton;
import assets.GMComboBox;
import assets.GMTable;
import assets.Res;
import common.View;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Mahfuz
 */
public class SchemainView extends JPanel{
    private final View mainView;
    private GMTable table, motTable, serviceTable;
    private GMButton addServiceBtn, addMotBtn, removeBtn, updateBtn, searchBtn;
    private GMComboBox yearField, monthField, dayField;

    public SchemainView(View mainView) {
        this.mainView = mainView;
        init();
    }
    
    public final void init() {
        // set layout
        this.setLayout(new BorderLayout(10, 10));
        
        // set Buttons
        addServiceBtn = new GMButton("Add Service Booking");
        addMotBtn = new GMButton("Add MoT Booking");
        removeBtn = new GMButton("Remove Booking");
        updateBtn = new GMButton("Update Booking");
        searchBtn = new GMButton("Search Booking");
        
        // button panel
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(0, 3, 5, 5));
        btnPanel.setBackground(Res.BKG_COLOR);
        btnPanel.add(addServiceBtn);
        btnPanel.add(addMotBtn);
        //btnPanel.add(removeBtn);
        //btnPanel.add(updateBtn);
        btnPanel.add(searchBtn);
        
        // create table and scrollpane
        table = new GMTable();
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tableBtnPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        tableBtnPanel.setBackground(Res.BKG_COLOR);
        tableBtnPanel.add(removeBtn);
        tableBtnPanel.add(updateBtn);
        JPanel tablePanel = new JPanel(new BorderLayout(5,5));
        tablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Scheduled Maintenance Bookings", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        tablePanel.setBackground(Res.BKG_COLOR);
        tablePanel.add(tableBtnPanel, BorderLayout.PAGE_START);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        
        // create JTable for todays Services
        serviceTable = new GMTable();
        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        JPanel servicePanel = new JPanel(new GridLayout());
        servicePanel.setBackground(Res.BKG_COLOR);
        servicePanel.setBorder(new TitledBorder(new EtchedBorder(), "Services", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        servicePanel.add(serviceScrollPane);
        
        // create JTable for todays Mots
        motTable = new GMTable();
        JScrollPane motScrollPane = new JScrollPane(motTable);
        JPanel motPanel = new JPanel(new GridLayout());
        motPanel.setBackground(Res.BKG_COLOR);
        motPanel.setBorder(new TitledBorder(new EtchedBorder(), "MoTs", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        motPanel.add(motScrollPane);
        
        // panel for reminders
        JPanel reminderPanel = new JPanel(new GridLayout(2, 0));
        reminderPanel.setBackground(Res.BKG_COLOR);
        reminderPanel.setBorder(new TitledBorder(new EtchedBorder(), "Today's Bookings", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        reminderPanel.add(servicePanel);
        reminderPanel.add(motPanel);
        
        // panel for main containers
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel(gridBagLayout);
        mainPanel.setBackground(Res.BKG_COLOR);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 2;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(tablePanel, gbc);
        gbc.weightx = 1.2;
        gbc.gridx = 1;
        mainPanel.add(reminderPanel, gbc);
        
        
        // main panel for Layout manager
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new GridLayout());
        layoutPanel.setBackground(Res.BKG_COLOR);
        /// add elements
        layoutPanel.add(mainPanel);
        
        
        this.add(btnPanel, BorderLayout.PAGE_START);
        this.add(layoutPanel, BorderLayout.CENTER);
        
        this.setBackground(Res.BKG_COLOR);
        //this.setSize(Res.WIDTH, mainView.PANEL_HEIGHT);
        //this.setPreferredSize(new Dimension(mainView.PANEL_WIDTH, mainView.PANEL_HEIGHT));

        this.setVisible(true);
    }
    
    public View getMainView() {
        return mainView;
    }
    
    public JTable getTable() {
        return table;
    }
    
    public JTable getServiceTable() {
        return serviceTable;
    }

    public GMTable getMotTable() {
        return motTable;
    }
    
    public void updateTable() {
        table.updateTable();
        serviceTable.updateTable();
        motTable.updateTable();
        revalidate();
    }

    public GMComboBox getYearField() {
        return yearField;
    }

    public GMComboBox getMonthField() {
        return monthField;
    }

    public GMComboBox getDayField() {
        return dayField;
    }
    
    public void addAddServiceBtnListener(ActionListener e) {
        addServiceBtn.addActionListener(e);
    }
    
    public void addAddMotBtnListener(ActionListener e) {
        addMotBtn.addActionListener(e);
    }
    
    public void addRemoveBtnListener(ActionListener e) {
        removeBtn.addActionListener(e);
    }
    
    public void addUpdateBtnListener(ActionListener e) {
        updateBtn.addActionListener(e);
    }
    
    public void addSearchBtnListener(ActionListener e) {
        searchBtn.addActionListener(e);
    }
    
    public void addTableListener(MouseListener e) {
        table.addMouseListener(e);
    }
    
    public void addMotTableListener(MouseListener e) {
        motTable.addMouseListener(e);
    }
    
    public void showErrorMessage(String err) {
        JOptionPane.showMessageDialog(this, err, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, null, JOptionPane.INFORMATION_MESSAGE);        
    }
    
    
}
