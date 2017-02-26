package customer.Logic;

//Haricharran Sampat

import common.Database;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;
import vehicle.VehicleModel;

public class CustomerController {

    
        public Customer model;

 

        public CustomerController(Customer m) {

            model = m;
            
        }


        //find customer details by customerID
        public DefaultTableModel findbyCID(int cid) throws SQLException {

            return Database.getTable(Database.executeQuery("Select * from \"Customer Accounts\" WHERE  \"Customer ID\" = \"" + cid + "\" "));

        }


        //find customer details by last name
        public DefaultTableModel findbyLastName(String lname) throws SQLException {

            return Database.getTable(Database.executeQuery("Select * from \"Customer Accounts\" WHERE  \"Last Name\" = \"" + lname + "\" "));

        }



        //add customer to database
        public void add(String fn, String ln, String type, String addr, String phnum, String email) throws SQLException {
            
            
            Database.executeUpdate("INSERT INTO \"Customer Accounts\" VALUES(NULL,\"" + fn + "\",\"" + ln + "\",\"" + type + "\",\"" + addr + "\",\"" + phnum + "\", \"" + email + "\")");

        }



        //remove a customer and all things associated with it eg {vehicles,parts,bookings} from the database
        public void remove(int cid) throws SQLException {

            Database.executeUpdate("DELETE FROM \"Customer Accounts\" WHERE \"Customer ID\" = \"" + cid + "\" ");
            
            // call external method from Vechicle package to remove all things associated with customer
             VehicleModel model = VehicleModel.getInstance(true);
             model.removeFor(cid);
             

        }



        // return a list of bookings associated with given vehicle
        public DefaultTableModel getBookings(int vid) throws SQLException {

            DefaultTableModel bookings = Database.getTable(Database.executeQuery("SELECT * FROM BOOKING WHERE \"VehicleVehicle ID\" = \"" + vid + "\" "));

            bookings.setColumnCount(11);

            return bookings;

        }



        // return a list of vehicles associated with given customer
        public DefaultTableModel getVehicles(int cid) throws SQLException {


                DefaultTableModel vehicles = Database.getTable(Database.executeQuery("SELECT * FROM VEHICLE WHERE \"Customer AccountsCustomer ID\" = \"" + cid + "\" "));

                vehicles.setColumnCount(9);

                return vehicles;


        }



        //update details for given customer, in the database
        public void update(String fn, String ln, String type, String addr, String phnum, String email) throws SQLException  {

            int cid = this.model.getID();


            Database.executeUpdate("UPDATE \"Customer Accounts\" SET \"First Names\" = \"" + fn + "\",\"Last Name\" = \"" + ln + "\",\"Customer Type\" = \"" + type + "\" ,\"Address \" = \"" + addr + "\",Tel = \"" + phnum + "\" , Email = \"" + email + "\"  WHERE \"Customer ID\" = \"" + cid + "\" ");

        }



        
        //return a table of customers who need to be phoned by the garage admin, 1 day before their diagnosis and repair booking is due
        public DefaultTableModel remindTomorrow() {

            try {

                //ordered by time -> bookings appear in ascending order of start time 
                //ie earliest booking first 

                return Database.getTable(Database.executeQuery("SELECT Booking.\"Start Time\", Booking.\"Booking ID\",\"Customer Accounts\".Tel,"
                        + "\"Customer Accounts\".\"Customer ID\", \"Customer Accounts\".\"First Names\", \"Customer Accounts\".\"Last Name\"  "
                        + " FROM \"Customer Accounts\" JOIN Booking on \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\"  "
                        + " WHERE   date('now','+1 day') = date(Booking.\"Start Date\")  AND Booking.Type = \"DIAGREP\"  ORDER BY Booking.\"Start Time\" ASC"));

            }

            catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // if an error occurs an empty table is returned
            return new DefaultTableModel();
        }


        

        /*return a table of customers with vehicles that are due to make a Scheduled Maintenance booking  for 1 months time.
          customers are selected if today's date is 11 months from the date of their last Scheduled Maintence Booking.
          ie customers are only reminded if they have not made a Scheduled Maintenance booking as of today
          and their last Scheduled maintence booking was 11 months ago today
        */ 
        
        public DefaultTableModel remindMonth() {


            try {
                
                
                DefaultTableModel currBookings;
                DefaultTableModel pastBookings =

                 //select details of all SM bookings which ended 11 months ago       
                 Database.getTable(Database.executeQuery("SELECT \"Customer Accounts\".\"Customer ID\", \"Customer Accounts\".\"First Names\", \"Customer Accounts\".\"Last Name\",  "
                        + " \"Customer Accounts\".Tel   "
                        + " FROM \"Customer Accounts\" JOIN BOOKING on \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\"  "
                        + " WHERE   date(Booking.\"End date\", '+11 months') = date('now') AND Booking.Type = \"SCHEMAIN\" "));

                //selected customerID
                String tempCID;

                //go through each customer and check if they have not already made a SM booking for the future
                for(int i=0 ;i<pastBookings.getRowCount();i++){
                    
                    tempCID =  pastBookings.getValueAt(i, 0).toString();
                    
                    currBookings = Database.getTable(Database.executeQuery("SELECT \"Customer Accounts\".\"First Names\" "
                     
                        + " FROM \"Customer Accounts\" JOIN BOOKING on \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\"  "
                        + " WHERE  \"Customer Accounts\".\"Customer ID\" = \""+tempCID+"\" AND date(Booking.\"Start Date\") >= date('now') AND Booking.Type = \"SCHEMAIN\" "));
               
                    
                    //if they have, then exclude them from the reminder table
                    if(currBookings.getRowCount()>0){
                       
                        pastBookings.removeRow(i);
                    }
                }
                
               return pastBookings;
            } 

            catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return new DefaultTableModel();

        }


}//end class
