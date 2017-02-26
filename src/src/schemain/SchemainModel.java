/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schemain;

import booking.BookingModel;
import common.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import schemain.mot.MotModel;
import schemain.service.ServiceModel;

/**
 *
 * @author Mahfuz
 */
public class SchemainModel extends BookingModel {
    
    /**
     * Used as the search constraint.
     */
    private String search = "AND Booking.\"Start Date\" >= Date('now') ";
    /**
     * Used as the query to display Scheduled Maintenance Table; also incudes search.
     */
    private String getTableModelQuery = "SELECT (Booking.\"Customer AccountsCustomer ID\" || ': ' "
            + "|| \"Customer Accounts\".\"First Names\" || ' ' "
            + "|| \"Customer Accounts\".\"Last Name\") as 'Customer', "
            + "Booking.\"Booking ID\", \"Scheduled Maintenance\".Type as 'Type', "
            + "Vehicle.Reg as 'Reg', (Vehicle.Make || ', ' || Vehicle.Model) as 'Vehicle', "
            + "Booking.\"Start Date\" as 'Date', strftime('%H:%M', Booking.\"Start Time\") as 'Start Time', "
            + "strftime('%H:%M', Booking.\"End Time\") as 'End Time', Booking.Bay, Mechanic.\"Mechanic Name\" as 'Mechanic' "
            + "FROM Booking "
            + "JOIN \"Customer Accounts\" ON Booking.\"Customer AccountsCustomer ID\" = \"Customer Accounts\".\"Customer ID\" "
            + "JOIN Vehicle ON Booking.\"VehicleVehicle ID\" = Vehicle.\"Vehicle ID\" "
            + "JOIN Mechanic ON Booking.\"MechanicMechanic ID\" = Mechanic.\"Mechanic ID\" "
            + "JOIN \"Scheduled Maintenance\" ON \"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\" "
            //+ "WHERE Booking.\"Booking ID\" = \"Scheduled Maintenance\".\"BookingBooking ID\" "
            + search
            + " ORDER BY Booking.\"Start Date\" ASC, \"Start Time\" ASC, Booking.Bay ASC;";            
    /**
     * Used as the query to display today's services.
     */
    private final String getServiceTableModelQuery = "SELECT \"Customer Accounts\".\"First Names\" as 'Name', "
            + "Vehicle.Reg, strftime('%H:%M', Booking.\"Start Time\") as 'Start', strftime('%H:%M', Booking.\"End Time\") as 'End', "
            + "Mechanic.\"Mechanic Name\" as 'Mechanic', Booking.Bay FROM Booking "
            + "JOIN Vehicle ON Vehicle.\"Vehicle ID\" = Booking.\"VehicleVehicle ID\" "
            + "JOIN \"Customer Accounts\" ON \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\" "
            + "JOIN Mechanic ON Booking.\"MechanicMechanic ID\" = Mechanic.\"Mechanic ID\" "
            + "JOIN \"Scheduled Maintenance\" ON \"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\" "
            + "WHERE Booking.\"Start Date\" = date('now') "
            + "AND \"Scheduled Maintenance\".Type = 'SERVICE' "
            + "ORDER BY Start ASC;";
    /**
     * Used as the query to display today's MoTs.
     */
    private final String getMotTableModelQuery = "SELECT \"Customer Accounts\".\"First Names\" as 'Name', "
            + "Vehicle.Reg, strftime('%H:%M', Booking.\"Start Time\") as 'Start', strftime('%H:%M', Booking.\"End Time\") as 'End', "
            + "Mot.Pass, Mechanic.\"Mechanic Name\" as 'Mechanic', Booking.Bay FROM Booking "
            + "JOIN Vehicle ON Vehicle.\"Vehicle ID\" = Booking.\"VehicleVehicle ID\" "
            + "JOIN \"Customer Accounts\" ON \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\" "
            + "JOIN Mechanic ON Booking.\"MechanicMechanic ID\" = Mechanic.\"Mechanic ID\" "
            + "JOIN Mot ON Mot.\"Scheduled MaintenanceScheduled Maintenance ID\" = "
            + "(SELECT \"Scheduled Maintenance\".\"Scheduled Maintenance ID\" FROM \"Scheduled Maintenance\" "
            + "WHERE \"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\") "
            + "WHERE Booking.\"Start Date\" = date('now');";
    
    
    public SchemainModel() {
        
    }
    
    /**
     * Adds a Schemain object to database.
     * @param schemain
     * @throws SQLException 
     */
    public void add(Schemain schemain) throws SQLException {
        super.add(schemain);
        String addQuery = "INSERT INTO \"Scheduled Maintenance\" (Type, "
                + "\"BookingBooking ID\") VALUES ('"+schemain.getSchemainType()+"', '"
                +schemain.getBookingID()+"');";
        Database.executeUpdate(addQuery);
        try (ResultSet rs = Database.executeQuery("SELECT \"Scheduled Maintenance ID\" FROM \"Scheduled Maintenance\" ORDER BY \"Scheduled Maintenance ID\" DESC LIMIT 1;")) {
            schemain.setSchemainId(rs.getInt("Scheduled Maintenance ID"));
        }
    }
    
    /**
     * Updates a Schemain object to database.
     * @param schemain
     * @throws SQLException 
     */
    public void update(Schemain schemain) throws SQLException {
        super.update(schemain);
        String updateQuery = "UPDATE \"Scheduled Maintenance\" "
                + "SET Type = '"+schemain.getSchemainType()+"' "
                + "WHERE \"Scheduled Maintenance ID\" = '"+schemain.getSchemainId()+"';";
        Database.executeUpdate(updateQuery);
        
    }
    
    /**
     * Removes a Schemain object to database.
     * @param schemain
     * @throws SQLException 
     */
    public void remove(Schemain schemain) throws SQLException {
        super.remove(schemain);
        String removeQuery = "DELETE FROM \"Scheduled Maintenance\" WHERE "
                + "\"Scheduled Maintenance ID\" = " + schemain.getSchemainId() +";";
        Database.executeUpdate(removeQuery);
        
    }
    
    /**
     * Removes all Scheduled Maintenance Bookings; Bookings; Services and Mots.
     * @param customerId - The Customer ID for Bookings to remove.
     * @return
     * @throws SQLException 
     */
    @Override
    public ArrayList<Integer> removeAll(Integer customerId) throws SQLException {
        ArrayList<Integer> bookingIds = super.removeAll(customerId);
        
        ArrayList<Integer> schemainIds = new ArrayList<>();        
        
        // get schemain ids
        for (Integer bookingId : bookingIds) {
            String getSchemainIds = "SELECT \"Scheduled Maintenance ID\" FROM \"Scheduled Maintenance\" WHERE "
                    + "\"BookingBooking ID\" = " + bookingId + ";";
            ResultSet rs = Database.executeQuery(getSchemainIds);
            if(rs.next())
                schemainIds.add(rs.getInt("Scheduled Maintenance ID"));   
        }
        
        // remove all services and mots
        ServiceModel.removeAllServices(schemainIds);
        MotModel.removeAllMots(schemainIds);
        
        // remove scheduled maintenance bookings
        for (Integer bookingId : bookingIds) {
            String getSchemainIds = "DELETE FROM \"Scheduled Maintenance\" WHERE \"BookingBooking ID\" = " + bookingId + ";";
            Database.executeUpdate(getSchemainIds);
        }
        
        return schemainIds;
    }
    
    /**
     * Removes all Scheduled Maintenance Bookings; Bookings; Services and Mots.
     * @param vehicleId
     * @return
     * @throws SQLException 
     */
    @Override
    public ArrayList<Integer> removeAllVehicle(Integer vehicleId) throws SQLException {
        ArrayList<Integer> bookingIds = super.removeAllVehicle(vehicleId);
        
        ArrayList<Integer> schemainIds = new ArrayList<>();        
        
        // get schemain ids
        for (Integer bookingId : bookingIds) {
            String getSchemainIds = "SELECT \"Scheduled Maintenance ID\" FROM \"Scheduled Maintenance\" WHERE "
                    + "\"BookingBooking ID\" = " + bookingId + ";";
            ResultSet rs = Database.executeQuery(getSchemainIds);
            if(rs.next())
                schemainIds.add(rs.getInt("Scheduled Maintenance ID"));   
        }
        
        // remove all services and mots
        ServiceModel.removeAllServices(schemainIds);
        MotModel.removeAllMots(schemainIds);
        
        // remove scheduled maintenance bookings
        for (Integer bookingId : bookingIds) {
            String getSchemainIds = "DELETE FROM \"Scheduled Maintenance\" WHERE \"BookingBooking ID\" = " + bookingId + ";";
            Database.executeUpdate(getSchemainIds);
        }
        
        return schemainIds;
    }

    public void setSearch(String search) {
        this.search = search;
        getTableModelQuery = "SELECT (Booking.\"Customer AccountsCustomer ID\" || ': ' "
            + "|| \"Customer Accounts\".\"First Names\" || ' ' "
            + "|| \"Customer Accounts\".\"Last Name\") as 'Customer', "
            + "Booking.\"Booking ID\", \"Scheduled Maintenance\".Type as 'Type', "
            + "Vehicle.Reg as 'Reg', (Vehicle.Make || ', ' || Vehicle.Model) as 'Vehicle', "
            + "Booking.\"Start Date\" as 'Date', strftime('%H:%M', Booking.\"Start Time\") as 'Start Time', "
            + "strftime('%H:%M', Booking.\"End Time\") as 'End Time', Booking.Bay, Mechanic.\"Mechanic Name\" as 'Mechanic' "
            + "FROM Booking, \"Scheduled Maintenance\" "
            + "JOIN \"Customer Accounts\" ON Booking.\"Customer AccountsCustomer ID\" = \"Customer Accounts\".\"Customer ID\" "
            + "JOIN Vehicle ON Booking.\"VehicleVehicle ID\" = Vehicle.\"Vehicle ID\" "
            + "JOIN Mechanic ON Booking.Bay = Mechanic.Bay "
            + "WHERE Booking.\"Booking ID\" = \"Scheduled Maintenance\".\"BookingBooking ID\" "
            + search
            + "ORDER BY Booking.\"Start Date\" ASC, \"Start Time\" ASC, Booking.Bay ASC;";   
    }
    
    /**
     * Returns the Scheduled Maintenance ID for a given Booking ID
     * @param bookingID
     * @return - Integer Scheduled Maintenance ID if exists; else returns null
     * @throws SQLException 
     */
    public Integer getSchemainID(Integer bookingID) throws SQLException {
        String query = "SELECT \"Scheduled Maintenance\".\"Scheduled Maintenance ID\" FROM \"Scheduled Maintenance\" "
                + "WHERE \"Scheduled Maintenance\".\"BookingBooking ID\" = '"+bookingID+"';";
        ResultSet rs = Database.executeQuery(query);
        return rs.getInt("Scheduled Maintenance ID");
    }
    
    /**
     * Get tableModel for Services and Mots
     * @return - DefaultTableModel
     * @throws SQLException 
     */
    public DefaultTableModel getTableModel() throws SQLException {
        return Database.getTable(Database.executeQuery(getTableModelQuery));
    }
    
    /**
     * Get tableModel for today's Services
     * @return - DefaultTableModel
     * @throws SQLException 
     */
    public DefaultTableModel getServiceTableModel() throws SQLException {
        return Database.getTable(Database.executeQuery(getServiceTableModelQuery));
    }
    
    /**
     * Get tableModel for today's Mots
     * @return - DefaultTableModel
     * @throws SQLException 
     */
    public DefaultTableModel getMotTableModel() throws SQLException {
        return Database.getTable(Database.executeQuery(getMotTableModelQuery));
    }
    
    /**
     * Get the row of a table and it's values as an ArrayList
     * @param table - JTable for which the row values are required
     * @return - an ArrayList with the values of the row of a given table
     */
    public ArrayList<Object> getTableRowAsArrayList(JTable table) {
        // get number of columns in tables
        int columns = table.getModel().getColumnCount();
        // get SELECTed row number
        int row = table.getSelectedRow();
        // arraylist to store row values
        ArrayList<Object> list = new ArrayList<>();
        // set values in row
        for(int i=0; i<columns; i++) {
            list.add(table.getValueAt(row, i));
        }
        
        return list;
    }
    
}
