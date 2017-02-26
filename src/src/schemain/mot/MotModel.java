/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.mot;

import booking.Booking;
import common.Database;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import schemain.Schemain;
import schemain.SchemainModel;

/**
 *
 * @author Mahfuz
 */
public class MotModel extends SchemainModel {
    
    public void add(Mot mot) throws SQLException {
        //mot.add();
        super.add(mot);
        String addQuery = "INSERT INTO Mot (\"Scheduled MaintenanceScheduled Maintenance ID\") "
                + "VALUES ('"+mot.getSchemainId()+"');";
        Database.executeUpdate(addQuery);
        try (ResultSet rs = Database.executeQuery("SELECT \"Mot ID\" FROM Mot ORDER BY \"Mot ID\" DESC LIMIT 1;")) {
            mot.setMotId(rs.getInt("Mot ID"));
        }
    }
    
    public void update(Mot mot) throws SQLException {
        super.update(mot);
        String pass = "";
        if(mot.getPass()!=null)
            pass = mot.getPass().toString();
        String updateQuery = "UPDATE Mot SET Pass = '"+pass+"', Reason = '"+mot.getReason()+"' "
                + "WHERE \"Scheduled MaintenanceScheduled Maintenance ID\" = '"+mot.getSchemainId()+"';";
        Database.executeUpdate(updateQuery);
    }
    
    public void remove(Mot mot) throws SQLException {
        //mot.remove();
        super.remove(mot);
        String removeQuery = "DELETE FROM Mot WHERE "
                + "\"Scheduled MaintenanceScheduled Maintenance ID\" = "+mot.getSchemainId()+";";
        Database.executeUpdate(removeQuery);
    }
    
    /**
     * Delete all Mots.
     * @param schemainIds
     * @param customerId 
     * @throws java.sql.SQLException 
     */
    public static void removeAllMots(ArrayList<Integer> schemainIds) throws SQLException {
        
        // remove scheduled maintenance bookings
        for (Integer schemainId : schemainIds) {
            String getSchemainIds = "DELETE FROM Mot WHERE \"Scheduled MaintenanceScheduled Maintenance ID\" = " + schemainId + ";";
            Database.executeUpdate(getSchemainIds);
        }    
    }
    
    public void motCompleted(Mot mot) throws SQLException {        
        String updateQuery = "UPDATE Mot SET Pass = '"+mot.getPass()+"', Reason = '"+mot.getReason()+"' "
                + "WHERE Mot.\"Mot ID\" = '"+mot.getMotId()+"';";
        Database.executeUpdate(updateQuery);
    }
    
    /**
     * Gets and Mot object from the database that match the given arguments
     * @param startDate - Start Date of the booking
     * @param startTime - Start Time of the booking
     * @param endDate - End Date of the booking
     * @param endTime - End Time of the booking
     * @param bay - the Bay selected for the booking
     * @param customerId - the Customer ID of the booking
     * @return an Mot object retrieved from the database
     * @throws SQLException
     */
    public Mot getMot(Date startDate, Time startTime, Date endDate, Time endTime, 
            Integer bay, Integer customerId) throws SQLException {
        String getMotQuery = "SELECT Booking.\"Booking ID\", Booking.\"Total Cost\", "
                + "Booking.\"VehicleVehicle ID\", Booking.Mileage, Booking.\"MechanicMechanic ID\", "
                + "\"Scheduled Maintenance\".\"Scheduled Maintenance ID\", Mot.\"Mot ID\", Mot.Pass, Mot.Reason "
                + "FROM Booking JOIN \"Scheduled Maintenance\" ON "
                + "\"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\" "
                + "JOIN Mot ON Mot.\"Scheduled MaintenanceScheduled Maintenance ID\" = "
                + "\"Scheduled Maintenance\".\"Scheduled Maintenance ID\" "
                + "WHERE Booking.\"Start Date\" = '" + startDate + "' AND Booking.\"Start Time\" = '" + startTime + "' "
                + "AND Booking.\"End Date\" = '" + endDate + "' AND Booking.\"End Time\" = '" + endTime + "' "
                + "AND Booking.Bay = '" + bay + "' OR Booking.\"Customer AccountsCustomer ID\" = '" + customerId + "' "
                + ";";
        Mot mot;
        try (ResultSet rs = Database.executeQuery(getMotQuery)) {
            mot = new Mot(rs.getInt("Booking ID"), rs.getInt("Scheduled Maintenance ID"), 
                    rs.getInt("VehicleVehicle ID"), customerId, rs.getInt("MechanicMechanic ID"),
                    startDate, startTime, endDate, endTime, bay, rs.getDouble("Total Cost"),
                    Booking.BookingType.SCHEMAIN, rs.getDouble("Mileage"), false,
                    Schemain.SchemainType.MOT, rs.getInt("Mot ID"), rs.getBoolean("Pass"), rs.getString("Reason"));
        }
        return mot;
    }
    
    public Mot getMot(Integer bookingId) throws SQLException {
        String getMotQuery = "SELECT Mot.\"Mot ID\" FROM Booking JOIN Mot ON "
                + "Mot.\"Scheduled MaintenanceScheduled Maintenance ID\" = (SELECT \"Scheduled Maintenance\".\"Scheduled Maintenance ID\""
                + "FROM \"Scheduled Maintenance\" WHERE \"Scheduled Maintenance\".\"BookingBooking ID\" = "+bookingId+");";
        Mot mot;
        try (ResultSet rs = Database.executeQuery(getMotQuery)) {
            mot = new Mot(bookingId, null, null,
                    null, null, null, null, null, null, null, null,
                    null, null, null, null, rs.getInt("Mot ID"), null, null);
        }
        return mot;
    }
}
