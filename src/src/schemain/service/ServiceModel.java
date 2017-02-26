/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schemain.service;

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
public class ServiceModel extends SchemainModel {
    
    public ServiceModel() {
        super();
    }
    
    public void add(Service service) throws SQLException {
        //service.add();
        super.add(service);
        String addQuery = "INSERT INTO Service (Type, \"Next Service\","
                + "\"Scheduled MaintenanceScheduled Maintenance ID\") "
                + "VALUES ('"+service.getServiceType()+"','"+service.getNextService()+"','"
                + service.getSchemainId()+"');";
        Database.executeUpdate(addQuery);
        try (ResultSet rs = Database.executeQuery("SELECT \"Service ID\" FROM Service ORDER BY \"Service ID\" DESC LIMIT 1;")) {
            service.setServiceId(rs.getInt("Service ID"));
        }
        
    }

    public void update(Service service) throws SQLException {
        super.update(service);
        String updateQuery = "UPDATE Service SET Type = '"+service.getServiceType()+"', "
                + "\"Next Service\" = '"+service.getNextService()+"' WHERE "
                + "\"Scheduled MaintenanceScheduled Maintenance ID\" = '"+service.getSchemainId()+"';";
        Database.executeUpdate(updateQuery);
    }

    public void remove(Service service) throws SQLException {
        //service.remove();
        super.remove(service);
        String removeQuery = "DELETE FROM Service WHERE "
                + "\"Scheduled MaintenanceScheduled Maintenance ID\" = "+service.getSchemainId()+";";
        Database.executeUpdate(removeQuery);
    }
    
    /**
     * Delete all Services; deletes data from Scheduled Maintenance and Booking table also.
     * @param schemainIds
     * @param customerId 
     * @throws java.sql.SQLException 
     */
    public static void removeAllServices(ArrayList<Integer> schemainIds) throws SQLException {
        
        // remove scheduled maintenance bookings
        for (Integer schemainId : schemainIds) {
            String getSchemainIds = "DELETE FROM Service WHERE \"Scheduled MaintenanceScheduled Maintenance ID\" = " + schemainId + ";";
            Database.executeUpdate(getSchemainIds);
        }    
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
    public Service getService(Date startDate, Time startTime, Date endDate, Time endTime, 
            Integer bay, Integer customerId) throws SQLException {
        String getServiceQuery = "SELECT Booking.\"Booking ID\", Booking.\"Total Cost\", "
                + "Booking.\"VehicleVehicle ID\", Booking.Mileage, Booking.\"MechanicMechanic ID\", "
                + "\"Scheduled Maintenance\".\"Scheduled Maintenance ID\", Service.\"Service ID\" "
                + "FROM Booking JOIN \"Scheduled Maintenance\" ON "
                + "\"Scheduled Maintenance\".\"BookingBooking ID\" = Booking.\"Booking ID\" "
                + "JOIN Service ON Service.\"Scheduled MaintenanceScheduled Maintenance ID\" = "
                + "\"Scheduled Maintenance\".\"Scheduled Maintenance ID\" "
                + "WHERE Booking.\"Start Date\" = '" + startDate + "' AND Booking.\"Start Time\" = '" + startTime + "' "
                + "AND Booking.\"End Date\" = '" + endDate + "' AND Booking.\"End Time\" = '" + endTime + "' "
                + "AND Booking.Bay = '" + bay + "' AND Booking.\"Customer AccountsCustomer ID\" = '" + customerId + "' "
                + ";";
        Service service;
        try (ResultSet rs = Database.executeQuery(getServiceQuery)) {
            service = new Service(rs.getInt("Booking ID"), rs.getInt("Scheduled Maintenance ID"), 
                    rs.getInt("VehicleVehicle ID"), customerId, rs.getInt("MechanicMechanic ID"),
                    startDate, startTime, endDate, endTime, bay, rs.getDouble("Total Cost"),
                    Booking.BookingType.SCHEMAIN, rs.getDouble("Mileage"), false, 
                    Schemain.SchemainType.MOT, rs.getInt("Service ID"), null, null);
        }
        return service;
    }
    
    /**
     * Gets an array of values relating to Next Service type.
     * @param serviceType
     * @return - String[] in miles or in years.
     */
    public String[] getNextServices(Service.ServiceType serviceType) {
        if(serviceType == Service.ServiceType.MILEAGE)
            return new String[]{"5000 miles","10000 miles","15000 miles","20000 miles","25000 miles","30000 miles"};
        return new String[]{"1 year", "2 year"};
    }
}
