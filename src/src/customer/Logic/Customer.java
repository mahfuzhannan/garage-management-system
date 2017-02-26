package customer.Logic;

//Haricharran Sampat

import common.Database;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.swing.table.DefaultTableModel;

public class Customer {
        
        /* customer Model
           idea = create a customer object and set its ID 
           call get methods below to get specific data from database
           only need getters
        */

        private int ID;

        //stores all details of given customer
        private DefaultTableModel vals;



        public Customer() {

            getAllVals();
        }



        public Customer(int cid) {

            setID(cid);

            getAllVals();

        }



        public int getID() {
            return ID;
        }



        public void setID(int id) {

            this.ID = id;
        }


        // gets all  details of customer with given customerID
        // only query the database once, as opposed to creating single queries for every customer field
        public void getAllVals() {

            try {
                DefaultTableModel allVals = Database.getTable(Database.executeQuery("SELECT * FROM \"Customer Accounts\" WHERE \"Customer ID\" = \"" + this.ID + "\" "));

                this.vals = allVals;
            } 

            catch (SQLException ex) {
                Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


        
        public String getFirstName() {
          
            return (String) vals.getValueAt(0, 1);
            
        }



        public String getLastName() {

            return (String) vals.getValueAt(0, 2);

        }

        
        
        public String getType() {

            return (String) vals.getValueAt(0, 3);

        }

        

        public String getAddress() {

            return (String) vals.getValueAt(0, 4);

        }



        public String getPhoneNum() {


            return vals.getValueAt(0, 5).toString();
        }



        public String getEmail() {

            return (String) vals.getValueAt(0, 6);

        }



        // allow customer to pay for a given booking 
        public boolean pay(int bookingID, int vehicleID) {

            

            try {
                
                //handle case where booking already paid for 
                //cant pay >1 time for same booking 

                DefaultTableModel paid = Database.getTable(Database.executeQuery("SELECT isPaid from BOOKING  WHERE \"Booking ID\" = \"" + bookingID + "\" "));
                int isPaid = (Integer) paid.getValueAt(0, 0);
                if (isPaid == 1) {
                    return false;
                }

                DefaultTableModel m = Database.getTable(Database.executeQuery("SELECT Expiry date from VEHICLE JOIN WARRANTY on VEHICLE.\"WarrantyWarranty ID\" = WARRANTY.\"Warranty ID\" WHERE VEHICLE.\"Vehicle ID\" = \"" + vehicleID + "\" "));


                //if the vehicle has a warranty 
                if (m.getRowCount() > 0) {

                    String exDate = (String) m.getValueAt(0, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

                    Date expd = sdf.parse(exDate);
                    Date now = new Date();

                    int i = expd.compareTo(now);

                    //if warranty is invalid
                    if (i == -1) {
                        Database.executeUpdate("UPDATE BOOKING  SET isPaid = \"1\" WHERE \"Booking ID\" = \"" + bookingID + "\" ");

                        return true;
                    } 

                    else {
                        return false;
                    }

                    
                }


            } 

            catch (Exception e) {
            }

            return false;


        }

    
    
    
}//end class
