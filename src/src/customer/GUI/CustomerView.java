package customer.GUI;

//Haricharran Sampat

import assets.GMButton;
import assets.GMTable;
import assets.Res;
import common.Database;
import customer.Logic.CustomerController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;




public class CustomerView extends JPanel {

    
    //record the customer ID which user selects from customer table
    static int cid=0;
    
    //record the booking ID which user selects from booking table
    static int bid;
    
    //record the vehicle ID which user selects from vehicle table
    static int vid;

    
    //Buttons
    GMButton add = new GMButton("Add");
    GMButton find = new GMButton("Find");
    GMButton remove = new GMButton("Remove");
    GMButton update = new GMButton("Update");
    GMButton pay = new GMButton("Pay");

    
    //Panels
    JPanel mainPanel,cDetails,cReminds, remTPanel,remMPanel;
    
    
    //ScrollPanes
    JScrollPane custPane,vehPane,bkPane,remTPane,remMPane;
  
    
    //Tables
    static GMTable vehTable,bookTable,remindTmTable,remindMnTable;
    static JTable custTable; 

    //Controller
    CustomerController controller;
    
  
    
    //Constructor 
    public CustomerView(CustomerController c){

        super(new BorderLayout(10, 10));

        this.setBackground(Res.BKG_COLOR);

        this.controller = c;

        initButtons();

        initListeners();

        initTables();

        initCustPanels();

        initMainPanel();

    }



    public void initButtons(){

        JPanel buttons = new JPanel(new GridLayout(0, 5, 5, 5));

        buttons.setBackground((Res.BKG_COLOR));

        buttons.add(add);
        buttons.add(find);
        buttons.add(remove);
        buttons.add(update);
        buttons.add(pay);

        this.add(buttons, BorderLayout.PAGE_START);

    }

    

    public void initMainPanel(){

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;


        mainPanel.add(cDetails, gbc);

        gbc.weightx = 1;
        gbc.gridx = 3;

        mainPanel.add(cReminds, gbc);

        this.add(mainPanel);

    }

    

    public void initCustPanels(){

        cDetails = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        cDetails.setBorder(new TitledBorder(new EtchedBorder(), "Customer Details", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        cDetails.setBackground(Res.BKG_COLOR);

        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        
        cDetails.add(custPane, gbc);

        gbc.weighty = 1;
        gbc.gridy = 1;
        cDetails.add(vehPane, gbc);

     
        gbc.weighty = 1;
        gbc.gridy = 2;
        cDetails.add(bkPane, gbc);

        cReminds = new JPanel(new GridBagLayout());
        cReminds.setBorder(new TitledBorder(new EtchedBorder(), "Reminders", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
        cReminds.setBackground(Res.BKG_COLOR);

        
        GridBagConstraints bc = new GridBagConstraints();

        bc.fill = GridBagConstraints.BOTH;
        bc.insets = new Insets(5, 5, 5, 5);
        bc.weightx = 1;
        bc.weighty = 1;
        bc.gridx = 0;
        bc.gridy = 0;
        cReminds.add(remTPanel, bc);

        bc.weighty = 1;
        bc.gridx = 0;
        bc.gridy = 1;
        cReminds.add(remMPanel, bc);

    }

    

    public void initTables(){

        try {


            //initialise remind tomorrow table 
            remindTmTable = new GMTable(controller.remindTomorrow());
            remTPane = new JScrollPane(remindTmTable);

            remTPanel = new JPanel(new GridLayout());
            remTPanel.setBorder(new TitledBorder(new EtchedBorder(), "DAR Bookings - Tomorrow", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
            remTPanel.setBackground(Res.BKG_COLOR);
            remTPanel.add(remTPane);

            
            //initialise remind next month table
            remindMnTable = new GMTable(controller.remindMonth());
            remMPane = new JScrollPane(remindMnTable);

            remMPanel = new JPanel(new GridLayout());
            remMPanel.setBorder(new TitledBorder(new EtchedBorder(), "SM Bookings - Next Month ", 1, TitledBorder.CENTER, null, Res.FONT_COLOR));
            remMPanel.setBackground(Res.BKG_COLOR);
            remMPanel.add(remMPane);


           
            custTable = new JTable();
            custTable.setModel(Database.getTable(Database.executeQuery("SELECT * FROM \"Customer Accounts\" ")));
            custPane = new JScrollPane(custTable);

            bookTable = new GMTable();
            bkPane = new JScrollPane(bookTable);

            vehTable = new GMTable();
            vehPane = new JScrollPane(vehTable);


           
            custTable.setSelectionBackground(Color.green);
            custTable.setSelectionForeground(Color.black);
            
            //specify what actions happen when a row in customer table is selected
            custTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {

                    if (!lse.getValueIsAdjusting() && custTable.getSelectedRow() != -1) {
                        try {
                            
                            int i = custTable.getSelectedRow();
                            cid = (Integer) custTable.getValueAt(i, 0);
                           
                            bookTable.setVisible(false);
                            vehTable.setVisible(true);
                            vehTable.setModel(controller.getVehicles(cid));

                        } 
                        catch (Exception ex) {
                            Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }


                    //in case have a customer who is due to be reminded -> appears in reminders panel
                    //if that customer is removed or details changed, the reminder tables will be updated as soon as this happens
                    //so do not get inconsistent data 
                    if (!lse.getValueIsAdjusting()) {
                        remindTmTable.setModel(controller.remindTomorrow());
                        remindMnTable.setModel(controller.remindMonth());
                    }

                }
            });


            
            bookTable.setSelectionBackground(Color.RED);
            bookTable.setSelectionForeground(Color.black);

            //specify what actions happen when a row in booking table is selected
            bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {

                    if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {

                        int i = bookTable.getSelectedRow();
                        bid = (Integer) bookTable.getValueAt(i, 0);
                        
                    }
                }
            });

            
            
            vehTable.setSelectionBackground(Color.CYAN);
            vehTable.setSelectionForeground(Color.black);

            //specify what actions happen when a row in vehicle table is selected
            vehTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {

                    if (!lse.getValueIsAdjusting() && vehTable.getSelectedRow() != -1) {
                        try {
                       
                            int i = vehTable.getSelectedRow();
                            vid = (Integer) vehTable.getValueAt(i, 0);

                            bookTable.setVisible(true);
                            bookTable.setModel(controller.getBookings(vid));
                        } 
                        
                        catch (Exception ex) {
                            Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });

        }
        
        catch (SQLException e) {
            Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, e);
        }

    }


    
    public void initListeners(){

        find.addActionListener(findLstn);
        add.addActionListener(addLstn);
        update.addActionListener(updLstn);
        remove.addActionListener(removeLstn);
        pay.addActionListener(payLstn);

    }

  
    
   
    // Define Listeners

    ActionListener findLstn = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {

            String regex = "[0-9]*";
            String choice = JOptionPane.showInputDialog("enter customer id or surname");

            try {

                if (!choice.isEmpty()) {

                    if (choice.matches(regex)) {

                        custTable.setModel(controller.findbyCID(Integer.parseInt(choice)));
                    } 
                    
                    else {
                        custTable.setModel(controller.findbyLastName(choice));
                    }
                }
                
                else {
                    JOptionPane.showMessageDialog(mainPanel, "please enter a search value");
                }

                resetSelections();
               
            }
            catch (Exception s) {
                //catches exception where user does not enter a string
            }
        }
    };
    
    
    ActionListener addLstn = new ActionListener() {
        
        public void actionPerformed(ActionEvent e) {

            CustomerDialog cd = new CustomerDialog();

            resetSelections();
        }
    };


    ActionListener updLstn = new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {

            if (cid != 0) {

                CustomerDialog ud = new CustomerDialog(cid);
                
                resetSelections();

            }
        }
 
    };


     ActionListener removeLstn = new ActionListener() {
        
         public void actionPerformed(ActionEvent e) {

            try {
                if (cid != 0) {
                    int reply = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to remove this customer ?", "", JOptionPane.YES_NO_OPTION);

                    if (reply == 0) {
                        controller.remove(cid);
                        custTable.setModel(Database.getTable(Database.executeQuery("SELECT * FROM \"Customer Accounts\" ")));

                        resetSelections();

                    }
                }
            } 
            catch (SQLException ex) {
                Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    };


     ActionListener payLstn = new ActionListener(){


        public void actionPerformed(ActionEvent e) {

            
              if (bookTable.getSelectedRow()==-1){
                  JOptionPane.showMessageDialog(mainPanel, "please select a booking ");

              }
              
              else if(! controller.model.pay(bid,vid)){
                  JOptionPane.showMessageDialog(mainPanel, "This vehicle can not be paid for ");
              }
              
              else{
                  JOptionPane.showMessageDialog(mainPanel, "This booking has been paid for ");

              }
              
              try{
                 bookTable.setModel(controller.getBookings(vid));
            
              }
              
              catch(SQLException ex){
                  
              }
             
        }

    };


     
     /*resets values of variables and tables which correspond to 
       table row selections the user has made.
       this makes sure that each button works independently and buttons do not 
       interfere with each other
     */
     public void resetSelections(){
         
         cid=0;
         custTable.clearSelection();
        
         vid=0;
         vehTable.clearSelection();
         
         bid=0;
         bookTable.clearSelection();
         
         vehTable.setVisible(false);
         bookTable.setVisible(false);
        
     }
         
     
}//end class



