
package diagrep;

import booking.Booking.BookingType;
import common.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 * @author vc300
 */
public class DiagRepModel extends BookingModel{
    
    //get bookings that have end date greater than the current date
    private String tableQuery = "SELECT ("
            + " \"Customer Accounts\".\"First Names\"  || ', ' ||"
            + "\"Customer Accounts\".\"Last Name\") as 'Customer',"
            + "Booking.\"Booking ID\",  "
            + "Vehicle.Reg as 'Reg', "
            + "(Vehicle.Make || ', ' || Vehicle.Model) as 'Vehicle', "
            + "Booking.\"Start Date\" as 'Date', "
            + "strftime('%H:%M', Booking.\"Start Time\") as 'Start Time', "
            + "Booking.\"End Date\","
            + "strftime('%H:%M', Booking.\"End Time\") as 'End Time', "
            + "Booking.Bay, Mechanic.\"Mechanic Name\" as 'Mechanic' "
            + "FROM Booking "
            + "JOIN \"Customer Accounts\" ON \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\"   "
            + "JOIN Vehicle ON Booking.\"VehicleVehicle ID\" = Vehicle.\"Vehicle ID\"  "
            + "JOIN Mechanic ON booking.\"MechanicMechanic ID\" = Mechanic.\"Mechanic ID\"  WHERE "
            + " Booking.\"Type\" = '"+BookingType.DIAGREP+"' and Booking.\"End Date\" >= Date('now') "
            + "ORDER BY Booking.\"Start Date\" ASC, \"Start Time\" ASC;";      
    
    
    
    //get bookings that start today
    private final String todayTableQuery = "SELECT \"Customer Accounts\".\"First Names\" as 'Name', "
            + "Vehicle.Reg, strftime('%H:%M', Booking.\"Start Time\") as 'Start', "
            + "Mechanic.\"Mechanic Name\" as 'Mechanic', Booking.Bay FROM Booking "
            + "JOIN Vehicle ON Vehicle.\"Vehicle ID\" = Booking.\"VehicleVehicle ID\" "
            + "JOIN \"Customer Accounts\" ON \"Customer Accounts\".\"Customer ID\" = Booking.\"Customer AccountsCustomer ID\" "
            + "JOIN Mechanic ON booking.\"MechanicMechanic ID\" = Mechanic.\"Mechanic ID\" "
            + "WHERE Booking.\"Start Date\" = date('now') "
            + "and  Booking.\"Type\" = '"+BookingType.DIAGREP+"' "
            + "ORDER BY Start ASC;";
    
    
    
    
    
    
    public DiagRepModel() {
    }
    
    
     ///////////////////////////////////////////////////////////
    /////////////////////Getters///////////////////////////////
    ///////////////////////////////////////////////////////////
    // fetch tableModel for Diagnostic and Repairs
    public DefaultTableModel getTableModel() throws SQLException {
        return Database.getTable(Database.executeQuery(tableQuery));
    }
    
    //fetch todayTableModel for Today's Diagnostic and Repair Bookings
    public DefaultTableModel getTodayTableModel() throws SQLException {
        return Database.getTable(Database.executeQuery(todayTableQuery));
    }
    
    //fetch partsTableModel for parts in stock
    public DefaultTableModel getPartsTableModel() throws SQLException {
        //partial query to fill the vehicle id to show parts installed
        final String getPartsTableModelQuery = "SELECT \"Part Id\",\"Part Name\",\"Price\""
            + " FROM \"Parts Stock\" "
            + "WHERE \"Parts Stock\".Stock >0";
    
        return Database.getTable(Database.executeQuery(getPartsTableModelQuery));
    }

    
    
    
    
}