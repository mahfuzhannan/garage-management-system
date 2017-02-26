/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking;

import common.Database;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Handles any database queries required for Bookings.
 * @author mafz
 */
public class BookingModel {
    private final String[] weekendTimes = new String[5], weekdayTimes = new String[16];
    private ArrayList<String> holidays;
    
    public BookingModel() {
        init();
    }
    
    /**
     * Adds a Booking object to a Database
     * @param booking
     * @throws SQLException 
     */
    public void add(Booking booking) throws SQLException {
        String addQuery = "INSERT INTO Booking (\"Start Date\", \"Start Time\","
            + "\"End Date\", \"End Time\", Bay, \"Total Cost\", Type, Mileage, isPaid, "
            + "\"MechanicMechanic ID\", \"VehicleVehicle ID\", \"Customer AccountsCustomer ID\") VALUES "
            + "('"+booking.getStartDate()+"','"+booking.getStartTime()+"','"
            +booking.getEndDate()+"','"+booking.getEndTime()+"','"+booking.getBay()+"','"
            +booking.getTotalCost()+"','"+booking.getType()+"','"+booking.getMileage()+"','"+booking.getIsPaid()+"','"
            +booking.getMechanicID()+"','"+booking.getVehicleID()+"','"+booking.getCustomerID()+"');";
        Database.executeUpdate(addQuery);
        try (ResultSet rs = Database.executeQuery("SELECT \"Booking ID\" FROM Booking ORDER BY \"Booking ID\" DESC LIMIT 1;")) {
            booking.setBookingID(rs.getInt("Booking ID"));
        }
    }
    
    /**
     * Updates a Booking object to a Database
     * @param booking
     * @throws SQLException 
     */
    public void update(Booking booking) throws SQLException {
        String updateQuery = "UPDATE Booking SET \"Start Date\" = '"+booking.getStartDate()+"', "
                + "\"Start Time\" = '"+booking.getStartTime()+"', \"End Date\" = '"+booking.getEndDate()+"', "
                + "\"End Time\" = '"+booking.getEndTime()+"', Bay = '"+booking.getBay()+"', "
                + "Mileage = '"+booking.getMileage()+"', \"VehicleVehicle ID\" = '"+booking.getVehicleID()+"' "
                + "WHERE \"Booking ID\" = '"+booking.getBookingID()+"';";
        Database.executeUpdate(updateQuery);
    }
    
    /**
     * Removes a Booking object to a Database
     * @param booking
     * @throws SQLException 
     */
    public void remove(Booking booking) throws SQLException {
        String removeQuery = "DELETE FROM Booking WHERE \"Booking ID\" = "+booking.getBookingID()+";";
        Database.executeUpdate(removeQuery);
    }
    
    /**
     * Remove all bookings with Customer ID.
     * @param customerId - Customer ID to remove bookings.
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> removeAll(Integer customerId) throws SQLException {
        String getBookingIds = "SELECT \"Booking ID\" FROM Booking WHERE \"Customer AccountsCustomer ID\" = "+customerId+";";
        ResultSet rs = Database.executeQuery(getBookingIds);
        
        ArrayList<Integer> bookingIds = new ArrayList<>();
        while(rs.next()) {
            bookingIds.add(rs.getInt("Booking ID"));
        }
        
        String removeQuery = "DELETE FROM Booking WHERE \"Customer AccountsCustomer ID\" = "+customerId+";";
        Database.executeUpdate(removeQuery);
        
        return bookingIds;
    }
    
    /**
     * Remove all bookings with Vehicle ID.
     * @param vehicleId - Vehicle ID to remove bookings.
     * @return
     * @throws SQLException 
     */
    public ArrayList<Integer> removeAllVehicle(Integer vehicleId) throws SQLException {
        String getBookingIds = "SELECT \"Booking ID\" FROM Booking WHERE \"VehicleVehicle ID\" = "+vehicleId+";";
        ResultSet rs = Database.executeQuery(getBookingIds);
        
        ArrayList<Integer> bookingIds = new ArrayList<>();
        while(rs.next()) {
            bookingIds.add(rs.getInt("Booking ID"));
        }
        
        String removeQuery = "DELETE FROM Booking WHERE \"VehicleVehicle ID\" = "+vehicleId+";";
        Database.executeUpdate(removeQuery);
        
        return bookingIds;
    }
    
    private void init() {
        // set times garage is open
        setTimes();
    }
    
    /**
     * Set the times that the garage will be open for; stored in two private
     * variables String[] weekendTimes and String[] weekdayTimes.
     */
    private void setTimes() {
        // i=n*2 where n is the hour of the time
        // this allows to check for odd numbers so: 30min = 1;
        // 9-4:30 (7.5 hours), (9*2) + (7.5*2) = 18 + 15 = 33;
            // assumption made that final hour bookings cannot be made
        for(int in=0, i=18; i<=33; i++, in++) {
            // set time hour
            int time;
            // if even number its an hour
            if(i%2==0)
                time = i/2;
            // if odd its a half hour
            else
                time = (i-1)/2;
            // weekend open 9-11 (2 hours) thus (9*2) + (2*2) = 18 + 4 = 22
            // assumption made that final hour bookings cannot be made
            if(i<=22) {
                if(i % 2 ==0)
                    weekendTimes[in] = ""+time+":00";
                else weekendTimes[in] = ""+time+":30";
            }
            if(i % 2 ==0)
                weekdayTimes[in] = ""+time+":00";
            else weekdayTimes[in] = ""+time+":30";
        }
    }
    
    /**
     * Sets the holidays for the garage;
     * stackoverflow.com/questions/13716314/java-get-json-object-array
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void setHolidays() {
        holidays = Database.getHolidays();
    }
    
    /**
     * Calculates the logic to check bays available for a given date and time,
     * the remaining free bays are returned.
     * @param startDate - Start Date of current Booking being made
     * @param startTime - Start Time of current Booking being made
     * @param endDate - End Date of current Booking being made
     * @param endTime - End Time of current Booking being made
     * @return an array representing the available bays; empty array if
     * no bays are free
     * @throws SQLException 
     */
    public Integer[] getAvailableBays(Date startDate, Time startTime, Date endDate, Time endTime) throws SQLException {
        String getAvailableBaysQuery = "SELECT bay FROM Booking WHERE "
                // booking is before the start date
                +"((\"Start Date\" < '"+startDate+"' AND "
                    // and ends after the end date 
                    + "(\"End Date\" > '"+endDate+"' "
                        // or end on the end date 
                        + "OR (\"End Date\" = '"+endDate+"' "
                            // and ends between start time and end time
                            + "AND ((\"End Time\" > '"+startTime+"' AND \"End Time\" <= '"+endTime+"') "
                            // or ends after end time
                            + "OR (\"End Time\" > '"+endTime+"'))))) "
                // booking is on the start date
                + "OR (\"Start Date\" = '"+startDate+"' AND "
                    // and ends after te end date
                    + "(\"End Date\" > '"+endDate+"' "
                        // or ends on the end date
                        + "OR (\"End Date\" = '"+endDate+"' "
                            // and ends between end time and start time
                            + "AND ((\"End Time\" > '"+startTime+"' AND \"End Time\" <= '"+endTime+"') "
                            // or starts before start time and ends after end time
                            + "OR (\"Start Time\" < '"+endTime+"' AND \"End Time\" >= '"+endTime+"') "
                            // or ends after end time
                            + "OR (\"End Time\" > '"+endTime+"' AND \"Start Time\" <= '"+startTime+"')))))) ;";

        ResultSet rs = Database.executeQuery(getAvailableBaysQuery);
        ArrayList<Integer> bays = new ArrayList<>();
        // insert values 1-6 into arraylist representing the bays
        for(int i=1; i<=6; i++)
            bays.add(i);
        
        // remove bays
        while(rs.next()) { 
            Integer val = rs.getInt(1);
            if(bays.contains(val))
                bays.remove(val);
        }
        
        // insert available bay values into an array
        Integer[] baysLeft = new Integer[bays.size()];
        for(int i=0 ;i<bays.size(); i++)
            baysLeft[i] = bays.get(i);
        
        return baysLeft;
    }
    
    /**
     * Gets the available Mechanic for a specific bay.
     * @param bay - the bay booked
     * @return - String consisting of Mechanic ID and Mechanic Name
     * @throws SQLException 
     */
    public String[] getMechanic(Integer bay) throws SQLException {
        String query = "SELECT Mechanic.\"Mechanic Name\", Mechanic.\"Mechanic ID\" "
                + "FROM Mechanic WHERE Mechanic.\"Bay\" = '"+bay+"';";
        ResultSet rs = Database.executeQuery(query);
        String str = null;
        ArrayList<String> list = new ArrayList<>();
        while(rs.next()) { 
            str = rs.getInt("Mechanic ID") + ": " + rs.getString("Mechanic Name");
            list.add(str);
        }
        
        String[] mechanics = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            mechanics[i] = list.get(i);
        }
        //return str;
        return mechanics;
    }
    
    /**
     * Checks weather a given Customer ID is in the database or not
     * @param customerId - Customer ID
     * @return true id Customer ID exists; false otherwise
     * @throws SQLException 
     */
    public boolean isCustomer(Integer customerId) throws SQLException {
        String query = "SELECT \"Customer ID\" FROM \"Customer Accounts\" WHERE \"Customer ID\" = " + customerId + ";";
        ResultSet rs = Database.executeQuery(query);
        if(rs.next()) { 
            rs.close();
            return true;
        }
        return false;
    }
    
    /**
     * Returns a String[] consisting of all Customers registered in the database.
     * @return - String[] customers, includes Customer ID and Name, in each index
     * @throws SQLException 
     */
    public String[] getCustomers() throws SQLException {
        String query = "SELECT \"Customer Accounts\".\"Customer ID\", "
                + "(\"Customer Accounts\".\"First Names\" || ' ' "
            + "|| \"Customer Accounts\".\"Last Name\") as 'Customer Name' FROM "
                + "\"Customer Accounts\";";
        ResultSet rs = Database.executeQuery(query);
        ArrayList<String> list = new ArrayList<>();
        while(rs.next()) 
            list.add(rs.getInt("Customer ID")+": "+rs.getString("Customer Name"));
        
        String[] customers = new String[list.size()];
        for(int i=0; i<list.size(); i++)
            customers[i] = list.get(i);
        
        return customers;
    }
    
    /**
     * Returns a String consisting of Customers registered in the database that has given id.
     * @param customerId - ID of the customer required
     * @return - String customer, includes Customer ID and Name
     * @throws SQLException 
     */
    public String getCustomerIDAndName(Integer customerId) throws SQLException {
        String query = "SELECT "
            + "(\"Customer Accounts\".\"Customer ID\" || ': ' || \"Customer Accounts\".\"First Names\" || ' ' "
            + "|| \"Customer Accounts\".\"Last Name\") as 'Customer Name' FROM \"Customer Accounts\" "
            + "WHERE \"Customer Accounts\".\"Customer ID\" = '"+customerId+"';";
        ResultSet rs = Database.executeQuery(query);
        
        return rs.getString("Customer Name");
    }
    
    /**
     * Returns all the vehicles a given Customer owns
     * @param customerId - Customer ID
     * @return returns an array containing the Vehicle ID of all vehicles;
     * if no vehicles owned {0} is returned
     * @throws SQLException 
     */
    public String[] getVehicles(Integer customerId) throws SQLException {
        // SQL query to get Vehicle ID's
        String query = "SELECT Vehicle.\"Vehicle ID\", Vehicle.Make, Vehicle.Model, Vehicle.Reg "
                + "FROM Vehicle WHERE \"Customer AccountsCustomer ID\" = " + customerId + ";";
        ResultSet rs = Database.executeQuery(query);
        ArrayList<String> list = new ArrayList<>();
        
        // add retrieved values from database into arraylist
        while(rs.next()) { 
            String val = rs.getInt("Vehicle ID")+": "+rs.getString("Reg");
            list.add(val);
        }
        // insert values into an array
        String[] vehicles = new String[list.size()];
        for(int i=0; i<list.size(); i++)
            vehicles[i] = list.get(i);
        return vehicles;
    }
    
    /**
     * Checks whether given date is a UK holiday or not.
     * @param date -  the date to check
     * @return - true if holiday; else false.
     */
    public boolean isHoliday(Date date) {
        return holidays.contains(date.toString());
    }
    
    /**
     * Get the times the Garage is opened on weekends
     * @return  an array of weekend opening times
     */
    public String[] getWeekendTimes() {
        return weekendTimes;
    }
    
    /**
     * Get the times the Garage is opened on weekdays
     * @return  an array of weekday opening times
     */
    public String[] getWeekdayTimes() {
        return weekdayTimes;
    }
    
}
