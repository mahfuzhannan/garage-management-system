/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import assets.GMButton;
import assets.GMTable;
import assets.Res;
import static auth.AuthHelpers.hashFunction;
import auth.AuthModel;
import common.Database;
import common.View;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author George/Mahfuz
 */
public class SettingsView extends JPanel {

    UsersModel model;

    GMTable table, mechanicTable;
    GMButton newUser, changePassword, changePrivs, deleteUser, addMechanic, editMechanic, deleteMechanic, testData, resetData;
    JPanel mainPanel, usersPanel, mechanicPanel, tablesPanel, displayPanel, infoPanel;
    ColorPanel colorPanel;
    View view;
    
    public SettingsView(View view) {
        super(new GridLayout());
        //this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setBackground(Res.BKG_COLOR);
        
        this.view = view;
        
        // set main panel
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Res.BKG_COLOR);
        
        initUsers();
        initMechanics();
        
        // panel to display mechanic and users table
        tablesPanel = new JPanel(new GridLayout(2, 1));
        tablesPanel.setBackground(Res.BKG_COLOR);
        tablesPanel.add(usersPanel);
        tablesPanel.add(mechanicPanel);
        
        // panel to display general setting
        displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setBackground(Res.BKG_COLOR);
        displayPanel.setBorder(new TitledBorder(new EtchedBorder(), "General Settings", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        
        // panel to display information regarding the software
        infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        JTextArea text = new JTextArea(10, 40);
        text.setOpaque(false);
        text.setForeground(Res.FONT_COLOR);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setBorder(null);
        String info = "Version: 1.2.45\n"
                + "Garage Management Information System (GMSIS)\n"
                + "GMSIS allows the user to manage a given garage to its daily needs; "
                + "including registering Customers and Vehicles; booking Services, MoTs and Repairs; as well as storing information about ordered Parts\n"
                + "\n"
                + "Developed by a group of Queen Mary 2nd Year students for a Software Engineering Project:\n"
                + "Mahfuz Hannan (Project Leader), George Richardson, Haricharran Sampat, Stefan Ionascu, Vasilis Costa\n\n"
                + "\n"
                + "2015\u00a9";
        text.setText(info);        
        infoPanel.add(text);
        
        colorPanel = new ColorPanel(view, this);
        testData = new GMButton("Reset Database with Test Data");
        resetData = new GMButton("Reset Database");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        displayPanel.add(colorPanel, gbc);
        gbc.gridy = 1;
        displayPanel.add(testData, gbc);
        gbc.gridy = 2;
        displayPanel.add(resetData, gbc);
        gbc.gridy = 3;
        gbc.weighty = 3;
        displayPanel.add(infoPanel, gbc);
       
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(displayPanel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(tablesPanel, gbc);
        
        this.add(mainPanel);

        // create listener
        ActionListener dataListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer choice  = JOptionPane.showConfirmDialog(null, "<html><body>Are you sure you want to Reset Database?<br />"
                        + "This will remove <b>ALL DATA</b> in the system, including:<br />"
                        + "<ul>"
                        + "<li><b>ALL</b> System Users and Mechanics</li>"
                        + "<li><b>ALL</b> Customers Registered</li>"
                        + "<li><b>ALL</b> Vehicles Registered</li>"
                        + "<li><b>ALL</b> Bookings (Past and Futur)</li>"
                        + "<li><b>ALL</b> Parts: Orders, Installed and Stocked</li>"
                        + "<li><b>ALL</b> Suppliers Registered</li>"
                        + "</ul></body></html>");
                if(choice==JOptionPane.NO_OPTION || choice==JOptionPane.CANCEL_OPTION)
                    return; // if no is pressed return to main view
                
                // require password to remove database
                askForPassword(((GMButton) e.getSource()));
            }
        };
        
        // add listeners
        testData.addActionListener(dataListener);
        resetData.addActionListener(dataListener);
        
        // check users priveledges
        AuthModel model = AuthModel.getInstance();
        boolean[] privs = model.getPrivileges();
        testData.setEnabled(privs[5]);
        resetData.setEnabled(privs[5]);
        newUser.setEnabled(privs[5]);
        changePrivs.setEnabled(privs[5]);
        deleteUser.setEnabled(privs[5]);
    }
    
    private void initUsers() {
        usersPanel = new JPanel(new BorderLayout(5, 5));
        usersPanel.setBackground(Res.BKG_COLOR);
        
        usersPanel.setBorder(new TitledBorder(new EtchedBorder(), "System Users", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));

        // Set up table.
        model = UsersModel.getInstance();
        table = new GMTable(model);
        table.updateTable();
        usersPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Set up buttons
        newUser = new GMButton("New");
        newUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewUserDialog();
            }
        });
        changePassword = new GMButton("Change Password");
        changePassword.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (userIsSelected()) {
                    new ChangePasswordDialog((int) model.getValueAt(table.getSelectedRow(), 0));
                }
            }
        });

        changePrivs = new GMButton("Change Privileges");
        changePrivs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                                    if (userIsSelected()) {
                    new ChangePrivsDialog((int) model.getValueAt(table.getSelectedRow(), 0));
                }
            }
        });

        deleteUser = new GMButton("Delete");
        deleteUser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (userIsSelected()) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?") == 0) {
                        model.remove((int) model.getValueAt(table.getSelectedRow(), 0));
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 3, 3));
        buttonPanel.add(newUser);
        buttonPanel.add(changePassword);
        buttonPanel.add(changePrivs);
        buttonPanel.add(deleteUser);
        buttonPanel.setOpaque(false);

        usersPanel.add(buttonPanel, BorderLayout.NORTH);
    }
    
    private void initMechanics() {
        mechanicPanel = new JPanel(new BorderLayout(5, 5));
        mechanicPanel.setBackground(Res.BKG_COLOR);
        mechanicPanel.setBorder(new TitledBorder(new EtchedBorder(), "Mechanics", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));

        // Set up table.
        mechanicTable = new GMTable();
        updateMechanicTable();

        mechanicPanel.add(new JScrollPane(mechanicTable), BorderLayout.CENTER);

        // Set up buttons
        addMechanic = new GMButton("Add Mechanic");
        addMechanic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMechanicDialog dialog = new AddMechanicDialog(false);
                dialog.setVisible(true);
                try {
                    mechanicTable.setModel(Database.getTable(Database.executeQuery("Select * from Mechanic;")));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Could not load Mechanic table!");
                }
                updateMechanicTable();
                revalidate();
            }
        });

        editMechanic = new GMButton("Edit Mechanic");
        editMechanic.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(mechanicTable.isRowSelected(mechanicTable.getSelectedRow())) {
                    AddMechanicDialog dialog = new AddMechanicDialog(true);

                    // get row values
                    String id = mechanicTable.getValueAt(mechanicTable.getSelectedRow(), 0).toString();
                    String name = mechanicTable.getValueAt(mechanicTable.getSelectedRow(), 1).toString();
                    String rate = mechanicTable.getValueAt(mechanicTable.getSelectedRow(), 2).toString();
                    String bay = mechanicTable.getValueAt(mechanicTable.getSelectedRow(), 3).toString();

                    // set row values
                    dialog.setId(Integer.parseInt(id));
                    dialog.setMechanicName(name);
                    dialog.setRate(Integer.parseInt(rate));
                    dialog.setBay(Integer.parseInt(bay));
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row!");
                }
                updateMechanicTable();
                revalidate();
            }
        });

        deleteMechanic = new GMButton("Delete Mechanic");
        deleteMechanic.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(mechanicTable.isRowSelected(mechanicTable.getSelectedRow())) {
                // Check if the user wants to delete the database entry
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this Mechanic?");

                    if(choice==JOptionPane.NO_OPTION || choice==JOptionPane.CANCEL_OPTION){
                        // do nothing
                    } else {
                        String idStr = mechanicTable.getValueAt(mechanicTable.getSelectedRow(), 0).toString();
                        int id = Integer.parseInt(idStr);
                        try {
                            Database.executeUpdate("DELETE FROM Mechanic WHERE \"Mechanic ID\" = '"+id+"';");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Mechanic not removed!");
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row!");
                }
                updateMechanicTable();
                revalidate();
            }
        });


        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 3, 3));
        buttonPanel.add(addMechanic);
        buttonPanel.add(editMechanic);
        buttonPanel.add(deleteMechanic);
        buttonPanel.setOpaque(false);

        mechanicPanel.add(buttonPanel, BorderLayout.NORTH);
    }
    
    private void askForPassword(GMButton button) {
        JDialog dialog = new JDialog();
        dialog.setLayout(null);
        dialog.setSize(270, 150);
        dialog.setModal(true);
        dialog.getContentPane().setBackground(Res.BKG_COLOR);
        dialog.setTitle("Enter Password");

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Res.FONT_COLOR);
        passLabel.setBounds(10, 40, 90, 20);
        dialog.add(passLabel);

        JPasswordField passField = new JPasswordField(30);
        passField.setBounds(104, 40, 150, 20);
        dialog.add(passField);

        GMButton add = new GMButton("CONFIRM");
        add.setBounds(25, 70, 100, 30);
        add.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passField.getPassword());
                try {
                    ResultSet rs = Database.executeQuery("SELECT * FROM users WHERE username = '" + Res.username + "'");

                    // get hashed password
                    String salt = rs.getString("salt");
                    String hash = hashFunction(password + salt);

                    // if password is not the same return
                    if(!hash.equals(rs.getString("password"))) {
                        JOptionPane.showMessageDialog(null, "Password Incorrect");
                        return;
                    }

                    JOptionPane.showMessageDialog(null, "Password Correct");

                    try {
                        Database.dropDb();
                        Database.createDb();
                        if(button==testData)
                            Database.addData();
                        JOptionPane.showMessageDialog(null, "Database Loaded. Default USER created (see manual).");
                    } catch (SQLException ex) {
                        // do nothing
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "An Error Occured!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                dialog.dispose();
            }
        });
        dialog.add(add);

        GMButton cancel = new GMButton("CANCEL");
        cancel.setBounds(135, 70, 100, 30);
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(cancel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    public void paintPanels() {
        repaint();
        mainPanel.setBackground(Res.BKG_COLOR);
        usersPanel.setBackground(Res.BKG_COLOR);
        mechanicPanel.setBackground(Res.BKG_COLOR);
        tablesPanel.setBackground(Res.BKG_COLOR);
        displayPanel.setBackground(Res.BKG_COLOR);
        colorPanel.setBackground(Res.BKG_COLOR);
    }

    private boolean userIsSelected() {
        if (table.getSelectedRow() != -1) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Please select a user");
            return false;
        }
    }
    
    private void updateMechanicTable() {
        try {
            mechanicTable.setModel(Database.getTable(Database.executeQuery("Select * from Mechanic;")));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Could not load Mechanic table!");
        }
    }
    
}
